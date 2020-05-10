package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;
import br.com.pix.qwcfp.ws.client.list.InvitedMembers;
import br.com.pix.qwcfp.ws.client.list.Privilegios;
import br.com.pix.qwcfp.ws.client.manager.Privilegio;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.ListPrivsDTO;
import br.com.qwcss.ws.dto.SimpleDTO;


public class PrivilegiosService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	

	/**
	 * Operao para adicionar um novo privilgio
	 * @param memberOperationId Identificador do usurio que receber um privilgio
	 * @param groupAlias Apelido do grupo 
	 * @param priv Nome do privilgio, vide dominios
	 * @param quantidadeDias Quantidade de dias de durao do privilgio
	 * @return 0 caso sucesso
	 */
	public SimpleDTO insertNewPriv( Integer memberOperationId, String groupAlias, String priv, Integer quantidadeDias ){
		return Privilegio.insertNewPriv(getConnectedUser(), memberOperationId, groupAlias, priv, quantidadeDias);
	}
	
	/**
	 * Operao para adicionar um novo privilgio
	 * @param memberOperationId Identificador do usurio que receber um privilgio
	 * @param groupAlias Apelido do grupo 
	 * @param priv Nome do privilgio, vide dominios
	 * @param quantidadeDias Quantidade de dias de durao do privilgio
	 * @param notifyId 
	 * @return 0 caso sucesso
	 */
	public SimpleDTO insertNewPrivEx( Integer memberOperationId, String groupAlias, String priv, Integer quantidadeDias, Integer notifyId ){
		return Privilegio.insertNewPrivEx(getConnectedUser(), memberOperationId, groupAlias, priv, quantidadeDias, notifyId);
	}
	
	/**
	 * Operao para adicionar um novo privilgio de 
	 * @param memberOperationId Identificador do usurio que receber um privilgio
	 * @param groupAlias Apelido do grupo 
	 * @param priv Nome do privilgio, vide dominios
	 * @param quantidadeDias Quantidade de dias de durao do privilgio
	 * @return 0 caso sucesso
	 */
	public SimpleDTO insertNewGiPriv( Integer memberOperationId, String quantidadeDias ){
		return Privilegio.insertNewGiPriv(getConnectedUser(), quantidadeDias, memberOperationId);
	}
	
	/**
	 * Remove um privilgio de um usurio
	 * @param memberOperationId Identificador do usurio a ser afetado
	 * @param groupAlias Apelido do grupo
	 * @param priv Nome do privilgio, vide dominios
	 * @return 0 caso sucesso
	 */
	public SimpleDTO removePriv( Integer memberOperationId, String groupAlias, String priv){
		return Privilegio.removePriv(getConnectedUser(), memberOperationId, groupAlias, priv);
	}
	
	/**
	 * Remove um privilgio de um usurio gestor de infra estrutura
	 * @param memberOperationId Identificador do usurio a ser afetado
	 * @param groupAlias Apelido do grupo
	 * @param priv Nome do privilgio, vide dominios
	 * @return 0 caso sucesso
	 */
	public SimpleDTO removeGiPriv(Integer memberOperationId){
		return Privilegio.removeGiPriv(getConnectedUser(), memberOperationId);
	}
	
	/**
	 * Remove o usurio do grupo
	 * @param memberOperationId Identificador do usurio
	 * @param groupAlias Apelido do grupo
	 * @return 0 caso sucesso
	 */
	public SimpleDTO removeUserFromGroup( Integer memberOperationId, String groupAlias ){
		return Privilegio.removeUserFromGroup(getConnectedUser(), memberOperationId, groupAlias);
	}
	
	/**
	 * Operao para solicitar privilgio
	 * @param justificativa Justificativa para solicitao
	 * @param group Apelido do grupo
	 * @param privilegios Nome do privilgio, vide dominios
	 * @return 0 caso sucesso
	 */
	public  SimpleDTO requestPriv( String justificativa, String group, String[] privilegios ){
		return Privilegio.requestPriv(getConnectedUser(), justificativa, group, privilegios);
	}
	
	public  SimpleDTO requestPrivArea( String justificativa, String area, String[] privilegios ){
		return Privilegio.requestPrivArea(getConnectedUser(), justificativa, area, privilegios);
	}
	
	
	/**
	 * Verificar os privilgios do usurio conectado em um grupo
	 * @param groupAlias Apelido do grupo
	 * @return Objeto contendo os privilgios
	 */
	public  ListPrivsDTO listar(String groupAlias){
		return Privilegios.listar(getConnectedUser(), groupAlias);
	}
	

	/**
	 * Verificar os privilgios de um usurio em um grupo
	 * @param groupAlias Apelido do grupo
	 * @param memberId Identificador do usurio
	 * @return Objeto contendo os privilgios
	 */
	public  ListPrivsDTO listar(String groupAlias, Integer memberId){
		return Privilegios.listar(getConnectedUser(), groupAlias, memberId);
	}
	
	public SimpleDTO insertPrivArea(Integer memberOperationId, Integer areaId, String priv, Integer quantidadeDias){
		return Privilegio.insertPrivArea(getConnectedUser(), memberOperationId, areaId, priv, quantidadeDias);
	}
	
	public SimpleDTO deletePrivArea(Integer memberOperationId, Integer areaId, String priv, Integer quantidadeDias){
		return Privilegio.deletePrivArea(getConnectedUser(), memberOperationId, areaId, priv, quantidadeDias);
	}
	
	public SimpleDTO removePrivArea(Integer memberOperationId, Integer areaId, String priv, Integer quantidadeDias){
		return Privilegio.removePrivArea(getConnectedUser(), memberOperationId, areaId, priv, quantidadeDias);
	}
	
	public SimpleDTO disableNotiPrivArea(Integer memberOperationId, Integer areaId, String priv, Integer quantidadeDias){
		return Privilegio.disableNotiPrivArea(getConnectedUser(), memberOperationId, areaId, priv, quantidadeDias);
	}

	/**
	 * Verifica se o usurio conectado  gestor de infra-estrutura
	 * @return Objeto contendo o privilgio
	 */
	public  SimpleDTO memberIsGi(){
		return Privilegios.memberIsGi(getConnectedUser());
	}
	
	/**
	 * Verifica se o usurio conectado  gestor de infra-estrutura
	 * @return Objeto contendo o privilgio
	 */
	public  SimpleDTO memberIsGi(ConnectedUser connectedUser){
		return Privilegios.memberIsGi(connectedUser);
	}
	
	/**
	 * Verifica se o membro informado se trata de um usurio gestor de infra-estrutura.
	 * @param memberCpf cpf do membro a ser avaliado
	 * @return Objeto simples contendo erro caso no seja gestor de infra-estrutura.
	 */
	public SimpleDTO memberIsGi(String mail) {
		return Privilegios.memberIsGi(getConnectedUser(), mail);
	}
	
	
	public SimpleDTO isInvited(Integer memberId) {
		return InvitedMembers.isInvited(getConnectedUser(), memberId);
	}
	
	public ListPrivsDTO returnPrivGrp(){
		return Privilegios.returnPrivGrp( getConnectedUser() );
	}
	
	public ListPrivsDTO returnPrivGrp(Integer targetMember, Integer groupId){
		return Privilegios.returnPrivGrp(getConnectedUser(), targetMember, groupId);
	}
	
	public ListPrivsDTO returnPrivArea(){
		return Privilegios.returnPrivArea( getConnectedUser() );
	}
	public ListPrivsDTO returnPrivArea(Integer targetMember, Integer areaId){
		return Privilegios.returnPrivArea(getConnectedUser(), targetMember, areaId);
	}
	
	public ListPrivsDTO returnPrivGG(){
		return Privilegios.returnPrivGG( getConnectedUser() );
	}
	
	public GroupDTO[] listManagedGroups(){
		return Privilegio.listManagedGroups( getConnectedUser() );
	}
	

}
