package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.manager.Config;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

public class ConfigService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Insere uma nova Config no banco de dados
	 * 
	 * @param key
	 * @param value
	 * @param context
	 * @param QwcssArea
	 *            area a ser inserido
	 * @throws ServiceException
	 */
	public SimpleDTO inserir(String key, String value, String context) {
			SimpleDTO configDTO = Config.insert(getConnectedUser(), key, value, context);
			return configDTO;
	}

	/**
	 * Altera uma Config cadastrada no banco de dados.
	 * 
	 * @param key
	 * @param value
	 * @param context
	 * @param QwcssAreaDAO
	 *            area
	 */
	public SimpleDTO alterar(String key, String value, String context) {
		SimpleDTO configDto = Config.update(getConnectedUser(), key, value, context);
		return configDto;
	}

	/**
	 * Exclui uma Config do banco de dados
	 * 
	 * @param key
	 * @param context
	 * @param Long
	 *            areaId
	 * @throws ServiceException
	 */
	public SimpleDTO excluir(String key, String context) {
		try {

			return Config.delete(getConnectedUser(), key, context);

		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * L todas as Config cadastradas no banco de dados
	 * 
	 * @return Lista de areas cadastradas
	 * @throws ServiceException
	 */
	public ConfigDTO[] listarConfig() {
		return br.com.pix.qwcfp.ws.client.list.Config.listar(getConnectedUser());
	}

	/**
	 * Carrega uma configurao a partir da chave primaria
	 * 
	 * @param key
	 */
	public ConfigDTO[] carregarByKey(String key) {
		return br.com.pix.qwcfp.ws.client.list.Config.configuracao(getConnectedUser(),key);
	}

	/**
	 * Carrega uma configurao a partir da chave primaria
	 * 
	 * @param key
	 */
	public ConfigDTO[] carregarByContext(String key) {
		return br.com.pix.qwcfp.ws.client.list.Config.configuracaoContext(getConnectedUser(),key);
	}
	
	/**
	 * Carrega uma configurao a partir da chave primaria
	 * 
	 * @param key
	 */
	public ConfigDTO  carregar(String key, String context) {
		ConfigDTO[] configArray = br.com.pix.qwcfp.ws.client.list.Config.carregar(getConnectedUser(), key, context);
		
		if(configArray != null && configArray.length > 0)
			return configArray[0];
		else
			return null; 
	}



}
