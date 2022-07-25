var string4oneByte     = "abcABC123`*?";
var string4twoByte     = "あいうアイウ亜意卯";
var string4jikeiHenkou = "逢餅祁";
var string4vistaTsuika = "瘦俱剝吞";
var string4surrogate   = "𠀋𡈽𠮟";

function init(request){
	executeJsUnit();
}

function executeJsUnit(){
	var result = JsUnit.execute(Web.current(), "xsl/jsunit/im_jsunit.xsl");

	var response = Web.getHTTPResponse();
	response.setContentType("text/xml; charset=utf-8");
	response.sendMessageBodyString(result);
}

var logger = Logger.getLogger();

function test_encode_decode_サロゲートペア文字_と_そのほかの文字の複合パターン(){
	var complexSurrogate = string4surrogate + string4oneByte + string4twoByte + string4jikeiHenkou + string4vistaTsuika + string4surrogate;	
	
	var actualEncoded = URL.encode(complexSurrogate, "UTF-8");
	var actualEncodedByJava = Packages.java.net.URLEncoder.encode(complexSurrogate, "UTF-8") + "";  // 文字連結しないとStringにならない

	var actualDecoded = URL.decode(actualEncoded, "UTF-8");
	var actualDecodedByJava = Packages.java.net.URLDecoder.decode(actualEncodedByJava, "UTF-8") + "";
	
	
	logger.trace("complexSurrogate: {}", complexSurrogate);
	
	logger.trace("actualEncoded: {}",       actualEncoded);
	logger.trace("actualEncodedByJava: {}", actualEncodedByJava);
	
	logger.trace("actualDecoded: {}",       actualDecoded);
	logger.trace("actualDecodedByJava: {}", actualDecodedByJava);

	
	JsUnit.assertEquals("エンコード→デコードを行うと元の文字に戻ること", complexSurrogate, actualDecoded);
	JsUnit.assertEquals("java.net.URLEncoder.encode()と同じ値になること", actualEncodedByJava.toLowerCase(), actualEncoded.toLowerCase());
	JsUnit.assertEquals("java.net.URLDecoder.decode()と同じ値になること", actualDecodedByJava.toLowerCase(), actualDecoded.toLowerCase());
	
}
