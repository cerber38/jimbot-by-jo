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

import java.io.IOException;
import javax.servlet.http.Cookie;

/**
 * Выводит базовую страницу сервлета
 *
 * @author ~Jo-MA-Jo~
 */
public class Main extends MainPageServletActions {

    public String perform(HttpConnection con) throws IOException {

           String name = con.get("name");
           String password = con.get("password");
           if (!MainConfig.getInstance().testUserAuth(name, password)){
             return"\""+con.getURI()+"?page=error&id=11&ret=login\"";
        //  return "<meta http-equiv=\"refresh\" content=\"1; url=\""+con.getURI()+"?page=index&id=11"+"\">" ;

           }else{
            String uid = con.request.getHashCookies().get("uid").getValue();
            System.out.println("Main "+con.request.getRemoteAddr());
            users.put(uid, new Users(name,password));
            //con.response.addCookie(new Cookie("uid",uid));
            System.out.println("addCookie uid = "+uid);
              return con.getURI()+"?page=index target=mainFrameset";
            //  return"\""+con.getURI()+"?page=index\"";
           }
     
    }
}