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

package ru.jimbot.core.api;

import java.util.LinkedHashMap;
import ru.jimbot.core.UinConfig;
import java.util.Vector;
import ru.jimbot.util.UserPreference;

/**
 * Интерфейс для конфига сервисов
 * @author Prolubnikov Dmitry
 */
public interface ServiceConfig {

    /**
     * Сохранить настройки в файл
     */
	public void save();

    /**
     * Автоматически запускать сервис при старте приложения?
     * @return
     */
	public boolean isAutoStart();


    public boolean testAdmin(String screenName);

    public String[] getAdminUins();
    public Vector<UinConfig> getUins();
    public void setUin(int i, String sn, String pass);
    public void addUin(String sn, String pass, String protocol);
    public void delUin(int i);
    public void delAllUin();
    public int getPauseOut();
    public int getMsgOutLimit();
    public int getMaxOutMsgSize();
    public int getMaxOutMsgCount();
    public int getPauseIn();

   public LinkedHashMap<String, Object> getPreference();
   public LinkedHashMap<String, ServiceConfigExtend> getHttpPreference();
   public String getProperty(String key);
   public String getStringProperty(String key);
   public String getProperty(String key, String def);
   public void setProperty(String key, String val);
   public void setStringProperty(String key, String val);
   public void setIntProperty(String key, int val);
   public void setBooleanProperty(String key, boolean val);
   public int getIntProperty(String key);
   public boolean getBooleanProperty(String key);




}