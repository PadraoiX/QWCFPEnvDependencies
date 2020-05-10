package br.com.pix.qware.qwcfp.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.extensions.event.ClipboardErrorEvent;
import org.primefaces.extensions.event.ClipboardSuccessEvent;

import br.com.pix.qware.qwcfp.lazy.ListArquivosBean;
import br.com.pix.qware.qwcfp.service.DownloadLinkService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DownloadLinkDTO;
import br.com.qwcss.ws.dto.FileAndFileVersionWrapper;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;

//@Named("rulesBean")
@ManagedBean(name = "downloadLink")
@RequestScoped
public class DownloadLinkBean extends AbstractBean {

	private static final String MAIL_SEPARATOR = ";";

	private static final long serialVersionUID = 8281391451977305172L;

	@Inject
	private DownloadLinkService downloadService;

	@Inject
	private FacesMessages messages;

	@Inject
	private ListArquivosBean fileBean;

	@ManagedProperty(value = "#{listArquivosBean.selectedFileList}")
	private List<FileListDTOEx> fileList;

	private String link;

	private String mailString;

	@PostConstruct
	public void init() {
		generateLink(false);
	}

	public void generateLink(boolean isMail) {

		if (link == null) {

			MyGroupsWrapper group = fileBean.getMyGroupWrapped();

			if (group == null) {
				messages.error(ViewError.DOWN_LNK_GROUP_NULL.getMsg());
			} else if (getFileList() == null) {
				messages.error(ViewError.DOWN_LNK_FILE_LIST_NULL.getMsg());
			} else if (getFileList().isEmpty()) {
				messages.error(ViewError.DOWN_LNK_FILE_LIST_NULL.getMsg());
			} else {
				FileListDTOEx[] dtos = new FileListDTOEx[getFileList().size()];

				for (int i = 0; i < dtos.length; i++) {
					dtos[i] = getFileList().get(i);
				}

				DownloadLinkDTO linkDto = null;
				if (isMail) {
					if (mailString != null && !mailString.isEmpty()) {
						String[] mailList = mailString.split(MAIL_SEPARATOR);
						linkDto = downloadService.generate("", group.getMyGroupsEx().getGroupId(), null, mailList,
								dtos);
					} else {
						messages.error(ViewError.DOWN_LNK_MAIL_LIST_REQUIRED.getMsg());
					}
				} else {
					linkDto = downloadService.generate("", group.getMyGroupsEx().getGroupId(), null, null, dtos);
				}
				if (linkDto.getErrorCode() != ViewError.OK.getCode()) {
					messages.error(linkDto.getErrorMsg());
				} else {
					link = linkDto.getLink();
				}
			}
		}
	}
	   public void successListener(final ClipboardSuccessEvent successEvent) {  
		      final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",  
		               "Component id: " + successEvent.getComponent().getId() + " Action: " + successEvent.getAction()  
		                        + " Text: " + successEvent.getText());  
		      FacesContext.getCurrentInstance().addMessage(null, msg);  
		    
		   }  
		  
		   public void errorListener(final ClipboardErrorEvent errorEvent) {  
		      final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  
		               "Component id: " + errorEvent.getComponent().getId() + " Action: " + errorEvent.getAction());  
		      FacesContext.getCurrentInstance().addMessage(null, msg);  
		   }  
	

	public void updateLink() {
	}

	public final String getLink() {
		return link;
	}

	public final void setLink(final String link) {
		this.link = link;
	}

	public String getMailString() {
		return mailString;
	}

	public void setMailString(String mailString) {
		this.mailString = mailString;
	}

	public List<FileListDTOEx> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileListDTOEx> fileList) {
		this.fileList = fileList;
	}

}
