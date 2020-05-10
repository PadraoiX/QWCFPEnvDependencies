package br.com.pix.qware.qwcfp.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("menuView")
public class MenuView {
    
    public void download() {
        addMessage("Success", "Download Realizado!");
    }
     
    public void compartilhar() {
        addMessage("Success", "Compartilhamento Realizado!");
    }
     
    public void mover() {
        addMessage("Success", "Arquivo Movido!");
    }
    
    public void remover() {
        addMessage("Success", "Arquivo Removido!");
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}