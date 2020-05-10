package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.transfer.ReceberArquivo;
import br.com.qwcss.ws.dto.TransferConfigDTO;

public class ReceberArquivoService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private ReceberArquivo receberArquivo;
	
	@Override
	public void init() {
		super.init();
		receberArquivo = new ReceberArquivo(getConnectedUser(), true);
	}
	

	/**
	 * Recebe uma verso
	 * @param versionId Identificador da verso
	 * @param saveAsFullPath Caminho completo para salvar o arquivo
	 * @return Objeto de envio de arquivo, o caso sucesso
	 */
	public TransferConfigDTO receber_versao(Integer versionId, String saveAsFullPath){
		return receberArquivo.receber_versao(versionId, saveAsFullPath);
	}

	/**
	 * Recebe uma verso passando o nome e o grupo
	 * @param fileName Nome do arquivo
	 * @param group Grupo do arquivo
	 * @param saveAsFullPath Caminho completo para salvar o arquivo
	 * @return Objeto de envio de arquivo, o caso sucesso
	 */
	public TransferConfigDTO receber_versao(String fileName, String group, String saveAsFullPath){
		return receberArquivo.receber_versao(fileName, group, saveAsFullPath);
	}
	
	/**
	 * Recebe uma verso passando o nome o grupo e a verso
	 * @param fileName Nome do arquivo
	 * @param group Apelido do grupo
	 * @param versionNumber Nmero da verso
	 * @param saveAsFullPath Caminho completo para salvar o arquivo
	 * @return Objeto de envio de arquivo, o caso sucesso
	 */
	public TransferConfigDTO receber_versao(String fileName, String group, Integer versionNumber, String saveAsFullPath){
		return receberArquivo.receber_versao(fileName, group, versionNumber, saveAsFullPath);
	}
	
	
	/**
	 * Recebe uma verso de uma caixa postal
	 * @param fileName Nome do arquivo
	 * @param group Grupo do arquivo
	 * @param saveAsFullPath Caminho completo para salvar o arquivo
	 * @return Objeto de envio de arquivo, o caso sucesso
	 */
	public TransferConfigDTO receber_versao_pobox(String fileName, String group, String saveAsFullPath){
		return receberArquivo.receber_versao_pobox(fileName, group, saveAsFullPath);
	}
	
	/**
	 * Recebe a verso de uma caixa postal
	 * @param fileName Nome do arquivo
	 * @param group Apelido do grupo
	 * @param versionNumber Nmero da verso
	 * @param saveAsFullPath Caminho completo para salvar o arquivoMOMO
	 * @return
	 */
	public TransferConfigDTO receber_versao_pobox(String fileName, String group, Integer versionNumber, String saveAsFullPath){
		return receberArquivo.receber_versao_pobox(fileName, group, versionNumber, saveAsFullPath);
	}
	
	
	
	
	

}
