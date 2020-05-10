package br.com.pix.qware.qwcfp.util;

import java.util.Calendar;

public class DateItem implements Comparable<DateItem> {

	private Calendar month;
	private String monthName;
	private int order;

	public DateItem(Calendar month, String monthName, int order) {
		this.month = month;
		this.monthName = monthName;
		this.order = order;
	}

	public Calendar getMonth() {
		return month;
	}

	public void setMonth(Calendar month) {
		this.month = month;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(DateItem o) {
		int ret = 0;
		if (o.order == this.order) {
		} else {
			ret = o.order < this.order ? 1 : -1;
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((monthName == null) ? 0 : monthName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateItem other = (DateItem) obj;
		if (monthName == null) {
			if (other.monthName != null)
				return false;
		} else if (!monthName.equals(other.monthName))
			return false;
		return true;
	}

	
}
