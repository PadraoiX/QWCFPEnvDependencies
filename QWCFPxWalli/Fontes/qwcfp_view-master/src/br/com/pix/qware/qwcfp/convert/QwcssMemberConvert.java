package br.com.pix.qware.qwcfp.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.MemberDTO;


@Named("memberConverter")
public class QwcssMemberConvert implements Converter {

	@Inject
	private MemberService	memberService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		MemberDTO membro = null;
		String retorno = "";
		
		if (value != null) {
			Integer id = Integer.parseInt(value);
			membro = memberService.listar(id);
			if(membro != null)
				retorno = membro.getMemberName();
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		try {
			
			MemberDTO member = null;

			if (value != null) {
				Integer code = ((Integer) value);
				String stringRetorno = "";
				member = memberService.listar(code);
				
				if(member != null){
					if(member.getErrorCode() == ViewError.OK.getCode()){
//						stringRetorno =  String.valueOf(member.getMemberId());
						stringRetorno =  member.getMemberName();
					}
				}
						
				return stringRetorno;
			}

		} catch (Exception e) {
		}

		return "";
	}
}