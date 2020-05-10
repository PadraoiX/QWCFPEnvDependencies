package br.com.pix.qware.qwcfp.beans.uties;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.lazy.ListDBoardMetaLazy;
import br.com.pix.qware.qwcfp.service.DashBoardService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.wrappers.DBoardFilesWrapper;
import br.com.pix.qware.qwcfp.wrappers.DBoardMetaWrapper;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.BoardItemDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.MetadataColumns;
import br.com.qwcss.ws.dto.MetadataColumnsContainerDTO;

@ManagedBean(name = "dBoardMeta")
@ViewScoped
public class ListDashboardMetaBean extends AbstractBean {

	public static final String STATUS2 = "Status";
	public static final String NOME_DO_LOTE = "Nome do lote";

	/**
	 * 
	 */
	private static final long serialVersionUID = -2298501422983833970L;

	@Inject
	private DashBoardService service;

	@Inject
	private InformationGroupService groupService;

	private ListDBoardMetaLazy itemList;

	private DBoardFilesWrapper selectedStatus;

	private List<MetadataColumns> metadataColumns;

	private String filterValue;
	
	private ListDBoardMetaLazy filteredList;

	private DBoardMetaWrapper selectedMetaItem;

	private String selected;

	private GroupDTO selectedGroup;

	@PostConstruct
	public void init() {
		filterValue = "";

		importSessionGroup();
		// TODO retirar linha abaixo quando parar de testar

		mountTableLaoyut();
		updateFileStatus();
		updateFileList();
	}

	private void mountTableLaoyut() {

		try {
			MetadataColumnsContainerDTO container = service.metadataColumns(selectedGroup.getGroupId());
			if (container.getErrorCode() == ViewError.OK.getCode()) {
				MetadataColumns[] cols = container.getColumns();
				
				metadataColumns = new ArrayList<MetadataColumns>(Arrays.asList(cols));

				
				//COLUNAS FIXAS
				MetadataColumns fileName = new MetadataColumns();
				fileName.setColName(NOME_DO_LOTE);
				MetadataColumns status = new MetadataColumns();
				status.setColName(STATUS2);

				metadataColumns.add(fileName);
				metadataColumns.add(status);
				
			}
		} catch (Exception e) {
			metadataColumns = new ArrayList<>();
		}
	}

	private void importSessionGroup() {
		Integer groupId = (Integer) Util.getPropertySession("ID_GROUP");
		if (groupId != null) {
			selectedGroup = groupService.getGroupById(groupId);
		}

	}

	public String goToDBGraph() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/dashboard.faces");
		return "dashboard.faces?redirect=trues";
	}

	public void updateFileStatus() {
		if (selectedMetaItem != null) {
			BoardItemDTO temp = service.fileStatus(selectedMetaItem.getVersion().intValue());
			
			if (temp != null && temp.getErrorCode() == ViewError.OK.getCode()) {
				selectedStatus = new DBoardFilesWrapper(temp);
			}
		}
	}

	public void updateFileList() {
		itemList = new ListDBoardMetaLazy(filterValue, selectedGroup, service);
	}

	public ListDBoardMetaLazy getItemList() {
		return itemList;
	}

	public void setItemList(ListDBoardMetaLazy itemList) {
		this.itemList = itemList;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public DBoardFilesWrapper getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(DBoardFilesWrapper selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public GroupDTO getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(GroupDTO selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public List<MetadataColumns> getMetadataColumns() {
		return metadataColumns;
	}

	public void setMetadataColumns(List<MetadataColumns> metadataColumns) {
		this.metadataColumns = metadataColumns;
	}

	public DBoardMetaWrapper getSelectedMetaItem() {
		return selectedMetaItem;
	}

	public void setSelectedMetaItem(DBoardMetaWrapper selectedMetaItem) {
		this.selectedMetaItem = selectedMetaItem;
	}

	public ListDBoardMetaLazy getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(ListDBoardMetaLazy filteredList) {
		this.filteredList = filteredList;
	}

	static public class ColumnModel implements Serializable {

		private String header;

		public ColumnModel(String header) {
			this.header = header;
		}

		public String getHeader() {
			return header;
		}

	}

}
