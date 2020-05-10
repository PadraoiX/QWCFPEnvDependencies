package br.com.pix.qware.qwcfp.beans.uties;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.qwcss.ws.dto.SimpleDTO;


@Named("fileRemove")
@RequestScoped
@WebServlet("/FileRemove")
public class FileRemove  extends HttpServlet  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7014177531387541163L;
	
	@Inject
	private FileManagedService		fileManagedService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			JSONObject jsParam = new JSONObject();
			
			String mensage;
			
			String fileName =  req.getParameter("FILENAME") != null ? req.getParameter("FILENAME"): null;
			String fileVersionIdString = req.getParameter("FILEVERSIONID") != null ? req.getParameter("FILEVERSIONID"): null;
			
			Integer fileVersionId = Integer.parseInt(fileVersionIdString);
			
						
				if(fileVersionId != null) {
					SimpleDTO ret = fileManagedService.deleteVersionEx( fileVersionId  );	
					if (ret.getErrorCode() != 0) {
						mensage = "N\u00E3o foi poss\u00EDvel cancelar a transferência do arquivo " + fileName;
						jsParam.put("MESSAGE", mensage);
						jsParam.put("SUCESSO", "false");
						resp.getOutputStream().print(jsParam.toString());
					}else{
						mensage = "Foi cancelado com sucesso o arquivo " + fileName;
						jsParam.put("MESSAGE", mensage);
						jsParam.put("SUCESSO", "true");
						resp.getOutputStream().print(jsParam.toString());
					}
				}else {
					mensage = "N\u00E3o foi poss\u00EDvel identificar o arquivo para cancelar a transferência " + fileName;
					jsParam.put("SUCESSO", "false");
					jsParam.put("MESSAGE", mensage);
					resp.getOutputStream().print(jsParam.toString());
				}
				
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}



}
