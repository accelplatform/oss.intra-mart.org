// バインド変数宣言
var nextPage;

function init(request){
	nextPage = "sample/tag/link/next";
}


function actionTest1(request){
	Debug.print("actionTest1() -> [request.arg = " + request.arg + "]");
}