package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.FlowEvent;

import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AreaDTO;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.MemberDTO;

/**
 * Bean da tela de cadastro de areas
 */
/*@Named("areasBean")
@RequestScoped*/
@ManagedBean(name="WSserviceBean")
@ViewScoped
public class QwcssWServiceBean extends AbstractBean {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Inject
	private FacesMessages		messages;

	@Inject
	private AreaService			areaService;

	@Inject
	private DomainService		domainService;
	
	@Inject
	private MemberService		memberService;
	
	@Inject
	private LoginBean	        loginBean;

	private AreaDTO				area;

	private List<AreaDTO>		areas;

	private AreaDTO[]			areasPendentes;

	private String				phoneNumber;

	private DomainDTO[]			listaStatus;
	
	private List<MemberDTO>		membros;
	
	private boolean				skip;
	
	private Integer				areaInativa;
	
	private Integer				areaAtiva;
	
	private MemberDTO			bossFk;
	
	private boolean disable;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		this.disable = true;
		updateAreas();
		listaStatus = domainService.returnDomain("STATUS_AREAS");
		areaInativa = areaInativa(listaStatus);
		areaAtiva = areaAtiva(listaStatus);
		
		getMembrosBoss();
		
	}
	
	public void getMembrosBoss(){
		MemberDTO[] membros = memberService.listar();
		
		if(membros != null && membros.length > 0){
			this.membros =  Arrays.asList(membros);
		}
	}
	
	public void updateAreas() {
		AreaDTO[] result  = areaService.listarAreas();
		AreaDTO firstMember = null;
		area = new AreaDTO();
		
		if (result != null) {
			firstMember = result[0];
		}
		if (firstMember != null && firstMember.getErrorCode() == ViewError.OK.getCode()) {
			setAreas(Arrays.asList(result));
		}else{
			setAreas( new ArrayList<AreaDTO>() );
		}
	}
	
	
	public String verMembros(AreaDTO area){
		if(area != null){
			Util.setPropertySessao("AREA_MEMBRO", area.getAreaId());
			return "listMembersByArea.faces?faces-redirect=true";
		}else{
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}
	
	private Integer areaAtiva(DomainDTO[] listaStatus2) {
		if(listaStatus != null && listaStatus.length > 0){
			for (int i = 0; i < listaStatus.length; i++) {
				if(listaStatus[i].getStringValue().equals("ATIVO")){
					return listaStatus[i].getId();
				}
			}	
		}
		return null;
	}
	
	private Integer areaInativa(DomainDTO[] listaStatus2) {
		if(listaStatus != null && listaStatus.length > 0){
			for (int i = 0; i < listaStatus.length; i++) {
				if(listaStatus[i].getStringValue().equals("Inativo")){
					return listaStatus[i].getId();
				}
			}	
		}
		return null;
	}
	
	
	public void ativarArea(AreaDTO area){
		if(area != null){
			area.setStatusId(areaAtiva);
			this.area = area;
			editar();
		}
	}
	
	public void inativarArea(AreaDTO area){
		if(area != null){
			area.setStatusId(areaInativa);
			this.area = area;
			editar();
		}
	}

	public void setarId(AreaDTO area) {
		if (area != null) {
			Util.setPropertySessao("AREA_ID", area);
		}
	}

	public String areaManaged(Integer id) {
		String s = "";

		if (id != null)
			s = areaService.carregar(id)[0] != null ? areaService.carregar(id)[0].getNome() : "";

		return s;
	}

	/**
	 * Cadastra ou atualiza uma area (depende do estado da flag 'alterar')
	 * 
	 * @return
	 */
	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; //reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}

	public String salvar() {
		try {

			String alias = area.getApelido();
			String description = area.getDescription();
			String name = area.getNome();
			
			String emailManager = null;
			String managerName  = null;
			String phoneManager = null;
			
			if(this.bossFk != null){
				emailManager = this.bossFk.getMemberEmail();
				managerName  = this.bossFk.getMemberName();
				phoneManager = this.bossFk.getNumberPhone1();
			}
			
			Integer subordinatedArea = null;
			if (area.getSubordinatedAreaId() != null && area.getSubordinatedAreaId() != 0)
				subordinatedArea = area.getSubordinatedAreaId();
			
			phoneManager = phoneManager.replaceAll("\\(", "");
			phoneManager = phoneManager.replaceAll("\\)", "");
			phoneManager = phoneManager.replaceAll("\\ ", "");
			phoneManager = phoneManager.replaceAll("\\-", "");

			AreaDTO areaDTO = areaService.inserir("ATIVO",
					alias,
					description,
					emailManager,
					managerName,
					name,
					phoneManager,
					subordinatedArea);

				if (areaDTO.getErrorCode() == 0) {
					messages.info(ViewError.AREA_INSERT_SUCESS.getMsg());
					
					FacesContext facesContext = FacesContext.getCurrentInstance();
					facesContext.getExternalContext().getFlash().setKeepMessages(true);
					facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "listArea");
					
					loginBean.updateGa();
					
					return "listArea.faces?redirect=true";
				} else {
					messages.add("Atenção! " + areaDTO.getErrorCode() + " " + areaDTO.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
					return null;
				}

		} catch (Exception e) {
			messages.error(ViewError.AREA_INSERT_ERROR.getCode() + ": " + ViewError.AREA_INSERT_ERROR.getMsg() + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Altera um registro cadastrado previamente no banco de dados
	 * 
	 * @return String outcome
	 */
	public String editar() {
		
		try {
			
			String alias = area.getApelido();
			String description = area.getDescription();
			String emailManager = area.getEmailManager();
			String managerName = area.getManagerName();
			String name = area.getNome();
			String phoneManager = area.getPhoneManager();
			Integer subordinatedArea = null;
			Integer statusIdFk = area.getStatusId();

			if (area.getSubordinatedAreaId() != null && area.getSubordinatedAreaId() != 0)
				subordinatedArea = area.getSubordinatedAreaId();

			Integer areaID = this.area.getAreaId();
			
			phoneManager = phoneManager.replaceAll("\\(", "");
			phoneManager = phoneManager.replaceAll("\\)", "");
			phoneManager = phoneManager.replaceAll("\\ ", "");
			phoneManager = phoneManager.replaceAll("\\-", "");

			AreaDTO areaDTO = areaService.alterar(areaID,domainService.returnDomain(statusIdFk).getStringValue(),
					alias,
					description,
					emailManager,
					managerName,
					name,
					phoneManager,
					subordinatedArea);

			updateAreas();
			
			if (areaDTO.getErrorCode() == 0)
				messages.info(ViewError.AREA_UPDATE_SUCESS.getMsg());
			else
				messages.add("Atenção " + areaDTO.getErrorCode() + " " + areaDTO.getErrorMsg(),FacesMessage.SEVERITY_ERROR);

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			messages.error(ViewError.AREA_UPDATE_ERROR.getCode() + ": " + ViewError.AREA_UPDATE_ERROR.getMsg() + e.getMessage());
			return null;
		}
	}

	public Integer getAreaAtiva() {
		return areaAtiva;
	}


	public void setAreaAtiva(Integer areaAtiva) {
		this.areaAtiva = areaAtiva;
	}


	public AreaDTO getArea() {
		return area;
	}

	public void setArea(AreaDTO area) {
		this.area = area;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public DomainDTO[] getListaStatus() {
		return listaStatus;
	}

	public void setListaStatus(DomainDTO[] listaStatus) {
		this.listaStatus = listaStatus;
	}

	public AreaDTO[] getAreasPendentes() {
		return areasPendentes;
	}

	public void setAreasPendentes(AreaDTO[] areasPendentes) {
		this.areasPendentes = areasPendentes;
	}


	public Integer getAreaInativa() {
		return areaInativa;
	}


	public void setAreaInativa(Integer areaInativa) {
		this.areaInativa = areaInativa;
	}

	public List<AreaDTO> getAreas() {
		return areas;
	}

	public void setAreas(List<AreaDTO> areas) {
		this.areas = areas;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public  List<MemberDTO>	getMembros() {
		return membros;
	}

	public void setMembros( List<MemberDTO>	membros) {
		this.membros = membros;
	}

	public MemberDTO getBossFk() {
		return bossFk;
	}

	public void setBossFk(MemberDTO bossFk) {
		this.bossFk = bossFk;
	}



}
