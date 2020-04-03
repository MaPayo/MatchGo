package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import es.ucm.fdi.iw.model.User.Role;

@Entity
public class Evento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nombre;
	private String descripcion;
	private String ubicacion;
	private LocalDateTime fecha;
	private LocalDateTime publicada;
	
	public enum Access {
		CREATOR,
		PARTICIPANT,
		MINIMAL		
	};

	public Evento() {
		super();
	}
	
	public Evento(String nombre) {
		super();
		this.nombre = nombre;
	}

	public Evento(long id, String nombre, String descripcion, String ubicacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.ubicacion = ubicacion;
		this.fecha = LocalDateTime.now();
		this.publicada = LocalDateTime.now();
	}

	public Access checkAccess(User u) {
		if (u.getId() == creador.getId()) return Access.CREATOR;
		if (u.hasRole(Role.ADMIN) || participantes.contains(u)) return Access.PARTICIPANT;
		return Access.MINIMAL;
	}


	@ManyToMany
	private List<Tags> tags;
	
	@ManyToMany
	private List<User> participantes;
	
	@ManyToOne
	private User creador;
	
	public long getId() {
	return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public LocalDateTime getPublicada() {
		return publicada;
	}

	public void setPublicada(LocalDateTime publicada) {
		this.publicada = publicada;
	}

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<User> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<User> participantes) {
		this.participantes = participantes;
	}

	public User getCreador() {
		return creador;
	}

	public void setCreador(User creador) {
		this.creador = creador;
	}



}
