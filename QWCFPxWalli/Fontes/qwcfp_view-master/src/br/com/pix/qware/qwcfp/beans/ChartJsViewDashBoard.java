package br.com.pix.qware.qwcfp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import br.com.pix.qware.qwcfp.service.DashBoardService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.util.DateBoardFilter;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.StatusCounterConteinerDTO;
import br.com.qwcss.ws.dto.BoardStatusCounterDTO;
import br.com.qwcss.ws.dto.GroupDTO;

@ManagedBean(name = "ChartJsViewDashBoard")
@ViewScoped
public class ChartJsViewDashBoard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PieChartModel pieModel1;

	@Inject
	private InformationGroupService groupService;

	@Inject
	private DashBoardService service;

	private Integer groupId;

	private DateBoardFilter filter;

	// private Map<Integer,String> monthsCatalog;

	private String selectedMonth;
	
	@PostConstruct
	public void init() {
		filter = new DateBoardFilter();

		Integer groupId = (Integer) Util.getPropertySession("ID_GROUP");
		if (groupId != null) {
			GroupDTO dto = groupService.getGroupById(groupId);
			if (dto != null && dto.getErrorCode() == ViewError.OK.getCode()) {
				this.groupId = dto.getGroupId();
			}
		}

		createPieModels();
	}

	public PieChartModel getPieModel1() {
		return pieModel1;
	}

	
	public void createPieModels() {
		createPieModel1();
	}

	private void createPieModel1() {
		pieModel1 = new PieChartModel();
		
		liveModelPutData();

		pieModel1.setLegendPosition("w");
		pieModel1.setShadow(true);
		pieModel1.setShowDataLabels(true);
		pieModel1.setExtender("skinPie");
		
	}
	
	public void liveModelPutData() {
		Calendar dataInicio = Calendar.getInstance();
		Calendar dataFim = Calendar.getInstance();
		int year = dataInicio.get(Calendar.YEAR);
		Calendar selectedCalendar = this.selectedMonth == null ? Calendar.getInstance()
				: filter.findMonthChoice(this.selectedMonth);

		dataInicio.set(selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH),
				selectedCalendar.getActualMinimum(Calendar.DAY_OF_MONTH),
				selectedCalendar.getActualMinimum(Calendar.HOUR_OF_DAY),
				selectedCalendar.getActualMinimum(Calendar.MINUTE),
				selectedCalendar.getActualMinimum(Calendar.MILLISECOND));

		dataFim.set(selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH),
				selectedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH),
				selectedCalendar.getActualMaximum(Calendar.HOUR_OF_DAY),
				selectedCalendar.getActualMaximum(Calendar.MINUTE),
				selectedCalendar.getActualMaximum(Calendar.MILLISECOND));
		
		StatusCounterConteinerDTO dto = service.countStatusGroup(groupId, dataInicio, dataFim);

		BoardStatusCounterDTO[] counters = dto.getCounters();
		Map<String,Number> values = new HashMap<>();

		if (counters != null) {
			for (int i = 0; i < counters.length; i++) {
				BoardStatusCounterDTO boardStatusCounterDTO = counters[i];
				values.put(boardStatusCounterDTO.getStatus(), boardStatusCounterDTO.getCount());

			}
		}else {
			values.put("Não existem registros nesta data.", 0);
		}
		
		pieModel1.setData(values);
	}

	public String getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(String selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public DateBoardFilter getFilter() {
		return filter;
	}

	public void setFilter(DateBoardFilter filter) {
		this.filter = filter;
	}

}
