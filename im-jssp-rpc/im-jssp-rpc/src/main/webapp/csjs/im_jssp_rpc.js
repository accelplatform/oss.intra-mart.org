/**
 * Send request
 */
function sendJsspRpcRequest(url, jsonString, callBack, method, unmarshallFunc) {

	method = (method == undefined) ? "post" : method;
	unmarshallFunc = (unmarshallFunc == undefined) ? unmarshall : unmarshallFunc;

	var http = createXMLHttpRequest();

	if(callBack != null){
		if(window.ActiveXObject){
			http.onreadystatechange = function () {
				if (http.readyState == 4) {
					callBack(receive(http, unmarshallFunc));
				}
			};
		} else {
			http.onload = function () {
				callBack(receive(http, unmarshallFunc));
			};
		}
	}
	else {
		if(window.ActiveXObject){
			http.onreadystatechange = function () {};
		}
		else {
			http.onload = function () {};
		}
	}

	http.open(method, url, (callBack != null));
	setContentType(http);
	try {
		http.send(jsonString);
	}
	catch(ex) {
		// For IE on WinXP (Error: 0x800c0008(-2146697208))
		writeDocument(http.responseText);
		throw ex;
	}

	if(callBack != null){
		return;
	}
	else{
		return receive(http, unmarshallFunc);
	}
}


/**
 * Create XMLHttpRequest
 */
function createXMLHttpRequest(){
	var msxmlNames =
			new Array("MSXML2.XMLHTTP.5.0", "MSXML2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP");

	/* Mozilla XMLHttpRequest */
	try {
		return new XMLHttpRequest();
	} catch(e) {}

	/* Microsoft MSXML ActiveX */
	for (var i=0;i < msxmlNames.length; i++) {
		try {
			return new ActiveXObject(msxmlNames[i]);
		} catch (e) {}
	}
}


/**
 * Receive response data
 */
function receive(http, unmarshallFunc){
	/* Get request results */
	var status, statusText, data;
	try {
		status     = http.status;
		statusText = http.statusText;
		data       = http.responseText;
	}
	catch(ex) {
		throw ex;
	}

	if(status != 200){
		// When error happens with session timeout, or internal server error.
		writeDocument(data);
		throw new Error(status, statusText);
	}

	/* un-marshall */
	try{
		return unmarshallFunc(data);
	}
	catch(ex){
				
		// if(ex.name == "SyntaxError" && ex.message == "parseJSON"){
		//     alert("Error occurred!! \n" + ex.name + ": " + ex.message);
		//     throw ex;
		// }

		// For Debug.Browse() page, mostly.
		writeDocument(data);
		return;
	}
}


/**
 * Set Content-Type value
 */
function setContentType(http){
	var contentTypeUrlenc = 'application/json; charset=utf-8';

	if(!window.opera){
		http.setRequestHeader('Content-Type',contentTypeUrlenc);
	}
	else {
		if((typeof http.setRequestHeader) == 'function'){
			http.setRequestHeader('Content-Type',contentTypeUrlenc);
		}
	}

	return http;
}


/**
 * Write document
 */
function writeDocument(value){
	document.write(value);
	document.close();
	return;
}
