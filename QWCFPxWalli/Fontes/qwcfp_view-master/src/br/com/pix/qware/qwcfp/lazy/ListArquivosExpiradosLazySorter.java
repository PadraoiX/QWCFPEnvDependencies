package br.com.pix.qware.qwcfp.lazy;

import java.lang.reflect.Field;
import java.util.Comparator;

import org.primefaces.model.SortOrder;

import br.com.qwcss.ws.dto.FileAndFileVersionWrapper;
import br.com.qwcss.ws.dto.FileListDTO;

public class ListArquivosExpiradosLazySorter implements Comparator<FileListDTO> {

	private String		sortField;

	private SortOrder	sortOrder;

	public ListArquivosExpiradosLazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@Override
    public int compare(FileListDTO file1, FileListDTO file2) {
        try {
        	Field  field1 = FileListDTO.class.getDeclaredField(this.sortField);
        	Field  field2 = FileListDTO.class.getDeclaredField(this.sortField);
        	field1.setAccessible(true);
        	field2.setAccessible(true);
        	
        	Object value1 = field1.get(file1);
        	Object value2 = field1.get(file2);
            
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }

}


