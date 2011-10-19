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

/**
 * Выводит страницу с сообщением об ошибке
 *
 * @author Prolubnikov Dmitry
 */
public class ErrorMessageAction extends MainPageServletActions {
    public String perform(HttpConnection con) throws IOException {
        HttpServletRequest request=con.request;
        String err = con.get("err");
        err = err == null ? "" : (err+"<P>");
        String ret = con.get("ret");
        String target = con.get("target");
        target = target == null ? "" : (" target="+target);
        if(ret==null) ret = "main_page";
        String ns = con.get("ns");
        ret += ns == null ? "" : ("&ns=" + ns);
        String bot = con.get("bot");
        ret += bot == null ? "" : ("&bot=" + bot);
        String pr = con.get("pr");
        ret += pr == null ? "" : ("&pr=" + pr);
        int id = Integer.parseInt(con.get("id"));
        String msg = (new String[]{"Отсутствует сервис с таким именем!",
            /*1*/      "Пустое имя сервиса!",
            /*2*/      "Сервис с таким именем уже существует!",
            /*3*/      "Необходимо выбрать тип сервиса!",
            /*4*/      "Произошла ошибка при создании сервиса.",
            /*5*/      "Произошла ошибка!",
            /*6*/      "Пустое имя группы!",
            /*7*/      "Пустой ник!",
            /*8*/      "Бот с таким ником уже существует!",
            /*9*/      "Пустая фраза!",
            /*10*/     "Произошла ошибка!"+err,
            /*11*/     "Неверный пароль!",
            /*12*/     "Пустое имя!",
            /*13*/     "Такой пользователь уже существует",
            /*14*/     "Ошибка авторизации!",
                                          })[id];
        con.print( HTML_HEAD + "<meta http-equiv=\"Refresh\" content=\"3; url=" +
                        "?page=" +ret +"\""+target+">" +
                        "<TITLE>JimBot "+ MainConfig.VERSION+" </TITLE></HEAD><BODY><H3><FONT COLOR=\"#FF0000\">" +
                        msg + " </FONT></H3>");
        con.print("<P><A HREF=\"?page=" +
                        ret +"\""+target+">" + "Назад</A><br>");
        con.print( "</FONT></BODY></HTML>");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}