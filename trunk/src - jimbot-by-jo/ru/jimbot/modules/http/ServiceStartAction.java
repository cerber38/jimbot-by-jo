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

import ru.jimbot.Manager;

import java.io.IOException;

/**
 * ������ ������� � �������� ������
 */
public class ServiceStartAction extends MainPageServletActions {
    public String perform(HttpConnection con) throws IOException {
        String ns = con.get("ns"); // ��� �������
    	if(!Manager.getInstance().getServiceNames().contains(ns)){
    		//return "/main?page=error&id=0&ret=index&target=mainFrameset";
              return"\""+con.getURI()+"?page=error&id=0&ret=index\"";
    	}
        Manager.getInstance().start(ns);
        //return "/main?page=message&id=1&ret=index&target=mainFrameset";
              return"\""+con.getURI()+"?page=message&id=1&ret=index\"";
    }
}