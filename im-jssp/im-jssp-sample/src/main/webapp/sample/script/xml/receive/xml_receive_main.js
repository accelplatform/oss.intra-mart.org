var xml_data;
var nextUrl4ReceiveByRequest;
var nextUrl4ReceiveByParser;

function init(request){

	var urlObject = null;
	urlObject = new URL("sample/script/xml/receive/xml_receive_by_request");
	nextUrl4ReceiveByRequest = urlObject.location();

	urlObject = new URL("sample/script/xml/receive/xml_receive_by_parser");
	nextUrl4ReceiveByParser = urlObject.location();

	xml_data = "";
	xml_data += "<?xml version='1.0' encoding='UTF-8'?>"                   + "\n";
	xml_data += "<account>"                                                + "\n";
	xml_data += "  <user-id>ueda</user-id>"                                + "\n";
	xml_data += "  <name>上田</name>"                                      + "\n";
	xml_data += "  <role>"                                                 + "\n";
	xml_data += "    <role-id sample-attr='サンプル属性'>level1</role-id>" + "\n";
	xml_data += "  </role>"                                                + "\n";
	xml_data += "</account>"                                               + "\n";

}