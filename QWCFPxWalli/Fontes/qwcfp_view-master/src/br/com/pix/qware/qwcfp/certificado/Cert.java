package br.com.pix.qware.qwcfp.certificado;

import java.util.List;

import br.gov.frameworkdemoiselle.certificate.extension.DefaultExtension;
import br.gov.frameworkdemoiselle.certificate.extension.DefaultExtensionType;
import br.gov.frameworkdemoiselle.certificate.extension.ICPBrasilExtension;
import br.gov.frameworkdemoiselle.certificate.extension.ICPBrasilExtensionType;

public class Cert {

	@ICPBrasilExtension(type = ICPBrasilExtensionType.CPF)
	private String			cpf;

	@ICPBrasilExtension(type = ICPBrasilExtensionType.NOME)
	private String			nome;
	
	@DefaultExtension(type = DefaultExtensionType.CRL_URL)
	private List<String>	crlURL;

	public String getCpf() {
		return cpf;
	}

	public String getNome() {
		return nome;
	}

	public List<String> getCrlURL() {
		return crlURL;
	}

}
