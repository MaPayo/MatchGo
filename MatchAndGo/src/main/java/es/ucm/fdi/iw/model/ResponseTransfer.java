package es.ucm.fdi.iw.model;

import java.util.List;

public class ResponseTransfer {
	private String text;
	private List<Event> events;
	
	public ResponseTransfer(String text) {
		super();
		this.text = text;
	}

	public ResponseTransfer(String text, List<Event> events) {
		super();
		this.text = text;
		this.events = events;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	@Override
	public String toString() {
		return "ResponseTransfer [text=" + text + ", events=" + events + "]";
	}
	
	
}
