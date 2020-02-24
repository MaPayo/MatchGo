package es.ucm.fdi.iw.matchandgo.control;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
	@GetMapping("/revisar") 
	public String index(
			Model model 
			) { 
			model.addAttribute("titulo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."); // escribe en modelo
			model.addAttribute("descripcion", "Orci a scelerisque purus semper eget duis at. Eleifend quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus.");
			model.addAttribute("tags", new ArrayList<String>() {{
				add("tag");
				add("tag1");
				add("tag2");
				add("tag3");
				add("tag4");
				add("tag5");
			}});
			model.addAttribute("listnum", new ArrayList<Integer>() {{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
				add(6);
			}});
			return "matchAndGoVistaModerador"; 
	}
}


