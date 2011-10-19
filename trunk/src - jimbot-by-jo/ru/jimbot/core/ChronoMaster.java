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

package ru.jimbot.core;

import java.util.Vector;

import ru.jimbot.core.api.Task;

/**
 * Управляет периодически запускаемыми процессами
 * @author Prolubnikov Dmitry
 */
public class ChronoMaster implements Runnable{
    private Vector<Task> vec;
    private int count = 0;
    private Thread th;

    public ChronoMaster() {
        vec = new Vector<Task>();
    }

    /**
     * Выполнить следующее событие
     */
    private void execNext() {
        if(vec.size()==0) return;
        try {
            if (count >= vec.size()) count = 0;
            Task c = vec.get(count);
            if (c.isActive()) {
                if (c.getStart() != 0 && (System.currentTimeMillis() > c.getStart())) {
                    c.exec();
                    c.disable();
                } else if (c.getPeriod() != 0 && (System.currentTimeMillis() > (c.getLastStart() + c.getPeriod()))) {
                    c.exec();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            count++;
        }
    }

    /**
     * Очищает отключенные объекты из списка
     */
    private void clearDisabled() {
        try {
            if(count>=vec.size()) return;
            if(!vec.get(count).isActive()) {
                vec.remove(count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Добавляет новое задание в список
     * @param c
     */
    public int addTask(Task c) {
        vec.add(c);
        return vec.size()-1;
    }

    public void disableTask(int i) {
        if(i>=vec.size()) return;
        vec.get(i).disable();
    }

    /**
     * Запуск процесса обработки событий
     */
    public void start() {
        th = new Thread(this, "ChronoMaster");
        th.start();
    }

    /**
     * Остановка
     */
    public void stop() {
        th=null;
        clear();
    }

    /**
     * Очистка списка событий
     */
    public void clear() {
        vec.clear();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        Thread me = Thread.currentThread();
        while (th == me) {
            execNext();
            clearDisabled();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { break; }
        }
        th=null;
    }
}
