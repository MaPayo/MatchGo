package es.ucm.fdi.iw.model;

import java.util.List;

import es.ucm.fdi.iw.model.Event.TransferEvent;

public class ResponseTransfer {
	private String text;
	private List<TransferEvent> events;
	
	
	public ResponseTransfer(String text) {
		super();
		this.text = text;
	}

	public ResponseTransfer(String text, List<TransferEvent> events) {
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

	public List<TransferEvent> getEvents() {
		return events;
	}

	public void setEvents(List<TransferEvent> events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return "ResponseTransfer [text=" + text + ", events=" + events + "]";
	}
}
