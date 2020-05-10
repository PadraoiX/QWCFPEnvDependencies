package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.manager.SendInvitations;
import br.com.qwcss.ws.dto.SimpleDTO;

public class SendInvitationsService extends Service{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8419967826602826144L;
	
	
	public SimpleDTO sendInvitations(String emails, String grupoInvitedId, String aditionalInformation){
		return SendInvitations.sendInvitations(getConnectedUser(), emails, grupoInvitedId, aditionalInformation);
	}
	
	public SimpleDTO checkValidToken(String token){
		return SendInvitations.checkValidToken(token);
	}
}
