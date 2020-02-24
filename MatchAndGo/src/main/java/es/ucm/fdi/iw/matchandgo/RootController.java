package es.ucm.fdi.iw.matchandgo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {
	@GetMapping("/revisar") 
	public String index(
			Model model // comunicación con vist
			) { // viene del formulario
			model.addAttribute("titulo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."); // escribe en modelo
			model.addAttribute("descripcion", "Orci a scelerisque purus semper eget duis at. Eleifend quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus.");
			model.addAttribute("tags", "tag");
			return "metchAndGoVistaModerador"; // vista resultante
			}
	
	@GetMapping("/evento") 
	public String showEvent(
			Model model // comunicación con vist
			) { // viene del formulario
			return "matchAndGoEvento"; // vista resultante
			}
}


