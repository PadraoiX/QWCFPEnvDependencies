package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pix.qware.qwcfp.util.Util;

@FacesConverter("converterFone")
public class ConverterTelefone  implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uIcomponent, String phone) {
		if (phone != null) {
			if (phone.trim().equals(""))
				return null;
			else {
				phone = phone.replace("-", "");
				phone = phone.replace("(", "");
				phone = phone.replace(")", "");
				phone = phone.replace(" ", "");
				phone = phone.replace("+", "");
				return phone;
			}
		}

		return null;

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if(arg2 == null || "".equals(arg2)){  
            return "";  
        }  
		String telefone = (String) arg2;
		telefone = telefone.trim();
		if(telefone.length() == 10)
			return Util.formatar(arg2.toString(), "(##) ####-####");
		else if(telefone.length() == 11)
			return Util.formatar(arg2.toString(), "(##) #####-####");
		else
			return telefone;
		
	}

}