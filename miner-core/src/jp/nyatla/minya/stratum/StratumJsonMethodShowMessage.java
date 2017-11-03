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

public class StratumJsonMethodShowMessage extends StratumJsonMethod
{
	//{"method":"client.reconnect",params:["test",1]}
	public final static String TEST_PATT = "{\"params\": [\"TEST\"], \"jsonrpc\": \"2.0\", \"method\": \"client.show_message\", \"id\": null}";
	public final String val;
	// public parameterima
	public StratumJsonMethodShowMessage(JsonNode i_json_node) throws MinyaException {
		super(i_json_node);
		String s = i_json_node.get("method").asText();
		if (s.compareTo("client.show_message") != 0) {
			throw new MinyaException();
		}
		this.val=i_json_node.get("params").asText();
		return;
	}
}
