var url = 'http://' + window.location.host + '/PIXINGUIA';
var urlLoader = url + '/images/loading_spinner.gif';
var server = "http://"+window.location.hostname+"/QWCFPBETA/";

function pagina(url) {
    var mreq;
    if (window.XMLHttpRequest) {
        mreq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        mreq = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("Seu navegador não tem suporte a AJAX.");
    }
    mreq.onreadystatechange = function () {
        if (mreq.readyState == 1) {
            document.getElementById('conteudo').innerHTML = '<img src=\"' + urlLoader + '\" alt=\"Carregando\" class=\"center\" />';
        } else if (mreq.readyState == 4) {
            document.getElementById('conteudo').innerHTML = mreq.responseText;
        }
    };
    mreq.open("get", url, true);
    mreq.send(null);
}

$(document).click(function() {
	
	//definir evento "onclick" do elemento (botao) ID butEnviar 
	$("#butEnviar").click(function() {
		//capturar o valor dos campos do fomulario
		var nome  = $("input[name=nome]").val();
		var email =  $("input[name=email]").val();

		//usar o metodo ajax da biblioteca jquery para postar os dados em processar.php
		$.ajax({
			"url": "task_common.php",
			"dataType": "html",
			"data": {
				"nome" : nome,
				"email":email 
			},
			"success": function(response) {
				//em caso de sucesso, a div ID=saida recebe o response do post
				$("div#divmain").html(response);
			}

		});
	});
});

function myFunction() {
    document.getElementById("divmain").target = "_self";
    document.getElementById("divmain").innerHTML = "The value of the target attribute was changed.";
}

var server = "http://"+window.location.hostname+"/QWCFPBETA/";

function showUser(str) {
    if (str == "") {
        document.getElementById("divmain").innerHTML = "";
        return;
    } else { 
        if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                document.getElementById("txtHint").innerHTML = xmlhttp.responseText;
            }
        };
        xmlhttp.open("GET","tasks.php?q="+str,true);
        xmlhttp.send();
    }
}