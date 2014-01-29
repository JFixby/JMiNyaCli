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

package jp.nyatla.minya.connection;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;

public interface IMiningConnection
{
	/**
	 * コネクションから非同期イベントを受け取るオブジェクトを追加する。
	 * {@link #connect()}実行前に設定する事。
	 * @param i_listener
	 * @throws MinyaException
	 */
	public void addListener(IConnectionEvent i_listener) throws MinyaException;
	public MiningWork connect() throws MinyaException;
	public void disconnect() throws MinyaException;
	public MiningWork getWork() throws MinyaException;
	public void submitWork(MiningWork i_work, int i_nonce) throws MinyaException;
}
