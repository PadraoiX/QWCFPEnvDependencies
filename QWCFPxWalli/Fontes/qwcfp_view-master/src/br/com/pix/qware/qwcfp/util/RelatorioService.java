package br.com.pix.qware.qwcfp.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("relatorioService")
@RequestScoped
public class RelatorioService implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final ArrayList<Order> orderList =  new ArrayList<Order>(Arrays.asList(
		
		new Order("Ocupação de Espaço", 		   "Area"),
		new Order("Arquivos por Faixa", 		   "ArquivosFaixa"),
		new Order("Tamanho de Espaço por Área",    "AreaGraficoTam"),
		new Order("Tamanho de Espaço por Grupo",   "GrupoGraficoTam"),
		new Order("Relatório de Espaço por Grupo", "GruposRelatorio"),
		new Order("Usuários do Sistema", 		   "Usuarios")
	));
	 
	public String rebebe(String option){
		
		
		return option;
	}	
	
	public ArrayList<Order> getOrderList() {
		return orderList;
 
	}
 
	public static class Order{
		
		String relatorio;	
		String obs;

		public Order(String relatorio, String obs) {
			this.relatorio = relatorio;
			this.obs = obs;
			
		}

		public String getRelatorio() {
			return relatorio;
		}

		public void setRelatorio(String relatorio) {
			this.relatorio = relatorio;
		}

		public String getObs() {
			return obs;
		}

		public void setObs(String obs) {
			this.obs = obs;
		}
		
		
		
	}
}
