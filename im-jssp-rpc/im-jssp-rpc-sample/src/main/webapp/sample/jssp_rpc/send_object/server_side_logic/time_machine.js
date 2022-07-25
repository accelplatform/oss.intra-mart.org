/**
 * タイムマシーン(偽)
 */
function travel( now, years, person ) {

	Debug.print("######## EXECUTE! : sample/jssp_rpc/send_object/server_side_logic/time_machine#travel() ########");
	Debug.print( "now   = " + now   + " [" + typeof now + "]");
	Debug.print( "years = " + years + " [" + typeof years + "]");
	Debug.print( "person.lastName    = " + person.lastName    + " [" + typeof person.lastName + "]");
	Debug.print( "person.firstName   = " + person.firstName   + " [" + typeof person.firstName + "]");
	Debug.print( "person.age         = " + person.age         + " [" + typeof person.age + "]");
	Debug.print( "person.nationality = " + person.nationality + " [" + typeof person.nationality + "]");

	if( !isDate(now) ){
		var obj = new Object();
		obj.error = true;
		obj.errorMessage = "ゲンザイ ニジチ ガ フセイ デス";
		return 	obj;
	}
	else if( !isNumber(years) || isNaN(years) ){
		var obj = new Object();
		obj.error = true;
		obj.errorMessage = "ナンネンゴ ニ イドウ スレバ ヨイカ ワカリマセン";
		return 	obj;
	}
	else if(parseInt(years, 10) < 0){
		var obj = new Object();
		obj.error = true;
		obj.errorMessage = "カコ ヘ　イドウ　デキマセン";
		return 	obj;
	}
	else if(parseInt(person.age, 10) < 0){
		var obj = new Object();
		obj.error = true;
		obj.errorMessage = "マダ　ウマレテ　イマセン";
		return 	obj;
	}
	
	// years 年後の日付を取得
	now.setFullYear( now.getFullYear() + years ); 
	
	// プロパティ初期化
	person.error = false;
	person.resultDate = now;
	person.married = false;
	person.children = new Array();	

	// 年齢
	var nowAge = parseInt(person.age, 10);
	person.age = nowAge + parseInt(years, 10);

	// marriedAge 才で結婚(改名＆国籍は日本)
	var marriedAge = 31;
	if(person.age >= marriedAge) {
		person.married     = true;
		person.lastName    = getLastName();
		person.nationality = "日本";
	}

	// 結婚から2年後に第1子
	if(person.age >= marriedAge + 2){
		person.children.push(getChildName(2));
	}

	// 結婚から3年後に第2子
	if(person.age >= marriedAge + 3){
		person.children.push(getChildName(3));
	}

	// 結婚から6年後に第3子
	if(person.age >= marriedAge + 6){
		person.children.push(getChildName(6));
	}

	// 結婚から10年後に第4子
	if(person.age >= marriedAge + 10){
		person.children.push(getChildName(10));
	}

	if(person.age >= marriedAge + 50){
		person.married = false;
	}
	
	return person;
}


function getLastName(){
	var lastNameArray = ["上田", "林", "円山", "関根", "寺田", "吉川", "萩本", "生田", "片山", "長宗我部"];
	var idx = (Session.getCreationTime() / 1000) % 10;

	return lastNameArray[parseInt(idx,10)];
}

function getChildName(val){
	var childNameArray = ["辰男", "順子", "政義", "彩乃", "雅彦", "あおい", "博文", "千香", "龍之介", "はるか"];
	var idx = ((Session.getCreationTime() / 1000) + val) % 10;

	return childNameArray[parseInt(idx,10)];
}

function changeFuture(){
	Session.invalidate();
	Debug.print("未来を変更しました[" + Session.getCreationTime() / 1000 + "]");
}