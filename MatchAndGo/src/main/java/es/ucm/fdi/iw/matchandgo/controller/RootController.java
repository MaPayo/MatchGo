package es.ucm.fdi.iw.matchandgo.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.matchandgo.controller.RootController.chatPersona.mensaje;

@Controller
public class RootController {

    private static Logger log = LogManager.getLogger(RootController.class);

    //CLASE AUXILIAR PARA LA PANTALLA "mensajes"
    private class chatPersona {
        private String persona;

        private class mensaje {
            private String contenido;
            private String sender; //La persona que lo envía

            public mensaje(String c, String s) {
                this.contenido = c;
                this.sender = s;
            }
        }
        private mensaje[] mensajes;

        public chatPersona(String persona, mensaje[]mensajes) {
            this.persona = persona;
            this.mensajes = mensajes;
        }
    }


    public chatPersona[] generarChatsPorDefecto() {
        chatPersona[] res;
        
        //res[0] = chatPersona("Manuel", [{"Buenas tardes", "Pepe"}, ])

        return res;
    }

    @GetMapping("/mensajes")
    public Object mostrarMensajes(Model model, @RequestParam String persona) {

        chatPersona[] chats = generarChatsPorDefecto();
        return null;
    }
}