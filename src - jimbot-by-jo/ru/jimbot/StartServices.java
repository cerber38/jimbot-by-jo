/**
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

package ru.jimbot;

import ru.jimbot.core.api.Service;
import ru.jimbot.util.Log;



public class StartServices implements Runnable {
    private Thread th;
    private int sleepAmount = 30*1000;



    public StartServices() {
    sleepAmount = MainConfig.getInstance().getPause()*1000;
    }


    public void start(){
        th = new Thread(this);
        th.setPriority(Thread.NORM_PRIORITY);
        th.start();

    }

    public synchronized void stop() {
        th = null;
        notify();
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (th == me) {
            try {
                th.sleep(sleepAmount);
            } catch (InterruptedException e) { break; }
            startAll();
        }
        th=null;
    }
	/**
	 * Запуск всех сервисов
	 */
	public void startAll() {
        Manager mn = Manager.getInstance();
		for(Service s : mn.getServiceHesh().values()){
			if(s.getConfig().isAutoStart()){
                            Log.getLogger(s.getName()).info("Запускаю сервис: " + s.getName());
                            s.start();
                        }
                }
                stop();
	}


 }

