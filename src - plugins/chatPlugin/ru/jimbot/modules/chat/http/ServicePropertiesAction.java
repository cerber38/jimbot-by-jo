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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import ru.jimbot.core.api.ServiceConfigExtend;
import ru.jimbot.modules.http.HttpConnection;
import ru.jimbot.modules.http.MainPageServletActions;
import ru.jimbot.util.UserPreference;



/**
 * Редактирование свойств сервиса
 *
 * @author Prolubnikov Dmitry
 * @author ~Jo-MA-Jo~
 */
public class ServicePropertiesAction extends MainPageServletActions {
    public String perform(HttpConnection con) throws IOException {
        String ns = con.get("ns"); // Имя сервиса
    	if(!Manager.getInstance().getServiceNames().contains(ns)){
    		//return "/main?page=error&id=0&ret=index";
              return"\""+con.getURI()+"?page=error&id=0&ret=index\"";
    	}
        String save = con.get("save");
        UserPreference[]Pref = {};
        LinkedHashMap p = new LinkedHashMap<String, ServiceConfigExtend>();
        p=Manager.getInstance().getService(ns).getConfig().getHttpPreference();
        String idPref = con.get("pr");
        Iterator prop = p.keySet().iterator();
               while (prop.hasNext()) {
                   String prp = (String) prop.next();
                    if(idPref.equals(((ServiceConfigExtend)p.get(prp)).getName())) {
                       Pref=((ServiceConfigExtend)p.get(prp)).getPref();
                       break;
                    }
                 }
         if(save == null) {
            con.print( HTML_HEAD_SPOILER + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
                    "<H2>Панель управления ботом</H2>" +
                    "<H3>Настройки сервиса " + ns + "</H3>");
            con.print( "<FORM METHOD=POST ACTION=\"main\">" +
                    "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"chat_srvs_props\">" +
                    "<INPUT TYPE=hidden NAME=\"ns\" VALUE=\"" + ns + "\">" +
                    "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">" +
                    "<INPUT TYPE=hidden NAME=\"pr\" VALUE=\"" + idPref + "\">" +
                    prefToHtml(Pref) +
                    "<P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>");
            con.print( "<P><A HREF=\"?page=main_service&ns="+ns+"\">" +
                            "Назад</A><br>");
            con.print( "</FONT></BODY></HTML>");

        } else {

            for (int i = 0; i < Pref.length; i++) {
                if (Pref[i].getType() == UserPreference.BOOLEAN_TYPE) {
                    boolean b = getBoolVal(con, Pref[i].getKey());
                    if (b != (Boolean) Pref[i].getValue()) {
                        Pref[i].setValue(b);
                        Manager.getInstance().getService(ns).getConfig().setBooleanProperty(Pref[i].getKey(), b);
                    }
                } else if (Pref[i].getType() == UserPreference.INTEGER_TYPE) {
                    int c = Integer.parseInt(getStringVal(con, Pref[i].getKey()));
                    if (c != (Integer) Pref[i].getValue()) {
                        Pref[i].setValue(c);
                        Manager.getInstance().getService(ns).getConfig().setIntProperty(Pref[i].getKey(), c);
                    }
                } else if (Pref[i].getType() != UserPreference.CATEGORY_TYPE) {
                    String s = getStringVal(con, Pref[i].getKey());
                    if (!s.equals((String) Pref[i].getValue())) {
                        Pref[i].setValue(s);
                        Manager.getInstance().getService(ns).getConfig().setStringProperty(Pref[i].getKey(), s);
                    }
                }
            }
            Manager.getInstance().getService(ns).getConfig().save();
           // return "/main?page=message&id=0&ret=index";
           //  return "/main?page=message&id=0&ret=main_service&ns="+ns;
              return"\""+con.getURI()+"?page=message&id=0&ret=main_service&ns="+ns+"\"";
     }

        return null;
    }
}