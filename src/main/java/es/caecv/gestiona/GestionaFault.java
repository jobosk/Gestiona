package es.caecv.gestiona;

public class GestionaFault {

	private String code;
	private String description;

	public GestionaFault(GestionaErrorMessage message) {
		this.code = message.getCode();
		this.description = message.getMessage();
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
