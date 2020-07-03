package es.ucm.fdi.iw.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.core.env.Environment;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.ResponseTransfer;
import es.ucm.fdi.iw.model.Tags;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.ValidEvent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Moderator controller
 * 
 * @author Gasan Nazer
 */
@Controller()
@RequestMapping("moderator")
public class ModeratorController {

	private static final Logger log = LogManager.getLogger(ModeratorController.class);

	@Autowired
	private Environment env;
	@Autowired 
	private EntityManager entityManager;


	@GetMapping("/")
	@Transactional
	public String getUser(Model model, HttpSession session) {
		User user = (User)session.getAttribute("u");
		model.addAttribute("listnum", readAllNonEvaluatedEvents(user.getId()));
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		return "moderator";
	}

	@PostMapping(path = "/{id}", produces = "application/json")
	@Transactional
	@ResponseBody
	public ResponseTransfer aceptRejectEvent(@PathVariable("id") int id, @RequestBody ValidEvent body, HttpSession session){
		ResponseTransfer result;
		if(body.isAcept()) {
			log.info("Acepting an event with id:" + id + " Body: " + body);
			result = new ResponseTransfer("Event acepted.");
		}
		else {
			log.info("Reject an event with id:" + id + " Body: " + body);
			result = new ResponseTransfer("Event rejected.");
		}

		int updated = entityManager.createNativeQuery("UPDATE EVENT SET IS_APPROPRIATE=? where id=?").setParameter(1, body.isAcept()).setParameter(2, id).executeUpdate();

		if(updated >= 1) {
			log.info("Updated sucessfully");
		}
		else {
			log.error("The update was not sucessful.");
		}

		User user = (User)session.getAttribute("u");

		List<Event> events = readAllNonEvaluatedEvents(user.getId());

		result.setEvents(Event.asTransferObjects(events));

		return result;
	}

	@Transactional
	private List<Event> readAllNonEvaluatedEvents(long userId) {
		List<Event> events = entityManager.createNativeQuery("SELECT * FROM EVENT WHERE IS_APPROPRIATE IS NULL AND (CREATOR_ID != ? OR CREATOR_ID IS NULL)", Event.class).setParameter(1, userId).getResultList();
		return events;
	}


	@PostMapping(path = "/eventlist", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Event.TransferEvent> retrieveEvents(final HttpSession session) {
		User user = (User)session.getAttribute("u");
		List<Event> events = entityManager.createNamedQuery("Event.getModerator",Event.class).setParameter("id",(long)user.getId()).getResultList();
		return Event.asTransferObjects(events);
	}
}
