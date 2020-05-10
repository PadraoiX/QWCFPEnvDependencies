
var countListener = 0;

function showFileSize( e ) {
	
	var res = $(this).attr("id");
	
	if(res != null && res != "undefined")
	{
		var idGrupoSelected = $(this).attr("id").split("-")[1];	
	}
	
	
	
	
    var input, file;
    countListener++;

    if (!window.FileReader) {
        PF('messages').renderMessage({"summary":"Atenção",  "detail":"A API de fileUpload não é soportada neste browser",  "severity":"warn"});
        return;
    }
    
    var files;
    
    if(e != null ) {
		FileDragHover(e);
		files = e.target.files || e.dataTransfer.files;
		document.getElementById('filedrag').style = 'display: none';
	}else {
		input = document.getElementById('fileinput');
	    document.getElementById('fileinput').disabled = 'true';
	    document.getElementById('filedrag').style = 'display: none';
	    
	    if (!input) {
	        PF('messages').renderMessage({"summary":"Atenção",  "detail":"Hum, não foi possível encontrar o elemento de upload",  "severity":"warn"});
	        return;
	    }
	    
	    files = input.files;
	}
    
        
   if (!files) {
        PF('messages').renderMessage({"summary":"Atenção",  "detail":"Este browser não suporta envio de arquivos",  "severity":"warn"});
        backStatus();
    }
    else if (!files[0]) {
	       PF('messages').renderMessage({"summary":"Atenção",  "detail":"Por favor escolha um arquivo para enviar",  "severity":"warn"});
	       backStatus();	       
    }
    else {
    	var i = 0;
    	var sizeArray = files.length;

    	var nameEvent = 'build' + countListener;
		var event = new Event(nameEvent); 
		
		validSession();
		
		var arrayBoolean = new Array(sizeArray);
		
		for (var j = 0; j < sizeArray; j++) {
			arrayBoolean[j] = 'true';
			file = files[j];
			createBar(file.name, file.size, arrayBoolean, j );
		}
		
		document.getElementById("formDIV").addEventListener (nameEvent, function (e){
					if(i < sizeArray ){
						if(arrayBoolean[i] == 'true'){
							file = files[i++];
							checkcondition ( file, event, (i-1), idGrupoSelected );
						}else{
							i++;
							document.getElementById("formDIV").dispatchEvent(new Event(nameEvent));
						}
			    	}else{
			    		backStatus();
			    		document.getElementById('fileinput').value = "";
			    		PF('uploadFile').hide();
			    		
			    	}
			}, false);
		
		
		document.getElementById("formDIV").dispatchEvent(event);

    }   
}


function backStatus(){
	$("#fileinput").removeAttr('disabled');
//	invalidSession();
	
/*	var span = $( ".ui-button-icon-left.ui-icon.ui-c.fa.fa-folder-open" );
	$( this ).removeClass( "uploadMain" );
	$(this).find(span).addClass("fa-folder");
	$(this).find(span).removeClass("fa-folder-open");*/
	
	document.getElementById('filedrag').style = 'display: block';
	$('#countBytes').text("Upload de Arquivos");
	
}

function getContextPath() {
   return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}


function checkcondition (file, event, id, idGrupoSelected){
	var condition, fileName, fileSize, contextPath, infoAdd, idGrupo;

	if ($('#substituir').is(":checked"))
    {
      	condition = 'Rep';
    }else
	{
		condition = 'New';
	}

	fileName = file.name;
	
	fileSize = file.size;
	infoAdd = document.getElementById("formDIV:aditionalInformation").value;
	idGrupo = document.getElementById("formDIV:idGrupo").value;
	
	if(idGrupo == null || idGrupo == '')
		idGrupo = idGrupoSelected;
	
	contextPath = getContextPath();
	
	
	var params = {'CONDITION': condition, 'FILENAME' : fileName, 'FILESIZE' : fileSize, 'INFOADD' : infoAdd, "GRUPOID" : idGrupo};
	
		var form = document.createElement("form");
	    form.setAttribute("method", 'POST');
	    form.setAttribute("action", contextPath + '/FileUpload');
	    form.setAttribute("id", "uploadForm");
	    form.setAttribute("accept-charset", "UTF-8");
	    
	    for(var key in params) {
	        if(params.hasOwnProperty(key)) {
	        	var hiddenField = document.createElement("input");
	            hiddenField.setAttribute("type", "hidden");
	            hiddenField.setAttribute("name", key);
	            hiddenField.setAttribute("value", params[key]);
	            form.appendChild(hiddenField);
			}
	    }
	    
	    document.body.appendChild(form);


	    $.ajax({
            type: form.method,
            url:  form.action,
            data: $('#uploadForm').serialize(),
            success: processSuccess,
            error: processError
        });

        function processSuccess (data, status, req) {
        	document.getElementById("uploadForm").remove();
        	insertFileSoap ( file, event, condition, JSON.parse (data), id );
        }

        function processError (data, status, req) {
        	document.getElementById("uploadForm").remove();
        	PF('messages').renderMessage({"summary":"Erro",  "detail":"Ocorreu um erro tentando cadastrar o arquivo no sistema",  "severity":"error"});
        }
}



	function insertFileSoap ( file, event, condition, json, id ){
			
    	var bar = $('#bar' + id);
		var percent = $('#percent' + id);
		var byteSend = $('#byteSend' + id);
		
		
		var myXhr = new window.XMLHttpRequest();

	    var fd = new FormData();

	    var ticket = json['TICKET'];
	    var urlHttpTransf = json['HTTPURL'];
	    var mensage = json['MESSAGE'];
	    var saveAs = json['SAVEAS'];


	    if( mensage.length != 0 ){
	    	PF('messages').renderMessage({"summary":"Error",  "detail": mensage,  "severity":"error"});
	    	$('#divProgress' + id).remove();
	    	document.getElementById("formDIV").dispatchEvent(event);
	    	updateTable();
	    	return false;
		}

	    if( !ticket ){
	    	PF('messages').renderMessage({"summary":"Error",  "detail":"Não foi possível encontrar o ticket de transmição",  "severity":"error"});
	    	document.getElementById("formDIV").dispatchEvent(event);
	    	updateTable();
	    	return false;
		}



	    if( !urlHttpTransf ){
	    	PF('messages').renderMessage({"summary":"Error",  "detail":"Não foi possível encontrar a url de transmição",  "severity":"error"});
	    	document.getElementById("formDIV").dispatchEvent(event);
	    	updateTable();
	    	return false;
		}
	    
	    document.getElementById('abortUpload' +id).style.display = 'block'; // cancela na transferência
		document.getElementById('cancelarUpload' + id).style.display = 'none'; // cancela sem transferência

		 fd.append( "CONTENT",  file );

		 $.ajax({
			  url: urlHttpTransf + "/" +  ticket,
			  data: fd,
			  processData: false,
			  contentType: false,
              cache: false,
			  type: 'POST',
		      enctype: 'multipart/form-data',
		      xhr: function() {
	                if(myXhr.upload){
	                    myXhr.upload.addEventListener('progress', progress, false);
	                }
	                return myXhr;
	        	},
		      	beforeSend: function() {
			        var percentVal = '0%';
			        bar.width(percentVal);
			        percent.html(percentVal);
			    },
			    uploadProgress: function(event, position, total, percentComplete) {
			        var percentVal = percentComplete + '%';
			        bar.width(percentVal);
			        percent.html(percentVal);
			    },
			 	success: function(data, status, req){
			 		var percentVal = '100%';
			        bar.width(percentVal);
			        percent.html(percentVal);
			     	PF('messages').renderMessage({"summary":"Sucesso",  "detail":"O arquivo " +file.name+" foi enviado com sucesso",  "severity":"info"});
			  },
			  error: function(data, status, req) { 
				  if (cancelar) {
						cancelar = false;
						cancelarUpload(saveAs);
				  }else
				  	PF('messages').renderMessage({"summary":"Error",  "detail":"Ocorreu um erro tentando transferir o arquivo "+file.name + " " + data.responseText,  "severity":"error"});
			  },
			  	complete: function(xhr) {
			        var percentVal = '0%';
			        bar.width(percentVal);
			        percent.html(percentVal);
			        var fileNameLabel = $('#fileNameLabel');
			    	fileNameLabel.html('');
			    	$('#divProgress' + id).remove();
			    	byteSend.remove();
			    	updateTable();
					document.getElementById("formDIV").dispatchEvent(event);
				}
			});
		 
		 function cancelarUpload(saveAs) {
				
				var contextPath = getContextPath();
				
				var params = {'FILENAME': file.name, 'FILEVERSIONID' : saveAs.split('.')[0]};
				
				var form = document.createElement("form");
			    form.setAttribute("method", 'POST');
			    form.setAttribute("action", contextPath + '/FileRemove');
			    form.setAttribute("id", "uploadRemove");
			    
			    for(var key in params) {
			        if(params.hasOwnProperty(key)) {
			        	var hiddenField = document.createElement("input");
			            hiddenField.setAttribute("type", "hidden");
			            hiddenField.setAttribute("name", key);
			            hiddenField.setAttribute("value", params[key]);
			            form.appendChild(hiddenField);
					}
			    }
			    
			    document.body.appendChild(form);

			    $.ajax({
		            type: form.method,
		            url:  form.action,
		            data: $('#uploadRemove').serialize(),
		            success: processSuccessRemove,
		            error: processErrorRemove
		        });
			    
			    function processSuccessRemove(data, status, req){
			    	document.getElementById("uploadRemove").remove();
			    	var json = JSON.parse (data);
			    	
			    	var mensage = json['MESSAGE'];
			    	var sucess = json['SUCESSO'];
			    	
					if( sucess == "false"){
						PF('messages').renderMessage({"summary":"Error",  "detail": mensage,  "severity":"error"});
						document.getElementById("formDIV").dispatchEvent(event);
						return false;
					}else {
				    	PF('messages').renderMessage({"summary":"Sucesso",  "detail": mensage,  "severity":"info"});				
					}

			    }
			    
			    function processErrorRemove(data, status, req) {
			    	document.getElementById("uploadRemove").remove();
				  	PF('messages').renderMessage({"summary":"Error",  "detail":"Ocorreu um erro tentando cancelar o arquivo "+fileName + " " + data.responseText,  "severity":"error"});
			    }
			    
			}
		 
		 var cancelar = false;
		 
		 $("#abortUpload" + id).click(function(e) {
				if (myXhr) {   
					cancelar = true;
					myXhr.abort();
					myXhr = null;
					$('#divProgress' + id).remove();
				}
			});
		 
		 
		 function progress(e){
			    if(e.lengthComputable){
			        var max = e.total;
			        var current = e.loaded;
			        var totalBytes = bytesToSize(e.total);
					var calc = (current * 100)/max;
			        var percentVal = parseInt(calc, 10) + '%';
			        bar.width(percentVal)
			        percent.html(percentVal);
			        
			        $('#countBytes').text("Enviados "+bytesToSize(e.loaded)+" de " + totalBytes);
			        
/*			        byteSend.html("Enviados "+bytesToSize(e.loaded)+" de " + totalBytes);*/
			    }  
			 }
		 
	}
	
	function bytesToSize(bytes) {
		var sizes = [ 'Bytes', 'KB', 'MB', 'GB', 'TB' ];
		if (bytes == 0)
			return '0 Byte';
		var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
		return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
	};
	
	
	function  createBar(fileName, fileSize, arrayBoolean, id ) {
		
		
		
		if ($("#formDIV\\:qwfileupload").css('display') == 'none') {
			PF('uploadFile').show();
		}
		
		if (fileName.length > 40) {
			fileName = fileName.substring(0, 40) + "...";
		}
		
		var size = bytesToSize(fileSize);
		
		var  divProgress  = document.createElement('DIV');
		divProgress.className = 'divProgress';
		divProgress.id = 'divProgress' + id;
		
		var divFileName = document.createElement('DIV');
		var node1 = document.createTextNode(fileName);
		divFileName.id = 'fileNameLabel';
		divFileName.style = 'ont-weight: bold';
		divFileName.appendChild(node1);
	
		divProgress.appendChild(divFileName);
		
		var progress = document.createElement('DIV');
		progress.className = 'progress';
		progress.id = 'progress' + id;
		
		
		var bar = document.createElement('DIV');
		bar.className = 'bar';
		bar.id = 'bar' + id;
		progress.appendChild(bar);
		
		var percent = document.createElement('DIV');
		percent.className = 'percent';
		percent.id = 'percent' + id;
		var node = document.createTextNode("0%");
		percent.appendChild(node);
		progress.appendChild(percent);
		
		var buttonCan = document.createElement('INPUT');
		buttonCan.type = 'BUTTON';
		buttonCan.style = 'cursor:pointer;border: none;display: none;float: left;margin-left: 340px;background: url(./resources/images/Icon_close_32.png) no-repeat;width: 32px;height: 32px';
//		buttonCan.className = ".ui-button  .ui-state-default, .ui-widget-content  .ui-widget-header btn btn-default fileinput-button";
		buttonCan.id = 'abortUpload' + id ;
		buttonCan.title = 'Cancelar Transferência';
		divProgress.appendChild(buttonCan);

		var buttonCan1 = document.createElement('INPUT');
		buttonCan1.type = 'BUTTON';
		buttonCan1.style = 'cursor:pointer;border: none;display: block;float: left;margin-left: 340px;background: url(./resources/images/Icon_close_32.png)no-repeat;width: 32px;height: 32px';
//		buttonCan1.className = ".ui-button  .ui-state-default, .ui-widget-content .ui-widget-header btn btn-default  fileinput-button ";
		buttonCan1.id = 'cancelarUpload' + id ;
		buttonCan1.addEventListener("click", function () {
			arrayBoolean[id] = 'false'; 
			$('#divProgress' + id).remove();
		});
		
		buttonCan.title = 'Cancelar Transferência';
		divProgress.appendChild(buttonCan1);
		
		divProgress.appendChild(progress);
		
		var byteSend  = document.createElement('LABEL');
		byteSend.id = 'byteSend' + id;
		divProgress.appendChild(byteSend);
		
		document.getElementById('divProgessContainer').appendChild(divProgress);
		
	}
	
	
