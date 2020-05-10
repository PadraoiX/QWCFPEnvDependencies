package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.lazy.ListArquivosExpiredLazy;
import br.com.pix.qware.qwcfp.lazy.ListArquivosLazy;
import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.service.PurgeService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

@ManagedBean(name = "fileBean")
@ViewScoped
public class QwcssFileBean extends AbstractBean {

	private static final long				serialVersionUID	= 1L;

	private FileListDTO						fileManaged;
	
	private ListArquivosExpiredLazy				fileListLazy;

	@Inject
	private FileManagedService				fileManagedService;

	@Inject
	private FileVersionListService			fileVersionService;
	
	@Inject
	private DomainService					domainService;

	@Inject
	private FacesMessages					messages;

	@Inject
	private PurgeService					purgeService;

	private List<FileListDTOEx>				fileList;
	
	private List<FileListDTOEx> 				selectedFiles;

	private FileListDTOEx[]					filesPurgue;

	private FileListDTOEx[]					filesPurgueGrupo;

	private static HashMap<String, String>	filtro;

	private List<String>					listFiltro;

	private String							operation;

	private static String					purged				= "PURGED";

	private static String					purgedAllowed		= "PURGEDALLOWED";

	private Integer							qtdeDias;

	public String nome(String nome) {
		if (nome.length() > 20)
			nome = nome.substring(0, 17) + "...";
		return nome;
	}
	
	public String status(Integer id) {
		VersionsDTO[] versions = fileVersionService.listar(id);
		String status = "";
		if (versions != null) {
			if (versions[0].getErrorCode() != 0)
				return null;
			else
				status = domainService.returnDomain(versions[0].getFileStatusDomIdFk())
						.getDescription();
		}

		return status;
	}


	public void autorizarExpurgo(boolean isGi) {
		Integer sucesso = 0;
		String erro = "";
		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			
			for (int i = 0; i < selectedFiles.size(); i++) {
				SimpleDTO retorno = purgeService.expurgarArquivo(selectedFiles.get(i).getFileId());

				if (retorno.getErrorCode() == 0)
					sucesso++;
				else
					erro = erro + retorno.getErrorCode() + " " + retorno.getErrorMsg()	+ selectedFiles.get(i).getFileName() + "\n";
			}
			
		} else
			messages.add(ViewError.FILE_VERSION_ID_NULL.getCode() + ": "
					+ ViewError.FILE_VERSION_ID_NULL.getMsg(), FacesMessage.SEVERITY_ERROR);
		
		if (sucesso > 0)
			messages.add(sucesso + " " + ViewError.PURGE_FILE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);

		if (!erro.isEmpty())
			messages.add(erro, FacesMessage.SEVERITY_ERROR);

		if (isGi)
			updateFilesPurgue();
		else
			updateFilesPurgueGrupo();
	}

	public void adiarDataValidadeGI(  boolean isGi ) {
		SimpleDTO retorno = null;
		Integer sucesso = 0;
		String erro = "";
		if (selectedFiles != null && qtdeDias != null && qtdeDias > 0) {
			
			for (FileListDTOEx fileListDTO : selectedFiles) {
				retorno = purgeService.adiarDataValidadeArquivo(fileListDTO.getFileId(), qtdeDias);
				if (retorno.getErrorCode() == 0)
					sucesso++;
				else
					erro = erro + retorno.getErrorCode() + " " + retorno.getErrorMsg()	+ fileListDTO.getFileName() + "\n";
			}
			
		} else
			messages.add(ViewError.FILE_VERSION_ID_NULL.getCode() + ": "+ ViewError.FILE_VERSION_ID_NULL.getMsg(), FacesMessage.SEVERITY_ERROR);
		
		if (sucesso > 0 )
			messages.add(ViewError.PURGE_FILE_DATE_SUCESS.getMsg() + sucesso + " arquivo(s)", FacesMessage.SEVERITY_INFO);

		if (!erro.isEmpty())
			messages.add(erro, FacesMessage.SEVERITY_ERROR);

		if (isGi)
			updateFilesPurgue();
		else
			updateFilesPurgueGrupo();

	}

	public FileListDTO getFileManaged() {
		return fileManaged;
	}

	public void setFileManaged(FileListDTO fileManaged) {
		this.fileManaged = fileManaged;
	}

	public void updateList() {
		this.getFileList();
	}

	public List<FileListDTOEx> getFileList() {
		String aliasGrupo = (String) Util.getPropertySession("GROUP_ALIAS");
		FileListDTOEx[] array = fileManagedService.listar(aliasGrupo, null, 0, 1000, false,null,null);
		if (array != null)
			fileList = Arrays.asList(array);
		return fileList;
	}

	public String sizeInByte(Integer id) {
		VersionsDTO[] versions = fileVersionService.listar(id);
		String sizeInByte = "";
		if (versions != null) {
			if (versions[0].getErrorCode() != 0)
				return null;
			else
				sizeInByte = Util.convertByte(versions[0].getSizeInBytes());
		}

		return sizeInByte;
	}


	public void setFileList(List<FileListDTOEx> fileList) {
		this.fileList = fileList;
	}

	public FileListDTOEx[] getFilesPurgue() {
		return filesPurgue;
	}

	public void updateFilesPurgue() {

		if (operation != null) {
			if (operation.trim().equals(filtro.get(purged)))
				setFileListLazy(new ListArquivosExpiredLazy( fileManagedService,false,true));// expurgado
			else if (operation.trim().equals(filtro.get(purgedAllowed)))
				setFileListLazy(new ListArquivosExpiredLazy( fileManagedService,true,true));// expurgado autorizado
		}
	}

	public void setFilesPurgue(FileListDTOEx[] filesPurgue) {
		this.filesPurgue = filesPurgue;
	}

	public FileListDTOEx[] getFilesPurgueGrupo() {
		return filesPurgueGrupo;
	}

	public void updateFilesPurgueGrupo() {
		if (operation != null) {
			if (operation.trim().equals(filtro.get(purged)))
				setFileListLazy(new ListArquivosExpiredLazy( fileManagedService,false,false));// expurgado
			else if (operation.trim().equals(filtro.get(purgedAllowed)))
				setFileListLazy(new ListArquivosExpiredLazy( fileManagedService,true,false));// expurgado autorizado
		}
	}

	public void setFilesPurgueGrupo(FileListDTOEx[] filesPurgueGrupo) {
		this.filesPurgueGrupo = filesPurgueGrupo;
	}

	public static HashMap<String, String> getFiltro() {
		filtro = new LinkedHashMap<String, String>();
		filtro.put(purged, "Expurgado");
		filtro.put(purgedAllowed, "Expurgados Autorizado");
		return filtro;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public List<String> getListFiltro() {
		listFiltro = new ArrayList<String>();
		getFiltro();
		listFiltro.add(filtro.get(purged));
		listFiltro.add(filtro.get(purgedAllowed));

		return listFiltro;
	}

	public void setListFiltro(List<String> listFiltro) {
		this.listFiltro = listFiltro;
	}

	public Integer getQtdeDias() {
		return qtdeDias;
	}

	public void setQtdeDias(Integer qtdeDias) {
		this.qtdeDias = qtdeDias;
	}

	public List<FileListDTOEx> getSelectedFiles() {
		return selectedFiles;
	}

	public void setSelectedFiles(List<FileListDTOEx> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}

	public ListArquivosExpiredLazy getFileListLazy() {
		return fileListLazy;
	}

	public void setFileListLazy(ListArquivosExpiredLazy fileListLazy) {
		this.fileListLazy = fileListLazy;
	}

}