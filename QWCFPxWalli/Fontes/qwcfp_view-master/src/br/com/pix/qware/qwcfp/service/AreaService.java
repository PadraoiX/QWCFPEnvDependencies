package br.com.pix.qware.qwcfp.service;


import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;
import br.com.pix.qwcfp.ws.client.list.Areas;
import br.com.pix.qwcfp.ws.client.manager.Area;
import br.com.qwcss.ws.dto.AreaDTO;

/**
 * Mtodos de negcio relacionados  entidade QwcssArea
 */
public class AreaService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Busca um registrro pelo id
	 */
	public AreaDTO[] carregar(Integer id) {
		return  Areas.area(getConnectedUser(),id);
	}
	
	public AreaDTO[] carregar(String user, String pass, Integer id) {
		return  Areas.area(user, pass, id);
	}
	
	public AreaDTO carregarEx(Integer id) {
		AreaDTO[] areas = Areas.area(getConnectedUser(),id);
		return areas  != null ? areas[0] : null;
	}
	
	
	
	/**
	 * 
	 */
	public AreaDTO[] returnAreasFilhas(Integer father) {
		return  Areas.listar(getConnectedUser(),father);
	}
	

	/**
	 * 
	 * Insere uma nova Area no banco de dados
	 * 
	 * @param status
	 * @param alias
	 * @param description
	 * @param emailManager
	 * @param managerName
	 * @param name
	 * @param phoneManager
	 * @param subordinatedArea
	 * @param QwcssArea
	 *            area a ser inserido
	 * @throws 
	 */
	public AreaDTO inserir(String status,
			String alias,
			String description,
			String emailManager,
			String managerName,
			String name,
			String phoneManager,
			Integer subordinatedArea) {

			AreaDTO areDTO = Area.insert(getConnectedUser(), 
					status,
					alias,
					description,
					emailManager,
					managerName,
					name,
					phoneManager,
					subordinatedArea);

			return areDTO;
	}

	/**
	 * Altera uma Area cadastrada no banco de dados.
	 * 
	 * @param areaID
	 * @param status
	 * @param alias
	 * @param description
	 * @param emailManager
	 * @param managerName
	 * @param name
	 * @param phoneManager
	 * @param subordinatedArea
	 * 
	 * @param QwcssArea
	 *            area
	 * @throws ServiceException
	 */
	public AreaDTO alterar(Integer areaID,
			String status,
			String alias,
			String description,
			String emailManager,
			String managerName,
			String name,
			String phoneManager,
			Integer subordinatedArea) {

			AreaDTO areaDto = Area.update(getConnectedUser(),
					areaID,
					status,
					alias,
					description,
					emailManager,
					managerName,
					name,
					phoneManager,
					subordinatedArea);
			return areaDto;

	}


	/**
	 * L todas as areas cadastradas no banco de dados
	 * 
	 * @return Lista de areas cadastradas
	 * @throws ServiceException
	 */
	public AreaDTO[] listarAreas() {
		return Areas.listar(getConnectedUser(),null);
	}
	
	public AreaDTO[] listarAreas(String loginUser, String senhaUser) {
		return Areas.listar(loginUser, senhaUser);
	}


	/**
	 * Obtm uma rea pelo ame
	 * 
	 * @param String
	 *            name
	 * @return QwcssAre
	 * @ServiceException
	 */
	public AreaDTO[] obterAreaName(String name) {
		return Areas.area(getConnectedUser(),name);
	}

	/**
	 * Obtm uma rea aparti do acronimo dela
	 * 
	 * @param String
	 *            acrony
	 * @return QwcssArea area
	 * @throws ServiceException
	 */
	public AreaDTO[] obterAreaAcrony(String acrony) {
		return Areas.area(getConnectedUser(),acrony);
	}
	
	
	public AreaDTO[] getAreasPendentes(){
//		return br.com.pix.qwcfp.ws.client.list.AreasEx.getAreasPendentes();
		return null;
	}
	
	
	
}
