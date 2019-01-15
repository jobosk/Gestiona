package es.caecv.gestiona;

import es.caecv.gestiona.GestionaException;
import es.caecv.gestiona.Request;
import es.caecv.gestiona.Response;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface GestionaService {

	@WebMethod
	@WebResult(name = "informes_control")
	public Response consultaOperadorCIF(@WebParam(name = "request") Request request) throws GestionaException;
}
