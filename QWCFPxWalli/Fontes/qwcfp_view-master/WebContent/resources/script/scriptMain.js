function $id(id) {
	return document.getElementById(id);
}

$( document ).ready(function() {
    if (window.File  && window.FileList && window.FileReader) {
		Init();
	}
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


function FileDragHover(e) {
	e.stopPropagation();
	e.preventDefault();
	e.target.className = (e.type == "dragover" ? "hover" : "");
}
