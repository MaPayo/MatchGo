
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
			sendSearch();
		}
	});
	document.getElementById("search").addEventListener("click",function() {
		sendSearch();
	});

	//response.forEach(e => );

	// add your after-page-loaded JS code here; or even better, call 
	// 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
	//   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});

function sendSearch(){
	


		/**
		 * take all events and compare them with conditions, if all pased append to events
		 **/
	//	List<Event> allEvents = entityManager.createNamedQuery("Event.all", Event.class).getResultList();
	//	for (Event e : allEvents){
			/**
			 * Global flag say if add or not event
			 **/
	//		boolean flag_add = false;
			//Compare tags
	//		if (tag != -1){
	//			boolean flag_find = false;
	//			for (Tags t : e.getTags()){
	//				if (t.getId() == tag){
	//					flag_find = true;
	//					break;
	//				}
	//			}
	//			if (!flag_find){
	//				flag_add = true;
	//			}
	//		}
			//Compare Gender
	//		if (!flag_add && !"".equals(genderToSearch)){
	//			log.warn("comparo genero");
	//			if (!e.getGenderPreference().equals(genderToSearch)){
	//				flag_add = true;
	//			}
	//		}
			//Compare Age
	//		if (!flag_add && !"".equals(ageToSearch)){
	//			log.warn("comparo edad");
	//			if (!e.getAgePreference().equals(ageToSearch)){
	//				flag_add = true;
	//			}
	//		}
			//Compare site
	//		if (!flag_add && !"".equals(siteToSearch)){
	//			log.warn("comparo sitio");
	//			if (!e.getLocation().contains(siteToSearch)){
	//				flag_add = true;
	//			}
	//		}
			//Compare title
	//		if (!flag_add && !"".equals(textToSearch)){
	//			log.warn("comparo titulo");
	//			if (!e.getName().contains(textToSearch)){
	//				log.warn("titulo no acierta");
	//				flag_add = true;
	//			} else{
	//				log.warn(" acierta");
	//			}
	//		}
			//Compare description 
	//		if (!flag_add && !"".equals(textToSearch)){
	//			log.warn("comparo descripcion");
	//			if (!e.getDescription().contains(textToSearch)){
	//				flag_add = true;
	//			}
	//		}
	//		if (!flag_add){
	//			events.add(e);
	//		}
	//	}





	const text = document.getElementById("textS").value;
	const loc = document.getElementById("locS").value;
	const dateF = document.getElementById("dateFS").value;
	const dateT = document.getElementById("dateTS").value;
	const age = document.getElementById("ageS").value;
	const gender = document.getElementById("genderS").value;
	const cat = document.getElementById("tagS").value;
	go(config.rootUrl + "event/eventToSearch","POST",{textS:text,tagS:cat,date_from:dateF,date_to:dateT,locS:loc,ageS:age,genderS:gender}).then(e => listUsers(e,"updateEvents"));
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

