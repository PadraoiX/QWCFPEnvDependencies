package br.com.pix.qware.qwcfp.service;

import java.util.Arrays;
import java.util.List;

import br.com.pix.qwcfp.ws.client.list.GroupMembers;
import br.com.pix.qwcfp.ws.client.list.InvitedMembers;
import br.com.pix.qwcfp.ws.client.list.ListMemberByArea;
import br.com.pix.qwcfp.ws.client.list.ListMembersByEmail;
import br.com.pix.qwcfp.ws.client.list.Members;
import br.com.pix.qwcfp.ws.client.manager.Member;
import br.com.pix.qwcfp.ws.client.manager.MemberEx;
import br.com.qwcss.ws.InvitationDTO;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.MyGroupsEXDTO;
import br.com.qwcss.ws.dto.SimpleDTO;




public class MemberService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Operao para inserir um novo usurio
	 * @param name Nome do usurio
	 * @param cpf CPF vlido do usurio
	 * @param passwd Senha do usurio
	 * @param mail E-Mail institucional do usurio
	 * @param personalMail E-Mail pessoal do usurio
	 * @param ddd Cdigo de rea do usurio
	 * @param phone Telefone do usurio
	 * @param cep Cdigo postal do usurio
	 * @param address Endereo do usurio
	 * @param areaAlias Apelido da rea ao qual o usurio participa
	 * @param bossCpf CPF do chefe do usurio
	 * @param justify Justificativa para o cadastramento
	 * @param ldapKeyForCorpAuth 
	 * @return 0 caso sucesso
	 */
	public LoginDTO insert( String name, String cpf, String passwd, String mail, String personalMail, String ddd, String phone, String cep, String address, String areaAlias, String bossCpf, String justify, String ldapKeyForCorpAuth){
		return Member.insert(getConnectedUser(), name, cpf, passwd, mail, personalMail, ddd, phone, cep, address, areaAlias, bossCpf, justify, ldapKeyForCorpAuth);
	}
	
	/**
	 * Operao para inserir um novo usurio sem precisar logar no webservice
	 * @param name Nome do usurio
	 * @param cpf CPF vlido do usurio
	 * @param passwd Senha do usurio
	 * @param mail E-Mail institucional do usurio
	 * @param personalMail E-Mail pessoal do usurio
	 * @param ddd Cdigo de rea do usurio
	 * @param phone Telefone do usurio
	 * @param cep Cdigo postal do usurio
	 * @param address Endereo do usurio
	 * @param areaAlias Apelido da rea ao qual o usurio participa
	 * @param bossCpf CPF do chefe do usurio
	 * @param justify Justificativa para o cadastramento
	 * @param ldapKeyForCorpAuth 
	 * @return 0 caso sucesso
	 */
	public LoginDTO insertExterno( String name, String cpf, String passwd, String mail, String personalMail, String ddd, String phone, String cep, String address, String areaAlias, String bossCpf, String justify, String ldapKeyForCorpAuth){
		return MemberEx.insert(name, cpf, passwd, mail, personalMail, ddd, phone, cep, address, areaAlias, bossCpf, justify, ldapKeyForCorpAuth);
	}
	
	public LoginDTO insertInvited( String name, String cpf, String passwd, String mail, String personalMail, String ddd, String phone, String cep, String address, String areaAlias, String bossCpf, String justify, String token, String ldapKeyForCorpAuth){
		return MemberEx.insertInvited(name, cpf, passwd, mail, personalMail, ddd, phone, cep, address, areaAlias, bossCpf, justify, token, ldapKeyForCorpAuth);
	}
	
	
	/**
	 * Mtodo para atualizar os dados de um usurio
	 * @param memberOperation id do usurio
	 * @param name Nome do usurio
	 * @param cpf CPF vlido do usurio
	 * @param passwd Senha do usurio
	 * @param mail E-Mail institucional do usurio
	 * @param personalMail E-Mail pessoal do usurio
	 * @param ddd Cdigo de rea do usurio
	 * @param phone Telefone do usurio
	 * @param cep Cdigo postal do usurio
	 * @param address Endereo do usurio
	 * @param areaAlias Apelido da rea ao qual o usurio participa
	 * @param bossCpf CPF do chefe do usurio
	 * @param justify Justificativa para o cadastramento
	 * @param ldapKeyForCorpAuth 
	 * @return 0 caso sucesso
	 */
	public  LoginDTO update(String memberOperation, String name, String cpf, String passwd, String mail, String personalMail, String ddd, String phone, String cep, String address, String areaAlias, String bossCpf, String justify, String ldapKeyForCorpAuth){
		return Member.update(getConnectedUser(), memberOperation, name, cpf, passwd, mail, personalMail, ddd, phone, cep, address, areaAlias, bossCpf, justify, ldapKeyForCorpAuth);
	}
	
	/**
	 * Retorna os usurios de um grupo
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @return Array contendo os usurios do grupo
	 */
	public MemberDTO[] listarMemberOfGroup(String group) {
		return getGroupMember().listar(getConnectedUser(), group);
	}

	/**
	 * Mtodo para ativar um usurio
	 * @param memberOperation id do usurio para ativar
	 * @param justify Justificativa da operao
	 * @return 0 caso sucesso
	 */
	public  LoginDTO active(String memberOperation, String justify){
		return Member.active(getConnectedUser(), memberOperation, justify);
	}
	
	/**
	 * Mtodo para desativar um usurio
	 * @param memberOperation id do usurio para desativar
	 * @param justify Justificativa para a operao
	 * @return 0 caso sucesso
	 */
	public  LoginDTO inactive(String memberOperation, String justify){
		return Member.inactive(getConnectedUser(), memberOperation, justify);
	}
		
	/**
	 * Lista os usurios de um grupo
	 * 
	 * @param name
	 *            Nome do usurio para filtrar
	 * @param status
	 *            Status do usurio, vide dominios
	 * @param group
	 *            Apelido do grupo
	 * @return Array contendo os usurio encontrados
	 */
	public MemberDTO[] listar(String name, String status, String group) {
		return getGroupMember().listar(getConnectedUser(), name, status, group);
	}

	/**
	 * Retorna um usurio de acordo com o seu CPF e grupo
	 * 
	 * @param userCpf
	 *            Cpf do usurio
	 * @param group
	 *            Apelido do grupo
	 * @return Array contendo o usurio, null caso no encontrado no grupo
	 */
	public MemberDTO[] listarByGroup(String userCpf, String group) {
		return getGroupMember().listar(getConnectedUser(), userCpf, group);
	}

	
	/**
	 * Lista os membros do sistema
	 * @param name Filtro pelo nome
	 * @param status Filtro por status, vide Dominios
	 * @return Array contendo detalhes dos usurios
	 */
	public MemberDTO[] listar(String name, String status){
		return getMembers().listar(getConnectedUser(), name, status);
	}
	
	/**
	 * Lista os membros do sistema
	 * @return Array contendo detalhes dos usurios
	 */
	public MemberDTO[] listar(){
		return getMembers().listar(getConnectedUser(), null, null);
	}
	
	/**
	 * Exibe os detalhes de um usurio de acordo com o seu CPF
	 * @param userMail CPF do usurio
	 * @return Array contendo detalhes do usurio, nulo caso no encontrado
	 */
	public MemberDTO listar(String userMail){
		MemberDTO[] array = getMembers().listar(getConnectedUser(), userMail);
		return array[0];
	}
	
	/**
	 * Retorna um usurio de acordo com o seu Indentificador e grupo
	 * 
	 * @param userId
	 *            Identificador do usurio
	 * @param group
	 *            Apelido do grupo
	 * @return Array contendo o usurio, null caso no encontrado no grupo
	 */
	public MemberDTO[] listar(Integer userId, String group) {
		return getGroupMember().listar(getConnectedUser(), userId, group);
	}

	/**
	 * Exibe os detalhes de um usurio de acordo com o seu Identificador
	 * @param userId Identificador do usurio
	 * @return Array contendo detalhes do usurio, nulo caso no encontrado
	 */
	public MemberDTO listar(Integer userId){
		if(userId!= null){
			if(userId == 0)
				userId = null;
		}
			
		MemberDTO[] array = getMembers().listar(getConnectedUser(), userId);
		if(array!= null && array.length > 0)
			return array[0];
		
		return null;
	}
	
	public List<InvitationDTO> listarMembrosConvidados(Integer groupId) {
		InvitationDTO[] array = InvitedMembers.listInvitedMembers(getConnectedUser(), groupId);
		if (array == null) {
			return null;
		}
		
		return Arrays.asList(array);
	}
	
	public SimpleDTO activateInvitedMember(Integer groupId, String memberMail, Integer qtdDays, String[] svPrivileges) {
		SimpleDTO dto = InvitedMembers.acceptIvitedMembers(getConnectedUser(), groupId, memberMail, qtdDays, svPrivileges);
		if (dto == null) {
			return null;
		}
		
		return dto;
	}
	
	public SimpleDTO refuseInvitedMember(Integer groupId, String memberMail, String justify) {
		SimpleDTO dto = InvitedMembers.refuseIvitedMembers(getConnectedUser(), groupId, memberMail, justify);
		
		return dto;
	}
	
	
	public MemberDTO[] listarMemberByArea(Integer areaIdFk){
		return ListMemberByArea.returnMemberByArea(getConnectedUser(), areaIdFk);
	}
	
	

	/**
	 * Método utilizado para retornar os membros com 
	 * um status específco, por exemplo para retornar
	 * os membros pendentes basta passar como parâmetro
	 * <b"CADASTRADO"</b> pertence aos dominios cadastrados
	 * previamente na tabela QWCSS_DOMAIN
	 * @param status
	 * @return
	 */
	public MemberDTO[] listarByStatus(String status){
		return getMembers().listarByStatus(getConnectedUser(), status);
	}
	
	
	/**
	 *  Metodo utilizado para retornar registros da view de grupos contendo referências dos membros
	 *  participantes.
	 * @param groupAlias
	 * @return
	 */
	public MyGroupsEXDTO[] listarByGroupView(String groupAlias){
		return getGroupMember().listarByGroupView(getConnectedUser(), groupAlias);
	}
	
	public MemberDTO lisarMemberByEmail(String email){
		return ListMembersByEmail.returnMemberByEmail(getConnectedUser(), email);
	}
	
	/**
	 *  Troca a senha do usurio sem precisar logar no webservice.
	 * @param email email do usurio 
	 * @return
	 */
	public SimpleDTO trocarSenha( String email) {
		return MemberEx.changePass( email);
	}
	
	
	private Members getMembers(){
		Members members = new Members();
		return members;
	}
	
	private GroupMembers getGroupMember() {
		GroupMembers groupMembers = new GroupMembers();
		return groupMembers;
	}

	
}
