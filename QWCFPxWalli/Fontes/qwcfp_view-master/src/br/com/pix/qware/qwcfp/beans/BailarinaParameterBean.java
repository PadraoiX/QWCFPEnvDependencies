package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;

import br.com.pix.qware.qwcfp.bailarina.client.ResponseString;
import br.com.pix.qware.qwcfp.bailarina.client.RestClient;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.wsproxy.dto.Operation;
import br.com.pix.qware.wsproxy.dto.Parameter;
import br.com.pix.qware.wsproxy.dto.WSDoc;

@Named("parameterBean")
@RequestScoped
public class BailarinaParameterBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051079019995847674L;

	@Inject
	FacesMessages messages;

	@Inject
	private qwcfpWServiceBean qwsBean;
	
	private List<Operation> operations;
	
	private WSDoc wsDoc;
	
	private Parameter parameter;
	
	private Operation operation;
	
	private String endpoint;

	private RestClient restClient;
	
	private String[] parameterTypes;

	@PostConstruct
	public void init() {
		
		parameterTypes = new String[] {"string","integer","boolean","object","array"};
		parameter = new Parameter();
		operation = new Operation();
		wsDoc = new WSDoc();
		restClient = new RestClient(); 

	}
	
	public void addParameter() {
		Gson gson = new Gson();
		if (wsDoc != null && parameter != null && this.operation != null) {
			Parameter par =  new Parameter();
			String body = null;
			if (wsDoc.getTipoServico().equals("Rest")) {
				body = gson.toJson(parameter);
			}else {
				messages.error("Operação permitida apenas para endpoints Rest");
				return;
			}
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_PAR_FORMAT, wsDoc.getIdService(),this.operation.getIdentificador()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient
						.body(body)
						.request(uriSuffix, RestClient.REQUEST_POST)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
				}else {
					messages.info("Sucesso!");
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
	
	public void removeParameter() {
		Gson gson = new Gson();
		if (wsDoc != null && parameter != null) {
			Parameter par =  new Parameter();
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_PAR1_FORMAT, wsDoc.getIdService(),this.operation.getIdentificador(),par.getIdentificador()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient
						.request(uriSuffix, RestClient.REQUEST_DELETE)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
				}else {
					messages.info("Sucesso!");
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
	
	public void editParameter() {
		
		Gson gson = new Gson();
		if (wsDoc != null && parameter != null) {
			String body = null;
			if (wsDoc.getTipoServico().equals("Rest")) {
				body = gson.toJson(parameter);
			}else {
				messages.error("Operação permitida apenas para endpoints Rest");
				return;
			}
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_PAR1_FORMAT, wsDoc.getIdService(),this.operation.getIdentificador(),parameter.getIdentificador()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient
						.body(body)
						.request(uriSuffix, RestClient.REQUEST_PUT)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
				}else {
					messages.info("Sucesso!");
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

	public FacesMessages getMessages() {
		return messages;
	}

	public void setMessages(FacesMessages messages) {
		this.messages = messages;
	}

	public qwcfpWServiceBean getQwsBean() {
		return qwsBean;
	}

	public void setQwsBean(qwcfpWServiceBean qwsBean) {
		this.qwsBean = qwsBean;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public WSDoc getWsDoc() {
		return wsDoc;
	}

	public void setWsDoc(WSDoc wsDoc) {
		this.wsDoc = wsDoc;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public RestClient getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	public String[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(String[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

}
