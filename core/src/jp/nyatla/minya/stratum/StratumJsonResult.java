package jp.nyatla.minya.stratum;

import jp.nyatla.minya.MinyaException;

import org.codehaus.jackson.JsonNode;

public class StratumJsonResult extends StratumJson
{
	public final JsonNode error;
	public final Long id;

	public StratumJsonResult(JsonNode i_json_node) throws MinyaException {
		if (i_json_node.has("id")) {
			this.id = i_json_node.get("id").isNull()?null:i_json_node.get("id").asLong();
		} else {
			this.id = null;
		}
		if (!i_json_node.has("error")){
			throw new MinyaException();
		}
		if (i_json_node.get("error").isNull()) {
			this.error = null;
		} else {
			this.error=i_json_node.get("error");
		}
	}
}