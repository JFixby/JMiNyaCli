package jp.nyatla.minya.stratum;

import jp.nyatla.minya.MinyaException;

import org.codehaus.jackson.JsonNode;

public class StratumJsonMethodReconnect extends StratumJsonMethod
{
	//{"method":"client.reconnect",params:["host",1]}
	public final static String TEST_PATT = "{\"params\": [\"host\",80], \"jsonrpc\": \"2.0\", \"method\": \"client.reconnect\", \"id\": null}";

	// public parameter
	public int port;
	public String host;

	public StratumJsonMethodReconnect(JsonNode i_json_node) throws MinyaException {
		super(i_json_node);
		String s = i_json_node.get("method").asText();
		if (s.compareTo("client.reconnect") != 0) {
			throw new MinyaException();
		}
		JsonNode p=i_json_node.get("params");
		this.host=p.get(0).asText();
		this.port=p.get(1).asInt();
		return;
	}
}
