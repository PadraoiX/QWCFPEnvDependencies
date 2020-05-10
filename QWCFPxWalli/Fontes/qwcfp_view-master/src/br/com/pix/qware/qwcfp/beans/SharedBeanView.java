/**
 * 
 */
package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.ShareService;
import br.com.pix.qware.qwcfp.service.TicketService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.util.download.HttpDownloadTool;
import br.com.pix.qware.qwcfp.util.download.TunneledFile;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.ShareDTO;
import br.com.qwcss.ws.dto.SimpleDTO;

@ManagedBean(name="sharedBean")
@ViewScoped
public class SharedBeanView {

	@Inject
	private ShareService	shareService;

	@Inject
	private FacesMessages	messages;
	
	@Inject
	private HttpDownloadTool downloadTool;
	
	@Inject
	private ConfigService			configService;
	
	@Inject
	private TicketService			ticketService;

	private ShareDTO		selectedSharedFile;

	private List<ShareDTO>	sharedfileList;

	private List<ShareDTO>	selectedSharedList;
	
	//campos do formulário 
	private String					saveAs;
	private String					fileName;
	private String 					statusFile;
	private String 					ticket;
	private String 					httpUrlTransf;
	
	private int 					qtdSharedFiles;
	
	@PostConstruct
	public void init(){
		updateSharedFiles();
	}
	
	public void updateSharedFiles(){
		ShareDTO[] array = shareService.listSharedFiles();
		if (array != null){
			setQtdSharedFiles(array.length);
			sharedfileList = Arrays.asList(array);
		}else
			setQtdSharedFiles(0);
	}

	public void getInputStream() throws Exception {
		getInputStream(null);
	}

public void getInputStream(ShareDTO buttonDto) throws Exception {
		
		setStatusFile(null);

		List<TunneledFile> inputList = new CopyOnWriteArrayList<TunneledFile>();
		
		String fileName = "";
		String saveAs = "";	
		boolean isMultiple = false;

		if (buttonDto != null) {
			selectedSharedList = new ArrayList<ShareDTO>();
			selectedSharedList.add(buttonDto);
		}
		
		ConfigDTO configUrl = configService.carregar("HTTP_TRANSFER_URL", "SYSTEM");

		if (configUrl != null) {
			if (configUrl.getErrorCode() == 0) {
				setHttpUrlTransf(configUrl.getValue());
			} else {
				messages.add(configUrl.getErrorCode() + ": " + configUrl.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
				return;
			}
		} else {
			messages.add(ViewError.TRANSFER_CONFIG_ERROR.getCode() + ": "
					+ ViewError.TRANSFER_CONFIG_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
			return;
		}

	 

		if (selectedSharedList != null) {
			for (ShareDTO selectedFiles : selectedSharedList) {
				

				if (selectedSharedList.size() > 1)
					isMultiple = true;
				else
					isMultiple = false;

				SimpleDTO simpleDTO = new SimpleDTO();
				
				TunneledFile tunneledFile = downloadTool.downloadFileVersioned(selectedFiles
						.getFileName(), selectedFiles.getVersionId(), selectedFiles
						.getFileManagedIdFk(), simpleDTO);

				if (tunneledFile != null) {
					if (isMultiple) {
						inputList.add(tunneledFile);
					} else {
						 saveAs   = tunneledFile.getName();
						 fileName = tunneledFile.getTrueFileName();
					}
				}else {
					setStatusFile(simpleDTO.getErrorMsg());
				}
			}
			
			for (TunneledFile tunneledFile : inputList) {
				 saveAs   += tunneledFile.getName() + ";";
				 fileName += tunneledFile.getTrueFileName() + ";";
			}
			
			if(isMultiple){
				setSaveAs(saveAs.substring(0, saveAs.length() -1));
				setFileName(fileName.substring(0, fileName.length() -1));
			}else {
				setSaveAs(saveAs);
				setFileName(fileName);
				
			}
			
			String usucario = (String) Util.getPropertySession("USER_LDAP");
			String senha = (String) Util.getPropertySession("PASS_LDAP");
			
			
			SimpleDTO simple = ticketService.getTicketDownload("Get", getFileName(), getSaveAs(), "Rep", "Binary", usucario, senha);
			
			if (simple != null) {
				if(simple.getErrorCode() == 0)
					setTicket( simple.getErrorMsg() );
				else{
					setStatusFile(simple.getErrorMsg());
				}
			} else {
				messages.add(ViewError.GET_TICKET_ERROR.getCode() + ": "	+ ViewError.GET_TICKET_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
				return;
			}
			

		} else {
			messages.add(ViewError.FILE_NOT_CHOSSE.getMsg(), FacesMessage.SEVERITY_ERROR);
		}

	}
	
	public void updateList(){
		this.getSharedfileList();
	}

	public ShareDTO getSelectedSharedFile() {
		return selectedSharedFile;
	}

	public void setSelectedSharedFile(ShareDTO selectedSharedFile) {
		this.selectedSharedFile = selectedSharedFile;
	}

	public List<ShareDTO> getSharedfileList() {
		return sharedfileList;
	}

	public void setSharedfileList(List<ShareDTO> sharedfileList) {
		this.sharedfileList = sharedfileList;
	}

	public List<ShareDTO> getSelectedSharedList() {
		return selectedSharedList;
	}

	public void setSelectedSharedList(List<ShareDTO> selectedSharedList) {
		this.selectedSharedList = selectedSharedList;
	}

	public String getSaveAs() {
		return saveAs;
	}

	public void setSaveAs(String saveAs) {
		this.saveAs = saveAs;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatusFile() {
		return statusFile;
	}

	public void setStatusFile(String statusFile) {
		this.statusFile = statusFile;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getHttpUrlTransf() {
		return httpUrlTransf;
	}

	public void setHttpUrlTransf(String httpUrlTransf) {
		this.httpUrlTransf = httpUrlTransf;
	}

	public int getQtdSharedFiles() {
		return qtdSharedFiles;
	}

	public void setQtdSharedFiles(int qtdSharedFiles) {
		this.qtdSharedFiles = qtdSharedFiles;
	}
	
	
	
	
}
