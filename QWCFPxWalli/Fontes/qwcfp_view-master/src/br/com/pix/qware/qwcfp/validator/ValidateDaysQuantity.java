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

@FacesValidator("validateDays")
public class ValidateDaysQuantity implements Validator, ClientValidator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object par)
			throws ValidatorException {

		FacesMessage msg = null;
		Integer versionLimit = 0;
		try {
			versionLimit = (Integer) par;
			
			
			if (versionLimit <= 0) {
				msg = new FacesMessage(MsgTitleEnum.ERROR.getMsg(),
						ViewError.DAYS_NUMBER_ZERO.getMsg());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		} catch (Exception e) {
			msg = new FacesMessage(MsgTitleEnum.ERROR.getMsg(),
					ViewError.DAYS_NUMBER_INVALID.getMsg());
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
		return "validateDays";
	}

}