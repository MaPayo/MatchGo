package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Message {

	public enum EstadoMensaje {
		LEIDO, PENDIENTE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String contenido;
	@ManyToOne(targetEntity = User.class)
	private User sender;			// La persona que lo envía
	@ManyToOne(targetEntity = User.class)
	private User receiver;		// La persona que lo recibe
	private LocalDateTime fecha;				// Hay que tener en cuenta el tipo java.sql.Date para las query SQL
	private EstadoMensaje estado;

	public Message() {
		super();
	}

    public Message(long id, String c, User s, User r,LocalDateTime f, EstadoMensaje e) {
		super();
		this.id = id;
        this.contenido = c;
		this.sender = s;
		this.receiver = r;
		this.fecha = f;
		this.estado = e;
	}
	
	public Message(String c, User s, User r, LocalDateTime f) {
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
    public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	/**
     * @return the receiver
     */
	public User getReceiver() {
		return this.receiver;
	}

	public void setReceiver(User receiver) {
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