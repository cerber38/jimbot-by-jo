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

package ru.jimbot.core.api;

import ru.jimbot.util.Log;

/**
 * @author Prolubnikov Dmitry
 *
 */
public interface IProtocolBuilder {
	public IProtocolBuilder pass(String val);

	public IProtocolBuilder server(String val);

	public IProtocolBuilder port(int val);

	public IProtocolBuilder status(int val);

	public IProtocolBuilder statustxt(String val);

	public IProtocolBuilder xstatus(int val);

	public IProtocolBuilder xstatustxt1(String val);

	public IProtocolBuilder xstatustxt2(String val);

	public IProtocolBuilder logger(Log val);

	public Protocol build();
}
