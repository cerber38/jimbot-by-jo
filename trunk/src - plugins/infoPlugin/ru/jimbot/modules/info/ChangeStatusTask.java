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

package ru.jimbot.modules.info;

import ru.jimbot.core.api.Service;
import ru.jimbot.core.api.Task;
import ru.jimbot.core.events.CommandProtocolChangeXStatusEvent;

import java.util.Vector;

/**
 * Периодическая смена х-статуса
 * 
 * @author Prolubnikov Dmitry
 */
public class ChangeStatusTask implements Task {
    private Service srv;
    private Vector<String> txt1 = new Vector<String>();
    private Vector<String> txt2 = new Vector<String>();
    private Vector<Integer> stat = new Vector<Integer>();
    private long period = 0;
    private int cnt = 0;
    private boolean enabled = true;
    private long lastStart = 0;
    private long countStart = 0; // число запусков задачи
    private InfoWork iw = null;

    /**
     * Период запуска задачи
     * @param period
     */
    public ChangeStatusTask(Service srv, long period) {
        this.srv = srv;
        this.period = period;
    }

    /**
     * Конструктор для показа рекламы из анекбота
     * @param srv
     * @param period
     * @param aw
     */
    public ChangeStatusTask(Service srv, long period, InfoWork iw) {
        this.srv = srv;
        this.period = period;
        this.iw = iw;
    }

    /**
     * Добавить в спсок новый статус
     * @param i
     * @param s1
     * @param s2
     */
    public void addStatus(int i, String s1, String s2) {
        stat.add(i);
        txt1.add(s1);
        txt2.add(s2);
    }

    /**
     * Выполнение заданной функции
     */
    public void exec() {
        lastStart = System.currentTimeMillis();
        if(countStart==0){
            // пропустим первый запуск, наверняка номера в этот момент еще не успели выйти в сеть.
            countStart++;
            return;
        }
//        System.out.println("exec ChangeStatusTask");
        if(iw==null) {
            if (stat.isEmpty()) return;
            if (cnt >= stat.size()) cnt = 0;
            srv.createEvent(new CommandProtocolChangeXStatusEvent(srv, stat.get(cnt), txt1.get(cnt), txt2.get(cnt)));
//            System.out.println("iw==null "+txt1.get(cnt));
            cnt++;
        } else {
            String s1 = iw.getAdsForStatus();
//            System.out.println("getAdsForStatus() "+s1);
            if(s1.equals("")) return;
            if(cnt>15) cnt=0;
            srv.createEvent(new CommandProtocolChangeXStatusEvent(srv, cnt+1, "Реклама :) ", s1));
            cnt++;
        }
        countStart++;
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
        return 0;
    }

    /**
     * Время последнего запуска (для расчета периодов)
     *
     * @return
     */
    public long getLastStart() {
        return lastStart;
    }

    /**
     * Команда активна?
     *
     * @return
     */
    public boolean isActive() {
        return enabled;
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
