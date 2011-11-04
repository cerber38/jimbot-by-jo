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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import ru.jimbot.core.UserContext;
import ru.jimbot.modules.chat.ChatCommandParser;
import ru.jimbot.modules.chat.ChatConfig;
import ru.jimbot.modules.chat.ChatQueue;
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.tables.Users;
import ru.jimbot.util.Log;

/**
 * регистрация в чате
 *
 */
public class CmdBan extends DefaultCommand {
    public CmdBan(Parser p) {
        super(p);
       }


    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!ban","!бан"});
    }

    /**
     * Список проверяемых командой объектов полномочий с их описанием
     * @return
     */
    public Map<String, String> getAutorityList(){
       HashMap autority = new HashMap<String, String>();
       autority.put("ban","Забанить пользователя");
       return autority;
    }

    /**
     * Проверка полномочий по уину
     *
     * @param screenName
     * @return
     */

    public boolean authorityCheck(String screenName) {
        ChatWork uw =((ChatService)p.getService()).getChatWork();
        return uw.authorityCheck(screenName, "ban");
    }
    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " - <id/uin> - забанить пользователя (и кикнуть при необходимости)";
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
    
    public Message m;
    /**
     * Выполнение команды
     *
     * @param uin   - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String uin, Vector param){

         ChatWork cw =((ChatService)p.getService()).getChatWork();
         ChatConfig psp =((ChatService)p.getService()).getConfig();
         ChatQueue cq =((ChatService)p.getService()).getChatQueue();
         ChatCommandParser ccp =((ChatCommandParser)p);

         if(!cw.isChat(uin)) return "*NO* сначала нужно зайти в чат!";
        try{
            String s = (String)param.get(0);
            String ms = (String)param.get(1);
            Users us;
            if(s.length()>=6)
                us = cw.getUser(s);
             else
                us = cw.getUser(Integer.parseInt(s));

               if (us.id==0)
            	    return "Такого пользователя несуществует!";

            String i = us.sn;
            if (psp.testAdmin(s)||psp.testAdmin(i)){
                    return "Вы не можете забанить администратора чата! *NO* ";
            }

              if (psp.testAban(us.basesn)||psp.testAban(i))
            	    return psp.getStringProperty("bot.antiban_MSG");

              if(ms.equals(""))
                    return "Необходимо добавить причину бана";

               if(uin.equals(us.sn))
                    return "Нельзя отправить в баню самого себя *NO*";
                 ccp.ban(us.sn, uin,ms);

            cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Пользователь " + i + " успешно отправлен в баню *YES*"));
            cq.addMsg(cw.getUser(i).localnick + " был забанен, причина: "+ ms, i, cw.getUser(i).room);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Ошибка "+ex.getMessage();
        }


         return null;
  }


    /**
     * Выполнение команды
     *
     * @param m - обрабатываемое сообщение с командой
     * @return - результат (если нужен)
     */
    public Message exec(Message m) {
        this.m=m;
        return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), p.getArgs(m, "$c $s")));
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