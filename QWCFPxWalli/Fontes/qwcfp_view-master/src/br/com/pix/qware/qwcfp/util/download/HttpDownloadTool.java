package br.com.pix.qware.qwcfp.util.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.service.ReceberArquivoService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.TransferConfigDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

/**
 * 
 * Classe para realizar o Download via http
 * 
 * @author rafael.ferreira
 * 
 */
@Named
@RequestScoped
public class HttpDownloadTool implements Serializable {

	private static final String		CONTENT_TYPE		= "CONTENTTYPE_";
	private static final String		REAL_NAME			= "REALNAME_";
	private static final String		FILE_NAME			= "FILENAME_";
	private static final String		FILE_AMOUNT			= "fileAmount";
	
	public static final String		HTTP_GET			= "/get";
	public static final String		HTTP_ZIP			= "/zipped";
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	private String					httpUrl;

	@Inject
	private ReceberArquivoService	receberArquivoSerivce;

	@Inject
	private FileVersionListService	versionService;

	@Inject
	private FacesMessages			messages;

	@Inject
	private ConfigService			configService;

	@PostConstruct
	public void init() {
		ConfigDTO[] httpUrl = configService.carregarByKey("HTTP_TRANSFER_URL");
		
		for (ConfigDTO configDTO : httpUrl) {
			this.httpUrl = configDTO.getValue();
		}
	}

	/**
	 * 
	 * Wraps a list of {@link TunneledFile} objects into one Zipped file on the fly.
	 * 
	 * @param tunneledFileList
	 *            list of files to be wrapped as a zip for download
	 * @return {@link PipedInputStream} with the stream for the zipped file.
	 */
	@Deprecated
	public PipedInputStream multipleFileDownload(List<TunneledFile> tunneledFileList) {

		PipedInputStream pippedInput = new PipedInputStream(DownloadTunnel.BYTE_ARRAY_SIZE);
		PipedOutputStream pippedOutput;
		try {
			pippedOutput = new PipedOutputStream(pippedInput);

			Thread download = new Thread(new DownloadTunnel(tunneledFileList, pippedOutput));
			download.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pippedInput;
	}

	public InputStream multipleFileDownloadPost(List<TunneledFile> tunneledFileList, String contentType) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		if (httpUrl != null) {
			
			HttpPost httpPost = new HttpPost(httpUrl + HTTP_ZIP);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair(FILE_AMOUNT, Integer.toString(tunneledFileList.size())));
			
			int i = 0;
			for (TunneledFile file : tunneledFileList) {
   				params.add(new BasicNameValuePair(FILE_NAME + Integer.toString(i), file.getName()));
				params.add(new BasicNameValuePair(REAL_NAME + Integer.toString(i), file.getTrueFileName()));
				params.add(new BasicNameValuePair(CONTENT_TYPE + Integer.toString(i), file.getMimeType()));
				i++;
			}
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				
				HttpResponse response = httpClient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream input = entity.getContent();
						ContentType cTypeParser = null;
						cTypeParser = ContentType.get(entity);
						
						contentType = cTypeParser.getMimeType();
						
						if (contentType.indexOf("html") > -1) {
							Document html = Jsoup.parse(EntityUtils.toString(entity));
							
							messages.add("Erro ao realizar download de arquivos: "
									+ html.getElementsByTag("h2").text(), FacesMessage.SEVERITY_ERROR);
							return null;
						}
						
						return input;
					}
				} else {
					messages.add(ViewError.TRANSFER_SYS_ERROR.getMsg(),
							FacesMessage.SEVERITY_ERROR);
				}
				
			} catch (ClientProtocolException e) {
				messages.add(ViewError.TRANSFER_PROTOCOL_ERROR.getMsg(),
						FacesMessage.SEVERITY_ERROR);
			} catch (IOException e) {
				messages.add(ViewError.TRANSFER_CONN_ERROR.getMsg(),
						FacesMessage.SEVERITY_ERROR);
			}
		}else{
			
			messages.add(ViewError.TRANSFER_CONFIG_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
			return null;
		}

		return null;
	}

	/**
	 * 
	 * Based on the fileVersion scope of a file, this method returns an object {@link TunneledFile} which has an
	 * inputStream with the data of the file that will be downloaded.
	 * 
	 * @param fileName
	 *            File name
	 * @param fileVersionId
	 *            id of the version which will be downloaded
	 * @param isOne
	 *            when false, the returned {@link TunneledFile} object will contain the info needed to be zipped, so
	 *            that it can be downloaded with other files.
	 * @return {@link TunneledFile} And null, in case there´s any error.
	 */
	public TunneledFile downloadFileVersioned(String fileName,
			Integer fileVersionId,
			Integer fileManagedId,
			SimpleDTO simpleDTO) {

		return getTunneledFile(fileName, null, fileManagedId, fileVersionId, simpleDTO);
	}

	/**
	 * 
	 * Based on the fileManaged scope of a file, this method returns an object {@link TunneledFile} which has an
	 * inputStream with the data of the file that will be downloaded.
	 * 
	 * @param fileName
	 *            File name
	 * @param fileManagedId
	 *            id of the version which will be downloaded
	 * @param groupAlias
	 *            File´s group alias constant
	 * @param isOne
	 *            when false, the returned {@link TunneledFile} object will contain the info needed to be zipped, so
	 *            that it can be downloaded with other files.
	 * @return {@link TunneledFile} And null, in case there´s any error.
	 * 
	 */
	public TunneledFile downloadFileManaged(String fileName,
			Integer fileManagedId,
			String groupAlias,
			SimpleDTO simpleDTO) {

		return getTunneledFile(fileName, groupAlias, fileManagedId, null, simpleDTO);
	}

	private TunneledFile getTunneledFile(String fileName,
			String groupAlias,
			Integer fileManagedId,
			Integer fileVersionId,
			SimpleDTO simpleDTO) {

		TransferConfigDTO transferConfig;
		VersionsDTO fileVersion;

		if (fileVersionId == null) {

			transferConfig = receberArquivoSerivce.receber_versao(fileName, groupAlias, null);
			fileVersion = versionService.listarUltimaVersao(fileManagedId);
		} else {

			transferConfig = receberArquivoSerivce.receber_versao(fileVersionId, "");
			fileVersion = versionService.listarVersao(fileManagedId, fileVersionId);
		}

		if (transferConfig.getErrorCode() != 0 || !transferConfig.getErrorMsg().equals("")) {
			simpleDTO.setErrorMsg(fileName + " - " + transferConfig.getErrorMsg());
			return null;
		}

		if (fileVersion.getErrorCode() != 0) {
			simpleDTO.setErrorMsg(fileName + " - " + fileVersion.getErrorMsg());
			return null;
		}

		String path = transferConfig.getFileName();
		String mimeType = transferConfig.getFileType();
		System.out.println(transferConfig.getFileSize());
		
		if (httpUrl != null) {
			
			String newUrl = httpUrl + HTTP_GET + path;
			
			TunneledFile tunneledFile = new TunneledFile(fileName, fileVersion.getSizeInBytes(), newUrl, path, mimeType);
			return tunneledFile;
			
		}else{
			messages.add(ViewError.TRANSFER_CONFIG_ERROR.getMsg(), FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}

	public String getHttpUrl() {
		return httpUrl;
	}
}
