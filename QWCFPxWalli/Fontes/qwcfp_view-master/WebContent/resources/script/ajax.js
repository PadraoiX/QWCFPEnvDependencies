function pagina(url) {// INICIO AJAX
	var mreq;
	// Procura o componente nativo do Mozilla/Safari para rodar o AJAX
	if (window.XMLHttpRequest) {
		// Inicializa o Componente XMLHTTP do Mozilla
		mreq = new XMLHttpRequest();
		// Caso ele no encontre, procura por uma verso ActiveX do IE
	} else if (window.ActiveXObject) {
		// Inicializa o Componente ActiveX para o AJAX
		mreq = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
		// Caso no consiga inicializar nenhum dos componentes, exibe um erro
		alert("Seu navegador não tem suporte a AJAX.");
	}

	// Carrega a funo de execuo do AJAX
	mreq.onreadystatechange = function() {
		if (mreq.readyState == 1) {
			// Quando estiver "Carregando a pgina", exibe a mensagem
			document.getElementById('conteudo').innerHTML = 'Carregando';
		} else if (mreq.readyState == 4) {
			// Quando estiver completado o Carregamento
			// Procura pela DIV com o id="minha_div" e insere as informaes
			document.getElementById('conteudo').innerHTML = mreq.responseText;
		}
	};
	// Envia via mtodo GET as informaes
	mreq.open("POST", url, true);
	// mreq.setRequestHeader("Content-Type",
	// "application/x-www-form-urlencoded;charset=utf-8")
	// <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	mreq.send(null);
}// FIM AJAX

function linkVoltar(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
//	var fullUrl = window.location.protocol + "//" + (window.location.host + "/" + contextRoot);
	location.href= contextRoot + "/" + "main.faces";
}
function linkVoltarMobile(){
	location.href="main.faces";
}
function sair(){
	location.href="index.faces";
}
function notificar(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
	location.href= contextRoot + "/" + "listNotify.faces";
}


function config(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
	location.href= contextRoot + "/" + "gestor/configSistema.faces";
}

function arquivosCompartilhados(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
	location.href= contextRoot + "/" + "arquivosCompartilhados.faces";
}




function certificado(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
	location.href= contextRoot + "/" + "LoginServlet";
}


function shared(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
	location.href= contextRoot + "/" + "arquivosCompartilhados.faces";
}
function notificarmob(){
	location.href="listNotifymob.faces";
}
function cpf(v){
    v=v.replace(/\D/g,"")                    //Remove tudo o que não é dígito
    v=v.replace(/(\d{3})(\d)/,"$1.$2")       //Coloca um ponto entre o terceiro e o quarto dígitos
    v=v.replace(/(\d{3})(\d)/,"$1.$2")       //Coloca um ponto entre o terceiro e o quarto dígitos
                                             //de novo (para o segundo bloco de números)
    v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2") //Coloca um hífen entre o terceiro e o quarto dígitos
    return v
}
function reloadPage() {
    location.reload();
}

function listAreas(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
    location.href= contextRoot + "/" + "listArea.faces";
}


function listUser(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
    alert(contextRoot + "/" + "listUser.faces");
    location.href= contextRoot + "/" + "listUser.faces";
}

function alterarUser(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
    location.href= contextRoot + "/" + "alterarUser.faces";
}

function novoGrupo(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
    location.href= contextRoot + "/" + "addGrupo.faces";
}

function listGrupos(){
	var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
    location.href= contextRoot + "/" + "listGrupos.faces";
}



function validaCPF(cpf) {
    if (cpf.length < 11) return false
    var nonNumbers = /\D/
    if (nonNumbers.test(cpf))return false
    if (cpf == "00000000000" || cpf == "11111111111" ||
        cpf == "22222222222" || cpf == "33333333333" ||
        cpf == "44444444444" || cpf == "55555555555" ||
        cpf == "66666666666" || cpf == "77777777777" ||
        cpf == "88888888888" || cpf == "99999999999")
            return false
    var a = []
    var b = new Number
    var c = 11
    for (i=0; i<11; i++){
            a[i] = cpf.charAt(i)
            if (i < 9) b += (a[i] * --c)
    }
    if ((x = b % 11) < 2) { a[9] = 0 } else { a[9] = 11-x }
    b = 0
    c = 11
    for (y=0; y<10; y++) b += (a[y] * c--)
    if ((x = b % 11) < 2) { a[10] = 0 } else { a[10] = 11-x }
    if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]))return false
    return true
}

var server = window.location.pathname   ;
