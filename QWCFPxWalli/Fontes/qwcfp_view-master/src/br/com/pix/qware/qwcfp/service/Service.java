package br.com.pix.qware.qwcfp.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;

/**
 * Superclasse de todos os services da aplicao
 */
@Named
@RequestScoped
public class Service implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Inject
	private LoginBean			loginBean;

	private ConnectedUser		connectedUser;
	private boolean				onlyWsContract;

	public Service() {
		super();
		this.connectedUser = new ConnectedUser();
	}

	@PostConstruct
	public void init() {
		connectedUser = loginBean.getLoggedUser();
	}

	public ConnectedUser getConnectedUser() {
		return connectedUser;
	}

	public void setConnectedUser(ConnectedUser connectedUser) {
		this.connectedUser = connectedUser;
	}

	public boolean isOnlyWsContract() {
		return onlyWsContract;
	}

	public void setOnlyWsContract(boolean onlyWsContract) {
		this.onlyWsContract = onlyWsContract;
	}

}
