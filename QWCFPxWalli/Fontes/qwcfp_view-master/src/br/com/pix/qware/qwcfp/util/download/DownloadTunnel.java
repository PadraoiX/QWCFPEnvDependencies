package br.com.pix.qware.qwcfp.util.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class DownloadTunnel implements Runnable {

	public static final int		BYTE_ARRAY_SIZE	= 2048;
	private List<TunneledFile>	inputList;
	private PipedOutputStream	tunnelStream;

	/**
	 * @param outStream
	 * @param inputList
	 * @param pippedInput
	 */
	public DownloadTunnel(List<TunneledFile> inputList,
			PipedOutputStream tunnelStream) {
		super();
		this.inputList = inputList;
		this.tunnelStream = tunnelStream;
	}

	@Override
	public void run() {

		ZipOutputStream zipStream = new ZipOutputStream(tunnelStream);

		try {

			for (TunneledFile file : inputList) {
				InputStream in;
				in = file.requestStream();
				try{
					Long fileSize = null;
					
					try {
						fileSize = file.getFileSize().setScale(0, BigDecimal.ROUND_UP).longValueExact();
					} catch (Exception e) {
						fileSize = 0L;
					}
					
					ZipEntry zipentry = new ZipEntry(file.getName());
					zipentry.setMethod(ZipEntry.STORED);
					zipentry.setCrc(file.genCrc32().getValue());
					zipentry.setSize(fileSize);
					zipentry.setCompressedSize(fileSize);
					
					zipStream.putNextEntry(zipentry);
					
					BufferedInputStream bufferedIn = new BufferedInputStream(in,
							DownloadTunnel.BYTE_ARRAY_SIZE);
					byte[] buffer = new byte[DownloadTunnel.BYTE_ARRAY_SIZE];
					int bytesRead = 0;
					
					while ((bytesRead = bufferedIn.read(buffer)) != -1) {
						zipStream.write(buffer, 0, bytesRead);
					}
					zipStream.closeEntry();
					
				}finally{
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				zipStream.flush();
				zipStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
