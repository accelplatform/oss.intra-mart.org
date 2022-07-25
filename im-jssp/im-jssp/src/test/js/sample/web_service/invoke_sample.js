var wsdlFileURL = "http://localhost:8080/imart/services/SampleMemberInfoOperatorService?wsdl";

var wsUserID       = "ueda";
var wsPassword     = "ueda";
var wsLoginGroupID = "default";

var logger = Logger.getLogger();

/**
 * SOAPClientオブジェクトを利用してWebサービスを呼び出すサンプルです。
 */
function init(args){
	add();
	findAll();
}

/**
 * メンバー情報を追加します。
 */
function add(){
	
	//**************************************************************
	// ステップ１：WSDLを指定して SOAPClientオブジェクト のインスタンスを生成
	//**************************************************************
	try {
		var soapClient = new SOAPClient(wsdlFileURL);
		Debug.print("ステップ１ 完了");
	} 
	catch(ex) {
		logger.error("エラーが発生しました。", ex);
		throw ex;
	}


	//**************************************************************
	// ステップ２：Webサービスを呼び出すソースコードのサンプルを表示
	//**************************************************************
	var sampleCode = soapClient.getSampleCode("add");
	var msg  = "ステップ２ 完了。" + "\n";
		msg += "Webサービスを呼び出すソースコードのサンプルが表示されました。"	+ "\n";
		msg += "以下の3行をコメントアウトしてステップ３を実行してください";
	Debug.print(msg);
	Debug.print(sampleCode);
	return;
	
	
	//**************************************************************
	// ステップ３： Webサービスの呼び出し （ステップ２で出力された内容をカスタマイズ）
	//**************************************************************
	// ↓↓↓↓　コピー＆ペースト (ここから)　↓↓↓↓
	//-------------------------------
	// Sample Data : 'wsUserInfo'
	//-------------------------------
	var wsUserInfo =
	/* Object <WSUserInfo> */
	{
		/* String */
		"password" : wsPassword,

		/* String */
		"authType" : "PlainTextPassword",

		/* String */
		"userID" : wsUserID,

		/* String */
		"loginGroupID" : wsLoginGroupID
	};
	
	//-------------------------------
	// Sample Data : 'member'
	//-------------------------------
	var member =
	/* Object <Member> */
	{
		/* Boolean */
		"married" : true,

		/* Number */
		"age" : 123,

		/* String */
		"name" : "prop_name",

		/* String */
		"id" : "prop_id",

		/* Array <Member[]> */
		"children" : [

		],

		/* Date (Thu Jun 19 2008 12:34:56 GMT+0900 (JST)) */
		"birthDate" : new Date(1213846496000)
	};
	// ↑↑↑↑　コピー＆ペースト (ここまで)　↑↑↑↑


	//-------------------------------
	// Webサービスの呼び出し
	//-------------------------------
	try{
		var result = soapClient.add(wsUserInfo, member);
	}
	catch(soapFault){
		logger.error("エラーが発生しました。", soapFault);
		throw soapFault;
	}

	Debug.console("ステップ３ 完了",
				 "追加しました。",
				 "結果：" + result);
	return;
}

/**
 * 登録されているすべてのメンバー情報を検索します。
 */
function findAll(){
    
    //**************************************************************
	// ステップ１：WSDLを指定して SOAPClientオブジェクト のインスタンスを生成
	//**************************************************************
	try {
		var soapClient = new SOAPClient(wsdlFileURL);
		Debug.print("ステップ１ 完了");
	} 
	catch(ex) {
		logger.error("エラーが発生しました。", ex);
		throw ex;
	}	


	//**************************************************************
	// ステップ２：Webサービスを呼び出すソースコードのサンプルを表示
	//**************************************************************
	var sampleCode = soapClient.getSampleCode();
	var msg  = "ステップ２ 完了。" + "\n";
		msg += "Webサービスを呼び出すソースコードのサンプルが表示されました。" + "\n";
		msg += "以下の3行をコメントアウトしてステップ３を実行してください";
	Debug.print(msg);
	Debug.print(sampleCode);
	return;
	
	
	//**************************************************************
	// ステップ３： Webサービスの呼び出し （ステップ２で出力された内容をカスタマイズ）
	//**************************************************************
	// ↓↓↓↓　コピー＆ペースト (ここから)　↓↓↓↓
	//-------------------------------
	// Sample Data : 'wsUserInfo'
	//-------------------------------
	var wsUserInfo =
	/* Object <WSUserInfo> */
	{
		/* String */
		"password" : wsPassword,

		/* String */
		"authType" : "PlainTextPassword",

		/* String */
		"userID" : wsUserID,

		/* String */
		"loginGroupID" : wsLoginGroupID
	};
	// ↑↑↑↑　コピー＆ペースト (ここまで)　↑↑↑↑


	//-------------------------------
	// Webサービスの呼び出し
	//-------------------------------
	try{
		var result = soapClient.findAll(wsUserInfo);
	}
	catch(soapFault){
		logger.error("エラーが発生しました。", soapFault);
		throw soapFault;
	}

	Debug.console("ステップ３ 完了", result);
	return;
}