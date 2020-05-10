package br.com.pix.qware.qwcfp.service;

import br.com.pix.qwcfp.ws.client.notifie.Mail;
import br.com.qwcss.ws.dto.SimpleDTO;

public class MailService extends Service{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * 	Envia E-mail para um ou mais usuários.
	 * 	
	 * @param to array com os destinatários
	 * @param subject O assunto
	 * @param msg  o conteúdo
	 * @return dto com o código de erro.
	 */
	public SimpleDTO sendMail(String[] to, String subject, String msg) {
		return Mail.sendMail(getConnectedUser(), to, subject, msg);
	}

}
