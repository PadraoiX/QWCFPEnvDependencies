package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.client.ClientProtocolException;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.pix.qware.qwcfp.bailarina.client.ResponseString;
import br.com.pix.qware.qwcfp.bailarina.client.RestClient;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.wsproxy.dto.Operation;
import br.com.pix.qware.wsproxy.dto.Parameter;
import br.com.pix.qware.wsproxy.dto.ParameterOpTest;
import br.com.pix.qware.wsproxy.dto.WSDoc;

@Named("testOperationBean")
@RequestScoped
public class BailarinaTestOPBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051079019995847674L;

	@Inject
	FacesMessages messages;
	

	private List<ParameterOpTest> tableContent;
	private List<Parameter> parameters;
	
	private Operation operation;
	private WSDoc wsDoc;
	
	private String codeContent;
	
	private String response;
	
	private RestClient restClient;

	private ParameterOpTest tempValue;

	@PostConstruct
	public void init() {
		qwcfpWServiceBean qwcfpWServiceBean = FacesContext.getCurrentInstance().getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{qwcfpWServiceBean}",
						qwcfpWServiceBean.class);
		
		if (qwcfpWServiceBean != null) {
			parameters =  qwcfpWServiceBean.getParameters();
			wsDoc = qwcfpWServiceBean.getSelectedUrl();
			operation = qwcfpWServiceBean.getSelectedOperation();
			if (parameters != null) {
				
				codeContent = "{";
				int i = 1;
				for (Parameter parameter : parameters) {
					codeContent += String.format("\"%s\": \"%s\"", parameter.getIdentificador(),parameter.getTipo());
					if (i < parameters.size()) {
						codeContent += ",";
					}
					i++;
				}
				codeContent += "}";
				JsonParser parser = new JsonParser();
			    JsonObject json = parser.parse(codeContent).getAsJsonObject();

			    Gson gson = new GsonBuilder().setPrettyPrinting().create();
			    codeContent= gson.toJson(json);
				
			}else{
				codeContent = "";
			}
		} 
		
		
		
		tableContent = new ArrayList<ParameterOpTest>();
		restClient = new RestClient(); 
		fillTable();
	}
	
	public void fillTable() {
		fillTable(parameters);
	}
	
	public void fillTable(List<Parameter> pars) {
		if (pars != null) {
			for (Parameter parameter : pars) {
				ParameterOpTest obj = new ParameterOpTest();
				
				obj.setIdentificador(parameter.getIdentificador());
				obj.setValue(parameter.getTipo());
				tableContent.add(obj);
			}
		}
	}
	
	public void onEditCell(CellEditEvent event) {
		
        try {
        	for (Iterator iterator = tableContent.iterator(); iterator.hasNext();) {
        		ParameterOpTest parameter = (ParameterOpTest) iterator.next();
        		if (parameter.getIdentificador().equals(event.getRowKey())) {
        			tempValue = parameter;
        			break;
        		}
        		
        	}
		} catch (Exception e) {
		}
	}
	
	public void onValueEditCell(ValueChangeEvent event) {
		 
		 try {
			 tempValue.setValue((String)event.getNewValue());
			
		} catch (Exception e) {
		}
	}
	
	
	public void test() {
		
		if (wsDoc != null) {
			Gson gson = new Gson();
			
			/*HashMap hmap = new HashMap<String,String>(); 
			for (ParameterOpTest parameter : tableContent) {
				hmap.put(parameter.getIdentificador(), parameter.getValue());
			}*/
			String body = codeContent;
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_TEST_OPERATION, wsDoc.getIdService(),this.operation.getIdentificador()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient.body(body)
						.request(uriSuffix, RestClient.REQUEST_POST)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
				}else {
					messages.info("Sucesso!");
					JsonParser parser = new JsonParser();
				    JsonObject json = parser.parse(responseString.toString()).getAsJsonObject();

				    gson = new GsonBuilder().setPrettyPrinting().create();
					setResponse(gson.toJson(json));
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


	public WSDoc getWsDoc() {
		return wsDoc;
	}


	public void setWsDoc(WSDoc wsDoc) {
		this.wsDoc = wsDoc;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<ParameterOpTest> getTableContent() {
		return tableContent;
	}

	public void setTableContent(List<ParameterOpTest> tableContent) {
		this.tableContent = tableContent;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public String getCodeContent() {
		return codeContent;
	}

	public void setCodeContent(String codeContent) {
		this.codeContent = codeContent;
	}

}
