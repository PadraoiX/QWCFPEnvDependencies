package br.com.pix.qware.qwcfp.beans.uties;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qware.qwcfp.lazy.ListNotifyMemberLazy;
import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.NotifieWrapper;
import br.com.qwcss.ws.dto.NotifieDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "listNotifySuggestedAreasBean")
@ViewScoped
public class ListNotifySuggestedAreasBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6819193157632342087L;

	@Inject
	private NotifyService notifyService;

	@Inject
	private FacesMessages messages;

	private List<NotifieDTO> selectedNotificacoes;
	
	private List<NotifieDTO> suggestedArea;

	@PostConstruct
	@Override
	public void init() {
		updateNotifyMember();
	}

	private void updateNotifyMember() {
		NotifieWrapper notificacoes = notifyService.listSuggestAreas();
		if(notificacoes.getErrorCode() == 0){
			suggestedArea = notificacoes.getNotificacoes() != null ? Arrays.asList(notificacoes.getNotificacoes()) : null;
		}
	}


	public void inativeNotify() {
		SimpleDTO simple = null;
		if (selectedNotificacoes != null && selectedNotificacoes.size() > 0) {
			for (int i = 0; i < selectedNotificacoes.size(); i++) {
				simple = notifyService.inativeNotification(selectedNotificacoes
						.get(i).getId());
			}

			if (simple != null) {
				if (simple.getErrorCode() == ViewError.OK.getCode())
					messages.add(
							ViewError.NOTIFICATION_INATIVE_SUCESS.getMsg(),
							FacesMessage.SEVERITY_INFO);
				else
					messages.add(
							simple.getErrorCode() + ":  "
									+ simple.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);
			}

			updateNotifyMember();
		}
	}

	public List<NotifieDTO> getSelectedNotificacoes() {
		return selectedNotificacoes;
	}

	public void setSelectedNotificacoes(List<NotifieDTO> selectedNotificacoes) {
		this.selectedNotificacoes = selectedNotificacoes;
	}

	public List<NotifieDTO> getSuggestedArea() {
		return suggestedArea;
	}

	public void setSuggestedArea(List<NotifieDTO> suggestedArea) {
		this.suggestedArea = suggestedArea;
	}

}
