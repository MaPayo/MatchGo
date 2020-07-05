
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {
	//subscriptions to ws
	if (config.socketUrl) {
		let subs = config.admin ? 
			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	go(config.rootUrl + "admin/eventlist","POST",null).then(e => sendSearch(e));
	//listener search enter key ovr title
	document.getElementById("textS").addEventListener("keypress",function(keyPress) {
		if (keyPress.keyCode == 13){
			event.preventDefault();
			go(config.rootUrl + "admin/eventlist","POST",null).then(e => sendSearch(e));
		}
	});

	//listener search
	document.getElementById("search").addEventListener("click",function() {
		var flag = false;
		if (document.getElementById("dateFS").value != "" && document.getElementById("dateTS").value != ""){
			flag = (document.getElementById("dateTS").min > document.getElementById("dateTS").value) ? true:false; 
		}
		if (!flag){
			go(config.rootUrl + "admin/eventlist","POST",null).then(e => sendSearch(e));
		} else{
			alert("compruebe las fechas");
		}
	});

	//date search from date less than to date
	document.getElementById("dateTS").addEventListener("focus",function(){
		document.getElementById("dateTS").min = document.getElementById("dateFS").value; 
	});

});
function sendSearch(jsonArray){
	/**
	 * take all events and compare them with conditions, if all pased append to events
	 **/

	const text = document.getElementById("textS").value;
	const loc = document.getElementById("locS").value;
	const dateF = document.getElementById("dateFS").value;
	const dateT = document.getElementById("dateTS").value;
	const age = document.getElementById("ageS").value;
	const gender = document.getElementById("genderS").value;
	const cat = document.getElementById("tagS").value;
	var arrayEvents = new Array();
	jsonArray.forEach(function(ev){
		var flag_add = false;
		if (!text == ""){
			flag_add = (ev.name.toLowerCase().includes(text) || ev.description.toLowerCase().includes(text)) ? false:true;
		}
		if (!flag_add && !loc == ""){
			flag_add = (ev.location.toLowerCase().includes(loc)) ? false:true;
		}
		if (!flag_add && !gender == ""){
			flag_add = (ev.genderPreference.includes(gender)) ? false:true;
		}
		if (!flag_add && !age == ""){
			flag_add = (ev.agePreference.includes(age)) ? false:true;
		}
		if (!flag_add && !dateF == "" || !dateT == ""){
			if (!dateF == "" && !dateT == ""){
				flag_add = (Date.parse(ev.date) >= Date.parse(dateF) && Date.parse(ev.date) <= Date.parse(dateT)) ? false:true;
			} else if (!dateF == ""){
				flag_add = (Date.parse(ev.date) >= Date.parse(dateF)) ? false:true;
			} else if (!dateT == ""){
				flag_add = (Date.parse(ev.date) <= Date.parse(dateT)) ? false:true;
			}

		}
		if (!flag_add && !cat == ""){
			flag_add = (ev.tagNames.includes(cat)) ? false:true;
		}
		if (!flag_add){
			arrayEvents.push(ev);
		}
	});
	listUsers(arrayEvents, "updateEvents");
}

function listUsers(jsonArray, type){
	switch(type){
		case "updateEvents":
			var node = document.getElementById("events");
			while (node.firstChild) {
				node.removeChild(node.lastChild);
			}
			jsonArray.forEach(e => appendChild(node,e,type));
			break;
		case "sayGoodBye":
			alert("admin say u goodbye :(");
			window.location.href = "/user/logout";
			break;
	}
}


function appendChild(where,element, type){

	let html;
	switch(type){
		case "updateEvents":
			if (config.admin){
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
			} else {
				html=`<div class="eventCard">
				<a href="/event/${element.id}">
				<div class="cardUpperContainer">
				<img src="/img/${element.id}.png" alt="imagen evento ${element.name}" class="placeImage">
				<h2>${element.name}</h2>
				</div>
				<div class="cardLowerContainer">
				<div>
				<div>${element.description}</div>
				<div class="tagBox">`;
				element.tagNames.forEach(e => html +=  `<span class="tag">${e}</span>`);
				html += `</div>
				</div>
				</div>
				</a>
				</div>`;
			}
			break;
	}
	where.insertAdjacentHTML('beforeend',html);
}

