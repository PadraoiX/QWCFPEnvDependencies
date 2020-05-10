package br.com.pix.qware.qwcfp.beans;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.VolumeInformationService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.VolumeDTO;

/**
 * Bean da tela de cadastro de Ns
 */
@ManagedBean(name = "volumeBean")
@ViewScoped

public class QwcssVolumeInformationBean extends AbstractBean {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;

	@Inject
	private VolumeInformationService	volumeService;

	@Inject
	private DomainService				domainService;

	@Inject
	private FacesMessages				messages;

	private List<VolumeDTO>				volumes;

	private VolumeDTO					volume;

	private DomainDTO[]					statusList;

	private String						sizeVol;
	
	private boolean						disable;
	
	
	@PostConstruct
	@Override
	public void init(){
		super.init();
		this.setDisable(true);
		statusList = domainService.returnDomain("STATUS_VOLUME");
		updateVolumes();
	}
	
	public void updateVolumes(){
		VolumeDTO[] volumesArray = volumeService.volumes();
		
		if(volumesArray != null && volumesArray.length > 0)
			this.volumes = Arrays.asList(volumesArray);
		else
			this.volumes = null;
	}

	/**
	 * Obtm a lista de volumes
	 * 
	 * @return Lista de volumes
	 */
	public List<VolumeDTO> getVolumes() {
		return this.volumes;
	}

	public String convertByte(String bytes) {

		Long bte = Long.valueOf(bytes).longValue();

		long kilobyte = 1024;
		long megabyte = kilobyte * 1024;
		long gigabyte = megabyte * 1024;
		long terabyte = gigabyte * 1024;

		if ((bte >= 0) && (bte < kilobyte)) {
			return bte + " B";

		} else if ((bte >= kilobyte) && (bte < megabyte)) {
			return (bte / kilobyte) + " KB";

		} else if ((bte >= megabyte) && (bte < gigabyte)) {
			return (bte / megabyte) + " MB";

		} else if ((bte >= gigabyte) && (bte < terabyte)) {
			return (bte / gigabyte) + " GB";

		} else if (bte >= terabyte) {
			return (bte / terabyte) + " TB";

		} else {
			return bte + " Bytes";
		}

	}
	
	public String convertByteEdit(BigDecimal bytes) {

		
		if(bytes != null){
			Long bte = bytes.longValue();
			
			long kilobyte = 1024;
			long megabyte = kilobyte * 1024;
			long gigabyte = megabyte * 1024;
			long terabyte = gigabyte * 1024;

			if ((bte >= 0) && (bte < kilobyte)) {
				setSizeVol("1");
				return Long.toString(bte);

			} else if ((bte >= kilobyte) && (bte < megabyte)) {
				setSizeVol("1024");
				return Long.toString(bte / kilobyte);

			} else if ((bte >= megabyte) && (bte < gigabyte)) {
				setSizeVol("1048576");
				return Long.toString(bte / megabyte);

			} else if ((bte >= gigabyte) && (bte < terabyte)) {
				setSizeVol("1073741824");
				return Long.toString(bte / gigabyte);

			} else if (bte >= terabyte) {
				setSizeVol("1099511627776");
				return Long.toString(bte / gigabyte);
			} else {
				return Long.toString(bte);
			}
		}
		
		return null;

	}

	/**
	 * Cadastra ou atualiza um Volume (depende do estado da flag 'alterar')
	 * 
	 * @return
	 */

	public String salvar() {

		try {

			BigDecimal tmh = new BigDecimal(sizeVol);

			String status = domainService.returnDomain(volume.getStatus()).getStringValue();

			String rootFullPathVol = volume.getRootFullPathVol();

			BigDecimal sizeInBytes = (volume.getSizeInBytes().multiply(tmh));

			VolumeDTO retorno = volumeService.insert(status, rootFullPathVol, sizeInBytes);

			if (retorno != null) {
				if (retorno.getErrorCode() == 0) {
					messages.info(ViewError.VOLUME_INSERT_SUCESS.getMsg());
					FacesContext facesContext = FacesContext.getCurrentInstance();
					facesContext.getExternalContext().getFlash().setKeepMessages(true);
					facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "listVolume");
					return "listVolume.faces?redirect=true";
				} else
					messages.add(retorno.getErrorCode() + ": " + retorno.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);
			} else
				messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() + ": "
						+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.VOLUME_INSERT_ERROR.getCode() + ": "
					+ ViewError.VOLUME_INSERT_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
		}

		return null;
	}

	/**
	 * Abre a tela de edio de Volume
	 * 
	 * @param QwcssVolumeInformation
	 *            volume
	 * 
	 * @return
	 */
	public String editar() {

		try {

			BigDecimal tmh = new BigDecimal(sizeVol);

			String status = domainService.returnDomain(volume.getStatus()).getStringValue();

			String rootFullPathVol = volume.getRootFullPathVol();

			BigDecimal sizeInBytes = (volume.getSizeInBytes().multiply(tmh));

			Integer volumeId = volume.getVolId();

			VolumeDTO retorno = volumeService
					.update(volumeId, status, rootFullPathVol, sizeInBytes);
			/*
			 * Util.DisplayErrMsg(FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance(), retorno.getErrorMsg(),
			 * retorno.getErrorCode().toString());
			 */

			if (retorno.getErrorCode() == 0)
				messages.info(ViewError.VOLUME_UPDATE_SUCESS.getMsg());
			else
				messages.add(retorno.getErrorCode() + ": " + retorno.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);

			updateVolumes();

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.VOLUME_UPDATE_ERROR.getCode() + ": "
					+ ViewError.VOLUME_UPDATE_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
		}

		return null;
	}

	/**
	 * Exclui um Volume
	 * 
	 * @param QwcssVolumeInformation
	 *            volume para excluir
	 * @return
	 */
	public String excluir(VolumeDTO volume) {
		try {

			VolumeDTO retorno = volumeService.delete(volume.getVolId());
			volumes = null;

			if (retorno.getErrorCode() == 0)
				messages.info(ViewError.VOLUME_DELETE_SUCESS.getMsg());
			else
				messages.add(retorno.getErrorCode() + retorno.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);

			updateVolumes();

			

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.VOLUME_DELETE_ERROR.getCode() + ": "+ ViewError.VOLUME_DELETE_ERROR.getMsg(),FacesMessage.SEVERITY_ERROR);
		}
		
		return null;
	}

	/*
	 * @PostConstruct public void init(){ this.volume = new QwcssVolumeInformation(); }
	 */

	public VolumeInformationService getVolumeService() {
		return volumeService;
	}

	public void setVolumeService(VolumeInformationService volumeService) {
		this.volumeService = volumeService;
	}

	public void setVolumes(List<VolumeDTO> volumes) {
		this.volumes = volumes;
	}

	public VolumeDTO getVolume() {
		if (volume == null)
			volume = new VolumeDTO();
		return volume;
	}

	public void setVolume(VolumeDTO volume) {
		this.volume = volume;
	}

	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public DomainDTO[] getStatusList() {
		return statusList;
	}

	public String getStatus(Integer status) {
		return domainService.returnDomain(status).getDescription();
	}

	public void setStatusList(DomainDTO[] statusList) {
		this.statusList = statusList;
	}

	public String getSizeVol() {
		return sizeVol;
	}

	public void setSizeVol(String sizeVol) {
		this.sizeVol = sizeVol;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

}
