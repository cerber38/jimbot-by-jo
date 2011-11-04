
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
import ru.jimbot.modules.chat.ChatWork;
import ru.jimbot.modules.chat.tables.Rooms;

/**
 * переход по комнатам
 *
 */
public class CmdCrRoom extends DefaultCommand {
    public CmdCrRoom(Parser p) {
        super(p);
    }
 public Message m;
    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!crroom","!созкомн"});
    }

    /**
     * Список проверяемых командой объектов полномочий с их описанием
     * @return
     */
    public Map<String, String> getAutorityList(){
       HashMap autority = new HashMap<String, String>();
       autority.put("wroom","Создавать и изменять комнаты");
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
        return uw.authorityCheck(screenName, "wroom");
    }
    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " - <ид> <название> - создать новую комнату";

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
     * @param v - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String uin, Vector v) {
         ChatWork cw =((ChatService)p.getService()).getChatWork();
        int room = (Integer)v.get(0);
        String s = (String)v.get(1);
        if(cw.checkRoom(room)){
            return "Такая комната уже существует!";
        }
        Rooms r = new Rooms();
        r.setId(room);
        r.setName(s);
        cw.createRoom(r);
       return "Комната " + room + " успешно создана!";
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