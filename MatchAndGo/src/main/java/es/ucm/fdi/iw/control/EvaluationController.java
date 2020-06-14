package es.ucm.fdi.iw.control;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Evaluation;

/**
 * User-administration controller
 *@author Carlos Olano 
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

	@PostMapping(path = "/add", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Evaluation.Transfer> addReview(@RequestBody JsonNode nodej, Model model, HttpSession session){
		final String text = nodej.get("textN").asText();
		final long idU = Long.parseLong(nodej.get("idU").asText()); 
		final long score = Long.parseLong(nodej.get("score").asText()); 
		final User v = (User)session.getAttribute("u");
		final User eva = (User) entityManager.createNamedQuery("User.getUser", User.class)
			.setParameter("idUser", idU).getSingleResult();
		List<String> wordsToCheck = Arrays.asList(text);
		if (!Utilities.checkStrings(wordsToCheck)){
			Evaluation e = new Evaluation(v, eva,(long)score,text);
			entityManager.persist(e);
			entityManager.flush();
		}
		return Evaluation.asTransferObjects(eva.getReceivedEvaluation());
	}
	@PostMapping(path = "/user/{id}", produces = "application/json")
	@Transactional
	@ResponseBody
	public List<Evaluation.Transfer> getEvaluationsUsers(@PathVariable long id, Model model){
		final User ev = entityManager.createNamedQuery("User.getUser", User.class)
			.setParameter("idUser", id)
			.getSingleResult();
		return Evaluation.asTransferObjects(ev.getReceivedEvaluation());
	}
	/**
	 * END
	 **/
}
