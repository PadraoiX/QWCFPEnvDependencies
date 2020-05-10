package br.com.pix.qware.qwcfp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.MaskFormatter;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.pix.qware.qwcfp.error.MsgTitleEnum;
import br.com.pix.qwcfp.ws.client.erro.ViewError;

public class Util {
	
	public static HashMap<String, String> countryCode = readPropertie();
	
	public static SimpleDateFormat wsDateFormat = new SimpleDateFormat("ddMMyyyy");
	
	/**
	 * Subrescrita de método Método static para padrinizar as mensagens de retorno da aplicação
	 * 
	 * @param severityError
	 * @param ctx
	 * @param err
	 * @param errCod
	 * @return
	 */
	public static String DisplayErrMsg(Severity severityError, FacesContext ctx, ViewError err) {
		return DisplayErrMsg(severityError, ctx, err, "");
	}
	
	
	public static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	
	/**
	 * Traz SH1 de um texto
	 * 
	 * @param txt
	 * @return
	 */
	public static String getSHA1(String txt) {
		try {
			SHA1Digest md5 = new SHA1Digest();
			md5.update(txt.getBytes("UTF-8"), 0, txt.getBytes("UTF-8").length);
			byte[] digest = new byte[md5.getDigestSize()];
			md5.doFinal(digest, 0);
			return new String(Hex.encode(digest));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Integer getSplitCert(Integer position){
		
		Reader reader = null;
		BufferedReader leitor = null;
		
		InputStream stream  = Util.class.getClassLoader().getResourceAsStream("split-cert.txt");
		
		try {
			reader = new InputStreamReader(stream);
			leitor = new BufferedReader(reader);
			
			List<String> lista =  new  ArrayList<String>();

			while ((leitor.ready())) {
				String linha = leitor.readLine();
				if (!linha.trim().equals("")) {
					lista.add(linha);
				}
			}
			
			return Integer.parseInt(lista.get(position));
			
		} catch (Exception e) {
			try {
				if (reader != null)
					reader.close();

				if (leitor != null)
					leitor.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
			return null;
		}
		
	}

	public static HashMap<String, String> readPropertie() {

		Reader reader = null;
		BufferedReader leitor = null;
		
		InputStream stream  = Util.class.getClassLoader().getResourceAsStream("country-code.txt"); 
	
		try {
			
			reader = new InputStreamReader(stream);
			leitor = new BufferedReader(reader);

			HashMap<String, String> countryCode = new LinkedHashMap<String, String>();

			while ((leitor.ready())) {
				String linha = leitor.readLine();
				if (!linha.trim().equals("")) {
					String[] tokens = linha.split("=");
					if(tokens!= null & tokens.length > 1)
						countryCode.put(tokens[0], tokens[1]);
				}
			}
			
			return countryCode;

		} catch (Exception e) {
			try {
				if (reader != null)
					reader.close();

				if (leitor != null)
					leitor.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		HashMap<String, String> countryCode =  readPropertie();
		
		System.out.println(countryCode.get("53"));
	}
	/**
	 * Mtodo para formatar qualquer tipo de dado numrico
	 * 
	 * @param value
	 * @param pattern
	 *            <p>
	 *            Pattern para cpf: <b>###.###.###-##</b><br>
	 *            Pattern para phoneNumber: <b>(##) ####-####</b><br>
	 *            Pattern para cep: <b>####-###</b>
	 *            <p>
	 * @return
	 */
	public static String formatar(String value, String pattern) {
		MaskFormatter mask;
		try {
			mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String convertByte(BigDecimal bytes) {

		Long bte = bytes.setScale(0, BigDecimal.ROUND_UP).longValueExact();

		long kilobyte = 1024;
		long megabyte = kilobyte * 1024;
		long gigabyte = megabyte * 1024;
		long terabyte = gigabyte * 1024;

		if ((bte >= 0) && (bte < kilobyte)) {
			return bte + " B";

		} else if ((bte >= kilobyte) && (bte < megabyte)) {
			return (bte / kilobyte) + " KB";

		} else if ((bte >= megabyte) && (bte < gigabyte)) {
			return (bte / megabyte) + " MB";

		} else if ((bte >= gigabyte) && (bte < terabyte)) {
			return (bte / gigabyte) + " GB";

		} else if (bte >= terabyte) {
			return (bte / terabyte) + " TB";

		} else {
			return bte + " Bytes";
		}
	}
	
	public static BigDecimal convertByte(Integer bytes, String unity) {
		
		Long bte = bytes.longValue();
		
		long kilobyte = 1024;
		long megabyte = kilobyte * 1024;
		long gigabyte = megabyte * 1024;
		long terabyte = gigabyte * 1024;
		
		
		if (unity.equalsIgnoreCase("KB")) {
			return new BigDecimal(bte * kilobyte);
		}else if (unity.equalsIgnoreCase("MB")) {
			return new BigDecimal(bte * megabyte);
		}else if (unity.equalsIgnoreCase("GB")) {
			return new BigDecimal( bte * gigabyte);
		}else if (unity.equalsIgnoreCase("TB")) {
			return new BigDecimal(bte * terabyte);
		}else{
			return new BigDecimal(bte);
		}

	}
	
	

	/**
	 * Subrescrita de método Método static para padrinizar as mensagens de retorno da aplicação
	 * 
	 * @param severityError
	 * @param ctx
	 * @param err
	 * @param errCod
	 * @return
	 */
	public static String DisplayErrMsg(Severity severityError,
			FacesContext ctx,
			ViewError err,
			String errCod) {

		String msgTitle = null;
		if (severityError != null) {
			if (severityError.equals(FacesMessage.SEVERITY_ERROR)) {
				msgTitle = MsgTitleEnum.ERROR.getMsg();
			} else if (severityError.equals(FacesMessage.SEVERITY_WARN)) {
				msgTitle = MsgTitleEnum.WARNING.getMsg();
			} else {
				msgTitle = MsgTitleEnum.ERROR.getMsg();
			}
		}
		String msg = null;

		if (err.equals(ViewError.SYSTEM_ERR)) {
			msg = err.getMsg() + errCod;
		} else {
			msg = err.getMsg();
		}

		if (severityError != null) {
			ctx.addMessage(null, new FacesMessage(severityError, msgTitle, msg));
		} else {
			ctx.addMessage(null, new FacesMessage(msgTitle, msg));
		}

		return null;
	}

	/**
	 * Método static para padrinizar as mensagens de retorno da aplicação
	 * 
	 * @param severityError
	 * @param ctx
	 * @param err
	 * @param errCod
	 * @return
	 */
	public static String DisplayErrMsg(Severity severityError,
			FacesContext ctx,
			String err,
			String errCod) {

		String msgTitle = null;
		if (severityError != null) {
			if (severityError.equals(FacesMessage.SEVERITY_ERROR)) {
				msgTitle = MsgTitleEnum.ERROR.getMsg();
			} else if (severityError.equals(FacesMessage.SEVERITY_WARN)) {
				msgTitle = MsgTitleEnum.WARNING.getMsg();
			} else {
				msgTitle = MsgTitleEnum.ERROR.getMsg();
			}
		}

		if (severityError != null) {
			ctx.addMessage(null, new FacesMessage(severityError, msgTitle, err));
		} else {
			ctx.addMessage(null, new FacesMessage(msgTitle, err));
		}

		return null;
	}

	/**
	 * Método estático que retorna uma propriedade da session
	 * 
	 * @param nome
	 *            - Algumas propriedades em session; ID_MEMBER,
	 * 
	 * @return
	 */
	public static Object getPropertySession(String nome) {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		HttpServletRequest request = (HttpServletRequest) req;
		session = (HttpSession) request.getSession();
		return session.getAttribute(nome);
	}

	/**
	 * Método static para setar uma propriedade na session
	 * 
	 * @param value
	 *            - valor a ser setado
	 * @param nome
	 *            - Nome da propriedade na session
	 */
	public static void setPropertySessao(String nome, Object value) {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute(nome, value);
	}

	/** Método que retorna o IP da requesição */
	public static String getMeuIp() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getRemoteAddr();
	}

	/** Método que pega a sessão do usuário */
	public static String getSessionId() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		return session.getId();
	}

	private final static String	MOBILE_DEVICE_DETECTION_PARAM	= "primefaces.mobile.DEVICE_DETECTION";

	public static boolean ClienteMovel(String userAgent) {
		Pattern pattern = Pattern
				.compile("(iPhone|iPad|iPod|Android|BlackBerry|Opera Mobi|Opera Mini|IEMobile)");
		Matcher matcher = pattern.matcher(userAgent);

		return matcher.find();
	}

	public static String getBrowserInfo(String Information) {
		String browsername = "";
		String browserversion = "";
		String browser = Information;
		if (browser.contains("MSIE")) {
			String subsString = browser.substring(browser.indexOf("MSIE"));
			String info[] = (subsString.split(";")[0]).split(" ");
			browsername = info[0];
			browserversion = info[1];
		} else if (browser.contains("Firefox")) {

			String subsString = browser.substring(browser.indexOf("Firefox"));
			String info[] = (subsString.split(" ")[0]).split("/");
			browsername = info[0];
			browserversion = info[1];
		} else if (browser.contains("Chrome")) {

			String subsString = browser.substring(browser.indexOf("Chrome"));
			String info[] = (subsString.split(" ")[0]).split("/");
			browsername = info[0];
			browserversion = info[1];
		} else if (browser.contains("Opera")) {

			String subsString = browser.substring(browser.indexOf("Opera"));
			String info[] = (subsString.split(" ")[0]).split("/");
			browsername = info[0];
			browserversion = info[1];
		} else if (browser.contains("Safari")) {

			String subsString = browser.substring(browser.indexOf("Safari"));
			String info[] = (subsString.split(" ")[0]).split("/");
			browsername = info[0];
			browserversion = info[1];
		}
		return browsername + "-" + browserversion;
	}
	
	
	public static String formatPrivileges(String[] privilegios) {
		String txt = "";
		for (int i = 0; i < privilegios.length; i++){
			
			String temp = privilegios[i];
			if(privilegios.length > 1 && i > 0){
				if ( i == (privilegios.length - 1)) {
					temp = " e " + temp;
				}else{
					temp = ", " + temp;
				}
			}
			txt = txt + temp ;
		}
		return txt;
	}
	
	public static Map<String, String> convertToHashMap(String szGsonStr) {
		Gson jsonData = new Gson(); 
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> myMap = jsonData.fromJson(szGsonStr, type);
		return myMap;
	}
}
