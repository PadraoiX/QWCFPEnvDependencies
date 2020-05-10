package br.com.pix.qware.qwcfp.wrappers;

public class PrivWrapper {

	private br.com.qwcss.ws.PrivilegioDTO	priv;
	private String							tipo;
	private String							desc;

	public PrivWrapper(br.com.qwcss.ws.PrivilegioDTO ggrpPrivs, String tipo, String desc) {
		super();
		this.priv = ggrpPrivs;
		this.tipo = tipo;
		this.desc = desc;
	}

	public br.com.qwcss.ws.PrivilegioDTO getPriv() {
		return priv;
	}

	public void setPriv(br.com.qwcss.ws.PrivilegioDTO priv) {
		this.priv = priv;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}