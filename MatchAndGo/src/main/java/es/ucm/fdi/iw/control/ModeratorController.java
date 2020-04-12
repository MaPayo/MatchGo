package es.ucm.fdi.iw.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Tags;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
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
    	Event e = new Event(1, "Event name", "Event description", "Event location");
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
    
    private List<Event> readAllEvents() {
    	 List<Event> events = entityManager.createNativeQuery("SELECT * FROM EVENT", Event.class).getResultList();
    	 return events;
    }
}