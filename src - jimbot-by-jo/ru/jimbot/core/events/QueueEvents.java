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

package ru.jimbot.core.events;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Обработчик очереди событий
 * @author Prolubnikov Dmitry
 */
public class QueueEvents implements Runnable {
    private Queue<Event> evq = new ConcurrentLinkedQueue<Event>();
    private Thread thread;

    public QueueEvents() {

    }

    /**
     * Запуск обработчика очереди
     */
    public synchronized void start() {
        thread = new Thread(this, "EVENTS");
        thread.start();
    }

    /**
     * Остановка обработчика очереди
     */
    public synchronized void stop() {
        clear();
        thread = null;
    }

    /**
     * Добавить новое событие в очередь
     * @param e - событие
     */
    public synchronized void addEvent(Event e) {
        evq.add(e);
    }

    /**
     * Очистить всю очередь событий (например, перед остановкой)
     */
    public synchronized void clear() {
        evq.clear();
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
        while (thread == me) {
            try {
                if(!evq.isEmpty())
                    evq.poll().handle();
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) { break; }
        }
        thread=null;
    }
}
