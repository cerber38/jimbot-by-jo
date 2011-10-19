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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import ru.jimbot.modules.chat.ChatConfig;
import ru.jimbot.modules.chat.ChatQueue;
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.tables.Users;

/**
 * переход по комнатам
 *
 */
public class CmdGoRoom extends DefaultCommand {
    public CmdGoRoom(Parser p) {
        super(p);
    }
 public Message m;
    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!room","!комната","!к"});
    }

    /**
     * Список проверяемых командой объектов полномочий с их описанием
     * @return
     */
    public Map<String, String> getAutorityList(){
       HashMap autority = new HashMap<String, String>();
       autority.put("anyroom", "возможность перехода по комнатам");
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
        return uw.authorityCheck(screenName, "anyroom");
    }


    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " <id> - перейти в комнату";
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
     * @param sn  - от кого?
     * @param v - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String uin, Vector v) {
         ChatWork uw =((ChatService)p.getService()).getChatWork();
         Users uss = uw.getUser(uin);
         ChatConfig psp =((ChatService)p.getService()).getConfig();
         ChatQueue cq =((ChatService)p.getService()).getChatQueue();
       if(!uw.isChat(uin)) return "*NO* сначало нужно зайти в чат!";

        try{
            int i = (Integer)v.get(0);
            String pass = (String)v.get(1);
        if(uss.room==i){
                return "*NO* Ты уже сидиш в этой комнате!";
            } else
                if(uw.checkRoom(i)){
                if (!uw.getRoom(i).checkPass(pass) && !psp.testAdmin(uin)) {
			return "Не верный пароль!";
			}
                cq.addMsg(psp.getStringProperty("auth.group_prist_"+uss.group)+" "+uss.localnick + " ушел из комнаты [" + uss.room + "] в комнату [" + i + " - " + uw.getRoom(i).getName() + "]", m.getSnIn(), uss.room);
                uss.room=i;
                uw.updateUser(uss);
                cq.changeUserRoom(uin, i);
                cq.addMsg(psp.getStringProperty("auth.group_prist_"+uss.group)+" "+uss.localnick + " вошел в комнату [" + uss.room + "]", m.getSnIn(), uss.room);
                cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Ты перешел в комнату " + i + " - " + uw.getRoom(i).getName() +
                        (uw.getRoom(i).getTopic().equals("") ? "" : ("\nТема: " + uw.getRoom(i).getTopic()))));
            } else {
                cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Такой комнаты не существует! Некуда переходить."));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
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
        return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), p.getArgs(m, "$n $c")));
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