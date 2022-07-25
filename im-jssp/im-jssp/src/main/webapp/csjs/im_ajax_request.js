/**
 * XMLHttpRequest 通信を簡単に行うためのメソッドを提供します。
 * 
 * @fileoverview XMLHttpRequest 通信を簡単に行うことができるオブジェクトです。<br/>
 * ImAjax オブジェクトを利用することによって、
 * XMLHttpRequest による通信を簡単に実装することができます。<br/>
 *
 * <p/>
 * <h4><a name='requireSourceFile'>必要なファイル</a></h4>
 * 	<ul>
 * 		<li>im_json.js</li>
 * 	</ul>
 *
 * <p/>
 * <h4><a name='aboutRequestData'>リクエストデータについて</a></h4>
 * ImAjax オブジェクトを利用した通信のリクエストデータは、
 * 指定された送信データの形式によって自動的に変換されます。<br/>
 * <br/>
 * 例えば、送信データにJavaScriptオブジェクトを指定した場合、
 * リクエストデータは、自動的にJSON形式の文字列に変換されます。<br/>
 * リクエストデータを受け取ったサーバサイドでは、
 * JSON形式の文字列 から JavaScriptオブジェクト へ変換を行うことでリクエストデータを処理します。<br/>
 * 
 * また、送信データにXMLオブジェクトを指定した場合、
 * リクエストデータは、自動的にXML形式の文字列に変換されます。<br/>
 * リクエストデータを受け取ったサーバサイドでは、
 * XML形式の文字列 から XMLオブジェクト へ変換することでリクエストデータを処理します。<br/>
 * 
 * （im-Jssp Framework の場合、
 *  Request オブジェクトのgetParameter()、および、getParameterValue()メソッドを利用することで、
 *  XML形式データの値を参照することもできます）<br/>
 * <br/>
 *
 * <p/>
 * <h4><a name='aboutResponseData'>レスポンスデータについて</a></h4>
 * ImAjax オブジェクトを利用した通信のレスポンスは、通信結果オブジェクトとしてまとめられます。<br/>
 * 通信結果オブジェクトには、エラーの有無、HTTPステータスコード、レスポンスデータ など が格納されます。<br/>
 * （詳しくは<a href="#aboutResultObject">「通信結果オブジェクトの構成」</a>を参照してください）<br/>
 * <br/>
 * 通信結果オブジェクトの「data」プロパティは、レスポンスのコンテントタイプに応じて自動的に変換されます。<br/>
 * <br/>
 * 例えば、コンテントタイプが「application/json」を含む場合、すなわち、レスポンスのボディがJSON形式の文字列である場合、<br/>
 * data プロパティには JavaScriptオブジェクト が格納されます。<br/>
 * 
 * また、コンテントタイプが「text/xml」を含む場合、すなわち、レスポンスのボディがXML形式の文字列である場合、<br/>
 * data プロパティには XMLオブジェクト が格納されます。<br/>
 * <br/>
 * 通信結果オブジェクトは、コールバック関数の引数として渡されます。<br/>
 * コールバック関数は、{@link ImAjax#requestAsyncSend} または {@link ImAjax#requestSend}の第三引数に指定します。<br/>
 * <br/>
 * このように ImAjaxを利用することによって、クライアント と サーバ間で、
 * JavaScriptオブジェクト、または、XMLによるデータの受け渡しを実現することが出来ます。<br/>
 * <br/>
 *
 * <p/>
 * <h4><a name='aboutResultObject'>通信結果オブジェクト</a></h4>
 * 通信結果オブジェクトの構成は以下の通りです。 <br>
 * 	<table border="1" cellpadding="2" cellspacing="1">
 * 		<tr>
 * 			<td rowspan="10">
 * 				<div align="center">通信結果オブジェクト(Object)</div>
 * 			</td>
 * 			<td colspan="2">
 * 				<strong>error</strong>
 * 				エラー判定 (Boolean)<br/>
 * 			</td>
 * 		</tr>
 * 		<tr>
 * 			<td colspan="2">
 * 				<strong>httpStatus</strong>
 * 				HTTPステータスコード (Number)<br/>
 * 			</td>
 * 		</tr>
 * 		<tr>
 * 			<td colspan="2">
 * 				<strong>code</strong>
 * 				エラーコード (String)<br/>
 * 				<br/>
 * 				error プロパティが false の場合、{@link ImAjax#AJAX_COMPLETE} (通信完了) が設定されます。<br/>
 * 				<br/>
 * 				error プロパティが true の場合、以下の値が設定されます。<br/>
 * 				<table border="1" cellpadding="2" cellspacing="1">
 * 					<tr>
 * 						<th>現象</th>
 * 						<th>コード値</th>
 * 					</tr>
 * 					<tr>
 * 						<td>JSON文字列からJavaScriptオブジェクトへの変換失敗した場合</td>
 * 						<td>{@link ImAjax#AJAX_PARSE_JSON_ERR}</td>
 * 					</tr>
 * 					<!--                 
 * 						<tr>
 * 							<td>セッションタイムアウトが発生した場合</td>
 * 							<td>{@link ImAjax#AJAX_TIMEOUT}</td>
 * 						</tr>
 * 					-->
 * 					<tr>
 * 						<td>上記以外</td>
 * 						<td>レスポンスヘッダ名「x-org-intra-mart-ajax-error-code（=ImAjax#RES_HEADER_ERROR_CODE）」に指定されている値</td>
 * 					</tr>
 * 				</table>
 * 			</td>
 * 		</tr>
 * 		<tr>
 * 			<td colspan="2">
 * 				<strong>message</strong>
 * 				code プロパティに対応したエラーメッセージ (String)<br/>
 * 			</td>
 * 		</tr>
 * 		<tr>
 * 			<td colspan="2">
 * 				<strong>contentType</strong>
 * 				レスポンスデータのコンテントタイプ (String)<br/>
 * 			</td>
 * 		</tr>
 * 		<tr>
 * 			<td colspan="2">
 * 				<strong>data</strong>
 * 				レスポンスデータ (JavaScriptオブジェクト、XMLオブジェクト、または、テキスト)<br/>
 * 			</td>
 * 		</tr>
 * 	</table>
 *
 * <p/>
 * <h4><a name='sampleCode'>サンプルコード</a></h4>
 * 	<table border="1">
 * 		<tr>
 * 			<th>
 * 				クライアント（client_sample.html ファイル）
 * 			</th>
 * 		</tr>
 * 		<tr>
 * 			<td>
 * 				<font size="-1.5">
 * <pre>
&lt;html&gt;
  &lt;head&gt;
    &lt;title&gt;ImAjaxオブジェクトのサンプル&lt;/title&gt;

    &lt;script type="text/javascript" src="csjs/im_json.js"&gt;&lt;/script&gt;
    &lt;script type="text/javascript" src="csjs/im_ajax_request.js"&gt;&lt;/script&gt;
    
    &lt;script language="javascript"&gt;
      // 送信先 URL
      var url = '&lt;imart type="string" value=url&gt;&lt;/imart&gt;';

      /&#42;&#42;
       &#42; 非同期通信を行います。
       &#42;/
      function async() {
        var sendData = new Object();
            sendData.<b><font color="fuchsia">clientSideText</font></b> = document.form1.text1.value;
        
        // リクエスト送信（非同期通信）
        ImAjax.requestAsyncSend(url, sendData, <b><font color="lime">callBackTest</font></b>);
        
        alert("リクエストを非同期通信で送信しました。");
      }

      /&#42;&#42;
       &#42; 同期通信を行います。
       &#42;/
      function sync() {
        var sendData = new Object();
            sendData.<b><font color="fuchsia">clientSideText</font></b> = document.form1.text1.value;
        
        // リクエスト送信（同期通信）
        ImAjax.requestSend(url, sendData, <b><font color="lime">callBackTest</font></b>);
        
        alert("リクエストを同期通信で送信しました。");
      }

      /&#42;&#42;
       &#42; コールバック関数
       &#42;/
      function <b><font color="lime">callBackTest</font></b>(result) {
        if(result.error == true){
          alert(result.message);
        }
        else {
          var alertMsg  = "========== callBackTest() で結果を受信しました。========== \n\n";
              alertMsg += result.data.<b><font color="red">serverSideText</font></b>;

          alert(alertMsg);
        }
      }

    &lt;/script&gt;
  &lt;/head&gt;

  &lt;body&gt;
    &lt;form name="form1"&gt;
      &lt;input type="text" name="text1" size="50" value="テストです。"&gt;&lt;br&gt;
      &lt;input type="button" value="リクエスト送信(非同期)" onclick="async()"&gt;
      &lt;input type="button" value="リクエスト送信(同期)"   onclick="sync()"&gt;
    &lt;/form&gt;
  &lt;/body&gt;
&lt;/html&gt;
 * </pre>
 * 				</font>
 * 			</td>
 * 		</tr>
 * 	</table>
 * <br/>
 *
 * 	<table border="1">
 * 		<tr>
 * 			<th>
 * 				クライアント（client_sample.js ファイル）
 * 			</th>
 * 		</tr>
 * 		<tr>
 * 			<td>
 * 				<font size="-1.5">
 * <pre>
var url;
function init(request){
  url = Web.getContextPath() + "/" + new URL("<b><font color="blue">server_sample</font></b>").location();
}
 * </pre>
 * 				</font>
 * 			</td>
 * 		</tr>
 * 	</table>
 * <br/>
 *
 * 	<table border="1">
 * 		<tr>
 * 			<th>
 * 				サーバサイド（<b><font color="blue">server_sample</font></b>.js ファイル）
 * 			</th>
 * 		</tr>
 * 		<tr>
 * 			<td>
 * 				<font size="-1.5">
 * <pre>
function init(request) {

  // JSON形式の文字列からJavaScriptオブジェクトに変換
  var messageBody = request.getMessageBody("UTF-8");
  var receiveData = ImJson.parseJSON(messageBody);

  // クライアントからのリクエストデータを表示
  Debug.console(receiveData);

  // 3秒間 スリープ（非同期通信 と 同期通信の違いを分かるようにするため）
  Debug.print("3秒間 スリープします。。。");
  sleep(3000);

  // レスポンスデータを作成
  var responseData = new Object();
      responseData.<b><font color="red">serverSideText</font></b> = receiveData.<b><font color="fuchsia">clientSideText</font></b> + "（" + new Date() + "に受け取りました。）";

  // クライアントにデータを返却
  var jsonString = ImJson.toJSONString(responseData);
  var response = Web.getHTTPResponse();
  response.setContentType("application/json; charset=UTF-8");
  response.sendMessageBodyString(jsonString);
}
 * </pre>
 * 				</font>
 * 			</td>
 * 		</tr>
 * 	</table>
 * <br/>
 *
 * @class XMLHttpRequest 通信を簡単に行うことができるオブジェクトです。<br/>
 * <br/>
 * ImAjax オブジェクトの利用方法、およびサンプルコードは、
 * <a href="overview-summary-im_ajax_request.js.html">こちら</a> を参照してください。<br/>
 * 
 * @constructor XMLHttpRequest 通信を行うメソッドを提供します。
 *
 * @version 1.0
 */
function ImAjax(){}

/**
 * エラーコード：通信完了
 * @final
 * @type String
 */
ImAjax.AJAX_COMPLETE = "AJAX_COMPLETE";

/**
 * エラーコード：JSON文字列からJavaScriptオブジェクトへの変換失敗
 * @final
 * @type String
 */
ImAjax.AJAX_PARSE_JSON_ERR = "AJAX_PARSE_JSON_ERR";

/**
 * @private
 * エラーコード：セッションタイムアウト
 * @final
 * @type String
 */
ImAjax.AJAX_TIMEOUT  = "IM-SECURITY-00001";

/**
 * @private
 * ImAjaxを利用した通信が、非同期通信であることを示す定数。
 * @final
 * @type Boolean
 */
ImAjax.COMM_ASYNC = true;

/**
 * @private
 * ImAjaxを利用した通信が、同期通信であることを示す定数。
 * @final
 * @type Boolean
 */
ImAjax.COMM_SYNC = false;

/**
 * @private
 * リクエストヘッダ名：ユーザエージェント
 * @type String
 */
ImAjax.REQ_HEADER_USER_AGENT = "x-org-intra-mart-ajax-user-agent";

/**
 * @private
 * リクエストヘッダ名：Ajaxリクエスト
 * @type String
 */
ImAjax.REQ_HEADER_AJAX_REQUEST = "x-org-intra-mart-ajax-request";

/**
 * @private
 * レスポンスヘッダ名：エラーコード<BR>
 * このヘッダが指定されている場合、ImAjaxはエラーが発生したと判定します。
 * @final
 * @type String
 */
ImAjax.RES_HEADER_ERROR_CODE = "x-org-intra-mart-ajax-error-code";

/**
 * @private
 * レスポンスヘッダ名：エラーメッセージ
 * このレスポンスヘッダの値は、
 * {@link ImAjax#RES_HEADER_ERROR_CODE} で指定されたコードに対応したメッセージが設定されます。<br/>
 * (このレスポンスヘッダの値は、encodeURIComponent()されている必要があります)
 * @final
 * @type String
 */
ImAjax.RES_HEADER_ERROR_MESSAGE = "x-org-intra-mart-ajax-error-message";

/**
 * @private
 * ContentType文字列：JSON形式
 * @final
 * @type String
 */
ImAjax.CONTENT_TYPE_JSON = "application/json";

/**
 * @private
 * ContentType文字列：XML形式
 * @final
 * @type String
 */
ImAjax.CONTENT_TYPE_XML  = "text/xml";

/**
 * @private
 * ContentType文字列：TEXT形式
 * @final
 * @type String
 */
ImAjax.CONTENT_TYPE_TEXT = "text/plain";

/**
 * @private
 * CharSet文字列 : UTF-8
 * @final
 * @type String
 */
ImAjax.CHARSET_UTF8 = "charset=utf-8";

/**
 * ブラウザ判定用変数。<br/>
 * 利用しているブラウザを判定するための変数です。「ImAjax.Browser.XXXX」形式で変数を参照可能です。<br/>
 * 例：Firefoxを利用している場合、変数「ImAjax.Browser.Firefox」は true を保持しています。<br/>
 * <br/>
 *  <table border="1">
 *  	<tr>
 *  		<th>利用しているブラウザ</th>
 *  		<th>変数</th>
 *  	</tr>
 *  	<tr>
 *  		<td>Internet Explorer</td>
 *  		<td>ImAjax.Browser.IE</td>
 *  	</tr>
 *  	<tr>
 *  		<td>Netscape</td>
 *  		<td>ImAjax.Browser.Netscape</td>
 *  	</tr>
 *  	<tr>
 *  		<td>FireFox</td>
 *  		<td>ImAjax.Browser.Firefox</td>
 *  	</tr>
 *  	<tr>
 *  		<td>Opera</td>
 *  		<td>ImAjax.Browser.Opera</td>
 *  	</tr>
 *  	<tr>
 *  		<td>Safari</td>
 *  		<td>ImAjax.Browser.Safari</td>
 *  	</tr>
 *  </table>
 * @final
 * @type Boolean
 */
ImAjax.Browser = new Object();
ImAjax.Browser.IE       = !!(window.attachEvent && !window.opera);
ImAjax.Browser.Opera    = !!window.opera;
ImAjax.Browser.WebKit   = !window.attachEvent && !window.opera && (navigator.userAgent.indexOf('AppleWebKit/') > -1);
ImAjax.Browser.Gecko    = !window.attachEvent && !window.opera && (navigator.userAgent.indexOf('Gecko') > -1) && (navigator.userAgent.indexOf('KHTML') == -1);
ImAjax.Browser.Firefox  = !window.attachEvent && !window.opera && (navigator.userAgent.indexOf('Firefox')  > -1);
ImAjax.Browser.Netscape = !window.attachEvent && !window.opera && (navigator.userAgent.indexOf('Netscape') > -1);
ImAjax.Browser.Safari   = !window.attachEvent && !window.opera && (navigator.userAgent.indexOf('AppleWebKit/') > -1) && (navigator.userAgent.indexOf('Safari') > -1);

/**
 * 非同期通信を行います。
 * @param {String}   url      送信先URL
 * @param {Object}   data     送信データ
 * @param {Function} callback コールバック関数
 * @param {String}   method   [option] サーバにデータを送る形式を get または post で指定します。デフォルトは"post"。
 */
ImAjax.requestAsyncSend = function(url, data, callback, method) {
	ImAjax.requestExecute(url, data, callback, method, ImAjax.COMM_ASYNC);
}

/**
 * 同期通信を行います。
 * @param {String}   url      送信先URL
 * @param {Object}   data     送信データ
 * @param {Function} callback コールバック関数
 * @param {String}   method   [option] サーバにデータを送る形式を get または post で指定します。デフォルトは"post"。
 */
ImAjax.requestSend = function(url, data, callback, method) {
	ImAjax.requestExecute(url, data, callback, method, ImAjax.COMM_SYNC);
}

/**
 * @private
 * Ajax通信処理
 *
 * @param {String}   url      送信先URL
 * @param {Object}   data     送信データ
 * @param {Function} callback コールバック関数
 * @param {String}   method   [option] "get"/"post" デフォルトは"post"
 * @param {Boolean}  isAsync  [option] ImAjax.COMM_ASYNC/ImAjax.COMM_SYNC。 デフォルトはImAjax.COMM_ASYNC
 */
ImAjax.requestExecute = function(url, data, callback, method, isAsync) {

	method  = (method  == undefined) ? "post" : method;
	isAsync = (isAsync == undefined) ? ImAjax.COMM_ASYNC : isAsync;

	//====================================
	// Create XMLHttpRequest(=xhr)
	//====================================
	var xhr = ImAjax.createXMLHttpRequest();

	//====================================
	// Set callback function
	//====================================
	if(window.ActiveXObject){
		xhr.onreadystatechange = function () {
			if (xhr.readyState == 4) {
				var result = ImAjax.createResultObject(xhr);
				callback(result);
			}
		};
	}
	else {
		xhr.onload = function () {
				var result = ImAjax.createResultObject(xhr);
				callback(result);
		};
	}

	//====================================
	// Convert Request Data & Define Content-Type
	//====================================
	var requestData = ImAjax.convertRequestData(data);

	//====================================
	// Send Request
	//====================================
	xhr.open(method, url, isAsync);
	xhr = ImAjax.setContentTypeAndReqHeaders(xhr, requestData.contentType);
	xhr.send(requestData.data);
}


/**
 * @private
 * JavaScriptオブジェクト（XML or Object or String）を、サーバに送信可能なリクエストデータに変換します。
 * あわせて、Content-Typeの判定も行います。
 *
 * @param {Object} data リクエストデータ。JavaScriptオブジェクト（XML or Object or String）を指定します。
 *
 * @return requestData 変換後のリクエストデータ。形式は{ data : "変換後のリクエストデータ", contentType : "コンテントタイプ"}
 * @type  Object
*/
ImAjax.convertRequestData = function(data){

	var requestData = {
		data        : undefined,
		contentType : ""
	}

	try {
		// XMLデータでは無い場合、下記の例外でキャッチ
		data.createElement("dummy");

		//  XMLデータの場合
		requestData.data        = ImAjax.domToXMLString(data);
		requestData.contentType = ImAjax.CONTENT_TYPE_XML + "; " + ImAjax.CHARSET_UTF8;
	}
	catch(e) {
		// JSONの場合
		if(typeof(data) == "object") {
			requestData.data        = ImJson.toJSONString(data);
			requestData.contentType = ImAjax.CONTENT_TYPE_JSON + "; " + ImAjax.CHARSET_UTF8;
		}
		// 上記以外
		else {
			requestData.data        = ImJson.escapeData( new String(data) );
			requestData.contentType = ImAjax.CONTENT_TYPE_TEXT + "; " + ImAjax.CHARSET_UTF8;
		}
	}

	return requestData;
}

/**
 * @private
 * DOMよりXML文字列を取得します
 *
 * @return XML文字列
 * @type String
 */
ImAjax.domToXMLString = function(dom){
	var xmlString = "";

	if(ImAjax.Browser.IE){
		xmlString = dom.xml;
	}
	else {
		var serial = new XMLSerializer();
		xmlString = serial.serializeToString(dom);
	}

	return xmlString;
}


/**
 * @private
 * Create XMLHttpRequest
 */
ImAjax.createXMLHttpRequest = function(){
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
 * @private
 * Set Content-Type & Request Headers
 */
ImAjax.setContentTypeAndReqHeaders = function(xhr, contentType){
	var userAgent = ImAjax.getBrowserName();

	if(!window.opera){
		xhr.setRequestHeader('Content-Type', contentType);
		xhr.setRequestHeader(ImAjax.REQ_HEADER_AJAX_REQUEST, "true");
		xhr.setRequestHeader(ImAjax.REQ_HEADER_USER_AGENT, userAgent);
	}
	else {
		if((typeof xhr.setRequestHeader) == 'function'){
			xhr.setRequestHeader('Content-Type', contentType);
			xhr.setRequestHeader(ImAjax.REQ_HEADER_AJAX_REQUEST, "true");
			xhr.setRequestHeader(ImAjax.REQ_HEADER_USER_AGENT, userAgent);
		}
	}

	return xhr;
}

/**
 * @private
 * Ajax通信結果オブジェクト作成
 *
 * @param {Object} xhr XMLHttpRequest
 *
 * @return objResult {Object} Ajax通信結果オブジェクト
 * @type Object
*/
ImAjax.createResultObject = function(xhr) {
	var parseJsonErrorFlg = false;

	/* ==================================== */
	/*  Create result object template       */
	/* ==================================== */
	var objResult = {
		error       : undefined,
		httpStatus  : xhr.status,
		code        : undefined,
		message     : undefined,
		contentType : undefined,
		data        : undefined
	};

	/* ====================================*/
	/*  Get value : IM_AJAX_ERROR_CODE and IM_AJAX_ERROR_MESSAGE*/
	/* ====================================*/
	var ajaxErrorCode    = undefined;
	var ajaxErrorMessage = undefined;

	var aryResHeaders = xhr.getAllResponseHeaders().split("\n");

	for(var idx = 0, max = aryResHeaders.length; idx < max; idx++){
		if(aryResHeaders[idx].match("Content-Type") != null){
			objResult.contentType = xhr.getResponseHeader("Content-Type");
		}
		else if(aryResHeaders[idx].match(ImAjax.RES_HEADER_ERROR_CODE) != null){
			ajaxErrorCode = xhr.getResponseHeader(ImAjax.RES_HEADER_ERROR_CODE);
		}
		else if(aryResHeaders[idx].match(ImAjax.RES_HEADER_ERROR_MESSAGE) != null){
			ajaxErrorMessage = xhr.getResponseHeader(ImAjax.RES_HEADER_ERROR_MESSAGE);

			/*  Unescape Error Message */
			try{
				ajaxErrorMessage = decodeURIComponent(ajaxErrorMessage);
			}
			catch(ex){
				// PLEASE SET ENGLISH MESSAGE. (or create original decoding function)
				// Because decodeURIComponent() is not defined. (ex IE5, NN4)
				ajaxErrorMessage = ajaxErrorMessage;
			}
		}
	}

	/* ==================================== */
	/*  Get value : "data"                  */
	/* ==================================== */
	var lowerCaseContentType = (typeof(objResult.contentType) == "string") ? objResult.contentType.toLowerCase() : "unknown";

	if (lowerCaseContentType.indexOf(ImAjax.CONTENT_TYPE_JSON) != -1) {
		try{
			objResult.data = ImJson.parseJSON(xhr.responseText);
		}
		/*  **** Detect parse JSON Error **** */
		catch(ex){
			parseJsonErrorFlg = true;

			objResult.message = ex.name + ": " + ex.message;
			objResult.data    = xhr.responseText;
		}
	}
	else if(lowerCaseContentType.indexOf(ImAjax.CONTENT_TYPE_XML) != -1) {
		objResult.data = xhr.responseXML;
	}
	else{
		objResult.data = xhr.responseText;
	}

	/* ====================================*/
	/*  Set result object property         */
	/* ====================================*/
	if( (xhr.status == 200) && (parseJsonErrorFlg == false) && (ajaxErrorCode == undefined) ){
		objResult.error = false;
		objResult.code  = ImAjax.AJAX_COMPLETE;
	}
	else{
		/* HTTP error */
		if(xhr.status != 200){
			objResult.error   = true;
			objResult.code    = ajaxErrorCode;
			objResult.message = xhr.statusText;
		}
		/* parse JSON Error */
		else if(parseJsonErrorFlg == true){
			objResult.error   = true;
			objResult.code    = ImAjax.AJAX_PARSE_JSON_ERR;
		}
		/* Session timeout */
		else if(ajaxErrorCode == ImAjax.AJAX_TIMEOUT){
			objResult.error   = true;
			objResult.code    = ImAjax.AJAX_TIMEOUT;
			objResult.message = (ajaxErrorMessage) ? ajaxErrorMessage : "Session timeout.";
		}
		/* Other Error */
		else{
			objResult.error   = true;
			objResult.code    = ajaxErrorCode;
			objResult.message = ajaxErrorMessage;
		}
	}

	return objResult;
}

/**
 * 利用しているブラウザを判定します。<br/>
 * <br/>
 *  <table border="1">
 *  	<tr>
 *  		<th>ブラウザ</th>
 *  		<th>返却値</th>
 *  	</tr>
 *  	<tr>
 *  		<td>Internet Explorer</td>
 *  		<td>IE</td>
 *  	</tr>
 *  	<tr>
 *  		<td>Netscape</td>
 *  		<td>Netscape</td>
 *  	</tr>
 *  	<tr>
 *  		<td>FireFox</td>
 *  		<td>Firefox</td>
 *  	</tr>
 *  	<tr>
 *  		<td>Opera</td>
 *  		<td>Opera</td>
 *  	</tr>
 *  	<tr>
 *  		<td>Safari</td>
 *  		<td>Safari</td>
 *  	</tr>
 *  </table>
 *
 * @return 利用しているブラウザをあらわす文字列
 * @type String
 */
ImAjax.getBrowserName = function(){

	if(ImAjax.Browser.IE){
		return "IE";
	}
	else if(ImAjax.Browser.Opera){
		return "Opera";
	}
	else if(ImAjax.Browser.Firefox){
		return "Firefox";
	}
	else if(ImAjax.Browser.Netscape){
		return "Netscape";
	}
	else if(ImAjax.Browser.Safari){
		return "Safari";
	}
	else if(ImAjax.Browser.WebKit){
		return "WebKit";
	}
	else if(ImAjax.Browser.Gecko){
		return "Gecko";
	}
	else {
		return "UnKnown";
	}
}
