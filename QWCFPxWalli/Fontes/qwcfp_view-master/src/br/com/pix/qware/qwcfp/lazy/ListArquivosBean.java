package br.com.pix.qware.qwcfp.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.ShareService;
import br.com.pix.qware.qwcfp.service.TicketService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.util.download.HttpDownloadTool;
import br.com.pix.qware.qwcfp.util.download.TunneledFile;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.FileAndFileVersionWrapper;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

@ManagedBean(name = "listArquivosBean")
@ViewScoped
public class ListArquivosBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -9066562376820402496L;
	
	private static final String		ICON_STRING_FORMAT	= "icon_%s.png";
	private static final String		DEFAULT_ICON	= "icon_default.png";
	private static final List<String> AVAILABLE_ICON = Arrays.asList( new String[]{"icon_avi.png"
			,"icon_css.png"
			,"icon_doc.png"
			,"icon_docx.png"
			,"icon_eps.png"
			,"icon_fla.png"
			,"icon_flv.png"
			,"icon_gif.png"
			,"icon_gz.png"
			,"icon_html.png"
			,"icon_jar.png"
			,"icon_java.png"
			,"icon_jpeg.png"
			,"icon_jpg.png"
			,"icon_mdb.png"
			,"icon_mid.png"
			,"icon_mov.png"
			,"icon_mp3.png"
			,"icon_mpg.png"
			,"icon_ogg.png"
			,"icon_pdf.png"
			,"icon_php.png"
			,"icon_png.png"
			,"icon_ppt.png"
			,"icon_pptx.png"
			,"icon_psd.png"
			,"icon_rar.png"
			,"icon_rtf.png"
			,"icon_txt.png"
			,"icon_wav.png"
			,"icon_wmv.png"
			,"icon_zip.png"
			,"icon_xls.png"
			,"icon_xlsx.png"
			,"icon_xml.png"});
	
	private ListArquivosLazy		fileListLazy;

	@Inject
	private FileManagedService		fileManagedService;
	
	@Inject
	private FileVersionListService	fileVersionService;

	@Inject
	private FacesMessages			messages;

	@Inject
	private HttpDownloadTool		downloadTool;

	@Inject
	private ShareService			shareService;

	@Inject
	private FileVersionListService	versionService;

	@Inject
	private InformationGroupService	groupService;
	
	@Inject
	private ConfigService			configService;
	
	@Inject
	private TicketService			ticketService;
	
	@Inject
	private DomainService					domainService;
	
	private List<FileListDTOEx>		selectedFileList;
	
	private MyGroupsWrapper			myGroupWrapped;
	
	//campos do formulário
	private String					saveAs;
	private String					fileName;
	private String 					statusFile;
	private String 					groupAlias;
	private String 					ticket;
	
	//General
	private String					justify;

	//sharing
	private Integer					memberToId;
	private Integer					qtdDias;

	//moving
	private Integer					groupToId;
	private String					moveCondition;

	// Versioned Dialog
	private FileListDTOEx				fileToBeVersioned;
	private VersionsDTO				selectedVersion;

	//sharing VS
	private Integer					qtdDiasVs;
	private Integer					memberToIdVs;

	//rendering VS
	private boolean					renderDownloadVs;
	private boolean					renderShareVs;
	private boolean					renderMoveVs;

	private Integer					tabIndex;

	//moving VS
	private String					moveConditionVs;
	private Integer					groupToIdVs;

	private Integer					qtdArquivos;
	
	private String 					httpUrlTransf;
	
	private String					filterValue;
	
	private Calendar				currentTime;
	
	
	

	@PostConstruct
	public void init() {
		String aliasGrupo = (String) Util.getPropertySession("GROUP_ALIAS");
		loadGroup(aliasGrupo);
		setCurrentTime(Calendar.getInstance());
		getCurrentTime().setTime(new java.util.Date(System.currentTimeMillis()));
		
		if(aliasGrupo != null) {
			setFileListLazy(new ListArquivosLazy(fileManagedService, fileVersionService));
			FileListDTOEx[] array = fileManagedService.listar(aliasGrupo, null, 0, 1, false, null, null);
			if(array != null && array.length > 0){
				FileListDTOEx file = array[0];
				if(file != null && file.getErrorCode() == ViewError.OK.getCode()){
					Long total = file.getTotal();
					setQtdArquivos(total.intValue());
				}
			}
			
			
		}
		
		
		cleanVersionManager();
	}
	
	private void loadGroup() {
		loadGroup(null);
	}
	private void loadGroup(String alias) {
		Integer groupId = null;
		try {
			groupId = (Integer) Util.getPropertySession("ID_GROUP");
		} catch (Exception e) {
		}
		
		if (groupId != null) {
			setMyGroupWrapped(groupService.myGroupEx(groupId));
		}else {
			if (alias != null) {
				setMyGroupWrapped(groupService.myGroupEx(alias));
			}
		}
	}
	
	public String getIcon(String extension){
		String iconFileName = String.format(ICON_STRING_FORMAT, extension);
		
		String availableIcon = ""; 
		if (AVAILABLE_ICON.contains(iconFileName)) {
			availableIcon = AVAILABLE_ICON.get(AVAILABLE_ICON.indexOf(iconFileName)); 
		}else {
			availableIcon = DEFAULT_ICON;
		}
		return availableIcon;
	}

	public void onTabChange() {
		if (tabIndex == 0) {
			renderDownloadVs = true;
			renderShareVs = false;
			renderMoveVs = false;
		} else if (tabIndex == 1) {
			renderDownloadVs = false;
			renderShareVs = false;
			renderMoveVs = true;
		} else if (tabIndex == 2) {
			renderDownloadVs = false;
			renderShareVs = true;
			renderMoveVs = false;
		}
	}
	
	public void editAddInfo() {
		
		if (selectedVersion != null) {
			SimpleDTO err = fileManagedService.editAddInfo(selectedVersion.getId(), justify);
			if (err.getErrorCode().compareTo(0) == 0) {
				messages.add(ViewError.EDIT_ADDINFO_SUCCESS.getMsg(), FacesMessage.SEVERITY_INFO);
			}else{
				messages.add(err.getErrorCode() + " : " + err.getErrorMsg(), FacesMessage.SEVERITY_INFO);
			}
		}else{
			messages.add(ViewError.FILE_VERSION_ID_NULL.getCode() + " : " + ViewError.FILE_VERSION_ID_NULL.getMsg(), FacesMessage.SEVERITY_ERROR);
		}
		
	}
	
	public void onVersionSelect() {
		justify = selectedVersion.getAddInformation();
	}
	
	public void cleanVersionManager() {
		justify = "";
		renderDownloadVs = true;
		renderShareVs = false;
		renderMoveVs = false;
		tabIndex = 0;
		moveConditionVs = "NEW-GRP";
		moveCondition = "NEW-GRP";
	}

	public boolean grupoVersionado() {
		String grupoAlias = (String) Util.getPropertySession("GROUP_ALIAS");
		if (grupoAlias != null) {
			GroupDTO grupoDTO = groupService.listar(grupoAlias);
			if (grupoDTO != null) {
				if (grupoDTO.getAceptVersion() == "S")
					return true;
			}
		}

		return false;
	}

	public List<VersionsDTO> listVersions() {

		if (selectedFileList != null && !selectedFileList.isEmpty()) {
			VersionsDTO[] array = versionService.listar(getFileToBeVersioned().getFileId());

			if (array != null)
				return Arrays.asList(array);

		}

		return new ArrayList<VersionsDTO>();
	}
	
	//foi sobreescrito o metodo porque a gente não sabe
	//se tem outro lugar que chama ele passando como parametro um FileListDTO
	public void getInputStream() throws Exception {
			getInputStream(null);
	}
	
	public void putAliasGroup() {
		String apelidoGroup = (String) Util.getPropertySession("GROUP_ALIAS");
		setGroupAlias(apelidoGroup);
	}
	
	/**
	 * Método que monta o caminho onde se encontra o arquivo que o usuário quer baixar e retorna uma {@link #file
	 * stream} para ele no navegador
	 * 
	 * @param versionId
	 * @param memberLogado
	 * @param groupId
	 * @param remotAd
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
  	public void getInputStream(FileListDTOEx buttonDto) throws Exception {
		setStatusFile(null);

		List<TunneledFile> inputList = new CopyOnWriteArrayList<TunneledFile>();
		
		String fileName = "";
		String saveAs = "";	
		boolean isMultiple = false;

		if (buttonDto != null) {
			selectedFileList = new ArrayList<FileListDTOEx>();
			selectedFileList.add(buttonDto);
		}
		
		
		ConfigDTO configUrl = configService.carregar("HTTP_TRANSFER_URL", "SYSTEM");
		
		if(configUrl != null){
			if(configUrl.getErrorCode() == 0){
				setHttpUrlTransf(configUrl.getValue());
			}else{
				messages.add(configUrl.getErrorCode() + ": " + configUrl.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				return;
			}
		}else{
			messages.add(ViewError.TRANSFER_CONFIG_ERROR.getCode() + ": "	+ ViewError.TRANSFER_CONFIG_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
			return;
		}
		

		if (selectedFileList != null) {
			String apelidoGroup = (String) Util.getPropertySession("GROUP_ALIAS");
			setGroupAlias(apelidoGroup);

			for (FileListDTOEx selectedFiles : selectedFileList) {
				if (selectedFileList.size() > 1)
					isMultiple = true;
				else
					isMultiple = false;

				SimpleDTO simpleDTO = new SimpleDTO();
				
				TunneledFile tunneledFile = downloadTool.downloadFileManaged(selectedFiles.getFileName(), selectedFiles.getFileId(), apelidoGroup, simpleDTO);

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
			
			/*if(usucario != null && senha != null){
				senha = QWCrypto.getInstance().decryptPassInternal(new String(senha));	
			}*/
			
			SimpleDTO simple = ticketService.getTicketDownload("Get", getFileName(), getSaveAs(), "Rep", "Binary", usucario, senha);
			
			if (simple != null) {
				if(simple.getErrorCode() == 0){
					setTicket( simple.getErrorMsg() );
				}else{
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

	/**
	 * Compartilha os arquivos selecionados com outro membro.
	 * 
	 */
	public void shareFiles() {

		if (getMemberToId() == null || getQtdDias() == null || getMemberToId() == 0
				|| getQtdDias() < 1) {
			messages.add("para o compartilhamento são necessários a quantidade de dias e o usuário de destino",
					FacesMessage.SEVERITY_WARN);
			return;
		}

		if (selectedFileList != null) {
			for (FileListDTOEx file : selectedFileList) {
				
				VersionsDTO[] tempVersion = fileVersionService.listar(file.getFileId());

				if (tempVersion != null && tempVersion[0].getErrorCode() == ViewError.OK.getCode()) {

					for (int i = 0; i < tempVersion.length; i++) {

						if (tempVersion[i].getErrorCode() == 0) {

							SimpleDTO ret = shareService.shareFile(tempVersion[i].getId(),
									getMemberToId(),
									getQtdDias());
							if (ret.getErrorCode() != 0) {
								messages.add(file.getFileName() + " Versão: "
										+ tempVersion[i].getVersion() + " - " + ret.getErrorMsg(),
										FacesMessage.SEVERITY_ERROR);
								continue;
							} else {
								messages.add(file.getFileName() + " Versão: "
										+ tempVersion[i].getVersion() + " Compartilhada.",
										FacesMessage.SEVERITY_INFO);

							}
						} else {
							messages.add(file.getFileName() + " Versão: "
									+ tempVersion[i].getVersion() + " - "
									+ tempVersion[i].getErrorMsg(),
									FacesMessage.SEVERITY_ERROR);
							continue;
						}
					}
				}
			}
		}
	}

	/**
	 * Move a ultima versão dos arquivos para outros grupos, sobrescrevendo ou criando novas versões.
	 */
	public void moveFiles() {
		if (getGroupToId() == null) {
			messages.add("Grupo de destino não selecionado", FacesMessage.SEVERITY_WARN);
			return;
		}

		SimpleDTO ret = new SimpleDTO();
		for (FileListDTOEx file : selectedFileList) {
			if (getMoveCondition() != null && !getMoveCondition().equals("")) {
				if (getMoveCondition().compareTo("NEW-GRP") == 0) {
					ret = fileManagedService
							.moveFileNew(file.getFileId(), getGroupToId());
				} else if (getMoveCondition().compareTo("REPL-GRP") == 0) {
					ret = fileManagedService
							.moveFileRep(file.getFileId(), getGroupToId());
				}

				if (ret.getErrorCode() != 0) {
					messages.add(file.getFileName() 
							+ " - " + ret.getErrorCode() + ": " + ret.getErrorMsg(),
							FacesMessage.SEVERITY_ERROR);
					continue;
				} else {
					messages.add(file.getFileName(), FacesMessage.SEVERITY_INFO);
					continue;
				}
			}
		}

	}
	
	
	

	public void removeFiles() {

		String sucess = new String();
		for (FileListDTOEx selectedFile : selectedFileList) {
			SimpleDTO ret = fileManagedService.deleteFileManagedEx(selectedFile.getFileId());

			if (ret.getErrorCode() != 0) {
				messages.add(selectedFile.getFileName() + ": " + ret.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
			} else {
				sucess += selectedFile.getFileName() + ";";
			}
		}

		if (!sucess.isEmpty()) {
			messages.add("Arquivos: " + sucess + " Removido(s) com sucesso.",
					FacesMessage.SEVERITY_INFO);
		}
		//updateList();
	}

	public void getInputStreamVersioned() {
		setStatusFile(null);
		SimpleDTO simpleDTO = new SimpleDTO();
		
		ConfigDTO configUrl = configService.carregar("HTTP_TRANSFER_URL", "SYSTEM");
		
		if(configUrl != null){
			if(configUrl.getErrorCode() == 0){
				setHttpUrlTransf(configUrl.getValue());
			}else{
				messages.add(configUrl.getErrorCode() + ": " + configUrl.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				return;
			}
		}else{
			messages.add(ViewError.TRANSFER_CONFIG_ERROR.getCode() + ": "	+ ViewError.TRANSFER_CONFIG_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		
	 
		
		
		if(fileToBeVersioned == null){
			messages.add(ViewError.FILE_NOT_CHOSSE.getMsg(), FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		TunneledFile tunneledFile = downloadTool.downloadFileVersioned(fileToBeVersioned.getFileName(), selectedVersion.getId(), fileToBeVersioned.getFileId(), simpleDTO);
		if (tunneledFile != null) {
			setSaveAs(tunneledFile.getName());
			setFileName(tunneledFile.getTrueFileName());
		} else {
			setStatusFile(simpleDTO.getErrorMsg());
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
		

	}

	public void shareVersion() {
		if (memberToIdVs == null || qtdDiasVs == null || memberToIdVs == 0 || qtdDiasVs < 1) {

			messages.add("para o compartilhamento são necessários a quantidade de dias e o usuário de destino",
					FacesMessage.SEVERITY_WARN);
			return;
		}

		SimpleDTO ret = shareService.shareFile(selectedVersion.getId(), memberToIdVs, qtdDiasVs);

		if (ret.getErrorCode() != 0) {
			messages.add(fileToBeVersioned.getFileName() + " Versão: "
					+ selectedVersion.getVersion() + " - " + ret.getErrorMsg(),
					FacesMessage.SEVERITY_ERROR);
			return;
		}

		messages.add(fileToBeVersioned.getFileName() + " Versão: " + selectedVersion.getVersion()
				+ " Compartilhada com sucesso. ", FacesMessage.SEVERITY_INFO);
		return;
	}

	public void moveVersion() {

		if (groupToIdVs == null) {
			messages.add("Grupo de destino não selecionado", FacesMessage.SEVERITY_WARN);
			return;
		}

		if (moveConditionVs != null && moveConditionVs != "") {
			SimpleDTO ret = new SimpleDTO();
			if (moveConditionVs.compareTo("NEW-GRP") == 0) {
				ret = fileManagedService.moveVersionNew(selectedVersion.getId(), groupToIdVs);
			} else if (moveConditionVs.compareTo("REPL-GRP") == 0) {
				ret = fileManagedService.moveVersionRep(selectedVersion.getId(), groupToIdVs);
			}

			if (ret.getErrorCode() != 0) {
				messages.add(fileToBeVersioned.getFileName() + " - Versão :"
						+ selectedVersion.getVersion() + " - " + ret.getErrorCode() + ": "
						+ ret.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
			} else {
				messages.add(fileToBeVersioned.getFileName() + " - Versão :"
						+ selectedVersion.getVersion() + " Movida com sucesso. ",
						FacesMessage.SEVERITY_INFO);
			}
		}
		return;
	}

	public void removeCancelVersion() {
		if (selectedVersion != null) {
			SimpleDTO ret = new SimpleDTO();

			ret = fileManagedService.deleteVersionEx(selectedVersion.getId());

			if (ret.getErrorCode() != 0) {
				messages.add(ret.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			} else {
				messages.add("Versão cancelada com sucesso!", FacesMessage.SEVERITY_INFO);
			}
		}
	}

	public Integer selectedAmount() {
		return selectedFileList != null ? selectedFileList.size() : 0;
	}

	public void updateList() {
		loadGroup();
		setCurrentTime(Calendar.getInstance());
		getCurrentTime().setTime(new java.util.Date(System.currentTimeMillis()));
		setFileListLazy(new ListArquivosLazy(fileManagedService, filterValue, fileVersionService));
	}
	
	public MethodExpression getOrdina() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication().getExpressionFactory().createMethodExpression(context.getELContext(), "#{listArquivosBean.ordina}", Integer.class, new Class[]{Object.class, Object.class});
	}
	
	public int sortFileSize(Object val1, Object val2){
		System.out.println("mySort" + val1 + "/" + val2);
	    return 0;
	}
	
	public String status(VersionsDTO versions) {
		String status = null;
		if (versions != null && versions.getErrorCode() == 0)
			status = domainService.returnDomain(versions.getFileStatusDomIdFk()).getDescription();

		return status;
	}
	

	public ListArquivosLazy getFileListLazy() {
		return fileListLazy;
	}

	public void setFileListLazy(ListArquivosLazy fileListLazy) {
		this.fileListLazy = fileListLazy;
	}

	public List<FileListDTOEx> getSelectedFileList() {
		return selectedFileList;
	}

	public void setSelectedFileList(List<FileListDTOEx> selectedFileList) {
		this.selectedFileList = selectedFileList;
	}

	public Integer getMemberToId() {
		return memberToId;
	}

	public void setMemberToId(Integer memberToId) {
		this.memberToId = memberToId;
	}

	public Integer getQtdDias() {
		return qtdDias;
	}

	public void setQtdDias(Integer qtdDias) {
		this.qtdDias = qtdDias;
	}

	public Integer getGroupToId() {
		return groupToId;
	}

	public void setGroupToId(Integer groupToId) {
		this.groupToId = groupToId;
	}

	public String getMoveCondition() {
		return moveCondition;
	}

	public void setMoveCondition(String moveCondition) {
		this.moveCondition = moveCondition;
	}

	public ShareService getShareService() {
		return shareService;
	}

	public void setShareService(ShareService shareService) {
		this.shareService = shareService;
	}

	public FileVersionListService getVersionService() {
		return versionService;
	}

	public void setVersionService(FileVersionListService versionService) {
		this.versionService = versionService;
	}

	public FileListDTOEx getFileToBeVersioned() {
		if (selectedFileList != null && !selectedFileList.isEmpty())
			fileToBeVersioned = selectedFileList.get(0);
		return fileToBeVersioned;
	}

	public void setFileToBeVersioned(FileListDTOEx fileToBeVersioned) {
		this.fileToBeVersioned = fileToBeVersioned;
	}

	public VersionsDTO getSelectedVersion() {
		return selectedVersion;
	}

	public void setSelectedVersion(VersionsDTO selectedVersion) {
		this.selectedVersion = selectedVersion;
	}

	public Integer getQtdDiasVs() {
		return qtdDiasVs;
	}

	public void setQtdDiasVs(Integer qtdDiasVs) {
		this.qtdDiasVs = qtdDiasVs;
	}

	public Integer getMemberToIdVs() {
		return memberToIdVs;
	}

	public void setMemberToIdVs(Integer memberToIdVs) {
		this.memberToIdVs = memberToIdVs;
	}

	public boolean isRenderDownloadVs() {
		return renderDownloadVs;
	}

	public void setRenderDownloadVs(boolean renderDownloadVs) {
		this.renderDownloadVs = renderDownloadVs;
	}

	public boolean isRenderShareVs() {
		return renderShareVs;
	}

	public void setRenderShareVs(boolean renderShareVs) {
		this.renderShareVs = renderShareVs;
	}

	public boolean isRenderMoveVs() {
		return renderMoveVs;
	}

	public void setRenderMoveVs(boolean renderMoveVs) {
		this.renderMoveVs = renderMoveVs;
	}

	public Integer getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(Integer tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getMoveConditionVs() {
		return moveConditionVs;
	}

	public void setMoveConditionVs(String moveConditionVs) {
		this.moveConditionVs = moveConditionVs;
	}

	public Integer getGroupToIdVs() {
		return groupToIdVs;
	}

	public void setGroupToIdVs(Integer groupToIdVs) {
		this.groupToIdVs = groupToIdVs;
	}

	public Integer getQtdArquivos() {
		return qtdArquivos;
	}

	public void setQtdArquivos(Integer qtdArquivos) {
		this.qtdArquivos = qtdArquivos;
	}

	public MyGroupsWrapper getMyGroupWrapped() {
		return myGroupWrapped;
	}

	public void setMyGroupWrapped(MyGroupsWrapper myGroupWrapped) {
		this.myGroupWrapped = myGroupWrapped;
	}

	public String getJustify() {
		return justify;
	}

	public void setJustify(String justify) {
		this.justify = justify;
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

	public String getGroupAlias() {
		return groupAlias;
	}

	public void setGroupAlias(String groupAlias) {
		this.groupAlias = groupAlias;
	}

	public String getHttpUrlTransf() {
		return httpUrlTransf;
	}

	public void setHttpUrlTransf(String httpUrlTransf) {
		this.httpUrlTransf = httpUrlTransf;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public Calendar getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Calendar currentTime) {
		this.currentTime = currentTime;
	}
	
	public static void main(String[] args) {
		String s = "icon_png.png";
		
		System.out.println(s.split("\\.")[0]);
	}


}
