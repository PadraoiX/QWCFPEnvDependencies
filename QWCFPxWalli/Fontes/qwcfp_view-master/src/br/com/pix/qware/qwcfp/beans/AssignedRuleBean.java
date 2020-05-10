package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import br.com.pix.qware.qwcfp.service.AssignedRuleService;
import br.com.pix.qware.qwcfp.service.RuleService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.qwcss.ws.dto.AssignedRuleDTO;
import br.com.qwcss.ws.dto.RuleDTO;

@Named("assignedBean")
@RequestScoped
public class AssignedRuleBean extends AbstractBean {

	private static final long		serialVersionUID	= 821582120381562246L;

	@Inject
	private RuleService				ruleService;

	@Inject
	private AssignedRuleService		assignedService;
	
	@Inject
	private FacesMessages			messages;

	private DualListModel<RuleDTO>	listModelRules;

	private Integer					selectedGrpId;
	
	private RuleDTO					ruleSelected;
	
	@Override
	public void init() {
		super.init();
		selectedGrpId = (Integer) Util.getPropertySession("ID_GROUP");
		
		if (selectedGrpId != null && selectedGrpId != 0) {
			
			List<RuleDTO> source = ruleService.listarRegras();
			AssignedRuleDTO[] listaAssigned = assignedService.listarAssignedRule(getSelectedGrpId());
			
			
			List<RuleDTO> target = new ArrayList<RuleDTO>();

			if (listaAssigned != null) {
				for (int i = (listaAssigned.length - 1); i >= 0; i--) {//loop invertido, pois o webservice retorna a ordem do maior para o menor e o pick list insere por baixo.
					RuleDTO ruleDTO = ruleService.listarRegraById(listaAssigned[i].getRuleId());
					target.add(ruleDTO);
				}
			}

			source.removeAll(target);

			listModelRules = new DualListModel<RuleDTO>(source, target);
			
		}else{
			List<RuleDTO> source =  new ArrayList<RuleDTO>();
			List<RuleDTO> target =  new ArrayList<RuleDTO>();
			listModelRules = new DualListModel<RuleDTO>(source, target);
		}

	}
	
	public DualListModel<RuleDTO> getListModelRules() {
		return listModelRules;
	}

	public void setListModelRules(DualListModel<RuleDTO> listModelRules) {
		this.listModelRules = listModelRules;
	}

	public void onTransfer(TransferEvent event) {

		String title = "";
		String msg = "";
		String msgError = "";

		@SuppressWarnings("unchecked")
		List<RuleDTO> selectedList = (List<RuleDTO>) event.getItems();
		AssignedRuleDTO[] listaAssigned = null;

		if (event.isAdd()) {
			title = "Associado(s)";
		} else if (event.isRemove()) {
			listaAssigned = assignedService.listarAssignedRule(getSelectedGrpId());
			title = "Desassociado(s)";
		}

		for (RuleDTO ruleDTO : selectedList) {
			if (event.isAdd()) {
				AssignedRuleDTO ret = assignedService.vincularRegra(ruleDTO.getRuleID(),
						getSelectedGrpId(),
						ruleDTO.getJsonStringParm());
				
				if (ret.getErrorCode() != 0) {
					msgError = msgError + ruleDTO.getName() + "(" + ret.getErrorMsg() +")" + " ;";
				}else{
					msg = msg + ruleDTO.getName() + ";";
				}
			} else if (event.isRemove()) {
				for (AssignedRuleDTO assignedRuleDTO : listaAssigned) {
					if (assignedRuleDTO.getRuleId().compareTo(ruleDTO.getRuleID()) == 0) {

						AssignedRuleDTO ret = assignedService.desvincularRegla(assignedRuleDTO.getAssignedRuleId());
						
						if (ret.getErrorCode() != 0) {
							msgError = msgError + ruleDTO.getName() + "(" + ret.getErrorMsg() +")" + " ;";
						}else{
							msg = msg + ruleDTO.getName() + ";";
						}
						break;
					}
				}
			}
		}

		if (!msgError.isEmpty()) {
			messages.error(msgError);
		}else if (!msg.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(title, msg));
		}
	}

	public void onSelect(SelectEvent event) {
		//TODO
		RuleDTO ruleSelected = (RuleDTO) event.getObject();
		
		AssignedRuleDTO[] listaAssigned = assignedService.listarAssignedRule(getSelectedGrpId());
		
		if( listaAssigned != null && listaAssigned.length > 0){
			for (AssignedRuleDTO assignedRuleDTO : listaAssigned) {
				if (assignedRuleDTO.getRuleId().equals(ruleSelected.getRuleID())){
					ruleSelected.setJsonStringParm(assignedRuleDTO.getJsonStringParm());
					break;
				}
			}
		}
		
		this.ruleSelected = ruleSelected;
	}

	public void onUnselect(UnselectEvent event) {

	}

	public void onReorder() {

		AssignedRuleDTO[] listAssigned = assignedService.listarAssignedRule(getSelectedGrpId());// regras associadas a um grupo
		List<RuleDTO> listTarget = listModelRules.getTarget();// as regras pick associadas a um grupo no pick list.
		
		if (listAssigned != null) {
			int[] movesCountMap = new int[listAssigned.length];
			
			for (int index = (listAssigned.length - 1); index >= 0; index--) {//loop invertido, pois o webservice retorna a ordem do maior para o menor
				
				RuleDTO ruleDTO = ruleService.listarRegraById(listAssigned[index].getRuleId());
				Integer targetRuleIndex = listTarget.indexOf(ruleDTO); // posição atual da regra
				
				if (targetRuleIndex > -1) {
					int movesCount = 0;
					Integer updatedIndex = listAssigned.length - (targetRuleIndex + 1); // posição atual da regra (invertida)
					movesCount = index - updatedIndex; // diferença de posição
					
					movesCountMap[index] = movesCount; //mapeia os movimentos
					
				}
			}
			
			for (int index = (listAssigned.length - 1); index >= 0; index--) {//reordenando cada AssignedRule
				
				int movesCount = movesCountMap[index];
				int looper = movesCount > 0 ? movesCount : movesCount * (-1);
				int nextMovesCount = 0;
				
				for (int i = 0; i < looper; i++) {// loop de movimento.
					if (movesCount > 0) {// se positivo desce a regra.
						
						int nextMapPos = index - (i + 1);
						
						if (nextMapPos >= 0 && nextMapPos < listAssigned.length) {
							nextMovesCount = movesCountMap[nextMapPos];
							
							if (nextMovesCount != 0)
								movesCountMap[nextMapPos] = nextMovesCount + 1;
							
						}
						
						assignedService.descerRegre(listAssigned[index].getAssignedRuleId());
						
					} else if (movesCount < 0) {// se negativo sobe a regra.
						
						int nextMapPos = index + (i + 1);
						
						if (nextMapPos >= 0 && nextMapPos < listAssigned.length) {
							nextMovesCount = movesCountMap[nextMapPos];
							
							if (nextMovesCount != 0)
								movesCountMap[nextMapPos] = nextMovesCount - 1;
							
						}
						
						assignedService.subirRegra(listAssigned[index].getAssignedRuleId());
					}
				}
			}
		}
	}

	public Integer getSelectedGrpId() {
		return selectedGrpId;
	}

	public void setSelectedGrpId(Integer selectedGrpId) {
		this.selectedGrpId = selectedGrpId;
	}

	public RuleDTO getRuleSelected() {
		return ruleSelected;
	}

	public void setRuleSelected(RuleDTO ruleSelected) {
		this.ruleSelected = ruleSelected;
	}

}
