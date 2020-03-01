package es.ucm.fdi.iw.matchandgo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Valoracion {

	private long id;
	private Usuario idValorante;
	private Usuario idValorado;
	private double puntuacion;
	private String comentario;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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
	
	@ManyToOne(targetEntity=Usuario.class)
	public Usuario getIdValorado() {
		return idValorado;
	}
	
	public void setIdValorado(Usuario idValorado) {
		this.idValorado = idValorado;
	}
	
	@ManyToOne(targetEntity=Usuario.class)
	public Usuario getiDValorante() {
		return idValorante;
	}
	
	public void setiDValorante(Usuario iDValorante) {
		this.idValorante = iDValorante;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
