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

import ru.jimbot.MainConfig;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Prolubnikov Dmitry
 */
public class HttpUtils {
    private static int ver_no = 0;
    private static long ver_last_read = 0;
    private static String ver_desc = "";

    private HttpUtils() {
        throw new AssertionError();
    }

    /**
     * Читает текстовый файл по URL
     * @param u
     * @return
     */
    public static String getStringFromHTTP(String u){
        String s = "";
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty ( "User-agent", "JimBot/0.4 (Java" +
                    "; U;" + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version") +
                    "; ru; " + System.getProperty("java.vendor") + " " + System.getProperty("java.version") +
                    ")");
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            byte[] b = new byte[1024];
            int count = 0;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while ((count=bis.read(b)) != -1)
                bout.write(b, 0, count);
            bout.close();
            bis.close();
            conn.disconnect();
            s = bout.toString("windows-1251");
        } catch (Exception ex) {
            Log.getDefault().error("Ошибка HTTP при чтении новой версии", ex);
        }
        return s;
    }

    /**
     * Проверка на новую версию
     * @return
     */
    public static synchronized boolean checkNewVersion() {
        if(!MainConfig.getInstance().isCheckNewVer()) return false;
        if(ver_no==0)
            readNewVer();
        if((System.currentTimeMillis()-ver_last_read)>24*3600000)
            readNewVer();
        return ver_no>MainConfig.VER_INT;
    }

    /**
     * Возвращает описание новой версии
     * @return
     */
    public static String getNewVerDesc() {
        return ver_desc;
    }

    /**
     * Читает информацию о новой версии с сайта
     */
    private static void readNewVer() {
        String s = getStringFromHTTP("http://jimbot.ru/ver.txt");
        ver_no = MainConfig.VER_INT;
        ver_desc = "";
        ver_last_read = System.currentTimeMillis();
        if(s.equals("")) return;
        try {
            BufferedReader r = new BufferedReader(new StringReader(s));
            String sd = r.readLine();
            if(!sd.equals("#JimBot version file")) return;
            ver_no = Integer.parseInt(r.readLine());
            String cnt = r.readLine();
            counter(cnt);
            if(ver_no>MainConfig.VER_INT)
                while(r.ready()){
                    String st = r.readLine();
                    if(st==null) break;
                    if(!st.equals(""))
                        if(Integer.parseInt(st.split("#")[0])==ver_no)
                            ver_desc += st.split("#")[1] + '\n';
                }
            r.close();
        } catch (Exception ex){
            Log.getDefault().error("Ошибка обработки описания новой версии",ex);
        }
    }

    private static void counter(String s){
        try {
            String u = s.substring(8);
            u = u.replaceAll("@", "chat_ver=" + MainConfig.VERSION);
            u = u.replaceAll(" ", "%20");
            getStringFromHTTP(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
