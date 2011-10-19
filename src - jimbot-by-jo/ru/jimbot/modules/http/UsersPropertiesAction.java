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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import ru.jimbot.Manager;
import ru.jimbot.UserConfig;

/**
 * Форма редактирования полномочий пользователей админки
 *
 * @author ~J0-MA-JO~
 */
public class UsersPropertiesAction extends MainPageServletActions {
   // private static HashMap<String, Set<String>> p = new HashMap<String, Set<String>>();
   public String perform(HttpConnection con) throws IOException {

     String save = con.get("save");
     MainConfig mc=MainConfig.getInstance();
     Manager man=Manager.getInstance();

     HashMap<String, Set<String>> p = new HashMap<String, Set<String>>();
      Vector v = new Vector();
      for(int s=0;s<mc.getServicesCount();s++){
        String t = mc.getServiceType(s);
        if (!v.contains(t)){
            try{
             p.put(mc.getServiceType(s),man.getService(mc.getServiceName(s)).getConfig().getHttpPreference().keySet());
            } catch (Exception ex){ System.out.print("!!! ошибка, отсутствует плагин, который указан в настройках...");}
          //  p.put(mc.getServiceType(s),man.getService(mc.getServiceName(s)).getConfig().getPreference().keySet());
            v.add(t);
         }

      }

     if(save == null) {
        HashSet[] srv = new HashSet[mc.getUsersCount()];
        HashSet[] usprop = new HashSet[mc.getUsersCount()];
        for(int i=0; i<mc.getUsersCount(); i++){
            srv[i] = new HashSet<String>();
            usprop[i] = new HashSet<String>();
            try {
                String[] ss = UserConfig.getInstance(mc.getUser(i)).getUserSrv();
                String[] s = UserConfig.getInstance(mc.getUser(i)).getUserProps();
             if(ss.length>0)
                    for(int j=0;j<ss.length; j++)
                        srv[i].add(ss[j]);
             if(s.length>0)
                    for(int j=0;j<s.length; j++)
                        usprop[i].add(s[j]);
            } catch (Exception ex) {}
        }



        con.print( HTML_HEAD + "<TITLE>JimBot "+ MainConfig.VERSION+" </TITLE></HEAD>" + BODY +
                            "<H2>Панель управления ботом</H2>" +
                            "<H3>Управление полномочиями пользователей</H3>");
        String s = "<FORM METHOD=POST ACTION=\"main\">" +
                   "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"srvs_users_props\">" +
                   "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">";
        s += "<TABLE><tbody><TR style=\"background-color: rgb(217, 217, 200);\"><TH ALIGN=LEFT>";
        for(int i=0;i<mc.getUsersCount();i++)
            s += "<TD><b><u>" + mc.getUser(i) + "</u></b></TD>";
        s += "</TR>";
        for(int i=0;i<mc.getServicesCount();i++){
            s += "<TR style=\"background-color: rgb(217, 217, 200);\" " +
            		"onmouseover=\"this.style.backgroundColor='#ecece4'\" " +
            		"onmouseout=\"this.style.backgroundColor='#d9d9c8'\">" +
            		"<TH ALIGN=LEFT> [ "+mc.getServiceType(i)+" ] сервис - " + mc.getServiceName(i) + "</TD>";
            for(int ii=0;ii<mc.getUsersCount();ii++){
                  s += "<TD><INPUT TYPE=CHECKBOX NAME=\"" + mc.getUser(ii) + "_" + mc.getServiceName(i) +
                       "\" VALUE=\"true\" " + (srv[ii].contains(mc.getServiceName(i)) ? "CHECKED" : "") + "></TD>";
            }
            s += "</TR>";
        }

        Iterator pp = p.keySet().iterator();
           while (pp.hasNext()) {
               String t=(String) pp.next();
               int i=0;
            for(String tp:p.get(t)){
              s += "<TR style=\"background-color: rgb(217, 217, 200);\" " +
            		"onmouseover=\"this.style.backgroundColor='#ecece4'\" " +
            		"onmouseout=\"this.style.backgroundColor='#d9d9c8'\">" +
            		"<TH ALIGN=LEFT> type [ "+t+" ] - " + tp + "</TD>";
              for(int ii=0;ii<mc.getUsersCount();ii++){
                s += "<TD><INPUT TYPE=CHECKBOX NAME=\"" + t + "_" + i + "_" + ii +
                       "\" VALUE=\"true\" " + (usprop[ii].contains(t+ "_" + tp) ? "CHECKED" : "") + "></TD>";
               }
              i++;
            s += "</TR>";

          }
       }
        //*****
                    s += "<TR style=\"background-color: rgb(217, 217, 200);\" " +
            		"onmouseover=\"this.style.backgroundColor='#ecece4'\" " +
            		"onmouseout=\"this.style.backgroundColor='#d9d9c8'\">" +
            		"<TH ALIGN=LEFT> Настройки UIN </TD>";
            for(int ii=0;ii<mc.getUsersCount();ii++){
                  s += "<TD><INPUT TYPE=CHECKBOX NAME=\"uins_props_"+ii+
                       "\" VALUE=\"true\" " + (usprop[ii].contains("Настройки UIN") ? "CHECKED" : "") + "></TD>";
            }
            s += "</TR>";
        //TODO доработать
        //*****
                    s += "<TR style=\"background-color: rgb(217, 217, 200);\" " +
            		"onmouseover=\"this.style.backgroundColor='#ecece4'\" " +
            		"onmouseout=\"this.style.backgroundColor='#d9d9c8'\">" +
            		"<TH ALIGN=LEFT> Статистика </TD>";
            for(int ii=0;ii<mc.getUsersCount();ii++){
                  s += "<TD><INPUT TYPE=CHECKBOX NAME=\"stat_props_"+ii+
                       "\" VALUE=\"true\" " + (usprop[ii].contains("Статистика") ? "CHECKED" : "") + "></TD>";
            }
            s += "</TR>";

        s += "</tbody></TABLE><P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>";
        con.print(s);
        con.print( "<P><A HREF=\"?page=srvs_users\"> Назад</A><br>");
        con.print( "</FONT></BODY></HTML>");

        } else {

          for(int ii=0;ii<mc.getUsersCount();ii++){//сохраняем доступные сервисы
             UserConfig uc= UserConfig.getInstance(mc.getUser(ii));
             String ss = "";
               for(int i=0;i<mc.getServicesCount();i++){
                  String s = mc.getServiceName(i);
                  boolean b = getBoolVal(con, mc.getUser(ii) + "_" + s);
                    if(b) ss += s + ";";
               }
             try{
             ss = ss.substring(0, ss.length()-1);
             } catch (Exception ex) {}
                uc.setUserSrv(ss);
                uc.save();
          }
       //сохраняем остальное

        Iterator pp = p.keySet().iterator();
        HashMap<Integer, String> pr = new HashMap<Integer, String>();
           while (pp.hasNext()) {
               String t=(String) pp.next();
               int i=0;
            for(String tp:p.get(t)){
                  for(int ii=0;ii<mc.getUsersCount();ii++){
                     String s = t+ "_" + tp;
                     String sss="";
                     boolean b = getBoolVal(con, t + "_" + i + "_" + ii);
                if (pr.containsKey(ii))
                                    sss=pr.get(ii);
                if(b) pr.put(ii, sss + s + ";");
               }
                  i++;
          }


        }

        //*****
       // String ss = "";
            for(int ii=0;ii<mc.getUsersCount();ii++){
                 String ss ="";
                  if (pr.containsKey(ii))
                                    ss=pr.get(ii);
                 String s0 = "Настройки UIN";
                 String s1 = "Статистика";
//                 String s2 = "Полномочия";
//                 String s3 = "Админ-бот";
                 boolean b0 = getBoolVal(con,"uins_props_"+ii);
                 boolean b1 = getBoolVal(con,"stat_props_"+ii);
//                 boolean b2 = getBoolVal(request,"seals_props_"+ii);
//                 boolean b3 = getBoolVal(request,"adm_props_"+ii);
                 if(b0) ss += s0 + ";";
                 if(b1) ss += s1 + ";";
//                 if(b2) ss += s2 + ";";
//                 if(b3) ss += s3 + ";";
                 pr.put(ii, ss);
            }
        //**TODO доработать

         for(int ii=0;ii<mc.getUsersCount();ii++){
              UserConfig uc= UserConfig.getInstance(mc.getUser(ii));
              String sss=pr.get(ii);
               try{
                 if (!pr.get(ii).isEmpty())sss = sss.substring(0, sss.length()-1);
                  } catch (Exception ex) {}
                   uc.setUserProps(sss);
                   uc.save();
         }

              //return "/main?page=message&id=0&ret=srvs_users_props";
                    return"\""+con.getURI()+"?page=message&id=0&ret=srvs_users_props"+"\"";

        }
        return null;
    }
}