// バインド変数宣言
var bind;

/**
 * 受け取った引数をバインドします。
 */
function init(argV){
	bind = "引数：「" + argV.arg + "」";
}

/**
 * 合計値を求めます。
 */
function sum(arg1, arg2, arg3){
	
	return parseFloat(arg1) + parseFloat(arg2) + parseFloat(arg3);
	
/*
	// 引数配列「arguments」も利用可能です。
    var ans = 0;
    for (var idx = 0; idx < sum.arguments.length; idx++) {
        ans += parseFloat(sum.arguments[idx]);
    }
    return(ans);
*/

}