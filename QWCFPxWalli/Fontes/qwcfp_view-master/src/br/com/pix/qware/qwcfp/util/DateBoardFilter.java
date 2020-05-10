package br.com.pix.qware.qwcfp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DateBoardFilter {

	private static final String MONTH_NAME_FORMAT = "%s - %d";

	private List<DateItem> months;
	private HashMap<Integer, String> monthsCatalog;

	public DateBoardFilter() {

		monthsCatalog = new HashMap<>();
		monthsCatalog.put(Calendar.JANUARY, "JANEIRO");
		monthsCatalog.put(Calendar.FEBRUARY, "FEVEREIRO");
		monthsCatalog.put(Calendar.MARCH, "MARÇO");
		monthsCatalog.put(Calendar.APRIL, "ABRIL");
		monthsCatalog.put(Calendar.MAY, "MAIO");
		monthsCatalog.put(Calendar.JUNE, "JUNHO");
		monthsCatalog.put(Calendar.JULY, "JULHO");
		monthsCatalog.put(Calendar.AUGUST, "AGOSTO");
		monthsCatalog.put(Calendar.SEPTEMBER, "SETEMBRO");
		monthsCatalog.put(Calendar.OCTOBER, "OUTUBRO");
		monthsCatalog.put(Calendar.NOVEMBER, "NOVEMBRO");
		monthsCatalog.put(Calendar.DECEMBER, "DEZEMBRO");
		months = new ArrayList<>();

		for (int i = 0; i < 12; i++) {

			Calendar cal = Calendar.getInstance();
			String chosenMonth = null;
			if (i == 0) {
				chosenMonth = String.format(MONTH_NAME_FORMAT, monthsCatalog.get(cal.get(Calendar.MONTH)),
						cal.get(Calendar.YEAR));
				
				months.add(new DateItem(cal, chosenMonth, 0));
			} else {
				cal.add(Calendar.MONTH, i * -1);
				chosenMonth = String.format(MONTH_NAME_FORMAT, monthsCatalog.get(cal.get(Calendar.MONTH)),
						cal.get(Calendar.YEAR));
				
				months.add(new DateItem(cal, chosenMonth, i));
			}

		}
	}

	public List<String> monthNames() {

		ArrayList<String> retList = new ArrayList<String>();
		Collections.sort(months);

		for (DateItem dateItem : months) {
			retList.add(dateItem.getMonthName());
		}

		return retList;
	}

	public Calendar findMonthChoice(String choice) {
		
		DateItem dateItem = new DateItem(null, choice, 0);

		if (months.contains(dateItem)) {
			return months.get(months.indexOf(dateItem)).getMonth();
		}
		
		return null;
	}

}
