function init(request){
	executeJsUnit();
}

function executeJsUnit(){
	var result = JsUnit.execute(Web.current(), "xsl/jsunit/im_jsunit.xsl");

	var response = Web.getHTTPResponse();
	response.setContentType("text/xml; charset=utf-8");
	response.sendMessageBodyString(result);
}

function test_コンストラクタ_XML解析エラー(){
	var xml = "<A>閉じタグ不正/A>";
	var dom = new XMLDocument(xml);

	var isError       = dom.isError();
	var errorMessage  = dom.getErrorMessage();
	var javaException = dom.javaException;

	Logger.getLogger().trace("isError: {}", isError);
	Logger.getLogger().trace("errorMessage: {}", errorMessage);
	Logger.getLogger().trace("javaException ->", javaException);

	JsUnit.assertTrue("エラーが発生していること", isError);
	JsUnit.assertNotNull("エラーメッセージが格納されていること", errorMessage);
	JsUnit.assertNotNull("発生したJava例外がjavaExceptionプロパティに格納されていること", javaException);
	JsUnit.assertNotUndefined("printStackTraceメソッドが定義されていること", javaException.printStackTrace);
}


function test_getXmlString_サロゲートペア文字が含まれる場合(){
	var expected, dom;

	expected = "<root>あい𠀋𡈽𠮟うえお</root>";
	dom = new XMLDocument(expected);
	JsUnit.assertEquals("文字化けしないこと", expected, dom.getXmlString());

	dom = new XMLDocument("<root>𠀋</root>");
	JsUnit.assertNotEquals("実体参照で表現されていないこと", "<root>&#131083;</root>", dom.getXmlString());

	dom = new XMLDocument("<root>&#131083;</root>");
	JsUnit.assertEquals("実体参照が通常の文字列に変換されていること", "<root>𠀋</root>", dom.getXmlString());
}
