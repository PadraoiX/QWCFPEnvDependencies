package br.com.pix.qware.qwcfp.beans;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.PropKeys;
import br.com.pix.qware.qwcfp.util.QwcfpInitials;

@ManagedBean(name = "installBean")
@ViewScoped
public class InstallBean implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Inject
	private FacesMessages		messages;

	private String				wsIp;
	private String				wsPort;
	private String				wsVersion;
	private String				wsHttps;
	private String				wsServiceName;
	private String				cacheName;

	@PostConstruct
	private void init() {

	}

	public String submit() {
		//TODO ip do jetty tem que vir das configs.
		Properties props = new Properties();
		QwcfpInitials initialsInstace = QwcfpInitials.getInstance();

		props.setProperty(PropKeys.WS_IP_KEY.getKey(), wsIp);
		props.setProperty(PropKeys.WS_PORT_KEY.getKey(), wsPort);
		props.setProperty(PropKeys.WS_VERSION_KEY.getKey(), wsVersion);
		props.setProperty(PropKeys.WS_HTTPS_KEY.getKey(), wsHttps);
		props.setProperty(PropKeys.WS_SERV_NAME_KEY.getKey(), wsServiceName);
		props.setProperty(PropKeys.CACHE_NAME.getKey(), cacheName);

		initialsInstace.setReset(false);
		boolean ret = initialsInstace.mountProperties(props);

		if (ret) {
			messages.error("Propriedades criadas com sucesso.");
		} else {
			messages.error("As propriedades não puderam ser criadas.");
		}

		return "index.faces?faces-redirect=true";
	}

	public String getWsIp() {
		return wsIp;
	}

	public void setWsIp(String wsIp) {
		this.wsIp = wsIp;
	}

	public String getWsPort() {
		return wsPort;
	}

	public void setWsPort(String wsPort) {
		this.wsPort = wsPort;
	}

	public String getWsVersion() {
		return wsVersion;
	}

	public void setWsVersion(String wsVersion) {
		this.wsVersion = wsVersion;
	}

	public String getWsHttps() {
		return wsHttps;
	}

	public void setWsHttps(String wsHttps) {
		this.wsHttps = wsHttps;
	}

	public String getWsServiceName() {
		return wsServiceName;
	}

	public void setWsServiceName(String wsServiceName) {
		this.wsServiceName = wsServiceName;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

}