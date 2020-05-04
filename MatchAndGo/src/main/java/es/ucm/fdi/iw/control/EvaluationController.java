package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import es.ucm.fdi.iw.model.Evaluation;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Event.Access;
import es.ucm.fdi.iw.model.Tags;
import es.ucm.fdi.iw.model.User.Role;

/**
 * User-administration controller
 * 
 * @author MaPayo
 */
@Controller()
@RequestMapping("reviews")
public class EvaluationController {
    private static final Logger log = LogManager.getLogger(EventController.class);
	
	@Autowired 
	private EntityManager entityManager;
	

    @PostMapping("/review/{id}")
	@Transactional
	public String postReview(
			HttpServletResponse response,
            @PathVariable long id, 
            @ModelAttribute User evaluated,
            @RequestParam double score, 
            @RequestParam String review,
			Model model, HttpSession session) {
		
		//Conseguimos de la session el usuario que va a evaluar
        User evaluator = (User)session.getAttribute("u");
        //Creamos la evaluacion
        Evaluation e = new Evaluation();
        e.setevaluator(evaluator);
        e.setEvaluated(evaluated);
        e.setScore(score);
        e.setReview(review);
        evaluator.getSenderEvaluation().add(e);
        entityManager.persist(evaluator);
        entityManager.persist(e);
        entityManager.flush();
        session.removeAttribute("u");
        session.setAttribute("u", evaluator);
        log.info("Review registred between " + evaluator.getUsername() + " and " +evaluated.getUsername() +"with ID " + e.getId());
        return "redirect:/event/"+ id;
    }


    @GetMapping("/reviewuser/{id}")
    public String getReview(@PathVariable long id, Model model){
        TypedQuery<Evaluation> query= entityManager.createNamedQuery("Evaluation.getreviews", Evaluation.class);
        query.setParameter("idUser", id);
		List<Evaluation> review= query.getResultList();
		model.addAttribute("review", review);
        return "user";
    }
	/**
	 * @author Carlos Olano
	 * **/
	@PostMapping(path = "/user/{id}", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Evaluation.Transfer> getEvaluationsUsers(@PathVariable long id, Model model){
		final User ev = entityManager.createNamedQuery("User.getUser", User.class)
			.setParameter("idUser", id)
			.getSingleResult();
		List<Evaluation> valors = new ArrayList (ev.getReceivedEvaluation());
		return Evaluation.asTransferObjects(valors);
	}
	/**
	 * END
	 **/
}
