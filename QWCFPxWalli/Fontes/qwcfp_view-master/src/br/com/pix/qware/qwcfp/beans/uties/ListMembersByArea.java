package br.com.pix.qware.qwcfp.beans.uties;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AreaDTO;
import br.com.qwcss.ws.dto.MemberDTO;

@ManagedBean(name="listMembersByAreas")
@ViewScoped
public class ListMembersByArea extends AbstractBean{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 384314451423749517L;

	@Inject
	private MemberService memberSerivce;
	
	@Inject
	private AreaService areaService;
	
	private AreaDTO selectedArea;
	
	private  MemberDTO[] listMember;
	
	@PostConstruct
	public void init(){
		updateList();
	}
	
	public void updateList() {
		Integer areaDTO = (Integer) Util.getPropertySession("AREA_MEMBRO");
		if(areaDTO != null){
			AreaDTO[] tempArray = areaService.carregar(areaDTO);
			if (tempArray != null && tempArray[0].getErrorCode() == ViewError.OK.getCode()) {
				selectedArea = tempArray[0];
			}else{
				selectedArea = new AreaDTO();
			}
			
			MemberDTO[] membros = memberSerivce.listarMemberByArea(areaDTO);
			if(membros != null){
				if(membros[0].getErrorCode() == 0 )
					setListMember(membros);
			}
		}
	}

	public  MemberDTO[] getListMember() {
		return listMember;
	}

	public void setListMember( MemberDTO[] listMember) {
		this.listMember = listMember;
	}

	public AreaDTO getSelectedArea() {
		return selectedArea;
	}

	public void setSelectedArea(AreaDTO selectedArea) {
		this.selectedArea = selectedArea;
	}

}
