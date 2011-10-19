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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import ru.jimbot.core.api.ServiceConfigExtend;

/**
 * Выводит базовую страницу сервлета
 *
 * @author Prolubnikov Dmitry
 * @author ~Jo-MA-Jo~
 */
public class MainSrv extends MainPageServletActions {

   public String perform(HttpConnection con) {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html");
//        response.setLocale(Locale.getDefault());
       try{
        String uid = con.request.getHashCookies().get("uid").getValue();
        // System.out.println(" uid = "+con.request.getHashCookies().get("uid").getValue());
        MainConfig ms = MainConfig.getInstance();
        Users user = users.get(uid);
        String ns = con.get("ns"); // Имя сервиса
        LinkedHashMap p = new LinkedHashMap<String, ServiceConfigExtend>();
        p=Manager.getInstance().getService(ns).getConfig().getHttpPreference();
        con.print(HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
                        "<H2>Панель управления сервисом "+ns+"</H2>");
                String s = "<TABLE>";
   if(ms.testUserProps(user.UserName,user.UserPass,"Настройки UIN"))
       s += "<TD><A HREF=\"?page=srvs_props_uin&ns="+ns+"\">Настройки UIN</A></TD>";
   if(ms.testUserProps(user.UserName,user.UserPass,"Статистика"))
       s += "<TD><A HREF=\"?page=srvs_stats&ns="+ns+"\">Статистика</A></TD>";
//   if(Manager.getInstance().getService(ns).getType().equals("chat")){
//        if(ms.testUserProps(user,"Полномочия")) s += "<TD><A HREF=\"?page=user_group_props&ns=" + ns + "\">Полномочия</A></TD>";
//        if(ms.testUserProps(user,"Админ-бот")) s += "<TD><A HREF=\"?page=bot_admin_props&ns=" + ns + "\">Админ-бот</A></TD>";
//   } else
//         s += "<TD> </TD>";
               Iterator prop = p.keySet().iterator();
               int i=-1;
                 while (prop.hasNext()) {
                     i++;
                     String prp = (String) prop.next();
                     String page = ((ServiceConfigExtend)p.get(prp)).getPage();
                     String type =Manager.getInstance().getService(ns).getType();
       if(ms.testUserProps(user.UserName,user.UserPass,type+"_"+prp))
           s +="<TR><TD><A HREF=\"?page="+page+"&ns="+ns+"\">"+prp+"</A></TD></TR>";
                  }
                    s += "</TR>";
                    s += "</TABLE>";
                con.print( s);
           //   for(IHTTPService t : HandlerFactory.getListHTTP()) {
           //    	print(response, "<br><A HREF=\"" + t.getPath() + "\">" + t.getName() + "</A>");
           //   }
                  con.print( "</FONT></BODY></HTML>");
        return null;
         }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }    
    }
}