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
import br.com.pix.qware.qwcfp.service.AreaService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.wsproxy.dto.Operation;
import br.com.pix.qware.wsproxy.dto.OperationPost;
import br.com.pix.qware.wsproxy.dto.WSDoc;

@Named("operationBean")
@RequestScoped
public class BailarinaOperationBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051079019995847674L;

	@Inject
	private AreaService areaService;

	@Inject
	FacesMessages messages;

	@Inject
	private qwcfpWServiceBean qwsBean;
	
	private List<Operation> operations;
	
	private WSDoc wsDoc;
	
	private OperationPost operation;
	
	private String operationId;
	
	private String operationDesc;
	
	private RestClient restClient;
	
	private String endpoint;

	private String description;

	private Integer id;

	private String method;

	@PostConstruct
	public void init() {
		
		operation = new OperationPost();
		wsDoc = new WSDoc();
		restClient = new RestClient(); 
		/*if (qwsBean != null && qwsBean.getSelectedUrl() != null) {
			
			endpoint = qwsBean.getSelectedUrl().getIdService();
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_OP_FORMAT, endpoint);
			
			restClient = new RestClient(); 
			
			try {
				ResponseString responseString = restClient.request(uriSuffix , RestClient.REQUEST_GET)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
				}else {
					
					Map map = responseString.toGson(Map.class);
					IOperation op = null;
					if (map.containsKey("operations.identificador")) {
						op = responseString.toGson(OperationRest.class);
					}else {
						op = responseString.toGson(OperationWsdl.class);
					}
					
					try {
						setOperations(op.getOperationList());
					} catch (Exception e) {
						setOperations(new ArrayList<Operation>());
					}
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			setOperations(new ArrayList<Operation>());
		}*/

	}
	
	public void addOperation() {
		Gson gson = new Gson();
		if (wsDoc != null) {
			OperationPost operation =  new OperationPost();
			String body = null;
			if (wsDoc.getTipoServico().equals("Rest")) {
				body = gson.toJson(this.operation);
			}else {
				body = gson.toJson(this.operation.getIdentificador());
			}
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_OP1_FORMAT, wsDoc.getIdService(),this.operation.getIdentificador()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient.body(body)
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
		this.operation = new OperationPost();
	}
	
	public void removeOperation() {
		if (operation != null && wsDoc != null) {
			Gson gson = new Gson();
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_OP1_FORMAT,wsDoc.getIdService(),operation.getIdentificador()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient
						.request(uriSuffix, RestClient.REQUEST_DELETE)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
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
	
	public void editOperation() {
		Gson gson = new Gson();
		if (wsDoc != null) {
			String body = null;
			if (wsDoc.getTipoServico().equals("Rest")) {
				body = gson.toJson(this.operation);
			}else {
				body = gson.toJson(this.operation.getIdentificador());
			}
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_OP1_FORMAT, wsDoc.getIdService(),this.operation.getIdentificador()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient.body(body)
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
		
		this.operation = new OperationPost();
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public OperationPost getOperation() {
		return operation;
	}

	public void setOperation(OperationPost operation) {
		this.operation = operation;
	}


}
