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
	private User valorante;
	
	@ManyToOne
	private User valorado;
	private double puntuacion;
	private String comentario;
	
	public Evaluation() {
		super();
	}
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public double getPuntuacion() {
		return puntuacion;
	}
	
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	public User getValorado() {
		return valorado;
	}
	
	public void setValorado(User valorado) {
		this.valorado = valorado;
	}
	
	public User getValorante() {
		return valorante;
	}
	
	public void setValorante(User valorante) {
		this.valorante = valorante;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
