package es.caecv.gestiona;

public class GestionaConstants {

	public static final String SELECT_CONSULTA_OPERADOR_CIF = "select o.op_razon_social, o.op_nif, o.op_fecha_notificacion, o.op_fecha_certificacion, t.cod_provincia, t.cod_municipio, t.poligono, t.parcela, t.recinto, t.subrecinto, t.superficie_sigpac_ha, t.superficiecertificadaha, ct.cod_capa, t.doblado, ct.regadio, t.iniciopraceco, t.fechabaja, t.estadoparcela, t.denparcela, ct.campana, t.ult_visita, t.estado_control from operadores as o inner join terrenos as t on o.op_nregistro = t.refoperador inner join cultivos_terrenos ct on t.codigoterreno = ct.ref_terrenos where o.op_nif = '%s'";
}
