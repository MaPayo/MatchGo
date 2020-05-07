
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
				childrens[i].classList.remove("bgblue");
			}
			event.target.classList.add("bgblue");
			const command = event.target.dataset.action;
			go(config.rootUrl + event.target.dataset.id,"POST",null).then(result => listUsers(result,command));
		}
	});

	go(config.rootUrl + "admin/userlist","POST",null).then(e => listUsers(e,"updateUsers"));
});

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
			html = ["<div class='margin1030 bggreen eventCard'>" + 
				"<div class='cardUpperContainer'>" +
				"<h2>"+element.tag+"</h2>" + 
				"</div>" +
				"<div class='cardLowerContainer'>" +
				"<form method='post' action='/admin/deleteUser'>" +
				"<input hidden readonly name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden readonly type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Eliminar' />" +
				"</form>" +
				"</div>" +
				"</div>"];
			break;
		case "updateUsers":
			html = ["<div class='margin1030 bggreen eventCard'>" + 
				"<div class='cardUpperContainer'>" +
				"<h2 id='nombre'><span>"+ element.username +" - "+element.firstName+" "+ element.lastName+"</span></h2>" + 
				"</div>" +
				"<div class='cardLowerContainer'>" +
				"<p id='edad'><span>"+ element.birthDate +"</span></p>" +
				"<p id='sexo'><span>"+ element.gender +"</span></p>" +
				"<form method='post' action='/admin/deleteUser'>" +
				"<input hidden readonly name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden readonly type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Eliminar' />" +
				"</form>" +
				"<form method='post' action='/admin/blockUser?id="+ element.id +"'>" +
				"<input hidden readonly name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden readonly type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Bloquear' />" +
				"</form>" +
				"</div>" +
				"</div>"];
			break;
		case "updateEvents":
			var dt = new Date(element.date);
			var when = dt.getDate()+"-"+(dt.getMonth()+1)+"-"+dt.getFullYear();
			dt = new Date(element.publicationDate);
			var publicationDate =  dt.getDate()+"-"+(dt.getMonth()+1)+"-"+dt.getFullYear();
			 html = ["<div class='margin1030 bggreen eventCard'>"+
				"<div class='cardUpperContainer'>"+
				"<h2>"+element.name+"</h2><div class='width100 textalignright'> Para: <span>"+when+"</span> Publicada: <span>"+publicationDate+"</span>"+
				"<div class='displayflex textalignright'>"+
				"<form method='post' class='width100' action='/admin/deleteEvent'>" +
				"<input hidden readonly name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden readonly type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Eliminar' />" +
				"</form>" +
				"<form method='post' action='/admin/blockEvent?id="+ element.id +"'>" +
				"<input hidden readonly name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden readonly type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Bloquear' />" +
				"</form>" +
				"</div>"+
				 "</div>"+
				"</div>"+
				"<div class='cardLowerContainer'>"+
				"<div>"+
				"<div><span>"+element.description+"</span></div>"+
				"<div class='tagBox'>" +
				"</div>"+
				"</div>"+
				"</div>"+
				"</div>"];
			break;
	}
	document.getElementById("contUsers").insertAdjacentHTML('beforeend',html);
}

