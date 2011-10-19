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
 * Страница настроек уинов для сервиса
 *
 * @author Prolubnikov Dmitry
 * @modification ~Jo-MA-Jo~
 */
public class UinPropertiesAction extends MainPageServletActions {
    public String perform(HttpConnection con) {
        try{
        String ns = con.get("ns"); // Имя сервиса
        if (!Manager.getInstance().getServiceNames().contains(ns)) {
            //return "/main?page=error&id=0&ret=main_page";
              return"\""+con.getURI()+"?page=error&id=0&ret=main_page\"";
        }
        String save = con.get("save");
        String cmd = con.get("cmd");
        String cnt = con.get("cnt");
        String sp = con.get("sp");
        if (save == null) {
            con.print( HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE>" + "</HEAD>" + BODY +
                    "<H2>Панель управления ботом</H2>" +
                    "<H3>Настройки UIN для сервиса " + ns + "</H3>");
         String s = "<FORM name='saver'METHOD=POST ACTION=\"main\">" +
                    "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"srvs_props_uin\">" +
                    "<INPUT TYPE=hidden NAME=\"ns\" VALUE=\"" + ns + "\">" +
                    "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">" +
                    "<INPUT id=\"cmd\" TYPE=hidden NAME=\"cmd\" VALUE=\"save\">";
              s += "<P><INPUT TYPE=button VALUE=\"Добавить новый UIN \"onclick=\"javascript:{document.getElementById('cmd').value='add'; document.saver.submit().click();}\">";
                //    s += "<P><A HREF=\"?page=srvs_props_uin&cmd=add&save=1&ns=" + ns + "&type=\">Добавить новый UIN</A>";
                 int ii=0;
                for(String c : Manager.getInstance().getAllProtocolManagers().keySet()) {
                  ii++;
                  s += ii == 1 ? c+"<input type=radio name=\"type\" value=\"" + c + "\"checked> " : (c+"<input type=radio name=\"type\" value=\"" + c + "\">");
               }
                 s += "<br>";
                for (int i = 0; i < Manager.getInstance().getService(ns).getConfig().getUins().size(); i++) {
                 s += "UIN" + i + " [" + Manager.getInstance().getService(ns).getConfig().getUins().get(i).getProtocol() + "]: " +
                        "<INPUT TYPE=text NAME=\"uin_" + i + "\" VALUE=\"" +
                        Manager.getInstance().getService(ns).getConfig().getUins().get(i).getScreenName() + "\"> : " +
                        "<INPUT TYPE=text NAME=\"pass_" + i + "\" VALUE=\"" +
                        "\"> " +
                        "<A HREF=\"?page=srvs_props_uin&cmd=del&save=1&ns=" + ns + "&cnt=" + i + "\">" +
                        "Удалить</A><br>";
            }
            s = s + "<P>Список uin;pass бота<P>(если добавить уины,поставь + в первую строчку )<br><textarea name=\"sp\" cols=\"20\" rows=\"15\" >"+""+"</textarea><br>";
            s += "<P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>";
            con.print( s);
            con.print( "<P><A HREF=\"?page=main_service&ns="+ns+"\">" +
                            "Назад</A><br>");
            con.print( "</FONT></BODY></HTML>");
        } else if("1".equals(save)) {
            if ("save".equals(cmd)) {
               if (sp.length()>0&& sp.indexOf(";")>=0){
                 String uin = "";
                 String pass = "";
                 int ii=1;
                 String[] baztext = sp.split("\n");
                 String[] up =baztext[0].split(";");
                    if (baztext[0].charAt(0)!='+'){
                        Manager.getInstance().getService(ns).getConfig().delAllUin();
                       ii=0;
                    }
                 for(int i=ii;i<baztext.length;i++){
                   up =baztext[i].split(";");
                   uin = up[0];
                   pass = up[1].replace("\n","");
                   pass = pass.replace("\r","");
                  Manager.getInstance().getService(ns).getConfig().addUin(uin, pass,con.request.getParameter("type"));
                }
            }else{
                 for (int i = 0; i < Manager.getInstance().getService(ns).getConfig().getUins().size(); i++) {
                    if (!"".equals(con.get("pass_" + i)))
                        Manager.getInstance().getService(ns).getConfig().setUin(i,
                                con.get("uin_" + i),con.get("pass_" + i));
                }
    }
                Manager.getInstance().getService(ns).getConfig().save();
                  //return "/main?page=message&id=0&ret=srvs_props_uin&ns="+ns;
                  return"\""+con.getURI()+"?page=message&id=0&ret=srvs_props_uin&ns="+ns+"\"";
            } else if ("add".equals(cmd)) {
                Manager.getInstance().getService(ns).getConfig().addUin("111", "pass", con.get("type"));
                //TODO Разобраться
                //return "/main?page=message&id=2&ret=srvs_props_uin&ns="+ns;
                   return"\""+con.getURI()+"?page=message&id=2&ret=srvs_props_uin&ns="+ns+"\"";
            } else if ("del".equals(cmd)) {
                int i = Integer.parseInt(cnt);
                Manager.getInstance().getService(ns).getConfig().delUin(i);
                //TODO Разобраться
                //return "/main?page=message&id=2&ret=srvs_props_uin&ns="+ns;
                   return"\""+con.getURI()+"?page=message&id=2&ret=srvs_props_uin&ns="+ns+"\"";
            }
        }
        return null;
         }catch (Exception ex){
          return con.getURI()+"?page=login target=mainFrameset";
     }    
    }
}