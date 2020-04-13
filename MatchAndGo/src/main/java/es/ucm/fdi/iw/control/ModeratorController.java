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
	private EntityManager entityManager;
	
    
    @GetMapping("")
    @Transactional
	public String getUser(Model model) {
    	List<Event> listnum = readAllEvents();
    	Event e = new Event(10, "Event name", "Event description", "Event location");
    	List<Tags> tags = new ArrayList();
    	Tags tag = new Tags();
    	tag.setTag("sport");
    	tags.add(tag);
    	e.setTags(tags);
    	listnum.add(e);
    	
    	readAllEvents().stream().forEach(att -> System.out.println(att.getTags()));
    	
    	model.addAttribute("listnum", listnum);
		return "moderator";
	}
    
    @PostMapping(path = "/{id}"/*, produces = "application/json"*/)
	@Transactional
	@ResponseBody
	public ResponseTransfer aceptRejectEvent(@PathVariable("id") int id, @RequestBody ValidEvent body){
    	ResponseTransfer result;
		if(body.isAcept()) {
			log.warn("Acepting an event with id:" + id + " Body: " + body);
			result = new ResponseTransfer("Event acepted.");
		}
		else {
			log.warn("Reject an event with id:" + id + " Body: " + body);
			result = new ResponseTransfer("Event rejected.");
		}
		
		return result;
	}
    
    private List<Event> readAllEvents() {
    	 List<Event> events = entityManager.createNativeQuery("SELECT * FROM EVENT", Event.class).getResultList();
    	 return events;
    }
}