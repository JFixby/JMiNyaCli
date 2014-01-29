package jp.nyatla.minya;

import jp.nyatla.minya.connection.IConnectionEvent;
import jp.nyatla.minya.connection.IMiningConnection;
import jp.nyatla.minya.connection.StratumMiningConnection;
import jp.nyatla.minya.connection.TestStratumMiningConnection;
import jp.nyatla.minya.worker.CpuMiningWorker;
import jp.nyatla.minya.worker.IMiningWorker;
import jp.nyatla.minya.worker.IWorkerEvent;

public class EntryPoint
{
	private static final String DEFAULT_URL = "stratum+tcp://stratum-eu.nyan.luckyminers.com:3320";
	private static final String DEFAULT_USER = "user";
	private static final String DEFAULT_PASS = "pass";
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args)
	{		
		try {
//			IMiningConnection mc=new TestStratumMiningConnection(0);
			IMiningConnection mc=new StratumMiningConnection(DEFAULT_URL,DEFAULT_USER,DEFAULT_PASS);
			IMiningWorker imw=new CpuMiningWorker(4);
			SingleMiningChief smc=new SingleMiningChief(mc,imw);
			smc.startMining();
			for(;;){
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
