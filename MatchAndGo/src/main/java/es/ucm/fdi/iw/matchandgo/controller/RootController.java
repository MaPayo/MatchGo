package es.ucm.fdi.iw.matchandgo.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.matchandgo.mensajes.Mensaje;

@Controller
public class RootController {

    private static Logger log = LogManager.getLogger(RootController.class);


    // Crea unos chats de ejemplo para la vista "mensajes"
    public Mensaje[] generarChatPorDefecto() {
        Mensaje[] res = new Mensaje[2];

        // Creamos el chat de ejemplo
        res[0] = new Mensaje("Buenas tardes!", "Rodolfo");
        res[1] = new Mensaje("Hombre, cuanto tiempo!", "Laura");

        return res;
    }

    @GetMapping("/mensajes")
    public String mostrarMensajes(Model model, HttpSession session) {

        Mensaje[] mensajes = generarChatPorDefecto();
        String[] contactos = new String[2];
        contactos[0] = "Laura";
        contactos[1] = "Samuel";

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("contactos", contactos);
        session.setAttribute("usuario", "Rodolfo");

        return "mensajes";
    }
}