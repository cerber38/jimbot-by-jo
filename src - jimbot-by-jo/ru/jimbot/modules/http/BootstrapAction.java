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
import java.util.Locale;

/**
 * Выводит базовую страницу сервлета
 *
 * @author ~Jo-MA-Jo~
 */
public class BootstrapAction extends MainPageServletActions {

    public String perform(HttpConnection con) throws IOException {
       // response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=\"utf-8\"");
//        response.setLocale(Locale.getDefault());
       //             String uid = con.request.getRemoteAddr();
       //  System.out.println("BootstrapAction "+uid);
         con.print( HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>");
         String s = "<frameset cols=\"200,*\" rows=\"*\"  border=\"1\" frameborder=\"1\" framespacing=\"1\" name=\"mainFrameset\" id=\"mainFrameset\">";
            s =s+"<frameset rows=\"160,*\" framespacing=\"0\" frameborder=\"1\" border=\"1\" name=\"leftFrameset\" id=\"leftFrameset\">";
            s =s+"<frame src=\"?page=main_pageL1&\" name=\"main_pageL1\" frameborder=\"1\" scrolling=\"no\" id=\"main_pageL1\" />";
            s =s+"<frame src=\"?page=main_pageL2&\" name=\"main_pageL2\" frameborder=\"1\" id=\"main_pageL2\" /></frameset>";
            s =s+"<frame src=\"?page=main_page&\" name=\"main0\" frameborder=\"1\" id=\"main0\" />";//</frameset>";
            s =s+" <noframes>"+
        "<body bgcolor=\"#FFFFFF\">"+
            "<p>Для работы в панеле управления ботом нужен браузер с поддержкой <b>фреймов</b>.</p>"+
        "</body>"+
       "</noframes>"+
      "</frameset>"+
     "</body>"+
    "</HTML>";
  // con.print("<INPUT TYPE=hidden NAME=\"uid\" VALUE=\""+uid+"\">" );
   con.print( s);

        return null;
    }
}