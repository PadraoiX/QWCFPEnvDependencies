package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.com.pix.qware.qwcfp.beans.uties.ListMyGroups;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.UserScopeService;
import br.com.pix.qware.qwcfp.util.CookieWork;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MyGroupsDTO;
import br.com.qwcss.ws.dto.UserScopeNodeDTO;

@Named("treeBean")
@RequestScoped
public class TreeGroupBean extends AbstractBean {

	@Inject
	FacesMessages					messages;

	@Inject
	private InformationGroupService	groupService;

	@Inject
	private ListMyGroups			listMyGroups;

	@Inject
	private CookieWork				cookieWork;

	@Inject
	private LoginBean				loginBean;
	
	@Inject
	private UserScopeService		userScope;

	private TreeNode				selectedNode;

	private TreeNode				root;
	/**
	 * 
	 */
	private static final long		serialVersionUID			= 1L;

	private static final String		CATEGORY_GROUP_SUBORDINATED	= "GRP_SUBORDINADO";

	private static final String		CATEGORY_GROUP_ROOT			= "GRP_RAIZ";

	private static final String		CATEGORY_BLIND_NODE			= "CEGO";
	
	private static final String		CATEGORY_AREA_SUBORDINATED	= "AREA_SUBORDINADO";
	
	private static final String		CATEGORY_AREA_ROOT			= "AREA_RAIZ";
	
	private boolean area = false;

	@Override
	public void init() {

		super.init();

		if (loginBean.getLoggedUser() != null) { //marreta
			growTree();
		}

	}
	
	public void onActivateContextMenu(NodeSelectEvent event) {
			TreeNode node = event.getTreeNode();
			setSelectedNode(node);
			String type = node.getType();
			
			if (type.equalsIgnoreCase(CATEGORY_GROUP_ROOT)|| type.equalsIgnoreCase(CATEGORY_GROUP_SUBORDINATED)) {
				area = false;
			}else{
				area = true;
			}
		
	}
	
	public void onNodeSelect(TreeNode node){
		if (node != null) {

			setSelectedNode(node);
			String type = node.getType();

			if (type.equalsIgnoreCase(CATEGORY_GROUP_ROOT)|| type.equalsIgnoreCase(CATEGORY_GROUP_SUBORDINATED)) {
				try {

					UserScopeNodeDTO scopeNode = (UserScopeNodeDTO) node.getData();
					Util.setPropertySessao("GROUP_ALIAS", scopeNode.getTargetAlias());
					Util.setPropertySessao("ID_GROUP", scopeNode.getTargetId());
					Cookie cookie = cookieWork.getCookie("group.getGroupId()");
					Integer qtdeAcesso = 0;
					if (cookie != null) {
						qtdeAcesso = Integer.parseInt(cookie.getValue());
					}
					int expiry = 60 * 60 * 24 * 365 * 10;
					qtdeAcesso = qtdeAcesso + 1;
					cookieWork.setCookie(String.valueOf(scopeNode.getTargetId()),
							String.valueOf(qtdeAcesso),
							expiry);

					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					ec.redirect(ec.getRequestContextPath() + "/listArquivosLazy.faces");

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (type.equalsIgnoreCase(CATEGORY_BLIND_NODE)) {
				messages.add(ViewError.MEMBER_NOT_PRIV_GROUP.getCode() + ": "
						+ ViewError.MEMBER_NOT_PRIV_GROUP.getMsg(), FacesMessage.SEVERITY_WARN);
			}else if (type.equalsIgnoreCase(CATEGORY_AREA_ROOT) || type.equalsIgnoreCase(CATEGORY_AREA_SUBORDINATED)){
				try {
					UserScopeNodeDTO scopeNode = (UserScopeNodeDTO) node.getData();
					Util.setPropertySessao("AREA_RAIZ_ID", scopeNode.getTargetId());
						
					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					ec.redirect(ec.getRequestContextPath() + "/areaConfig.faces");
				
				

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void onNodeSelect(NodeSelectEvent event) {
		TreeNode node = event.getTreeNode();
		onNodeSelect(node);
	}
	
	public void onNodeSelect() {
		onNodeSelect(this.selectedNode);
	}
	
	public void onNodeSelectConfig() {
		
		if(selectedNode != null){
			setSelectedNode(selectedNode);
			String type = selectedNode.getType();
			
			if (type.equalsIgnoreCase(CATEGORY_GROUP_ROOT)|| type.equalsIgnoreCase(CATEGORY_GROUP_SUBORDINATED)) {
				try {
					
					UserScopeNodeDTO scopeNode = (UserScopeNodeDTO) selectedNode.getData();
					Util.setPropertySessao("GROUP_ALIAS", scopeNode.getTargetAlias());
					Util.setPropertySessao("ID_GROUP", scopeNode.getTargetId());
					Cookie cookie = cookieWork.getCookie("group.getGroupId()");
					Integer qtdeAcesso = 0;
					if (cookie != null) {
						qtdeAcesso = Integer.parseInt(cookie.getValue());
					}
					int expiry = 60 * 60 * 24 * 365 * 10;
					qtdeAcesso = qtdeAcesso + 1;
					cookieWork.setCookie(String.valueOf(scopeNode.getTargetId()),
							String.valueOf(qtdeAcesso),
							expiry);
					
					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					ec.redirect(ec.getRequestContextPath() + "/configGrupo.faces");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (type.equalsIgnoreCase(CATEGORY_BLIND_NODE)) {
				messages.add(ViewError.MEMBER_NOT_PRIV_GROUP.getCode() + ": " + ViewError.MEMBER_NOT_PRIV_GROUP.getMsg(), FacesMessage.SEVERITY_WARN);
			}else if (type.equalsIgnoreCase(CATEGORY_AREA_ROOT)){
				try {
					UserScopeNodeDTO scopeNode = (UserScopeNodeDTO) selectedNode.getData();
					Util.setPropertySessao("AREA_RAIZ_ID", scopeNode.getTargetId());
					
					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					ec.redirect(ec.getRequestContextPath() + "/areaConfig.faces");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void onNodeSelectCopyGroup(String operation) {
		
		if(selectedNode != null){
			setSelectedNode(selectedNode);
			String type = selectedNode.getType();
			
			Util.setPropertySessao("ADD_GROUP_OPERATION", operation);
			
			if (type.equalsIgnoreCase(CATEGORY_GROUP_ROOT)|| type.equalsIgnoreCase(CATEGORY_GROUP_SUBORDINATED)) {
				try {
					
					UserScopeNodeDTO scopeNode = (UserScopeNodeDTO) selectedNode.getData();
					Util.setPropertySessao("ID_GROUP_SUBNOVO", scopeNode.getTargetId());
					Cookie cookie = cookieWork.getCookie("group.getGroupId()");
					Integer qtdeAcesso = 0;
					if (cookie != null) {
						qtdeAcesso = Integer.parseInt(cookie.getValue());
					}
					int expiry = 60 * 60 * 24 * 365 * 10;
					qtdeAcesso = qtdeAcesso + 1;
					cookieWork.setCookie(String.valueOf(scopeNode.getTargetId()),
							String.valueOf(qtdeAcesso),
							expiry);
					
					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					ec.redirect(ec.getRequestContextPath() + "/addGrupo.faces");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (type.equalsIgnoreCase(CATEGORY_BLIND_NODE)) {
				messages.add(ViewError.MEMBER_NOT_PRIV_GROUP.getCode() + ": " + ViewError.MEMBER_NOT_PRIV_GROUP.getMsg(), FacesMessage.SEVERITY_WARN);
			}else if (type.equalsIgnoreCase(CATEGORY_AREA_ROOT) ||  type.equalsIgnoreCase(CATEGORY_AREA_SUBORDINATED)){
				try {
					UserScopeNodeDTO scopeNode = (UserScopeNodeDTO) selectedNode.getData();
					Util.setPropertySessao("AREA_RAIZ_ID_SUBGROUP", scopeNode.getTargetId());
					
					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					ec.redirect(ec.getRequestContextPath() + "/addGrupo.faces");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void growTree() {

		root = new DefaultTreeNode();
		HashMap<String, TreeNode> nodeMap = new HashMap<String, TreeNode>(8);
		
		LoginDTO loggedUser = loginBean.getLoggedUser().getLoginDto();
		UserScopeNodeDTO[] nodeList = userScope.getUserScope(loggedUser.getMemberId());
		
		if (nodeList == null) {
			return;
		}else{
			if(nodeList[0].getErrorCode() != 0){
				messages.add(nodeList[0].getErrorCode() + ": " + nodeList[0].getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				return;
			}
		}
		
		
		for (int i = 0; i < nodeList.length; i++) {
			DefaultTreeNode node = new DefaultTreeNode(nodeList[i]);
			
			if (nodeList[i].getTargetType().equals("AREA")) {
				if (nodeList[i].isHasPriv()) {
					if (nodeList[i].getParentId() == null) {
						node.setType(getCategoryAreaRoot());
						node.setExpanded(true);
					}else{
						node.setType(getCategoryAreaSubordinated());
						node.setExpanded(true);
					}
				}else{
					node.setType(getCategoryBlindNode());
					node.setExpanded(true);
				}
			}else if (nodeList[i].getTargetType().equals("GROUP")) {
				if (nodeList[i].isHasPriv()) {
					if (nodeList[i].getParentId() == null) {
						node.setType(getCategoryGroupRoot());
					}else{
						node.setType(getCategoryGroupSubordinated());
					}
				}else{
					node.setType(getCategoryBlindNode());
				}
			}
			nodeMap.put(nodeList[i].getId(), node);
		}
		
		Collection<TreeNode> values = nodeMap.values();
		
		for (TreeNode treeNode : values) {
			UserScopeNodeDTO content = (UserScopeNodeDTO) treeNode.getData();
			TreeNode fatherNode = nodeMap.get(content.getParentId());

			if (fatherNode != null) {
				treeNode.setParent(fatherNode);
			}else{
				treeNode.setParent(root);
				fatherNode = root;
			}
			
			List<TreeNode> children = fatherNode.getChildren();
			children.add(treeNode);
		}

		/*try {

			if (listMyGroups.getMyGroups() != null) {
				MyGroupsDTO[] groups = (MyGroupsDTO[]) listMyGroups.getMyGroups().toArray();

				HashMap<Integer, TreeNode> nodeMap = new HashMap<Integer, TreeNode>(8);
				HashMap<String, List<GroupDTO>> categorizedGroups = new HashMap<String, List<GroupDTO>>();

				categorizedGroups.put(getCategoryGroupSubordinated(), new ArrayList<GroupDTO>(6));
				categorizedGroups.put(getCategoryGroupRoot(), new ArrayList<GroupDTO>());

				if (groups != null) {
					for (int i = 0; i < groups.length; i++) {

						GroupDTO group = myGroupToGroup(groups[i]);

						if (groups[i].getSubordinateGroup() != null) {

							categorizedGroups.get(getCategoryGroupSubordinated()).add(group);
							DefaultTreeNode node = new DefaultTreeNode(group);
							node.setType(getCategoryGroupSubordinated());
							nodeMap.put(group.getGroupId(), node);

						} else {

							categorizedGroups.get(getCategoryGroupRoot()).add(group);
							DefaultTreeNode node = new DefaultTreeNode(group, root);
							node.setType(getCategoryGroupRoot());
							nodeMap.put(group.getGroupId(), node);

						}
					}

					List<GroupDTO> orphanGroups = new ArrayList<GroupDTO>();
					if (categorizedGroups.containsKey(getCategoryGroupSubordinated())) {
						Collection<GroupDTO> values = categorizedGroups
								.get(getCategoryGroupSubordinated());

						for (GroupDTO groupDTO : values) {
							if (nodeMap.containsKey(groupDTO.getSubordinateGroup())) {

								TreeNode fatherNode = nodeMap.get(groupDTO.getSubordinateGroup());
								TreeNode child = nodeMap.get(groupDTO.getGroupId());

								child.setParent(fatherNode);
								List<TreeNode> children = fatherNode.getChildren();
								children.add(child);

								//new DefaultTreeNode(getCategoryGroupSubordinated(), groupDTO, fatherNode);

							} else {
								orphanGroups.add(groupDTO);
							}
						}
					}

					for (GroupDTO orphanGroup : orphanGroups) {
						recursiveAdoption(orphanGroup, true);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	private TreeNode recursiveAdoption(GroupDTO groupDTO, boolean isFirst) {

		if (groupDTO.getSubordinateGroup() == null) {
			return new DefaultTreeNode(isFirst ? getCategoryGroupRoot() : getCategoryBlindNode(),
					groupDTO, root);
		} else {
			GroupDTO group = groupService.getGroupById(groupDTO.getSubordinateGroup());
			TreeNode node = recursiveAdoption(group, false);
			return new DefaultTreeNode(isFirst ? getCategoryGroupSubordinated()
					: getCategoryBlindNode(), groupDTO, node);
		}

	}

	private GroupDTO myGroupToGroup(MyGroupsDTO myGroup) {
		GroupDTO ret = new GroupDTO();

		ret.setAceptVersion(myGroup.getAceptVersion());
		ret.setApelido(myGroup.getApelido());
		ret.setAreaId(myGroup.getAreaId());
		ret.setCreationDate(myGroup.getCreationDate());
		ret.setDaysLimitDiscart(myGroup.getDaysLimitDiscart());
		ret.setDescription(myGroup.getDescription());
		ret.setFileSystemStorageDomain(myGroup.getFileSystemStorageDomain());
		ret.setGroupId(myGroup.getGroupId());
		ret.setInputDateLimit(myGroup.getInputDateLimit());
		ret.setManagerGroup(myGroup.getManagerGroupId());
		ret.setNome(myGroup.getNome());
		ret.setNotificationTypeDomain(myGroup.getNotificationTypeDomain());
		ret.setOutputDateLimit(myGroup.getOutputDateLimit());
		ret.setOwnerCreator(myGroup.getOwnerCreator());
		ret.setSizeInBytes(myGroup.getSizeInBytes());
		ret.setStatus(myGroup.getStatus());
		ret.setSubordinateGroup(myGroup.getSubordinateGroup());
		ret.setSuporte1(myGroup.getSuporte1Id());
		ret.setSuporte2(myGroup.getSuporte2Id());
		ret.setVersionLimit(myGroup.getVersionLimit());

		return ret;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public static String getCategoryBlindNode() {
		return CATEGORY_BLIND_NODE;
	}

	public static String getCategoryGroupRoot() {
		return CATEGORY_GROUP_ROOT;
	}

	public static String getCategoryGroupSubordinated() {
		return CATEGORY_GROUP_SUBORDINATED;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public static String getCategoryAreaSubordinated() {
		return CATEGORY_AREA_SUBORDINATED;
	}

	public static String getCategoryAreaRoot() {
		return CATEGORY_AREA_ROOT;
	}

	public boolean isArea() {
		return area;
	}

	public void setArea(boolean area) {
		this.area = area;
	}
}
