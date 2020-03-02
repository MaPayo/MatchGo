package es.ucm.fdi.iw.matchandgo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;


import es.ucm.fdi.iw.matchandgo.model.Mensaje;
import es.ucm.fdi.iw.matchandgo.model.Evento;
import es.ucm.fdi.iw.matchandgo.model.Test;
import es.ucm.fdi.iw.matchandgo.model.Tags;
import es.ucm.fdi.iw.matchandgo.model.Usuario;
import es.ucm.fdi.iw.matchandgo.model.Valoracion;



@Controller
public class RootController {

    private static Logger log = LogManager.getLogger(RootController.class);
    
    @Autowired
    private EntityManager entityManager;


    // Crea unos chats de ejemplo para la vista "mensajes"
    public Mensaje[] generarChatPorDefecto() {
        Mensaje[] res = new Mensaje[2];

		// Creamos el chat de ejemplo
		res[0] = new Mensaje("Buenas tardes!", new Usuario()/*Rodolfo*/, new Usuario()/*Laura*/, new Date());
		res[1] = new Mensaje("Hombre, cuanto tiempo!", new Usuario()/*Laura*/,
				new Usuario()/*Rodolfo*/, new Date());

        return res;
    }
/*
	@GetMapping("/profile")
	public String getSignUp(Model model, HttpSession session) {
		if(session.getAttribute("user") != null) 
			return "redirect:/user/" + ((Usuario) session.getAttribute("user")).getId();
		return "registro";
	}*/

	
	@GetMapping("/profile")
	public String getProfile(Model model, HttpSession session) {
		Usuario user = new Usuario();
		Tags etiqueta1= new Tags();
		etiqueta1.setCategoriaTipo(false);
		etiqueta1.setContenido("Futbol");

		Tags etiqueta2= new Tags();
		etiqueta2.setCategoriaTipo(true);
		etiqueta2.setContenido("Concierto");
		List<Tags> tags = new ArrayList<Tags>();
		tags.add(etiqueta1);
		tags.add(etiqueta2);
		
		user.setCorreo("pepe@ucm.es");
		user.setPassword("1234");
		user.setNombre("Pepe");
		user.setApellidos("el del quinto");
		user.setFecha_nac("05/03/1965");
		user.setSexo("Hombre");
		user.setImagen("");
		user.setTags(tags);
		user.setTags(tags);
		user.setRoles("USER");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" );
		System.out.println(user.getNombre());

		session.setAttribute("user", user);

		model.addAttribute("user", user);
		model.addAttribute("nombre", user.getNombre());
		model.addAttribute("edad", user.getFecha_nac());
		model.addAttribute("sexo", user.getSexo());
		model.addAttribute("valoracion", "3 estrellas");
		model.addAttribute("tags", user.getTags());
		
		return "profile";
	}
	
	
    @GetMapping("/mensajes")
    public String mostrarMensajes(Model model, HttpSession session) {

        Mensaje[] mensajes = generarChatPorDefecto();
        String[] contactos = new String[2];
        contactos[0] = "Laura";
        contactos[1] = "Samuel";

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("contactos", contactos);
        model.addAttribute("contacto", "Laura");
        session.setAttribute("usuario", "Rodolfo");

        return "mensajes";
    }
  
    @GetMapping("/busqueda")
    public String getMethodName(Model model, HttpSession session) {
        return "busqueda";
    }
    
    @Transactional
    @GetMapping("/revisar") 
	public String index(
			Model model 
			) { 
			model.addAttribute("titulo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."); // escribe en modelo
			model.addAttribute("descripcion", "Orci a scelerisque purus semper eget duis at. Eleifend quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus.");
			model.addAttribute("tags", new ArrayList<String>() {{
				add("tag");
				add("tag1");
				add("tag2");
				add("tag3");
				add("tag4");
				add("tag5");
			}});
			model.addAttribute("listnum", new ArrayList<Integer>() {{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
				add(6);
			}});
			
			Evento e = new Evento("e1");
			this.entityManager.persist(e);
			return "matchAndGoVistaModerador"; 
	}
    
    @GetMapping("/evento") 
	public String showEvent(
			Model model // comunicación con vist
			) { // viene del formulario
			return "matchAndGoEvento"; // vista resultante
	}
    @GetMapping("/admin") 
	public String admin(
			Model model // comunicación con vist
			) { // viene del formulario
			return "admin_view"; // vista resultante
	}
    
    
}