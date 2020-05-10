 

package br.com.pix.qware.qwcfp.lazy;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.qwcss.ws.NotifieWrapper;
import br.com.qwcss.ws.dto.NotifieDTO;

@Named
public class ListNotifyMemberLazy extends  LazyDataModel<NotifieDTO> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7602611525881883891L;

	public List<NotifieDTO>		datasource;
	
	private NotifyService 		notifyService;
	
	private Integer	 			memberId;

	
	public ListNotifyMemberLazy(NotifyService notifyService, Integer memberId) {
		this.setNotifyService(notifyService);
		this.setMemberId(memberId);
	}

	@Override
	public NotifieDTO getRowData(String rowKey) {
        for(NotifieDTO event : datasource) {
            if(event.getId().toString().equals(rowKey)) {
                return event;
            }
        }
        return null;
	}

	@Override
	public Object getRowKey(NotifieDTO NotifieDTO) {
		return NotifieDTO.getId();
	}

	@Override
	public List<NotifieDTO> load(int first,
			int pageSize,
			String sortField,
			SortOrder sortOrder,
			Map<String, Object> filters) {
		
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.add(Calendar.DATE, -7);

		Calendar dataFim = Calendar.getInstance();
		dataFim.add(Calendar.DATE, 1);
		
		NotifieWrapper array = notifyService.listarMemberEx(getMemberId(), dataInicio, dataFim, true, first/pageSize, pageSize);
		
		Integer totalFiles  = 0;
		
		if(array != null && array.getErrorCode() == 0){
			Long total = array.getTotalRecords();
			totalFiles = total.intValue();

			
			if(array.getNotificacoes() != null ){
				datasource = Arrays.asList(array.getNotificacoes());
			}
			
			
			// filter
			if (datasource != null) {

				// sort
				if (sortField != null) {
					Collections.sort(datasource, new ListNotifyLazySorter(
							sortField, sortOrder));
				}

				// rowCount
				int dataSize = datasource.size();
				
				
				
				
				this.setRowCount(totalFiles);

				// paginate
				if (dataSize > pageSize) {
					try {
						return datasource.subList(first, first + pageSize);
					} catch (IndexOutOfBoundsException e) {
						return datasource.subList(first, first + (dataSize % pageSize));
					}
				} else
					return datasource;
			}		
			
		}
		
		return null;
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

}
