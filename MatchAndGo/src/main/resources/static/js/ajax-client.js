function aceptEvent(id){
	console.log(config)
	console.log(id);
	let params = {acept: true};
	params[config.csrf.name] = config.csrf.value;
	
	return go(config.apiUrl + "/moderator/" + id, 'POST', params)
			.then(res => console.log(res))
			.catch(() => "nombre de usuario inválido o duplicado");
}

function rejectEvent(id){
	console.log(config)
	console.log(id);
	let params = {acept: false};
	params[config.csrf.name] = config.csrf.value;
	
	return go(config.apiUrl + "/moderator/" + id, 'POST', params)
			.then(res => console.log(res))
			.catch(() => "nombre de usuario inválido o duplicado");
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
	console.log("hhh");
});

