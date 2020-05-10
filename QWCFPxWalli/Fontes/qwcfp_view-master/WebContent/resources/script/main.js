function $id(id) {
	return document.getElementById(id);
}

$(document).ready(function() {
	if (window.File && window.FileList && window.FileReader) {
		Init();
		Init2();
	}
	
	
	$("#cardGroup").click();//GAMBIARARA -> O primeiro click não está funcionando 
	
	
});



function Init() {

	var filedrag = $id("filedrag");

	// is XHR2 available?
	var xhr = new XMLHttpRequest();
	if (xhr.upload) {

		// file drop
		filedrag.addEventListener("dragover", FileDragHover, false);
		filedrag.addEventListener("dragleave", FileDragHover, false);
		filedrag.addEventListener("drop", showFileSize, false);
		filedrag.style.display = "block";

	}

}

function Init2() {

	var listDrags = $(".transforma");


	for (var ii = 0; ii < listDrags.length; ii++) {
		var filedrag = listDrags[ii];//.getAttribute('id');

		// is XHR2 available?
		var xhr = new XMLHttpRequest();
		if (xhr.upload) {

			// file drop
			filedrag.addEventListener("dragover", FileDragHover, false);
			filedrag.addEventListener("dragleave", FileDragHover, false);
			filedrag.addEventListener("drop", showFileSize, false);
//			filedrag.style.display = "block";

		}
		
	}
}

function removeClass(ele,cls) {
   var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');
   ele.className = ele.className.replace(reg,' ');
}

function FileDragHover(e) {
	e.stopPropagation();
	e.preventDefault();
	
	if ($(this).hasClass("transforma")){
		if(e.type == "dragover"){
			var span = $( ".ui-button-icon-left.ui-icon.ui-c.fa.fa-folder" );
			$( this ).addClass( "uploadMain" );
			$(this).find(span).removeClass("fa-folder");
			$(this).find(span).addClass("fa-folder-open");
		}else{
			var span = $( ".ui-button-icon-left.ui-icon.ui-c.fa.fa-folder-open" );
			$( this ).removeClass( "uploadMain" );
			$(this).find(span).addClass("fa-folder");
			$(this).find(span).removeClass("fa-folder-open");
		}
	}
	
	
//	e.target.className = (e.type == "dragover" ? "hover" : "");
	//console.log(e.target);
}

function eventSelected() {
	var addclass = 'color';
	var $cols = $('.transforma').click(function(e) {
		$cols.removeClass(addclass);
		$(this).addClass(addclass);
	});
}

function expandInfo() {
	var addclass = "layout-wrapper-menu-expanded";
//	var info = $("[id='formDIV:groupDetailTab']");
	var mainClass = $(".layout-main"); 
	var expandedClass = $(".layout-sidebar-info");
	
	if (expandedClass.hasClass("layout-wrapper-menu-expanded")) {
		mainClass.css('margin-right', '0px');
		expandedClass.removeClass(addclass);
	}else{
		mainClass.css('margin-right', '310px');
		expandedClass.addClass(addclass);
	}
};
