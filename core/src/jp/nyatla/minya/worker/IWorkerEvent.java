package jp.nyatla.minya.worker;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;

public interface IWorkerEvent
{
	public void onNonceFound(MiningWork i_work,int i_nonce);
}
