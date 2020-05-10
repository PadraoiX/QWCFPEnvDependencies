package br.com.pix.qware.qwcfp.beans.uties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.UserScopeNodeDTO;

@ManagedBean(name="listGroupsBean")
@ViewScoped
public class ListGroupsBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 2408795418711028370L;
	
	@Inject
	private FacesMessages			messages;

	@Inject
	private InformationGroupService	groupService;
	
	@Inject
	private LoginBean	loginBean;
	
	@Inject
	private DomainService			domainService;

	private List<GroupDTO>				groups;
	
	private GroupDTO				group;
	
	private Integer ativado = null;
	
	private Integer inativado = null;
	
	private Integer  cadastrado = null;
	
	private boolean disable; 

	@PostConstruct
	public void init() {
		
		disable = true;
		updateList();
		
	}
	
	
	public void copyGroup(String operation) {
		if (group != null) {
			Util.setPropertySessao("ADD_GROUP_OPERATION", operation);
			try {
				Util.setPropertySessao("ID_GROUP_SUBNOVO", group.getGroupId());

				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect(ec.getRequestContextPath() + "/addGrupo.faces");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Ativa ou inativa o grupo
	 * 
	 * @param ativar
	 *            true ativa, false inativa.
	 * @param grupoAlterado
	 *            o grupo que ser ativado.
	 */
	public void altStatus(boolean ativar, GroupDTO grupoAlterado) {
		if (grupoAlterado != null) {
			SimpleDTO retDto;
			if (ativar){
				retDto = groupService.active(grupoAlterado.getGroupId(), "-");
				if (retDto.getErrorCode() != ViewError.OK.getCode())
					messages.add(retDto.getErrorCode() + ": " + retDto.getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
				else{
					messages.add(ViewError.GROUP_ATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);	
				}
			}else{
				retDto = groupService.inactive(grupoAlterado.getGroupId(), "-");
				if (retDto.getErrorCode() != ViewError.OK.getCode())
					messages.add(retDto.getErrorCode() + ": " + retDto.getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
				else{
					messages.add(ViewError.GROUP_INATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
				}
			}
		} else
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "	+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_INFO);

		updateList();
	}
	
	private void updateList() {
		GroupDTO[] tempGroups = groupService.listar();
		GroupDTO firstGroup = null;
		
		if (tempGroups != null) {
			firstGroup = tempGroups[0];
		}
		if (firstGroup != null && firstGroup.getErrorCode() == ViewError.OK.getCode()) {
			setGroups(Arrays.asList(tempGroups));
		}else{
			setGroups( new ArrayList<GroupDTO>() );
		}
	}
	
	/**
	 * decide a renderizao do componente de ativao de grupo.
	 * 
	 * @param button
	 *            identificador do butao
	 * @param grupoAlterado
	 *            grupo a ser alterado.
	 * @return true, renderizado; false, no renderizado.
	 */
	public boolean altStatusVis(Integer button, GroupDTO grupoAlterado) {
		
		if (grupoAlterado != null) {
			if(grupoAlterado.getStatus() != null && loginBean.isGi()){
				ativado = domainService.returnDomain("STATUS_GI", "ATIVO").getId();
				inativado = domainService.returnDomain("STATUS_GI", "INATIVO").getId();
				cadastrado = domainService.returnDomain("STATUS_GI", "CADASTRADO").getId();
				
				switch (button) {
					case 0://Ativar
						if (grupoAlterado.getStatus().compareTo(inativado) == 0	|| grupoAlterado.getStatus().compareTo(cadastrado) == 0) {
							return true;
						}
						break;
					case 1: //Desativar
						if (grupoAlterado.getStatus().compareTo(ativado) == 0) {
							return true;
						}
						break;
					default:
						return false;
				}
			}
		}

		return false;
	}

	public List<GroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}


	public GroupDTO getGroup() {
		return group;
	}


	public void setGroup(GroupDTO group) {
		this.group = group;
	}


	public boolean isDisable() {
		return disable;
	}


	public void setDisable(boolean disable) {
		this.disable = disable;
	}

}
