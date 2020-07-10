function createEventCard(event){
	let cardUpperContainer = `<div class='cardUpperContainer'>
		<img src='/img/noImage.png' alt='no image' class='placeImage'>
			<h2>
				<span>${event.name}</span>
			</h2>
	</div>`;
	
let tagClass = `<div class="tagBox">`

for(let tagName of event.tagNames){
	tagClass += `<span class="tag">${tagName}<span></span></span>`
}
	
tagClass += `</div>`;

let cardLowerContainer = `<div class="cardLowerContainer">
       	<div>
           	<div>
           		<span>${event.description}</span>
           	</div>
              ${tagClass}
        	</div>
        	<div>			
					<button id="moderatorAceptEventBtn" type="button" class="acceptButton" onclick="javascript:aceptEvent(${event.id})">Aceptar</button>
				<button id="moderatorRejectEventBtn" type="button" class="declineButton" onclick="rejectEvent(${event.id})">Rechazar</button>
			</div>
		</div>
	</div>`;


let eventCard = `<div class="eventCard"> ${cardUpperContainer} ${cardLowerContainer}</div>`;

return eventCard;
}

function refreshPage(res){
	var div = document.getElementById('moderator-central-panel');
	
	let eventCards = "";
	for(let event of res.events){
		eventCards += createEventCard(event);
	}
	
	if(res.events.length <= 0){
		eventCards += `<div class='div-centered'> 
							<p>No hay Eventos para verificar.</p>
					   </div>`
	}
	
	div.innerHTML = eventCards;
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

function aceptEvent(id){
	let params = {acept: true};
	params[config.csrf.name] = config.csrf.value;
	
	return go(config.apiUrl + "/moderator/" + id, 'POST', params)
			.then(res => listUsers(res.events,"updateEvents"))
			.catch(() => "nombre de usuario inválido o duplicado");
}

function rejectEvent(id){
	let params = {acept: false};
	params[config.csrf.name] = config.csrf.value;
	
	return go(config.apiUrl + "/moderator/" + id, 'POST', params)
			.then(res => listUsers(res.events,"updateEvents"))
			.catch(() => "nombre de usuario inválido o duplicado");
}
