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

package ru.jimbot.protocol;


import ru.jimbot.core.api.CommandProtocolListener;
import ru.jimbot.core.api.Protocol;
import ru.jimbot.core.api.ProtocolListener;
import ru.jimbot.util.FileUtils;
import ru.jimbot.util.Log;

import java.util.List;
import java.util.Vector;


import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;


import ru.jimbot.core.Message;

import ru.mmp.core.MMPClient;
import ru.mmp.listener.MessageListener;
import ru.mmp.listener.StatusListener;


/**
 * Работа с протоколом MRA (Mail Ru Agent).
 *
 * @author Raziel
 */
public class MrimProtocol implements Protocol, CommandProtocolListener,
        StatusListener, MessageListener {

        private MMPClient con = null;
        private String screenName = "";
        private String pass = "";
        private int status = 1;
        private String desc1 = "";
        private String desc = "";
        private String serviceName = "";
        private boolean connected = false;
        private String lastError = "";
      //  private EventProxy eva;
       // private OutgoingMessageEventHandler h1;
      //  private ProtocolCommandEventHandler h2;
        private long pauseOutMsg = 2000;
        private int maxOutQueue = 20;
        private ConcurrentLinkedQueue<Message> q = new ConcurrentLinkedQueue<Message>();
        private long timeLastOutMsg = 0; // Время последнего отправленного сообщения
        private Timer timer;
        private TimerTask qt;


        
 
    	private String lastInfo = "";
//    private Service srv; // Ссылка на сервис

    private String server = "";
 //   private int port = 5980;

    private String statustxt = "";
    private int xstatus = 0;
    private String xstatustxt1 = "";
    private String xstatustxt2 = "";
 
    private List<ProtocolListener> protList = new Vector<ProtocolListener>();
    Log logger = Log.getDefault();

    
    
    public MrimProtocol() {
    }

    
    
    private void notifyMsg(Message m) {
        for(ProtocolListener i:protList) {
            i.onTextMessage(m);
        }
    }

    private void notifyStatus(Message m) {
        for(ProtocolListener i:protList) {
            i.onStatusMessage(m);
        }
    }

    private void notifyLogon() {
//        srv.createEvent(new ProtocolLogonEvent(srv, screenName));
        for(ProtocolListener i:protList) {
            i.logOn(screenName);
        }
    }

    private void notifyLogout() {
//        srv.createEvent(new ProtocolLogoutEvent(srv, screenName));
        for(ProtocolListener i:protList) {
            i.logOut(screenName);
        }
    }

   
    
    public void setConnectionData(String server, int port, String sn, String pass) {
  //      this.server = server;
  //      this.port = port;
        this.screenName = sn;
        this.pass = pass;
    }

    public void setLogger(Log logger) {
        this.logger = logger;
    }

    public void setStatusData(int status, String text) {
        this.status = status;
        this.statustxt = text;
    }

    public void setXStatusData(int status, String text1, String text2) {
        this.xstatus = status;
        this.xstatustxt1 = text1;
        this.xstatustxt2 = text2;
    }
    
    
    public void addProtocolListener(ProtocolListener e) {
        protList.add(e);
    }

    public boolean removeProtocolListener(ProtocolListener e) {
        return protList.remove(e);
    }

    public List<ProtocolListener> getProtocolListeners() {
        return protList;
    }

    
        @Override
        public void connect() {
                con = new MMPClient();
                con.setEmail(screenName+"@mail.ru");
                con.setPass(pass);
                
                con.addStatusListener(this);
                con.addMessageListener(this);
                try {
                        con.connect();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
        

        @Override
        public void disconnect() {
                try {
                        con.disconnect();
                        con.removeMessageListener(this);
                        con.removeStatusListener(this);
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                connected = false;
        }


        @Override
        public boolean isOnLine() {
                if (con == null)
                        return false;
                return connected;
        }


           @Override
        public void sendMsg(String sn, String msg) {
                con.sendMsg(sn, msg);
        }
 
    


    public void addContactList(String sn) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void RemoveContactList(String sn) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getScreenName() {
        return screenName;
    }
    
    public String getLastError() {
        return lastError;
    }
    
    

    /**
     *************************************************************************************
     */
//
    
    
    

        @Override
        public void onChangeStatus(int id, String txt) {
                // TODO Auto-generated method stub

        }

        @Override
        public void onChangeXStatus(int id, String txt1, String txt2) {
                this.xstatus = id;
                this.xstatustxt1 = txt1;
                this.xstatustxt2 = txt2;
                con.setStatus(getStatusId(id), txt1, txt2);

        }


     private int getStatusId(int id){
      HashMap <Integer,Integer> statuses= new HashMap();
          //statuses.put(268435456, 0);/*0 - Невидимый*/
            statuses.put(0, 1);/*1 - Онлайн*/
            //statuses.put(id, 2);/*2 - Отошоел*/
            //statuses.put(id, 3);/*3 - Не беспокоить*/
            //statuses.put(id, 4);/*4 - Занят*/
            //statuses.put(id, 5);/*5 - Недоступен*/
            statuses.put(7, 6);/*6 - Кушаю*/
          //statuses.put(24576, 7);/*7 - На работе*/
          //statuses.put(20480, 8);/*8 - Дома*/
          //statuses.put(16384, 9);/*9 - Депрессия*/
          //statuses.put(12288, 10);/*10 - Злой*/
            //statuses.put(id, 11);/*11 - Готов поболтать*/
            statuses.put(12, 12);/*12 - Дела*/
            statuses.put(1, 13);/*13 - Сердитый*/
            statuses.put(19, 14);/*14 - Болею*/
            statuses.put(30, 15);/*15 - Вопрос*/
            statuses.put(2, 16);/*16 - Купаюсь*/
            statuses.put(14, 17);/*17 - Весело*/
            statuses.put(20, 18);/*18 - Сплю*/
            statuses.put(32, 19);/*19 - Сердце*/
            statuses.put(9, 20);/*20 - Друзья*/
            statuses.put(15, 21);/*21 - Болтаю*/
            statuses.put(4, 22);/*22 - Праздник*/
            statuses.put(10, 23);/*23 - Чай,кофе*/
            statuses.put(16, 24);/*24 - Играю*/
            statuses.put(5, 25);/*25 - Пиво*/
            statuses.put(11, 26);/*26 - Музыка*/
            statuses.put(17, 27);/*27 - Учусь*/
            statuses.put(29, 28);/*28 - Туалет*/
            statuses.put(24, 29);/*29 - Пишу*/
     return statuses.containsKey(id) ? statuses.get(id): 1;
   }
    
    
    
    

    
    public void sendMessage(String in, String out, String text) {
        if(screenName.equalsIgnoreCase(in)) sendMsg(out,text);
    }



        public void logOn() {
                connect();
  //              pauseOutMsg = p.getPauseOut();
  //              maxOutQueue = p.getMsgOutLimit();
                timer = new Timer("queue out " + screenName);
                qt = new TimerTask() {

                        @Override
                        public void run() {
                                if (q.size() == 0)
                                        return;
                                if ((System.currentTimeMillis() - timeLastOutMsg) < pauseOutMsg)
                                        return;
                                Message m = q.poll();
                                sendMsg(m.getSnOut(), m.getMsg());
                                timeLastOutMsg = System.currentTimeMillis();
                        }
                };
                timer.schedule(qt, pauseOutMsg / 2, pauseOutMsg / 2);
        }
    
    

        public void logOut() {
                timer.cancel();
                timer.purge();
                q.clear();
                disconnect();
                notifyLogout();

        }
 
    



    /**
     * *********************************************************************************
     */

 
        public void onMessage(Message m) {
                if (q.size() > 0
                                || (System.currentTimeMillis() - timeLastOutMsg) < pauseOutMsg) {
                        if (q.size() <= maxOutQueue) {
                                q.add(m);
                        } else {
                                q.poll();
                                q.add(m);
                        }
                } else {
                        sendMsg(m.getSnOut(), m.getMsg());
                        timeLastOutMsg = System.currentTimeMillis();
                }
        }
         
         
         
//    /**
//     *
//     * @param exception
//     */
//    public void onLogout(Exception exception) {
//        System.err.println("Разрыв соединения: " + screenName + " - " + exception.getMessage());
//		logger.error("Разрыв соединения: " + screenName);
//        lastError = exception.getMessage();
//        disconnect();
//        notifyLogout();
////        connected = false;
//    }
    
        @Override
        public void onLogout() {
                System.err.println("Разрыв соединения: " + screenName);
                logger.error("Разрыв соединения: " + screenName);
                lastError = "";
                disconnect();
//                eva.protocolChangeState(screenName, EventProxy.STATE_LOGOFF, null);
                notifyLogout();
                // TODO подумать где отключать таймер и нужно ли в случае ошибок очищать
                // очередь
        }
    

//    public void onLogin() {
//        OscarInterface.changeStatus(con, new StatusModeEnum(status));
//	//	OscarInterface.changeXStatus(con, new XStatusModeEnum(xstatus));
//                onChangeXStatus(xstatus, xstatustxt1, xstatustxt2);
//
//        notifyLogon();
//        connected = true;
////        System.err.println("Connected " + con.getUserId());
//    }
    
        @Override
        public void onLogin() {
                notifyLogon();
                connected = true;
             //   eva.protocolChangeState(screenName, EventProxy.STATE_LOGON, null);
                con.setStatus(getStatusId(xstatus), xstatustxt1, xstatustxt2);

        }
    
        @Override
        public void onAuthorizationError(String message) {
                logger.error("Authorization for " + screenName
                                + " failed, reason " + message);
                lastError = message;
                disconnect();
                System.out.println("Authorization for " + screenName
                                + " failed, reason " + message);
        }

//    public void onAuthorizationFailed(LoginErrorEvent e) {
//        logger.error("Authorization for " + screenName + " failed, reason " + e.getErrorMessage());
//        lastError = e.getErrorMessage();
//        disconnect();
//        notifyLogout();
////        con.close();
////        connected = false;
//    }

//    public void onStatusResponse(StatusEvent e) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    /**
     * *******************************************************************************
     */
        @Override
        public void onIncomingMessage(String from, String msg) {
                    if(FileUtils.isIgnor(from)){
			logger.flood2("IGNORE LIST: " + from + "->" + screenName + ": " + msg);
			return;
		}
 //               eva.incomingMessage(new Message(from, screenName, msg,
  //                              Message.TYPE_TEXT));
                 notifyMsg(new Message(from,screenName,msg,Message.TYPE_TEXT));
        }

//    /**
//     *
//     * @param e
//     */
//    public void onIncomingMessage(IncomingMessageEvent e) {
//        if(FileUtils.isIgnor(e.getSenderID())){
//			logger.flood2("IGNORE LIST: " + e.getMessageId() + "->" + screenName + ": " + e.getMessage());
//			return;
//		}
//		// Сервисное сообщение от УИНа 1
//		if(e.getSenderID().equals("1")){
//		    logger.error("Ошибка совместимости клиента ICQ. Будет произведена попытка переподключения...");
//		    try{
//                con.close();
//                notifyLogout();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//		    return;
//		}
//        notifyMsg(new Message(e.getSenderID(),screenName,e.getMessage(),Message.TYPE_TEXT));
//    }


        @Override
        public void onMessageAck() {
                // TODO Auto-generated method stub

        }

        @Override
        public void onAuthorization(String from, String msg) {
                con.Authorize(from);
        }

        @Override
        public void onMessageError() {
                logger.error("Message error");

        }

    public void addContactList(String contact, String group) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getGroupByName(String group) {
        return false;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getContactById(String uin) {
        return false;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clearContactList() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addGroup(String group) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delGroup(String group) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * *********************************************************************************************
     */




}
