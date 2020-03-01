package es.ucm.fdi.iw.matchandgo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import es.ucm.fdi.iw.matchandgo.mensajes.Mensaje;

@Entity
@NamedQueries({
	@NamedQuery(name="User.ByEmail", query= "SELECT u form Usuario u WHERE"
			+ "u.email= :email")
})

public class Usuario {

	public enum Role{
		USER,ADMIN, MOD
	}
	private long id;
	private String nombre;
	private String apellidos;
	private String correo;
	private String password;
	private String fecha_nac;
	private String sexo;
	private String roles;
	private String imagen;
	
	
	private List<Valoracion> valoracion= new ArrayList<Valoracion>();
	
	private List<Mensaje> listaMensajes = new ArrayList<Mensaje>();
	
	private List<Tags> tags = new ArrayList<Tags>();
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@Column(unique=true)
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	@Column(nullable = false)
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
	
	@OneToMany(targetEntity=Valoracion.class)
	@JoinColumn(name="idValorado")
	public List<Valoracion> getValoracion() {
		return valoracion;
	}
	
	public void setValoracion(List<Valoracion> valoracion) {
		this.valoracion = valoracion;
	}
	
	@OneToMany(targetEntity=Mensaje.class)
	@JoinColumn(name="sender")
	public List<Mensaje> getListaMensajes() {
		return listaMensajes;
	}
	
	public void setListaMensajes(List<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}

	@ManyToMany(targetEntity=Tags.class)
	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}
	

}
