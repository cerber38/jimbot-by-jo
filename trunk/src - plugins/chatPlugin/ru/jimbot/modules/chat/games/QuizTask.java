/*
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2009 JimBot project
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

package ru.jimbot.modules.chat.games;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import ru.jimbot.core.Message;
import ru.jimbot.core.api.Service;
import ru.jimbot.core.api.Task;

import ru.jimbot.modules.chat.ChatCommandParser;
import ru.jimbot.modules.chat.ChatConfig;
import ru.jimbot.modules.chat.ChatQueue;
import ru.jimbot.modules.chat.ChatService;
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.MsgParserListener;
import ru.jimbot.modules.chat.tables.Users;

/**
 * Викторина
 * 
 * @author Prolubnikov Dmitry
 */
public class QuizTask implements Task, MsgParserListener{
    private ChatService srv;
    private long period = 0;
    private boolean enabled = true;
    private long lastStart = 0;
    private long countStart = 0; // число запусков задачи
    private ChatWork cw = null;
    private ChatCommandParser ccp = null;
    private ChatConfig psp = null;
    private String answer="",riddle="";
    private ChatQueue cq = null;
    private Random r = new Random();
    private int room = 0;
    private int price;
    private int rep = 0;    
    private int id = 0;  
    private HashSet ignor;    

    /**
     * Период запуска задачи
     * @param period
     */
    public QuizTask(ChatService srv, long period) {
        this.srv = srv;
        this.period = period;
        psp = srv.getConfig();
        room = psp.getIntProperty("vik.room");//комната игры
        cw = srv.getChatWork();
        cq = srv.getChatQueue();
        ccp = (ChatCommandParser) srv.getCommandParser();        
        ccp.addMsgParserListener(this);
        ignor = new HashSet();
        price = psp.getIntProperty("vik.ball")+1;
    }

    /**
     * Конструктор для викторины
     * @param srv
     * @param period
     * @param cw
     */
    public QuizTask(ChatService srv, long period, ChatWork cw) {
        this.srv = srv;
        this.period = period;
        this.cw = cw;
        psp = (ChatConfig) srv.getConfig();
        room = psp.getIntProperty("vik.room");//комната игры
        cq = srv.getChatQueue();
        ccp = (ChatCommandParser) srv.getCommandParser();
        ccp.addMsgParserListener(this);
        ignor = new HashSet();
        price = psp.getIntProperty("vik.ball")+1;
    }
  
    
    public void onMessage(Message m) {
        Users user = cw.getUser(m.getSnIn());
        //int room = user.room;
        int top = user.language+1;
        String msg = m.getMsg();

		if (this.room == user.room && answer.equalsIgnoreCase(msg) ) {
                    speak("Правильно ответил(а): " + user.localnick + " и получает +"+price+" "+psp.getStringProperty("money")+"\n" +
                          "Всего правильно ответил(а) на ( "+top+" ) вопросов.\n");
                        user.country+=price;
                        user.language=top;
                        cw.updateUser(user);
                        clear();
                        //getRiddle();
                        exec();
                }
    }
    
        /**
         * получаем сообщение
         * @return
         */
        public String getMsg() {
               if ("".equals(riddle))getRiddle();
               ++rep;
               String Tips = getTips();
               if (Tips==null){
                   clear();
                   getRiddle();
                   Tips = getTips();
               }
               if (price>1)price -=1;
              return "Цена вопроса [" +price+"] "+psp.getStringProperty("money")+
                     "\nВопрос :" + id + 
                     "\n" +riddle + 
                     "\n" +Tips;

	} 
        
       	private String getTips() {
            if (rep==answer.length()){
                return psp.getStringProperty("vik.noAnswer");
            }
            if (rep>answer.length())
                return null;
                        
		String s = "Подсказка №"+rep+": ";
                String s1 = "";
                String s2 = " -["+answer.length() + " букв]";

                int nn =0;
                do {
                    nn=getRND(answer.length());
                    if (!ignor.contains(nn)) break;
                   } while (ignor.size()<answer.length()-1);
                ignor.add(nn);
                    for (int n = 0; n < answer.length(); ++n)
                         s1 += (ignor.contains(n)) ? answer.charAt(n) : "*";
                         s += s1+" " + s2;

		return s;
	}     
        
    	private String[] getRiddle() {
            String[]s={};
		try {
                        id =getRND((int)cw.db.get("victorina").getLastIndex("victorina"));
			PreparedStatement pst = cw.db.get("victorina").getDb().prepareStatement("SELECT * FROM victorina WHERE id = "+id);
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				riddle = rs.getString(2);
				answer = rs.getString(3);
                                s=new String[]{rs.getString(2),rs.getString(3)};
			}
			rs.close();
			pst.close();

		} catch (Exception ex) {}
                return s;

	}

   /**
    * бла бла бла
    * @param msg
    */
    private void speak(String msg) {
        String s = "Викторина\n" + msg;
	srv.getChatQueue().addMsg(s,"",psp.getIntProperty("vik.room"));
    }  
    
    private int getRND(int i) {
	return r.nextInt(i-1)+1;
    }

    public void clear() {
        riddle = "";
        answer = "";
        rep = 0;
        ignor.clear();
        price = psp.getIntProperty("vik.ball")+1;
    }   
    /**
     * Выполнение заданной функции
     */
    public void exec() {
        lastStart = System.currentTimeMillis();
        if(countStart==0){
            // пропустим первый запуск, наверняка номера в этот момент еще не успели выйти в сеть.
            countStart++;
            return;
          }
        if(testRoom())speak(getMsg());
        countStart++;
    }
    private boolean testRoom() {
             boolean b = false;
             Enumeration<String> e = srv.getChatQueue().uq.keys();
              while(e.hasMoreElements()){
               Users us = srv.getChatWork().getUser(e.nextElement());
                if(us.state==srv.getChatWork().STATE_CHAT) 
                  if(us.room==psp.getIntProperty("vik.room"))return true;
           }
             return b;
    }
    /**
     * Возвращает период в мс, или 0
     *
     * @return
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Когда запустить (при однократном запуске), иначе 0
     *
     * @return
     */
    public long getStart() {
        return 0;
    }

    /**
     * Время последнего запуска (для расчета периодов)
     *
     * @return
     */
    public long getLastStart() {
        return lastStart;
    }

    /**
     * Команда активна?
     *
     * @return
     */
    public boolean isActive() {
        return enabled;
    }

    /**
     * Установить время запуска команды
     *
     * @param t
     */
    public void setStart(long t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Установить период для периодического регулярного запуска
     *
     * @param t
     */
    public void setPeriod(long t) {
        period = t;
    }

    /**
     * Отключение активности (после выполнения)
     */
    public void disable() {
        enabled = false;
    }


}
