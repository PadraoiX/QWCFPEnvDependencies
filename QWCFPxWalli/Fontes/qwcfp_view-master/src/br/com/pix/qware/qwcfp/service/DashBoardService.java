package br.com.pix.qware.qwcfp.service;


import java.util.Calendar;

import br.com.pix.qwcfp.ws.client.list.DashBoard;
import br.com.qwcss.ws.StatusCounterConteinerDTO;
import br.com.qwcss.ws.dto.BoardItemDTO;
import br.com.qwcss.ws.dto.MetadataColumnsContainerDTO;
import br.com.qwcss.ws.dto.MetadataConteinerDTO;
import br.com.qwcss.ws.dto.MetadataListFiltersEntry;

/**
 * Mtodos de negcio relacionados  entidade QwcssArea
 */
public class DashBoardService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Busca um registrro pelo id
	 */
	public BoardItemDTO fileStatus(Integer id) {
		return  new DashBoard().fileStatus(getConnectedUser(), id);
	}
	
	public StatusCounterConteinerDTO countStatusGroup(Integer id, Calendar dataInicio, Calendar dataFim) {
		return  new DashBoard().countStatusGroup(getConnectedUser(), id, dataInicio, dataFim);
	}
	
	public MetadataConteinerDTO listMetadata(Integer groupId, Integer pager, Integer records, String dataInicio, String dataFim, MetadataListFiltersEntry[] filters) {
		return  new DashBoard().listMetadataTable(getConnectedUser(), groupId, pager, records, dataInicio, dataFim, filters);
	}
	
	public MetadataColumnsContainerDTO metadataColumns(Integer groupId) {
		return  new DashBoard().metadataColumns(getConnectedUser(), groupId);
	}
	
}
