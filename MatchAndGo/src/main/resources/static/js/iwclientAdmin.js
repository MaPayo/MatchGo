/**
 * WebSocket API, which only works once initialized
 */
const ws = {		

	/**
	 * Number of retries if connection fails
	 */
	retries: 3,

	/**
	 * Default action when message is received. 
	 */
	receive: (text) => {
		listUsers(text[1],text[0]);

		console.log("updating view updated list received via socket");
	},

	headers: {'X-CSRF-TOKEN' : config.csrf.value},

	/**
	 * Attempts to establish communication with the specified
	 * web-socket endpoint. If successfull, will call 
	 */
	initialize: (endpoint, subs = []) => {
		try {
			ws.stompClient = Stomp.client(endpoint);
			ws.stompClient.reconnect_delay = (ws.retries -- > 0) ? 2000 : 0;
			ws.stompClient.connect(ws.headers, () => {

				ws.connected = true;
				console.log('Connected to ', endpoint, ' - subscribing...');		        
				while (subs.length != 0) {
					ws.subscribe(subs.pop())
				}
			});			

			console.log("Connected to WS '" + endpoint + "'")
		} catch (e) {
			console.log("Error, connection to WS '" + endpoint + "' FAILED: ", e);
		}
	},


	subscribe: (sub) => {
		try {
			ws.stompClient.subscribe(sub, 
				(m) => ws.receive(JSON.parse(m.body))); 	// falla si no recibe JSON!
			console.log("Hopefully subscribed to " + sub);
		} catch (e) {
			console.log("Error, could not subscribe to " + sub);
		}

	}
} 

/**
 * Sends an ajax request using fetch
 */
//envía json, espera json de vuelta; lanza error si status != 200
function go(url, method, data = {}) {

	let params = {
		method: method, // POST, GET, POST, PUT, DELETE, etc.
		headers: {
			"Content-Type": "application/json; charset=utf-8",
		},
		body: JSON.stringify(data)
	};
	if (method === "GET") {
		delete params.body;
	} else {
		params.headers["X-CSRF-TOKEN"] = config.csrf.value; 
	}  
	console.log("sending", url, params)
	return fetch(url, params)
		.then(response => {
			if (response.ok) {
				return response.json(); // esto lo recibes con then(d => ...)
			} else {
				throw response.text();  // esto lo recibes con catch(d => ...)
			}
		})

}

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
	switch(type){
		case "updateUsers":
			if(document.getElementById("listUsers").classList.contains("bgblue")){
				while (node.firstChild) {
					node.removeChild(node.lastChild);
				}
				jsonArray.forEach(e => appendChild(e,type));
			}
			break;
		case "updateEvents":
			if(document.getElementById("listEvents").classList.contains("bgblue")){
				while (node.firstChild) {
					node.removeChild(node.lastChild);
				}
				jsonArray.forEach(e => appendChild(e,type));
			}
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
				"<h2>"+element.name+"</h2> - <span>"+element.description+"</span>"+
				"</div>"+
				"<div class='cardLowerContainer'>"+
				" Para: <span>"+element.date+"</span> Publicada: <span>"+element.publicationDate+"</span>"+
				"<div class='tagBox'>" +
				"</div>"+
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
				"</div>"+
				"</div>"];
			break;
	}
	document.getElementById("contUsers").insertAdjacentHTML('beforeend',html);
}

