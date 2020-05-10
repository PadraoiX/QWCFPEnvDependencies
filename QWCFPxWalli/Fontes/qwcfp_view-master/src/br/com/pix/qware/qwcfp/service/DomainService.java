package br.com.pix.qware.qwcfp.service;


import br.com.pix.qwcfp.ws.client.dominios.Dominio;
import br.com.qwcss.ws.dto.DomainDTO;

public class DomainService extends Service {

	private static final long serialVersionUID = 1L;

	/**
	 * Busca um dominio especfico pelo name e o stringVlaue
	 * @param name
	 * @param stringValue
	 * @return
	 */
	public DomainDTO returnDomain(String name, String stringValue) {
		return Dominio.returnDomain(getConnectedUser(),name, stringValue);
	}

	/**
	 * Retorno uma array de domains pelo tippo 'NAME'
	 * @param name
	 * @return
	 */
	public DomainDTO[] returnDomain(String name) {

		return Dominio.returnDomain(getConnectedUser(),name);

	}

	/**
	 * Retorna um domain especfico apartir do ID 
	 * @param id
	 * @return
	 */
	public DomainDTO returnDomain(Integer id) {

		return Dominio.returnDomain(getConnectedUser(),id);

	}

}
