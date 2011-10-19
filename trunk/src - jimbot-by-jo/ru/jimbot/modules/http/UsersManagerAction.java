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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import ru.jimbot.UserConfig;
import ru.jimbot.core.Password;

/**
 * Страница управления сервисами: создание и удаление
 *
 * @author ~J0-MA-JO~
 */
public class UsersManagerAction extends MainPageServletActions {
    public String perform(HttpConnection con)  {
        try{
        String cmd = con.get("cmd");
        MainConfig mc= MainConfig.getInstance();
      if(cmd == null) {
              con.print( HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
                "<H2>Панель управления ботом</H2>" +
                "<H3>Управление пользователями</H3>");
             con.print( "<FORM METHOD=POST ACTION=\"main\">" +
                            "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"srvs_users\">" +
                            "<INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"add\">" +
                            "Имя пользователя: <INPUT TYPE=text NAME=\"user\" size=\"7\"> " +
                            "<INPUT TYPE=submit VALUE=\"Создать\"></FORM>");
             con.print( "<P><A HREF=\"?page=srvs_users_props\">" + "Редактировать полномочия</A><br>");

             con.print( "<FORM METHOD=POST ACTION=\"main\">" +
                            "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"srvs_users\">" +
                            "<INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"save\">");
              String s = "<TABLE>";
                     s += "<TR><TH ALIGN=LEFT>Пользователь</TD>";
                     s += "<TH ALIGN=LEFT> Пароль<TD></TD></TR>";

               for(int i =0; i<mc.getHttpUsers().size();i++) {
                    UserConfig uc= UserConfig.getInstance(mc.getHttpUsers().get(i));
                   s += "<TR><TD><u>" + uc.getHttpUser() + "</u></TD>";
                   s += "<TD><INPUT TYPE=password NAME=\"pass_" + i + "\" VALUE=\"" +
                                uc.getStringProperty("httpPass")+"\" SIZE=15></TD>";
                   s += "<TD><A HREF=\"?page=srvs_users&cmd=del&us=" + uc.getHttpUser() + "\">(Удалить)</A></TD>";
                   s += "</TR>";

                }
              s += "</TABLE>";
              s += "<P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>";
              con.print( s);
              con.print( "<P><A HREF=\"?page=main_page\">Назад</A><br>");
              con.print( "</FONT></BODY></HTML>");
              return null;
      }
        if(cmd.equals("add")) {//добавляем пользователя
         String us = con.get("user");
         if (us.equals(""))//return "/main?page=error&id=10&ret=srvs_users&err=Пустое имя";
                      return"\""+con.getURI()+"?page=error&id=12&ret=srvs_users"+"\"";
         if (mc.getHttpUsers().contains(us))//return "/main?page=error&id=10&ret=srvs_users&err=Такой пользователь уже существует";
                      return"\""+con.getURI()+"?page=error&id=13&ret=srvs_users"+"\"";
         mc.adduser(us);
         //return "/main?page=message&id=0&ret=srvs_users";
                    return"\""+con.getURI()+"?page=message&id=0&ret=srvs_users"+"\"";
       }
        if(cmd.equals("save")) {//сохраняем данные
          for(int i =0; i<mc.getHttpUsers().size();i++) {
           UserConfig uc= UserConfig.getInstance(mc.getHttpUsers().get(i));
           uc.setHttpPass(new Password(con.request.getParameter("pass_"+i)));
           uc.save();
          }
        // return "/main?page=message&id=0&ret=srvs_users";
                    return"\""+con.getURI()+"?page=message&id=0&ret=srvs_users"+"\"";
       }
        if(cmd.equals("del")) {//удаляем пользователя
         String us = con.request.getParameter("us");
         mc.deluser(us);
         //mc.save();
        // return "/main?page=message&id=0&ret=srvs_users";
                    return"\""+con.getURI()+"?page=message&id=0&ret=srvs_users"+"\"";
       }


        return null;
          }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }   
    }
}