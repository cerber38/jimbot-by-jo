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
import java.util.HashMap;
import java.util.Set;


/**
 * ����� ������� ��� ������. ����� �� ������������� �� � ������� ��������.
 *
 * @author Prolubnikov Dmitry
 */
public abstract class DefaultCommand implements Command {
    protected Parser p;

    protected DefaultCommand(Parser p) {
        this.p = p;
    }

    /**
     * �������������. ���������� ����� �������� ���������� ������.
     */
    public void init() {
        // ������ �� ������
    }

    public void destroy() {
        // ������ �� ������
    }

    /**
     * ������ ����������� �������� �������� ���������� � �� ���������
     *
     * @return
     */
    public Map<String, String> getAutorityList() {
        // ������ ������
        return new HashMap<String, String>();
    }

    /**
     * �������� ����������
     *
     * @param s - ������ ���������� �����
     * @return - ������, ���� ������� ��������
     */
    public boolean authorityCheck(Set<String> s) {
        return true;
    }

    /**
     * �������� ���������� �� ����
     *
     * @param screenName
     * @return
     */
    public boolean authorityCheck(String screenName) {
        return authorityCheck(p.getAuthList(screenName));
    }
  
    /**
     * ������� ������ �������
     * @return
     */
    public String getHelpPart(){
        return "---";
    }

}
