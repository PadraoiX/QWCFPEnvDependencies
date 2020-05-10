package br.com.pix.qware.qwcfp.beans.uties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.cliapi.QWException.QWException;
import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AreaDTO;
/*@ManagedBean(name="listAreasSuggestBean")
@ViewScoped*/
@Named("listAreasSuggestBean")
@RequestScoped
public class ListAreasSuggestBean extends AbstractBean{
	
	private List<AreaDTO>		areas;
	
	@Inject
	private AreaService			areaService;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		updateAreas();
	}
	
	public void updateAreas()   {
		
		String usucario = (String) Util.getPropertySession("USER_LDAP");
		String senha = (String) Util.getPropertySession("PASS_LDAP");
		
		if(usucario != null && senha != null){
			try {
				senha = QWCrypto.getInstance().decryptPassInternal(new String(senha));
				AreaDTO[] result  = areaService.listarAreas(usucario, senha);
				AreaDTO firstMember = null;
				
				if (result != null) {
					firstMember = result[0];
				}
				if (firstMember != null && firstMember.getErrorCode() == ViewError.OK.getCode()) {
					setAreas(Arrays.asList(result));
				}else{
					setAreas( new ArrayList<AreaDTO>() );
				}
			} catch (QWException e) {
				e.printStackTrace();
			}
			
		}
	}

	public List<AreaDTO> getAreas() {
		return areas;
	}

	public void setAreas(List<AreaDTO> areas) {
		this.areas = areas;
	}

}
