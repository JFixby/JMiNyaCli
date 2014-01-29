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

package jp.nyatla.minya;

import jp.nyatla.minya.connection.IConnectionEvent;
import jp.nyatla.minya.connection.IMiningConnection;
import jp.nyatla.minya.worker.IMiningWorker;
import jp.nyatla.minya.worker.IWorkerEvent;

/**
 * 1つの採掘場を採掘する現場監督クラス
 *
 */
public class SingleMiningChief
{		
	private IMiningConnection _connection;
	private IMiningWorker _worker;
	private EventListener _eventlistener;
	public class EventListener implements IConnectionEvent,IWorkerEvent
	{
		private SingleMiningChief _parent;
		private int _number_of_accept;
		private int _number_of_all;
		EventListener(SingleMiningChief i_parent)
		{
			this._parent=i_parent;
			this.resetCounter();
		}
		public void resetCounter()
		{
			this._number_of_accept=this._number_of_all=0;
		}
		@Override
		public void onNewWork(MiningWork i_work)
		{
			try {
				MinyaLog.message("New work detected!");
				//新規ワークの開始
				synchronized(this){
					this._parent._worker.doWork(i_work);
				}
			} catch (MinyaException e){
				e.printStackTrace();
			}
		}
		@Override
		public void onSubmitResult(MiningWork i_listener, int i_nonce,boolean i_result)
		{
			this._number_of_accept+=(i_result?1:0);	
			this._number_of_all++;
			MinyaLog.message("SubmitStatus:"+(i_result?"Accepted":"Reject")+"("+this._number_of_accept+"/"+this._number_of_all+")");
		}
		public boolean onDisconnect()
		{
			//再接続するならtrue
			return false;
		}
		@Override
		public void onNonceFound(MiningWork i_work, int i_nonce)
		{
			try {
				this._parent._connection.submitWork(i_work,i_nonce);
			} catch (MinyaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public SingleMiningChief(IMiningConnection i_connection,IMiningWorker i_worker) throws MinyaException
	{
		this._connection=i_connection;
		this._worker=i_worker;
		this._eventlistener=new EventListener(this);
		this._connection.addListener(this._eventlistener);
		this._worker.addListener(this._eventlistener);
	}
	public void startMining() throws MinyaException
	{
		//コネクションを接続
		MiningWork first_work=this._connection.connect();
		//情報リセット
		this._eventlistener.resetCounter();
		//初期ワークがあるならワーク開始
		if(first_work!=null){
			synchronized(this){
				this._worker.doWork(first_work);
			}
		}
	}
	public void stopMining() throws MinyaException
	{
		//コネクションを切断
		this._connection.disconnect();
		//ワーカーを停止
		this._worker.stopWork();
	}
}