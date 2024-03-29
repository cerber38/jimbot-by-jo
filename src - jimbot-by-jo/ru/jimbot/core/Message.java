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

package ru.jimbot.core;

/**
 * ���������
 * @author Prolubnikov Dmitry
 */
public class Message {
    public static final int TYPE_TEXT = 0;            // ��������� ���������
    public static final int TYPE_STATUS = 1;          // ��������� � �������
    public static final int TYPE_INFO = 2;
    public static final int TYPE_FLOOD_NOTICE = 3;    // ��������� �������� ��� ���� (��� �������� ������� � ���������� �����)

    private String snIn = "";  // �� ����
    private String snOut = ""; // ����
    private String msg = "";   // ����� ���������
    private int type = 0;      // ��� ���������
    private int status = 0;    // ������, ��� ��������� � �������
    private long time = 0;     // ����� ��������� ��������� (��� ��������)

    public Message(String snIn, String snOut, String msg, int type) {
        this.snIn = snIn;
        this.snOut = snOut;
        this.msg = msg;
        this.type = type;
        this.time = System.currentTimeMillis();
    }
  /**
   *
   * @param snIn �� ����
   * @param snOut ����
   * @param msg ���������
   */
    public Message(String snIn, String snOut, String msg) {
        this.snIn = snIn;
        this.snOut = snOut;
        this.msg = msg;
        this.type = TYPE_TEXT;
        this.time = System.currentTimeMillis();
    }

    /**
     * ���������� ����� ����, �� � ������ �������
     * (��������� ��� �������� ��������� �� ����� ��� �������� ����� ��������� ������ ��������)
     * @return
     */
    public Message getCopy(String s) {
        Message m = new Message(snIn, snOut, s, type);
        m.setTime(time);
        return m;
    }

    public String getSnIn() {
        return snIn;
    }

    public void setSnIn(String snIn) {
        this.snIn = snIn;
    }

    public String getSnOut() {
        return snOut;
    }

    public void setSnOut(String snOut) {
        this.snOut = snOut;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
