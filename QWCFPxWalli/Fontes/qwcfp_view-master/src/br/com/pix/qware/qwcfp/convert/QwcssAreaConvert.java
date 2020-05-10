package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.qwcss.ws.dto.AreaDTO;



@Named("areaConverter")
public class QwcssAreaConvert implements Converter {

	@Inject
	private AreaService areaService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		AreaDTO[] retorno = null;

		if (value != null) {
			Integer id = Integer.parseInt(value);
			retorno = areaService.carregar(id);
			
			if(retorno != null && retorno.length > 0){
				return retorno[0].getNome();
			}
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		AreaDTO[] retorno = null;

		if (value != null) {
			Integer code =  ((Integer) value);
			retorno = areaService.carregar(code);
			if(retorno != null && retorno.length > 0){
				return retorno[0].getNome();
			}
			
		}
		
		return "";
	}
}