package br.com.pix.qware.qwcfp.beans.uties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DualListModel;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.QwcssNotifyBean;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.NotifieDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name = "notiAllMyGroupBean")
@ViewScoped
public class NotifyAllMyGroupBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -3621787798744964697L;

	@Inject
	private NotifyService			notifyService;

	@Inject
	private FacesMessages			messages;

	@Inject
	private InformationGroupService	groupService;

	@Inject
	private PrivilegiosService		privService;

	@Inject
	private NotificationBean		notificationBean;

	@Inject
	private MemberService			memberService;

	@Inject
	private QwcssNotifyBean			notiBean;

	@Inject
	private DomainService			domainService;
	
	@Inject
	private ListMyManagedGroups		listMyManagedGroups;

	private String[]				privSelect;

	private NotifieDTO				notification;
	
	private DualListModel<GroupDTO>  groupToGrantList;

	private NotifieDTO[]			notifiesAllMyGroup;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		updateNotifiesAllMyGroup();
	}

	public void updateNotifiesAllMyGroup() {
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.set(Calendar.YEAR, 2009);
		dataInicio.set(Calendar.MONTH, Calendar.JANUARY);
		dataInicio.set(Calendar.DAY_OF_MONTH, 1);
		Calendar dataFim = Calendar.getInstance();
		dataFim.set(Calendar.YEAR, 2090);
		dataFim.set(Calendar.MONTH, Calendar.JANUARY);
		dataFim.set(Calendar.DAY_OF_MONTH, 1);
		setNotifiesAllMyGroup(notifyService.listarAllMyGroup(dataInicio, dataFim));
		buildDualList();
	}
	
	private void buildDualList() {
		List<GroupDTO> source = listMyManagedGroups.getMyManagedGroups();
		List<GroupDTO> target = new ArrayList<GroupDTO>();
		
		setGroupToGrantList(new DualListModel<GroupDTO>(source, target));
	}

	public String concederPrivAC(MemberDTO member) {

		if (member != null) {

			DomainDTO download = null;
			SimpleDTO retorno = null;
			GroupDTO group = groupService.listar("AP");

			retorno = privService.insertNewPriv(member.getMemberId(),
					"AP",
					"DOWN-GRP",
					365);

			if (retorno != null) {
				if (retorno.getErrorCode() == ViewError.OK.getCode()) {
					download = domainService.returnDomain("PRIVILEGIOS", "DOWN-GRP");
					String privilegio = download != null ? download.getDescription() : "";
					messages.add("atribuido(s) o(s) privilégio(s) de: " + privilegio
							+ " ao usuário por 1 ano", FacesMessage.SEVERITY_INFO);
					if (member != null)
						notificationBean.aprovacaoPrivilegios(group, member, privilegio);
				} else {
					messages.add(retorno.getErrorCode() + ": " + retorno.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);
				}
			} else
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
						+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);

		} else
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
					+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);

		updateNotifiesAllMyGroup();
		privSelect = null;
		return null;
	}

	public String concederPrivGrp() {
		if (privSelect != null && !(privSelect.length == 0) && notification != null) {
			if (notification.getQtdDias() > 0) {
				
				//Integer groupToGrant = notification.getGroupScopeId();
				List<GroupDTO> target = groupToGrantList.getTarget();
				
				if (notification.getCreatorMemberId() != null && target != null
						&& notification.getQtdDias() != null) {
					
					if (target.size() > 0) {
						
						/*String aliasGroup = "";
						
						GroupDTO group = groupService.getGroupById(groupToGrant);
						
						if (group != null) {
							if (group.getErrorCode() == ViewError.OK.getCode())
								aliasGroup = group.getApelido();
							else {
								messages.add(group.getErrorCode() + ": " + group.getErrorMsg(),
										FacesMessage.SEVERITY_ERROR);
								return null;
							}
						} else {
							messages.add(ViewError.GROUP_NOT_FOUND.getCode() + ": "
									+ ViewError.GROUP_NOT_FOUND.getMsg(), FacesMessage.SEVERITY_ERROR);
							return null;
						}*/
						
						Set<GroupDTO> groups = new HashSet<GroupDTO>();
						Set<DomainDTO> privs = new HashSet<DomainDTO>();
						Set<GroupDTO> groupsErr = new HashSet<GroupDTO>();
						Set<DomainDTO> privsErr = new HashSet<DomainDTO>();
						
						for (GroupDTO groupDTO : target) {
							SimpleDTO retorno = null;
							
							for (int i = 0; i < privSelect.length; i++) {
								retorno = privService.insertNewPrivEx(notification.getCreatorMemberId(),
										groupDTO.getApelido(),
										privSelect[i],
										notification.getQtdDias(),
										notification.getId());
								
								
								if (retorno != null) {
									if (retorno.getErrorCode() == ViewError.OK.getCode()) {
										fillGroupPrivSet(groups,groupDTO,privSelect[i],privs);
									} else {
										fillGroupPrivSet(groupsErr,groupDTO,privSelect[i],privsErr);
									}
								}
							}
							
						}
						
						if (!groups.isEmpty()) {
							StringBuilder stringPrivs = new StringBuilder();
							StringBuilder stringGroups = new StringBuilder();
							
							buildMessage(stringPrivs,stringGroups,privs,groups);
						
							notification.getGroupScopeId();
							MemberDTO member = memberService.listar(notification.getCreatorMemberId());
							
							String message = "Foram atribuido(s) o(s) privilégio(s) de: " + stringPrivs + " para o(s) grupos " + stringGroups
									+ " ao usuário " + member.getMemberName() + " por " + notification.getQtdDias() + " dias";
							
							messages.add( message,
									FacesMessage.SEVERITY_INFO);
							if (member != null)
								notificationBean.aprovacaoPrivilegios(member, message);
						}
						
						if (!groupsErr.isEmpty()) {
							StringBuilder stringPrivs = new StringBuilder();
							StringBuilder stringGroups =  new StringBuilder();
							
							buildMessage(stringPrivs,stringGroups,privsErr,groupsErr);
							
							messages.add("Não foi possivel atribuir o(s) privilégio(s) " + stringPrivs + " Para o(s) grupo(s)" + stringGroups, FacesMessage.SEVERITY_ERROR);
						}
					}else
						messages.add(ViewError.GROUP_NOT_SELECTED.getCode() + ": "
								+ ViewError.GROUP_NOT_SELECTED.getMsg(), FacesMessage.SEVERITY_ERROR);
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

		updateNotifiesAllMyGroup();
		privSelect = null;
		return null;
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
		
		DomainDTO[] priviArray = notiBean.getDomainPriv();
		for (int j = 0; j < priviArray.length; j++) {
			if (priviArray[j].getStringValue().equals(privSelect))
				if (!privs.contains(priviArray[j])) {
					privs.add(priviArray[j]);
				}
		}
	}

	public NotifieDTO[] getNotifiesAllMyGroup() {
		return notifiesAllMyGroup;
	}

	public void setNotifiesAllMyGroup(NotifieDTO[] notifiesAllMyGroup) {
		this.notifiesAllMyGroup = notifiesAllMyGroup;
	}

	public String[] getPrivSelect() {
		return privSelect;
	}

	public void setPrivSelect(String[] privSelect) {
		this.privSelect = privSelect;
	}

	public NotifieDTO getNotification() {
		return notification;
	}

	public void setNotification(NotifieDTO notification) {
		this.notification = notification;
	}

	public DualListModel<GroupDTO> getGroupToGrantList() {
		return groupToGrantList;
	}

	public void setGroupToGrantList(DualListModel<GroupDTO> groupToGrantList) {
		this.groupToGrantList = groupToGrantList;
	}

}
