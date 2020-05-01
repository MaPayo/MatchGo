package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
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
import es.ucm.fdi.iw.model.Tags;
import es.ucm.fdi.iw.model.User.Role;

/**
 * User-administration controller
 * 
 * @author mfreire
 * @author MaPayo
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
	public String getNewEvent(Model model, HttpSession session) {
		
		User requester = (User)session.getAttribute("u");
		requester = entityManager.find(User.class, requester.getId());
		TypedQuery<Tags> query= entityManager.createNamedQuery("Tag.getCategories", Tags.class);
		List<Tags> categories= query.getResultList();
		
		
		model.addAttribute("user", requester);
		model.addAttribute("newEvent", true); 
		model.addAttribute("categories", categories);
		return "event";
	}
	
	@PostMapping("/newEvent")
	@Transactional
	public String postNewEvent(Model model, HttpServletRequest request, @RequestParam String name,
			@RequestParam String description, @RequestParam String date, 
			@RequestParam String agePreference, @RequestParam String genderPreference,
			@RequestParam String location, @RequestParam Long category,
			@RequestParam String tagsAll, HttpSession session) {
		
		User requester = (User)session.getAttribute("u");
		requester = entityManager.find(User.class, requester.getId());
		model.addAttribute("user", requester);
		Event newEvent = new Event();
		newEvent.setName(name);
		newEvent.setDescription(description);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        newEvent.setDate(LocalDate.parse(date, formatter).atStartOfDay());
		newEvent.setPublicationDate(LocalDateTime.now());
		newEvent.setLocation(location);
		newEvent.setAgePreference(agePreference);
		newEvent.setGenderPreference(genderPreference);
		newEvent.setCreator(requester);
		List<Tags> tags = new ArrayList();
		
		String[] tagNames = tagsAll.split("\n");
		for(String tagName : tagNames) {
			Tags t = new Tags();
			t.setisCategory(false);
			t.setTag(tagName);
			tags.add(t);
			entityManager.persist(t);
		}
		
		tags.add(entityManager.find(Tags.class, category));
		newEvent.setTags(tags);
		
		entityManager.persist(newEvent);

		/*
		User requester = (User)session.getAttribute("u");
		if (requester.getId() != target.getCreator().getId() &&
				! requester.hasRole(Role.ADMIN)) {			
			response.sendError(HttpServletResponse.SC_FORBIDDEN, 
					"No eres administrador, y éste no es tu evento");
		}
		*/
		return "redirect:/user/" + requester.getId();
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("event", entityManager.createQuery(
				"SELECT u FROM Event u").getResultList());
		
		TypedQuery<Tags> query= entityManager.createNamedQuery("Tag.getCategories", Tags.class);
		List<Tags> categories= query.getResultList();
		model.addAttribute("category", categories);
		return "events";
	}
	
	@PostMapping("/subscribe/{id}")
	@Transactional
	public String subscribe(HttpServletResponse response, @PathVariable long id, Model model, HttpSession session){
		Event target = entityManager.find(Event.class, id);
		User requester = (User)session.getAttribute("u");
		//Esta opcion solo se mostrara a los usuarios con permisos que no estan subscritos, quedara definido en la vista de detalle del evento
		requester.getJoinedEvents().add(target);
		entityManager.persist(requester);
		entityManager.flush();
		//Actualizamos la session con la nueva información del usuario ya guardada en la base de datos
		session.removeAttribute("u"); //Lo eliminamos para que no haya ningun problema de tener dos atributos "u"
		session.setAttribute("u", requester); //Lo introducimos en la session
			//Le devolvemos al evento
		return "redirect:/event/"+id;
	}


	@GetMapping("/search")
	public String search(@RequestParam String title, Model model){
	
		TypedQuery<Event> queryEvent= entityManager.createNamedQuery("Event.searchByName", Event.class);
		queryEvent.setParameter("uname", "%"+title+"%"); //Añadimos el % para que busque una cadena que contenga la palabra
		List<Event> lista= queryEvent.getResultList();
		model.addAttribute("event", lista);
		return "events";

	}
	


	//Seguramente haya una forma mas eficiente de hacer esto, ahora mismo es la unica que encuentro
	@GetMapping("/advanceSearch")
	public String advanceSearch(@RequestParam String title, @RequestParam String location, @RequestParam String category, Model model ) {
		List<Event> listaTitle = null;
		List<Event> listalocation = null;
		List<Event> listacategories = null;
		if(title != null){
		TypedQuery<Event> queryEventt= entityManager.createNamedQuery("Event.searchByName", Event.class);
		queryEventt.setParameter("uname", "%"+title+"%"); //Añadimos el % para que busque una cadena que contenga la palabra
		listaTitle= queryEventt.getResultList();
		}
		if(location != null){
			TypedQuery<Event> queryEventl= entityManager.createNamedQuery("Event.searchByLocation", Event.class);
		queryEventl.setParameter("ulocation", "%"+location+"%"); //Añadimos el % para que busque una cadena que contenga la palabra
		listalocation= queryEventl.getResultList();
		}
		
		if(category != ""){
		//Buscamos los eventos que tengan esa categoria en concreto
		TypedQuery<Event> queryEventc= entityManager.createNamedQuery("Tag.getEventTags", Event.class);
		queryEventc.setParameter("ucategory", Long.parseLong(category)); 
		//Creamos una lista con los ids de los eventos que contienen esos tags
		listacategories= queryEventc.getResultList();
		}
		List<Event> result = new ArrayList<Event>();
		List<Event> finalresult = new ArrayList<Event>();
		//Juntamos los 3 arrays en uno solo que sera el que devolveremos, sin repetir los eventos encontrados
		if(listaTitle != null){ //Si se ha usado el campo buscar por nombre
			if(listalocation != null){//Si se usado tambien la localizacion
				for (Event eventT : listaTitle) 
					for (Event eventL : listalocation) 
						if(eventT.getId() == eventL.getId()) //Si los eventos encontrados tienen el mismo id, se añaden al array resultado
							result.add(eventT);

				if(listacategories != null){ //Se ha buscado por los 3 campos
					for (Event eventR : result) 
						for(Event eventC : listacategories)
							if(eventR.getId() == eventC.getId())
								finalresult.add(eventR);
				//incluimos en el modelo el array resultante de juntar los 3 arrays		
				model.addAttribute("event", finalresult); 
				}else{ //Si no se ha buscado por categoria el resultado final es result
					model.addAttribute("event", result);
				}
			} else{ //Si solo se ha buscado por titulo
				model.addAttribute("attributeName", listaTitle);
			}
			
		}else{ //No se ha usado titulo
			if(listalocation != null){//Si se ha usado localizacion
				if(listacategories !=null){//Y se ha usado categoria
					for (Event eventT : listalocation) 
						for(Event eventC : listacategories)
							if(eventT.getId() == eventC.getId())
								finalresult.add(eventT);
								model.addAttribute("event", finalresult);
				}else{
					model.addAttribute("event", listalocation); //Si solo se ha usado localizacion
				}
			}else{ //Si no se ha usado ni titulo ni localizacion
				if(listacategories != null){ //Pero si categoria
					model.addAttribute("event", listacategories);
				}else{//Si no se ha usado nada pero se ha dado al boton devolvemos vacio
					model.addAttribute("event", new ArrayList<Event>() );
				}
			}
		}
		
		
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

	@GetMapping("/eventusers/{id}")
	public String getUsers(@PathVariable long id, Model model){
		TypedQuery<User> idusers = entityManager.createNamedQuery("Event.participants", User.class);
		idusers.setParameter("eid", id);
		List<User> users = idusers.getResultList();
		model.addAttribute("users", users);

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
