package ru.jimbot;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import ru.jimbot.core.api.IServiceBuilder;





/**
 *
 */
public class ServiceBuilder {


    public void build() {
    try {
            JarFile jarFile = new JarFile("./JimBot_by-jo.jar");
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                // ���� �� ���������� �������� ���������� - ��������� ������� �� ����� ��������
                if (match(normalize(jarEntry.getName()), "ru.jimbot.modules.srvbilders")) {
           //        System.out.println("!!! "+normalize(jarEntry.getName()));
                    String clazz = normalize2(jarEntry.getName());
                Manager.getInstance().putServiceBuilder((IServiceBuilder) this.getClass().getClassLoader().loadClass(clazz).newInstance());    
                }
            }
      //Manager.getInstance().putServiceBuilder((IServiceBuilder) this.getClass().getClassLoader().loadClass("ru.jimbot.modules.chat.ChatServiceBuilder").newInstance());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * ��������� ������ - �������� ����������� �� ����� ��������� ������ � ����� ��
     * �� ���������� .class
     *  
     * @param className
     * @param packageName
     * @return
     */
    private boolean match(String className, String packageName) {
        return className.startsWith(packageName) && className.endsWith(".class");
    }   
    /**
     * ����������� ��� � �������� ������� � ��� ������
     * (�������� ����� �� �����)
     *
     * @param className
     * @return
     */
    private String normalize(String className) {
        return className.replace('/', '.');
    }
    
    private String normalize2(String className) {
        return className.replace('/', '.').replace(".class", "");
    }
}
