// バインド変数宣言
var result;         // 受信結果

var pageTransitionInfoOnImSubmit;
var isImSubmit = false;

function init(request){

	Debug.print("##### 「sample/tag/form/submit_next.js#init()」を実行 #####");

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