/**
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2009 JimBot project
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package ru.jimbot;

import java.io.PrintStream;
import java.util.Vector;


import ru.jimbot.modules.http.Server;
import ru.jimbot.util.Log;
import ru.jimbot.util.MainProps;
import ru.jimbot.util.SystemErrLogger;

/**
 * ������ ����
 * @author Prolubnikov Dmitriy
 *
 */
public class StartBot {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	//	java.awt.EventQueue.invokeLater(new Runnable() {
          // public void run() {
	//	System.out.println("!!!Start main");
	//	System.out.println(new File("./").getAbsolutePath());
            	System.setErr(new PrintStream(new SystemErrLogger(), true));
                // ������� ���������

                Manager.getInstance().startAll();
      //          MainConfig.load();
     //           Manager.getInstance();
                if( MainConfig.getInstance().getBooleanProperty("main.StartHTTP"))
                    try {
//                        Vector<String> v = WorkScript.getInstance("").listHTTPScripts();
//                        String[] s = new String[2+v.size()*2];
//                        s[0] = "/";
//                        s[1] = "ru.jimbot.modules.http.MainPage";
//                        for(int i=0;i<v.size();i++){
//                            s[i*2+2] = v.get(i);
//                            s[i*2+3] = "ru.jimbot.modules.http.HTTPScriptRequest";
//                        }

                        String[] s = new String[2];
                        s[0] = "/";
                        s[1] = "ru.jimbot.modules.http.MainPageServlet";
                        Server.startServer(s);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                 try {
       //         	 Manager.getInstance().startAll();
                 } catch (Exception ex) {
                	 ex.printStackTrace();
                 }
   //         }
   //     });
        }
}

