// バインド変数宣言
var numbers01to10 = new Array(1,2,3,4,5,6,7,8,9,10);
var strings01to10 = numbers01to10.toString();

var numbers11to20 = new Array(11,12,13,14,15,16,17,18,19,20);
var strings11to20 = numbers11to20.toString();

var result4sum;
var result4average;

var result4sample_sum;
var result4sample_average;

var result4creattionTime;

/**
 * 共通関数の実行
 */
function init(request) {

	// 1から10の合計と平均
	result4sum     = Procedure.sum(numbers01to10);
	result4average = Procedure.average(numbers01to10);

	// 11から20の合計と平均
	result4sample_sum     = Procedure.sample.sum(numbers11to20);
	result4sample_average = Procedure.sample.average(numbers11to20);

	// 共通関数が定義された日時
	result4creattionTime = Procedure.creattionTime;

}
