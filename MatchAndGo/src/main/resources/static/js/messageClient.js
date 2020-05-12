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
    //Introducimos los nuevos mensajes
    let html = [];
    json.forEach(m => {
        let msg = "";
        
        if (m.sender == config.usuario) {
            idUsuario = m.senderId;
            idContacto = m.receiverId;
            msg = "<div class='mensajeMio'>"
                + "<pre> "+ m.textMessage +"</pre>"
                + "</div>";
        } else {
            idUsuario = m.receiverId;
            idContacto = m.senderId;
            msg ="<div class='mensajeContacto'>"
                + "<pre> "+ m.textMessage +"</pre>"
                + "</div>";
        }
        
        html.push("<div class='mensaje'>" + msg + "</div>");

    });
    div.insertAdjacentHTML('beforeend', html.reverse().join("\n"));

    // Insertamos un atributo en el formulario
    let elemContacto = document.getElementById("contactId");
    if (elemContacto != null) {
        elemContacto.remove();
    }
    /*  Ejemplo para insertar un atributo hidden
    let form = document.getElementById("FormMessage");
    let htmlForm = "<input type='hidden' id='contactId' value ='" + idContacto + "'>";
    form.insertAdjacentHTML('beforeend', htmlForm);
    */

    // Actualizamos el botón de enviar mensaje
    updateFormMessageButton(idUsuario, idContacto);
}

function updateFormMessageButton(idUsuario, idContacto) {
    let button = document.getElementById("botonFormMessage");
    console.log("añadiendo manejador a ", button);

    button.addEventListener("click", (e) => {
        e.preventDefault();

        let textMessage = document.getElementById("textMessageForm").getAttribute("value");
        console.log("Enviando el texto: ", textMessage);

        let message = {
            sender: "",
            senderId: idUsuario,
            receiver: "",
            receiverId: idContacto,
            sendDate: "",
            readMessage: "",
            textMessage: textMessage
        };
        console.log("hola mundo  - enviando ", message);
        go(config.rootUrl + "messages/addMessage", "POST", message);
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