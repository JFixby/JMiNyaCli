import java.util.ArrayList;


public class PoolList
{
	public static class Item{
		public String currency_name;
		public String pool_name;
		public String url;
		public Item(String c,String p,String u)
		{
			this.currency_name=c;
			this.pool_name=p;
			this.url=u;
		}
	}
	final static private String[][] preset={
		{
			"(manual)",
			null,
			null
		},
		{
			"MONA",
			"2chpool",
			"stratum+tcp://mona.2chpool.com:5555"
		},
		{
			"MONA",
			"monapool",
			"stratum+tcp://mona1.monapool.com:6666"
		},
		{
			"MONA",
			"CryptoPoolMining.com",
			"stratum+tcp://server.domain.com:4444",
		},
		{
			"NYAN",
			"NyanCoin pool - moonishere.com",
			"stratum+tcp://moonishere.com:3333"
		},
		{
			"NYAN",
			"NyanCoin pool - HappyMiners.net",
			"stratum+tcp://s1.happyminers.net:1337"
		},
		{
			"LTC",
			"WEMINELTC.COM(CPU)",
			"stratum+tcp://freedom.wemineltc.com:3339"
		},
		{
			"BEER",
			"beer.coinmining.pw",
			"stratum+tcp://stratum.coinmining.pw:3339"
		}
	};
	public static ArrayList<Item> getPoolList()
	{
		ArrayList<Item> list=new ArrayList<Item>();
		for(int i=0;i<preset.length;i++){
			list.add(new Item(preset[i][0],preset[i][1],preset[i][2]));
		}
		return list;
	}

}
