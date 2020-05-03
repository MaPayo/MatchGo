
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {
	if (config.socketUrl) {
		let subs = config.admin ? 

			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	document.getElementById("search").addEventListener("click",function() {
		const text = document.getElementById("textS").value;
		const cat = document.getElementById("tagS").value;
		go(config.rootUrl + "event/eventToSearch","POST",{textS:text,tagS:cat}).then(e => listUsers(e,"updateEvents"));
	});

	//response.forEach(e => );

	// add your after-page-loaded JS code here; or even better, call 
	// 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
	//   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});

function listUsers(jsonArray, type){
	switch(type){
		case "updateEvents":
			var node = document.getElementById("events");
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

