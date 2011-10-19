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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//import ru.jimbot.modules.anek.commands.*;
import ru.jimbot.core.*;
import ru.jimbot.core.api.Command;
import ru.jimbot.core.api.DefaultCommandParser;
import ru.jimbot.core.api.QueueListener;
import ru.jimbot.modules.info.commands.CommandBuilder;


import ru.jimbot.util.HttpUtils;

/**
 * Парсер команд
 * @author Prolubnikov Dmitry
 */
public class InfoCommandParser extends DefaultCommandParser implements QueueListener {
    public ConcurrentHashMap <String,StateUin> uq;
    public ConcurrentHashMap <Command,Integer> ci;
    public long state=0; //Статистика запросов
    public long state_add = 0;
 //   public BotAdmin badm=null;
    private boolean firstStartMsg = false;

  //  public ChatConfig psp;
  //  public ChatWork us;


    /** Creates a new instance of AnekCommandProc
     * @param s
     */
    public InfoCommandParser(InfoService s) {
        super(s);
        uq = new ConcurrentHashMap<String,StateUin>();
        ci = new ConcurrentHashMap<Command,Integer> ();
       // autority = new HashMap<String, String>();
        srv.addParserListener(this);
        cm.setDefaultAge(60000); // Время жизни одной сессии по умолчанию
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

    public void onMessage(Message m) {
        parse(m);
    }

   public void parse(Message m) {
		try {
			// Игнорируем все лишнее
			if (m.getType() != Message.TYPE_TEXT)
				return;
			//firstMsg(m);
			addState(m.getSnIn());
			Command cmd;
			if ("".equals(cm.getContext(m.getSnIn()).getLastCommand())) {
				cmd = this.getCommand(this.getCommand(m));
 			} else {
				cmd = this.getCommand(cm.getContext(m.getSnIn())
						.getLastCommand());
			}
			if (cmd == null) {
				notify(new Message(m.getSnOut(), m.getSnIn(),
						"Неверная команда! Для справки отправте - !help или знак - ?"));
			} else {
				if (cmd.authorityCheck(m.getSnIn())){
                                 int i=0;
                                 if (ci.containsKey(cmd))i=ci.get(cmd);
                                 ci.put(cmd, i+1);
                                 notify(cmd.exec(m));
                                }
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void notify(Message m) {
        for(QueueListener i:srv.getOutQueueListeners()) {
            i.onMessage(m);
        }
    }

    /**
     * Возвращает список разрешенных полномочий для пользователя с заданным УИНом
     *
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
    
    public String getCmdStat(){
        String s="";
        for (Command cmd : ci.keySet()){
            String Patterns="";
               for(String p:cmd.getCommandPatterns()){
                    Patterns+=p+" , ";
                }
            s+=Patterns.substring(0, Patterns.length()-3)+" -  [ "+ci.get(cmd)+" ]\n";
        }
        return s;
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
//        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
//        df.setTimeZone(TimeZone.getTimeZone("GMT"));
//        return (t/86400000) + " дней " + df.format(dt);
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
}