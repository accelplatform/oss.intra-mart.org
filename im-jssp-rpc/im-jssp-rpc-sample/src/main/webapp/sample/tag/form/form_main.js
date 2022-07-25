// バインド変数宣言
var result;         // 受信結果

var val_text;       // テキストフィールドの初期値
var val_passwd;     // パスワードフィールドの初期値
var val_check;      // チェックボックスの初期状態
var val_radio1;     // ラジオボタンの初期状態
var val_radio2;
var val_textarea;   // テキストエリアの初期値
var val_select;     // セレクトボックスの値
var selectedValue;  // セレクトボックスの初期値

var val_hidden1;    // hidden項目の初期値
var val_hidden2;

var pageTransitionInfoOnImSubmit;
var isImSubmit = false;

/**
 * ＜IMART type="form"＞ を利用したサンプル画面の初期化関数
 * フォームを作りながら値を受信する方法を示します。
 */
function init(request){

	Debug.print("***** 「sample/tag/form/form_main#init()」を実行 *****");

	// セレクトボックスのリストを作成
	val_select = new Array();
	val_select.prop1 = "value0";
	val_select.prop1 = "value1";
	val_select.prop2 = "value2";
	val_select.prop3 = "value3";
	val_select.prop4 = "value4";

	// テキストフィールド
	val_text = request.fc_text;

	// パスワードフィールド
	val_passwd = request.fc_passwd;

	// チェックボックス
	val_check = ! isUndefined(request.fc_check);

	// ラジオボタン
	if(request.fc_radio == "radio1"){
		val_radio1 = true;
	}
	else if(request.fc_radio == "radio2"){
		val_radio2 = true;
	}

	// テキストエリア
	val_textarea = request.fc_textarea;

	// セレクトボックス
	selectedValue = request.fc_select ;

	// hidden項目
	val_hidden1 = "Value for hidden1";
	val_hidden2 = "Value for hidden2";


	// ＜IMART type="submit"＞で遷移した際のページ遷移情報を抜き出します。
	for(var prop in request){
		if(prop.indexOf("imsubmit_") == 0){
			isImSubmit = true;
			pageTransitionInfoOnImSubmit = prop;
			
			delete(request[prop]);
			break;
		}
	}

	// バインド変数に登録
	result = request;
}

function submitAction2(request){
	Debug.print("***** 「sample/tag/form/form_main#submitAction2()」を実行 *****");
}

function submitAction4(request){
	Debug.print("***** 「sample/tag/form/form_main#submitAction4()」を実行 *****");
}