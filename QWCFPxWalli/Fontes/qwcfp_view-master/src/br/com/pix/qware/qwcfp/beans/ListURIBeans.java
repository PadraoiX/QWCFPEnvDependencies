 package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DualListModel;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Version;

import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.util.FacesMessages;
import br.com.pix.qware.qwcfp.util.QwcfpInitials;
import br.com.pix.qware.wsproxy.dto.Parametros;
import br.com.pix.qware.wsproxy.dto.WSProxyCatalog;
import br.com.pix.qware.wsproxy.dto.wsdlDoc;
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
@Root(name = "QW2SG_SERVCES_CATALOG")
@Version(revision = 1.0, name = "Versao", required = false)
@ManagedBean(name = "listURIBeans")
@ViewScoped
public class ListURIBeans extends AbstractBean implements RTCTS4Qw2sg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2408795418711028370L;

	@Inject
	private FacesMessages messages;
	
	@Inject
	private MemberService			memberService;

	@ElementList(name = "wsdlDoc", required = false, empty = true, inline = true, type = wsdlDoc.class)
	private List<wsdlDoc> listWsdlDoc = null;
	private Hashtable<String, wsdlDoc> keysearch = null;
	private Integer ativado = null;
	private boolean disable = false;
	private wsdlDoc selectedUrl;
	private wsdlDoc newdUrl;
	private List<wsdlDoc> selectdUrls;
	private DualListModel<MemberDTO>  moListUserPrivs;
	
	
	private List<Parametros> parametros;
	private List<Parametros> parametrosEdicao;

	
	
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
	
	@PostConstruct
	public void init() {
		selectedUrl = new wsdlDoc();
		newdUrl = new wsdlDoc();
//		setUsuarios(memberService.listar());
		parametros = new ArrayList<Parametros>(); 		
		Parametros para = new Parametros();
		para.setNome("");
		para.setValor("");
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

		wsdlDoc rec = keysearch.get(this.selectedUrl.getPkName());

		int in = this.listWsdlDoc.indexOf(rec);

		if (in > 0)
			listWsdlDoc.set(in, this.selectedUrl);

		this.keysearch.put(this.selectedUrl.getPkName(), this.selectedUrl);


	}

	public void removeURL() {

		wsdlDoc rec = keysearch.get(this.selectedUrl.getPkName());

		int in = this.listWsdlDoc.indexOf(rec);

		if (in > 0)
			listWsdlDoc.remove(in);

		this.keysearch.remove(this.selectedUrl.getPkName());


	}

	public void addURL() {
		String key = wsdlDoc.genKey("KEYID", this.newdUrl.getName(), this.newdUrl.getWSDLUrl());
		this.newdUrl.setPkName(key);
		
		HashMap<String, String> map = new HashMap<>();
		
		this.newdUrl.setParametros(this.parametros);
		
		for (String kecy : map.keySet()) {
			System.out.println("Key: " + kecy + " Value: " + map.get(kecy));
		}
		
		this.listWsdlDoc.add(this.newdUrl);
		this.ativado = this.listWsdlDoc.indexOf(this.newdUrl);
		this.keysearch.put(this.newdUrl.getPkName(), this.newdUrl);
		newdUrl = null;
	}

	public void saveURL() {
		updateList();
	}

	private void setKeys() {
		keysearch = new Hashtable<String, wsdlDoc>();
		for (wsdlDoc qwcssUrlsBean : listWsdlDoc) {
			keysearch.put(qwcssUrlsBean.getPkName(), qwcssUrlsBean);
		}
	}

	public void localInit(String readFrom) {
		try {
			ListURIBeans lst = readCfg(readFrom);
			if ((lst != null) && (lst.getListWsdlDoc() != null)) {
				this.setListWsdlDoc(lst.getListWsdlDoc());
				this.setSelectedUrl(lst.listWsdlDoc.get(0));
				this.ativado = 0;
				setKeys();
			} else {
				this.setListWsdlDoc(new ArrayList<wsdlDoc>());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveCfg(ListURIBeans ctx, String readFrom) throws Exception {
		String fileINI =  QwcfpInitials.getInstance().getProperties().getProperty("QW2SG.ini");
		String fileXML = ((readFrom == null) ? QwcfpInitials.getInstance().getProperties().getProperty("QW2SG.dir")
				: readFrom);
		if ((fileXML == null) || fileXML.isEmpty()) {
			System.err.println("Tentativa de Salvar configurações para arquivo: " + fileXML + " não informado.");
			return;
		}
		
		WSProxyCatalog tsts = new WSProxyCatalog();
		tsts.setListWsdlDoc(ctx.getListWsdlDoc());
		//WSProxyCatalog.save2INI(tsts, fileINI);
		WSProxyCatalog.save(tsts, fileXML);

		// System.err.println("Advertencia: Salvando CATALAGO de definições WSDL para (" + fileXML + ").");
	}

	public static ListURIBeans readCfg(String readFrom) throws Exception {
		String fileXML = ((readFrom == null) ? QwcfpInitials.getInstance().getProperties().getProperty("QW2SG.dir")
				: readFrom);
		if ((fileXML == null) || fileXML.isEmpty()) {
			System.err.println(
					"Tentativa de Encontrar direitório de configurações para arquivo: " + fileXML + " não informado.");
			return null;
		}
		WSProxyCatalog ctx = WSProxyCatalog.read(fileXML);
		for (wsdlDoc xmlDoc : ctx.getListWsdlDoc() ) {
			wsdlDoc.flop(xmlDoc);
		}
		ListURIBeans ctx2  = new ListURIBeans();
		ctx2.setListWsdlDoc(ctx.getListWsdlDoc() );
		return ctx2;
	}

	@Override
	public String toString() {
		String ret = "<QW2SG_SERVCES_CATALOG>\n";
		for (wsdlDoc qwcssUrlsBean : listWsdlDoc) {
			ret += qwcssUrlsBean.toString();
		}
		ret += "</QW2SG_SERVCES_CATALOG>\n";
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
	public List<wsdlDoc> getListWsdlDoc() {
		return listWsdlDoc;
	}

	/**
	 * @param listWsdlDoc
	 *            the listWsdlDoc to set
	 */
	public void setListWsdlDoc(List<wsdlDoc> listWsdlDoc) {
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

	public wsdlDoc getSelectedUrl() {
		return selectedUrl;
	}

	public void setSelectedUrl(wsdlDoc wsdlDoc) {
		this.selectedUrl = wsdlDoc;
	}

	public List<wsdlDoc> getSelectdUrls() {
		return selectdUrls;
	}

	public void setSelectdUrls(List<wsdlDoc> selectdUrls) {
		this.selectdUrls = selectdUrls;
	}

	public static void main(String[] args) throws Exception {
		String DEST_FILE_XML = "D:\\Temp\\QW2SG\\" + "QW2SG_SERVCES_CATALOG-4.xml";
		ListURIBeans tsts = new ListURIBeans();
		// Incializa de XML
		// tsts.localInit(DEST_FILE_XML);
		tsts.setListWsdlDoc(new ArrayList<wsdlDoc>());

		for (int i = 0; i < 20; i++) {
			wsdlDoc ctx = new wsdlDoc();
			// ctx.init();   // Usado no J2EE
			ctx.localInit(); // Usado standalone

			ctx.setName(ctx.getName() + " Nó " + i);
			ctx.setPkName( wsdlDoc.genKey("KEYID", ctx.getName(), ctx.getWSDLUrl() ) );
			ctx.setDescription("Descrição do chamado Nó " + i + ", " + ctx.getDescription());
			tsts.listWsdlDoc.add(ctx);
		}
		ListURIBeans.saveCfg(tsts, DEST_FILE_XML);
	}

	public wsdlDoc getNewdUrl() {
		return newdUrl;
	}

	public void setNewdUrl(wsdlDoc newdUrl) {
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

}
