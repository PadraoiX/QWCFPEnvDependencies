package br.com.pix.qware.qwcfp.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.pix.qware.qwcfp.beans.uties.ListDashboardMetaBean;
import br.com.pix.qware.qwcfp.service.DashBoardService;
import br.com.pix.qware.qwcfp.wrappers.DBoardMetaWrapper;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DBoardMetaDTO;
import br.com.qwcss.ws.dto.DBoardMetaListDTO;
import br.com.qwcss.ws.dto.FileAndFileVersionWrapper;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.MetadataConteinerDTO;
import br.com.qwcss.ws.dto.MetadataListFiltersEntry;
import br.com.qwcss.ws.dto.MetadataTableItemDTO;

@Named
public class ListDBoardMetaLazy extends LazyDataModel<DBoardMetaWrapper> {

	private static final String STATUS = "STATUS";

	private static final String FILE_NAME = "FILE_NAME";

	/**
	 * 
	 */
	private static final long serialVersionUID = -2212720083472507022L;

	public List<DBoardMetaWrapper> datasource;

	public DashBoardService dbService;

	private String filterValue;

	private GroupDTO selectedGroup;

	public ListDBoardMetaLazy() {
	}

	public ListDBoardMetaLazy(String filterValue, GroupDTO selectedGroup, DashBoardService dbService) {
		this.filterValue = filterValue;
		this.selectedGroup = selectedGroup;
		this.dbService = dbService;
	}

	@Override
	public DBoardMetaWrapper getRowData(String rowKey) {

		for (DBoardMetaWrapper dbFile : datasource) {
			if (dbFile.getFileName().equalsIgnoreCase(rowKey))
				return dbFile;
		}
		return null;
	}

	@Override
	public Object getRowKey(DBoardMetaWrapper dbFile) {
		return dbFile.getFileName();
	}

	@Override
	public List<DBoardMetaWrapper> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {

		List<DBoardMetaWrapper> dBoardList = new ArrayList<DBoardMetaWrapper>();

		datasource = datasource == null ? new ArrayList<DBoardMetaWrapper>() : datasource;

		if (first > 0 && first <= datasource.size()) {
			dBoardList = datasource.subList(0, first);
		}
		
		MetadataListFiltersEntry[] filterEntries = null;

		if (filters != null && !filters.isEmpty()) {
			
			Set<String> keys = filters.keySet();
			filterEntries = new MetadataListFiltersEntry[keys.size()];
			
			int i = 0;
			for (String key : keys) {
				MetadataListFiltersEntry entry = null;
				if (key.equals(ListDashboardMetaBean.NOME_DO_LOTE)) {
					entry = new MetadataListFiltersEntry(FILE_NAME, (String) filters.get(key));
				} else if (key.equals(ListDashboardMetaBean.STATUS2)) {
					entry = new MetadataListFiltersEntry(STATUS, (String) filters.get(key));
				} else {
					entry = new MetadataListFiltersEntry(key, (String) filters.get(key));
				}
				
				filterEntries[i] = entry;
				i++;
			}
		}

		MetadataConteinerDTO wsReturn = dbService.listMetadata(selectedGroup.getGroupId(), first / pageSize, pageSize,
				null, null, filterEntries);

		Long totalFiles = 0L;
		if (wsReturn.getErrorCode().equals(ViewError.OK.getCode())) {
			MetadataTableItemDTO[] itens = wsReturn.getItens();
			totalFiles = wsReturn.getTotal();
			if (itens != null) {
				for (MetadataTableItemDTO metadataTableItemDTO : itens) {
					dBoardList.add(new DBoardMetaWrapper(metadataTableItemDTO));
				}
			}
		}

		datasource = dBoardList;

		// filter
		if (datasource != null && !datasource.isEmpty()) {

			// sort
			if (sortField != null) {
				Collections.sort(datasource, new ListDBoardMetaSorter(sortField, sortOrder));
			}

			// rowCount
			int dataSize = datasource.size();

			if (totalFiles != null) {
				this.setRowCount(totalFiles.intValue());
			}
			this.setPageSize(pageSize);

			// paginate
			if (dataSize > pageSize) {
				try {
					return datasource.subList(first, first + pageSize);
				} catch (IndexOutOfBoundsException e) {
					return datasource.subList(first, first + (dataSize % pageSize));
				}
			}
		}
		return datasource;
	}

}
