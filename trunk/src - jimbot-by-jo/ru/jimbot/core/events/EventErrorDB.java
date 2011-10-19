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

import ru.jimbot.core.api.DbStatusListener;
import ru.jimbot.core.api.Service;

/**
 * Событие ошибки базы данных.
 * @author Prolubnikov Dmitry
 */
public class EventErrorDB implements Event{
    private String e;
    private Service srv;

    public EventErrorDB(Service srv, String e) {
        this.srv = srv;
        this.e = e;
    }

    public void handle() {
        for(DbStatusListener i:srv.getDbStatusListeners()){
            i.onError(e);
        }
    }
}
