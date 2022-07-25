function init(request){

}

function executeTest(){
	Debug.print("テストを開始します: " + Web.current());
	var result = JsUnit.execute("sample/script/jsunit/AllTests", "xsl/jsunit/im_jsunit.xsl");

	var response = Web.getHTTPResponse();
	response.setContentType("text/xml; charset=utf-8");
	response.sendMessageBodyString(result);
}

function defineTestSuite() {
	var suite = new JsTestSuite("JsUnitを利用したテストのサンプル");

	suite.addTest("その１", "sample/script/jsunit/test_case_001");
	return suite;
}