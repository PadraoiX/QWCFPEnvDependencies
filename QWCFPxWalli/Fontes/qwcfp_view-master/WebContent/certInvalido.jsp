<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>QWCFP - Error</title>


<meta name="keywords"
	content="404 iphone web template, Android web template, Smartphone web template, free webdesigns for Nokia, Samsung, LG, Sony Ericsson, Motorola web design" />


<link href="resources/barcelona-layout/css/nanoscroller.css"
	rel="stylesheet" type="text/css" />
<link href="resources/barcelona-layout/css/animate.css" rel="stylesheet"
	type="text/css" />
<link href="resources/barcelona-layout/css/ripple.css" rel="stylesheet"
	type="text/css" />
<link href="resources/barcelona-layout/css/layout-blue.css"
	rel="stylesheet" type="text/css" />
<link href="resources/barcelona-layout/css/geralBarcelona.css"
	rel="stylesheet" type="text/css" />


<style>
body .ui-button {
	overflow: hidden;
	background-color: #1976d2;
	color: #ffffff;
	height: 30px;
	padding: 0 14px;
	border: 0 none;
	-moz-box-shadow: 0 1px 2.5px 0 rgba(0, 0, 0, 0.26), 0 1px 5px 0
		rgba(0, 0, 0, 0.16);
	-webkit-box-shadow: 0 1px 2.5px 0 rgba(0, 0, 0, 0.26), 0 1px 5px 0
		rgba(0, 0, 0, 0.16);
	box-shadow: 0 1px 2.5px 0 rgba(0, 0, 0, 0.26), 0 1px 5px 0
		rgba(0, 0, 0, 0.16);
	-moz-transition: background-color 0.3s;
	-o-transition: background-color 0.3s;
	-webkit-transition: background-color 0.3s;
	transition: background-color 0.3s;
}

body .ui-button .ui-button-text {
    padding: 0;
    line-height: 30px;
    font-size: 14px;
}
</style>

<script type="text/javascript">

        function loginPage(){
        	var base = document.getElementsByTagName('base')[0];
            if (base && base.href  && (base.href.length > 0)) {
                base = base.href;
            } else {
                base = document.URL;
            }
            var contextRoot  = base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)); 
        	location.href= contextRoot + "/" + "index.faces";
        }
        
        </script>

</head>

<body class="exception-body accessdenied-body">
	<div class="exception-panel">

		<img src="resources/barcelona-layout/images/exception/icon-access.png" />

		<button id="j_idt8" name="j_idt8" type="button"
			class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only error-page-btn"
			onclick="loginPage()" role="button" aria-disabled="false">
			<span class="ui-button-text ui-c">DASHBOARD</span>
		</button>

	</div>

	<div class="exception-band">
		<div class="exception-content">
			<h1 style="width: 444px;">CERTIFICADO DIGITAL</h1>
			<p style="width: 400px;"><%=session.getAttribute("ErrorCode") %>
				:
				<%=session.getAttribute("ErrorMsg") %></p>
		</div>
	</div>

</body>
</html>