package br.com.pix.qware.qwcfp.beans.uties;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.PrivilegioDTO;
import br.com.qwcss.ws.dto.ListPrivsDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.MyGroupsEXDTO;
import br.com.qwcss.ws.dto.SimpleDTO;


@Named("listMemberOfGroupBean")
@RequestScoped
public class ListMembersOfGroup extends AbstractBean {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5156770612075698511L;

	@Inject
	private MemberService		memberService;

	@Inject
	private PrivilegiosService	privService;
	
	@Inject
	private FacesMessages			messages;
	
	private List<MyGroupsWrapper> memberMyGroup;

	private PrivilegioDTO[]		privilegioMyGroup;
	

	@PostConstruct
	@Override
	public void init() {
		String group = (String) Util.getPropertySession("GROUP_ALIAS");
		if (group != null) {
			refreshMembers(group);
		}
	}
	
	 public boolean filterByName(Object value, Object filter, Locale locale) {
	        String filterText = (filter == null) ? null : filter.toString().trim();
	        if(filterText == null||filterText.equals("")) {
	            return true;
	        }
	         
	        if(value == null) {
	            return false;
	        }
	         
	        MyGroupsWrapper mgw = (MyGroupsWrapper) value;
	        return mgw.getMyGroupsEx().getMemberName().toUpperCase().contains(filterText.toUpperCase());
	    }
	
	public void refreshMembers(String group) {
		MyGroupsEXDTO[] list = memberService.listarByGroupView(group);
		ArrayList<MyGroupsWrapper> objList = new ArrayList<MyGroupsWrapper>();
		for (int i = 0; i < list.length; i++) {
			objList.add(new MyGroupsWrapper(list[i]));
		}
		setMemberMyGroup(objList);
	}
	
	public void setMemberId(Integer memberId){
		if(memberId != null){
			Util.setPropertySessao("MEMBER_PRIV_ID", memberId);
		}
	}
	
	public PrivilegioDTO[] populateListPriv() {
		
		Integer memberId = (Integer) Util.getPropertySession("MEMBER_PRIV_ID");
		String group = (String) Util.getPropertySession("GROUP_ALIAS");
		
		 PrivilegioDTO[] privReturn = null;
		
		if(memberId != null){
				ListPrivsDTO privilegios = privService.listar(group, memberId);
				if (privilegios != null) {
					privReturn = privilegios.getListPriv();
			}
		}
		
		return privReturn;
	}
	
	public void removerMembro(Integer memberId){
		String group = (String) Util.getPropertySession("GROUP_ALIAS");
		if(memberId != null){
			SimpleDTO simpleDTO = privService.removeUserFromGroup(memberId, group);
			
			if(simpleDTO != null){
				if( simpleDTO.getErrorCode() == 0){
					messages.add(ViewError.USER_REMOVE_GROUP_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
				}else{
					messages.add(simpleDTO.getErrorCode() + " " + simpleDTO.getErrorMsg(), FacesMessage.SEVERITY_ERROR);	
				}
			}else{
				messages.add(ViewError.USER_REMOVE_GROUP_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
			}
			
			refreshMembers(group);
		}
	}
	


	public PrivilegioDTO[] getPrivilegioMyGroup() {
		return privilegioMyGroup;
	}

	public void setPrivilegioMyGroup(PrivilegioDTO[] privilegioMyGroup) {
		this.privilegioMyGroup = privilegioMyGroup;
	}

	public List<MyGroupsWrapper> getMemberMyGroup() {
		return memberMyGroup;
	}

	public void setMemberMyGroup(List<MyGroupsWrapper> memberMyGroup) {
		this.memberMyGroup = memberMyGroup;
	}

}
