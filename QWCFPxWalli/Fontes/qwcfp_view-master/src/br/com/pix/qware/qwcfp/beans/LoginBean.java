package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.cliapi.QWException.QWException;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.connection.ConnectUser;
import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.PrivilegioDTO;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.ListPrivsDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.MyGroupsEXDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@Named("loginBean")
@SessionScoped
public class LoginBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Integer MESSAGE_TIME_DURATION_DFT = 800;

	private ConnectedUser loggedUser;

	private MemberDTO member;

	@Inject
	private MemberService memberService;

	@Inject
	private FacesMessages message;

	@Inject
	private InformationGroupService groupService;

	@Inject
	private DomainService domainService;

	@Inject
	private PrivilegiosService PrivService;

	@Inject
	private ConfigService configService;

	private boolean authenticationLdap;
	private boolean usuarioAutenticouComLdap;

	private String pass;

	private String usuario;

	private boolean isGi;

	private boolean isInvited;

	private boolean ga;

	private boolean cpfRequired;

	private String urlQvdtApp;

	private String teamName;

	private boolean dashBoardCfg;
	
	private boolean isIFrame;
	
	private Integer messageTime;

	public LoginBean() {
		FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(60 * 10);
	}

	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(60 * 10);
		super.init();

		ConfigDTO authenticationConfigLdap = configService.carregar("LDAP.AUTHENTICATION", "SYSTEM");
		this.authenticationLdap = false;

		if (authenticationConfigLdap != null && authenticationConfigLdap.getErrorCode() == 0) {
			this.authenticationLdap = authenticationConfigLdap.getValue().equals("1");
		}

		ConfigDTO qwvdtApp = configService.carregar("URLAPP", "QWVDT");

		if (qwvdtApp != null & qwvdtApp.getErrorCode() == 0) {
			setUrlQvdtApp(qwvdtApp.getValue());
		}

		ConfigDTO teamName = configService.carregar("TEAM_NAME", "SYSTEM");

		if (teamName != null & teamName.getErrorCode() == 0) {
			setTeamName(teamName.getValue());
		}
		
		ConfigDTO msgTime = configService.carregar("MESSAGE_TIME_MILSECS", "SYSTEM_PROP");
		
		if (msgTime != null & msgTime.getErrorCode() == 0) {
			try {
				messageTime =  Integer.valueOf(msgTime.getValue());
			} catch (Exception e) {
				messageTime = MESSAGE_TIME_DURATION_DFT;
			}
		}else {
			messageTime = MESSAGE_TIME_DURATION_DFT;
		}

		ConfigDTO cpf = configService.carregar("CPF", "SYSTEM");
		setCpfRequired(false);
		if (cpf != null && cpf.getErrorCode().equals(0)) {
			if (cpf.getValue().trim().equalsIgnoreCase("1")) {
				setCpfRequired(true);
			}
		}
		
		isIFrame = false;
	}

	public void validSession() {
		FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(0);
	}

	public void invalidSession() {
		FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(60 * 10);
	}

	public void install() {
		ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ex.redirect(ex.getRequestContextPath() + "/formInstall.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza o logout do sistema
	 */
	public void logout() {
		ConnectUser.disconnect(loggedUser.getLoginUser(), loggedUser.getSenhaUser(), loggedUser.getLoginKey());
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.invalidateSession();
		try {
			ec.redirect(ec.getRequestContextPath() + "/index.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// session.invalidate();
	}

	/**
	 * Se o usurio logado for gestor de grupo retorna true.
	 * 
	 * @return
	 */
	public boolean isGg() {
		Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");

		if (isGi())
			return true;

		if (idGroup != null) {
			GroupDTO grupoObj = groupService.getGroupById(idGroup);
			if (grupoObj != null) {
				MyGroupsEXDTO[] list = memberService.listarByGroupView(grupoObj.getApelido());

				if (list != null) {
					for (int i = 0; i < list.length; i++) {
						if (list[i].getGroupId().equals(idGroup) && list[i].getMemberId().equals(member.getMemberId())
								&& list[i].getPrivStringValue().contains("GESTOR-GRP"))
							return true;
					}
				}
			}
		}

		return false;
	}

	public boolean connectLdap() {
		SimpleDTO simpleDTO = ConnectUser.connectLdap(usuario, pass);
		if (simpleDTO != null) {
			if (simpleDTO.getErrorCode() == 0) {
				try {
					Util.setPropertySessao("USER_LDAP", usuario);
					Util.setPropertySessao("PASS_LDAP", QWCrypto.getInstance().encryptPassInternal(pass));
				} catch (QWException e) {
				}

				return true;
			} else {
				ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
				HttpServletResponse response = (HttpServletResponse) context.getResponse();
				response.setStatus(500);
			}
		}
		message.add(ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
		return false;
	}

	public String conectar() {

		try {

			String tipo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("hidden1");

			// loggedUser = ConnectUser.connect(formatUser(usuario), pass);
			loggedUser = ConnectUser.connect(usuario, pass);

			if (loggedUser != null) {

				if (loggedUser.getLoginDto().getErrorCode() != ViewError.OK.getCode()) {

					if (loggedUser.getLoginDto().getErrorCode() != 90043) {// Membro inativo.
						message.add("Erro " + loggedUser.getLoginDto().getErrorCode() + ": "
								+ loggedUser.getLoginDto().getErrorMsg(), FacesMessage.SEVERITY_ERROR);
						return null;
					} else {
						message.error(ViewError.MEMBER_AUTH.getMsg());
						return null;
					}
				}

				Util.setPropertySessao("USER_LDAP", usuario);
				Util.setPropertySessao("PASS_LDAP", QWCrypto.getInstance().encryptPassInternal(pass));

				member = memberService.listar(loggedUser.getLoginDto().getMemberEmail());
				setUsuarioAutenticouComLdap(member.getLdapKeyForCorpAuth() != null);

				if (member != null) {
					if (member.getErrorCode() != ViewError.OK.getCode()) {
						message.add("Erro " + member.getErrorCode() + ": " + member.getErrorMsg(),
								FacesMessage.SEVERITY_ERROR);
						return null;
					}

					DomainDTO statusMember = domainService.returnDomain(member.getStatusMemberDomFk());
					DomainDTO domainAtivo = domainService.returnDomain("STATUS_MEMBERS", "ATIVO");

					if (statusMember != null && domainAtivo != null) {
						String stringValueAtivo = domainAtivo.getStringValue();
						String stringValue = statusMember.getStringValue();
						String status = statusMember.getDescription();

						if (!stringValueAtivo.equals(stringValue)) {
							message.add("Usuário " + status + " " + ViewError.INFRA_CONTACT.getCode(),
									FacesMessage.SEVERITY_WARN);
							return null;
						}

					}

					SimpleDTO simpleDto = PrivService.memberIsGi();

					if (simpleDto != null) {
						isGi = simpleDto.getErrorCode().compareTo(0) == 0 ? true : false;
					} else {
						isGi = false;
					}

					SimpleDTO simpleDtoInv = PrivService.isInvited(member.getMemberId());

					if (simpleDtoInv != null) {
						isInvited = simpleDtoInv.getErrorCode().compareTo(0) == 0 ? true : false;
					} else {
						isInvited = false;
					}
					
					ConfigDTO dashBoardCfg = configService.carregar("DASHBOARD", "SYSTEM");
					this.dashBoardCfg = false;

					if (dashBoardCfg != null && dashBoardCfg.getErrorCode().equals(0)) {
						if (dashBoardCfg.getValue().trim().equalsIgnoreCase("1")) {
							this.dashBoardCfg = true;
						}
					}

					updateGa();
					/*
					 * if(isAuthenticationLdap()) Util.setPropertySessao("PASS_KEY", pass);
					 */

					Util.setPropertySessao("ID_MEMBER", member.getMemberId());
					// return tipo;
					return "main.faces?faces-redirect=true";
				}

			}

			message.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(),
					FacesMessage.SEVERITY_ERROR);
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			message.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(),
					FacesMessage.SEVERITY_ERROR);
			return null;
		}

	}

	public void updateGa() {
		ga = false;
		ListPrivsDTO listaPrivsArea = PrivService.returnPrivArea();
		if (listaPrivsArea != null && listaPrivsArea.getErrorCode() == 0) {
			PrivilegioDTO[] array = listaPrivsArea.getListPriv();
			if (array != null) {
				for (PrivilegioDTO privilegioDTO : array) {
					if (privilegioDTO.getPrivTypeStringValue().equalsIgnoreCase("GESTOR-ARE")) {
						ga = true;
					}
				}
			}
		}
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		this.member = member;
	}

	public ConnectedUser getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(ConnectedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	public void reset() {
		RequestContext.getCurrentInstance().reset("form:panel");
		System.out.println("form resetado!!!!");
	}

	public void resetFail() {
		this.usuario = null;
		this.pass = null;

		FacesMessage msg = new FacesMessage("Model reset, but it won't work properly.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("form resetado!!!!");
	}

	/*
	 * private static String formatUser(String user) {
	 * 
	 * if (user != null) { if (Validation.isCPFValido(user)) { String formated =
	 * user.replace(".", ""); formated = formated.replace("-", ""); return formated;
	 * } } return user; }
	 */

	public void setGi(boolean isGi) {
		this.isGi = isGi;
	}

	public boolean isUsuarioAutenticouComLdap() {
		return usuarioAutenticouComLdap;
	}

	public void setUsuarioAutenticouComLdap(boolean usuarioAutenticouComLdap) {
		this.usuarioAutenticouComLdap = usuarioAutenticouComLdap;
	}

	public Boolean isGi() {
		return isGi;
	}

	public boolean isInvited() {
		return isInvited;
	}

	public void setInvited(boolean isInvited) {
		this.isInvited = isInvited;
	}

	public boolean isCpfRequired() {
		return cpfRequired;
	}

	public void setCpfRequired(boolean cpfRequired) {
		this.cpfRequired = cpfRequired;
	}

	public boolean isAuthenticationLdap() {
		return authenticationLdap;
	}

	public void setAuthenticationLdap(boolean authenticationLdap) {
		this.authenticationLdap = authenticationLdap;
	}

	public boolean isGa() {
		return ga;
	}

	public void setGa(boolean ga) {
		this.ga = ga;
	}

	public String getUrlQvdtApp() {
		return urlQvdtApp;
	}

	public void setUrlQvdtApp(String urlQvdtApp) {
		this.urlQvdtApp = urlQvdtApp;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public boolean isDashBoardCfg() {
		return dashBoardCfg;
	}

	public void setDashBoardCfg(boolean dashBoardCfg) {
		this.dashBoardCfg = dashBoardCfg;
	}

	public boolean isIFrame() {
		return isIFrame;
	}

	public void setIFrame(boolean isIFrame) {
		this.isIFrame = isIFrame;
	}

	public Integer getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(Integer messageTime) {
		this.messageTime = messageTime;
	}

}