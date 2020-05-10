package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.manager.ProcessQueue;
import br.com.qwcss.ws.dto.SimpleDTO;

public class ProcessQueueService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5240180071795203222L;
	
	/**
	 * Método para inserir um processo para a fila de processamento
	 * de um arquivo na hora de upload
	 * @param connectedUser
	 * @param versionId
	 * @return
	 */
	public SimpleDTO insertNewFile(Integer versionId){
		return ProcessQueue.insertNewFile(getConnectedUser(), versionId);
	}
	
	
	/**
	 * Método para inserir um processo para a fila de processamento
	 * de um arquivo na hora de upload
	 * @param connectedUser
	 * @param versionId
	 * @return
	 */
	public SimpleDTO repleaceFile(Integer versionId){
		return ProcessQueue.repleaceFile(getConnectedUser(), versionId);
	}
	
	

}
