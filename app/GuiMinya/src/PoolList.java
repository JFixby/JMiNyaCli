import java.util.ArrayList;


public class PoolList
{
	public class Item{
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
			"LTC",
			"WEMINELTC.COM(CPU)",
			"stratum+tcp://freedom.wemineltc.com:3339"
		},
		{
			"BEER",
			"beer.coinmining.pw",
			"stratum+tcp://stratum.coinmining.pw:3339"
		},
		{
			"NYAN",
			"NyanCoin pool - HappyMiners.net",
			"stratum+tcp://s1.happyminers.net:1337"
		}
	};
	ArrayList<Item> _list=new ArrayList<Item>();
	public PoolList()
	{
		for(int i=0;i<preset.length;i++){
			this._list.add(new Item(preset[i][0],preset[i][1],preset[i][2]));
		}
	}

}
