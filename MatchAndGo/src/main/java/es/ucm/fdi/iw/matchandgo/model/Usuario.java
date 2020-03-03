package es.ucm.fdi.iw.matchandgo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;



@Entity
/*@NamedQueries({
	@NamedQuery(name="User.ByEmail", query= "SELECT u form Usuario u WHERE"
			+ "u.email= :email")
})*/

public class Usuario {

	public enum Role{
		USER,ADMIN, MOD
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nombre;
	private String apellidos;

	@Column(unique=true)
	private String correo;

	@Column(nullable = false)
	private String password;
	private String fecha_nac;
	private String sexo;
	private String roles;
	private String imagen;
	
	@OneToMany(mappedBy="idValorado")
	private List<Valoracion> valoracionesRecibidas;

	@OneToMany(mappedBy="idValorante")
	private List<Valoracion> valoracionesDadas;
	
	@OneToMany(mappedBy="sender")
	private List<Mensaje> listaMensajesEnviados;
	
	@OneToMany(mappedBy="receiver")
	private List<Mensaje> listaMensajesRecibidos;

	@ManyToMany
	private List<Tags> tags;
	
	@ManyToMany(mappedBy = "participantes")
	private List<Evento> joinedEvents;
	
	@OneToMany(mappedBy="creador")
	private List<Evento> createdEvents;
	
	
	public List<Evento> getJoinedEvents() {
		return joinedEvents;
	}


	public void setJoinedEvents(List<Evento> joinedEvents) {
		this.joinedEvents = joinedEvents;
	}


	public List<Evento> getCreatedEvents() {
		return createdEvents;
	}


	public void setCreatedEvents(List<Evento> createdEvents) {
		this.createdEvents = createdEvents;
	}


	public Usuario() {
		super();
	}
	
	
	public Usuario(long id, String nombre, String apellidos, String correo, String password, String fecha_nac, String sexo, String roles) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.roles = roles;
		this.password = password;
		this.correo = correo;
		this.fecha_nac = fecha_nac;
		this.sexo = sexo;
	}

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
	
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFecha_nac() {
		return fecha_nac;
	}
	
	public void setFecha_nac(String fecha_nac) {
		this.fecha_nac = fecha_nac;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getRoles() {
		return roles;
	}
	
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public void setImagen(String img) {
		this.imagen = img;
	}
	

	public List<Valoracion> getValoracionesRecibidas() {
		return valoracionesRecibidas;
	}
	
	public void setValoracionesRecibidas(List<Valoracion> valoracion) {
		this.valoracionesRecibidas = valoracion;
	}
	
	public List<Valoracion> getValoracionesDadas() {
		return valoracionesDadas;
	}
	
	public void setValoracionesDadas(List<Valoracion> valoracion) {
		this.valoracionesDadas = valoracion;
	}
	

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<Mensaje> getListaMensajesEnviados() {
		return listaMensajesEnviados;
	}

	public void setListaMensajesEnviados(List<Mensaje> listaMensajesEnviados) {
		this.listaMensajesEnviados = listaMensajesEnviados;
	}

	public List<Mensaje> getListaMensajesRecibidos() {
		return listaMensajesRecibidos;
	}

	public void setListaMensajesRecibidos(List<Mensaje> listaMensajesRecibidos) {
		this.listaMensajesRecibidos = listaMensajesRecibidos;
	}
	

}
