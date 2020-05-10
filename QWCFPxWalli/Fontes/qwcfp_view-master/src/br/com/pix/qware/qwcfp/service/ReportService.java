package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.list.ReportEx;
import br.com.qwcss.ws.dto.RptAreaSpaceDTO;
import br.com.qwcss.ws.dto.RptFileByLayerDTO;
import br.com.qwcss.ws.dto.RptGroupSpaceDTO;
import br.com.qwcss.ws.dto.RptGroupsDTO;
import br.com.qwcss.ws.dto.RptMemberDTO;
import br.com.qwcss.ws.dto.RptOcupiedSpaceDTO;

public class ReportService extends Service{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	public RptAreaSpaceDTO[] listAreaSpace() {
		return ReportEx.listAreaSpace(getConnectedUser());
	}
	
	public RptFileByLayerDTO[] listFilesByLayer() {
		return ReportEx.listFilesByLayer(getConnectedUser());
	}
	
	public RptGroupSpaceDTO[] listGroupSpace() {
		return ReportEx.listGroupSpace(getConnectedUser());
	}
	
	public RptGroupsDTO[] listGroups() {
		return ReportEx.listGroups(getConnectedUser());
	}
	
	public RptOcupiedSpaceDTO[] listOcupiedSpace() {
		return ReportEx.listOcupiedSpace(getConnectedUser());
	}
	
	public RptMemberDTO[] listUsers() {
		return ReportEx.listUsers(getConnectedUser());
	}

}
