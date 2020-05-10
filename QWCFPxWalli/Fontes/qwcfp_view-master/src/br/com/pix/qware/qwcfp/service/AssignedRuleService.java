package br.com.pix.qware.qwcfp.service;

import java.util.List;

import javax.inject.Inject;

import br.com.pix.qwcfp.ws.client.manager.AssignedRule;
import br.com.pix.qwcfp.ws.client.manager.Rules;
import br.com.qwcss.ws.dto.AssignedRuleDTO;

public class AssignedRuleService extends Service {
	
	
	/**
	 * Insere uma nova AssignedRule no banco de dados
	 * @param ruleId 
	 * @param groupId 
	 * @param jsonStringParm 
	 * @param assignedRule area a ser inserido
	 * @return 
	 * @throws ServiceException
	 */
	public AssignedRuleDTO vincularRegra(Integer ruleId, Integer groupId, String jsonStringParm) {
		try {
			
			AssignedRuleDTO ret = AssignedRule.vincularRegra(getConnectedUser(), ruleId, groupId, jsonStringParm);
		
			return ret;
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * Exclui uma AssignedRule do banco de dados
	 * @param assignedId 
	 * @param Long assignedRuleId
	 * @return 
	 * @throws ServiceException
	 */
	public AssignedRuleDTO desvincularRegla(Integer assignedId) {
		try {
			
			AssignedRuleDTO ret = AssignedRule.desvincularRegra(getConnectedUser(), assignedId);
			
			return ret;
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * L todas as Assigned Rules cadastradas no banco de dados
	 * @return Lista de AssignedRule cadastradas
	 * @throws ServiceException
	 */
	public AssignedRuleDTO[] listarAssignedRule(Integer groupId) {
		return br.com.pix.qwcfp.ws.client.list.AssignedRule.regras(getConnectedUser(),groupId);
	}
	

	/**
	 * Chamada do mtodo do webService para 
	 * subir uma regra num determinado grupo
	 * @param assignedId
	 * @return
	 */
	public AssignedRuleDTO subirRegra(Integer assignedId){
		return AssignedRule.subirRegra(getConnectedUser(), assignedId);
	}
	
	/**
	 * Chamada do mtodo do webService para 
	 * subir uma regra num determinado grupo
	 * @param assignedId
	 * @return
	 */
	public AssignedRuleDTO descerRegre(Integer assignedId){
		return AssignedRule.descerRegra(getConnectedUser(), assignedId);
	}
}
