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

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ru.jimbot.core.ContextManager;
import ru.jimbot.core.Message;

/**
 * ��������� ��� ������� ������
 *
 * @author Prolubnikov Dmitry
 */
public interface Parser {

    /**
     * ���������� ������, � ������� ��������������� ���� ������
     * @return
     */
    public Service getService();

    /**
     * ��������� �������
     * @param m
     */
    public void parse(Message m);

    /**
     * �������� �������
     * @param m
     * @return
     */
    public String getCommand(Message m);

    /**
     * ������ ���������� �� �������
     * @param m
     * @return
     */
    @SuppressWarnings("unchecked")
	public Vector getArgs(Message m, String pattern);

    /**
     * �� ���� ������ �������
     * @param m
     * @return
     */
    public String getScreenName(Message m);

    /**
     * �������� ����� ������� � ������
     * @param pattern
     * @param cmd
     */
    public void addCommand(String pattern, Command cmd);

    /**
     * �������� ����� ������� � ������ � ��������� �� ���������
     * @param cmd
     */
    public void addCommand(Command cmd);

    /**
     * ������ �������� ���������� ������������������ ������ (��� ������ ��������� ������� � �������)
     * @return
     */
    public Map<String, String> getAuthList();

    /**
     * ���������� ������ ������������������ ������
     * @return
     */
    public Set<String> getCommands();

    /**
     * ���������� ������� �� �� ��������
     * @param cmd
     * @return
     */
    public Command getCommand(String cmd);

    /**
     * ���������� ������ ����������� ���������� ��� ������������ � �������� �����
     * @param screenName
     * @return
     */
    public Set<String> getAuthList(String screenName);

    /**
     * ���������� ��������� ��������� ������ �������������
     * @return
     */
    public ContextManager getContextManager();
}
