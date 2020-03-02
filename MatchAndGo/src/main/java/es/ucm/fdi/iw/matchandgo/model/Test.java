package es.ucm.fdi.iw.matchandgo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Test {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private int year;
	
	public Test() {}
	
	public Test(long id, String name, int year) {
		super();
		this.id = id;
		this.name = name;
		this.year = year;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Test [id=" + id + ", name=" + name + ", year=" + year + "]";
	}
}
