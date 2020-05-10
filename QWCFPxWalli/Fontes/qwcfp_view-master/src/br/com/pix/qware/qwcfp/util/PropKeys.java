package br.com.pix.qware.qwcfp.util;

public enum PropKeys {
	
	WS_IP_KEY("wsIp",true),
	WS_PORT_KEY("wsPort",true),
	WS_VERSION_KEY("wsVersion",true),
	WS_HTTPS_KEY("wsHttps",true),
	WS_SERV_NAME_KEY("wsServiceName",true),
	CACHE_NAME("cacheName",false),
	QWVDT_WS_IP("qwvdtWsIp",false),
	QWVDT_PORT("qwvdtPort",false),
	QWVDT_PROTOCOL("qwvdtProtocol",false),
	QWVDT_USER_ADMIN("qwvdtUserAdmin",false),
	QWVDT_USER_PASS("qwvdtUserPass",false);
	
	
	private final String	msg;
	private final boolean	required;
	
	
	private PropKeys( String msg, Boolean required) {
		this.msg = msg;
		this.required = required;
	}

	public String getKey() {
		return msg;
	}
	
	public boolean isRequired() {
		return required;
	}

	@Override
	public String toString() {
		return  msg;
	}
	
}
