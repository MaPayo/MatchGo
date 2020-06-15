
/**
 * Actions to perform once the page is fully loaded
 */
  
  document.addEventListener("DOMContentLoaded", () => {
	  
	  if (config.socketUrl) {
			let subs = config.admin ? 

				["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
			ws.initialize(config.socketUrl, subs);
		}
	  
	  
	  document.getElementById("listaTags").addEventListener("click",function() {
			document.getElementById("listaTags").classList.add("bgblue");
			document.getElementById("listaEventosCreados").classList.remove("bgblue");
			document.getElementById("listaEventosInscritos").classList.remove("bgblue");
			document.getElementById("listaComentarios").classList.remove("bgblue");
			var pageURL = window.location.href;
			var lastURLSegment = pageURL.substr(pageURL.lastIndexOf('/') + 1);
			go(config.rootUrl + "tag/listTags/"+ lastURLSegment,"POST",null).then(e => console.log("holaaaaaaaaaa"));

	  
	  });
	  document.getElementById("botonAnadirTag").addEventListener("click",function() {
			
		  	let idTag = document.getElementById("tagSeleccionada").value;

			go(config.rootUrl + "tag/addTagUser/"+idTag,"POST",null);

	  
	  });
	  
	  document.getElementById("tagInput").addEventListener("keypress",function(key) {
			if (key.keyCode == 13){ //pulsa enter
				let tag = document.getElementById("tagInput").value;
				go(config.rootUrl + "tag/newTag","POST",{tagName:tag}).then();

			}
		});
	  

	//response.forEach(e => );

	// add your after-page-loaded JS code here; or even better, call 
	// 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
	//   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});



function listUsers(jsonArray, type){
	const node = document.getElementById("listaTags");
	const select = document.getElementById("tagSeleccionada");
	switch(type){
		case "updateTagUser":

			while (node.firstChild ) {
				node.removeChild(node.lastChild);
			}

			jsonArray.forEach(e => appendChild(e,type));
			break;
		case "updateSelectTag":
			while (select.firstChild ) {
				select.removeChild(select.lastChild);
			}
			
			jsonArray.forEach(e => appendChild(e,type));
			break;
		case "selectTag":
			alert("Please select one tag.");
			break;
		case "tagExists":
			alert("the tag you are trying to insert already exists.")
			break;
			
	}
}


function appendChild(element, type){

	let html;
	let modificarId;
	switch(type){
		case "updateTagUser":
			modificarId= "listaTags";
			html = ["<li>"+element.tag+ "</li>"];
			break;
		case "updateSelectTag":
			modificarId = "tagSeleccionada";
			html =["<option value="+element.id +">"+element.tag + "</option>"];
			break;
		case "updateEvents":
			 html = ["<div class='eventCard bgwhite'>"+
				"<div class='cardUpperContainer'>"+
				"<img src='/img/"+element.id+".png' alt='Imagen de "+element.name+"' class='placeImage'>"+
				"<h2>"+element.name+"</h2>"+
				"</div>"+
				"<div class='cardLowerContainer'>"+
				"<div>"+
				"<div><span>"+element.description+"</span> Para: <span>"+element.date+"></span> Publicada: <span>"+element.publicationDate+"></span></div>"+
				"<form method='post' action='/admin/deleteEvent'>" +
				"<input type='hidden' name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Eliminar' />" +
				"</form>" +
				"<form method='post' action='/admin/blockEvent?id="+ element.id +"'>" +
				"<input type='hidden' name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Bloquear' />" +
				"</form>" +
				"<div class='tagBox'>" +
				"</div>"+
				"</div>"+
				"</div>"+
				"</div>"];
			break;
	}
	document.getElementById(modificarId).insertAdjacentHTML('beforeend',html);
}

