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
import java.util.Date;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

/**
 * User-administration controller
 * 
 * @author mfreire
 */
@Controller()
@RequestMapping("user")
public class UserController {
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/")
	public String getUserSession(Model model, HttpSession session) {
		User u = (User) session.getAttribute("u"); 
        u = entityManager.find(User.class, u.getId()); 
		return "redirect:/user/"+u.getId();
	}
	
	@GetMapping("/{id}")
	public String getUser(@PathVariable long id, Model model, HttpSession session) {
		User u = entityManager.find(User.class, id);
		model.addAttribute("user", u);
		return "profile";
	}

	@PostMapping("/{id}")
	@Transactional
	public String postUser(
			HttpServletResponse response,
			@PathVariable long id, 
			@ModelAttribute User edited, 
			@RequestParam(required=false) String pass2,
			Model model, HttpSession session) throws IOException {
		User target = entityManager.find(User.class, id);
		model.addAttribute("user", target);
		
		User requester = (User)session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				! requester.hasRole(Role.ADMIN)) {			
			response.sendError(HttpServletResponse.SC_FORBIDDEN, 
					"No eres administrador, y éste no es tu perfil");
		}
		
		if (edited.getPassword() != null && edited.getPassword().equals(pass2)) {
			// save encoded version of password
			target.setPassword(passwordEncoder.encode(edited.getPassword()));
		}		
		target.setUsername(edited.getUsername());
		return "profile";
	}	
	
	@GetMapping(value="/{id}/photo")
	public StreamingResponseBody getPhoto(@PathVariable long id, Model model) throws IOException {		
		File f = localData.getFile("user", ""+id);
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
		model.addAttribute("user", target);
		
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
		return "profile";
	}
	
	
	@GetMapping("/signup")
	public String getRegister(Model model, HttpSession session) {
		if(session.getAttribute("user") != null) 
			return "redirect:/user/" + ((User) session.getAttribute("user")).getId();
		return "login";
	}
	
	@PostMapping("/signup")
	@Transactional
	public String register(Model model, HttpServletRequest request, Principal principal, @RequestParam String username,
			@RequestParam String password, @RequestParam String password2, @RequestParam String email,
			@RequestParam String firstname, @RequestParam String lastname, @RequestParam String gender,
			@RequestParam Date birthdate, @RequestParam String tags,
			@RequestParam("userPhoto") MultipartFile userPhoto, HttpSession session) {

		
		//redirigimos al registro si el usrname ya existe o las contraseñas no coinciden
		//aunq esto lo quiero hacer desde el html y que salga un aviso en la pagina
		if (usernameAlreadyInUse(username) || !password.equals(password2)) {
			return "redirect:/user/login";
		}

		// Creación de un usuario
		User u = new User();
		u.setUsername(username);
		u.setPassword(passwordEncoder.encode(password));
		u.setUserRole("USER");
		u.setEmail(email);
		u.setFirstName(firstname);
		u.setLastName(lastname);
		//u.setBirthDate(birthdate);
		u.setGender(gender);
		u.setEnabled(true);
		
		//No se como tratar las tags
		
	
		entityManager.persist(u);
		entityManager.flush();
		log.info("Creating & logging new user {}, with ID {} and password {}", username, u.getId(), password);
		
		//En el momento en que se crea correctamente el usuario se inicia sesion y se redirige al perfil
		doAutoLogin(username, password, request);
		
		log.info("Created & logged new user {}, with ID {} and password {}", username, u.getId(), password);
		
		
		if (!userPhoto.isEmpty()) {
			File f = localData.getFile("user", String.valueOf(u.getId()));
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = userPhoto.getBytes();
				stream.write(bytes);
			} catch (Exception e) {
				log.info("Error uploading photo for user with ID {}", u.getId());
			}
			log.info("Successfully uploaded photo for {} into {}!", u.getId(), f.getAbsolutePath());
		}
		
		session.setAttribute("user", u);
		session.setAttribute("ws", request.getRequestURL().toString()
				.replaceFirst("[^:]*", "ws")		// http[s]://... => ws://...
				.replaceFirst("/user.*", "/ws"));

		return "redirect:/user/" + u.getId();
	}
	@GetMapping("/login")
	public String getLogin(Model model, HttpSession session) {
		if(session.getAttribute("user") != null) 
			return "redirect:/user/" + ((User) session.getAttribute("user")).getId();
	
		return "login";
	}

	@PostMapping("/login")
	@Transactional
	public String login(Model model, HttpServletRequest request, Principal principal, @RequestParam String userName,
			@RequestParam String password, HttpSession session) {

	
	
		// if the user exists, we check the if the password is correct
		if (usernameAlreadyInUse(userName)) {
			// Se saca la constraseña del usuario que se está loggeando
			String pass = entityManager.createNamedQuery("User.Password", String.class)
					.setParameter("userName", userName).getSingleResult();

			// Se compara la contraseña introducida con la contraseña cifrada de la BD
			boolean correct = passwordEncoder.matches(password, pass);
			log.info("The passwords match: {}", correct);
			if (correct) {
				User u = entityManager.createNamedQuery("User.ByName", User.class).setParameter("userName", userName)
						.getSingleResult();

				session.setAttribute("user", u);
				return "redirect:/user/" + u.getId(); // Devuelve el usuario loggeado
			} else {
				return "redirect:/login";
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.setAttribute("user", null);
		return "redirect:/index";
	}

	
	private boolean usernameAlreadyInUse(String userName) {
		Long usernameAlreadyInUse = entityManager.createNamedQuery("User.HasName", Long.class)
				.setParameter("userName", userName).getSingleResult();
		if(usernameAlreadyInUse != 0) {
			return true;
		}
		return false;
	}
	/**
	 * Non-interactive authentication; user and password must already exist
	 *
	 * @param username
	 * @param password
	 * @param request
	 */
	private void doAutoLogin(String username, String password, HttpServletRequest request) {
		try {
			// Must be called from request filtered by Spring Security, otherwise
			// SecurityContextHolder is not updated
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authenticationManager.authenticate(token);
			log.debug("Logging in with [{}]", authentication.getPrincipal());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			log.error("Failure in autoLogin", e);
		}
	}
}
