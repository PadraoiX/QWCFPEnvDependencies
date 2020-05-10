package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.cliapi.QWException.QWException;
import br.com.pix.qware.qwcfp.beans.uties.NotifyAllMyGroupBean;
import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.MailService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;
import br.com.pix.qwcfp.ws.client.connection.WsUrls;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.pix.qwcfp.ws.client.notifie.Notificate;
import br.com.qwcss.ws.PrivilegioDTO;
import br.com.qwcss.ws.dto.AreaDTO;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.ListPrivsDTO;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@Named("memberBean")
@RequestScoped
public class QwcssMemberBean extends AbstractBean {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	@Inject
	private MemberService			memberService;

	@Inject
	private LoginBean				loginBean;

	@Inject
	private DomainService			domainService;

	@Inject
	private AreaService				areaService;

	@Inject
	private PrivilegiosService		privService;

	@Inject
	private MailService				mailService;

	@Inject
	private FacesMessages			messages;
	
	@Inject
	private NotifyAllMyGroupBean	concederPriv;
	
	@Inject
	private NotifyService			notificate;
	
	private MemberDTO				member;

	private boolean					usuarioLogado;

	private boolean					gi;

	private String					usuario;

	private String					pass;

	private String					passOld;

	private String					phoneNumber;

	private String					code;

	private List<String>			codigoPais;

	private String					valueHasMap;

	private String					mailTo;

	private String					mailSubject;

	private String					mailTxt;
	
	private List<PrivilegioDTO>		privMember;
	
	private boolean 				authenticationUserLdap;
	
	Integer ativado = null; 
	Integer inativado = null;
	Integer cadastrado =  null;
	
	private boolean 				isLdapAthentication;
	
	private String 					elementosProcessar;
	
	private String 					elementosProcessarInterno;

	@Inject
	private InformationGroupService	groupService;
	
	
	@PostConstruct
	@Override
	public void init(){
		member = new MemberDTO();
		this.elementosProcessar = loginBean.isAuthenticationLdap() ? "MEMBER_USER MEMBER_CPF AREA_ID_FK MEMBER_PASS3 MEMBER_LDAP MEMBER_MAIL countryCode telefone MEMBER_ADDRESS MEMBER_CEP MEMBER_JUSTIFY" : "MEMBER_USER MEMBER_CPF MEMBER_PASS MEMBER_PASS2 MEMBER_MAIL countryCode telefone MEMBER_ADDRESS MEMBER_CEP MEMBER_JUSTIFY";
		this.elementosProcessarInterno = "MEMBER_USER MEMBER_CPF MEMBER_PASS MEMBER_PASS2 MEMBER_MAIL countryCode telefone MEMBER_ADDRESS MEMBER_CEP MEMBER_BOSS MEMBER_AREA";
	}
	
	public String managerGroup(Integer id) {
		String s = "";
		if (id != null)
			s = memberService.listar(id).getMemberName();

		return s;
	}
	
	public String clear() {
		return null;
	}

	public void setarId(Integer id) {
		Util.setPropertySessao("OVER_CREATOR", id);
	}
	
	public void populatePriv(Integer memberId){
		setPrivMember(null);
		if(memberId != null){
			Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");
			
			if (idGroup != null) {

				GroupDTO group = groupService.getGroupById(idGroup);
				
				if(group != null){
					ListPrivsDTO privs = privService.listar(group.getApelido(), memberId);
					if(privs != null)
						if (privs.getListPriv() != null)
							setPrivMember(Arrays.asList(privs.getListPriv()));
				}
			}
		}
		
	}
	
	public void sendNotificationArea(){
		if(this.member != null){
			String memberName 			= this.member.getMemberName();
			String memberEmail 			= this.member.getMemberEmail();
			String telefone 			= this.valueHasMap != null ? this.valueHasMap + this.member.getNumberPhone1() : this.member.getNumberPhone1();
			String memberEmailPersonal	= this.member.getMemberEmailPersonal();
			String justificativa 		= this.member.getJustificativa();
			
			String messageText = "%s está propondo a criação de uma nova área com o nome %s. Segue as informações do usuário:\n Email: %s\nTelefone: %s\nJustificativa: %s";
			messageText = String.format(messageText, memberName, memberEmailPersonal, memberEmail, telefone, justificativa);
			notificate.notificateSuggestArea(loginBean.getUsuario(), loginBean.getPass(), "1", messageText);
			messages.add(ViewError.SUGGEST_AREA_SUCCESS.getMsg(), FacesMessage.SEVERITY_INFO);
		}else
			messages.add(ViewError.SUGGEST_AREA_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
	}
	
	
	public String checkLdap(){
		if(authenticationUserLdap){
			this.elementosProcessar = "MEMBER_USER MEMBER_CPF MEMBER_LDAP MEMBER_MAIL countryCode telefone MEMBER_ADDRESS MEMBER_CEP MEMBER_JUSTIFY";
		}else{
			this.elementosProcessar = "MEMBER_USER MEMBER_CPF MEMBER_PASS MEMBER_PASS2 MEMBER_MAIL countryCode telefone MEMBER_ADDRESS MEMBER_CEP MEMBER_JUSTIFY";
		}
		
		return this.elementosProcessar;
	}
	
	public String checkLdapInternal(){
		if(authenticationUserLdap){
			this.elementosProcessarInterno = "MEMBER_USER MEMBER_CPF MEMBER_LDAP MEMBER_MAIL countryCode telefone MEMBER_ADDRESS MEMBER_CEP MEMBER_BOSS MEMBER_JUSTIFICATIVA MEMBER_AREA";
		}else{
			this.elementosProcessarInterno = "MEMBER_USER MEMBER_CPF MEMBER_PASS MEMBER_PASS2 MEMBER_MAIL countryCode telefone MEMBER_ADDRESS MEMBER_CEP MEMBER_BOSS MEMBER_JUSTIFICATIVA MEMBER_AREA";
		}
		
		return this.elementosProcessarInterno;
	}
	
	
	public boolean isGg() {
		return isGg(loginBean.getMember() != null ? loginBean.getMember().getMemberId() : null );
	}

	public boolean isGg(Integer memberId) {

		Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");

		if (idGroup != null) {

			GroupDTO group = groupService.getGroupById(idGroup);

			if (group.getManagerGroup().compareTo(memberId) == 0) {
				return true;
			} else {

				ListPrivsDTO privs = privService.listar(group.getApelido(), memberId);
				DomainDTO domaingestor = domainService.returnDomain("PRIVILEGIOS", "GESTOR-GRP");

				if (privs != null) {

					br.com.qwcss.ws.PrivilegioDTO[] privsList = privs.getListPriv();
					if (privsList != null) {
						for (int i = 0; i < privsList.length; i++) {
							if (privsList[i].getPrivTypeStringValue().equalsIgnoreCase(domaingestor.getStringValue())) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;

	}
	
	
	public void sendMail(){
		sendMail(member.getMemberEmail());
	}

	public void sendMail(String mail) {

		String[] mailToArray = { mail };//{ member.getMemberEmail() };

		if (mailToArray.length > 0) {
			SimpleDTO ret = mailService.sendMail(mailToArray, mailSubject, mailTxt);

			if (ret.getErrorCode() != 0) {
				messages.add(ret.getErrorCode() + ": " + ret.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
			} else {
				messages.add(ViewError.EMAIL_SENDER_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			}
		}

	}



	public String prepararMembro() {
		member = new MemberDTO();
		return "addUsuraio.faces";
	}

	/**
	 * recupera a senha do usuario.
	 */
	public void recuperarSenha() {
		MemberService serv = new MemberService();
		SimpleDTO retDto;
		retDto = serv.trocarSenha(member.getMemberEmail());

		if (retDto.getErrorCode().compareTo(ViewError.OK.getCode()) != 0) {
			messages.add(retDto.getErrorCode() + " - " + retDto.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
		} else
			messages.info(ViewError.MEMBER_PASS_SENDER.getMsg());
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
					passwd = "";
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
				String memberOperation = member.getMemberId().toString();

				String areaAlias = null;
				AreaDTO areaDTO = null;
				if (this.member.getAreaIdFk() != null){
					areaDTO = areaService.carregar(this.member.getAreaIdFk())[0];
					loginBean.getMember().setAreaIdFk(this.member.getAreaIdFk());
				}
				
				
				String bossMail = null;
				if (areaDTO != null && areaDTO.getErrorCode() == 0){
					areaAlias = areaDTO.getApelido();
					bossMail = areaDTO.getEmailManager();
				}

				String justify = member.getJustificativa() !=  null? member.getJustificativa(): "";
				
				String ldapKeyForCorpAuth = member.getLdapKeyForCorpAuth();
				

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
						ldapKeyForCorpAuth);

				if (retorno.getErrorCode() == 0) {

					messages.info(ViewError.MEMBER_UPDATE_SUCESS.getMsg());

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

	public void editProfile() {
		member = loginBean.getMember();

		editar(false);
	}

	/**
	 * Abre a tela de edição de Member
	 * 
	 * @param QwcssMember
	 * @return
	 */
	public String alterarSenha() {

		try {

			Integer idMember = (Integer) Util.getPropertySession("ID_MEMBER");

			if (pass == null || passOld == null) {
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
						+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
				return null;
			}

			MemberDTO member = memberService.listar(idMember);
			
			String passwd = null;

			if (member != null) {
				
				if (member.getErrorCode() != ViewError.OK.getCode()) {
					messages.add(member.getErrorMsg() + ": " + member.getErrorCode(), FacesMessage.SEVERITY_ERROR);
					return null;
				}

				String senhaBD = member.getQwarePasswordEnc();

				try {
					
					if(senhaBD != null && !senhaBD.isEmpty() ){
						if (senhaBD.equals(QWCrypto.getInstance().encryptPassInternal(passOld)))
							passwd = pass;
						else {
							messages.add(ViewError.MEMBER_OLD_PASS_NOT_MATCH.getCode() + ": "
									+ ViewError.MEMBER_OLD_PASS_NOT_MATCH.getMsg(),
									FacesMessage.SEVERITY_ERROR);
							return null;
						}
					}else if(member.getLdapKeyForCorpAuth() != null && !member.getLdapKeyForCorpAuth().isEmpty() )
						passwd = pass;
						


				} catch (QWException e) {
					e.printStackTrace();
				}

				String memberCpf = member.getLoginCpfId();

				String name = null;
				if (member.getMemberName() != null && !member.getMemberName().trim().equals(""))
					name = member.getMemberName();

				String mail = null;
				if (member.getMemberEmail() != null)
					mail = member.getMemberEmail();

				String personalMail = member.getMemberEmailPersonal();
				
				if (personalMail != null && personalMail.length() <= 0) {
					personalMail = null;
				}

				String ddd = "00";
				String phone = member.getNumberPhone1();
				String cep = member.getZipCode();
				String address = member.getPhisicalAddress();
				String memberOperation = member.getMemberId().toString();

				String areaAlias = null;
				AreaDTO areaDTO = null;
				if (this.member.getAreaIdFk() != null){
					areaDTO = areaService.carregar(this.member.getAreaIdFk())[0];
					loginBean.getMember().setAreaIdFk(this.member.getAreaIdFk());
				}
				
				
				String bossMail = null;
				if (areaDTO != null && areaDTO.getErrorCode() == 0){
					areaAlias = areaDTO.getApelido();
					bossMail = areaDTO.getEmailManager();
				}
				
				String justify = member.getJustificativa();
				
				String ldapKeyForCorpAuth = member.getLdapKeyForCorpAuth();

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
						ldapKeyForCorpAuth);

				ConnectedUser loggedUser;
				if (retorno.getErrorCode() == 0){
					loggedUser = loginBean.getLoggedUser();
					loginBean.setPass(passwd);
					loggedUser.setSenhaUser(passwd);
					messages.info(ViewError.MEMBER_PASS_UPDATE_SUCESS.getMsg());
				}else
					messages.add(retorno.getErrorCode() + " " + retorno.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);

				return null;
			} else {
				messages.add(ViewError.USR_NOT_FOUND_ERR.getCode() + ": "
						+ ViewError.USR_NOT_FOUND_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.MEMBER_PASS_UPDATE_ERROR.getCode() + ": "
					+ ViewError.MEMBER_PASS_UPDATE_ERROR.getMsg() + " " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}

	/*
	 * public void mudarStatus(Long memberId, Boolean inativar,String summary){
	 * memberService.mudarStatusMember(memberId, inativar);
	 * System.out.println("ooooooooooooooooooooooooooooooooooooooooooooooo"); FacesMessage message = new
	 * FacesMessage(FacesMessage.SEVERITY_INFO, summary, null); FacesContext.getCurrentInstance().addMessage(null,
	 * message);
	 * 
	 * }
	 */

	// public String mudarStatus(Long memberId, Boolean inativar) {
	// memberService.mudarStatusMember(memberId, inativar);
	// System.out.println("ooooooooooooooooooooooooooooooooooooooooooooo");
	// member = new QwcssMember();
	// members = memberService.listarMembers();
	//
	// FacesMessage msg = new FacesMessage("Operao",
	// "realizada com sucesso!");
	// FacesContext.getCurrentInstance().addMessage(null, msg);
	// return null;
	//
	// }

	

	public String getValueHasMap() {

		Set<String> chaves = null;
		HashMap<String, String> properties = Util.countryCode;

		if (properties == null)
			return null;

		chaves = properties.keySet();
		for (String key : chaves) {
			if (key.equals(code))
				valueHasMap = properties.get(key);
		}

		return valueHasMap;
	}

	public String mask() {
		String mask = "";

		if (valueHasMap != null) {
			if (valueHasMap.trim().length() == 4)
				mask = "+9999";
			if (valueHasMap.trim().length() == 3)
				mask = "+999";
			if (valueHasMap.trim().length() == 2)
				mask = "+99";
			if (valueHasMap.trim().length() == 1)
				mask = "+9";
		}
		
		

		return mask;
	}
	
	/**
	 * Cadastra ou atualiza um Member (depende do estado da flag 'alterar')
	 * 
	 * @return
	 */
	public String salvar(boolean externo) {
		return salvar(externo, false);
	}

	public String salvar(boolean externo, boolean isOperatorGi) {

		try {
			String passwd = null;
			String phone = null;

			String name = member.getMemberName();

			String cpf = member.getLoginCpfId();

			String codigoTel = "";
			if (Util.countryCode != null) {
				codigoTel = Util.countryCode.get(code);
			}
			
			String ddd = "";
			
			if (codigoTel != null) {
				ddd = codigoTel.replace("\\n", "").replace("\\t", "").trim();;
			}
			
//			if(!authenticationUserLdap)
			passwd = member.getQwarePasswordEnc();
			
			phone = member.getNumberPhone1().trim();

			String mail = member.getMemberEmail().trim();
			
			String personalMail = member.getMemberEmailPersonal();
			if (personalMail != null && personalMail.length() <= 0) {
				personalMail = null;
			}

			String cep = member.getZipCode();
			String address = member.getPhisicalAddress();

			String areaAlias = null;
			AreaDTO areaDTO = null;
			if (member.getAreaIdFk() != null){
				String usucario = (String) Util.getPropertySession("USER_LDAP");
				String senha = (String) Util.getPropertySession("PASS_LDAP");
				if(usucario != null && senha != null){
					senha = QWCrypto.getInstance().decryptPassInternal(new String(senha));					
					areaDTO = areaService.carregar(usucario, senha, member.getAreaIdFk())[0];
				}else{
					if (!externo) {
						areaDTO = areaService.carregar(member.getAreaIdFk())[0];
					}
				}
			}

			String bossMail = null;
			if (areaDTO != null && areaDTO.getErrorCode() == 0){
				areaAlias = areaDTO.getApelido();
				bossMail = areaDTO.getEmailManager();
			}

			String justify = member.getJustificativa();
			
			LoginDTO retorno = null;
			
			String ldapKeyForCorpAuth = member.getLdapKeyForCorpAuth();
			
			if (externo) {
				retorno = memberService.insertExterno(name,
						cpf,
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
						ldapKeyForCorpAuth);
				
			} else {

				retorno = memberService.insert(name,
						cpf,
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
						ldapKeyForCorpAuth);
				
				

			}


			if (retorno != null) {
				if (retorno.getErrorCode() == 0) {
					if (!externo) {
						MemberDTO insertedMember = memberService.listar(member.getMemberEmail());
						concederPriv.concederPrivAC(insertedMember);
					}
				} else {
					messages.add(retorno.getErrorCode() + " " + retorno.getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
					return null;
				}
			} else {
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
				return null;
			}

			if (externo) {
				messages.info(ViewError.MEMBER_INSERT_SUCESS.getMsg());
				return null;
			} else {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				facesContext.getExternalContext().getFlash().setKeepMessages(true);
				facesContext.getApplication().getNavigationHandler()
						.handleNavigation(facesContext, null, "listUser");
				return "listUser.faces?redirect=true";
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.MEMBER_INSERT_ERROR.getCode() + ": "	+ ViewError.MEMBER_INSERT_ERROR.getMsg() + " " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}

	public String isMemberGi(String mail) {

		SimpleDTO ret = privService.memberIsGi(mail);
		setGi(ret.getErrorCode().compareTo(ViewError.OK.getCode()) == 0 ? true : false);
		return isGi() ? "Gestor" : "";
	}

	public boolean isMemberGiInternal(String mail) {
		SimpleDTO ret = privService.memberIsGi(mail);
		return ret.getErrorCode().compareTo(ViewError.OK.getCode()) == 0 ? true : false;
	}

	public boolean revogarGi() {

		if (member != null) {
			SimpleDTO ret = privService.removeGiPriv(member.getMemberId());

			if (ret.getErrorCode().compareTo(ViewError.OK.getCode()) != 0) {
				messages.add(ret.getErrorCode() + ": " + ret.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
				return false;
			} else {
				messages.add(ViewError.PRIV_DELETE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
				return true;
			}
		} else {
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
					+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
			return false;
		}
	}

	public void removeBean(String bean) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(bean);
	}

	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		isMemberGi(member.getLoginCpfId());
		this.member = member;
	}

	public boolean isUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean isGi() {
		return gi;
	}

	public void setGi(boolean gi) {
		this.gi = gi;
	}


	public String getPassOld() {
		return passOld;
	}

	public void setPassOld(String passOld) {
		this.passOld = passOld;
	}

	public List<String> getCodigoPais() {
		HashMap<String, String> proporties = Util.countryCode;
		Set<String> chaves = null;
		codigoPais = new ArrayList<String>();
		if (proporties != null) {
			chaves = proporties.keySet();
			for (String key : chaves) {
				codigoPais.add(key);
			}
		}
		return codigoPais;
	}

	public void setCodigoPais(List<String> codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setValueHasMap(String valueHasMap) {
		this.valueHasMap = valueHasMap;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailTxt() {
		return mailTxt;
	}

	public void setMailTxt(String mailTxt) {
		this.mailTxt = mailTxt;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public List<PrivilegioDTO> getPrivMember() {
		return privMember;
	}

	public void setPrivMember(List<PrivilegioDTO> privMember) {
		this.privMember = privMember;
	}

	public boolean isLdapAthentication() {
		return isLdapAthentication;
	}

	public void setLdapAthentication(boolean isLdapAthentication) {
		this.isLdapAthentication = isLdapAthentication;
	}
	
	public boolean isAuthenticationUserLdap() {
		return authenticationUserLdap;
	}

	public void setAuthenticationUserLdap(boolean authenticationUserLdap) {
		this.authenticationUserLdap = authenticationUserLdap;
	}
	
	
	public String getElementosProcessar() {
		return elementosProcessar;
	}

	public void setElementosProcessar(String elementosProcessar) {
		this.elementosProcessar = elementosProcessar;
	}

	public String getElementosProcessarInterno() {
		return elementosProcessarInterno;
	}

	public void setElementosProcessarInterno(String elementosProcessarInterno) {
		this.elementosProcessarInterno = elementosProcessarInterno;
	}


}