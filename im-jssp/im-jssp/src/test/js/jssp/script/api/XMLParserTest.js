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
	var parser = new XMLParser();
	var dom = parser.parseString(xml);

	var isError       = parser.isError();
	var errorMessage  = parser.getErrorMessage();
	var javaException = parser.javaException;

	JsUnit.assertTrue("エラーが発生していること", isError);
	JsUnit.assertNotNull("エラーメッセージが格納されていること", errorMessage);
	JsUnit.assertNotNull("発生したJava例外がjavaExceptionプロパティに格納されていること", javaException);
	JsUnit.assertNotUndefined("printStackTraceメソッドが定義されていること", javaException.printStackTrace);
}


function test_getXmlString_サロゲートペア文字が含まれる場合(){
	var expected, actual, dom;
	var parser = new XMLParser();

	dom = parser.parseString("<root>あい𠀋𡈽𠮟うえお</root>");
	actual = dom.getElementsByTagName("root")[0].getChildNodes()[0].getValue();
	JsUnit.assertEquals("文字化けしないこと", "あい𠀋𡈽𠮟うえお", actual);

	dom = parser.parseString("<root>𠀋</root>");
	actual = dom.getElementsByTagName("root")[0].getChildNodes()[0].getValue();
	JsUnit.assertNotEquals("実体参照で表現されていないこと", "&#131083;", actual);

	dom = parser.parseString("<root>&#131083;</root>");
	actual = dom.getElementsByTagName("root")[0].getChildNodes()[0].getValue();
	JsUnit.assertEquals("実体参照が通常の文字列に変換されていること", "𠀋", actual);
}
