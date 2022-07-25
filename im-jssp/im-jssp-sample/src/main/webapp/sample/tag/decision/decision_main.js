// バインド変数宣言
var dateIndex;			// 曜日を表す数字を指定

/**
 *  ＜IMART type="decision"＞ を利用したサンプル画面の初期化関数
 *
 * 現在日付の曜日インデックスを取得します。
 * JavaScript の仕様により、各曜日は下記インデックスに対応します。
 *      0: 日曜日
 *      1: 月曜日
 *      2: 火曜日
 *      3: 水曜日
 *      4: 木曜日
 *      5: 金曜日
 *      6: 土曜日
 */
function init(request){
	// 現在時刻の取得
	var now = new Date();			

	// バインド変数への登録
	dateIndex = now.getDay().toString();	// 曜日を示す数字を取得
}
