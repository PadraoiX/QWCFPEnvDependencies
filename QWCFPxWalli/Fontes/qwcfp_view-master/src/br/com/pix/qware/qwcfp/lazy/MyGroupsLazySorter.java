package br.com.pix.qware.qwcfp.lazy;

import java.lang.reflect.Field;
import java.util.Comparator;

import org.primefaces.model.SortOrder;





import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.qwcss.ws.dto.FileListDTO;

public class MyGroupsLazySorter implements Comparator<MyGroupsWrapper> {

	private String		sortField;

	private SortOrder	sortOrder;

	public MyGroupsLazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@Override
    public int compare(MyGroupsWrapper file1, MyGroupsWrapper file2) {
        try {
        	Field  field1 = MyGroupsWrapper.class.getDeclaredField(this.sortField);
        	Field  field2 = MyGroupsWrapper.class.getDeclaredField(this.sortField);
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


