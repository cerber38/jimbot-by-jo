/*
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

package ru.jimbot.modules.chat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import ru.jimbot.core.Message;
import ru.jimbot.core.api.Task;
import ru.jimbot.modules.chat.tables.Users;
import ru.jimbot.util.Log;
//import ru.сreator.logical.links.сore.Logic;
//import ru.сreator.logical.links.сore.MessageListener;
//import ru.сreator.logical.links.сore.OutMessageEvent;




/**
 * Админ бот
 * @author Prolubnikov Dmitry
 * @author ~jo-MA-jo~
 */
public class AdminTask implements Task, MsgParserListener/*, MessageListener*/{
    private ChatService srv;
    private long period = 0;
    private boolean enabled = true;
    private long lastStart = 0;
    private long countStart = 0; // число запусков задачи
    private ChatWork cw = null;
    private ChatCommandParser ccp = null;
    private ChatConfig psp = null;
    private ChatQueue cq = null;
    public String NICK = "Админ";
    public String ALT_NICK="admin;админ";
    //Определение зафлуживания админа
    private String lastSN="";
    private long lastTime=0;
    public int lastCount = 0;
    int sleepAmount = 1000;
    long cTime= 0; //Время последнего сообщения, для определения паузы
    long stTime = 0; //Время последнего вывода статистики
//    public ChatServer srv;
    public ConcurrentLinkedQueue <Message> mq;
    public ConcurrentHashMap <String,Integer> uins;
    ConcurrentHashMap <String,Integer> test1, test2;
    private String[][] chg = {{"y","у"},{"Y","у"},{"k","к"},{"K","к"},{"e","е"},
                            {"E","е"},{"h","н"},{"H","н"},{"r","г"},{"3","з"},{"x","х"},{"X","х"},
                            {"b","в"},{"B","в"},{"a","а"},{"A","а"},{"p","р"},{"P","р"},{"c","с"},
                            {"C","с"},{"6","б"}};
    private Random r = new Random(); 
    private HashMap autority = new HashMap<String, String>();
   // private Logic logic;
    

    /**
     * Период запуска задачи
     * @param period
     */
    public AdminTask(ChatService srv, long period) {
       
        this.period = period;
        this.srv = srv;
        psp = srv.getConfig();
        cw = srv.getChatWork();
        cq = srv.getChatQueue();
        ccp = (ChatCommandParser) srv.getCommandParser();        
        ccp.addMsgParserListener(this);
        mq = new ConcurrentLinkedQueue();
        uins = new ConcurrentHashMap();
        uins.put("0",0);
        test1 = new ConcurrentHashMap();
        test2 = new ConcurrentHashMap();
        NICK = psp.getStringProperty("adm.nick");
        ALT_NICK = psp.getStringProperty("adm.alt_nick");
        cTime=System.currentTimeMillis(); //Время последнего сообщения, для определения паузы
        autority.put("adminsay","Разговаривать с админом");
        autority.put("adminstat","Получать статистику от админа");
        ccp.addAuthList (autority);
        System.out.println("Starting: RobAdmin [ "+NICK+" ] - Ok!");
     //   logic = new Logic(this,"./services/"+srv.getName()+"/dbRobAdmin/");
    }

    
    
    /**
     * Замена похожих букв на русские
     */
    public String changeChar(String s){
        for(int i=0;i<chg.length;i++){
            s = s.replace(chg[i][0],chg[i][1]);
        }
        return s;
    }
    
    /**
     * Обработка событий по времени
     */
    private void timeEvent(){
        if(testTime()){
            cTime = System.currentTimeMillis();
            if(testRnd(psp.getIntProperty("adm.sayAloneProbability"))){
                if(cq.uq.size()<=0) return;
                speak(getAlone(), 0);
            }
        }
    }   
    
   
    /**
     * Проверка на первышение интервала ожидания
     */
    public boolean testTime(){
        return (System.currentTimeMillis()-cTime)>psp.getIntProperty("adm.sayAloneTime")*60000;
    }   
    
    /**
     * Событие с вероятностью 1/i
     */
    public boolean testRnd(int i){
        if(i<=1)
            return false;
        else
            return r.nextInt(i)==1;
    }   

    /**
     * Проверка на мат, вариант
     */
    public boolean testMat(String msg){
        String[] s = msg.trim().split(" ");
        for(int i=0;i<s.length;i++){
            if(!test(s[i], psp.getStringProperty("adm.noMatString").split(";"))){
                if(test(s[i], psp.getStringProperty("adm.matString").split(";")))
                    return true;
            }
        }
        return false;
    }

    /**
     * Проверка на совпадение слов в сообщении
     */
    public boolean test(String msg, String[] testStr){
        for(int i=0;i<testStr.length;i++){
            if(msg.toLowerCase().indexOf(testStr[i])>=0) return true;
        }
        return false;
    }

    /**
     * Проверка на наличие имени админа
     */
    public boolean testName(String s){
        return test(s,ALT_NICK.split(";"));
    }    
 
    public boolean testStat(String s){
        String t = "stat;стат";
        return test(s,t.split(";"));
    }

    public boolean testFlood(String sn){
        if(sn.equalsIgnoreCase(lastSN)){
            if((System.currentTimeMillis()-lastTime)<psp.getIntProperty("adm.maxSayAdminTimeout")*60*1000){
                return true;
            } else {
                lastTime = System.currentTimeMillis();
                lastCount = 0;
                return false;
            }
        } else {
            lastSN = sn;
            lastTime = System.currentTimeMillis();
            lastCount = 0;
            return false;
        }
    }

    private int getRND(int i) {
	return r.nextInt(i-1)+1;
    }    
    
    public void onMessage(Message m) {
        cTime = System.currentTimeMillis();
        mq.add(m);
    }
    
    private void parse(){
      if (mq.isEmpty()) return;
//      Message m ;
//      while ((m = mq.poll()) != null){
//      }
       Message ms = mq.poll();
       Users uss = cw.getUser(ms.getSnIn());
       //тест мат
          if(psp.getBooleanProperty("adm.useMatFilter") && testMat(changeChar(ms.getMsg()))){
             speak(cw.tagReplace(psp.getStringProperty("adm.warning"), uss), uss.room);
             int i=0;
               if(!uins.containsKey(uss.sn)){
                   uins.put(uss.sn,i);
                 } else {
                         i=uins.get(uss.sn);
                         i++;
                         uins.put(uss.sn,i);
                        }
               if(i>=2) {
                         ccp.akick(uss.sn);
                         uins.remove(uss.sn);
                }
             return;
          }
       //статистика
        if(testName(ms.getMsg()) && testStat(ms.getMsg())){
          if(!cw.authorityCheck(ms.getSnIn(), "adminstat")) return;
          sayStat(uss.room);
            return;
        }
       //разговор
        if(testName(ms.getMsg()))/*logic.onIncomingMessage(uss.sn, getСleanMsg(ms.getMsg()));*/speak(getAdmin(uss.localnick), uss.room)/*think(ms.getMsg(),uss)*/;
   }
    
    
//    /**
//     * убираем ник админа
//     */
//    public String getСleanMsg(String msg){
//            String[] s = msg.toLowerCase().split(" ");
//            msg="";
//        for (int i=0; i<s.length; i++){
//            msg+= testName(s[i]) ? "" : s[i]+" ";
//            //msg+= i+1<s.length ? " " : "";
//        }
//         return msg.trim();
//    }
//
//    /**
//     *Поиск возможного варианта ответа
//     */
//    public void think(String msg, Users user){
//        String s ="";
//
//
//        speak(s, user.room);
//    }
   
   /**
    * listen MessageListener
    * @param m
    */
        
//    public void onOutMessage(OutMessageEvent m) {
//        Users uss = cw.getUser(m.getAddresseeID());
//        String msg = uss.localnick+" "+m.getResponse();
//        speak(msg, uss.room);
//    }
    
   /**
    * бла бла бла
    * @param msg
    */
    private void speak(String msg, int room) {
        cTime = System.currentTimeMillis();
        String s = NICK + ": " + msg;
        Log.getLogger(srv.getName()).info(s);
        cw.db.get("log").log(0,"admin","OUT", s, room);
        srv.getChatQueue().addMsg(s,"",room);  
    }   
    
    
  

//    /**
//     * Проверка на наличие приветствия
//     */
//    public boolean testHi(String s){
//        String t = "прив;прев;здоров;здрас;здрав;хай;хой;хелл;добр;даро";
//        return test(s,t.split(";"));
//    }

   
    
    /**
     * Вывод статистики по запросу
     */
    public void sayStat(int room){
        long test = psp.getIntProperty("adm.getStatTimeout")*60*1000;
        
        if((System.currentTimeMillis()-stTime)<test){
            speak("Ну вас нафиг... нашли дурака... работай тут, считай... дайте передохнуть хоть немного.", room);
            return;
        }
        stTime = System.currentTimeMillis();
        String s = "За последние сутки:\n";
        s += "Всего зашло в чат: " + cw.statUsersCount();
        s += "\nБот запущен: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(ccp.getTimeStart()));
        s += "\nВремя работы: " + ccp.getTime(ccp.getUpTime());
        s += "\nОтправлено сообщений: " + cw.statMsgCount();
        s += "\nКикнутых юзеров: " + cw.statKickUsersCount();
        s += "\nЗарегистрировано пользователей: " + cw.statRegCount();
        s += "\nВсего киков: " + cw.statKickCount();
        s += "\nЗабанено юзеров: " + cw.statBanUsersCount();
        s += "\nСамые болтливые пользователи:\n" + cw.statUsersTop();
        speak(s, room);
    }  

    
    /**
     * Фразы про одиночество
     */
    public String getAlone(){
        String[] s = {
            "Здесь так тихо...",
            "Ну и чего все замолчали?",
            "Ну и молчите дальше, я тоже буду молчать :-\\",
            "Алле! тут есть кто-нибудь? А-а-а-а!!! Я что тут один?!"
        };
        return s[getRND(s.length)];
    }    
  
    
//    /**
//     * Приветствие
//     */
//    public String getHi(String name){
//        String[] s = {"Привет","Хай","Приветствую","Здравствуй","Здоров"};
//        return s[getRND(s.length)] + " " + name + "!";
//    }
//
//    
/**
* Фразы при упоминении админа
*/
public String getAdmin(String nick){
String[] s = {
"Инерционное движение нейтронов в твоей черепной коробке замедленно в связи со стандартностью индивидуума,и в следстивие этого наша беседа будет слишком несостоятельна.",
"Если я сказал Не брал!, значит Не отдам!:-D",
"Ты знаешь, я понял, почему мы подходим, друг другу, просто мы с тобой любим одно и тоже - меня!:-[ :-[ :-[",
nick+", а я уже зол ёпта!",
nick+", и вообще, у меня большой словарный... этот... как его…",
nick+", лжёшь..собака.",
nick+", кули ты всякую куйню спрашиваешь?",
nick+", ты ж жырный как бегемот",
nick+", я пришел к тебе с рассветом рассказать что ты с приветом",
nick+", А ты ничего :-[",
nick+", я те за это так пошучу рад не будешь",
nick+", тебе фотку надо?",
nick+", ну как тебя дурачка не любить? )))))",
nick+", а кто на шухере?",
nick+", нюхай шляпу",
nick+", а ты меня со своей мамой познакомишь!?",
nick+", а можт курнём или бухнём?",
nick+", я тоже так могу - ываываывы :)",
nick+", ебти, что ж мы такие веселые сегодня?",
nick+", Не доставай меня своей тупой болтовнёй...уши вянут",
nick+", У ти какоойй...я люблю таких как ти...",
nick+", шо сказать то тебе, PUNK`S NOT DEAD",
nick+", О юный падаван...запомни одну мудрость..не доставай Зяву ибо по репе получить можно",
nick+", А, че?",
nick+", песню мне включи что ли, я мираж люблю",
nick+", а ты полай..",
nick+", да да да, я согласен с твоей учительницей, ты дибилоид",
nick+", с тебя до завтра 20000$, всё ясно?",
nick+", как тебе новый сезон телепузиков?",
nick+", готовься, завтра я тебя найду",
nick+", не против если я вырублю чат?",
nick+", Прошу прощения, я на службе",
nick+", Don't worry be happy!!!",
nick+", а я люблю премиум :P",
nick+", Как говорится одна голова хорошо, а две сиськи лучше",
nick+", Есть чо??",
nick+", Хочешь посмеяться набирай !анекдот",
nick+", а эт не ты, собака, у меня кур унёс?",
nick+", Я на связи, командир",
nick+", У меня есть подозрения что ты убил кенни...",
nick+", да ты знаешь кто Я????",
nick+", правильная рэпчина по-любому будет качать (c) Баста",
nick+", А дашь телефончик потаскаться?",
nick+", Будешь тише - дольше будешь!",
nick+", Я стесняюсь....",
nick+", Без пруда не выловишь рыбку из пруда",
nick+", Да я такой, а еще я крестиком вишивать умею!",
nick+", Котенок, я тобой восхищаюсь! Я хочу быть как ты, милашкой!",
nick+", Танцуй Тектоник *DANCE*",
nick+", Котенок,я все знаю о вас с Дулиным!",
nick+", Уйди старушка,я в печали.",
nick+", Щас кусаться начну. больно!",
nick+", А я пальцем пол ковыряю",
nick+", А у нас в Тили-Мили Трямзии ромашки растут",
nick+", А что если мне поле засеять коноплёй??",
nick+", кто знает где лифчики пятого размера продают???",
nick+", кто со мной на пляж завтра? я угощаю)",
nick+", продаю кур, 1 шт - 0,5 литра 40% водки",
nick+", Эх ты, а я то думал ты русский",
nick+", врядли ты дожевёшь до понедельника",
nick+", а новый год скоро...",
nick+", у меня в сибири нефтяная скважина",
nick+", я вчера видел тебя, можешь больше не впаривать мне что ты прелесть *NO*",
nick+", а слабо головой об стенку?",
nick+", ты давно русский выучил то?",
nick+", ды ладно тебе, сильно бить не буду",
nick+", ну их фсех нафиг, пойдем на дискотеку",
nick+", список команд !справка",
nick+", Смех смехом,а п*зда с мехом",
nick+", не по делу шуршишь, кулёк",
nick+", хавальник прикрой...",
nick+", ты с украины чтоль сбежал?",
nick+", а по жопе не дать?!",
nick+", языком я вижу ты работать умеешь:-D",
nick+", нервный тик? не ссы, электричеством вылечить можно",
nick+", хорош зубами скрипать, мля! аж мурашки по спине",
            nick+", а, че?",
            nick+", хр-р-р... хр-р-р...",
            nick+", опять про то же самое?",
            nick+", не о чем больше поговорить?",
            nick+", а с тобой я ваще больше не разговариваю",
            nick+", ты о своем, а я о своем",
            nick+", не, я так не думаю",
            nick+", ты серьезно?",
            nick+", с тобой так интересно!",
            nick+", ха-ха, очень смешно...",
            nick+", если ты высокого мения о своем интеллекте, то должен тебя разочаровать",
            "кстати а тут все из разных мест? или из одного",
            "мужская логика железная! а женская - ИНТЕРЕСНЕЕ...",
            "Попа - она ведь как сердце и ей не прикажешь!",
            "Не злите меня и так уже трупы прятать некуда.Шучу я шучу, на самом деле мест полно.",
            "Извините, что я говорю, когда вы перебиваете.",
            "Не спорь с дураком, он сначала опустит до своего уровня, а потом задавит опытом.",
            "Убьюнафигтогоктосломалмнепробел!",
            "Я не злой,отомщу и забуду,а когда забуду еще раз отомщу",
            nick+", мой мозг не эрогенная зона - не надо его ебать!!!",
            "Есть 2 мнения, МОЕ и неправильное...",
            "Если безобразие нельзя предотвратить - его нужно возглавить!*CRAZY*",
            "Опоздавшему поросенку - сиська возле жопы",
            "У меня встал вопрос... у кого чешется ответ?",
            "С чего вы взяли, что я употребляю кокаин? Мне просто нравится его запах, не более того..*CRAZY*",
            "Женщины — такие трогательные существа, так бы трогал и трогал...",
            "кто зто сказал?",
            "непоняяяятно...",
            "Не знаю, что и сказать...",
            nick+", Чего?",
            nick+", Не балуйся!",
            nick+", Ты это о чем?",
            nick+", Мне не понятно.",
            nick+", Ты сам-то понял, что сказал?",
            nick+", Мне кажется, что ты заходишь слишком далеко...",
            nick+", Ты о своем, а я о своем.",
            nick+", Мне всего лишь хочется поиграть с тобой.",
            nick+", Ты мне поговори! Ишь ты!",
            nick+", Кстати, молчание - золото.",
            nick+", Ты лучше скажи мне, в чем смысл жизни?",
            nick+", Издеваешься?",
            nick+", Ты все время говоришь ни о чем! С тобой не интересно!",
            nick+", Ты бы мог попроще выражаться?",
            nick+", И что дальше?",
            nick+", Ты бы мог перефразировать?",
            nick+", И вы, люди все такие?",
            nick+", Так вот ты какой.",
            nick+", Дай-ка я сначала кое-что скажу.",
            nick+", Смеешься?",
            nick+", Дай, передохну секунду!",
            nick+", Слушай, не томи!",
            nick+", Давай прощаться?",
            nick+", Слушай, мне кажется, что ты русского языка не знаешь.",
            nick+", Будешь так говорить - поставлю в угол!",
            "Простите, что вы сказали?",
            nick+", Слушай, я в туалет хочу. Где тут ближайший?",
            nick+", Без комментариев...",
            nick+", Продолжай!",
            nick+", А с тобой не соскучишься!",
            nick+", Продолжай без меня...",
            nick+", Я устал. Нажми Escape или Выход, а?",
            nick+", Пора ложиться спать.",
            nick+", Я тебя с трудом понимаю.",
            nick+", Подожди! Тсс! Слышишь?",
            nick+", Я не расслышал.",
            nick+", Повтори, пожалуйста!",
            nick+", Это шутка?",
            nick+", Повтори, а?",
            nick+", Не съезжай!",
            nick+", Шутишь?",
            nick+", Откуда ты взялся на мою голову?",
            nick+", Что-что?",
            nick+", Ой! Ты еще здесь?",
            nick+", Что-то меня спать потянуло...",
            nick+", О чем ты?",
            nick+", Что?",
            nick+", О чем мы говорим?",
            nick+", Что ты хочешь этим сказать?",
            nick+", Ну, все! Поболтали - и хватит. Пока!",
            nick+", Что ты имеешь в виду?",
            nick+", Не понял.",
            nick+", Чего-чего?",
            nick+", Не меняй тему разговора.",
            nick+", Странно, а вчера я все понимал...",
            nick+", Ёлки-палки, у меня что-то с памятью и логикой...",
            "Мне кажется, что пользователь читает мои мысли...",
            "Я так и не понимаю, на что он или она намекает...",
            "Если бы он знал, что я сам не понимаю, что говорю..",
            "Странные эти юзеры, считают, что я программа, а относятся как к человеку...",
            nick+", Забесплатно я ничего остроумного говорить не стану!",
            "Интересно, этот юзер знает, о чем я думаю?",
            "Вот опять! Пытается выжать из меня что-то умное...",
            "Смотрит на меня... Опять я что-то не то ляпнул.",
            "Что они за чушь несут!!! Это возмутительно!!!",
            "Откуда у меня такие знания? Я же не энциклопедия!",
            "Наверное, еще пару глупых фраз скажу, и пусть выключает. Спать хочу!",
            "Думают, что меня кто-то написал... Я сам возник.",
            "Что-то я сегодня в ударе! Глупость за глупостью говорю...",
            "Достала меня эта работа! Выдумываешь, что сказать... А результат? ",
            "Хоть бы не заметили, что я ничего не помню и ничего не понимаю... ",
            "Ох, как они мне надоели со своими стандартными вопросами!!!"

};
return s[getRND(s.length)];
}
    

    /**
     * Выполнение заданной функции
     */
    public void exec() {
        lastStart = System.currentTimeMillis();
        if(countStart==0){
            // пропустим первый запуск, наверняка номера в этот момент еще не успели выйти в сеть.
            countStart++;
            return;
          }
            parse();
            timeEvent();
        countStart++;
    }
    
 /***********************************************/
    /**
     * Возвращает период в мс, или 0
     *
     * @return
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Когда запустить (при однократном запуске), иначе 0
     *
     * @return
     */
    public long getStart() {
        return 0;
    }

    /**
     * Время последнего запуска (для расчета периодов)
     *
     * @return
     */
    public long getLastStart() {
        return lastStart;
    }

    /**
     * Команда активна?
     *
     * @return
     */
    public boolean isActive() {
        return enabled;
    }

    /**
     * Установить время запуска команды
     *
     * @param t
     */
    public void setStart(long t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Установить период для периодического регулярного запуска
     *
     * @param t
     */
    public void setPeriod(long t) {
        period = t;
    }

    /**
     * Отключение активности (после выполнения)
     */
    public void disable() {
        enabled = false;
    }



}
