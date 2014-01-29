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

public class StratumJson
{
	protected StratumJson()
	{		
		return;
	}
	/**
	 * コピーコンストラクタ
	 * @param i_src
	 */
	protected StratumJson(StratumJson i_src)
	{
		return;
	}
	/**
	 * HEXArrayを文字サイズチェック付きでnewする。
	 * @param i_str
	 * @param i_str_len
	 * @return
	 * @throws MinyaException
	 */
	protected static HexArray toHexArray(String i_str, int i_str_len) throws MinyaException
	{
		if (i_str.length() != i_str_len) {
			throw new MinyaException();
		}
		return new HexArray(i_str);
	}
}
