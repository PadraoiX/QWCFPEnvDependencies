package br.com.pix.qware.qwcfp.util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import br.com.pix.qware.qwcfp.error.MsgTitleEnum;



public class FacesMessages implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public void add(String message, Severity severity){
		FacesContext context = FacesContext.getCurrentInstance();
		
		String title = "";
		if (severity.equals(FacesMessage.SEVERITY_INFO)) {
			title = MsgTitleEnum.SUCESS.getMsg();
		}else if (severity.equals(FacesMessage.SEVERITY_ERROR)) {
			title = MsgTitleEnum.ERROR.getMsg();
		}
		
		FacesMessage msg = new FacesMessage(title, message);
		msg.setSeverity(severity);
		
		context.addMessage(null, msg);
		
	}
	
	public void info(String message){
		add(message, FacesMessage.SEVERITY_INFO);
	}
	
	public void error(String message){
		add(message, FacesMessage.SEVERITY_ERROR);
	}
	
	
	
	
}
