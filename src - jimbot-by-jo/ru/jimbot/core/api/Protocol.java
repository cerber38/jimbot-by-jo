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

package ru.jimbot.core.api;

import ru.jimbot.util.Log;

import java.util.List;

/**
 * ��������� ��� ������ � ����������� � ����
 *
 * @author Prolubnikov Dmitry
 */
public interface Protocol {

    /**
     * ������� ������ ��� ������ ���������
     */

    /**
     * �������� ������ � ����������
     * @param server
     * @param port
     * @param sn
     * @param pass
     */
    public void setConnectionData(String server, int port, String sn, String pass);

    /**
     * ���� �������� ����� ������ ����?
     * @param logger
     */
    public void setLogger(Log logger);

    /**
     * ������� ������
     * @param status
     * @param text
     */
    public void setStatusData(int status, String text);

    /**
     * ����������� ������
     * @param status
     * @param text1
     * @param text2
     */
    public void setXStatusData(int status, String text1, String text2);

    /**
     * ������ ��� ������ � ����������
     */

    /**
     * ���������� ����������
     */
    public void connect();

    /**
     * ��������� ����������
     */
    public void disconnect();

//    /**
//     * ���������� ������
//     * @param status
//     */
//    public void setStatus(int status);

    /**
     * ���������� �����������?
     * @return
     */
    public boolean isOnLine();

    /**
     * ��������� ���������
     * @param sn - ����
     * @param msg - �����
     */
    public void sendMsg(String sn, String msg);

//    /**
//     *
//     * @param sn
//     * @param status
//     */
//    public void getStatus(String sn, int status);

    /**
     * �������� � �������-����
     * @param sn
     */
    public void addContactList(String sn);

    /**
     * ������� �� �������-�����
     * @param sn
     */
    public void RemoveContactList(String sn);

//    /**
//     * ���������� ��������� ����������
//     * @param server
//     * @param port
//     * @param screenName
//     * @param pass
//     */
//    public void setConnectData(String server, int port, String screenName, String pass);

    /**
     * ���������� ���
     * @return
     */
    public String getScreenName();

    /**
     * ���������� ��������� ������ ����������
     * @return
     */
    public String getLastError();

    /**
     * ��������� ����� ��������� ��������� IM
     * @param e
     */
    public void addProtocolListener(ProtocolListener e);

    /**
     * ������� �������� ���������
     * @param e
     * @return
     */
    public boolean removeProtocolListener(ProtocolListener e);

    /**
     * ���������� ������ ���������� ������� ��������� IM
     * @return
     */
    public List<ProtocolListener> getProtocolListeners();
}
