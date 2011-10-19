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

package ru.jimbot.core;

import ru.jimbot.util.Log;
import ru.jimbot.core.api.ProtocolListener;
import ru.jimbot.core.api.QueueListener;
import ru.jimbot.core.api.Service;
import ru.jimbot.core.events.CommandProtocolLogonEvent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Очередь входящих сообщений
 * @author Prolubnikov Dmitry
 */
public class MsgInQueue implements Runnable, ProtocolListener {
    ConcurrentLinkedQueue <Message> q;
    private Thread th;
    int sleepAmount = 10;
    public boolean ignoreOffline = true;
    // Пары уин - время последнего сообщения
    private HashMap<String,Long> flood = new HashMap<String,Long>();
    private Service srv; // Ссылка на сервис
    private ConcurrentHashMap<String, LogoutInfo> lastLogout; // время последнего падения уина
    private int checkCount = 0;

    /** Creates a new instance of MsgInQueue */
    public MsgInQueue(Service s) {
        srv = s;
        q = new ConcurrentLinkedQueue<Message>();
        lastLogout = new ConcurrentHashMap<String, LogoutInfo>();
//        srv.addProtocolListener(this);
        for(String sn : srv.getAllProtocols()) {
            srv.getProtocol(sn).addProtocolListener(this);
        }
    }

    /**
     * Проверка на флуд входящий сообщений. Слишком частые не должны попадать в очередь.
     * @param uin
     * @return
     */
    private boolean testFlood(String uin){
    	long i = srv.getConfig().getPauseIn();
    	long c = System.currentTimeMillis();
    	if(flood.containsKey(uin)){
    		if((c-flood.get(uin))<i){
    			flood.put(uin, c);
    			return true;
    		} else {
    			flood.put(uin, c);
    			return false;
    		}
    	} else {
    		flood.put(uin, c);
			return false;
    	}
    }

    /**
     * Запуск процесса
     */
    public void start(){
        th = new Thread(this,"msg_in");
        th.setPriority(Thread.NORM_PRIORITY);
        th.start();
    }

    /**
     * Остановка процесса
     */
    public synchronized void stop() {
        th = null;
        notify();
    }

    /**
     * Обработка сообщения в очереди
     */
    private void parseMsg(){
        try{
            if(q.size()==0) return;
            notifyCommandParser(q.poll());
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    private void notifyCommandParser(Message m) {
        for(QueueListener i:srv.getParserListeners()) {
            i.onMessage(m);
        }
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (th == me) {
            parseMsg();
            checkLogout();
            try {
                Thread.sleep(sleepAmount);
            } catch (InterruptedException e) { break; }
        }
        th=null;
    }

    /**
     * Проверка упадших уинов и их переподключение
     */
    private void checkLogout() {
        checkCount++;
        if(checkCount<100) return;
        checkCount = 0;
        for(LogoutInfo i:lastLogout.values()){
            if(i.getTime()>0 &&
                    (System.currentTimeMillis()-i.getTime())>
                            ((i.getCount()*30000)>900000 ? 900000 : (i.getCount()*30000))){
                srv.createEvent(new CommandProtocolLogonEvent(srv, i.getSn()));
                i.incCount();
            }
        }
    }

    /**
     * Размер очереди
     * @return
     */
    public int size(){
    	return q.size();
    }

    public void onTextMessage(Message m) {
        MsgStatCounter.getElement(m.getSnOut()).addMsgCount();
        if(!testFlood(m.getSnIn()))
            q.add(m);
        else {
            Log.getLogger(srv.getName()).flood("FLOOD from " + m.getSnIn() + ">> " + m.getMsg());
            // TODO Подумать над флудом
            m.setType(Message.TYPE_FLOOD_NOTICE);
            q.add(m);
        }
    }

    public void onStatusMessage(Message m) {
        q.add(m);
    }

    public void onError(Message m) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void logOn(String sn) {
        System.out.println("OnLogon " + sn);
        lastLogout.put(sn, new LogoutInfo(sn));
    }

    public void logOut(String sn) {
        try {
            System.out.println("OnLogout " + sn);
            LogoutInfo u = lastLogout.get(sn);
            if(u==null) u = new LogoutInfo(sn);
            u.setTime(System.currentTimeMillis());
            u.incCount();
            lastLogout.put(sn, u);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onOtherMessage(Message m) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Класс содержит информацию о падениях уина. Используется для переподключения и определения паузы между
     * переподключениями номера.
     */
    private class LogoutInfo {
        private long time = 0;
        private int count = 0;
        private String sn;

        private LogoutInfo(String sn) {
            this.sn = sn;
        }

        public String getSn() {
            return sn;
        }

        /**
         * Время последнего падения
         * @return
         */
        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        /**
         * Общее число падений
         * @return
         */
        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void incCount() {
            count++;
        }
    }
}
