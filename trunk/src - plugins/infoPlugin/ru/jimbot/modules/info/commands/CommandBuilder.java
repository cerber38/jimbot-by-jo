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

import java.util.ArrayList;
import java.util.List;

import ru.jimbot.core.api.Command;
import ru.jimbot.core.api.ICommandBuilder;
import ru.jimbot.core.api.Parser;


/**
 * 
 * @author Prolubnikov Dmitry
 *
 */
public class CommandBuilder implements ICommandBuilder {

	public List<Command> build(Parser p) {
		List<Command> lc = new ArrayList<Command>();
	        lc.add(new CmdAbout(p));//информация об авторе программы
		lc.add(new CmdFree(p));//переход на самый свободный уин
		lc.add(new CmdHelp(p));//помощь по командам
		lc.add(new CmdStat(p));//статистика работы бота
		lc.add(new CmdAdd(p));//Добавить новый анекдот
		lc.add(new CmdAdsstat(p));//статистика показа рекламы
		lc.add(new CmdAnek(p));//Получить анекдот с заданным id
		lc.add(new CmdRefresh(p));//обновление кеша после изменения БД
		lc.add(new CmdAds(p));//работа с рекламными сообщениями
                lc.add(new Cmd1(p));//случайный анекдот

                lc.add(new CmdHorosscope(p));//ежедневный гороскоп.
                lc.add(new CmdSky(p));//прогноз погоды вашего города
                lc.add(new CmdStatCmd(p));//статистика работы команд
		return lc;
	}

}
