package br.com.pix.qware.qwcfp.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.qwcss.ws.dto.FileListDTO;

@Named
public class ListArquivosExpiredLazy extends LazyDataModel<FileListDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2212720083472507022L;

	public List<FileListDTO> datasource;

	private FileManagedService fileManagedService;

	//private String filterValue;
	
	private Boolean allowed;

	private Boolean gi;

	public ListArquivosExpiredLazy(FileManagedService fileManagedService) {
		this.fileManagedService = fileManagedService;
		
	}

	public ListArquivosExpiredLazy(FileManagedService fileManagedService, Boolean allowed, Boolean gi) {
		this.fileManagedService = fileManagedService;
		this.allowed = allowed;
		this.gi = gi;
	}

	@Override
	public FileListDTO getRowData(String rowKey) {
		int intRowKey = Integer.parseInt(rowKey);
		
		for (FileListDTO fileListDTO : datasource) {
			if (fileListDTO.getFileid()== intRowKey)
				return fileListDTO;
		}
		return null;
	}

	@Override
	public Object getRowKey(FileListDTO FileListDTO) {
		return FileListDTO.getFileid();
	}

	@Override
	public List<FileListDTO> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		List<FileListDTO> dynamicSource =  new ArrayList<FileListDTO>();
		datasource =  datasource == null? new ArrayList<FileListDTO>(): datasource;
		String aliasGrupo = (String) Util.getPropertySession("GROUP_ALIAS");
		
		if (first > 0) {
			dynamicSource = datasource.subList(0, first);
		}
		
		FileListDTO[] array = null;
		if (gi) {
			if (allowed)
				array = fileManagedService.listarPurgedGiPurgedAllowed(first/pageSize, pageSize);// expurgado autorizado
			else
				array = fileManagedService.listarPurgedGiPurged(first/pageSize, pageSize);// expurgado
		}else{
			if (allowed)
				array = fileManagedService.listarPurgedPurgedAllowed(aliasGrupo, first/pageSize, pageSize);// expurgado autorizado
			else 
				array = fileManagedService.listarPurgedPurged(aliasGrupo, first/pageSize, pageSize);// expurgado
			
		}
		
		dynamicSource.addAll(array != null ? Arrays.asList(array) : new ArrayList<FileListDTO>());
		
		datasource = dynamicSource;
		
		// filter
		if (datasource != null && !datasource.isEmpty()) {

			// sort
			if (sortField != null) {
				Collections.sort(datasource, new ListArquivosExpiradosLazySorter(
						sortField, sortOrder));
			}

			// rowCount
			int dataSize = datasource.size();
			
			
			Integer totalFiles  = 0;
			
			FileListDTO file = datasource.get(0);
			if(file != null){
				Long total = file.getTotalRecords();
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
