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

/**
 * ��������� ��������� ������ ��� ���������� IM ����������
 *
 * @author Prolubnikov Dmitry
 */

public interface CommandProtocolListener {

    /**
     * ���������� ������
     * @param id
     * @param text
     */
    public void onChangeStatus(int id, String text);

    /**
     * ���������� �-������
     * @param id
     * @param text1
     * @param text2
     */
    public void onChangeXStatus(int id, String text1, String text2);

    /**
     * ��������� ��������� ���������
     * @param in - �� ����
     * @param out - ����
     * @param text - ���������
     */
    public void sendMessage(String in, String out, String text);

    /**
     * ������������ � �������
     */
    public void logOn();

    /**
     * ����������� �� �������
     */
    public void logOut();

    /**
     * ���������� ��� ���������
     * @return
     */
    public String getScreenName();
}
