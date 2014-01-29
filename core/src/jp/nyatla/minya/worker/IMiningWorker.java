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

package jp.nyatla.minya.worker;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;

public interface IMiningWorker
{
	/**
	 * ワークを開始します。既にワークを実行中の場合は一度すべてのワークをシャットダウンして再起動します。
	 * 統計情報はリセットされます。
	 * @throws MinyaException 
	 */
	public boolean doWork(MiningWork i_work) throws MinyaException;
	/**
	 * 実行中のワークを停止します。
	 * @throws MinyaException 
	 */
	public void stopWork() throws MinyaException;
	/**
	 * 進行度を返します。
	 * @return
	 */
	public int getProgress();
	/**
	 * {@link #doWork}が計算したハッシュの数を返します。
	 * @return
	 */
	public int getNumberOfHash();
	
	public void addListener(IWorkerEvent i_listener) throws MinyaException;
}
