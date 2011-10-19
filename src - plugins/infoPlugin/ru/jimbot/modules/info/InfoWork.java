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

package ru.jimbot.modules.info;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import ru.jimbot.util.Log;
import ru.jimbot.util.FileUtils;

/**
 *
 * @author Prolubnikov Dmitry
 */
public class InfoWork {
    public ConcurrentHashMap<String,DBInfo> db;
    private Random r = new Random();
    private String serviceName = "";
    private InfoConfig config;
    private InfoService srv;
    public int maxAnek=0;
    public int maxAds=0;    
//    private Vector<Integer> adsKey; //= new Vector<Integer>();
    private ConcurrentHashMap <Integer,Ads> ads;    

    /** Creates a new instance of UserWork */
    public InfoWork(InfoService srv) {
        try{

            this.srv = srv;
            serviceName = srv.getName();
            config = (InfoConfig)srv.getConfig();
            db = new ConcurrentHashMap<String,DBInfo>();
            initDB();
            readAdsKey();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    /**
     *Инициализация доступных баз
     */
    public void initDB() {
        try {
            Vector v =FileUtils.listFiles("services/" + serviceName + "/db/","db");
         for(int i=0; i<v.size(); i++){
             String file_db = (String) v.get(i);
             DBInfo temp_db = new DBInfo(serviceName);
             temp_db.openConnection(serviceName, file_db);
            db.put(FileUtils.getName(file_db),temp_db);
             Log.getLogger(serviceName).info("Connected to: " + v.get(i));
    	 }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void closeDB(){
         for( String temp_db:  db.keySet()){
             db.get(temp_db).shutdown();
         }
    }

    public ConcurrentHashMap<String,DBInfo> getDB() {
        return this.db;
    }

      
     /**
     * Обновление данных при изменении БД
     */
    public void refreshData(){
    	readAdsKey();
    	maxAnek=(int)db.get("aneks").getLastIndex("aneks");
    }
    
    /**
     * Читает ключи к активным рекламным объявлениям
     */
    private void readAdsKey(){
    	ads = new ConcurrentHashMap <Integer,Ads> ();   
    	Statement stm = null;
    	ResultSet rs = null;
     //   int i=1;
    	try {
    		stm = db.get("ads").getDb().createStatement();
    		rs = stm.executeQuery("select * from ads where enable=1");
    		while(rs.next()){
                  Ads a = new Ads();
                      a.id=rs.getInt(1);
                      a.txt=rs.getString(2);
                      a.enable=rs.getInt(3);
                      a.note=rs.getString(4);
                      a.client=rs.getString(5);
                      a.expDate=rs.getLong(6);
                      a.maxCount=rs.getInt(7);                     
    		      ads.put(a.id,a);
                //    i++;
    		}
    	} catch (Exception ex){
    		ex.printStackTrace();
    	} finally {
    		if(rs!=null) try{rs.close();}catch(Exception e){};
    		if(stm!=null) try{stm.close();}catch(Exception e){};
    	}
    }
    

    
    public void addLogAds(int id){
    	try{
            PreparedStatement pst = db.get("ads_log").getDb().prepareStatement("insert into ads_log values (?, ?, ?)");
            pst.setInt(1,id);
            pst.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
            pst.setString(3, "");
            pst.execute();
            pst.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Статистика показа рекламы за сутки
     * @return
     */
    public String adsStat(){
        long last = System.currentTimeMillis() - 1000*3600*24;
    	String s = "Статистика показа рекламы:\n";
    	Statement stm = null;
		ResultSet rs = null;
		try {
			stm = db.get("ads_log").getDb().createStatement();
			rs = stm.executeQuery("SELECT ads_id, count( ads_id ) cnt " +
					"FROM `ads_log` " +
					"WHERE time  >= "+last+" "+
//					"WHERE (date('now', 'start of mounth') - date(time)) <1 " +
					"GROUP BY ads_id ORDER BY ads_id");
			while(rs.next()){
				s += rs.getString(1) + " - " + rs.getString(2) + "\n";
			}
		} catch (Exception ex){
            ex.printStackTrace();
        } finally {
        	if(rs!=null) try{rs.close();}catch(Exception e){};
        	if(stm!=null) try{stm.close();}catch(Exception e){};
        }
        return s;
    }
    
   
    /**
     * Событие с вероятностью 1/i
     */
    public boolean testRnd(int i){
        if(i<=1)
            return false;
        else
            return r.nextInt(i)==1;
    }
    
    public String getAnek(int id) {
        String s = "";
//    	Aneks a;
        if(id<1 || id>CountAnek()) return "Нет такого анека";
        Statement stm = null;
		ResultSet rs = null;
		try {
			stm = db.get("aneks").getDb().createStatement();
			rs = stm.executeQuery("select text from aneks where id=" + id);
			while(rs.next()){
				s = rs.getString(1);
			}
		} catch (Exception ex){
            ex.printStackTrace();
        } finally {
        	if(rs!=null) try{rs.close();}catch(Exception e){};
        	if(stm!=null) try{stm.close();}catch(Exception e){};
        }
//        a = (Aneks)db.getObject("select * from aneks where id=" + id);
        return "Анекдот №" + id + "\n" + s + getAds();
    }
    
    public int CountAnek() {
        if (maxAnek==0)
            maxAnek=(int)db.get("aneks").getLastIndex("aneks");
        return maxAnek;
    }
    
     public int CountAds() {
        if (maxAds==0)
            maxAds=(int)db.get("ads").getLastIndex("ads");
        return maxAds;
    }
     
    public String getAnek(){
        Random r = new Random();
//        long i = db.getLastIndex("db.aneks");
        int i = CountAnek();
        int t = getRND(i-1) +1;
        return getAnek(t);
    }
    
    public void addAnek(String s) {
        int i = CountAnek()+1;
        Aneks a = new Aneks(i,s);
        db.get("aneks").insertObjectAneks(a);
        maxAnek=i;
    }
    
    /**
     * Добавление анекдота во временную таблицу
     * @param s
     */
    public void addTempAnek(String s, String uin){
    	try{
            PreparedStatement pst =  db.get("aneks_tmp").getDb().prepareStatement("insert into aneks_tmp values (?, ?, ?)");
            pst.setString(1,null);
            pst.setString(2,s);
            pst.setString(3, uin);
            pst.execute();
            pst.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
     public String adsList() {
    	String s = "";
      Integer[] ADS=(Integer[])ads.keySet().toArray(new Integer[0]);
      Arrays.sort(ADS);
     	for(Integer i : ADS) {
    		try {
    			Ads a = ads.get(i);
    			s += "[" + i + ", " + a.client + ", " + a.note + ", " +
    				new SimpleDateFormat("dd.MM.yyyy").format(new Date(a.expDate)) +
    				"] - " + a.txt + "\n";
    		} catch (Exception e) {
    			e.printStackTrace();
    			s += e.getMessage();
    		}
    	}
        if (s.equals(""))s = "Список пуст!";
    	return s;
    } 
    
    
    
 
   public int getLastIndex(String base,String tabl) {
        return (int)this.db.get(base).getLastIndex(tabl);
    }
 
  
    public int addAds(String txt,String note,String сlient) {
        int i = CountAds()+1;
        Ads a = new Ads(i, txt, 1, note, сlient,System.currentTimeMillis() + 7*24*3600*1000,0);
        db.get("ads").insertObjectAds(a);
        ads.put(i, a);
        maxAds=i;
        return i;
    }
    
    public boolean extendAds(int id) {
    	boolean f = false;
    	if(!ads.contains(id)) return false;
    	try {
               Ads a = getFullAds(id);
               a.expDate=System.currentTimeMillis() + 7*24*3600*1000;
               updateAds(a);
    		f = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return f;
    }    
    public String getAdsForStatus() {
        if(!config.isUseAds())
    		return "";
    	String s = getAds(getRND(CountAds()-1)+1);
        return s;
    } 
  
     /**
     * Возвращает данные о пользователе из БД
     * @param id - ИД пользователя
     * @return
     */
    private Ads getAdsFromDB(int id){
    	Vector v =  db.get("ads").getObjectVector("select * from ads where id="+id);
    	Ads a = new Ads();
    	if(v.size()>0) a = (Ads)v.get(0);
     	return a;
    }   
    
    
    public Ads getFullAds(int id) {
    	if(ads.containsKey(id)){
                addLogAds(id);
    		return ads.get(id);
    	}
    	Ads a = getAdsFromDB(id);
    	if(a.id==0) return a; // Нет в БД, или глюк :)
        addLogAds(id);
    	updateAds(a);
    	return a;
    }   
    /**
     * Возвращает случайное объявление, или ""
     * @return
     */
    public String getAds(){
    	if(!config.getBooleanProperty("bot.useAds"))
    		return "";
    	String s = getFullAds(getRND(CountAds()-1)+1).txt;
    	if(s.equals(""))
    		return "";
    	else
    		return "\n***\n" + s;
    } 
    
    public String getAds(int id){
    	String s = "";
    	if(testRnd(config.getIntProperty("bot.adsRate"))){
            s = getFullAds(id).txt;
    	}
    	return s;
    }     
    
    public boolean delAds(int id) {
    	boolean f = false;
        Ads a = getFullAds(id);
    	if(a.id==0) return false;
    	try {
            a.enable=0;
            updateAds(a);
    		ads.remove(id);
                maxAds--;
    		f = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return f;
    }  
    
    public void updateAds(Ads a) {
        ads.put(a.id,a); //кэшируем
        db.get("ads").updateObjectAds(a);
    }

        public int getRND(int i){
        if (i<=0)return 0;
        return r.nextInt(i);
    }
}