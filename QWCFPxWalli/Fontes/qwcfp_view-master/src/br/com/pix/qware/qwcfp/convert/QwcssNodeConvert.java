//package br.com.pix.qware.qwcfp.convert;
//
//import javax.faces.component.UIComponent;
//import javax.faces.context.FacesContext;
//import javax.faces.convert.Converter;
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import br.com.pix.qwcfp.api.model.QwcssNodeInformation;
//import br.com.pix.qwcfp.bl.business.NoBusiness;
//
//@Named("nodeConverter")
//public class QwcssNodeConvert implements Converter {
//
//	@Inject
//	private NoBusiness nodeService;
//
//	@Override
//	public Object getAsObject(FacesContext context, UIComponent component, String value) {
//
//		QwcssNodeInformation retorno = null;
//
//		if (value != null) {
//			Long id = Long.parseLong(value);
//			retorno = nodeService.carregar(id);
//		}
//
//		return retorno;
//	}
//
//	@Override
//	public String getAsString(FacesContext context, UIComponent component, Object value) {
//
//		if (value != null) {
//			Long code = ((QwcssNodeInformation) value).getNodeInformationId();
//			String stringRetorno = (code == null ? null : code.toString());
//			return stringRetorno;
//		}
//		return "";
//	}
//}