var resultDirName  = "target/JsspUnitTestResult/";
var resultFileName = "result_for_jsspUnitTestExecute.xml";

var logger = Logger.getLogger();

function init(request){
	var testSrc = "jsspUnitTestExecute";
	
	logger.info("テスト開始: {}", testSrc);
	var testResult = JsUnit.execute(testSrc, "xsl/jsunit/im_jsunit.xsl");

	try{
		var response = Web.getHTTPResponse();
		response.setContentType("text/xml; charset=utf-8");
		response.sendMessageBodyString(testResult);
	}
	catch(ex){
		logger.trace("WebコンテナからJSSPを起動していない(=JVMだけで起動している)", ex);
		
		copyDir(new java.io.File("src/test/webapp/csjs"),   new java.io.File(resultDirName + "/csjs"));
		copyDir(new java.io.File("src/test/webapp/images"), new java.io.File(resultDirName + "/images"));
		copyDir(new java.io.File("src/test/webapp/xsl"),    new java.io.File(resultDirName + "/xsl"));
		
		var dir = new File(resultDirName);
		dir.makeDirectories();
		
		var file = new File(dir.path(), resultFileName);
		var result = file.write(testResult);
		
		if(result){
			logger.info("テスト結果を出力しました: {}", file.path());
		}
		else{
			logger.info("テスト結果の出力に失敗しました: {}", file.path());
		}
		
	}
}

function defineTestSuite() {
	var suite = new JsTestSuite("All Test");
	suite.addTest("サーバサイドのImJSONテスト",      "jssp/script/api/im_json_test");
	suite.addTest("URLオブジェクトのテスト",         "jssp/script/api/URLTest");
	suite.addTest("XMLDocumentオブジェクトのテスト", "jssp/script/api/XMLDocumentTest");
	suite.addTest("XMLParserオブジェクトのテスト",   "jssp/script/api/XMLParserTest");
	suite.addTest("Archiverオブジェクトのテスト",   "jssp/script/api/ArchiverTest");
	return suite;
}


function copyDir(/*<java.io.File>*/ srcDir, /*<java.io.File>*/ destDir) {
	
	if(srcDir.exists() == false || srcDir.isDirectory() == false){
		throw new Error("IllegalArgument");
	}
	
	if(destDir.exists()){
		deleteDir(destDir);
	}
	
	destDir.mkdirs();
	
	var srcDirLists = srcDir.list();
	for(var idx = 0, max=srcDirLists.length; idx < max; idx++){
		var name = srcDirLists[idx];
		
		var child4src  = new java.io.File( srcDir, name);
		var child4dest = new java.io.File(destDir, name);
		
		if(child4src.isDirectory()){
			if(child4src.getName().equals(".svn")){
				continue;
			}
			
			child4dest.mkdirs();
			copyDir(child4src, child4dest); // 再帰！
		}
		else{
			copyFile(child4src, child4dest);
		}
	}
	
}

function copyFile(/*<java.io.File>*/ srcFile, /*<java.io.File>*/  destFile) {
	
	var sourceChannel      = new java.io.FileInputStream (srcFile) .getChannel();
	var destinationChannel = new java.io.FileOutputStream(destFile).getChannel();
	try{
		sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
	}
	finally{
		sourceChannel.close();
		destinationChannel.close();
	}
}

function deleteDir(/*<java.io.File>*/ targetDir) {
	if(targetDir.list() != null){
		
		var targetDirLists = targetDir.list();
		for(var idx = 0, max=targetDirLists.length; idx < max; idx++){
			var name = targetDirLists[idx];
			var child = new java.io.File(targetDir, name);
			
			if(child.isDirectory()){
				deleteDir(child); // 再帰！
			}
			else{
				var result = doDelete(child);
				if(!result){
					throw new Error("Deletion failure: " + child.getAbsolutePath());
				}
			}
		}
	}
	
	doDelete(targetDir);
}

function doDelete(/*<java.io.File>*/ file){
	var target = new File(file);
	var result = target.remove();
	return result;
}