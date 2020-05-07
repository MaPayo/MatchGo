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
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Evaluation;
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
	public String index(final Model model) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		return "admin_view";
	}

	@PostMapping(path = "/eventlist", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Event.TransferEvent> retrieveEvents(final HttpSession session) {
		log.info("Generating Event List");
		List<Event> events = entityManager.createNamedQuery("Event.all",Event.class).getResultList();
		return Event.asTransferObjects(events);
	}

	@PostMapping(path = "/userlist", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<User.Transfer> retrieveUsers(final HttpSession session) {
		log.info("Generating User List");
		List<User> users = new ArrayList();
		for(User u : entityManager.createNamedQuery("User.all",User.class).getResultList()){
			if(!u.hasRole(User.Role.SYS)){
				users.add(u);
			}
		}
		return User.asTransferObjects(users);
	}
//	@PostMapping(path = "/taglist", produces = "application/json")
//	@Transactional
//	@ResponseBody
//	public List<Tags.Transfer> retrieveTags(final HttpSession session) {
//		log.info("Generating Tag List");
//		List<Tags> tags = entityManager.createNamedQuery("Tags.all",Tags.class).getResultList();
//		return Tags.asTransferObjects(tags);
//	}
	@PostMapping("/blockEvent")
	@Transactional
	public String blockEvent(Model model, @RequestParam long id) {
		Event target = entityManager.find(Event.class, id);
		boolean newState = false;
		if (target.getIsAppropriate()){
			newState = false;
		} else {
			newState = true;
		}
		entityManager.createNamedQuery("Event.blockEvent")
			.setParameter("idUser",id)
			.setParameter("state",newState)
			.executeUpdate();

		List<Event> eventsU = entityManager.createNamedQuery("Event.all",Event.class).getResultList();
		sendMessageWS(eventsU,"updateEvents","/topic/admin");
		return "redirect:/admin/";
	}	

	@PostMapping("/blockUser")
	@Transactional
	public String blockUser(final Model model, @RequestParam final long id) {
		final User target = entityManager.find(User.class, id);
		boolean newState = false;
		if (target.isEnabled()) {
			newState = false;
		} else {
			newState = true;
		}
		entityManager.createNamedQuery("User.blockUser").setParameter("idUser", id).setParameter("state", newState)
			.executeUpdate();

		List<User> users = new ArrayList();
		for(User u : entityManager.createNamedQuery("User.all",User.class).getResultList()){
			if(!u.hasRole(User.Role.SYS)){
				users.add(u);
			}
		}
		sendMessageWS(users,"updateUsers","/topic/admin");
		return "redirect:/admin/";
	}

	@PostMapping("/deleteEvent")
	@Transactional
	public String deleteEvent(final Model model, @RequestParam final long id) {
		final Event e = (Event) entityManager.createNamedQuery("Event.getEvent", Event.class).setParameter("idUser", id)
			.getSingleResult();
	
		log.info("I will Remove all Messages ");
		entityManager.createNamedQuery("Message.deleteEventMessages").setParameter("idUser", e.getId()).executeUpdate();
		log.info("removed all messages");


		final List<Tags> tags = new ArrayList<>(e.getTags());
		log.info("I will Remove all subcribed tags ");
		for (final Tags tag : tags) {
			e.getTags().remove(tag);
			log.info("Remove event from tag " + tag.getId());
		}

		final List<User> joined = new ArrayList<>(e.getParticipants());
		log.info("I will Remove all participants");
		for (final User u : joined) {
			e.getParticipants().remove(u);
			log.info("Remove user " + u.getId() + " from event " + e.getId());
		}

		log.info("I will remove from owned event");
		e.getCreator().getCreatedEvents().remove(e);

		log.info("I will remove event");
		entityManager.createNamedQuery("Event.deleteEvent").setParameter("idUser", e.getId()).executeUpdate();
		log.info("Removed event");

		entityManager.flush();
		final List<Event> eventsU = entityManager.createNamedQuery("Event.all",Event.class).getResultList();
		sendMessageWS(eventsU,"updateEvents","/topic/admin");
		log.info("all visitors redirect to other page this page is deleted");
		sendMessageWS(eventsU,"pleaseExit","/topic/event/"+id);
		return "redirect:/admin/";
	}

	@PostMapping("/deleteUser")
	@Transactional
	public String deleteUser(final Model model, @RequestParam final long id) {
		User u = (User) entityManager.createNamedQuery("User.getUser", User.class).setParameter("idUser", id)
			.getSingleResult();

		List<Tags> tags = u.getTags();
		log.info("I will Remove all subcribed tags ");
		for (final Tags tag : tags) {
			tag.getSubscribers().remove(u);
			log.info("Remove user from tag " + tag.getId());
		}

		List<Event> events = u.getJoinedEvents();
		log.info("I will Remove from all Events Joined");
		for (final Event event : events) {
			event.getParticipants().remove(u);
			long idE = event.getId();
			log.info("Remove user from event " + idE);
			log.info("Sending new userlist to topic event");
			sendMessageWS(event.getParticipants(),"updateUsersEvent","/topic/event/"+idE);
		}

		events = new ArrayList<>(u.getCreatedEvents());
		boolean flag = false;
		if (events.size() != 0) {
			for (final Event event : events) {
				final List<User> participants = event.getParticipants();
				if (participants.size() != 0) {
					log.info("I will change owned event");
					final User u2 = (User) entityManager.createNamedQuery("User.getUser", User.class)
						.setParameter("idUser", participants.get(0).getId()).getSingleResult();
					event.setCreator(u2);
					event.getParticipants().remove(u2);
					log.info("changed ok");
				} else {
					tags = event.getTags();
					log.info("Preparing events to be removed");
					log.info("I will Remove all event tags ");
					for (final Tags tag : tags) {
						tag.getEvents().remove(event);
						log.info("Remove event from tag " + tag.getId());
					}
					log.info("I will Remove event owned");
					u.getCreatedEvents().remove(event);
					entityManager.createNamedQuery("Event.deleteEvent").setParameter("idUser", event.getId())
						.executeUpdate();
					log.info("Removed event");
					flag = true;
				}
			}
		}


		List<Evaluation> evaluations = new ArrayList<>(u.getReceivedEvaluation());
		if (evaluations.size() != 0){
			log.info("I will Remove all received evaluations ");
			for (final Evaluation eva : evaluations) {
				entityManager.createNamedQuery("Evaluation.deleteEvaluation").setParameter("idUser", eva.getId()).executeUpdate();
				log.info("Removed evaluation {} from user {} ", eva.getId(),u.getId());
			}
		}

		final User noUser = (User) entityManager.createNamedQuery("User.getUser", User.class).setParameter("idUser", (long)3).getSingleResult();
		evaluations = new ArrayList<>(u.getSenderEvaluation());
		if (evaluations.size() != 0){
			log.info("I will Remove all writedd evaluations");

			for (final Evaluation eva : evaluations) {
				log.warn(eva.getEvaluator());
				eva.setevaluator(noUser);
				log.info("moved evaluation {} from user {} to noUser ", eva.getId(),u.getId());
				log.warn(eva.getEvaluator());
			}
		}

		entityManager.createNamedQuery("Message.deleteMessagesUser").setParameter("idUser",(long)u.getId()).executeUpdate();

		entityManager.flush();
		entityManager.createNamedQuery("User.deleteUser")
			.setParameter("idUser",id)
			.executeUpdate();

		List<User> users = new ArrayList();
		for(User u2 : entityManager.createNamedQuery("User.all",User.class).getResultList()){
			if(!u2.hasRole(User.Role.SYS)){
				users.add(u2);
			}
		}
		log.info("If user connected ");
		sendMessageWS(users,"sayGoodBye","/user/"+u.getUsername()+"/queue/updates");
		log.info("broadcast admin");
		sendMessageWS(users,"updateUsers","/topic/admin");
		if (flag){
			final List<Event> eventsU = entityManager.createNamedQuery("Event.all",Event.class).getResultList();
			sendMessageWS(eventsU,"updateEvents","/topic/admin");
		}
		return "redirect:/admin/";
	}

	public void sendMessageWS(final List content, final String type, final String topic) {
		log.info("Sending updated " + type + " via websocket");
		final List response = new ArrayList();
		response.add(type);
		switch(type){
			case "updateUsersEvent":
			case "updateUsers":
				response.add(User.asTransferObjects(content));
				break;
			case "updateEvents":
				response.add(Event.asTransferObjects(content));
				break;
			default:
				response.add("sorry");
		}
		messagingTemplate.convertAndSend(topic,response);
	}	
}
