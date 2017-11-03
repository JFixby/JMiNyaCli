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