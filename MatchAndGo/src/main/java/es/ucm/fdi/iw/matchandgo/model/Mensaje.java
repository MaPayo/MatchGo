package es.ucm.fdi.iw.matchandgo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.ucm.fdi.iw.matchandgo.model.enums.EstadoMensaje;

@Entity
public class Mensaje {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String contenido;
	@ManyToOne(targetEntity = Usuario.class)
	private Usuario sender;			// La persona que lo envía
	@ManyToOne(targetEntity = Usuario.class)
	private Usuario receiver;		// La persona que lo recibe
	private LocalDateTime fecha;				// Hay que tener en cuenta el tipo java.sql.Date para las query SQL
	private EstadoMensaje estado;

	public Mensaje() {
		super();
	}

    public Mensaje(long id, String c, Usuario s, Usuario r,LocalDateTime f, EstadoMensaje e) {
		super();
		this.id = id;
        this.contenido = c;
		this.sender = s;
		this.receiver = r;
		this.fecha = f;
		this.estado = e;
	}
	
	public Mensaje(String c, Usuario s, Usuario r, LocalDateTime f) {
		super();
		this.contenido = c;
		this.sender = s;
		this.receiver = r;
		this.fecha = f;
	}
	
	/**
     * @return the id
     */
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
     * @return the contenido
     */
    public String getContenido() {
        return this.contenido;
	}
	
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
     * @return the sender
     */
    public Usuario getSender() {
		return sender;
	}

	public void setSender(Usuario sender) {
		this.sender = sender;
	}

	/**
     * @return the receiver
     */
	public Usuario getReceiver() {
		return this.receiver;
	}

	public void setReceiver(Usuario receiver) {
		this.receiver = receiver;
	}

	/**
     * @return the fecha
     */
	public LocalDateTime getFecha() {
		return this.fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	/**
     * @return the estado
     */
	public EstadoMensaje getEstado() {
		return this.estado;
	}

	public void setEstado(EstadoMensaje estado) {
		this.estado = estado;
	}	
}