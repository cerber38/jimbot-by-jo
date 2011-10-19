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

package ru.jimbot.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

/**
 * Разные пофторяющиеся функции для работы с файлами
 * @author Prolubnikov Dmitry
 */
public class FileUtils {
    private static HashSet<String> ignor;

    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * Загрузка содержимого текстового файла в строковую переменную
     * @param fn - имя файла
     * @param encode - кодировка файла (windows-1251, utf8)
     * @return содержимое файла
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static String loadFile(String fn, String encode) throws IOException, UnsupportedEncodingException {
        String s = "";
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fn),encode));
        while (r.ready()){
            s += r.readLine() + '\n';
        }
        r.close();
        return s;
    }
    
 
     /**
      * Сохранение данных в файл
      * @param fname
      * @param text
      * @param encode
      * @throws java.io.IOException
      * @throws java.io.UnsupportedEncodingException
      */       
    
      public static void saveFile(String fname,String text,String encode) throws IOException, UnsupportedEncodingException {
  //  try {
      OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(fname),encode);
      ow.write(text);
      ow.close();
  //  } catch (Exception ex) {
   //   ex.printStackTrace();
  //  }
  }

    /**
     * Загружает файл свойств
     * @param fn
     * @return
     */
    public static Properties loadProperties(String fn) throws IOException {
        Properties p = new Properties();
        FileInputStream fi = new FileInputStream(fn);
        p.loadFromXML(fi);
        fi.close();
        return p;
    }

    /**
     * Сохраняет файл свойств
     * @param p
     * @param fn
     * @throws IOException
     */
    public static void saveProperties(Properties p, String fn) throws IOException {
        FileOutputStream fo = new FileOutputStream(fn);
        p.storeToXML(fo, "JimBot Properties");
        fo.close();
    }

    public static void beanToXML(Object bean, String fn) throws FileNotFoundException {
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fn)));
        encoder.writeObject(bean);
        encoder.close();
    }

    public static Object XmlToBean(String fn) throws FileNotFoundException {
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(fn)));
        Object o = decoder.readObject();
        decoder.close();
        return o;
    }

    /**
     * Возвращает строковый массив в виде строки с разделителем ";"
     * @param ss
     * @return
     */
    public static String arrayToString(String[] ss) {
        String s = "";
        int c = 0;
        for(String i : ss) {
            s += (c==0 ? "" : ";") + i;
            c++;
        }
        return s;
    }

//    public static String[] addItem(String[] ss, String s) {
//        String[] ss2 = new String[ss.length+1];
//        System.arraycopy(ss,0,ss2,0,ss.length);
//        ss2[ss2.length-1] = s;
//        return ss2;
//    }
//
//    public static String[] removeItem(String[] ss, int item) {
//        ArrayList<String> list = new ArrayList<String>(Arrays.asList(ss));
//        list.remove(item);
//        return (String[])list.toArray();
//    }
//
//    public static UinConfig[] addItem(UinConfig[] ss, UinConfig u) {
//        UinConfig[] ss2 = new UinConfig[ss.length+1];
//        System.arraycopy(ss,0,ss2,0,ss.length);
//        ss2[ss2.length-1] = u;
//        return ss2;
//    }
//
//    public static UinConfig[] removeItem(UinConfig[] ss, int item) {
//        ArrayList<String> list = new ArrayList(Arrays.asList(ss));
//        list.remove(item);
//        return (UinConfig[])list.toArray();
//    }


    /**
     * Загружает игнор-лист из файла
     */
    public static synchronized void loadIgnorList(){
    	String s;
    	ignor = new HashSet<String>();
        try{
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("ignore.txt"),"windows-1251"));
            while (r.ready()){
                s = r.readLine();
                if(!s.equals("")){
                    ignor.add(s);
                }
            }
            r.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    /**
     * Загружает текст из файла
     * @param fname
     * @return
     */
    public static String loadText(String fname) {
       String s = "";
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "windows-1251"));
            while (r.ready())
             s = s + r.readLine() + "\n";
             r.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     return s;
    }

    /**
     * УИН в игноре?
     * @param uin
     * @return
     */
    public static boolean isIgnor(String uin){
    	if(ignor==null) return false;
    	return ignor.contains(uin);
    } 
    
    /**
     * Удаление папки, содержащей файлы
     * @param path
     * @return
     */
    static public boolean deleteDirectory(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
      }
    
    /**
     * Копирование папки с файлами
     * @param sourceLocation
     * @param targetLocation
     * @throws java.io.IOException
     */
     public static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
           }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
              // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    } 
    
    /**
     * Возвращает список файлов
     * @param dir путь к файлам
     * @param ext расширение файлов
     * @return
     */
    public static Vector<String> listFiles(String dir,String ext){
        Vector<String> v = new Vector<String>();
        try{
            File f = new File(dir);
            File[] fs = f.listFiles();
            if(fs.length<0) return v;
            for(int i=0;i<fs.length;i++){
                if(fs[i].isFile())
                    if(getExt(fs[i].getName()).equals(ext))
                       v.add(fs[i].getName());
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return v;
    } 
     
     
    public static String getName(String s){
        if(s.indexOf(".")<0) 
            return s;
        else
            return s.replace('.', ':').split(":")[0];
    }
    
    public static String getExt(String s){
        if(s.indexOf(".")<0)
            return "";
        else
            return s.replace('.', ':').split(":")[1];
    }
    
}
