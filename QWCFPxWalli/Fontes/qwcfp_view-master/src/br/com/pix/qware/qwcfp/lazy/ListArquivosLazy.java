package br.com.pix.qware.qwcfp.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.pix.qwcfp.ws.client.manager.File;
import br.com.qwcss.ws.dto.FileAndFileVersionWrapper;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;
import br.com.qwcss.ws.dto.VersionsDTO;

@Named
public class ListArquivosLazy extends LazyDataModel<FileListDTOEx> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2212720083472507022L;

	public List<FileListDTOEx> datasource;
	

	private FileManagedService fileManagedService;
	
	private FileVersionListService	fileVersionService;

	private String filterValue;

	public ListArquivosLazy(FileManagedService fileManagedService, FileVersionListService	fileVersionService) {
		this.fileManagedService = fileManagedService;
		this.fileVersionService = fileVersionService;
	}

	public ListArquivosLazy(FileManagedService fileManagedService,
			String filterValue, FileVersionListService	fileVersionService) {
		this.fileManagedService = fileManagedService;
		this.filterValue = filterValue;
		this.fileVersionService = fileVersionService;
	}
	

	@Override
	public FileListDTOEx getRowData(String rowKey) {
		int intRowKey = Integer.parseInt(rowKey);
		
		for (FileListDTOEx fileListDTO : datasource) {
			if (fileListDTO.getFileId() == intRowKey)
				return fileListDTO;
		}
		return null;
	}

	@Override
	public Object getRowKey(FileListDTOEx FileListDTO) {
		return FileListDTO.getFileId();
	}
	

	@Override
	public List<FileListDTOEx> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		
		List<FileListDTOEx> listFileAndVersion = new ArrayList<FileListDTOEx>();
		
		datasource =  datasource == null ? new ArrayList<FileListDTOEx>(): datasource;
		String aliasGrupo = (String) Util.getPropertySession("GROUP_ALIAS");
		
		if (first > 0 && first <= datasource.size()) {
			listFileAndVersion = datasource.subList(0, first);
		}
		
		String sortBy = null;
		String sortByField = null;
		
		if (sortField != null) {
			if (sortField.equalsIgnoreCase("fileName")) {
				sortByField = File.FIELD_FILE_NAME;
			}else if (sortField.equalsIgnoreCase("memberName")) {
				sortByField = File.FIELD_OWNER_MEMBER;
			}else if (sortField.equalsIgnoreCase("transferDate")) {
				sortByField = File.FIELD_TRANSFER_DATE;
			}else if (sortField.equalsIgnoreCase("aditionalInformation")) {
				sortByField = File.FIELD_COMENT;
			}else if (sortField.equalsIgnoreCase("sizeInBytes")) {
				sortByField = File.FIELD_FILE_SIZE;
			}
		}
		
		
		if (sortOrder != null) {
			if (sortOrder.compareTo(SortOrder.ASCENDING) == 0) {
				sortBy = File.SORT_ASC;
			}else {
				sortBy = "";
			}
		}
		
		FileListDTOEx[] array = fileManagedService.listar(aliasGrupo, filterValue, first/pageSize, pageSize, true,sortBy,sortByField);
		if (array != null) {
			listFileAndVersion = Arrays.asList(array);
		}else {
			listFileAndVersion = new ArrayList<FileListDTOEx>() ;
		}
		/*if(array != null){
			for (int i = 0; i < array.length; i++) {
				if (array[i].getErrorCode() != ViewError.OK.getCode()) {
					break;
				}
				FileAndFileVersionWrapper fileVersionDTO = new FileAndFileVersionWrapper();
				VersionsDTO[] versions = fileVersionService.listar(array[i].getFileid());
				fileVersionDTO.setVersionDTO(versions);
				fileVersionDTO.setFileDTO(array[i]);
				listFileAndVersion.add(fileVersionDTO);
			}	
		}*/
		
//		dynamicSource.addAll(array != null ? Arrays.asList(array) : new ArrayList<FileListDTO>());
		
		datasource = listFileAndVersion;
		
		// filter
		if (datasource != null && !datasource.isEmpty()) {

			// sort
			/*if (sortField != null) {
				Collections.sort(datasource, new ListArquivosLazySorter(
						sortField, sortOrder));
			}*/

			// rowCount
			int dataSize = datasource.size();
			
			
			Integer totalFiles  = 0;
			
			FileListDTOEx file = datasource.get(0);
			if(file != null){
				Long total = file.getTotal();
				totalFiles = total.intValue();
			}
			
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

		return null;
	}

}
