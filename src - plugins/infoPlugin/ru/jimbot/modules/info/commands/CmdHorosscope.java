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

import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Vector;
import ru.jimbot.core.UserContext;
import ru.jimbot.modules.info.InfoService;
import ru.jimbot.modules.info.InfoWork;
import ru.jimbot.util.MainProps;

/**
 * @author Prolubnikov Dmitry
 */
public class CmdHorosscope extends DefaultCommand {
    public CmdHorosscope(Parser p) {
        super(p);
    }

    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!hor","!гороскоп"});
    }

    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " - ежедневный гороскоп.";
    }

    /**
     * Выводит подробную помощь по команде
     *
     * @return
     */
    public String getXHelp() {
        return " наберите команду и следуйте инструкциям.";
    }

    /**
     * Выводит раздел справки
     * @return
     */
    public String getHelpPart(){
        return "Информационные";
    }
//   /**
//     * Список проверяемых командой объектов полномочий с их описанием
//     * @return
//     */
//    public Map<String, String> getAutorityList(){
//       HashMap autority = new HashMap<String, String>();
//       autority.put("pmsg", "Отправка приватных сообщений");
//       return autority;
//    }
//
//   /**
//     * Проверка полномочий по уину
//     *
//     * @param screenName
//     * @return
//     */
//
//    public boolean authorityCheck(String screenName) {
//        ChatWork uw =((ChatService)p.getService()).getChatWork();
//        return uw.authorityCheck(screenName, "pmsg");
//    }
    /**
     * Выполнение команды
     *
     * @param sn    - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String sn, int i1) {
        String s = "";
//        int i1=0;
//        try {
//            i1 = (Integer)param.get(0);
//        } catch (Exception e) {}
        
      try{
//         int i2 = 0;
//         if(i1==0 || i1>=14){
//          s = "Ежедневный гороскоп от Hyrax.ru\n"+
//              "Для вывода гороскопа введите\n"+
//              "!гороскоп <индификатор>\n"+
//              "[Идентификаторы]\n"+
//              "1 - Общая характеристика дня\n"+
//              "2 - ОВЕН\n"+
//              "3 - ТЕЛЕЦ\n"+
//              "4 - БЛИЗНЕЦЫ\n"+
//              "5 - РАК\n"+
//              "6 - ЛЕВ\n"+
//              "7 - ДЕВА\n"+
//              "8 - ВЕСЫ\n"+
//              "9 - СКОРПИОН\n"+
//              "10 - СТРЕЛЕЦ\n"+
//              "11 - КОЗЕРОГ\n"+
//              "12 - ВОДОЛЕЙ\n"+
//              "13 - РЫБЫ\n";
//            return s;
//         }
        // i1++;
            s =  MainProps.getStringFromHTTP("http://www.hyrax.ru/cgi-bin/bn_xml.cgi");
            String[] ss = s.split("<title>");
            String[] ss1 = s.split("<description>");
                     ss = ss[i1+1].split("</title>");
                     ss1 = ss1[i1+1].split("</description>");
            String text = "Ежедневный общий гороскоп:\n"+ss[0] + "\n" + ss1[0]+ "\n";
                    if(i1==1){return text;}//else i1=i1-1;
            s =  MainProps.getStringFromHTTP("http://www.hyrax.ru/cgi-bin/love_xml.cgi");
                     ss = s.split("<title>");
                     ss1 = s.split("<description>");
                     ss = ss[i1].split("</title>");
                     ss1 = ss1[i1].split("</description>");
                   text +="\nЕжедневный любовный гороскоп:\n"+ss[0] + "\n" + ss1[0];
            s =  MainProps.getStringFromHTTP("http://www.hyrax.ru/cgi-bin/mob_xml.cgi");
                     ss = s.split("<title>");
                     ss1 = s.split("<description>");
                     ss = ss[i1].split("</title>");
                     ss1 = ss1[i1].split("</description>");
                   text +="\nЕжедневный мобильный гороскоп:\n" + ss[0] + "\n" + ss1[0];
            return text;
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
        UserContext c = p.getContextManager().getContext(m.getSnIn());
        String k = c.getData("cmd")==null ? "" : (String)c.getData("cmd");
        Vector param = p.getArgs(m, "$n");
        String s = "";
        int n=0;
        try {
            n = (Integer)param.get(0);
        } catch (Exception e) {
               return new Message(m.getSnOut(), m.getSnIn(), "Некорректное число!");  
        }
           if("".equals(k)&&n>0){
             if(n>13){
                      return new Message(m.getSnOut(), m.getSnIn(), "Некорректное число!");   
             }
               return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), n));  
           }
           if("".equals(c.getLastCommand())) {
			c.setLastCommand("!гороскоп");
                        c.getData().put("cmd", "1");
                        k="1";
	   } 
           if("1".equals(k)) {
                   s = "Ежедневный гороскоп от Hyrax.ru\n"+
                   "Для вывода гороскопа выберете номер:\n"+
                  // "!гороскоп <индификатор>\n"+
                  // "[Идентификаторы]\n"+
                   "1 - Общая характеристика дня\n"+
                   "2 - ОВЕН\n"+
                   "3 - ТЕЛЕЦ\n"+
                   "4 - БЛИЗНЕЦЫ\n"+
                   "5 - РАК\n"+
                   "6 - ЛЕВ\n"+
                   "7 - ДЕВА\n"+
                   "8 - ВЕСЫ\n"+
                   "9 - СКОРПИОН\n"+
                   "10 - СТРЕЛЕЦ\n"+
                   "11 - КОЗЕРОГ\n"+
                   "12 - ВОДОЛЕЙ\n"+
                   "13 - РЫБЫ\n";  
                   c.getData().put("cmd", "2");
                   c.update();
		   return new Message(m.getSnOut(), m.getSnIn(), s);    
             } else if("2".equals(k)) {      
                      try {
                          n = Integer.parseInt(m.getMsg());
                          } catch (Exception e) {
                          c.getData().put("cmd", "2");
                          c.update();
                          s = "Некорректное число!\nвыберете номер";   
                          return new Message(m.getSnOut(), m.getSnIn(), s);  
                          }
                      if(n>13){
                               return new Message(m.getSnOut(), m.getSnIn(), "Некорректное число!");   
                              }
                       c.getData().remove("cmd");
                       c.setLastCommand("");
                       return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), n));
		    } 
                  c.update();
                  return new Message(m.getSnOut(), m.getSnIn(), s);  
    }

	public String exec(String sn, Vector param) {
		return null;
	}
}