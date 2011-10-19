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

import ru.jimbot.util.HttpUtils;

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
public class BootstrapMain extends MainPageServletActions {

    public String perform(HttpConnection con) /*throws IOException */{
        try{
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html");
//        response.setLocale(Locale.getDefault());
     
        String uid = con.request.getHashCookies().get("uid").getValue();
        MainConfig ms = MainConfig.getInstance();
        LinkedHashMap p = new LinkedHashMap<String, ServiceConfigExtend>();
       // System.out.println("User- "+request.getRemoteUser());
        Users user = users.get(uid);
  //      System.out.println("User- "+user.UserName);
  //       System.out.println("uid- "+uid);
        con.print( HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
                        "<H2>Панель управления ботом</H2>");
        if(MainConfig.getInstance().getHttpUser().equals("admin") &&
                        MainConfig.getInstance().getHttpPass().getPass().equals("admin"))
                    con.print( "<H3><FONT COLOR=\"#FF0000\">В целях безопасности как можно скорее измените " +
                            "стандартный логин и пароль для доступа к этой странице! Рекомендуется также изменить порт.</FONT></H3>");
                if(HttpUtils.checkNewVersion()){
                    con.print( "<p>На сайте <A HREF=\"http://jimbot.ru\">jimbot.ru</A> Доступна новая версия!<br>");
                    con.print( HttpUtils.getNewVerDesc().replaceAll("\n", "<BR>"));
                    con.print( "</p>");
                }
        if(ms.testAdmin(user.UserName,user.UserPass)){
                con.print( "<H3>Главное меню</H3>");
                con.print( "<A HREF=\"?page=main_props\">" +
                        "Основные настройки</A><br>");
                con.print( "<A HREF=\"?page=srvs_manager\">" +
                        "Управление сервисами</A><br>");
        }
                String s = "<TABLE>";
             //   for(String n: Manager.getInstance().getServiceNames()){
                for(int i=0;i<MainConfig.getInstance().getServiceNames().size();i++){
                  String n = MainConfig.getInstance().getServiceNames().get(i);
       if(ms.testUserSrv(user.UserName,user.UserPass,n)){//проверка доступа к сервису
                   if(!Manager.getInstance().getServiceNames().contains(n))  continue;
                   p=Manager.getInstance().getService(n).getConfig().getHttpPreference();
             //     System.out.println(n);
                    s += "<TR><TH ALIGN=LEFT>"+n+"</TD>";
                      Iterator prop = p.keySet().iterator();
               int ii=-1;
                 while (prop.hasNext()) {
                     ii++;
                     String prp = (String) prop.next();
                     String page = ((ServiceConfigExtend)p.get(prp)).getPage();
                     String type =Manager.getInstance().getService(n).getType();
               //      System.out.println(type+"_"+prp);
       if(ms.testUserProps(user.UserName,user.UserPass,type+"_"+prp))
                  s +="<TD><A HREF=\"?page="+page+"&ns="+n+"\">"+prp+"</A></TD>";
                  }
               
                  //  s += "<TD><A HREF=\"?page=srvs_props&ns="+n+"\">Настройки сервиса</A></TD>";
       if(ms.testUserProps(user.UserName,user.UserPass,"Настройки UIN"))
                  s += "<TD><A HREF=\"?page=srvs_props_uin&ns="+n+"\">Настройки UIN</A></TD>";
      //              if(MainConfig.getInstance().getServiceTypes().get(i).equals("chat")){
     //  if(ms.testUserProps(user.UserName,user.UserPass,"Полномочия"))  s += "<TD><A HREF=\"?page=user_group_props&ns=" + n + "\">Полномочия</A></TD>";
     //  if(ms.testUserProps(user.UserName,user.UserPass,"Админ-бот"))  s += "<TD><A HREF=\"?page=bot_admin_props&ns=" + n + "\">Админ-бот</A></TD>";
                    } //else
                 //       s += "<TD> </TD>";
       if(ms.testUserProps(user.UserName,user.UserPass,"Статистика"))
               s += "<TD><A HREF=\"?page=srvs_stats&ns="+n+"\">Статистика</A></TD>";
                    if(Manager.getInstance().getService(n).isRun()){
                        s += "<TD><A HREF=\"?page=srvs_stop&ns="+n+"\"target=mainFrameset>Stop</A></TD>";
                    } else {
                        s += "<TD><A HREF=\"?page=srvs_start&ns="+n+"\"target=mainFrameset>Start</A></TD>";
                    }
                    s += "</TR>";
  //     }
                }
                s += "</TABLE>";
                con.print( s);
//                for(IHTTPService t : HandlerFactory.getListHTTP()) {
//                	print(response, "<br><A HREF=\"" + t.getPath() + "\">" + t.getName() + "</A>");
//                }
       if(ms.testAdmin(user.UserName,user.UserPass)){
                con.print( "<br><A HREF=\"?page=stop_bot\">" + "Отключить бота</A>");
                con.print( "<br><A HREF=\"?page=restart_bot\">" + "Перезапустить бота</A>");
       }
                con.print( "</FONT></BODY></HTML>");
        return null;
    }catch (Exception ex){
        
      //   return"\""+con.getURI()+"?page=error&id=14&ret=login&target=mainFrameset\"";
          return con.getURI()+"?page=login target=mainFrameset";
    }
   }
        
    
}

