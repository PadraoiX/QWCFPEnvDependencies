package br.com.pix.qware.qwcfp.service;

import java.util.Calendar;

import br.com.pix.qware.qwcfp.beans.uties.QtdeNotificacoesBean;
import br.com.pix.qwcfp.ws.client.list.Files;
import br.com.pix.qwcfp.ws.client.manager.File;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;
import br.com.qwcss.ws.dto.SimpleDTO;

public class FileManagedService extends Service{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L ;

	/**
	 * Operao para deletar um arquivo e suas verses
	 * 
	 * @param idFileManaged
	 *            Identificador do arquivo
	 * @return 0 caso sucesso
	 */
	public SimpleDTO deleteFileManaged(Integer idFileManaged) {
		return File.deleteFileManaged(getConnectedUser(), idFileManaged);
	}
	
	/** cv 
	 * Operao para deletar um arquivo e suas verses
	 * 
	 * @param idFileManaged
	 *            Identificador do arquivo
	 * @return 0 caso sucesso
	 */
	public SimpleDTO deleteFileManagedEx(Integer idFileManaged) {
		return File.deleteFileManagedEx(getConnectedUser(), idFileManaged);
	}

	/**
	 * Operao para deletar uma verso
	 * 
	 * @param idFileVersion
	 *            Identificado da verso
	 * @return 0 caso sucesso
	 */
	public SimpleDTO deleteVersion(Integer idFileVersion) {
		return File.deleteVersion(getConnectedUser(), idFileVersion);
	}
	
	/**
	 * Operao para deletar uma verso
	 * 
	 * @param idFileVersion
	 *            Identificado da verso
	 * @return 0 caso sucesso
	 */
	public SimpleDTO deleteVersionEx(Integer idFileVersion) {
		return File.deleteVersionEx(getConnectedUser(), idFileVersion);
	}

	/**
	 * Cancela a transferncia de uma verso
	 * 
	 * @param versionId
	 *            Identificador da verso
	 * @return 0 caso sucesso
	 */
	public SimpleDTO cancelVersionTransfer(Integer idFileVersion) {
		return File.cancelVersionTransfer(getConnectedUser(), idFileVersion);
	}

	/**
	 * Operao para mover uma verso para outro grupo, com condio NEW (caso tenha um arquivo com o mesmo nome,
	 * cria-se um novo)
	 * 
	 * @param idFileVersion
	 *            Identificador da verso
	 * @param groupTo
	 *            Identificador do grupo de destino
	 * @return 0 caso sucesso
	 */
	public SimpleDTO moveVersionNew(Integer idFileVersion, Integer groupTo) {
		return File.moveVersionNew(getConnectedUser(), idFileVersion, groupTo);
	}

	/**
	 * Operao para mover uma verso para outro grupo, com condio REP (caso tenha um arquivo com o mesmo nome,
	 * substitui-se)
	 * 
	 * @param idFileVersion
	 *            Identificador da verso
	 * @param groupTo
	 *            Identificador do grupo de destino
	 * @return 0 caso sucesso
	 */
	public SimpleDTO moveVersionRep(Integer idFileVersion, Integer groupTo) {
		return File.moveVersionRep(getConnectedUser(), idFileVersion, groupTo);
	}
	
	public SimpleDTO moveFileNew(Integer idFile, Integer groupTo) {
		return File.moveFileNew(getConnectedUser(), idFile, groupTo);
	}
	
	public SimpleDTO moveFileRep(Integer idFile, Integer groupTo) {
		return File.moveFileRep(getConnectedUser(), idFile, groupTo);
	}

	/**
	 * Operao para copiar uma verso para outro grupo, com condio NEW (caso tenha um arquivo com o mesmo nome,
	 * cria-se um novo)
	 * 
	 * @param idFileVersion
	 *            Identificador da verso
	 * @param groupTo
	 *            Identificado do grupo de destino
	 * @return 0 caso sucesso
	 */
	public SimpleDTO copyVersionNew(Integer idFileVersion, Integer groupTo) {
		return File.copyVersionNew(getConnectedUser(), idFileVersion, groupTo);
	}
	
	
	/**
	 * 	 Operação para editar o comentario de uma versão.
	 * 
	 * @param idFileVersion id da versão a ser alterada
	 * @param addInfo novo comentário
	 * @return 0 caso sucesso
	 */
	public SimpleDTO editAddInfo(Integer idFileVersion, String addInfo) {
		return File.editAddInfo(getConnectedUser(), idFileVersion, addInfo);
	}

	/**
	 * Operao para copiar uma verso para outro grupo, com condio REP (caso tenha um arquivo com o mesmo nome,
	 * cria-se um novo)
	 * 
	 * @param idFileVersion
	 *            Identificador da verso
	 * @param groupTo
	 *            Identificador do grupo de destino
	 * @return 0 caso sucesso
	 */
	public SimpleDTO copyVersionRep(Integer idFileVersion, Integer groupTo) {
		return File.copyVersionRep(getConnectedUser(), idFileVersion, groupTo);
	}

	/**
	 * Lista os arquivos de um grupo
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo para filtrar, caso nulo, retorna todos
	 * @param ilike 
	 * @param sortBy 
	 * @param sortByField 
	 * @return Array com a lista de arquivos
	 */
	public FileListDTOEx[] listar(String group, String fileName, Integer pager, Integer records, boolean ilike, String sortBy, String sortByField) {
		return Files.listar(getConnectedUser(), group, fileName, pager, records, ilike, sortBy, sortByField);
	}
	
	public FileListDTOEx[] listar(String group, String fileName, Integer pager, Integer records, String sortBy, String sortByField) {
		return Files.listar(getConnectedUser(), group, fileName, pager, records, false, sortBy, sortByField);
	}

	/**
	 * Lista os arquivos de um grupo, filtrando por data e nome de arquivo
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo, caso nulo retorna todos
	 * @param dataInicio
	 *            Data de inicio a filtrar
	 * @param dataFim
	 *            Data de fil a filtrar
	 * @return Array com a lista de arquivos resultados
	 */
	public FileListDTO[] listar(String group,
			String fileName,
			Calendar dataInicio,
			Calendar dataFim,
			Integer pager,
			Integer records) {
		return Files.listar(getConnectedUser(), group, fileName, dataInicio, dataFim, pager, records);
	}

	/**
	 * Lista os arquivos de uma inbox do usurio que est conectado
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo, caso nulo retona todos
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarInbox(String group, String fileName, Integer pager, Integer records) {
		return Files.listarInbox(getConnectedUser(), group, fileName, pager, records);
	}

	/**
	 * Lista os arquivos de uma inbox do usurio que est conectado, filtrando por nome de arquivo e data
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo, caso nulo retorna todos
	 * @param dataInicio
	 *            Data de inicio de criao do arquivo a filtrar
	 * @param dataFim
	 *            Data de Fim de criao do arquivos a filtrar
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarInbox(String group,
			String fileName,
			Calendar dataInicio,
			Calendar dataFim,
			Integer pager,
			Integer records) {
		return Files.listar(getConnectedUser(), group, fileName, dataInicio, dataFim, pager, records);
	}

	/**
	 * Lista os arquivos de uma outbox do usurio que est conectado
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo, caso nulo retona todos
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarOutbox(String group, String fileName, Integer pager, Integer records) {
		return Files.listarOutbox(getConnectedUser(), group, fileName, pager, records);
	}

	/**
	 * Lista os arquivos de uma outbox do usurio que est conectado, filtrando por nome de arquivo e data
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo, caso nulo retorna todos
	 * @param dataInicio
	 *            Data de inicio de criao do arquivo a filtrar
	 * @param dataFim
	 *            Data de Fim de criao do arquivos a filtrar
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarOutbox(String group,
			String fileName,
			Calendar dataInicio,
			Calendar dataFim,
			Integer pager,
			Integer records) {
		return Files.listarOutbox(getConnectedUser(), group, fileName, dataInicio, dataFim, pager, records);
	}

	/**
	 * Listar arquivos que venceram a validade, opo apenas para gestores de infra-estrutura
	 * @param pager 
	 * @param qtdRecords 
	 * 
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarPurgedGiOutOfDate(Integer pager, Integer qtdRecords) {
		return Files.listarPurgedGiOutOfDate(getConnectedUser(), pager ,qtdRecords);
	}

	/**
	 * Listar arquivos que venceram a validade e esto com status para expurgo - espera autorizao - , opo apenas
	 * para gestores de infra-estrutura
	 * @param pager 
	 * @param qtdRecords 
	 * 
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarPurgedGiPurged(Integer pager, Integer qtdRecords) {
		return Files.listarPurgedGiPurged(getConnectedUser(), pager ,qtdRecords);
	}

	/**
	 * Listar arquivos que venceram a validade e esto prontos para serem expurgados - j assinados - , opo apenas
	 * para gestores de infra-estrutura
	 * @param pager 
	 * @param qtdRecords 
	 * 
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarPurgedGiPurgedAllowed(Integer pager, Integer qtdRecords) {
		return Files.listarPurgedGiPurgedAllowed(getConnectedUser(), pager ,qtdRecords);
	}

	/**
	 * Listar arquivos que venceram a validade, opo apenas para gestores do grupo
	 * 
	 * @param groupAlias
	 *            Apelido do grupo
	 * @param pager 
	 * @param qtdRecords 
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarPurgedOutOfDate(String groupAlias, Integer pager, Integer qtdRecords) {
		return Files.listarPurgedOutOfDate(getConnectedUser(), groupAlias, pager ,qtdRecords);
	}

	/**
	 * Listar arquivos que venceram a validade e esto com status para expurgo - espera autorizao - , opo apenas
	 * para gestores do grupo
	 * 
	 * @param groupAlias
	 *            Apelido do grupo
	 * @param pager 
	 * @param qtdRecords 
	 * @return Array contendo os arquivos
	 */
	public FileListDTO[] listarPurgedPurged(String groupAlias, Integer pager, Integer qtdRecords) {
		return Files.listarPurgedPurged(getConnectedUser(), groupAlias, pager ,qtdRecords);
	}

	/**
	 * Listar arquivos que venceram a validade e estão prontos para serem expurgados -já assinados- , opção apenas
	 * para gestores do grupo
	 * 
	 * @param groupAlias -> Apelido do grupo
	 * @param pager 
	 * @param qtdRecords 
	 * @return -> Array contendo os arquivos
	 */
	public FileListDTO[] listarPurgedPurgedAllowed(String groupAlias, Integer pager, Integer qtdRecords) {
		return Files.listarPurgedPurgedAllowed(getConnectedUser(), groupAlias, pager ,qtdRecords);
	}

}
