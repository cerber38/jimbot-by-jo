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
import ru.jimbot.util.UserPreference;

/**
 * Хранение данных
 *
 *
 */
public class Users {
  public long dt;
  public long lastErrLogin;
  public int loginErrCount;
  public String UserName;
  public String UserPass;

	/**
         *
         * @param _lastErrLogin
         * @param _loginErrCount
         * @param _UserName
         */
	public Users(String _UserName,String _UserPass) {

         dt= System.currentTimeMillis();
         lastErrLogin=dt;
         loginErrCount=0;
         UserName=_UserName;
         UserPass=_UserPass;
     	}


    public boolean checkSession(String id) {
      boolean f = System.currentTimeMillis() - this.dt < MainConfig.getInstance().getIntProperty("http.delay") * 60000;
      this.dt = System.currentTimeMillis();
     return (f);

  }
}
