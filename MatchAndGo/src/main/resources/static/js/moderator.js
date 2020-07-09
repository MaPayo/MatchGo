
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
			go(config.rootUrl + id,"POST",null).then(result => listUsers(result,command));
		}
	});

	go(config.rootUrl + "moderator/eventlist","POST",null).then(e => listUsers(e,"updateEvents"));
});

function listUsers(jsonArray, type){
	const node = document.getElementById("contUsers");
	while (node.firstChild) {
		node.removeChild(node.lastChild);
	}
	switch(type){
		case "updateTags":
		case "updateEvents":
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
			html = `<div class="widthauto bggreen eventCard">
				<div class="cardUpperContainer">
				<h2>${element.tag}</h2>
				</div>
				<div class="cardLowerContainer">
				<form method="post" action="/admin/deleteTag">
				<input hidden readonly name="_csrf" value="${config.csrf.value}" />
				<input hidden readonly type="number" name="id" value="${element.id}">
				<button type="submit" class="button declineButton"><span>Eliminar</span></button>
				</form>
				</div>
				</div>`;
			break;
		case "updateEvents":
			var dt = new Date(element.date);
			var when = dt.getDate()+"-"+(dt.getMonth()+1)+"-"+dt.getFullYear();
			dt = new Date(element.publicationDate);
			var publicationDate =  dt.getDate()+"-"+(dt.getMonth()+1)+"-"+dt.getFullYear();
			html = `<div class="bggreen eventCard">
				<div class="cardUpperContainer">
				<img src="/event/${element.id}/photo" algt="imagen del evento ${element.id}.png" class="eventImg">
				<h2>${element.name}</h2><div class="width100 textalignright"> Para: <span>${when}</span> Publicada: <span>${publicationDate}</span>
				<div class="displayflex textalignright">
				<button class="button acceptButton" onclick="javascript:aceptEvent(${element.id});"><span>Aceptar</span></button>
				<button class="button declineButton"  onclick='javascript:rejectEvent(${element.id});'><span>Rechazar</span></button>
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
