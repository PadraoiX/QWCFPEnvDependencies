package br.com.pix.qware.qwcfp.util;

import java.io.Serializable;


public class RelatorioModel  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		public String id;		
		public String nome;
		public String observacao;
		public String tipos;
		
		public RelatorioModel(String id, String nome, String observacao, String tipos){
			this.id = id;
			this.nome = nome;
			this.observacao = observacao;
			this.tipos = tipos;
		}
		

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getObservacao() {
			return observacao;
		}
		public void setObservacao(String observacao) {
			this.observacao = observacao;
		}
		public String getTipos() {
			return tipos;
		}
		public void setTipos(String tipos) {
			this.tipos = tipos;
		}
		
		@Override
	    public String toString() {
	        return "Relatorios{" + "nome=" + nome + ", observacao=" + observacao + ", tipos=" + tipos+'}';
	    }
		
		
		

}
