package ar.edu.iua.rest;

public final class Constantes {

	public static final String URL_API = "/api";
	public static final String URL_API_VERSION = "/final";
	public static final String URL_BASE = URL_API + URL_API_VERSION;

	public static final String URL_ORDENES = URL_BASE + "/ordenes";
	public static final String URL_ESTIMULOS = URL_BASE + "/estimulos";
	public static final String URL_ESTIMULOS_SIMPLE_ACCESS = URL_BASE + "/estimulosSimpleAccess";
	public static final String URL_MINISTERIOS = URL_BASE + "/ministerios";
	public static final String URL_ROLES = URL_BASE + "/roles";
	public static final String URL_DOCUMENTOS = URL_BASE + "/documentos";
	public static final String URL_CAMIONES = URL_BASE + "/camiones";
	public static final String URL_CHOFERES = URL_BASE + "/choferes";
	public static final String URL_PRODUCTOS = URL_BASE + "/productos";
	public static final String URL_CLIENTES = URL_BASE + "/clientes";
	public static final String URL_DETALLE_ORDEN = URL_BASE + "/detallesOrdenes";
	public static final String URL_ALARMAS = URL_BASE + "/alarmas";
	public static final String URL_PERMISOS = URL_BASE + "/permisos";
	public static final String URL_NOTIFICACIONES = URL_BASE + "/notificaciones";
	public static final String URL_NOTIFICACIONES_USUARIO = URL_BASE + "/notificacionesUsuario";
	public static final String URL_NOTICIAS = URL_BASE + "/noticias";
	public static final String URL_AUTH_INFO = "/auth-info";
	public static final String URL_LOGOUT = "/logout-token";
	public static final String URL_WEBSOCKET_ENPOINT = URL_BASE + "/ws";
	public static final String TOPIC_SEND_WEBSOCKET_GRAPH = "/iw3/data";

}
