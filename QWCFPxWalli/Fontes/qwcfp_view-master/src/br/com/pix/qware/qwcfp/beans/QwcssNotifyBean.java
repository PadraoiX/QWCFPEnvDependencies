package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DualListModel;

import br.com.pix.qware.qwcfp.beans.uties.ListMembersBean;
import br.com.pix.qware.qwcfp.beans.uties.ListMyManagedGroups;
import br.com.pix.qware.qwcfp.beans.uties.NotificationBean;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qware.qwcfp.wrappers.PrivWrapper;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.PrivilegioDTO;
import br.com.qwcss.ws.dto.AreaDTO;
import br.com.qwcss.ws.dto.AssignedRuleDTO;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.ListPrivsDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.MyGroupsEXDTO;
import br.com.qwcss.ws.dto.RuleDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "notiBean")
@ViewScoped
public class QwcssNotifyBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Inject
	private FacesMessages		messages;

	@Inject
	private PrivilegiosService	privService;

	@Inject
	private DomainService		domainService;
	
	@Inject
	private InformationGroupService	groupService;
	
	@Inject
	private NotificationBean notificationBean;
	
	@Inject
	private LoginBean				 loginBean;
	
	@Inject
	private ListMyManagedGroups		listMyManagedGroups;
	
	private MyGroupsWrapper 	selectionMyGroupsWrapper;
	
	private String[]			privSelect;
	
	private MemberDTO			member;
	
	private Integer			groupToGrant;
	
	private DualListModel<GroupDTO>  groupToGrantList;
	
	private Integer 			qtdDias;
	
	private MyGroupsEXDTO 		wrappedMember;
	
	private DomainDTO[]			domainPriv;
	
	private DomainDTO[]			domainPrivSol;
	
	private DomainDTO[]			domainPrivSolArea;

	private String				justificativa;

	private String[]			privSelectSol;
	
	private List<PrivWrapper> 	wrappedPrivs;
	
	private boolean				giPriv;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		updatePriv();
		giPriv = false;
		groupToGrant = null;
		qtdDias = 0;
		buildDualList();
	}
	
	private void buildDualList() {
		List<GroupDTO> source = listMyManagedGroups.getMyManagedGroups();
		List<GroupDTO> target = new ArrayList<GroupDTO>();
		
		groupToGrantList = new DualListModel<GroupDTO>(source, target);
	}

	private void updatePriv() {
		if (loginBean.isGi()) {
			domainPriv = new DomainDTO[5];
			domainPriv[0] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-GRP");
			domainPriv[1] = domainService.returnDomain("PRIVILEGIOS", "NEW-GRP");
			domainPriv[2] = domainService.returnDomain("PRIVILEGIOS", "REPL-GRP");
			domainPriv[3] = domainService.returnDomain("PRIVILEGIOS", "DOWN-GRP");
			domainPriv[4] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-INF");
		}else{
			domainPriv = new DomainDTO[4];
			domainPriv[0] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-GRP");
			domainPriv[1] = domainService.returnDomain("PRIVILEGIOS", "NEW-GRP");
			domainPriv[2] = domainService.returnDomain("PRIVILEGIOS", "REPL-GRP");
			domainPriv[3] = domainService.returnDomain("PRIVILEGIOS", "DOWN-GRP");
		}
		
		setDomainPrivSol(new DomainDTO[4]);
		getDomainPrivSol()[0] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-GRP");
		getDomainPrivSol()[1] = domainService.returnDomain("PRIVILEGIOS", "NEW-GRP");
		getDomainPrivSol()[2] = domainService.returnDomain("PRIVILEGIOS", "REPL-GRP");
		getDomainPrivSol()[3] = domainService.returnDomain("PRIVILEGIOS", "DOWN-GRP");
		
		setDomainPrivSolArea(new DomainDTO[5]);
		getDomainPrivSolArea()[0] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-GRP");
		getDomainPrivSolArea()[1] = domainService.returnDomain("PRIVILEGIOS", "GESTOR-ARE");
		getDomainPrivSolArea()[2] = domainService.returnDomain("PRIVILEGIOS", "NEW-GRP");
		getDomainPrivSolArea()[3] = domainService.returnDomain("PRIVILEGIOS", "REPL-GRP");
		getDomainPrivSolArea()[4] = domainService.returnDomain("PRIVILEGIOS", "DOWN-GRP");
	}
	
	public void clearMenu() {
		privSelectSol = null;
		justificativa = null;
		qtdDias = 0;
		groupToGrant = null;
		privSelect = null;
		member = null;
		giPriv = false;
		updatePriv();
	}
	
	public String clear() {
		clearMenu();
		return null;
	}
	
	public String revogarPriv( PrivilegioDTO priv){
		
		//Integer grantorMemberId =  (Integer) Util.getPropertySessao("MEMBER_PRIV_ID");
		
		if(priv != null){
			Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");
			
			if (idGroup != null) {

				GroupDTO group = groupService.getGroupById(idGroup);
				
				if(group != null){
					SimpleDTO privs = privService.removePriv(wrappedMember.getMemberId(), group.getApelido(), priv.getPrivTypeStringValue());
					if(privs != null)
						if (privs.getErrorCode() == ViewError.OK.getCode()){
							messages.add(ViewError.PRIV_DELETE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
						}
						else
							messages.add(privs.getErrorCode() + ": " + privs.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				}
			}
			fillPrivs();
		}
		
		return null;
	}
	
	public String revogarPrivArea( PrivilegioDTO priv) {
		
		if(priv != null){
			Integer areaId = (Integer) Util.getPropertySession("AREA_MEMBRO");
			
			if (areaId != null) {
				SimpleDTO privs = privService.deletePrivArea(member.getMemberId(), areaId, priv.getPrivTypeStringValue(), 0);
				if(privs != null)
					if (privs.getErrorCode() == ViewError.OK.getCode()){
						messages.add(ViewError.PRIV_DELETE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
					}
					else
						messages.add(privs.getErrorCode() + ": " + privs.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			}
			fillPrivsArea(member.getMemberId());
		}
		
		return null;
	}
	
	public String removeMemberArea(MemberDTO car) {
		
		if(car != null){
			Integer areaId = (Integer) Util.getPropertySession("AREA_MEMBRO");
			
			if (areaId != null) {
				SimpleDTO privs = privService.removePrivArea(car.getMemberId(), areaId, null, 0);
				if(privs != null)
					if (privs.getErrorCode() == ViewError.OK.getCode()){
						messages.add(ViewError.PRIV_DELETE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
					}
					else
						messages.add(privs.getErrorCode() + ": " + privs.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			}
		}
		
		return null;
	}
	
	public void fillPrivsArea(Integer memberId) {
		ArrayList<PrivWrapper> wrappedPrivs = new ArrayList<PrivWrapper>();
		Integer areaId = (Integer) Util.getPropertySession("AREA_MEMBRO");
		PrivilegioDTO[] ggrpPrivs ;
		if (areaId != null) {
			ListPrivsDTO privilegios = privService.returnPrivArea(memberId,areaId);
			ggrpPrivs = privilegios.getListPriv();
			
			if (ggrpPrivs != null) {
				for (int i = 0; i < ggrpPrivs.length; i++) {
					DomainDTO dom = domainService.returnDomain(ggrpPrivs[i].getPrivTypeName(), ggrpPrivs[i].getPrivTypeStringValue());
					
					if (dom != null) {
						PrivWrapper pWrap = new PrivWrapper(ggrpPrivs[i], "AREA", dom.getDescription());
						wrappedPrivs.add(pWrap);
					}
				}
			}
		}
		
		this.setWrappedPrivs(wrappedPrivs);
	}
	
	public void fillPrivs() {
		ArrayList<PrivWrapper> wrappedPrivs = new ArrayList<PrivWrapper>();

			Integer memberId =  wrappedMember.getMemberId();
			String groupAlias = (String) Util.getPropertySession("GROUP_ALIAS");
			
			if(memberId != null){
				
				GroupDTO group = groupService.getGroupByAlias(groupAlias);
				
				if (group.getErrorCode() == ViewError.OK.getCode()) {
					Integer areaId = group.getAreaId();
					
					
					ListPrivsDTO privilegios = privService.returnPrivGrp(memberId,group.getGroupId());
					
					PrivilegioDTO[] ggrpPrivs = privilegios.getListPriv();
					
					if (ggrpPrivs != null) {
						for (int i = 0; i < ggrpPrivs.length; i++) {
							DomainDTO dom = domainService.returnDomain(ggrpPrivs[i].getPrivTypeName(), ggrpPrivs[i].getPrivTypeStringValue());
							if (dom != null) {
								
								PrivWrapper pWrap = new PrivWrapper(ggrpPrivs[i], "GRUPO", dom.getDescription());
								wrappedPrivs.add(pWrap);
							}
						}
					}
					
					if (areaId != null) {
						privilegios = privService.returnPrivArea(memberId,areaId);
						ggrpPrivs = privilegios.getListPriv();
						
						if (ggrpPrivs != null) {
							for (int i = 0; i < ggrpPrivs.length; i++) {
								DomainDTO dom = domainService.returnDomain(ggrpPrivs[i].getPrivTypeName(), ggrpPrivs[i].getPrivTypeStringValue());
								
								if (dom != null) {
									PrivWrapper pWrap = new PrivWrapper(ggrpPrivs[i], "AREA", dom.getDescription());
									wrappedPrivs.add(pWrap);
								}
							}
						}
					}
				}
				
				this.setWrappedPrivs(wrappedPrivs);
			}
		
		this.setWrappedPrivs(wrappedPrivs);
	}
	

	public String solicitarPrivilegio() {

		String group = (String) Util.getPropertySession("GROUP_ALIAS");

		if (privSelectSol != null && group != null && justificativa != null && privSelectSol.length > 0) {
			
			DomainDTO[] priviArray = getDomainPriv();
			String[] priviSolArray = new String[privSelectSol.length];
			for (int i = 0; i < privSelectSol.length; i++) {
				for (int j = 0; j < priviArray.length; j++) {
					if(priviArray[j].getStringValue().equals(privSelectSol[i]))
						priviSolArray[i] = priviArray[j].getDescription();
				}
			}
			

			SimpleDTO simpleDTO = privService.requestPriv(justificativa, group, priviSolArray);

			if (simpleDTO != null) {
				if (simpleDTO.getErrorCode() == 0)
					messages.add(ViewError.PRIV_GROUP_REQUEST_SUCESS.getMsg(),
							FacesMessage.SEVERITY_INFO);
				else
					messages.add(simpleDTO.getErrorCode() + ": " + simpleDTO.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);

			} else
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
						+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);

		} else
			messages.add(ViewError.PRIV_NOT_SELECT.getCode() + ": "
					+ ViewError.PRIV_NOT_SELECT.getMsg(), FacesMessage.SEVERITY_ERROR);

		clearMenu();
		return null;

	}

	public String solicitarPrivilegioArea() {

		AreaDTO areaDTO = (AreaDTO) Util.getPropertySession("AREA_ID");

		if (privSelectSol != null && (privSelectSol.length > 0) && areaDTO != null
				&& justificativa != null) {
			
			DomainDTO[] priviArray = getDomainPrivSolArea();
			String[] priviSolArray = new String[privSelectSol.length];
			for (int i = 0; i < privSelectSol.length; i++) {
				for (int j = 0; j < priviArray.length; j++) {
					if(priviArray[j].getStringValue().equals(privSelectSol[i]))
						priviSolArray[i] = priviArray[j].getDescription();
				}
			}
			

			SimpleDTO simpleDTO = privService.requestPrivArea(justificativa,
					areaDTO.getApelido(),
					priviSolArray);

			if (simpleDTO != null) {
				if (simpleDTO.getErrorCode() == 0)
					messages.add(ViewError.PRIV_AREA_RESQUES_SUCESS.getMsg() + areaDTO.getNome(),
							FacesMessage.SEVERITY_INFO);
				else
					messages.add(simpleDTO.getErrorCode() + ": " + simpleDTO.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);

			} else
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
						+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);

		} else
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
					+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);

		clearMenu();
		return  null;

	}
	
	public void concederPrivGrpOutro(){
		if (privSelect != null && !(privSelect.length == 0)) {
			if (qtdDias > 0) {
				
				SimpleDTO retorno = null;
				giPriv = false;
				Integer memberId = (Integer) Util.getPropertySession("MEMBER_PRIV_ID"); 
				
				if (memberId != null && qtdDias != null) {
					
					String aliasGroup = "";
					
					Integer groupToGrant = (Integer) Util.getPropertySession("ID_GROUP");
					
					ArrayList<String> privList = new ArrayList<String>(Arrays.asList(privSelect));
					
					if (privList.contains("GESTOR-INF")) {
						concederGi(memberId);
						privList.remove("GESTOR-INF");
						giPriv = true;
					}
					
					if (groupToGrant != null) {
						
						GroupDTO group = groupService.getGroupById(groupToGrant);
						
						if (group != null) {
							if (group.getErrorCode() == ViewError.OK.getCode())
								aliasGroup = group.getApelido();
							else {
								messages.add(group.getErrorCode() + ": " + group.getErrorMsg(),
										FacesMessage.SEVERITY_ERROR);
								clearMenu();
								return ;
							}
						} else {
							messages.add(ViewError.GROUP_NOT_FOUND.getCode() + ": "
									+ ViewError.GROUP_NOT_FOUND.getMsg(), FacesMessage.SEVERITY_ERROR);
							clearMenu();
							return ;
						}
						
						String privilegios = "";
						
						for (String priv : privList) {
							retorno = privService
									.insertNewPriv(memberId,
											aliasGroup,
											priv,
											qtdDias);
							
							if (retorno != null) {
								if (retorno.getErrorCode() == ViewError.OK.getCode()) {
									DomainDTO[] priviArray = getDomainPriv();
									for (int j = 0; j < priviArray.length; j++) {
										if(priviArray[j].getStringValue().equals(priv))
											privilegios =  privilegios + ", " + priviArray[j].getDescription();
									}
									if (privList.indexOf(priv) + 1 == privList.size())
										privilegios = privilegios.substring(1);
								}
							}
						}
						
						if(retorno != null){
							if(retorno.getErrorCode() == 0){
								messages.add("atribuido(s) o(s) privilégio(s) de: " + privilegios + " ao usuário por " + qtdDias, FacesMessage.SEVERITY_INFO);
								notificationBean.aprovacaoPrivilegios(group, member, privilegios);
							}else{
								messages.add(retorno.getErrorCode() + ": " + retorno.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
								clearMenu();
								return ;
							}
						}
					}else{
						if (!giPriv) {
							messages.error(ViewError.GROUP_NOT_SELECTED.getMsg());
							clearMenu();
							return ;
						}
					}
				} else
					messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
							+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
			}else{
				messages.add(ViewError.DAYS_NUMBER_ZERO.getCode() + ": "
						+ ViewError.DAYS_NUMBER_ZERO.getMsg(),
						FacesMessage.SEVERITY_WARN);	
			}
			
			
		} else
			messages.add(ViewError.PRIV_NOT_FOUND.getCode() + ": "
					+ ViewError.PRIV_NOT_FOUND.getMsg(),
					FacesMessage.SEVERITY_ERROR);

		
		clearMenu();
	}
	
	public void concederPrivGrp(MemberDTO memberSelected){
		if (privSelect != null && (!(privSelect.length == 0))) {
			if (qtdDias > 0) {
				giPriv = false;
				SimpleDTO retorno = null;
				if (memberSelected != null && qtdDias != null) {
					ArrayList<String> privList = new ArrayList<String>(Arrays.asList(privSelect));
					
					if (privList.contains("GESTOR-INF")) {
						concederGi(memberSelected.getMemberId());
						privList.remove("GESTOR-INF");
						giPriv = true;
					}
					
					if (groupToGrant == null) 
						groupToGrant = (Integer) Util.getPropertySession("ID_GROUP");
					
					List<GroupDTO> target = groupToGrantList.getTarget();
					if (target != null && target.size() > 0) {
						
						Set<GroupDTO> groups = new HashSet<GroupDTO>();
						Set<DomainDTO> privs = new HashSet<DomainDTO>();
						Set<GroupDTO> groupsErr = new HashSet<GroupDTO>();
						Set<DomainDTO> privsErr = new HashSet<DomainDTO>();
						
						for (GroupDTO groupDTO : target) {
							for (String priv : privList) {
								retorno = privService
										.insertNewPriv(memberSelected.getMemberId(),
												groupDTO.getApelido(),
												priv,
												qtdDias);
								
								if (retorno != null) {
									if (retorno.getErrorCode() == ViewError.OK.getCode()) {
										fillGroupPrivSet(groups,groupDTO,priv,privs);
									} else {
										fillGroupPrivSet(groupsErr,groupDTO,priv,privsErr);
									}
								}
							}
						}
						
						if (!groups.isEmpty()) {
							StringBuilder stringPrivs = new StringBuilder();
							StringBuilder stringGroups = new StringBuilder();
							
							buildMessage(stringPrivs,stringGroups,privs,groups);
							
							String message = "Foram atribuido(s) o(s) privilégio(s) de: " + stringPrivs + " para o(s) grupos " + stringGroups
									+ " ao usuário " + memberSelected.getMemberName() + " por " + qtdDias + " dias";
							
							messages.add( message,
									FacesMessage.SEVERITY_INFO);
							if (member != null)
								notificationBean.aprovacaoPrivilegios(memberSelected, message);
						}
						
						if (!groupsErr.isEmpty()) {
							StringBuilder stringPrivs = new StringBuilder();
							StringBuilder stringGroups =  new StringBuilder();
							
							buildMessage(stringPrivs,stringGroups,privsErr,groupsErr);
							
							messages.add("Não foi possivel atribuir o(s) privilégio(s) " + stringPrivs + " Para o(s) grupo(s)" + stringGroups, FacesMessage.SEVERITY_ERROR);
						}
					}else{
						if (!giPriv) {
							messages.error(ViewError.GROUP_NOT_SELECTED.getMsg());
							clearMenu();
							return ;
						}
					}
				} else
					messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
							+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
			}else{
				messages.add(ViewError.DAYS_NUMBER_ZERO.getCode() + ": "
						+ ViewError.DAYS_NUMBER_ZERO.getMsg(),
						FacesMessage.SEVERITY_WARN);	
			}
			
			
		} else
			messages.add(ViewError.PRIV_NOT_FOUND.getCode() + ": "
					+ ViewError.PRIV_NOT_FOUND.getMsg(),
					FacesMessage.SEVERITY_ERROR);

		
		clearMenu();
		buildDualList();
	}
		
	private void buildMessage(StringBuilder stringPrivs,
			StringBuilder stringGroups,
			Set<DomainDTO> privs,
			Set<GroupDTO> groups) {
		
		int i = 0;
		for (DomainDTO domainDTO : privs) {
			if (i > 0) {
				stringPrivs.append(", ");
			}
			stringPrivs.append(domainDTO.getDescription());
			i++;
		}
		
		i = 0;
		for (GroupDTO groupDTO : groups) {
			if (i > 0) {
				stringGroups.append(", ");
			}
			stringGroups.append(groupDTO.getNome());
			i++;
		}
		
		
	}
	
	private void fillGroupPrivSet(Set<GroupDTO> groups, GroupDTO groupDTO, String privSelect, Set<DomainDTO> privs) {
		if (!groups.contains(groupDTO)) {
			groups.add(groupDTO);
		}
		
		DomainDTO[] priviArray = this.getDomainPriv();
		for (int j = 0; j < priviArray.length; j++) {
			if (priviArray[j].getStringValue().equals(privSelect))
				if (!privs.contains(priviArray[j])) {
					privs.add(priviArray[j]);
				}
		}
	}
	
	public boolean concederGi(Integer id) {

		Integer memberId = null;

		if (id == null) {
			if (this.member != null)
				memberId = member.getMemberId();
			else {
				messages.add(ViewError.USR_NOT_FOUND_ERR.getCode() + ": "
						+ ViewError.USR_NOT_FOUND_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);
				return false;
			}
		} else
			memberId = id;

		if ( qtdDias <= 0) {
			messages.add(ViewError.PRIV_GI_NUM_DAY_INVALIDED.getCode() + ": "
					+ ViewError.PRIV_GI_NUM_DAY_INVALIDED.getMsg(), FacesMessage.SEVERITY_ERROR);
			return false;
		} else {

			SimpleDTO ret = privService.insertNewGiPriv(memberId, String.valueOf(qtdDias));

			if (ret.getErrorCode().compareTo(ViewError.OK.getCode()) != 0) {
				messages.add(ret.getErrorCode() + ": " + ret.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
				return false;
			} else {
				messages.add(ViewError.PRIV_INSERT_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			}

		}

		return true;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String[] getPrivSelectSol() {
		return privSelectSol;
	}

	public void setPrivSelectSol(String[] privSelectSol) {
		this.privSelectSol = privSelectSol;
	}

	public DomainDTO[] getDomainPriv() {
		return domainPriv;
	}

	public String[] getPrivSelect() {
		return privSelect;
	}

	public void setPrivSelect(String[] privSelect) {
		this.privSelect = privSelect;
	}

	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		this.member = member;
	}

	public Integer getGroupToGrant() {
		return groupToGrant;
	}

	public void setGroupToGrant(Integer groupToGrant) {
		this.groupToGrant = groupToGrant;
	}

	public Integer getQtdDias() {
		return qtdDias;
	}

	public void setQtdDias(Integer qtdDias) {
		this.qtdDias = qtdDias;
	}

	public boolean isGiPriv() {
		return giPriv;
	}

	public void setGiPriv(boolean giPriv) {
		this.giPriv = giPriv;
	}

	public MyGroupsEXDTO getWrappedMember() {
		return wrappedMember;
	}

	public void setWrappedMember(MyGroupsEXDTO wrappedMember) {
		this.wrappedMember = wrappedMember;
	}

	public List<PrivWrapper> getWrappedPrivs() {
		return wrappedPrivs;
	}

	public void setWrappedPrivs(List<PrivWrapper> wrappedPrivs) {
		this.wrappedPrivs = wrappedPrivs;
	}

	public DualListModel<GroupDTO> getGroupToGrantList() {
		return groupToGrantList;
	}

	public void setGroupToGrantList(DualListModel<GroupDTO> groupToGrantList) {
		this.groupToGrantList = groupToGrantList;
	}

	public MyGroupsWrapper getSelectionMyGroupsWrapper() {
		return selectionMyGroupsWrapper;
	}

	public void setSelectionMyGroupsWrapper(MyGroupsWrapper selectionMyGroupsWrapper) {
		this.selectionMyGroupsWrapper = selectionMyGroupsWrapper;
	}

	public DomainDTO[] getDomainPrivSol() {
		return domainPrivSol;
	}

	public void setDomainPrivSol(DomainDTO[] domainPrivSol) {
		this.domainPrivSol = domainPrivSol;
	}

	public DomainDTO[] getDomainPrivSolArea() {
		return domainPrivSolArea;
	}

	public void setDomainPrivSolArea(DomainDTO[] domainPrivSolArea) {
		this.domainPrivSolArea = domainPrivSolArea;
	}

}
