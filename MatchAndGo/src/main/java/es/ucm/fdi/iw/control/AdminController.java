package es.ucm.fdi.iw.control;

import java.util.List;
import java.io.File;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.LocalData;
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
	private Environment env;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
	//	model.addAttribute("users", entityManager.createQuery(
	//			"SELECT u FROM Usuario u").getResultList());
		
	//	model.addAttribute("allUsers", entityManager.createQuery(
	//			"SELECT u FROM Usuario u").getResultList());
		model.addAttribute("allEvents", entityManager.createQuery(
				"SELECT u FROM Evento u").getResultList());
	
		return "admin_view";
	}

	@GetMapping(path = "/userlist", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Usuario.Transfer> retrieveUsers(HttpSession session){
		log.info("Generating User List");
		List<Usuario> usuarios = entityManager.createQuery(
				"SELECT u FROM Usuario u").getResultList();
		return Usuario.asTransferObjects(usuarios);
	}


	
	@PostMapping("/toggleuser")
	@Transactional
	public String blockUser2(Model model,	@RequestParam long id) {
		Usuario target = entityManager.find(Usuario.class, id);
		if (target.isEnabled()) {
			// disable
			File f = localData.getFile("user", ""+id);
			if (f.exists()) {
				f.delete();
			}
			// disable user
			target.setEnabled(false); 
		} else {
			// enable user
			target.setEnabled(true);
		}
		return index(model);
	}	
	@PostMapping("/blockUser")
	@Transactional
	public String blockUser(Model model, @RequestParam long id) {
		Usuario target = entityManager.find(Usuario.class, id);
		boolean newState = false;
		if (target.isEnabled()){
			newState = false;
		} else {
			newState = true;
		}
		entityManager.createNamedQuery("Usuario.blockUser")
			.setParameter("idUser",id)
			.setParameter("state",newState)
			.executeUpdate();
		return index(model);
	}	


	@GetMapping("/deleteUser")
	@Transactional
	public String deleteUser(Model model, @RequestParam long id) {		
		entityManager.createNamedQuery("Usuario.deleteUser")
			.setParameter("idUser",id)
			.executeUpdate();
		return index(model);
	}	
}
