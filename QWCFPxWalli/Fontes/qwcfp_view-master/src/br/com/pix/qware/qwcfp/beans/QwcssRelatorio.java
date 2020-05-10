package br.com.pix.qware.qwcfp.beans;

import java.io.Serializable;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named("qwcssRelatorio")
@RequestScoped
public class QwcssRelatorio implements Serializable {
/*	
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 1L;

	private List<QwcssMember> listUsuario;
	
	@Inject
	private QwcssMemberService mService;

	JasperPrint jasperPrint;

	public void init() throws JRException {
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listUsuario);
		String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\relatorios\\Usuarios.jasper");
		jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap(),beanCollectionDataSource);
	}

	public void PDF() throws JRException, IOException {
		init();
		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		httpServletResponse.addHeader("Content-disposition","attachment; filename=report.pdf");
		ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public List<QwcssMember> getListUsuario() {
		listUsuario = mService.listarMembers();
		return listUsuario;
	}

	public void setListUsuario(List<QwcssMember> listUsuario) {
		this.listUsuario = listUsuario;
	}*/

}