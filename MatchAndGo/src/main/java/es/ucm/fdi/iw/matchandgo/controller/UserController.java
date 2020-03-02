package es.ucm.fdi.iw.matchandgo.controller;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.matchandgo.model.Tags;
import es.ucm.fdi.iw.matchandgo.model.Usuario;
import es.ucm.fdi.iw.matchandgo.model.Valoracion;

@Controller()
@RequestMapping("user")
public class UserController {

	@Autowired 
	private EntityManager entityManager;
	

	@GetMapping("/")
	public String getUserInSession(Model model, HttpSession session) {
		Usuario user = (Usuario) session.getAttribute("user"); // <-- este usuario no está conectado a la bd
        user = entityManager.find(Usuario.class, user.getId()); // <-- obtengo usuario de la BD
		return "redirect:/user/"+user.getId();
	}

	@GetMapping("/{id}")
	public String getUser(@PathVariable long id, Model model, HttpSession session) {
		Usuario usuario = entityManager.find(Usuario.class, id);
		model.addAttribute("user", usuario);
		return "user";
	}


	@RequestMapping(value = "/modificarPerfil{id}", method = RequestMethod.POST)
	@Transactional
	public String setUser(@RequestParam String email, @RequestParam String password,
			@RequestParam String nombre, @RequestParam String apellidos,
			@RequestParam String sexo,@RequestParam String fecha_nacimiento,
			@RequestParam Tags  form_tags,@RequestParam String img_perfil,
			HttpSession session) {
 
		
			return "";

	}
	
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	@Transactional
	public String createUser(@RequestParam String email, @RequestParam String password,
			@RequestParam String nombre, @RequestParam String apellidos,
			@RequestParam String sexo,@RequestParam String fecha_nacimiento,
			@RequestParam Tags  form_tags,@RequestParam String img_perfil,
			HttpSession session) {

		if (entityManager.createNamedQuery("User.ByEmail").setParameter("email", email).getResultList().isEmpty()) {

			Usuario user = new Usuario();
			user.setCorreo(email);
			user.setPassword(password);
			user.setNombre(nombre);
			user.setApellidos(apellidos);
			//la fecha creo que hay que mapearla
			user.setFecha_nac(fecha_nacimiento);
			user.setSexo(sexo);
			//Aqui algo habria que hacer con la foto ya que es un fichero
			user.setImagen(img_perfil);
			
			//
		//	user.setTags(new ArrayList<form_tags>());
			//user.setValoracion(new ArrayList<Valoracion>());
			user.setRoles("USER");

			entityManager.persist(user);
			session.setAttribute("user", user);
			return "redirect:/user/" + user.getId();

		}else {
			session.setAttribute("user", null);
			return "profile";
		}
	}

	/*@GetMapping(value = "/{id}/photo")
	public getImgUser() {
		
	}*/
	

}
