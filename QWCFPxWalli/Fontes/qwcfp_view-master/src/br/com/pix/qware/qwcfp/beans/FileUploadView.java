package br.com.pix.qware.qwcfp.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.EnviarArquivoService;
import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.ProcessQueueService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.TransferConfigDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

@ManagedBean(name = "fileUploadView")
@ViewScoped
public class FileUploadView implements Serializable {

	private static final long		serialVersionUID	= -1844961243893415493L;
	
	public static final String		HTTP_PUT			= "/put";

	@Inject
	private InformationGroupService	groupService;

	@Inject
	private FacesMessages			messages;

	@Inject
	private FileManagedService		fileManagedService;

	@Inject
	private FileVersionListService	fileVersionService;

	@Inject
	private EnviarArquivoService	uploadService;

	@Inject
	private ProcessQueueService		processQueueService;
	
	@Inject
	private ConfigService			configService;

	private UploadedFile			file;

	private boolean					replace;
	
	private String 					inforAdd;
	
	public static void main(String[] args) {
		
		FileUploadView fuw = new FileUploadView();
		try {
			InputStream in = new FileInputStream(new File("C:\\Users\\youre.fernandez\\Desktop\\dog.jpg"));
			Retorno retorno = fuw.postFile("http://172.16.253.188:8080/LoginTrust", in , "dog.jpg");
			System.out.println(retorno.getCodeErro());
			System.out.println(retorno.getMsg());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public void upload() {
		if (file != null) {
			TransferConfigDTO retornoString = null;
			SimpleDTO simple = null;
			try {
				boolean novo = false;
				retornoString = inserirFileAndVersion( file, novo, inforAdd != null ? inforAdd : "--" );
				if (retornoString != null) {
					String sourceFileFullPath = file.getFileName();
					if (retornoString.getErrorCode().equals(0)) {
						
						ConfigDTO[] cfgHttpUrl = configService.carregarByKey("HTTP_TRANSFER_URL");
						String httpUrl = "";
						
						for (ConfigDTO configDTO : cfgHttpUrl) {
							httpUrl = configDTO.getValue();
						}
						
						Retorno retorno = postFile(httpUrl + HTTP_PUT,
								file.getInputstream(),
								retornoString.getSaveAs());

						if (!retorno.getCodeErro().equals("200")) {
							messages.add("Erro: " + retorno.getCodeErro()
									+ "\n Não foi possível carregar o arquivo "
									+ sourceFileFullPath + " porque: " + retorno.getMsg(),
									FacesMessage.SEVERITY_ERROR);
						} else {
							if (replace && !novo)
								simple = processQueueService.repleaceFile(retornoString.getVersionId());
							else
								simple = processQueueService.insertNewFile(retornoString.getVersionId());

							if (simple.getErrorCode() == ViewError.OK.getCode())
								messages.add("Sucesso!\n" + sourceFileFullPath
										+ "\n Arquivo Carregado!", FacesMessage.SEVERITY_INFO);
							else
								messages.add("Ocorreu um erro tentando enfilerar o arquivo. Código de erro: "
										+ simple.getErrorCode() + ": " + simple.getErrorMsg(),
										FacesMessage.SEVERITY_ERROR);
						}

					} else
						messages.add("Erro! \nNão foi possível carregar o arquivo : "
								+ sourceFileFullPath + "\n" + retornoString.getErrorCode() + " "
								+ retornoString.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				}

				replace = false;

			} catch (Exception e) {
				e.printStackTrace();
				messages.add("Erro! \nNão foi possível carregar o arquivo " + e.getMessage(),
						FacesMessage.SEVERITY_ERROR);
			}
		}
	}
	

	/**
	 * Método utilizado pela interface desktop para realizar upload de arquivo
	 * 
	 * @param event
	 * @param aditionalInformation 
	 */
	public void upload(FileUploadEvent event ) {

		TransferConfigDTO retornoString = null;
		SimpleDTO simple = null;
		//
		

		try {
		
			retornoString = inserirFileAndVersion( event, inforAdd != null ? inforAdd : "--" );

			if (retornoString != null) {
				if (retornoString.getErrorCode().equals(0)) {
					
					ConfigDTO[] cfgHttpUrl = configService.carregarByKey("HTTP_TRANSFER_URL");
					
					String httpUrl = "";
					
					for (ConfigDTO configDTO : cfgHttpUrl) {
						httpUrl = configDTO.getValue();
					}
					
					Retorno retorno = postFile(httpUrl + HTTP_PUT, event
							.getFile().getInputstream(), retornoString.getSaveAs());

					
					
					if (!retorno.getCodeErro().equals("200")) {
						messages.add("Erro: " + retorno.getCodeErro()
								+ "\n Não foi possível carregar o arquivo "
								+ event.getFile().getFileName() + " porque: " + retorno.getMsg(),
								FacesMessage.SEVERITY_ERROR);
					} else {

						if (replace && !retornoString.getAsa().equals("S"))
							simple = processQueueService.repleaceFile(retornoString.getVersionId());
						else
							simple = processQueueService.insertNewFile(retornoString.getVersionId());

						if (simple.getErrorCode() == ViewError.OK.getCode()) {
							messages.add("Sucesso!\n" + event.getFile().getFileName()
									+ "\n Arquivo Carregado!", FacesMessage.SEVERITY_INFO);
						} else
							messages.add("Ocorreu um erro tentando enfilerar o arquivo. Código de erro: "
									+ simple.getErrorCode() + ": " + simple.getErrorMsg(),
									FacesMessage.SEVERITY_ERROR);
					}

				} else
					messages.add("Erro! \nNão foi possível carregar o arquivo : "
							+ event.getFile().getFileName() + "\n" + retornoString.getErrorCode()
							+ " " + retornoString.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			}

			replace = false;
		} catch (Exception e) {
			e.printStackTrace();
			messages.add("Erro! \nNão foi possível carregar o arquivo " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}
	}
	

	public void repleaceBt() {
		if (isReplace())
			setReplace(true);
		else
			setReplace(false);
	}
	
	/**
	 * Envia um arquivo a um servidor diferente ao servidor que a aplicao est rodando
	 * 
	 * @param url
	 *            do servidor a ser conectado
	 * @param in
	 *            fluxo do arquivo
	 * @param fileName
	 *            nome do arquivo
	 * @return Um objeto Retorno, com cdigo de erro e mensagem de erro
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Retorno postFile(String url, InputStream in, String fileName) throws ClientProtocolException, IOException {

		// instanciando o clinte (a aplicao'QWCFP')
		HttpClient client = HttpClientBuilder.create().build();

		// preparando para enviar uma requisio do tipo POST ao servidor
		HttpPost postRequest = new HttpPost(url);

		MultipartEntityBuilder multiPartEntity = MultipartEntityBuilder.create();

		InputStreamBody streamBody = new InputStreamBody(in, fileName);

		multiPartEntity.addPart("attachment", streamBody);
		
		postRequest.setEntity(multiPartEntity.build());
		HttpResponse response = client.execute(postRequest);
		String servletOutput = EntityUtils.toString(response.getEntity());

		System.out.println("********************************************\n" + servletOutput	+ "********************************************");

		Pattern p = Pattern.compile("<h1>+([0-9]+)</h1>\n<h2>+(.+)</h2>");
		Matcher m = p.matcher(servletOutput);

		String erroCod = null;
		String msg = null;
		if (m.find()) {
			erroCod = m.group(1);
			msg = m.group(2);
		}

		Retorno retorno = new Retorno();

		retorno.setCodeErro(erroCod);
		retorno.setMsg(msg);
		return retorno;

	}

	/**
	 * Método utilizado para inserir a versão no banco de dados e associá-lo a um grupo
	 * 
	 * @param event
	 * @return
	 */
	private TransferConfigDTO inserirFileAndVersion(FileUploadEvent event, String aditionalInformation ) {
		UploadedFile file = event.getFile();
		String sourceFileFullPath = file.getFileName();
		
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		HttpSession session = (HttpSession) req.getSession();

		Integer groupId = (Integer) session.getAttribute("ID_GROUP");
		TransferConfigDTO retorno = new TransferConfigDTO();
		retorno.setErrorCode(0);
		retorno.setErrorMsg("");
		uploadService.setOnlyWsContract(true);

		if (groupId != null) {
			GroupDTO grupo = groupService.getGroupById(groupId);
			if (grupo != null) {
				if (grupo.getErrorCode() == 0) {
					String apelido = grupo.getApelido();
					String aceptVersion = grupo.getAceptVersion().trim();

					FileListDTOEx[] files = fileManagedService.listar(apelido,
							sourceFileFullPath,
							0,
							10, false,null,null);

					VersionsDTO[] fileVersion = null;

					if ((files == null || (files.length == 0)) && aceptVersion.equals("N")) {
						retorno = uploadService.naoVersionado_novo(apelido,
								aditionalInformation,
								sourceFileFullPath,
								file.getSize());
						retorno.setAsa("S");
					}

					if ((files == null || (files.length == 0)) && aceptVersion.equals("S")) {
						retorno = uploadService.versionado_novo(apelido,
								aditionalInformation,
								sourceFileFullPath,
								file.getSize());
						
						retorno.setAsa("S");
					}

					if (files != null && (files.length > 0) && aceptVersion.equals("N")) {
						if (replace)
							retorno = uploadService.naoVersionado_replace(apelido,
									aditionalInformation,
									sourceFileFullPath,
									file.getSize());
						else {
							retorno.setErrorCode(ViewError.GRUPO_NOT_VERSION.getCode());
							retorno.setErrorMsg(ViewError.GRUPO_NOT_VERSION.getMsg()
									+ " Por favor, escolha a opção de realizar o replace e tente de novo");
							return retorno;
						}
					}

					if (files != null && (files.length > 0) && aceptVersion.equals("S")) {
						if (replace)
							retorno = uploadService.versionado_replace(apelido,
									aditionalInformation,
									sourceFileFullPath,
									file.getSize());
						else {
							fileVersion = fileVersionService.listar(apelido,
									sourceFileFullPath,
									"TRANSFERINDO");
							if (fileVersion != null) {
								if (fileVersion.length >= grupo.getVersionLimit()) {
									retorno.setErrorCode(ViewError.GRUPO_EXCEEDED_VERSION.getCode());
									retorno.setErrorMsg(ViewError.GRUPO_EXCEEDED_VERSION.getMsg()
											+ ". Por favor, escolha a opção de realizar o repleace e tente de novo");
									return retorno;
								}
							}

							retorno = uploadService.versionado_novo(apelido,
									aditionalInformation,
									sourceFileFullPath,
									file.getSize());
						}

					}
				} else {
					retorno.setErrorCode(grupo.getErrorCode());
					retorno.setErrorMsg(grupo.getErrorMsg());
				}

			} else {
				retorno.setErrorCode(ViewError.WEBSERVICE_OFF_ERR.getCode());
				retorno.setErrorMsg(ViewError.WEBSERVICE_OFF_ERR.getMsg());
			}

		} else {
			retorno.setErrorCode(ViewError.WEBSERVICE_OFF_ERR.getCode());
			retorno.setErrorMsg(ViewError.WEBSERVICE_OFF_ERR.getMsg());
		}

		return retorno;

	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	private TransferConfigDTO inserirFileAndVersion(UploadedFile file, boolean novo, String aditionalInformation ) {
		String sourceFileFullPath = file.getFileName();
		
		

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		HttpSession session = (HttpSession) req.getSession();

		Integer groupId = (Integer) session.getAttribute("ID_GROUP");
		TransferConfigDTO retorno = new TransferConfigDTO();
		retorno.setErrorCode(0);
		retorno.setErrorMsg("");
		uploadService.setOnlyWsContract(true);

		if (groupId != null) {
			GroupDTO grupo = groupService.getGroupById(groupId);
			if (grupo != null) {
				if (grupo.getErrorCode() == 0) {
					String apelido = grupo.getApelido();
					String aceptVersion = grupo.getAceptVersion().trim();

					FileListDTOEx[] files = fileManagedService.listar(apelido,
							sourceFileFullPath,
							0,
							10, false,null,null);

					VersionsDTO[] fileVersion = null;

					if ((files == null || (files.length == 0)) && aceptVersion.equals("N")) {
						retorno = uploadService.naoVersionado_novo(apelido,
								aditionalInformation,
								sourceFileFullPath,
								file.getSize());
						novo = true;
					}

					if ((files == null || (files.length == 0)) && aceptVersion.equals("S")) {
						retorno = uploadService.versionado_novo(apelido,
								aditionalInformation,
								sourceFileFullPath,
								file.getSize());
						novo = true;
					}

					if (files != null && (files.length > 0) && aceptVersion.equals("N")) {
						if (replace)
							retorno = uploadService.naoVersionado_replace(apelido,
									aditionalInformation,
									sourceFileFullPath,
									file.getSize());
						else {
							retorno.setErrorCode(ViewError.GRUPO_NOT_VERSION.getCode());
							retorno.setErrorMsg(ViewError.GRUPO_NOT_VERSION.getMsg()
									+ " Por favor, escolha a opção de realizar o repleace e tente de novo");
							return retorno;
						}
					}

					if (files != null && (files.length > 0) && aceptVersion.equals("S")) {
						if (replace)
							retorno = uploadService.versionado_replace(apelido,
									aditionalInformation,
									sourceFileFullPath,
									file.getSize());
						else {
							fileVersion = fileVersionService.listar(apelido,
									sourceFileFullPath,
									"TRANSFERINDO");
							if (fileVersion != null) {
								if (fileVersion.length >= grupo.getVersionLimit()) {
									retorno.setErrorCode(ViewError.GRUPO_EXCEEDED_VERSION.getCode());
									retorno.setErrorMsg(ViewError.GRUPO_EXCEEDED_VERSION.getMsg()
											+ ViewError.PLEASE_CHOOSE_REPLACE.getMsg());
									return retorno;
								}
							}

							retorno = uploadService.versionado_novo(apelido,
									aditionalInformation,
									sourceFileFullPath,
									file.getSize());
						}

					}
				} else {
					retorno.setErrorCode(grupo.getErrorCode());
					retorno.setErrorMsg(grupo.getErrorMsg());
				}

			} else {
				retorno.setErrorCode(ViewError.WEBSERVICE_OFF_ERR.getCode());
				retorno.setErrorMsg(ViewError.WEBSERVICE_OFF_ERR.getMsg());
			}

		} else {
			retorno.setErrorCode(ViewError.WEBSERVICE_OFF_ERR.getCode());
			retorno.setErrorMsg(ViewError.WEBSERVICE_OFF_ERR.getMsg());
		}

		return retorno;

	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public boolean isReplace() {
		return replace;
	}

	public void setReplace(boolean repleace) {
		this.replace = repleace;
	}

	public String getInforAdd() {
		return inforAdd;
	}
	
	public void setJustificativa(String inforAdd) {
		setInforAdd(inforAdd);
	}

	public void setInforAdd(String inforAdd) {
		this.inforAdd = inforAdd;
	}

	private static class Retorno {
		private String	codeErro;

		private String	msg;

		public String getCodeErro() {
			return codeErro;
		}

		public void setCodeErro(String codeErro) {
			this.codeErro = codeErro;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}
}