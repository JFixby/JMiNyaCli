package jp.nyatla.minya.worker;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;

public interface IMiningWorker
{
	/**
	 * ワークを開始します。既にワークを実行中の場合は一度すべてのワークをシャットダウンして再起動します。
	 * 統計情報はリセットされます。
	 * @throws MinyaException 
	 */
	public boolean doWork(MiningWork i_work) throws MinyaException;
	/**
	 * 実行中のワークを停止します。
	 * @throws MinyaException 
	 */
	public void stopWork() throws MinyaException;
	/**
	 * 進行度を返します。
	 * @return
	 */
	public int getProgress();
	/**
	 * {@link #doWork}が計算したハッシュの数を返します。
	 * @return
	 */
	public int getNumberOfHash();
	
	public void addListener(IWorkerEvent i_listener) throws MinyaException;
}
