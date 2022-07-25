var logger = Logger.getLogger();

function init(request){
	
	errorTest();
	warnTest();
	infoTest();
	debugTest();
	traceTest();
	
	doLogLoggerNameSpecify();
	doLogNullUndefinedAndObject();
	
	include("sample/script/logger/logger_included");

	Debug.browse("ログが出力されました。\n出力先を確認してください。\n（出力先のデフォルトはコンソールです）");
}

function errorTest(){
	Debug.print("エラーレベルは？: " + logger.isErrorEnabled());

	if (logger.isErrorEnabled()) {
		logger.error("(1)エラーレベルです。");
		logger.error("(2)エラーレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "ほげ");
		logger.error("(3)エラーレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "㈱①", new Date());

		var argArray = new Array();
		argArray[0] = "文字列";
		argArray[1] = 5.678; //数値
		argArray[2] = new Date()
		argArray[3] = true; 
		argArray[4] = false;
		logger.error("(4)エラーレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", argArray);

		var err = new Error("エラーレベルのJSError");
			err.prop0 = "文字列";
			err.prop1 = 5.678; //数値
			err.prop2 = new Date()
			err.prop3 = true; 
			err.prop4 = false;
		logger.error("(5)エラーレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", err);
		// logger.error("(6)エラーレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", new Packages.java.lang.Exception("エラーレベルの例外メッセージ"));
	}
}


function warnTest(){
	Debug.print("ワーニングレベルは？: " + logger.isWarnEnabled());

	if (logger.isWarnEnabled()) {
		logger.warn("(1)ワーニングレベルです。");
		logger.warn("(2)ワーニングレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "ほげ");
		logger.warn("(3)ワーニングレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "㈱①", new Date());

		var argArray = new Array();
		argArray[0] = "文字列";
		argArray[1] = 5.678; //数値
		argArray[2] = new Date()
		argArray[3] = true; 
		argArray[4] = false;
		logger.warn("(4)ワーニングレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", argArray);

		var err = new Error("ワーニングレベルのJSError");
			err.prop0 = "文字列";
			err.prop1 = 5.678; //数値
			err.prop2 = new Date()
			err.prop3 = true; 
			err.prop4 = false;
		logger.warn("(5)ワーニングレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", err);
		// logger.warn("(6)ワーニングレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", new Packages.java.lang.Exception("ワーニングレベルの例外メッセージ"));
	}
}

function infoTest(){
	Debug.print("インフォレベルは？: " + logger.isInfoEnabled());

	if (logger.isInfoEnabled()) {
		logger.info("(1)インフォレベルです。");
		logger.info("(2)インフォレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "ほげ");
		logger.info("(3)インフォレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "㈱①", new Date());

		var argArray = new Array();
		argArray[0] = "文字列";
		argArray[1] = 5.678; //数値
		argArray[2] = new Date()
		argArray[3] = true; 
		argArray[4] = false;
		logger.info("(4)インフォレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", argArray);

		var err = new Error("インフォレベルのJSError");
			err.prop0 = "文字列";
			err.prop1 = 5.678; //数値
			err.prop2 = new Date()
			err.prop3 = true; 
			err.prop4 = false;
		logger.info("(5)インフォレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", err);
		// logger.info("(6)インフォレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", new Packages.java.lang.Exception("インフォレベルの例外メッセージ"));
	}
}

function debugTest(){
	Debug.print("デバッグレベルは？: " + logger.isDebugEnabled());

	if (logger.isDebugEnabled()) {
		logger.debug("(1)デバッグレベルです。");
		logger.debug("(2)デバッグレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "ほげ");
		logger.debug("(3)デバッグレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "㈱①", new Date());

		var argArray = new Array();
		argArray[0] = "文字列";
		argArray[1] = 5.678; //数値
		argArray[2] = new Date()
		argArray[3] = true; 
		argArray[4] = false;
		logger.debug("(4)デバッグレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", argArray);

		var err = new Error("デバッグレベルのJSError");
			err.prop0 = "文字列";
			err.prop1 = 5.678; //数値
			err.prop2 = new Date()
			err.prop3 = true; 
			err.prop4 = false;
		logger.debug("(5)デバッグレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", err);
		// logger.debug("(6)デバッグレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", new Packages.java.lang.Exception("デバッグレベルの例外メッセージ"));
	}
}

function traceTest(){
	Debug.print("トレースレベルは？: " + logger.isTraceEnabled());
	
	if (logger.isTraceEnabled()) {
		logger.trace("(1)トレースレベルです。");
		logger.trace("(2)トレースレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "ほげ");
		logger.trace("(3)トレースレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", "㈱①", new Date());

		var argArray = new Array();
		argArray[0] = "文字列";
		argArray[1] = 5.678; //数値
		argArray[2] = new Date()
		argArray[3] = true; 
		argArray[4] = false;
		logger.trace("(4)トレースレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", argArray);

		var err = new Error("トレースレベルのJSError");
			err.prop0 = "文字列";
			err.prop1 = 5.678; //数値
			err.prop2 = new Date()
			err.prop3 = true; 
			err.prop4 = false;
		logger.trace("(5)トレースレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", err);
		// logger.trace("(6)トレースレベルです。「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」 and 「{}」.", new Packages.java.lang.Exception("トレースレベルの例外メッセージ"));
	}
}

function doLogLoggerNameSpecify(){
	var loggerName = "hoge.foo.bar";
	var loggerSpecify = Logger.getLogger(loggerName);
	
	loggerSpecify.info("ロガー名「"+ loggerName +"」でログを出力しました。");
}

function doLogNullUndefinedAndObject(){	
	logger.error(null);
	logger.warn(undefined);
	
	logger.info(12.34);
	logger.info(new Date());
	logger.info(false);

	var obj1 = {
		prop1: "value1",
		prop2: 56.7,
	};
	logger.info(obj1);

	var obj2 = {
		prop1: "obj2",
		prop2: 89.0,
		toString: function(){
			return "「" + this.prop1 + "」と「" + this.prop2 + "」";
		}
	};
	logger.info(obj2);
}
