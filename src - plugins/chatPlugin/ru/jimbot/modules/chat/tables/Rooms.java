/**
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ru.jimbot.modules.chat.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Statement;
import java.util.Vector;
import ru.jimbot.db.DBObject;

/**
* Комната в чате
*
* @author Prolubnikov Dmitry
*/
public class Rooms extends DBObject {
	private int id=0;
	private String name="";
	private String topic="";
	private int user_id=0;
	private String pass="";

	public Rooms(){
          init();
	}

	public Rooms(int _id, String _name, String _topic, int _user_id){
		id=_id;
		name=_name;
		topic=_topic;
		user_id=_user_id;
                init();
	}

    private void init(){
        fields = new String[] {"id","name","topic","user_id","pass"};
        types = new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR};
        tableName="rooms";
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
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

            public boolean checkPass(String p) {
        if (pass.equals("")) {
            return true;
        } else
            return pass.equals(p);
   }

   public void setPass(String pass) {
      this.pass = pass;
   }

        public DBObject getObject(Connection db, String q){
        Rooms r = new Rooms();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
            stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rSet = stmt.executeQuery(q);
            rSet.next();
            r.id = rSet.getInt(1);
            r.name= rSet.getString(2);
            r.topic  = rSet.getString(3);
            r.user_id = rSet.getInt(4);
            r.pass  = rSet.getString(5);
            
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
        	if(rSet!=null) try{rSet.close();} catch(Exception e) {};
        	if(stmt!=null) try{stmt.close();} catch(Exception e) {};
        }
        return r;
    }

    public Vector<DBObject> getObjectVector(Connection db, String q){
        Vector<DBObject> v = new Vector<DBObject>();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
        	stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        	rSet = stmt.executeQuery(q);
            while(rSet.next()) {
            Rooms r = new Rooms();
            r.id = rSet.getInt(1);
            r.name= rSet.getString(2);
            r.topic  = rSet.getString(3);
            r.user_id = rSet.getInt(4);
            r.pass  = rSet.getString(5);
                v.addElement(r);
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
             Rooms r = this;
             PreparedStatement pst = null;
             String sql = "insert into "+tableName+" values (";
             for(int i=0; i<=fields.length; i++){
                sql+= (i!=fields.length) ? "?" : ")";
                sql+= (i+1<fields.length) ? "," : "";
             }
        try{
            pst = db.prepareStatement(sql);
            pst.setInt(1,r.id);
            pst.setString(2,r.name);
            pst.setString(3,r.topic);
            pst.setInt(4,r.user_id);
            pst.setString(5,r.pass);
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }

    public PreparedStatement updatePrepStat(Connection db) {
             Rooms r = this;
             PreparedStatement pst = null;
             String sql = "update "+tableName+" set ";
             for(int i=1; i<=fields.length; i++){
                sql+= (i!=fields.length) ? fields[i]+"=?" : " where id="+r.id;
                sql+= (i+1<=fields.length) ? "," : "";
             }
        try{
            pst = db.prepareStatement(sql);
            pst.setString(1,r.name);
            pst.setString(2,r.topic);
            pst.setInt(3,r.user_id);
            pst.setString(4,r.pass);
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }    


}
