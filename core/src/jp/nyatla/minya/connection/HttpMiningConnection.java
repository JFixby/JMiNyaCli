package jp.nyatla.minya.connection;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.nyatla.minya.Base64;
import jp.nyatla.minya.HexArray;
import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;



public class HttpMiningConnection implements IMiningConnection
{
	private static final int DEFAULT_TIMEOUT = 10000;// in msec
	private URL _rpc_url;
	private String _auth=null;
	
	public HttpMiningConnection(String i_url,String i_userid,String i_password) throws MinyaException
	{
		try {
		    this._auth=i_userid+":"+i_password;
				this._rpc_url=new URL(i_url);
			throw new MinyaException(
				"Authorization failed."+
				"{url:"+i_url+","+
				"user:"+i_userid+","+
				"pass:"+i_password+")");
		} catch (MalformedURLException e){
			throw new MinyaException(e);
		}
	}	
	private static final Pattern resultPattern = Pattern.compile("\"result\"\\s*:\\s*([0-9A-Za-z]+)");
	

	
	public MiningWork getWork() throws MinyaException
	{
		/*
		content={"result":{"midstate":"14bad41483b9e1147a71cab45b59bdf9c970fdedbf34cb9ece9842d260fd22ea","data":"000000027805b2d199da1acc77a78e97f1c25279e0dfe3cc32d798ab5d1cb95c6c1c5039934ad388d565a8a61aa779aa98c666ccf1860f9f9c23b08b6e9d6b7c16b4726f52a88b191d00e3ef00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000","hash1":"00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000","target":"0000000000000000000000000000000000000000000000000000efe300000000"},"error":null,"id":0}

		data=000000027805b2d199da1acc77a78e97f1c25279e0dfe3cc32d798ab5d1cb95c6c1c5039934ad388d565a8a61aa779aa98c666ccf1860f9f9c23b08b6e9d6b7c16b4726f52a88b191d00e3ef00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000
		target=0000000000000000000000000000000000000000000000000000efe300000000
		header=02000000d1b20578cc1ada99978ea7777952c2f1cce3dfe0ab98d7325cb91c5d39501c6c88d34a93a6a865d5aa79a71acc66c6989f0f86f18bb0239c7c6b9d6e6f72b416198ba852efe3001d00000000
		HASH=3259cc9b62ad3de471c8e4e2eb6a82878ae33d1385b7dff70135fc2560c5b517
		 */
	   	final String request = "{\"method\": \"getwork\", \"params\": [], \"id\":0}";
		try{
			String content = this.getJsonRpcResult(request);
			//content="{\"result\":{\"midstate\":\"14bad41483b9e1147a71cab45b59bdf9c970fdedbf34cb9ece9842d260fd22ea\",\"data\":\"000000027805b2d199da1acc77a78e97f1c25279e0dfe3cc32d798ab5d1cb95c6c1c5039934ad388d565a8a61aa779aa98c666ccf1860f9f9c23b08b6e9d6b7c16b4726f52a88b191d00e3ef00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000\",\"hash1\":\"00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000\",\"target\":\"0000000000000000000000000000000000000000000000000000efe300000000\"},\"error\":null,\"id\":0}";
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jo=mapper.readTree(content).get("result");
			HexArray data=new HexArray(jo.get("data").asText());
			HexArray target=new HexArray(jo.get("target").asText());
			return new MiningWork(data,target);
		}catch(Throwable e){
			throw new MinyaException();
		}		
	}
	
	public boolean submitWork(MiningWork i_work,int i_nonce) throws MinyaException
	{
		try{
			String request = "{\"method\": \"getwork\", \"params\": [ \"" + i_work.makeSubmitData(i_nonce) + "\" ], \"id\":1}";
			String content =this.getJsonRpcResult(request);
	    	
	        Matcher m = resultPattern.matcher(content);
	        if (m.find() && m.group(1).equals("true"))
	        	return true;
			return false;	
		}catch(Throwable e){
			throw new MinyaException();
		}		
		
	}
	private String getJsonRpcResult(String request) throws IOException
	{
		HttpURLConnection conn=(HttpURLConnection) this._rpc_url.openConnection();
		if (conn.getConnectTimeout() == 0)
			conn.setConnectTimeout(DEFAULT_TIMEOUT);
		if (conn.getReadTimeout() == 0)
			conn.setReadTimeout(DEFAULT_TIMEOUT);
    	conn.setRequestMethod("POST");
    	if (this._auth != null){
    		conn.setRequestProperty("Authorization", "Basic " + Base64.encode(this._auth));
    	}
    	conn.setRequestProperty("Content-Type", "application/json");
    	conn.setRequestProperty("Content-Length", Integer.toString(request.getBytes().length));
    	conn.setRequestProperty("X-Mining-Extensions", "midstate");
    	conn.setAllowUserInteraction(false);
    	conn.setUseCaches(false);
		conn.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	    wr.writeBytes(request);
	    wr.close();
    	int response = conn.getResponseCode(); 
    	if (response == 401 || response == 403){
    		throw new IllegalArgumentException("Access denied");    	
    	}
	    //Result
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[4096];
        while ((len = is.read(buffer)) != -1) {
        	bos.write(buffer, 0, len);
        }
    	String content = bos.toString();
        is.close();
        return content;	    
	}
	private boolean _is_connect=false;

	@Override
	public void addListener(IConnectionEvent i_listener) throws MinyaException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() throws MinyaException
	{
		this._is_connect=true;
	}

	@Override
	public void disconnect() throws MinyaException
	{
		this._is_connect=false;
	}

}