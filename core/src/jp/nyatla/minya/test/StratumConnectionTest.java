package jp.nyatla.minya.test;


import jp.nyatla.minya.connection.StratumMiningConnection;

public class StratumConnectionTest
{
	private static final String DEFAULT_URL = "stratum+tcp://xxxx";
	private static final String DEFAULT_USER = "user";
	private static final String DEFAULT_PASS = "pass";
	public static void main(String[] args)
	{		
		try {
			StratumMiningConnection smc=new StratumMiningConnection(DEFAULT_URL,DEFAULT_USER,DEFAULT_PASS);
			smc.connect();
			for(;;){
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
