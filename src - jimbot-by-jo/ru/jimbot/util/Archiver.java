

package ru.jimbot.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

    public class Archiver {

/**
 * извлечь содержимое zip файла
 * @param ZipFilePath
 * @param ExtractPath
 * @return
 */

     public boolean extractFromZip(String ZipFilePath, String ExtractPath) {

        try
        {

         if (testFile(ExtractPath))return true;
      //   copyOfTemp("./services/" + service+"/","db.zip");
         int i;
         File f = new File(ZipFilePath);
          if(!f.exists()) {
           System.out.println("\nNot found Zip file: " + ZipFilePath);
           return false;
          }
         File f1 = new File(ExtractPath);
            if(!f1.exists())
               f1.mkdir();
         ZipFile zf;
         Vector zipEntries = new Vector();
         zf = new ZipFile(ZipFilePath);
          Enumeration en = zf.entries();

        while(en.hasMoreElements())
          {
            zipEntries.addElement(
	    (ZipEntry)en.nextElement());
          }

        for (i = 0; i < zipEntries.size(); i++)
          {
           ZipEntry ze = (ZipEntry)zipEntries.elementAt(i);
           extract( ExtractPath,
	   ze.getName(), zf, ze);
         }
      zf.close();
      System.out.println("Done!");
         if(f.exists())f.delete();
    }
    catch(Exception ex)
    {
      System.out.println(ex.getMessage());
      return false;
    }

      return true;
  }
/**
 * извлечь содержимое jar файла
 * @param JarFilePath
 * @param ExtractPath
 * @return
 */
       public boolean extractFromJar(String JarFilePath, String ExtractPath) {

        try
        {

         if (testFile(ExtractPath))return true;
      //   copyOfTemp("./services/" + service+"/","db.zip");
         int i;
         File f = new File(JarFilePath);
          if(!f.exists()) {
           System.out.println("\nNot found Zip file: " + JarFilePath);
           return false;
          }
         File f1 = new File(ExtractPath);
            if(!f1.exists())
               f1.mkdir();
         JarFile jf;
         Vector jarEntries = new Vector();
         jf = new JarFile(JarFilePath);
          Enumeration en = jf.entries();

        while(en.hasMoreElements())
          {
            jarEntries.addElement(
	    (JarEntry)en.nextElement());
          }

        for (i = 0; i < jarEntries.size(); i++)
          {
           JarEntry je = (JarEntry)jarEntries.elementAt(i);
           extract( ExtractPath,
	   je.getName(), jf, je);
         }
      jf.close();
      System.out.println("Done!");
         if(f.exists())f.delete();
    }
    catch(Exception ex)
    {
      System.out.println(ex.getMessage());
      return false;
    }

      return true;
  }

 // ============================================
  // extractFromZip
  // ============================================
  static void extract( String szExtractPath,
    String szName,
    ZipFile zf, ZipEntry ze)
  {
    if(ze.isDirectory())
      return;

    String szDstName = slash2sep(szName);

    String szEntryDir;

    if(szDstName.lastIndexOf(File.separator) != -1)
    {
      szEntryDir =
        szDstName.substring(
	  0, szDstName.lastIndexOf(File.separator));
    }
    else
      szEntryDir = "";

    System.out.print(szDstName);
    long nSize = ze.getSize();
    long nCompressedSize =
      ze.getCompressedSize();

    System.out.println(" " + nSize + " (" +
      nCompressedSize + ")");

    try
    {
       File newDir = new File(szExtractPath +
	 File.separator + szEntryDir);

       newDir.mkdirs();

       FileOutputStream fos =
	 new FileOutputStream(szExtractPath +
	 File.separator + szDstName);

       InputStream is = zf.getInputStream(ze);
       byte[] buf = new byte[1024];

       int nLength;

       while(true)
       {
         try
         {
	   nLength = is.read(buf);
         }
         catch (EOFException ex)
         {
	   break;
	 }

         if(nLength < 0)
	   break;
         fos.write(buf, 0, nLength);
       }

       is.close();
       fos.close();
    }
    catch(Exception ex)
    {
      System.out.println(ex.toString());
      System.exit(0);
    }
  }

 // ============================================
  // extractFromJar
  // ============================================
  static void extract( String ExtractPath,
    String sjName,
    JarFile jf, JarEntry je)
  {
    if(je.isDirectory())
      return;

    String sjDstName = slash2sep(sjName);

    String sjEntryDir;

    if(sjDstName.lastIndexOf(File.separator) != -1)
    {
      sjEntryDir =
        sjDstName.substring(
	  0, sjDstName.lastIndexOf(File.separator));
    }
    else
      sjEntryDir = "";

    System.out.print(sjDstName);
    long nSize = je.getSize();
    long nCompressedSize =
      je.getCompressedSize();

    System.out.println(" " + nSize + " (" +
      nCompressedSize + ")");

    try
    {
       File newDir = new File(ExtractPath +
	 File.separator + sjEntryDir);

       newDir.mkdirs();

       FileOutputStream fos =
	 new FileOutputStream(ExtractPath +
	 File.separator + sjDstName);

       InputStream is = jf.getInputStream(je);
       byte[] buf = new byte[1024];

       int nLength;

       while(true)
       {
         try
         {
	   nLength = is.read(buf);
         }
         catch (EOFException ex)
         {
	   break;
	 }

         if(nLength < 0)
	   break;
         fos.write(buf, 0, nLength);
       }

       is.close();
       fos.close();
    }
    catch(Exception ex)
    {
      System.out.println(ex.toString());
      System.exit(0);
    }
  }

static String slash2sep(String src)
{
  int i;
  char[] chDst = new char[src.length()];
  String dst;

  for(i = 0; i < src.length(); i++)
  {
    if(src.charAt(i) == '/')
      chDst[i] = File.separatorChar;
    else
      chDst[i] = src.charAt(i);
  }
  dst = new String(chDst);
  return dst;
}

     private boolean testFile(String path) {
           return new File(path).exists();
     }


    }

