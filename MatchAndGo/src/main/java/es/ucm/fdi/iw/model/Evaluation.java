package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity

/**
 * @author Carlos Olano
 */
@NamedQueries({
	@NamedQuery(name="Evaluation.getreviews", query= "SELECT r from Evaluation r WHERE r.evaluated.id = :idUser"),
	@NamedQuery(name="Evaluation.deleteEvaluation", query= "DELETE FROM Evaluation u WHERE id = :idUser")
})


/**
 * End
 */

public class Evaluation {

	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@ManyToOne
	private User evaluator; //valorador
	
	@ManyToOne
	private User evaluated; //valorado
	private double score;
	private String review;
	
	public Evaluation() {
		super();
	}
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public User getEvaluated() {
		return evaluated;
	}
	
	public void setEvaluated(User evaluated) {
		this.evaluated = evaluated;
	}
	
	public User getEvaluator() {
		return evaluator;
	}
	
	public void setevaluator(User evaluator) {
		this.evaluator = evaluator;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
}
