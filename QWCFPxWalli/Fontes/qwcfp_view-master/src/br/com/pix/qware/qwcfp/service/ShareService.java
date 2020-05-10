package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.list.ListSharedFiles;
import br.com.pix.qwcfp.ws.client.manager.ShareFile;
import br.com.qwcss.ws.dto.ShareDTO;
import br.com.qwcss.ws.dto.SimpleDTO;


public class ShareService extends Service {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5930100259804650725L;

	/**
	 *  Responsável por compartilhar um arquivo com outro membro diretamente.
	 *  
	 * @param idFileVersion id da versao do arquivo a ser compartilhada
	 * @param memberIdTo id do membro com quem o arquivo será compartilhado
	 * @param qtdDias quantidade de tempo, em dias, que o compartilhamento será valido.
	 * @return dto simples de resposta com codigo e menssagem de erro. codigo 0 para sucesso.
	 */
	public SimpleDTO shareFile(Integer idFileVersion, Integer memberIdTo, Integer qtdDias) {
		return ShareFile.shareFile(getConnectedUser(), idFileVersion, memberIdTo, qtdDias);
	}
	
	/**
	 *  Lista as referencias a arquivos compartilhados com o usuário logado.
	 * @return lista de referencias a versoes de arquivos compartilhados.
	 */
	public ShareDTO[] listSharedFiles() {
		return ListSharedFiles.listSharedFiles(getConnectedUser());
	}
	
}



