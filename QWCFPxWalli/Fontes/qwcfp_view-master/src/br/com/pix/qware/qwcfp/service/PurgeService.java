package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.manager.Purge;
import br.com.qwcss.ws.dto.SimpleDTO;

public class PurgeService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Envia comando para expourgar arquivos que tenham vencido a validade
	 * @param groupId Identificador do grupo
	 * @return 0 caso sucesso
	 */
	public  SimpleDTO expurgarArquivosDeUmGrupo(Integer groupId){
		return Purge.expurgarArquivosDeUmGrupo(getConnectedUser(), groupId);
	}
	
	/**
	 * Envia comando para expurgar um arquivo
	 * @param fileId Identificador do arquivo
	 * @return 0 caso sucesso
	 */
	public  SimpleDTO expurgarArquivo(Integer fileId){
		return Purge.expurgarArquivo(getConnectedUser(), fileId);
	}
	
	/**
	 * Adia a data de validade de um arquivo
	 * @param fileId Identificador do arquivo
	 * @param qtdDias Quantidade de dias para adiar
	 * @return 0 caso sucesso
	 */
	public  SimpleDTO adiarDataValidadeArquivo(Integer fileId, Integer qtdDias){
		return Purge.adiarDataValidadeArquivo(getConnectedUser(), fileId, qtdDias);
	}
	
	/**
	 * Adiar a data de validade dos arquivos de um grupo
	 * @param groupId Identificador do grupo
	 * @param qtdDias Quantidade de dias para adiar
	 * @return 0 caso sucesso
	 */
	public  SimpleDTO adiarDataValidadeGrupo(Integer groupId, Integer qtdDias){
		return Purge.adiarDataValidadeGrupo(getConnectedUser(), groupId, qtdDias);
	}
	
	
	
}
