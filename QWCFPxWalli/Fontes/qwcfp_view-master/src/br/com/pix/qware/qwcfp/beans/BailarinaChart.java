package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.http.client.ClientProtocolException;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.PieChartModel;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import br.com.pix.qware.qwcfp.bailarina.client.ResponseString;
import br.com.pix.qware.qwcfp.bailarina.client.RestClient;
import br.com.pix.qware.wsproxy.dto.RecordDTO;
import br.com.pix.qware.wsproxy.dto.RecordDTOArray;
import br.com.pix.qware.wsproxy.dto.Statistic;
import br.com.pix.qware.wsproxy.dto.StatisticsDate;

@ManagedBean(name = "bailarinaChart")
@ViewScoped
public class BailarinaChart implements Serializable {

	private static final String ANO = "ano";
	private static final String MES = "mes";
	private static final String SEMANA = "semana";
	private static final String DIA = "dia";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PieChartModel pieModel1;
	
	private RestClient restClient; 
	
	private String selectedFilter;
	
	private Calendar selectedDate;
	
	private List<String> filterList;
	
	@PostConstruct
	public void init() {
		restClient = new RestClient();
		filterList = new ArrayList<>();
		filterList.add(DIA);
		filterList.add(MES);
		filterList.add(SEMANA);
		setSelectedDate(Calendar.getInstance());

		selectedFilter = DIA;
		
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
		pieModel1.setLegendRows(4);		
		pieModel1.setShadow(true);
		pieModel1.setShowDataLabels(true);
		pieModel1.setExtender("skinPie");
		pieModel1.setTitle("Hits por hora");
	}
	
	public void liveModelPutData() {
		
		try {
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_STATISTICS, getSelectedFilter() ).replace(" ", "%20");
			StatisticsDate dateFilter = new  StatisticsDate();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar modifiedCalendar = Calendar.getInstance();
			Gson gson = new Gson();
			
			
			String labelSufix = "";
			
			
			if (selectedFilter.equals(DIA)) {
				modifiedCalendar.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
						selectedDate.get(Calendar.DAY_OF_MONTH),
						selectedDate.getActualMinimum(Calendar.HOUR_OF_DAY),
						selectedDate.getActualMinimum(Calendar.MINUTE),
						selectedDate.getActualMinimum(Calendar.MILLISECOND));
				
				labelSufix = ":00 Hrs";
				
			}else if (selectedFilter.equals(SEMANA)) {
				modifiedCalendar.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
						selectedDate.getActualMinimum(Calendar.DAY_OF_WEEK),
						selectedDate.getActualMinimum(Calendar.HOUR_OF_DAY),
						selectedDate.getActualMinimum(Calendar.MINUTE),
						selectedDate.getActualMinimum(Calendar.MILLISECOND));
				
			}else if (selectedFilter.equals(MES)) {
				modifiedCalendar.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
						selectedDate.getActualMinimum(Calendar.DAY_OF_MONTH),
						selectedDate.getActualMinimum(Calendar.HOUR_OF_DAY),
						selectedDate.getActualMinimum(Calendar.MINUTE),
						selectedDate.getActualMinimum(Calendar.MILLISECOND));
				
			}else if (selectedFilter.equals(ANO)) {
				modifiedCalendar.set(selectedDate.get(Calendar.YEAR), selectedDate.getActualMinimum(Calendar.MONTH),
						selectedDate.getActualMinimum(Calendar.DAY_OF_MONTH),
						selectedDate.getActualMinimum(Calendar.HOUR_OF_DAY),
						selectedDate.getActualMinimum(Calendar.MINUTE),
						selectedDate.getActualMinimum(Calendar.MILLISECOND));
				
			}
			
			Date date = modifiedCalendar.getTime();
			String string = df.format(date);
			dateFilter.setTarget_date(string);
			
			ResponseString responseString = restClient.body(gson.toJson(dateFilter))
					.request(uriSuffix, RestClient.REQUEST_GET)
					.execute()
					.getResponseString();
			
			if (responseString.getStatusCode() == 200) {
				
				Type endpointKind = new TypeToken<RecordDTOArray<Statistic>>() {}.getType();
				RecordDTOArray<Statistic> record = responseString.toGson(endpointKind);
				Map<String,Number> values = new HashMap<>();
				
				if (record != null) {
					Statistic[] data =  record.getMessage();
					
					if (data != null) {
						
						for (int i = 0; i < data.length; i++) {
							if (values.containsKey(data[i].getUnidade())) {
								Integer storedValue = 0;
								storedValue = values.get(data[i].getUnidade()).intValue();
								storedValue += Integer.parseInt(data[i].getHits()) ; 
								values.put(data[i].getUnidade() + labelSufix, storedValue);
							}else {
								values.put(data[i].getUnidade() + labelSufix,  Integer.parseInt(data[i].getHits()));
							}
						}
					}
					
				}
				pieModel1.setData(values);
			}
			
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RestClient getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	public List<String> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<String> filterList) {
		this.filterList = filterList;
	}

	public String getSelectedFilter() {
		return selectedFilter;
	}

	public void setSelectedFilter(String selectedFilter) {
		this.selectedFilter = selectedFilter;
	}

	public Calendar getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Calendar selectedDate) {
		this.selectedDate = selectedDate;
	}

}
