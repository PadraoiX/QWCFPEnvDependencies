package br.com.pix.qware.qwcfp.wrappers;

import java.util.HashMap;

import br.com.pix.qware.qwcfp.beans.uties.ListDashboardMetaBean;
import br.com.qwcss.ws.dto.MetadataTableItemDTO;

public class DBoardMetaWrapper {

	private static final String REGEX_1 = ",";
	private static final String REGEX_2 = "=";
	private static final String REGEX_3 = "\\{";
	private static final String REGEX_4 = "\\}";
	private static final String REGEX_5 = "\"";
	private static final String BLANK = "";
	private String fileName;
	private String status;
	private Long version;
	private Long groupId;

	private HashMap<String, String> columnValueMap;

	public DBoardMetaWrapper(MetadataTableItemDTO metadataTableItemDTO) {
		super();

		this.fileName = metadataTableItemDTO.getFileName();
		this.version = metadataTableItemDTO.getVersionId();
		this.status = metadataTableItemDTO.getStatus();
		this.groupId = metadataTableItemDTO.getInfogrpId();
		String values = metadataTableItemDTO.getMetaValues();

		columnValueMap = new HashMap<>();

		if (values != null) {
			values = values.replaceAll(REGEX_3, BLANK).replaceAll(REGEX_4, BLANK).replaceAll(REGEX_5, BLANK);
			String[] columns = values.split(REGEX_1);
			for (String string : columns) {
				String[] content = string.split(REGEX_2);

				columnValueMap.put(content[0], content[1]);
			}
		}
		
		columnValueMap.put(ListDashboardMetaBean.NOME_DO_LOTE, fileName);
		columnValueMap.put(ListDashboardMetaBean.STATUS2, status);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public HashMap<String, String> getColumnValueMap() {
		return columnValueMap;
	}

	public void setColumnValueMap(HashMap<String, String> columnValueMap) {
		this.columnValueMap = columnValueMap;
	}

}
