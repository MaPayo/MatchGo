package es.ucm.fdi.iw.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Tags;
import es.ucm.fdi.iw.model.User;




@Controller
public class RootController {

	private static Logger log = LogManager.getLogger(RootController.class);
	
	@GetMapping("/")
	public String index(Model model,HttpSession session) {
		
		return "index";
	}

	@GetMapping("/index")
	public String welcome(Model model) {
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model,HttpSession session) {
		if(session.getAttribute("user") != null) 
			return "redirect:/user/" + ((User) session.getAttribute("user")).getId();
	
		return "login";
	}

	
	@GetMapping("/error")
	public String error(Model model) {
		return "error";
	}	

}
	
/*
	@Autowired
	private EntityManager entityManager;


	// Crea unos chats de ejemplo para la vista "mensajes"
	public Message[] generarChatPorDefecto() {
		Message[] res = new Message[2];

		// Creamos el chat de ejemplo
		User user1 = new User();
		user1.setFirstName("Rodolfo");
		User user2 = new User();
		user2.setFirstName("Laura");
		res[0] = new Message("Buenas tardes!", user1, user2, LocalDateTime.now());
		res[1] = new Message("Hombre, cuanto tiempo!", user2, user1, LocalDateTime.now());

		return res;
	}
	/*
	   @GetMapping("/profile")
	   public String getSignUp(Model model, HttpSession session) {
	   if(session.getAttribute("user") != null) 
	   return "redirect:/user/" + ((Usuario) session.getAttribute("user")).getId();
	   return "registro";
	   }


	public User pasoDeModificarElImportSql() {
		User user = new User();
		Tags etiqueta1= new Tags();
		etiqueta1.setisCategory(false);
		etiqueta1.setTag("Futbol");
		entityManager.persist(etiqueta1);
		
		Tags etiqueta2= new Tags();
		etiqueta2.setisCategory(true);
		etiqueta2.setTag("Concierto");
		entityManager.persist(etiqueta2);

		user.setEmail("pepe@ucm.es");
		user.setPassword("1234");
		user.setFirstName("Pepe");
		user.setLastName("el del quinto");
		user.setBirthDate("05/03/1965");
		user.setSexo("Hombre");
		user.setPhoto("");
		user.setEnabled(true);
		// user.setTags(tags);
		user.setRole("USER");
		entityManager.persist(user);
		entityManager.flush();
		return user;
	}
	
	@GetMapping("/profile")
	@Transactional
	public String getProfile(Model model, HttpSession session) {

		User user = null;
		try {
			user = (User)entityManager.createNamedQuery("Usuario.byUsername", User.class)
	                .setParameter("username", "Pepe")
	                .getSingleResult();
		} catch (Exception e) {
			user = pasoDeModificarElImportSql();
		}	

		// session.setAttribute("user", user);

		model.addAttribute("user", user);
		model.addAttribute("nombre", user.getFirstName());
		model.addAttribute("edad", user.getBirthDate());
		model.addAttribute("sexo", user.getGender());
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
	
  @GetMapping("/busqueda")
  public String searching(Model model) {
	  Event e = new Event();
	  Event e2 = new Event();
	  Tags t = new Tags();
	  Tags t2 = new Tags();
	  Event[] eventos = new Event[2];
	  List<Tags> categoria1 = new ArrayList<Tags>();
	  List<Tags> categoria2 = new ArrayList<Tags>();
	  e.setName("Partido Benéfico de Fútbol");
	  e.setDescripcion("Para ayudar a la asociacion 'Afectados por IW'");

	  e2.setName("Visita al Museo del Jamon");
	  e2.setDescripcion("Nos lo vamos a pasar super bien");

	  t.setTag("Deportivo");
	  t.setisCategory(true);

	  t2.setTag("Cultural");
	  t2.setisCategory(true);

	  categoria1.add(t);
	  categoria2.add(t2);
	  e.setTags(categoria1);
	  e2.setTags(categoria2);

	  eventos[0]= e;
	  eventos[1]= e2;

	  model.addAttribute("event", eventos);

	  return "busqueda";
  }

	@GetMapping("/error")
	public String error(Model model) {
		return "error";
	}	
}
*/