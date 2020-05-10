package br.com.pix.qware.qwcfp.lazy;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import br.com.qwcss.ws.dto.EventDTO;
import br.com.qwcss.ws.dto.FileListDTO;

public class ListHistoricoLazySorter implements Comparator<EventDTO> {

	private String		sortField;

	private SortOrder	sortOrder;

	public ListHistoricoLazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@Override
    public int compare(EventDTO file1, EventDTO file2) {
        try {
            Object value1 = EventDTO.class.getField(this.sortField).get(file1);
            Object value2 = EventDTO.class.getField(this.sortField).get(file2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }

}
