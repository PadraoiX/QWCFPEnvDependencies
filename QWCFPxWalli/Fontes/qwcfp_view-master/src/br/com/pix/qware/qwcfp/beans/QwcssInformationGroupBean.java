package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import br.com.pix.qware.qwcfp.service.AssignedRuleService;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.RuleService;
import br.com.pix.qware.qwcfp.util.CookieWork;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qware.qwcfp.wrappers.WrappedGroupsContainer;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AssignedRuleDTO;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.GroupSpaceDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.MyGroupsDTO;
import br.com.qwcss.ws.dto.MyGroupsEXDTO;

/**
 * Bean da tela de cadastro de areas
 */
/*@Named("groupBean")
@RequestScoped*/
@ManagedBean(name="groupBean")
@ViewScoped
public class QwcssInformationGroupBean extends AbstractBean {

	private static final long		serialVersionUID	= 1L;

	@Inject
	private InformationGroupService	groupService;

	@Inject
	private FacesMessages			messages;

	@Inject
	private MemberService			memberService;

	@Inject
	private DomainService			domainService;
	
	@Inject
	private LoginBean				loginBean;
	
	@Inject
	private CookieWork				cookieWork;
	
	 
	@Inject
	private RuleService				ruleService;

	@Inject
	private AssignedRuleService		assignedService;
	
	
	private GroupDTO[]				gruposFilhos;

	private MyGroupsDTO				myGroup;

	private DomainDTO[]				storageType;

	private GroupDTO				group;

	private boolean					alterar;

	private DomainDTO[]				statusList;

	private Date					inputDate;

	private Date					outputDate;

	private Boolean					isGi;

	private boolean					gg;

	private GroupDTO				myGroupDeta;

	private Integer					alteredSpace;

	private String					spaceOperation;

	private String					spaceUnitSymbol;
	
	private Double					sizeValue;			
	
	private Integer 				statusAtivo;
	
	private String 					groupAlias;
	
	private Integer					grupoId;

	private MyGroupsWrapper			myWrappedGroupDeta;
	
	private final static String NEW_SUB_GROUP = "SUB";
	private final static String COPY_GROUP = "COPY";
	

	public String subordinado(Integer id) {
		String s = "";
		if (id != null && id != 0)
			s = groupService.getGroupById(id) != null ? groupService.getGroupById(id).getNome()
					: "";

		return s;
	}

	public String returnDomain(Integer domainId) {
		String s = "";

		if (domainId != null)
			s = domainService.returnDomain(domainId) != null ? domainService.returnDomain(domainId)
					.getDescription() : "";

		return s;
	}


	public void buscaInf(MyGroupsDTO myGroup) {

		Integer numDono = myGroup.getManagerGroupId();
		System.out.println(numDono);
	}

	public String convertByte(BigDecimal bytes) {

		Long bte = bytes.setScale(3, BigDecimal.ROUND_UP).longValueExact();

		long kilobyte = 1024;
		long megabyte = kilobyte * 1024;
		long gigabyte = megabyte * 1024;
		long terabyte = gigabyte * 1024;
		
		float floatValue = 0;
		String measure = "";

		
		if ((bte >= kilobyte) && (bte < megabyte)) {
			measure = " KB";
			floatValue =  ((float)bte / kilobyte) ;
		} else if ((bte >= megabyte) && (bte < gigabyte)) {
			measure = " MB";
			floatValue =  ((float)bte / megabyte) ;
		} else if ((bte >= gigabyte) && (bte < terabyte)) {
			measure = " GB";
			floatValue =  ((float)bte / gigabyte) ;
		} else if (bte >= terabyte) {
			measure = " TB";
			floatValue =  ((float)bte / terabyte) ;
		} else {
			measure = " B";
			floatValue = (float)bte ;
		}
		
		if (floatValue == 0) {
			return  String.format("%.0f", floatValue) + measure;
		}else{
			return String.format("%.2f", floatValue) + measure;
		}
	}

	public String totalSpace(){
		Integer groupId  = (Integer) Util.getPropertySession("ID_GROUP");
		GroupSpaceDTO grupo = getGroupSpace(groupId);
		return convertByte(grupo.getTotalSpace());
	}
	
	public String childreanSpace(){
		Integer groupId  = (Integer) Util.getPropertySession("ID_GROUP");
		GroupSpaceDTO grupo = getGroupSpace(groupId);
		return convertByte(grupo.getChildreanSpace());
	}
	
	public String freeSpace(){
		Integer groupId  = (Integer) Util.getPropertySession("ID_GROUP");
		GroupSpaceDTO grupo = getGroupSpace(groupId);
		return convertByte(grupo.getFreeSpace());
	}


	/**
	 * Abre a tela de edio de Group
	 * 
	 * @param QwcssMember
	 * 
	 * @return
	 */
	public String editar() {
		
		Integer groupId = myGroupDeta.getGroupId();

		Calendar inputDate = myGroupDeta.getInputDateLimit();
		Calendar outputDate = myGroupDeta.getOutputDateLimit();
		
		/*if (this.inputDate != null)
			inputDate.setTime(myGroupDeta.getInputDateLimit());

		if (this.outputDate != null)
			outputDate.setTime(this.outputDate);*/
		
		boolean aceptVersioning = false;
		
		if (myGroupDeta.getAceptVersion().equals("S"))
			aceptVersioning = true;
		else if (myGroupDeta.getAceptVersion().equals("N"))
			aceptVersioning = false;
		
		Integer daysLimitDiscart = 1;
		
		if(myGroupDeta.getDaysLimitDiscart() != null )
		daysLimitDiscart = myGroupDeta.getDaysLimitDiscart();
		
		Integer layoutIdFk = myGroupDeta.getLayoutIdFk();

		Integer fileLimitVersions = myGroupDeta.getVersionLimit();
		Integer areaId = myGroupDeta.getAreaId();
		Integer groupManagerId = myGroupDeta.getManagerGroup();
		Integer supportPerson1Id = myGroupDeta.getSuporte1();
		Integer supportPerson2Id = myGroupDeta.getSuporte2();
		Integer fkIdMetadata = myGroupDeta.getFkIdMetadata();

		String groupName = myGroupDeta.getNome();
		String ownerCreator = myGroupDeta.getOwnerCreator();
		String shortDescription = myGroupDeta.getDescription();
		BigDecimal spaceLimit;

		if (alteredSpace != 0) {
			if (spaceOperation.equalsIgnoreCase("add")) {
				spaceLimit = myGroupDeta.getSizeInBytes().add(Util.convertByte(alteredSpace,
						spaceUnitSymbol));
			} else {
				spaceLimit = myGroupDeta.getSizeInBytes().subtract(Util.convertByte(alteredSpace,
						spaceUnitSymbol));
			}
		} else {
			spaceLimit = myGroupDeta.getSizeInBytes();
		}
		String apelido = myGroupDeta.getApelido();

		Integer subordinatedGroupId = myGroupDeta.getSubordinateGroup();
		if(subordinatedGroupId != null){
			if (subordinatedGroupId == 0)
				subordinatedGroupId = null;	
		}
		
		String notificationType = "EMAIL";
		String justificativa = "--";
		String fileSystemStorageType = null;
		try {
			if (myGroupDeta.getFileSystemStorageDomain() != null) {
				
				for (DomainDTO type : storageType) {
					if (type.getId().equals(myGroupDeta.getFileSystemStorageDomain())) {
						fileSystemStorageType = type.getStringValue();
						break;
					}
				}
			}
		} catch (Exception e) {
		}

		MyGroupsDTO retorno = groupService.update(groupId,
				inputDate,
				outputDate,
				daysLimitDiscart,
				fileLimitVersions,
				areaId,
				groupManagerId,
				supportPerson1Id,
				supportPerson2Id,
				subordinatedGroupId,
				groupName,
				ownerCreator,
				shortDescription,
				spaceLimit,
				apelido,
				aceptVersioning,
				fileSystemStorageType,
				notificationType,
				justificativa,
				layoutIdFk,
				fkIdMetadata);


		Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");
		if (retorno.getErrorCode() == 0){
			
			if(layoutIdFk != null &&  layoutIdFk != 0) {
				
				AssignedRuleDTO[] regrasAssociadas = assignedService.listarAssignedRule(retorno.getGroupId());
				
				boolean possoAssociar = true;
				
				if ( regrasAssociadas != null && regrasAssociadas.length > 0 ) {
					if (regrasAssociadas[0].getErrorCode() == 0) {
						
						for (AssignedRuleDTO assignedRuleDTO : regrasAssociadas) {
							AssignedRuleDTO assignedRule = assignedService.desvincularRegla(assignedRuleDTO.getAssignedRuleId());
							
							if ( assignedRule != null ){
								if(assignedRule.getErrorCode() != 0) {
									possoAssociar = false;
									break;
								}
							}
						}
					}
				}
				
				if(possoAssociar)
					assignedService.vincularRegra(10, retorno.getGroupId(), "{\"layout_id\":\""+layoutIdFk+"\"}");
				
			}
			
			messages.info(ViewError.GROUP_UPDATE_SUCESS.getMsg());
			myWrappedGroupDeta  = groupService.myGroupEx(idGroup);
		}else{
			messages.add(retorno.getErrorCode() + ": "+ retorno.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
		}
		myGroupDeta = groupService.getGroupById(idGroup);
		
		return null;
		
	}

	public String getMemberName(Integer id) {
		String memberName = "";

		if (id != null) {
			MemberDTO member = memberService.listar(id);
			if (member != null) {
				if (member.getErrorCode() == 0)
					memberName = member.getMemberName();
			}
		}
		return memberName;
	}

	/**
	 * Cadastra ou atualiza um Group (depende do estado da flag 'alterar')
	 * 
	 * @return
	 */
	public String salvar() {
		
		
		try {
			
		Calendar inputDate = Calendar.getInstance();
		Calendar outputDate = Calendar.getInstance();

		if (this.inputDate != null)
			inputDate.setTime(this.inputDate);

		if (this.outputDate != null)
			outputDate.setTime(this.outputDate);

		Integer daysLimitDiscart = group.getDaysLimitDiscart();
		Integer areaId = group.getAreaId();
		
		Integer groupManagerId = 0;  
		if (loginBean.isGi()) {
			groupManagerId = group.getManagerGroup();
		}else{
			groupManagerId = loginBean.getMember().getMemberId();
		}
		Integer supportPerson1Id = group.getSuporte1();
		Integer supportPerson2Id = group.getSuporte2();
		Integer fkIdMetadata = group.getFkIdMetadata();

		Integer subordinatedGroupId = null;
		if (group.getSubordinateGroup() != null && group.getSubordinateGroup() != 0)
			subordinatedGroupId = group.getSubordinateGroup();

		String groupName = group.getNome();
		String ownerCreator = loginBean.getMember().getMemberName();
		String shortDescription = group.getDescription();
		BigDecimal spaceLimit = null;
		
		if(sizeValue != null)
		 spaceLimit = group.getSizeInBytes().multiply(new BigDecimal(sizeValue));
		 
		String apelido = group.getApelido();
		String msgRetorno = "";

		boolean aceptVersioning = false;
		Integer fileLimitVersions = null;
		if (group.getAceptVersion().equals("S")) {
			aceptVersioning = true;
			if( group.getVersionLimit() != null &&  group.getVersionLimit() != 0)
				fileLimitVersions =  group.getVersionLimit();
			else{
				msgRetorno =  " O número de versões foi definido pelo sistema (1)";
				fileLimitVersions = 1;
			}
		} else {
			aceptVersioning = false;
			fileLimitVersions = 1;
		}

		String notificationType = "EMAIL";	
		String fileSystemStorageType = null;
		try {
			if (group.getFileSystemStorageDomain() != null) {
				
				for (DomainDTO type : storageType) {
					if (type.getId().equals(group.getFileSystemStorageDomain())) {
						fileSystemStorageType = type.getStringValue();
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		

		String justificativa = "---";
		Integer layoutIdFk = group.getLayoutIdFk();
		
		


		MyGroupsDTO retorno = groupService.insert(inputDate,
				outputDate,
				daysLimitDiscart,
				fileLimitVersions,
				areaId,
				groupManagerId,
				supportPerson1Id,
				supportPerson2Id,
				subordinatedGroupId,
				groupName,
				ownerCreator,
				shortDescription,
				spaceLimit,
				apelido,
				aceptVersioning,
				fileSystemStorageType,
				notificationType,
				justificativa,
				layoutIdFk,
				fkIdMetadata);

		this.inputDate = null;
		this.outputDate = null;

			if (retorno.getErrorCode() == 0){
				
				if(layoutIdFk != null &&  layoutIdFk != 0) {
					assignedService.vincularRegra(10, retorno.getGroupId(), "{\"layout_id\":\""+layoutIdFk+"\"}");
				}
				
				messages.info(ViewError.GROUP_INSERT_SUCESS.getMsg() + msgRetorno);
				FacesContext facesContext = FacesContext.getCurrentInstance();
				facesContext.getExternalContext().getFlash().setKeepMessages(true);
				facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "listGrupo");
				return "listGrupos.faces?redirect=true";
			}else{
				messages.add(retorno.getErrorCode() +": "+  retorno.getErrorMsg(), 	FacesMessage.SEVERITY_ERROR);
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.GROUP_INSERT_ERROR.getCode() +": "+ ViewError.GROUP_INSERT_ERROR.getMsg() + " " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
			return null;
		}

	}

	@PostConstruct
	public void init() {
		
		statusAtivo = domainService.returnDomain("STATUS_GI", "ATIVO") != null ? domainService.returnDomain("STATUS_GI", "ATIVO").getId() : 34;
		setGg(false);
		
		setAlteredSpace(0);
		storageType = new DomainDTO[]{domainService.returnDomain("STORAGE_TYPE", "STORAGE_FS")};
		Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");
		String alias = (String)Util.getPropertySession("GROUP_ALIAS");
		if (myGroupDeta == null) {
			if (idGroup != null) {
				myGroupDeta = groupService.getGroupById(idGroup);
			}else {
				if (alias != null) {
					myGroupDeta = groupService.getGroupByAlias(alias);
					if (myGroupDeta != null && myGroupDeta.getErrorCode() != ViewError.OK.getCode()) {
						idGroup = myGroupDeta.getGroupId();
					}
				}
			}
		}
		
		if (myWrappedGroupDeta == null) {
			Integer targetMember = null;
			if (myGroupDeta != null && myGroupDeta.getErrorCode() == ViewError.OK.getCode()) {
				if (!myGroupDeta.getStatus().equals(statusAtivo)) {
					targetMember = myGroupDeta.getManagerGroup();
				}
			}
			setMyWrappedGroupDeta(groupService.myGroupEx(idGroup, targetMember));
		}
		
		MyGroupsEXDTO wGroup = myWrappedGroupDeta.getMyGroupsEx();
		if (myWrappedGroupDeta != null && wGroup.getErrorCode()!= null && wGroup.getErrorCode() == ViewError.OK.getCode()) {
			if (loginBean.isGi()) {
				setGg(true);
			}else{
				MemberDTO memberDto = loginBean.getMember();
				if (myWrappedGroupDeta.getMyGroupsEx().getMemberId().equals(memberDto.getMemberId())
						&& myWrappedGroupDeta.isManager()){
					setGg(true);
				}
			}
		}
		
		group = new GroupDTO();
		group.setAceptVersion("N");
		group.setVersionLimit(1);
		
		Integer idGroupPai = (Integer) Util.getPropertySession("ID_GROUP_SUBNOVO");
		Integer idAreaRaiz = (Integer) Util.getPropertySession("AREA_RAIZ_ID_SUBGROUP");
		String 	operation = (String) Util.getPropertySession("ADD_GROUP_OPERATION");
		
		if(operation != null){
			if(operation.equals(NEW_SUB_GROUP)){
				if(idGroupPai != null){
					GroupDTO grupoPai = groupService.getGroupById(idGroupPai);
					
					if(grupoPai != null && grupoPai.getErrorCode() == 0){
						group.setSubordinateGroup(idGroupPai);
						group.setAreaId(grupoPai.getAreaId());
						group.setFileSystemStorageDomain(grupoPai.getFileSystemStorageDomain());
						group.setManagerGroup(grupoPai.getManagerGroup());
						group.setSuporte1(grupoPai.getSuporte1());
						group.setSuporte2(grupoPai.getSuporte2());
					}
				}
			}
				
			if(operation.equals(COPY_GROUP)){
				if(idGroupPai != null){
					GroupDTO groupSource = groupService.getGroupById(idGroupPai);
					
					if(groupSource != null && groupSource.getErrorCode() == 0){
						group.setCreationDate(groupSource.getCreationDate());
						group.setDaysLimitDiscart(groupSource.getDaysLimitDiscart());
						group.setDescription(groupSource.getDescription());
						group.setNotificationTypeDomain(groupSource.getNotificationTypeDomain());
						
						setInputDate(groupSource.getInputDateLimit().getTime());
						setOutputDate(groupSource.getOutputDateLimit().getTime());
						
						/*group.setOutputDateLimit(groupSource.getOutputDateLimit());
						group.setInputDateLimit(groupSource.getInputDateLimit());*/
						
						group.setSizeInBytes( groupSource.getSizeInBytes().divide(new BigDecimal(1024*1024)));
						group.setVersionLimit(groupSource.getVersionLimit());
						group.setAreaId(groupSource.getAreaId());
						group.setFileSystemStorageDomain(groupSource.getFileSystemStorageDomain());
						group.setManagerGroup(groupSource.getManagerGroup());
						group.setSuporte1(groupSource.getSuporte1());
						group.setSuporte2(groupSource.getSuporte2());
						group.setSubordinateGroup(groupSource.getSubordinateGroup());
					}
				}
			}
				
			if(idAreaRaiz != null)	
				group.setAreaId(idAreaRaiz);
			
			Util.setPropertySessao("ID_GROUP_SUBNOVO", null);
			Util.setPropertySessao("AREA_RAIZ_ID_SUBGROUP", null);
			Util.setPropertySessao("ADD_GROUP_OPERATION", null);
		}
		
	}

	public InformationGroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(InformationGroupService groupService) {
		this.groupService = groupService;
	}


	public String setarId(MyGroupsWrapper myGroup, boolean upload) throws IOException {
		
		if (myGroup == null)
			return null;
		
		
		if (myGroup.getMyGroupsEx().getGroupAlias() != null) {
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getMyGroupsEx().getGroupAlias());
			setGroupAlias(myGroup.getMyGroupsEx().getGroupAlias());
			
		}
		
		if (myGroup.getMyGroupsEx().getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getMyGroupsEx().getGroupId());
			setGrupoId(myGroup.getMyGroupsEx().getGroupId());
			Cookie cookie = cookieWork.getCookie(String.valueOf(myGroup.getMyGroupsEx().getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(myGroup.getMyGroupsEx().getGroupId()), String.valueOf(qtdeAcesso), expiry);			
		}
		
		if (upload)
			return null;
		else {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/listArquivosLazy.faces");
			return "listArquivosLazy.faces?redirect=trues";
		}
	}
	
	public String setarId(MyGroupsDTO myGroup, boolean upload) throws IOException {

		if (myGroup == null)
			return null;


		if (myGroup.getApelido() != null)
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getApelido());

		if (myGroup.getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getGroupId());
			Cookie cookie = cookieWork.getCookie(String.valueOf(myGroup.getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(myGroup.getGroupId()), String.valueOf(qtdeAcesso), expiry);			
		}

		if (upload)
			return null;
		else {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/listArquivosLazy.faces");
			return "listArquivosLazy.faces?redirect=trues";
		}
	}

	public String setarIdMob(MyGroupsDTO myGroup, boolean upload) throws IOException {

		if (myGroup == null)
			return null;


		if (myGroup.getApelido() != null)
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getApelido());

		if (myGroup.getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getGroupId());
			Cookie cookie = cookieWork.getCookie(String.valueOf(myGroup.getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(myGroup.getGroupId()), String.valueOf(qtdeAcesso), expiry);
		}

		if (upload)
			return null;
		else {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/listArquivosmob.faces");
			return "listArquivosmob.faces?redirect=trues";
		}
	}

	public String setarId(GroupDTO myGroup, boolean upload) throws IOException {

		if (myGroup == null)
			return null;


		if (myGroup.getApelido() != null){
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getApelido());
			setGroupAlias(myGroup.getApelido());
		}

		if (myGroup.getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getGroupId());
			setGrupoId(myGroup.getGroupId());
			
			Cookie cookie = cookieWork.getCookie(String.valueOf(myGroup.getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(myGroup.getGroupId()), String.valueOf(qtdeAcesso), expiry);
		}


		if (upload)
			return null;
		else {

			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/listArquivosLazy.faces");
			return "listArquivosLazy.faces?redirect=trues";
		}
	}

	public String setarIdGrupo(MyGroupsDTO myGroup) throws IOException {

		if (myGroup == null)
			return null;


		if (myGroup.getApelido() != null)
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getApelido());

		if (myGroup.getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getGroupId());
			Cookie cookie = cookieWork.getCookie(String.valueOf(myGroup.getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(myGroup.getGroupId()), String.valueOf(qtdeAcesso), expiry);
		}

		return null;

	}
	
	public String goToDashBoard(MyGroupsWrapper myGroup) throws IOException  {
		
		if (myGroup == null)
			return null;
//		int grupoid = myGroup.getMyGroupsEx().getGroupId();
		
		
		if (myGroup.getMyGroupsEx().getGroupAlias() != null)
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getMyGroupsEx().getGroupAlias());
		
		if (myGroup.getMyGroupsEx().getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getMyGroupsEx().getGroupId());
		}
		
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/listdashboard.faces");
		return "listdashboard.faces?redirect=trues";
	}

	public String setarIdRegraGrupo(MyGroupsWrapper myGroup) throws IOException {
		
		if (myGroup == null)
			return null;
//		int grupoid = myGroup.getMyGroupsEx().getGroupId();
		
		
		if (myGroup.getMyGroupsEx().getGroupAlias() != null)
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getMyGroupsEx().getGroupAlias());
		
		if (myGroup.getMyGroupsEx().getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getMyGroupsEx().getGroupId());
			Cookie cookie = cookieWork.getCookie(String.valueOf(myGroup.getMyGroupsEx().getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(myGroup.getMyGroupsEx().getGroupId()), String.valueOf(qtdeAcesso), expiry);
		}
		
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/configGrupo.faces");
		return "configGrupo.faces?redirect=trues";
		
	}
	
	public String setarIdRegraGrupo(MyGroupsDTO myGroup) throws IOException {

		if (myGroup == null)
			return null;
//		int grupoid = myGroup.getGroupId();


		if (myGroup.getApelido() != null)
			Util.setPropertySessao("GROUP_ALIAS", myGroup.getApelido());

		if (myGroup.getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", myGroup.getGroupId());
			Cookie cookie = cookieWork.getCookie(String.valueOf(myGroup.getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(myGroup.getGroupId()), String.valueOf(qtdeAcesso), expiry);
		}


		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/configGrupo.faces");
		return "configGrupo.faces?redirect=trues";

	}

	public String setarIdRegraGrupo(GroupDTO group) throws IOException {

		if (group == null)
			return null;
//		int grupoid = group.getGroupId();


		if (group.getApelido() != null)
			Util.setPropertySessao("GROUP_ALIAS", group.getApelido());

		if (group.getGroupId() != null){
			Util.setPropertySessao("ID_GROUP", group.getGroupId());
			Cookie cookie = cookieWork.getCookie(String.valueOf(group.getGroupId()));
			Integer qtdeAcesso = 0;
			if(cookie != null){
				qtdeAcesso = Integer.parseInt(cookie.getValue());
			}
			int expiry = 60 * 60 * 24 * 365 * 10;
			qtdeAcesso = qtdeAcesso + 1;
			cookieWork.setCookie(String.valueOf(group.getGroupId()), String.valueOf(qtdeAcesso), expiry);
		}

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/configGrupo.faces");
		return "configGrupo.faces?redirect=trues";
		
	}
	
	public String setarIdRegraGrupo() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/configGrupo.faces");
		return "configGrupo.faces?redirect=trues";
	}
	
	public String setarIdRegraGrupo(List<FileListDTO> file) throws IOException {

		if (file == null || file.isEmpty())
			return null;
		
		file.get(0);
		
		Integer grupoid = file.get(0).getGroupId();
		
		GroupDTO group = groupService.getGroupById(grupoid);
		

		if (group.getApelido() != null)
			Util.setPropertySessao("GROUP_ALIAS", group.getApelido());

		if (file.get(0).getGroupId() != null)
			Util.setPropertySessao("ID_GROUP", file.get(0).getGroupId());

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/configGrupo.faces");
		return "configGrupo.faces?redirect=trues";

	}
	
	
	public GroupDTO getGroup() {
		if (group == null)
			group = new GroupDTO();
		return group;
	}

	public void setGroup(GroupDTO group) {
		this.group = group;
	}

	public boolean isAlterar() {
		return alterar;
	}

	public void setAlterar(boolean alterar) {
		this.alterar = alterar;
	}

	/*public MyGroupsDTO[] getMyGroupsStatus(String status) {
		return groupService.myGroups("STATUS_GI", status);
	}*/
	
	public List<MyGroupsWrapper> getMyGroupsStatusEx(String status, Integer page, Integer records) {
		WrappedGroupsContainer container = groupService.myGroupsEx("STATUS_GI", status, page,records);
		return container != null? container.getList(): null;
	}


	public DomainDTO[] getStorageType() {
		return storageType;
	}

	public void setStorageType(DomainDTO[] storageType) {
		this.storageType = storageType;
	}

	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public Date getOutputDate() {
		return outputDate;
	}

	public void setOutputDate(Date outputDate) {
		this.outputDate = outputDate;
	}

	public DomainDTO[] getStatusList() {
		statusList = domainService.returnDomain("STATUS_GI");
		return statusList;
	}

	public void setStatusList(DomainDTO[] statusList) {
		this.statusList = statusList;
	}

	public String getStatus(Integer status) {
		return domainService.returnDomain(status).getDescription();
	}

	public MyGroupsDTO getMyGroup() {
		return myGroup;
	}
	

	public void setMyGroup(MyGroupsDTO myGroup) {
		this.myGroup = myGroup;
	}

	public Boolean getIsGi() {
		return isGi;
	}

	public void setIsGi(Boolean isGi) {
		this.isGi = isGi;
	}
	
	public boolean isGg(GroupDTO group) {

		if (group != null && group.getErrorCode() == 0) {
			MemberDTO member = loginBean.getMember();
			
			if (group.getManagerGroup().compareTo(member.getMemberId()) == 0 || loginBean.isGi()) {
				return true;
			} else {
				
				
				MyGroupsEXDTO[] privs = memberService.listarByGroupView(group.getApelido());
				DomainDTO domaingestor = domainService.returnDomain("PRIVILEGIOS", "GESTOR-GRP");
				
				if (privs != null) {
					
					for (MyGroupsEXDTO myGroupsEXDTO : privs) {
						if (myGroupsEXDTO.getMemberId().equals(member.getMemberId()) 
								&& myGroupsEXDTO.getPrivStringValue().contains(domaingestor.getStringValue())) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}

	public GroupDTO[] getGruposFilhos(Integer fatherGroup) {
		gruposFilhos = groupService.listar(fatherGroup);
		return gruposFilhos;
	}

	public void setGruposFilhos(GroupDTO[] gruposFilhos) {
		this.gruposFilhos = gruposFilhos;
	}

	public GroupDTO getMyGroupDeta() {
		return myGroupDeta;
	}

	public void setMyGroupDeta(GroupDTO myGroupDeta) {
		this.myGroupDeta = myGroupDeta;
	}

	public GroupSpaceDTO getGroupSpace(Integer groupId) {
		GroupSpaceDTO space = new GroupSpaceDTO();
		space = groupService.returnGroupSpace(groupId, null);
		return space;
	}

	public Integer getAlteredSpace() {
		return alteredSpace;
	}

	public void setAlteredSpace(Integer alteredSpace) {
		this.alteredSpace = alteredSpace;
	}

	public String getSpaceOperation() {
		return spaceOperation;
	}

	public void setSpaceOperation(String spaceOperation) {
		this.spaceOperation = spaceOperation;
	}

	public String getSpaceUnitSymbol() {
		return spaceUnitSymbol;
	}

	public void setSpaceUnitSymbol(String spaceUnitSymbol) {
		this.spaceUnitSymbol = spaceUnitSymbol;
	}
	
	public Double getSizeValue() {
		return sizeValue;
	}

	public void setSizeValue(Double sizeValue) {
		this.sizeValue = sizeValue;
	}

	public Integer getStatusAtivo() {
		return statusAtivo;
	}

	public void setStatusAtivo(Integer statusAtivo) {
		this.statusAtivo = statusAtivo;
	}
	
	public int getGroupSpaceBarValue(MyGroupsEXDTO group) {
		int barValue = 0; 
		if (group.getErrorCode() == ViewError.OK.getCode()) {
			try {
				
				double divisor = group.getGroupOriginalSpaceLimit().doubleValue();
				double dividendo = group.getGroupOriginalSpaceLimit().subtract(group.getGroupFreespace()).doubleValue() * 100;
				double tempDouble = dividendo/divisor;
				barValue = (int) tempDouble;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return barValue;
	}
	
	public int getSharedSpaceBarValue(MyGroupsEXDTO group) {
		int barValue = 0; 
		
		if(group != null){
			if (group.getErrorCode() == ViewError.OK.getCode()) {
				try {
					
					double divisor = group.getGroupTotalSpace().doubleValue();
					double dividendo = group.getGroupChildreanSpace().doubleValue() * 100;
					double tempDouble = dividendo/divisor;
					barValue = (int) tempDouble;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return barValue;
	}
	
	
	public boolean isSubGroupFull(Integer groupId) {
		GroupSpaceDTO space = getGroupSpace(groupId);
		
		if (space != null) {
			return space.getSpaceLimit().compareTo(space.getChildreanSpace()) <= 0;
		}else{
			return false;
		}
	}
	
	public BigDecimal getOcupiedSpace(MyGroupsEXDTO group) {
		BigDecimal big = new BigDecimal(0);
		if (group.getErrorCode() == ViewError.OK.getCode()) {

			big = group.getGroupOriginalSpaceLimit().subtract(group.getGroupFreespace());
		}
		
		return big;
	}

	public String getGroupAlias() {
		return groupAlias;
	}

	public void setGroupAlias(String groupAlias) {
		this.groupAlias = groupAlias;
	}

	public Integer getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Integer grupoId) {
		this.grupoId = grupoId;
	}

	public MyGroupsWrapper getMyWrappedGroupDeta() {
		return myWrappedGroupDeta;
	}

	public void setMyWrappedGroupDeta(MyGroupsWrapper myWrappedGroupDeta) {
		this.myWrappedGroupDeta = myWrappedGroupDeta;
	}

	public boolean isGg() {
		return gg;
	}

	public void setGg(boolean gg) {
		this.gg = gg;
	} 

}
