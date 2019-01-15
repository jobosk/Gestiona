package es.caecv.gestiona;

import javax.xml.ws.WebFault;

@WebFault(name = "GestionaException")
public class GestionaException extends Exception {

	private GestionaFault fault;

	public GestionaException(GestionaErrorMessage message, Exception e) {
		super(message.getMessage(), e);
		this.fault = new GestionaFault(message);
	}

	public GestionaFault getFault() {
		return fault;
	}
}
