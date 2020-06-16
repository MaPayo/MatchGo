
/**
 * Actions to perform once the page is fully loaded
 */
  
  document.addEventListener("DOMContentLoaded", () => {
	  
	  if (config.socketUrl) {
			let subs = config.admin ? 

				["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
			ws.initialize(config.socketUrl, subs);
		}
	  
	  
	  document.getElementById("mostrarTags").addEventListener("click",function() {
			document.getElementById("mostrarTags").classList.add("bgdarkgrey");
			document.getElementById("listaEventosCreados").classList.remove("bgdarkgrey");
			document.getElementById("listaEventosInscritos").classList.remove("bgdarkgrey");
			document.getElementById("listaComentarios").classList.remove("bgdarkgrey");
			var pageURL = window.location.href;
			var lastURLSegment = pageURL.substr(pageURL.lastIndexOf('/') + 1);
			go(config.rootUrl + "tag/listTags/"+ lastURLSegment,"POST",null).then(e => console.log("holaaaaaaaaaa"));

	  
	  });
	  
	  document.getElementById("ModificarPerfil").addEventListener("click",function() {
		 
		  go(config.rootUrl + "user/modificarPerfil","POST",null);

	  
	  });
	  document.getElementById("listaTags").addEventListener("click",function() {
		  if(event.target.matches("img")){
				const id = event.target.dataset.tag;
				go(config.rootUrl + "tag/deleteTagUser/"+id,"POST",null);

		  }
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
	const datosPerfil =document.getElementById("datosBasicosPerfil");
	switch(type){
		case "updateTagUser":
		case "deleteTagUser":
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
		case "updateUserPage":
			while (datosPerfil.firstChild ) {
				datosPerfil.removeChild(datosPerfil.lastChild);
			}

			jsonArray.forEach(e => appendChild(e,type));
			break;
	}
}


function appendChild(element, type){

	let html;
	let modificarId;
	switch(type){
		case "updateTagUser":
		case "deleteTagUser":
			modificarId= "listaTags";
			html = ["<li> <span>"+element.tag+"</span>" +
				"<img class='icono' data-tag= "+element.id+" src='/img/delete.png'> </li>"];
			break;
		case "updateSelectTag":
			modificarId = "tagSeleccionada";
			html =["<option value="+element.id+">"+element.tag+"</option>"];
			break;
		case "updateUserPage":
		/*	modificarId="datosBasicosPerfil";
			html=["<h2 id='nombre'> <span" + element.firstName + "></span></h2>" ];
             th:text="${user.firstName} "></span></h2>      
            <p id="edad"><span th:text="${user.birthDate}"></span></p>	   
            <p id="sexo"><span th:text="${user.gender}"></span></p>	 
*/
			break
	}
	document.getElementById(modificarId).insertAdjacentHTML('beforeend',html);
}

