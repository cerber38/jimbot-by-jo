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

import ru.jimbot.Manager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import ru.jimbot.MainConfig;


/**
 * панель с сервисами
 * @author ~Jo-MA-Jo~
 */
public class MainPageL2 extends MainPageServletActions {
    public String perform(HttpConnection con) {
        try{
     //  response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=\"utf-8\"");
//        response.setLocale(Locale.getDefault());
        String uid = con.request.getHashCookies().get("uid").getValue();
     if (uid==null)return null;
     //   System.out.println("MainPageL2 "+uid);
        MainConfig ms = MainConfig.getInstance();
        Users user = users.get(uid);
       con.print(HTML_HEAD +"<meta http-equiv=\"Refresh\" content=\"7; url=?page=main_pageL2\" />" +
//      con.print(HTML_HEAD +
               "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY1);
    if(ms.testAdmin(user.UserName,user.UserPass)){
    con.print( "<H4>Главное меню</H4><H5>");
    // con.print("<INPUT TYPE=hidden NAME=\"uid\" VALUE=\""+uid+"\">" );
    con.print( "<A HREF=\"?page=main_props\" style=\"text-decoration:none\"target=main0>" + "Основные настройки</A><br>");
    con.print( "<A HREF=\"?page=srvs_manager\" style=\"text-decoration:none\"target=main0>" + "Управление сервисами</A><br>");
    con.print( "<A HREF=\"?page=srvs_users\" style=\"text-decoration:none\"target=main0>" + "Управление пользователями</A><br>");
    con.print( "<A HREF=\"?page=stop_bot\" style=\"text-decoration:none\"target=mainFrameset>" + "Отключить бота</A><br>");
    con.print( "<A HREF=\"?page=restart_bot\" style=\"text-decoration:none\"target=mainFrameset>" + "Перезапустить бота</A>");
    con.print( "<HR></H5>");
  }
    con.print( "<H4>Список сервисов:</H4>");
    String s = "<TABLE>";
    for(String n:Manager.getInstance().getServiceNames()){
  if(ms.testUserSrv(user.UserName,user.UserPass,n)){
        s = s + "<TD><H5><A HREF=\"?page=main_service&ns="+n+"\" style=\"text-decoration:none\"target=main0>"+ n +"</A></H5></TD>";

     if(Manager.getInstance().getService(n).isRun()){
        s = s + "<TD><H5><A HREF=\"?page=srvs_stop&ns="+n+"\" style=\"text-decoration:none\"target=mainFrameset>cтоп</A></H5></TD>";
      }
      else {
        s = s + "<TD><H5><A HREF=\"?page=srvs_start&ns="+n+"\" style=\"text-decoration:none\"target=mainFrameset>cтарт</A></H5></TD>";
      }
          s = s + "</TR>";
  }
    }
    s = s + "</TABLE>";
    con.print(s);
    con.print("</FONT></BODY></HTML>");

       return null;
     }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }  
       
    }



}