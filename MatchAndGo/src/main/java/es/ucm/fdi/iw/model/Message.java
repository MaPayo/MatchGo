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
	@NamedQuery(name="Message.getEventMessages", query= "SELECT m FROM Message m WHERE event_id = :idUser ORDER BY send_date"),
	@NamedQuery(name="Message.deleteEventMessages", query= "DELETE FROM Message u WHERE event_id = :idUser"),
	@NamedQuery(name="Message.deleteMessagesUser", query= "DELETE FROM Message u WHERE sender_id = :idUser OR receiver_id = :idUser"),
	@NamedQuery(name="Message.getListMessages", query= "SELECT m from Message m WHERE event_id = null AND "
		+ "((sender_id = :sender AND receiver_id = :receiver) OR (sender_id = :receiver AND receiver_id = :sender))"
		+ "ORDER BY send_date ASC"),
	@NamedQuery(name="Message.getSendedUsers", query= "SELECT DISTINCT m.receiver from Message m WHERE "
		+ "sender_id = :sender"),
	@NamedQuery(name="Message.getReceivedUsers", query= "SELECT DISTINCT m.sender from Message m WHERE "
		+ "receiver_id = :receiver")
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
	private Event event;
	private boolean readMessage;

	public Message() {
	}

	public Message(String c, User s, User r,LocalDateTime f, boolean e, Event ev) {
		this.textMessage = c;
		this.sender = s;
		this.receiver = r;
		this.sendDate = f;
		this.readMessage = e;
		this.event = ev;
	}

	public Message(String c, User s, User r, LocalDateTime f) {
		this(c, s, r, f, false, null);
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

	public String toString() {
		return new Transfer(this).toString();
	}

	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 * @author EnriqueTorrijos
	 */
	public static class Transfer {
		private String sender;
		private String senderId;
		private String receiver;
		private String receiverId;
		private String sendDate;
		private String readMessage;
		private String textMessage;
		private String id;

		public Transfer(){}

		public Transfer(Message m) {
			this.sender = m.getSender().getUsername();
			if(m.getReceiver() != null){
				this.receiver = m.getReceiver().getUsername();
			}
			this.senderId = Long.toString(m.getSender().getId());
			if(m.getReceiver() != null){
				this.receiverId = Long.toString(m.getReceiver().getId());
			}
			this.sendDate = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getSendDate());
			this.readMessage = String.valueOf(m.getReadMessage());
			this.textMessage = m.getTextMessage();
			this.id = Long.toString(m.getId());
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getSenderId() {
			return senderId;
		}
		public void setSenderId(String senderId) {
			this.senderId = senderId;
		}
		public String getReceiver() {
			return receiver;
		}
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		public String getReceiverId() {
			return receiverId;
		}
		public void setReceiverId(String receiverId) {
			this.receiverId = receiverId;
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
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "id " + id + " from " + senderId +  " to " + receiverId + " saying '" + textMessage + "'";
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
