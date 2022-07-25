// バインド変数宣言
var count;			// 表示回数

/**
 * セッション変数を利用したサンプル画面の初期化関数
 */
function init(request){
	// セッション変数の取得
	var times = Session.getAttribute("example.visit_times");

	// 初回訪問チェック
	if(isUndefined(times)){
		times = 0;
	}

	// 訪問回数のインクリメント
	times = times + 1;

	// セッション変数への保存
	Session.setAttribute("example.visit_times", times);

	// バインド変数への登録
	count = times;



	// ---- 以下は、関数実行のサンプルです ----
	Session.setAttribute("example.name1", "value1");
	Session.setAttribute("example.name2", "value2");
	Session.setAttribute("example.name3", "value3");

	Debug.print("*************************************************************");
	Debug.print("ID                  : " + Session.getId());
	Debug.print("AttributeNames      : " + Session.getAttributeNames());
	Debug.print("MaxInactiveInterval : " + Session.getMaxInactiveInterval() + "[s]");
	Debug.print("CreationTime        : " + new Date(Session.getCreationTime()));
	Debug.print("LastAccessedTime    : " + new Date(Session.getLastAccessedTime()));
	Debug.print("*************************************************************");
}


/**
 * セッションを無効化します。
 */
function invalidate(){
	Session.invalidate();
}