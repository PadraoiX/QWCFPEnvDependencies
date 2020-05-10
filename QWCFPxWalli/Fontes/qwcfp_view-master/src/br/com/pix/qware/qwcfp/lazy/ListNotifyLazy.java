package br.com.pix.qware.qwcfp.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.qwcss.ws.dto.NotifieDTO;

public class ListNotifyLazy extends  LazyDataModel<NotifieDTO> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7602611525881883891L;

	public List<NotifieDTO>		datasource;

	public ListNotifyLazy(List<NotifieDTO> datasource) {
		this.datasource = datasource;
	}

	@Override
	public NotifieDTO getRowData(String rowKey) {
		for (NotifieDTO NotifieDTO : datasource) {
			if (NotifieDTO.getId().toString().equals(rowKey))
				return NotifieDTO;
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
		List<NotifieDTO> data = new ArrayList<NotifieDTO>();

		//filter
		for (NotifieDTO NotifieDTO : datasource) {
			boolean match = true;

			if (filters != null) {
				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
					try {
						String filterProperty = it.next();
						Object filterValue = filters.get(filterProperty);
						String fieldValue = String.valueOf(NotifieDTO.getClass()
								.getField(filterProperty).get(NotifieDTO));

						if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
							match = true;
						} else {
							match = false;
							break;
						}
					} catch (Exception e) {
						match = false;
					}
				}
			}

			if (match) {
				data.add(NotifieDTO);
			}
		}

		//sort
		if (sortField != null) {
			Collections.sort(data, new ListNotifyLazySorter(sortField, sortOrder));
		}

		//rowCount
		int dataSize = data.size();
		this.setRowCount(dataSize);

		//paginate
		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}

}
