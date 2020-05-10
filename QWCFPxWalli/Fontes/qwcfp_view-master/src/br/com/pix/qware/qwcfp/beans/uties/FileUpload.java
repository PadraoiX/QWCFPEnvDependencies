package br.com.pix.qware.qwcfp.beans.uties;

import java.io.IOException;
import java.math.BigDecimal;

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

import br.com.pix.qware.cliapi.QWCrypto.QWCrypto;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.EnviarArquivoService;
import br.com.pix.qware.qwcfp.service.FileManagedService;
import br.com.pix.qware.qwcfp.service.FileVersionListService;
import br.com.pix.qware.qwcfp.service.InformationGroupService;
import br.com.pix.qware.qwcfp.service.TicketService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.pix.qwcfp.ws.client.erro.ViewError;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.FileListDTO;
import br.com.qwcss.ws.dto.FileListDTOEx;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.GroupSpaceDTO;
import br.com.qwcss.ws.dto.SimpleDTO;
import br.com.qwcss.ws.dto.TransferConfigDTO;
import br.com.qwcss.ws.dto.VersionsDTO;

@Named("fileUpload")
@RequestScoped
@WebServlet("/FileUpload")
public class FileUpload extends HttpServlet {

	private static final long		serialVersionUID	= 5988237150707469520L;

	@Inject
	private InformationGroupService	groupService;

	@Inject
	private FileManagedService		fileManagedService;

	@Inject
	private FileVersionListService	fileVersionService;

	@Inject
	private EnviarArquivoService	uploadService;

	@Inject
	private ConfigService			configService;

	@Inject
	private TicketService			ticketService;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,	IOException {

		try {
			TransferConfigDTO retorno;
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");

			JSONObject jsParam = new JSONObject();

			String mensage;

			Long fileSize = req.getParameter("FILESIZE") != null ? Long.parseLong(req.getParameter("FILESIZE")) : null;
			String fileName = req.getParameter("FILENAME") != null ? req.getParameter("FILENAME"): null;
			String condition = req.getParameter("CONDITION") != null ? req.getParameter("CONDITION") : null;
			String aditionalInformation = req.getParameter("INFOADD") != null ? req.getParameter("INFOADD") : null;
			String grupoIdString = req.getParameter("GRUPOID") != null ? req.getParameter("GRUPOID") : null;
			
			if (fileName == null || fileSize == null || condition == null) {
				mensage = "N\u00E3o foi poss\u00EDvel identificar o arquivo de envio";
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}


			if(grupoIdString == null){ 
				mensage = "N\u00E3o foi poss\u00EDvel encontrar o grupo de destino";
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}
			 

			if (!checkSpace(Integer.parseInt(grupoIdString), fileSize)) {
				mensage = ViewError.SPACE_GROUP_NOT_AVALIABLE.getCode() + ": "	+ ViewError.SPACE_GROUP_NOT_AVALIABLE.getMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}

			retorno = inserirFileAndVersion(Integer.parseInt(grupoIdString), fileName,	fileSize, condition, aditionalInformation);

			if (retorno == null) {
				mensage = ViewError.FILE_INSERT_ERROR.getCode() + ": "	+ ViewError.FILE_INSERT_ERROR.getMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}

			if (retorno.getErrorCode()  != ViewError.OK.getCode() ) {
				mensage = retorno.getErrorCode() + ": " + retorno.getErrorMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
				
			}

			ConfigDTO cfgHttpUrl = configService.carregar("HTTP_TRANSFER_URL", "SYSTEM");

			String objId = retorno.getObjId();
			String saveAs = retorno.getSaveAs();
			condition = retorno.getDisp();
			
			String httpUrl = null;
			if (cfgHttpUrl != null){
				if(cfgHttpUrl.getErrorCode() == 0){
					httpUrl = cfgHttpUrl.getValue();
				}else{
					mensage = cfgHttpUrl.getErrorCode() + ": "	+  cfgHttpUrl.getErrorMsg();
					jsParam.put("MESSAGE", mensage);
					resp.getOutputStream().print(jsParam.toString());
					return;	
				}
				
			}else {
				mensage = ViewError.TRANSFER_CONFIG_ERROR.getCode() + ": "	+ ViewError.TRANSFER_CONFIG_ERROR.getMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}

			if (objId == null) {
				mensage = ViewError.GET_OBJID_FILE.getCode() + ": "	+ ViewError.GET_OBJID_FILE.getMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}

			if (saveAs == null) {
				mensage = ViewError.GET_SAVEAS_FILE.getCode() + ": "	+ ViewError.GET_SAVEAS_FILE.getMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}

			if (httpUrl == null) {
				mensage = ViewError.TRANSFER_CONFIG_ERROR.getCode() + ": "	+ ViewError.TRANSFER_CONFIG_ERROR.getMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}
			
			String username = (String) req.getSession().getAttribute("USER_LDAP");
			String senha = (String) req.getSession().getAttribute("PASS_LDAP");
		 
			retorno = returnTicket("Put", saveAs, "-", condition, "Binary", objId, String.valueOf(fileSize), username, senha);
			if (retorno.getErrorCode() != ViewError.OK.getCode()) {
				mensage = retorno.getErrorCode() + ": " + retorno.getErrorMsg();
				jsParam.put("MESSAGE", mensage);
				resp.getOutputStream().print(jsParam.toString());
				return;
			}
			
			jsParam.put("TICKET", retorno.getErrorMsg());
			jsParam.put("HTTPURL", httpUrl);
			jsParam.put("MESSAGE", "");
			jsParam.put("SAVEAS", saveAs);

			resp.getOutputStream().print(jsParam.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	//TODO tirar comentário
	boolean checkSpace(Integer groupId, Long fileSize) {
/*		GroupSpaceDTO space = groupService.returnGroupSpace(groupId, null);
		BigDecimal espacoDisponivel = space.getSpaceLimit();
		BigDecimal totalSpace = space.getTotalSpace();
		BigDecimal childreanSpace = space.getChildreanSpace();
		BigDecimal espacoCompartilhadoDisponivel = totalSpace.subtract(childreanSpace);
		BigDecimal espacoPermitido = espacoDisponivel.subtract(new BigDecimal(fileSize));
		BigDecimal espacoPermitidoComp = espacoCompartilhadoDisponivel.subtract(new BigDecimal(
				fileSize));

		if (espacoPermitido.doubleValue() > 0 && espacoPermitidoComp.doubleValue() > 0)
			return true;
		else
			return false;*/
		
		return true;

	}

	private TransferConfigDTO returnTicket(String operation, String fileName, String saveAs, String disp, String fileType, String objId, String fileSize, String usucario, String senha  ){
		TransferConfigDTO retorno = new TransferConfigDTO();
		retorno.setErrorCode(0);
		
		

		SimpleDTO simple = ticketService.getTicketUpload(operation, fileName, saveAs, disp, fileType, objId, fileSize, usucario, senha);

		if (simple != null) {
			retorno.setErrorCode(simple.getErrorCode());
			retorno.setErrorMsg(simple.getErrorMsg());
		} else {
			retorno.setErrorCode(ViewError.GET_TICKET_ERROR.getCode());
			retorno.setErrorMsg(ViewError.GET_TICKET_ERROR.getMsg());
		}

		return retorno;

	}

	/**
	 * Método utilizado para inserir a versão no banco de dados e associá-lo a um grupo
	 * 
	 * @param event
	 * @return
	 */
	private TransferConfigDTO inserirFileAndVersion(Integer groupId,
			String fileName,
			Long fileSize,
			String condition,
			String aditionalInformation) {
		if (fileName != null) {
			TransferConfigDTO retorno = new TransferConfigDTO();
			retorno.setErrorCode(0);
			retorno.setErrorMsg("");
			uploadService.setOnlyWsContract(true);

			if (groupId != null) {
				GroupDTO grupo = groupService.getGroupById(groupId);
				if (grupo != null) {
					if (grupo.getErrorCode() == 0) {
						String apelido = grupo.getApelido();
						String aceptVersion = grupo.getAceptVersion().trim();

						FileListDTOEx[] files = fileManagedService.listar(apelido, fileName, 0, 10,null,null);

						VersionsDTO[] fileVersion = null;

						if ((files == null || (files.length == 0)) && aceptVersion.equals("N")) {
							retorno = uploadService.naoVersionado_novo(apelido,
									aditionalInformation,
									fileName,
									fileSize);
							retorno.setOperation("New");
							retorno.setAsa("S");
						}

						if ((files == null || (files.length == 0)) && aceptVersion.equals("S")) {
							retorno = uploadService.versionado_novo(apelido,
									aditionalInformation,
									fileName,
									fileSize);
							retorno.setOperation("New");
							retorno.setAsa("S");
						}

						if (files != null && (files.length > 0) && aceptVersion.equals("N")) {
							if (condition.equals("Rep")){
								retorno = uploadService.naoVersionado_replace(apelido,
										aditionalInformation,
										fileName,
										fileSize);
							}else {
								retorno.setErrorCode(ViewError.GRUPO_NOT_VERSION.getCode());
								retorno.setErrorMsg(ViewError.GRUPO_NOT_VERSION.getMsg()
										+ ". Por favor, escolha a op\u00E7\u00E3o de realizar o repleace de: " + fileName);
								return retorno;
							}
						}

						if (files != null && (files.length > 0) && aceptVersion.equals("S")) {
							if (condition.equals("Rep")){
								retorno = uploadService.versionado_replace(apelido,
										aditionalInformation,
										fileName,
										fileSize);
							}else {
								fileVersion = fileVersionService.listar(apelido,
										fileName,
										"TRANSFERINDO");
								if (fileVersion != null) {
									if (fileVersion.length >= grupo.getVersionLimit()) {
										retorno.setErrorCode(ViewError.GRUPO_EXCEEDED_VERSION
												.getCode());
										retorno.setErrorMsg(ViewError.GRUPO_EXCEEDED_VERSION
												.getMsg()
												+ ". Por favor, escolha a op\u00E7\u00E3o de realizar o repleace de " +fileName);
										return retorno;
									}
								}
								retorno = uploadService.versionado_novo(apelido,
										aditionalInformation,
										fileName,
										fileSize);
							}
						}
					} else {
						retorno.setErrorCode(grupo.getErrorCode());
						retorno.setErrorMsg(grupo.getErrorMsg());
					}

				} else {
					retorno.setErrorCode(ViewError.WEBSERVICE_OFF_ERR.getCode());
					retorno.setErrorMsg(ViewError.WEBSERVICE_OFF_ERR.getMsg());
				}

			} else {
				retorno.setErrorCode(ViewError.WEBSERVICE_OFF_ERR.getCode());
				retorno.setErrorMsg(ViewError.WEBSERVICE_OFF_ERR.getMsg());
			}

			return retorno;

		}

		return null;

	}

}
