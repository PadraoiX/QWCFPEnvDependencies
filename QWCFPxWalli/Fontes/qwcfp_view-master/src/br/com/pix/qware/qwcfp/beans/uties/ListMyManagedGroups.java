package br.com.pix.qware.qwcfp.beans.uties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.qwcss.ws.dto.GroupDTO;

@ManagedBean(name = "listMyManagedGroups")
@ViewScoped
public class ListMyManagedGroups extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 5301722940343914242L;

	@Inject
	private PrivilegiosService		privService;

	@Inject
	private LoginBean				loginBean;

	private List<GroupDTO>			myManagedGroups;

	

	@Override
	@PostConstruct
	public void init() {
		setMyManagedGroups(myManagedGroups());
	}

	private List<GroupDTO> myManagedGroups() {
		if(privService.listManagedGroups() != null && privService.listManagedGroups().length > 0)
			return  Arrays.asList(privService.listManagedGroups());
		else
			return new ArrayList<GroupDTO>();
	}

	public List<GroupDTO> getMyManagedGroups() {
		return myManagedGroups;
	}

	public void setMyManagedGroups(List<GroupDTO> myManagedGroups) {
		this.myManagedGroups = myManagedGroups;
	}

}
