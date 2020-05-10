package br.com.pix.qware.qwcfp.service;

import java.util.Calendar;

import br.com.pix.qwcfp.ws.client.list.VersionsEx;
import br.com.qwcss.ws.dto.VersionDetailDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

public class FileVersionListService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Lista as verses de acordo com o identificador do arquivo
	 * 
	 * @param fileId
	 *            Identificador do arquivo
	 * @return Array com os detalhes das verses
	 */
	public VersionsDTO[] listar(Integer fileId) {
		return Versions().listar(getConnectedUser(), fileId);
	}
	
	
	public VersionDetailDTO[] executeListFileById(Integer[] ids, Integer records, Integer pager) {
		return br.com.pix.qwcfp.ws.client.list.Versions.executeListFileById(getConnectedUser(), ids, records, pager);

	}

	/**
	 * Listas as versões de acordo com o grupo e status do arquivo
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param status
	 *            Status dos arquivos, vide dominios
	 * @return Array com a lista dos arquivos retornados
	 */
	public VersionsDTO[] listar(String group, String status) {
		return Versions().listar(getConnectedUser(), group, status);
	}

	/**
	 * Lista as verses contidas em um grupo de acordo com o nome do arquivo e status
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo para filtrar
	 * @param status
	 *            Status do arquivo, vide dominios
	 * @return Array contendo as verses
	 */
	public VersionsDTO[] listar(String group, String fileName, String status) {
		return Versions().listar(getConnectedUser(), group, fileName, status);
	}

	/**
	 * Lista as verses de um grupo, filtrando pelo nome do arquivo, data e status
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo
	 * @param status
	 *            Status, vide dominios
	 * @param dataInicio
	 *            Data de ininio
	 * @param dataFim
	 *            Data de fim
	 * @return Array contendo as veres
	 */
	public VersionsDTO[] listar(String group,
			String fileName,
			String status,
			Calendar dataInicio,
			Calendar dataFim) {
		return Versions().listar(getConnectedUser(), group, fileName, status, dataInicio, dataFim);
	}

	/**
	 * Retorna as veres de um grupo, filtranso por nome, status, data e executando expreses regular no banco de dados
	 * 
	 * @param group
	 *            Apelido do grupo
	 * @param fileName
	 *            Nome do arquivo
	 * @param status
	 *            Status, vide dominios
	 * @param dataInicio
	 *            Data de inico
	 * @param dataFim
	 *            Data de fim
	 * @param regex
	 *            Expreses regular
	 * @param noRegex
	 *            Negao da expresso reguar
	 * @return Array das veres encontradas
	 */
	public VersionsDTO[] listar(String group,
			String fileName,
			String status,
			Calendar dataInicio,
			Calendar dataFim,
			String regex,
			String noRegex) {
		return Versions().listar(getConnectedUser(), group, fileName, status, dataInicio, dataFim, regex, noRegex);
	}

	/**
	 * Lista a ultima versão do arquivo
	 * 
	 * @param fileId
	 *            Identificador do arquivo
	 * @return Array com os detalhes das verses
	 */
	public VersionsDTO listarUltimaVersao(Integer fileId) {

		VersionsDTO[] versions = Versions().listar(getConnectedUser(), fileId);
		VersionsDTO lastVersion = new VersionsDTO();
		lastVersion.setVersion(0);

		if (versions != null) {

			for (int i = 0; i < versions.length; i++) {
				lastVersion = versions[i].getVersion() > lastVersion.getVersion() ? versions[i] : lastVersion;
			}
		}

		return lastVersion;
	}

	public VersionsDTO listarVersao(Integer fileId, Integer fileVersion) {

		VersionsDTO[] versions = Versions().listar(getConnectedUser(), fileId);

		if (versions != null) {

			for (int i = 0; i < versions.length; i++) {
				if (versions[i].getId().compareTo(fileVersion) == 0) {
					return versions[i];
				}
			}
		}

		return null;
	}

	private VersionsEx Versions() {
		VersionsEx versions = new VersionsEx();
		return versions;
	}
	
	public VersionDetailDTO listarVersionDetail(Integer versionId) {
		return Versions().listarDetail(getConnectedUser(), versionId);
	}

}
