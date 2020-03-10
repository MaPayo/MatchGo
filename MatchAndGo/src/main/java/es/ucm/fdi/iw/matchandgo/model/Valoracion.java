package es.ucm.fdi.iw.matchandgo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Valoracion {

	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@ManyToOne
	private Usuario valorante;
	
	@ManyToOne
	private Usuario valorado;
	private double puntuacion;
	private String comentario;
	
	public Valoracion() {
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
	
	public Usuario getValorado() {
		return valorado;
	}
	
	public void setValorado(Usuario valorado) {
		this.valorado = valorado;
	}
	
	public Usuario getValorante() {
		return valorante;
	}
	
	public void setValorante(Usuario valorante) {
		this.valorante = valorante;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
