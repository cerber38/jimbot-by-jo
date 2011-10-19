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

package ru.jimbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import ru.jimbot.core.Password;
import ru.jimbot.util.FileUtils;

import java.util.Vector;
import ru.jimbot.util.UserPreference;

/**
 * Общие настройки для бота
 * @author Prolubnikov Dmitry
 */

public class MainConfig {
    private static MainConfig config = null;

    public static final String VERSION = "v.0.5.0 alpha 3 mod by ~Jo-MA-Jo~ (10/09/2011)";
    private static final String PROG_TITLE = "jImBot";
    public static final int VER_INT = 18;
    private static final String FILE_NAME = "./jimbot-config.xml";

//    private boolean autoStart = false;
//    private boolean startHTTP = true;
//    private boolean delService = true;
//    private String httpUser_0 = "admin";
//    private Password httpPass_0 = new Password("admin");
    private int httpUsersCount = 1;
//    private int httpPort = 8888;
//    private int httpDelay = 10;
//    private int pause = 30;
//    private boolean checkNewVer = true;
    private Vector<String> serviceNames = new Vector<String>();
    private Vector<String> serviceTypes = new Vector<String>();
    private String defaultPath = "./";
    private static Properties appProps;
    public MainConfig() {
    }

    public static MainConfig getInstance(){
       if(config==null) {
               File file = new File(FILE_NAME);
    		config = new MainConfig();
      		config.setDefault();
    		if (file.exists()){
                    config.load();
                   System.out.println("Loaded JimBot Configuration successfully");
                }
    	}
        return config;
    }


    public final void load() {
        String fileName = FILE_NAME;
        File file = new File(fileName);
        setDefault();
 //       loadIgnorList();
        try {
            FileInputStream fi = new FileInputStream(file);
            appProps.loadFromXML(fi);
            fi.close();
            System.out.println("Load preferences ok");
//            loadServerList();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error opening preferences: ");
        }
    }

    public final void save() {
        String fileName = FILE_NAME;
        File file = new File(fileName);
        try {
            FileOutputStream fo = new FileOutputStream(file);
            appProps.storeToXML(fo, "jImBot properties");
            fo.close();
            System.out.println("Save preferences ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error saving preferences: ");
        }
    }




     public final void setDefault() {
        appProps = new Properties();
        setStringProperty("icq.serverDefault","login.icq.com");
        setIntProperty("icq.portDefault",5190);
        setIntProperty("httpPort",8888);

//        setStringProperty("main.Socks5ProxyHost","");
//        setStringProperty("main.Socks5ProxyPort","");
//        setStringProperty("main.Socks5ProxyUser","");
//        setStringProperty("main.Socks5ProxyPass","");
        setBooleanProperty("main.autoStart",true);
//      setIntProperty("icq.AUTORETRY_COUNT",5);
//        setBooleanProperty("icq.md5login",false);
//        setStringProperty("main.dbType","MYSQL");
//        setStringProperty("db.host","localhost:3306");
//        setStringProperty("db.user","root");
//        setStringProperty("db.pass","");
//        setStringProperty("db.dbname","botdb");
        setBooleanProperty("main.StartHTTP",true);
        setStringProperty("httpUser","admin"); // главный юзер для админки
        setStringProperty("httpPass","admin"); // пароль для доступа в админку

//        setIntProperty("http.UsersCount",1);

        setIntProperty("http.delay",10);
        setIntProperty("http.maxErrLogin",3);
        setIntProperty("http.timeErrLogin",10);
        setIntProperty("http.timeBlockLogin",20);
        setIntProperty("srv.servicesCount",1);
        setStringProperty("srv.serviceName0","ChatBot");
        setStringProperty("srv.serviceType0","chat");
        setBooleanProperty("main.checkNewVer", false);
        setBooleanProperty("delService",true);
        setIntProperty("timeOut",30);
    }

    public UserPreference[] getUserPreference(){
        UserPreference[] p = {
            new UserPreference(UserPreference.CATEGORY_TYPE,"main", "Основные настройки",""),

            new UserPreference(UserPreference.BOOLEAN_TYPE,"main.checkNewVer","Уведомлять о новых версиях",getBooleanProperty("main.checkNewVer")),

            new UserPreference(UserPreference.BOOLEAN_TYPE,"main.autoStart","Автозапуск сервисов бота",getBooleanProperty("main.autoStart")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"main.StartHTTP","Запускать HTTP сервер",getBooleanProperty("main.StartHTTP")),
            new UserPreference(UserPreference.INTEGER_TYPE,"http.delay","Время жизни http-сессии (минут)",getIntProperty("http.delay")),
            new UserPreference(UserPreference.INTEGER_TYPE,"httpPort","Порт HTTP",getIntProperty("httpPort")),
            new UserPreference(UserPreference.INTEGER_TYPE,"http.maxErrLogin","Число ошибочных входов для блокировки",getIntProperty("http.maxErrLogin")),
            new UserPreference(UserPreference.INTEGER_TYPE,"http.timeErrLogin","Допустимый период между ошибками",getIntProperty("http.timeErrLogin")),
            new UserPreference(UserPreference.INTEGER_TYPE,"http.timeBlockLogin","Время блокировки входа",getIntProperty("http.timeBlockLogin")),
            new UserPreference(UserPreference.STRING_TYPE,"httpUser","Главный пользователь HTTP-админки",getStringProperty("httpUser")),
            new UserPreference(UserPreference.PASS_TYPE,"httpPass","Пароль для входа в HTTP-админку",getStringProperty("httpPass")),

//            new UserPreference(UserPreference.CATEGORY_TYPE,"main", "Настройки прокси",""),
//            new UserPreference(UserPreference.STRING_TYPE,"main.Socks5ProxyHost","Прокси хост",getStringProperty("main.Socks5ProxyHost")),
//            new UserPreference(UserPreference.STRING_TYPE,"main.Socks5ProxyPort","Прокси порт",getStringProperty("main.Socks5ProxyPort")),
//            new UserPreference(UserPreference.STRING_TYPE,"main.Socks5ProxyUser","Прокси пользователь",getStringProperty("main.Socks5ProxyUser")),
//            new UserPreference(UserPreference.STRING_TYPE,"main.Socks5ProxyPass","Прокси пароль",getStringProperty("main.Socks5ProxyPass")),
            new UserPreference(UserPreference.CATEGORY_TYPE,"bot", "Настройки бота",""),
            new UserPreference(UserPreference.STRING_TYPE,"icq.serverDefault","ICQ Сервер 1",getStringProperty("icq.serverDefault")),
            new UserPreference(UserPreference.INTEGER_TYPE,"icq.portDefault","ICQ Порт 1",getIntProperty("icq.portDefault")),
  //          new UserPreference(UserPreference.BOOLEAN_TYPE,"icq.md5login","Безопасный логин",getBooleanProperty("icq.md5login")),
            new UserPreference(UserPreference.BOOLEAN_TYPE,"delService","Не удалять папки сервисов бота",getBooleanProperty("delService")),
            new UserPreference(UserPreference.INTEGER_TYPE,"timeOut","Пауза перед запуском сервисов (сек.)",getIntProperty("timeOut"))
        //    new UserPreference(UserPreference.INTEGER_TYPE,"timeOut","Пауза между подключениями UINs сервиса",getIntProperty("timeOut"))
        };
        return p;
    }



    /**
     * удаляет сервис
     * @param name
     */
    public void delService(String name) {
        // Сдвигаем элементы после удаленного на его место
    	boolean f = false;
    	for(int i=0; i<(getServicesCount()); i++){
    		if(getServiceName(i).equals(name))
    			f = true;
    		if(f&&i+1<getServicesCount()){
    			setStringProperty("srv.serviceName"+i, getServiceName(i+1));
    			setStringProperty("srv.serviceType"+i, getServiceType(i+1));
       		}
    	}
    	//Удаляем самый последний элемент
    	appProps.remove("srv.serviceName"+(getServicesCount()-1));
    	appProps.remove("srv.serviceType"+(getServicesCount()-1));
    	setIntProperty("srv.servicesCount", getServicesCount()-1);

        for(String s : serviceNames) {
            if(name.equals(s)) {
            	serviceNames.remove(s);
                if(!isDelService()){
                    FileUtils.deleteDirectory(new File("./services/" + s));
                    FileUtils.deleteDirectory(new File("./log/" + s));
                }
            	break;
            }
        }
    }

    /**
     * добавляет сервис
     * @param name
     * @param type
     * @throws java.io.IOException
     */
    public void addService(String name, String type) throws IOException {
        serviceNames.add(name);
        serviceTypes.add(type);
        int c = getServicesCount();
    	setIntProperty("srv.servicesCount", c+1);
    	setStringProperty("srv.serviceName"+c, name);
    	setStringProperty("srv.serviceType"+c, type);
        CreateTemplate(name, type);
    }



    /**
     * создает файлы и папки необходимые для сервиса
     * @param ServiceName
     * @param ServiceType
     * @throws java.io.IOException
     */
    public void CreateTemplate(String ServiceName,String ServiceType) throws IOException {

        //   FileUtils.copyDirectory(new File("./template/" + ServiceType), new File("./services/" + ServiceName));
           File logDir= new File("./log/"+ServiceName);
           if (!logDir.exists()) logDir.mkdir();
           File ServiceDir= new File("./services/"+ServiceName);
           if (!ServiceDir.exists()) ServiceDir.mkdir();
//            InputStream in = Manager.class.getClassLoader().getResourceAsStream("ru/jimbot/"+type+"/templats/"+file);
        String logProp= "";
        BufferedReader r = new BufferedReader(new InputStreamReader
                (Manager.class.getClassLoader().getResourceAsStream("ru/jimbot/templats/log4j.properties")));
        while (r.ready()){
            logProp += r.readLine() + '\n';
        }
        r.close();
           
           logProp=logProp.replace("ServicesBot", ServiceName);
           FileUtils.saveFile("./services/"+ServiceName+"/log4j.properties", logProp,"windows-1251");
    }

    /**
     * добавляет пользователя
     * @param name
     * @throws java.io.IOException
     */
    public void adduser(String name) throws IOException {
 //       int c = getUsersCount();
 //   	setIntProperty("http.UsersCount", c+1);
 //   	setStringProperty("httpUser_"+c, name);
 //   	setStringProperty("httpPass_"+c, name);
        UserConfig uc = UserConfig.getInstance(name);
        uc.setHttpPass(new Password(name));
        uc.save();
    }

    /**
     * удаляет пльзователя
     * @param name
     */
    public void deluser(String name) {
/*        // Сдвигаем элементы после удаленного на его место
    	boolean f = false;
    	for(int i=0; i<(getUsersCount()); i++){
    		if(getUserName(i).equals(name))
    			f = true;
    		if(f&&i+1<getUsersCount()){
    			setStringProperty("httpUser_"+i, getUserName(i+1));
    			setStringProperty("httpPass_"+i, getUserPass(i+1));
       		}
    	}
    	//Удаляем самый последний элемент
    	appProps.remove("httpUser_"+(getUsersCount()-1));
    	appProps.remove("httpPass_"+(getUsersCount()-1));
   	setIntProperty("http.UsersCount", (getUsersCount()-1));
   */
        File file=new File("./users/" + name+ ".xml");
                if(file.exists()) file.delete();
     }

     public boolean testUser(String name) {
        File file=new File("./users/" + name+ ".xml");
                return file.exists();
     }

    public int getUsersCount(){
    //	return getIntProperty("http.UsersCount");
        File dir = new File("./users");
            if (!(dir.exists())) dir.mkdirs();
        return FileUtils.listFiles("./users/","xml").size();
    }

//    public String getUserName(int i){
//   	return getStringProperty("httpUser_"+i);
//    }

//    public String getUserPass(int i){
//    	return getStringProperty("httpPass_"+i);
//    }

    public String getUser(int i){
    	//return getStringProperty("srvUser_"+i);
        return FileUtils.getName(FileUtils.listFiles("./users/","xml").get(i));
    }

    /**
     * проверка доступа к сервисам
     * @param user
     * @param srv
     * @return
     */
    public boolean testUserSrv(String user,String pass,String srv){
        if (testAdmin(user, pass))return true;
        if (!testUser(user))return false;
        UserConfig uc = UserConfig.getInstance(user);
        if (!uc.getHttpPass().equals(pass)) return false;
        return testText(srv,uc.getUserSrv());
    }

    /**
     * проверка доступа к настройкам
     * @param user
     * @param props
     * @return
     */
    public boolean testUserProps(String user,String pass,String props){
        if (testAdmin(user, pass))return true;
        if (!testUser(user))return false;
        UserConfig uc = UserConfig.getInstance(user);
        return testText(props,uc.getUserProps());
    }

    /**
     * проверка авторизации
     * @param user
     * @param props
     * @return
     */
    public boolean testUserAuth(String user,String pass){
        if (testAdmin(user, pass))return true;
        if (!testUser(user))return false;
        UserConfig uc = UserConfig.getInstance(user);
        return uc.getHttpPass().equals(pass);
    }

    public boolean testText(String text,String[] t) {
         for(String s : t) {
             
           if(text.equals(s)) return true;
        }
        return false;
    }

    /**
     * проверка на главного пользователя
     * @param name
     * @return
     */
    public boolean testAdmin(String name,String pass) {
     //   if (!testUser(name))return false;
         return (getHttpUser().equals(name)&&getHttpPass().getPass().equals(pass));
    }

//    public void setUserName(int i,String name){
//    	setStringProperty("httpUser_"+i,name);
//    }

//    public void setUserPass(int i,String pass){
//    	setStringProperty("httpPass_"+i,pass);
//    }

//    public void setUserSrv(int i,String name){
//    	setStringProperty("srvUser_"+i,name);
//    }

    public int getServicesCount(){
    	return getIntProperty("srv.servicesCount");
    }

    public String getServiceName(int i){
    	return getStringProperty("srv.serviceName"+i);
    }

    public String getServiceType(int i) {
    	return getStringProperty("srv.serviceType"+i);
    }

    public boolean isAutoStart() {
        return getBooleanProperty("main.autoStart");
    }

    public void setAutoStart(boolean autoStart) {
        setBooleanProperty("main.autoStart",autoStart);
    }

    public boolean isStartHTTP() {
        return getBooleanProperty("main.StartHTTP");
    }

    public void setStartHTTP(boolean startHTTP) {
        setBooleanProperty("main.StartHTTP",startHTTP);
    }

    public String getHttpUser() {
        return this.getStringProperty("httpUser");
    }

    public Password getHttpPass() {
        return new Password(this.getStringProperty("httpPass"));
    }

    public Vector<String> getHttpUsers() {
        Vector<String> HttpUsers = new Vector<String>();
        for(int i = 0;i<getUsersCount();i++){
            HttpUsers.add(UserConfig.getInstance(getUser(i)).getHttpUser());
        }
    //    System.out.println(HttpUsers);
        return HttpUsers;
    }
    public int getHttpPort() {
        return getIntProperty("httpPort");
    }

    public void setHttpPort(int httpPort) {
        setIntProperty("httpPort",httpPort);
    }

    public boolean isCheckNewVer() {
        return getBooleanProperty("main.checkNewVer");
    }

    public void setCheckNewVer(boolean checkNewVer) {
        setBooleanProperty("main.checkNewVer",checkNewVer);
    }

     public boolean isDelService() {
         return getBooleanProperty("delService");
    }

    public void setDelService(boolean delService) {
        setBooleanProperty("main.checkNewVer",delService);
    }

    public Vector<String> getServiceNames() {
       this.serviceNames = new Vector<String>();
           for(int i=0; i<getServicesCount(); i++){
            serviceNames.add(getServiceName(i));
           }
       return serviceNames;
    }

    public void setServiceNames(Vector<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public Vector<String> getServiceTypes() {
        this.serviceTypes = new Vector<String>();
           for(int i=0; i<getServicesCount(); i++){
            serviceTypes.add(getServiceType(i));
           }
        return serviceTypes;
    }

    public void setServiceTypes(Vector<String> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public int getHttpDelay() {
        return getIntProperty("http.delay");
    }

    public void setHttpDelay(int httpDelay) {
        setIntProperty("http.delay",httpDelay);
    }

    public int getPause() {
        return getIntProperty("timeOut");
    }

    public void setPause(int pause) {
        setIntProperty("timeOut",pause);
    }

   /**
    * @return the defaultPath
    */
    public String getDefaultPath() {
	 return defaultPath;
    }

   /**
    * @param defaultPath the defaultPath to set
    */
    public void setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
    }

    public static String getAbout(){
        return PROG_TITLE + " " + VERSION + "\n(c) Spec, 2006-2010\n" +
           "Поддержка проекта: http://jimbot.ru"/* +
           "\nIcqLib version: " + OscarInterface.getVersion()*/;
    }

    public void registerProperties(Properties _appProps) {
        appProps = _appProps;
    }

    public String getProperty(String key) {
        return appProps.getProperty(key);
    }

    public String getStringProperty(String key) {
        return appProps.getProperty(key);
    }

    public String getProperty(String key, String def) {
        return appProps.getProperty(key,def);
    }

    public void setProperty(String key, String val) {
        appProps.setProperty(key,val);
    }

    public void setStringProperty(String key, String val) {
        appProps.setProperty(key,val);
    }

    public void setIntProperty(String key, int val) {
        appProps.setProperty(key,Integer.toString(val));
    }

    public void setBooleanProperty(String key, boolean val) {
        appProps.setProperty(key, val ? "true":"false");
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(appProps.getProperty(key));
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.valueOf(appProps.getProperty(key)).booleanValue();
    }
}
