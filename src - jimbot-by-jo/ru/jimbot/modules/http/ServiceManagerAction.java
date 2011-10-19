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

/**
 * Страница управления сервисами: создание и удаление
 *
 * @author Prolubnikov Dmitry
 */
public class ServiceManagerAction extends MainPageServletActions {
    public String perform(HttpConnection con)  {
        try{
        con.print( HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
                "<H2>Панель управления ботом</H2>" +
                "<H3>Управление сервисами</H3>");
        con.print( "<A HREF=\"?page=srvs_create\">" +
                "Создать новый сервис</A><br><br>");
        String s = "<TABLE>";
        for (String n : Manager.getInstance().getServiceNames()) {
            s += "<TR><TH ALIGN=LEFT>" + n + "</TD>";
            s += "<TD><A HREF=\"?page=srvs_delete&ns=" + n + "\">(Удалить)</A></TD>";
            s += "</TR>";
        }
        s += "</TABLE>";
        con.print( s);
        con.print( "<P><A HREF=\"?page=main_page\">" +
                "Назад</A><br>");
        con.print("</FONT></BODY></HTML>");

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    
         }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }
   }
}