package br.com.pix.qware.qwcfp.beans;


import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("navegacao")
@ApplicationScoped
public class QwcssNavegacao implements Serializable{

	private static final long serialVersionUID = 1L;
		
		private String pagina = "pagina.xhtml";
	
		public void setPagina1(){
			setPagina("teste.xhtml");
		}
				
		public String getPagina(){
			return pagina;
		}
	
		public void setPagina(String pagina) {
			this.pagina = pagina;
		}
		
		public String gotoSecond() {
	        return "pm:second";
	    }
}
