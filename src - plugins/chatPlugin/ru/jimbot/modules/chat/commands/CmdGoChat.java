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
import ru.jimbot.core.UserContext;
import ru.jimbot.modules.chat.ChatConfig;
import ru.jimbot.modules.chat.ChatQueue;
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.tables.Users;
import ru.jimbot.util.Log;

/**
 * вход в чат
 *
 */
public class CmdGoChat extends DefaultCommand {
    public CmdGoChat(Parser p) {
        super(p);
       }


    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!чат","!вход","!ч"});
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
        return " - вход в чат";
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
     * @param sn    - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String uin, Vector param){
         UserContext c = p.getContextManager().getContext(m.getSnIn());
         ChatWork uw =((ChatService)p.getService()).getChatWork();
         Users uss = uw.getUser(uin);
         ChatConfig psp =((ChatService)p.getService()).getConfig();
         ChatQueue cq =((ChatService)p.getService()).getChatQueue();
         String k = c.getData("cmd")==null ? "" : (String)c.getData("cmd");
        if(uss.localnick==null || uss.localnick.equals("") || uss.state==ChatWork.STATE_NO_REG) {
            return "Прежде чем войти в чат, необходимо зарегистрироваться.Команда !ник";
        }else
         if (uw.isChat(uin)) return "Вы уже в чате!";
         if("".equals(c.getLastCommand())) c.setLastCommand("!чат");

         if(!"да заходи, чего уш там".equals(k)) {
             String s = "Для входа в чат выберете № комнаты:\n";
             Set<Integer> rid = uw.getRooms();
             Integer[] rooms=(Integer[])rid.toArray(new Integer[0]);
              Arrays.sort(rooms);
              for(Integer i:rooms){
                 int cnt=0;
                 Enumeration<String> e = cq.uq.keys();
                 while(e.hasMoreElements()){
                      String g = e.nextElement();
                      Users us = uw.getUser(g);
                      if(us.state==uw.STATE_CHAT && us.room==i) cnt++;
                 }
               s += i + " - " + uw.getRoom(i).getName() + "|" + cnt + "|"+ "\n";
             }
            c.getData().put("cmd", "да заходи, чего уш там");
            c.getData().put("rooms", s);
            c.update();
            return s;
         }else{
                int room;
           try {
                room = Integer.parseInt(m.getMsg());
               } catch(NumberFormatException e) {
                  c.update();
                  cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(), (String)c.getData().get("rooms")));//список комнат
                  return "Введите номер комнаты, в которую хотите зайти.";
               }
              if(!uw.authorityCheck(uin, "anyroom") || !uw.checkRoom(room)) {
                  c.update();
                  cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(), (String)c.getData().get("rooms")));//список комнат
                  return "Такой комнаты не существует! Некуда переходить.";
              }
              if (!uw.getRoom(room).checkPass("") && !psp.testAdmin(uin)) {
                  c.update();
                  cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(), (String)c.getData().get("rooms")));//список комнат
                  return "У вас нет доступа к данной комнате, выберете другую";
              }
               uss.room = room;
               uw.updateUser(uss);
               c.setLastCommand("");
               c.getData().remove("cmd");
               c.getData().remove("rooms");
                try{
                   String MESSAGE2 = " "+psp.getStringProperty("bot.potok_inchat");
                   String MESSAGE3 = "[";
                   String MESSAGE4 = "] ";

                            Log.getLogger(p.getService().getName()).info("Add contact " + uin);
                            uss.state = ChatWork.STATE_CHAT;
                            uss.basesn = m.getSnOut();
                            uw.updateUser(uss);
                           // if (cmd.srv.us.authorityCheck(uin, "Show")) {
                            cq.addMsg(psp.getStringProperty("auth.group_prist_"+uss.group)+" "+uss.localnick +MESSAGE3 + uss.id +MESSAGE4 + MESSAGE2, m.getSnIn(), uss.room);
                          //  }
                            cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),
                                    "<<< *HI* Добро пожаловать в чат >>>" +
                                    "\nВаш ID = [" + uss.id + "]." +
                                  //  "\nВаш UIN = [" + uin + "]." +
                                    "\nВаш NICK = [" + uss.localnick + "]." +
                                    "\nУ вас в кошельке = [" + uss.country + "] "+psp.getStringProperty("money")+
                                    "\n~======~" +
                                    "\nВы в комнате = [" + uss.room + "]." +
                                    "\nНазвание комнаты = [" + uw.getRoom(uss.room).getName() + "].  " +
                                    "\nТема комнаты = [" + uw.getRoom(uss.room).getTopic() + "]." +
                                    "\n"+psp.getStringProperty("bot.user_inchat")));

                        Log.getLogger(p.getService().getName()).talk(uss.localnick + MESSAGE2);
                        uw.log(uss.id,uin,"STATE_IN",uss.localnick + MESSAGE2,uss.room);
                        uw.event(uss.id, uin, "STATE_IN", 0,"",psp.getStringProperty("auth.group_prist_"+uss.group)+" "+uss.localnick +MESSAGE3 + uss.id +MESSAGE4 + MESSAGE2);
                        cq.addUser(m, uss.room);

                                if(uw.getCurrUinUsers(uss.basesn)>psp.getIntProperty("chat.maxUserOnUin")){
                                   cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Данный номер слишком загружен, вы будете автоматически переведены на более свободный номер."));
                                   String s = uw.getFreeUin();
                                   uss.basesn = s;
                                   uw.updateUser(uss);
                                   cq.changeUser(uin, s);
                                   cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Следующее сообщение придет с номера " + s));
                               }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.getLogger(p.getService().getName()).error(ex.getMessage());
                        }
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