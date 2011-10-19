/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.jimbot.modules.chat.http;

import java.util.HashMap;
import java.util.Map;
import ru.jimbot.core.api.IHTTPServiceConfig;


/**
 *
 * @author ~Jo-MA-Jo~
 */

public class ChatHttpBuilder implements IHTTPServiceConfig {


     public Map getHTTPServiceConfig(){
         Map map = new HashMap();
         map.put("chat_user_group_props", UserGroupPropertiesAction.class);//Полномочия
         map.put("chat_user_auth_props", UserAuthPropertiesAction.class);//Полномочия_ex
         map.put("chat_srvs_props", ServicePropertiesAction.class);//Настройки,Прочие настройки,Системные сообщения


         return map;
     }


}