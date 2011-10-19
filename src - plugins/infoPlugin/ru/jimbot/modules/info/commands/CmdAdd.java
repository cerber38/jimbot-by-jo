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

import ru.jimbot.core.Message;
import ru.jimbot.core.api.DefaultCommand;
import ru.jimbot.core.api.Parser;
import ru.jimbot.util.Log;

import java.util.Vector;
import java.util.List;
import java.util.Arrays;
import ru.jimbot.modules.info.InfoCommandParser;
import ru.jimbot.modules.info.InfoService;


/**
 * Добавление анекдота
 * 
 * @author Prolubnikov Dmitry
 */
public class CmdAdd extends DefaultCommand {
    public CmdAdd(Parser p) {
        super(p);
    }

    /**
     * Выполнение команды
     *
     * @param m - обрабатываемое сообщение с командой
     * @return - результат (если нужен)
     */
    public Message exec(Message m) {
        return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), p.getArgs(m, "$s")));
    }

    /**
     * Выполнение команды
     *
     * @param sn    - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String sn, Vector param) {
        String s = (String) param.get(0);
        if (s.equals("")) return "Пустой анекдот.";
        if (s.length() < 20) return "";
        if (s.length() > 500) return "";
        try {
			((InfoService) p.getService()).getInfoWork().addTempAnek(s, sn);
			Log.getLogger(p.getService().getName()).talk("Add anek <" + sn + ">: " + s);
	        ((InfoCommandParser) p).state_add++;
	        return "Анекдот сохранен. После рассмотрения администрацией он будет добавлен в базу.";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return "";
    }

    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!add","!доб"});
    }

    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " <анекдот> - Добавить новый анекдот";
    }

    /**
     * Выводит подробную помощь по команде
     *
     * @return
     */
    public String getXHelp() {
        return getHelp();
    }


}
