package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.http.client.ClientProtocolException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.service.ShareService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.util.download.HttpDownloadTool;
import br.com.pix.qware.qwcfp.util.download.TunneledFile;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

@ManagedBean(name = "fileDownload")
@ViewScoped
public class FileDownloadView extends AbstractBean {

	private static final long		serialVersionUID	= 8010122856839637417L;

	/** Objeto do primeface, para facilitar o download de arquivos */
	private StreamedContent			file;

	@Inject
	private HttpDownloadTool		downloadTool;

	@Inject
	private FileManagedService		fileManagedService;

	@Inject
	private FacesMessages			messages;

	@Inject
	private ShareService			shareService;

	@Inject
	private DomainService			domainService;

	@Inject
	private FileVersionListService	versionService;

	@Inject
	private QwcssFileBean			fileBean;

	private List<FileListDTO>		selectedFileList;

	//sharing

	private Integer					memberToId;

	private Integer					qtdDias;

	//moving

	private Integer					groupToId;

	private String					moveCondition;

	// Versioned Dialog
	private FileListDTO				fileToBeVersioned;
	private VersionsDTO				selectedVersion;

	//rendering VS
	private boolean					renderDownloadVs;
	private boolean					renderShareVs;
	private boolean					renderMoveVs;

	private Integer					tabIndex;
	//sharing VS
	private Integer					qtdDiasVs;
	private Integer					memberToIdVs;

	//moving VS
	private String					moveConditionVs;
	private Integer					groupToIdVs;

	@PostConstruct
	public void init() {
		renderDownloadVs = true;
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

	public StreamedContent getFile() {
		return file;
	}

	public Integer selectedAmount() {
		return selectedFileList != null ? selectedFileList.size() : 0;
	}

	public List<VersionsDTO> listVersions() {

		if (selectedFileList != null && !selectedFileList.isEmpty()) {

			VersionsDTO[] array = versionService.listar(getFileToBeVersioned().getFileid());

			if (array != null)
				return Arrays.asList(array);

		}

		return new ArrayList<VersionsDTO>();
	}

	public void removeCancelVersion() {

		if (selectedVersion != null) {
			String status = selectedVersion.getFileStatusDomStringValue();
			SimpleDTO ret = new SimpleDTO();
			DomainDTO transferDomain = domainService.returnDomain("FILE_VERSION_STATUS",
					"TRANSFERINDO");

			if (transferDomain.getErrorCode() == 0) {
				if (status.compareTo(transferDomain.getStringValue()) == 0) {
					ret = fileManagedService.cancelVersionTransfer(selectedVersion.getId());
				} else {
					ret = fileManagedService.deleteVersion(selectedVersion.getId());
				}

				if (ret.getErrorCode() != 0) {
					messages.add(ret.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				} else {
					messages.add("Versão cancelada com sucesso!", FacesMessage.SEVERITY_INFO);
				}
			}
		}
	}

	public String removeFiles() {

		String sucess = new String();
		for (FileListDTO selectedFile : selectedFileList) {
			SimpleDTO ret = fileManagedService.deleteFileManaged(selectedFile.getFileid());

			if (ret.getErrorCode() != 0) {
				messages.add(selectedFile.getFileName() + ": " + selectedFile.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
			} else {
				sucess += selectedFile.getFileName() + ";";
			}
		}

		if (!sucess.isEmpty()) {
			messages.add("Arquivos: " + sucess + " Removidos com sucesso.",
					FacesMessage.SEVERITY_INFO);
		}
		fileBean.getFileList();
		return null;
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

	/**
	 * Move a ultima versão dos arquivos para outros grupos, sobrescrevendo ou criando novas versões.
	 */
	public void moveFiles() {

		if (getGroupToId() == null) {
			messages.add("Grupo de destino não selecionado", FacesMessage.SEVERITY_WARN);
			return;
		}

		SimpleDTO ret = new SimpleDTO();
		for (FileListDTO file : selectedFileList) {

			VersionsDTO lastVersion = versionService.listarUltimaVersao(file.getFileid());

			if (lastVersion.getErrorCode() == 0) {

				if (getMoveCondition() != null && getMoveCondition() != "") {
					if (getMoveCondition().compareTo("NEW-GRP") == 0) {
						ret = fileManagedService
								.moveVersionNew(lastVersion.getId(), getGroupToId());
					} else if (getMoveCondition().compareTo("REPL-GRP") == 0) {
						ret = fileManagedService
								.moveVersionRep(lastVersion.getId(), getGroupToId());
					}

					if (ret.getErrorCode() != 0) {
						messages.add(file.getFileName() + " - Versão :" + lastVersion.getVersion()
								+ " - " + ret.getErrorCode() + ": " + ret.getErrorMsg(),
								FacesMessage.SEVERITY_ERROR);
					}
				}
			} else {
				messages.add(file.getFileName() + " - " + lastVersion.getErrorCode() + ": "
						+ lastVersion.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			}

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

	/**
	 * Compartilha os arquivos selecionados com outro membro.
	 * 
	 */
	public void shareFiles() {

		if (getMemberToId() == null || getQtdDias() == null || getMemberToId() == 0
				|| getQtdDias() < 1) {
			messages.add(ViewError.SHARE_FILE_NULL_DAYS_AND_USER.getCode()+ ": " + ViewError.SHARE_FILE_NULL_DAYS_AND_USER.getMsg(), FacesMessage.SEVERITY_WARN);
			return;
		}

		for (FileListDTO file : selectedFileList) {

			VersionsDTO[] tempVersion = versionService.listar(file.getFileid());

			if (tempVersion != null) {

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
						}
					} else {
						messages.add(file.getFileName() + " Versão: " + tempVersion[i].getVersion()
								+ " - " + tempVersion[i].getErrorMsg(), FacesMessage.SEVERITY_ERROR);
						continue;
					}
				}
			}
		}
	}

/*	public DefaultStreamedContent getInputStreamVersioned(){
		TunneledFile fileStream = downloadTool.downloadFileVersioned(fileToBeVersioned.getFileName(), selectedVersion.getId(), fileToBeVersioned.getFileid(), );
		
		InputStream returnInput;
		
		try {
			returnInput = fileStream.requestStream();
			
			return new DefaultStreamedContent(returnInput, fileStream.getMimeType(),
					fileToBeVersioned.getFileName());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new DefaultStreamedContent();

	}*/

	/*public DefaultStreamedContent getInputStream() throws Exception {
		return getInputStream(null);
	}*/

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
	//TODO por enquanto o método está verificando as permissões do usuário nem está testando
	//as regras do grupos antes de fazer o download
	/*public DefaultStreamedContent getInputStream(FileListDTO buttonDto) throws Exception {

		String retFileName = "arquivos"
				+ Long.toString(new Date(System.currentTimeMillis()).getTime()) + ".zip";
		List<TunneledFile> inputList = new CopyOnWriteArrayList<TunneledFile>();

		//Hashtable<String, String> errorList = new Hashtable<String, String>();
		if (buttonDto != null) {
			selectedFileList = new ArrayList<FileListDTO>();
			selectedFileList.add(buttonDto);
		}

		if (selectedFileList != null) {
			String apelidoGroup = (String) Util.getPropertySessao("GROUP_ALIAS");

			for (FileListDTO selectedFiles : selectedFileList) {
				try {

					boolean isMultiple;

					if (selectedFileList.size() > 1)
						isMultiple = true;
					else
						isMultiple = false;

					TunneledFile tunneledFile = downloadTool.downloadFileManaged(selectedFiles
							.getFileName(), selectedFiles.getFileid(), apelidoGroup);

					if (tunneledFile != null) {
						if (isMultiple) {
							inputList.add(tunneledFile);
						} else {
							return new DefaultStreamedContent(tunneledFile.requestStream(),
									tunneledFile.getMimeType(), tunneledFile.getName());
						}
					} else {
						return null;
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			String cType =  new String();
			InputStream zippedStream = downloadTool.multipleFileDownloadPost(inputList,cType);

			if (zippedStream != null) {
				
				return new DefaultStreamedContent(zippedStream, cType, retFileName);
			}

		} else {
			messages.add(ViewError.FILE_VERSION_ID_NULL.getCode() + ": " + ViewError.FILE_VERSION_ID_NULL.getMsg(), FacesMessage.SEVERITY_ERROR);
		}

		return null;

	}*/

	public List<FileListDTO> getSelectedFileList() {
		return selectedFileList;
	}

	public void setSelectedFileList(List<FileListDTO> selectedFileList) {
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

	private void montaErroFacesMsg(Hashtable<String, String> msgList) {
		//String msg = "Relatório: <br>";

		for (String key : msgList.keySet()) {
			messages.add(key + " - " + msgList.get(key), FacesMessage.SEVERITY_ERROR);

			/*
			 * msg = msg + key + " - " + msgList.get(key); msg = msg + " <br>";
			 */
		}

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

	public FileListDTO getFileToBeVersioned() {
		if (selectedFileList != null && !selectedFileList.isEmpty())
			fileToBeVersioned = selectedFileList.get(0);

		return fileToBeVersioned;
	}

	public void setFileToBeVersioned(FileListDTO fileToBeVersioned) {
		this.fileToBeVersioned = fileToBeVersioned;
	}

	public VersionsDTO getSelectedVersion() {
		return selectedVersion;
	}

	public void setSelectedVersion(VersionsDTO selectedVersion) {
		this.selectedVersion = selectedVersion;
	}

	public boolean isRenderDownloadVs() {
		return renderDownloadVs;
	}

	public void setRenderDownloadVs(boolean renderDownloadVs) {
		this.renderDownloadVs = renderDownloadVs;
	}

	public Integer getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(Integer tabIndex) {
		this.tabIndex = tabIndex;
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

	public Integer getGroupToIdVs() {
		return groupToIdVs;
	}

	public void setGroupToIdVs(Integer groupToIdVs) {
		this.groupToIdVs = groupToIdVs;
	}

	public String getMoveConditionVs() {
		return moveConditionVs;
	}

	public void setMoveConditionVs(String moveConditionVs) {
		this.moveConditionVs = moveConditionVs;
	}

}
