package br.com.pix.qware.qwcfp.util.download;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.zip.CRC32;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * 
 *  objects from this class references a File to be downloaded "on the Fly".
 *  
 * @author rafael.ferreira
 *
 */
public class TunneledFile {

	private String		name;
	private BigDecimal	fileSize;
	private String		mimeType;
	private String		trueFileName;

	private String		httpUrl;
	private HttpClient	httpClient;
	
	public TunneledFile(String name, BigDecimal bigDecimal, String httpUrl, String trueFileName, String mimeType) {
		super();
		this.name = name;
		this.trueFileName = trueFileName;
		this.httpUrl = httpUrl;
		this.fileSize = bigDecimal;
		this.mimeType = mimeType;
	}

	/**
	 *  generates a {@link CRC32} of the File.
	 * @return
	 */
	public CRC32 genCrc32() {
		CRC32 crc = new CRC32();
		InputStream in;
		try {
			in = requestStream();

			BufferedInputStream bufferedIn = new BufferedInputStream(in, DownloadTunnel.BYTE_ARRAY_SIZE);
			try {
				byte[] buffer = new byte[DownloadTunnel.BYTE_ARRAY_SIZE];
				int bytesRead = 0;

				crc.reset();
				while ((bytesRead = bufferedIn.read(buffer)) != -1) {
					crc.update(buffer, 0, bytesRead);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bufferedIn.close();
				} catch (IOException e2) {
				}
			}

		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return crc;
	}

	/**
	 *  sends an get request to an Url to retrieve a File data Stream
	 * @return A {@link InputStream} containing the File´s data.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public InputStream requestStream() throws ClientProtocolException, IOException {

		HttpGet getRequest = new HttpGet(httpUrl);
		
		HttpResponse response;
		InputStream ret = null;

		response = httpClient.execute(getRequest);

		ret = response.getEntity().getContent();

		return ret;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getFileSize() {
		return fileSize;
	}

	public void setFileSize(BigDecimal fileSize) {
		this.fileSize = fileSize;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getTrueFileName() {
		return trueFileName;
	}

	public void setTrueFileName(String trueFileName) {
		this.trueFileName = trueFileName;
	}

}
