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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ru.jimbot.core.api.Command;
import ru.jimbot.core.api.ICommandBuilder;
import ru.jimbot.core.api.Parser;


/**
 * 
 * @author Prolubnikov Dmitry
 * @author ~jo-MA-jo~
 */
public class CommandBuilder implements ICommandBuilder {

	public List<Command> build(Parser p) {
		List<Command> lc = new ArrayList<Command>();
//	        lc.add(new CmdAbout(p));//информация об авторе программы
//		lc.add(new CmdFree(p));//переход на самый свободный уин
//		lc.add(new CmdHelp(p));//помощь по командам
//		lc.add(new CmdStat(p));//статистика работы бота
//
//                lc.add(new CmdGoChat(p));//вход в чат
//                lc.add(new CmdExitChat(p));//выход из чата
//                lc.add(new CmdGoRoom(p));//перейти в другую комнату
//                lc.add(new CmdRooms(p));//список комнат в чате
//                lc.add(new CmdAdm(p));//список модеров и админов в чате
//                lc.add(new CmdVse(p));//Список пользователей в чате
//                lc.add(new CmdReg(p));//регистрация в чате или смена ника
//                lc.add(new CmdPrivate(p));//Отправить приватное сообщение
//                lc.add(new CmdKick(p));//выгнать пользователя из чата
//                lc.add(new CmdBan(p));//забанить пользователя
//                lc.add(new CmdUban(p));//снять бан с пользователя
//                lc.add(new CmdChRoom(p));//создать новую комнату
//                lc.add(new CmdCrRoom(p));//изменить название комнаты
//                lc.add(new CmdDelRoom(p));//удалить комнату
//                lc.add(new CmdUnicodeOn(p));
//                lc.add(new CmdUnicodeOff(p));
      try {
            JarFile jarFile = new JarFile("./JimBot_by-jo.jar");
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                // Одно из назначений хорошего загрузчика - валидация классов на этапе загрузки
                if (match(normalize(jarEntry.getName()), "ru.jimbot.modules.chat.commands")) {
           //        System.out.println("!!! "+normalize(jarEntry.getName()));
                    String cl = normalize2(jarEntry.getName());
                    Class clazz= this.getClass().getClassLoader().loadClass(cl);
                    Constructor c= clazz.getConstructor(new Class[] {Parser.class});
                    lc.add((Command)c.newInstance(new Object[] {p}));
                }
            }
      //Manager.getInstance().putServiceBuilder((IServiceBuilder) this.getClass().getClassLoader().loadClass("ru.jimbot.modules.chat.ChatServiceBuilder").newInstance());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

		return lc;
	}


     /**
     * Валидация класса - проверят принадлежит ли класс заданному пакету и имеет ли
     * он расширение .class
     *
     * @param className
     * @param packageName
     * @return
     */
    private boolean match(String className, String packageName) {
        return  !className.equalsIgnoreCase("CommandBuilder.class") && className.startsWith(packageName) && className.endsWith(".class");
    }
    /**
     * Преобразуем имя в файловой системе в имя класса
     * (заменяем слэши на точки)
     *
     * @param className
     * @return
     */
    private String normalize(String className) {
        return className.replace('/', '.');
    }

    private String normalize2(String className) {
        return className.replace('/', '.').replace(".class", "");
    }


}
