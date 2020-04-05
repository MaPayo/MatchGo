package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;



/**
 * @author colano
 */

@Entity
@NamedQueries({
	@NamedQuery(name="Usuario.byUsername", query= "SELECT u from Usuario u WHERE "
		+ "u.nombre = :username"),
	@NamedQuery(name="Usuario.all", query= "SELECT u from Usuario u"),
	@NamedQuery(name="Usuario.deleteUser", query= "DELETE FROM Usuario u WHERE "
		+ "u.id = :idUser"),
	@NamedQuery(name="Usuario.blockUser", query= "UPDATE Usuario u SET u.enabled = :state "
		+ "WHERE u.id = :idUser")
})

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

	private boolean enabled;

	@OneToMany(mappedBy="valorado")
	private List<Valoracion> valoracionesRecibidas;

	@OneToMany(mappedBy="valorante")
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



	/**
	 * Convierte colecciones de mensajes a formato JSONificable
	 * @param messages
	 * @return
	 * @throws JsonProcessingException
	 */
	public static List<Transfer> asTransferObjects(Collection<Usuario> users) {
		ArrayList<Transfer> all = new ArrayList<>();
		for (Usuario u : users) {
			all.add(new Transfer(u));
		}
		return all;
	}

	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 * @author colano
	 */

	public static class Transfer {
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
		private boolean enabled;
		private List<Valoracion> valoracionesRecibidas;
		private List<Valoracion> valoracionesDadas;
		private List<Mensaje> listaMensajesEnviados;
		private List<Mensaje> listaMensajesRecibidos;
		private List<Tags> tags;
		private List<Evento> joinedEvents;
		private List<Evento> createdEvents;

		public Transfer(Usuario m) {
			this.id = m.getId();
			this.nombre = m.getNombre();
			this.apellidos = m.getApellidos();
			this.roles = m.getRoles();
			this.password = m.getPassword();
			this.correo = m.getCorreo();
			this.fecha_nac = m.getFecha_nac();
			this.sexo = m.getSexo();
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
		public boolean isEnabled() {
			return enabled;
		}
		public boolean hasRole(Role role) {
			String roleName = role.name();
			return Arrays.stream(roles.split(","))
				.anyMatch(r -> r.equals(roleName));
		}
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
	}




	@Override
	public int hashCode() {
		return Long.hashCode(getId());
	}

	@Override
	public boolean equals(Object o) {
		return ((Usuario)o).getId() == getId();
	}

	/**
	 * Checks whether this user has a given role.
	 * @param role to check
	 * @return true iff this user has that role.
	 */
	public boolean hasRole(Role role) {
		String roleName = role.name();
		return Arrays.stream(roles.split(","))
			.anyMatch(r -> r.equals(roleName));
	}


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


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


}
