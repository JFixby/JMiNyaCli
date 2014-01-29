package jp.nyatla.minya.test;


import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.connection.StratumMiningConnection;
import jp.nyatla.minya.connection.TestStratumMiningConnection;

public class SubmitFormatTest
{
	private static final String DEFAULT_URL = "stratum+tcp://xxxx";
	private static final String DEFAULT_USER = "user";
	private static final String DEFAULT_PASS = "pass";
	public static void main(String[] args)
	{		
		try {
			TestStratumMiningConnection smc=new TestStratumMiningConnection(1);
			smc.connect();
			MiningWork mw=smc.getWork();
			smc.submitWork(mw,0x12345678);
			for(;;){
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
