package br.com.pix.qware.qwcfp.service;

import br.com.pix.qware.qwcfp.util.BRSUtil;
import br.com.pix.qwcfp.ws.client.list.Config;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.ws.BRSServico.Brslogado;

public class BRSService extends Service{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public Brslogado openConnection(String port, String ip) {
		ConfigDTO[] baseName = Config.carregar(getConnectedUser(), "PROP_QWISUSER", "SYSTEM_PROP");
		return BRSUtil.openConnection(baseName[0].getValue(), port, ip);
	}
	
	public Brslogado closeConnection(String nmSessao) {
		return BRSUtil.closeConnection(nmSessao);
	}
	
	public Brslogado openBase(String nmSessao) {
		ConfigDTO[] baseName = Config.carregar(getConnectedUser(), "PROP_QWISBASENAME", "SYSTEM_PROP");
		return BRSUtil.openBase(baseName[0].getValue(), nmSessao);
	}
	
	public Brslogado searchPlus(String nmSessao, String stringPesquisa, int nrDocPesquisa, int qtdPesquisa) {
		ConfigDTO[] baseName = Config.carregar(getConnectedUser(), "PROP_QWPARAGRAFNAME", "SYSTEM_PROP");
		return BRSUtil.searchPlus(baseName[0].getValue(), nmSessao, stringPesquisa, nrDocPesquisa, qtdPesquisa);
	}
	
}
