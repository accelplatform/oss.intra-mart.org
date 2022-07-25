/**
 * 合計値を求めます。
 */
function sum(arg1, arg2, arg3){

	Debug.print("######## EXECUTE! : sample/jssp_rpc/sum/server_side_logic/sum#sum() ########");
	Debug.print( "arg1 = " + arg1 + " [" + typeof arg1 + "]");
	Debug.print( "arg2 = " + arg2 + " [" + typeof arg2 + "]");
	Debug.print( "arg3 = " + arg3 + " [" + typeof arg3 + "]");

	return arg1 + arg2 + arg3;

/*
	// 引数配列「arguments」も利用可能です。
    var ans = 0;
    for (var idx = 0; idx < sum.arguments.length; idx++) {
        ans += sum.arguments[idx];
    }
    return(ans);
*/

}