package br.com.pix.qware.qwcfp.beans.uties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.pix.qware.qwcfp.util.Util;

@ManagedBean
@ViewScoped
public class PesquisaGrupoBean {
	
	private boolean main;
	
	private String filterValue;
	
	@PostConstruct
	public void init() {
		String pagRe = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		
		if(pagRe.lastIndexOf("main") > -1)
			setMain(true);
		else
			setMain(false);
	}
	
	public String redirectMain() {
		if(filterValue != null && !filterValue.isEmpty()){
			Util.setPropertySessao("pesquisaGrupo", filterValue);
			return "main?faces-redirect=true";
		}
		
		return null;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
	
	
	

}
