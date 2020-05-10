package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.qwcfp.service.BRSService;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.service.TicketService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qware.qwcfp.util.download.HttpDownloadTool;
import br.com.pix.qware.qwcfp.util.download.TunneledFile;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.ShareDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.VersionDetailDTO;
import br.com.ws.BRSServico.Brslogado;

@ManagedBean(name = "brsBean")
@ViewScoped
public class BRSBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	@Inject
	private BRSService				brsService;

	@Inject
	private ConfigService			configService;

	@Inject
	private FacesMessages			messages;

	@Inject
	private TicketService			ticketService;

	@Inject
	private HttpDownloadTool		downloadTool;

	@Inject
	private FileVersionListService	fvlservice;

	private List<Integer>			idList;

	private List<VersionDetailDTO>	versionList;

	private String					filterValue;

	private int						nrDocPesquisa;

	private int						qtdPesquisa;

	private String					resultSearch;

	private List<VersionDetailDTO>	selectedVersions;

	//campos do formulário 
	private String					saveAs;
	private String					fileName;
	private String					statusFile;
	private String					ticket;
	private String					httpUrlTransf;

	@PostConstruct
	public void init() {
		super.init();
		idList = new ArrayList<Integer>();
		versionList = new ArrayList<VersionDetailDTO>();
		
		filterValue = (String) Util.getPropertySession("BRS_FILTER");
		setFilterValue(Util.removerAcentos(filterValue));
		searchPlus();
		if (idList != null) {
			Integer[] idsArray = new Integer[idList.size()];
			idsArray = idList.toArray(idsArray);
			VersionDetailDTO[] temp = fvlservice.executeListFileById(idsArray, 10000, 0);
			
			if(temp != null ){
				if (temp[0].getErrorCode() == 0) {
					for (VersionDetailDTO versionDetailDTO : temp) {
						versionList.add(versionDetailDTO);	
					}
						
				}
			}
		}
	}

	public void searchPlus() {

		ConfigDTO ip = configService.carregar("PROP_QWISSERVER", "SYSTEM_PROP");
		ConfigDTO porta = configService.carregar("PROP_QWISPORT", "SYSTEM_PROP");
		filterValue = filterValue == null ? "" : filterValue;
		filterValue = "@DOCN e " + filterValue;
		nrDocPesquisa = 0;
		qtdPesquisa = 1000;
		//TODO melhorar validação

		if (porta != null || ip != null) {

			Brslogado con = brsService.openConnection(porta.getValue(), ip.getValue());

			if (con.getNLogado() == 1) {
				Brslogado base = brsService.openBase(con.getNmSessao());
				if (base.getNLogado() == 1) {
					Brslogado result = brsService.searchPlus(con.getNmSessao(),
							filterValue,
							nrDocPesquisa,
							qtdPesquisa);

					if (result.getConteudoParagrafosSolicitados() != null) {
						String[] conteudoPararafos = result.getConteudoParagrafosSolicitados()
								.getConteudoParagrafos();

						if (conteudoPararafos != null) {
							
							for (String string : conteudoPararafos) {
								if (!(string.indexOf(":") == -1)) {
									try {
										Integer versionId = Integer.valueOf(string.split(":")[1]);
										
										idList.add(versionId);
									} catch (Exception e) {
									}
								}
							}
						}
					}

				}
				
				brsService.closeConnection(con.getNmSessao());
			}
		}
	}

	/*	public void searchPlus() {
		ConfigDTO ip = configService.carregar("PROP_QWISSERVER", "SYSTEM_PROP");
		ConfigDTO porta = configService.carregar("PROP_QWISPORT", "SYSTEM_PROP");
		searchValue = searchValue == null ? "": searchValue; 
		searchValue = "@DOCN e "  + "a";
		nrDocPesquisa = 0;
		qtdPesquisa = 1000;
			//TODO melhorar validação
			Brslogado con = brsService.openConnection(porta.getValue(),ip.getValue());

			if (con.getNLogado() == 1) {
				Brslogado base = brsService.openBase(con.getNmSessao());
				if (base.getNLogado() == 1) {
					Brslogado result = brsService.searchPlus(con.getNmSessao(),
							searchValue,
							nrDocPesquisa,
							qtdPesquisa);

					String resultSearch = result.getListaPesquisa();
				}
			}
		}*/

	public void getInputStream() throws Exception {
		getInputStream(null);
	}

	public void getInputStream(VersionDetailDTO buttonDto) throws Exception {

		setStatusFile(null);

		List<TunneledFile> inputList = new CopyOnWriteArrayList<TunneledFile>();

		String fileName = "";
		String saveAs = "";
		boolean isMultiple = false;

		if (buttonDto != null) {
			selectedVersions = new ArrayList<VersionDetailDTO>();
			selectedVersions.add(buttonDto);
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

		if (selectedVersions != null) {
			for (VersionDetailDTO selectedFiles : selectedVersions) {

				if (selectedVersions.size() > 1)
					isMultiple = true;
				else
					isMultiple = false;

				SimpleDTO simpleDTO = new SimpleDTO();

				TunneledFile tunneledFile = downloadTool.downloadFileVersioned(selectedFiles
						.getFileName(),
						selectedFiles.getId(),
						selectedFiles.getFileId(),
						simpleDTO);

				if (tunneledFile != null) {
					if (isMultiple) {
						inputList.add(tunneledFile);
					} else {
						saveAs = tunneledFile.getName();
						fileName = tunneledFile.getTrueFileName();
					}
				} else {
					setStatusFile(simpleDTO.getErrorMsg());
				}
			}

			for (TunneledFile tunneledFile : inputList) {
				saveAs += tunneledFile.getName() + ";";
				fileName += tunneledFile.getTrueFileName() + ";";
			}

			if (isMultiple) {
				setSaveAs(saveAs.substring(0, saveAs.length() - 1));
				setFileName(fileName.substring(0, fileName.length() - 1));
			} else {
				setSaveAs(saveAs);
				setFileName(fileName);

			}
			
			String usucario = (String) Util.getPropertySession("USER_LDAP");
			String senha = (String) Util.getPropertySession("PASS_LDAP");
		 

			SimpleDTO simple = ticketService.getTicketDownload("Get",
					getFileName(),
					getSaveAs(),
					"Rep",
					"Binary",
					usucario,
					senha);

			if (simple != null) {
				if (simple.getErrorCode() == 0)
					setTicket(simple.getErrorMsg());
				else {
					setStatusFile(simple.getErrorMsg());
				}
			} else {
				messages.add(ViewError.GET_TICKET_ERROR.getCode() + ": "
						+ ViewError.GET_TICKET_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
				return;
			}

		} else {
			messages.add(ViewError.FILE_NOT_CHOSSE.getMsg(), FacesMessage.SEVERITY_ERROR);
		}

	}

	public int getNrDocPesquisa() {
		return nrDocPesquisa;
	}

	public void setNrDocPesquisa(int nrDocPesquisa) {
		this.nrDocPesquisa = nrDocPesquisa;
	}

	public int getQtdPesquisa() {
		return qtdPesquisa;
	}

	public void setQtdPesquisa(int qtdPesquisa) {
		this.qtdPesquisa = qtdPesquisa;
	}

	public String getResultSearch() {
		return resultSearch;
	}

	public void setResultSearch(String resultSearch) {
		this.resultSearch = resultSearch;
	}

	public List<VersionDetailDTO> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<VersionDetailDTO> versionList) {
		this.versionList = versionList;
	}

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

	public String getSaveAs() {
		return saveAs;
	}

	public void setSaveAs(String saveAs) {
		this.saveAs = saveAs;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<VersionDetailDTO> getSelectedVersions() {
		return selectedVersions;
	}

	public void setSelectedVersions(List<VersionDetailDTO> selectedVersions) {
		this.selectedVersions = selectedVersions;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
}
