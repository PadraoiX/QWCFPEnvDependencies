package br.com.pix.qware.qwcfp.wrappers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.qwcss.ws.dto.MyGroupsEXDTO;

public class MyGroupsWrapper implements Comparable<MyGroupsWrapper> {

	private MyGroupsEXDTO			myGroupsEx;
	private boolean					manager;
	private boolean					download;
	private boolean					uploadRep;
	private boolean					uploadNew;
	private boolean					downloadExpired;
	private boolean					uploadExpired;
	private boolean					aceptVersioning;
	private boolean					blindPrivileges;

	private static String			uploadNameOff				= "upload1a.png";
	private static String			uploadNameOn				= "upload2a.png";
	private static String			downloadNameOff				= "download1a.png";
	private static String			downloadNameOn				= "download2a.png";

	private static String			uploadTitleOff				= "Você não tem permissão para realizar o upload de arquivos neste grupo.";
	private static String			uploadTitleOn				= "Você tem permissão para realizar o upload de arquivos neste grupo. ";
	private static String			uploadTitleExpired			= "O upload de arquivos neste grupo expirou na data: ";
	private static String			uploadTitleExpiredNoDate	= "O upload de arquivos neste grupo expirou.";
	private static String			downloadTitleOff			= "Você não tem permissão para realizar o download de arquivos neste grupo.";
	private static String			downloadTitleOn				= "Você tem permissão para realizar o download de arquivos neste grupo.";
	private static String			downloadTitleExpired		= "O download de arquivos neste grupo expirou na data: ";
	private static String			downloadTitleExpiredNoDate	= "O download de arquivos neste grupo expirou";

	private static SimpleDateFormat	df							= new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * @param myGroupsEx
	 */
	public MyGroupsWrapper(MyGroupsEXDTO myGroupsEx) {
		super();
		initWrapper(myGroupsEx);
	}
	
	public static MyGroupsWrapper[] WrappMyGroupsArray(MyGroupsEXDTO[] myGroupsExArray) {
		MyGroupsWrapper[] retArray =  new  MyGroupsWrapper[myGroupsExArray.length];
		for (int i = 0; i < myGroupsExArray.length; i++) {
			retArray[i] = new MyGroupsWrapper(myGroupsExArray[i]);
		}
		
		return retArray;
	}

	public void initWrapper(MyGroupsEXDTO myGroupsEx2) {
		
		if (myGroupsEx2 == null)
			myGroupsEx2 = new MyGroupsEXDTO();
		
		blindPrivileges = true;
		String hashPrivs = myGroupsEx2.getPrivStringValue();
		if (hashPrivs != null && !hashPrivs.isEmpty()) {

			List<String> privs = Arrays.asList(hashPrivs.split(","));

			if (privs.contains("GESTOR-GRP") || privs.contains("GESTOR-ARE")) {
				manager = true;
			} else {
				manager = false;
			}
			if (privs.contains("NEW-GRP")) {
				uploadNew = true;
			} else {
				uploadNew = false;
			}
			if (privs.contains("REPL-GRP")) {
				uploadRep = true;
			} else {
				uploadRep = false;
			}
			if (privs.contains("DOWN-GRP")) {
				download = true;
			} else {
				download = false;
			}
		} else {
			download = false;
			uploadRep = false;
			uploadNew = false;
			manager = false;
		}

		Calendar oDate = myGroupsEx2.getGroupOutputDtLimit();
		Calendar iDate = myGroupsEx2.getGroupInputDtLimit();
		Date now = new Date(System.currentTimeMillis());

		if (oDate != null) {
			if (now.before(oDate.getTime())) {
				downloadExpired = false;
			} else {
				downloadExpired = true;
			}
		} else {
			downloadExpired = true;
		}

		if (iDate != null) {
			if (now.before(iDate.getTime())) {
				uploadExpired = false;
			} else {
				uploadExpired = true;
			}
		} else {
			uploadExpired = true;
		}

		if (myGroupsEx2.getGroupAceptVersioning() != null
				&& !myGroupsEx2.getGroupAceptVersioning().isEmpty()) {

			if (myGroupsEx2.getGroupAceptVersioning().equals("S")) {
				aceptVersioning = true;
			} else {
				aceptVersioning = false;
			}
		} else {
			aceptVersioning = false;
		}

		if (download || uploadRep || uploadNew || manager) {
			blindPrivileges = false;
		}
		
		this.myGroupsEx = myGroupsEx2;

	}

	public String getDownloadName() {

		if (this.isDownloadExpired()) {
			return downloadNameOff;
		} else {
			if (this.isManager()) {
				return downloadNameOn;
			} else {
				return this.isDownload() ? downloadNameOn : downloadNameOff;
			}
		}
	}

	public String getUploadName() {

		if (this.isUploadExpired()) {
			return uploadNameOff;
		} else {
			if (this.isManager()) {
				return uploadNameOn;
			} else {
				return this.isUploadNew() || this.isUploadRep() ? uploadNameOn : uploadNameOff;
			}
		}
	}

	public String getDownloadTitle() {

		if (this.isDownloadExpired()) {
			Calendar oDate = myGroupsEx.getGroupInputDtLimit();
			if (oDate != null) {
				return downloadTitleExpired + df.format(oDate.getTime());
			} else {
				return downloadTitleExpiredNoDate;
			}
		} else {
			if (this.isManager()) {
				return downloadTitleOn;
			} else {
				return this.isDownload() ? downloadTitleOn : downloadTitleOff;
			}
		}
	}

	public String getUploadTitle() {

		if (this.isDownloadExpired()) {
			Calendar oDate = myGroupsEx.getGroupInputDtLimit();
			if (oDate != null) {
				return uploadTitleExpired + df.format(oDate.getTime());
			} else {
				return uploadTitleExpiredNoDate;
			}
		} else {
			if (this.isManager()) {
				return uploadTitleOn;
			} else {
				if (this.isUploadNew() || this.isUploadRep()) {
					return uploadTitleOn
							+ (this.isUploadNew() ? "Para nova versão de arquivo " : "")
							+ (this.isUploadNew() && this.isUploadRep() ? "e " : "")
							+ (this.isUploadRep() ? "Para Atualização de versão de arquivo " : "");
				} else {
					return uploadTitleOff;
				}
			}
		}
	}

	public boolean canUpload() {
		if (this.isUploadExpired()) {
			return false;
		} else {
			if (this.isManager()) {
				return true;
			} else {
				return this.isUploadNew() || this.isUploadRep() ? true : false;
			}
		}
	}

	public boolean canDownload() {
		if (this.isDownloadExpired()) {
			return false;
		} else {
			if (this.isManager()) {
				return true;
			} else {
				return this.isDownload() ? true : false;
			}
		}
	}

	public MyGroupsEXDTO getMyGroupsEx() {
		return myGroupsEx;
	}

	public void setMyGroupsEx(MyGroupsEXDTO myGroupsEx) {
		this.myGroupsEx = myGroupsEx;
	}

	public boolean isManager() {
		return manager;
	}

	public boolean isDownload() {
		return download;
	}

	public boolean isUploadRep() {
		return uploadRep;
	}

	public boolean isUploadNew() {
		return uploadNew;
	}

	public boolean isDownloadExpired() {
		return downloadExpired;
	}

	public boolean isUploadExpired() {
		return uploadExpired;
	}

	public boolean isAceptVersioning() {
		return aceptVersioning;
	}

	@Override
	public int compareTo(MyGroupsWrapper o) {
		/*		if (o != null) {
					if (myGroupsEx != null) {
						MyGroupsEXDTO parGroup = o.getMyGroupsEx();
						if (parGroup != null) {
							return myGroupsEx.compareTo(parGroup);
						}
					}
				}
		*/
		return o != null ? o.getMyGroupsEx().compareTo(myGroupsEx) : 1;
	}

	public boolean isBlindPrivileges() {
		return blindPrivileges;
	}

	public void setBlindPrivileges(boolean blindPrivileges) {
		this.blindPrivileges = blindPrivileges;
	}

}
