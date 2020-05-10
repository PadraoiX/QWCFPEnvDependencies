package br.com.pix.qware.qwcfp.service;

import java.math.BigDecimal;

import br.com.pix.qwcfp.ws.client.list.Volumes;
import br.com.pix.qwcfp.ws.client.manager.Volume;
import br.com.qwcss.ws.dto.VolumeDTO;



public class VolumeInformationService extends Service {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5794660996671699679L;
	
	
	/**
	 * Inserir um novo volume
	 * @param status Status do volume, vide dominios
	 * @param rootFullPathVol Caminho completo do volume
	 * @param sizeInBytes Tamanho do volume em bytes
	 * @return 0 caso sucesso
	 */
	public  VolumeDTO insert(String status, String rootFullPathVol, BigDecimal sizeInBytes){
		return Volume.insert(getConnectedUser(), status, rootFullPathVol, sizeInBytes);
	}
	
	/**
	 * Atualizar um volume
	 * @param volId Identificador do volume
	 * @param status Status do volume, vide dominios
	 * @param rootFullPathVol Caminho completo do volume
	 * @param sizeInBytes Tamanho em bytes do volume
	 * @return 0 caso sucesso
	 */
	public  VolumeDTO update(Integer volId, String status, String rootFullPathVol, BigDecimal sizeInBytes){
		return Volume.update(volId, status, rootFullPathVol, sizeInBytes);
	}
	

	/**
	 * Deletar um volume
	 * @param volId Identificado do volume
	 * @return 0 caso sucesso
	 */
	public  VolumeDTO delete(Integer volId){
		return Volume.delete(volId);
	}
	
	
	/**
	 * Retornas os volumes cadastrados no sistema
	 * @return Array com os detalhes do volume
	 */
	public  VolumeDTO[] volumes(){
		return Volumes.volumes(getConnectedUser());
	}
	
	/**
	 * Retorna os detalhes de um colume de acordo com o Identificador
	 * @param volumeId Identificador do volume
	 * @return Array contendo os detalhes do volume
	 */
	public  VolumeDTO volumes(Integer volumeId){
		VolumeDTO[] volumeDTO = Volumes.volumes(getConnectedUser(), volumeId);
		return volumeDTO[0];
	}

}
