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

@Entity
public class Message {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String text;
	@ManyToOne(targetEntity = User.class)
	private User sender;			// La persona que lo envía
	@ManyToOne(targetEntity = User.class)
	private User receiver;		// La persona que lo recibe
	private LocalDateTime sendDate;		// Hay que tener en cuenta el tipo java.sql.Date para las query SQL
	private boolean read;

	public Message() {
		super();
	}

    public Message(long id, String c, User s, User r,LocalDateTime f, boolean e) {
		super();
		this.id = id;
        this.text = c;
		this.sender = s;
		this.receiver = r;
		this.sendDate = f;
		this.read = e;
	}
	
	public Message(String c, User s, User r, LocalDateTime f) {
		super();
		this.text = c;
		this.sender = s;
		this.receiver = r;
		this.sendDate = f;
		this.read = false;
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
		private String read;
		private String text;
		long id;

		public Transfer(Message m) {
			this.sender = m.getSender().getUsername();
			this.receiver = m.getReceiver().getUsername();
			this.sendDate = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getSendDate());
			this.read = String.valueOf(m.getRead());
			this.text = m.getText();
			this.id = m.getId();
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getreceiver() {
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
		public String getRead() {
			return read;
		}
		public void setRead(String read) {
			this.read = read;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
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
    public String getText() {
        return this.text;
	}
	
	public void setText(String text) {
		this.text = text;
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
	public boolean getRead() {
		return this.read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}	
}