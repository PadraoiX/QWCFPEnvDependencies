package br.com.pix.qware.qwcfp.service;

import java.util.ArrayList;

import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qwvdt.client.ClientWS;
import br.com.pix.qwvdt.client.dto.Layout;
import br.com.pix.qwvdt.client.dto.LayoutFull;
import br.com.qwcss.ws.dto.ConfigDTO;

public class QwvdtService extends Service {

	private String url, user, pass;

	@Inject
	private ConfigService configService;

	@Inject
	private LoginBean loginBean;

	public QwvdtService() {
		// TODO colocar aqui a inteligencia para popular as propriedades
	}

	public ArrayList<Layout> listLayoutsByUser() {

		ConfigDTO configDto = configService.carregar("URL", "QWVDT");

		if (configDto != null && loginBean.getLoggedUser() != null) {
			if (configDto.getErrorCode() == 0 && loginBean.getLoggedUser().getLoginUser() != null
					&& loginBean.getLoggedUser().getSenhaUser() != null) {
				url = configDto.getValue();// "http://172.16.253.108:8080/qwvdt-webservice/rest";
				//url = "http://172.16.253.128:8080/qwvdt-webservice/rest";
				user = loginBean.getLoggedUser().getLoginUser();// "98765432100";
				//user = "98765432100";
				pass = loginBean.getLoggedUser().getSenhaUser();// "pix2000";
				//pass = "pix2000";

				ClientWS cl = new ClientWS(url, user, pass);

				try {
					if (cl.login()) {
						ArrayList<Layout> retorno = cl.getLayoutsUser(getConnectedUser().getLoginDto().getQwareUser());
						cl.logoff();
						return retorno;
					} else {
						return null;
					}
					// System.out.println("Autenticado com sucesso!" +
					// cl.getToken());
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

			}
		}

		return null;

	}
	
	public ArrayList<Layout> listLayouts() {

		ConfigDTO configDto = configService.carregar("URL", "QWVDT");

		if (configDto != null && loginBean.getLoggedUser() != null) {
			if (configDto.getErrorCode() == 0 && loginBean.getLoggedUser().getLoginUser() != null
					&& loginBean.getLoggedUser().getSenhaUser() != null) {
				url = configDto.getValue();// "http://172.16.253.108:8080/qwvdt-webservice/rest";
				//url = "http://172.16.253.128:8080/qwvdt-webservice/rest";
				user = loginBean.getLoggedUser().getLoginDto().getQwareUser();// "98765432100";
				//user = "98765432100";
				pass = loginBean.getLoggedUser().getSenhaUser();// "pix2000";
				//pass = "pix2000";

				ClientWS cl = new ClientWS(url, user, pass);

				try {
					if (cl.login()) {
						ArrayList<Layout> retorno = cl.getLayouts();
						cl.logoff();
						return retorno;
					} else {
						return null;
					}
					// System.out.println("Autenticado com sucesso!" +
					// cl.getToken());
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

			}
		}

		return null;

	}

	public String[] login() {
		ConfigDTO configDto = configService.carregar("URL", "QWVDT");

		if (configDto != null && loginBean.getLoggedUser() != null) {
			if (configDto.getErrorCode() == 0 && loginBean.getLoggedUser().getLoginDto().getQwareUser() != null
					&& loginBean.getLoggedUser().getSenhaUser() != null) {
				url = configDto.getValue();// "http://172.16.253.108:8080/qwvdt-webservice/rest";
				//url = "http://172.16.253.128:8080/qwvdt-webservice/rest";
				user = loginBean.getLoggedUser().getLoginDto().getQwareUser();// "98765432100";
				//user = "98765432100";
				pass = loginBean.getLoggedUser().getSenhaUser();// "pix2000";
				//pass = "pix2000";
				
				ClientWS cl = new ClientWS(url, user, pass);

				try {
					if (cl.login()) {
						return new String[] { loginBean.getLoggedUser().getLoginDto().getQwareUser(), cl.getToken() };
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	public LayoutFull getLayoutFull(Integer id) {

		ConfigDTO configDto = configService.carregar("URL", "QWVDT");

		if (configDto != null && loginBean.getLoggedUser() != null) {
			if (configDto.getErrorCode() == 0 && loginBean.getLoggedUser().getLoginUser() != null
					&& loginBean.getLoggedUser().getSenhaUser() != null) {
				url = configDto.getValue();// "http://172.16.253.108:8080/qwvdt-webservice/rest";
				//url = "http://172.16.253.128:8080/qwvdt-webservice/rest";
				user = loginBean.getLoggedUser().getLoginUser();// "98765432100";
				//user = "98765432100";
				pass = loginBean.getLoggedUser().getSenhaUser();// "pix2000";
				//pass = "pix2000";

				ClientWS cl = new ClientWS(url, user, pass);

				try {
					if (cl.login()) {
						LayoutFull l = cl.getFull(id);
						cl.logoff();
						return l;
					} else {
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}

		return null;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}
