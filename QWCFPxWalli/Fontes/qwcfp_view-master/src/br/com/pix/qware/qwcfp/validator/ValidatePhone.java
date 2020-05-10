package br.com.pix.qware.qwcfp.validator;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

import br.com.pix.qware.qwcfp.error.MsgTitleEnum;
import br.com.pix.qwcfp.ws.client.erro.ViewError;

@FacesValidator("validatePhone")
public class ValidatePhone implements Validator, ClientValidator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object par)
			throws ValidatorException {

		String phone = (String) par;
		
		if (phone != null) {
			if (!phone.trim().equals("")){
				phone = phone.replace("-", "");
				phone = phone.replace("(", "");
				phone = phone.replace(")", "");
				phone = phone.replace(" ", "");
				phone = phone.replace("+", "");
			}
		}

		if (phone.length() <= 0) {
			FacesMessage msg = new FacesMessage(MsgTitleEnum.ERROR.getMsg(),
					ViewError.PHONE_INVALID.getMsg());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "validateEmail";
	}

}
