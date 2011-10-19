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

package ru.jimbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;
import ru.jimbot.core.Password;


/**
 * пользовательские настройки
 * @author ~J0-MA-JO~
 */

public class UserConfig {

    public String PROPS_FILE = "";
    public static String PROPS_FOLDER = "./users";
    private Properties appProps;
    public static HashMap<String,UserConfig> props = new HashMap<String,UserConfig>();

    public UserConfig() {
    }

    public UserConfig(String name) {
       this.PROPS_FILE = name;
    }


    public static UserConfig getInstance(String name){
       if(props.containsKey(name))
    		return props.get(name);
    	else {
                File file = new File(PROPS_FOLDER+"/"+name+".xml");
    		UserConfig config = new UserConfig();
                config.PROPS_FILE = name;
      		config.setDefault();
      		if (file.exists())
                    config.load();
                   props.put(name, config);
                   return config;
    	}
    }


    public final void load() {
        File file = new File(PROPS_FOLDER+"/"+PROPS_FILE+".xml");
        setDefault();
        try {
            FileInputStream fi = new FileInputStream(file);
            appProps.loadFromXML(fi);
            fi.close();
       //     System.out.println("Load user preferences ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error opening user preferences: "+ex);
        }
    }

    public final void save() {
        File file = new File(PROPS_FOLDER+"/"+PROPS_FILE+".xml");
        File dir = new File(PROPS_FOLDER);
        try {
           if (!(dir.exists()))
                     dir.mkdirs();
              FileOutputStream fo = new FileOutputStream(file);
            appProps.storeToXML(fo, "jImBot user properties");
            fo.close();
            System.out.println("Save user preferences ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error saving user preferences: "+ex);
        }
    }


    public final void setDefault() {
        appProps = new Properties();
        setStringProperty("srvUser",""); // сервисы пользователя
        setStringProperty("srvPropsUser",""); // настройки сервисов пользователя
        setStringProperty("httpPass",""); // пароль для доступа в админку

    }

    public String[] getUserSrv(){
    	return getStringProperty("srvUser").split(";");
    }

    public void setUserSrv(String[] srv){
        String s="";
        for(int i=0;i<srv.length;i++)s+=srv[i]+";";
        s = s.substring(0, s.length()-1);
    	setStringProperty("srvUser",s);
    }

    public void setUserSrv(String srv){
    	setStringProperty("srvUser",srv);
    }

    public String[] getUserProps(){
    	return getStringProperty("srvPropsUser").split(";");
    }

    public void setUserProps(String[] srv){
    	String s="";
        for(int i=0;i<srv.length;i++)s+=srv[i]+";";
        s = s.substring(0, s.length()-1);
    	setStringProperty("srvPropsUser",s);
    }

    public void setUserProps(String srv){
    	setStringProperty("srvPropsUser",srv);
    }

    public String getHttpUser() {
        return this.PROPS_FILE;
    }

    public String getHttpPass() {
        return getStringProperty("httpPass");
    }

    public void setHttpPass(Password httpPass) {
        setStringProperty("httpPass",httpPass.getPass());
    }

    public void registerProperties(Properties _appProps) {
        appProps = _appProps;
    }

    public String getStringProperty(String key) {
        return appProps.getProperty(key);
    }

    public String getProperty(String key, String def) {
        return appProps.getProperty(key,def);
    }

    public void setProperty(String key, String val) {
        appProps.setProperty(key,val);
    }

    public void setStringProperty(String key, String val) {
        appProps.setProperty(key,val);
    }

    public void setIntProperty(String key, int val) {
        appProps.setProperty(key,Integer.toString(val));
    }

    public void setBooleanProperty(String key, boolean val) {
        appProps.setProperty(key, val ? "true":"false");
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(appProps.getProperty(key));
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.valueOf(appProps.getProperty(key)).booleanValue();
    }
}
