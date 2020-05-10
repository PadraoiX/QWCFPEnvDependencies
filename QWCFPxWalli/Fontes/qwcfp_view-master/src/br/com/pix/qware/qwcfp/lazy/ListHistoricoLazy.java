package br.com.pix.qware.qwcfp.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.qwcss.ws.dto.EventDTO;
import br.com.qwcss.ws.dto.FileListDTO;

public class ListHistoricoLazy extends LazyDataModel<EventDTO> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2212720083472507022L;
	
	
	public List<EventDTO>  datasource;
	
     
	    public ListHistoricoLazy(List<EventDTO> datasource) {
	        this.datasource = datasource;
	    }
	     
	    @Override
	    public EventDTO getRowData(String rowKey) {
	        for(EventDTO EventDTO : datasource) {
	            if(EventDTO.getId().toString().equals(rowKey))
	                return EventDTO;
	        }
	        return null;
	    }
	 
	    @Override
	    public Object getRowKey(EventDTO EventDTO) {
	        return EventDTO.getId();
	    }
	 
	    @Override
	    public List<EventDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
	        List<EventDTO> data = new ArrayList<EventDTO>();
	 
	        //filter
	        for(EventDTO EventDTO : datasource) {
	            boolean match = true;
	 
	            if (filters != null) {
	                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	                    try {
	                        String filterProperty = it.next();
	                        Object filterValue = filters.get(filterProperty);
	                        String fieldValue = String.valueOf(EventDTO.getClass().getField(filterProperty).get(EventDTO));
	 
	                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
	                            match = true;
	                    }
	                    else {
	                            match = false;
	                            break;
	                        }
	                    } catch(Exception e) {
	                        match = false;
	                    }
	                }
	            }
	 
	            if(match) {
	                data.add(EventDTO);
	            }
	        }
	 
	        //sort
	        if(sortField != null) {
	            Collections.sort(data, new ListHistoricoLazySorter(sortField, sortOrder));
	        }
	 
	        //rowCount
	        int dataSize = data.size();
	        this.setRowCount(dataSize);
	 
	        //paginate
	        if(dataSize > pageSize) {
	            try {
	                return data.subList(first, first + pageSize);
	            }
	            catch(IndexOutOfBoundsException e) {
	            	if (first > dataSize ) {
	            		return data.subList(dataSize - (dataSize % pageSize),  dataSize);
					}else{
						return data.subList(first, first + (dataSize % pageSize));
					}
	            }
	        }else {
	            return data;
	        }
	    }
	
	
}
