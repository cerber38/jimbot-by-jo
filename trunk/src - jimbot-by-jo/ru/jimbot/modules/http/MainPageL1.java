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

import java.io.File;
import ru.jimbot.Manager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import ru.jimbot.MainConfig;


/**
 * панель с инфой
 * @author ~Jo-MA-Jo~
 */
public class MainPageL1 extends MainPageServletActions {
    public String perform(HttpConnection con) throws IOException {
   //     response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=\"utf-8\"");
//        response.setLocale(Locale.getDefault());
        MainConfig ms = MainConfig.getInstance();
//        String user = request.getRemoteUser();
      // print(response, HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
         int srv=0;
   String imgURL="http://aska-bot.ru/img/";
//   String cmd = request.getParameter("action");
 for(String n:Manager.getInstance().getServiceNames()){
    if(Manager.getInstance().getService(n).isRun()) srv++;
 }
//  if ("upd".equals(cmd)) {
//String content =  "<div> "+
//"Всего сервисов: "+Manager.getInstance().getServiceCount()+"<br> "+
//"Запущеных сервисов: "+srv+"<br> "+
//"Использовано памяти: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())+"<br>"+
//"</div> ";
//    return content;
//  }
   String s = "<html><head><title>aceweb.ru - Scripts - JavaScript - Images Electro Clock</title>"+
"<META http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1251\">"+

 "<Style> A:Link{ Color: #000000; Text-decoration: underline}"+
         "A:Visited{ Color: #000000; Text-decoration: underline}"+
         "A:Hover{ Color: #000000; Text-decoration: none}"+
 "td, body {font-family: verdana, arial, helvetica; font-size:11px;}"+
"</Style>"+
  "<!-- HEAD START HERE -->" +
  "<Script Language=\"JavaScript1.1\">"+
  "c1=new Image(); c1.src=\""+imgURL+"c1.gif\" \n"+
  "c2=new Image(); c2.src=\""+imgURL+"c2.gif\" \n"+
  "c3=new Image(); c3.src=\""+imgURL+"c3.gif\" \n"+
  "c4=new Image(); c4.src=\""+imgURL+"c4.gif\" \n"+
  "c5=new Image(); c5.src=\""+imgURL+"c5.gif\" \n"+
  "c6=new Image(); c6.src=\""+imgURL+"c6.gif\" \n"+
  "c7=new Image(); c7.src=\""+imgURL+"c7.gif\" \n"+
  "c8=new Image(); c8.src=\""+imgURL+"c8.gif\" \n"+
  "c9=new Image(); c9.src=\""+imgURL+"c9.gif\" \n"+
  "c0=new Image(); c0.src=\""+imgURL+"c0.gif\" \n"+
  "cb=new Image(); cb.src=\""+imgURL+"cb.gif\" \n"+
  "function extract(d,h,m,s) \n"+
   "{if (!document.images) return \n"+
    "if (d<=9) \n"+
      "{document.images.aa.src=cb.src \n"+
       "document.images.ab.src=eval(\"c\"+d+\".src\")} \n"+
    "else \n"+
      "{document.images.aa.src=eval(\"c\"+Math.floor(d/10)+\".src\") \n"+
       "document.images.ab.src=eval(\"c\"+(d%10)+\".src\")} \n"+
    "if (h<=9) \n"+
      "{document.images.a.src=c0.src \n"+
       "document.images.b.src=eval(\"c\"+h+\".src\")} \n"+
    "else \n"+
      "{document.images.a.src=eval(\"c\"+Math.floor(h/10)+\".src\") \n"+
       "document.images.b.src=eval(\"c\"+(h%10)+\".src\")} \n"+
    "if (m<=9) \n"+
      "{document.images.d.src=c0.src \n"+
       "document.images.e.src=eval(\"c\"+m+\".src\")} \n"+
   "else \n"+
      "{document.images.d.src=eval(\"c\"+Math.floor(m/10)+\".src\") \n"+
       "document.images.e.src=eval(\"c\"+(m%10)+\".src\")} \n"+
    "if (s<=9) \n"+
      "{document.g.src=c0.src \n"+
       "document.images.h.src=eval(\"c\"+s+\".src\")} \n"+
    "else \n"+
      "{document.images.g.src=eval(\"c\"+Math.floor(s/10)+\".src\") \n"+
      " document.images.h.src=eval(\"c\"+(s%10)+\".src\")} \n"+
    "} \n"+
  "function show3() \n"+
    "{if (!document.images) \n"+
      " return \n"+
    " var Digital=new Date(new Date()-"+getTimeStart()+") \n"+
    " var days="+getUpTime()/86400000 +"\n"+
    " var hours=Digital.getHours() \n"+
    " var minutes=Digital.getMinutes() \n"+
    " var seconds=Digital.getSeconds() \n"+
    " if (hours==0) hours=9 \n"+
    " if (hours==1) hours=10 \n"+
    " if (hours==2) hours=11 \n"+
    " if (hours>=3) hours=hours-3 \n"+
    " extract(days,hours,minutes,seconds) \n"+

    " setTimeout(\"show3()\",1000)} \n"+
"</Script> \n"+
// "<Script> \n"+
//   " function updateInfList(){ \n"+
//       " var infList = $(\".content > div\"); \n"+
//       " var adr = document.location; \n"+
//       " var Digital=new Date(new Date()+5000) \n"+
//		"$.ajax({ \n"+
//			"type: \"POST\", url: adr, data: \"action=upd\",\n"+
//			"complete: function(data){\n"+
//			"infList.html(data.responseText);\n"+
//  			"}\n"+
//		"});\n"+
//	"setTimeout(\"updateInfList()\",5000); \n"+
//  "}\n"+
//"</Script> \n"+
"<!-- HEAD END HERE --> "+
"</head> "+
"<body bgcolor=\"7F82A8\" text=\"#000000\" link=\"#000000\" topmargin=\"0\" leftmargin=\"0\" OnLoad=\"show3()\"> "+
//"<body bgcolor=\"7F82A8\" text=\"#000000\" link=\"#000000\" topmargin=\"0\" leftmargin=\"0\" OnLoad=\"javascript:{show3();updateInfList();}\"> "+
"<!-- BODY START HERE --> "+
 "Бот запущен: <br><center>" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(getTimeStart()))+"</center><HR> "+
  "Время работы: <br> "+
"<Img Src=\""+imgURL+"cb.gif\" Name=\"aa\" border=0><Img Src=\""+imgURL+"cb.gif\" Name=\"ab\" border=0> дн. <Img Src=\""+imgURL+"cb.gif\" Name=\"a\" border=0><Img Src=\""+imgURL+"cb.gif\" Name=\"b\" border=0><Img Src=\""+imgURL+"colon.gif\" Name=\"c\" border=0><Img Src=\""+imgURL+"cb.gif\" Name=\"d\" border=0><Img Src=\""+imgURL+"cb.gif\" Name=\"e\" border=0><Img Src=\""+imgURL+"colon.gif\" Name=\"f\" border=0><Img Src=\""+imgURL+"cb.gif\" Name=\"g\" border=0><Img Src=\""+imgURL+"cb.gif\" Name=\"h\" border=0> <br><HR>";
// "<div class=\"content\"> "+
// "<div> "+
//if(ms.testAdmin(user)){
s +="Всего сервисов: "+Manager.getInstance().getServiceCount()+"<br> ";
s +="Запущеных сервисов: "+srv+"<br> ";
//}
//s +="Использовано памяти: " + formatMb(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())+"<br>"+
//"Свободной памяти: " + formatMb(Runtime.getRuntime().freeMemory())+"<br>"+
//"Всего памяти: " + formatMb(Runtime.getRuntime().totalMemory())+"<br>"+
//"</div> "+
//"</div> "+
//"<script type=\"text/javascript\" src=\"http://aska-bot.ru/lib/jquery.js\"></script> "+
//"<script type=\"text/javascript\" src=\"http://aska-bot.ru/lib/jquery.timers.js\"></script> "+
s +="</body>"+
"<!-- BODY END HERE --> "+
"</html>";
     con.print( s);

        return null;
    }
 private String formatMb(long bytes) {
   float megaBytes = bytes;
     megaBytes /= 1048576;
     //megaBytes /= 1024;
  int round = Math.round(megaBytes * 100);

     megaBytes = round;
     megaBytes /= 100;
return megaBytes + " Mb";
}
    /**
     * Определение времени запуска бота
     */
    private long getTimeStart(){
        long t = 0;
        try{
            File f = new File("./state");
            t = f.lastModified();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return t;
    }

    private long getUpTime(){
        return System.currentTimeMillis()-getTimeStart();
    }
}