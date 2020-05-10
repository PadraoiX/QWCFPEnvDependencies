package br.com.pix.qware.qwcfp.beans;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.NodeInformationService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.NodeDTO;

/**
 * Bean da tela de cadastro de areas
 */
@ManagedBean(name = "nodeBean")
@ViewScoped
public class QwcssNodeInformationBean extends AbstractBean {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	@Inject
	private NodeInformationService	nodeService;

	@Inject
	private DomainService			domainService;

	@Inject
	private FacesMessages			messages;

	private NodeDTO					node;
	
	private NodeDTO					nodeEdit;

	private List<NodeDTO>			nodes;

	private DomainDTO[]				statusList;
	
	private boolean 				disable;

	

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.disable = true;
		statusList = domainService.returnDomain("STATUS_NO");
		updateNodes();
		node = new NodeDTO();
	}
	
	public void updateNodes(){
		NodeDTO[] myNodes = nodeService.nodes();
		
		if (myNodes != null) {
			nodes = Arrays.asList(myNodes);
		}else{
			nodes = null;
		}
	};

	/**
	 * @return lista de Nos
	 */

	public NodeDTO getNode() {
		return node;
	}

	public void setNode(NodeDTO node) {
		this.node = node;
	}


	/**
	 * Cadastra ou atualiza um Node (depende do estado da flag 'alterar')
	 * 
	 * @param creationDate
	 * @return
	 */
	public String salvar() {
		
		try {

		String status = domainService.returnDomain(node.getStatus()).getStringValue();
		String apelido = node.getApelido();
		String dbPasswdQwareLogin = node.getDbPasswdQwareLogin();
		String dbUserQwarelogin = node.getDbUserQwarelogin();
		Integer defaultInfoGroup = node.getDefaultInfoGroup();
		String description = node.getDescription();
		String ipAddress = node.getIpAddress();
		String name = node.getName();
		String osQwarePaswdEnc = node.getOsQwarePaswdEnc();
		String osQwareUser = node.getOsQwareUser();
		Integer portConnection = node.getPortConnection();
		String qwareHomePath = node.getQwareHomePath();
		String qwareKeyEnc = node.getQwareKeyEnc();
		String qwarePasswdEnc = node.getQwarePasswdEnc();
		String qwareUser = node.getQwareUser();
		String rootFullPath4Files = node.getRootFullPath4Files();
		String uriJdbcForDbConection = node.getUriJdbcForDbConection();

		NodeDTO retorno = nodeService.insert(status,
				apelido,
				dbPasswdQwareLogin,
				dbUserQwarelogin,
				defaultInfoGroup,
				description,
				ipAddress,
				name,
				osQwarePaswdEnc,
				osQwareUser,
				portConnection,
				qwareHomePath,
				qwareKeyEnc,
				qwarePasswdEnc,
				qwareUser,
				rootFullPath4Files,
				uriJdbcForDbConection);

		if (retorno.getErrorCode() == 0) {
			messages.add(ViewError.NODE_INSERT_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.getApplication().getNavigationHandler()
					.handleNavigation(facesContext, null, "listNode");
			return "listNode.faces?redirect=true";
		} else {
			messages.add(retorno.getErrorCode() + retorno.getErrorMsg(),
					FacesMessage.SEVERITY_ERROR);
			return null;
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.NODE_INSERT_ERROR.getCode() + ": " +ViewError.NODE_INSERT_ERROR.getMsg(),
					FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}

	/**
	 * Cadastra ou atualiza um Node (depende do estado da flag 'alterar')
	 * 
	 * @param creationDate
	 * @return
	 */
	public String editar() {
		
		try {

		String status = domainService.returnDomain(nodeEdit.getStatus()).getStringValue();
		String apelido = nodeEdit.getApelido();
		String dbPasswdQwareLogin = nodeEdit.getDbPasswdQwareLogin();

		String dbUserQwarelogin = nodeEdit.getDbUserQwarelogin();
		Integer defaultInfoGroup = nodeEdit.getDefaultInfoGroup();
		String description = nodeEdit.getDescription();
		String ipAddress = nodeEdit.getIpAddress();
		String name = nodeEdit.getName();
		String osQwarePaswdEnc = nodeEdit.getOsQwarePaswdEnc();
		String osQwareUser = nodeEdit.getOsQwareUser();
		Integer portConnection = nodeEdit.getPortConnection();
		String qwareHomePath = nodeEdit.getQwareHomePath();
		String qwareKeyEnc = nodeEdit.getQwareKeyEnc();
		String qwarePasswdEnc = nodeEdit.getQwarePasswdEnc();
		String qwareUser = nodeEdit.getQwareUser();
		String rootFullPath4Files = nodeEdit.getRootFullPath4Files();
		String uriJdbcForDbConection = nodeEdit.getUriJdbcForDbConection();
		Integer nodeId = nodeEdit.getNodeId();

		NodeDTO retorno = nodeService.update(nodeId,
				status,
				apelido,
				dbPasswdQwareLogin,
				dbUserQwarelogin,
				defaultInfoGroup,
				description,
				ipAddress,
				name,
				osQwarePaswdEnc,
				osQwareUser,
				portConnection,
				qwareHomePath,
				qwareKeyEnc,
				qwarePasswdEnc,
				qwareUser,
				rootFullPath4Files,
				uriJdbcForDbConection);

		updateNodes();
		
		if (retorno.getErrorCode() == 0)
			messages.info(ViewError.NODE_UPDATE_SUCESS.getMsg());
		else
			messages.add("Atenção! " + retorno.getErrorCode() + retorno.getErrorMsg(), FacesMessage.SEVERITY_ERROR);

		
		
		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.NODE_UPDATE_ERROR.getCode() +": "+ViewError.NODE_UPDATE_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
		}

		return null;
	}

	public DomainDTO[] getStatusList() {
		return statusList;
	}

	public void setStatusList(DomainDTO[] statusList) {
		this.statusList = statusList;
	}

	public String getStatus(Integer status) {
		return domainService.returnDomain(status).getDescription();
	}

	public List<NodeDTO> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeDTO> nodes) {
		this.nodes = nodes;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public NodeDTO getNodeEdit() {
		return nodeEdit;
	}

	public void setNodeEdit(NodeDTO nodeEdit) {
		this.nodeEdit = nodeEdit;
	}

}