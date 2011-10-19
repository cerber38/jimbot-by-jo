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
 * Интерфейс для работы с протоколами в боте
 *
 * @author Prolubnikov Dmitry
 */
public interface Protocol {

    /**
     * Внешние данные для работы протокола
     */

    /**
     * Основные данные о соединении
     * @param server
     * @param port
     * @param sn
     * @param pass
     */
    public void setConnectionData(String server, int port, String sn, String pass);

    /**
     * Куда протокол будет писать логи?
     * @param logger
     */
    public void setLogger(Log logger);

    /**
     * Обычный статус
     * @param status
     * @param text
     */
    public void setStatusData(int status, String text);

    /**
     * Расширенный статус
     * @param status
     * @param text1
     * @param text2
     */
    public void setXStatusData(int status, String text1, String text2);

    /**
     * Методы для работы с протоколом
     */

    /**
     * Установить соединение
     */
    public void connect();

    /**
     * Разорвать соединение
     */
    public void disconnect();

//    /**
//     * Установить статус
//     * @param status
//     */
//    public void setStatus(int status);

    /**
     * Соединение установлено?
     * @return
     */
    public boolean isOnLine();

    /**
     * Отправить сообщение
     * @param sn - кому
     * @param msg - текст
     */
    public void sendMsg(String sn, String msg);

//    /**
//     *
//     * @param sn
//     * @param status
//     */
//    public void getStatus(String sn, int status);

    /**
     * Добавить в контакт-лист
     * @param sn
     */
    public void addContactList(String sn);

    /**
     * Удалить из контакт-листа
     * @param sn
     */
    public void RemoveContactList(String sn);

//    /**
//     * Установить параметры соединения
//     * @param server
//     * @param port
//     * @param screenName
//     * @param pass
//     */
//    public void setConnectData(String server, int port, String screenName, String pass);

    /**
     * Возвращает УИН
     * @return
     */
    public String getScreenName();

    /**
     * Возвращает последнюю ошибку соединения
     * @return
     */
    public String getLastError();

    /**
     * Добавляем новый слушатель протокола IM
     * @param e
     */
    public void addProtocolListener(ProtocolListener e);

    /**
     * Удаляем ненужный слушатель
     * @param e
     * @return
     */
    public boolean removeProtocolListener(ProtocolListener e);

    /**
     * Возвращает список слушателей событий протокола IM
     * @return
     */
    public List<ProtocolListener> getProtocolListeners();
}
