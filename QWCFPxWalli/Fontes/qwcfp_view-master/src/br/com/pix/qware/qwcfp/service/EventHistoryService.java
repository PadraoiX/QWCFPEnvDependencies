package br.com.pix.qware.qwcfp.service;


import java.util.Calendar;

import br.com.pix.qwcfp.ws.client.list.Eventos;
import br.com.qwcss.ws.dto.EventDTO;

public class EventHistoryService extends Service {

	/**
	 * Lista os eventos do sistema (Histrico), deve-se passar um perodo curto para o servidor aceitar a transsao 
	 * @param dataInicio 
	 * @param dataFim
	 * @param historyLayer Contexto que se deseja filtrar a pesquisa, vide dominios
	 * @param historyType Tipo de eventos a filtrar, vide dominios
	 * @return Array contendo os eventos pesquisados
	 */
	public EventDTO[] listarEventos(Calendar dataInicio, Calendar dataFim, String historyLayer, String historyType){
		return Eventos.listarEventos(getConnectedUser(),dataInicio, dataFim, historyLayer, historyType);
	}
	
	public EventDTO[] listarEventos(Calendar dataInicio, Calendar dataFim,  String membro, String descricao, String host){
		return Eventos.listarEventos(getConnectedUser(),dataInicio, dataFim, membro, descricao, host);
	}
	

}
