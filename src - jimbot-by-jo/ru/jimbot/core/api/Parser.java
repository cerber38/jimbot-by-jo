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

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ru.jimbot.core.ContextManager;
import ru.jimbot.core.Message;

/**
 * Интерфейс для парсера команд
 *
 * @author Prolubnikov Dmitry
 */
public interface Parser {

    /**
     * Возвращает сервис, в котором зарегистрирован этот парсер
     * @return
     */
    public Service getService();

    /**
     * Обработка команды
     * @param m
     */
    public void parse(Message m);

    /**
     * Название команды
     * @param m
     * @return
     */
    public String getCommand(Message m);

    /**
     * Массив аргументов по шаблону
     * @param m
     * @return
     */
    @SuppressWarnings("unchecked")
	public Vector getArgs(Message m, String pattern);

    /**
     * От кого пришла команда
     * @param m
     * @return
     */
    public String getScreenName(Message m);

    /**
     * Добавить новую команду в реестр
     * @param pattern
     * @param cmd
     */
    public void addCommand(String pattern, Command cmd);

    /**
     * Добавить новую команду в реестр с паттерном по умолчанию
     * @param cmd
     */
    public void addCommand(Command cmd);

    /**
     * Список объектов полномочий зарегистрированных команд (для работы остальных модулей и админки)
     * @return
     */
    public Map<String, String> getAuthList();

    /**
     * Возвращает список зарегистрированных команд
     * @return
     */
    public Set<String> getCommands();

    /**
     * Возвращает команду по ее названию
     * @param cmd
     * @return
     */
    public Command getCommand(String cmd);

    /**
     * Возвращает список разрешенных полномочий для пользователя с заданным УИНом
     * @param screenName
     * @return
     */
    public Set<String> getAuthList(String screenName);

    /**
     * Возвращает экземпляр менеджера сессий пользователей
     * @return
     */
    public ContextManager getContextManager();
}
