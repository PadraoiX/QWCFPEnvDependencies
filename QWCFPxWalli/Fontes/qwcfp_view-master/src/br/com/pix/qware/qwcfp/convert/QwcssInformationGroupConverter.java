package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.qwcss.ws.dto.GroupDTO;

@Named("groupConverter")
public class QwcssInformationGroupConverter implements Converter {

	@Inject
	private InformationGroupService	groupService;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {

		GroupDTO retorno = null;

		if (value != null) {
			Integer id = Integer.parseInt(value);
			retorno = groupService.getGroupById(id);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		GroupDTO retorno = null;
		if (value != null) {
			Integer code = ((Integer) value);
			retorno = groupService.getGroupById(code);
			if (retorno != null) {
				return retorno.getNome();
			}
		}

		return "";
	}

}
