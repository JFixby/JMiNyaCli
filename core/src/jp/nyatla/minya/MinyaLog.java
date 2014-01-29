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

package jp.nyatla.minya;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MinyaLog
{
	public static interface ILogOutput
	{
		final static int LV_DEBUG	=1;
		final static int LV_MESSAGE	=2;
		final static int LV_WARNING	=3;
		public void println(int level,String s);
	}
	public static class StandardOutput implements ILogOutput
	{
		public void println(int level,String s)
		{
			System.out.println(s);
			System.out.flush();
		}
	}
	private static ILogOutput output=new StandardOutput();
	public static void setOutput(ILogOutput p){
		MinyaLog.output=p;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static String getDateHeader()
	{
        //現在日時を取得する
        Calendar c = Calendar.getInstance();
        //フォーマットパターンを指定して表示する
        return "["+sdf.format(c.getTime())+"]";
	}
	public static synchronized void message(String s)
	{
		MinyaLog.output.println(ILogOutput.LV_MESSAGE,getDateHeader()+s);
	}

	public static synchronized void warning(String s)
	{
		MinyaLog.output.println(ILogOutput.LV_WARNING,getDateHeader()+"Warning:"+s);
	}
	public static synchronized void debug(String s)
	{
		MinyaLog.output.println(ILogOutput.LV_DEBUG,getDateHeader()+"Debug:"+s);
	}
}
