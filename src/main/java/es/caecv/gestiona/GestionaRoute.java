package es.caecv.gestiona;

import org.apache.camel.Exchange;
import org.apache.camel.FailedToCreateRouteException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.cxf.binding.soap.SoapFault;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class GestionaRoute extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(GestionaRoute.class);

	private static final ObjectFactory objectFactory = new ObjectFactory();

	@Override
	public void configure() {

		onException(Exception.class).handled(true).process(new Processor() {
			@Override
			public void process(Exchange exchange) {
				Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				logger.error("Captured exception:", exception);
				GestionaFault fault;
				if (exception instanceof GestionaException) {
					fault = ((GestionaException) exception).getFault();
				} else if (exception instanceof IllegalArgumentException || exception instanceof FailedToCreateRouteException) {
					fault = new GestionaFault(GestionaErrorMessage.UNREACHABLE_DATABASE);
				} else if (exception instanceof SQLSyntaxErrorException || exception instanceof PSQLException) {
					fault = new GestionaFault(GestionaErrorMessage.DATABASE_ERROR);
				} else {
					fault = new GestionaFault(GestionaErrorMessage.UNDEFINED_ERROR);
				}
				exchange.getOut().setFault(true);
				exchange.getOut().setBody(new SoapFault(fault.getDescription(), new QName(fault.getCode())));
			}
		}).end();

		from("cxf:bean:gestionaService").log(LoggingLevel.INFO, "Received exchange:\n${body}").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws GestionaException {
				Request request = exchange.getIn().getBody(Request.class);
				String nif = request != null ? request.getNif() : null;
				if (nif == null || "".equals(nif.trim())) {
					throw new GestionaException(GestionaErrorMessage.INPUT_REQUIRED, null);
				}
				if (!GestionaUtils.validateInput(nif)) {
					throw new GestionaException(GestionaErrorMessage.INPUT_INVALID, null);
				}
				exchange.getOut().setBody(String.format(GestionaConstants.SELECT_CONSULTA_OPERADOR_CIF, nif));
			}
		}).log(LoggingLevel.INFO, "Execute query:\n${body}").to("jdbc:gestionaDS").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws GestionaException {
				exchange.getOut().setBody(getResponse((ArrayList<HashMap<String, Object>>) exchange.getIn().getBody(ArrayList.class)));
			}
		});
	}

	private Response getResponse(ArrayList<HashMap<String, Object>> list) {
		Response response = objectFactory.createResponse();
		if (list != null) {
			for (HashMap<String, Object> map : list) {
				response.getInformeControl().add(getInformeControl(map));
			}
		}
		return response;
	}

	private InformeControl getInformeControl(HashMap<String, Object> map) {
		InformeControl informeControl = objectFactory.createInformeControl();
		informeControl.setTitular(getTitular(map));
		informeControl.setRecinto(getRecinto(map));
		informeControl.setProcesoControl(getProcesoControl(map));
		return informeControl;
	}

	private Titular getTitular(HashMap<String, Object> map) {
		Titular titular = objectFactory.createTitular();
		if (map != null) {
			titular.setOpRazonSocial(getValueString(map.get("op_razon_social")));
			titular.setOpNif(getValueString(map.get("op_nif")));
			titular.setOpFechaNotificacion(getValueDate(map.get("op_fecha_notificacion")));
			titular.setOpFechaCertificacion(getValueDate(map.get("op_fecha_certificacion")));
		}
		return titular;
	}

	private Recinto getRecinto(HashMap<String, Object> map) {
		Recinto recinto = objectFactory.createRecinto();
		if (map != null) {
			recinto.setCodProvincia(getValueString(map.get("cod_provincia")));
			recinto.setCodMuncipio(getValueString(map.get("cod_municipio")));
			recinto.setPoligono(getValueString(map.get("poligono")));
			recinto.setParcela(getValueString(map.get("parcela")));
			recinto.setRecinto(getValueString(map.get("recinto")));
			recinto.setSubrecinto(getValueString(map.get("subrecinto")));
			recinto.setSuperficieSigpacHa(getValueString(map.get("superficie_sigpac_ha")));
			recinto.setSuperficiecertificadaha(getValueString(map.get("superficiecertificadaha")));
			recinto.setCodCapa(getValueString(map.get("cod_capa")));
			recinto.setDoblado(getValueString(map.get("doblado")));
			recinto.setRegadio(getValueString(map.get("regadio")));
			recinto.setIniciopraceco(getValueDate(map.get("iniciopraceco")));
			recinto.setFechabaja(getValueDate(map.get("fechabaja")));
			recinto.setEstadoparcela(getValueString(map.get("estadoparcela")));
			recinto.setDenparcela(getValueString(map.get("denparcela")));
			recinto.setCampana(getValueString(map.get("campana")));
		}
		return recinto;
	}

	private ProcesoControl getProcesoControl(HashMap<String, Object> map) {
		ProcesoControl procesoControl = objectFactory.createProcesoControl();
		if (map != null) {
			procesoControl.setUltVisita(getValueString(map.get("ult_visita")));
			procesoControl.setEstadoControl(getValueString(map.get("estado_control")));
		}
		return procesoControl;
	}

	private String getValueString(Object value) {
		return value instanceof String ? (String) value : String.valueOf(value);
	}

	private XMLGregorianCalendar getValueDate(Object value) {
		return value instanceof Timestamp ? getXMLCalendar(getCalendar((Timestamp) value)) : null;
	}

	private GregorianCalendar getCalendar(Timestamp timestamp) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(timestamp);
		return calendar;
	}

	private XMLGregorianCalendar getXMLCalendar(GregorianCalendar calendar) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			logger.error("", e);
			return null;
		}
	}
}
