package br.com.pix.qware.qwcfp.beans.uties;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.cliapi.QWException.QWException;
import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AreaDTO;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;

@ManagedBean(name = "listMemberBean")
@ViewScoped
public class ListMembersBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1863057387212611243L;

	@Inject
	private FacesMessages		messages;

	@Inject
	private MemberService		memberService;
	
	@Inject
	private LoginBean				loginBean;

	@Inject
	private NotificationBean	notificationBean;
	
	@Inject
	private NotifyAllMyGroupBean concederPrivAC;

	private List<MemberDTO>			members;
	
	@Inject
	private DomainService			domainService;

	@Inject
	private AreaService				areaService;

	private MemberDTO			member;
	
	private MemberDTO			memberSelected;
	
	private boolean renderAtiva;
	private boolean renderDesativa;
	
	private Integer ativado = null;
	
	private Integer inativado = null;
			
	private Integer cadastrado =  null;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		updateMembers();
		setRenderAtiva( false );    
		setRenderDesativa( false ); 
	}
	
	public void render() {

		ativado = domainService.returnDomain("STATUS_MEMBERS", "ATIVO").getId();
		inativado = domainService.returnDomain("STATUS_MEMBERS", "INATIVO").getId();
		cadastrado = domainService.returnDomain("STATUS_MEMBERS", "CADASTRADO").getId();
		MemberDTO membroAlterado = this.member;

		if (membroAlterado != null && membroAlterado.getStatusMemberDomFk() != null) {
			
			if (membroAlterado.getStatusMemberDomFk().compareTo(inativado) == 0 || membroAlterado.getStatusMemberDomFk().compareTo(cadastrado) == 0) {
				setRenderAtiva(loginBean.isGi());
				setRenderDesativa(false);
			} else if (membroAlterado.getStatusMemberDomFk().compareTo(ativado) == 0) {
				setRenderDesativa(loginBean.isGi());
				setRenderAtiva(false);
			} else {
				setRenderAtiva(false);
				setRenderDesativa(false);
			}
		} else {
			setRenderAtiva(false);
			setRenderDesativa(false);
		}

	}

	/**
	 * Ativa ou inativa o membro
	 * 
	 * @param ativar
	 *            true ativa, false inativa.
	 * @param membroAlterado
	 *            o membro que ser ativado.
	 */
	public void altStatus(boolean ativar) {
		MemberDTO membroAlterado = this.member;
		if (membroAlterado != null) {
			LoginDTO retDto;
			if (ativar){
				retDto = memberService.active( membroAlterado.getMemberId().toString(), "-");
				concederPrivAC.concederPrivAC(membroAlterado);
				if (retDto.getErrorCode() != ViewError.OK.getCode())
					messages.add(retDto.getErrorCode() + ": " + retDto.getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
				else{
					notificationBean.aprovacaoCadastro(membroAlterado);
					messages.add(ViewError.MEMBER_ATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);	
				}
			}else{
				retDto = memberService.inactive(membroAlterado.getMemberId().toString(), "-");
				if (retDto.getErrorCode() != ViewError.OK.getCode())
					messages.add(retDto.getErrorCode() + ": " + retDto.getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
				else{
					notificationBean.desaprovacaoCadastro(membroAlterado);
					messages.add(ViewError.MEMBER_INATIVE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
				}
			}
		} else
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "	+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_INFO);

		updateMembers();

	}
	
	/**
	 * decide a renderizao do componente de ativao de usurio.
	 * 
	 * @param button
	 *            identificador do butao
	 * @param membroAlterado
	 *            membro a ser alterado.
	 * @return true, renderizado; false, no renderizado.
	 */
	public boolean altStatusVis(Integer button) {

		ativado = domainService.returnDomain("STATUS_MEMBERS", "ATIVO").getId(); 
		inativado = domainService.returnDomain("STATUS_MEMBERS", "INATIVO").getId();
		cadastrado =  domainService.returnDomain("STATUS_MEMBERS", "CADASTRADO").getId();
		MemberDTO membroAlterado = this.member;
		
		if(membroAlterado == null){
			return false;
		}
		
		if(membroAlterado.getStatusMemberDomFk() != null){
			switch (button) {
				case 0://Ativar
					if (membroAlterado.getStatusMemberDomFk().compareTo(inativado) == 0	|| membroAlterado.getStatusMemberDomFk().compareTo(cadastrado) == 0) {
						return true && loginBean.isGi();
					}
					break;
				case 1: //Desativar
					if (membroAlterado.getStatusMemberDomFk().compareTo(ativado) == 0) {
						return true && loginBean.isGi();
					}
					break;
				default:
					return false;
			}
		}

		return false;
	}
	
	public void updateMembers() {
		MemberDTO[] result = memberService.listar();
		MemberDTO firstMember = null;
		
		if (result != null) {
			firstMember = result[0];
		}
		if (firstMember != null && firstMember.getErrorCode() == ViewError.OK.getCode()) {
			setMembers(Arrays.asList(result));
		}else{
			setMembers( new ArrayList<MemberDTO>() );
		}
		
	}
	
	/**
	 * Abre a tela de edição de Member
	 * 
	 * @param QwcssMember
	 * @return
	 */
	public String editar(boolean isOperatorGi) {

		try {
			if (member != null) {
				String memberCpf = member.getLoginCpfId();

				String name = null;
				if (member.getMemberName() != null && !member.getMemberName().trim().equals(""))
					name = member.getMemberName();

				String passwd = null;
				if (member.getQwarePasswordEnc() != null) {
					try {
						passwd = QWCrypto.getInstance()
								.decryptPassInternal(member.getQwarePasswordEnc());
					} catch (QWException e1) {
					}
				} else {
					passwd = null;
				}

				String mail = null;
				if (member.getMemberEmail() != null)
					mail = member.getMemberEmail();

				String personalMail = member.getMemberEmailPersonal();
				if (personalMail != null && personalMail.length() <= 0) {
					personalMail = null;
				}
				
				String ddd = "00";

				if (member.getAreaCodePhone1() != null && member.getAreaCodePhone1() > 0)
					ddd = String.valueOf(member.getAreaCodePhone1());

				String phone = member.getNumberPhone1();
				String cep = member.getZipCode();
				String address = member.getPhisicalAddress();
				String memberOperation =  member.getMemberId().toString(); 
				
				String areaAlias = null;
				if (member.getAreaIdFk() != null) {
					AreaDTO[] area = areaService.carregar(member.getAreaIdFk());
					if (area != null)
						areaAlias = area[0].getApelido();
				}

				String bossMail = null;
				if (member.getBossIdFk() != null) {
					if (member.getBossIdFk().compareTo(0) != 0) {
						MemberDTO boss = memberService.listar(member.getBossIdFk());
						bossMail = boss.getMemberEmail();
					}
				}

				String justify = member.getJustificativa() !=  null? member.getJustificativa(): "";
				

				String ldapKeyForCorpAuth = member.getLdapKeyForCorpAuth();
				
				passwd = ldapKeyForCorpAuth != null && !ldapKeyForCorpAuth.isEmpty() ? null : passwd;
				
				LoginDTO retorno = memberService.update(memberOperation,
						name,
						memberCpf,
						passwd,
						mail,
						personalMail,
						ddd,
						phone,
						cep,
						address,
						areaAlias,
						bossMail,
						justify,
						ldapKeyForCorpAuth );

				if (retorno.getErrorCode() == 0) {

					messages.info(ViewError.MEMBER_UPDATE_SUCESS.getMsg());
					updateMembers();

				} else
					messages.add(retorno.getErrorCode() + " " + retorno.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);

			}

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.MEMBER_UPDATE_ERROR.getMsg() + " " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}
		return null;
	}

	public List<MemberDTO> getMembers() {
		return members;
	}

	public void setMembers(List<MemberDTO> members) {
		this.members = members;
	}

	public MemberDTO getMemberSelected() {
		return memberSelected;
	}

	public void setMemberSelected(MemberDTO memberSelected) {
		this.memberSelected = memberSelected;
	}

	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		this.member = member;
	}

	public boolean isRenderAtiva() {
		return renderAtiva;
	}

	public void setRenderAtiva(boolean renderAtiva) {
		this.renderAtiva = renderAtiva;
	}

	public boolean isRenderDesativa() {
		return renderDesativa;
	}

	public void setRenderDesativa(boolean renderDesativa) {
		this.renderDesativa = renderDesativa;
	}


}
