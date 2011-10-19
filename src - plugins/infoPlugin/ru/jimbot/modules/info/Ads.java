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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Vector;
import ru.jimbot.db.DBObject;
/**
 *
 */
public class Ads extends DBObject {
    public int id=0;
    public String txt = "";
    public int enable=1;
    public String note = "";    
    public String client = "";
    public long expDate = System.currentTimeMillis();
    public int maxCount=1;    

    
    /** Creates a new instance of Aneks */
    public Ads() {
        init();
    }
    
    public Ads(int id, String txt, int enable, String note, String client, long expDate, int maxCount) {
       init();
    this.id = id;
    this.txt = txt;
    this.enable = enable;
    this.note = note;  
    this.client = client;
    this.expDate = expDate;
    this.maxCount = maxCount;   
    }
    
    private void init(){
        fields = new String[] {"id","txt","enable","note","client","expDate","maxCount"};
        types = new int[] {Types.INTEGER, Types.LONGVARCHAR, Types.INTEGER, Types.LONGVARCHAR, Types.LONGVARCHAR,Types.TIMESTAMP,Types.INTEGER};
        tableName="ads";
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

    @Override
    public DBObject getObject(Connection arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector<DBObject> getObjectVector(Connection arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement insertPrepStat(Connection arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement updatePrepStat(Connection arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
