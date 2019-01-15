package es.caecv.gestiona;

public enum GestionaErrorMessage {

	UNDEFINED_ERROR("ERR000", "Error desconocido"), INPUT_REQUIRED("ERR001", "Es necesario proporcionar un NIF/CIF/DNI"), UNREACHABLE_DATABASE("ERR002", "No hay conexión a la base de datos"), INPUT_INVALID("ERR003", "El NIF/CIF/DNI proporcionado no es válido"), DATABASE_ERROR("ERR004", "Error en consulta a la base de datos");

	private String code;
	private String message;

	GestionaErrorMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}
