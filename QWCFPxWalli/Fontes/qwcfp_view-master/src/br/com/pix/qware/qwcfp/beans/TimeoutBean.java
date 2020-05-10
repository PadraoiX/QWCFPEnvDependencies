package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class TimeoutBean {
	
	public void onIdele(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sem Atividades", "O Sistema voltará a tela de login!"));
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.faces");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public void onActive(){
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Bem vindo", "Sistema em atividade!"));
	 }

}
