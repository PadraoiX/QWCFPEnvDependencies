package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.qwcss.ws.dto.VersionsDTO;

@Named("converterFileComment")
public class ConverterFileComment implements Converter {

	@Inject
	private FileVersionListService fileVersionService;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		String retorno = "";

		if (value != null) {
			Integer id = Integer.parseInt(value);

			VersionsDTO[] versions = fileVersionService.listar(id);
			if (versions != null) {
				if (versions[0].getErrorCode() == 0)
					retorno = versions[0].getAddInformation();
			}
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		try {

			if (value != null) {
				Integer code = ((Integer) value);
				String stringRetorno = "";

				VersionsDTO[] versions = fileVersionService.listar(code);
				if (versions != null) {
					if (versions[0].getErrorCode() == 0)
						stringRetorno = versions[0].getAddInformation();
				}

				return stringRetorno;
			}

		} catch (Exception e) {
		}

		return "";

	}

}
