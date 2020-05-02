
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {
	
	if (config.socketUrl) {
		let subs = config.admin ? 

			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	document.getElementById("listEvents").addEventListener("click",function() {
		document.getElementById("listEvents").classList.add("bgblue");
		document.getElementById("listUsers").classList.remove("bgblue");
		go(config.rootUrl + "admin/eventlist","POST",null).then(e => listUsers(e,"updateEvents"));
	});
	document.getElementById("listUsers").addEventListener("click",function() {
		document.getElementById("listEvents").classList.remove("bgblue");
		document.getElementById("listUsers").classList.add("bgblue");
		go(config.rootUrl + "admin/userlist","POST",null).then(e => listUsers(e,"updateUsers"));
	});

	go(config.rootUrl + "admin/userlist","POST",null).then(e => listUsers(e,"updateUsers"));

	//response.forEach(e => );

	// add your after-page-loaded JS code here; or even better, call 
	// 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
	//   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});



function listUsers(jsonArray, type){
	const node = document.getElementById("contUsers");
	while (node.firstChild) {
		node.removeChild(node.lastChild);
	}
	switch(type){
		case "updateUsers":
			jsonArray.forEach(e => appendChild(e,type));
			break;
		case "updateEvents":
			jsonArray.forEach(e => appendChild(e,type));
			break;
	}
}


function appendChild(element, type){

	let html;
	switch(type){
		case "updateUsers":
			html = ["<div class='eventCard bgwhite'>" + 
				"<div class='cardUpperContainer'>" +
				"<h2 id='nombre'><span>"+ element.username +" - "+element.firstName+" "+ element.lastName+"</span></h2>" + 
				"</div>" +
				"<div class='cardLowerContainer'>" +
				"<p id='edad'><span>"+ element.birthDate +"</span></p>" +
				"<p id='sexo'><span>"+ element.gender +"</span></p>" +
				"<form method='post' action='/admin/deleteUser'>" +
				"<input type='hidden' name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Eliminar' />" +
				"</form>" +
				"<form method='post' action='/admin/blockUser?id="+ element.id +"'>" +
				"<input type='hidden' name='_csrf' value='"+config.csrf.value+"' />" +
				"<input hidden type='number' name='id' value="+ element.id +">" +
				"<input type='submit' class='declineButton' value='Bloquear' />" +
				"</form>" +
				"</div>" +
				"</div>"];
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
	document.getElementById("contUsers").insertAdjacentHTML('beforeend',html);
}

