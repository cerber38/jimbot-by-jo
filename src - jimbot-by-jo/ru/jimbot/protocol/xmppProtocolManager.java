
package ru.jimbot.protocol;

import java.util.HashMap;

import ru.jimbot.core.api.IProtocolBuilder;
import ru.jimbot.core.api.IProtocolManager;
import ru.jimbot.core.api.Protocol;
import ru.jimbot.util.Log;

/**
 * Создает и хранит объекты, реализующие подключение по протоколу XMPP(Jabber)
 *
 * @author Prolubnikov Dmitry
 * @modification ~Jo-MA-Jo~
 */
public class xmppProtocolManager implements IProtocolManager {
	private HashMap<String, xmppProtocol> protocols = new HashMap<String, xmppProtocol>();

	public xmppProtocolManager() {

	}

	public String getProptocolName(){
		return "XMPP(Jabber)";
	}

	public xmppProtocol getProtocol(String sn) {
		return protocols.get(sn);
	}

	public Protocol addProtocol(Protocol p) {
		protocols.put(p.getScreenName(), (xmppProtocol)p);
		return p;
	}



	/* (non-Javadoc)
	 * @see ru.jimbot.core.IProtocolManager#getBuilder(java.lang.String)
	 */
	public IProtocolBuilder getBuilder(String sn) {
		return new Builder(sn);
	}



	class Builder implements IProtocolBuilder {
		private String screenName = "";
		private String pass = "";
		private String server = "jabber.ru";
		private int port = 5222;
		private int status = 0;
		private String statustxt = "";
		private int xstatus = 0;
		private String xstatustxt1 = "";
		private String xstatustxt2 = "";
		private Log logger = Log.getDefault();

		public Builder(String screenName) {
			this.screenName = screenName;
		}

		public Builder pass(String val) {
			pass = val;
			return this;
		}

		public Builder server(String val) {
			server = val;
			return this;
		}

		public Builder port(int val) {
			port = val;
			return this;
		}

		public Builder status(int val) {
			status = val;
			return this;
		}

		public Builder statustxt(String val) {
			statustxt = val;
			return this;
		}

		public Builder xstatus(int val) {
			xstatus = val;
			return this;
		}

		public Builder xstatustxt1(String val) {
			xstatustxt1 = val;
			return this;
		}

		public Builder xstatustxt2(String val) {
			xstatustxt2 = val;
			return this;
		}

		public Builder logger(Log val) {
			logger = val;
			return this;
		}

		public xmppProtocol build() {
			xmppProtocol p = new xmppProtocol();
			p.setConnectionData(server, port, screenName, pass);
			p.setStatusData(status, statustxt);
			p.setXStatusData(xstatus, xstatustxt1, xstatustxt2);
			p.setLogger(logger);
			return p;
		}
	}

}