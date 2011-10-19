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
import java.util.Set;
import java.util.Vector;
import ru.jimbot.modules.chat.ChatConfig;
import ru.jimbot.modules.chat.ChatQueue;
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.tables.Users;

/**
 * вход в чат
 *
 */
public class CmdVse extends DefaultCommand {
    public CmdVse(Parser p) {
        super(p);
       }


    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"+все","+aa","+аа","!все"});
    }

    /**
     * Список проверяемых командой объектов полномочий с их описанием
     * @return
     */
  //  public Map<String, String> getAutorityList(){
  //     HashMap autority = new HashMap<String, String>();
  //     autority.put("adm", "список администрации чата");
  //     return autority;
  //  }

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
        return " - список тех кто сейчас в чате";
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
        return "Информационные";
    }        
    
    public Message m;
    /**
     * Выполнение команды
     *
     * @param sn    - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String uin, Vector param){

         ChatWork uw =((ChatService)p.getService()).getChatWork();
         ChatConfig psp =((ChatService)p.getService()).getConfig();
         ChatQueue cq =((ChatService)p.getService()).getChatQueue();
       if(!psp.testAdmin(uin)&&!uw.isChat(uin)) return "*NO* сначало нужно зайти в чат!";
        String s = "Список пользователей в чате:\n";
     //   if(psp.getBooleanProperty("adm.useAdmin"))
      //  s +=" 0 - " + radm.NICK +"(бот)\n";
        Enumeration <String> e = cq.uq.keys();
        int cnt=0;
        String sn ="";
        String ss ="";
        String sk ="";
    if (psp.getBooleanProperty("vse.mob")){
    sn="------------\n";
    ss ="\n";
    sk="------------";
    }
        while(e.hasMoreElements()){
            String i = e.nextElement();
            Users us = uw.getUser(i);
            if(us.state==ChatWork.STATE_CHAT)
                cnt++;
             s += sn+"[" + us.id + "] - " +psp.getStringProperty("auth.group_prist_"+us.group)+ us.localnick +" "+ss+" Кошелек: ["+us.country+"] "+psp.getStringProperty("money")+ss+" Cтатус: [" + us.homepage + "]"+ss+" Комната: ["+ us.room + "]\n"+sk;
        }
           String st = psp.getStringProperty("bot.privetstvie_9") + "";
   String[] sst = st.split(";");
   int R = (int) ((Math.random()*sst.length));

    s += "\nВсего в чате: " + cnt;
    s += "\nВсего зашло в чат: " + uw.statUsersCount();
    s += "\n~======~";
    s += "\nЗарегистрированно: " + Integer.toString(uw.count()-1)+" чел.";
    s += "\n~~~~~~~~~~~~~~";
    s += "\n" + sst[R];
    return s;

  }

    /**
     * Выполнение команды
     *
     * @param m - обрабатываемое сообщение с командой
     * @return - результат (если нужен)
     */
    public Message exec(Message m) {
        this.m=m;
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