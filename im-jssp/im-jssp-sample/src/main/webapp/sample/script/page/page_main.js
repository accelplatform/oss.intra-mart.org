// バインド変数宣言
var sumValue;
var pageContents;

/**
 * 「sample/script/page/execute/page_execute」のページコンテンツを取得します。
 */
function init(request){

	var argV = new Object();
	argV.arg = "あいうえお";

	// ページコンテンツの取得
	pageContents = Page.getContents("sample/script/page/execute/page_execute", argV);
}

/**
 * 「sample/script/page/execute/page_execute」内に定義されている「sum()」関数を実行します。
 */
function callSumFunction(request){

	// 指定関数の実行
	var sumValue = Page.executeFunction("sample/script/page/execute/page_execute",
										"sum",
										request.arg1, request.arg2, request.arg3);

	Debug.browse("「" + request.arg1 + "」と「" + request.arg2 + "」と「" + request.arg3 + "」の合計値は、" +
				 "『" + sumValue + "』です。");
}
