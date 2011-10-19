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

package ru.jimbot.modules.chat.tasks;

import ru.jimbot.core.api.Parser;
import ru.jimbot.core.api.Task;

/**
 * Проверяет и очищает сессии с истекшим сроком
 *
 * @author Prolubnikov Dmitry
 */
public class CheckSessionTask implements Task {
    private Parser p;
    private long period = 0;
    private long last = 0;
    private boolean enabled = true;

    public CheckSessionTask(Parser p, long period) {
        this.p = p;
        this.period = period;
    }

    public void exec() {
        if(!enabled) return;
        try {
            p.getContextManager().clearExpired();
        } catch (Exception e) {
            // ничего не делаем
        }
        last = System.currentTimeMillis();
    }

    public long getPeriod() {
        return period;
    }

    public long getStart() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public long getLastStart() {
        return last;
    }

    public boolean isActive() {
        return true;
    }

    public void setStart(long t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPeriod(long t) {
        period = t;
    }

    public void disable() {
        enabled = false;
    }
}
