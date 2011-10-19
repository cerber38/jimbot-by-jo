/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;



/**
 *
 * @author ~jo-MA-jo~
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
       // arh.packToJar("./plugins/","./JimBot_by-jo.jar");
        createManifest();
        packToJar("./plugins/",args[0]);

    }
    
    public static void packToJar (String JarFilesPath,  String JarOutputPath) {
       try {
            File Manifest = new File("./plugins/Manifest.mf");
            Vector jarEntries = new Vector();
            FileOutputStream dest = new FileOutputStream(JarOutputPath);
            JarOutputStream out = new JarOutputStream(new BufferedOutputStream(dest));
            Vector files = listFiles(JarFilesPath);
           for (int i = 0; i < files.size(); i++) {
               JarFile jf = (JarFile)files.get(i);
               Enumeration en = jf.entries();

                  while(en.hasMoreElements()) {
                       JarEntry je = (JarEntry)en.nextElement();
                    if (!jarEntries.contains(je.getName())) {
                       jarEntries.add(je.getName());
                       InputStream in = jf.getInputStream(je);
                      if (je.getName().toLowerCase().indexOf("manifest.mf")!=-1) {
                         System.out.println("Adding: Manifest.mf - OK!");
                         in = new BufferedInputStream(new FileInputStream(Manifest));
//                       je = new JarEntry("./Meta-inf/Manifest.mf");
                         je.setCompressedSize(new JarEntry("./plugins/Manifest.mf").getCompressedSize());
                         je.setSize(Manifest.length());
                         je.setTime(Manifest.lastModified());
                       }                       
                       out.putNextEntry(je);
                       byte[] buf = new byte[1024];
                       int len;
                         while ((len = in.read(buf)) > 0) {
                               out.write(buf, 0, len);
                        }
                     in.close();
                    }
                 }
              String file = jf.getName().substring(jf.getName().lastIndexOf(File.separator)+1,jf.getName().length());
              System.out.println("Adding: "+file+" - OK!");
              jf.close();
           }
           out.closeEntry();
           out.close();
           if (Manifest.exists())Manifest.delete();
           System.out.println("BUILD SUCCESSFUL!");
         } catch(Exception ex) {
           System.out.println(ex.getMessage());
         }
   }

      /**
     * Возвращает вектор jar-файлов
     * @param dir путь к файлам
     * @return
     */
    public static Vector<JarFile> listFiles(String dir){
        Vector <JarFile> v = new Vector <JarFile>();
        try{
            File f = new File(dir);
            File[] fs = f.listFiles();
            if(fs.length<0) return v;
            for(int i=0;i<fs.length;i++){
                if(fs[i].isFile())
                    if(fs[i].getName().endsWith(".jar"))
                       v.add(new JarFile(fs[i].getPath()));
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return v;
    } 
    
    public static void createManifest(){
        try {

             File[] files = new File("./lib").listFiles();
         String m = "Manifest-Version: 1.0";
               m += "\nAnt-Version: Apache Ant 1.8.1";
               m += "\nCreated-By: 1.6.0_16-b01 (Sun Microsystems Inc.)";
               m += "\nMain-Class: ru.jimbot.StartBot";
               m += "\nClass-Path:";               
               for (int i = 0; i < files.length; i++) {
               String name =files[i].getPath();
               if(name.endsWith(".jar"))
               m += " "+name.substring(2, name.length());
               }
               m += "\nX-COMMENT: Main-Class will be added automatically by build";
         OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream("./plugins/Manifest.mf", false), "windows-1251");
         ow.write(m);
         ow.close();
         System.out.println("Create: Manifest - OK!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }        

}
