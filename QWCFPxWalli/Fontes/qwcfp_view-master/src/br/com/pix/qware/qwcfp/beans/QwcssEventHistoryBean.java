package br.com.pix.qware.qwcfp.beans;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.EventHistoryService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.EventDTO;
import br.com.qwcss.ws.dto.MemberDTO;

@Named("historyBean")
@RequestScoped
public class QwcssEventHistoryBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1577452880791234096L;

	@Inject
	private EventHistoryService	eventHistoryService;

	@Inject
	private FacesMessages		messages;

	@Inject
	private DomainService		domainService;

	@Inject
	private MemberService		memberService;
	
	private DomainDTO[]			domainLayer;

	private DomainDTO[]			domainType;

	private EventDTO[]			events;

	private Date				dataInicio;

	private Date				dataFim;

	private String				historyLayer;

	private String				historyType;
	
	@PostConstruct
	public void init(){
		populateEvents();
		domainLayer = domainService.returnDomain("HISTORY_LAYER");
		domainType = domainService.returnDomain("HISTORY_TYPE");
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
		} else if (dataFim == null && dataInicio != null) {
			dataInicioX.setTime(dataInicio);
			dataFimX.setTime(new Date());
		} else {
			dataInicioX.setTime(dataInicio);
			dataFimX.setTime(dataFim);
		}

		if (historyLayer != null) {
			if (historyLayer.trim().equals(""))
				historyLayer = null;
		}

		if (historyType != null) {
			if (historyType.trim().equals(""))
				historyType = null;
		}

		events = eventHistoryService.listarEventos(dataInicioX, dataFimX, historyLayer, historyType);

		if (events != null) {
			if (events[0].getErrorCode() != 0) {
				messages.add("Ocorreu o erro " + events[0].getErrorCode() + " "	+ events[0].getErrorMsg(),	FacesMessage.SEVERITY_ERROR);
			}
		}
	}

	public EventDTO[] getEvents() {
		return events;
	}

	public DomainDTO[] getDomainLayer() {
		return domainLayer;
	}

	public DomainDTO[] getDomainType() {
		return domainType;
	}

	public void setDomainLayer(DomainDTO[] domainLayer) {
		this.domainLayer = domainLayer;
	}

	public void setDomainType(DomainDTO[] domainType) {
		this.domainType = domainType;
	}

	public void setEvents(EventDTO[] events) {
		this.events = events;
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

	public String getHistoryLayer() {
		return historyLayer;
	}

	public void setHistoryLayer(String historyLayer) {
		this.historyLayer = historyLayer;
	}

	public String getHistoryType() {
		return historyType;
	}

	public void setHistoryType(String historyType) {
		this.historyType = historyType;
	}

	public String getMember(Integer id) {
		String nome = "INDEFINIDO";

		if (id == 0)
			return nome;

		MemberDTO member = null;

		if (id != null)
			member = memberService.listar(id);

		if (member != null)
			nome = member.getMemberName();

		return nome;
	}

	public String getDominio(Integer id) {
		DomainDTO domain = domainService.returnDomain(id);
		String description = "";
		if (domain != null)
			description = domain.getDescription();

		return description;
	}

}
