/**
 * Webオブジェクトの実行サンプルです
 */
function init(request){

	var debugObject = new Object();

	debugObject.current = Web.current();
	debugObject.referer = Web.referer();

	debugObject.getenv = new Object();
	debugObject.getenv.HTTP_REFERER    = Web.getenv("HTTP_REFERER");
	debugObject.getenv.HTTP_USER_AGENT = Web.getenv("HTTP_USER_AGENT");
	debugObject.getenv.AUTH_TYPE       = Web.getenv("AUTH_TYPE");
	debugObject.getenv.CONTENT_TYPE    = Web.getenv("CONTENT_TYPE");
	debugObject.getenv.REQUEST_METHOD  = Web.getenv("REQUEST_METHOD");
	debugObject.getenv.PATH_INFO       = Web.getenv("PATH_INFO");
	debugObject.getenv.PATH_TRANSLATED = Web.getenv("PATH_TRANSLATED");
	debugObject.getenv.QUERY_STRING    = Web.getenv("QUERY_STRING");
	debugObject.getenv.REMOTE_USER     = Web.getenv("REMOTE_USER");
	debugObject.getenv.SCRIPT_NAME     = Web.getenv("SCRIPT_NAME");
	debugObject.getenv.CONTENT_LENGTH  = Web.getenv("CONTENT_LENGTH");
	debugObject.getenv.SERVER_PROTOCOL = Web.getenv("SERVER_PROTOCOL");
	debugObject.getenv.SERVER_NAME     = Web.getenv("SERVER_NAME");
	debugObject.getenv.SERVER_PORT     = Web.getenv("SERVER_PORT");
	debugObject.getenv.REMOTE_ADDR     = Web.getenv("REMOTE_ADDR");
	debugObject.getenv.REMOTE_HOST     = Web.getenv("REMOTE_HOST");

	debugObject.host = Web.host();
	debugObject.port = Web.port();
	debugObject.protocol = Web.protocol();
	debugObject.script = Web.script();
	debugObject.base = Web.base();
	debugObject.location = Web.location();

	//	Web.getHTTPResponse();
	//	Web.setHTTPResponseHeader(String, String);

	debugObject.encodeURL = Web.encodeURL("sample/url");
	debugObject.encodeRedirectURL = Web.encodeRedirectURL("sample/url");
	debugObject.getContextPath = Web.getContextPath();
	debugObject.getRemoteAddr = Web.getRemoteAddr();
	debugObject.getRemoteHost = Web.getRemoteHost();
	debugObject.getProtocol = Web.getProtocol();
	debugObject.getScheme = Web.getScheme();
	debugObject.getServerName = Web.getServerName();
	debugObject.getServerPort = Web.getServerPort();
	debugObject.isSecure = Web.isSecure();

	Debug.browse("Webオブジェクトの実行サンプルです", debugObject);
}