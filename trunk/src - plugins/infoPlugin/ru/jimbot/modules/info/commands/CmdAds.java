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

package ru.jimbot.modules.info.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import ru.jimbot.core.Message;
import ru.jimbot.core.UserContext;
import ru.jimbot.core.api.DefaultCommand;
import ru.jimbot.core.api.Parser;
import ru.jimbot.modules.info.InfoService;
import ru.jimbot.modules.info.InfoWork;


/**
 * Работа с рекламой
 * 
 * @author Prolubnikov Dmitry
 *
 */
public class CmdAds extends DefaultCommand {

	protected CmdAds(Parser p) {
		super(p);
	}

	public Message exec(Message m) {
		UserContext c = p.getContextManager().getContext(m.getSnIn());
		InfoWork iw = ((InfoService)p.getService()).getInfoWork();
		String s = "Активных рекламных объявлений: " + iw.CountAds() +
			"\nВыберите действие: \n0 - выход\n1 - список активных объявлений" +
			"\n2 - удалить\n3 - добавить\n4 - продлить";
		String k = c.getData("cmd")==null ? "" : (String)c.getData("cmd");
		if("".equals(c.getLastCommand())) {
			c.setLastCommand("!ads");
		} else if("2".equals(k)) {
			s = iw.delAds(Integer.parseInt(m.getMsg())) ? "Выполнено." : "Произошла ошибка.";
			c.getData().remove("cmd");
		} else if("3".equals(k)) {
			c.getData().put("txt", m.getMsg());
			c.getData().put("cmd", "31");
			s = "Примечание: ";
		} else if("31".equals(k)) {
			int id = iw.addAds((String)c.getData().get("txt"), m.getMsg(), m.getSnIn());
			s = id<0 ? "Ошибка добавления" : "Добавлено новое объявление " + id;
			c.getData().remove("txt");
			c.getData().remove("cmd");
		}else if("4".equals(k)) {
			s = iw.extendAds(Integer.parseInt(m.getMsg())) ? "Выполнено." : "Произошла ошибка.";
			c.getData().remove("cmd");
		} else if("0".equals(m.getMsg())) {
			c.setLastCommand("");
			s = "Сеанс работы с рекламными сообщениями окончен";
		} else if("1".equals(m.getMsg())) {
			s = iw.adsList();
		} else if("2".equals(m.getMsg())) {
			s = "Номер удаляемого объявления: ";
			c.getData().put("cmd", "2");
		} else if("3".equals(m.getMsg())) {
			s = "Текст объявления: ";
			c.getData().put("cmd", "3");
		} else if("4".equals(m.getMsg())) {
			s = "Номер продляемого объявления: ";
			c.getData().put("cmd", "4");
		} else {

		}
		c.update();
		return new Message(m.getSnOut(), m.getSnIn(), s);
	}

	public String exec(String sn, Vector param) {
		return null;
	}

	public List<String> getCommandPatterns() {
		return Arrays.asList(new String[] {"!ads","!реклама","!рек"});
	}

	public String getHelp() {
		return " - работа с рекламными сообщениями";
	}

	public String getXHelp() {
		return getHelp();
	}

	/**
     * Проверка полномочий по уину
     *
     * @param screenName
     * @return
     */
    @Override
    public boolean authorityCheck(String screenName) {
        return p.getService().getConfig().testAdmin(screenName);
    }
}
