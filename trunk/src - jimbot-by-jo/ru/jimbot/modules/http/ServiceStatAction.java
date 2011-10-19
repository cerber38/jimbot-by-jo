/*
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2010 JimBot project
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

package ru.jimbot.modules.http;

import ru.jimbot.MainConfig;
import ru.jimbot.Manager;
import ru.jimbot.core.MsgStatCounter;

import java.io.IOException;

/**
 * Выводит страницу со статистикой работы сервиса
 *
 * @author Prolubnikov Dmitry
 */
public class ServiceStatAction extends MainPageServletActions {
    public String perform(HttpConnection con)  {
        try{
        String ns = con.get("ns"); // Имя сервиса
    	if(!Manager.getInstance().getServiceNames().contains(ns)){
    		//return "/main?page=error&id=0&ret=index";
              return"\""+con.getURI()+"?page=error&id=0&ret=index\"";
    	}
    	con.print( HTML_HEAD + "<meta http-equiv=\"Refresh\" content=\"3; url=" +
    			"?page=srvs_stats&ns="+ ns + "\" />" +
    			"<TITLE>JimBot "+ MainConfig.VERSION+" </TITLE></HEAD>" + BODY +
                "<H3>Статистика работы " + ns + "</H3>");
        if(Manager.getInstance().getService(ns).isRun()){
    	con.print( "Очередь входящих сообщений: " + Manager.getInstance().getService(ns).getInQueue().size() + "<br>");
    	con.print( "Очередь исходящих сообщений: <br>");
    	for(int i=0;i<Manager.getInstance().getService(ns).getConfig().getUins().size();i++){
            String sn = Manager.getInstance().getService(ns).getConfig().getUins().get(i).getScreenName();
    		con.print( ">> " + sn +
    				(Manager.getInstance().getService(ns).getProtocol(sn).isOnLine() ? "  [ ON]  " : "  [OFF]  ") +
    				Manager.getInstance().getService(ns).getOutQueue(sn).size() +
    				", потери:" + Manager.getInstance().getService(ns).getOutQueue().getLostMsgCount(sn) +
                    (Manager.getInstance().getService(ns).getProtocol(sn).isOnLine() ?
                            "" : " [" + Manager.getInstance().getService(ns).getProtocol(sn).getLastError() + "] ") +
                    "<br>");
    	}
    	con.print( "<br>Статистика принятых сообщений по номерам:<br>");
    	String s = "<TABLE BORDER=\"1\"><TR><TD>UIN</TD><TD>1 минута</TD><TD>5 митут</TD><TD>60 минут</TD><TD>24 часа</TD><TD>Всего</TD></TR>";
    	int c = Manager.getInstance().getService(ns).getConfig().getUins().size();
    	for(int i=0;i<c;i++){
    		String u = Manager.getInstance().getService(ns).getConfig().getUins().get(i).getScreenName();
    		s += "<TR><TD>" + u +
    			"</TD><TD>" + MsgStatCounter.getElement(u).getMsgCount(MsgStatCounter.M1) +
    			"</TD><TD>" + MsgStatCounter.getElement(u).getMsgCount(MsgStatCounter.M5) +
    			"</TD><TD>" + MsgStatCounter.getElement(u).getMsgCount(MsgStatCounter.M60) +
    			"</TD><TD>" + MsgStatCounter.getElement(u).getMsgCount(MsgStatCounter.H24) +
    			"</TD><TD>" + MsgStatCounter.getElement(u).getMsgCount(MsgStatCounter.ALL) +
    			"</TD></TR>";
    	}
    	s += "</TABLE>";
        con.print( s);
        } else {
            con.print( "Сервис не запущен.");
        }
    	  con.print("<P><A HREF=\"?page=main_service&ns="+ns+"\">" +
                            "Назад</A><br>");
    	con.print( "</FONT></BODY></HTML>");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
        }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }
    
    }
}