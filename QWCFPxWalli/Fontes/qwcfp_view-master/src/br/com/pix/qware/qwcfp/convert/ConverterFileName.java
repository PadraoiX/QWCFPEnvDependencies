package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("convertFileName")
public class ConverterFileName  implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uIcomponent, String nome) {
		if (nome != null) {
			if (nome.length() > 20)
				nome = nome.substring(0, 17) + "...";
			return nome;
		}

		return null;

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if(arg2 == null || "".equals(arg2)){  
            return "";  
        }  
		String nome = (String) arg2;
		
		if (nome.length() > 20)
			nome = nome.substring(0, 17) + "...";
		return nome;
		
		//123 5678 5678
	}

}