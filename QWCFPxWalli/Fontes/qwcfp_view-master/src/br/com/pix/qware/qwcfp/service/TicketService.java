package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.manager.GetTicket;
import br.com.pix.qwcfp.ws.client.manager.GetTicketDownload;
import br.com.pix.qwcfp.ws.client.manager.GetTicketUpload;
import br.com.qwcss.ws.dto.SimpleDTO;

public class TicketService extends Service {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4005304718781514092L;

	public SimpleDTO getTicket() {
		return GetTicket.getTicket(getConnectedUser().getLoginKey());
	}

	public SimpleDTO getTicketUpload(String operation, String fileName,
			String saveAs, String disp, String fileType, String objId,
			String fileSize, String ldapUser, String ldapUserPass) {
		
		
		return GetTicketUpload.getTicketUpload(
				getConnectedUser().getLoginKey(), operation, fileName, saveAs,
				disp, fileType, objId, fileSize, ldapUser, ldapUserPass);
	}
	
	
	
	
	
	public SimpleDTO getTicketDownload(String operation, String fileName,
			String saveAs, String disp, String fileType, String ldapUser, String ldapUserPass) {
		return GetTicketDownload.getTicketDownload(getConnectedUser().getLoginKey(), operation, fileName, saveAs, disp, fileType, ldapUser, ldapUserPass);
	}

}
