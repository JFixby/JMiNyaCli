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
