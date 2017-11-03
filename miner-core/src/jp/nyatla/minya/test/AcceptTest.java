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

package jp.nyatla.minya.test;

import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.connection.TestStratumMiningConnection;


/**
 * ハッシュ関数のAcceptテスト
 * @author nyatla
 *
 */
public class AcceptTest
{
	public static void main(String[] args)
	{		
		try {
			TestStratumMiningConnection twf=new TestStratumMiningConnection(0);
			MiningWork mw=twf.getWork();
			mw.dump();
			long start = System.currentTimeMillis();
			System.out.println(System.currentTimeMillis()-start);
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
