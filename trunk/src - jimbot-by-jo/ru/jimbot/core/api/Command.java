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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ru.jimbot.core.Message;

/**
 * Интерфейс команды бота.
 *
 * @author Prolubnikov Dmitry
 */
public interface Command {

    /**
     * Инициализация. Вызывается после создания экземпляра класса.
     */
    public void init();

    /**
     * Уничтожение. Вызывается перед удалением команды. Необходимо освободить все ресурсы.
     */
    public void destroy();

    /**
     * Выполнение команды
     * @param m - обрабатываемое сообщение с командой
     * @return - результат (если нужен)
     */
    public Message exec(Message m);


    /**
     * Выполнение команды
     * @param sn - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    @SuppressWarnings("unchecked")
	public String exec(String sn, Vector param);

    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     * @return
     */
    public List<String> getCommandPatterns();

    /**
     * Выводит короткую помощь по команде (1 строка)
     * @return
     */
    public String getHelp();

    /**
     * Выводит подробную помощь по команде
     * @return
     */
    public String getXHelp();

    /**
     * Выводит раздел справки
     * @return
     */
    public String getHelpPart();

    /**
     * Список проверяемых командой объектов полномочий с их описанием
     * @return
     */
    public Map<String, String> getAutorityList();

    /**
     * Проверка полномочий
     * @param s - список полномочий юзера
     * @return  - истина, если команда доступна
     */
    public boolean authorityCheck(Set<String> s);

    /**
     * Проверка полномочий по уину
     * @param screenName
     * @return
     */
    public boolean authorityCheck(String screenName);
}
