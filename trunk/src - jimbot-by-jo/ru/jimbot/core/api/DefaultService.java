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
import ru.jimbot.core.events.QueueEvents;
import ru.jimbot.core.events.Event;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Определение общих для всех сервисов функций - добавление и удаление слушателей
 *
 * @author Prolubnikov Dmitry
 */
public abstract class DefaultService implements Service {
    private HashMap<String, CommandProtocolListener> comProtList = new HashMap<String, CommandProtocolListener>();
    private List<QueueListener> outQueueList = new Vector<QueueListener>();
    private List<QueueListener> parserList = new Vector<QueueListener>();
    private List<DbStatusListener> dbList = new Vector<DbStatusListener>();
    protected HashMap<String, Protocol> protocols = new HashMap<String, Protocol>();
    private HashMap<String, Object> storage = new HashMap<String, Object>();
    protected MsgInQueue inq;
    protected MsgOutQueue outq;
    protected QueueEvents qe = new QueueEvents();
    private ChronoMaster cron = new ChronoMaster();

    /**
     * Возвращает экземпляр планировщика задач
     * @return
     */
    public ChronoMaster getCron() {
        return cron;
    }

    /**
     * Добавлене в очередь нового события
     *
     * @param e
     */
    public void createEvent(Event e) {
        qe.addEvent(e);
    }

    /**
     * Удалить ссылки на все слушатели (при завершении работы сервиса)
     */
    public void removeAllListeners() {
        comProtList.clear();
//        protList.clear();
        outQueueList.clear();
        parserList.clear();
        dbList.clear();
        for(Protocol i:protocols.values()) {
            i.getProtocolListeners().clear();
        }
    }

    /**
     * Засунуть объект в хранилище данных
     *
     * @param key
     * @param o
     */
    public void addDataStorage(String key, Object o) {
        storage.put(key, o);
    }

    /**
     * Получить объект из хранилища
     *
     * @param key
     * @return
     */
    public Object getDataStorage(String key) {
        return storage.get(key);
    }

    /**
     * Возвращает очередь входящих
     *
     * @return
     */
    public MsgInQueue getInQueue() {
        return inq;
    }

    /**
     * Возвращает очередь исходящих для заданного уина
     *
     * @return
     */
    public ConcurrentLinkedQueue<Message> getOutQueue(String sn) {
        return outq.getUinQueue(sn);
    }

    /**
     * возвращает очередь исходящих
     *
     * @return
     */
    public MsgOutQueue getOutQueue() {
        return outq;
    }

    /**
     * Добавляет новый уин и объект протокола
     * @param screenName
     * @param p
     */
    public void addProtocol(String screenName, Protocol p) {
        protocols.put(screenName, p);
    }

    /**
     * Возвращает по уину объект протокола
     * @param screenName
     * @return
     */
    public Protocol getProtocol(String screenName) {
        return protocols.get(screenName);
    }

    /**
     * Список УИНов ко всем установленным протоколам
     *
     * @return
     */
    public Set<String> getAllProtocols() {
        return protocols.keySet();
    }

    /**
     * Добавить слушатель команд для управления протоколом IM
     * @param e
     */
    public void addCommandProtocolListener(CommandProtocolListener e) {
        comProtList.put(e.getScreenName(), e);
    }

    /**
     * Удаляет слушатель команд для управления протоколом IM
     * @param e
     * @return
     */
    public void removeCommandProtocolListener(CommandProtocolListener e) {
        comProtList.remove(e.getScreenName());
    }

    /**
     * Возвращает список слушателей
     * @return
     */
    public Collection<CommandProtocolListener> getCommandProtocolListeners() {
        return comProtList.values();
    }

    /**
     * Возвращает слушателя с нужным УИНом
     * @param screenName
     * @return
     */
    public CommandProtocolListener getCommandProtocolListener(String screenName) {
        return comProtList.get(screenName);
    }

    /**
     *
     * @param e
     */
    public void addOutQueueListener(QueueListener e) {
        outQueueList.add(e);
    }

    /**
     *
     * @param e
     * @return
     */
    public boolean removeOutQueueListener(QueueListener e) {
        return outQueueList.remove(e);
    }

    /**
     *
     * @return
     */
    public List<QueueListener> getOutQueueListeners() {
        return outQueueList;
    }

    /**
     *
     * @param e
     */
    public void addParserListener(QueueListener e) {
        parserList.add(e);
    }

    /**
     *
     * @param e
     * @return
     */
    public boolean removeParserListener(QueueListener e) {
        return parserList.remove(e);
    }

    /**
     *
     * @return
     */
    public List<QueueListener> getParserListeners() {
        return parserList;
    }

    public void addDbStatusListener(DbStatusListener e) {
        dbList.add(e);
    }

    public boolean removeDbStatusListener(DbStatusListener e) {
        return dbList.remove(e);
    }

    public List<DbStatusListener> getDbStatusListeners() {
        return dbList;
    }
}
