// バインド変数宣言
var ampm;			// 午前・午後判定(午前: true, 午後: false)

/**
 * ＜IMART type="condition"＞ を利用したサンプル画面の初期化関数
 *
 * バインド変数 ampm に対して、午前 or 午後を判定した真偽値を渡す事で、画面上の『午前』or『午後』の表示を切り替えます。
 * 変数 ampm の値が真値(true)の場合は『午前』と表示
 * 変数 ampm の値が偽値(false)の場合は『午後』と表示
 */
function init(request){
	var now = new Date();			// 現在時刻の取得

	// バインド変数への登録
	ampm = now.getHours() < 12;		// １２時前なら true (午前判定)
}