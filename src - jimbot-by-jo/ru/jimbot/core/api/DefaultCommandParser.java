/**
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

package ru.jimbot.core.api;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Vector;

import ru.jimbot.core.ContextManager;
import ru.jimbot.core.Message;

/**
 * Стандартные процедуры для любого парсера команд
 * - Собирает список доступных команд
 * - Список доступных объектов полномочий
 *
 * @author Prolubnikov Dmitry
 */
public abstract class DefaultCommandParser implements Parser {
    protected HashMap<String, Command> commands;
    protected HashMap<String, String> autority;
    protected Service srv;
    protected ContextManager cm;

    public DefaultCommandParser(Service srv) {
        this.srv = srv;
        commands = new HashMap<String, Command>();
        autority = new HashMap<String, String>();
        cm = new ContextManager();
    }

    /**
     * Освобождение всех ресурсов, связанных с командами
     */
    public void destroyCommands() {
        for(Command c : commands.values()) {
            c.destroy();
        }
    }

    public ContextManager getContextManager() {
        return cm;
    }

    /**
     * Возвращает сервис, в котором зарегистрирован этот парсер
     *
     * @return
     */
    public Service getService() {
        return srv;
    }

    /**
     * Добавить новую команду в реестр
     *
     * @param pattern
     * @param cmd
     */
    public void addCommand(String pattern, Command cmd) {
        if(commands.containsKey(pattern)) try {commands.get(pattern).destroy(); } catch (Exception e) {}
        commands.put(pattern, cmd);
        autority.putAll(cmd.getAutorityList());
    }

    /**
     * Добавить новую команду в реестр с паттерном по умолчанию
     *
     * @param cmd
     */
    public void addCommand(Command cmd) {
        for(String s:cmd.getCommandPatterns()) {
            addCommand(s, cmd);
        }
    }

    /**
     * Список объектов полномочий зарегистрированных команд (для работы остальных модулей и админки)
     *
     * @return
     */
    public Map<String, String> getAuthList() {
        return autority;
    }

    /**
     * Возвращает список зарегистрированных команд
     *
     * @return
     */
    public Set<String> getCommands() {
        return commands.keySet();
    }

    /**
     * Созвращает команду по ее названию
     *
     * @param cmd
     * @return
     */
    public Command getCommand(String cmd) {
        return commands.get(cmd);
    }

    /**
     * От кого пришла команда
     *
     * @param m
     * @return
     */
    public String getScreenName(Message m) {
        return m.getSnIn();
    }

    /**
     * Название команды
     *
     * @param m
     * @return
     */
    public String getCommand(Message m) {
        String s = "";
        try {
            s = m.getMsg().trim().split(" ")[0];
            s = s.toLowerCase();
        } catch (Exception e) {}
        return s;
    }

    /**
     * Массив аргументов по шаблону
     * $n - число
     * $c - слово без пробелов
     * $s - произвольная строка до конца сообщения
     *
     * @param m
     * @return
     */
    @SuppressWarnings("unchecked")
	public Vector getArgs(Message m, String arg) {
        Vector v = new Vector();
        if(arg.equals("")) return v;
        for(int i=0;i<arg.split(" ").length;i++){
            if(i>=(m.getMsg().split(" ").length-1)){
                if(arg.split(" ")[i].equals("$s") ||
                        arg.split(" ")[i].equals("$c"))
                    v.add("");
                else
                    v.add(0);

            } else {
                if(arg.split(" ")[i].equals("$c"))
                    v.add(m.getMsg().split(" ")[i+1]);
                else if(arg.split(" ")[i].equals("$n"))
                    v.add(parseN(m.getMsg().split(" ")[i+1]));
                else
                    v.add(parseS(i+1,m.getMsg()));
            }
        }
        return v;

    }

    /**
     * Обработка строкового параметра команды
     * @param c
     * @param s
     * @return
     */
    private String parseS(int c, String s){
        int k=0,i=0;
        for(i=0;i<s.length();i++){
            if(s.charAt(i)==' ') k++;
            if(k>=c) break;
        }
        return s.substring(i+1);
    }

    /**
     * Обработка числового параметра команды
     * @param s
     * @return
     */
    private int parseN(String s){
        try{
            return Integer.parseInt(s);
        } catch (Exception ex) {}
        return 0;
    }
}
