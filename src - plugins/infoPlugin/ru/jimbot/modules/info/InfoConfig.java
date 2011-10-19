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

package ru.jimbot.modules.info;


import ru.jimbot.core.Password;
import ru.jimbot.core.UinConfig;
import ru.jimbot.core.api.ServiceConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Vector;

import ru.jimbot.core.api.ServiceConfigExtend;
import ru.jimbot.util.Log;
import ru.jimbot.util.UserPreference;

/**
 * @author Prolubnikov Dmitry
 */
public class InfoConfig implements ServiceConfig {
    public static final String FILE_NAME = "Info-config.xml";
    public String PROPS_FILE = "";
    public String PROPS_FOLDER = "";
    public static HashMap<String,InfoConfig> props = new HashMap<String,InfoConfig>();
    private String name = "InfoBot";
    private Vector<UinConfig> uins = new Vector<UinConfig>();
    private int xstatus = 0;
    private int maxInviteTime = 24;
//    private String authgroups = "user;poweruser;moder;admin";
    private Properties appProps;


    public InfoConfig() {
    }

    public InfoConfig(String name) {
        this.name = name;
    }

    public String patch() {
    	return "services/" + name + "/" + FILE_NAME;
    }


     public void save() {
    File file = new File(this.PROPS_FILE);
    File dir = new File(this.PROPS_FOLDER);
    try {
      if (!(dir.exists()))
        dir.mkdirs();
      FileOutputStream fo = new FileOutputStream(file);

      this.appProps.storeToXML(fo, "Info properties");
      fo.close();
      Log.getLogger(name).info("["+name+"] save preferences: Ok!");
    } catch (Exception ex) {
      ex.printStackTrace();
      Log.getLogger(name).error("["+name+"] not saving preferences!");
    }
  }


        public static InfoConfig getInstance(String name){
    	if(props.containsKey(name))
    		return props.get(name);
    	else {
               File file = new File("./services/"+name+"/"+FILE_NAME);
    		InfoConfig p = new InfoConfig();
                p.name=name;
    		p.PROPS_FILE = "./services/"+name+"/"+FILE_NAME;
    		p.PROPS_FOLDER = "./services/"+name;
    		p.setDefault();
    		if (file.exists())p.load();
    		props.put(name, p);
    		return p;
    	}
    }

      public final void load() {
       File file = new File(this.PROPS_FILE);
       setDefault();
         try {
             FileInputStream fi = new FileInputStream(file);
             this.appProps.loadFromXML(fi);
             fi.close();
             Log.getLogger(name).info("["+name+"] load preferences: Ok!");
         } catch (Exception ex) {
           ex.printStackTrace();
          Log.getLogger(name).error("["+name+"] not opening preferences!");
        }
     }

//********************************************//
    public void setDefault() {
        appProps = new Properties();
        setIntProperty("conn.uinCount",1);
        setStringProperty("conn.uin0","111");
        setStringProperty("conn.pass0","Password");
        setStringProperty("icq.status","32"/*Icq.STATUS_ONLINE*/);
        setStringProperty("icq.xstatus","14");
//        setIntProperty("icq.statusFlag",0);
        setIntProperty("bot.pauseIn",3000); //Пауза входящих сообщений
        setIntProperty("bot.pauseOut",500); //Пауза исходящих сообщений
        setIntProperty("bot.msgOutLimit",20); //Ограничение очереди исходящих сообщений
        setIntProperty("bot.pauseRestart",11*60*1000); //Пауза перед запуском упавшего коннекта
        setStringProperty("bot.adminUIN","111111;222222");
        setIntProperty("icq.AUTORETRY_COUNT",5);
        setStringProperty("icq.STATUS_MESSAGE1","Сообщение x-статуса 1");
        setStringProperty("icq.STATUS_MESSAGE2","Сообщение x-статуса 2");
        setBooleanProperty("bot.useAds",false);
        setIntProperty("bot.adsRate",3);
        setBooleanProperty("main.StartBot",false);

        setIntProperty("timeOut",10);

        setStringProperty("conn.protocol0","icq");
        setStringProperty("icq.statustxt","");

        setIntProperty("bot.MaxOutMsgSize",500);
        setIntProperty("bot.MaxOutMsgCount",5);

//        setIntProperty("chat.MaxOutMsgSize",500);
//        setIntProperty("chat.MaxOutMsgCount",5);



    }

    private String[][] icqStatus = {{"0","В сети"},{"268435456","Не в сети"},{"10","Занят"},
    {"4","Недоступен"},{"512","Невидим для всех"},{"256","Невидим"},{"2","Не беспокоить"},
    {"32","Готов поболтать"},{"1","Отошёл"},{"8193","Кушаю"},{"24576","Работа"},{"20480","Дома"},
    {"16384","Депрессия"},{"12288","Злой"}};

    private String[][] icqXstatus = {{"0","Нет"},{"1","Злой"},{"2","Купаюсь"},{"3","Устал"},
    {"4","Вечеринка"},{"5","Пиво"},{"6","Думаю"},{"7","Кушаю"},{"8","ТВ"},{"9","Друзья"},
    {"10","Кофе"},{"11","Слушаю музыку"},{"12","Дела"},{"13","Кино"},{"14","Весело"},
    {"15","Телефон"},{"16","Играю"},{"17","Учусь"},{"18","Магазины"},{"19","Болею"},{"20","Сплю"},
    {"21","Отрываюсь"},{"22","Интернет"},{"23","Работаю"},{"24","Печатаю"},{"25","Пикник"},
    {"26","Готовлю"},{"27","Курю"},{"28","Релакс"},{"29","Туалет"},{"30","Вопрос"},
    {"31","Дорога"},{"32","Любовь"},{"33","Поиск"},{"34","Дневник"}};


      public UserPreference[] getUserPreference(){
        UserPreference[] p = {
            new UserPreference(UserPreference.CATEGORY_TYPE,"anek", "Основные настройки",""),
       // new UserPreference(UserPreference.SPOILER_TYPE_ON,"SPOILER_ON", "Основные настройки",""),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"main.StartBot","Запускать анекдотный бот",getBooleanProperty("main.StartBot")),
        //new UserPreference(UserPreference.SPOILER_TYPE_OFF,"SPOILER_OFF", "-------------",""),
        new UserPreference(UserPreference.SPOILER_TYPE_ON,"SPOILER_ON", "Настройки статусов:",""),
            new UserPreference(UserPreference.SELECT_TYPE,"icq.status","ICQ статус",getStringProperty("icq.status"),icqStatus),
            new UserPreference(UserPreference.STRING_TYPE,"icq.statustxt","Текст ICQ статуса",getStringProperty("icq.statustxt")),
          //  new UserPreference(UserPreference.INTEGER_TYPE,"icq.xstatus","x-статус (0-34)",getIntProperty("icq.xstatus")),
            new UserPreference(UserPreference.SELECT_TYPE,"icq.xstatus","x-статус (0-34)",getStringProperty("icq.xstatus"),icqXstatus),
            new UserPreference(UserPreference.TEXTAREA_TYPE,"icq.STATUS_MESSAGE1","Сообщение x-статуса 1",getStringProperty("icq.STATUS_MESSAGE1")),
            new UserPreference(UserPreference.TEXTAREA_TYPE,"icq.STATUS_MESSAGE2","Сообщение x-статуса 2",getStringProperty("icq.STATUS_MESSAGE2")),
        new UserPreference(UserPreference.SPOILER_TYPE_OFF,"SPOILER_OFF", "-------------",""),
           // new UserPreference(UserPreference.CATEGORY_TYPE,"anek", "Настройки анекдотного бота",""),
            new UserPreference(UserPreference.INTEGER_TYPE,"icq.AUTORETRY_COUNT","Число переподключений движка при обрыве",getIntProperty("icq.AUTORETRY_COUNT")),
//            new UserPreference(UserPreference.STRING_TYPE,"icq.STATUS_MESSAGE","Сообщение статуса",getStringProperty("icq.STATUS_MESSAGE")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.pauseIn","Пауза для входящих сообщений",getIntProperty("bot.pauseIn")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.pauseOut","Пауза для исходящих сообщений",getIntProperty("bot.pauseOut")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.msgOutLimit","Ограничение очереди исходящих",getIntProperty("bot.msgOutLimit")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.MaxOutMsgSize","Максимальный размер одного исходящего сообщения",getIntProperty("bot.MaxOutMsgSize")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.MaxOutMsgCount","Максимальное число частей исходящего сообщения",getIntProperty("bot.MaxOutMsgCount")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.pauseRestart","Пауза перед перезапуском коннекта",getIntProperty("bot.pauseRestart")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.adminUIN","Админские UIN",getStringProperty("bot.adminUIN")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"bot.useAds","Использовать рекламу в боте",getBooleanProperty("bot.useAds")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.adsRate","Частота рекламы",getIntProperty("bot.adsRate")),
       
        };
        return p;
    }

    
    

   /**
    * кэш настроек для админки
    * @return
    */
    public LinkedHashMap<String, Object> getPreference(){
      LinkedHashMap p = new LinkedHashMap<String, UserPreference[]>();
//               p.put("Settings", getUserPreference());
//               p.put("Other_options", getOtherUserPreference());
//               p.put("System_messages", sysMsgPreference());
//               p.put("BotAdmin", botAdminPreference());
        return p;
     };

   /**
    * кэш настроек для админки
    * @return
    */
    public LinkedHashMap<String, ServiceConfigExtend> getHttpPreference(){
      LinkedHashMap p = new LinkedHashMap<String, ServiceConfigExtend>();
               p.put("Настройки", new ServiceConfigExtend("Settings", "info_srvs_props&pr=Settings", getUserPreference()));
//               p.put("Прочие настройки", new ServiceConfigExtend("Other_options", "chat_srvs_props&pr=Other_options", getOtherUserPreference()));
//               p.put("Системные сообщения", new ServiceConfigExtend("System_messages", "chat_srvs_props&pr=System_messages", sysMsgPreference()));
//               p.put("Полномочия", new ServiceConfigExtend(null, "chat_user_group_props", null));
        return p;
     };



    //****************************************//
    public boolean testAdmin(String screenName) {
         for(String s : getStringProperty("bot.adminUIN").split(";")) {
           if(screenName.equals(s)) return true;
        }
        return false;
    }


    public int uinCount() {
    return getIntProperty("conn.uinCount");
  }
    public void setUin(int i, String sn, String pass) {
        this.uins.get(i).setScreenName(sn);
        this.uins.get(i).setPass(new Password(pass));
         setStringProperty("conn.uin" + i, sn);
         setStringProperty("conn.pass" + i, pass);
    }

    public void addUin(String sn, String pass, String protocol) {
        uins.add(new UinConfig(sn, pass, protocol));
         int c = uinCount();
    setIntProperty("conn.uinCount", c + 1);
    setStringProperty("conn.uin" + c, sn);
    setStringProperty("conn.pass" + c, pass);
    setStringProperty("conn.protocol" + c, protocol);
    }

    public String getUin(int i) {
    return getStringProperty("conn.uin" + i);
    }

    public String getPass(int i) {
    // String pass=getStringProperty("conn.pass" + i);
     return getStringProperty("conn.pass" + i);
  }

    public String getProtocol(int i) {
    // String pass=getStringProperty("conn.pass" + i);
     return getStringProperty("conn.protocol" + i);
  }
    public void delUin(int i) {
        uins.remove(i);
           for (int ii = 0; ii < uinCount() - 1; ++ii)
      if (ii >= i) {
        setStringProperty("conn.uin" + i, getUin(ii + 1));
        setStringProperty("conn.pass" + i, getPass(ii + 1));
        setStringProperty("conn.protocol" + i, getProtocol(ii + 1));
      }
    this.appProps.remove("conn.uin" + (uinCount() - 1));
    this.appProps.remove("conn.pass" + (uinCount() - 1));
    this.appProps.remove("conn.protocol" + (uinCount() - 1));
    setIntProperty("conn.uinCount", uinCount() - 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<UinConfig> getUins() {
        this.uins = new Vector<UinConfig>();
         for (int ii = 0; ii < uinCount(); ++ii){
          uins.add(new UinConfig(getStringProperty("conn.uin" + ii),
                  getStringProperty("conn.pass" + ii),
                  getStringProperty("conn.protocol" + ii)));
         }
        return uins;

    }


    public void setUins(Vector<UinConfig> uins) {
       for(int i=0;i<uins.size();i++) {
           setStringProperty("conn.uin" + i, uins.get(i).getScreenName());
           setStringProperty("conn.pass" + i, uins.get(i).getPass().getPass());
           setStringProperty("conn.protocol" + i, uins.get(i).getProtocol());
      }
           setIntProperty("conn.uinCount", uins.size());
        this.uins = uins;
    }

    public void delAllUin()
  {
    this.uins = new Vector<UinConfig>();
    for (int i = 0; i < uinCount(); ++i){
    this.appProps.remove("conn.uin" + i);
    this.appProps.remove("conn.pass" + i);
    this.appProps.remove("conn.protocol" + i);
    }
    setIntProperty("conn.uinCount", 0);
  }

    public void registerProperties(Properties _appProps) {
    this.appProps = _appProps;
    }
    public int getStatus() {
       return Integer.parseInt(getStringProperty("icq.status"));
    }

    public void setStatus(int status) {
        setStringProperty("icq.status",String.valueOf(xstatus));
    }

    public String getStatustxt() {
        return getStringProperty("icq.statustxt");
    }

    public void setStatustxt(String statustxt) {
        setStringProperty("icq.statustxt",statustxt);
    }

    public int getXstatus() {
        return  Integer.parseInt(getStringProperty("icq.xstatus"));
    }

    public void setXstatus(int xstatus) {
         setStringProperty("icq.xstatus",String.valueOf(xstatus));
    }

    public String getXstatustxt1() {
        return getStringProperty("icq.STATUS_MESSAGE1");
    }

    public void setXstatustxt1(String xstatustxt1) {
        setStringProperty("icq.STATUS_MESSAGE1",xstatustxt1);
    }

    public String getXstatustxt2() {
         return getStringProperty("icq.STATUS_MESSAGE2");
    }

    public void setXstatustxt2(String xstatustxt2) {
         setStringProperty("icq.STATUS_MESSAGE2",xstatustxt2);
    }

    public int getPauseIn() {
        return getIntProperty("bot.pauseIn"); //Пауза входящих сообщений
    }

    public void setPauseIn(int pauseIn) {
        setIntProperty("bot.pauseIn",pauseIn);
    }

    public int getPauseOut() {
        return  getIntProperty("bot.pauseOut"); //Пауза исходящих сообщений
    }

    public void setPauseOut(int pauseOut) {
         setIntProperty("bot.pauseOut",pauseOut);
    }

    public int getMsgOutLimit() {
        return getIntProperty("bot.msgOutLimit"); //Ограничение очереди исходящих сообщений
    }

    public void setMsgOutLimit(int msgOutLimit) {
        setIntProperty("bot.msgOutLimit",msgOutLimit);
    }

    public long getPauseRestart() {
        return  getIntProperty("bot.pauseRestart"); //Пауза перед запуском упавшего коннекта
    }

    public void setPauseRestart(long pauseRestart) {
         setIntProperty("bot.pauseRestart",(int) pauseRestart);
    }

    public String getAdminUin() {
         return getStringProperty("bot.adminUIN");
    }

    public String[] getAdminUins() {
          return getStringProperty("bot.adminUIN").split(";");
    }

    public void setAdminUin(String adminUin) {
    //    this.adminUin = adminUin;
         setStringProperty("bot.adminUIN",adminUin);
    }

    public boolean isUseAds() {
        return getBooleanProperty("bot.useAds");
    }

    public void setUseAds(boolean useAds) {
        setBooleanProperty("bot.useAds",useAds);
    }

    public int getAdsRate() {
        return getIntProperty("bot.adsRate");
    }

    public void setAdsRate(int adsRate) {
        setIntProperty("bot.adsRate",adsRate);
    }

    public boolean isAutoStart() {
        return getBooleanProperty("main.StartBot");
    }

    public void setAutoStart(boolean autoStart) {
          setBooleanProperty("main.StartBot",autoStart);
    }

    public int getMaxOutMsgSize() {
        return  getIntProperty("bot.MaxOutMsgSize");
    }

    public void setMaxOutMsgSize(int maxOutMsgSize) {
          setIntProperty("bot.MaxOutMsgSize",maxOutMsgSize);
    }

    public int getMaxOutMsgCount() {
        return  getIntProperty("bot.MaxOutMsgCount");
    }

    public void setMaxOutMsgCount(int maxOutMsgCount) {
         setIntProperty("bot.MaxOutMsgCount",maxOutMsgCount);
    }

//    public boolean getWriteAllMsgs() {
//        return   getBooleanProperty("chat.writeAllMsgs");
//    }
//
//    public void setWriteAllMsgs(boolean writeAllMsgs) {
//         setBooleanProperty("chat.writeAllMsgs",writeAllMsgs);
//    }


//    public int getMaxInviteTime() {
//        return maxInviteTime;
//    }
//
//    public void setMaxInviteTime(int MaxInviteTime) {
//        this.maxInviteTime = MaxInviteTime;
//    }
//
//    public String getAuthgroups() {
//        return getStringProperty("auth.groups");
//    }
//
//    public void setAuthgroups(String authgroups) {
//        setStringProperty("auth.groups",authgroups);
//    }


  public String getProperty(String key) {
    return this.appProps.getProperty(key);
  }

  public String getStringProperty(String key) {
    return this.appProps.getProperty(key);
  }

  public String getProperty(String key, String def) {
    return this.appProps.getProperty(key, def);
  }

  public void setProperty(String key, String val) {
    this.appProps.setProperty(key, val);
  }

  public void setStringProperty(String key, String val) {

    this.appProps.setProperty(key, val);
  }

  public void setIntProperty(String key, int val) {
    this.appProps.setProperty(key, Integer.toString(val));
  }

  public void setBooleanProperty(String key, boolean val) {
    this.appProps.setProperty(key, (val) ? "true" : "false");
  }

  public int getIntProperty(String key) {
    return Integer.parseInt(this.appProps.getProperty(key));
  }

  public boolean getBooleanProperty(String key) {
    return Boolean.valueOf(this.appProps.getProperty(key)).booleanValue();
  }

  public Properties getProps() {
    return this.appProps;
  }

}
