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

package jp.nyatla.minya.worker;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;
import jp.nyatla.minya.MinyaLog;
import jp.nyatla.minya.hasher.Hasher;


public class CpuMiningWorker implements IMiningWorker
{
	private int _number_of_thread;
	private ExecutorService _exec;
	private Worker[] _workr_thread;
	private class EventList extends ArrayList<IWorkerEvent>
	{
		private static final long serialVersionUID = -4176908211058342478L;
		void invokeNonceFound(MiningWork i_work,int i_nonce)
		{
			MinyaLog.message("Nonce found! +"+((0xffffffffffffffffL)&i_nonce));
			for(IWorkerEvent i: this){
				i.onNonceFound(i_work,i_nonce);
			}			
		}
	}
	private class Worker implements Runnable
	{
		CpuMiningWorker _parent;
		
		MiningWork _work;
		int _start;
		int _step;
		public long number_of_hashed;
		public Worker(CpuMiningWorker i_parent)
		{
			this._parent=i_parent;
		}
		/**
		 * nonce計算のパラメータを指定する。
		 * スレッドはi_startを起点にi_startづつnonceを増加させて計算する。
		 * @param i_work
		 * @param i_start
		 * @param i_step
		 */
		public void setWork(MiningWork i_work,int i_start,int i_step)
		{
			this._work=i_work;
			this._start=i_start;
			this._step=i_step;
		}
		private final static int NUMBER_OF_ROUND=100;
		@Override
		public void run()
		{
			//ここにハッシュ処理を書く
			this.number_of_hashed=0;
			try{
				//初期nonceの決定
				int nonce=this._start;
				MiningWork work=this._work;
				Hasher hasher = new Hasher();
				//めんどくさいので計算は途中で止めない
				byte[] target=work.target.refHex();
				while(true){
					for(long i=NUMBER_OF_ROUND-1;i>=0;i--){
						byte[] hash = hasher.hash(work.header.refHex(), nonce);
						//nonceのチェック
						for (int i2 = hash.length - 1; i2 >= 0; i2--) {
							if ((hash[i2] & 0xff) > (target[i2] & 0xff)){
								break;
							}
							if ((hash[i2] & 0xff) < (target[i2] & 0xff)){
								//発見!
								this._parent._as_listener.invokeNonceFound(work,nonce);
								break;
							}
						}
						nonce+=this._step;
					}
					this.number_of_hashed+=NUMBER_OF_ROUND;
					Thread.sleep(0);
				}
			} catch (GeneralSecurityException e){
				e.printStackTrace();
			} catch (InterruptedException e) {
				//Shutdownのハンドリング
				MinyaLog.debug("Thread killed. Hashes="+this.number_of_hashed);
			}
		}
	}
	public CpuMiningWorker()
	{
		this(Runtime.getRuntime().availableProcessors());
	}

	public CpuMiningWorker(int i_number_of_thread)
	{
		this._number_of_thread=i_number_of_thread;
		this._workr_thread=new Worker[10];
		//Threadの生成
		for(int i=this._number_of_thread-1;i>=0;i--){
			this._workr_thread[i]=new Worker(this);
		}
	}
	private long _last_time=0;
	@Override
	public boolean doWork(MiningWork i_work) throws MinyaException
	{
		MinyaLog.debug("Start doWork");
		if(this._exec!=null){
			//実行中なら一度すべてのワークを停止
			this.stopWork();
			long hashes=0;
			for(int i=this._number_of_thread-1;i>=0;i--){
				hashes+=this._workr_thread[i].number_of_hashed;
			}
			//ハッシュレートの計算
			double t=(double)hashes/((System.currentTimeMillis()-this._last_time)/1000);
			MinyaLog.message("Caluculated "+ (t)+ "Hash/s");
			
		}
		//ハッシュレート計算の為に時刻リセット
		this._last_time=System.currentTimeMillis();
		//Executerの生成
		this._exec= Executors.newFixedThreadPool(this._number_of_thread);
		for(int i=this._number_of_thread-1;i>=0;i--){
			//ワーカに初期値を設定
			this._workr_thread[i].setWork(i_work,(int)i,this._number_of_thread);
			//ワーカの実行
			this._exec.execute(this._workr_thread[i]);
		}
		return true;
	}
	@Override
	public void stopWork() throws MinyaException
	{
		this._exec.shutdownNow();
		//キャンセルの一斉送信
		try {
			//停止待ち
			this._exec.awaitTermination(600,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new MinyaException(e);
		}
		this._exec=null;
	}

	@Override
	public int getProgress()
	{
		return 0;
	}

	@Override
	public int getNumberOfHash()
	{
		return 0;
	}
	

	private EventList _as_listener=new EventList();
	
	/**
	 * この関数は非同期コールスレッドと衝突するので{@link #doWork(MiningWork)}前に実行する事。
	 */
	public void addListener(IWorkerEvent i_listener)
	{
		this._as_listener.add(i_listener);
		return;
	}
}
