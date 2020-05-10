package br.com.pix.qware.qwcfp.beans.uties;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.NotifyRequestService;
import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.NotifieDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "notiAreaGIBean")
@ViewScoped
public class NotifyAreaGIBean extends AbstractBean  {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6025686352916901833L;
	
	@Inject
	private NotifyRequestService	notifyReqService;
	
	@Inject
	private NotifyService 			notifyService;
	
	@Inject
	private FacesMessages			messages;
	
	@Inject
	private MemberService			memberService;
	
	@Inject
	private PrivilegiosService		privService;
	
	@Inject
	private DomainService			domainService;
	
	private NotifieDTO[]			notifiesArea;
	
	private Integer[]				privSelect;
	
	private DomainDTO[]				domainPriv;
	
	private NotifieDTO				notification;
	
	private String					justificativa;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		updateNotifiesArea();
		updatePriv();
		
	}
	
	private void updatePriv(){
		domainPriv = new DomainDTO[5];
		domainPriv[0] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-GRP");
		domainPriv[1] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-ARE");
		domainPriv[2] = domainService.returnDomain("PRIVILEGIOS", "NEW-GRP");
		domainPriv[3] = domainService.returnDomain("PRIVILEGIOS", "REPL-GRP");
		domainPriv[4] = domainService.returnDomain("PRIVILEGIOS", "DOWN-GRP");
	}
	
	private void updateNotifiesArea(){
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.set(Calendar.YEAR, 2009);
		dataInicio.set(Calendar.MONTH, Calendar.JANUARY);
		dataInicio.set(Calendar.DAY_OF_MONTH, 1);
		Calendar dataFim = Calendar.getInstance();
		dataFim.set(Calendar.YEAR, 2090);
		dataFim.set(Calendar.MONTH, Calendar.JANUARY);
		dataFim.set(Calendar.DAY_OF_MONTH, 1);
		setNotifiesArea(notifyReqService.listarAreaInfra(dataInicio, dataFim, true));
	}
	
	public String refuseAreaNotify(Integer areaId) {
		if (areaId != null && areaId > 0) {
			SimpleDTO dto = notifyService.inativeNotification(areaId, justificativa);
			
			if (dto != null ) {
				if (dto.getErrorCode()  != ViewError.OK.getCode()) {
					messages.error(ViewError.NOTIFICATION_INATIVE_ERROR.getMsg());
				}else{
					messages.info(ViewError.NOTIFICATION_INATIVE_SUCESS.getMsg());
				}
			}else{
				messages.error(ViewError.NOTIFICATION_INATIVE_ERROR.getMsg());
			}
		}
		updateNotifiesArea();
		
		return null;
	}

	
	public String concederPriv() {
		if (privSelect != null && privSelect.length != 0 && notification != null) {
			if (notification.getCreatorMemberId() != null && notification.getAreaScopeId() != null
					&& notification.getQtdDias() != null) {
				
				if (notification.getQtdDias() > 0) {
					
					MemberDTO member = memberService.listar(notification.getCreatorMemberId());
					String nome = "";
					
					if (member != null) {
						if (member.getErrorCode() == ViewError.OK.getCode())
							nome = member.getMemberName();
						else {
							messages.add(member.getErrorCode() + ": " + member.getErrorMsg(),
									FacesMessage.SEVERITY_ERROR);
							return null;
						}
					} else {
						messages.add(ViewError.USR_NOT_FOUND_ERR.getCode() + ": "
								+ ViewError.USR_NOT_FOUND_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
						return null;
					}
					
					String[] privilegios = new String[privSelect.length];
					String[] privilegiosRecusados = new String[]{};
					for (int i = 0; i < privSelect.length; i++) {
						DomainDTO dom = domainService.returnDomain(privSelect[i]);
						if (dom.getErrorCode() == 0) {
							
							SimpleDTO retorno = privService.insertPrivArea(notification
									.getCreatorMemberId(),
									notification.getAreaScopeId(),
									dom.getStringValue(),
									notification.getQtdDias());
							
							if (retorno != null) {
								if (retorno.getErrorCode() == ViewError.OK.getCode()) {
									privilegios[i] = dom.getDescription();
								}else{
									privilegiosRecusados[privilegiosRecusados.length] = dom.getDescription();
								}
							}
						}
						
					}
					
					
					String formatedPrivs = Util.formatPrivileges(privilegios);
					messages.add("atribuido(s) o(s) privilégio(s) de: " + formatedPrivs + " ao usuário "
							+ nome + " por " + notification.getQtdDias() + " dias", FacesMessage.SEVERITY_INFO);
					
					if (privilegiosRecusados.length > 0) {
						formatedPrivs = Util.formatPrivileges(privilegiosRecusados);
						messages.add("privilégio(s) de: " + formatedPrivs + " não atribuídos ao usuário "
								+ nome, FacesMessage.SEVERITY_ERROR);
					}
				}else{
					messages.add(ViewError.DAYS_NUMBER_ZERO.getCode() + ": "
							+ ViewError.DAYS_NUMBER_ZERO.getMsg(),
							FacesMessage.SEVERITY_WARN);	
				}
			} else
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
						+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
		} else
			messages.add(ViewError.PRIV_NOT_FOUND.getCode() + ": "
					+ ViewError.PRIV_NOT_FOUND.getMsg(),
					FacesMessage.SEVERITY_ERROR);
		
		
		privSelect = null;
		updateNotifiesArea();
		return null;

	}
	
	
	public NotifieDTO[] getNotifiesArea() {
		return notifiesArea;
	}

	public void setNotifiesArea(NotifieDTO[] notifiesArea) {
		this.notifiesArea = notifiesArea;
	}

	public PrivilegiosService getPrivService() {
		return privService;
	}

	public void setPrivService(PrivilegiosService privService) {
		this.privService = privService;
	}

	public NotifieDTO getNotification() {
		return notification;
	}

	public void setNotification(NotifieDTO notification) {
		this.notification = notification;
	}

	public DomainDTO[] getDomainPriv() {
		return domainPriv;
	}

	public void setDomainPriv(DomainDTO[] domainPriv) {
		this.domainPriv = domainPriv;
	}

	public Integer[] getPrivSelect() {
		return privSelect;
	}

	public void setPrivSelect(Integer[] privSelect) {
		this.privSelect = privSelect;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
	
}
