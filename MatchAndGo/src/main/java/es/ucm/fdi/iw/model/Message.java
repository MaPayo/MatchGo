package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity

@NamedQueries({
@NamedQuery(name="Message.getEventMessages", query= "SELECT m FROM Message m WHERE id_event_id = :idUser ORDER BY send_date"),
@NamedQuery(name="Message.deleteMessagesUser", query= "DELETE FROM Message u WHERE sender_id = :idUser OR receiver_id = :idUser"),
@NamedQuery(name="Message.getListMessages", query= "SELECT m from Message m WHERE "
+ "(sender_id = :sender AND receiver_id = :receiver AND id_event_id = null) OR (sender_id = :receiver AND receiver_id = :sender AND id_event_id = null) "
+ "ORDER BY send_date ASC")
})
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String textMessage;
	@ManyToOne(targetEntity = User.class)
	private User sender;			// La persona que lo envía
	@ManyToOne(targetEntity = User.class)
	private User receiver;		// La persona que lo recibe
	private LocalDateTime sendDate;		// Hay que tener en cuenta el tipo java.sql.Date para las query SQL
	@ManyToOne(targetEntity = Event.class)
	private Event idEvent;
	private boolean readMessage;

	public Message() {
		super();
	}

	public Message(String c, User s, User r,LocalDateTime f, boolean e, Event ev) {
		super();
		this.textMessage = c;
		this.sender = s;
		this.receiver = r;
		this.sendDate = f;
		this.readMessage = e;
		this.idEvent = ev;
	}
	public Message(long id, String c, User s, User r,LocalDateTime f, boolean e) {
		super();
		this.id = id;
		this.textMessage = c;
		this.sender = s;
		this.receiver = r;
		this.sendDate = f;
		this.readMessage = e;
	}

	public Message(String c, User s, User r, LocalDateTime f) {
		super();
		this.textMessage = c;
		this.sender = s;
		this.receiver = r;
		this.sendDate = f;
		this.readMessage = false;
	}

	/**
	 * Convierte colecciones de mensajes a formato JSONificable
	 * @param messages
	 * @return
	 * @throws JsonProcessingException
	 */
	public static List<Transfer> asTransferObjects(Collection<Message> messages) {
		ArrayList<Transfer> all = new ArrayList<>();
		for (Message m : messages) {
			all.add(new Transfer(m));
		}
		return all;
	}

	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 * @author EnriqueTorrijos
	 */
	public static class Transfer {
		private String sender;
		private String receiver;
		private String sendDate;
		private String readMessage;
		private String textMessage;
		long id;

		public Transfer(Message m) {
			this.sender = m.getSender().getUsername();
			if(m.getReceiver() != null){
				this.receiver = m.getReceiver().getUsername();
			}
			this.sendDate = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getSendDate());
			this.readMessage = String.valueOf(m.getReadMessage());
			this.textMessage = m.getTextMessage();
			this.id = m.getId();
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getReceiver() {
			return receiver;
		}
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		public String getSendDate() {
			return sendDate;
		}
		public void setSendDate(String sendDate) {
			this.sendDate = sendDate;
		}
		public String getReadMessage() {
			return readMessage;
		}
		public void setReadMessage(String read) {
			this.readMessage = read;
		}
		public String getTextMessage() {
			return textMessage;
		}
		public void setTextMessage(String text) {
			this.textMessage = text;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}		
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
	 * @return the text
	 */
	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String text) {
		this.textMessage = text;
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
	 * @return the sendDate
	 */
	public LocalDateTime getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(LocalDateTime sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the read
	 */
	public boolean getReadMessage() {
		return this.readMessage;
	}

	public void setReadMessage(boolean read) {
		this.readMessage = read;
	}	
}
