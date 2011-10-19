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

package ru.jimbot.modules.chat.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.Vector;
import ru.jimbot.db.DBObject;

/**
 *
 * @author Prolubnikov Dmitry
 */
public class Users extends DBObject {
    public int id = 0;
    public String sn = "";
    public String nick="";
    public String localnick = "";
    public String fname="";
    public String lname="Инфа не установленна";
    public String email="Инфа не установленна";
    public String city="город не установлен";
    public String homepage="OnLine";
    public int gender= 0;
    public int birthyear= 0;
    public int birthmonth= 0;
    public int birthday= 0;
    public int age= 0;
    public int country= 0;
    public int language= 0;
    public int state = 0;
    public String basesn="";
    public long createtime=0;
    public int room = 0;
    public long lastKick = System.currentTimeMillis();
    public String group = "";
    public int lbalans = 0;
    public int balans = 0;
    public int cena = 0;
    public long dateb = System.currentTimeMillis();
     public String lastnick="";
     public String pass="";
     public String webpage="нет";
    /** Creates a new instance of Users */
    public Users() {
        init();
        createtime = System.currentTimeMillis();
    }

    public Users (int _id,
            String _sn,
            String _nick,
            String _localNick,
            String _lastNick,
            String _fname,
            String _lname,
            String _email,
            String _city,
            String _homepage,
            int _gender,
            int _birthYear,
            int _birthMonth,
            int _birthDay,
            int _age,
            int _country) {
        id = _id;
        sn = _sn;
        nick = _nick;
        localnick = _localNick;
        lastnick = _lastNick;
        fname = _fname;
        lname = _lname;
        email = _email;
        city = _city;
        homepage = _homepage;
        gender = _gender;
        birthyear = _birthYear;
        birthmonth = _birthMonth;
        birthday = _birthDay;
        age = _age;
        country = _country;
        init();
    }

    private void init(){
        fields = new String[] {"id","sn","nick","localnick","fname","lname",
            "email","city","homepage","gender","birthyear","birthmonth","birthday",
            "age","country","language","state","basesn","createtime", "room", "lastkick",
            "lbalans", "balans", "cena", "dateb","lastnick","pass","webpage"};
        types = new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
            Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
            Types.INTEGER, Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,
            Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.VARCHAR,Types.TIMESTAMP,
            Types.INTEGER, Types.TIMESTAMP,Types.INTEGER,Types.INTEGER,Types.INTEGER,
            Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        tableName="users";
    }

    public String getInfo(){
        String s = "индификатор в чате id(ИД) = " + id + '\n';
        s += "Личный статус: "+homepage+'\n';
        s += "UIN (уин)="+sn+'\n';
        s += "ник в чате=" + localnick + '\n';
        s += "ник в ICQ="+nick+'\n';
        s += "Имя="+fname+'\n';
        s += "Возраст: "+gender+'\n';
        s += "Пол: "+lname+'\n';
        s += "Город="+city+'\n';
        s += "О себе: "+email+'\n';
        s += "Кошелек="+country+'\n';
        s += "Предупреждений="+birthday+'\n';
        if(state==-1) s += "\nпользователь забанен";
        return s;
    }

    public String[] getFields(){
        return fields;
    }

    public int[] getTypes(){
        return types;
    }

    public String getTableName(){
        return this.tableName;
    }
    
   public DBObject getObject(Connection db, String q){
        Users us = new Users();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
            stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rSet = stmt.executeQuery(q);
            rSet.next();
            us.id = rSet.getInt(1);
            us.sn = rSet.getString(2);
            us.nick = rSet.getString(3);
            us.localnick = rSet.getString(4);
            us.fname = rSet.getString(5);
            us.lname = rSet.getString(6);
            us.email = rSet.getString(7);
            us.city = rSet.getString(8);
            us.homepage = rSet.getString(9);
            us.gender = rSet.getInt(10);
            us.birthyear = rSet.getInt(11);
            us.birthmonth = rSet.getInt(12);
            us.birthday = rSet.getInt(13);
            us.age = rSet.getInt(14);
            us.country = rSet.getInt(15);
            us.language = rSet.getInt(16);
            us.state =  rSet.getInt(17);
            us.basesn = rSet.getString(18);
            us.createtime = rSet.getTimestamp(19).getTime();
            us.room = rSet.getInt(20);
            if(rSet.getLong(21)==0)
            	us.lastKick = System.currentTimeMillis();
            else
            	us.lastKick = rSet.getTimestamp(21).getTime();
            us.lbalans = rSet.getInt(22);
            us.balans = rSet.getInt(23);
            us.cena = rSet.getInt(24);
           if(rSet.getLong(25)==0)
            	us.dateb = System.currentTimeMillis();
            else
            	us.dateb = rSet.getTimestamp(25).getTime();
            us.lastnick = rSet.getString(26);
            us.pass = rSet.getString(27);
            us.webpage = rSet.getString(28);
//            closeQuery();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
        	if(rSet!=null) try{rSet.close();} catch(Exception e) {};
        	if(stmt!=null) try{stmt.close();} catch(Exception e) {};
        }
        return us;
    }

    public Vector<DBObject> getObjectVector(Connection db, String q){
        Vector<DBObject> v = new Vector<DBObject>();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
        	stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        	rSet = stmt.executeQuery(q);
            while(rSet.next()) {
                Users us = new Users();
                us.id = rSet.getInt(1);
                us.sn = rSet.getString(2);
                us.nick = rSet.getString(3);
                us.localnick = rSet.getString(4);
                us.fname = rSet.getString(5);
                us.lname = rSet.getString(6);
                us.email = rSet.getString(7);
                us.city = rSet.getString(8);
                us.homepage = rSet.getString(9);
                us.gender = rSet.getInt(10);
                us.birthyear = rSet.getInt(11);
                us.birthmonth = rSet.getInt(12);
                us.birthday = rSet.getInt(13);
                us.age = rSet.getInt(14);
                us.country = rSet.getInt(15);
                us.language = rSet.getInt(16);
                us.state =  rSet.getInt(17);
                us.basesn = rSet.getString(18);
                us.createtime = rSet.getTimestamp(19).getTime();
                us.room = rSet.getInt(20);
                if(rSet.getLong(21)==0)
                	us.lastKick = System.currentTimeMillis();
                else
                	us.lastKick = rSet.getTimestamp(21).getTime();
                us.lbalans = rSet.getInt(22);
                us.balans = rSet.getInt(23);
                us.cena = rSet.getInt(24);
           if(rSet.getLong(25)==0)
            	us.dateb = System.currentTimeMillis();
            else
            	us.dateb = rSet.getTimestamp(25).getTime();
                 us.lastnick = rSet.getString(26);
                 us.pass = rSet.getString(27);
                 us.webpage = rSet.getString(28);
                v.addElement(us);
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
    
    public PreparedStatement insertPrepStat(Connection db) {
             Users us = this;
             PreparedStatement pst = null;
             String sql = "insert into "+tableName+" values ( ";
             for(int i=0; i<=fields.length; i++){
                sql+= (i!=fields.length) ? "?" : " )";
                sql+= (i+1<fields.length) ? "," : "";
             }
            //  System.out.println(sql);
        try{
            pst = db.prepareStatement(sql);
            pst.setInt(1,us.id);
            pst.setString(2,us.sn);
            pst.setString(3,us.nick);
            pst.setString(4,us.localnick);
            pst.setString(5,us.fname);
            pst.setString(6,us.lname);
            pst.setString(7,us.email);
            pst.setString(8,us.city);
            pst.setString(9,us.homepage);
            pst.setInt(10,us.gender);
            pst.setInt(11,us.birthyear);
            pst.setInt(12,us.birthmonth);
            pst.setInt(13,us.birthday);
            pst.setInt(14,us.age);
            pst.setInt(15,us.country);
            pst.setInt(16,us.language);
            pst.setInt(17,us.state);
            pst.setString(18,us.basesn);
            pst.setTimestamp(19,new Timestamp(us.createtime));
            pst.setInt(20,us.room);
            pst.setTimestamp(21,new Timestamp(us.lastKick));
            pst.setInt(22,us.lbalans);
            pst.setInt(23,us.balans);
            pst.setInt(24,us.cena);
            pst.setTimestamp(25,new Timestamp(us.dateb));
            pst.setString(26,us.lastnick);
            pst.setString(27,us.pass);
            pst.setString(28,us.webpage);
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }

    public PreparedStatement updatePrepStat(Connection db) {
             Users us = this;
             PreparedStatement pst = null;
             String sql = "update "+tableName+" set ";
             for(int i=1; i<=fields.length; i++){
                sql+= (i!=fields.length) ? fields[i]+"=?" : " where id="+us.id;
                sql+= (i+1<fields.length) ? "," : "";
             }
            // System.out.println(sql);
        try{
            pst = db.prepareStatement(sql);
            pst.setString(1,us.sn);
            pst.setString(2,us.nick);
            pst.setString(3,us.localnick);
            pst.setString(4,us.fname);
            pst.setString(5,us.lname);
            pst.setString(6,us.email);
            pst.setString(7,us.city);
            pst.setString(8,us.homepage);
            pst.setInt(9,us.gender);
            pst.setInt(10,us.birthyear);
            pst.setInt(11,us.birthmonth);
            pst.setInt(12,us.birthday);
            pst.setInt(13,us.age);
            pst.setInt(14,us.country);
            pst.setInt(15,us.language);
            pst.setInt(16,us.state);
            pst.setString(17,us.basesn);
            pst.setTimestamp(18,new Timestamp(us.createtime));
            pst.setInt(19,us.room);
            pst.setTimestamp(20,new Timestamp(us.lastKick));
            pst.setInt(21,us.lbalans);
            pst.setInt(22,us.balans);
            pst.setInt(23,us.cena);
            pst.setTimestamp(24,new Timestamp(us.dateb));
            pst.setString(25,us.lastnick);
            pst.setString(26,us.pass);
            pst.setString(27,us.webpage);
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }    
}
