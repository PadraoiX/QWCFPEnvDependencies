package br.com.pix.qware.qwcfp.service;

import java.io.File;

import br.com.pix.qwcfp.ws.client.transfer.EnviarVariosArquivos;
import br.com.qwcss.ws.dto.TransferConfigDTO;

public class EnviarVariosArquivosService extends Service{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private EnviarVariosArquivos enviarArquivos;

	public EnviarVariosArquivosService(boolean onlyWsContract){
		enviarArquivos = new EnviarVariosArquivos(getConnectedUser(), onlyWsContract);
	}
	
	/**
	 * Envia arquivo de um diretrio com condio versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] versionado_novo_director(String group, String aditionalInformation, String sendDirectory){
		return enviarArquivos.versionado_novo_director(group, aditionalInformation, sendDirectory);
	}
	
	/**
	 * Envia arquivos de um diretrio com condio versionado replace
	 * @param group Identificador do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] versionado_replace(String group, String aditionalInformation, String sendDirectory){
		return enviarArquivos.versionado_replace(group, aditionalInformation, sendDirectory);
	}
	
	/**
	 * Envia arquivos de um diretrio com condio no versionado novo
	 * @param group Identificador do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] naoVersionado_novo(String group, String aditionalInformation, String sendDirectory){
		return enviarArquivos.naoVersionado_novo(group, aditionalInformation, sendDirectory);
	}
	
	/**
	 * Envia arquivos de um diretrio com condio no versionado replace
	 * @param group Identificador do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] naoVersionado_replace(String group, String aditionalInformation, String sendDirectory){
		return enviarArquivos.naoVersionado_replace(group, aditionalInformation, sendDirectory);
	}
	
	/**
	 * Envia arquivos de um diretrio com condio versionado novo para a caixa postal do usurio
	 * @param group Identificador do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_versionado_novo(String group, String aditionalInformation, String sendDirectory, boolean inbox){
		return enviarArquivos.pobox_versionado_novo(group, aditionalInformation, sendDirectory, inbox);
	}
	

	/**
	 * Envia arquivos de um diretrio com condio versionado replace para a caixa postal do usurio
	 * @param group Identificador do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_versionado_replace(String group, String aditionalInformation, String sendDirectory, boolean inbox){
		return enviarArquivos.pobox_versionado_replace(group, aditionalInformation, sendDirectory, inbox);
	}
	
	/**
	 * Envia arquivos de um diretrio com condio no versionado novo para a caixa postal do usurio
	 * @param group Identificador do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_naoVersionado_novo(String group, String aditionalInformation, String sendDirectory, boolean inbox){
		return enviarArquivos.pobox_naoVersionado_novo(group, aditionalInformation, sendDirectory, inbox);
	}
	
	/**
	 * Envia arquivos de um diretrio com condio no versionado replace para a caixa postal do usurio
	 * @param group Identificador do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param sendDirectory Diretrio de envio
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_naoVersionado_replace(String group, String aditionalInformation, String sendDirectory, boolean inbox){
		return enviarArquivos.pobox_naoVersionado_replace(group, aditionalInformation, sendDirectory, inbox);
	}
	
	/**
	 * Envia vriso arquivos com condio versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] versionado_novo(String group, String aditionalInformation, File[] files){
		return enviarArquivos.versionado_novo(group, aditionalInformation, files);
	}
	
	/**
	 * Envia vriso arquivos com condio versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] versionado_replace(String group, String aditionalInformation, File[] files){
		return enviarArquivos.versionado_replace(group, aditionalInformation, files);
	}
	
	
	
	/**
	 * Envia vriso arquivos com condio no versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] naoVersionado_novo(String group, String aditionalInformation, File[] files){
		return enviarArquivos.naoVersionado_novo(group, aditionalInformation, files);
	}
	
	/**
	 * Envia vriso arquivos com condio no versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] naoVersionado_replace(String group, String aditionalInformation, File[] files){
		return enviarArquivos.naoVersionado_replace(group, aditionalInformation, files);
	}
	
	/**
	 * Envia vriso arquivos para a caixa postal do usurio com condio versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_versionado_novo(String group, String aditionalInformation, File[] files, boolean inbox){
		return enviarArquivos.pobox_versionado_novo(group, aditionalInformation, files, inbox);
	}
	
	/**
	 * Envia vriso arquivos para a caixa postal do usurio com condio versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_versionado_replace(String group, String aditionalInformation, File[] files, boolean inbox){
		return enviarArquivos.pobox_versionado_replace(group, aditionalInformation, files, inbox);
	}
	

	/**
	 * Envia vriso arquivos para a caixa postal do usurio com condio no versionado novo
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_naoVersionado_novo(String group, String aditionalInformation, File[] files, boolean inbox){
		return enviarArquivos.pobox_naoVersionado_novo(group, aditionalInformation, files, inbox);
	}
	
	/**
	 * Envia vriso arquivos para a caixa postal do usurio com condio no versionado replace
	 * @param group Apelido do grupo
	 * @param aditionalInformation Informao adicional do arquivo
	 * @param files Array de arquivos
	 * @param inbox
	 * @return Array de Objeto de envio de arquivo, 0 caso sucesso
	 */
	public TransferConfigDTO[] pobox_naoVersionado_replace(String group, String aditionalInformation, File[] files, boolean inbox){
		return enviarArquivos.pobox_naoVersionado_replace(group, aditionalInformation, files, inbox);
	}
}
