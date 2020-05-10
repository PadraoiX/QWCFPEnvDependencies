package br.com.pix.qware.qwcfp.beans.uties;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.QwcssNotifyBean;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.NotifyRequestService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.NotifieDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "notiGroupBean")
@ViewScoped
public class NotifyGroupBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 7830406090805859145L;

	@Inject
	private FacesMessages			messages;

	@Inject
	private NotifyRequestService	notifyRequestService;

	@Inject
	private InformationGroupService	groupService;

	@Inject
	private PrivilegiosService		privService;
	
	@Inject
	private NotificationBean		notificationBean;
	
	@Inject
	private  MemberService			memberService;
	
	@Inject
	private QwcssNotifyBean			notiBean;

	private NotifieDTO[]			notifyMyGroup;

	private String[]				privSelect;

	private NotifieDTO				notification;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		updateNotifyMyGroup();
		setNotification(new NotifieDTO());
	}

	private void updateNotifyMyGroup() {
		Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");
		setNotifyMyGroup(null);

		if (idGroup != null) {
			Calendar dataInicio = Calendar.getInstance();
			dataInicio.set(Calendar.YEAR, 2009);
			dataInicio.set(Calendar.MONTH, Calendar.JANUARY);
			dataInicio.set(Calendar.DAY_OF_MONTH, 1);

			Calendar dataFim = Calendar.getInstance();
			dataFim.set(Calendar.YEAR, 2090);
			dataFim.set(Calendar.MONTH, Calendar.JANUARY);
			dataFim.set(Calendar.DAY_OF_MONTH, 1);
			notifyMyGroup = notifyRequestService.listarGrupo(idGroup, dataInicio, dataFim);
		}
	}
	
	private void updateNotifyMyGroup(Integer idGroup) {

		if (idGroup != null) {
			Calendar dataInicio = Calendar.getInstance();
			dataInicio.set(Calendar.YEAR, 2009);
			dataInicio.set(Calendar.MONTH, Calendar.JANUARY);
			dataInicio.set(Calendar.DAY_OF_MONTH, 1);

			Calendar dataFim = Calendar.getInstance();
			dataFim.set(Calendar.YEAR, 2090);
			dataFim.set(Calendar.MONTH, Calendar.JANUARY);
			dataFim.set(Calendar.DAY_OF_MONTH, 1);
			notifyMyGroup = notifyRequestService.listarGrupo(idGroup, dataInicio, dataFim);
		}
	}
//TODO
	public void concederPrivGrp() {
		SimpleDTO retorno = null;
		if (privSelect != null && !(privSelect.length == 0) && notification != null) {

			Integer groupToGrant = notification.getGroupScopeId();

			if (notification.getCreatorMemberId() != null && groupToGrant != null	&& notification.getQtdDias() != null) {

				String aliasGroup = "";

				GroupDTO group = groupService.getGroupById(groupToGrant);
				
				if (group != null) {
					if (group.getErrorCode() == ViewError.OK.getCode())
						aliasGroup = group.getApelido();
					else {
						messages.add(group.getErrorCode() + ": " + group.getErrorMsg(),
								FacesMessage.SEVERITY_ERROR);
						return;
					}
				} else {
					messages.add(ViewError.GROUP_NOT_FOUND.getCode() + ": "
							+ ViewError.GROUP_NOT_FOUND.getMsg(), FacesMessage.SEVERITY_ERROR);
					return ;
				}

				String privilegios = "";
				boolean sucess = false;
				for (int i = 0; i < privSelect.length; i++) {
					  retorno = privService
							.insertNewPriv(notification.getCreatorMemberId(),
									aliasGroup,
									privSelect[i],
									notification.getQtdDias());

					if (retorno != null) {
						if (retorno.getErrorCode() == ViewError.OK.getCode()) {
							DomainDTO[] priviArray = notiBean.getDomainPriv();
							for (int j = 0; j < priviArray.length; j++) {
								if(priviArray[j].getStringValue().equals(privSelect[i]))
									privilegios =  privilegios + ", " + priviArray[j].getDescription();
							}
							if (i+1 == privSelect.length)
								privilegios = privilegios.substring(1);
							
							sucess = true;
						}else {
							messages.add(retorno.getErrorCode() + ": "
									+ retorno.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
						}
					}
				}

				if(sucess){
					MemberDTO member = memberService.listar(notification.getCreatorMemberId());
					messages.add("atribuido(s) o(s) privilégio(s) de: " + privilegios + " ao usuário por " + notification.getQtdDias() + " dias", FacesMessage.SEVERITY_INFO);
					
					if(member != null)
						notificationBean.aprovacaoPrivilegios(group, member, privilegios);
					
				} else
					messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
							+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
				
				
			} else
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
						+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
			
			updateNotifyMyGroup(groupToGrant);
			
		} else
			messages.add(ViewError.PRIV_NOT_FOUND.getCode() + ": "
					+ ViewError.PRIV_NOT_FOUND.getMsg(),
					FacesMessage.SEVERITY_ERROR);

		
		privSelect = null;
	}

	public NotifieDTO[] getNotifyMyGroup() {
		return notifyMyGroup;
	}

	public void setNotifyMyGroup(NotifieDTO[] notifyMyGroup) {
		this.notifyMyGroup = notifyMyGroup;
	}

	public String[] getPrivSelect() {
		return privSelect;
	}

	public void setPrivSelect(String[] privSelect) {
		this.privSelect = privSelect;
	}

	public NotifieDTO getNotification() {
		return notification;
	}

	public void setNotification(NotifieDTO notification) {
		this.notification = notification;
	}
	
	
	

}
