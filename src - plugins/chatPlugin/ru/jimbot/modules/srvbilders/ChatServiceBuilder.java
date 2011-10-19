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

package ru.jimbot.modules.srvbilders;

import ru.jimbot.modules.chat.*;
import ru.jimbot.core.api.IServiceBuilder;

/**
 * @author Prolubnikov Dmitry
 *
 */
public class ChatServiceBuilder implements IServiceBuilder {

	
	public ChatService build(String name) {
    	return new ChatService(name);

    }

	/* (non-Javadoc)
	 * @see ru.jimbot.core.IServiceBuilder#getServiceType()
	 */
	public String getServiceType() {
		return "chat";
	}

	/* (non-Javadoc)
	 * @see ru.jimbot.core.api.IServiceBuilder#createServiceData(java.lang.String)
	 */
	public boolean createServiceData(String name) {
		// TODO Создать папку, файлы и базу данных для бота
		return true;
	}

	/* (non-Javadoc)
	 * @see ru.jimbot.core.api.IServiceBuilder#deleteServiceData(java.lang.String)
	 */
	public boolean deleteServiceData(String name) {
		// TODO Удалить папку, файлы и базу данных бота
		return true;
	}
	
	
}
