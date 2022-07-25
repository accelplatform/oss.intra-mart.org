function getTrue(){
	return true;
}

function getSampleObject(){
	var obj = {
		prop1 : "string",
		prop2 : 123,
		prop3 : false,
		prop4 : new Date(),
		prop5 : [1,2,3]
	};
	
	return obj;
}

function getFalse(){
	// 本来はfalseを返すべき関数ですが、テストを失敗させるために、trueを返却しています。
	return true;
}

function getNaN(){
	return NaN;
}

