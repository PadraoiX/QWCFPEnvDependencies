package br.com.pix.qware.qwcfp.util;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.pix.qware.qwcfp.service.ReportService;
import br.com.qwcss.ws.dto.RptAreaSpaceDTO;
import br.com.qwcss.ws.dto.RptFileByLayerDTO;
import br.com.qwcss.ws.dto.RptGroupSpaceDTO;
import br.com.qwcss.ws.dto.RptGroupsDTO;
import br.com.qwcss.ws.dto.RptMemberDTO;
import br.com.qwcss.ws.dto.RptOcupiedSpaceDTO;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named("executeReport")
@RequestScoped

public class executeReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ReportService reportService;

	public void imprimeRelatorio(String option) throws IOException {

		@SuppressWarnings("rawtypes")
		HashMap parameters = new HashMap();

		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();

			facesContext.responseComplete();

			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			JRBeanCollectionDataSource datasource = null;

			if (option == "Area") {
				RptOcupiedSpaceDTO[] array = reportService.listOcupiedSpace();
				if (array != null) {
					List<RptOcupiedSpaceDTO> list = Arrays.asList(array);
					datasource = new JRBeanCollectionDataSource(list);
				} else {
					List<RptOcupiedSpaceDTO> list = new ArrayList<RptOcupiedSpaceDTO>();
					datasource = new JRBeanCollectionDataSource(list);
				}
			} else if (option == "ArquivosFaixa") {
				RptFileByLayerDTO[] array = reportService.listFilesByLayer();
				if (array != null) {
					List<RptFileByLayerDTO> list = Arrays.asList(array);
					datasource = new JRBeanCollectionDataSource(list);
				} else {
					List<RptFileByLayerDTO> list = new ArrayList<RptFileByLayerDTO>();
					datasource = new JRBeanCollectionDataSource(list);
				}
			} else if (option == "AreaGraficoTam") {
				RptAreaSpaceDTO[] array = reportService.listAreaSpace();
				if (array != null) {
					List<RptAreaSpaceDTO> list = Arrays.asList(array);
					datasource = new JRBeanCollectionDataSource(list);
				} else {
					List<RptAreaSpaceDTO> list = new ArrayList<RptAreaSpaceDTO>();
					datasource = new JRBeanCollectionDataSource(list);
				}
			} else if (option == "GrupoGraficoTam") {
				RptGroupSpaceDTO[] array = reportService.listGroupSpace();
				if (array != null) {
					List<RptGroupSpaceDTO> list = Arrays.asList(array);
					datasource = new JRBeanCollectionDataSource(list);
				} else {
					List<RptGroupSpaceDTO> list = new ArrayList<RptGroupSpaceDTO>();
					datasource = new JRBeanCollectionDataSource(list);
				}
			} else if (option == "GruposRelatorio") {
				RptGroupsDTO[] array = reportService.listGroups();
				if (array != null) {
					List<RptGroupsDTO> list = Arrays.asList(array);
					datasource = new JRBeanCollectionDataSource(list);
				} else {
					List<RptGroupsDTO> list = new ArrayList<RptGroupsDTO>();
					datasource = new JRBeanCollectionDataSource(list);
				}
			} else if (option == "Usuarios") {
				RptMemberDTO[] array = reportService.listUsers();
				if (array != null) {
					List<RptMemberDTO> list = Arrays.asList(array);
					datasource = new JRBeanCollectionDataSource(list);
				} else {
					List<RptMemberDTO> list = new ArrayList<RptMemberDTO>();
					datasource = new JRBeanCollectionDataSource(list);
				}
			}

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					scontext.getRealPath("/WEB-INF/relatorios/" + option + ".jasper"), parameters, datasource);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

			exporter.exportReport();

			byte[] bytes = baos.toByteArray();

			if (bytes != null && bytes.length > 0) {

				HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

				response.setContentType("application/download");

				response.setHeader("Content-disposition", "attachment; filename=\"\\relatorios\\" + option + ".pdf");

				response.setContentLength(bytes.length);
				ServletOutputStream outputStream = response.getOutputStream();

				try {
					outputStream.write(bytes, 0, bytes.length);

					outputStream.flush();

				} finally {
					outputStream.close();
				}
			}

			facesContext.responseComplete();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}