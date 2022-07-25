// 共通関数定義
Procedure.define("sum",     _sum);
Procedure.define("average", _average);

Procedure.define("sample", new sampleProcedure());  // オブジェクトの定義も可能です。
Procedure.define("creattionTime", new Date());

/**
 * 「合計」と「平均」を算出
 */
function sampleProcedure() {
	this.sum     = _sum;
	this.average = _average;
}

/**
 * 引数 arrayArg 内の要素の合計を返却します。
 * 引数 arrayArg が配列でない場合は、nullを返却します。
 * 引数 arrayArg の各要素が数値ではない場合、nullを返却します。
 */
function _sum(arrayArg) {

	var sum = 0;

	if(! isArray(arrayArg)) {
		return null;
	}

	for(var idx in arrayArg) {
		if (arrayArg.hasOwnProperty(idx)) {
			if(! isNumber(arrayArg[idx])) {
				return null;
			}
	
			sum += arrayArg[idx];
		}
	}

	return sum;
}

/**
 * 引数 arrayArg 内の要素の平均を返却します。
 * 引数 arrayArg が配列でない場合は、nullを返却します。
 * 引数 arrayArg の各要素が数値ではない場合、nullを返却します。
 */
function _average(arrayArg) {
	var sum = _sum(arrayArg);
	return sum / arrayArg.length;
}
