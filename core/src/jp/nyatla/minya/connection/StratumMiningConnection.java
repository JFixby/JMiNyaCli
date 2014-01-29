/********************************************************************************
 * MiNya pjeject
 * Copyright 2014 nyatla.jp
 * https://github.com/nyatla/JMiNya
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 ********************************************************************************/

package jp.nyatla.minya.connection;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;
import jp.nyatla.minya.MinyaLog;
import jp.nyatla.minya.StratumMiningWork;
import jp.nyatla.minya.stratum.*;


public class StratumMiningConnection implements IMiningConnection
{
	private class SubmitOrder
	{
		public SubmitOrder(long i_id, StratumMiningWork i_work, int i_nonce)
		{
			this.id=i_id;
			this.work=i_work;
			this.nonce=i_nonce;
			return;
		}
		public final long id;
		public final MiningWork work;
		public final int nonce;
	}
	/**
	 * 非同期受信スレッド
	 *
	 */
	private class AsyncRxSocketThread extends Thread
	{
		private ArrayList<SubmitOrder> _submit_q=new ArrayList<SubmitOrder>();
		private ArrayList<StratumJson> _json_q=new ArrayList<StratumJson>();
		private StratumMiningConnection _parent;
		public AsyncRxSocketThread(StratumMiningConnection i_parent) throws SocketException
		{
			this._parent=i_parent;
			this._parent._sock.setSoTimeout(100);
		}
		public void run()
		{
			for(;;){
				try {
					StratumJson json=this._parent._sock.recvStratumJson();
					if(json==null){
						Thread.sleep(1);
						continue;
					}
					this.onJsonRx(json);
				} catch (SocketTimeoutException e){
					if(this.isInterrupted()){
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e){
					//割り込みでスレッド終了
					break;
				}
			}
		}
		
		/**
		 * JSONを解析してキューに入れる。
		 * @param i_json
		 */
		private void onJsonRx(StratumJson i_json)
		{
			Class<?> iid=i_json.getClass();
			MinyaLog.debug("onJsonRx:"+iid.getName());
			
			if(iid==StratumJsonMethodGetVersion.class)
			{
			}else if(iid==StratumJsonMethodMiningNotify.class)
			{
				this._parent.cbNewMiningNotify((StratumJsonMethodMiningNotify)i_json);
			}else if(iid==StratumJsonMethodReconnect.class){
			}else if(iid==StratumJsonMethodSetDifficulty.class)
			{
				this._parent.cbNewMiningDifficulty((StratumJsonMethodSetDifficulty) i_json);
			}else if(iid==StratumJsonMethodShowMessage.class){
			}else if(iid==StratumJsonResultStandard.class)
			{
				//submit_qを探索してsubmitIDと一致したらjson_qに入れないでコール
				{
					StratumJsonResultStandard sjson=(StratumJsonResultStandard)i_json;
					SubmitOrder so=null;
					synchronized(this._submit_q){
						for(SubmitOrder i: this._submit_q){
							if(i.id==sjson.id){
								//submit_qから取り外し
								this._submit_q.remove(i);
								so=i;
								break;
							}
						}
					}
					if(so!=null){
						this._parent.cbSubmitRecv(so,sjson);
					}
				}				
				synchronized(this._json_q)
				{
					this._json_q.add(i_json);
				}
				this.semaphore.release();
			}else if(iid==StratumJsonResultSubscribe.class)
			{
				synchronized(this._json_q)
				{
					this._json_q.add(i_json);
				}
				this.semaphore.release();
			}
			return;
		}
		private Semaphore semaphore = new Semaphore(0);
		/**
		 * 指定したidのJsonResultを取得する。
		 * @param i_id
		 * @param i_wait_for_msec
		 * @return
		 * @throws InterruptedException
		 */
		public StratumJson waitForJsonResult(long i_id,Class<?> i_class,int i_wait_for_msec)
		{
			long time_out=i_wait_for_msec;
			do{
				long s=System.currentTimeMillis();
				try {
					if(!semaphore.tryAcquire(time_out,TimeUnit.MILLISECONDS)){
						return null;
					}
				} catch (InterruptedException e) {
					return null;
				}
				synchronized(this._json_q)
				{
					//受信キューをスキャン
		            for(StratumJson json : this._json_q){
		            	if(!(json.getClass()==i_class)){
		            		continue;
		            	}
		            	StratumJsonResult jr=(StratumJsonResult)json;
		            	if(jr.id==null){
		            		continue;
		            	}
		            	if(jr.id!=i_id){
		            		continue;
		            	}
		            	this._json_q.remove(json);
		            	return json;
		            }
				}
				time_out-=(System.currentTimeMillis()-s);
			}while(time_out>0);
            return null;
		}
		/**
		 * SubmitオーダーQにIDを追加する。
		 * @param i_submit_id
		 */
		public void addSubmitOrder(SubmitOrder i_submit_id)
		{
			synchronized(this._submit_q){
				this._submit_q.add(i_submit_id);
			}
		}
	}	

	private final String CLIENT_NAME="MiNya/beta";
	private String _uid;
	private String _pass;
	private URI _server;
	private StratumSocket _sock=null;
	private AsyncRxSocketThread _rx_thread;

	public StratumMiningConnection(String i_url, String i_userid, String i_password) throws MinyaException
	{
		this._pass=i_password;
		this._uid=i_userid;
		try {
			this._server=new URI(i_url);
		} catch (URISyntaxException e) {
			throw new MinyaException(e);
		}
	}
	private StratumJsonMethodSetDifficulty _last_difficulty=null;
	private StratumJsonMethodMiningNotify _last_notify=null;
	
	/**
	 * Serverに接続します。
	 * @throws MinyaException
	 */
	public MiningWork connect() throws MinyaException
	{
		//Connect to host
		try {
			MiningWork ret=null;
			this._sock=new StratumSocket(this._server);

			this._rx_thread=new AsyncRxSocketThread(this);
			this._rx_thread.start();
			//3回トライ
			int i;

			//subscribe
			StratumJsonResultSubscribe subscribe=null;
			{
				for(i=0;i<3;i++){
					MinyaLog.message("Request Stratum subscribe...");
					subscribe=(StratumJsonResultSubscribe)this._rx_thread.waitForJsonResult(this._sock.subscribe(CLIENT_NAME),StratumJsonResultSubscribe.class,3000);
					if(subscribe==null || subscribe.error!=null){
						MinyaLog.warning("Stratum subscribe error.");
						continue;
					}
					break;
				}
				if(i==3){
					throw new MinyaException("Stratum subscribe error.");
				}
			}
			
			//Authorize and make  a 1st work.
			for(i=0;i<3;i++){
				MinyaLog.message("Request Stratum authrise...");
				StratumJsonResultStandard auth=(StratumJsonResultStandard) this._rx_thread.waitForJsonResult(this._sock.authorize(this._uid,this._pass), StratumJsonResultStandard.class,3000);
				if(auth==null || auth.error!=null){
					MinyaLog.warning("Stratum authrise error.");
					continue;
				}
				if(!auth.result){
					MinyaLog.warning("Stratum authrise result error.");
				}
				synchronized(this._data_lock){
					//worker builderの構築
					this._work_builder=new StratumWorkBuilder(subscribe);
					//事前に受信したメッセージがあれば設定
					if(this._last_difficulty!=null){
						this._work_builder.setDiff(this._last_difficulty);
					}
					if(this._last_notify!=null){
						this._work_builder.setNotify(this._last_notify);
					}
					ret=this._work_builder.buildMiningWork();
				}
				//Complete!
				MinyaLog.message("Stratum authrise complete!");
				return ret;
			}
			throw new MinyaException("Stratum authrise process failed.");
		} catch (UnknownHostException e){
			throw new MinyaException(e);
		} catch (IOException e) {
			throw new MinyaException(e);
		}
	}
	
	public void disconnect() throws MinyaException
	{
		try {
			//threadの停止
			this._rx_thread.interrupt();
			this._rx_thread.join();
			//Socketの停止
			this._sock.close();
			synchronized(this._data_lock)
			{
				this._work_builder=null;
			}
		} catch (IOException e) {
			throw new MinyaException(e);
		} catch (InterruptedException e) {
			throw new MinyaException(e);
		}
	}
	private Object _data_lock=new Object();
	private StratumWorkBuilder _work_builder=null;

	/**
	 * 現在のMiningWorkを生成して返す。
	 * @return
	 */
	public MiningWork getWork()
	{
		MiningWork work=null;
		synchronized(this._data_lock)
		{
			if(this._work_builder==null){
				return null;
			}
			try {
				work=this._work_builder.buildMiningWork();
			} catch (MinyaException e){
				return null;
			}
		}
		return work;		
	}
	private ArrayList<IConnectionEvent> _as_listener=new ArrayList<IConnectionEvent>();
	
	/**
	 * この関数は非同期コールスレッドと衝突するのでconnect前に実行する事。
	 */
	public void addListener(IConnectionEvent i_listener)
	{
		this._as_listener.add(i_listener);
		return;
	}
	
	/**
	 * Threadからのコールバック(Thread)
	 * @throws MinyaException 
	 */
	private void cbNewMiningNotify(StratumJsonMethodMiningNotify i_notify)
	{
		synchronized(this._data_lock)
		{
			if(this._work_builder==null){
				this._last_notify=i_notify;
				return;
			}
		}
		//notifyを更新
		try {
			MinyaLog.message("Receive new job:"+i_notify.job_id);
			this._work_builder.setNotify(i_notify);
		} catch (MinyaException e){
			MinyaLog.debug("Catch Exception:\n"+e.getMessage());
		}
		MiningWork w=this.getWork();
		if(w==null){
			return;
		}
		//登録されているlistenerをコール
		for(IConnectionEvent i: this._as_listener){
			i.onNewWork(w);
		}
	}
	private void cbNewMiningDifficulty(StratumJsonMethodSetDifficulty i_difficulty)
	{
		MinyaLog.message("Receive set difficulty:"+i_difficulty.difficulty);
		synchronized(this._data_lock)
		{
			if(this._work_builder==null){
				this._last_difficulty=i_difficulty;
				return;
			}
		}
		//notifyを更新
		try {
			this._work_builder.setDiff(i_difficulty);
		} catch (MinyaException e) {
			MinyaLog.debug("Catch Exception:\n"+e.getMessage());
		}
		MiningWork w=this.getWork();
		if(w==null){
			return;
		}
		//登録されているlistenerをコール
		for(IConnectionEvent i: this._as_listener){
			i.onNewWork(w);
		}
	}

	private void cbSubmitRecv(SubmitOrder so, StratumJsonResultStandard i_result)
	{
		MinyaLog.message("SubmitResponse "+so.nonce+" ["+(i_result.result?"Accepted":"Rejected")+"]");
		//登録されているlistenerをコール
		for(IConnectionEvent i: this._as_listener){
			i.onSubmitResult(so.work,so.nonce,i_result.error==null);
		}
	}	
	public void submitWork(MiningWork i_work, int i_nonce) throws MinyaException
	{
		if(!(i_work instanceof StratumMiningWork)){
			throw new MinyaException();
		}
		StratumMiningWork w=(StratumMiningWork)i_work;
		String ntime=w.data.getStr(StratumMiningWork.INDEX_OF_NTIME,4);
		//Stratum送信
		try {
			long id=this._sock.submit(i_nonce,this._uid,w.job_id, w.xnonce2, ntime);
			SubmitOrder so=new SubmitOrder(id,w,i_nonce);
			this._rx_thread.addSubmitOrder(so);
			MinyaLog.message("Found! "+so.nonce+" Request submit("+w.job_id+")");
		} catch (IOException e) {
			throw new MinyaException(e);
		}
	}

}
