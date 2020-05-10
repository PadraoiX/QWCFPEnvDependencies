package br.com.pix.qware.qwcfp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qware.qwcfp.wrappers.WrappedGroupsContainer;
import br.com.pix.qwcfp.ws.client.list.Groups;
import br.com.pix.qwcfp.ws.client.list.GruposParticipanteEx;
import br.com.pix.qwcfp.ws.client.list.ListGroupById;
import br.com.pix.qwcfp.ws.client.list.ListGroupsPendentes;
import br.com.pix.qwcfp.ws.client.manager.Group;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.GroupSpaceDTO;
import br.com.qwcss.ws.dto.ListMyGroupsEXDTO;
import br.com.qwcss.ws.dto.MyGroupsDTO;
import br.com.qwcss.ws.dto.MyGroupsEXDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

public class InformationGroupService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Mtodo para se solicitar a criao de um novo grupo
	 * 
	 * @param inputDate
	 *            Data limite para enviar uma verso
	 * @param outputDate
	 *            Data limite para fazer download de uma verso
	 * @param daysLimitDiscart
	 *            Dias totais que um arquivo tem de vida dentro do grupo
	 * @param fileLimitVersions
	 *            Limite total de verso de um grupo
	 * @param areaId
	 *            Identificador da rea a qual o grupo pertence
	 * @param groupManagerId
	 *            Identificador do usurio gerente do grupo
	 * @param supportPerson1Id
	 *            Identificador do usurio suporte 1 do grupo
	 * @param supportPerson2Id
	 *            Identificador do usurio suporte 2 do grupo
	 * @param subordinatedGroupId
	 *            Identificador do grupo do qual  subordinado, nulo caso no seja subordinado a nenhum
	 * @param groupName
	 *            Nome do grupo
	 * @param ownerCreator
	 *            Nome da pessoa que idealizou o grupo
	 * @param shortDescription
	 *            Descrio do grupo
	 * @param spaceLimit
	 *            Espao limite do grupo em bytes
	 * @param apelido
	 *            Apelido do grupo
	 * @param aceptVersioning
	 *            Caso o grupo aceite versionamento
	 * @param fileSystemStorageType
	 *            String value correspondente da tabela de dominio que indica o tipo de armazenamento dos arquivo
	 * @param notificationType
	 *            String value correspondente da tabela de dominio que indica o tipo de notificao
	 * @param justificativa
	 *            Justificativa para criar o grupo
	 * @param layoutIdFk 
	 * @param fkIdMetadata 
	 * @return 0 caso sucesso
	 */
	public MyGroupsDTO insert(Calendar inputDate,
			Calendar outputDate,
			Integer daysLimitDiscart,
			Integer fileLimitVersions,
			Integer areaId,
			Integer groupManagerId,
			Integer supportPerson1Id,
			Integer supportPerson2Id,
			Integer subordinatedGroupId,
			String groupName,
			String ownerCreator,
			String shortDescription,
			BigDecimal spaceLimit,
			String apelido,
			boolean aceptVersioning,
			String fileSystemStorageType,
			String notificationType,
			String justificativa,
			Integer layoutIdFk,
			Integer fkIdMetadata) {
		return Group.insert(getConnectedUser(), 
				inputDate,
				outputDate,
				daysLimitDiscart,
				fileLimitVersions,
				areaId,
				groupManagerId,
				supportPerson1Id,
				supportPerson2Id,
				subordinatedGroupId,
				groupName,
				ownerCreator,
				shortDescription,
				spaceLimit,
				apelido,
				aceptVersioning,
				fileSystemStorageType,
				notificationType,
				justificativa,
				layoutIdFk,
				fkIdMetadata);
	}

	/**
	 * Mtodo para atualiar os detalhes de um grupo
	 * 
	 * @param groupId
	 *            Identificador do grupo
	 * @param inputDate
	 *            Data limite para enviar uma verso
	 * @param outputDate
	 *            Data limite para fazer download de uma verso
	 * @param daysLimitDiscart
	 *            Dias totais que um arquivo tem de vida dentro do grupo
	 * @param fileLimitVersions
	 *            Limite total de verso de um grupo
	 * @param areaId
	 *            Identificador da rea a qual o grupo pertence
	 * @param groupManagerId
	 *            Identificador do usurio gerente do grupo
	 * @param supportPerson1Id
	 *            Identificador do usurio suporte 1 do grupo
	 * @param supportPerson2Id
	 *            Identificador do usurio suporte 2 do grupo
	 * @param subordinatedGroupId
	 *            Identificador do grupo do qual  subordinado, nulo caso no seja subordinado a nenhum
	 * @param groupName
	 *            Nome do grupo
	 * @param ownerCreator
	 *            Nome da pessoa que idealizou o grupo
	 * @param shortDescription
	 *            Descrio do grupo
	 * @param spaceLimit
	 *            Espao limite do grupo em bytes
	 * @param apelido
	 *            Apelido do grupo
	 * @param aceptVersioning
	 *            Caso o grupo aceite versionamento
	 * @param fileSystemStorageType
	 *            String value correspondente da tabela de dominio que indica o tipo de armazenamento dos arquivo
	 * @param notificationType
	 *            String value correspondente da tabela de dominio que indica o tipo de notificao
	 * @param justificativa
	 *            Justificativa para criar o grupo
	 * @param layoutIdFk 
	 * @param fkIdMetadata 
	 * @return 0 caso sucesso
	 */
	public MyGroupsDTO update(Integer groupId,
			Calendar inputDate,
			Calendar outputDate,
			Integer daysLimitDiscart,
			Integer fileLimitVersions,
			Integer areaId,
			Integer groupManagerId,
			Integer supportPerson1Id,
			Integer supportPerson2Id,
			Integer subordinatedGroupId,
			String groupName,
			String ownerCreator,
			String shortDescription,
			BigDecimal spaceLimit,
			String apelido,
			boolean aceptVersioning,
			String fileSystemStorageType,
			String notificationType,
			String justificativa,
			Integer layoutIdFk,
			Integer fkIdMetadata) {
		return Group.update(getConnectedUser(), 
				groupId,
				inputDate,
				outputDate,
				daysLimitDiscart,
				fileLimitVersions,
				areaId,
				groupManagerId,
				supportPerson1Id,
				supportPerson2Id,
				subordinatedGroupId,
				groupName,
				ownerCreator,
				shortDescription,
				spaceLimit,
				apelido,
				aceptVersioning,
				fileSystemStorageType,
				notificationType,
				justificativa,
				layoutIdFk,
				fkIdMetadata);
	}

	/**
	 * Operao para deletar um grupo
	 * 
	 * @param groupId
	 *            Identificador do grupo
	 * @param justificativa
	 *            Justificativa para a operao
	 * @return 0 caso sucesso
	 */
	public SimpleDTO delete(Integer groupId, String justificativa) {
		return Group.delete(getConnectedUser(), groupId, justificativa);
	}

	/**
	 * Operao para ativar um grupo
	 * 
	 * @param groupId
	 *            Identificador do grupo
	 * @param justificativa
	 *            Justificativa para a operao
	 * @return 0 caso sucesso
	 */
	public SimpleDTO active(Integer groupId, String justificativa) {
		return Group.active(getConnectedUser(), groupId, justificativa);
	}

	/**
	 * Operao para desativar um grupo
	 * 
	 * @param groupId
	 *            Identificador do grupo
	 * @param justificativa
	 *            Justificativa para a operao
	 * @return 0 caso sucesso
	 */
	public SimpleDTO inactive(Integer groupId, String justificativa) {
		return Group.inactive(getConnectedUser(), groupId, justificativa);
	}

	/**
	 * Retorna os grupos de acordo com o seu pai
	 * 
	 * @param fatherGroup
	 *            Identificador do grupo pai
	 * @return
	 */
	public GroupDTO[] listar(Integer fatherGroup) {
		return Groups.listar(getConnectedUser(), fatherGroup);
	}

	/**
	 * Retorna detalhes do grupo de acordo com seu apelido
	 * 
	 * @param groupAlias
	 *            Apelido do grupo
	 * @return Array contendo o grupo, nulo caso no encontrado
	 */
	public GroupDTO listar(String groupAlias) {
		GroupDTO [] groups = Groups.listar(getConnectedUser(), groupAlias);
		if(groups != null && groups.length > 0)
			return groups[0];
		else
			return null;
	}

	/**
	 * Retorna todos os grupos do sistema
	 * 
	 * @return Array com os detalhes de todos os grupos
	 */
	public GroupDTO[] listar() {
		return Groups.listar(getConnectedUser());
	}

	/**
	 * Exibe todos os grupo do qual o usurio conectado faz parte
	 * 
	 * @return Array com os detalhes do grupo
	 */
	/*public MyGroupsDTO[] myGroups() {
		return GruposParticipante.listar(getConnectedUser());
	}*/
	

	/**
	 * Exibe todos os grupos, com determinado status, do qual o usurio faz parte
	 * 
	 * @return Array com os grupos do usuário
	 */
	/*public MyGroupsDTO[] myGroups(String domainName, String stringValue) {

		MyGroupsDTO[] myGroups = myGroups();
		List<MyGroupsDTO> ret = new ArrayList<MyGroupsDTO>();
		CookieWork cookieWork = new CookieWork();
		List<Cookie> cookies = new ArrayList<Cookie>();
		
		DomainService domainService = new DomainService();
		DomainDTO status = domainService.returnDomain(domainName, stringValue);

		if (status != null && myGroups != null && myGroups.length > 0) {
			if(myGroups[0].getErrorCode() == null){
				
				for (MyGroupsDTO myGroupsDTO : myGroups) {
					if (myGroupsDTO != null) {
						if (status.getId().compareTo(myGroupsDTO.getStatus()) == 0)
							ret.add(myGroupsDTO);
					}
				}
			}else{
				for (MyGroupsDTO myGroupsDTO : myGroups) {
					ret.add(myGroupsDTO);
				}
			}
			
			for (MyGroupsDTO myGroupsDTO : ret) {
				Cookie cookieTemp = cookieWork.getCookie(String.valueOf(myGroupsDTO.getGroupId()));
				if(cookieTemp != null){
					cookies.add( cookieTemp );
					myGroupsDTO.setQtdeAcesso(Integer.parseInt(cookieTemp.getValue()));
				}
			}
			
			if(cookies.size() > 0)
				Collections.sort(ret);
				
			return ret.toArray(new MyGroupsDTO[ret.size()]);
			
		} else {
			return null;
		}
	}*/
	
	/**
	 * Exibe todos os grupo do qual o usurio conectado faz parte
	 * @param page 
	 * @param records 
	 * 
	 * @return Array com os detalhes do grupo
	 */
	
	public ListMyGroupsEXDTO myGroupsEx(Integer page, Integer records, String filter) {
		return GruposParticipanteEx.list(getConnectedUser(),page,records, filter);
	}
	
	
	/**
	 * Exibe todos os grupos, com determinado status, do qual o usurio faz parte
	 * @param page 
	 * @param records 
	 * 
	 * @return Array com os grupos do usuário
	 */
	public WrappedGroupsContainer myGroupsEx(String domainName, String stringValue, Integer page, Integer records) {
		return myGroupsEx(domainName, stringValue, page, records, null);
	}
	public WrappedGroupsContainer myGroupsEx(String domainName, String stringValue, Integer page, Integer records, String filter) {
		
		ListMyGroupsEXDTO list = myGroupsEx(page, records, filter);
		MyGroupsEXDTO[] myGroups = list.getReturnList();
		List<MyGroupsWrapper> ret = new ArrayList<MyGroupsWrapper>();
	/*	CookieWork cookieWork = new CookieWork();
		List<Cookie> cookies = new ArrayList<Cookie>();*/
		
		DomainService domainService = new DomainService();
		DomainDTO status = domainService.returnDomain(domainName, stringValue);
		
		if (status != null && myGroups != null && myGroups.length > 0) {
			if(myGroups[0].getErrorCode() == null){
				
				for (MyGroupsEXDTO myGroupsDTO : myGroups) {
					if (myGroupsDTO != null) {
						if (status.getStringValue().compareTo(myGroupsDTO.getGroupStatus()) == 0)
							ret.add(new MyGroupsWrapper(myGroupsDTO));
					}
				}
			}else{
				for (MyGroupsEXDTO myGroupsDTO : myGroups) {
					ret.add(new MyGroupsWrapper(myGroupsDTO));
				}
			}
			
			/*for (MyGroupsWrapper myGroupsDTO : ret) {
				Cookie cookieTemp = cookieWork.getCookie(String.valueOf(myGroupsDTO.getMyGroupsEx().getGroupId()));
				if(cookieTemp != null){
					cookies.add( cookieTemp );
					myGroupsDTO.getMyGroupsEx().setQtdeAcesso(Integer.parseInt(cookieTemp.getValue()));
				}
			}*/
			
			/*if(cookies.size() > 0)
				Collections.sort(ret);*/
			
			WrappedGroupsContainer wrappedGroupsContainer = new WrappedGroupsContainer(); 
			wrappedGroupsContainer.setList(ret);
			wrappedGroupsContainer.setSize(list.getSize());
			
			return wrappedGroupsContainer;
			
		} else {
			return null;
		}
	}
	
	public MyGroupsWrapper myGroupEx(Integer groupId) {
		return myGroupEx(groupId, null) ;
	}
	
	public MyGroupsWrapper myGroupEx(String alias) {
		return myGroupEx(alias, null) ;
	}
	
	public MyGroupsWrapper myGroupEx(Integer groupId,Integer memberId) {
		return new MyGroupsWrapper(GruposParticipanteEx.get(getConnectedUser(), groupId, memberId, null)) ;
	}
	
	public MyGroupsWrapper myGroupEx(String alias,Integer memberId) {
		return new MyGroupsWrapper(GruposParticipanteEx.get(getConnectedUser(), null, memberId, alias)) ;
	}

	public GroupDTO getGroupById(Integer id) {
		return ListGroupById.getGroupById(getConnectedUser(), id);
	}

	public GroupDTO getGroupByAlias(String alias) {
		return ListGroupById.getGroupByAlias(getConnectedUser(), alias);
	}

	public GroupDTO[] retunrGroupByArea(Integer areaId) {
		return Groups.returnGroupByArea(getConnectedUser(), areaId);
	}

	public GroupDTO[] retunrGroupByArea(String areaAlias) {
		return Groups.returnGroupByArea(getConnectedUser(), areaAlias);
	}

	/**
	 * Exibe todos os grupos, com determinado status, do qual o usurio faz parte
	 * 
	 * @return Array com os grupos do usuário
	 */
	public GroupDTO[] getGroupPendentes() {
		return ListGroupsPendentes.getGrupoPendentes(getConnectedUser());
	}
	
	
	/**
	 * 	Retorna informções sobre o spaço de um grupo especifico.
	 * 
	 * @param groupId se informado, o modo de busca sera por groupId
	 * @param groupAlias se informado, o modo de busca sera por apelido
	 * @return
	 */
	public GroupSpaceDTO returnGroupSpace(Integer groupId, String groupAlias) {
		return Groups.listGroupSpace(getConnectedUser(), groupId, groupAlias);
	}

}
