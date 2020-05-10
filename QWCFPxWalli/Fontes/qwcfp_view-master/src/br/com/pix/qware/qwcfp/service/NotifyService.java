package br.com.pix.qware.qwcfp.service;

import java.util.Calendar;

import org.bouncycastle.asn1.x509.NoticeReference;

import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;
import br.com.pix.qwcfp.ws.client.list.Notificacoes;
import br.com.pix.qwcfp.ws.client.notifie.Notificate;
import br.com.qwcss.ws.NotifieWrapper;
import br.com.qwcss.ws.dto.NotifieDTO;
import br.com.qwcss.ws.dto.SimpleDTO;


public class NotifyService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	

	/**
	 * Gera uma notificao para um grupo
	 * @param groupId Identificador do grupo
	 * @param messageText Texto da notificao
	 * @return 0 caso sucesso
	 */
	public  SimpleDTO notificateGroup(Integer groupId, String messageText){
		return Notificate.notificateGroup(getConnectedUser(), groupId, messageText);
	}
	
	public SimpleDTO countNotifications(){
		return Notificacoes.countNotifications(getConnectedUser());
	}
	
	/**
	 * Gera uma notificao para uma rea
	 * @param areaId Identificador da rea
	 * @param messageText Texto da notificao
	 * @return 0 caso sucesso
	 */
	public  SimpleDTO notificateArea(Integer areaId, String messageText){
		return Notificate.notificateArea(getConnectedUser(), areaId, messageText);
	}
	
	/**
	 * Gera uma notificao a um usurio
	 * @param memberId Identificador do usurio
	 * @param messageText Texto da notificao
	 * @return
	 */
	public  SimpleDTO notificateMember(Integer memberId, String messageText){
		return Notificate.notificateMember(getConnectedUser(), memberId, messageText);
	}
	
	/**
	 * Gera uma notificao a um usurio
	 * @param memberId Identificador do usurio
	 * @param messageText Texto da notificao
	 * @return
	 */
	public  SimpleDTO notificateMemberEx(Integer memberId, String messageText){
		return Notificate.notificateMemberEx(getConnectedUser(), memberId, messageText);
	}
	
	
	
	/**
	 * Lista as notificaes de que existem para um grupo
	 * @param groupId Identificador do grupo
	 * @param dataInicio Data de inicio para filtro
	 * @param dataFim Data de fil para filtro
	 * @param ativo Exibir notificaes ativas ou inativas
	 * @return Array contendo os detalhes das notificaes
	 */
	public  NotifieDTO[] listarGrupo(Integer groupId, Calendar dataInicio, Calendar dataFim, boolean ativo){
		return Notificacoes.listarGrupo(getConnectedUser(), groupId, dataInicio, dataFim, ativo);
	}
	

	/**
	 * Lista as notificaes de que existem para uma rea
	 * @param areaId Identificador da rea
	 * @param dataInicio Data de inicio para filtro
	 * @param dataFim Data de fim para filtro
	 * @param ativo Exibir notificaes ativas ou inativas
	 * @return Array contendo os detalhes das notificaes
	 */
	public  NotifieDTO[] listarArea(Integer areaId, Calendar dataInicio, Calendar dataFim, boolean ativo){
		return Notificacoes.listarArea(getConnectedUser(), areaId, dataInicio, dataFim, ativo);
	}
	
	public SimpleDTO inativeAllNotification(){
		return Notificate.inativeAllNotification(getConnectedUser());
	}
	
	public SimpleDTO inativeNotification( Integer notifyId ){
		return Notificate.inativeNotification(getConnectedUser(), notifyId);
	}
	
	public SimpleDTO inativeNotification( Integer notifyId, String justify ){
		return Notificate.inativeNotification(getConnectedUser(), notifyId, justify);
	}
	
	public SimpleDTO ativeNotification( Integer notifyId ){
		return Notificate.ativeNotification(getConnectedUser(), notifyId);
	}
	
	public SimpleDTO notificateSuggestArea(String login, String senha, String notifieSendType, String messageText) {
		return Notificate.notificateSuggestArea(login, senha, notifieSendType, messageText);
	}
	
	public NotifieWrapper listSuggestAreas(){
		return Notificacoes.listSuggestAreas(getConnectedUser());
	}
		
	/**
	 * Lista de notificaes para um determinado usurio
	 * @param memberId Identificador nido do usurio
	 * @param dataInicio Data de inicio para filtro
	 * @param dataFim Data de fim para filtro
	 * @param ativo Exibir notificaes ativas e inativas
	 * @return Array contendo os detalhes das notificaes
	 */
	public  NotifieDTO[] listarMember(Integer memberId, Calendar dataInicio, Calendar dataFim, boolean ativo){
		return Notificacoes.listarMember(getConnectedUser(), memberId, dataInicio, dataFim, ativo);
	}
	
	public  NotifieWrapper listarMemberEx(Integer memberId, Calendar dataInicio, Calendar dataFim, boolean ativo, Integer pager, Integer qtdRecords){
		return Notificacoes.listarMemberEx(getConnectedUser(), memberId, dataInicio, dataFim, ativo, pager, qtdRecords);
	}
	
	public NotifieDTO[] listarAllMyGroup(Calendar dataInicio, Calendar dataFim){
		return Notificacoes.listarAllMyGroup(getConnectedUser(), dataInicio, dataFim);
	}
	
	public NotifieDTO[] listarAllMyArea(Calendar dataInicio, Calendar dataFim){
		return Notificacoes.listarAllMyArea(getConnectedUser(), dataInicio, dataFim);
	}
	
}
