 package br.com.pix.qware.qwcfp.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.http.client.ClientProtocolException;
import org.primefaces.model.DualListModel;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Version;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

import br.com.pix.qware.qwcfp.bailarina.client.ResponseString;
import br.com.pix.qware.qwcfp.bailarina.client.RestClient;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.wsproxy.dto.Endpoint;
import br.com.pix.qware.wsproxy.dto.EndpointDTO;
import br.com.pix.qware.wsproxy.dto.EndpointList;
import br.com.pix.qware.wsproxy.dto.EndpointShort;
import br.com.pix.qware.wsproxy.dto.IOperation;
import br.com.pix.qware.wsproxy.dto.Operation;
import br.com.pix.qware.wsproxy.dto.OperationPost;
import br.com.pix.qware.wsproxy.dto.OperationRest;
import br.com.pix.qware.wsproxy.dto.OperationWsdl;
import br.com.pix.qware.wsproxy.dto.Parameter;
import br.com.pix.qware.wsproxy.dto.ParametersDTO;
import br.com.pix.qware.wsproxy.dto.ParametersDTOGet;
import br.com.pix.qware.wsproxy.dto.Parametros;
import br.com.pix.qware.wsproxy.dto.RecordDTO;
import br.com.pix.qware.wsproxy.dto.RecordDTOOp;
import br.com.pix.qware.wsproxy.dto.WSDoc;
import br.com.qwcss.ws.dto.MemberDTO;

/**
 * <p>
 * Bean usado para conter a lista de objeos de URLs usadas como cadastro de
 * serviços Web Service registrados pela interface do QWCFP.
 * </p>
 * 
 * <p>
 * Este bean requer alteração do arquivo de configuração do QWCFP, situado no
 * JBOSS_HOME/<i><b>qwcfpDocs/qwcfp.properties</b></i> (p.e.:
 * D:\Bin\JBoss6.1EAP\jboss-eap-6.1\qwcfpDocs), para conter uma property
 * {@link QW2SG.dir} que aponta para localização do arquivo XML de cadastro das
 * URIs dos Web Services, por exemplo: <b><i>QW2SG.dir</i></b>
 * =d:\\TEMP\\QW2SG_SERVCES_CATALOG.xml
 * </p>
 * 
 * @author Anderson e Ivan
 *
 */
@Root(name = "qwcfpWServiceBean")
@Version(revision = 1.0, name = "Versao", required = false)
@ManagedBean(name = "qwcfpWServiceBean")
@ViewScoped
public class qwcfpWServiceBean extends AbstractBean implements RTCTS4Qw2sg {

	private static final long serialVersionUID = 2408795418711028370L;

	@Inject
	private FacesMessages	messages;
	
	@Inject
	private MemberService	memberService;

	@ElementList(name = "WSDoc", required = false, empty = true, inline = true, type = WSDoc.class)
	private List<WSDoc> listWsdlDoc = null;
	private Hashtable<String, WSDoc> keysearch = null;
	private Integer ativado = null;
	private boolean disable = false;
	private WSDoc selectedUrl;
	private WSDoc newdUrl;
	private List<WSDoc> selectdUrls;
	private DualListModel<MemberDTO>  moListUserPrivs;
	
	private List<Operation> operations;
	private List<Parameter> parameters;
	
	private String tipoServico;
	private String usaCertificado;
	
	private boolean restful;
	
	private Date created_at;
	
	private RestClient restClient; 
	
	
	private List<Parametros> parametros;
	private List<Parametros> parametrosEdicao;

	private Operation selectedOperation;
	private OperationPost detailedOperation;
	private Parameter selectedParameter;
	private Parameter detailedParameter;
	
	private String endProxy;
	
	
	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateFormat(date);
	}
	
	
	
	private String dateFormat(Date date) {
		// TODO Auto-generated method stub
		return null;
	}



	public void buildDualList() {
		List<MemberDTO> source = memberService.listar() != null ? Arrays.asList(memberService.listar()) : new ArrayList<MemberDTO>();
		List<MemberDTO> target = new ArrayList<MemberDTO>();
		
		setMoListUserPrivs(new DualListModel<MemberDTO>(source, target));
	}
	
	public void adicionarPrivilegio(){
		
		List<MemberDTO> listTargetUser = moListUserPrivs.getTarget();
		
		List<String> list = new ArrayList<>();
		
		for (MemberDTO memberDTO : listTargetUser) {//1|98765432100|infra@pix.com.br
			list.add(String.format("%d|%s|%s", memberDTO.getMemberId(), memberDTO.getLoginCpfId(), memberDTO.getMemberEmail() ));
		}
		
		if (selectedUrl != null) {
			selectedUrl.setUserPriv(list);
		}
		
		editURL();
		
	}

	public void onChangeUrl(){
		if (selectedUrl != null) {
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT, selectedUrl.getIdService()).replace(" ", "%20");
			
			try {
				ResponseString responseString = restClient.request(uriSuffix, RestClient.REQUEST_GET)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() == 200) {
					
					Type endpointKind = new TypeToken<RecordDTO<EndpointDTO>>() {}.getType();
					RecordDTO<EndpointDTO> record = responseString.toGson(endpointKind);
					EndpointDTO data = (EndpointDTO) record.getData();
					WSDoc wsDoc = new WSDoc();
					Endpoint endpoint = null;
					
					if (data != null ) {
						endpoint = data.getEndpoint();
					}else {
						endpoint = new Endpoint();
					}
					
					wsDoc.setWsId(endpoint.getId());					
					wsDoc.setIdService(endpoint.getIdentificador());
					wsDoc.setTipoAutorizacao(endpoint.getTipo_autorizacao());
					wsDoc.setDescription(endpoint.getDescricao());
					wsDoc.setTipoServico(endpoint.getTipo_servico());
					usaCertificado = endpoint.getUsa_certificado().toString();
					tipoServico = endpoint.getTipo_servico();
					wsDoc.setWSDLUrl(endpoint.getEndpoint());
					
					endProxy = restClient.getserverUri() + uriSuffix;
					
					if (tipoServico.equals("Rest")) {
						restful = true;
					}else {
						restful = false;
					}
					newdUrl = wsDoc;
					
					updateOperations();
					
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
	
	public void onChangeOperation(){
		if (selectedOperation != null) {
			try {
				
				String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_OP1_FORMAT, selectedUrl.getIdService(),selectedOperation.getIdentificador()).replace(" ", "%20");
				ResponseString responseString = restClient.request(uriSuffix, RestClient.REQUEST_GET)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() == 200) {
					
					Type endpointKind = new TypeToken<RecordDTO<RecordDTOOp>>() {}.getType();
					RecordDTO<RecordDTOOp> record = responseString.toGson(endpointKind);
					RecordDTOOp data = (RecordDTOOp) record.getData();
					
					detailedOperation = data.getService();
					updateParams();
					
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
	
	public void onChangeParameter(){
		if (selectedOperation != null && selectedParameter != null) {
			try {
				
				String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_PAR1_FORMAT, selectedUrl.getIdService(),selectedOperation.getIdentificador(),selectedParameter.getIdentificador()).replace(" ", "%20");
				ResponseString responseString = restClient.request(uriSuffix, RestClient.REQUEST_GET)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() == 200) {
					
					Type endpointKind = new TypeToken<RecordDTO<ParametersDTOGet>>() {}.getType();
					RecordDTO<ParametersDTOGet> record = responseString.toGson(endpointKind);
					ParametersDTOGet recordPar = record.getData();
					
					detailedParameter =  recordPar.getService();
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
	
	public void updateOperations() {
		
		if (selectedUrl != null && selectedUrl != null) {
			
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_OP_FORMAT, selectedUrl.getIdService());
			
			restClient = new RestClient(); 
			
			try {
				ResponseString responseString = restClient.request(uriSuffix , RestClient.REQUEST_GET)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
				}else {
					
					Type operationKind = new TypeToken<RecordDTO<OperationRest>>() {}.getType();
					RecordDTO<OperationRest> record = responseString.toGson(operationKind);
					OperationRest data = (OperationRest) record.getData();
					
					
					try {
						setOperations(data.getOperationList());
					} catch (Exception e) {
						setOperations(new ArrayList<Operation>());
					}
				}
				
			} catch (Exception e) {
				setOperations(new ArrayList<Operation>());
			} 
		}else {
			setOperations(new ArrayList<Operation>());
		}
		
	}
	
	public void updateParams() {
		
		if (selectedOperation != null && selectedUrl != null) {
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT_PAR_FORMAT, selectedUrl.getIdService(),selectedOperation.getIdentificador());
			
			restClient = new RestClient(); 
			
			try {
				ResponseString responseString = restClient.request(uriSuffix , RestClient.REQUEST_GET)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200) {
					messages.error(responseString.getMessage());
					parameters = new ArrayList<>();
				}else {
					
					Type parameterKind = new TypeToken<RecordDTO<ParametersDTO>>() {}.getType();
					RecordDTO<ParametersDTO> record = responseString.toGson(parameterKind);
					ParametersDTO data = (ParametersDTO) record.getData();
					
					parameters = data.getParameters();
					
				}
			}catch(Exception e){
				parameters = new ArrayList<>();
			}
		}
	}
	
	public void cleanParams() {
		parameters = new ArrayList<>();
		selectedOperation = new Operation();
	}



	@PostConstruct
	public void init() {
		selectedUrl = new WSDoc();
		newdUrl = new WSDoc();
		restClient = new RestClient();
		//setUsuarios(memberService.listar());
		parametros = new ArrayList<Parametros>(); 		
		Parametros para = new Parametros();
		para.setNome("A");
		para.setValor("B");
		parametros.add(para);
		
		buildDualList();
		
		disable = true;
		localInit(null);
		
	}
	
	
	public void onAddNew() {
		if (parametros != null){
			
			Parametros para = new Parametros();
			para.setNome("");
			para.setValor("");
			parametros.add(para);
		
		}else{
			parametros = new ArrayList<Parametros>() ;
		}
	}
	
	public void onAddNewEd(){
		if ( selectedUrl != null ){
			
			if (selectedUrl.getParametros() != null) {
				Parametros para = new Parametros();
				para.setNome("");
				para.setValor("");				
				selectedUrl.getParametros().add(para);
			}else{
				selectedUrl.setParametros(new ArrayList<Parametros>());
			}
		}
		
		
	}

	/*public QwcssUrlsBean getURLbyId(Integer idc) {
		QwcssUrlsBean rec = this.listWsdlDoc.get(idc);
		this.ativado = idc;
		this.selectedUrl = rec;
		return rec;
	}*/

	/*public QwcssUrlsBean getURLbyPkName(String pkName) {
		QwcssUrlsBean rec = this.keysearch.get(pkName);
		this.ativado = this.listWsdlDoc.indexOf(rec);
		this.selectedUrl = rec;
		return rec;
	}*/

	public void editURL() {

		/*WSDoc rec = keysearch.get(this.selectedUrl.getIdService());

		int in = this.listWsdlDoc.indexOf(rec);

		if (in > 0)
			listWsdlDoc.set(in, this.selectedUrl);

		this.keysearch.put(this.selectedUrl.getIdService(), this.selectedUrl);*/
		
		if (selectedUrl != null) {
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT, selectedUrl.getIdService()).replace(" ", "%20");
			
			try {
				
				Endpoint endpoint = new Endpoint();
				
				endpoint.setCreated_at(new Date());
				endpoint.setIdentificador(newdUrl.getIdService());
				endpoint.setId(newdUrl.getWsId());
				endpoint.setDescricao(newdUrl.getDescription());
				
				endpoint.setTipo_autorizacao(newdUrl.getTipoAutorizacao());
				endpoint.setTipo_servico(tipoServico);
				endpoint.setCreated_at(created_at);
				endpoint.setEndpoint(newdUrl.getWSDLUrl());
				
				
				String body = new Gson().toJson(endpoint);
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

		localInit(null);

	}

	public void removeURL() {

		if (selectedUrl != null) {
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT, selectedUrl.getIdService()).replace(" ", "%20");
			
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

		localInit(null);


	}

	public void addURL() {
		String key = WSDoc.genKey(this.newdUrl.getWSDLUrl(), null, null);
		this.newdUrl.setSvcUID( key );
		this.newdUrl.setIdService(WSDoc.genIdentificador(this.newdUrl.getIdService()));
		
		HashMap<String, String> map = new HashMap<>();
		
		// this.newdUrl.setParametros(this.parametros);
		
		Gson gson = new Gson();
		Endpoint point = new Endpoint(); 
		point.setDescricao(this.newdUrl.getDescription());
		point.setIdentificador(this.newdUrl.getIdService());
		point.setEndpoint(this.newdUrl.getWSDLUrl());
		point.setTipo_servico(tipoServico);
		point.setTipo_autorizacao(newdUrl.getTipoAutorizacao());
		point.setEndpoint(newdUrl.getWSDLUrl());
		point.setCreated_at(getCreated_at());
		point.setUsa_certificado(Boolean.valueOf(usaCertificado));
		String body = gson.toJson(point);
		
		if (selectedUrl != null) {
			String uriSuffix = String.format(RestClient.URI_SUFFIX_ENDPOINT, newdUrl.getIdService());
			
			try {
				
				ResponseString responseString = restClient.body(body)
						.request(uriSuffix, RestClient.REQUEST_POST)
						.execute()
						.getResponseString();
				
				if (responseString.getStatusCode() != 200 && responseString.getStatusCode() != 201) {
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

		localInit(null);
		
		newdUrl = new WSDoc();
	}

	public void saveURL() {
		updateList();
	}

	private void setKeys() {
		if ( keysearch == null )
			keysearch = new Hashtable<String, WSDoc>();
		if ( listWsdlDoc != null ) {
			for (WSDoc qwcssUrlsBean : listWsdlDoc) {
				keysearch.put(qwcssUrlsBean.getIdService(), qwcssUrlsBean);
			}
		}
	}

	/**
	 * <p>Lê do arquivo de parametros JBOSS_HOME/qwcfpDoc/qwcfp.properties 
	 * a URL do Bailarina no parâmetro {@link wsBailarinaHost} e a porta
	 * em {@link wsBailarinaPort}e tenta recuperar uma lista de Web Services 
	 * cadastrados previamente.</p>
	 * 
	 * @param readFrom
	 */
	public void localInit(String readFrom) {
		try {
			
			ResponseString responseString = restClient.request(RestClient.URI_SUFFIX_ENDPOINTS, RestClient.REQUEST_GET)
														.execute()
														.getResponseString();
			
			Type endpointKind = new TypeToken<RecordDTO<EndpointList>>() {
			}.getType();
			RecordDTO<EndpointList> record = responseString.toGson(endpointKind);
			List<WSDoc> modelWService = new ArrayList<WSDoc>();
			if (record != null) {
				EndpointList dto =   (EndpointList) record.getData();
				List<EndpointShort> jsonArr = dto.getEndpoints();
				
				for (EndpointShort endpoint : jsonArr) {
					WSDoc item = new WSDoc();
					
					item.setWsId       ( endpoint.getId());
					item.setIdService  ( endpoint.getIdentificador());
					item.setDescription( endpoint.getEndpoint());
					item.setName       ( endpoint.getIdentificador()); // "Name????"
					item.setWSDLUrl    ( restClient.getserverUri() 
							+ "endpoint/" 
							+ item.getIdService());
					item.setSvcUID     ( WSDoc.genKey(item.getWSDLUrl(), null, null));
					item.setVersion("0.0");
					item.setCertificadoDec("");
					item.setTipoAutorizacao(0);
					item.setTipoServico("WSDL");
					item.setWsdlDocument("");
					item.setWsdlDocWithReplaces("");
					modelWService.add(item);
				}
			}
			this.setListWsdlDoc(modelWService);
			if (!modelWService.isEmpty()) {
				this.setSelectedUrl(modelWService.get(0));
			}
			this.ativado = 0;
			setKeys();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveCfg(qwcfpWServiceBean ctx, String readFrom) throws Exception {
	}

	public static qwcfpWServiceBean readCfg(String readFrom) throws Exception {
		return null;
	}

	@Override
	public String toString() {
		String ret = "<QWCFP_SERVCES_CATALOG>\n";
		for (WSDoc qwcssUrlsBean : listWsdlDoc) {
			ret += qwcssUrlsBean.toString();
		}
		ret += "</QWCFP_SERVCES_CATALOG>\n";
		return ret;

	}

	private void updateList() {
		try {
			saveCfg(this, null);
			localInit(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	/**
	 * @return the listWsdlDoc
	 */
	public List<WSDoc> getListWsdlDoc() {
		return listWsdlDoc;
	}

	/**
	 * @param listWsdlDoc
	 *            the listWsdlDoc to set
	 */
	public void setListWsdlDoc(List<WSDoc> listWsdlDoc) {
		this.listWsdlDoc = listWsdlDoc;
	}

	/**
	 * @return the ativado
	 */
	public Integer getAtivado() {
		return ativado;
	}

	/**
	 * @param ativado
	 *            the ativado to set
	 */
	public void setAtivado(Integer ativado) {
		this.ativado = ativado;
	}

	public WSDoc getSelectedUrl() {
		return selectedUrl;
	}

	public void setSelectedUrl(WSDoc WSDLDoc) {
		this.selectedUrl = WSDLDoc;
	}

	public List<WSDoc> getSelectdUrls() {
		return selectdUrls;
	}

	public void setSelectdUrls(List<WSDoc> selectdUrls) {
		this.selectdUrls = selectdUrls;
	}

	public static void main(String[] args) throws Exception {
		String DEST_FILE_XML = "D:\\Temp\\QW2SG\\" + "QWCFP_SERVCES_CATALOG-4.xml";
		qwcfpWServiceBean tsts = new qwcfpWServiceBean();
		// Incializa de XML
		// tsts.localInit(DEST_FILE_XML);
		tsts.setListWsdlDoc(new ArrayList<WSDoc>());

		for (int i = 0; i < 5; i++) {
			WSDoc doc = new WSDoc();
			// ctx.init();   // Usado no J2EE
			doc.init4Tests();
			doc.localInit(); // Usado standalone

			doc.setName(doc.getName() + " Nó " + i);
			doc.setIdService( WSDoc.genIdentificador( doc.getName() ));
			doc.setDescription("Descrição do chamado Nó " + i + ", " + doc.getDescription());
			tsts.listWsdlDoc.add(doc);
		}
		qwcfpWServiceBean.saveCfg(tsts, DEST_FILE_XML);
	}

	public WSDoc getNewdUrl() {
		return newdUrl;
	}

	public void setNewdUrl(WSDoc newdUrl) {
		this.newdUrl = newdUrl;
	}

	public DualListModel<MemberDTO> getMoListUserPrivs() {
		return moListUserPrivs;
	}

	public void setMoListUserPrivs(DualListModel<MemberDTO> moListUserPrivs) {
		this.moListUserPrivs = moListUserPrivs;
	}

	public List<Parametros> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametros> parametros) {
		this.parametros = parametros;
	}

	public List<Parametros> getParametrosEdicao() {
		return parametrosEdicao;
	}

	public void setParametrosEdicao(List<Parametros> parametrosEdicao) {
		this.parametrosEdicao = parametrosEdicao;
	}
	
	public int getRowKey() { 
		return this.hashCode(); 
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getUsaCertificado() {
		return usaCertificado;
	}

	public void setUsaCertificado(String usaCertificado) {
		this.usaCertificado = usaCertificado;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}



	public List<Operation> getOperations() {
		return operations;
	}



	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}



	public boolean isRestful() {
		return restful;
	}



	public void setRestful(boolean restful) {
		this.restful = restful;
	}



	public Operation getSelectedOperation() {
		return selectedOperation;
	}



	public void setSelectedOperation(Operation selectedOperation) {
		this.selectedOperation = selectedOperation;
	}



	public List<Parameter> getParameters() {
		return parameters;
	}



	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}



	public Parameter getSelectedParameter() {
		return selectedParameter;
	}



	public void setSelectedParameter(Parameter selectedParameter) {
		this.selectedParameter = selectedParameter;
	}



	public String getEndProxy() {
		return endProxy;
	}



	public void setEndProxy(String endProxy) {
		this.endProxy = endProxy;
	}



	public OperationPost getDetailedOperation() {
		return detailedOperation;
	}



	public void setDetailedOperation(OperationPost detailedOperation) {
		this.detailedOperation = detailedOperation;
	}



	public Parameter getDetailedParameter() {
		return detailedParameter;
	}



	public void setDetailedParameter(Parameter detailedParameter) {
		this.detailedParameter = detailedParameter;
	}


	
}
