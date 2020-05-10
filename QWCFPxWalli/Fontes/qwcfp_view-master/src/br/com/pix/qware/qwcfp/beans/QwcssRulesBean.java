package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.extensions.event.CompleteEvent;

import br.com.pix.qware.qwcfp.service.DomainService;
import br.com.pix.qware.qwcfp.service.RuleService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.AssignedRuleDTO;
import br.com.qwcss.ws.dto.DomainDTO;
import br.com.qwcss.ws.dto.RuleDTO;

//@Named("rulesBean")
@ManagedBean(name = "rulesBean")
@ViewScoped
public class QwcssRulesBean extends AbstractBean {

	private static final long serialVersionUID = 2158419734603479904L;

	@Inject
	private RuleService ruleService;

	@Inject
	private DomainService domainService;

	@Inject
	private FacesMessages messages;

	private String newRuleCondition;

	private String newRuleStatus;

	private RuleDTO rule;
	
	private List<RuleDTO> rules;

	private AssignedRuleDTO assigned;

	private static final String STATUS_DOMAIN_NAME = "ASSINED_RULE";
	private static final String STATUS_DOMAIN_SV = "ATIVO";

	private DomainDTO domainStatus;

	private boolean disable;


	@PostConstruct
	public void init() {
		domainStatus = domainService.returnDomain(STATUS_DOMAIN_NAME, STATUS_DOMAIN_SV);
		rule = new RuleDTO();
		this.disable = true;
		updateRules();
	}
	

	public void updateRules() {
		RuleDTO[] rules = ruleService.regras();
		if (rules != null && rules.length > 0)
			this.rules = Arrays.asList(rules);
		else
			this.rules = null;
	}

	public String inserir() {

		try {

			DomainDTO domainCondition = domainService.returnDomain(rule.getCondition());

			if (domainStatus.getErrorCode() != ViewError.OK.getCode()) {
				messages.add(domainStatus.getErrorCode() + ": " + domainStatus.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
				return null;
			}
			if (domainCondition.getErrorCode() != ViewError.OK.getCode()) {
				messages.add(domainCondition.getErrorCode() + ": " + domainCondition.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
				return null;
			}

			boolean ativo = rule.getStatus().compareTo(domainStatus.getId()) == 0 ? true : false;

			RuleDTO ret = ruleService.insert(rule.getName(), rule.getDescription(), rule.getSourceRuleJexl(), ativo,
					domainCondition.getStringValue(), rule.getJsonStringParm());

			if (ret.getErrorCode().compareTo(ViewError.OK.getCode()) != 0) {
				messages.add(ret.getErrorCode() + ": " + ret.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
				return null;
			} else {
				messages.add(ViewError.RULE_INSERT_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);
				FacesContext facesContext = FacesContext.getCurrentInstance();
				facesContext.getExternalContext().getFlash().setKeepMessages(true);
				facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "listRegras");
				return "listRegras.faces?redirect=true";

			}

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.RULE_INSERT_ERROR.getCode() + ": " + ViewError.RULE_INSERT_ERROR.getMsg(),
					FacesMessage.SEVERITY_ERROR);
			return null;
		}

	}

	public void editar() {

		try {

			DomainDTO domainCondition = domainService.returnDomain(rule.getCondition());

			if (domainStatus.getErrorCode() != ViewError.OK.getCode()) {
				messages.add(domainStatus.getErrorCode() + ": " + domainStatus.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
			}
			if (domainCondition.getErrorCode() != ViewError.OK.getCode()) {
				messages.add(domainCondition.getErrorCode() + ": " + domainCondition.getErrorMsg(),
						FacesMessage.SEVERITY_ERROR);
			}
			boolean ativo = rule.getStatus().compareTo(domainStatus.getId()) == 0 ? true : false;
			RuleDTO ret = ruleService.update(rule.getRuleID(), rule.getName(), rule.getDescription(),
					rule.getSourceRuleJexl(), ativo, domainCondition.getStringValue(), rule.getJsonStringParm());

			if (ret.getErrorCode().compareTo(ViewError.OK.getCode()) != 0){
				messages.add(ret.getErrorCode() + ": " + ret.getErrorMsg(), FacesMessage.SEVERITY_ERROR);
			}else
				messages.add(ViewError.RULE_UPDATE_SUCESS.getMsg(), FacesMessage.SEVERITY_INFO);

		} catch (Exception e) {
			e.printStackTrace();
			messages.add(ViewError.RULE_UPDATE_ERROR.getCode() + ": " + ViewError.RULE_UPDATE_ERROR.getMsg(),
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public String getRuleDomain(Integer id) {

		return domainService.returnDomain(id).getAbbreveation();
	}

	public DomainDTO[] getDomainList(int option) {

		DomainDTO[] statusList = null;

		switch (option) {
		case 0:
			statusList = domainService.returnDomain("ASSINED_RULE");
			break;

		case 1:
			statusList = domainService.returnDomain("RULES");
			break;

		default:
			return null;
		}
		return statusList;
	}

	public List<RuleDTO> getRules() {
		return rules;

	}

	public String getNewRuleCondition() {
		return newRuleCondition;
	}

	public void setNewRuleCondition(String newRuleCondition) {
		this.newRuleCondition = newRuleCondition;
	}

	public String getNewRuleStatus() {
		return newRuleStatus;
	}

	public void setNewRuleStatus(String newRuleStatus) {
		this.newRuleStatus = newRuleStatus;
	}

	public RuleDTO getRule() {
		return rule;
	}

	public void setRule(RuleDTO rule) {
		this.rule = rule;
	}

	public AssignedRuleDTO getAssigned() {
		return assigned;
	}

	public void setAssigned(AssignedRuleDTO assigned) {
		this.assigned = assigned;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

}
