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
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.tables.Users;


/**
 * список администрации чата
 *
 */
public class CmdUnicodeOff extends DefaultCommand {
    public CmdUnicodeOff(Parser p) {
        super(p);
    }

    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!выкл","!vikl"});
    }

//    /**
//     * Список проверяемых командой объектов полномочий с их описанием
//     * @return
//     */
//    public Map<String, String> getAutorityList(){
//       HashMap autority = new HashMap<String, String>();
//       autority.put("adm", "список администрации чата");
//       return autority;
//    }

   /**
     * Проверка полномочий по уину
     *
     * @param screenName
     * @return
     */

    public boolean authorityCheck(String screenName) {
        return true;
    }
    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " - отключить поддержку символов Unicode";
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
     * Выводит раздел справки
     * @return
     */
    public String getHelpPart(){
        return "Различные действия";
    }
    /**
     * Выполнение команды
     *
     * @param sn    - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String sn, Vector param) {
       ChatWork uw =((ChatService)p.getService()).getChatWork();
        try{
            Users us = uw.getUser(sn);
            us.unicode=0;
            uw.updateUser(us);
           return "Поддержка символов Unicode отключена!";
       } catch (Exception ex) {
         ex.printStackTrace();
         return "Ошибка "+ex.getMessage();
       }
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

    /**
     * Проверка полномочий
     *
     * @param s - список полномочий юзера
     * @return - истина, если команда доступна
     */
    public boolean authorityCheck(Set<String> s) {
        return true;
    }


}