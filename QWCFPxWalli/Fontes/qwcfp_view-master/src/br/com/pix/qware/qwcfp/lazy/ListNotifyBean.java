package br.com.pix.qware.qwcfp.lazy;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.NotifieDTO;

@ManagedBean(name = "listNotifyBean")
@ViewScoped
public class ListNotifyBean extends AbstractBean {

	/**
	
	 * 
	 * 
	 */
	private static final long	serialVersionUID	= 1859427209693919691L;
	
	@Inject
	private FacesMessages			messages;

	@Inject
	private NotifyService			notifyService;

	private ListNotifyLazy			listNotifyLazy;

	private List<NotifieDTO>		listNotify;
	
	
	@PostConstruct
	public void inti(){
		super.init();
		updateNotifyByGroup();
	}

	private void updateNotifyByGroup() {
		Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");
		if (idGroup != null) {
			Calendar dataInicio = Calendar.getInstance();
			dataInicio.set(Calendar.DAY_OF_MONTH, -7);
			Calendar dataFim = Calendar.getInstance();

			//TODO implementar na tab de configurações para que seja configuravel o range de data.
			NotifieDTO[] notifyByGroup = notifyService.listarGrupo(idGroup,
					dataInicio,
					dataFim,
					true);

			if (notifyByGroup != null && notifyByGroup.length > 0) {
				if (notifyByGroup[0].getErrorCode() != ViewError.OK.getCode()) {
					messages.add("Ocorreu o erro " + notifyByGroup[0].getErrorCode() + " "	+ notifyByGroup[0].getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
					return;
				}
				setListNotify(Arrays.asList(notifyByGroup));
				setListNotifyLazy(new ListNotifyLazy(getListNotify()));
			}
		}
	}

	public ListNotifyLazy getListNotifyLazy() {
		return listNotifyLazy;
	}

	public void setListNotifyLazy(ListNotifyLazy listNotifyLazy) {
		this.listNotifyLazy = listNotifyLazy;
	}

	public List<NotifieDTO> getListNotify() {
		return listNotify;
	}

	public void setListNotify(List<NotifieDTO> listNotify) {
		this.listNotify = listNotify;
	}

}
