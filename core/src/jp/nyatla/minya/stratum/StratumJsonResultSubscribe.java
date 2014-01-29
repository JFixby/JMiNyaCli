/********************************************************************************
 * MiNya pjeject
 * Copyright 2014 nyatla.jp
 * https://github.com/nyatla/JMiNya
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 ********************************************************************************/

package jp.nyatla.minya.stratum;

import jp.nyatla.minya.HexArray;
import jp.nyatla.minya.MinyaException;

import org.codehaus.jackson.JsonNode;

public class StratumJsonResultSubscribe extends StratumJsonResult {
	public final String session_id;
	public final HexArray xnonce1;
	public final int xnonce2_size;
	public final static String TEST_PATT ="{\"id\":1,\"result\":[[\"mining.notify\",\"b86c07fd6cc70b367b61669fb5e91bfa\"],\"f8000105\",4],\"error\":null}";
	public StratumJsonResultSubscribe(JsonNode i_json_node) throws MinyaException {
		super(i_json_node);
		//エラー理由がある場合
		if(this.error!=null){
			throw new MinyaException(this.error.asText());
		}
		JsonNode n = i_json_node.get("result");
		if (!n.isArray()) {
			throw new MinyaException();
		}
		// sessionID
		if (n.get(0).get(0).asText().compareTo("mining.notify") != 0) {
			throw new MinyaException();
		}
		this.session_id = n.get(0).get(1).asText();
		// xnonce1
		this.xnonce1 = new HexArray(n.get(1).asText());
		//xnonce2_size
		this.xnonce2_size = n.get(2).asInt();
		return;
	}
}