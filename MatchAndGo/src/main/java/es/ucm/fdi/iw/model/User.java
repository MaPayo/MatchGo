package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;



@Entity
@NamedQueries({
	@NamedQuery(name="User.byUsername", query= "SELECT u from User u WHERE "
			+ "u.username = :username")
})

public class User {

	public enum Role{
		USER,ADMIN, MOD
	}	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String username;
	private String firstName;
	private String lastName;

	@Column(unique=true)
	private String email;

	@Column(nullable = false)
	private String password;
	private String birthDate;
	private String gender;
	private String userRole;
	private String photo;
	
	private boolean enabled;
	
	@OneToMany(mappedBy="evaluated")
	private List<Evaluation> receivedEvaluation;

	@OneToMany(mappedBy="evaluator")
	private List<Evaluation> senderEvaluation;
	
	@OneToMany(mappedBy="sender")
	private List<Message> sentMessages;
	
	@OneToMany(mappedBy="receiver")
	private List<Message> receivedMessages;

	@ManyToMany
	private List<Tags> tags;
	
	@ManyToMany(mappedBy = "participants")
	private List<Event> joinedEvents;
	
	@OneToMany(mappedBy="creator")
	private List<Event> createdEvents;
	
	@Override
	public int hashCode() {
		return Long.hashCode(getId());
	}

	@Override
	public boolean equals(Object o) {
		return ((User)o).getId() == getId();
	}
	
	/**
	 * Checks whether this user has a given role.
	 * @param role to check
	 * @return true iff this user has that role.
	 */
	public boolean hasRole(Role role) {
		String roleName = role.name();
		return Arrays.stream(userRole.split(","))
				.anyMatch(r -> r.equals(roleName));
	}
	
	
	public List<Event> getJoinedEvents() {
		return joinedEvents;
	}


	public void setJoinedEvents(List<Event> joinedEvents) {
		this.joinedEvents = joinedEvents;
	}


	public List<Event> getCreatedEvents() {
		return createdEvents;
	}


	public void setCreatedEvents(List<Event> createdEvents) {
		this.createdEvents = createdEvents;
	}


	public User() {
		super();
	}
	
	
	public User(long id,String username, String firtName, String lastname, String email, String password, String birthate, String gender, String roles) {
		this.id = id;
		this.username= username;
		this.firstName = firtName;
		this.lastName = lastname;
		this.userRole = roles;
		this.password = password;
		this.email = email;
		this.birthDate = birthate;
		this.gender = gender;
	}
	
	
	/**
	 * Convierte colecciones de mensajes a formato JSONificable
	 * @param messages
	 * @return
	 * @throws JsonProcessingException
	 */
	public static List<Transfer> asTransferObjects(Collection<User> users) {
		ArrayList<Transfer> all = new ArrayList<>();
		for (User u : users) {
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
		private String username;
		private String firstName;
		private String lastName;
		private String email;
		private String password;
		private String birthDate;
		private String gender;
		private String userRole;
		private String photo;
		private boolean enabled;
		private List<Evaluation> receivedEvaluation;
		private List<Evaluation> senderEvaluation;
		private List<Message> sentMessages;
		private List<Message> receivedMessages;
		private List<Tags> tags;
		private List<Event> joinedEvents;
		private List<Event> createdEvents;
		

		public Transfer(User m) {
			this.id = m.getId();
			this.username = m.getUsername();
			this.firstName = m.getFirstName();
			this.lastName = m.getLastName();
			this.userRole = m.getUserRole();
			this.password = m.getPassword();
			this.email = m.getEmail();
			this.birthDate = m.getBirthDate();
			this.gender = m.getGender();
		}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastname) {
		this.lastName = lastname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String mail) {
		this.email = mail;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthdate) {
		this.birthDate = birthdate;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public void setPhoto(String img) {
		this.photo = img;
	}
	

	public List<Evaluation> getReceivedEvaluation() {
		return receivedEvaluation;
	}
	
	public void setReceivedEvaluation(List<Evaluation> evaluation) {
		this.receivedEvaluation = evaluation;
	}
	
	public List<Evaluation> getSenderEvaluation() {
		return senderEvaluation;
	}
	
	public void setSenderEvaluation(List<Evaluation> evaluation) {
		this.senderEvaluation = evaluation;
	}
	

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<Message> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	

	}
	

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastname) {
		this.lastName = lastname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String mail) {
		this.email = mail;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthdate) {
		this.birthDate = birthdate;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public void setPhoto(String img) {
		this.photo = img;
	}
	

	public List<Evaluation> getReceivedEvaluation() {
		return receivedEvaluation;
	}
	
	public void setReceivedEvaluation(List<Evaluation> evaluation) {
		this.receivedEvaluation = evaluation;
	}
	
	public List<Evaluation> getSenderEvaluation() {
		return senderEvaluation;
	}
	
	public void setSenderEvaluation(List<Evaluation> evaluation) {
		this.senderEvaluation = evaluation;
	}
	

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<Message> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	

}
