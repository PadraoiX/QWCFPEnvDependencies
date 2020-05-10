package br.com.pix.qware.qwcfp.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AreaDTO;


@Named("areaConfigBean")
@RequestScoped
public class AreaConfigBean extends AbstractBean{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7051079019995847674L;
	

	@Inject
	private AreaService areaService;
	
	@Inject
	FacesMessages					messages;
	
	private AreaDTO area;
	
	@PostConstruct
	public void init(){
		Integer areaId = (Integer) Util.getPropertySession("AREA_RAIZ_ID");
		
		AreaDTO[] areaDTO = areaService.carregar(areaId);
		if(areaDTO != null && areaDTO.length > 0){
			if(areaDTO[0] != null && areaDTO[0].getErrorCode() == 0){
				area = areaDTO[0];
			}else{
				messages.add( areaDTO[0].getErrorCode() + ": " + areaDTO[0].getErrorMsg() , FacesMessage.SEVERITY_ERROR);	
			}
		}else{
			messages.add( ViewError.AREA_ERROR_LOAD.getCode() + ": " + ViewError.AREA_ERROR_LOAD.getMsg() , FacesMessage.SEVERITY_ERROR);
		}
		
	}


	public AreaDTO getArea() {
		return area;
	}


	public void setArea(AreaDTO area) {
		this.area = area;
	}
	
	
	

	
	
	
}
