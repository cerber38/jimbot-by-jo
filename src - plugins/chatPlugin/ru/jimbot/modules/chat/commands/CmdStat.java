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

package ru.jimbot.modules.chat.commands;

import ru.jimbot.core.Message;
import ru.jimbot.core.api.DefaultCommand;
import ru.jimbot.core.api.Parser;
import ru.jimbot.modules.chat.ChatService;
import ru.jimbot.modules.chat.ChatCommandParser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import java.util.Date;
import java.util.Arrays;

/**
 * Статистика
 *
 * @author Prolubnikov Dmitry
 */
public class CmdStat extends DefaultCommand {
    public CmdStat(Parser p) {
        super(p);
    }

    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " - статистика работы бота";
    }

    /**
     * Выводит подробную помощь по команде
     *
     * @return
     */
    public String getXHelp() {
        return getHelp();
    }

    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!stat"});
    }

    /**
     * Выводит раздел справки
     * @return
     */
    public String getHelpPart(){
        return "Информационные";
    }      
    
    /**
     * Выполнение команды
     *
     * @param sn    - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String sn, Vector param) {
      //  String s = "Всего в базе анекдотов: " + ((ChatService)p.getService()).getChatWork().count();
      //  String s = "\nОтправлено анекдотов: " + ((ChatCommandParser)p).state;
      //  s += "\nДобавлено анекдотов: " + ((ChatCommandParser)p).state_add;
       String s = "\nУникальных UIN: " + ((ChatCommandParser)p).uq.size();
        s += "\nАктивных в данный момент: " + p.getContextManager().getAllContexts().size();
        s += "\nБот запущен: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(((ChatCommandParser)p).getTimeStart()));
        s += "\nВремя работы: " + ((ChatCommandParser)p).getTime(((ChatCommandParser)p).getUpTime());
       // s += "\nВ среднем анекдотов в час: " + ((ChatCommandParser)p).getHourStat();
       // s += "\nВ среднем анекдотов в сутки: " + ((ChatCommandParser)p).getDayStat();
      //  s += "\nПрочитано вами анекдотов: " + ((ChatCommandParser)p).uq.get(sn).cnt;
        if(p.getService().getConfig().testAdmin(sn)){
            s += "\nИспользовано памяти: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        }
        return s;
    }

    /**
     * Выполнение команды
     *
     * @param m - обрабатываемое сообщение с командой
     * @return - результат (если нужен)
     */
    public Message exec(Message m) {
        return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), null));
    }
}