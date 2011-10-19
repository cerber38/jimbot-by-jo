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

package ru.jimbot.core.api;

/**
 * Процесс, который должен запускаться периодически, или в заданное время
 * @author Prolubnikov Dmitry
 */
public interface Task {

    /**
     * Выполнение заданной функции
     */
    public void exec();

    /**
     * Возвращает период в мс, или 0
     * @return
     */
    public long getPeriod();

    /**
     * Когда запустить (при однократном запуске), иначе 0
     * @return
     */
    public long getStart();

    /**
     * Время последнего запуска (для расчета периодов)
     * @return
     */
    public long getLastStart();

    /**
     * Команда активна?
     * @return
     */
    public boolean isActive();

    /**
     * Установить время запуска команды
     * @param t
     */
    public void setStart(long t);

    /**
     * Установить период для периодического регулярного запуска
     * @param t
     */
    public void setPeriod(long t);

    /**
     * Отключение активности (после выполнения)
     */
    public void disable();
}
