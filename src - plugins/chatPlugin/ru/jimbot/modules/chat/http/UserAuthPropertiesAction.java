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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.jimbot.core.api.DefaultCommandParser;
import ru.jimbot.modules.http.HttpConnection;
import ru.jimbot.modules.http.MainPageServletActions;

/**
 * Форма редактирования полномочий пользователей
 *
 * @author Prolubnikov Dmitry
 */
public class UserAuthPropertiesAction extends MainPageServletActions {
    public String perform(HttpConnection con) throws IOException {
        String ns = con.get("ns"); // Имя сервиса

        if (!Manager.getInstance().getServiceNames().contains(ns)) {
           // return "/main?page=error&id=0&ret=main_page";
              return"\""+con.getURI()+"?page=error&id=0&ret=main_page\"";
        }
        String[] gr = Manager.getInstance().getService(ns).getConfig().getStringProperty("auth.groups").split(";") ;
        String save = con.get("save");
     //   String cmd = request.getParameter("cmd");
        if(save == null) {
        Set<String> au = ((DefaultCommandParser)Manager.getInstance().getService(ns).getCommandParser()).getAuthList().keySet();
        Map m = ((DefaultCommandParser)Manager.getInstance().getService(ns).getCommandParser()).getAuthList();
        HashSet[] grs = new HashSet[gr.length];
        for(int i=0; i<gr.length; i++){
            grs[i] = new HashSet<String>();
            try {
                String[] ss = Manager.getInstance().getService(ns).getConfig().getStringProperty("auth.group_"+gr[i]).split(";");
                if(ss.length>0)
                    for(int j=0;j<ss.length; j++)
                        grs[i].add(ss[j]);
            } catch (Exception ex) {}
        }

            con.print( HTML_HEAD + "<TITLE>JimBot "+ MainConfig.VERSION+" </TITLE></HEAD>" + BODY +
                            "<H2>Панель управления ботом</H2>" +
                            "<H3>Управление группами пользователей</H3>");
        String s = "<FORM METHOD=POST ACTION=\"main\">" +
                   "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"chat_user_auth_props\">" +
                   "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">" +
                 //  "<INPUT TYPE=hidden NAME=\"cmd\" VALUE=\"add\">" +
                   "<INPUT TYPE=hidden NAME=\"ns\" VALUE=\"" + ns + "\">";
        s += "<TABLE><tbody><TR style=\"background-color: rgb(217, 217, 200);\"><TH ALIGN=LEFT>";
        for(int i=0;i<gr.length;i++)
            s += "<TD><b><u>" + gr[i] + "</u></b></TD>";
        s += "</TR>";
        for(String ss:au){
            s += "<TR style=\"background-color: rgb(217, 217, 200);\" " +
            		"onmouseover=\"this.style.backgroundColor='#ecece4'\" " +
            		"onmouseout=\"this.style.backgroundColor='#d9d9c8'\">" +
            		"<TH ALIGN=LEFT>" + m.get(ss) + "  [" + ss + "]</TD>";
            for(int i=0; i<gr.length; i++){
                s += "<TD><INPUT TYPE=CHECKBOX NAME=\"" + gr[i] + "_" + ss +
                    "\" VALUE=\"true\" " + (grs[i].contains(ss) ? "CHECKED" : "") + "></TD>";
            }
            s += "</TR>";
        }
        s += "</tbody></TABLE><P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>";
       con.print(s);
        con.print( "<P><A HREF=\"?page=chat_user_group_props&ns="+ns+"\">" +
                          "Назад</A><br>");
                    con.print( "</FONT></BODY></HTML>");

        } else {
         Set<String> au = ((DefaultCommandParser)Manager.getInstance().getService(ns).getCommandParser()).getAuthList().keySet();
        HashSet[] grs = new HashSet[gr.length];
        for(int i=0; i<gr.length; i++){
            grs[i] = new HashSet<String>();
            try {
                String[] ss = Manager.getInstance().getService(ns).getConfig().getStringProperty("auth.group_"+gr[i]).split(";");
                if(ss.length>0)
                    for(int j=0;j<ss.length; j++)
                        grs[i].add(ss[j]);
            } catch (Exception ex) {}
        }
        for(int i=0; i<gr.length; i++){
            for(String s:au){
                boolean b = getBoolVal(con, gr[i] + "_" + s);
                if(b && !grs[i].contains(s))
                    grs[i].add(s);
                else if(!b && grs[i].contains(s))
                    grs[i].remove(s);
            }
        }
        for(int i=0; i<gr.length; i++){
            String s = "";
            for(Object c:grs[i]){
                s += c.toString() + ";";
            }
            s = s.substring(0, s.length()-1);
            Manager.getInstance().getService(ns).getConfig().setStringProperty("auth.group_"+gr[i], s);
        }
        Manager.getInstance().getService(ns).getConfig().save();
           //  return "/main?page=message&id=0&ret=chat_user_group_props&ns="+ns;
               return"\""+con.getURI()+"?page=message&id=0&ret=chat_user_group_props&ns="+ns+"\"";
        }


        return null;
    }
}