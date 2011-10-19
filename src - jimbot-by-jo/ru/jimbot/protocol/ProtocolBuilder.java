
package ru.jimbot.protocol;

import ru.jimbot.Manager;



/**
 * 
 *
 */
public class ProtocolBuilder {


    public ProtocolBuilder(){

    }

    public void build(){
          Manager.getInstance().putProtocolManager(new IcqProtocolManager());
          Manager.getInstance().putProtocolManager(new xmppProtocolManager());
          Manager.getInstance().putProtocolManager(new MrimProtocolManager());
    }

  
}
