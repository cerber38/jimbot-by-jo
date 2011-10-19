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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.Cookie;

/**
 * Сервлет основной страницы веб-админки
 *
 */
public class MainPageServlet extends HttpServlet {
//    protected ActionFactory factory = new ActionFactory();

    Class self;
    Class[] methodParamTypes;
  
    
    public MainPageServlet() {
        super();
    }
  private boolean start =false;

  public void init() throws ServletException
  {
    this.self = getClass();
    this.methodParamTypes = new Class[1];
   // String userID = MainPropertiesAction.getSessionId();
    try {
      this.methodParamTypes[0] = Class.forName("ru.jimbot.modules.http.HttpConnection");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new ServletException(e.getMessage());
    }
    System.out.println("init MainPage");
  }
  
       @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
    	doGetOrPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
    	doGetOrPost(request, response);
    }

    
    public void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html; charset=\"utf-8\"");
        HttpConnection con = new HttpConnection(request, response);
          	String page = request.getParameter("page");
          if(page==null||!con.request.getHashCookies().containsKey("uid")||!start){
              String uid = SrvUtil.getSessionId();
              con.response.addCookie(new Cookie("uid",uid));
              page = "login";
              start =true;
          }                
        Action action =new ActionFactory().create(page);
        String url = action.perform(con);
        
        if (url != null) 
        if (url.indexOf("target")!=-1)              
              con.print(" <script language=\"JavaScript\">"+
                          " window.top.location.href = \""+url.split("target=")[0]+"\" </script>");
//                        "window.top.frames['"+url.split("target=")[1]+"'].location.href = \""+url.split("target=")[0]+"\" </script>");
        else
              con.print(" <script language=\"JavaScript\">"+
                          " window.location.href = "+url+" </script>");    
        con.send();
    }
/*
        http://javascript.about.com/library/bltarget.htm
        <a href="page.htm" target="_top">
        top.location.href = 'page.htm'; 
        
        <a href="page.htm" target="thatframe"> 
        top.frames['thatframe'].location.href = 'page.htm';

Зарезервированные имена фрэймов служат для разрешения специальных ситуаций. Все они начинаются со знака подчеруивания. Любые другие имена фрэймов, начинающиеся с подчеркивания будут игнорироваться броузером.
TARGET="_blank"
Данное значение определяет, что документ, полученный по ссылке будет отображаться в новом окне броузера.

TARGET="_self"
Данное значение определяет, что документ, полученный по ссылке будет отображаться в том же фрэйме, в котором находится ссылка. Это имя удобно для переопределения окна назначения, указанного ранее в тэге BASE.

TARGET="_parent"
Данное значение определяет, что документ, полученный по ссылке будет отображаться в родительском окне, вне зависимости от параметров FRAMESET. Если родительского окна нет, то данное имя аналогично "_self".

TARGET="_top"
Данное значение определяет, что документ, полученный по ссылке будет отображаться на всей поверхности окна, вне зависимости от наличия фрэймов. Использование данного параметра удобно в случае вложенных фрэймов.
*/
  
}
