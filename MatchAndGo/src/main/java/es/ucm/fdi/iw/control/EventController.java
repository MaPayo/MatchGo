package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Event.Access;
import es.ucm.fdi.iw.model.User.Role;

/**
 * User-administration controller
 * 
 * @author mfreire
 */
@Controller()
@RequestMapping("event")
public class EventController {
	
	private static final Logger log = LogManager.getLogger(EventController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/newEvent")
	public String getNewEvent(@PathVariable long id, Model model, HttpSession session) {

		Event e = entityManager.find(Event.class, id);
		
		User requester = (User)session.getAttribute("u");
		requester = entityManager.find(User.class, requester.getId());

		//model.addAttribute("access", e.checkAccess(requester)); //no funciona
		model.addAttribute("newEvent", true); 
		
		model.addAttribute("event", e);
		return "event";
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("event", entityManager.createQuery(
				"SELECT u FROM Event u").getResultList());
		return "events";
	}
	

	@GetMapping("/search")
	public String search(@RequestParam String title, Model model){
	
		TypedQuery<Event> queryEvent= entityManager.createNamedQuery("Event.searchByName", Event.class);
		queryEvent.setParameter("uname", "%"+title+"%"); //Añadimos el % para que busque una cadena que contenga la palabra
		List<Event> lista= queryEvent.getResultList();
		model.addAttribute("event", lista);


		return "events";
	}

	@GetMapping("/{id}")
	public String getEvent(@PathVariable long id, Model model, HttpSession session) {
		
		Event e = entityManager.find(Event.class, id);
		
		User requester = (User)session.getAttribute("u");
		requester = entityManager.find(User.class, requester.getId());

		//model.addAttribute("access", e.checkAccess(requester)); //no funciona
		model.addAttribute("access", Access.MINIMAL); 
		
		model.addAttribute("event", e);

		return "event";
	}
	
	@PostMapping("/{id}")
	@Transactional
	public String postEvent(
			HttpServletResponse response,
			@PathVariable long id, 
			@ModelAttribute Event edited, 
			Model model, HttpSession session) throws IOException {
		Event target = entityManager.find(Event.class, id);
		model.addAttribute("event", target);
		
		User requester = (User)session.getAttribute("u");
		if (requester.getId() != target.getCreator().getId() &&
				! requester.hasRole(Role.ADMIN)) {			
			response.sendError(HttpServletResponse.SC_FORBIDDEN, 
					"No eres administrador, y éste no es tu evento");
		}
		
		// copiar todos los campos cambiados de edited a target

		return "event";
	}	
	
	@GetMapping(value="/{id}/photo")
	public StreamingResponseBody getPhoto(@PathVariable long id, Model model) throws IOException {		
		File f = localData.getFile("event", ""+id);
		InputStream in;
		if (f.exists()) {
			in = new BufferedInputStream(new FileInputStream(f));
		} else {
			in = new BufferedInputStream(getClass().getClassLoader()
					.getResourceAsStream("static/img/unknown-user.jpg"));
		}
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream os) throws IOException {
				FileCopyUtils.copy(in, os);
			}
		};
	}
	
	@PostMapping("/{id}/photo")
	public String postPhoto(
			HttpServletResponse response,
			@RequestParam("photo") MultipartFile photo,
			@PathVariable("id") String id, Model model, HttpSession session) throws IOException {
		User target = entityManager.find(User.class, Long.parseLong(id));
		model.addAttribute("event", target);
		
		// check permissions
		User requester = (User)session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				! requester.hasRole(Role.ADMIN)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, 
					"No eres administrador, y éste no es tu perfil");
			return "profile";
		}
		
		log.info("Updating photo for user {}", id);
		File f = localData.getFile("user", id);
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
			} catch (Exception e) {
				log.warn("Error uploading " + id + " ", e);
			}
			log.info("Successfully uploaded photo for {} into {}!", id, f.getAbsolutePath());
		}

		return "event";
	}
}
