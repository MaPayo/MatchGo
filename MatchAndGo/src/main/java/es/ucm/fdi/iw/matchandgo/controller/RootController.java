package es.ucm.fdi.iw.matchandgo.controller;

import java.util.ArrayList;
import java.util.Date;
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
		Usuario user1 = new Usuario();
		user1.setNombre("Rodolfo");
		Usuario user2 = new Usuario();
		user2.setNombre("Laura");
		res[0] = new Mensaje("Buenas tardes!", user1, user2, new Date());
		res[1] = new Mensaje("Hombre, cuanto tiempo!", user2,
				user1, new Date());

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


		user.setCorreo("pepe@ucm.es");
		user.setPassword("1234");
		user.setNombre("Pepe");
		user.setApellidos("el del quinto");
		user.setFecha_nac("05/03/1965");
		user.setSexo("Hombre");
		user.setImagen("");
		// user.setTags(tags);
		user.setRoles("USER");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" );
		System.out.println(user.getNombre());

		// session.setAttribute("user", user);

		model.addAttribute("user", user);
		model.addAttribute("nombre", user.getNombre());
		model.addAttribute("edad", user.getFecha_nac());
		model.addAttribute("sexo", user.getSexo());
		model.addAttribute("valoracion", "3 estrellas");
		model.addAttribute("tags", new ArrayList<String>() {{
			add("tag");
			add("tag1");
			add("tag2");
			add("tag3");
			add("tag4");
			add("tag5");
		}});

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
	
		Usuario [] allUsers = new Usuario[7];
		allUsers[0] = new Usuario(1, "nombre 2", "apellidos 2", "correo 2", "password 2", "fecha_nac 2", "String sexo 2", "String roles 2");
		allUsers[1] = new Usuario(2, "nombre", "apellidos", "correo", "password", "fecha_nac", "String sexo", "String roles");
		allUsers[2] = new Usuario(3, "nombre 3", "apellidos 3", "correo 3", "password 3", "fecha_nac 3", "String sexo 3", "String roles 3");
		allUsers[3] = new Usuario(4, "nombre 4", "apellidos 4", "correo 4", "password 4", "fecha_nac 4", "String sexo 4", "String roles 4");
		allUsers[4] = new Usuario(5, "nombre 5", "apellidos 5", "correo 5", "password 5", "fecha_nac 5", "String sexo 5", "String roles 5");
		allUsers[5] = new Usuario(6, "nombre 6", "apellidos 6", "correo 6", "password 6", "fecha_nac 6", "String sexo 6", "String roles 6");
		allUsers[6] = new Usuario(7, "nombre 7", "apellidos 7", "correo 7", "password 7", "fecha_nac 7", "String sexo 7", "String roles 7");
	
		Evento [] allEvents = new Evento[4];
		Date dat = new Date();
		allEvents[0]= new Evento(0,"nombre evento", "descripcion evento", "ubicacion evento", dat, dat);
		allEvents[1]= new Evento(1,"nombre evento 1", "descripcion evento 1", "ubicacion evento 1", dat, dat);
		allEvents[2]= new Evento(2,"nombre evento 2", "descripcion evento 2", "ubicacion evento 2", dat, dat);
		allEvents[3]= new Evento(3,"nombre evento 3", "descripcion evento 3", "ubicacion evento 3", dat, dat);

		model.addAttribute("allUsers", allUsers);
		model.addAttribute("allEvents", allEvents);
		return "admin_view"; // vista resultante
			}


}
