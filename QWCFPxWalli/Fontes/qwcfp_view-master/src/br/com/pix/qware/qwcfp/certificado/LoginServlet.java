package br.com.pix.qware.qwcfp.certificado;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.PrivilegiosService;
import br.com.pix.qware.qwcfp.util.NormalizeFileName;
import br.com.pix.qware.qwcfp.util.PropKeys;
import br.com.pix.qware.qwcfp.util.QwcfpInitials;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.connection.ConnectUser;
import br.com.pix.qwcfp.ws.client.connection.ConnectedUser;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.LoginDTO;
import br.com.qwcss.ws.dto.MemberDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.gov.frameworkdemoiselle.certificate.CertificateManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		/*Map<String, String> lista = getHeadersInfo(request);

		for (String string : lista.keySet()) {
			System.out.println("Chave: " + string + " Valor: " + lista.get(string));
		}*/

		try {
			String cpf = getCredential(request);
			if (cpf != null) {
				Cache<String, Object> cache;
				try {
					cache = getCache();

					if (cache == null) {
						session.setAttribute("ErrorCode", ViewError.CACHE_CONTAINER_ERROR.getCode());
						session.setAttribute("ErrorMsg", ViewError.CACHE_CONTAINER_ERROR.getMsg());
						response.sendRedirect("certInvalido.jsp");
						return;
					}

					String loginKey = generateRandomSessionId(session.getId(), String.valueOf(System.currentTimeMillis()));

					cache.put(loginKey + "_cpf", cpf, 20, TimeUnit.SECONDS);

					ConnectedUser loggedUser = ConnectUser.connect(loginKey);

					Boolean isGi = new Boolean(false);

					boolean sucess = verifyLoggerUser(loggedUser );
					
					if(sucess){

							PrivilegiosService privService = new PrivilegiosService();
							SimpleDTO simpleDto = privService.memberIsGi(loggedUser);

							if (simpleDto != null) {
								isGi = simpleDto.getErrorCode().compareTo(0) == 0 ? new Boolean (true)  : new Boolean ( false );
							} else {
								isGi = false;
							}

					}

					if (!sucess && loggedUser != null) {
						session.setAttribute("ErrorCode", loggedUser.getLoginDto().getErrorCode());
						session.setAttribute("ErrorMsg", loggedUser.getLoginDto().getErrorMsg());
						response.sendRedirect("certInvalido.jsp");
						return;
					} else if (!sucess && loggedUser == null) {
						session.setAttribute("ErrorCode", ViewError.PASS_USR_WRONG_ERR.getCode());
						session.setAttribute("ErrorMsg", ViewError.PASS_USR_WRONG_ERR.getMsg());
						response.sendRedirect("certInvalido.jsp");
						return;
					} else if (sucess && loggedUser != null) {
						session.setAttribute("ID_MEMBER", loggedUser.getLoginDto().getMemberId());
						session.setAttribute("loggedUser", loggedUser);
						session.setAttribute("IsGi", new Boolean(isGi));

						MemberService memberService = new MemberService();
						MemberDTO member = memberService.listar(cpf);

						if (member != null) {
							session.setAttribute("MEMBER_SESSION", member);
							br.com.pix.qwcfp.ws.client.dominios.Dominio.returnDomain(loggedUser,"STATUS_MEMBERS");
						} else {
							session.setAttribute("ErrorCode",ViewError.PASS_USR_WRONG_ERR.getCode());
							session.setAttribute("ErrorMsg", ViewError.PASS_USR_WRONG_ERR.getMsg());
							response.sendRedirect("certInvalido.jsp");
						}

						response.sendRedirect("main.faces");
						return;
					}else{
						session.setAttribute("ErrorCode", ViewError.PASS_USR_WRONG_ERR.getCode());
						session.setAttribute("ErrorMsg", ViewError.PASS_USR_WRONG_ERR.getMsg());
						response.sendRedirect("certInvalido.jsp");
						return;
					}

				} catch (NamingException e) {
					e.printStackTrace();
					session.setAttribute("ErrorCode", ViewError.CACHE_CONTAINER_ERROR.getCode());
					session.setAttribute("ErrorMsg", ViewError.CACHE_CONTAINER_ERROR.getMsg());
					response.sendRedirect("certInvalido.jsp");
					return;
				}
			} else {
				session.setAttribute("ErrorCode", ViewError.CREDENTIAL_INVALID.getCode());
				session.setAttribute("ErrorMsg", ViewError.CREDENTIAL_INVALID.getMsg());
				response.sendRedirect("certInvalido.jsp");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("ErrorCode", ViewError.CREDENTIAL_INVALID.getCode());
			session.setAttribute("ErrorMsg", ViewError.CREDENTIAL_INVALID.getMsg() + " " + e.getMessage());
			response.sendRedirect("certInvalido.jsp");
			return;
		}

	}

	private boolean verifyLoggerUser(ConnectedUser loggedUser) {
		if (loggedUser != null) {
			LoginDTO loginDto = loggedUser.getLoginDto();
			if (loginDto != null) {
				if (loginDto.getErrorCode() != ViewError.OK.getCode()) {
					if (loginDto.getErrorCode() != 90043) {
						return false;
					} else {
						loginDto.setErrorMsg(ViewError.MEMBER_AUTH.getMsg());
						loggedUser.setLoginDto(loginDto);
						return false;
					}
				} 
			}
		}
		return true;
	}

	/*@SuppressWarnings("rawtypes")
	private Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		return map;
	}

	private String getUsername(HttpServletRequest request) {
		String cert = request.getHeader("ssl_client_s_dn_cn");
		String[] chainCert = cert.split("2.16.76.1.3.1=");
		if (chainCert != null) {
			String cadeiaInfo = chainCert[1];
			if (cadeiaInfo != null) {
				String res = cadeiaInfo.replaceAll("\\s+", "");//remove whitespaces 
				StringBuilder output = new StringBuilder();
				for (int i = 0; i < res.length(); i += 2) {//converter de hexa para ascii
					String str = res.substring(i, i + 2);
					output.append((char) Integer.parseInt(str, 16));
				}
				return output.substring(Util.getSplitCert(0), Util.getSplitCert(1)); //retorna o cpf
			}
		}
		return null;
	}*/

	private String getCredential(ServletRequest request) throws Exception {
		final String CLIENT_CERT = "javax.servlet.request.X509Certificate";
		//final String CIPHER_SUITE = "javax.servlet.request.cipher_suite"; String cipherSuite = (String) request.getAttribute(CIPHER_SUITE);

		X509Certificate[] certChain = (X509Certificate[]) request.getAttribute(CLIENT_CERT);
		if (certChain != null && certChain.length > 0) {
			CertificateManager cm = new CertificateManager(certChain[0], false);
			Cert cert = cm.load(Cert.class);
			String _cpf = cert.getCpf();
			if (_cpf == null || _cpf.length() < 1) {
				_cpf = cert.getNome().split(":")[1];
			}

			return _cpf;
		}

		return null;
	}

	private String generateRandomSessionId(String stringBase, String timesTamp) {
		Random gerador = new Random();
		NormalizeFileName normalize = new NormalizeFileName();
		String hashIdName = normalize.get(stringBase + gerador.nextLong() + timesTamp);

		return hashIdName;
	}

	private Cache<String, Object> getCache() throws NamingException {

		QwcfpInitials qwcfpInitials = QwcfpInitials.getInstance();

		String cacheName = (String) qwcfpInitials.getProperties().get(PropKeys.CACHE_NAME.getKey());

		if (cacheName != null && !cacheName.isEmpty()) {
			CacheContainer replicadeContainer = null;
			Cache<String, Object> cache = null;

			Context ct = new InitialContext();
			Object obj = ct.lookup("java:jboss/infinispan/container/" + cacheName);

			replicadeContainer = (CacheContainer) obj;
			if (replicadeContainer != null) {
				cache = replicadeContainer.getCache();
				return cache;
			}
		}

		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
