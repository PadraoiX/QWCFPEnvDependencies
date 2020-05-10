package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.pix.qware.qwcfp.error.MsgTitleEnum;
import br.com.pix.qware.qwcfp.lazy.ListArquivosBean;
import br.com.pix.qware.qwcfp.service.DownloadLinkService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.wrappers.MyGroupsWrapper;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.DownloadLinkDTO;
import br.com.qwcss.ws.dto.DownloadLinkParsDTO;
import br.com.qwcss.ws.dto.FileAndFileVersionWrapper;
import br.com.qwcss.ws.dto.FileListDTO;

//@Named("rulesBean")
@ManagedBean(name = "openDownload")
@ViewScoped
public class OpenDownloadBean extends AbstractBean {

	private static final String MAIL_SEPARATOR = ";";

	private static final long serialVersionUID = 8281391451977305172L;
	
	@Inject
	private FacesMessages messages;

	@Inject
	private DownloadLinkService downloadService;
	
	private DownloadLinkParsDTO info;

	private boolean tokenOk;

	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		        .getRequest();

		String token = request.getParameter("token");
		if (token == null) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			try {
				ec.redirect(ec.getRequestContextPath() + "/index.faces");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else if(token.isEmpty()) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			try {
				ec.redirect(ec.getRequestContextPath() + "/index.faces");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else {
			DownloadLinkParsDTO value = downloadService.start(token);
			if (value.getErrorCode() != ViewError.OK.getCode()) {
				messages.error(value.getErrorMsg());
				setTokenOk(false);
				info= new DownloadLinkParsDTO();
			}else {
				info = value;
				setTokenOk(true);
			}
		}
	}
	
	public void doDownload() {
		// TODO Auto-generated method stub
		

	}

	public DownloadLinkParsDTO getInfo() {
		return info;
	}

	public void setInfo(DownloadLinkParsDTO info) {
		this.info = info;
	}

	public boolean isTokenOk() {
		return tokenOk;
	}

	public void setTokenOk(boolean tokenOk) {
		this.tokenOk = tokenOk;
	}

}
