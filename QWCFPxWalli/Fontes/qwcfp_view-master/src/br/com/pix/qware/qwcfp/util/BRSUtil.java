package br.com.pix.qware.qwcfp.util;

import br.com.brsSearchPlus.ws.BRSSearchPlusSoapBindingStub;
import br.com.brscloseConnection.ws.BRSCloseConnectionSoapBindingStub;
import br.com.brsopenBase.ws.BRSOpenBaseSoapBindingStub;
import br.com.brsopenConnection.ws.BRSOpenConnectionSoapBindingStub;
import br.com.pix.qwcfp.ws.client.connection.WsUrls;
import br.com.ws.BRSServico.Brslogado;

public class BRSUtil {

	private static final String	BRS_ISSUE	= "BRS ISSUE: ";

	/*private static final String	SYSTEM_PROP			= "SYSTEM_PROP";
	private static final String	PROP_QWISUSER		= "PROP_QWISUSER";
	private static final String	PROP_QWPARAGRAFNAME	= "PROP_QWPARAGRAFNAME";
	private static final String	PROP_QWISBASENAME	= "PROP_QWISBASENAME";*/

	public static Brslogado openConnection(String connectedUser, String port, String ip) {

		Brslogado logado = new Brslogado();

		try {
			BRSOpenConnectionSoapBindingStub openCon = new BRSOpenConnectionSoapBindingStub(WsUrls.getUrlBRSOpenConnection(), null);
			//ConfigDTO[] userName = Config.carregar(connectedUser, PROP_QWISUSER, SYSTEM_PROP);

			//if (userName != null && userName.length > 0) {
				logado = openCon.BRSOpenConnection(ip, port, connectedUser, "");
				if (logado.getErroMsg() != null && !logado.getErroMsg().isEmpty()) {
					System.err.println(BRS_ISSUE + logado.getErroMsg());
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logado;
	}

	public static Brslogado closeConnection(String nmSessao) {

		Brslogado logado = new Brslogado();

		try {
			BRSCloseConnectionSoapBindingStub closeCon = new BRSCloseConnectionSoapBindingStub(WsUrls.getUrlBRSCloseConnection(), null);

			logado = closeCon.BRSCloseConnection(nmSessao);
			if (logado.getErroMsg() != null && !logado.getErroMsg().isEmpty()) {
				System.err.println(BRS_ISSUE + logado.getErroMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logado;
	}

	public static Brslogado openBase( String connectedUser, String nmSessao) {

		Brslogado logado = new Brslogado();

		try {
			BRSOpenBaseSoapBindingStub openBase = new BRSOpenBaseSoapBindingStub(WsUrls.getUrlBRSOpenBase(), null);
			//ConfigDTO[] baseName = Config.carregar(connectedUser, PROP_QWISBASENAME, SYSTEM_PROP);

			//if (baseName != null && baseName.length > 0) {
				logado = openBase.BRSOpenBase(nmSessao, connectedUser);
				if (logado.getErroMsg() != null && !logado.getErroMsg().isEmpty()) {
					System.err.println(BRS_ISSUE + logado.getErroMsg());
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logado;
	}

	public static Brslogado searchPlus(String connectedUser, String nmSessao, String stringPesquisa, int nrDocPesquisa, int qtdPesquisa) {

		Brslogado logado = new Brslogado();

		try {
			BRSSearchPlusSoapBindingStub openCon = new BRSSearchPlusSoapBindingStub(WsUrls.getUrlBRSSearchPlus(), null);
			//ConfigDTO[] baseName = Config.carregar(connectedUser, PROP_QWPARAGRAFNAME, SYSTEM_PROP);

			//if (baseName != null && baseName.length > 0) {
				logado = openCon.BRSSearchPlus(nmSessao, stringPesquisa, connectedUser, nrDocPesquisa, qtdPesquisa);
				if (logado.getErroMsg() != null && !logado.getErroMsg().isEmpty()) {
					System.err.println(BRS_ISSUE + logado.getErroMsg());
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logado;
	}
}
