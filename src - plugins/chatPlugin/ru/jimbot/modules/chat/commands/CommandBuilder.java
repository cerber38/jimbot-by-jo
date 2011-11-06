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
                    String cl = normalize2(jarEntry.getName());
                    System.out.println("Load chat command: "+cl.replace("ru.jimbot.modules.chat.commands.", ""));
                    Class clazz= this.getClass().getClassLoader().loadClass(cl);
                    Constructor c= clazz.getConstructor(new Class[] {Parser.class});
                    lc.add((Command)c.newInstance(new Object[] {p}));
                }
            }
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
        return  !className.endsWith("CommandBuilder.class") && className.startsWith(packageName) && className.endsWith(".class");
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
