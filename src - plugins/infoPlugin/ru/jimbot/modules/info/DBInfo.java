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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import ru.jimbot.db.DBObject;
import ru.jimbot.db.DBSQLite;
import ru.jimbot.util.Log;

/**
 *
 * @author Prolubnikov Dmitry
 */
public class DBInfo extends DBSQLite{
    private String serviceName = "";
    
    /** Creates a new instance of DBChat */
    public DBInfo(String name) throws Exception {
    	serviceName = name;
//        this.DRIVER = "org.hsqldb.jdbcDriver";
//        this.URL = "jdbc:hsqldb:file:";
//        this.dbName = "db/users";
//        this.openConnection();
    }
    
    public void createDB(){

    }

    public void status(int user, String sn, String type, int user2, String sn2, String msg) {
        try {
            PreparedStatement pst = getDb().prepareStatement("insert into status values(null, ?, ?, ?, ?, ?, ?, ?)");
            pst.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            pst.setInt(2,user);
            pst.setString(3,sn);
            pst.setString(4,type);
            pst.setInt(5,user2);
            pst.setString(6,sn2);
            pst.setString(7,msg);
            pst.execute();
            pst.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Запись лога в БД
     */
    public void log(int user, String sn, String type, String msg, int room){
        if(!InfoConfig.getInstance(serviceName).getBooleanProperty("chat.writeAllMsgs")) return;
        try{
            PreparedStatement pst = getDb().prepareStatement("insert into log values(null, ?, ?, ?, ?, ?, ?)");
            pst.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            pst.setInt(2,user);
            pst.setString(3,sn);
            pst.setString(4,type);
            pst.setString(5,msg);
            pst.setInt(6, room);
            pst.execute();
            pst.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Запись события в БД
     */
    public void event(int user, String sn, String type, int user2, String sn2, String msg) {
        try {
            PreparedStatement pst = getDb().prepareStatement("insert into events values(null, ?, ?, ?, ?, ?, ?, ?)");
            pst.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            pst.setInt(2,user);
            pst.setString(3,sn);
            pst.setString(4,type);
            pst.setInt(5,user2);
            pst.setString(6,sn2);            
            pst.setString(7,msg);
            pst.execute();
            pst.close();            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Поиск параметров пользователя
     */
    public boolean existUserProps(int user_id){
        boolean f = false;
        try{
        	Vector<String[]> v = this.getValues("select count(*) from user_props where user_id="+user_id);
        	if(Integer.parseInt(v.get(0)[0])>0) f = true;
//            if(openQuery("select count(*) from user_props where user_id="+user_id)){
//                String[] s=readNext();
//                if(Integer.parseInt(s[0])>0) f=true;
//                closeQuery();
//            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return f;
    }
    
    /**
     * Возвращает параметры пользователя
     */
    public Vector<String[]> getUserProps(int user_id){
        Vector<String[]> v=new Vector<String[]>();
        try{
            v=getValues("select name, val from user_props where user_id="+user_id);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return v;
    }
    
    /**
     * Устанавливает параметр пользователя
     */
    public boolean setUserProps(int user_id, String name, String val){
        boolean f = false;
        try{
            executeQuery("delete from user_props where user_id="+user_id+" and name='"+name + "'");
            PreparedStatement pst = getDb().prepareStatement("insert into user_props values(?, ?, ?)");
            pst.setString(2, name);
            pst.setString(3, val);
            pst.setInt(1, user_id);
            pst.execute();
            pst.close();
            f = true;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return f;
    }
  
  
    
    public Vector<Ads> getObjectVector(String q){
        Vector<Ads> v = new Vector<Ads>();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
        	stmt = getDb().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        	Log.getLogger(serviceName).debug("EXEC: " + q);
        	rSet = stmt.executeQuery(q);
            while(rSet.next()) {
                Ads a = new Ads();                
                    a.id = rSet.getInt(1);
                    a.txt = rSet.getString(2);
                    a.enable = rSet.getInt(3);
                    a.note = rSet.getString(4);
                    a.client = rSet.getString(5);
                    a.expDate = rSet.getTimestamp(6).getTime();
                    a.maxCount = rSet.getInt(7);  
                v.addElement(a);
            }
//            closeQuery();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
        	if(rSet!=null) try{rSet.close();} catch(Exception e) {};
        	if(stmt!=null) try{stmt.close();} catch(Exception e) {};
        }
        return v;
        
    }
    

    
    public void insertObjectAds(DBObject o){
        Ads a = (Ads)o;
        Log.getLogger(serviceName).debug("INSERT ads id=" + a.id);
        try{
            PreparedStatement pst = getDb().prepareStatement("insert into ads values (?,?,?,?,?,?,?)");
                      pst.setInt(1,a.id);
                      pst.setString(2,a.txt);
                      pst.setInt(3,a.enable);
                      pst.setString(4,a.note);
                      pst.setString(5,a.client);
                      pst.setTimestamp(6,new Timestamp(a.expDate));
                      pst.setInt(7,a.maxCount);          
  
            pst.execute();
            pst.close();
//            commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

   
    
       
      public void updateObjectAds(DBObject o){
        Ads a = (Ads)o;
        Log.getLogger(serviceName).debug("UPDATE ads id=" + a.id);
        try{
            PreparedStatement pst = getDb().prepareStatement("update ads set txt=?,enable=?," + 
                    "note=?,client=?,expDate=?,maxCount=? where id=" + a.id);
//            pst.setInt(1,us.id);
              pst.setString(1,a.txt);
              pst.setInt(2,a.enable);
              pst.setString(3,a.note);
              pst.setString(4,a.client);
              pst.setTimestamp(5,new Timestamp(a.expDate));
              pst.setInt(6,a.maxCount);      
              pst.execute();
              pst.close();
//            commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void createDB(String db) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    

    public void insertObjectAneks(DBObject o) {
        Aneks a = (Aneks)o;
        Log.getLogger(serviceName).debug("INSERT aneks id=" + a.id);
        try{
            PreparedStatement pst = getDb().prepareStatement("insert into aneks values (?,?)");
                      pst.setInt(1,a.id);
                      pst.setString(2,a.text);
            pst.execute();
            pst.close();
//            commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

   
    public void updateObjectAneks(DBObject o) {
               Aneks a = (Aneks)o;
        Log.getLogger(serviceName).debug("UPDATE aneks id=" + a.id);
        try{
            PreparedStatement pst = getDb().prepareStatement("update ads set text=? where id=" + a.id);
//            pst.setInt(1,us.id);
              pst.setString(1,a.text);
              pst.execute();
              pst.close();
//            commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public DBObject getObject(DBObject o, String q){
        try{
            o=o.getObject(getDb(), q);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return o;
    }



    public Vector<DBObject> getObjectVector(DBObject o, String q){
        Vector<DBObject> v = new Vector<DBObject>();
        try{
            v=o.getObjectVector(getDb(), q);
        } catch (Exception ex){
            ex.printStackTrace();
        } 
        return v;

    }



    public void insertObject(DBObject o){
        //Users us = (Users)o;
        Log.getLogger(serviceName).debug("INSERT "+o.getTableName());
        try{
             PreparedStatement pst = o.insertPrepStat(getDb());

            pst.execute();
            pst.close();
//            commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }




      public void updateObject(DBObject o){
        //Users us = (Users)o;
        Log.getLogger(serviceName).debug("UPDATE "+o.getTableName());
        try{
            PreparedStatement pst = o.updatePrepStat(getDb());

            pst.execute();
            pst.close();
//            commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}