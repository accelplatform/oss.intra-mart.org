
/**
 * Debugオブジェクトを利用したデバッグ方法の初期化関数
 */
function init(request){

	var debugString = "デバッグ中です。";

	// コンソールへメッセージを出力します。
	Debug.print("Debug.print: " + debugString);

	// ファイルへメッセージを出力します。
	// 出力ファイルはコンテキストパス直下の debug.log という名前のファイルです。 （設定にて変更可能）
	Debug.write("Debug.write: " + debugString);

	var testObj = new Object();
	testObj.stringProp   = "value1";
	testObj.dateProp     = new Date();
	testObj.numberProp   = 1.23;
	testObj.booleanProp  = false;
	testObj.arrayProp    = new Array();
	testObj.arrayProp[0] = "arry0";
	testObj.arrayProp[1] = "arry1";
	testObj.arrayProp[2] = "arry2";
	testObj.arrayProp[3] = "arry3";
	testObj.arrayProp[4] = "arry4";

	// 指定の変数の情報をブラウザコンソール上に表示します。
	Debug.console(debugString, testObj);
	
	// 指定の変数の情報をブラウザ画面上に表示します。
	Debug.browse(debugString, testObj);
}