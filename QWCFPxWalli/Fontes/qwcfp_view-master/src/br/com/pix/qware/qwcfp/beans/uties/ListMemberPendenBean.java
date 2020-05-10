package br.com.pix.qware.qwcfp.beans.uties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.pix.qwcfp.ws.client.manager.Member;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;

@ManagedBean(name = "listMemberPendBean")
@ViewScoped
public class ListMemberPendenBean extends AbstractBean  {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5525790234614316652L;
	
	@Inject
	private FacesMessages			messages;
	
	@Inject
	private MemberService			memberService;
	
	@Inject
	private NotifyAllMyGroupBean	concederPriv;

	private MemberDTO[]				memberPendente;
	
	@Inject
	private NotificationBean       notificationBean;
	
	private MemberDTO 			   member; 
	
	private String 					justificativa;

	@Override
	@PostConstruct
	public void init() {
		setMemberPendente(memberService.listarByStatus("CADASTRADO"));
	}
	
	public void recusarMembro(MemberDTO member) {
		
		LoginDTO loginDTO = null;
		
		if(member != null ){
			if(member.getLoginCpfId() != null){
				if( justificativa != null && !justificativa.isEmpty())
					loginDTO = memberService.inactive(member.getMemberId().toString(), justificativa);
				else
					loginDTO = memberService.inactive(member.getMemberId().toString(), "--");
			}
			
			if(loginDTO == null ){
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_FATAL);
				return;
			}
			
			if(loginDTO.getErrorCode() == 0){
				notificationBean.recusaoCadastro(member, justificativa);
				setMemberPendente(memberService.listarByStatus("CADASTRADO"));
				messages.add(ViewError.INVITED_MEMBER_RECURSE_SUCCESS.getCode() + " : " + ViewError.INVITED_MEMBER_RECURSE_SUCCESS.getMsg() , FacesMessage.SEVERITY_INFO);
			}else
				messages.add(loginDTO.getErrorCode() + ": " + loginDTO.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			
		}
		
		
		
	}

	public MemberDTO[] getMemberPendente() {
		return memberPendente;
	}

	public void setMemberPendente(MemberDTO[] memberPendente) {
		this.memberPendente = memberPendente;
	}
	
	public void altStatus(boolean ativar, MemberDTO membroAlterado) {
		if(membroAlterado != null){
			LoginDTO retDto;
			if (ativar){
				retDto = memberService.active(membroAlterado.getMemberId().toString(), justificativa);
				concederPriv.concederPrivAC(membroAlterado);				
			}
			else
				retDto = memberService.inactive(membroAlterado.getMemberId().toString(), "-");

			if (retDto.getErrorCode() != ViewError.OK.getCode()) {
				messages.add(retDto.getErrorCode() + ": " + retDto.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			} else {
				if (ativar){
					messages.add(ViewError.MEMBER_ATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
//					notificationBean.aprovacaoCadastro(membroAlterado);
				}else{
					messages.add(ViewError.MEMBER_INATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
					notificationBean.desaprovacaoCadastro(membroAlterado);
				}
			}
		}else 
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_INFO);

	
		
		setMemberPendente(memberService.listarByStatus("CADASTRADO"));
		
	}

	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		this.member = member;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

}
