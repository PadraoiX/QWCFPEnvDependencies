package br.com.pix.qware.qwcfp.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.pix.qware.qwcfp.service.Service;

public class CookieWork extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5170911965611441254L;
	
	public void setCookie(String name, String value, int expiry) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					break;
				}
			}
		}
		if (cookie != null) {
			cookie.setValue(value);
		} else {
			cookie = new Cookie(name, value);
			cookie.setPath(request.getContextPath());
		}
		cookie.setMaxAge(expiry);
		HttpServletResponse  response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addCookie(cookie);
	}


	/**
	 * 
	 * @param name
	 * @param value
	 * @param expiry <br>
	 * 	 - para que o cookie expire quando o browser fechar passar -1<br>
	 *   - para que o cookie expire em 10 anos passar 60 * 60 * 24 * 365 * 10
	 */
	public void setCookie(String name, String value, int expiry, String path) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					break;
				}
			}
		}
		if (cookie != null) {
			cookie.setValue(value);
			cookie.setPath(path);
		} else {
			cookie = new Cookie(name, value);
			cookie.setPath(path);
		}
		cookie.setMaxAge(expiry);
		HttpServletResponse  response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addCookie(cookie);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Cookie getCookie(String name) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					return cookie;
				}
			}
		}
		return null;
	}

}
