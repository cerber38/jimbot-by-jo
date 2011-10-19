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

import ru.jimbot.modules.chat.tables.Users;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//import ru.jimbot.modules.anek.commands.*;
import ru.jimbot.core.*;
import ru.jimbot.core.api.Command;
import ru.jimbot.core.api.DefaultCommandParser;
import ru.jimbot.core.api.QueueListener;
import ru.jimbot.modules.FloodElement;
import ru.jimbot.modules.chat.commands.CommandBuilder;
import ru.jimbot.modules.chat.games.QuizTask;


import ru.jimbot.util.HttpUtils;
import ru.jimbot.util.Log;

/**
 * Парсер команд
 * @author Prolubnikov Dmitry
 */
public class ChatCommandParser extends DefaultCommandParser implements QueueListener {
    public ConcurrentHashMap <String,StateUin> uq;
    public long state=0; //Статистика запросов
    public long state_add = 0;
 //   public BotAdmin badm=null;
    private boolean firstStartMsg = false;
    private ConcurrentHashMap <String, FloodElement> floodMap, floodMap2, floodNoReg;
    private ConcurrentHashMap <String, KickInfo> statKick; // Расширенная статистика киков
    private HashSet<String> warnFlag; // Флаг предупреждения о молчании
    private ConcurrentHashMap <String,String> up; // Запоминаем последний пришедший приват
    private List<MsgParserListener> msgParserList = new Vector<MsgParserListener>();

  //  public ChatConfig psp;
  //  public ChatWork us;


    /** Creates a new instance of AnekCommandProc
     * @param s
     */
    public ChatCommandParser(ChatService s) {
        super(s);
        uq = new ConcurrentHashMap<String,StateUin>();
        up = new ConcurrentHashMap<String,String>();
       // autority = new HashMap<String, String>();
        srv.addParserListener(this);
        cm.setDefaultAge(60000); // Время жизни одной сессии по умолчанию
            floodMap = new ConcurrentHashMap<String, FloodElement>();
            floodMap2 = new ConcurrentHashMap<String, FloodElement>();
            floodNoReg = new ConcurrentHashMap<String, FloodElement>();
            warnFlag = new HashSet<String>();
            statKick = new ConcurrentHashMap();
        initCommands();
    }


    /**
     * Создаем реестр всех команд
     */
    public void initCommands() {
        System.out.println("initCommands");
//    	for(ICommandBuilder i : ((ChatService)srv).getCommandConnector().getAllCommandBuilders()) {
//    		for(Command j : i.build(this)) {
//    			addCommand(j);
//      		}
//    	}
       /*****добавляем команды*****/
       for(Command c : new CommandBuilder().build(this)) {
                        addCommand(c);
       }
       /*************************************************/
        for(Command i:commands.values()) {
            i.init();
            autority.putAll(i.getAutorityList());
        }
 //       srv.getCron().addTask(new CheckScriptTask(this, 1000));
        

    }

     class KickInfo {
        public int id=0;
        public int len=0;
        public int moder_id=0;
        public int count=0;
        public String reason = "";

        KickInfo(int id, int moder_id, String r, int len) {
            this.id = id;
            this.len = len;
            this.moder_id = moder_id;
            this.reason = r;
            count = 0;
        }

        public int inc() {return count++;}
    }
    public void onMessage(Message m) {
        parse(m);
    }
    
    public void parse(Message m) {
		try {
			// Игнорируем все лишнее
			if (m.getType() != Message.TYPE_TEXT)
				return;
                                ChatWork cw =((ChatService)srv).getChatWork();
                                Users uss = cw.getUser(m.getSnIn());
                                ChatConfig psp = ((ChatConfig) srv.getConfig());
                                ChatQueue cq =((ChatService) srv).getChatQueue();
                                String uin =m.getSnIn();
                                String mmsg =m.getMsg();
			//    firstMsg(m);
			    addState(m.getSnIn());
                            Log.getLogger(srv.getName()).debug("CHAT: parse " + m.getSnOut() + ", " + m.getSnIn() + ", " + m.getMsg());
                                String tmsg = mmsg.trim();
                        if(tmsg.length()==0){
        	            Log.getLogger(srv.getName()).error("Пустое сообщение в парсере команд: " + uin + ">" + mmsg);
        	            return;
                        }

                       //*****************проверки на кик бан флуд и т.д.*******************\\
                    if(cw.testUser(uin)){
                        if(isBan(uin)){
                            Log.getLogger(srv.getName()).flood2("CHAT_BAN: " + uin + ">" + mmsg);
                            return;
                        }
                        if(testKick(uin)>0){
                            infrequentSend(new Message(m.getSnOut(), m.getSnIn(),"Вы не можете войти в чат. Осталось минут: " + testKick(uin)));
                            Log.getLogger(srv.getName()).info("CHAT_KICK: " + uin + ">" + mmsg);
                            return;
                        }

                    } else {
                	// Для нового юзера
                	// Проверка на флуд
                	if(floodNoReg.containsKey(uin)){
                		    FloodElement e = floodNoReg.get(uin);
                		    if(e.getDeltaTime()<(psp.getIntProperty("chat.floodTimeLimitNoReg")*1000)){
                			e.addMsg(tmsg);
                			floodNoReg.put(uin, e);
                			Log.getLogger(srv.getName()).flood("FLOOD NO REG " + uin + "> " + tmsg);
                			return; // Слишком часто
                		    }
                		    if(e.isDoubleMsg(tmsg) && e.getCount()>3){
                			e.addMsg(tmsg);
                			floodNoReg.put(uin, e);
                			Log.getLogger(srv.getName()).flood("FLOOD NO REG " + uin + "> " + tmsg);
                			return; // Повтор сообщений
                		     }
                		e.addMsg(tmsg);
            			floodNoReg.put(uin, e);
                	} else {
                		FloodElement e = new FloodElement(psp.getIntProperty("chat.floodTimeLimitNoReg")*1000);
                		floodNoReg.put(uin, e);
                	}
                }
            // Проверка на флуд
                         if(floodMap.containsKey(uin)){
                                FloodElement e = floodMap.get(uin);
            	                e.addMsg(tmsg);
            	                floodMap.put(uin, e);
                         } else {
            	                FloodElement e = new FloodElement(psp.getIntProperty("chat.floodTimeLimit")*1000);
            	                e.addMsg(tmsg);
            	                floodMap.put(uin, e);
                         }
                             testFlood(m);

    //        mmsg = WorkScript.getInstance(srv.getName()).startMessagesScript(mmsg, srv);
   //         if(tmsg.equals("")) return; // Сообщение было удалено в скрипте
                        if(tmsg.charAt(0)=='!' || tmsg.charAt(0)=='+'){
                             Log.getLogger(srv.getName()).info("CHAT COM_LOG: " + uin + ">>" + tmsg);
                        }
                    //***************************************************\\
                	Command cmd;
			if ("".equals(cm.getContext(m.getSnIn()).getLastCommand())) {
				cmd = this.getCommand(this.getCommand(m));
			} else {
				cmd = this.getCommand(cm.getContext(m.getSnIn())
						.getLastCommand());
			}

			if (cmd == null) {
                              if (testReg(m))return;
		              if(cw.getUser(m.getSnIn()).state==ChatWork.STATE_NO_CHAT){
                                notify(new Message(m.getSnOut(), m.getSnIn(),
                                psp.getStringProperty("bot.privetstvie_1") + "\n" +
                                psp.getStringProperty("bot.privetstvie_1_3") + "\n" +
                                psp.getStringProperty("bot.privetstvie_2") + "\n" +
                                "Не посылайте ваши сообщения слишком часто."));
                                return;
                              }
			} else {
                        //    if(!psp.testAdmin(m.getSnIn())&&!isChat(m))return;
				if (cmd.authorityCheck(m.getSnIn())){
					notify(cmd.exec(m));
                                  return;
                                }
                                else{ notify(new Message(m.getSnOut(), m.getSnIn(),
						"У вас нет доступа к данной команде!"));
                                return;
                               }
                 	}


                      String s = "";
                      if(mmsg.charAt(0)=='!' || mmsg.charAt(0)=='+'){
                        notify(new Message(m.getSnOut(), m.getSnIn(),"*NO* нет такой команды!"));
                        return;
                      }
                       notifyCommandParser(m);
                      if(mmsg.indexOf("/me")==0) s = mmsg.replaceFirst("/me", "*" + uss.localnick);
                         else
                      if(psp.getBooleanProperty("pokaz")){
                              s += psp.getStringProperty("auth.group_prist_"+uss.group)+
                                               " "+uss.localnick + " [" + uss.id + "]" +
                                    psp.getStringProperty("chat.delimiter") +" " + mmsg;
                       } else {
                              s += psp.getStringProperty("auth.group_prist_"+uss.group)+
                                               " "+uss.localnick + " " +
                                    psp.getStringProperty("chat.delimiter") +" " + mmsg;
                        }
                      if(s.length()>psp.getIntProperty("chat.MaxMsgSize")){
                              s = s.substring(0,psp.getIntProperty("chat.MaxMsgSize"));
                              notify(new Message(m.getSnOut(), m.getSnIn(),"Слишком длинное сообщение было обрезано: " + s));
                      }
                              s = s.replace('\n',' ');
                              s = s.replace('\r',' ');
                  Log.getLogger(srv.getName()).talk("CHAT: " + m.getSnIn() + "<" + uss.id +"> ["+uss.room +"]>>" + s);
                      cw.log(uss.id,m.getSnIn(),"OUT", s, uss.room);
                      cq.addMsg(s,m.getSnIn(), uss.room);
                     

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Добавить слушатель сообщений парсера
     * @param mpl
     */
   public void addMsgParserListener (MsgParserListener mpl) {
        msgParserList.add(mpl);
    }

   private void notifyCommandParser(Message m) {
        for(MsgParserListener i:msgParserList) {
            i.onMessage(m);
        }
    }  
     public void notify(Message m) {
          if (m.getMsg()==null)return;
        for(QueueListener i:srv.getOutQueueListeners()) {
            i.onMessage(m);
        }
     }

     public boolean isChat(Message m) {
        try{
             ChatWork cw =((ChatService)srv).getChatWork();
            if(cw.isChat(m.getSnIn())){
                return true;
            } else {
                notify(new Message(m.getSnOut(), m.getSnIn(), "Чтобы использовать команду вы должны зайти в чат"));
                return false;
            }
        } catch (Exception ex){
            return false; //если это новый пользователь
        }
    }
     private boolean testReg(Message m){
         try{
         ChatWork cw =((ChatService)srv).getChatWork();
         ChatConfig psp =((ChatConfig) srv.getConfig());
        if(cw.getUser(m.getSnIn()).state==ChatWork.STATE_NO_REG){
            notify(new Message(m.getSnOut(), m.getSnIn(),
	             psp.getStringProperty("bot.privetstvie_1") + "\n" +
                     psp.getStringProperty("bot.privetstvie_1_1") + "\n" +
                     psp.getStringProperty("bot.privetstvie_2") + "\n" +
                     "Не посылайте ваши сообщения слишком часто."));
         return true;
        }
         return false;
         } catch (Exception e) {
			e.printStackTrace();
                        return false;
		}
       }

        public boolean isBan(String uin) {
            ChatWork cw =((ChatService)srv).getChatWork();
              try{
                  return (cw.getUser(uin).state==ChatWork.STATE_BANNED);
              } catch (Exception ex) {
               return false; //если это новый пользователь
              }
        }

   /**
     * Проверка юзера, кикнут ли он
     */
    public int testKick(String sn){
         ChatWork cw =((ChatService)srv).getChatWork();
    	long tc = cw.getUser(sn).lastKick;
    	long t = System.currentTimeMillis();
    	return tc>t ? (int)(tc-t)/60000 : 0;
    }

    /**
     * Возвращает список разрешенных полномочий для пользователя с заданным УИНом
     * @param screenName
     * @return
     */
    public Set<String> getAuthList(String screenName) {
        HashSet<String> h = new HashSet<String>();
        if(srv.getConfig().testAdmin(screenName)){
            h.add("admin");
        }
        return h;
    }
    
    public void addAuthList (Map<String, String> a) {
            autority.putAll(a);
    }    

    private void firstMsg(Message m){
    	if(!firstStartMsg){
    		String[] s = srv.getConfig().getAdminUins();
    		for(int i=0;i<s.length;i++){
    		    String ss = "Бот успешно запущен!\n";
                if(HttpUtils.checkNewVersion())
                    ss += "На сайте http://jimbot.ru Доступна новая версия!\n" + HttpUtils.getNewVerDesc();
                else
                    ss += "Вся информация о боте из первых рук только на сайте: http://jimbot.ru";
               notify(new Message(m.getSnOut(), s[i], ss));
    		}
    		firstStartMsg=true;
    	}
    }

    /**
     * Определение времени запуска бота
     */
    public long getTimeStart(){
        long t = 0;
        try{
            File f = new File("./state");
            t = f.lastModified();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return t;
    }

    public long getUpTime(){
        return System.currentTimeMillis()-getTimeStart();
    }

    public long getHourStat(){
        if(getUpTime()>1000*60*60){
            return state/(getUpTime()/3600000);
        }
        return 0;
    }

    public long getDayStat(){
        if(getUpTime()>1000*60*60*24){
            return state/(getUpTime()/86400000);
        }
        return 0;
    }

    public String getTime(long t){
        Date dt = new Date(t);
        SimpleDateFormat df = new SimpleDateFormat("HH часов mm минут");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return (t/86400000) + " дней " + df.format(dt);
    }

    /**
     * Возвращает наименее загруженный номер
     * @return
     */
    public String getFreeUin(){
    	String u = "";
    	int k = 99;
    	int c = 0;
    	for(int i=0;i<srv.getConfig().getUins().size();i++){
            String s = srv.getConfig().getUins().get(i).getScreenName();
    		if(srv.getProtocol(s).isOnLine()){
                c = MsgStatCounter.getElement(s).getMsgCount(MsgStatCounter.M1);
    			if(k>c){
    				k = c;
    				u = s;
    			}
    		}
    	}
    	return u;
    }
    /**
     * Отправка сообщений забаненым и кикнутым юзерам происходит изредка
     */
    private void infrequentSend(Message m){
        if(testRnd(20))
           notify(m);
    }

     /**
     * Событие с вероятностью 1/i
     */
    public boolean testRnd(int i){
        Random r = new Random();
        if(i<=1)
            return false;
        else
            return r.nextInt(i)==1;
    }

    public void addState(String uin){
        if(!uq.containsKey(uin)){
            StateUin u = new StateUin(uin,0);
            uq.put(uin,u);
        }
    }

    public void stateInc(String uin){
        StateUin u = uq.get(uin);
        u.cnt++;
        uq.put(uin,u);
    }

    public class StateUin {
        public String uin="";
        public int cnt=0;

        public StateUin(String u, int c){
            uin = u;
            cnt = c;
        }
    }

    /**
     * Запоминание источника нового входящего сообщения
     */
    public void setPM(String sn, String from_sn){
        up.put(sn,from_sn);
    }
    /**
     * Выход из чата
     * @param proc
     * @param uin
     */
    public void exitChat(String uin) {
        ChatWork cw =((ChatService)srv).getChatWork();
        ChatConfig psp = ((ChatConfig) srv.getConfig());
        ChatQueue cq =((ChatService) srv).getChatQueue();
        Users uss = cw.getUser(uin);
        if (uss.state==ChatWork.STATE_CHAT ||
                uss.state==ChatWork.STATE_OFFLINE) {
            if(!psp.getBooleanProperty("chat.NoDelContactList")){
                Log.getLogger(srv.getName()).info("Delete contact " + uin);
            }
        } else
            return; // Юзера нет в чате - игнорируем команду

   String s = psp.getStringProperty("bot.proschanie") + "";
   String[] ss = s.split(";");
   int R = (int) ((Math.random()*ss.length));
        uss.state = ChatWork.STATE_NO_CHAT;
        cw.updateUser(uss);

        Log.getLogger(srv.getName()).talk(uss.localnick + " Ушел из чата");
        cw.log(uss.id,uin,"STATE_OUT",uss.localnick + " |" + uss.id + "| Ушел из чата",uss.room);
        cw.event(uss.id, uin, "STATE_OUT", 0, "", uss.localnick + "  |" + uss.id + "| Ушел из чата");
        cq.addMsg(psp.getStringProperty("auth.group_prist_"+uss.group)+" "+uss.localnick + " |" + uss.id + "|" + psp.getStringProperty("bot.potok_outchat"), uss.sn, uss.room);
        cq.sendMsg(new Message(uss.basesn, uin,psp.getStringProperty("auth.group_prist_"+uss.group)+" "+uss.localnick + ", |" + uss.id + "| " + psp.getStringProperty("bot.user_outchat")+"\n~~~~~~~~~~~~~~\n"+ss[R]));
        cq.delUser(uin);
    }



    /**
     * Кик юзера по времени
     */
    public void setKick(String sn, int min, int user_id, String r){
        ChatWork cw =((ChatService)srv).getChatWork();
        Users u = cw.getUser(sn);
        if(statKick.containsKey(sn)){
            KickInfo ki = statKick.get(sn);
            ki.moder_id = user_id;
            ki.reason = r;
            ki.inc();
            statKick.put(sn, ki);
        } else {
            KickInfo ki = new KickInfo(u.id, user_id, r, min);
            statKick.put(sn, ki);
        }
        u.lastKick = System.currentTimeMillis() + min*60000;
        cw.updateUser(u);
    }

    public void kick(String uin) {
        ChatWork cw =((ChatService)srv).getChatWork();
        Users uss = cw.getUser(uin);
        if(uss.state != ChatWork.STATE_CHAT) return;
        Log.getLogger(srv.getName()).talk("Kick user " + uin);

       exitChat(uin);
    }

    /**
     * КИК с записью в лог
     */
    public void lkick(String uin, String txt, int id) {
        ChatWork cw =((ChatService)srv).getChatWork();
        kick(uin);
        cw.log(cw.getUser(uin).id,uin,"KICK", txt,cw.getUser(uin).room);
        cw.event(cw.getUser(uin).id, uin, "KICK", id, "", txt);
    }

    /**
     * КИК с выставлением времени
     */
    public void tkick(String uin, int t, int user_id, String r){
        ChatWork cw =((ChatService)srv).getChatWork();
        ChatConfig psp = ((ChatConfig) srv.getConfig());
        ChatQueue cq =((ChatService) srv).getChatQueue();
        int room = cw.getUser(user_id).room;
        setKick(uin,t, user_id, r);
        Log.getLogger(srv.getName()).talk("kick user " + uin + " on " + t + " min.");
        if (cw.getUser(uin).state == ChatWork.STATE_CHAT)
            if (psp.getBooleanProperty("chat.isShowKickReason")) {
              //  cq.addMsg(s,m.getSnIn(), uss.room);
                cq.addMsg("Удален из чата,на "
                        + t
                        + " минут, модер: "
                        + (user_id == 0 ? psp.getStringProperty("adm.nick")
                                : cw.getUser(user_id).localnick)
                        + (r.equals("") ? "" : (", Причина: " + r)),uin,room);
            } else {
                cq.addMsg( "Удален из чата,на  " + t + " минут",uin,room);
            }
        lkick(uin, "kick user on " + t + " min. - " + r, user_id);
    }

    public void tkick(String uin, int t){
        tkick(uin, t, 0, "");
    }

    /**
     * КИК с автоматическим определением времени
     */
    public void akick(String uin, int user_id){
        ChatConfig psp = ((ChatConfig) srv.getConfig());
        int def = psp.getIntProperty("chat.defaultKickTime");
        int max = psp.getIntProperty("chat.maxKickTime");
        int i=def;
        if(statKick.containsKey(uin)){
        	int t = statKick.get(uin).len;
            i = t<max ? t*2 : def;
            i = i>max ? max : i;
        }
        tkick(uin, i, user_id, "");
    }

    public void akick(String uin){
        akick(uin, 0);
    }



    /**
     * Процедура проверки на срабатывание условий флуда. Включает кик при необходимости
     * @param proc
     * @param uin
     * @return истина, если юзер выпнут за флуд
     */
    private boolean testFlood(Message m){
        String uin = m.getMsg();
        ChatConfig psp = ((ChatConfig) srv.getConfig());
    	if(warnFlag.contains(uin)) warnFlag.remove(uin);
    	if(floodMap.containsKey(uin)){
    		if(floodMap.get(uin).getCount()>psp.getIntProperty("chat.floodCountLimit")){
    			akick(m.getSnIn());
    			return true;
    		}
    	}
    	if(floodMap2.containsKey(uin)) {
    		if(floodMap2.get(uin).getCount()>psp.getIntProperty("chat.floodCountLimit")){
    			akick(m.getSnIn());
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Проверка молчунов
     * @param uin
     */
    public void testState(String uin){
      //  if(psp.testAdmin(uin)) return;
        ChatConfig psp = ((ChatConfig) srv.getConfig());
        ChatWork cw =((ChatService)srv).getChatWork();
        long t = floodMap.get(uin).getDeltaTime();
        Message m=new Message(cw.getUser(uin).basesn, uin,"");
        if(t>(psp.getIntProperty("chat.autoKickTimeWarn")*60000) &&
         !warnFlag.contains(uin)){
            Log.getLogger(srv.getName()).info("Warning to " + uin);
            m.setMsg("Предупреждение! Вы слишком долго молчите и будете отключены от чата %)");
            notify(m);
          //  srv.getIcqProcess(srv.us.getUser(uin).basesn).mq.add(uin,"Предупреждение! Вы слишком долго молчите и будете отключены от чата %)");
            warnFlag.add(uin);
         }
        if(t>(psp.getIntProperty("chat.autoKickTime")*60000)){
            Log.getLogger(srv.getName()).talk("Autokick to " + uin);
            warnFlag.remove(uin);
            kick(uin);
        }
    }


    private String[][] chg = {{"y","у"},{"Y","у"},{"k","к"},{"K","к"},{"e","е"},
                            {"E","е"},{"h","н"},{"H","н"},{"r","г"},{"3","з"},{"x","х"},{"X","х"},
                            {"b","в"},{"B","в"},{"a","а"},{"A","а"},{"p","р"},{"P","р"},{"c","с"},
                            {"C","с"},{"6","б"}};

        /**
     * Замена похожих букв на русские
     */
    public String changeChar(String s){
        for(int i=0;i<chg.length;i++){
            s = s.replace(chg[i][0],chg[i][1]);
        }
        return s;
    }
       /**
     * Тихая проверка полномочий. Не выводит сообщений.
     * @param proc
     * @param uin
     * @param obj
     * @return
     */
    public boolean qauth( String uin, String obj){
        ChatWork cw =((ChatService)srv).getChatWork();
        if(!cw.authorityCheck(uin, obj)){
            return false;
        }
        return true;
    }
      /**
     * Проверка ника на правильность
     */
    public boolean testNick(String sn, String nick){
         ChatConfig psp = ((ChatConfig) srv.getConfig());
        if(psp.testAdmin(sn)) return true; // Админам можно любой ник :)
        String[] ss = psp.getStringProperty("chat.badNicks").split(";");
        String nick1 = changeChar(nick.toLowerCase());
        for(int i=0;i<ss.length;i++){
            if(nick.toLowerCase().indexOf(ss[i])>=0 ||
                    nick1.toLowerCase().indexOf(ss[i])>=0) return false;
        }
        String s = psp.getStringProperty("chat.badSymNicks");
        String s1 = psp.getStringProperty("chat.goodSymNicks");
        if(s1.equals("")){
        	for(int i=0;i<s.length();i++){
        		if(nick.indexOf(s.charAt(i))>=0) return false;
            }
        } else {
        	for(int i=0;i<nick.length();i++){
        		if(s1.indexOf(nick.charAt(i))<0) return false;
            }
        }

        return true;
    }
        public int getRND(int i){
        Random r = new Random();
        return r.nextInt(i);
    }
     public String getCaptcha(){
         ChatWork cw =((ChatService)srv).getChatWork();
        Random r = new Random();
        int i1 = getRND(15);
    	int i2 = getRND(15);
    	int i3 = getRND(15);
    	String Question = "";
	String Answer = "";
        String s = "(" + i1 + "+" + i2 + ")*" + i3 + "=" + ((i1+i2)*i3);

        int maxCapch =  cw.getLastIndex("capcha","capcha");
        int num = (int) (r.nextInt(maxCapch)); // случаиное число

        try {
   	PreparedStatement pst = (PreparedStatement)cw.getDB().get("capcha").getDb().prepareStatement("SELECT * FROM `capcha` WHERE id = ? ");
     	pst.setInt(1,num);
	ResultSet rs = pst.executeQuery();
		while(rs.next()){
		Question = rs.getString(2);
		Answer = rs.getString(3);
		}
			rs.close();
			pst.close();
        s = Question + "=" + Answer;
        } catch (Exception ex) {}
    	return s;
    }
}