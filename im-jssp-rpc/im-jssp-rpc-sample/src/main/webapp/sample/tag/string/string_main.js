// バインド変数宣言
var now;			// 現在時刻
var time;			// 01/01,1970-00:00'00"000 からの経過時間
var gmt;			// 現在時刻の世界標準時間

/**
 * ＜IMART type="string"＞ を利用したサンプル画面の初期化関数
 */
function init(request) {
	now  = new Date();			// 現在時刻の取得
	time = now.getTime();		// 経過時間
	gmt  = now.toGMTString();	// GMT 表記文字列

}