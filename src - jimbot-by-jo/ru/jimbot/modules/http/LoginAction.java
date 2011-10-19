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
public class LoginAction extends MainPageServletActions {

    public String perform(HttpConnection con) throws IOException {
     con.print(SrvUtil.HTML_HEAD + "<TITLE>JimBot "+MainConfig.VERSION +" </TITLE></HEAD>" +
                 "<body bgcolor=\"B0B3C1\">"+
                 "<table border=0 width=100% height=90%><tr>"+
	         "<td align=center><table border=0 cellpadding=0 cellspacing=0 width=300><tr>"+
                 "<td bgcolor=#000000><table border=0 cellpadding=3 cellspacing=1 width=300><tr>"+
                 "<td align=center bgcolor=212B80><font color=A7C4C4 face='Verdana, Arial, Helvetica, sans-serif' size=2>"+
                 "<b>Панель управления ботом</b></font></td></tr><tr>"+
		 "<td align=center bgcolor=15589F><font color=#FFFFFF face='Verdana, Arial, Helvetica, sans-serif' size=2>"+
                 "<b>Авторизация</b></font></td></tr><tr>"+
		 "<td align=center bgcolor=BAC3D3><font color=#000000 face='Verdana, Arial, Helvetica, sans-serif' size=2>"+
                 "<FORM METHOD=POST ACTION=\"" + con.getURI() +
                  "\"><INPUT TYPE=hidden NAME=\"page\" VALUE=\"main\">" +
                  "<TABLE><TR><TH ALIGN=LEFT>Имя:</TD>" +
                  "<TD><INPUT TYPE=text NAME=\"name\" SIZE=32></TD></TR>" +
                  "<TR><TH ALIGN=LEFT>Пароль:</TD>" +
                  "<TD><INPUT TYPE=password NAME=\"password\" SIZE=32></TD></TR></TABLE><P>" +
                  "<INPUT TYPE=submit VALUE=\"Войти\"></FORM>"+
                  "</font></td></tr>"+
                  "</table></td></tr></table>"+
                  "<font color=gray face='Verdana, Arial, Helvetica, sans-serif' size=1>&copy; mod By Jo-ma-Jo</font></a></td>"+
                  "</tr></table></FORM></body></html>");

        return null;
    }
}