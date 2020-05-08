package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity

@NamedQueries({
@NamedQuery(name="Tags.deleteTag", query="DELETE FROM Tags  WHERE id = :idTag"),
@NamedQuery(name="Tags.all", query="SELECT t FROM Tags t"),
@NamedQuery(name="Tag.getCategories", query="SELECT t FROM Tags t WHERE t.isCategory IS TRUE"),
@NamedQuery(name="Tag.getEventTagsByName", query="SELECT t FROM Tags t Where lower(t.tag) = :tagname")
})

public class Tags {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String tag;
	private boolean isCategory;
	@ManyToMany(mappedBy="tags")
	private List<User> subscribers;
	@ManyToMany(mappedBy="tags")
	private List<Event> events;

	public Tags() {
		super();
	}
	public boolean isCategory() {
		return isCategory;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public boolean getIsCategory() {
		return isCategory;
	}
	public void setisCategory(boolean isCategory) {
		this.isCategory = isCategory;
	}
	public List<User> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(List<User> subscribers) {
		this.subscribers = subscribers;
	}
	@Override
	public String toString() {
		return "{" +
			" \"id\": \"" + getId() + "\"" +
			", \"tag\": \"" + getTag() + "\"" +
			"}";
	}

	public static List<Transfer> asTransferObjects(Collection<Tags> allTags) {
		ArrayList<Transfer> all = new ArrayList<>();
		for (Tags t : allTags) {
			all.add(new Transfer(t));
		}
		return all;
	}
	public static class Transfer{
		private long id;
		private String tag;

		public Transfer(Tags t) {
			super();
			id = t.getId();
			tag = t.getTag();
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
	}
}
