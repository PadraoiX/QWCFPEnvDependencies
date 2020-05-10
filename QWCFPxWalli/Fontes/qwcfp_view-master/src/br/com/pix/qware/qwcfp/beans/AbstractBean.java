package br.com.pix.qware.qwcfp.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import br.com.pix.qware.qwcfp.util.QwcfpInitials;
import br.com.pix.qwcfp.ws.client.connection.Definitions;
import br.com.qwcss.ws.dto.SimpleDTO;

/**
 * Superclasse de todos os CDI beans da aplicao
 * Realmente ele no  um BeanS
 */
public abstract class AbstractBean implements Serializable {

	private boolean	isSameVersion;
	private boolean	isWSOn;
	
	@PostConstruct
	public void init() {

		QwcfpInitials.getInstance().initializeWebService();
		//testeWSConnection();

	}
	
	public void testeWSConnection(){
		
		
		SimpleDTO dto1 = Definitions.checkVersion();

		if (dto1 != null) {
			if (!dto1.getErrorCode().equals(0)) {
				setSameVersion(false);
			}else{
				setSameVersion(true);
			}
		} else {
			//No foi encontrado um servidor respondendo
			setWSOn(false);
			return;
		}
		
		setWSOn(true);
	}

	public boolean isSameVersion() {
		return isSameVersion;
	}

	public void setSameVersion(boolean isSameVersion) {
		this.isSameVersion = isSameVersion;
	}

	public boolean isWSOn() {
		return isWSOn;
	}

	public void setWSOn(boolean isWSOn) {
		this.isWSOn = isWSOn;
	}

}
