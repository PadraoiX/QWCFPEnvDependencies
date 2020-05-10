package br.com.pix.qware.qwcfp.util;

import java.lang.reflect.Type;
import java.text.*;
import java.util.*;

import com.google.gson.*;

public class DateDeserializer implements JsonDeserializer<Date> {

	private static final String[] DATE_FORMATS = new String[] { "MMM dd, yyyy HH:mm:ss", "MMM dd, yyyy", "dd/MM/yyyy",
			"mmm dd, yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd-MM-yyyy" };

	@Override
	public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc)
			throws JsonParseException {
		for (String format : DATE_FORMATS) {
			try {
				return new SimpleDateFormat(format, Locale.ROOT).parse(jsonElement.getAsString());
			} catch (ParseException e) {
			}
			try {
				return new SimpleDateFormat(format, new Locale("pt", "BR")).parse(jsonElement.getAsString());
			} catch (ParseException e) {
			}
		}
		throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString() + "\". Supported formats: "
				+ Arrays.toString(DATE_FORMATS));
	}

}