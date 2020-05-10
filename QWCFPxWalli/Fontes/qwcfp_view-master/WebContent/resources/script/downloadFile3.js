function getFile() {

	var statusFile = document.getElementById("formDIV:statusFile").value;
	var urlHttpTransf = document.getElementById("formDIV:urlHttpTransf").value;
	var ticket = document.getElementById("formDIV:ticket").value;
	

	if (statusFile) {
		PF('messages').renderMessage({
			"summary" : "Erro",
			"detail" : statusFile,
			"severity" : "error"
		});
		return;
	}

	if (ticket) {
		var form = document.createElement("form");
		form.setAttribute("method", 'GET');
		form.setAttribute("action", urlHttpTransf + "/" + ticket);
		form.setAttribute("id", "formDownload");
		
		document.body.appendChild(form);
		form.submit();
	}

}


function getFileExt() {
	
	var link = document.getElementById("formDIV:link").value;
	
	if (link) {
		var form = document.createElement("form");
		form.setAttribute("method", 'GET');
		form.setAttribute("action", link);
		form.setAttribute("id", "formDownload");
		
		document.body.appendChild(form);
		form.submit();
	}
	
}