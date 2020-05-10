package br.com.pix.qware.qwcfp.lazy;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;

import org.primefaces.model.SortOrder;

import br.com.qwcss.ws.dto.FileAndFileVersionWrapper;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

public class ListArquivosLazySorter implements Comparator<FileAndFileVersionWrapper> {

	private String sortField;

	private SortOrder sortOrder;

	public ListArquivosLazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(FileAndFileVersionWrapper file1, FileAndFileVersionWrapper file2) {

		/*if (this.sortField.contains("getLastVersion()") || this.sortField.contains("fileDTO") ) {
			String token[] = this.sortField.split("\\.");
			this.sortField = token[1];
		}
		
		if(this.sortField.contains("sizeInBytes")){		
			
			try {
				Field field1 = VersionsDTO.class.getDeclaredField(this.sortField);
				Field field2 = VersionsDTO.class.getDeclaredField(this.sortField);
				field1.setAccessible(true);
				field2.setAccessible(true);

				Object value1 = field1.get(file1.getVersionDTO()[file1.getLastVersionIndex()]);
				Object value2 = field1.get(file2.getVersionDTO()[file2.getLastVersionIndex()]);

				int value = ((Comparable) value1).compareTo(value2);

				return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;

			} catch (Exception e) {
				System.err.println("Erro tentando ordenar o VersionsDTO => " + this.sortField);
			}

		}else if(this.sortField.equals("lastVersionInfo()")) {
		
			try {
				Field field1 = FileListDTO.class.getDeclaredField(this.sortField);
				Field field2 = FileListDTO.class.getDeclaredField(this.sortField);
				field1.setAccessible(true);
				field2.setAccessible(true);

				Object value1 = file1.lastVersionInfo();
				Object value2 = file2.lastVersionInfo();

				int value = ((Comparable) value1).compareTo(value2);

				return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
			} catch (Exception e) {
				System.err.println("Erro tentando ordenar o FileListDTO => " + this.sortField);
			}
			
		}else if(this.sortField.contains("creationDate")) {
		
			try {
				Field field1 = VersionsDTO.class.getDeclaredField(this.sortField);
				Field field2 = VersionsDTO.class.getDeclaredField(this.sortField);
				field1.setAccessible(true);
				field2.setAccessible(true);

				Object value1 = field1.get(file1.getLastVersion());
				Object value2 = field2.get(file2.getLastVersion());

				int value = ((Comparable) value1).compareTo(value2);

				return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
			} catch (Exception e) {
				System.err.println("Erro tentando ordenar o FileListDTO => " + this.sortField);
			}
			
		}else if(this.sortField.equals("ownerMemberName") || this.sortField.equals("fileName")){
			
			try {
				Field field1 = FileListDTO.class.getDeclaredField(this.sortField);
				Field field2 = FileListDTO.class.getDeclaredField(this.sortField);
				field1.setAccessible(true);
				field2.setAccessible(true);

				String value1 = (String) field1.get(file1.getFileDTO());
				String value2 = (String) field1.get(file2.getFileDTO());

				int value = value1.compareToIgnoreCase(value2);
				
				return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
			} catch (Exception e) {
				System.err.println("Erro tentando ordenar o FileListDTO => " + this.sortField);
			}
			
			
		}else{
			throw new RuntimeException();
		}*/
		
		return 0; 
	}

}
