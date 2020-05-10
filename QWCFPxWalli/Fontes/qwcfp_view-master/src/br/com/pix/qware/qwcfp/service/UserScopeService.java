package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.list.UserScope;
import br.com.pix.qwcfp.ws.client.manager.No;
import br.com.qwcss.ws.dto.NodeDTO;
import br.com.qwcss.ws.dto.UserScopeNodeDTO;


public class UserScopeService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	public  UserScopeNodeDTO[] getUserScope(Integer memberId){
		return UserScope.getUserScope(getConnectedUser(), memberId);
	}
	
}
