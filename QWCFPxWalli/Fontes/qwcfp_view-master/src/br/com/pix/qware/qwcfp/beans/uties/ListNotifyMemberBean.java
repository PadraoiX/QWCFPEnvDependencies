package br.com.pix.qware.qwcfp.beans.uties;

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
import br.com.qwcss.ws.dto.NotifieDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "listNotifyMemberBean")
@ViewScoped
public class ListNotifyMemberBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6819193157632342087L;

	@Inject
	private NotifyService notifyService;

	@Inject
	private LoginBean loginBean;

	@Inject
	private FacesMessages messages;

	private List<NotifieDTO> selectedNotificacoes;

	private ListNotifyMemberLazy notificationsLazy;

	@PostConstruct
	@Override
	public void init() {
		updateNotifyMember();
	}

	private void updateNotifyMember() {
		setNotificationsLazy(null);
		if (loginBean.getMember() != null) {
			setNotificationsLazy(new ListNotifyMemberLazy(notifyService,
					loginBean.getMember().getMemberId()));
		}
	}

	public void inativeAll(){
		SimpleDTO simple = notifyService.inativeAllNotification();
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

	public ListNotifyMemberLazy getNotificationsLazy() {
		return notificationsLazy;
	}

	public void setNotificationsLazy(ListNotifyMemberLazy notificationsLazy) {
		this.notificationsLazy = notificationsLazy;
	}

}
