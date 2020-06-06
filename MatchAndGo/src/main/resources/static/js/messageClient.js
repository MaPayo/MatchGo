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
                + "<pre> "+ m.textMessage +"</pre>"
                + "</div>";
        } else {
            idUsuario = m.receiverId;
            idContacto = m.senderId;
            contacto = m.sender;
            msg ="<div class='mensajeContacto'>"
                + "<pre> "+ m.textMessage +"</pre>"
                + "</div>";
        }
        
        html.push("<div class='mensaje'>" + msg + "</div>");

    });
    div.insertAdjacentHTML('beforeend', html.reverse().join("\n"));

    /*  Ejemplo para insertar un atributo hidden
    // Insertamos un atributo en el formulario
    let elemContacto = document.getElementById("contactId");
    if (elemContacto != null) {
        elemContacto.remove();
    }
    
    let form = document.getElementById("FormMessage");
    let htmlForm = "<input type='hidden' id='contactId' value ='" + idContacto + "'>";
    form.insertAdjacentHTML('beforeend', htmlForm);
    */

    // Actualizamos el botón de enviar mensaje
    updateFormMessageButton(idUsuario, idContacto, contacto);
}

function updateFormMessageButton(idUsuario, idContacto, contacto) {
    // Actualizamos el nombre del contacto en el chat
    document.getElementById("contactInChat").remove();
    document.getElementById("chatDiv").insertAdjacentHTML('beforeend',
        "<div id='contactInChat' class='contactoMensaje'>" +
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
            go(config.rootUrl + "messages/addMessage", "POST", message);
            e.stopImmediatePropagation();
        }
    });
}


// Asocia los botones de los contactos con recarga de chats
document.addEventListener("DOMContentLoaded", () => {

    ws.receive = (o) => {
        // asume que añades un senderId en los mensajes emitidos desde el controlador...
        if (o.senderId == idContactoActual) {
            requestMessages(idContactoActual);
        }
    }
})

// Recibe la llamada del WebSocket y recarga el chat
document.addEventListener("DOMContentLoaded",  () => {
    if (config.socketUrl) {
        let url = ["/messages/wsSender", "/messages/wsUpdate"];
        ws.initialize(config.socketUrl, url);
    }
})