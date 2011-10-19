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
import java.sql.Types;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Random;

import ru.jimbot.db.DBObject;

/**
 *
 * @author Prolubnikov Dmitry
 */
public class Invites extends DBObject {
    public int id = 0;
    public int user_id=0;
    public long time=0;
    public String invite="";
    public int new_user=0;
    public long create_time=0;
    
    /** Creates a new instance of Invites */
    public Invites() {
        init();
    }
    
        public Invites(int _id,
            int _user_id,
            long _time,
            String _invite,
            int _new_user,
            long _createTime){
        id = _id;
        user_id = _user_id;
        time = _time;
        invite = _invite;
        new_user = _new_user;
        create_time = _createTime;
    }
    
    public Invites(int _id,
            int _user_id){
        id = _id;
        user_id = _user_id;
        time = System.currentTimeMillis();
        invite = getUID();
        new_user=0;
        create_time=0;
    }

    private void init(){
        fields = new String[] {"id","user_id", "time", "invite", "new_user", "create_time"};
        types = new int[] {Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR,
            Types.INTEGER, Types.TIMESTAMP};
        tableName="invites";        
    }
        
    public boolean checkPrompt(String p){
        return p.equalsIgnoreCase(invite);
    }
    
    public void setUser(int user){
        new_user = user;
        create_time = System.currentTimeMillis();
    }
    
    public String getUID(){
        String s = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
        Random r = new Random();
        String v="";
        for(int i=0;i<10;i++){
            v += s.charAt(r.nextInt(s.length()));
        }
        return v;
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
        Invites in = new Invites();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
            stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rSet = stmt.executeQuery(q);
            rSet.next();
            in.id = rSet.getInt(1);
            in.user_id = rSet.getInt(2);
            in.time = rSet.getTimestamp(3).getTime();
            in.invite = rSet.getString(4);
            in.new_user = rSet.getInt(5);
            in.create_time = rSet.getTimestamp(6).getTime();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
        	if(rSet!=null) try{rSet.close();} catch(Exception e) {};
        	if(stmt!=null) try{stmt.close();} catch(Exception e) {};
        }
        return in;
    }

    public Vector<DBObject> getObjectVector(Connection db, String q){
        Vector<DBObject> v = new Vector<DBObject>();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
        	stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        	rSet = stmt.executeQuery(q);
            while(rSet.next()) {
            Invites in = new Invites();
            in.id = rSet.getInt(1);
            in.user_id = rSet.getInt(2);
            in.time = rSet.getTimestamp(3).getTime();
            in.invite = rSet.getString(4);
            in.new_user = rSet.getInt(5);
            in.create_time = rSet.getTimestamp(6).getTime();
                v.addElement(in);
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
             Invites in = this;
             PreparedStatement pst = null;
             String sql = "insert into "+tableName+" values (";
             for(int i=0; i<=fields.length; i++){
                sql+= (i!=fields.length) ? "?" : ")";
                sql+= (i+1<fields.length) ? "," : "";
             }
        try{
            pst = db.prepareStatement(sql);
            pst.setInt(1,in.id);
            pst.setInt(2,in.user_id);
            pst.setTimestamp(3,new Timestamp(in.time));
            pst.setString(4,in.invite);
            pst.setInt(5,in.new_user);
            pst.setTimestamp(6,null/*new Timestamp(in.create_time)*/);
            pst.setString(7, "");
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }

    public PreparedStatement updatePrepStat(Connection db) {
             Invites in = this;
             PreparedStatement pst = null;
             String sql = "update "+tableName+" set ";
             for(int i=1; i<=fields.length; i++){
                sql+= (i!=fields.length) ? fields[i]+"=?" : " where id="+in.id;
                sql+= (i+1<=fields.length) ? "," : "";
             }
        try{
            pst = db.prepareStatement(sql);
           // pst.setInt(6,in.id);
            pst.setInt(1,in.user_id);
            pst.setTimestamp(2,new Timestamp(in.time));
            pst.setString(3,in.invite);
            pst.setInt(4,in.new_user);
            pst.setTimestamp(5,new Timestamp(in.create_time));
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }       
}
