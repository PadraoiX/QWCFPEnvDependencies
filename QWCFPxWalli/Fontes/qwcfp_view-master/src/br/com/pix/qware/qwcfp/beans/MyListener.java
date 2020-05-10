package br.com.pix.qware.qwcfp.beans;

import java.security.MessageDigest;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.pix.qware.qwcfp.util.QwcfpInitials;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;

public class MyListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2513834184125642283L;

	private static List<String>	managerViews		= null;
	

	/**
	 * Mtodo que ser chamado pelo containers cada vez que o usurio acessar uma pgina
	 */
	@Override
	public void afterPhase(PhaseEvent event) {
		QwcfpInitials initials = QwcfpInitials.getInstance();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		HttpSession session = request.getSession();

		String telaLogin = "";
		String tipo = "";
		
		String frame = request.getParameter("iframe");
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		
		tipo = "index.xhtml";
		telaLogin = "loginPage";

		FacesContext facesContext = event.getFacesContext();
		NavigationHandler nh = facesContext.getApplication().getNavigationHandler();

		if (facesContext.getViewRoot() != null) {
			String currentPage = facesContext.getViewRoot().getViewId();
			boolean isLoginPage = (currentPage.lastIndexOf(tipo) > -1);
			boolean isloginInstall = (currentPage.lastIndexOf("loginInstall") > -1);
			boolean discoVirtual = (currentPage.lastIndexOf("loginInstall") > -1);
			boolean isInvited = (currentPage.lastIndexOf("invited") > -1);
			boolean isOpenDownload = (currentPage.lastIndexOf("openDownload") > -1);

			//			Integer userId = (Integer)  Util.getPropertySessao("ID_MEMBER");

			if (initials.isLoaded() && !isloginInstall && !isInvited && !isOpenDownload) {
				Boolean isGestor = false;
				MemberDTO member = null;
				if (!isLoginPage) {

					LoginBean logBean = event
							.getFacesContext()
							.getApplication()
							.evaluateExpressionGet(event.getFacesContext(),
									"#{loginBean}",
									LoginBean.class);

					
					ConnectedUser connected = null;

					if (logBean.getLoggedUser() != null) {
						isGestor = logBean.isGi();
						connected = logBean.getLoggedUser();
						member = logBean.getMember();
					} else {
						isGestor = (Boolean) session.getAttribute("IsGi");
						member = (MemberDTO) session.getAttribute("MEMBER_SESSION");
						connected = (ConnectedUser) session.getAttribute("loggedUser");
					}
					
					if ((frame != null && Boolean.parseBoolean(frame)) || logBean.isIFrame()) {
						
						boolean shouldLog = false;
						try {
							String beanHash = logBean.getUsuario() + logBean.getPass();
							String parHash = user + pass;
							
							if (connected == null) {
								shouldLog = true;
							}else if (!parHash.equalsIgnoreCase(beanHash) && !parHash.equals("nullnull")) {
								shouldLog = true;
							}
						} catch (Exception e) {
							shouldLog = true;
						}
						
						if (shouldLog) {
							logBean.setUsuario(user);
							logBean.setPass(pass);
							logBean.conectar();
						}
						LoginDTO dto = logBean.getLoggedUser().getLoginDto();
						if (dto != null && dto.getErrorCode() != ViewError.OK.getCode()) {
							//System.out.println("redirecionar para pagina de erro.");
							nh.handleNavigation(facesContext, null, telaLogin);
						}else if(dto == null){
							//System.out.println("redirecionar para pagina de erro.");
							nh.handleNavigation(facesContext, null, telaLogin);
						}else {
							
							if (!logBean.isIFrame()) {
								logBean.setIFrame(true);
							}
							
							if (!discoVirtual) {
								String alias = request.getParameter("alias");
								Util.setPropertySessao("GROUP_ALIAS", alias);
								nh.handleNavigation(facesContext, null, "discoVirtual");
							}
						}
					}else {
						
						if (connected == null || member == null || logBean.isIFrame()) {
							nh.handleNavigation(facesContext, null, telaLogin);
						} else if (currentPage.contains("/gestor") ) {
							if (!isGestor) {
								nh.handleNavigation(facesContext, null, "restrictAcess");
							}
							
							if(logBean.getLoggedUser() == null){
								logBean.setLoggedUser(connected);
								logBean.setGi(isGestor);
								logBean.setMember(member);
							}
						}else{
							if(logBean.getLoggedUser() == null){
								logBean.setLoggedUser(connected);
								logBean.setGi(isGestor);
								logBean.setMember(member);
							}
						}
					}
				}
			} else {
				boolean isInstallPage = (currentPage.lastIndexOf("Install") > -1);
				if (!isInstallPage && !isInvited && !isOpenDownload)
					nh.handleNavigation(facesContext, null, "loginInstall.xhtml");
				else {
					if (!isloginInstall && !isInvited && !isOpenDownload) {
						ConnectInstallBean connInstall = event
								.getFacesContext()
								.getApplication()
								.evaluateExpressionGet(event.getFacesContext(),
										"#{connectInstallBean}",
										ConnectInstallBean.class);

						if (!connInstall.isAuthorized()) {
							nh.handleNavigation(facesContext, null, "loginInstall.xhtml");
						}
					}
				}
			}
		} else {
			nh.handleNavigation(facesContext, null, telaLogin);
		}
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public void getManagerViews() {

	}

}
