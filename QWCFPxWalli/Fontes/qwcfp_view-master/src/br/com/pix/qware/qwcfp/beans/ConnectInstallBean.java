package br.com.pix.qware.qwcfp.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.QwcfpInitials;
import br.com.pix.qwcfp.ws.client.erro.ViewError;

@Named("connectInstallBean")
@SessionScoped
public class ConnectInstallBean implements Serializable {

	private static final long		serialVersionUID	= 1L;
	
	@Inject
	private FacesMessages messages;

	private String user;
	
	private String pass;
	
	private static final String authUser = "admin";
	
	private static final String authPass = "pix2015admin";
	
	private boolean authorized;
	
	@PostConstruct
	private void init() {
		authorized = false;
	}
	
	public String autenticar() {
		
		if (user.equals(authUser) && pass.equals(authPass)) {
			authorized = true;
			QwcfpInitials.getInstance().setReset(true);
			return "formInstall.faces?faces-redirect=true";
		}else{
			messages.error(ViewError.PASS_USR_WRONG_ERR.getMsg());
		}
		
		return null;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean isAuthorized() {
		return authorized;
	}
	
}