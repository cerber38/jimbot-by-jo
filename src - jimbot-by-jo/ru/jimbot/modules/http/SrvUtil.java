/**
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2009 JimBot project
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

import java.io.IOException;
import java.util.Random;
import ru.jimbot.MainConfig;
import ru.jimbot.Manager;
import ru.jimbot.util.MainProps;

/**
 *
 * @author Prolubnikov Dmitry
 */
public class SrvUtil {
    static final String HTML_HEAD =
     "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\"><HTML><HEAD>" +
     "<meta content=\"text/html; charset=windows-1251\" http-equiv=\"Content-Type\" />";
    static final String BODY = "<BODY BGCOLOR=\"B0B3C1\">";
    static final String BODY_1 = "<BODY BGCOLOR=\"8A8DA3\">";
  static final String mootools = "";
    
    static final String EMPTY_LIST = "<OPTION>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</OPTION></SELECT>";

    public static String encodeURL(String str) {
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

    public static String encodeHTML(String str) {
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

    /**
     * 0 - ���� ������������ �� �������
     * @param user
     * @param pass
     * @return
     */
    public static int getAuth(String user, String pass){
        
    	if(user.equals(MainConfig.getInstance().getStringProperty("httpUser")) &&
    			pass.equals(MainConfig.getInstance().getStringProperty("httpPass")))
    		return 1;
    	else 
    	        return 0;
   
      }
        /**
     * -1 - ���� ������������ �� �������
     * @param user
     * @param pass
     * @return
     */
        public static int getAuth1(String user, String pass){
              
        for (int i = 0; i < MainConfig.getInstance().getIntProperty("srv.servicesCount"); ++i) {
    	if(user.equals(MainConfig.getInstance().getStringProperty("srv.serviceName"+i)) &&
    			pass.equals(MainConfig.getInstance().getStringProperty("srv.servicePass_"+user)))
    	      return i;
         } 
              return -1;
   	      
      }
        
              /**
     * -1 - ���� ������������ �� �������
     * @param srv
     * @return
     */
        public static int getNum(String srv){
              
        for (int i = 0; i < MainConfig.getInstance().getIntProperty("srv.servicesCount"); ++i) {
    	if(srv.equals(MainConfig.getInstance().getStringProperty("srv.serviceName"+i)))
    	      return i;
         } 
              return -1;
   	      
      }  
        
    
  
    public static void mainMenuReference(HttpConnection con) throws IOException {
       con.print("<P><HR><CENTER><A HREF=\"" + con.getURI() + "\" target=mainFrameset>�����</A></CENTER>");
    	con.print("</BODY></HTML>");
    }

  public static void error(HttpConnection con, String msg) throws IOException {
        con.print(HTML_HEAD +
                  "<TITLE>JimBot error</TITLE></HEAD><BODY><FONT COLOR=\"#FF0000\"><b>" +
                  encodeHTML(msg) + "</b></FONT>");
        mainMenuReference(con);
    }

    public static void message(HttpConnection con, String msg) throws IOException {
        con.print(HTML_HEAD +
                  "<TITLE>JimBot message</TITLE></HEAD><BODY><FONT COLOR=\"#004000\"><b>" +
                  encodeHTML(msg) + "</b></FONT>");
        mainMenuReference(con);
  
    }

    /**
     * ��������� ������ - ������������� ������
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
    
    public static String getStringVal(HttpConnection con, String name) throws IOException {
    	return con.get(name) == null ? "" : con.get(name);
    }
    
    public static boolean getBoolVal(HttpConnection con, String name) throws IOException {
    	return con.get(name) == null ? false : true;
    }
}
