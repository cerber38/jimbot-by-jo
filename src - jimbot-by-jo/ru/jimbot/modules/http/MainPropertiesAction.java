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


import java.util.logging.Level;
import java.util.logging.Logger;
import ru.jimbot.MainConfig;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import ru.jimbot.util.UserPreference;

/**
 * Редактирование основных свойств программы
 *
 * @author Prolubnikov Dmitry
 */
public class MainPropertiesAction extends MainPageServletActions {
    public String perform(HttpConnection con) {
      try {
        String save = con.get("save");
 
        if(save == null) {
                con.print(HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY + "<H2>Панель управления ботом</H2>" + "<H3>Основные настройки бота</H3>");
                con.print("<FORM METHOD=POST ACTION=\"main\">" + "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"main_props\">" + "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">" + prefToHtml(MainConfig.getInstance().getUserPreference()) + "<P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>");
                con.print("<P><A HREF=\"?page=main_page\">" + "Назад</A><br>");
                con.print("</FONT></BODY></HTML>");

        } else {

            UserPreference[] p = MainConfig.getInstance().getUserPreference();
            for (int i = 0; i < p.length; i++) {
                if (p[i].getType() == UserPreference.BOOLEAN_TYPE) {
                    boolean b = getBoolVal(con, p[i].getKey());
                    if (b != (Boolean) p[i].getValue()) {
                        p[i].setValue(b);
                        MainConfig.getInstance().setBooleanProperty(p[i].getKey(), b);
                    }
                } else if (p[i].getType() == UserPreference.INTEGER_TYPE) {
                    int c = Integer.parseInt(getStringVal(con, p[i].getKey()));
                    if (c != (Integer) p[i].getValue()) {
                        p[i].setValue(c);
                        MainConfig.getInstance().setIntProperty(p[i].getKey(), c);
                    }
                } else if (p[i].getType() != UserPreference.CATEGORY_TYPE) {
                    String s = getStringVal(con, p[i].getKey());
                    if (!s.equals((String) p[i].getValue())) {
                        p[i].setValue(s);
                        MainConfig.getInstance().setStringProperty(p[i].getKey(), s);
                    }
                }
            }
            MainConfig.getInstance().save();
           // return "/main?page=message&id=0&ret=main_page";
              return"\""+con.getURI()+"?page=message&id=0&ret=main_page"+"\"";
        }
        return null;
     }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }
    }
}