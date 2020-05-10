package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DomainDTO;



@Named ("converterDomain")
public class QwcssDomainConvert implements Converter {

	@Inject
	private DomainService domainService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,	String value) {
		
		DomainDTO retorno = null;
		
		if (value != null) {
			Integer id = Integer.parseInt(value);
				retorno = domainService.returnDomain(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,	Object value) {
		
		try {
			
			DomainDTO domainDTO =null;

			if (value != null) {
				Integer code = ((Integer) value);
				String stringRetorno = "";
				
				domainDTO = domainService.returnDomain(code);
				
				if(domainDTO != null){
					if(domainDTO.getErrorCode() == ViewError.OK.getCode())
						stringRetorno = domainDTO.getDescription();
				}
				
				return stringRetorno;
			}
			
		} catch (Exception e) {}
		
		return "";
		
	}
}