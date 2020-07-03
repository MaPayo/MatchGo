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

document.addEventListener("DOMContentLoaded", () => {
    // https://developer.mozilla.org/en-US/docs/Learn/HTML/Howto/Use_data_attributes
    let elements = document.getElementsByClassName("bContacto");
    
    for (let i = 0; i < elements.length; i++) {
        elements[i].addEventListener("click", function () {
            
            // Si tienen una notificación lo quitamos
            imgNotification = elements[i].firstChild;
            if (elements[i].childElementCount == 1) {
                elements[i].removeChild(elements[i].lastChild);
            }

            // Obtiene los mensajes con el contacto del botón
            requestMessages(elements[i].getAttribute('data-id'));
	
    		// Actualizamos el botón de enviar mensaje
    		updateFormMessageButton(config.id, elements[i].getAttribute('data-id'), elements[i].textContent);
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

    let idUsuario;
    let idContacto;
    let contacto;
    //Introducimos los nuevos mensajes
    let html = [];
    json.forEach(m => {
        let msg = "";
        if (m.sender == config.usuario) {
            idUsuario = m.senderId;
            idContacto = m.receiverId;
            contacto = m.receiver;
            msg = "<div class='mensajeMio'>"
                + "<p> "+ m.textMessage +"</p>"
                + "</div>";
        } else {
            idUsuario = m.receiverId;
            idContacto = m.senderId;
            contacto = m.sender;
            msg ="<div class='mensajeContacto'>"
                + "<p> "+ m.textMessage +"</p>"
                + "</div>";
        }
        
        html.push("<div class='mensaje'>" + msg + "</div>");

    });
    div.insertAdjacentHTML('beforeend', html.reverse().join("\n"));
}

function updateFormMessageButton(idUsuario, idContacto, contacto) {
    // Actualizamos el nombre del contacto en el chat
    document.getElementById("contactInChat").remove();
    document.getElementById("chatDiv").insertAdjacentHTML('beforeend',
        "<div id='contactInChat' class='contactoMensaje' name='" + contacto + "'>" +
        "<p>"+ contacto +"</p>" +
        "</div>");
    
    // Cada vez que accedemos a este método añadimos un eventListener, esto hace que el mensaje se
    // envíe a todos los chats por los que ha pasado el usuario, y la función es anónima, por lo tanto
    // no la podemos eliminar, así que eliminamos el botón y lo volvemos a crear.
    document.getElementById("botonFormMessage").remove();
    document.getElementById("FormMessage").insertAdjacentHTML('beforeend', 
        "<input type='submit' id='botonFormMessage' class='botonMensaje' name='boton' value='enviar'>");
    
    let button = document.getElementById("botonFormMessage");

    console.log("añadiendo manejador a ", button);
    button.addEventListener("click", (e)  => {
        e.preventDefault();
        e.stopImmediatePropagation();
        
        let textMessage = document.getElementById("textMessageForm").value;
        textMessage = textMessage.trim();
        if (textMessage) {  // Si hay un texto que enviar
            document.getElementById("textMessageForm").value = "";

            let message = {
                sender: null,
                senderId: idUsuario,
                receiver: null,
                receiverId: idContacto,
                sendDate: null,
                readMessage: false,
                textMessage: textMessage
            };
            console.log("Enviando ", message);
            go(config.rootUrl + "messages/addMessage", "POST", message)
                .then(d => {                        // Si todo sale bien, 200 OK
                    console.log("enviado ok", d);
                    let div = document.getElementById("M");
                    msg = "<div class='mensaje'><div class='mensajeMio'>"
                        + "<p> "+ d.textMessage +"</p>"
                        + "</div> </div>";
                    div.insertAdjacentHTML('afterbegin', msg);
            })
                .catch( e => { /* se llama si NO es un 200 OK (= cualquier error) */});
        }
    });
}


// Asocia los botones de los contactos con recarga de chats
document.addEventListener("DOMContentLoaded", () => {

    ws.receive = (o) => {
        let nombreContacto = document.getElementById("contactInChat");
        
        // Es un mensaje para mí y tengo su chat abierto
        if (config.id == o.receiverId && nombreContacto != null &&
                nombreContacto.getAttribute("name") == o.sender) {
            requestMessages(o.senderId);
        // Es un mensaje para mí y no tengo su chat abierto
        } else if (config.id == o.receiverId) {
            let contacto = document.getElementById(o.senderId);
            if (contacto != null && document.getElementById("notificationMsg") == null) {
                let html = "<img id='notificationMsg' src = '/img/notificationContact.png'" +
                    "height = '16px' width = '16px' >";
                contacto.insertAdjacentHTML('beforeend',html);
            }
        }
    }
})

// Recibe la llamada del WebSocket y recarga el chat
document.addEventListener("DOMContentLoaded",  () => {
    if (config.socketUrl) {
        let url = ["/user/queue/updates"];
        ws.initialize(config.socketUrl, url);
    }
})
