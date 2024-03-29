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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.OscarInterface;
import ru.caffeineim.protocols.icq.integration.events.*;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;
import ru.caffeineim.protocols.icq.integration.listeners.XStatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.XStatusModeEnum;
import ru.jimbot.core.*;
import ru.jimbot.core.api.CommandProtocolListener;
import ru.jimbot.core.api.Protocol;
import ru.jimbot.core.api.ProtocolListener;
import ru.jimbot.util.FileUtils;
import ru.jimbot.util.Log;

import java.util.List;
import java.util.Vector;
import ru.caffeineim.protocols.icq.contacts.Contact;
import ru.caffeineim.protocols.icq.contacts.ContactListItem;
import ru.caffeineim.protocols.icq.contacts.Group;
import ru.caffeineim.protocols.icq.exceptions.ContactListOperationException;
import ru.caffeineim.protocols.icq.integration.listeners.ContactListListener;
import ru.caffeineim.protocols.icq.setting.enumerations.ClientsEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.SsiResultModeEnum;

/**
 * ������ � ���������� ICQ
 *
 * @author Prolubnikov Dmitry
 */
public class IcqProtocol implements Protocol, CommandProtocolListener,
        OurStatusListener, MessagingListener, XStatusListener , ContactListListener {
    private OscarConnection con=null;
    	private String lastInfo = "";
//    private Service srv; // ������ �� ������
    private String screenName = ""; // ���
//    private int screenNameID = 0; // �� ���� � ���������� (����� �������� ���������, ������ � �.�.
    private String pass = "";
    private String server = "login.icq.com";
    private int port = 5980;
    private int status = 0;
    private String statustxt = "";
    private int xstatus = 0;
    private String xstatustxt1 = "";
    private String xstatustxt2 = "";
    private boolean connected = false;
    private String lastError = "";
    private List<ProtocolListener> protList = new Vector<ProtocolListener>();
    Log logger = Log.getDefault();

    public IcqProtocol() {
//        srv = s;
//        screenNameID = id;
//        screenName = srv.getProps().getUin(id);
        // ����������� � ���������
//        srv.addCommandProtocolListener(this);
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
//        status = srv.getProps().getIntProperty("icq.xstatus");
//        statustxt1 = srv.getProps().getStringProperty("icq.STATUS_MESSAGE1");
//        statustxt2 = srv.getProps().getStringProperty("icq.STATUS_MESSAGE2");
		con = new OscarConnection(server, port, screenName, pass);
        con.addOurStatusListener(this);
        con.addMessagingListener(this);
        con.addXStatusListener(this);
        con.connect();
//        connected = true;
    }

    public void disconnect() {
        try {
            System.out.println("Disconnect...");
            con.close();
            con.removeOurStatusListener(this);
            con.removeMessagingListener(this);
            con.removeXStatusListener(this);
//            notifyLogout();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        connected = false;
    }

    public boolean isOnLine() {
        if(con==null) return false;
        return connected;
    }

    public String getLastError() {
        return lastError;
    }

    public void sendMsg(String sn, String msg) {
        try {
			OscarInterface.sendBasicMessage(con, sn, msg);
		} catch (ConvertStringException e) {
			logger.info("ERROR send message: " + msg);
			e.printStackTrace();
		}
    }



    public String getScreenName() {
        return screenName;
    }

    /**
     *************************************************************************************
     */

    /**
     *
     * @param id
     * @param text
     */
    public void onChangeStatus(int id, String text) {
        this.status = id;
        this.statustxt = text;
//        srv.getProps().setIntProperty("icq.status", id);
        OscarInterface.changeStatus(con, new StatusModeEnum(id));
    }

    public void onChangeXStatus(int id, String text1, String text2) {
        xstatus = id;
        xstatustxt1 = text1;
        xstatustxt2 = text2;

       // OscarInterface.changeXStatus(con, new XStatusModeEnum(xstatus));
        OscarInterface.changeXStatus(con, new XStatusModeEnum(xstatus),new ClientsEnum(clientsMatrix[1]));
    }

    
    
    private byte[][] clientsMatrix ={
 /*0 - QIP 2005*/   { (byte) 0x56, (byte) 0x3F, (byte) 0xC8, (byte) 0x09, (byte) 0x0B, (byte) 0x6F, (byte) 0x41, (byte) 0x51, (byte) 0x49, (byte) 0x50, (byte) 0x20, (byte) 0x32, (byte) 0x30, (byte) 0x30, (byte) 0x35, (byte) 0x61},
 /*1 - QIP Infium*/  { (byte) 0x7C, (byte) 0x73, (byte) 0x75, (byte) 0x02, (byte) 0xC3, (byte) 0xBE, (byte) 0x4F, (byte) 0x3E, (byte) 0xA6, (byte) 0x9F, (byte) 0x01, (byte) 0x53, (byte) 0x13, (byte) 0x43, (byte) 0x1E, (byte) 0x1A},
   };
    
    public void sendMessage(String in, String out, String text) {
        if(screenName.equalsIgnoreCase(in)) sendMsg(out,text);
    }

    public void logOn() {
        connect();
    }

    public void logOut() {
        disconnect();
        notifyLogout();
    }

    /**
     * *********************************************************************************
     */

    /**
     *
     * @param exception
     */
    public void onLogout(Exception exception) {
        System.err.println("������ ����������: " + screenName + " - " + exception.getMessage());
		logger.error("������ ����������: " + screenName);
        lastError = exception.getMessage();
        disconnect();
        notifyLogout();
//        connected = false;
    }

    public void onLogin() {
        OscarInterface.changeStatus(con, new StatusModeEnum(status));
	//	OscarInterface.changeXStatus(con, new XStatusModeEnum(xstatus));
                onChangeXStatus(xstatus, xstatustxt1, xstatustxt2);

        notifyLogon();
        connected = true;
//        System.err.println("Connected " + con.getUserId());
    }

    public void onAuthorizationFailed(LoginErrorEvent e) {
        logger.error("Authorization for " + screenName + " failed, reason " + e.getErrorMessage());
        lastError = e.getErrorMessage();
        disconnect();
        notifyLogout();
//        con.close();
//        connected = false;
    }

    public void onStatusResponse(StatusEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * *******************************************************************************
     */

    /**
     *
     * @param e
     */
    public void onIncomingMessage(IncomingMessageEvent e) {
        if(FileUtils.isIgnor(e.getSenderID())){
			logger.flood2("IGNORE LIST: " + e.getMessageId() + "->" + screenName + ": " + e.getMessage());
			return;
		}
		// ��������� ��������� �� ���� 1
		if(e.getSenderID().equals("1")){
		    logger.error("������ ������������� ������� ICQ. ����� ����������� ������� ���������������...");
		    try{
                con.close();
                notifyLogout();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
		    return;
		}
        notifyMsg(new Message(e.getSenderID(),screenName,e.getMessage(),Message.TYPE_TEXT));
    }

    public void onIncomingUrl(IncomingUrlEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onOfflineMessage(OfflineMessageEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onMessageAck(MessageAckEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onMessageError(MessageErrorEvent e) {
        logger.error("Message error " + e.getError().toString());
    }

    public void onMessageMissed(MessageMissedEvent e) {
        logger.debug("Message from " + e.getUin() + " can't be recieved because " + e.getReason()  +
				" count="+e.getMissedMsgCount());
    }

    /**
     * *********************************************************************************************
     */

    public void onXStatusRequest(XStatusRequestEvent e) {
        try {
    		OscarInterface.sendXStatus(con, new XStatusModeEnum(xstatus),
    				xstatustxt1,
    				xstatustxt2, e.getTime(), e.getMsgID(), e.getSenderID(), e.getSenderTcpVersion());
    	}
    	catch(ConvertStringException ex) {
    		System.err.println(ex.getMessage());
    	}
    }

    public void onXStatusResponse(XStatusResponseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    //--------������ � ������� ������--------//

    /**
    * ��������� ��������
    * @param contact - UIN ������������
    * @param group - �������� ������
    */
        //@Override
    public void addContactList(String contact, String group) {
    try {

    addGroup(group);

    if(getContactById(contact)) con.getContactList().addContact(contact, group);

    } catch (ContactListOperationException ex) {
        ex.printStackTrace();
    System.out.println("Error " + ex.getMessage());
    }
    }

    /**
     * �������� ������� ������ ��� ���
     * @param group
     * @return
     */
    public boolean getGroupByName(String group) {
    if(con.getContactList().getGroupByName(group) == null) return true;
    System.out.println("������ "+group+" ��� ���������");
    return false;
    }

    /**
     * �������� �������� ��� ��� ���
     * @param group
     * @return
     */
    public boolean getContactById(String uin) {
    if(con.getContactList().getContactById(uin) == null) return true;
    System.out.println("��� "+uin+" ��� ��������");
    return false;
    }

    /**
    * �������� ������������ � UIN
    * @param uin - UIN ������������
    */
        //@Override
    public void RemoveContactList(String uin) {
    try {

    if(!getContactById(uin))  con.getContactList().removeContact(uin);

    } catch (ContactListOperationException ex) {
    System.out.println("Error " + ex.getMessage());
    }
    }

    /**
    * ������� ������� �����
    */
        //@Override
    public void clearContactList() {
        try {
            con.getContactList().clearContactList();
        } catch (ContactListOperationException ex) {
            System.out.println("Error " + ex.getMessage());
        }

    }

    /**
    * ��������� ����� ������
    * @param group
    */
        //@Override
    public void addGroup(String group){
    try {
    if(getGroupByName(group))  con.getContactList().addGroup(group);
    } catch (ContactListOperationException ex) {
    System.out.println("Error " + ex.getMessage());
    }
    }

    /**
    * �������� ������ �� �� ��������
    * @param group
    */
        //@Override
    public void delGroup(String group){
    try {
         for (Iterator iter = this.con.getContactList().getRootGroup().getContainedItems().iterator(); iter.hasNext(); ) {
          ContactListItem item = (ContactListItem)iter.next();
          if (item instanceof Group) {
            Group grp = (Group)item;
          }
        }
    if(!getGroupByName(group)) con.getContactList().delGroup(group);

    } catch (ContactListOperationException ex) {
    System.out.println("Error " + ex.getMessage());
    }
    }

public void onUpdateContactList(ContactListEvent e) {
System.out.println("\nMy Contact List");
System.out.println(con.getContactList());
}

   // //@Override
public void onSsiModifyingAck(SsiModifyingAckEvent e) {
for (int i = 0; i < e.getResults().length; i++) {
System.out.println("Result = " + e.getResults()[i] + " code = " + e.getResults()[i].getResult());
}
if (e.getResults()[e.getResults().length - 1].getResult() == SsiResultModeEnum.NO_ERRORS) {
System.out.println("\nMy Contact List");
System.out.println(con.getContactList().toString());
}
}

 //@Override
public void onSsiFutureAuthGrant(SsiFutureAuthGrantEvent e) {
System.out.println("FutureAuthGrant UIN: " + e.getSenderUin() + " Mesage: " + e.getMessage());
}

   //@Override
public void onSsiAuthRequest(SsiAuthRequestEvent e) {
try {
System.out.println("AuthRequest UIN: " + e.getSenderUin() + " Mesage: " + e.getMessage());
con.getContactList().sendAuthReplyMessage(e.getSenderUin(), "Welcome!", true);
} catch (ContactListOperationException ex) {
System.out.println(ex.getMessage());
}
}

 //@Override
public void onSsiAuthReply(SsiAuthReplyEvent e) {
System.out.println("AuthReply UIN: " + e.getSenderUin() + " Mesage: " + e.getMessage() + " Flag: " + e.getAuthFlag());
}

    public void addContactList(String sn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

 
}
