package br.com.pix.qware.qwcfp.lazy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qware.qwcfp.wrappers.WrappedGroupsContainer;

@Named
public class MyGroupsLazy extends LazyDataModel<MyGroupsWrapper> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2212720083472507022L;

	public List<MyGroupsWrapper> datasource;

	private InformationGroupService groupService;

	private String filterValue;

	public MyGroupsLazy(InformationGroupService groupService) {
		this.groupService = groupService;
	}

	public MyGroupsLazy(InformationGroupService groupService,
			String filterValue) {
		this.groupService = groupService;
		this.filterValue = filterValue;
	}

	@Override
	public MyGroupsWrapper getRowData(String rowKey) {
		for (MyGroupsWrapper group : datasource) {
			if (group.getMyGroupsEx().getGroupId().toString().equals(rowKey))
				return group;
		}
		return null;
	}

	@Override
	public Object getRowKey(MyGroupsWrapper group) {
		return group.getMyGroupsEx().getGroupId();
	}

	@Override
	public List<MyGroupsWrapper> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		WrappedGroupsContainer container = groupService.myGroupsEx("STATUS_GI", "ATIVO", first/pageSize, pageSize, filterValue);
		List<MyGroupsWrapper> list = container != null? container.getList() : null;
		datasource = list;

		// filter
		if (datasource != null) {

			// sort
			if (sortField != null) {
				Collections.sort(datasource, new MyGroupsLazySorter(
						sortField, sortOrder));
			}

			// rowCount
			int dataSize = datasource.size();
			
			
			Long totalFiles  = container.getSize();
			
			this.setRowCount(totalFiles != null? totalFiles.intValue(): 0);

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

		return null;
	}

}
