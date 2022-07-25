/**
 * XMLParserオブジェクトを使用したXML形式データの受信方法を示します。
 */
function init(request){

	//----------------------------
	// メッセージボディを取得
	//----------------------------
	var messageBody = request.getMessageBody("UTF-8");

	//----------------------------
	// XMLデータの構文解析
	//----------------------------
	var xmlParser = new XMLParser();
	var doc = xmlParser.parseString(messageBody);

	 if(xmlParser.isError()){
		Debug.browse("エラーが発生しました。", xmlParser.getErrorMessage());
	}

	//----------------------------
	// <user-id>, <name>, <role>ノード取得
	//----------------------------
	var childNodes  = null;
	var accountNode = doc.getDocumentElement();
	var userIdNode  = null;
	var nameNode    = null;
	var roleNode    = null;

	childNodes = accountNode.getChildNodes();
	for(var i = 0 ; i < childNodes.length ;i++) {
		if(childNodes[i].getTagName() == "user-id") {
			userIdNode = childNodes[i];
		}
		else if(childNodes[i].getTagName() == "name") {
			nameNode = childNodes[i];
		}
		 else if(childNodes[i].getTagName() == "role") {
			roleNode = childNodes[i];
		}
	}

	//----------------------------
	// <role-id>ノード取得
	//----------------------------
	var roleIdNode = null;
	childNodes = roleNode.getChildNodes();
	for(var i = 0 ; i < childNodes.length ;i++) {
		if(childNodes[i].getTagName() == "role-id") {
			roleIdNode = childNodes[i];
		}
	}

	//----------------------------
	// <role-id>ノードの属性取得
	//----------------------------
	var roleIdAttr = roleIdNode.getAttribute("sample-attr");

	//----------------------------
	// 各ノードの値を表示
	//----------------------------
	Debug.browse("XMLParserオブジェクトを使用したXML形式データの受信方法", 
				 userIdNode.getChildNodes()[0].getValue(),
				 nameNode.getChildNodes()[0].getValue(),
				 roleIdNode.getChildNodes()[0].getValue(),
				 roleIdAttr);

 }