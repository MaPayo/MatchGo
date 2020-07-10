
/**
 * Actions to perform once the page is fully loaded
 */

document.addEventListener("DOMContentLoaded", () => {

	if (config.socketUrl) {
		let subs = config.admin ? 

			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	/*
	  document.getElementById("mostrarTags").addEventListener("click",function() {
			document.getElementById("mostrarTags").classList.add("bgdarkgrey");
			document.getElementById("listaEventosCreados").classList.remove("bgdarkgrey");
			document.getElementById("listaEventosInscritos").classList.remove("bgdarkgrey");
			document.getElementById("listaComentarios").classList.remove("bgdarkgrey");
			var pageURL = window.location.href;
			var lastURLSegment = pageURL.substr(pageURL.lastIndexOf('/') + 1);
			go(config.rootUrl + "tag/listTags/"+ lastURLSegment,"POST",null).then(e => console.log("holaaaaaaaaaa"));


	  });

*/
	//1 habilita modificar el perfil, 2 guarda los cambios
	document.getElementById("ModificarPerfil").addEventListener("click",function() {
		let boton =  document.getElementById("ModificarPerfil");
		if(boton.dataset.action == 1){
			boton.dataset.action = 2;
			let children = document.getElementById("datosBasicosPerfil").children;
			for (i = 0; i < children.length; i++) {
				children[i].removeAttribute("readonly");
				children[i].removeAttribute("disabled");
			}
			boton.value = "Guardar cambios";
		}else{
			boton.dataset.action = 1;
			let name = document.getElementById("nombre").value;
			let fecha = document.getElementById("edad").value;
			let gender = document.getElementById("genderUser").value;
			go(config.rootUrl + "user/modificarPerfil/","POST",{name:name, fecha:fecha, gender:gender});

			document.getElementById("nombre").readOnly= true;
			document.getElementById("edad").readOnly= true;
			document.getElementById("genderUser").setAttribute("disabled",true);
			boton.value = "Modificar perfil";
		}
	});  



	const targets = document.getElementsByClassName("event-item");
	let pitchers = document.querySelectorAll("input[type=radio]");
	const dateTime = new Date();
	const now  = dateTime.getFullYear()+"/"+dateTime.getMonth()+"/"+dateTime.getDay()+" "+dateTime.getHours()+":"+dateTime.getMinutes();
	for (j=0;j<pitchers.length;j++){
		pitchers[j].addEventListener("change",function(){
			const valWho = document.querySelector("input[name=who]:checked").value;
			const valWhen = document.querySelector("input[name=when]:checked").value;
			for (i=0;i<targets.length;i++){
				targets[i].removeAttribute("hidden");
			}
			for (i=0;i<targets.length;i++){
				if(valWho == 1){
					if(targets[i].dataset.me == 0){
						targets[i].setAttribute("hidden",1);
					}
				} else if(valWho == 2){
					if(targets[i].dataset.me == 1){
						targets[i].setAttribute("hidden",1);
					}
				}

				if(valWhen == 1){
					if(targets[i].dataset.when < now){
						targets[i].setAttribute("hidden",1);
					}
				} else if(valWhen == 2){
					if(targets[i].dataset.when > now){
						targets[i].setAttribute("hidden",1);
					}
				}
			}
		});
	}

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

