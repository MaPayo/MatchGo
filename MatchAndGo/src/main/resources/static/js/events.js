
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {
	if (config.socketUrl) {
		let subs = config.admin ? 

			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	document.getElementById("textS").addEventListener("keypress",function(keyPress) {
		if (keyPress.keyCode == 13){
			event.preventDefault();
			go(config.rootUrl + "admin/eventlist","POST",null).then(e => sendSearch(e));
		}
	});
	document.getElementById("search").addEventListener("click",function() {
		go(config.rootUrl + "admin/eventlist","POST",null).then(e => sendSearch(e));
	});

	//response.forEach(e => );

	// add your after-page-loaded JS code here; or even better, call 
	// 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
	//   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
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
			flag_add = (ev.name.includes(text) || ev.description.includes(text)) ? false:true;
		}
		if (!flag_add && !loc == ""){
			flag_add = (ev.location.includes(loc)) ? false:true;
		}
		if (!flag_add && !gender == ""){
			flag_add = (ev.genderPreference.includes(gender)) ? false:true;
		}
		if (!flag_add && !age == ""){
			flag_add = (ev.agePreference.includes(age)) ? false:true;
		}
		if (!flag_add && !dateF == "" || !dateT == ""){
			if (!dateF == "" && !dateT == ""){
				flag_add = (Date.parse(ev.date) <= Date.parse(dateF) && Date.parse(ev.date) >= Date.parse(dateT)) ? false:true;
			} else if (!dateF == ""){
				flag_add = (Date.parse(ev.date) <= Date.parse(dateF)) ? false:true;
			} else if (!dateT == ""){
				flag_add = (Date.parse(ev.date) >= Date.parse(dateT)) ? false:true;
			}

		}
		if (!flag_add && !cat == ""){
			flag_add = (ev.tagNames.includes(cat)) ? false:true;
		}
		if (!flag_add){
			arrayEvents.push(ev);
		}
	});
	console.log(arrayEvents);
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

			html="<div class='eventCard'>"+
				"<div class='cardUpperContainer'>"+
				"<img th:src='/img/noImage.png' alt='' class='placeImage'>"+
				"<h2>"+element.name+"<a href='/event/"+element.id+"' class='cgreen'> Go</a></h2>"+
				"</div>"+
				"<div class='cardLowerContainer'>"+
				"<div>"+
				"<div>"+element.description+"</div>"+
				"<div class='tagBox'>";
			element.tagNames.forEach(e => html +=  "<span class='tag'>"+e+"</span>");
			html +=
				"</div>"+
				"</div>"+
				"</div>"+
				"</div>";
			break;
	}
	where.insertAdjacentHTML('beforeend',html);
}

