var ImJsspRpc = {

	JSON_CONTENT_TYPE : "application/json; charset=utf-8",

	/**
	 * Default callback function when error occurs
	 */
	defaultOnErrorCallback : function(ex){

		var errorStr = "" + ex.message;

		// errorStr += "\n";
		// errorStr += "httpStatus : "  + ex.httpStatus  + "\n";
		// errorStr += "code: "         + ex.code        + "\n";
		// errorStr += "contentType : " + ex.contentType + "\n";
		// errorStr += "data : "        + ex.data        + "\n";

		alert(errorStr);
		return;
	},

	/**
	 * Send request
	 */
	sendJsspRpcRequest : function(url, argsArray, callback, onErrorCallback, method){

		onErrorCallback = (onErrorCallback == null) ? ImJsspRpc.defaultOnErrorCallback : onErrorCallback;
		method = (method == undefined) ? "post" : method;

		var jsonString = ImJson.toJSONString(argsArray);
		var asynFlag   = (callback != null);

		//====================================
		// Create XMLHttpRequest(=xhr)
		//====================================
		var xhr = ImAjax.createXMLHttpRequest();

		//====================================
		// Set callback function
		//====================================
		if(asynFlag){
			if(window.ActiveXObject){
				xhr.onreadystatechange = function () {
					if (xhr.readyState == 4) {
						var result;

						try{
							result = ImJsspRpc.receive(xhr);
						}
						catch(ex){
							onErrorCallback(ex);
							return;
						}

						callback(result);
					}
				};
			}
			else {
				xhr.onload = function () {
						var result;

						try{
							result = ImJsspRpc.receive(xhr);
						}
						catch(ex){
							onErrorCallback(ex);
							return;
						}

						callback(result);
				};
			}
		}
		else {
			if(window.ActiveXObject){
				xhr.onreadystatechange = function(){};
			}
			else {
				xhr.onload = function(){};
			}
		}

		//====================================
		// Send Request
		//====================================
		xhr.open(method, url, asynFlag);
		xhr = ImAjax.setContentTypeAndReqHeaders(xhr, ImJsspRpc.JSON_CONTENT_TYPE);
		xhr.send(jsonString);


		//====================================
		// Return result
		//====================================
		if(asynFlag){
			return;
		}
		else{
			return ImJsspRpc.receive(xhr);
		}
	},

	/**
	 * Receive response data
	 */
	receive : function(xhr){

		var objResult = ImAjax.createResultObject(xhr);

		if(objResult.error == true){
			var err = new Error(objResult.message);
				err.httpStatus  = objResult.httpStatus;
				err.code        = objResult.code;
				err.message     = objResult.message;
				err.contentType = objResult.contentType;
				err.data        = objResult.data;

			// **** THROW ERROR ****
			throw err;
		}
		else{
			return objResult.data;
		}

	}
}
