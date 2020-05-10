package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.manager.No;
import br.com.qwcss.ws.dto.NodeDTO;


public class NodeInformationService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	

	/**
	 * Mtodo para cadastrar um novo n
	 * @param status Status do n, vide dominios
	 * @param apelido Apelido do n
	 * @param dbPasswdQwareLogin Senha do banco de dados Q-Ware 
	 * @param dbUserQwarelogin Usurio do banco de dados Q-Ware
	 * @param defaultInfoGroup Grupo de informao padro
	 * @param description Descrio do n
	 * @param ipAddress Endereo IP do n
	 * @param name Nome do n
	 * @param osQwarePaswdEnc Senha do usurio Q-Ware do sistema operacional
	 * @param osQwareUser Nome do usurio Q-Ware do sistema operacional
	 * @param portConnection Porta de conexo utilizada
	 * @param qwareHomePath Diretrio home de instalao do Q-Ware
	 * @param qwareKeyEnc Chave de criptografica do Q-Ware
	 * @param qwarePasswdEnc Senha do usurio Q-Ware
	 * @param qwareUser Nome do usurio Q-Ware
	 * @param rootFullPath4Files Diretrio onde so salvos os arquivos transferidos
	 * @param uriJdbcForDbConection String de conexo com o banco de dados Q-Ware
	 * @return 0 caso sucesso
	 */
	public  NodeDTO insert(String status, String apelido, String dbPasswdQwareLogin, String dbUserQwarelogin, Integer defaultInfoGroup, String description, String ipAddress, String name, String osQwarePaswdEnc, String osQwareUser, Integer portConnection, String qwareHomePath, String qwareKeyEnc, String qwarePasswdEnc, String qwareUser, String rootFullPath4Files, String uriJdbcForDbConection){
		return No.insert(getConnectedUser(), status, apelido, dbPasswdQwareLogin, dbUserQwarelogin, defaultInfoGroup, description, ipAddress, name, osQwarePaswdEnc, osQwareUser, portConnection, qwareHomePath, qwareKeyEnc, qwarePasswdEnc, qwareUser, rootFullPath4Files, uriJdbcForDbConection);
	}
	
	/**
	 * Mtodo para atualizar um n
	 * @param nodeId Identificador do n
	 * @param status Status do n, vide dominios
	 * @param apelido Apelido do n
	 * @param dbPasswdQwareLogin Senha do banco de dados Q-Ware 
	 * @param dbUserQwarelogin Usurio do banco de dados Q-Ware
	 * @param defaultInfoGroup Grupo de informao padro
	 * @param description Descrio do n
	 * @param ipAddress Endereo IP do n
	 * @param name Nome do n
	 * @param osQwarePaswdEnc Senha do usurio Q-Ware do sistema operacional
	 * @param osQwareUser Nome do usurio Q-Ware do sistema operacional
	 * @param portConnection Porta de conexo utilizada
	 * @param qwareHomePath Diretrio home de instalao do Q-Ware
	 * @param qwareKeyEnc Chave de criptografica do Q-Ware
	 * @param qwarePasswdEnc Senha do usurio Q-Ware
	 * @param qwareUser Nome do usurio Q-Ware
	 * @param rootFullPath4Files Diretrio onde so salvos os arquivos transferidos
	 * @param uriJdbcForDbConection String de conexo com o banco de dados Q-Ware
	 * @return 0 caso sucesso
	 */
	public  NodeDTO update(Integer nodeId, String status, String apelido, String dbPasswdQwareLogin, String dbUserQwarelogin, Integer defaultInfoGroup, String description, String ipAddress, String name, String osQwarePaswdEnc, String osQwareUser, Integer portConnection, String qwareHomePath, String qwareKeyEnc, String qwarePasswdEnc, String qwareUser, String rootFullPath4Files, String uriJdbcForDbConection){
		return No.update(getConnectedUser(), nodeId, status, apelido, dbPasswdQwareLogin, dbUserQwarelogin, defaultInfoGroup, description, ipAddress, name, osQwarePaswdEnc, osQwareUser, portConnection, qwareHomePath, qwareKeyEnc, qwarePasswdEnc, qwareUser, rootFullPath4Files, uriJdbcForDbConection);
	}
	
	
	/**
	 * Mtodo para deletar um n
	 * @param nodeId Identificador do n
	 * @return 0 caso sucesso
	 */
	public  NodeDTO delete(Integer nodeId){
		return No.delete(getConnectedUser(), nodeId);
	}
	
	/**
	 * Exibe os detalhes de todos os ns do sistema
	 * @return Array contendo os ns
	 */
	public  NodeDTO[] nodes(){
		return br.com.pix.qwcfp.ws.client.list.No.nodes(getConnectedUser());
	}
	
	/**
	 * Exibe os detalhes de um determinado n de acordo com seu Identificador
	 * @param nodeId Identificador do n
	 * @return Array contendo o n, nulo caso no encontrado
	 */
	public  NodeDTO[] nodes(Integer nodeId){
		return br.com.pix.qwcfp.ws.client.list.No.nodes(getConnectedUser(), nodeId);
	}
	
	
}
