function init(request){
	executeJsUnit();
}

function executeJsUnit(){
	var result = JsUnit.execute(Web.current(), "xsl/jsunit/im_jsunit.xsl");

	var response = Web.getHTTPResponse();
	response.setContentType("text/xml; charset=utf-8");
	response.sendMessageBodyString(result);
}


importClass(java.lang.IllegalArgumentException);
importClass(java.io.FileNotFoundException);
importClass(java.util.zip.ZipException);

importClass(Packages.org.mozilla.javascript.WrappedException);
importClass(Packages.org.intra_mart.jssp.util.config.HomeDirectory);
importClass(Packages.org.intra_mart.jssp.script.api.FileAccessObject);


var logger = Logger.getLogger();
var HOME_DIR = HomeDirectory.instance().getAbsolutePath();

var WORK = HOME_DIR + "/ArchiverTest/";
var WORK_TEMP = WORK + "/temp";

if(!Web){
	var Web = new Object();
	Web.current = function(){
		return "api/ArchiverTest";
	}
}

function oneTimeSetUp(){
	deleteDir(new File(WORK));
	deleteDir(new File(HOME_DIR, "test"));

	var im_jssp_project_root = ".";
	var archiverTestRes = "test/api/ArchiverTest/src";
	var src  = new java.io.File(im_jssp_project_root, "src/test/resources/jssp/script/api/" + archiverTestRes);
	var dest = new java.io.File(HOME_DIR, archiverTestRes);
	dest.mkdirs();
	Packages.org.intra_mart.common.aid.jdk.java.util.ArchiverTest.copyDir(src, dest);

	deleteDir(new File(WORK));
}

function oneTimeTearDown(){
	deleteDir(new File(WORK));
	deleteDir(new File(HOME_DIR, "test"));
}

function setUp(){
	new File(WORK_TEMP).makeDirectories();
}

/**
 * 
 */
function test_zip_srcがnull() {
	var expected;
	var actual;
	
	var args = { src : null };

	try{
		actual = Archiver.zip(args);
		assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}

/**
 * 
 */
function test_zip_srcがUndefined() {
	var expected;
	var actual;
	
	var args = { src : undefined };

	try{
		actual = Archiver.zip(args);
		assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}

/**
 * 
 */
function test_zip_srcプロパティがない() {
	var expected;
	var actual;
	
	var args = { srcs : null };

	try{
		actual = Archiver.zip(args);
		assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}

/**
 * 
 */
function test_zip_srcがString_空文字() {
	var expected;
	var actual;
	
	var args = { src : "" };

	try{
		actual = Archiver.zip(args);
		assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}


/**
 * 
 */
function test_zip_srcがString_空ではないがsrcFinleNameプロパティが存在しない() {
	var expected;
	var actual;
	
	var args = { src : "ダミーのZip形式ファイル内容", srcFileNameZZZ : "ファイル名"};

	try{
		actual = Archiver.zip(args);
		assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}

/**
 * 
 */
function test_zip_srcがFile_存在しないファイル() {
	var expected;
	var actual;
	
	var args = { 
			src : new File("sonzai_shinai/file_deth.txt")
	};

	try{
		actual = Archiver.zip(args);
		assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
		JsUnit.assertTrue("CauseがFileNotFoundExceptionである事", e.javaException.getCause() instanceof FileNotFoundException);
	}
}

/**
 * Antのメッセージ
 * [zip] Warning: skipping zip archive /foo/bar/target/000_empty_dir.zip because no files were included.
 * 
 */
function test_zip_srcがFile_空のディレクトリ_destが未指定() {
	var expected;
	var actual;
	
	var args = { 
			src : new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/empty_dir") 
	};

	try{
		actual = Archiver.zip(args);
		JsUnit.assertEquals("空のディレクトリの場合、返却値がundefinedとなること", undefined, actual);
	}
	catch(e){
		// Debug.console(e);
		JsUnit.assert("例外が発生してはいけません", false);
	}
}

/**
 * 
 */
function test_zip_srcがFile_空のディレクトリ_destがFile() {
	var expected;
	var actual;
	
	var destination = new File(WORK, "target/test/test_zip_srcがFile_空のディレクトリ_destがFile.zip");
	var args = { 
			src : new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/empty_dir"),
			dest : destination
	};

	try{
		actual = Archiver.zip(args);
		
		JsUnit.assertFalse("ZIP圧縮は行われていないこと", actual.exist());
		JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
		JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);
	}
	catch(e){
		// Debug.console(e);
		JsUnit.assert("例外が発生してはいけません", false);
	}
}

/**
 * 
 */
function test_zip_filterの動作() {
	var hoge = 99;
	
	var filterFunc = function(file){
		if(hoge < 100 ){
			// クロージャとして実行されますよ！
			Debug.print("==========\n [test_zip_filterの動作] -> hoge: " + hoge + "\n==========");
			hoge++;
		}
		
		if(file.isDirectory()){
			if(file.directories().length > 0 || file.files().length > 0){
				return true;
			}
			else{
				return false
			}
		}
		
		if( file.path().indexOf('.txt') != -1 ){
		    return true;
		}
		else{
		    return false;
		}
	};
	
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy");
	var destination = new File(WORK, "target/test/test_zip_filterの動作/002_3_hierarchy_without_txt.zip");
	JsUnit.assertFalse("前提：destinationが存在しないこと", destination.exist());
	
	var expected;
	var actual;
	
	var args = { 
			src    : source,
			dest   : destination,
			filter : filterFunc
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, "test_zip_filterの動作のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, "test_zip_filterの動作_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	var target;
	var result;
	var origin;
	
	// ============= ROOT =============
	target = "/";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 2, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 2, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());             // filterされていることの確認
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 3, origin.directories().length); // filterされていることの確認
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, origin.files().length);       // filterされていることの確認

	target = "000_this_file_is_0KB.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "001_plain_text.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "im-jssp-api-list-0.1.2.zip";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());

	target = "oim_banner01.gif";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());
	
	// ============= bar =============
	target = "bar/";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 2, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, origin.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, origin.files().length);	

	target = "bar/000_this_file_is_0KB.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "bar/001_plain_text.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "bar/im-jssp-api-list-0.1.2.zip";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());

	target = "bar/oim_banner01.gif";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());
	
	
	// ============= foo =============
	target = "foo/";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 1, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 2, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 2, origin.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, origin.files().length);	

	target = "foo/000_this_file_is_0KB.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/001_plain_text.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/im-jssp-api-list-0.1.2.zip";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());

	target = "foo/oim_banner01.gif";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());

	// ============= foo/foo2 =============
	target = "foo/foo2/";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 2, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 1, origin.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, origin.files().length);	

	target = "foo/foo2/000_this_file_is_0KB.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/foo2/001_plain_text.txt";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/foo2/im-jssp-api-list-0.1.2.zip";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());

	target = "foo/foo2/oim_banner01.gif";
	result = new File(unzipped.path(), target);
	origin = new File(source.path(),   target);
	JsUnit.assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", result.exist());
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.exist());
}



// Case番号はAPIリスト参照。
/**
 * src が File : dest が File = Application Runtime上で圧縮処理が行われます。 
 */
function test_zip_Case_1() {
	var functionName = "test_zip_Case_1";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy");
	var destination = new File(WORK, "target/test/" + functionName + "/002_3_hierarchy_dest.zip");
	
	JsUnit.assertFalse("前提：destinationが存在しないこと", destination.exist());
	
	var expected;
	var actual;
	
	var args = { 
			src    : source,
			dest   : destination
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(unzipped, source);
}

function test_zip_Case_1_既存ファイルを上書き() {

	var functionName = "test_zip_Case_1_既存ファイルを上書き";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy");
	var destination = new File(WORK, "target/test/" + functionName + "/002_3_hierarchy_dest.zip");
	
	new File(WORK, "target/test/" + functionName).makeDirectories();
	destination.save("abcde");
	JsUnit.assertTrue("前提：destinationが存在すること", destination.exist())
	
	var expected;
	var actual;
	
	var args = { 
			src    : source,
			dest   : destination
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(unzipped, source);
}


/**
 * src が File : dest が 未指定 = Application Runtime上で圧縮処理が行われます。 
 */
function test_zip_Case_3() {
	var functionName = "test_zip_Case_3";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy");
	
	var expected;
	var actual;
	
	var args = { 
			src    : source,
//			dest   : destination ！！！未指定にして返却値をString(Binary)に！！！
	};

	actual = Archiver.zip(args);

	JsUnit.assertTrue("返却値がStringであること", typeof(actual) == "string");

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual);
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(unzipped, source);
}

function test_zip_Case_3_既存ファイルを上書き() {
	var functionName = "test_zip_Case_3_既存ファイルを上書き";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy");
	
	var expected;
	var actual;
	
	var args = { 
			src    : source,
//			dest   : destination ！！！未指定にして返却値をString(Binary)に！！！
	};

	actual = Archiver.zip(args);

	JsUnit.assertTrue("返却値がStringであること", typeof(actual) == "string");

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual);
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(unzipped, source);
}


/**
 * src が String(binary) : dest が File = Application Runtime上で圧縮処理が行われます。 
 */
function test_zip_Case_7() {
	var functionName = "test_zip_Case_7";
	
	var source     = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/im-jssp-api-list-0.1.2.zip").load();
	var sourceFileName = "hoge.zip"; // im-jssp-api-list-0.1.2.zipをhoge.zipというファイル名で圧縮
	var destination = new File(WORK, "target/test/" + functionName + "/im-jssp-api-list-0.1.2をさらにzip.zip");
	JsUnit.assertFalse("前提：destinationが存在しないこと", destination.exist());
	
	var expected;
	var actual;
	
	var args = { 
			src    : source,
			srcFileName : sourceFileName,
			dest   : destination
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );
	
	var result = new File(unzipped.path(), sourceFileName);
	
	JsUnit.assertTrue("圧縮後のファイル名が正しいこと", result.exist());
	JsUnit.assertTrue("圧縮後のファイル名が正しいこと", result.isFile());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事", source, result.load());
}

function test_zip_Case_7_既存ファイルを上書き() {
	var functionName = "test_zip_Case_7_既存ファイルを上書き";
	
	var source     = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/im-jssp-api-list-0.1.2.zip").load();
	var sourceFileName = "hoge.zip"; // im-jssp-api-list-0.1.2.zipをhoge.zipというファイル名で圧縮
	var destination = new File(WORK, "target/test/" + functionName + "/im-jssp-api-list-0.1.2をさらにzip.zip");
	new File(WORK, "target/test/" + functionName).makeDirectories();
	destination.save("abcde");
	JsUnit.assertTrue("前提：destinationが存在すること", destination.exist())
	
	var expected;
	var actual;
	
	var args = { 
			src    : source,
			srcFileName : sourceFileName,
			dest   : destination
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );
	
	var result = new File(unzipped.path(), sourceFileName);
	
	JsUnit.assertTrue("圧縮後のファイル名が正しいこと", result.exist());
	JsUnit.assertTrue("圧縮後のファイル名が正しいこと", result.isFile());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事", source, result.load());	
}


/**
 * src が String(binary) : dest が 未指定 = Application Runtime上で圧縮処理が行われます。
 */
function test_zip_Case_9() {
	var functionName = "test_zip_Case_9";
	
	var source     = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/oim_banner01.gif").load();
	var sourceFileName = "foo.zip"; // oim_banner01.gifをfoo.zipというファイル名で圧縮

	var expected;
	var actual;
	
	var args = { 
			src    : source,
			srcFileName : sourceFileName,
//			dest   : destination ！！！未指定にして返却値をString(Binary)に！！！
	};

	actual = Archiver.zip(args);

	JsUnit.assertTrue("返却値がStringであること", typeof(actual) == "string");

	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual);
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );
	
	var result = new File(unzipped.path(), sourceFileName);
	
	JsUnit.assertTrue("圧縮後のファイル名が正しいこと", result.exist());
	JsUnit.assertTrue("圧縮後のファイル名が正しいこと", result.isFile());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事", source, result.load());
}

function test_zip_Case_9_既存ファイルを上書き() {
	JsUnit.assertTrue("このテストケースは不要です。（∵出力がString(binary)なので、上書きする既存ファイルが存在しない）", true);
}

/**
 * src が Fileの配列 : dest が File = Application Runtime上で圧縮処理が行われます。 
 */
function test_zip_Case_10() {
	var functionName = "test_zip_Case_10";
	var log = Logger.getLogger(Web.current().replace("\/", "\.") + "." + functionName);

	var testResourceRootPath = "test/api/ArchiverTest/src/003_for_FileArray/";
	
	var src_0 = new File(HOME_DIR, testResourceRootPath + "oim_banner01.gif");             // ファイル指定
	var src_1 = new File(HOME_DIR, testResourceRootPath + "bar/001_plain_text.txt");       // ファイル指定（サブディレクトリ）
	var src_2 = new File(HOME_DIR, testResourceRootPath + "bar/000_this_file_is_0KB.txt"); // ファイル指定（filterで弾かれるファイル）
	var src_3 = new File(HOME_DIR, testResourceRootPath + "foo/");                         // ディレクトリ指定
	
	var sources = [src_0, src_1, src_2, src_3];
	
	var destination = new File(WORK, "target/test/" + functionName + "/003_for_FileArray.zip");

	var filterFunc = function(file){
		// ファイル名に「000_」が含まれるファイルは圧縮しない
		if(file.path().indexOf("000_") == -1){
			return true;
		}
		else{
			return false;
		}
	};

	log.trace("destination: " + destination.path());
			
	JsUnit.assertFalse("前提：destinationが存在しないこと", destination.exist());
	
	var expected;
	var actual;
	
	var args = {
			src    : sources,
			dest   : destination,
			filter : filterFunc
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	doCheck_003_for_FileArray_zipの解凍結果が正しいこと(unzipped, sources);
}

function test_zip_Case_10_既存ファイルを上書き() {
	var functionName = "test_zip_Case_10_既存ファイルを上書き";
	var log = Logger.getLogger(Web.current().replace("\/", "\.") + "." + functionName);

	var testResourceRootPath = "test/api/ArchiverTest/src/003_for_FileArray/";
	
	var src_0 = new File(HOME_DIR, testResourceRootPath + "oim_banner01.gif");             // ファイル指定
	var src_1 = new File(HOME_DIR, testResourceRootPath + "bar/001_plain_text.txt");       // ファイル指定（サブディレクトリ）
	var src_2 = new File(HOME_DIR, testResourceRootPath + "bar/000_this_file_is_0KB.txt"); // ファイル指定（filterで弾かれるファイル）
	var src_3 = new File(HOME_DIR, testResourceRootPath + "foo/");                         // ディレクトリ指定
	
	var sources = [src_0, src_1, src_2, src_3];

	var destinationParent = new File(WORK, "target/test/" + functionName);
	var destination = new File(destinationParent.path(), "/003_for_FileArray.zip");

	var filterFunc = function(file){
		// ファイル名に「000_」が含まれるファイルは圧縮しない
		if(file.path().indexOf("000_") == -1){
			return true;
		}
		else{
			return false;
		}
	};

	log.trace("destination: " + destination.path());

	destinationParent.makeDirectories();
	destination.save("abcde");
	JsUnit.assertTrue("前提：destinationが存在すること", destination.exist());
	
	var expected;
	var actual;
	
	var args = {
			src    : sources,
			dest   : destination,
			filter : filterFunc
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	doCheck_003_for_FileArray_zipの解凍結果が正しいこと(unzipped, sources);

}

function test_zip_Case_10_かつJavaのFileFilterをJSで実装した場合() {
	var functionName = "test_zip_Case_10_かつJavaのFileFilterをJSで実装した場合";
	var log = Logger.getLogger(Web.current().replace("\/", "\.") + "." + functionName);

	var testResourceRootPath = "test/api/ArchiverTest/src/003_for_FileArray/";
	
	var src_0 = new File(HOME_DIR, testResourceRootPath + "oim_banner01.gif");             // ファイル指定
	var src_1 = new File(HOME_DIR, testResourceRootPath + "bar/001_plain_text.txt");       // ファイル指定（サブディレクトリ）
	var src_2 = new File(HOME_DIR, testResourceRootPath + "bar/000_this_file_is_0KB.txt"); // ファイル指定（filterで弾かれるファイル）
	var src_3 = new File(HOME_DIR, testResourceRootPath + "foo/");                         // ディレクトリ指定
	
	var sources = [src_0, src_1, src_2, src_3];
	
	var destination = new File(WORK, "target/test/" + functionName + "/003_for_FileArray.zip");

	// JavaのFileFilterをJSで実装した場合
	var impl = {
			accept : function(pathname) {
				java.lang.System.out.println("デバッグ中！: " + pathname);
				// pathnameの型は「java.io.File」です。
				// ファイル名に「000_」が含まれるファイルは圧縮しない
				if(pathname.getName().indexOf("000_") == -1){
					return true;
				}
				else{
					return false;
				}
			}
		};
	var filterFunc = new java.io.FileFilter(impl);

	//-----------------------------------------------
	// なお、以下の形式は、動作しませんでした。。。
	//-----------------------------------------------
	//	var filterFunc = new java.io.FileFilter(
	//			function(pathname) {
	//				java.lang.System.out.println("デバッグ中！: " + pathname);
	//				// pathnameの型は「java.io.File」です。
	//				// ファイル名に「000_」が含まれるファイルは圧縮しない
	//				if(pathname.getName().indexOf("000_") == -1){
	//					return true;
	//				}
	//				else{
	//					return false;
	//				}
	//			});
	//-----------------------------------------------

	log.trace("destination: " + destination.path());
			
	JsUnit.assertFalse("前提：destinationが存在しないこと", destination.exist());
	
	var expected;
	var actual;
	
	var args = {
			src    : sources,
			dest   : destination,
			filter : filterFunc
	};

	actual = Archiver.zip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual.load());
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy,
							dest : destResultCheck
						}
				 );

	doCheck_003_for_FileArray_zipの解凍結果が正しいこと(unzipped, sources);
}


/**
 * src が Fileの配列 : dest が 未指定 = Application Runtime上で圧縮処理が行われます。
 */
function test_zip_Case_12() {
	var functionName = "test_zip_Case_12";
	var log = Logger.getLogger(Web.current().replace("\/", "\.") + "." + functionName);

	var testResourceRootPath = "test/api/ArchiverTest/src/003_for_FileArray/";
	
	var src_0 = new File(HOME_DIR, testResourceRootPath + "oim_banner01.gif");             // ファイル指定
	var src_1 = new File(HOME_DIR, testResourceRootPath + "bar/001_plain_text.txt");       // ファイル指定（サブディレクトリ）
	var src_2 = new File(HOME_DIR, testResourceRootPath + "bar/000_this_file_is_0KB.txt"); // ファイル指定（filterで弾かれるファイル）
	var src_3 = new File(HOME_DIR, testResourceRootPath + "foo/");                         // ディレクトリ指定
	
	var sources = [src_0, src_1, src_2, src_3];
	
	var filterFunc = function(file){
		// ファイル名に「000_」が含まれるファイルは圧縮しない
		if(file.path().indexOf("000_") == -1){
			return true;
		}
		else{
			return false;
		}
	};

	var expected;
	var actual;
	
	var args = {
			src    : sources,
//			dest   : destination, //  ！！！未指定にして返却値をString(Binary)に！！！
			filter : filterFunc
	};

	actual = Archiver.zip(args);

	JsUnit.assertTrue("返却値がStringであること", typeof(actual) == "string");
	
	// unzipが正しく動作していることが前提
	var actualCopy = new File(WORK_TEMP, functionName + "のコピー.zip");
	actualCopy.save(actual);
	var destResultCheck = new File(WORK_TEMP, functionName + "_解凍先");
	
	var unzipped = Archiver.unzip(
						{
							src  : actualCopy.load(),
							dest : destResultCheck
						}
				 );

	doCheck_003_for_FileArray_zipの解凍結果が正しいこと(unzipped, sources);
}

function test_zip_Case_12_既存ファイルを上書き() {
	JsUnit.assertTrue("このテストケースは不要です。（∵出力がString(binary)なので、上書きする既存ファイルが存在しない）", true);
}


/**
 * 
 */
function test_unzip_srcがnull() {
	var expected;
	var actual;
	
	var args = { src : null };

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}


/**
 * 
 */
function test_unzip_srcがUndefined() {
	var expected;
	var actual;
	
	var args = { src : undefined };

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}


/**
 * 
 */
function test_unzip_srcプロパティがない() {
	var expected;
	var actual;
	
	var args = { no_src : null };

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}


/**
 * 
 */
function test_unzip_srcがString_空文字() {
	var expected;
	var actual;
	
	var args = { src : "" };

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}

/**
 * 
 */
function test_unzip_destがnull() {
	var expected;
	var actual;
	
	var args = { 
			src  : new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy.zip"),
			dest : null
	};

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}


/**
 * 
 */
function test_unzip_destがUndefined() {
	var expected;
	var actual;
	
	var args = { 
			src  : new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy.zip"),
			dest : undefined
	};

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}


/**
 * 
 */
function test_unzip_destプロパティがない() {
	var expected;
	var actual;
	
	var args = { 
			src  : new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy.zip"),
			no_dest : null
	};

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
	}
}

/**
 * 
 */
function test_unzip_srcがFile_存在しないファイル() {
	var expected;
	var actual;
	
	var args = {
			src  : new File("sonzai_shinai/file_deth.zip"),
			dest : new File(WORK, "dest")
	};

	try{
		actual = Archiver.unzip(args);
		JsUnit.assert("例外が発生する必要があります", false);
	}
	catch(e){
		JsUnit.assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.javaException instanceof IllegalArgumentException);
		JsUnit.assertTrue("CauseがFileNotFoundExceptionである事", e.javaException.getCause() instanceof FileNotFoundException);
	}
}	

/**
 * 
 */
function test_unzip_srcがZip形式ではない() {
	var expected;
	var actual;
	
	var destination = new File(WORK, "target/test_unzip_srcがZip形式ではない");
	destination.remove();
	JsUnit.assertFalse("前提：出力先ディレクトリが存在しないこと", destination.exist());
	
	var args = {
			src  : "zip_keishiki_deha_arimasen",
			dest : destination
	};
	
	try{
		actual = Archiver.unzip(args);
	}
	catch(e){
		Debug.console(e);
		// 内部でZipExceptionが発生しますが、無視します。
		JsUnit.assertTrue("例外が発生してはいけません", false);
	}

	JsUnit.assertEquals("Zip形式ではないモノをunzipした場合は、結果がundefinedで返却されること", undefined, actual);
	
	// unzipに失敗しても、出力先は生成されます。（制限とする）
	JsUnit.assertTrue("出力先ディレクトリが存在する事", destination.exist());
	JsUnit.assertTrue("出力先がディレクトリである事", destination.isDirectory());
	JsUnit.assertEquals("出力先ディレクトリが空である事", 0, destination.files().length);

	destination.remove();
}


//Case番号はAPIリスト参照。
/**
 * src が File : dest が File = Application Runtime上で解凍処理が行われます。 
 */
function test_unzip_Case_1() {
	var functionName = "test_unzip_Case_1";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy.zip");
	var destination = new File(WORK, "target/test/" + functionName + "/002_3_hierarchy_dest");
	
	JsUnit.assertFalse("前提：destinationが存在しないこと", destination.exist());
	
	var expected = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/"); //解凍済みのディレクトリ
	var actual;
	
	var args = { 
			src    : source,
			dest   : destination
	};
	actual = Archiver.unzip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(expected, actual);
}

function test_unzip_Case_1_既存ディレクトリに出力() {
	var functionName = "test_unzip_Case_1_既存ディレクトリに出力";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy.zip");
	var destination = new File(WORK, "target/test/" + functionName + "/002_3_hierarchy_dest");
	
	destination.makeDirectories();
	var aboriginal = new File(destination.path() + "/" + "hoge.txt");
	aboriginal.save("abcde");
	JsUnit.assertTrue("前提：destinationが存在すること", destination.exist());
	
	var expected = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/"); //解凍済みのディレクトリ
	var actual;
	
	var args = { 
			src    : source,
			dest   : destination
	};
	actual = Archiver.unzip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);
	JsUnit.assertFalse("既存のファイルが削除されていること", aboriginal.exist());

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(expected, actual);
}


/**
 * src が String(binary) : dest が File = Application Runtime上で解凍処理が行われます。 
 */
function test_unzip_Case_5() {
	var functionName = "test_unzip_Case_5";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy.zip").load();
	var destination = new File(WORK, "target/test/" + functionName + "/002_3_hierarchy_dest");
	
	JsUnit.assertFalse("前提：destinationが存在しないこと", destination.exist());
	
	var expected = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/"); //解凍済みのディレクトリ
	var actual;
	
	var args = { 
			src    : source,
			dest   : destination
	};
	actual = Archiver.unzip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(expected, actual);
}

function test_unzip_Case_5_既存ディレクトリに出力() {
	var functionName = "test_unzip_Case_5";
	
	var source      = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy.zip").load();
	var destination = new File(WORK, "target/test/" + functionName + "/002_3_hierarchy_dest");
	
	destination.makeDirectories();
	var aboriginal = new File(destination.path() + "/" + "hoge.txt");
	aboriginal.save("abcde");
	JsUnit.assertTrue("前提：destinationが存在すること", destination.exist());
	
	var expected = new File(HOME_DIR, "test/api/ArchiverTest/src/002_3_hierarchy/"); //解凍済みのディレクトリ
	var actual;
	
	var args = { 
			src    : source,
			dest   : destination
	};
	actual = Archiver.unzip(args);

	JsUnit.assertEquals("返却値がFileであること", "File", getTypeName(actual));
	JsUnit.assertEquals("ZIP圧縮後のファイルパスが正しい事", destination.path(), actual.path());
	JsUnit.assertFalse("返却値が引数オブジェクトのdestと同じインスタンスではない事", destination === actual);
	JsUnit.assertFalse("既存のファイルが削除されていること", aboriginal.exist());
	

	doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(expected, actual);
}

function doCheck_003_for_FileArray_zipの解凍結果が正しいこと(destination, sources){
	var targetDir;
	var target;
	var result;
	var origin;
	
	var src_0 = sources[0];
	var src_1 = sources[1];
	var src_2 = sources[2]; // 利用されません（∵filterで弾かれるファイルのため）
	var src_3 = sources[3];
	
	target = "/";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 2, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",   5, targetDir.files().length);	

	target = "oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target).load();
	origin = src_0.load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	target = "001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = src_1.load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
	// ↑サブディレクトリ「bar」の下に作られないことに注意

	target = "copy_of_001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
	
	target = "copy_of_im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
	
	target = "copy_of_oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
	
	
	// ============ empty_dir ============ ← 「foo/empty_dir/」ではなく「empty_dir/」として圧縮されます。
	target = "empty_dir";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 0, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",   0, targetDir.files().length);	
	
	
	// ============= foo2 =============　← 「foo/foo2/」ではなく「foo2/」として圧縮されます。
	target = "foo2";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 1, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",   3, targetDir.files().length);	
	
	target = "foo2/001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	target = "foo2/im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	target = "foo2/oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	// ============ foo2/empty_dir ============ ← 「foo/foo2/empty_dir/」ではなく「foo2/empty_dir/」として圧縮されます。
	target = "foo2/empty_dir";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 0, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",    0, targetDir.files().length);		

}

function doCheck_002_3_hierarchy_zipの解凍結果が正しいこと(destination, source){
	var target;
	var result;
	var origin;
	
	// ============ ROOT ============
	target = "/";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 3, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.directories().length, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.files().length,       result.files().length);
	
	target = "000_this_file_is_0KB.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.load() == origin.load());

	target = "oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());
	
	// ============= bar =============
	target = "bar/";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.directories().length, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.files().length,       result.files().length);

	target = "bar/000_this_file_is_0KB.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "bar/001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "bar/im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "bar/oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());
	
	
	// ============= empty_dir =============
	target = "empty_dir/";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.directories().length, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.files().length,       result.files().length);

	
	// ============= foo =============
	target = "foo/";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 2, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.directories().length, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.files().length,       result.files().length);

	target = "foo/000_this_file_is_0KB.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	// ============= foo/empty_dir =============
	target = "foo/empty_dir/";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.directories().length, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.files().length,       result.files().length);

	
	// ============= foo/foo2 =============
	target = "foo/foo2/";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 1, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 4, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.directories().length, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.files().length,       result.files().length);
	
	target = "foo/foo2/000_this_file_is_0KB.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/foo2/001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/foo2/im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	target = "foo/foo2/oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result.load(), origin.load());

	// ============= foo/foo2/empty_dir =============
	target = "foo/foo2/empty_dir/";
	result = newFileOrVirtualFile(destination, target);
	origin = newFileOrVirtualFile(source,      target);
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", result.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, result.files().length);	
	JsUnit.assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", origin.isDirectory());
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.directories().length, result.directories().length);
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", origin.files().length,       result.files().length);
}


function doCheck_Filterなしの003_for_FileArray_zipの解凍結果が正しいこと(destination, sources){
	var targetDir;
	var target;
	var result;
	var origin;
	
	var src_0 = sources[0];
	var src_1 = sources[1];
	var src_2 = sources[2]; // 利用されます！（∵filterは無効なため）
	var src_3 = sources[3];
	
	target = "/";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 2, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",   7, targetDir.files().length);	

	target = "oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target).load();
	origin = src_0.load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	target = "000_this_file_is_0KB.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = src_2.load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
	// ↑サブディレクトリ「bar」の下に作られないことに注意
	// ↑フィルター対象にならないこと

	target = "001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = src_1.load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
	// ↑サブディレクトリ「bar」の下に作られないことに注意

	target = "copy_of_000_this_file_is_0KB.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
	// ↑フィルター対象にならないこと

	target = "copy_of_001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
	
	target = "copy_of_im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
	
	target = "copy_of_oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
	
	
	// ============ empty_dir ============ ← 「foo/empty_dir/」ではなく「empty_dir/」として圧縮されます。
	target = "empty_dir";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 0, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",   0, targetDir.files().length);	
	
	
	// ============= foo2 =============　← 「foo/foo2/」ではなく「foo2/」として圧縮されます。
	target = "foo2";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 1, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",   4, targetDir.files().length);	
	
	target = "foo2/000_this_file_is_0KB.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
	// ↑フィルター対象にならないこと

	target = "foo2/001_plain_text.txt";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	target = "foo2/im-jssp-api-list-0.1.2.zip";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	target = "foo2/oim_banner01.gif";
	result = newFileOrVirtualFile(destination, target).load();
	origin = newFileOrVirtualFile(src_3, target).load();
	JsUnit.assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

	// ============ foo2/empty_dir ============ ← 「foo/foo2/empty_dir/」ではなく「foo2/empty_dir/」として圧縮されます。
	target = "foo2/empty_dir";
	targetDir = newFileOrVirtualFile(destination, target);
	JsUnit.assertTrue("対象がディレクトリであること(" + target + ")", targetDir.isDirectory());
	JsUnit.assertEquals("対象がディレクトリ配下のディレクトリ数が正しいこと(" + target + ")", 0, targetDir.directories().length);
	JsUnit.assertEquals("対象がディレクトリ配下のファイル数が正しいこと(" + target + ")",    0, targetDir.files().length);		

}

/**
 * 再帰してディレクトリを削除！ 
 */
function deleteDir(targetDir, indent) {
	indent = (indent == undefined) ? "" : indent;
	
	logger.trace(indent + "Delete Dir Enter: '{}'", targetDir.path());

	if(!targetDir.exist()){
		return;
	}
	
	var dirs = targetDir.directories();
	var files = targetDir.files();
	logger.trace(indent + "ディレクトリ数：" + dirs.length + ", " + "ファイル数：" + files.length);

	for(var idx = 0; idx < files.length; idx++){
		var child = ( getTypeName(targetDir) == "File" ) 
					? new File(targetDir.path() + "/" + files[idx]) : new VirtualFile(targetDir.path() + "/" + files[idx]);

		var result = child.remove();
		if(!result){
			throw new Error("Deletion failure: " + child.path());
		}

		logger.trace(indent + "[" + idx + "] Delete File: '{}'", child.path());
	}

	for(var idx = 0; idx < dirs.length; idx++){
		var child = ( getTypeName(targetDir) == "File" ) 
					? new File(targetDir.path() + "/" + dirs[idx]) : new VirtualFile(targetDir.path() + "/" + dirs[idx]);

		deleteDir(child , indent + "    "); // 再帰！
	}
	
	targetDir.remove();
	logger.trace(indent + "Delete Dir: '{}'", targetDir.path());
}

function getTypeName(obj){
	var constSource = obj.constructor.toSource();
	
	if(constSource.indexOf("VirtualFile") != -1){
		return "VirtualFile";
	}
	if(constSource.indexOf("File") != -1){
		return "File";
	}
	else{
		return "unknown";
	}
}

function newFileOrVirtualFile(instance, child){
	var typeName = getTypeName(instance);
	
	switch(typeName){
	case "File":
		return new File(instance.path(), child);
		break;
	case "VirtualFile":
		return new VirtualFile(instance.path() + "/" + child);
		break;
	default:
		throw new Error("unknown");
	}
}