package br.com.pix.qware.qwcfp.convert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "converteDataHist")
public class ConverteDataHist implements Converter {

   
    
    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        df.setLenient(false);
        try {
            
        	Date data = df.parse(arg2);
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(data);
        	
            return calendar;            
            
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player")); 
        }
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            StringBuilder date = new StringBuilder(df.format(((Calendar) arg2).getTime()));
            return date.toString();
        } catch (Exception e) {
            return "";
        }
    }
}

