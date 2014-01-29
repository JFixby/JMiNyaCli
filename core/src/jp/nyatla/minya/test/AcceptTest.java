package jp.nyatla.minya.test;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.connection.TestStratumMiningConnection;


/**
 * ハッシュ関数のAcceptテスト
 * @author nyatla
 *
 */
public class AcceptTest
{
	public static void main(String[] args)
	{		
		try {
			TestStratumMiningConnection twf=new TestStratumMiningConnection(0);
			MiningWork mw=twf.getWork();
			mw.dump();
			long start = System.currentTimeMillis();
			System.out.println(System.currentTimeMillis()-start);
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
