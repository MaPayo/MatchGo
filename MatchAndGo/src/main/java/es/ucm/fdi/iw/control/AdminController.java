package es.ucm.fdi.iw.control;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.File;
import javax.servlet.http.HttpSession;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Tags;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.User;
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
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private Environment env;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		return "admin_view";
	}



	@PostMapping(path = "/eventlist", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Event.TransferEvent> retrieveEvents(HttpSession session){
		log.info("Generating Event List");
		List<Event> events = entityManager.createNamedQuery("Event.all").getResultList();
		return Event.asTransferObjects(events);
	}

	@PostMapping(path = "/userlist", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<User.Transfer> retrieveUsers(HttpSession session){
		log.info("Generating User List");
		List<User> users = entityManager.createNamedQuery("User.all").getResultList();
		return User.asTransferObjects(users);
	}

	@PostMapping("/blockUser")
	@Transactional
	public String blockUser(Model model, @RequestParam long id) {
		User target = entityManager.find(User.class, id);
		boolean newState = false;
		if (target.isEnabled()){
			newState = false;
		} else {
			newState = true;
		}
		entityManager.createNamedQuery("User.blockUser")
			.setParameter("idUser",id)
			.setParameter("state",newState)
			.executeUpdate();

		List<User> usersU = entityManager.createNamedQuery("User.all").getResultList();
		sendMessageWS(usersU,"updateUsers");
		return "redirect:/admin/";
	}	

	@PostMapping("/deleteEvent")
	@Transactional
	public String deleteEvent(Model model,@RequestParam long id) {		
		Event e = (Event) entityManager.createNamedQuery("Event.getEvent",Event.class)
			.setParameter("idUser",id)
			.getSingleResult();

		List<Tags> tags = new ArrayList<>(e.getTags());
		log.info("I will Remove all subcribed tags ");
		for (Tags tag : tags){
			e.getTags().remove(tag);
			log.info("Remove event from tag " + tag.getId());
		}

		List<User> joined = new ArrayList<>(e.getParticipants());
		log.info("I will Remove all participants");
		for (User u : joined){
			e.getParticipants().remove(u);
			log.info("Remove user "+u.getId()+" from event " + e.getId());
		}

		log.info("I will remove from owned event");
		e.getCreator().getCreatedEvents().remove(e);

		log.info("I will remove event");
		entityManager.createNamedQuery("Event.deleteEvent")
			.setParameter("idUser",e.getId())
			.executeUpdate();
		log.info("Removed event");

		entityManager.flush();
		List<Event> eventsU = entityManager.createNamedQuery("Event.all").getResultList();
		sendMessageWS(eventsU,"updateEvents");
		return "redirect:/admin/";
	}

	@PostMapping("/deleteUser")
	@Transactional
	public String deleteUser(Model model,@RequestParam long id) {		
		User u = (User) entityManager.createNamedQuery("User.getUser",User.class)
			.setParameter("idUser",id)
			.getSingleResult();

		List<Tags> tags = u.getTags();
		log.info("I will Remove all subcribed tags ");
		for (Tags tag : tags){
			tag.getSubscribers().remove(u);
			log.info("Remove user from tag " + tag.getId());
		}

		List<Event> events = u.getJoinedEvents();
		log.info("I will Remove from all Events Joined");
		for (Event event : events){
			event.getParticipants().remove(u);
			log.info("Remove user from event " + event.getId());
		}

		events = new ArrayList<>(u.getCreatedEvents());
		if(events.size() != 0){
			for (Event event : events){
				List<User> participants = event.getParticipants();
				if (participants.size() != 0){
					log.info("I will change owned event");
					User u2 = (User) entityManager.createNamedQuery("User.getUser",User.class)
						.setParameter("idUser",participants.get(0).getId())
						.getSingleResult();
					event.setCreator(u2);
					event.getParticipants().remove(u2);
					log.info("changed ok");
				} else {
					tags = event.getTags();
					log.info("Preparing events to be removed");
					log.info("I will Remove all event tags ");
					for (Tags tag : tags){
						tag.getEvents().remove(event);
						log.info("Remove event from tag " + tag.getId());
					}
					log.info("I will Remove event owned");
					u.getCreatedEvents().remove(event);				
					entityManager.createNamedQuery("Event.deleteEvent")
						.setParameter("idUser",event.getId())
						.executeUpdate();
					log.info("Removed event");
				}
			}

		}

		entityManager.flush();
		entityManager.createNamedQuery("User.deleteUser")
			.setParameter("idUser",id)
			.executeUpdate();
		
		List<User> usersU = entityManager.createNamedQuery("User.all").getResultList();
		sendMessageWS(usersU,"updateUsers");
		return "redirect:/admin/";
	}

	public void sendMessageWS(List content, String type) {
		log.info("Sending updated " + type + " via websocket");
		List response = new ArrayList();
		response.add(type);
		switch(type){
			case "updateUsers":
			response.add(User.asTransferObjects(content));
			break;
			case "updateEvents":
			response.add(Event.asTransferObjects(content));
			break;
		}
		messagingTemplate.convertAndSend("/topic/admin",response);
	}	
}
