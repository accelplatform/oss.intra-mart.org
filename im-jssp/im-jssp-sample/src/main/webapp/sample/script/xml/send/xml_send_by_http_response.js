/**
 * HTTPResponseオブジェクトを使用したXML形式データの送信方法
 *
 * 一般的なWebブラウザは、受信したデータがどのような形式であるかを判定するために、レスポンスの
 * Content-Type エンティティヘッダフィールドを利用します。サーバで作成したXML形式のデータをクラ
 * イアントに送信するには、レスポンスの Content-Type エンティティヘッダフィールドに"text/xml"を指
 * 定します。ここでは、簡単な例として、以下のXML形式データをクライアントに送信するアプリケーション
 * を作成します。
 *
 * <?xml version='1.0' encoding='UTF-8'?>
 * <account>
 *   <user-id>ueda</user-id>
 *   <name>上田</name>
 *   <role>
 *     <role-id sample-attr="サンプル属性">level1</role-id>
 *   </role>
 * </account>
 */
function init(request){

	//----------------------------
	// DOM ツリーを構築
	//----------------------------
	var doc = new XMLDocument("<account/>");
	var accountNode = doc.getDocumentElement();

	// エレメントを作成
	var userIdNode = doc.createElement("user-id");
	var nameNode = doc.createElement("name");
	var roleNode = doc.createElement("role");
	var roleIdNode = doc.createElement("role-id");

	// テキストノードを作成
	var userIdText = doc.createTextNode("ueda");
	var nameText = doc.createTextNode("上田");
	var roleIdText = doc.createTextNode("level1");

	// 属性を設定
	roleIdNode.setAttribute("sample-attr", "サンプル属性");

	// 子ノードを追加
	userIdNode.appendChild(userIdText);
	nameNode.appendChild(nameText);
	roleNode.appendChild(roleIdNode);
	roleIdNode.appendChild(roleIdText);

	accountNode.appendChild(userIdNode);
	accountNode.appendChild(nameNode);
	accountNode.appendChild(roleNode);

	//----------------------------
	// XMLの文字列をバインド
	//----------------------------
	var encoding = "UTF-8";
	var xmlString = "<?xml version='1.0' encoding='" + encoding + "'?>" + doc.getXmlString();

	//----------------------------
	// Content-Type を設定
	//----------------------------
	var response = Web.getHTTPResponse();
	response.setContentType("text/xml; charset=" + encoding);

	//----------------------------
	// データ送信
	//----------------------------
	response.sendMessageBodyString(xmlString);

}