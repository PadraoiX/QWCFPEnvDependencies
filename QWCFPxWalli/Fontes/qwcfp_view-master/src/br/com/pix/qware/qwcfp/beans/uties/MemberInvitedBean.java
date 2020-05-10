package br.com.pix.qware.qwcfp.beans.uties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.SendInvitationsService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "memberInvitedBean")
@ViewScoped
public class MemberInvitedBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3549743451400098964L;

	@Inject
	private FacesMessages 			messages;

	@Inject
	private MemberService 			memberService;

	@Inject
	private SendInvitationsService 	sendInvitationService; 

	private MemberDTO 				member;

	private String 					code;

	private String 					emails;
	
	private String					valueHasMap;
	
	private List<String>			codigoPais;
	
	private String					pass;
	
	private String 					token;
	
	private boolean					validToken; 
	
	private String 					userId;
	
	@PostConstruct
	@Override
	public void init(){
		member = new MemberDTO();
		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String tokenTemp = req.getParameter("token");
		SimpleDTO simpleObj = sendInvitationService.checkValidToken(tokenTemp);
		validToken = simpleObj.getErrorCode() == 0? true:false;
	}


	public void send() {
		
		Integer grupoInvitedId = (Integer) Util.getPropertySession("ID_GROUP");
		
		SimpleDTO ret = sendInvitationService.sendInvitations(emails, String.valueOf(grupoInvitedId), "");
		
		if(ret != null){
			if(ret.getErrorCode() == 0){
				messages.add(ViewError.EMAIL_SENDER_SUCESS.getCode() + ": "
						+ ViewError.EMAIL_SENDER_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			}else{
				messages.add(
						ret.getErrorCode() + ": " + ret.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
			}
		}else{
			messages.add(
					ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(),
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public String salvar() {

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
				ddd = codigoTel.replace("\\n", "").replace("\\t", "").trim();
			}
			passwd = member.getQwarePasswordEnc();

			phone = member.getNumberPhone1().trim();

			String mail = member.getMemberEmail().trim();

			String personalMail = member.getMemberEmailPersonal();
			if (personalMail == null || personalMail.isEmpty()) {
				personalMail = null;
			}

			String cep = member.getZipCode();
			String address = member.getPhisicalAddress();
			
			String areaAlias =  null;//areaService.carregar(grupoSelected.getAreaId())[0].getApelido();

			String bossCpf = null;//grupoSelected.getManagerGroup().toString();
			String justify = "INVITED";

			LoginDTO retorno = null;
			
			String ldapKeyForCorpAuth = member.getLdapKeyForCorpAuth();

			retorno = memberService.insertInvited(name, cpf, passwd, mail, personalMail, ddd, phone, cep, address, areaAlias, bossCpf, justify, token, ldapKeyForCorpAuth);

			if (retorno != null) {
				if (retorno.getErrorCode() == 0) {
					messages.add(ViewError.MEMBER_INSERT_EXT_SUCESS.getCode() + ": " + ViewError.MEMBER_INSERT_EXT_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
					FacesContext context = FacesContext.getCurrentInstance();
					context.getExternalContext().getFlash().setKeepMessages(true);
					
					return "index.faces?faces-redirect=true";
							
				} else {
					messages.add(retorno.getErrorCode() + " " + retorno.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
					return null;
				}
			} else {
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": " + ViewError.WEBSERVICE_OFF_ERR.getMsg(),
						FacesMessage.SEVERITY_ERROR);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.MEMBER_INSERT_ERROR.getCode() + ": " + ViewError.MEMBER_INSERT_ERROR.getMsg() + " "
					+ e.getMessage(), FacesMessage.SEVERITY_ERROR);
			return null;
		}
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


	public String getEmails() {
		return emails;
	}


	public void setEmails(String emails) {
		this.emails = emails;
	}


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


	public void setValueHasMap(String valueHasMap) {
		this.valueHasMap = valueHasMap;
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


	public MemberDTO getMember() {
		return member;
	}


	public void setMember(MemberDTO member) {
		this.member = member;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getPass() {
		return pass;
	}


	public void setPass(String pass) {
		this.pass = pass;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public boolean isValidToken() {
		return validToken;
	}


	public void setValidToken(boolean validToken) {
		this.validToken = validToken;
	}
	
	
	

}
