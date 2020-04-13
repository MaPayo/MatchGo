package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;

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