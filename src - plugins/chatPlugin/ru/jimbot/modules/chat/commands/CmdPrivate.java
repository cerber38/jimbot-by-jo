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
import ru.jimbot.modules.chat.ChatCommandParser;
import ru.jimbot.modules.chat.ChatConfig;
import ru.jimbot.modules.chat.ChatQueue;
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.tables.Users;
import ru.jimbot.util.Log;

/**
 * переход по комнатам
 *
 */
public class CmdPrivate extends DefaultCommand {
    public CmdPrivate(Parser p) {
        super(p);
    }
 public Message m;
    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"+р","+p","+п","+лс"});
    }

    /**
     * Список проверяемых командой объектов полномочий с их описанием
     * @return
     */
    public Map<String, String> getAutorityList(){
       HashMap autority = new HashMap<String, String>();
       autority.put("pmsg", "Отправка приватных сообщений");
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
        return uw.authorityCheck(screenName, "pmsg");
    }
    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " <id> <сообщение> - Отправить приватное сообщение";
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
     * @param uin - от кого?
     * @param v - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String uin, Vector v) {
         ChatWork uw =((ChatService)p.getService()).getChatWork();
         ChatConfig psp =((ChatService)p.getService()).getConfig();
         ChatQueue cq =((ChatService)p.getService()).getChatQueue();
         ChatCommandParser ccp =((ChatCommandParser)p);
       if(!uw.isChat(uin)) return "*NO* сначало нужно зайти в чат!";
     try {
         int no = ((Integer)v.get(0)).intValue();
         String txt = (String)v.get(1);
          if ((txt.equals("")) || (txt.equals(" "))) {
             return "Сообщение отсутствует";
          }
         Users uss = uw.getUser(no);
         Users us = uw.getUser(uin);
          if (uss == null) {
             return "Такого пользователя не существует";
          }
          if (!(cq.testUser(uss.sn))) {
             return "Пользователь не в сети";
          }
          if (txt.length() > psp.getIntProperty("chat.MaxMsgSize")) {
            txt = txt.substring(0, psp.getIntProperty("chat.MaxMsgSize"));
            cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(), "Слишком длинное сообщение было обрезано: " + txt));
          }
        String s = psp.getStringProperty("uin.private");
        String[] ss = s.split(";");
          for (int i = 0; i < ss.length; ++i) {
              Users usss = uw.getUser(ss[i]);
              String snOut= "".equals(usss.basesn) ? m.getSnOut(): usss.basesn;
         //   srv.getIcqProcess(usss.basesn).mq.add(ss[i],this.srv.us.getUser(uin).localnick + "|" + this.srv.us.getUser(uin).id + "|~|" + uin + "|" + " отправил личное сообщение пользователю " + uss.localnick + "|" + uss.id + "|" + ": " + txt);
              cq.sendMsg(new Message(snOut, ss[i],uw.getUser(uin).localnick + "|" + uw.getUser(uin).id + "|~|" + uin + "|" + " отправил личное сообщение пользователю " + uss.localnick + "|" + uss.id + "|" + ": " + txt));
          }
          uw.event(uss.id, uin, "PM", us.id, us.sn, "Отправил пм");
          Log.getLogger(p.getService().getName()).talk("CHAT: " + uss.sn + ">> Личное сообщение от " + uw.getUser(uin).localnick + "|" + uw.getUser(uin).id + "|: " + txt);
          uw.log(uss.id, uin, "LICH", ">> Личное сообщение от " + uw.getUser(uin).localnick + "|" + uw.getUser(uin).id + "|: " + txt, uss.room);
          cq.sendMsg(new Message(uss.basesn, uss.sn, "Личное сообщение от " + uw.getUser(uin).localnick + "|" + uw.getUser(uin).id + "|: " + txt));
          ccp.setPM(uss.sn, uin);
          cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(), "Сообщение пользователю " + uss.localnick + "|" + uss.id + "|" + " отправлено"));
     } catch (Exception ex) {
      ex.printStackTrace();
      Log.getLogger(p.getService().getName()).talk(uin + " Private msg error: " + m.getMsg());
      cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(), "ошибка отправки сообщения"));
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
        return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), p.getArgs(m, "$n $s")));
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