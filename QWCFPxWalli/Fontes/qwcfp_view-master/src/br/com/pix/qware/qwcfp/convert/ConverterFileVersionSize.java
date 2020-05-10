package br.com.pix.qware.qwcfp.convert;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.qwcss.ws.dto.VersionsDTO;

@Named("convertVersionSize")
public class ConverterFileVersionSize implements Converter {

	@Inject
	private FileVersionListService	fileVersionService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		String retorno = "";
		
		if (value != null) {
			
			try {
				BigDecimal size = new BigDecimal(value);
				retorno = Util.convertByte(size);
			} catch (Exception e) {
			}
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		String retorno = "";
		
		if (value != null) {
			try {
				BigDecimal size = (BigDecimal) value;
				retorno = Util.convertByte(size);
			} catch (Exception e) {
			}
		}

		return retorno;

	}
}