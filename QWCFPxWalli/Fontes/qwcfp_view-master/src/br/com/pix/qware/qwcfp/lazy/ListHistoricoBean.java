package br.com.pix.qware.qwcfp.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.EventHistoryService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.EventDTO;

@ManagedBean(name = "listHistoricoBean")
@ViewScoped
public class ListHistoricoBean extends AbstractBean {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3067138000448971859L;

	
	@Inject
	private EventHistoryService	eventHistoryService;

	@Inject
	private FacesMessages		messages;

	private ListHistoricoLazy	listHistoryLazy;
	
	private List<EventDTO>		listHistory;
	
	private DomainDTO[]			domainLayer;

	private DomainDTO[]			domainType;
	
	private Date				dataInicio;

	private Date				dataFim;

	private String				membro;

	private String				descricao;
	
	private String				host;
	
	@PostConstruct
	public void init(){
		populateEvents();
	}
	
	public void populateEvents(){

		Calendar dataInicioX = Calendar.getInstance();
		Calendar dataFimX = Calendar.getInstance();
	
		
		if (dataFim == null || dataInicio == null) {
			dataInicioX.add(Calendar.DATE, -5);
			dataFimX.setTime(new Date());
		} else if (dataFim != null && dataInicio == null) {
			dataInicioX.setTime(dataFim);
			dataInicioX.add(Calendar.DATE, -5);
			dataFimX.setTime(dataFim);
			dataFimX.add(Calendar.DATE, 1);
		} else if (dataFim == null && dataInicio != null) {
			dataInicioX.setTime(dataInicio);
			dataFimX.setTime(new Date());
		} else {
			dataInicioX.setTime(dataInicio);
			dataFimX.setTime(dataFim);
			dataFimX.add(Calendar.DATE, 1);
		}


		EventDTO[] events = eventHistoryService.listarEventos(dataInicioX, dataFimX, membro, descricao, host);

		if (events != null) {
			if (events[0].getErrorCode() != 0) {
				messages.add("Ocorreu o erro " + events[0].getErrorCode() + " "	+ events[0].getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
				return;
			}
			
			listHistory = Arrays.asList(events);
			setListHistoryLazy(new ListHistoricoLazy(listHistory));
		}else{
			listHistory = new ArrayList<EventDTO>();
			setListHistoryLazy(new ListHistoricoLazy(listHistory));
		}
			
	}

	public ListHistoricoLazy getListHistoryLazy() {
		return listHistoryLazy;
	}

	public void setListHistoryLazy(ListHistoricoLazy listHistoryLazy) {
		this.listHistoryLazy = listHistoryLazy;
	}

	public List<EventDTO> getListHistory() {
		return listHistory;
	}

	public void setListHistory(List<EventDTO> listHistory) {
		this.listHistory = listHistory;
	}

	public DomainDTO[] getDomainLayer() {
		return domainLayer;
	}

	public void setDomainLayer(DomainDTO[] domainLayer) {
		this.domainLayer = domainLayer;
	}

	public DomainDTO[] getDomainType() {
		return domainType;
	}

	public void setDomainType(DomainDTO[] domainType) {
		this.domainType = domainType;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getMembro() {
		return membro;
	}

	public void setMembro(String membro) {
		this.membro = membro;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}


	
	

}
