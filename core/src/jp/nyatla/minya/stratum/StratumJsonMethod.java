package jp.nyatla.minya.stratum;

import jp.nyatla.minya.MinyaException;

import org.codehaus.jackson.JsonNode;

public class StratumJsonMethod extends StratumJson
{
	public final Long id;

	public StratumJsonMethod(JsonNode i_json_node) throws MinyaException {
		if (i_json_node.has("id")){
			this.id = i_json_node.get("id").isNull()?null:i_json_node.get("id").asLong();
		} else {
			this.id = null;
		}
		if(!i_json_node.has("method")){
			throw new MinyaException();
		}
	}
}