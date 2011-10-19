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

package ru.jimbot.modules.chat.http;

import ru.jimbot.MainConfig;
import ru.jimbot.Manager;

import java.io.IOException;
import ru.jimbot.modules.http.*;

/**
 * Форма редактирования полномочий пользователей
 *
 * @author Prolubnikov Dmitry
 * @modification ~Jo-MA-Jo~
 */
public class UserGroupPropertiesAction extends MainPageServletActions {
    public String perform(HttpConnection con) throws IOException {
        String ns = con.get("ns"); // Имя сервиса

        if (!Manager.getInstance().getServiceNames().contains(ns)) {
          //  return "/main?page=error&id=0&ret=main_page";
              return"\""+con.getURI()+"?page=error&id=0&ret=main_page\"";
        }
        String[] gr = Manager.getInstance().getService(ns).getConfig().getStringProperty("auth.groups").split(";") ;

      //  String save = request.getParameter("save");
        String cmd = con.get("cmd");
        if(cmd == null) {
            con.print( HTML_HEAD + "<TITLE>JimBot "+ MainConfig.VERSION+" </TITLE></HEAD>" + BODY +
                            "<H2>Панель управления ботом</H2>" +
                            "<H3>Управление группами пользователей</H3>");
                    con.print( "<FORM METHOD=POST ACTION=\"main\">" +
                            "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"chat_user_group_props\">" +
                       //     "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">" +
                            "<INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"add\">" +
                            "<INPUT TYPE=hidden NAME=\"ns\" VALUE=\"" + ns + "\">" +
                            "Имя группы: <INPUT TYPE=text NAME=\"gr\" size=\"20\"> " +
                            "<INPUT TYPE=submit VALUE=\"Создать новую группу\"></FORM>");
                   con.print("<P><A HREF=\"?page=chat_user_auth_props&ns="+ns+"\">" + "Редактировать полномочия</A><br>");
                   con.print("<FORM METHOD=POST ACTION=\"main\">" +
                            "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"chat_user_group_props\">" +
                            "<INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"save\">" +
                            "<INPUT TYPE=hidden NAME=\"ns\" VALUE=\"" + ns + "\">");
                     String s = "<TABLE>";
                    for(int i=0; i<gr.length; i++){
                        s += "<TR><TH ALIGN=LEFT>"+gr[i]+"</TD>";
                        s += "<TD><INPUT TYPE=text NAME=\"pris_" + i + "\" VALUE=\"" +
                                Manager.getInstance().getService(ns).getConfig().getStringProperty("auth.group_prist_"+ gr[i])+"\" SIZE=7></TD>";
                        s += i==0 ? "" : "<TD><A HREF=\"?page=chat_user_group_props&cmd=del&ns="+ns+"&gr=" + i + "\">(Удалить)</A></TD>";
                        s += "</TR>";
                    }
                    s += "</TABLE>";
                    s += "<P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>";
                    con.print(s);
                        con.print( "<P><A HREF=\"?page=main_service&ns="+ns+"\">" +
                            "Назад</A><br>");
                    con.print( "</FONT></BODY></HTML>");
                    return null;
        }
        if(cmd.equals("add")) {
             String g = con.get("gr");
             if (g.equals(""))//return "/main?page=error&id=6&ret=chat_user_group_props";
              return" \""+con.getURI()+"?page=error&id=6&ret=chat_user_group_props\"";
            Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.groups", Manager.getInstance().getService(ns).getConfig().getStringProperty("auth.groups") + ";" + g);
            Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.group_" + g, "");
            Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.group_prist_" + g, "");
            Manager.getInstance().getService(ns).getConfig().save();
        // return "/main?page=message&id=0&ret=chat_user_group_props&ns="+ns;
              return"\""+con.getURI()+"?page=message&id=0&ret=chat_user_group_props&ns="+ns+"\"";
        }
        if(cmd.equals("del")) {
          String g = gr[Integer.parseInt(con.get("gr"))];
          String s = Manager.getInstance().getService(ns).getConfig().getStringProperty("auth.groups");
          s = s.replace(";"+g, "");
          Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.groups",s);
          Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.group_" + g, "");
          Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.group_prist_" + g, "");
          Manager.getInstance().getService(ns).getConfig().save();
      // return "/main?page=message&id=0&ret=chat_user_group_props&ns="+ns;
              return"\""+con.getURI()+"?page=message&id=0&ret=chat_user_group_props&ns="+ns+"\"";
        }
        if(cmd.equals("save")) {
               for (int i = 0; i < gr.length; ++i) {
           Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.group_prist_" + gr[i], con.get("pris_"+ i));
           }
             Manager.getInstance().getService(ns).getConfig().save();
        //return "/main?page=message&id=0&ret=chat_user_group_props&ns="+ns;
              return"\""+con.getURI()+"?page=message&id=0&ret=chat_user_group_props&ns="+ns+"\"";
        }

        return null;
    }
}