package es.ucm.fdi.iw.model;

public class ValidEvent {
	private boolean acept;

	public boolean isAcept() {
		return acept;
	}

	public void setAcept(boolean acept) {
		this.acept = acept;
	}

	@Override
	public String toString() {
		return "ValidEvent [acept=" + acept + "]";
	}

}
