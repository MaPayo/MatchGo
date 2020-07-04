
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {

	if (config.socketUrl) {
		let subs = config.admin ? 

			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	document.getElementById("menuDisplayE").addEventListener("click",function() {
		if(event.target.matches("li")){
			const childrens =  document.getElementById("menuDisplayE").children;
			for (i = 0; i < childrens.length; i++) {
				childrens[i].classList.remove("bglightgrey");
			}
			event.target.classList.add("bglightgrey");
			const id = event.target.dataset.id;
			const command = event.target.dataset.action;
			document.getElementById("textS").dataset.id = id;//update finder
			go(config.rootUrl + id,"POST",null).then(result => listUsers(result,command));
		}
	});

	document.getElementById("textS").addEventListener("keypress",function(key) {
		if (key.keyCode == 13){
			const whatFind = document.getElementById("textS").dataset.id;
			go(config.rootUrl + whatFind,"POST",null).then(e => finder(e,whatFind));
		}
	});
	go(config.rootUrl + "admin/userlist","POST",null).then(e => listUsers(e,"updateUsers"));
});

function finder(jsonArrayEvents, whatFind){

	const text = document.getElementById("textS").value;
	var arrayToPrint = new Array();

	switch(whatFind){
		case "admin/eventlist":
			jsonArrayEvents.forEach(function(ev){
				var flag_add = false;
				if (!text == ""){
					flag_add = (ev.name.toLowerCase().includes(text) || ev.description.toLowerCase().includes(text)) ? true:false;
				}
				if (!flag_add){
					flag_add = (ev.location.toLowerCase().includes(text)) ? true:false;
				}
				if (!flag_add){
					flag_add = (ev.genderPreference.includes(text)) ? true:false;
				}
				if (!flag_add){
					flag_add = (ev.agePreference.includes(text)) ? true:false;
				}
				if (!flag_add){
					if (!dateF == "" && !dateT == ""){
						flag_add = (Date.parse(ev.date) >= Date.parse(dateF) && Date.parse(ev.date) <= Date.parse(dateT)) ? false:true;
					} else if (!dateF == ""){
						flag_add = (Date.parse(ev.date) >= Date.parse(dateF)) ? false:true;
					} else if (!dateT == ""){
						flag_add = (Date.parse(ev.date) <= Date.parse(dateT)) ? false:true;
					}	
				}
				if (!flag_add){
					flag_add = (ev.tagNames.includes(text)) ? true:false;
				}
				if (flag_add){
					arrayToPrint.push(ev);
				}
			});
			console.log(arrayToPrint);
			listUsers(arrayToPrint,"updateEvents");
			break;
		case "admin/userlist":
			jsonArrayEvents.forEach(function(ev){
				var flag_add = false;
				if (!text == ""){
					flag_add = (ev.firstName.toLowerCase().includes(text)) ? true:false;
				}
				if (!flag_add){
					flag_add = (ev.lastName.toLowerCase().includes(text)) ? true:false;
				}
				if (!flag_add){
					flag_add = (ev.username.toLowerCase().includes(text)) ? true:false;
				}
				if (!flag_add){
					flag_add = (ev.email.toLowerCase().includes(text)) ? true:false;
				}
				if (!flag_add){
					flag_add = (ev.gender.toLowerCase().includes(text)) ? true:false;
				}

			//	if (!flag_add){
			//		if (!dateF == "" && !dateT == ""){
			//			flag_add = (Date.parse(ev.date) >= Date.parse(dateF) && Date.parse(ev.date) <= Date.parse(dateT)) ? false:true;
			//		} else if (!dateF == ""){
			//			flag_add = (Date.parse(ev.date) >= Date.parse(dateF)) ? false:true;
			//		} else if (!dateT == ""){
			//			flag_add = (Date.parse(ev.date) <= Date.parse(dateT)) ? false:true;
			//		}	
			//	}
				if (!flag_add){
					flag_add = (ev.tags.includes(text)) ? true:false;
				}
				if (flag_add){
					arrayToPrint.push(ev);
				}
			});
			console.log(arrayToPrint);
			listUsers(arrayToPrint,"updateUsers");
			break;
		case "admin/taglist":
			jsonArrayEvents.forEach(function(ev){
				var flag_add = false;
				if (!text == ""){
					flag_add = (ev.tag.toLowerCase().includes(text)) ? true:false;
				}
				if (flag_add){
					arrayToPrint.push(ev);
				}
			});
			console.log(arrayToPrint);
			listUsers(arrayToPrint,"updateTags");
			break;
	}
}

function listUsers(jsonArray, type){
	const node = document.getElementById("contUsers");
	while (node.firstChild) {
		node.removeChild(node.lastChild);
	}
	switch(type){
		case "updateTags":
		case "updateEvents":
		case "updateUsers":
			jsonArray.forEach(e => appendChild(e,type));
			break;
		case "pleaseExit":
			alert("Sorry but this page just deleted u must go other");
			window.location.href = "/event/";
			break;
		case "sayGoodBye":
			alert("admin say u goodbye :(");
			window.location.href = "/user/logout";
			break;
	}
}


function appendChild(element, type){

	let html;
	switch(type){
		case "updateTags":
			html = ["<div class='widthauto bggreen eventCard'>" + 
				"<div class='cardUpperContainer'>" +
				"<h2>"+element.tag+"</h2>" + 
				"</div>" +
				"<div class='cardLowerContainer'>" +
				"<form method='post' action='/admin/deleteTag'>" +
				"<input hidden readonly name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden readonly type='number' name='id' value="+ element.id +">" +
				"<button type='submit' class='button declineButton'><span>Eliminar</span></button>" +
				"</form>" +
				"</div>" +
				"</div>"];
			break;
		case "updateUsers":
			var textBlockDesblock = (element.enabled)?('Bloquear'):('Desbloquear');
			html = `<div class="bggreen eventCard">
				<div class="cardUpperContainer">
				<h2 id="nombre"><span>${element.username} - ${element.firstName} ${element.lastName}</span></h2>
				</div>
				<div class="cardLowerContainer">
				<p id="edad"><span>${element.birthDate}</span></p>
				<p id="sexo"><span>${element.gender}</span></p>
				<form method="post" action="/admin/deleteUser">
				<input hidden readonly name="_csrf" value="${config.csrf.value}" />
				<input hidden readonly type="number" name="id" value="${element.id}">
				<button type="submit" class="button declineButton"><span>Eliminar</span></button>
				</form>
				<form method="post" action="/admin/blockUser?id=${element.id}">
				<input hidden readonly name="_csrf" value="${config.csrf.value}" />
				<input hidden readonly type="number" name="id" value="${element.id}">
				<button type="submit" class="button warnButton"><span>${textBlockDesblock}</span></button>
				</form>
				</div>
				</div>`;
			break;
		case "updateEvents":
			var dt = new Date(element.date);
			var when = dt.getDate()+"-"+(dt.getMonth()+1)+"-"+dt.getFullYear();
			dt = new Date(element.publicationDate);
			var publicationDate =  dt.getDate()+"-"+(dt.getMonth()+1)+"-"+dt.getFullYear();
			var textBlockDesblock = (!element.isAppropriate)?('Bloquear'):('Desbloquear');
			html = `<div class="bggreen eventCard">
				<div class="cardUpperContainer">
				<h2>${element.name}</h2><div class="width100 textalignright"> Para: <span>${when}</span> Publicada: <span>${publicationDate}</span>
				<div class="displayflex textalignright">
				<form method="post" class="width100" action="/admin/deleteEvent">
				<input hidden readonly name="_csrf" value="${config.csrf.value}"/>
				<input hidden readonly type="number" name="id" value="${element.id}">
				<button type="submit" class="button declineButton"><span>Eliminar</span></button>
				</form>
				<form method="post" action="/admin/blockEvent?id=${element.id}">
				<input hidden readonly name='_csrf' value="${config.csrf.value}"/>
				<input hidden readonly type="number" name="id" value="${element.id}">
				<button type="submit" class="button warnButton"><span>${textBlockDesblock}</span></button>
				</form>
				</div>
				</div>
				</div>
				<div class="cardLowerContainer">
				<div>
				<div><span>${element.description}</span></div>
				<div class="tagBox">
				</div>
				</div>
				</div>
				</div>`;
			break;
	}
	document.getElementById("contUsers").insertAdjacentHTML('beforeend',html);
}

