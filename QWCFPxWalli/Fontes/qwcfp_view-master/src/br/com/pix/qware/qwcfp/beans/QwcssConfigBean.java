package br.com.pix.qware.qwcfp.beans;


import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean( name= "configBean")
@ViewScoped
public class QwcssConfigBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6746113804129665953L;

	@Inject
	private ConfigService		configService;

	@Inject
	private FacesMessages		messages;

	private List<ConfigDTO>		configs;

	private ConfigDTO			configSave;

	private ConfigDTO			configEdit;
	
	private boolean 			disable;

	@PostConstruct
	public void init() {

		super.init();
		disable = true;
		updateConfigs();
		configSave = new ConfigDTO();
	}

	public void updateConfigs() {
		ConfigDTO[] configArray = configService.listarConfig();

		if (configArray != null) {
			if (configArray[0].getErrorCode() == 0) {
				configs = Arrays.asList(configArray);
			}
		}
	}

	/**
	 * Altera um registro cadastrado previamente no banco de dados
	 * 
	 * @return String outcome
	 */
	public String editar() {
		String key = configEdit.getKey();
		String value = configEdit.getValue();
		String context = configEdit.getContext();

		SimpleDTO configDTO = configService.alterar(key, value, context);
		if (configDTO.getErrorCode() != 0) {
			messages.add(configDTO.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			return null;
		} else {

			updateConfigs();
			messages.add(ViewError.CONFIG_UPDATE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			return null;
		}
	}
	
	/**
	 * Exclui um registro cadastrado previamente no banco de dados
	 * @param key 
	 * @param context 
	 * 
	 * @return String outcome
	 */
	public void excluir(){
		if(configEdit != null){
			SimpleDTO configDTO = configService.excluir(configEdit.getKey(), configEdit.getContext());
			if (configDTO.getErrorCode() != 0) {
				messages.add(configDTO.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			} else {
				updateConfigs();
				messages.add(ViewError.CONFIG_DELETE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			}
		}else{
			messages.add(ViewError.WEBSERVICE_OFF_ERR.getCode() +" : "+ ViewError.WEBSERVICE_OFF_ERR.getMsg(), FacesMessage.SEVERITY_ERROR);	
		}
	}


	/**
	 * Cadastra uma configurao
	 * 
	 * @return
	 */
	public void salvar() {
		String key = getConfigSave().getKey();
		String value = getConfigSave().getValue();
		String context = getConfigSave().getContext();
		configEdit = new ConfigDTO();
		
		SimpleDTO configDTO = configService.inserir(key, value, context);

		if (configDTO.getErrorCode() != 0) {
			messages.error(configDTO.getErrorMsg());
		} else {
			messages.add(ViewError.CONFIG_ADD_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			configSave = new ConfigDTO();
		}
		
		updateConfigs();

	}

	public ConfigDTO getConfigEdit() {
		return configEdit;
	}

	public void setConfigEdit(ConfigDTO configEdit) {
		this.configEdit = configEdit;
	}

	public ConfigDTO getConfigSave() {
		return configSave;
	}

	public void setConfigSave(ConfigDTO configSave) {
		this.configSave = configSave;
	}

	public ConfigDTO[] getConfigByContext(String key) {
		ConfigDTO[] configs = configService.carregarByContext(key);
		return configs;
	}

	public ConfigDTO[] getConfigByKey(String key) {
		ConfigDTO[] configs = configService.carregarByKey(key);
		return configs;
	}

	public List<ConfigDTO> getConfigs() {
		return configs;
	}

	public void setConfigs(List<ConfigDTO> configs) {
		this.configs = configs;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

}
