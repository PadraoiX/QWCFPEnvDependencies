package br.com.pix.qware.qwcfp.beans.uties;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.pix.qwcfp.ws.client.manager.Member;
import br.com.qwcss.ws.InvitationDTO;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "listInvitedBean")
@ViewScoped
public class ListInvitedMembersBean extends AbstractBean  {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5525790234614316652L;
	
	@Inject
	private FacesMessages			messages;
	
	@Inject
	private MemberService			memberService;
	
	private List<InvitationDTO>			invitedList;
	
	private InvitationDTO 			   invitation; 
	
	private String[]				privSelect;
	
	private Integer					qtdDays;
	
	private String 					justificativa;

	@Override
	@PostConstruct
	public void init() {
		updateList();
	}
	
	public void updateList() {

		Integer groupId  = (Integer) Util.getPropertySession("ID_GROUP");
		
		if (groupId != null) {
			setInvitedList(memberService.listarMembrosConvidados(groupId));
		}else{
			setInvitedList(new ArrayList<InvitationDTO>());
		}
	}
	
	public void refusedInvitedMember() {
	
		Integer groupId  = (Integer) Util.getPropertySession("ID_GROUP");
		
		if (groupId != null) {
			memberService.refuseInvitedMember(groupId, invitation.getEmail(), justificativa);
		}else{
			messages.add(ViewError.GROUP_NOT_FOUND.getMsg(), FacesMessage.SEVERITY_INFO);
		}
	}

	
	public void activate(boolean ativar) {
		
		Integer groupId  = (Integer) Util.getPropertySession("ID_GROUP");
		
		if (groupId != null) {
			if(invitation != null){
				SimpleDTO retDto = new SimpleDTO();
				if (ativar){
					retDto = memberService.activateInvitedMember(groupId, invitation.getEmail(), qtdDays, privSelect);
				}
				else{
//					retDto = memberService.inactive(membroAlterado.getLoginCpfId(), "-");
				}
				
				if (retDto.getErrorCode() != ViewError.OK.getCode()) {
					messages.add(retDto.getErrorCode() + ": " + retDto.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				} else {
					if (ativar){
						messages.add(ViewError.MEMBER_ATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
					}else{
						messages.add(ViewError.MEMBER_INATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
					}
				}
			}else 
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_FATAL);
		}
	
		
	}

	public InvitationDTO getInvitation() {
		return invitation;
	}

	public void setInvitation(InvitationDTO invitation) {
		this.invitation = invitation;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public List<InvitationDTO> getInvitedList() {
		return invitedList;
	}

	public void setInvitedList(List<InvitationDTO> arrayList) {
		this.invitedList = arrayList;
	}

	public String[] getPrivSelect() {
		return privSelect;
	}

	public void setPrivSelect(String[] privSelect) {
		this.privSelect = privSelect;
	}

	public Integer getQtdDays() {
		return qtdDays;
	}

	public void setQtdDays(Integer qtdDays) {
		this.qtdDays = qtdDays;
	}

}
