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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Управляет пользовательскими сессиями
 *
 * @author Prolubnikov Dmitry
 */
public class ContextManager {
    private ConcurrentHashMap<String, UserContext> ss;
    private long defaultAge = 60000;

    public ContextManager() {
        ss = new ConcurrentHashMap<String, UserContext>();
    }

    public ContextManager(long defaultAge) {
        this.defaultAge = defaultAge;
        ss = new ConcurrentHashMap<String, UserContext>();
    }

    public long getDefaultAge() {
        return defaultAge;
    }

    public void setDefaultAge(long defaultAge) {
        this.defaultAge = defaultAge;
    }

    /**
     * Возвращает текущий контекст пользователя
     * Создает новый контекст, если время жизни сессии истекло, или новый юзер
     * @param uid
     * @return
     */
    public UserContext getContext(String uid) {
        if(!ss.containsKey(uid)) ss.put(uid, new UserContext(uid, defaultAge));
        if(ss.get(uid).isExpired()) ss.put(uid, new UserContext(uid, defaultAge));
        return ss.get(uid);
    }

    /**
     * Проверяет наличие сессии пользователя
     * @param uid
     * @return
     */
    public boolean checkContext(String uid) {
        return ss.containsKey(uid);
    }

    /**
     * Добавляет новую сессию
     * @param u
     */
    public void putContext(UserContext u) {
        ss.put(u.getUserId(), u);
    }

    /**
     * Очищает все сессии с истекшим сроком
     */
    public synchronized void clearExpired() {
        for(String c : ss.keySet()) {
            if(ss.get(c).isExpired()) ss.remove(c);
        }
    }

    public synchronized Map<String, UserContext> getAllContexts() {
        return ss;
    }
}
