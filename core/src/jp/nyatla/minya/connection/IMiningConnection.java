package jp.nyatla.minya.connection;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;

public interface IMiningConnection
{
	/**
	 * コネクションから非同期イベントを受け取るオブジェクトを追加する。
	 * {@link #connect()}実行前に設定する事。
	 * @param i_listener
	 * @throws MinyaException
	 */
	public void addListener(IConnectionEvent i_listener) throws MinyaException;
	public MiningWork connect() throws MinyaException;
	public void disconnect() throws MinyaException;
	public MiningWork getWork() throws MinyaException;
	public void submitWork(MiningWork i_work, int i_nonce) throws MinyaException;
}
