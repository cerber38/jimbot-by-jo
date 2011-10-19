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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ru.jimbot.core.Message;

/**
 * ��������� ������� ����.
 *
 * @author Prolubnikov Dmitry
 */
public interface Command {

    /**
     * �������������. ���������� ����� �������� ���������� ������.
     */
    public void init();

    /**
     * �����������. ���������� ����� ��������� �������. ���������� ���������� ��� �������.
     */
    public void destroy();

    /**
     * ���������� �������
     * @param m - �������������� ��������� � ��������
     * @return - ��������� (���� �����)
     */
    public Message exec(Message m);


    /**
     * ���������� �������
     * @param sn - �� ����?
     * @param param - ������ ���������� (����� ���� ��� ������, ��� � �����)
     * @return - ��������� (���� �����)
     */
    @SuppressWarnings("unchecked")
	public String exec(String sn, Vector param);

    /**
     * ������ �������� ����, �� ������� ����� ������� ��� �������
     * @return
     */
    public List<String> getCommandPatterns();

    /**
     * ������� �������� ������ �� ������� (1 ������)
     * @return
     */
    public String getHelp();

    /**
     * ������� ��������� ������ �� �������
     * @return
     */
    public String getXHelp();

    /**
     * ������� ������ �������
     * @return
     */
    public String getHelpPart();

    /**
     * ������ ����������� �������� �������� ���������� � �� ���������
     * @return
     */
    public Map<String, String> getAutorityList();

    /**
     * �������� ����������
     * @param s - ������ ���������� �����
     * @return  - ������, ���� ������� ��������
     */
    public boolean authorityCheck(Set<String> s);

    /**
     * �������� ���������� �� ����
     * @param screenName
     * @return
     */
    public boolean authorityCheck(String screenName);
}
