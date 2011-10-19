package ru.jimbot.modules.chat.tables;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Statement;
import java.util.Vector;
import ru.jimbot.db.DBObject;

/**
 *
 * @author ~jo-MA-jo~
 */
public class LinkMind extends DBObject {
    public int id = 0;
    public String word = "";
    public String link="";
    public String subject = "";

    
   public LinkMind (int _id, String _word, String _link, String _subject) {
        id = _id;
        word = _word;
        link = _link;
        subject = _subject;
        init();
    }
   
   public LinkMind () {
        init();
   }
    private void init(){
        fields = new String[] {"id","word","link","subject"};
        types = new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        tableName="essence";
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
        LinkMind lm = new LinkMind();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
            stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rSet = stmt.executeQuery(q);
            rSet.next();
            lm.id = rSet.getInt(1);
            lm.word= rSet.getString(2);
            lm.link  = rSet.getString(3);
            lm.subject = rSet.getString(4);
            
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
        	if(rSet!=null) try{rSet.close();} catch(Exception e) {};
        	if(stmt!=null) try{stmt.close();} catch(Exception e) {};
        }
        return lm;
    }

    public Vector<DBObject> getObjectVector(Connection db, String q){
        Vector<DBObject> v = new Vector<DBObject>();
        ResultSet rSet=null;
        Statement stmt=null;
        try{
        	stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        	rSet = stmt.executeQuery(q);
            while(rSet.next()) {
                LinkMind lm = new LinkMind();
                lm.id = rSet.getInt(1);
                lm.word = rSet.getString(2);
                lm.link = rSet.getString(3);
                lm.subject = rSet.getString(4);
                v.addElement(lm);
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
             LinkMind lm = this;
             PreparedStatement pst = null;
             String sql = "insert into "+tableName+" values (";
             for(int i=0; i<=fields.length; i++){
                sql+= (i!=fields.length) ? "?" : ")";
                sql+= (i+1<fields.length) ? "," : "";
             }
        try{
            pst = db.prepareStatement(sql);
            pst.setInt(1,lm.id);
            pst.setString(2,lm.word);
            pst.setString(3,lm.link);
            pst.setString(4,lm.subject);
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }

    public PreparedStatement updatePrepStat(Connection db) {
             LinkMind lm = this;
             PreparedStatement pst = null;
             String sql = "update "+tableName+" set ";
             for(int i=1; i<=fields.length; i++){
                sql+= (i!=fields.length) ? fields[i]+"=?" : " where id="+lm.id;
                sql+= (i+1<=fields.length) ? "," : "";
             }
        try{
            pst = db.prepareStatement(sql);
            pst.setString(1,lm.word);
            pst.setString(2,lm.link);
            pst.setString(3,lm.subject);
        } catch (Exception ex){
            ex.printStackTrace();
        }
             return pst;
    }    


    
}
