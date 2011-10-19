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

import ru.jimbot.core.ChronoMaster;
import ru.jimbot.core.Message;
import ru.jimbot.core.MsgInQueue;
import ru.jimbot.core.MsgOutQueue;
import ru.jimbot.core.events.Event;

import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ��������� ��� ���� �������� ����
 *
 * @author Prolubnikov Dmitry
 */
public interface Service {
    /**
     * ���������� DefaultCommandParser
     * @return
     */
     public DefaultCommandParser getCommandParser();
    /**
     * ��������� � ������� ������ �������
     * @param e
     */
    public void createEvent(Event e);

    /**
     * ������ �������
     */
    public void start();

    /**
     * ��������� �������
     */
    public void stop();

    /**
     * ������ �������?
     * @return
     */
    public boolean isRun();

    /**
     * ���������� ��������� ��������� �� ����
     * @param screenName
     * @return
     */
    public Protocol getProtocol(String screenName);

    /**
     * ������ ����� �� ���� ������������� ����������
     * @return
     */
    public Set<String> getAllProtocols();

    /**
     * ���������� ��� ������� �������
     * @return
     */
    public String getName();

    /**
     * ���������� ��� ������� �������
     * @return
     */
    public String getType();

    /**
     * ����� �������� �������
     * @return
     */
    public ServiceConfig getConfig();

    /**
     * ����� HTTP �������� �������
     * @return
     */
    public IHTTPServiceConfig getHTTPConfig();

//    /**
//     * ��������� ������ ��� ������ � ��
//     * @return
//     */
//    public DBAdaptor getDB();

    /**
     * �������� ������ � ��������� ������
     * @param key
     * @param o
     */
    public void addDataStorage(String key, Object o);

    /**
     * �������� ������ �� ���������
     * @param key
     * @return
     */
    public Object getDataStorage(String key);

    /**
     * ���������� ������� ��������
     * @return
     */
    public MsgInQueue getInQueue();

    /**
     * ���������� ������� ��������� ��� ��������� ����
     * @return
     */
    public ConcurrentLinkedQueue<Message> getOutQueue(String sn);

    /**
     * ���������� ������� ���������
     * @return
     */
    public MsgOutQueue getOutQueue();

    public List<DbStatusListener> getDbStatusListeners();
    void addDbStatusListener(DbStatusListener e);
    boolean removeDbStatusListener(DbStatusListener e);
//    public void addProtocolListener(ProtocolListener e);
//    public boolean removeProtocolListener(ProtocolListener e);
//    public List<ProtocolListener> getProtocolListeners();
    public void addParserListener(QueueListener e);
    public boolean removeParserListener(QueueListener e);
    public List<QueueListener> getParserListeners();
    public void addCommandProtocolListener(CommandProtocolListener e);
    public void removeCommandProtocolListener(CommandProtocolListener e);
    public Collection<CommandProtocolListener> getCommandProtocolListeners();
    public CommandProtocolListener getCommandProtocolListener(String screenName);
    public void addOutQueueListener(QueueListener e);
    public boolean removeOutQueueListener(QueueListener e);
    public List<QueueListener> getOutQueueListeners();

    /**
     * ���������� ��������� ������������ �����
     * @return
     */
    public ChronoMaster getCron();
}