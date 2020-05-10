package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.manager.DownloadLink;
import br.com.pix.qwcfp.ws.client.manager.SendInvitations;
import br.com.qwcss.ws.dto.DownloadLinkDTO;
import br.com.qwcss.ws.dto.DownloadLinkParsDTO;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;
import br.com.qwcss.ws.dto.SimpleDTO;

public class DownloadLinkService extends Service{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8419967826602826144L;
	
	
	public DownloadLinkDTO generate(String justify, Integer groupId, Integer versionId, String[] mailToList, FileListDTOEx[] files) {
		return DownloadLink.generate(getConnectedUser(), justify, groupId, versionId, mailToList, files);
	}
	
	public DownloadLinkParsDTO start(String token) {
		return DownloadLink.start(token);
	}
}
