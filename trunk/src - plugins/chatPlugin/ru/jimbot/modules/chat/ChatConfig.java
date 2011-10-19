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

package ru.jimbot.modules.chat;


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
public class ChatConfig implements ServiceConfig {
    public static final String FILE_NAME = "Chat-config.xml";
    public String PROPS_FILE = "";
    public String PROPS_FOLDER = "";
    public static HashMap<String,ChatConfig> props = new HashMap<String,ChatConfig>();
    private String name = "ChatBot";
    private Vector<UinConfig> uins = new Vector<UinConfig>();
//    private int status = 0;
//    private String statustxt = "";
    private int xstatus = 0;
//    private String xstatustxt1 = "";
//    private String xstatustxt2 = "";
//    private int pauseIn = 3000;
//    private int pauseOut = 500;
//    private int msgOutLimit = 20;
//    private long pauseRestart = 11*60*1000;
 //   private String adminUin = "111111;222222";
    private boolean useAds = false;
    private int adsRate = 3;
//    private boolean autoStart = false;
//    private int maxOutMsgSize = 500;
//    private int maxOutMsgCount = 5;
//    private boolean writeAllMsgs = true;
    private int maxInviteTime = 24;
//    private String authgroups = "user;poweruser;moder;admin";
    private Properties appProps;


    public ChatConfig() {
    }

    public ChatConfig(String name) {
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

      this.appProps.storeToXML(fo, "Chat properties");
      fo.close();
      Log.getLogger(name).info("["+name+"] save preferences: Ok!");
    } catch (Exception ex) {
      ex.printStackTrace();
      Log.getLogger(name).error("["+name+"] not saving preferences!");
    }
  }


        public static ChatConfig getInstance(String name){
    	if(props.containsKey(name))
    		return props.get(name);
    	else {
               File file = new File("./services/"+name+"/"+FILE_NAME);
    		ChatConfig p = new ChatConfig();
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
        setStringProperty("conn.protocol0","icq");
        setIntProperty("chat.pauseOut",5000);
        setBooleanProperty("chat.IgnoreOfflineMsg",true);
        setIntProperty("chat.TempKick",10); //Временный кик, минут
        setIntProperty("chat.ChangeStatusTime",60000);
        setIntProperty("chat.ChangeStatusCount",5);
        setBooleanProperty("chat.FreeReg",true); //регистрация новых пользователей без ограничений
        setIntProperty("chat.MaxMsgSize",150); //Максимальный размер одного сообщения от пользователя
        setIntProperty("chat.MaxOutMsgSize",500);
        setIntProperty("chat.MaxOutMsgCount",5);
        setStringProperty("icq.status","32"/*Icq.STATUS_ONLINE*/);
        setStringProperty("icq.xstatus","14");
        setStringProperty("icq.STATUS_MESSAGE1","Сообщение x-статуса 1");
        setStringProperty("icq.STATUS_MESSAGE2","Сообщение x-статуса 2");
        setIntProperty("icq.client",30);
//        setIntProperty("icq.statusFlag",0);
        setBooleanProperty("main.StartBot",false);
        setIntProperty("bot.pauseIn",3000); //Пауза входящих сообщений
        setIntProperty("bot.pauseOut",500); //Пауза исходящих сообщений
        setIntProperty("bot.msgOutLimit",20); //Ограничение очереди исходящих сообщений
        setIntProperty("bot.pauseRestart",11*60*1000); //Пауза перед запуском упавшего коннекта
        setStringProperty("bot.adminUIN","111111;222222");
        setStringProperty("msg.adminuUIN","111111;222222");
        setIntProperty("chat.autoKickTime",60);
        setIntProperty("chat.autoKickTimeWarn",58);
        setIntProperty("icq.AUTORETRY_COUNT",5);
        setBooleanProperty("chat.ignoreMyMessage", true);
        setBooleanProperty("chat.isAuthRequest", false);
        setStringProperty("chat.badNicks","admin;админ");
        setIntProperty("chat.defaultKickTime",5);
        setIntProperty("chat.maxKickTime",300);
        setIntProperty("chat.maxNickLenght",10);
        setBooleanProperty("chat.showChangeUserStatus",true);
        setBooleanProperty("chat.writeInMsgs",false);
        setBooleanProperty("chat.writeAllMsgs",true);
        
        setBooleanProperty("adm.useAdmin",true);
        setBooleanProperty("adm.useMatFilter",true);
        setBooleanProperty("adm.useSayAdmin",true);
        setStringProperty("adm.matString","бля;хуй;хуя;хуе;хуё;хуи;хули;пизд;сук;суч;ублюд;сволоч;гандон;ебат;ебет;ибат;ебан;ебал;ибал;пидар;пидор;залуп;муда;муди");
        setStringProperty("adm.noMatString","рубл;нибал;абля;обля;оскорбля;шибал;гибал;хулига;требля;скреба;скребе;страх;стеб;хлеб;скипидар;любля;барсук");
        setIntProperty("adm.getStatTimeout",15);
        setIntProperty("adm.maxSayAdminCount",5);
        setIntProperty("adm.maxSayAdminTimeout",10);
        setIntProperty("adm.sayAloneTime",15);
        setIntProperty("adm.sayAloneProbability",20);
        setStringProperty("adm.nick","Админ");
        setStringProperty("adm.alt_nick","admin;админ");
        setStringProperty("adm.warning","<NICK> еще слово и полетишь!]:->");
        
        
        setStringProperty("auth.groups","user;poweruser;moder;admin");
        setStringProperty("auth.group_prist_user","");//приставка для группы
        setStringProperty("auth.group_prist_poweruser","{vip}");//приставка для группы
        setStringProperty("auth.group_prist_moder","{m}");//приставка для группы
        setStringProperty("auth.group_prist_admin","{a}");//приставка для группы
        setStringProperty("auth.group_user","pmsg;reg;invite;adminsay;adminstat;room;anyroom");
        setStringProperty("auth.group_poweruser","pmsg;reg;invite;adminsay;adminstat;room;anyroom");
        setStringProperty("auth.group_moder","pmsg;reg;invite;adminsay;adminstat;kickone;settheme;exthelp;whouser;room;dblnick;anyroom;wroom");
        setStringProperty("auth.group_admin","pmsg;reg;invite;adminsay;adminstat;kickone;kickall;ban;settheme;info;exthelp;authread;whouser;room;kickhist;whoinv;chgkick;dblnick;anyroom;wroom");
        setIntProperty("chat.MaxInviteTime",24);
        setBooleanProperty("chat.NoDelContactList",false);
        setIntProperty("chat.maxUserOnUin",7);
        setStringProperty("chat.badSymNicks","");
        setStringProperty("chat.goodSymNicks","");
        setStringProperty("chat.delimiter",":");
        setStringProperty("chat.inviteDescription","Для регистрации в чате вам необходимо получить приглашение одного из пользователей.");
        setIntProperty("chat.floodCountLimit",5);
        setIntProperty("chat.floodTimeLimit",10);
        setIntProperty("chat.floodTimeLimitNoReg",5);
//        setStringProperty("db.host","localhost:3306");
//        setStringProperty("db.user","root");
//        setStringProperty("db.pass","");
//        setStringProperty("db.dbname","botdb");
        setBooleanProperty("chat.useCaptcha", false);
        setBooleanProperty("chat.isUniqueNick", false);
        setIntProperty("chat.maxNickChanged",99);
        setBooleanProperty("chat.isShowKickReason", false);
        setBooleanProperty("pokaz", false);
//        setStringProperty("bot.antikick_UIN","111;333");
//        setStringProperty("bot.antiban_UIN","111;333");
//        setStringProperty("bot.antikick_MSG","АНТИКИК! *NO*");//Сообщение юзеру при антикике
//        setStringProperty("bot.antiban_MSG","АНТИБАН! *NO*");//Сообщение юзеру при антибане
        setStringProperty("bot.user_outchat","Вы вышли из чата!");//Сообщение юзеру при выходе из чата
        setStringProperty("bot.user_outchat","Вы вышли из чата!");//Сообщение юзеру при выходе из чата
        setStringProperty("bot.potok_outchat","вышел(а) из чата");//Сообщение в поток при выходе юзера из чата
        setStringProperty("bot.user_inchat","Вы вошли в чат");//Сообщение юзеру при входе в чат
        setStringProperty("bot.potok_inchat","вошёл(а) в чат");//Сообщение в поток при входе юзера
        setStringProperty("bot.autoriz","Что бы чат видел ваш статус - авторизируйте его");//Запрос авторизации
        setStringProperty("bot.privetstvie_1","Добро пожаловать в наш чат!");//Приветствие(строка 1)
        setStringProperty("bot.privetstvie_1_1","Для регистрации отправьте: !ник ваш_ник");//Приветствие(строка 1.1)
        setStringProperty("bot.privetstvie_1_2","Пример: !ник Пупсик");//Приветствие(строка 1.2)
        setStringProperty("bot.privetstvie_1_3","Для входа в чат используйте команду: !чат");//Приветствие(строка 1.3)
        setStringProperty("bot.privetstvie_2","Для помощи отправьте команду: !справка");//Приветствие(строка 2)
        setStringProperty("bot.privetstvie_8","status");//Приветствие(строка 2)
        setStringProperty("bot.privetstvie_9","Реклама1;Реклама2;Реклама3;Реклама4");//Приветствие(строка 9)
        setStringProperty("bot.proschanie","Реклама1;Реклама2;Реклама3;Реклама4");//реклама при выходе
        setStringProperty("bot.user_inreg","Регистрация завершена, вход в чат по команде: !вход");//реклама при выходе
        setStringProperty("bot.potok_inreg","Зарегистрировался пользователь");//реклама при выходе
        //setStringProperty("bot.blok","Вход заблокирован! Обращайтесь к администратору чата!");//сообщение при блокировке чата

        setIntProperty("vik.ball",5);
        setBooleanProperty("ads.useXT", true);
        setIntProperty("vik.sek",30);
        setIntProperty("vik.room",1);
        setStringProperty("vik.noAnswer","*PARDON* Подсказок больше нет! :-|");
     //  setIntProperty("butilka.room",0);
//        setIntProperty("zags.room",11);
        setIntProperty("ruletka",5);
     //   setIntProperty("ruletka.bal",2);
     //   setIntProperty("ruletka.room",0);
     //   setIntProperty("ruletka.kick",20);
      //  setBooleanProperty("getruletka", false);
        setIntProperty("chat.xt", 5);
        setStringProperty("vik.tabl","viktorina");
        setStringProperty("money","монет(а)");
        setStringProperty("bot.not_messeges","Неверная команда. Для вывода списка команд наберите: !справка");//Вывод сообщениея если команда неверна
      //  setStringProperty("Oplata.UIN","123");
//        setStringProperty("smile","]:->  :-*  8-)  *CRAZY*  *IN LOVE*  :-)");
//        //Магазин
//        setIntProperty("magas.moderation",1000);
//        setIntProperty("magas.admin",2000);
//        setIntProperty("magas.rooms",1000);
//        setIntProperty("magas.kick",8000);
//        setIntProperty("magas.ban",100000);
//        setIntProperty("magas.smile",100);
//        setIntProperty("magas.room",6);
//        setBooleanProperty("magas.uin",false);
//        setBooleanProperty("magas.smile_chek",false);
//        setIntProperty("magas.uin_6",6000);
//        setIntProperty("magas.uin_7",3000);
//        setIntProperty("magas.uin_8",1500);
//        setIntProperty("magas.uin_9",500);
        setIntProperty("uin.private",1233456);
      //  setBooleanProperty("spisok_room", false);
      //  setBooleanProperty("dostup_osn", true);
      //  setBooleanProperty("vhod_ch", false);
      //  setBooleanProperty("grant_ch", false);
        setBooleanProperty("vse.mob", true);
    //    setIntProperty("room.money",0);
       // setIntProperty("min.money",1);
        setIntProperty("room_v",0);
    //    setIntProperty("max.room_vik",7);
    //    setIntProperty("max.wrong_vik",10);
    //    setIntProperty("max.min_vik",3);
        setStringProperty("keykey","");
        setStringProperty("adm_nick","Администратор");

//        setBooleanProperty("millioner.on.off", false);
//        setIntProperty("millioner.room", 1000 );
//        setIntProperty("millioner.game.in.day", 3 );
//        setIntProperty("millioner.question.time", 3 );
//        setIntProperty("millioner.not.consumed.1", 50 );
//        setIntProperty("millioner.not.consumed.2", 100 );
//        setIntProperty("millioner.not.consumed.3", 150 );
//        setIntProperty("goroda_time", 3 );
//        setIntProperty("goroda_ball", 5 );
//        setIntProperty("goroda_room", 0 );
//        setIntProperty("kr_time", 3 );
//        setIntProperty("kr_ball", 5 );
//        setIntProperty("kr_room", 0 );
//        setIntProperty("anek.sek", 30);
//        setIntProperty("anek.room", 14);
     //   setBooleanProperty("bot.OnlyReg", false);
     //   setIntProperty("bot.pauseOnlyReg", 3);
        setIntProperty("timeOut",10);

        setStringProperty("icq.statustxt","");
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
             new UserPreference(UserPreference.CATEGORY_TYPE,"main", "Основные настройки",""),
            new UserPreference(UserPreference.INTEGER_TYPE,"timeOut","Пауза между подключениями UINs сервиса",getIntProperty("timeOut")),
            //new UserPreference(UserPreference.BOOLEAN_TYPE,"dostup_osn","Ограничить доступ к настройкам",getBooleanProperty("dostup_osn")),
           // new UserPreference(UserPreference.BOOLEAN_TYPE,"spisok_room","Показывать список комнат при входе",getBooleanProperty("spisok_room")),
          // new UserPreference(UserPreference.BOOLEAN_TYPE,"vhod_ch","Блокировать вход при 0 балансе",getBooleanProperty("vhod_ch")),
          //  new UserPreference(UserPreference.BOOLEAN_TYPE,"grant_ch","Снимать полномочия до (user) про 0 балансе",getBooleanProperty("grant_ch")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"main.StartBot","Запускать чат-бот",getBooleanProperty("main.StartBot")),
            //new UserPreference(UserPreference.CATEGORY_TYPE,"bot", "Настройки бота",""),

        new UserPreference(UserPreference.SPOILER_TYPE_ON,"SPOILER_ON", "Настройки статусов:",""),
            new UserPreference(UserPreference.SELECT_TYPE,"icq.status","ICQ статус",getStringProperty("icq.status"),icqStatus),
            new UserPreference(UserPreference.STRING_TYPE,"icq.statustxt","Текст ICQ статуса",getStringProperty("icq.statustxt")),
          //  new UserPreference(UserPreference.INTEGER_TYPE,"icq.xstatus","x-статус (0-34)",getIntProperty("icq.xstatus")),
            new UserPreference(UserPreference.SELECT_TYPE,"icq.xstatus","x-статус (0-34)",getStringProperty("icq.xstatus"),icqXstatus),
            new UserPreference(UserPreference.TEXTAREA_TYPE,"icq.STATUS_MESSAGE1","Сообщение x-статуса 1",getStringProperty("icq.STATUS_MESSAGE1")),
            new UserPreference(UserPreference.TEXTAREA_TYPE,"icq.STATUS_MESSAGE2","Сообщение x-статуса 2",getStringProperty("icq.STATUS_MESSAGE2")),
        new UserPreference(UserPreference.SPOILER_TYPE_OFF,"SPOILER_OFF", "-------------",""),

            new UserPreference(UserPreference.CATEGORY_TYPE,"bot", "Настройки бота",""),             
          //  new UserPreference(UserPreference.STRING_TYPE,"bot.blok","Сообщение при блокировке чата",getStringProperty("bot.blok")),
            new UserPreference(UserPreference.INTEGER_TYPE,"icq.AUTORETRY_COUNT","Число переподключений движка при обрыве",getIntProperty("icq.AUTORETRY_COUNT")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.pauseIn","Пауза для входящих сообщений",getIntProperty("bot.pauseIn")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.pauseOut","Пауза для исходящих сообщений",getIntProperty("bot.pauseOut")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.msgOutLimit","Ограничение очереди исходящих",getIntProperty("bot.msgOutLimit")),
            new UserPreference(UserPreference.INTEGER_TYPE,"bot.pauseRestart","Пауза перед перезапуском коннекта",getIntProperty("bot.pauseRestart")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.adminUIN","Админские UIN",getStringProperty("bot.adminUIN")),
          //  new UserPreference(UserPreference.STRING_TYPE,"Oplata.UIN","UIN извещение о изменении баланса",getStringProperty("Oplata.UIN")),
        //     new UserPreference(UserPreference.CATEGORY_TYPE,"bot.antispam", "Настройки антиспама",""),
         //    new UserPreference(UserPreference.BOOLEAN_TYPE,"bot.OnlyReg","Реагировать только на зарегистрированных",getBooleanProperty("bot.OnlyReg")),
      //      new UserPreference(UserPreference.INTEGER_TYPE,"bot.maxFlood.msg","Максимальное кол-во одинаковых",getIntProperty("bot.maxFlood.msg")),
      //      new UserPreference(UserPreference.INTEGER_TYPE,"bot.pauseOnlyReg","Период отключения блокировки (мин.)",getIntProperty("bot.pauseOnlyReg")),
          // new UserPreference(UserPreference.CATEGORY_TYPE,"chat", "Настройки чата",""),

        new UserPreference(UserPreference.SPOILER_TYPE_ON,"SPOILER_ON", "Настройки чата:",""),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.floodCountLimit","Число повторов флуда",getIntProperty("chat.floodCountLimit")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.floodTimeLimit","Период флуда (сек)",getIntProperty("chat.floodTimeLimit")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.floodTimeLimitNoReg","Пауза сообщений для незареганых (сек)",getIntProperty("chat.floodTimeLimitNoReg")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.pauseOut","Задержка очереди чата",getIntProperty("chat.pauseOut")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.IgnoreOfflineMsg","Игнорировать оффлайн сообщения",getBooleanProperty("chat.IgnoreOfflineMsg")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.ignoreMyMessage","Игнорировать собственные сообщения в чате",getBooleanProperty("chat.ignoreMyMessage")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.TempKick","Временный кик (минут)",getIntProperty("chat.TempKick")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.ChangeStatusTime","Период переподключения юзера",getIntProperty("chat.ChangeStatusTime")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.ChangeStatusCount","Количество переподключений для блокировки юзера",getIntProperty("chat.ChangeStatusCount")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.FreeReg","Свободная регистрация",getBooleanProperty("chat.FreeReg")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.useCaptcha","Использовать CAPTCHA при регитсрации",getBooleanProperty("chat.useCaptcha")),
            new UserPreference(UserPreference.STRING_TYPE,"chat.inviteDescription","Пояснения по поводу приглашений в чат",getStringProperty("chat.inviteDescription")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.MaxInviteTime","Время действия приглашения (часов)",getIntProperty("chat.MaxInviteTime")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.MaxMsgSize","Максимальный размер одного сообщения",getIntProperty("chat.MaxMsgSize")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.MaxOutMsgSize","Максимальный размер одного исходящего сообщения",getIntProperty("chat.MaxOutMsgSize")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.MaxOutMsgCount","Максимальное число частей исходящего сообщения",getIntProperty("chat.MaxOutMsgCount")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.autoKickTime","Время автокика при молчании (минут)",getIntProperty("chat.autoKickTime")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.autoKickTimeWarn","Время предупреждения перед автокиком",getIntProperty("chat.autoKickTimeWarn")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.isAuthRequest","Запрашивать авторизацию у пользователей",getBooleanProperty("chat.isAuthRequest")),
            new UserPreference(UserPreference.STRING_TYPE,"chat.badNicks","Запрещенные ники",getStringProperty("chat.badNicks")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.maxNickChanged","Число смен ника за сутки",getIntProperty("chat.maxNickChanged")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.defaultKickTime","Время кика по умолчанию",getIntProperty("chat.defaultKickTime")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.maxKickTime","Максимальное время кика",getIntProperty("chat.maxKickTime")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.maxNickLenght","Максимальная длина ника в чате",getIntProperty("chat.maxNickLenght")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.isUniqueNick","Уникальные ники в чате",getBooleanProperty("chat.isUniqueNick")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.showChangeUserStatus","Показывать вход-выход при падении юзеров",getBooleanProperty("chat.showChangeUserStatus")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.writeInMsgs","Записывать все входящие сообщения в БД",getBooleanProperty("chat.writeInMsgs")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.writeAllMsgs","Записывать сообщения в БД (отключит статистику и т.п.)",getBooleanProperty("chat.writeAllMsgs")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.NoDelContactList","Не очищать контакт-лист",getBooleanProperty("chat.NoDelContactList")),
            new UserPreference(UserPreference.INTEGER_TYPE,"chat.maxUserOnUin","Максимум юзеров на 1 уин",getIntProperty("chat.maxUserOnUin")),
            new UserPreference(UserPreference.STRING_TYPE,"chat.badSymNicks","Запрещенные символы в никах",getStringProperty("chat.badSymNicks")),
            new UserPreference(UserPreference.STRING_TYPE,"chat.goodSymNicks","Разрешенные символы в никах",getStringProperty("chat.goodSymNicks")),
            new UserPreference(UserPreference.STRING_TYPE,"chat.delimiter","Разделитель после ника",getStringProperty("chat.delimiter")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"chat.isShowKickReason","Выводить нарушителю причину кика",getBooleanProperty("chat.isShowKickReason")),
        new UserPreference(UserPreference.SPOILER_TYPE_OFF,"SPOILER_OFF", "-------------",""),

        new UserPreference(UserPreference.SPOILER_TYPE_ON,"SPOILER_ON", "Настройки Админа:",""),                
          //  new UserPreference(UserPreference.CATEGORY_TYPE,"adm", "Настройки Админа",""),
             new UserPreference(UserPreference.STRING_TYPE,"adm.nick","Ник админа",getStringProperty("adm.nick")),
             new UserPreference(UserPreference.STRING_TYPE,"adm.alt_nick","Альтернативные ники",getStringProperty("adm.alt_nick")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"adm.useAdmin","Использовать Админа в чате",getBooleanProperty("adm.useAdmin")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"adm.useMatFilter","Разрешить реакцию на мат",getBooleanProperty("adm.useMatFilter")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"adm.useSayAdmin","Разрешить админу разговаривать",getBooleanProperty("adm.useSayAdmin")),
            new UserPreference(UserPreference.STRING_TYPE,"adm.matString","Слова для мата",getStringProperty("adm.matString")),
            new UserPreference(UserPreference.STRING_TYPE,"adm.noMatString","Слова исключения",getStringProperty("adm.noMatString")),
             new UserPreference(UserPreference.STRING_TYPE,"adm.warning","Текст предупреждения админа",getStringProperty("adm.warning")),          
            new UserPreference(UserPreference.INTEGER_TYPE,"adm.getStatTimeout","Пауза между показами статистики",getIntProperty("adm.getStatTimeout")),
            new UserPreference(UserPreference.INTEGER_TYPE,"adm.maxSayAdminCount","Максимум обращений к админу для одного человека",getIntProperty("adm.maxSayAdminCount")),
            new UserPreference(UserPreference.INTEGER_TYPE,"adm.maxSayAdminTimeout","Время сброса статистики обращений",getIntProperty("adm.maxSayAdminTimeout")),
            new UserPreference(UserPreference.INTEGER_TYPE,"adm.sayAloneTime","Время молчания, через которое админ заговорит",getIntProperty("adm.sayAloneTime")),
            new UserPreference(UserPreference.INTEGER_TYPE,"adm.sayAloneProbability","Вероятность разговора админа в тишине (1 к ...)",getIntProperty("adm.sayAloneProbability")),
        new UserPreference(UserPreference.SPOILER_TYPE_OFF,"SPOILER_OFF", "-------------",""),
             
             //     new UserPreference(UserPreference.CATEGORY_TYPE,"db", "Настройки mySQL",""),
        //    new UserPreference(UserPreference.STRING_TYPE,"db.host","Хост БД",getStringProperty("db.host")),
         //   new UserPreference(UserPreference.STRING_TYPE,"db.user","Пользователь",getStringProperty("db.user")),
         //   new UserPreference(UserPreference.PASS_TYPE,"db.pass","Пароль",getStringProperty("db.pass")),
          //  new UserPreference(UserPreference.STRING_TYPE,"db.dbname","Название базы данных",getStringProperty("db.dbname")),
              };
        return p;
    }

        public UserPreference[] getOtherUserPreference(){
        UserPreference[] p = {

       new UserPreference(UserPreference.SPOILER_TYPE_ON,"SPOILER_ON", "Настройки системных сообщений:",""), 
            //new UserPreference(UserPreference.CATEGORY_TYPE,"bot", "Настройки системных сообщений",""),
            new UserPreference(UserPreference.STRING_TYPE,"bot.user_inreg","Сообщение после регистрации в чате",getStringProperty("bot.user_inreg")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.potok_inreg","Сообщение в поток после регистрации в чате",getStringProperty("bot.potok_inreg")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.user_inchat","Сообщение при входе в чат",getStringProperty("bot.user_inchat")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.potok_inchat","Сообщение в поток при входе юзера",getStringProperty("bot.potok_inchat")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.user_outchat","Сообщение при выходе из чата",getStringProperty("bot.user_outchat")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.potok_outchat","Сообщение в поток при выходе юзера",getStringProperty("bot.potok_outchat")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.autoriz","Запрос авторизации",getStringProperty("bot.autoriz")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1","Приветствие(строка 1)",getStringProperty("bot.privetstvie_1")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1_1","Приветствие(строка 1.1)",getStringProperty("bot.privetstvie_1_1")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1_2","Приветствие(строка 1.2)",getStringProperty("bot.privetstvie_1_2")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1_3","Приветствие(строка 1.3)",getStringProperty("bot.privetstvie_1_3")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_2","Приветствие(строка 2)",getStringProperty("bot.privetstvie_2")),
            new UserPreference(UserPreference.TEXTAREA_TYPE,"bot.privetstvie_9","Реклама при отправке +а",getStringProperty("bot.privetstvie_9")),
            new UserPreference(UserPreference.TEXTAREA_TYPE,"bot.proschanie","Реклама при выходе из чата",getStringProperty("bot.proschanie")),
            new UserPreference(UserPreference.STRING_TYPE,"bot.not_messeges","Если команда не верна",getStringProperty("bot.not_messeges")),
        new UserPreference(UserPreference.SPOILER_TYPE_OFF,"SPOILER_OFF", "-------------",""),
           
            new UserPreference(UserPreference.CATEGORY_TYPE,"bot", "Разные настройки",""),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"vse.mob","+a,+aa версия для мобильника",getBooleanProperty("vse.mob")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"pokaz","Показывать ид возле ника",getBooleanProperty("pokaz")),
            new UserPreference(UserPreference.INTEGER_TYPE,"uin.private","Уин на который будут приходить все приваты",getIntProperty("uin.private")),
            new UserPreference(UserPreference.STRING_TYPE,"msg.adminuUIN","Уин на который будут приходить все сообщения для админа",getStringProperty("msg.adminuUIN")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.antikick_UIN","Антикик UIN",getStringProperty("bot.antikick_UIN")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.antiban_UIN","Антибан UIN",getStringProperty("bot.antiban_UIN")),
            new UserPreference(UserPreference.STRING_TYPE,"money","Денежная единица в чате",getStringProperty("money")),
           // new UserPreference(UserPreference.INTEGER_TYPE,"room.money","Комната для перевода с кошелька на кошелек",getIntProperty("room.money")),
         //   new UserPreference(UserPreference.INTEGER_TYPE,"min.money","Минимальная сумма перевода",getIntProperty("min.money")),
            new UserPreference(UserPreference.CATEGORY_TYPE,"xstatus", "Настройки xstatus",""),
            new UserPreference(UserPreference.BOOLEAN_TYPE, "ads.useXT", "Менять x-статус", Boolean.valueOf(getBooleanProperty("ads.useXT"))),
            new UserPreference(UserPreference.INTEGER_TYPE, "chat.xt",  "Интервал смены x-статуса", Integer.valueOf(getIntProperty("chat.xt"))),
          //  new UserPreference(UserPreference.CATEGORY_TYPE,"bot_games", "Игровые настройки",""),
//            new UserPreference(UserPreference.INTEGER_TYPE,"zags.room","Комната загс",getIntProperty("zags.room")),
         //   new UserPreference(UserPreference.INTEGER_TYPE,"butilka.room","Комната игра бутылочка",getIntProperty("butilka.room")),
         //   new UserPreference(UserPreference.CATEGORY_TYPE,"ruletka", "Настройки игра рулетка",""),
         //   new UserPreference(UserPreference.INTEGER_TYPE,"ruletka.room","Комната игра рулетка",getIntProperty("ruletka.room")),
         //   new UserPreference(UserPreference.INTEGER_TYPE,"ruletka","Максимальное значение в рулетке",getIntProperty("ruletka")),
         //   new UserPreference(UserPreference.INTEGER_TYPE,"ruletka.bal","Сумма при выигрыше",getIntProperty("ruletka.bal")),
        //    new UserPreference(UserPreference.INTEGER_TYPE,"ruletka.kick","Время кика в рулетке",getIntProperty("ruletka.kick")),
        //    new UserPreference(UserPreference.BOOLEAN_TYPE,"getruletka","Сумма выигрыша равна загаданному числу",getBooleanProperty("getruletka")),
//            new UserPreference(UserPreference.CATEGORY_TYPE,"anekdot", "Анекдотного бота",""),
//            new UserPreference(UserPreference.INTEGER_TYPE,"anek.room","Комната Анекдотного бота",getIntProperty("anek.room")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"anek.sek","Посылать анекдоты с интервалом (мин.значение 20 сек)",getIntProperty("anek.sek")),
            new UserPreference(UserPreference.CATEGORY_TYPE,"victorina", "Настройки викторина",""),
            new UserPreference(UserPreference.INTEGER_TYPE,"vik.ball","Баллов за правильный ответ",getIntProperty("vik.ball")),
            new UserPreference(UserPreference.INTEGER_TYPE,"vik.room","Комната викторина",getIntProperty("vik.room")),
            new UserPreference(UserPreference.INTEGER_TYPE,"vik.sek","Посылать вопросы с интервалом (мин.значение 20 сек)",getIntProperty("vik.sek")),
            new UserPreference(UserPreference.STRING_TYPE,"vik.noAnswer","Сообщение при отсутствии ответа",getStringProperty("vik.noAnswer")),
          //  new UserPreference(UserPreference.CATEGORY_TYPE,"million", "Настройки игры миллионер" ,""),
          //  new UserPreference(UserPreference.BOOLEAN_TYPE,"millioner.on.off","Включить/Выключить игру",getBooleanProperty("millioner.on.off")),
          //  new UserPreference(UserPreference.INTEGER_TYPE,"millioner.room","Комната игры",getIntProperty("millioner.room")),
          //  new UserPreference(UserPreference.INTEGER_TYPE,"millioner.game.in.day","Количество игр в сутки",getIntProperty("millioner.game.in.day")),
          //  new UserPreference(UserPreference.INTEGER_TYPE,"millioner.question.time","Время на один вопрос(минуты)",getIntProperty("millioner.question.time")),
          //  new UserPreference(UserPreference.INTEGER_TYPE,"millioner.not.consumed.1","Первая не сгораемая сумма",getIntProperty("millioner.not.consumed.1")),
           // new UserPreference(UserPreference.INTEGER_TYPE,"millioner.not.consumed.2","Вторая не сгораемая сумма",getIntProperty("millioner.not.consumed.2")),
           // new UserPreference(UserPreference.INTEGER_TYPE,"millioner.not.consumed.3","Третья не сгораемая сумма",getIntProperty("millioner.not.consumed.3")),
//            new UserPreference(UserPreference.CATEGORY_TYPE,"goroda", "Настройки игры Города",""),
//            new UserPreference(UserPreference.INTEGER_TYPE,"goroda_time","Время на раздумье",getIntProperty("goroda_time")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"goroda_ball","Кол-во $ при правильном ответе",getIntProperty("goroda_ball")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"goroda_room","Комната для игры в города",getIntProperty("goroda_room")),
//            new UserPreference(UserPreference.CATEGORY_TYPE,"krestiki", "Настройки игры Крестики нолики",""),
//            new UserPreference(UserPreference.INTEGER_TYPE,"kr_time","Время на раздумье",getIntProperty("kr_time")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"kr_ball","Кол-во $ при выигрыше",getIntProperty("kr_ball")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"kr_room","Комната для игры в крестики нолики",getIntProperty("kr_room")),
//            new UserPreference(UserPreference.CATEGORY_TYPE,"magasin", "Настройки Магазина",""),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.room","Комната Магазин",getIntProperty("magas.room")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.moderation","Стоимость звания модератора",getIntProperty("magas.moderation")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.admin","Стоимость звания администратора",getIntProperty("magas.admin")),
//           // new UserPreference(UserPreference.INTEGER_TYPE,"magas.rooms","Стоимость личной комнаты",getIntProperty("magas.rooms")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.kick","Стоимость права кикать",getIntProperty("magas.kick")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.ban","Стоимость права банить",getIntProperty("magas.ban")),
//            new UserPreference(UserPreference.BOOLEAN_TYPE,"magas.smile_chek", "Продажа смайлов",getBooleanProperty("magas.smile_chek")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.smile","Стоимость смайла в ник",getIntProperty("magas.smile")),
//            new UserPreference(UserPreference.STRING_TYPE,"smile","Смайлы на продажу (писать через двойной пробел)",getStringProperty("smile")),
//            new UserPreference(UserPreference.BOOLEAN_TYPE,"magas.uin", "Продажа uin_оф",getBooleanProperty("magas.uin")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.uin_6","6-сти знаки",getIntProperty("magas.uin_6")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.uin_7","7-ми знаки",getIntProperty("magas.uin_7")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.uin_8","8-ми знаки",getIntProperty("magas.uin_8")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"magas.uin_9","9-ти знаки",getIntProperty("magas.uin_9")),
       
        };
    return p;
  }
//        public UserPreference[] sysMsgPreference(){
//        UserPreference[] p = {
//            new UserPreference(UserPreference.CATEGORY_TYPE,"bot", "Настройки системных сообщений",""),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.user_inreg","Сообщение после регистрации в чате",getStringProperty("bot.user_inreg")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.potok_inreg","Сообщение в поток после регистрации в чате",getStringProperty("bot.potok_inreg")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.user_inchat","Сообщение при входе в чат",getStringProperty("bot.user_inchat")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.potok_inchat","Сообщение в поток при входе юзера",getStringProperty("bot.potok_inchat")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.user_outchat","Сообщение при выходе из чата",getStringProperty("bot.user_outchat")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.potok_outchat","Сообщение в поток при выходе юзера",getStringProperty("bot.potok_outchat")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.autoriz","Запрос авторизации",getStringProperty("bot.autoriz")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1","Приветствие(строка 1)",getStringProperty("bot.privetstvie_1")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1_1","Приветствие(строка 1.1)",getStringProperty("bot.privetstvie_1_1")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1_2","Приветствие(строка 1.2)",getStringProperty("bot.privetstvie_1_2")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_1_3","Приветствие(строка 1.3)",getStringProperty("bot.privetstvie_1_3")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.privetstvie_2","Приветствие(строка 2)",getStringProperty("bot.privetstvie_2")),
//            new UserPreference(UserPreference.TEXTAREA_TYPE,"bot.privetstvie_9","Реклама при отправке +а",getStringProperty("bot.privetstvie_9")),
//            new UserPreference(UserPreference.TEXTAREA_TYPE,"bot.proschanie","Реклама при выходе из чата",getStringProperty("bot.proschanie")),
//            new UserPreference(UserPreference.STRING_TYPE,"bot.not_messeges","Если команда не верна",getStringProperty("bot.not_messeges")),
////            new UserPreference(UserPreference.STRING_TYPE,"bot.antikick_MSG","Сообщение при антикике",getStringProperty("bot.antikick_MSG")),
////            new UserPreference(UserPreference.STRING_TYPE,"bot.antiban_MSG","Сообщение при антибане",getStringProperty("bot.antiban_MSG")),
//
//        };
//    return p;
//  }

//     public UserPreference[] botAdminPreference(){
//        UserPreference[] p = {
//            new UserPreference(UserPreference.CATEGORY_TYPE,"adm", "Общие настройки админбота",""),
//            new UserPreference(UserPreference.BOOLEAN_TYPE,"adm.useAdmin","Использовать Админа в чате",getBooleanProperty("adm.useAdmin")),
//            new UserPreference(UserPreference.BOOLEAN_TYPE,"adm.useMatFilter","Разрешить реакцию на мат",getBooleanProperty("adm.useMatFilter")),
//            new UserPreference(UserPreference.BOOLEAN_TYPE,"adm.useSayAdmin","Разрешить админу разговаривать",getBooleanProperty("adm.useSayAdmin")),
//            new UserPreference(UserPreference.STRING_TYPE,"adm.matString","Слова для мата",getStringProperty("adm.matString")),
//            new UserPreference(UserPreference.STRING_TYPE,"adm.noMatString","Слова исключения",getStringProperty("adm.noMatString")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"adm.getStatTimeout","Пауза между показами статистики",getIntProperty("adm.getStatTimeout")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"adm.maxSayAdminCount","Максимум обращений к админу для одного человека",getIntProperty("adm.maxSayAdminCount")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"adm.maxSayAdminTimeout","Время сброса статистики обращений",getIntProperty("adm.maxSayAdminTimeout")),
//            new UserPreference(UserPreference.INTEGER_TYPE,"adm.sayAloneTime","Время молчания, через которое админ заговорит",getIntProperty("adm.sayAloneTime")),
//           new UserPreference(UserPreference.INTEGER_TYPE,"adm.sayAloneProbability","Вероятность разговора админа в тишине (1 к ...)",getIntProperty("adm.sayAloneProbability")),
//        };
//    return p;
//  }


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
               p.put("Настройки", new ServiceConfigExtend("Settings", "chat_srvs_props&pr=Settings", getUserPreference()));
               p.put("Прочие настройки", new ServiceConfigExtend("Other_options", "chat_srvs_props&pr=Other_options", getOtherUserPreference()));
 //              p.put("Системные сообщения", new ServiceConfigExtend("System_messages", "chat_srvs_props&pr=System_messages", sysMsgPreference()));
               p.put("Полномочия", new ServiceConfigExtend(null, "chat_user_group_props", null));
 //              p.put("Админ-бот", new ServiceConfigExtend("BotAdmin", "chat_bot_admin_props&pr=BotAdmin", botAdminPreference()));
        return p;
     };



    //****************************************//
    
     
    public boolean testAdmin(String screenName) {
         for(String s : getStringProperty("bot.adminUIN").split(";")) {
           if(screenName.equals(s)) return true;
        }
        return false;
    }


public String translitEng(String str){

       str=str.replace("а","a");
       str=str.replace("б","b");
       str=str.replace("в","v");
       str=str.replace("г","g");
       str=str.replace("д","d");
       str=str.replace("е","e");
       str=str.replace("ё","e");
       str=str.replace("з","z");
       str=str.replace("и","i");
       str=str.replace("й","y");
       str=str.replace("к","k");
       str=str.replace("л","l");
       str=str.replace("м","m");
       str=str.replace("н","n");
       str=str.replace("о","o");
       str=str.replace("п","p");
       str=str.replace("р","r");
       str=str.replace("с","s");
       str=str.replace("т","t");
       str=str.replace("у","u");
       str=str.replace("ф","f");
       str=str.replace("х","h");
       str=str.replace("ъ","`");
       str=str.replace("ь","'");
       str=str.replace("ы","i");
       str=str.replace("э","e");

       str=str.replace("А","A");
       str=str.replace("Б","B");
       str=str.replace("В","V");
       str=str.replace("Г","G");
       str=str.replace("Д","D");
       str=str.replace("Е","E");
       str=str.replace("Ё","E");
       str=str.replace("З","Z");
       str=str.replace("И","I");
       str=str.replace("Й","Y");
       str=str.replace("К","K");
       str=str.replace("Л","L");
       str=str.replace("М","M");
       str=str.replace("Н","N");
       str=str.replace("О","O");
       str=str.replace("П","P");
       str=str.replace("Р","R");
       str=str.replace("С","S");
       str=str.replace("Т","T");
       str=str.replace("У","U");
       str=str.replace("Ф","F");
       str=str.replace("Х","H");
       str=str.replace("Ъ","`");
       str=str.replace("Ь","'");
       str=str.replace("Ы","I");
       str=str.replace("Э","E");

     // Затем - "многосимвольные".

       str=str.replace("ж","zh");
       str=str.replace("ц","ts");
       str=str.replace("ч","ch");
       str=str.replace("ш","sh");
       str=str.replace("щ","sch");
       str=str.replace("ю","yu");
       str=str.replace("я","ya");
       str=str.replace("Ж","ZH");
       str=str.replace("Ц","TS");
       str=str.replace("Ч","CH");
       str=str.replace("Ш","SH");
       str=str.replace("Щ","SCH");
       str=str.replace("Ю","YU");
       str=str.replace("Я","YA");

 return str;
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
        return useAds;
    }

    public void setUseAds(boolean useAds) {
        this.useAds = useAds;
    }

    public int getAdsRate() {
        return adsRate;
    }

    public void setAdsRate(int adsRate) {
        this.adsRate = adsRate;
    }

    public boolean isAutoStart() {
        return   getBooleanProperty("main.StartBot");
    }

    public void setAutoStart(boolean autoStart) {
          setBooleanProperty("main.StartBot",autoStart);
    }

    public int getMaxOutMsgSize() {
        return   getIntProperty("chat.MaxOutMsgSize");
    }

    public void setMaxOutMsgSize(int maxOutMsgSize) {
          setIntProperty("chat.MaxOutMsgSize",maxOutMsgSize);
    }

    public int getMaxOutMsgCount() {
        return  getIntProperty("chat.MaxOutMsgCount");
    }

    public void setMaxOutMsgCount(int maxOutMsgCount) {
         setIntProperty("chat.MaxOutMsgCount",maxOutMsgCount);
    }

    public boolean getWriteAllMsgs() {
        return   getBooleanProperty("chat.writeAllMsgs");
    }

    public void setWriteAllMsgs(boolean writeAllMsgs) {
         setBooleanProperty("chat.writeAllMsgs",writeAllMsgs);
    }


    public int getMaxInviteTime() {
        return maxInviteTime;
    }

    public void setMaxInviteTime(int MaxInviteTime) {
        this.maxInviteTime = MaxInviteTime;
    }

    public String getAuthgroups() {
        return getStringProperty("auth.groups");
    }

    public void setAuthgroups(String authgroups) {
        setStringProperty("auth.groups",authgroups);
    }


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
