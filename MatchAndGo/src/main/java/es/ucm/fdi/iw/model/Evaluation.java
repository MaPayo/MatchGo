package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Evaluation {

	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@ManyToOne
	private Usuario evaluator; //valorador
	
	@ManyToOne
	private Usuario evaluated; //valorado
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
	
	public Usuario getEvaluated() {
		return evaluated;
	}
	
	public void setEvaluated(Usuario evaluated) {
		this.evaluated = evaluated;
	}
	
	public Usuario getEvaluator() {
		return evaluator;
	}
	
	public void setValorante(Usuario evaluator) {
		this.evaluator = evaluator;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
}
