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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import ru.jimbot.core.Message;
import ru.jimbot.core.api.DefaultCommand;
import ru.jimbot.core.api.Parser;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Vector;
import ru.jimbot.modules.info.InfoService;
import ru.jimbot.modules.info.InfoWork;
import ru.jimbot.util.MainProps;

/**
 * @author Prolubnikov Dmitry
 */
public class CmdSky extends DefaultCommand {
    public CmdSky(Parser p) {
        super(p);
    }

    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!sky","!погода"});
    }

    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " <назавние вашего города> - прогноз погоды вашего города";
    }

    /**
     * Выводит подробную помощь по команде
     *
     * @return
     */
    public String getXHelp() {
        return " <назавние вашего города> - прогноз погоды по вашему городу.";
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
        String s = "";
        String x = "";
        String text = "";
        InfoWork iw = ((InfoService)p.getService()).getInfoWork();
        text = (String)param.get(0);
        if ("".equals(text))return "Ошибка! Не указан город\n (пример) !погода москва ";
     
try{
     String firstChar=text.substring(0, 1).toUpperCase();//первая буква
     String lastChar=text.substring(1,text.length()).toLowerCase();
     text=firstChar+lastChar;
     int num = 0;

         PreparedStatement pst = iw.db.get("weather").getDb().prepareStatement("SELECT `num` FROM `weather` WHERE `txt` LIKE '"+text+"';");
         ResultSet rs = pst.executeQuery();
         if(rs.next())
          {
              num = rs.getInt(1);
            }
            else{
          return "Город не наиден.";
         }
            rs.close();
            pst.close();
        
//String u = "http://informer.gismeteo.ru/xml/"+num+"_1.xml";

            s =  MainProps.getStringFromHTTP("http://informer.gismeteo.ru/xml/"+num+"_1.xml");  

            s = s.replace("<FORECAST day=","");
            s = s.replace("/FORECAST","");
            s = s.replace("/FORECAST","");
            s = s.replace(" month=",".");
            s = s.replace(" year=",".");
            s = s.replace("hour=\"00\"","г.");
            s = s.replace("hour=\"01\"","г.");
            s = s.replace("hour=\"02\"","г.");
            s = s.replace("hour=\"03\"","г.");
            s = s.replace("hour=\"04\"","г.");
            s = s.replace("hour=\"05\"","г.");
            s = s.replace("hour=\"06\"","г.");
            s = s.replace("hour=\"07\"","г.");
            s = s.replace("hour=\"08\"","г.");
            s = s.replace("hour=\"09\"","г.");
            s = s.replace("hour=\"10\"","г.");
            s = s.replace("hour=\"11\"","г.");
            s = s.replace("hour=\"12\"","г.");
            s = s.replace("hour=\"13\"","г.");
            s = s.replace("hour=\"14\"","г.");
            s = s.replace("hour=\"15\"","г.");
            s = s.replace("hour=\"16\"","г.");
            s = s.replace("hour=\"17\"","г.");
            s = s.replace("hour=\"18\"","г.");
            s = s.replace("hour=\"19\"","г.");
            s = s.replace("hour=\"20\"","г.");
            s = s.replace("hour=\"21\"","г.");
            s = s.replace("hour=\"22\"","г.");
            s = s.replace("hour=\"23\"","г.");

            s = s.replace("predict=\"0\"","");
            s = s.replace("predict=\"1\"","");
            s = s.replace("predict=\"2\"","");
            s = s.replace("predict=\"3\"","");
            s = s.replace("predict=\"4\"","");
            s = s.replace("predict=\"5\"","");
            s = s.replace("predict=\"6\"","");
            s = s.replace("predict=\"7\"","");
            s = s.replace("predict=\"8\"","");
            s = s.replace("predict=\"9\"","");
            s = s.replace("predict=\"10\"","");
            s = s.replace("predict=\"11\"","");
            s = s.replace("predict=\"12\"","");
            s = s.replace("predict=\"13\"","");
            s = s.replace("predict=\"14\"","");
            s = s.replace("predict=\"15\"","");
            s = s.replace("predict=\"16\"","");
            s = s.replace("predict=\"17\"","");
            s = s.replace("predict=\"18\"","");
            s = s.replace("predict=\"19\"","");
            s = s.replace("predict=\"20\"","");
            s = s.replace("predict=\"21\"","");
            s = s.replace("predict=\"22\"","");
            s = s.replace("predict=\"23\"","");
            s = s.replace("predict=\"24\"","");

            s = s.replace("weekday=\"2\"","~Понедельник~");
            s = s.replace("weekday=\"3\"","~Вторник~");
            s = s.replace("weekday=\"4\"","~Среда~");
            s = s.replace("weekday=\"5\"","~Четверг~");
            s = s.replace("weekday=\"6\"","~Пятница~");
            s = s.replace("weekday=\"7\"","~Суббота~");
            s = s.replace("weekday=\"1\"","~Воскресенье~");

            s = s.replace("tod=\"0\"","(ночь)");
            s = s.replace("tod=\"1\"","(утро)");
            s = s.replace("tod=\"2\"","(день)");
            s = s.replace("tod=\"3\"","(вечер)");

            s = s.replace("<PHENOMENA ","");
            s = s.replace("cloudiness=\"0\" ","ясно");
            s = s.replace("cloudiness=\"1\" ","малооблачно");
            s = s.replace("cloudiness=\"2\" ","облачно");
            s = s.replace("cloudiness=\"3\" ","пасмурно");

            s = s.replace("precipitation=\"4\" ",", дождь");
            s = s.replace("precipitation=\"5\" ",", ливень");
            s = s.replace("precipitation=\"6\" ",", снег");
            s = s.replace("precipitation=\"7\" ",", снег");
            s = s.replace("precipitation=\"8\" ",", гроза");
            s = s.replace("precipitation=\"9\" ","");
            s = s.replace("precipitation=\"10\" ",", без осадков");

            s = s.replace("spower=\"0\"/>","");
            s = s.replace("spower=\"1\"/>",", гроза");

            s = s.replace("rpower=\"0\" ","");
            s = s.replace("rpower=\"1\" ","");

            s = s.replace("<PRESSURE max=\"","");
            s = s.replace("\" min=\"","-");

            s = s.replace("<TEMPERATURE max=\"","");


            s = s.replace("<WIND min=\"","");
            s = s.replace("\" max=\"","-");

            s = s.replace(" direction=\"0"," м/с, северный");
            s = s.replace(" direction=\"1"," м/с, северо-восточный");
            s = s.replace(" direction=\"2"," м/с, восточный");
            s = s.replace(" direction=\"3"," м/с, юго-восточный");
            s = s.replace(" direction=\"4"," м/с, южный");
            s = s.replace(" direction=\"5"," м/с, юго-западный");
            s = s.replace(" direction=\"6"," м/с, западный");
            s = s.replace(" direction=\"7"," м/с, северо-западный");


            s = s.replace("<RELWET max=\"","");
            s = s.replace("<<HEAT min=\"","");

            s = s.replace("\"/>","");

            s = s.replace("<","");
            s = s.replace(">","");
            s = s.replace("\"","");

            String[] ss = s.split("\n");
        x = "~ Прогноз погоды по городу "+text+"~"+
           "\n"+
           "\nПрогноз на " + ss[4].substring(3, ss[4].length())+
           "\nОблачность: " + ss[5].substring(4, ss[5].length())+
           "\nАтмосферное давление: " + ss[6].substring(4, ss[6].length())+" мм.рт.ст."+
           "\nТемпература: " + ss[7].substring(4, ss[7].length())+" °C"+
           "\nВетер: " + ss[8].substring(4, ss[8].length())+
           "\nВлажность воздуха: " + ss[9].substring(4, ss[9].length())+"%"+
           "\n"+
           "\nПрогноз на " + ss[12].substring(3, ss[12].length())+
           "\nОблачность: " + ss[13].substring(4, ss[13].length())+
           "\nАтмосферное давление: " + ss[14].substring(4, ss[14].length())+" мм.рт.ст."+
           "\nТемпература: " + ss[15].substring(4, ss[15].length())+" °C"+
           "\nВетер: " + ss[16].substring(4, ss[16].length())+
           "\nВлажность воздуха: " + ss[17].substring(4, ss[17].length())+"%"+
           "\n"+
           "\nПрогноз на " + ss[20].substring(3, ss[20].length())+
           "\nОблачность: " + ss[21].substring(4, ss[21].length())+
           "\nАтмосферное давление: " + ss[22].substring(4, ss[22].length())+" мм.рт.ст."+
           "\nТемпература: " + ss[23].substring(4, ss[23].length())+" °C"+
           "\nВетер: " + ss[24].substring(4, ss[24].length())+
           "\nВлажность воздуха: " + ss[25].substring(4, ss[25].length())+"%"+
           "\n"+
           "\nПрогноз на " + ss[28].substring(3, ss[28].length())+
           "\nОблачность: " + ss[29].substring(4, ss[29].length())+
           "\nАтмосферное давление: " + ss[30].substring(4, ss[30].length())+" мм.рт.ст."+
           "\nТемпература: " + ss[31].substring(4, ss[31].length())+" °C"+
           "\nВетер: " + ss[32].substring(4, ss[32].length())+
           "\nВлажность воздуха: " + ss[33].substring(4, ss[33].length())+"%"+
           "\n"+
           "\nПредоставлено Gismeteo.ru";
         } catch (Exception ex) {
        return "Ошибка "+ex.getMessage();
        }
    
        return x;
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
}