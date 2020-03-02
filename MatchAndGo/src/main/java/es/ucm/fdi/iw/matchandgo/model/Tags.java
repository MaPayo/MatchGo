package es.ucm.fdi.iw.matchandgo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Tags {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String contenido;
	private boolean categoriaTipoPorDefecto;

	@ManyToMany(mappedBy="tags")
	private List<Usuario> usuarioSubscrito;
	
	public Tags() {
		super();
	}
	
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public boolean getCategoriaTipo() {
		return categoriaTipoPorDefecto;
	}
	public void setCategoriaTipo(boolean categoriaTipo) {
		this.categoriaTipoPorDefecto = categoriaTipo;
	}
	
	public List<Usuario> getUsuarioSubscrito() {
		return usuarioSubscrito;
	}
	public void setUsuarioSubscrito(List<Usuario> usuarioSubscrito) {
		this.usuarioSubscrito = usuarioSubscrito;
	}
}
