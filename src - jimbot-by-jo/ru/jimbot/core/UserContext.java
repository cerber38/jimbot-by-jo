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

import java.util.HashMap;

/**
 * Контекст пользователя для использования команд
 * @author Prolubnikov Dmitry
 */
public class UserContext {
    private String userId = "";
    private HashMap<String, Object> data = new HashMap<String, Object>();
    private long expired = 0;
    private long maxAge = 0;
    private String mode = "";
    private String lastCommand = "";
    private int count = 0;

    /**
     *
     * @param userId - уин пользователя
     */
    public UserContext(String userId) {
        this.userId = userId;
        maxAge = 60000;
        update();
        count = 0;
    }

    /**
     *
     * @param userId - уин пользователя
     * @param maxAge - время жизни сессии (мс)
     */
    public UserContext(String userId, long maxAge) {
        this.userId = userId;
        this.maxAge = maxAge;
        update();
        count = 0;
    }

    /**
     * Обновить сессию
     */
    public void update() {
        expired = System.currentTimeMillis() + maxAge;
        count ++;
    }

    /**
     * Сохранить данные
     * @param key
     * @param o
     */
    public void putData(String key, Object o) {
        data.put(key, o);
    }

    /**
     * Извлечь данные
     * @param key
     * @return
     */
    public Object getData(String key) {
        return data.get(key);
    }

    /**
     * Уин пользователя
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Все сохраненные данные
     * @return
     */
    public HashMap<String, Object> getData() {
        return data;
    }

    /**
     * Момент окончания сессии
     * @return
     */
    public long getExpired() {
        return expired;
    }

    /**
     * Возвращает счетчик обновлений сессии
     * если 0 - то это новый пользователь, либо новыя сессия после истечения таймаута
     * @return
     */
    public int getCounter() {
    	return count;
    }

    /**
     * Время вышло?
     * @return
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expired;
    }

    /**
     * Время жизни сессии
     * @return
     */
    public long getMaxAge() {
        return maxAge;
    }

    /**
     * Установить время жизни сессии
     * @param maxAge
     */
    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
        update();
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
        update();
    }

    public String getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand(String lastCommand) {
        this.lastCommand = lastCommand;
        update();
    }
}
