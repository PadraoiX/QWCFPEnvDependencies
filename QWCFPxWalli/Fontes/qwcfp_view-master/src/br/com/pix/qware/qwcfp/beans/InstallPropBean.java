package br.com.pix.qware.qwcfp.beans;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.pix.qware.qwcfp.util.PropKeys;
import br.com.pix.qware.qwcfp.util.QwcfpInitials;



@ManagedBean (name = "installPropBean")
@ViewScoped
public class InstallPropBean implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6580819713307455086L;
	
	
	private String wsUrl;
	
	@PostConstruct 
	public void init () {
		carreagarPropriedades();
	}
	
	
	private void carreagarPropriedades(){
		QwcfpInitials initialsInstace = QwcfpInitials.getInstance();
		Properties properties = initialsInstace.getProperties();
		
		if(properties != null){
			
			String httpsString = (String) properties.get(PropKeys.WS_HTTPS_KEY.getKey());
			Boolean https = new Boolean(httpsString);
			
			String protocol = "";
			
			if(https)
				protocol = "https://";
			else
				protocol = "http://";
			
			String ip = (String) properties.get(PropKeys.WS_IP_KEY.getKey());
			String porta = (String) properties.get(PropKeys.WS_PORT_KEY.getKey());
			String contextRoot = (String) properties.get(PropKeys.WS_SERV_NAME_KEY.getKey());
			
			if(ip != null && !ip.isEmpty() && porta != null && !porta.isEmpty() && contextRoot != null && !contextRoot.isEmpty()){
				setWsUrl(protocol+ip+":"+porta+contextRoot+"/");
			}
		}
	}


	public String getWsUrl() {
		return wsUrl;
	}


	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}

}
