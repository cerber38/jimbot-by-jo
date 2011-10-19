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

package ru.jimbot.modules.chat;



   /**
     * Элемент очереди сообщений
     * @author spec
     *
     */
    public class MsgElement {
        public String msg="";
        public int countID=0;
        public String userSN=""; // Отсекать посылку своих сообщений себе
        public int room=0;

        MsgElement(String s, int id, String user, int room) {
            msg = s;
            countID = id;
            userSN = user;
            this.room = room;
        }
    }
