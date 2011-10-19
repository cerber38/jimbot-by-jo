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
public class CmdReg extends DefaultCommand {
    public CmdReg(Parser p) {
        super(p);
       }


    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!reg","!ник"});
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
        return " - регистрация/смена ника в чате";
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

         ChatWork uw =((ChatService)p.getService()).getChatWork();
         Users uss = uw.getUser(uin);
         ChatConfig psp =((ChatService)p.getService()).getConfig();
         ChatQueue cq =((ChatService)p.getService()).getChatQueue();
         String mmsg = m.getMsg();
         UserContext c = p.getContextManager().getContext(uin);
         String k = c.getData("cmd")==null ? "" : (String)c.getData("cmd");
         String ms = c.getData("txt")==null ? "" : (String)c.getData("txt");
         ms = ms.replace("\n", "");
         ms = ms.replace("\r", "");

     try{

             	boolean twoPart = false; // Второй заход в процедуру после ответа?
        	if(psp.getBooleanProperty("chat.useCaptcha") && !c.isExpired()&& "cap".equals(k)){
        		if(ms.equalsIgnoreCase(mmsg)){
        			twoPart=true;
        			param = (Vector)c.getData("param");
                                c.setLastCommand("");
                                c.getData().remove("cmd");
                                c.getData().remove("param");
                                c.getData().remove("txt");
        		} else {
                                c.setLastCommand("");
        			c.getData().remove("cmd");
                                c.getData().remove("param");
                                c.getData().remove("txt");
        			return "Вы неправильно ответили на проверочный вопрос, попытайтесь зарегистрироваться еще раз.";
        		}
        	}
            int maxNick = psp.getIntProperty("chat.maxNickLenght");
            String lnick = (String)param.get(0);

            if (lnick.equals("") || lnick.equals(" ")){
                Log.getLogger(p.getService().getName()).talk(uin + " Reg error: " + mmsg);
                return "ошибка регистрации, пустой ник";
            }
            if(lnick.length()>maxNick) {
                lnick = lnick.substring(0,maxNick);
                cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Предупреждение! Ваш ник слишком длинный и будет обрезан."));
            }
            if(!((ChatCommandParser)p).testNick(uin,lnick)){
                return"Ошибочный ник, попытайтесь еще раз";
              }
            lnick = lnick.replace('\n',' ');
            lnick = lnick.replace('\r',' ');
            if(psp.getBooleanProperty("chat.isUniqueNick") && !((ChatCommandParser)p).qauth(uin,"dblnick") && !psp.testAdmin(uin))
            	if(uw.isUsedNick(lnick)){
            		return "Такой ник уже существует. Попробуйте другой ник";
            	}
            String oldNick = uss.localnick;
            //смена ника - юзер уже в чате, пароль не нужен
            if(uss.state!=ChatWork.STATE_NO_REG) {
                if(uss.state!=ChatWork.STATE_CHAT) return "*NO* сначало нужно зайти в чат!"; // Менять ник только в чате
                if(uw.getCountNickChange(uss.id)>psp.getIntProperty("chat.maxNickChanged")){
                	return "Вы не можете так часто менять ник.";
                }
                if(oldNick.equals(lnick)){
                    if(uss.state==ChatWork.STATE_NO_CHAT)
                        return"Ник не изменен. Для входа в чат используйте команду: !чат";
                    else
                        return "Ник не изменен.";

                }
                uss.localnick = lnick;
                Log.getLogger(p.getService().getName()).talk(uin + " update " + mmsg);
                cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Ник изменён"));
                cq.addMsg(oldNick + " сменил(а) ник на " + lnick, "", uss.room); //Сообщение для всех
                uw.log(uss.id,uin,"REG",lnick,uss.room);
                uw.event(uss.id, uin, "REG", 0, "", lnick);
                uss.basesn = m.getSnOut();
                uw.updateUser(uss);
                return null;
            }
            if(!((ChatCommandParser)p).testNick(uin,lnick)){
                return "Ошибочный ник, попытайтесь еще раз";
            }
            // Свободная регистрация
            if(psp.getBooleanProperty("chat.FreeReg") ||
                    psp.testAdmin(uin)){
            	if(psp.getBooleanProperty("chat.useCaptcha") && !twoPart){
                      if("".equals(c.getLastCommand())) {
			        c.setLastCommand("!reg");
                             }
                	String s = ((ChatCommandParser)p).getCaptcha();
                	cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Для подтверждения того что вы человек, напишите ответ на несложный вопрос " +
                    			"Время на раздумье 1 минута: \n" + s.split("=")[0] + "\n....ваш ответ?"));
                        c.getData().put("txt", s.split("=")[1]);
			c.getData().put("cmd", "cap");
                        c.getData().put("param", param);
                        c.setMaxAge(60000);
                        Log.getLogger(p.getService().getName()).info("ответ "+s.split("=")[1]);
                   	return null;
                }
                uss.state=ChatWork.STATE_NO_CHAT;
                uss.basesn = m.getSnOut();
                uss.localnick = lnick;
                uss.group = "user";
                int id = uw.addUser(uss);
                uw.setUserPropsValue(id, "group", "user");
                uw.setUserPropsValue(id, "grant", "");
                uw.setUserPropsValue(id, "revoke", "");
                uw.clearCashAuth(id);
              //  proc.mq.add(uin, "", 1);
                 Log.getLogger(p.getService().getName()).talk(uin + " Reg new user: " + mmsg);
                  cq.addMsg(psp.getStringProperty("bot.potok_inreg")+" " + lnick + "|" + uss.id + "|", "", uss.room); //Сообщение для всех
                  cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),psp.getStringProperty("bot.user_inreg")));
                  uw.log(id,uin,"REG",lnick, uss.room);
                  uw.event(id, uin, "REG", 0, "", lnick);

                return null;
            }
            // Регистрация по приглашению
            String inv = (String)param.get(1);
            if(inv.equals("")){
                Log.getLogger(p.getService().getName()).talk(uin + " Reg error: " + mmsg);
                return "ошибка регистрации, пустой пароль.\n" +
                		psp.getStringProperty("chat.inviteDescription");//"Для регистрации в чате вам необходимо получить приглашение одного из пользователей.");

            }
            if(uw.testInvite(inv)){
                if(!uw.updateInvite(uin,inv)){
                    cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"ошибка регистрации.\nДля регистрации в чате вам необходимо получить приглашение одного из пользователей."));
                    Log.getLogger(p.getService().getName()).talk(uin + " Reg error: " + mmsg);
                } else {
                	if(psp.getBooleanProperty("chat.useCaptcha") && !twoPart){
                             if("".equals(c.getLastCommand())) {
			              c.setLastCommand("!reg");
                                      }
                    	String s = ((ChatCommandParser)p).getCaptcha();
                    	 cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),"Для подтверждения того что вы человек, напишите ответ на несложный вопрос " +
                    			"Время на раздумье 1 минута: \n" + s.split("=")[0] + "\n....ваш ответ?"));
                        c.getData().put("txt", s.split("=")[1]);
			c.getData().put("cmd", "cap");
                        c.getData().put("param", param);
                        c.setMaxAge(1000);
                        Log.getLogger(p.getService().getName()).info("ответ "+s.split("=")[1]);
                    	return null;
                    }
                    uss.state=ChatWork.STATE_NO_CHAT;
                    uss.basesn = m.getSnOut();
                    uss.localnick = lnick;
                    uss.group = "user";
                    int id = uw.addUser(uss);
                    uw.updateInvite(uin,inv);// До этого ИД юзера был неизвестен!!!
                  //  proc.mq.add(uin, "", 1);
                    Log.getLogger(p.getService().getName()).talk(uin + " Reg new user: " + mmsg);
                    cq.sendMsg(new Message(m.getSnOut(), m.getSnIn(),psp.getStringProperty("bot.user_inreg")));

                    uw.log(id,uin,"REG",lnick, uss.room);
                    uw.event(id, uin, "REG", 0, "", lnick);
                }
            } else {
                 Log.getLogger(p.getService().getName()).talk(uin + " Reg error: " + mmsg);
                return "ошибка регистрации, неверный пароль приглашения.\n" +
                		psp.getStringProperty("chat.inviteDescription");//"Для регистрации в чате вам необходимо получить приглашение одного из пользователей.");
                 }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.getLogger(p.getService().getName()).talk(uin + " Reg error: " + mmsg);
            return "ошибка регистрации.";
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
        return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), p.getArgs(m, "$c")));
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