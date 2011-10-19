
package ru.jimbot.protocol;

import java.net.ProtocolException;
import java.util.Collection;
import java.util.HashMap;
import org.jivesoftware.smack.PacketListener;
import ru.jimbot.core.*;
import ru.jimbot.core.api.CommandProtocolListener;
import ru.jimbot.core.api.Protocol;
import ru.jimbot.core.api.ProtocolListener;
import ru.jimbot.util.FileUtils;
import ru.jimbot.util.Log;

import java.util.List;
import java.util.Vector;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

/**
 * Работа с протоколом XMPP(Jabber)
 *
 * @author ~Jo-MA-Jo~
 */
public class xmppProtocol implements Protocol, CommandProtocolListener,
        MessageListener,ChatManagerListener {

    private XMPPConnection connection;
    	private String lastInfo = "";
//    private Service srv; // Ссылка на сервис
    private String screenName = ""; // УИН
//    private int screenNameID = 0; // ИД УИНа в настройках (чтобы вытащить параметры, пароль и т.п.
    private String pass = "";
    private String server = "jabber.ru";
    private int port = 5222;
    private int status = 0;
    private String statustxt = "";
    private int xstatus = 0;
    private String xstatustxt1 = "";
    private String xstatustxt2 = "";
    private boolean connected = false;
    private String lastError = "";
    private List<ProtocolListener> protList = new Vector<ProtocolListener>();
    Log logger = Log.getDefault();
    private HashMap<String, Chat> chatMap;
        // filter the Message class and presence class



    public xmppProtocol() {
//        srv = s;
//        screenNameID = id;
//        screenName = srv.getProps().getUin(id);
        // Добавляемся в слушатели
//        srv.addCommandProtocolListener(this);
        chatMap = new HashMap<String, Chat>();
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);


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
        this.server = server;
        this.port = port;
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

    public void connect() {
        try {
            //  XMPPConnection.DEBUG_ENABLED = true;
            ConnectionConfiguration config = new ConnectionConfiguration(server, port, server);
            SASLAuthentication.supportSASLMechanism("PLAIN");
            connection = new XMPPConnection(config);
            connection.connect();
            connection.login(screenName, pass);
            connection.getChatManager().addChatListener(this);

            setStatus(true, xstatustxt2);
            if (connection.isConnected())System.out.println(screenName + " Online!");
            connected = true;
          //  notifyLogon();
        } catch (XMPPException ex) {
            ex.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            System.out.println("Disconnect...");
      connection.disconnect();
      connection.getChatManager().removeChatListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        connected = false;
    //    notifyLogout();
    }

    public boolean isOnLine() {
        if(connection==null) return false;
        return connected;
    }

    public String getLastError() {
        return lastError;
    }

    public void sendMsg(String to, String message) {
        try {
        Chat chat;
        if (chatMap.containsKey(to)) {
              chat = chatMap.get(to);
        } else {
	      chat = connection.getChatManager().createChat(to, this);
//              chat.addMessageListener(this);
              chatMap.put(to, chat);
               }
              chat.sendMessage(message);
	} catch (XMPPException e) {
		logger.info("ERROR send message: " + message);
		e.printStackTrace();
	}
    }



     public void displayBuddyList()  {
        Roster roster = connection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();

        System.out.println("\n\n" + entries.size() + " buddy(ies):");
         for(RosterEntry r:entries) {
             System.out.println(r.getUser());
        }
    }
     public void setStatus(boolean available, String status) {
        Presence.Type type = available? Type.available: Type.unavailable;
        Presence.Mode mode = Mode.chat;
        Presence presence = new Presence(type, status, 30, mode);
        connection.sendPacket(presence);
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

    /**
     *************************************************************************************
     */



    public void sendMessage(String in, String out, String text) {
        if(screenName.equalsIgnoreCase(in)) sendMsg(out,text);
    }

    public void logOn() {
        connect();
        notifyLogon();
    }

    public void logOut() {
        disconnect();
        notifyLogout();
    }

    /**
     * *********************************************************************************
     */




    /**
     * *******************************************************************************
     */


    public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message){
   //     System.out.println("processMessage "+chat.getParticipant() + " says: " + message.getBody());
   //     System.out.println("SenderID "+chat.getParticipant().split("/")[0]);
     if(message.getType() == org.jivesoftware.smack.packet.Message.Type.chat){
        String SenderID = chat.getParticipant().split("/")[0];
        String msg = message.getBody();
              if(FileUtils.isIgnor(SenderID)){
			logger.flood2("IGNORE LIST: " + SenderID + "->" + screenName + ": " + msg);
			return;
		}
       notifyMsg(new Message(SenderID,screenName,msg,Message.TYPE_TEXT));
      // chat.removeMessageListener(this);
     }
    }



    /**
     * *********************************************************************************************
     */


     @Override
        public void chatCreated(Chat chat, boolean createdLocally)
        {
            if (!createdLocally){
                chat.addMessageListener(this);
               // System.out.println("addMessageListener " + this);
            }

         }

   /**
     *
     * @param id
     * @param text
     */
    public void onChangeStatus(int id, String text) {
        this.status = id;
        this.statustxt = text;
//        srv.getProps().setIntProperty("icq.status", id);
//        OscarInterface.changeStatus(con, new StatusModeEnum(id));
    }

  public void onChangeXStatus(int id, String text1, String text2) {
        xstatus = id;
        xstatustxt1 = text1;
        xstatustxt2 = text2;

   //     OscarInterface.changeXStatus(con, new XStatusModeEnum(status));
    }
}