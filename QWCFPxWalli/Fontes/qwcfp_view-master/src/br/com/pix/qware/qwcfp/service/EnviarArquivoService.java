package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.transfer.EnviarArquivo;
import br.com.pix.qwcfp.ws.client.transfer.EnviarArquivoEx;
import br.com.qwcss.ws.dto.TransferConfigDTO;

public class EnviarArquivoService extends Service{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Envia um arquivo com condio Versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional da verso
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO versionado_novo(String group, String aditionalInformation, String sourceFileFullPath){
		return EnviarArquivo.versionado_novo(getConnectedUser(), group, aditionalInformation, sourceFileFullPath);
	}
	public TransferConfigDTO versionado_novo(String group, String aditionalInformation, String sourceFileFullPath, Long fileSize){
		return EnviarArquivo.versionado_novo(getConnectedUser(), group, aditionalInformation, sourceFileFullPath, fileSize);
	}
	
	
	/**
	 * Envia um arquivo com condio versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional da verso
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO versionado_replace(String group, String aditionalInformation, String sourceFileFullPath){
		return EnviarArquivo.versionado_replace(getConnectedUser(), group, aditionalInformation, sourceFileFullPath);
	}
	public TransferConfigDTO versionado_replace(String group, String aditionalInformation, String sourceFileFullPath, Long fileSize){
		return EnviarArquivoEx.versionado_replace(getConnectedUser(), group, aditionalInformation, sourceFileFullPath, fileSize);
	}
	
	/**
	 * Envia um arquivo com condio no versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional da verso
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO naoVersionado_novo(String group, String aditionalInformation, String sourceFileFullPath){
		return EnviarArquivo.naoVersionado_novo(getConnectedUser(), group, aditionalInformation, sourceFileFullPath);
	}
	public TransferConfigDTO naoVersionado_novo(String group, String aditionalInformation, String sourceFileFullPath, Long fileSize){
		return EnviarArquivo.naoVersionado_novo(getConnectedUser(), group, aditionalInformation, sourceFileFullPath, fileSize);
	}
	
	
	/**
	 * Envia um arquivo com cindição versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional da verso
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO naoVersionado_replace(String group, String aditionalInformation, String sourceFileFullPath){
		return EnviarArquivo.naoVersionado_replace(getConnectedUser(),group, aditionalInformation, sourceFileFullPath);
	}
	public TransferConfigDTO naoVersionado_replace(String group, String aditionalInformation, String sourceFileFullPath, Long fileSize){
		return EnviarArquivoEx.naoVersionado_replace(getConnectedUser(), group, aditionalInformation, sourceFileFullPath, fileSize);
	}
	
	/**
	 * Envia um arquivo para caixa postal do usurio com condio versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @param inbox [INBOX : OUTBOX]
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO pobox_versionado_novo(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox){
		return EnviarArquivo.pobox_versionado_novo(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox);
	}
	public TransferConfigDTO pobox_versionado_novo(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox, Long fileSize){
		return EnviarArquivo.pobox_versionado_novo(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox, fileSize);
	}
	
	/**
	 * Envia um arquivo para caixa postal do usurio com condio versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informação adicional do arquivo
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @param inbox [INBOX : OUTBOX]
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO pobox_versionado_replace(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox){
		return EnviarArquivo.pobox_versionado_replace(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox);
	}
	public TransferConfigDTO pobox_versionado_replace(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox, Long fileSize){
		return EnviarArquivo.pobox_versionado_replace(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox, fileSize);
	}
	

	/**
	 * Envia um arquivo para caixa postal do usurio com condio no versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @param inbox [INBOX : OUTBOX]
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO pobox_naoVersionado_novo(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox){
		return EnviarArquivo.pobox_naoVersionado_novo(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox);
	}
	public TransferConfigDTO pobox_naoVersionado_novo(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox, Long fileSize){
		return EnviarArquivo.pobox_naoVersionado_novo(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox, fileSize);
	}
	
	/**
	 * Envia um arquivo para caixa postald o usurio com condio no versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sourceFileFullPath Caminho completo do arquivo
	 * @param inbox [INBOX : OUTBOX]
	 * @return Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO pobox_naoVersionado_replace(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox){
		return EnviarArquivo.pobox_naoVersionado_replace(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox);
	}
	public TransferConfigDTO pobox_naoVersionado_replace(String group, String aditionalInformation, String sourceFileFullPath, boolean inbox, Long fileSize){
		return EnviarArquivo.pobox_naoVersionado_replace(getConnectedUser(),group, aditionalInformation, sourceFileFullPath, inbox, fileSize);
	}
	
	

}
