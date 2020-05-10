package br.com.pix.qware.qwcfp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.QwvdtService;
import br.com.pix.qware.qwcfp.util.CookieWork;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwvdt.client.dto.ItemObject;
import br.com.pix.qwvdt.client.dto.Layout;
import br.com.pix.qwvdt.client.dto.LayoutFull;
import br.com.pix.qwvdt.client.dto.ParameterObject;
import br.com.pix.qwvdt.client.dto.PropertieObject;
import br.com.pix.qwvdt.client.dto.view.Record;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.pix.qwvdt.client.dto.view.Field;
import br.com.pix.qwvdt.client.dto.view.LayoutFactory;
import br.com.pix.qwvdt.client.dto.view.LayoutInterface;

@ManagedBean(name = "layoutBean")
@ViewScoped
public class LayoutBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4109990604937971877L;

	private ArrayList<Layout> listLayouts;

	@Inject
	private QwvdtService qwvdtService;
	
	@Inject
	private InformationGroupService	groupService;
	
	private Integer layoutSelected;

	private LayoutFull visualizacao;

	private String header;
	private ArrayList<String> iObjstype;
	
	@Inject
	private CookieWork				cookieWork;
	
	

	br.com.pix.qwvdt.client.dto.view.Layout layout;
	
	public void setCookies(){
		
		String[] acesso = qwvdtService.login();
		
		if ( acesso != null  ) {
			cookieWork.setCookie("qwvdt.user", acesso[0], 10*60*60, "/qwvdt-app");
			cookieWork.setCookie("qwvdt.token", acesso[1], 10*60*60, "/qwvdt-app");	
		}
		
	}

	@PostConstruct
	public void init() {
		iObjstype = new ArrayList<String>();
		iObjstype.add("stream");
		iObjstype.add("record");
		iObjstype.add("field");
		setListLayouts(qwvdtService.listLayouts());
		
		Integer idGroup = (Integer) Util.getPropertySession("ID_GROUP");
		
		if ( idGroup != null ){
			GroupDTO group = groupService.getGroupById(idGroup);
			
			if( group != null ) {
				if( group.getErrorCode() == 0 ){
					setLayoutSelected(group.getLayoutIdFk());
					onLayoutChange(getLayoutSelected());
				}
			}
		}
	}

	public void onLayoutChange(Integer selected) {
		setVisualizacao(null);
		setLayout(null);
		setHeader(null);
		if (selected != null) {
			setVisualizacao(qwvdtService.getLayoutFull(selected.intValue()));
			populateLayout();
		}
	}
	

	public void populateLayout() {
		if (getVisualizacao() != null) {

			for (ItemObject itemObj : getVisualizacao().getItens()) {
				if (itemObj.getType().equals("stream")) {
					String tipo = null;

					for (PropertieObject prop : itemObj.getProperties()) {
						if (prop.getKey().equals("format")) {
							tipo = prop.getValue();
						}
					}

					br.com.pix.qwvdt.client.dto.view.Layout layout = LayoutFactory.create(tipo);
					if (layout != null) {

						if (getVisualizacao().getDelimiter() != null) {
							layout.setDelimiter(getVisualizacao().getDelimiter());
							((LayoutInterface) layout).updateTitle();
						}
						setHeader(getVisualizacao().getName() + " " + layout.getType());

						List<Record> records = new ArrayList<>();
						Record r = null;

						if (itemObj.getChildren() != null) {
							for (ItemObject record : itemObj.getChildren()) {

								r = new Record();
								
								if (!iObjstype.contains(record.getType()) ) {
									continue;
								}
								String name = null;

								for (PropertieObject prop : record.getProperties()) {
									if (prop.getKey().equals("name")) {
										name = prop.getValue();
										break;
									}

									if (prop.getPars() != null) {
										for (ParameterObject a : prop.getPars()) {
											System.out.println(a.getValue());
											System.out.println(a.getOrder());
										}
									}
								}

								r.setName(name);

								List<Field> fields = new ArrayList<>();
								Field f = null;

								if (record.getChildren() != null) {
									for (ItemObject field : record.getChildren()) {

										f = new Field();

										for (PropertieObject propField : field.getProperties()) {

											if (propField.getPars() != null) {
												for (ParameterObject a : propField.getPars()) {
													System.out.println(a.getValue());
													System.out.println(a.getOrder());
												}
											}

											if (propField.getKey().equals("name")) {
												f.setName(propField.getValue());
											}

											if (propField.getKey().equals("length")) {
												f.setLength(propField.getValue());
											}

											if (propField.getKey().equals("validate")) {
												f.setValidate(propField.getValue());
											}

											if (propField.getKey().equals("literal")) {
												f.setLiteral(propField.getValue());
											}

											if (propField.getKey().equals("required")) {
												f.setRequired(propField.getValue().equals("true") ? "✅" : "❌");
											}

											if (propField.getKey().equals("type")) {
												f.setEspecialType(propField.getValue());
											}

											if (propField.getKey().equals("format")) {
												f.setFormat(propField.getValue());
											}

										}

										fields.add(f);
									}
									r.setListFields(fields);
									records.add(r);
								}

							}

							layout.setRecords(records);
							setLayout(layout);
						}

					}
				}
			}
		}
	}

	public ArrayList<Layout> getListLayouts() {
		return listLayouts;
	}

	public void setListLayouts(ArrayList<Layout> listLayouts) {
		this.listLayouts = listLayouts;
	}

	public Integer getLayoutSelected() {
		return layoutSelected;
	}

	public void setLayoutSelected(Integer layoutSelected) {
		this.layoutSelected = layoutSelected;
	}

	public LayoutFull getVisualizacao() {
		return visualizacao;
	}

	public void setVisualizacao(LayoutFull visualizacao) {
		this.visualizacao = visualizacao;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public br.com.pix.qwvdt.client.dto.view.Layout getLayout() {
		return layout;
	}

	public void setLayout(br.com.pix.qwvdt.client.dto.view.Layout layout) {
		this.layout = layout;
	}

}
