package br.com.pix.qware.qwcfp.service;

import java.util.ArrayList;
import java.util.List;

import br.com.pix.qwcfp.ws.client.manager.Rules;
import br.com.qwcss.ws.dto.RuleDTO;

public class RuleService extends Service {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	
	/**
	 * Inserir uma nova regra no sistema
	 * @param name Nome para a regra
	 * @param description Descrio da regra
	 * @param sourceRuleJexl Cdigo da regra
	 * @param ativa Regra ativa
	 * @param condition Tempo de execuo da regra, vide dominios
	 * @param String no formado JSON contendo os parmetros utilizados na regra EX: {"variavel1" : "valor1", "variavelN" : "valorN"}
	 * @return
	 */
	public  RuleDTO insert(String name, String description, String sourceRuleJexl, boolean ativa, String condition, String jsonStringParm){
		return Rules.insert(getConnectedUser(), name, description, sourceRuleJexl, ativa, condition, jsonStringParm);
	}
	
	
	/**
	 * Atualizar uma nova regra
	 * @param ruleId Identificador da regra
	 * @param name Nome da regra
	 * @param description Descrio da regra
	 * @param sourceRuleJexl Cdigo da regra
	 * @param ativa Regra ativa
	 * @param condition Tempo de execuo da regra
	 * @param String no formado JSON contendo os parmetros utilizados na regra EX: {"variavel1" : "valor1", "variavelN" : "valorN"}
	 * @return 0 caso sucesso
	 */
	public  RuleDTO update(Integer ruleId, String name, String description, String sourceRuleJexl, boolean ativa, String condition, String jsonStringParm){
		return Rules.update(getConnectedUser(), ruleId, name, description, sourceRuleJexl, ativa, condition, jsonStringParm);
	}

	/**
	 * Delerar uma regra
	 * @param ruleId Identificador da regra
	 * @return 0 caso sucesso
	 */
	public  RuleDTO delete(Integer ruleId){
		return Rules.delete(getConnectedUser(), ruleId);
	}
	
	/**
	 * Retornas as regras cadastradas no sistema
	 * @return Array com os detalhes das regras
	 */
	public  RuleDTO[] regras(){
		return br.com.pix.qwcfp.ws.client.list.Rules.regras(getConnectedUser());
	}
	
	/**
	 * Retornas as regras cadastradas no sistema
	 * @return Array com os detalhes das regras
	 */
	public  List<RuleDTO> listarRegras(){
		RuleDTO[] ruleDTO = br.com.pix.qwcfp.ws.client.list.Rules.regras(getConnectedUser());
		List<RuleDTO> listaRule = new ArrayList<RuleDTO>();
		if (ruleDTO != null) {
			for (int i = 0; i < ruleDTO.length; i++) {
				listaRule.add(ruleDTO[i]);
			}
		}
		return listaRule;
	}
	
	public RuleDTO listarRegraById(Integer id){
		return  br.com.pix.qwcfp.ws.client.list.Rules.listarRegraById(getConnectedUser(), id);
	}
	
	/**
	 * Retorna os detalhes de uma regra de acordo com o Identificador
	 * @param ruleId Identificador da regra
	 * @return Array contendo os detalhes das regras
	 */
	public  RuleDTO[] regras(Integer ruleId){
		return br.com.pix.qwcfp.ws.client.list.Rules.regras(getConnectedUser(), ruleId);
	}
		
}
