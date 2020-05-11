document.addEventListener("DOMContentLoaded", () => {
    
    let elements = document.getElementsByClassName("bContacto");
    
    for (let i = 0; i < elements.length; i++) {
        elements[i].addEventListener("click", function () {
            
            requestMessages(elements[i].getAttribute('data-id'));
        });   
    }
});


function requestMessages(id) {
    go(config.rootUrl + "messages/" + id, "GET").then(json => updateMessages(json));
}

function updateMessages(json) {
    let div = document.getElementById("M");

    //Borramos los mensajes
    while (div.firstChild) {
        div.removeChild(div.lastChild);
    }

    //Introducimos los nuevos mensajes
    json.forEach(m => {
        let html = [];
        html +="<div class='mensaje'>";
        
        if (m.sender == config.usuario) {
            html+="<div class='mensajeMio'>";
            html+="<pre> "+ m.textMessage +"</pre>";
            html+="</div>";
        } else {
            html+="<div class='mensajeContacto'>";
            html+="<pre> "+ m.textMessage +"</pre>";
            html+="</div>";
        }
        
        html+="</div>";

        div.insertAdjacentHTML('beforeend',html);
    });
}

/*

// Asocia los botones de los contactos con recarga de chats
document.addEventListener("DOMContentLoaded", () => {
    document.getElementsByClassName("bContacto").forEach(b => {
        // https://developer.mozilla.org/en-US/docs/Learn/HTML/Howto/Use_data_attributes
        b.onclick = e => {
            idContactoActual = b.dataset.id;
            requestMessages(b.dataset.id);
        }
    })

    ws.receive = (o) => {
        // asume que añades un senderId en los mensajes emitidos desde el controlador...
        if (o.senderId == idContactoActual) {
            requestMessages(idContactoActual);
        }
    }
})
*/