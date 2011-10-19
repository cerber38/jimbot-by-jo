/**
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2009 JimBot project
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

package ru.jimbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//import org.eclipse.jetty.server.nio.SelectChannelConnector;
//import ru.jimbot.http.HandlerFactory;
//import ru.jimbot.modules.anek.AnekService;
//import ru.jimbot.modules.http.Server;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.jimbot.util.Log;
import ru.jimbot.core.api.IProtocolManager;
import ru.jimbot.core.api.IServiceBuilder;
import ru.jimbot.core.api.Service;
import ru.jimbot.protocol.ProtocolBuilder;

/**
 * Управление сервисами бота
 *
 * @author Prolubnikov Dmitriy
 *
 */
public class Manager {

	private HashMap<String, Service> services = new HashMap<String, Service>();
	private Monitor2 mon = new Monitor2();
	private static Manager mn = null;
	private ConcurrentHashMap<String, Object> data = null;
//    private org.eclipse.jetty.server.Server server = null;
	private Map<String, IServiceBuilder> sbs = new ConcurrentHashMap<String, IServiceBuilder>();
	private Map<String, IProtocolManager> ipm = new ConcurrentHashMap<String, IProtocolManager>();
        private ProtocolBuilder pb= new ProtocolBuilder();
        private ServiceBuilder sb= new ServiceBuilder();
	/**
	 * Инициализация. Создание сервисов, определенных в файле конфигурации.
	 */
	public Manager() {

		createState();
		mon.start();

//		for(int i=0;i<MainConfig.getInstance().getServiceNames().length;i++){
//			addService(MainConfig.getInstance().getServiceNames()[i],
//                    MainConfig.getInstance().getServiceTypes()[i]);
//		}
	}

	/**
	 * Добавляет билдер сервиса при регистрации нового плагина,
	 * создает сервисы, поддерживаемые этим билдером.
	 * @param sb
	 */
	public void putServiceBuilder(IServiceBuilder sb) {
		sbs.put(sb.getServiceType(), sb);
		//System.out.println("put!");
		for(int i=0;i<MainConfig.getInstance().getServiceNames().size();i++){
			if(sb.getServiceType().equals(MainConfig.getInstance().getServiceTypes().get(i)))
				addService(MainConfig.getInstance().getServiceNames().get(i),
						MainConfig.getInstance().getServiceTypes().get(i));
		}
	}

	/**
	 * Удаление билдеров при отключении плагинов, реализующих сервисы
	 * @param sb
	 */
	public void removeServiceBuilder(IServiceBuilder sb) {
		sbs.remove(sb.getServiceType());
		//TODO Остановить и удалить необходимые сервисы
	}

	public IServiceBuilder getServiceBuilder(String type) {
		return sbs.get(type);
	}

	public Collection<String> getAvailableServices() {
		return sbs.keySet();
	}

	public void putProtocolManager(IProtocolManager m) {
		ipm.put(m.getProptocolName(), m);
	}

	public void removeProtocolManager(IProtocolManager m) {
		ipm.remove(m.getProptocolName());
	}

	public Map<String, IProtocolManager> getAllProtocolManagers() {
		return ipm;
	}

	/**
	 * Возвращает нужный объект по ключу
	 * @param key
	 * @return
	 */
	public Object getData(String key){
	    if(!data.containsKey(key)) return null;
	    return data.get(key);
	}

	/**
	 * Запоминает новое значение объекта
	 * @param key
	 * @param o
	 */
	public void setData(String key, Object o){
	    data.put(key, o);
	}

	/**
	 * Перезапускает все действующие процессы
	 */
	public  static void restart() {
	    mn.stopAll();
	    mn.mon.stop();
	//    mn = null;
	    System.gc();
	    getInstance().startAll();
	}

	/**
	 * Возвращает экземпляр класса. При необходимости производит его создание и инициализацию.
	 * @return
	 */
	public static Manager getInstance() {
		if(mn==null){
			mn = new Manager();
			mn.data = new ConcurrentHashMap<String, Object>();
                        mn.pb.build();
                        mn.sb.build();
		}
		return mn;
	}

	/**
	 * Общее число зареганых сервисов
	 * @return
	 */
	public int getServiceCount() {
		return services.keySet().size();
	}
        
       public Map getAllHTTPServiceConfig() {
                   Map map = new HashMap();
		for(Service s : services.values()){
                    try{
		       map.putAll(s.getHTTPConfig().getHTTPServiceConfig());
                    } catch (Exception ex) {}
                }

        return map;
	}
       /**
	 * Общее число зареганых сервисов
	 * @return
	 */
	public HashMap<String, Service> getServiceHesh() {
            return  services;
	}

	/**
	 * Возвращает набор имен сервисов (для последующего перебора)
	 * @return
	 */
	public Set<String> getServiceNames() {
		return services.keySet();
	}

	/**
     * Проверка состояния файла. При необходимости - выключение
     */
    public void testState(){
    	BufferedReader r = null;
        try{
            File f = new File("./state");
            if(!f.exists()) {
                createState();
                return;
            }
            r = new BufferedReader(new InputStreamReader(new FileInputStream("state")));
            String s = r.readLine();
            if(s.equals("Stop")) {
                exit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	if(r!=null){
        		try{r.close();}catch(Exception e) {}
        	}
        }
    }

    /**
     * Проверка подключения к БД. Повоторят попытку подключиться если база недоступна.
     */
//    public void testDB(){
//    	for(String s : services.keySet()){
//    		if(services.get(s).isRun()){
//    			try {
//    				if(services.get(s).getDB()!=null)
//	    				if(services.get(s).getDB().isClosed()){
//	    					services.get(s).getDB().getDb();
//	    				}
//    			} catch (SQLException e) {
//    				e.printStackTrace();
//    			}
//    		}
//    	}
//    }

    /**
     * Создает файл статуса программы
     */
    private void createState(){
        try {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("state")));
            w.write("start");
            w.newLine();
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public void exit() {
    	stopAll();
//    	if(MainProps.getBooleanProperty("main.StartHTTP"))
//            stopHTTPServer();
    	mon.stop();
    	Log.getDefault().info("Exit bot " + new Date(System.currentTimeMillis()).toString());
//    	for(String s : services.keySet()){
//    		services.get(s).getProps().save();
//    	}
    	System.exit(0);
    }

	/**
	 * Добавление нового сервиса
	 * @param name
	 * @param type = "anek", "chat"
	 */
	public void addService(String name, String type) {
		System.out.println("add service " + name + " : " + type);
		services.put(name, sbs.get(type).build(name));
//		if(type.equals("chat")){
////			services.put(name, new ChatServer(name));
//            // TODO ...
//		} else if (type.equals("anek")){
////			services.put(name, new AnekServer(name));
//            services.put(name, new AnekService(name));
//		} else {
//			Log.getDefault().error("Неизвестный тип сервиса: "+type);
//		}
////		services.get(name).getProps().load();
	}

	/**
	 * Удаление сервиса
	 * @param name
	 */
	public void delService(String name){
		if(services.containsKey(name)){
			try{
			    services.get(name).stop();
			} catch (Exception e) {}
			services.remove(name);
//			MainProps.delService(name);
		} else {
			Log.getDefault().error("Отсутствет сервис с именем "+name);
		}
	}

	/**
	 * Запуск сервиса
	 * @param name
	 */
	public void start(String name){
		if(services.containsKey(name)){
			Log.getLogger(name).info("Запускаю сервис: " + name);
			services.get(name).start();
		} else {
			Log.getDefault().error("Отсутствет сервис с именем "+name);
		}
	}

	/**
	 * Остановка сервиса
	 * @param name
	 */
	public void stop(String name){
		if(services.containsKey(name)){
			Log.getLogger(name).info("Останавливаю сервис: " + name);
			services.get(name).stop();
		}else{
			Log.getDefault().error("Отсутствет сервис с именем "+name);
		}
	}

//	/**
//	 * Запуск всех сервисов
//	 */
//	public void startAll() {
//          Log.getDefault().info("["+new Time(System.currentTimeMillis()).toString()+"] - "+MainConfig.getInstance().getPause()+" сек. ожидание запуска сервисов");
//          new StartServices().start();
//	}

        /**
	 * Запуск всех сервисов
	 */
	public void startAll() {
           
    		for(Service s : services.values()){
			if(s.getConfig().isAutoStart()){
                            Log.getLogger(s.getName()).info("Запускаю сервис: " + s.getName());
                            s.start();
                        }
                }
	}

	/**
	 * Остановка всех сервисов
	 */
	public void stopAll() {
		for(Service s : services.values()){
			if(s.isRun())s.stop();
		}
	}

	/**
	 * Возвращает ссылку на конкретный сервис
	 * @param name
	 * @return
	 */
	public Service getService(String name) {
		return services.get(name);
	}

	/**
	 * Статус выполнения данного сервиса
	 * @param name
	 * @return
	 */
	public boolean isRun(String name) {
		if(services.containsKey(name))
			return services.get(name).isRun();
		else
			return false;
	}


//    public synchronized org.eclipse.jetty.server.Server getHTTPServer() {
//        if(server == null) {
//            startHTTPServer();
//        }
//        return server;
//    }
//
//    public synchronized void startHTTPServer() {
//        server = new org.eclipse.jetty.server.Server();
//        try {
//            SelectChannelConnector connector = new SelectChannelConnector();
//            connector.setPort(MainConfig.getInstance().getHttpPort());
//            server.addConnector(connector);
//            server.setHandler(HandlerFactory.getAvailableHandlers());
//
////            HandlerFactory.setAvailableHandlers(server);
//            server.start();
////            server.join();
//
////            new HandlerFactory().setAvailableHandlers2(server);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public synchronized void stopHTTPServer() {
//        try {
//            if(server != null) server.stop();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
