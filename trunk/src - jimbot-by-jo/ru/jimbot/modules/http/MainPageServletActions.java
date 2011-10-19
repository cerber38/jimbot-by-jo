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


import java.io.BufferedReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Random;
import ru.jimbot.Manager;
import ru.jimbot.util.UserPreference;

/**
 * Общие методы для действий сервлета
 *
 * @author Prolubnikov Dmitry
 */
public abstract class MainPageServletActions implements Action {
   public static final String HTML_HEAD_SPOILER =
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\"><HTML><HEAD>" +
            "<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\" />" +
            "<style>"+
            "div.spoiler{"+
                    "border: 3px solid #e6e6fa;"+
                    "background-color: #c7d0cc;"+
                    "margin: 7px;"+
                    "padding: 7px;"+
            "}"+
            "div.advSpoilerHeader{"+
                    "margin-left: 7px;"+
                    "margin-right: 7px;"+
                    "margin-bottom: -8px;"+
                    "border-bottom: 2px solid #e6e6fa;"+
                    "width: 99%;"+
                    "cursor: pointer;"+
            "}"+
            "</style>"+
            //"<script type=\"text/JavaScript\" src=\"http://aska-bot.ru/jquery_min.js\"></script>"+
            //"<script type=\"text/JavaScript\" src=\"http://aska-bot.ru/jquery_spoiler_control.js\"></script>"+
            "<script type=\"text/JavaScript\"> "+
            getScript("jquery-1.4.2.min.js")+
            "</script>"+

            "<script type=\"text/JavaScript\"> "+
            getScript("jquery-spoiler_control.js")+
            "</script>"+

            "<script type=\"text/JavaScript\"> "+
            "$(document).ready(function(){"+
            "BspoilerControl();"+
            "});"+
            "</script>";
  public static final String HTML_HEAD =
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\"><HTML><HEAD>" +
            "<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\" />";   
  public static final String BODY = "<BODY BGCOLOR=\"#c0c0c0\">";
  public static final String BODY1 = "<BODY BGCOLOR=\"#8a8da3\">";
  public static HashMap <String,Users> users = new HashMap <String,Users>();



   public static String getScript(String name) {
     String script= "";
     try{
        BufferedReader r = new BufferedReader(new InputStreamReader
                (Manager.class.getClassLoader().getResourceAsStream("ru/jimbot/templats/"+name)));
           while (r.ready()){
             script += r.readLine()+ '\n';
           }
         r.close();
       } catch (IOException ex) {}
       return script;
    }      
   
   public Users getUser(String uid) {
        if (users.containsKey(uid)) {
              return users.get(uid);
           }
        return null;
    }

   public HashMap <String,Users> getUsers() {
        return users;
    }

    public void addUser(String uid,String UserName,String UserPass ){
        users.put(uid, new Users(UserName,UserPass));
   }
    /**
     * Вывод данных на веб-страницу
     * @param response
     * @param output
     * @throws IOException
     */
    public void print(HttpServletResponse response, String output) throws IOException {
        new PrintStream(response.getOutputStream(), false, "UTF-8").println(output);
    }

    /**
     * Формирует форму для редактирования настроек бота
     *
     * @param p
     * @return
     */
    protected String prefToHtml(UserPreference[] p) {
        String s = "";
        String t = "<TABLE>";
        String e = "";
        boolean b =false;
        for (int i = 0; i < p.length; i++) {
               if (i+1 < p.length&&p[i+1].getType() == UserPreference.SPOILER_TYPE_OFF){
                e = "</TABLE>";
               }else if (i+1 == p.length){
                e = "</TABLE>";
               }else if (i+1 < p.length&&p[i+1].getType() == UserPreference.SPOILER_TYPE_ON){
                e = "</TABLE>";
               }else e = "";
            if (p[i].getType() == UserPreference.CATEGORY_TYPE) {
                s +=  t+"<TR><TH ALIGN=LEFT><u>" + p[i].getDisplayedKey() + "</u></TD></TR>"+e;
            } else if (p[i].getType() == UserPreference.BOOLEAN_TYPE) {
                s += t+"<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD><INPUT TYPE=CHECKBOX NAME=\"" + p[i].getKey() +
                        "\" VALUE=\"true\" " + ((Boolean) p[i].getValue() ? "CHECKED" : "") + "></TD></TR>"+e;
            } else if (p[i].getType() == UserPreference.SELECT_TYPE) {
                s += t+"<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD> <SELECT NAME=\"" + p[i].getKey() +"\">";
//                    try{
//                       s += "<OPTION SELECTED VALUE=\""+p[i].getValue()+"\">"+p[i].getAvailableValEx()[Integer.valueOf(p[i].getValue().toString())][1];
//                       } catch (Exception ex) {
                       s += "<OPTION SELECTED VALUE=\""+p[i].getValue()+"\">"+p[i].getValue();
//                       }
                     for (int ii = 0; ii < p[i].getAvailableValEx().length; ii++) {
                      s += "<OPTION VALUE=\""+p[i].getAvailableValEx()[ii][0]+"\">"+p[i].getAvailableValEx()[ii][1]+"</OPTION>";
                     }
                s += "</SELECT></TD></TR>"+e;
            } else if (p[i].getType() == UserPreference.TEXTAREA_TYPE) {
                 s += t+"<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD><textarea name=\""+ p[i].getKey() +"\" cols=\"52\" rows=\"2\" >"+ p[i].getValue() +"</textarea>"
//                        + "<INPUT size=\"70\" TYPE=text NAME=\"" + p[i].getKey() +
//                        "\" VALUE=\"" + p[i].getValue() + "\">"
                        + "</TD></TR>"+e;
             }else if (p[i].getType() == UserPreference.SPOILER_TYPE_ON) {
                s += "<h3><TH ALIGN=LEFT><u>" + p[i].getDisplayedKey() + "</u></TD></h3>"
                        + "<p name='spoilerbutton' style='cursor:pointer'></p>"
                        + "<DIV name='spoiler' class='spoiler'>";
                        //+ "<table>";
                        b = true;
             }else if (p[i].getType() == UserPreference.SPOILER_TYPE_OFF) {
               // s += "</DIV>";
                s += (i+1 < p.length&&p[i+1].getType() != UserPreference.SPOILER_TYPE_ON)?"</DIV><p></p>":"</DIV>";
                        b = true;
             }else{
                s += t+"<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD><INPUT size=\"70\" TYPE=text NAME=\"" + p[i].getKey() +
                        "\" VALUE=\"" + p[i].getValue() + "\"></TD></TR>"+e;
            }
             if (b){
                  t = "<TABLE>";
                  b =false;
            }else t = "";

        }
       // System.out.println(s);
     //   s += b ? "</TABLE>":"";
     //   s += "</TABLE>";
        return s;

    }

 /**
     * Формирует форму для редактирования настроек бота
     *
     * @param p
     * @return
     */
    protected String prefToHtml1(UserPreference[] p) {
        String s = "<TABLE>";
        for (int i = 0; i < p.length; i++) {
            if (p[i].getType() == UserPreference.CATEGORY_TYPE) {
                s += "<TR><TH ALIGN=LEFT><u>" + p[i].getDisplayedKey() + "</u></TD></TR>";
            } else if (p[i].getType() == UserPreference.BOOLEAN_TYPE) {
                s += "<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD><INPUT TYPE=CHECKBOX NAME=\"" + p[i].getKey() +
                        "\" VALUE=\"true\" " + ((Boolean) p[i].getValue() ? "CHECKED" : "") + "></TD></TR>";
            } else if (p[i].getType() == UserPreference.SELECT_TYPE) {
                s += "<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD> <SELECT NAME=\"" + p[i].getKey() +"\">";
//                    try{
//                       s += "<OPTION SELECTED VALUE=\""+p[i].getValue()+"\">"+p[i].getAvailableValEx()[Integer.valueOf(p[i].getValue().toString())][1];
//                       } catch (Exception ex) {
                       s += "<OPTION SELECTED VALUE=\""+p[i].getValue()+"\">"+p[i].getValue();
//                       }
                     for (int ii = 0; ii < p[i].getAvailableValEx().length; ii++) {
                      s += "<OPTION VALUE=\""+p[i].getAvailableValEx()[ii][0]+"\">"+p[i].getAvailableValEx()[ii][1]+"</OPTION>";
                     }
                s += "</SELECT></TD></TR>";
            } else if (p[i].getType() == UserPreference.TEXTAREA_TYPE) {
                 s += "<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD><textarea name=\""+ p[i].getKey() +"\" cols=\"52\" rows=\"2\" >"+ p[i].getValue() +"</textarea>"
//                        + "<INPUT size=\"70\" TYPE=text NAME=\"" + p[i].getKey() +
//                        "\" VALUE=\"" + p[i].getValue() + "\">"
                        + "</TD></TR>";
            }else {
                s += "<TR><TH ALIGN=LEFT>" + p[i].getDisplayedKey() + "</TD> " +
                        "<TD><INPUT size=\"70\" TYPE=text NAME=\"" + p[i].getKey() +
                        "\" VALUE=\"" + p[i].getValue() + "\"></TD></TR>";
            }
        }
        s += "</TABLE>";
        return s;
    }
    protected String encodeURL(String str) {
    	System.out.println("Encode URL: "+str);
        StringBuffer buf = new StringBuffer();
        for (int i = 0, n = str.length(); i < n; i++) {
            char ch = str.charAt(i);
            if (ch == ' ') {
                buf.append("+");
            } else if (!((ch >= '0' && ch <= '9') || (ch >= 'a' || ch <= 'z') ||
                         (ch >= 'A' || ch <= 'Z'))) {
                buf.append("%");
                buf.append(((ch >> 4) & 0xF) >= 10 ?
                           ((ch >> 4) & 0xF) + 'A' - 10 :
                           ((ch >> 4) & 0xF) + '0');
                buf.append((ch & 0xF) >= 10 ? (ch & 0xF) + 'A' - 10 :
                           (ch & 0xF) + '0');
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    protected String encodeHTML(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0, n = str.length(); i < n; i++) {
            char ch = str.charAt(i);
            switch (ch) {
            case '<':
                buf.append("&lt;");
                break;
            case '>':
                buf.append("&gt;");
                break;
            case '&':
                buf.append("&amp;");
                break;
            case '"':
                buf.append("&quot;");
                break;
            default:
                buf.append(ch);
            }
        }
        return buf.toString();
    }

//    protected String getStringVal(HttpServletRequest request, String name) throws IOException {
//        return (name) == null ? "" : request.getParameter(name);
//    }
//
//    protected boolean getBoolVal(HttpServletRequest request, String name) throws IOException {
//        return request.getParameter(name) == null ? false : true;
//    }

    protected static String getStringVal(HttpConnection con, String name) throws IOException {
    	return con.get(name) == null ? "" : con.get(name);
    }
    
    protected static boolean getBoolVal(HttpConnection con, String name) throws IOException {
    	return con.get(name) == null ? false : true;
    }
    
        /**
     * Случайная строка - идентификатор сеанса
     * @return
     */
    public static String getSessionId(){
    	String s = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijklmnprstuvwxyz";
        Random r = new Random();
        String v="";
        for(int i=0;i<10;i++){
            v += s.charAt(r.nextInt(s.length()));
        }
        return v;
    }
}
