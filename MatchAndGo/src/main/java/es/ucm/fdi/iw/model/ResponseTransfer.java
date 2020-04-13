package es.ucm.fdi.iw.model;

public class ResponseTransfer {
	private String text;
	
	public ResponseTransfer(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "ResponseTransfer [text=" + text + "]";
	}
}
