package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pix.qware.qwcfp.util.Util;

@FacesConverter("converterCpf")
public class ConverterCpf implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uIcomponent, String cpf) {
		if (cpf != null) {
			if (cpf.trim().equals(""))
				return null;
			else {
				cpf = cpf.replace("-", "");
				cpf = cpf.replace(".", "");
				return cpf;
			}
		}

		return null;

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if(arg2 == null || "".equals(arg2)){  
            return "";  
        }  
		return Util.formatar(arg2.toString(), "###.###.###-##");
	}

}

