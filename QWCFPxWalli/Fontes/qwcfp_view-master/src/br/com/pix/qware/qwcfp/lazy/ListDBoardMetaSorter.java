package br.com.pix.qware.qwcfp.lazy;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import br.com.pix.qware.qwcfp.wrappers.DBoardFilesWrapper;
import br.com.pix.qware.qwcfp.wrappers.DBoardMetaWrapper;
import br.com.qwcss.ws.dto.DBoardMetaListDTO;
import br.com.qwcss.ws.dto.EventDTO;
import br.com.qwcss.ws.dto.FileListDTO;

public class ListDBoardMetaSorter implements Comparator<DBoardMetaWrapper> {

	private String		sortField;

	private SortOrder	sortOrder;

	public ListDBoardMetaSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@Override
    public int compare(DBoardMetaWrapper file1, DBoardMetaWrapper file2) {
        try {
            Object value1 = DBoardMetaListDTO.class.getField(this.sortField).get(file1);
            Object value2 = DBoardMetaListDTO.class.getField(this.sortField).get(file2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }

}
