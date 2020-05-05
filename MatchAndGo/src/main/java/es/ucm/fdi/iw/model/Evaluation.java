package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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


	public Evaluation(User evaluator,User evaluated,double score,String review) {
		super();
		this.evaluator = evaluator;
		this.evaluated = evaluated;
		this.score = score;
		this.review = review;
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






	public static List<Transfer> asTransferObjects(Collection<Evaluation> users) {
		ArrayList<Transfer> all = new ArrayList<>();
		for (Evaluation u : users) {
			all.add(new Transfer(u));
		}
		return all;
	}



	public static class Transfer{
		private long id;
		private String evaluator;
		private String evaluated;
		private double score;
		private String review;
		
		public Transfer(Evaluation eva){
			this.id=eva.getId();
			this.evaluator = eva.getEvaluator().getUsername();
			this.score = eva.getScore();
			this.review = eva.getReview();
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

		public String getEvaluated() {
			return evaluated;
		}

		public void setEvaluated(User evaluated) {
			this.evaluated = evaluated.getUsername();
		}

		public String getEvaluator() {
			return evaluator;
		}

		public void setevaluator(User evaluator) {
			this.evaluator = evaluator.getUsername();
		}

		public String getReview() {
			return review;
		}

		public void setReview(String review) {
			this.review = review;
		}
	}
}
