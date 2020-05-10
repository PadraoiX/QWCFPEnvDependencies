package br.com.pix.qware.qwcfp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.cliapi.QWException.QWException;
import br.com.pix.qwcfp.ws.client.connection.Definitions;
import br.com.pix.qwcfp.ws.client.connection.WsUrls;

public class QwcfpInitials {

	private static final String		PROPS_PATH	= "../qwcfpDocs/";
	
	private static final String		PROPS_FILE	= "qwcfp.properties";

	
	
	private static QwcfpInitials	instance	= null;
	private boolean					initialized	= false;
	private boolean					reset;

	private Properties				props;

	private QwcfpInitials() {
		reset =  false;
	}

	public static QwcfpInitials getInstance() {
		if (instance == null) {
			instance = new QwcfpInitials();
		}
		return instance;
	}

	public boolean initializeWebService() {
		if (!isInitialized()) {

			String ip = props.getProperty(PropKeys.WS_IP_KEY.getKey());
			String port = props.getProperty(PropKeys.WS_PORT_KEY.getKey());
			String version = props.getProperty(PropKeys.WS_VERSION_KEY.getKey());
			String https = props.getProperty(PropKeys.WS_HTTPS_KEY.getKey());
			String servico = props.getProperty(PropKeys.WS_SERV_NAME_KEY.getKey());
			
			
/*			String qwvdturl = String.format("%s://%s:%s/qwvdt-webservice/rest", props.getProperty(PropKeys.QWVDT_PROTOCOL.getKey()), props.getProperty(PropKeys.QWVDT_WS_IP.getKey()), props.getProperty(PropKeys.QWVDT_PORT.getKey()));
			String qwvdtUserAdmin = props.getProperty(PropKeys.QWVDT_USER_ADMIN.getKey());
			String qwvdtpassenc = props.getProperty(PropKeys.QWVDT_USER_PASS.getKey());
			String qwvdtUserPass = null; 
			
			if (qwvdtpassenc != null && !qwvdtpassenc.isEmpty()) {
				try {
					qwvdtUserPass = QWCrypto.getInstance().decryptPassInternal(qwvdtpassenc);
				} catch (QWException e) {
					e.printStackTrace();
				}
			}
			
 			if (qwvdturl != null && qwvdtUserAdmin != null && qwvdtUserPass != null){
				WsUrls.setQwvdturl(qwvdturl);
				WsUrls.setQwvdtUserAdmin(qwvdtUserAdmin);
				WsUrls.setQwvdtUserPass(qwvdtUserPass);
			} */	
		
			

			if (ip != null && port != null && version != null && https != null) {

				try {
					
					WsUrls.setHttps(https.equals("true") ? true : false);
					WsUrls.setInternetAddr(ip);
					WsUrls.setInternetPort(port);
					WsUrls.setServiceName(servico);
					
					WsUrls.setIsInternetAddr(ip);
					WsUrls.setIsInternetPort(port);

					Definitions.setIdAplicacao("1");
					Definitions.setIdVersionNumber(version);
					

				} catch (Exception e) {
					setInitialized(false);
					return false;
				}

				setInitialized(true);
				return true;
			} else {
				setInitialized(false);
				return false;
			}
		}
		return true;
	}

	public boolean loadPropertiesFile() {
		
		String JBOSS_HOME = System.getenv("JBOSS_HOME"); 
		
		File file;
		if (JBOSS_HOME != null && !JBOSS_HOME.isEmpty())
			file = new File(JBOSS_HOME +"/qwcfpDocs/" + PROPS_FILE);
		else
			file = new File(PROPS_PATH + PROPS_FILE);
		
		Properties prp = new Properties();
		FileInputStream in = null;

		try {
			in = new FileInputStream(file);

			prp.load(in);
			props = prp;

			if (isComplete()) {
				System.out.println("** QWCFP ** -- ** Arquivo de propriedades carregado. **   \""+ file.getAbsolutePath() + " \" ");

				return true;
			}

		} catch (Exception e) {
			return false;
		} finally {
			try {
				in.close();
			} catch (Exception e2) {
			}
		}

		return false;

	}

	public synchronized boolean mountProperties(Properties par) {

		String JBOSS_HOME = System.getenv("JBOSS_HOME"); 

		if (par != null) {
			props = par;
		} else {
			props = new Properties();
		}

		FileOutputStream out = null;
		if (!props.isEmpty()) {
			try {
				File file, directory;
				if (JBOSS_HOME != null && !JBOSS_HOME.isEmpty()){
					file = new File(JBOSS_HOME +"/qwcfpDocs/" + PROPS_FILE);
					directory = new File(JBOSS_HOME +"/qwcfpDocs/");
					
					if(!directory.exists())
						Files.createDirectory(Paths.get(JBOSS_HOME +"/qwcfpDocs/"));
					
				}else {
					file = new File(PROPS_PATH + PROPS_FILE);
					directory = new File(PROPS_PATH);
					
					if(!directory.exists())
						Files.createDirectory(Paths.get(PROPS_PATH));	
				}
				
				file.createNewFile();
				System.out.println("** QWCFP ** -- ** Arquivo de propriedades criado. ** ' " + file.getAbsolutePath() + " ' ");

				out = new FileOutputStream(file);
				props.store(out, "Arquivo de propriedades Sistema QWCFP");
				System.out.println("** QWCFP ** -- ** Arquivo de propriedades carregado. ** ' " + file.getAbsolutePath() + " ' ");

				return true;

			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("** QWCFP ** -- ** Propriedades carregadas em memória. **");
				return true;
			} finally {
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
		}

		System.out.println("** Arquivo de propriedades não pode ser carregado. **");
		return false;

	}

	public Properties getProperties() {
		return props;
	}

	public boolean isLoaded() {
		if (isReset()) {
			return false;
		}else{
			if (isComplete()) {
				return true;
			} else {
				return loadPropertiesFile();
			}
		}
		
	}

	public boolean isComplete() {

		if (props != null) {

			PropKeys[] values = PropKeys.values();

			for (PropKeys propKey : values) {
				if (propKey.isRequired()) {
					String propertie = props.getProperty(propKey.getKey());
					
					if (!(propertie != null && !propertie.isEmpty())) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}

	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}
}
