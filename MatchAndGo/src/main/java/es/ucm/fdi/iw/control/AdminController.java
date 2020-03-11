package es.ucm.fdi.iw.control;

import java.io.File;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Evento;
import es.ucm.fdi.iw.model.Usuario;

/**
 * Admin-only controller
 * @author mfreire
 * @author colano
 */
@Controller()
@RequestMapping("admin")
public class AdminController {
	
	private static final Logger log = LogManager.getLogger(AdminController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));

		model.addAttribute("users", entityManager.createQuery(
				"SELECT u FROM User u").getResultList());
		
		Usuario [] allUsers = new Usuario[7];
		allUsers[0] = new Usuario(1, "nombre 2", "apellidos 2", "correo 2", "password 2", "fecha_nac 2", "String sexo 2", "String roles 2");
		allUsers[1] = new Usuario(2, "nombre", "apellidos", "correo", "password", "fecha_nac", "String sexo", "String roles");
		allUsers[2] = new Usuario(3, "nombre 3", "apellidos 3", "correo 3", "password 3", "fecha_nac 3", "String sexo 3", "String roles 3");
		allUsers[3] = new Usuario(4, "nombre 4", "apellidos 4", "correo 4", "password 4", "fecha_nac 4", "String sexo 4", "String roles 4");
		allUsers[4] = new Usuario(5, "nombre 5", "apellidos 5", "correo 5", "password 5", "fecha_nac 5", "String sexo 5", "String roles 5");
		allUsers[5] = new Usuario(6, "nombre 6", "apellidos 6", "correo 6", "password 6", "fecha_nac 6", "String sexo 6", "String roles 6");
		allUsers[6] = new Usuario(7, "nombre 7", "apellidos 7", "correo 7", "password 7", "fecha_nac 7", "String sexo 7", "String roles 7");
	
		Evento [] allEvents = new Evento[4];
		allEvents[0]= new Evento(0,"nombre evento", "descripcion evento", "ubicacion evento", LocalDateTime.now(), LocalDateTime.now());
		allEvents[1]= new Evento(1,"nombre evento 1", "descripcion evento 1", "ubicacion evento 1", LocalDateTime.now(), LocalDateTime.now());
		allEvents[2]= new Evento(2,"nombre evento 2", "descripcion evento 2", "ubicacion evento 2", LocalDateTime.now(), LocalDateTime.now());
		allEvents[3]= new Evento(3,"nombre evento 3", "descripcion evento 3", "ubicacion evento 3", LocalDateTime.now(), LocalDateTime.now());

		model.addAttribute("allUsers", allUsers);
		model.addAttribute("allEvents", allEvents);


		return "admin_view";
	}
	
	@PostMapping("/toggleuser")
	@Transactional
	public String delUser(Model model,	@RequestParam long id) {
		User target = entityManager.find(User.class, id);
		if (target.getEnabled() == 1) {
			// disable
			File f = localData.getFile("user", ""+id);
			if (f.exists()) {
				f.delete();
			}
			// disable user
			target.setEnabled((byte)0); 
		} else {
			// enable user
			target.setEnabled((byte)1);
		}
		return index(model);
	}	
}
