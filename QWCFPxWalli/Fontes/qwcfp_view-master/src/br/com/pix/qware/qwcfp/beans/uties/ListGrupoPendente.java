package br.com.pix.qware.qwcfp.beans.uties;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name="groupPendente")
@ViewScoped
public class ListGrupoPendente extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 2408795418711028370L;

	@Inject
	private InformationGroupService	groupService;
	
	
	@Inject
	private FacesMessages			messages;
	
	@Inject
	private NotificationBean		notificationBean;
	
	private List<GroupDTO>			gruposPendentes;
	
	private List<GroupDTO>			selectedGroups;
	
	private String 					justificativa;
	
	

	@PostConstruct
	public void init() {
		updateGruposPendentes();
		justificativa = new String();
	}
	
	private void updateGruposPendentes() {
		GroupDTO[] gruposPendentes = groupService.getGroupPendentes();
		if(gruposPendentes != null)
			this.gruposPendentes = Arrays.asList(gruposPendentes);
		else
			this.gruposPendentes = null;
	}

	public List<GroupDTO> getGruposPendentes() {
		return gruposPendentes;
	}

	public void setGruposPendentes(List<GroupDTO> gruposPendentes) {
		this.gruposPendentes = gruposPendentes;
	}

	public void recursarGrupo(){
		if (selectedGroups == null || selectedGroups.isEmpty()) {
			messages.add(ViewError.GROUP_NOT_CHOOSE.getCode() + ": " + ViewError.GROUP_NOT_CHOOSE.getMsg(), FacesMessage.SEVERITY_FATAL);
			return;
		}

		SimpleDTO retorno = null;
		
		for(GroupDTO grupoPendente : selectedGroups){
			if (justificativa != null && !justificativa.isEmpty()) {
					retorno = groupService.delete(grupoPendente.getGroupId(), justificativa);
			}else
				retorno = groupService.delete(grupoPendente.getGroupId(), "--");

			if (retorno == null) {
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() +": " + ViewError.WEBSERVICE_OFF_ERR.getMsg() ,	FacesMessage.SEVERITY_ERROR);
				return;
			}
			
			if (retorno.getErrorCode() == ViewError.OK.getCode()){
				messages.add(ViewError.GRUPO_RECUSADO_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
				notificationBean.recusaoGrupo(grupoPendente);
			}else
				messages.add(retorno.getErrorCode() + " " + retorno.getErrorMsg(),	FacesMessage.SEVERITY_ERROR);	
		}
		
		updateGruposPendentes();
		
	}
	
	public void ativarGrupo() {
		if (selectedGroups == null || selectedGroups.isEmpty()) {
			messages.add(ViewError.GROUP_NOT_CHOOSE.getCode() + ": " + ViewError.GROUP_NOT_CHOOSE.getMsg(), FacesMessage.SEVERITY_FATAL);
			return;
		}
		
		for(GroupDTO grupoPendente : selectedGroups){
			SimpleDTO retorno = null;
			if (justificativa != null && !justificativa.isEmpty()) {
				retorno = groupService.active(grupoPendente.getGroupId(), justificativa);
			} else
				retorno = groupService.active(grupoPendente.getGroupId(), "--");

			if (retorno == null) {
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() +": " + ViewError.WEBSERVICE_OFF_ERR.getMsg() ,	FacesMessage.SEVERITY_ERROR);
				return;
			}
			
			if (retorno.getErrorCode() == ViewError.OK.getCode()){
				messages.add(ViewError.GRUPO_ATIVADO_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
				notificationBean.aprovacaoGrupo(grupoPendente);
			}else
				messages.add(retorno.getErrorCode() + " " + retorno.getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
	
		}

		updateGruposPendentes();
	}

	public List<GroupDTO> getSelectedGroups() {
		return selectedGroups;
	}

	public void setSelectedGroups(List<GroupDTO> selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

}