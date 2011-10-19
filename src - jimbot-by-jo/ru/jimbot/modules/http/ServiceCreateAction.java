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

import java.io.IOException;

/**
 * Создание нового сервиса
 *
 * @author Prolubnikov Dmitry
 */
public class ServiceCreateAction extends MainPageServletActions {

    public String perform(HttpConnection con) {
        try{
        String save = con.get("save");
        if (save == null) {
            con.print( HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
                    "<H2>Панель управления ботом</H2>" +
                    "<H3>Создание нового сервиса</H3>");
            con.print( "<FORM METHOD=POST ACTION=\"main\">" +
                    "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"srvs_create\">" +
                    "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">" +
                    "Имя сервиса: <INPUT TYPE=text NAME=\"ns\" size=\"40\"> <br>" +
                    "Тип сервиса: ");
            for(String i : Manager.getInstance().getAvailableServices()) {
            	 con.print( i + "<input type=radio name=\"type\" value=\"" + i + "\"> ");
            }
//                    "chat <input type=radio name=\"type\" value=\"chat\"> " +
//                    "anek <input type=radio name=\"type\" value=\"anek\">" +
            con.print( "<P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>");

            con.print( "<P><A HREF=\"?page=srvs_manager\">" +
                    "Назад</A><br>");
            con.print("</FONT></BODY></HTML>");
        } else {
            String ns = con.get("ns");
            String type = con.get("type");
            if (ns.equals("")) {
              //  return "/main?page=error&id=1&ret=srvs_create";
              return"\""+con.getURI()+"?page=error&id=1&ret=srvs_create"+"\"";
            }
            if (Manager.getInstance().getServiceNames().contains(ns)) {
               // return "/main?page=error&id=2&ret=srvs_create";
              return"\""+con.getURI()+"?page=error&id=2&ret=srvs_create"+"\"";
            }
            if (type == null) {
                //return "/main?page=error&id=3&ret=srvs_create";
              return"\""+con.getURI()+"?page=error&id=3&ret=srvs_create"+"\"";
            }
            if(!Manager.getInstance().getServiceBuilder(type).createServiceData(ns)) {
            	//return "/main?page=error&id=4&ret=srvs_create";
              return"\""+con.getURI()+"?page=error&id=4&ret=srvs_create"+"\"";
            }
 
            MainConfig.getInstance().addService(ns, type);
            MainConfig.getInstance().save();
            Manager.getInstance().addService(ns, type);
            Manager.getInstance().getService(ns).getConfig().save();
                 // return "/main?page=message&id=0&ret=srvs_manager";
              return"\""+con.getURI()+"?page=message&id=0&ret=srvs_manager\"";
        }
        return null;
     }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }  
        
    }
    

}