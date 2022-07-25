/**
 * 定数値をクライアントに返却します
 */
function getConstant(){

	Debug.print("######## EXECUTE! : sample/jssp_rpc/const_check/server_side_logic/const#getConstant() ########");

	var ary = new Array();

	//Number定数
	ary[0]=Number.MAX_VALUE;
	ary[1]=Number.MIN_VALUE;
	ary[2]=Number.POSITIVE_INFINITY;
	ary[3]=Number.NEGATIVE_INFINITY;
	ary[4]=Number.NaN;
	ary[5]=Infinity;
	ary[6]=-Infinity;
	ary[7]=NaN;

	//Math定数
	ary[8]=Math.PI;
	ary[9]=Math.SQRT2;
	ary[10]=Math.SQRT1_2;
	ary[11]=Math.E;
	ary[12]=Math.LN2;
	ary[13]=Math.LN10;
	ary[14]=Math.LOG2E;
	ary[15]=Math.LOG10E;

	//論理値, null, undefined
	ary[16]=true;
	ary[17]=false;
	ary[18]=null;
	ary[19]=undefined;

	return ary;
}

/**
 * 定数値をクライアントから受取ります。
 */
function sendConstant(obj){

	Debug.print("######## EXECUTE! : sample/jssp_rpc/const_check/server_side_logic/const#sendConstant() ########");

	var idx = 0;

	// 結果表示
	for(var elem in obj){
		if (obj.hasOwnProperty(elem)) {
			Debug.print(idx++ + " : " + String(obj[elem]) + " [" + (typeof obj[elem]) + "]");
		}
	}

}