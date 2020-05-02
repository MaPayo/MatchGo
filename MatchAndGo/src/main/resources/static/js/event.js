
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {

	if (config.socketUrl) {
		let subs = config.admin ? 

			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	var pageURL = window.location.href;
	var lastURLSegment = pageURL.substr(pageURL.lastIndexOf('/') + 1);
	console.log(lastURLSegment);
	go(config.rootUrl + "event/u/"+lastURLSegment,"POST",null).then(e => listUsers(e,"updateUsersEvent"));

	//response.forEach(e => );

	// add your after-page-loaded JS code here; or even better, call 
	// 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
	//   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});



function listUsers(jsonArray, type){
	switch(type){
		case "updateUsers":
			var node = document.getElementById("contUsers");
			while (node.firstChild) {
				node.removeChild(node.lastChild);
			}
			jsonArray.forEach(e => appendChild(node,e,type));
			break;
		case "updateUsersEvent":
			node = document.getElementById("contUsers");
			while (node.firstChild) {
				node.removeChild(node.lastChild);
			}
			jsonArray.forEach(e => appendChild(node,e,type));
			var elements = document.getElementsByClassName("anUser");
			for (i = 0; i < elements.length;i++){
				elements[i].addEventListener("click",function() {
					go(config.rootUrl + "event/valorations/"+this.dataset.id,"POST",null).then(e => listUsers(e,"updateListValuations"));
				});
			}

			break;
		case "updateListValuations":
			var node = document.getElementById("contEvaluations");
			while (node.firstChild) {
				node.removeChild(node.lastChild);
			}
			jsonArray.forEach(e => appendChild(node,e,type));
			break;
	}
}


function appendChild(where,element, type){

	let html;
	switch(type){
		case "updateListValuations":
			html = ["<div class='anUser' data-id='"+element.id+"'>" +
				"<span>"+ element.evaluator +" - "+element.score+" - "+element.review+"</span>" + 
				"</div>"];
			break;
		case "updateUsersEvent":
			html = ["<div class='anUser' data-id='"+element.id+"'>" +
				"<span>"+ element.username +" - "+element.firstName+"</span>" + 
				"</div>"];
			break;
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
	where.insertAdjacentHTML('beforeend',html);
}

