var logger = Logger.getLogger();
var testTargetPagePath = "sample/script/jsunit/test_target";

function oneTimeSetUp(){
	logger.debug("********** oneTimeSetUp() が実行されました **********");

	// テスト対象のソースをロード
	load(testTargetPagePath);
}

function oneTimeTearDown(){
	logger.debug("========== oneTimeTearDown() が実行されました ==========");
}

function setUp(){
	logger.debug("----- setUp() が実行されました -----");
}

function tearDown(){
	logger.debug("::::: tearDown() が実行されました :::::");
}


// 以下が、テストケースです。
/**
 * 
 */
function test_getTrue(){
	logger.debug("test_getTrue() が実行されました");

	var actual = getTrue();
	JsUnit.assert("評価値がTrueであること", actual);
}

/**
 * 
 */
function test_getSampleObject_同じことを確認する場合(){
	logger.debug("test_getSampleObject_同じことを確認する場合() が実行されました");
	
	var actual = getSampleObject();
	var expected = {
			prop1 : "string",
			prop2 : 123,
			prop3 : false,
			prop4 : new Date(),
			prop5 : [1, 2, 3]
		};
	JsUnit.assertEquals("評価値と期待値が同じであること(String)",  expected.prop1,    actual.prop1);
	JsUnit.assertEquals("評価値と期待値が同じであること(Number)",  expected.prop2,    actual.prop2);
	JsUnit.assertEquals("評価値と期待値が同じであること(Boolean)", expected.prop3,    actual.prop3);
	JsUnit.assertEquals("評価値と期待値が同じであること(Date)",    expected.prop4,    actual.prop4);
	JsUnit.assertEquals("評価値と期待値が同じであること(配列)",    expected.prop5[0], actual.prop5[0]);
	JsUnit.assertEquals("評価値と期待値が同じであること(配列)",    expected.prop5[1], actual.prop5[1]);
	JsUnit.assertEquals("評価値と期待値が同じであること(配列)",    expected.prop5[2], actual.prop5[2]);
}

/**
 * 
 */
function test_getFalse(){
	logger.debug("test_getFalse() が実行されました");
	
	var actual = getFalse();
	JsUnit.assertFalse("評価値がFalseであること（注意：このテストケースは故意に失敗させています）", actual);
}

/**
 * 
 */
function test_getNaN(){
	logger.debug("test_getNaN() が実行されました");

	// JavaScriptのモジュールを取得してテストを実行
	var testTarget = JsUnit.loadScriptModule(testTargetPagePath);
	
	var actual = testTarget.getNaN();
	JsUnit.assertNaN("評価値がNaNであること", actual);
}

/**
 * 
 */
function test_getSampleObject_異なることを確認する場合(){
	logger.debug("test_getSampleObject_異なることを確認する場合() が実行されました");

	// JavaScriptのモジュールを取得してテストを実行
	var testTarget = JsUnit.loadScriptModule(testTargetPagePath);

	var actual = testTarget.getSampleObject();
	var notExpected = 4;
	JsUnit.assertNotEquals("評価値と期待値が同じでないこと", notExpected, actual.prop5[2]);
}
