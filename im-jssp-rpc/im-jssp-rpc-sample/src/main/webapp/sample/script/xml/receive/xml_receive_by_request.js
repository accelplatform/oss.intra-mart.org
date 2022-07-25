/**
 * Requestオブジェクトを使用したXML形式データの受信方法を示します。
 *
 * Request オブジェクトの getParameter()、および、getParameterValue() メソッドを利用することで、
 * XML形式データの値を参照することができます（これにより、Adobe Flash Playerなどのリッチクライア
 * ントから送信されるXML形式データを簡単に取り扱うことができます）。
 *
 * Request#getParameter()の引数には、以下の形式に則ったパラメータ名を指定します。
 * 　・ XML 形式データの各タグ名をセパレータ「/」で区切って指定する （ルートは「/」）
 * 　・ 属性値を取得する際は、属性名の前に「@」を付与する
 *
 * この機能を利用するには以下の条件を満たしている必要があります。
 * 　・ リクエストのメソッドが「POST」であること
 * 　・ リクエストの Content-Type エンティティヘッダフィールドが「text/xml」であること
 * 　・ リクエストのメッセージボディ部が構文解析可能なXMLデータであること
 * 　・ org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestMessageBodyFilter が適用されていること
 */
function init(request){

	var userId     = request.getParameterValue("/account/user-id");
	var name       = request.getParameterValue("/account/name");
	var roleId     = request.getParameterValue("/account/role/role-id");
	var sampleAttr = request.getParameterValue("/account/role/role-id/@sample-attr");

	Debug.browse("Requestオブジェクトを使用したXML形式データの受信方法", userId, name, roleId, sampleAttr);
	
}
