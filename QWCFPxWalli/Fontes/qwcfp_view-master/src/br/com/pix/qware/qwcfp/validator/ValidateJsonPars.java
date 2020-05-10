package br.com.pix.qware.qwcfp.validator;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.codehaus.jettison.json.JSONObject;
import org.primefaces.validate.ClientValidator;

import br.com.pix.qware.qwcfp.error.MsgTitleEnum;
import br.com.pix.qware.qwcfp.util.Validation;
import br.com.pix.qwcfp.ws.client.erro.ViewError;

@FacesValidator("validateJsonPars")
public class ValidateJsonPars implements Validator, ClientValidator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		try {
			JSONObject jsonStringParm = new JSONObject((String) arg2);
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(MsgTitleEnum.ERROR.getMsg(),
					ViewError.INVALID_JSON.getMsg());
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
		return "validateJson";
	}

}
