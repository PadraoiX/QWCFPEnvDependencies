package br.com.pix.qware.qwcfp.service;

import java.util.Calendar;

import br.com.pix.qwcfp.ws.client.list.NotifieInfraList;
import br.com.pix.qwcfp.ws.client.list.RequestPriv;
import br.com.qwcss.ws.dto.NotifieDTO;

public class NotifyRequestService extends Service{
	
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Listar as notifições existentes em um grupo,  necessário o usuário ter privilégio no grupo
	 * @param groupId Identificador do grupo
	 * @param dataInicio Data de inicio para filtro
	 * @param dataFim Data de fim para filtro
	 * @return um array de notificações encontrada
	 */
	public  NotifieDTO[] listarGrupo(Integer groupId, Calendar dataInicio, Calendar dataFim){
		return RequestPriv.listarGrupo(getConnectedUser(), groupId, dataInicio, dataFim);
	}
	
	/**
	 * Listar as notificações em uma área,  necessário o usuário ter privilégio na área
	 * @param areaId Identificador da área
	 * @param dataInicio Data de inicio para filtro
	 * @param dataFim Data de fim para filtro
	 * @return um array de notificações encontrada
	 */
	public  NotifieDTO[] listarArea(Integer areaId, Calendar dataInicio, Calendar dataFim){
		return RequestPriv.listarArea(getConnectedUser(), areaId, dataInicio, dataFim);
	}

	public NotifieDTO[] listarGroupInfra(Calendar dataInicio, Calendar dataFim, Boolean ativo){
		return NotifieInfraList.listarGrupo(getConnectedUser(), dataInicio, dataFim, ativo);
	}
	
	public NotifieDTO[] listarAreaInfra( Calendar dataInicio, Calendar dataFim, Boolean ativo){
		return NotifieInfraList.listarArea(getConnectedUser(), dataInicio, dataFim, ativo);
	}
	
}
