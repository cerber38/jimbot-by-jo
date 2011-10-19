/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.jimbot.modules.info.http;

import java.util.HashMap;
import java.util.Map;
import ru.jimbot.core.api.IHTTPServiceConfig;


/**
 *
 * @author ~Jo-MA-Jo~
 */

public class InfoHttpBuilder implements IHTTPServiceConfig {


     public Map getHTTPServiceConfig(){
         Map map = new HashMap();
         map.put("info_srvs_props", ServicePropertiesAction.class);//Настройки
        return map;
     }


}