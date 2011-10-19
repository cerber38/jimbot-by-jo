/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.jimbot.db;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import ru.jimbot.Manager;
import ru.jimbot.util.Archiver;
import ru.jimbot.util.FileUtils;
import ru.jimbot.util.Log;

/**
 * Проверка на наличие файлов базы и их создание
 * работа с файлами SQL запросов
 * @author ~joMAjo~
 */
public class CreateDB {
private static Archiver arch= new Archiver();

    public void createDB(String service,String type) {
       // arch.extractFromJar("./JimBot_by-jo.jar", "./JB/");
       if (createDbDir(service,type))
        try {

              Class.forName("org.sqlite.JDBC");
                 Vector v =FileUtils.listFiles("./services/" + service + "/db/sql/","sql");
                 Vector v2 =FileUtils.listFiles("./services/" + service + "/db/","sql");
                 String name="";
                 String SQLerr0="";
                for(int i=0; i<v.size(); i++){
                    name = FileUtils.getName((String) v.get(i));
                    if (!testFile("./services/" + service + "/db/"+name+".db")){
                     Connection con=DriverManager.getConnection("jdbc:sqlite:services/"+service+"/db/"+name+".db");
                     PreparedStatement Pstat = null;
                     String SQLdump = FileUtils.loadText("./services/" + service + "/db/sql/"+name+".sql");
                     String[] SQLrequest = SQLdump.split(";\n");
                       for(int i_sql=0; i_sql<SQLrequest.length; i_sql++){
                           try {
                               if (testSQL(SQLrequest[i_sql])){
                                Pstat = (PreparedStatement)con.prepareStatement(SQLrequest[i_sql].replace("<br>", "\n"));
                                Pstat.execute();
                                Pstat.close();
                                System.out.println("SQL: "+name+".sql - str#"+(i_sql+1)+" ok!");
                               }
                               } catch (SQLException e) {
                                   if (i_sql+1!=SQLrequest.length){
                                     SQLerr0 +=("!!!("+name+".sql) "+SQLrequest[i_sql]+"  - [ "+e.getMessage()+" ]\n");    
                                     Log.getLogger(service).error("!!!SQLОшибка ("+name+".sql)#"+(i_sql+1)+" - [ "+e.getMessage()+" ]");
                                  }
                               }
                       }

                      con.close();
                      con=null;
                      Log.getLogger(service).info("Created new db: " + name+".db");
                    }

                }
                if (!SQLerr0.equals(""))saveToFile("./services/" + service + "/db/sql/SQLerr.txt",SQLerr0);
        
                boolean b=true;
                String SQLerr="";
                for(int i=0; i<v2.size(); i++){
                    name = FileUtils.getName((String) v2.get(i));
                  //  if (!testFile("./services/" + service + "/db/"+name+".db")){
                     Connection con=DriverManager.getConnection("jdbc:sqlite:services/"+service+"/db/"+name+".db");
                     PreparedStatement Pstat = null;
                     String SQLdump = FileUtils.loadText("./services/" + service + "/db/"+name+".sql");
                     File file= new File("./services/" + service + "/db/"+name+".sql");
                     String[] SQLrequest = SQLdump.split(";\n");
                     b=true;
                       for(int i_sql=0; i_sql<SQLrequest.length; i_sql++){
                           try {
                               if (testSQL(SQLrequest[i_sql])){

                                Pstat = (PreparedStatement)con.prepareStatement(SQLrequest[i_sql].replace("<br>", "\n"));
                                Pstat.execute();
                                Pstat.close();
                                System.out.println("SQL: "+name+".sql - str#"+(i_sql+1)+" ok!");
                               }
                               } catch (SQLException e) {
                                   if (i_sql+1!=SQLrequest.length){
                                     SQLerr +=("!!!("+name+".sql) "+SQLrequest[i_sql]+"  - [ "+e.getMessage()+" ]\n");  
                                     Log.getLogger(service).error("!!!SQLОшибка ("+name+".sql)#"+(i_sql+1)+" - [ "+e.getMessage()+" ]");
                                     b=false;
                                   }
                               }
                           
                       }
                      if (b) file.renameTo(new File("./services/" + service + "/db/"+name+".ok"));
                         else
                             file.renameTo(new File("./services/" + service + "/db/"+name+".err"));
                      con.close();
                      con=null;
                    //  Log.info("Created new db: " + name+".db");
                    }
                    if (!SQLerr.equals(""))saveToFile("./services/" + service + "/db/SQLerr.txt",SQLerr);
  
           v=null;
           v2=null;
           name=null;
           SQLerr=null;
           SQLerr0=null;
       //   Log.info("Установка файлов базы завершена!");
         } catch (Exception ex) {
            ex.printStackTrace();
         }

    }
    /**
     * сохраняет текст в файл
     * @param Path путь куда сохранять
     * @param text текст для сохранения
     * @throws java.lang.Exception
     */
    private void saveToFile(String Path,String text) throws Exception{
            File LogErr = new File (Path);
            if (LogErr.exists())LogErr.delete();
            OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(Path,true),"windows-1251");
            ow.write(text);
            ow.close();
     }

    /**
     * копирует файл из ru.jimbot.templats
     * @param service имя сервиса
     * @param file имя файла
     */
     public void copyOfTemp(String service, String type, String file) {
           try {
            InputStream in = Manager.class.getClassLoader().getResourceAsStream("ru/jimbot/"+type+"/templats/"+file);
            OutputStream out = new FileOutputStream("./services/"+ service+"/"+file);
              // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
         }
           catch (Exception ex){
           System.out.println(ex.getMessage());
         }

     }
     /**
      * проверяет наличие базы и если нужно создает ее из внутреннего архива
      * @param service
      * @return
      */
     public boolean createDbDir(String service, String type) {
         String ZipFilePath= "./services/" + service+"/db.zip";   
         String ExtractPath = "./services/" + service+"/db/";         
         if (testFile(ExtractPath))return true;
         copyOfTemp(service,type,"db.zip");
       boolean b= arch.extractFromZip(ZipFilePath,ExtractPath);
       File f = new File(ZipFilePath);
       if(f.exists())f.delete();
       return b;
     }

     /**
      * проверка наличия файла или дирректории
      * @param path
      * @return
      */
      public boolean testFile(String path) {
           File file= new File(path);
           if (file.length()==0)file.delete();
           return file.exists();
     }



     public boolean testSQL(String SQLcontent) {
           if(SQLcontent==null)return false;
           if("".equals(SQLcontent))return false;
           if(" ".equals(SQLcontent))return false;
           if("\n".equals(SQLcontent))return false;
           if("\r".equals(SQLcontent))return false;
           if("\n\r".equals(SQLcontent))return false;

           return true;

     }

 

}