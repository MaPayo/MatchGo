package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Evaluation;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Tags;
import es.ucm.fdi.iw.model.Tags.Transfer;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

/**
 * User-administration controller
 * 
 * @author mfreire
 */
@Controller()
@RequestMapping("tag")
public class TagController {
	
	private static final Logger log = LogManager.getLogger(TagController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@PostMapping(path = "/listTags/{id}", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Transfer> listTagUser (final HttpSession session, @PathVariable long id) {

		log.warn("Entra en listagUser");
		User u = entityManager.find(User.class, id);
		final List<Tags> user_tags = new ArrayList<>(u.getTags()); 
		List<Transfer> alltagsUser = Tags.asTransferObjects(user_tags);
	
	/*	for(Tags t: user_tags) {
			alltagsUser.add(t.toString());
		}*/
	
		log.warn("Manda la lista de tags del user");
		return alltagsUser;
	}

	@PostMapping(path = "/addTagUser/{id}")
	@Transactional
	public void addUserTag (final HttpSession session, @PathVariable long id) {

		log.warn("Entra en listagUser");
		User u = (User) session.getAttribute("u");
		 
		Tags nuevaTag = entityManager.createNamedQuery("Tags.getTag", Tags.class)
				.setParameter("idTag", id).getSingleResult();
		u.getTags().add(nuevaTag);
		entityManager.persist(u);
		entityManager.flush();
		log.info("Añade la tag al usuario");
		
	}

	@PostMapping("/newTag/{tag}")
	@Transactional
	public String register(Model model, HttpServletRequest request, @PathVariable String tag, HttpSession session) {
			/**
			 * First we test all params are clean
			 **/

				User u = (User) session.getAttribute("u"); 
				u = entityManager.find(User.class, u.getId()); 
				log.warn("ENTRA en crear nueva tag");
				//redirigimos al registro si el usrname ya existe o las contraseñas no coinciden
				//aunq esto lo quiero hacer desde el html y que salga un aviso en la pagina
				if (tagExist(tag)) {
					//aqui habria que mostrar un alert o algo
					return "redirect:/user/"+u.getId();
				}
				// Creación de un usuario
				Tags t = new Tags();
				t.setTag(tag);
				entityManager.persist(t);
				u.getTags().add(t);
				entityManager.flush(); //guardar bbdd
				
				return "redirect:/user/" + u.getId();
	}

	private boolean tagExist(String tag) {
		return entityManager
				.createNamedQuery("Tags.getEventTagsByName", Long.class)
				.setParameter("tagname", tag)
				.getSingleResult() 
			!= 0;	// 0 = no user; >0 = number of user with that username

	}
}
