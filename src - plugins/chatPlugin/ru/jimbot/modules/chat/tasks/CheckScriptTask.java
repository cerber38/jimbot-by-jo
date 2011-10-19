/*
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2009 JimBot project
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


import ru.jimbot.core.ScriptServer;
import ru.jimbot.core.api.Parser;
import ru.jimbot.core.api.Task;

/**
 * Периодическая проверка скриптов на обновления и их установка при необходимости
 *
 * @author Prolubnikov Dmitry
 */
public class CheckScriptTask implements Task {
    private Parser p;
    private ScriptServer scr;
    private long period = 0;
    private long last = 0;
    private boolean enabled = true;

    public CheckScriptTask(Parser p, long period) {
        this.p = p;
        this.period = period;
        scr = new ScriptServer(p);
    }

    /**
     * Выполнение заданной функции
     */
    public void exec() {
        if(!enabled) return;
        scr.refreshAllCommandScripts();
        last = System.currentTimeMillis();
    }

    /**
     * Возвращает период в мс, или 0
     *
     * @return
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Когда запустить (при однократном запуске), иначе 0
     *
     * @return
     */
    public long getStart() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Время последнего запуска (для расчета периодов)
     *
     * @return
     */
    public long getLastStart() {
        return last;
    }

    /**
     * Команда активна?
     *
     * @return
     */
    public boolean isActive() {
        return true;
    }

    /**
     * Установить время запуска команды
     *
     * @param t
     */
    public void setStart(long t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Установить период для периодического регулярного запуска
     *
     * @param t
     */
    public void setPeriod(long t) {
        period = t;
    }

    /**
     * Отключение активности (после выполнения)
     */
    public void disable() {
        enabled = false;
    }
}
