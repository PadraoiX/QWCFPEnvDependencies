package br.com.pix.qware.qwcfp.error;

public enum MsgTitleEnum {
	
	ERROR("Atenção!"),
	WARNING("Advertencia!"),
	SUCESS("Sucesso!")
	;
	
	private final String	msg;
	
	
	private MsgTitleEnum( String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return  msg;
	}

}
