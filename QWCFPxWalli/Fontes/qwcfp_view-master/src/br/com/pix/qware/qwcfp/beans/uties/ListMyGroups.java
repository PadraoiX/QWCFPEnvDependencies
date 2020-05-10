package br.com.pix.qware.qwcfp.beans.uties;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.lazy.MyGroupsLazy;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.QwcfpInitials;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.MyGroupsDTO;

@ManagedBean(name = "myGroupsBean")
@ViewScoped
public class ListMyGroups extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 2408795418711028370L;

	@Inject
	private InformationGroupService	groupService;
	
	@Inject
	private ConfigService			configService;
	
	@Inject
	private FacesMessages			messages;

	private List<MyGroupsDTO>		myGroups;
	
	private MyGroupsLazy			myGroupsEx;
	
	private Integer 				qtdRecord;

	private String					filterValue;
	
	private String					filterValue2;
	
	private boolean					brsStarted;
	
	private MyGroupsWrapper			myGroupsDetail;
	
	private boolean 				expandedDetail;
	
	private String					buttonDetailIcon;
	
	public void expandDetail() {
		expandedDetail = !expandedDetail;
		buttonDetailIcon = expandedDetail?  "fa fa-arrow-right":"fa fa-arrow-left";
	}
	
	public String redirectBrs() {
		
		if(filterValue2 != null && !filterValue2.equals("")){
			Util.setPropertySessao("BRS_FILTER", filterValue2);
			
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			try {
				ec.redirect(ec.getRequestContextPath() + "/pesquisaBrs.faces");
				return "pesquisaBrs.faces?redirect=trues";
			} catch (IOException e) {
			}	
		}
		
		
		messages.add("É necessário informar um parâmetro de pesquisa", FacesMessage.SEVERITY_WARN);
		return "";
	}

	public void filterList() {

		/*if (myGroupsEx != null && !myGroupsEx.isEmpty() && filterValue != null && !filterValue.isEmpty()) {
			
			List<MyGroupsWrapper> resultado = new ArrayList<MyGroupsWrapper>();
			List<String> nome = new ArrayList<String>();

			for (MyGroupsWrapper myGroupsDTO : myGroupsEx) {
				nome.add(myGroupsDTO.getMyGroupsEx().getGroupName().toUpperCase());
			}

			List<String> listaNomes = Lists.newArrayList(Collections2.filter(nome,	Predicates.containsPattern(filterValue.toUpperCase())));

			for (MyGroupsWrapper myGroupsDTO : myGroupsEx) {
				for (String string : listaNomes) {
					if (string.toUpperCase().equals(myGroupsDTO.getMyGroupsEx().getGroupName().toUpperCase())) {
						resultado.add(myGroupsDTO);
					}
				}
			}
			setMyGroupsEx(resultado);
		}
		
		if(filterValue.isEmpty()){
			myGroupsEx = new MyGroupsLazy(groupService);
		}*/

	}

	@PostConstruct
	public void init() {
		QwcfpInitials.getInstance().initializeWebService();
		
		ConfigDTO cfg = configService.carregar("PROP_QWISACTIVE", "SYSTEM_PROP");
		if (cfg.getErrorCode() == 0) {
			brsStarted = cfg.getValue().equalsIgnoreCase("true") ? true:false;
		}else{
			brsStarted = false;
		}
		
		String pesquisaGrupo = (String)  Util.getPropertySession("pesquisaGrupo");
		
		if(pesquisaGrupo != null && !pesquisaGrupo.isEmpty()){
			myGroupsEx = new MyGroupsLazy(groupService, pesquisaGrupo);	
			setFilterValue(pesquisaGrupo);
		}else{
			myGroupsEx = new MyGroupsLazy(groupService);
		}
	
		expandedDetail = false;
		buttonDetailIcon= "fa fa-arrow-left";
			
		//myGroups = groupService.myGroups("STATUS_GI", "ATIVO") != null ? Arrays.asList(groupService.myGroups("STATUS_GI", "ATIVO")) : null ;
	}
	
	public void updateListMyGroups () {
		myGroupsEx = new MyGroupsLazy(groupService, filterValue);
	}

	public List<MyGroupsDTO> getMyGroups() {
		return myGroups;
	}

	public void setMyGroups(List<MyGroupsDTO> myGroups) {
		this.myGroups = myGroups;
	}

	public InformationGroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(InformationGroupService groupService) {
		this.groupService = groupService;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public MyGroupsLazy getMyGroupsEx() {
		return myGroupsEx;
	}

	public void setMyGroupsEx(MyGroupsLazy myGroupsEx) {
		this.myGroupsEx = myGroupsEx;
	}

	public boolean isBrsStarted() {
		return brsStarted;
	}

	public void setBrsStarted(boolean brsStarted) {
		this.brsStarted = brsStarted;
	}

	public String getFilterValue2() {
		return filterValue2;
	}

	public void setFilterValue2(String filterValue2) {
		this.filterValue2 = filterValue2;
	}

	public MyGroupsWrapper getMyGroupsDetail() {
		return myGroupsDetail;
	}

	public void setMyGroupsDetail(MyGroupsWrapper myGroupsDetail) {
		this.myGroupsDetail = myGroupsDetail;
	}

	public boolean isExpandedDetail() {
		return expandedDetail;
	}

	public void setExpandedDetail(boolean expandedDetail) {
		this.expandedDetail = expandedDetail;
	}

	public String getButtonDetailIcon() {
		return buttonDetailIcon;
	}

	public void setButtonDetailIcon(String buttonDetailIcon) {
		this.buttonDetailIcon = buttonDetailIcon;
	}

}
