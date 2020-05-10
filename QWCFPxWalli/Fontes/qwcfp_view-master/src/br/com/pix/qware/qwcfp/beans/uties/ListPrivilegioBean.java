package br.com.pix.qware.qwcfp.beans.uties;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.SortOrder;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AreaDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.ListPrivsDTO;

@ManagedBean(name = "listPrivilegio")
@ViewScoped
public class ListPrivilegioBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= -347722654359722583L;

	@Inject
	private PrivilegiosService					privService;
	
	@Inject
	private AreaService areaService;
	
	@Inject
	private InformationGroupService	groupService;

	private List<br.com.qwcss.ws.PrivilegioDTO>	privArea;

	private List<br.com.qwcss.ws.PrivilegioDTO>	privGroup;

	@PostConstruct
	public void init() {
		setPrivArea(updatePrivArea());
		setPrivGroup(updatePrivGrp());
	}
	
	public int sortIdByStringArea(Object obj1, Object obj2) {
		Integer id1 = (Integer) obj1;
		Integer id2 = (Integer) obj2;
		
		if (id1 != null && id2 != null) {
			AreaDTO area1 = areaService.carregarEx(id1);
			AreaDTO area2 = areaService.carregarEx(id2);
			
			if(area1 != null && area2 != null  ){
				 int value = ((Comparable) area1.getNome()).compareTo(area2.getNome());
					return value;
			}
		}
		
		return 1;
 
	}
	
	public int sortIdByStringGroup(Object obj1, Object obj2) {
		Integer id1 = (Integer) obj1;
		Integer id2 = (Integer) obj2;
		
		if (id1 != null && id2 != null) {
			GroupDTO grupo1 = groupService.getGroupById(id1);
			GroupDTO grupo2 = groupService.getGroupById(id2);
			
			if(grupo1 != null && grupo2 != null  ){
				 int value = ((Comparable) grupo1.getNome()).compareTo(grupo2.getNome());
					return value;
			}
		}
		
		return 1;
 
	}


	public List<br.com.qwcss.ws.PrivilegioDTO> updatePrivArea() {
		List<br.com.qwcss.ws.PrivilegioDTO> listaPriv = null;
		ListPrivsDTO privArea = privService.returnPrivArea();

		if (privArea != null) {
			if (privArea.getErrorCode() == ViewError.OK.getCode()) {
				br.com.qwcss.ws.PrivilegioDTO[] priv = privArea.getListPriv();
				if(priv != null)
					listaPriv = Arrays.asList(priv);

			}
		}

		return listaPriv;
	}

	public List<br.com.qwcss.ws.PrivilegioDTO> updatePrivGrp() {
		List<br.com.qwcss.ws.PrivilegioDTO> listaPriv = null;
		ListPrivsDTO privGrp = privService.returnPrivGrp();

		if (privGrp != null) {
			if (privGrp.getErrorCode() == ViewError.OK.getCode()) {
				br.com.qwcss.ws.PrivilegioDTO[] priv = privGrp.getListPriv();
				if (priv != null) {
					listaPriv = Arrays.asList(priv);
				}
			}
		}

		return listaPriv;
	}

	public List<br.com.qwcss.ws.PrivilegioDTO> getPrivGroup() {
		return privGroup;
	}

	public void setPrivGroup(List<br.com.qwcss.ws.PrivilegioDTO> privGroup) {
		this.privGroup = privGroup;
	}

	public List<br.com.qwcss.ws.PrivilegioDTO> getPrivArea() {
		return privArea;
	}

	public void setPrivArea(List<br.com.qwcss.ws.PrivilegioDTO> listPrivsDTO) {
		this.privArea = listPrivsDTO;
	}

}
