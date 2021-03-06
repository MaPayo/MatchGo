package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import es.ucm.fdi.iw.model.User.Role;

import es.ucm.fdi.iw.model.User.Transfer;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import es.ucm.fdi.iw.model.User.Role;

@Entity
/**
 * @author Carlos Olano
 * @author MaPayo
 * @author RoNiTo0
 */
@NamedQueries({
	@NamedQuery(name="Event.getModerator", query= "SELECT e FROM Event e WHERE IS_APPROPRIATE IS NULL AND (CREATOR_ID != :id OR CREATOR_ID IS NULL)"),
	@NamedQuery(name="Event.getEventsByTags", query= "Select e from Event e inner join e.tags t where t.id = :idCat and is_appropriate is true"),
	@NamedQuery(name="Event.blockEvent", query= "UPDATE Event SET isAppropriate = :state " + "WHERE id = :idUser"),
	@NamedQuery(name="Event.getEvent", query= "SELECT u from Event u WHERE u.id = :idUser"),
	@NamedQuery(name="Event.getEventSearchWC", query= "SELECT u from Event u inner join u.tags t WHERE "+
       		"(lower(name) LIKE :textToSearch OR lower(description) LIKE :textToSearch OR lower(location) LIKE :textToSearch) "+
		"AND t.id = :tagToSearch"),
	@NamedQuery(name="Event.getEventSearch", query= "SELECT u from Event u WHERE "+
       		"(lower(name) LIKE :textToSearch OR lower(description) LIKE :textToSearch OR lower(location) LIKE :textToSearch)"),
	@NamedQuery(name="Event.allAppropriate", query= "SELECT u from Event u where is_appropriate is true"),
	@NamedQuery(name="Event.all", query= "SELECT u from Event u"),
	@NamedQuery(name="Event.deleteEvent", query= "DELETE FROM Event u WHERE "
		+ "u.id = :idUser"),
	@NamedQuery(name="Event.searchByName", query="SELECT u FROM Event u WHERE lower(u.name) LIKE lower(:uname)"),
	@NamedQuery(name="Event.searchByLocation", query="SELECT u FROM Event u WHERE lower(u.location) LIKE lower(:ulocation)"),
	@NamedQuery(name="Tag.getEventCategories", query="SELECT e From Event e WHERE e.tags = :ucategory"),
	@NamedQuery(name="Event.participants", query="SELECT e.participants FROM Event e  WHERE e.id = :eid " )
})


/**
 * End
 */
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private String location;
	private String photo; //not saved
	
	private Boolean privateLocation;
	private Boolean privateDate;
	private String agePreference;
	private String genderPreference;
	@OneToMany(targetEntity = Message.class)
	private List<Message> messagesGroup;


	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime date; 
	private LocalDateTime publicationDate; 

	@Column(columnDefinition = "boolean default null")
	private Boolean isAppropriate;

	@ManyToMany(fetch = FetchType.EAGER)

	private List<Tags> tags;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private List<User> participants;
	

	
	@ManyToOne(targetEntity = User.class)
	private User creator;
	
	public enum Access {
		CREATOR,
		PARTICIPANT,
		MINIMAL		
	};
	
	public static List<TransferEvent> asTransferObjects(Collection<Event> events) {
		ArrayList<TransferEvent> all = new ArrayList<>();
		for (Event e : events) {
			all.add(new TransferEvent(e));
		}
		return all;
	}


	public static class TransferEvent {
		public enum Access {
			CREATOR,
			PARTICIPANT,
			MINIMAL		
		};
		
		private long id;
		private String name;
		private String description;
		private String location;
		private String photo;
		private LocalDateTime date;
		private LocalDateTime publicationDate;
		private Boolean isAppropriate;
		private Boolean privateLocation;
		private Boolean privateDate;
		private String agePreference;
		private String genderPreference;
		
		private List<String> tagNames;
		private List<Long> participants;
		private Long creator;

		public TransferEvent(Event e) {
			this.id = e.getId();
			this.name = e.getName();
			this.description = e.getDescription();
			this.location = e.getLocation();
			this.photo = e.getPhoto();
			this.date = e.getDate();
			this.publicationDate = e.getPublicationDate();
			this.isAppropriate = e.getIsAppropriate();
			this.tagNames = new ArrayList();
			this.participants = new ArrayList();
			this.privateLocation = e.getPrivateLocation();
			this.privateDate = e.getPrivateDate();
			this.agePreference = e.getAgePreference();
			this.genderPreference = e.getGenderPreference();
			
			if(e.creator != null)
				this.creator = e.getCreator().getId();
			
			if(e.getTags() != null)
				e.getTags().forEach(tag -> this.tagNames.add(tag.getTag()));
			if(e.getParticipants() != null)
				e.getParticipants().forEach(user -> this.participants.add(user.getId()));
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

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}
		
		public Boolean getPrivateLocation() {
			return privateLocation;
		}

		public void setPrivateLocation(Boolean privateLocation) {
			this.privateLocation = privateLocation;
		}

		public Boolean getPrivateDate() {
			return privateDate;
		}

		public void setPrivateDate(Boolean privateDate) {
			this.privateDate = privateDate;
		}

		public String getAgePreference() {
			return agePreference;
		}

		public void setAgePreference(String agePreference) {
			this.agePreference = agePreference;
		}

		public String getGenderPreference() {
			return genderPreference;
		}

		public void setGenderPreference(String genderPreference) {
			this.genderPreference = genderPreference;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public LocalDateTime getDate() {
			return date;
		}

		public void setDate(LocalDateTime date) {
			this.date = date;
		}

		public LocalDateTime getPublicationDate() {
			return publicationDate;
		}

		public void setPublicationDate(LocalDateTime publicationDate) {
			this.publicationDate = publicationDate;
		}

		public Boolean getIsAppropriate() {
			return isAppropriate;
		}

		public void setIsAppropriate(Boolean isAppropriate) {
			this.isAppropriate = isAppropriate;
		}

		public List<String> getTagNames() {
			return tagNames;
		}

		public void setTagNames(List<String> tagNames) {
			this.tagNames = tagNames;
		}

		public List<Long> getParticipants() {
			return participants;
		}

		public void setParticipants(List<Long> participants) {
			this.participants = participants;
		}

		public Long getCreator() {
			return creator;
		}

		public void setCreator(Long creator) {
			this.creator = creator;
		}

		@Override
		public String toString() {
			return "TransferEvent [id=" + id + ", name=" + name + ", description=" + description + ", location="
					+ location + ", photo=" + photo + ", date=" + date + ", publicationDate=" + publicationDate
					+ ", isAppropriate=" + isAppropriate + ", tagNames=" + tagNames + ", participants=" + participants
					+ ", creator=" + creator + "]";
		}
	}

	public Event() {
		super();
	}
	
	public Event(String name) {
		super();
		this.name = name;
	}

	public Event(long id, String name, String description, String location) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.date = LocalDateTime.now();
		this.publicationDate = LocalDateTime.now();
	}

	public Access checkAccess(User u) {
		if (u.getId() == creator.getId()) return Access.CREATOR;
		if (u.hasRole(Role.ADMIN) || participants.contains(u)) return Access.PARTICIPANT;
		return Access.MINIMAL;
		
	}
	
	private boolean isParticipant(User u) {
		for(User p : this.participants) {
			if(p.getId() == u.getId())
				return true;
		}
		return false;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public Boolean getPrivateLocation() {
		return privateLocation;
	}

	public void setPrivateLocation(Boolean privateLocation) {
		this.privateLocation = privateLocation;
	}

	public Boolean getPrivateDate() {
		return privateDate;
	}

	public void setPrivateDate(Boolean privateDate) {
		this.privateDate = privateDate;
	}

	public String getAgePreference() {
		return agePreference;
	}

	public void setAgePreference(String agePreference) {
		this.agePreference = agePreference;
	}

	public String getGenderPreference() {
		return genderPreference;
	}

	public void setGenderPreference(String genderPreference) {
		this.genderPreference = genderPreference;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getPublicationDate() {
		return publicationDate;
	}
	
	public void setPublicationDate(LocalDateTime publicationDate) {
		this.publicationDate = publicationDate;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Boolean getIsAppropriate() {
		return isAppropriate;
	}

	public void setIsAppropriate(Boolean isAppropriate) {
		this.isAppropriate = isAppropriate;
	}
	public List<Message> getMessages(){
		return messagesGroup;
	}
	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", description=" + description + ", location=" + location + "]";
	}
	
}
