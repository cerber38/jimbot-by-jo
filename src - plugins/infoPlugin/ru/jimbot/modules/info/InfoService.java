package ru.jimbot.modules.info;
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




//import ru.jimbot.modules.ChatCommandConnector;
import ru.jimbot.Manager;
import ru.jimbot.core.*;
import ru.jimbot.core.api.CommandProtocolListener;
import ru.jimbot.core.api.DbStatusListener;
import ru.jimbot.core.api.DefaultCommandParser;
import ru.jimbot.core.api.DefaultService;
//import ru.jimbot.core.api.IHTTPServiceConfig;
import ru.jimbot.core.api.IHTTPServiceConfig;
import ru.jimbot.core.api.IProtocolManager;
import ru.jimbot.core.api.IServiceBuilder;
import ru.jimbot.core.api.Protocol;
//import ru.jimbot.modules.Chat.http.ChatHttpBuilder;
import ru.jimbot.core.api.Service;
import ru.jimbot.util.Log;
import ru.jimbot.db.CreateDB;
import ru.jimbot.modules.info.http.InfoHttpBuilder;
import ru.jimbot.modules.srvbilders.InfoServiceBuilder;

/**
 * Реализация сервиса анекдотного бота
 *
 * @author Prolubnikov Dmitry
 */
public class InfoService extends DefaultService implements DbStatusListener {
    public InfoServiceBuilder СSB =new InfoServiceBuilder();
    private String name = ""; // Имя сервиса
    private String type = "info"; // тип сервиса
    private InfoConfig config;

   // private ChatWork cw;
    private InfoWork iw;
    private boolean start = false;
    private InfoCommandParser cmd;
//    private ChronoMaster cron = new ChronoMaster();
 //   private ChatCommandConnector con = null;
    public InfoHttpBuilder CHB =new InfoHttpBuilder();
    public CreateDB CDB=new CreateDB();



    public InfoService(String name) {
        this.name = name;
        type=СSB.getServiceType();
        config = InfoConfig.getInstance(name);
        CDB.createDB(this.name,type);
       // Log.getLogger(this.name).talk("Запускаю сервис: " + this.name);

     //   cw = new ChatWork(name, this);

    }


    public InfoWork getInfoWork() {
        return iw;
    }

 

    public DefaultCommandParser getCommandParser() {
        if (cmd==null) cmd = new InfoCommandParser(this);
        return cmd;
    }

    public CreateDB getCDB() {
        return CDB;
    }

    /**
     * Запуск сервиса
     */
    public void start() {

        getCron().clear();
        getCron().start();
        for(int i=0;i<config.getUins().size();i++) {
            IProtocolManager pm = Manager.getInstance().getAllProtocolManagers().get(config.getUins().get(i).getProtocol());
          	Protocol p = pm.addProtocol(pm.getBuilder(config.getUins().get(i).getScreenName())
        			.pass(config.getUins().get(i).getPass().getPass())
        			.status(config.getStatus())
        			.statustxt(config.getStatustxt())
        			.xstatus(config.getXstatus())
        			.xstatustxt1(config.getXstatustxt1())
        			.xstatustxt2(config.getXstatustxt2())
        			.build());
        	p.setLogger(Log.getLogger(name));
        	addCommandProtocolListener((CommandProtocolListener)p);
            protocols.put(config.getUins().get(i).getScreenName(), p);
            System.out.println("Create protocol " + p.getScreenName());
        }
        qe.start();
        if (inq==null) inq = new MsgInQueue(this);
        if (outq==null) outq = new MsgOutQueue(this);

        inq.start();
        outq.start();

       if (cmd==null) cmd = new InfoCommandParser(this);
        getCron().addTask(new CheckSessionTask(cmd, 60000));

        // TODO ...
        addDbStatusListener(this);
        start = true;

        if (iw==null) iw = new InfoWork(this);
        try {
          //	cw.initDB();
        	onConnect();
        } catch (Exception ex) {
        	ex.printStackTrace();
        	onError(ex.getMessage());
        }
//        db = aw.db;
    }

    /**
     * Остановка сервиса
     */
    public void stop() {
        getCron().stop();
        for(CommandProtocolListener i:getCommandProtocolListeners()) {
            try{
                i.logOut();
            } catch (Exception e) {}
        }
        cmd.destroyCommands();
        qe.clear();
        qe.stop();
        inq.stop();
        inq = null;
        outq.stop();
        outq = null;
        iw.closeDB();
        iw = null;
        start = false;
  //      removeAllListeners();
        // TODO Подумать как лучше убрать ссылки и слушатели очередей, парсеров команд, протоколом и т.п.
    }

    /**
     * Сервис запущен?
     *
     * @return
     */
    public boolean isRun() {
        return start;
    }

    /**
     * Возвращает имя данного сервиса
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает тип данного сервиса
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Набор настроек сервиса
     *
     * @return
     */
    public InfoConfig getConfig() {
        return config;
    }


    /**
     * Соединение с базой произошло, можно запускать УИНы
     */
    public void onConnect() {
        for(CommandProtocolListener i:getCommandProtocolListeners()) {
            i.logOn();
        }
        getCron().addTask(new ChangeStatusTask(this, 60000, iw));
//        ChangeStatusTask t = new ChangeStatusTask(this, 120000, iw);
//        t.addStatus(1, "Статус1", "Текст статуса 1");
//        t.addStatus(2, "Статус2", "Текст статуса 2");
//        t.addStatus(3, "Статус3", "Текст статуса 3");

 //       getCron().start();
    }

    /**
     * При подключении к базе произошла ошибка
     * @param e
     */
    public void onError(String e) {
        Log.getLogger(getName()).error("Ошибка соединения с базой данных. Отключаю сервис " + name);
        stop();
    }

    public IHTTPServiceConfig getHTTPConfig() {
        return CHB;
    }

    public Service build(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  





}

