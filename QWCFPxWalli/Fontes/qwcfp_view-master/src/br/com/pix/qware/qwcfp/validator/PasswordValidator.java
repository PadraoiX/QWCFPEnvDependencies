package br.com.pix.qware.qwcfp.validator;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

import br.com.pix.qware.qwcfp.error.MsgTitleEnum;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;

/*@ManagedBean
@ViewScoped*/
@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator, Serializable, ClientValidator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7104968304158779858L;


	@Override
	public void validate(FacesContext fc, UIComponent uic, Object propertyValue) throws ValidatorException {

		String confirmPassword = propertyValue.toString();

		UIInput uiInputConfirmPassword = (UIInput) uic.getAttributes().get("startComponent");
		String  password = (String) uiInputConfirmPassword.getValue();

		// Let required="true" do its job.
		if (confirmPassword == null || confirmPassword.isEmpty() || password == null || password.isEmpty() ) {
			return;
		}

		if (!confirmPassword.equals(password)) {
			FacesMessage msg = new FacesMessage(MsgTitleEnum.ERROR.getMsg(), ViewError.PASS_CONFIRM_INVALID.getMsg());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

		ConfigService configService = new ConfigService();
		ConfigDTO patternConfig = configService.carregar("PASS.POLICY", "SYSTEM");
		String pattern = null;

		if (patternConfig != null)
			if (patternConfig.getErrorCode() == 0)
				pattern = patternConfig.getValue();

		if (pattern == null) {
			pattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
		}

		if (!confirmPassword.matches(pattern)) {
			FacesMessage msg = new FacesMessage(MsgTitleEnum.ERROR.getMsg(), ViewError.PASS_INVALID.getMsg());
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
		return "passwordValidator";
	}

}