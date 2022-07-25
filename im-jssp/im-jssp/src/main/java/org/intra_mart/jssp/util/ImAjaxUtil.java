package org.intra_mart.jssp.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.jssp.util.locale.LocaleHandler;
import org.intra_mart.jssp.util.locale.LocaleHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * CSJSライブラリ「ImAjax」で通信を行う際に有用なサーバサイドのユーティリティクラスです。<br/>
 * <br/>
 * <a name="ResponseHeaderFormat"><br/>
 * CSJSライブラリ「ImAjax」で通信を行う際、
 * レスポンスヘッダにエラーコードとエラーメッセージを設定することが可能です。<br/>
 * レスポンスヘッダのフォーマットは以下の通りです。
 * <table border="1">
 * 	<tr>
 * 		<th>
 * 			ヘッダー名
 * 		</th>
 * 		<th>
 * 			ヘッダー値
 * 		</th>
 * 	</tr>
 * 	<tr>
 * 		<td>
 * 			<a href="#RESPONSE_HEADER_NAME_IM_AJAX_ERROR_CODE">x-org-intra-mart-ajax-error-code</a>
 * 		</td>
 * 		<td>
 * 			エラーコード
 * 		</td>
 * 	</tr>
 * 	<tr>
 * 		<td>
 * 			<a href="#RESPONSE_HEADER_NAME_IM_AJAX_ERROR_MESSAGE">x-org-intra-mart-ajax-error-message</a>
 * 		</td>
 * 		<td>
 * 			エラーコードに紐づくエラーメッセージ<br>
 * 		</td>
 * 	</tr>
 * </table>
 * <br/>
 * 
 * エラーメッセージは、JavaScript(＝EcmaScript) のencodeURIComponent()を利用して、
 * 自動的にエンコードされます。<br/>
 * 
 * @version 1.0
 */
public class ImAjaxUtil {

	/**
	 * ImAjaxからの通信だということを判定可能にするためのリクエスト・ヘッダ名。
	 */
	public static final String REQUEST_HEADER_NAME_IM_AJAX_REQUEST = "x-org-intra-mart-ajax-request";
	
	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダ名。
	 */
	public static final String REQUEST_HEADER_NAME_IM_AJAX_USER_AGENT = "x-org-intra-mart-ajax-user-agent";

	
	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Internet Explorer 用
	 */
	public static final String IM_AJAX_USER_AGENT_IE = "IE";

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Firefox 用
	 */
	public static final String IM_AJAX_USER_AGENT_FIREFOX = "Firefox";

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Netscape 用
	 */
	public static final String IM_AJAX_USER_AGENT_NETSCAPE = "Netscape";

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Opera 用
	 */
	public static final String IM_AJAX_USER_AGENT_OPERA = "Opera";

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Safari 用
	 */
	public static final String IM_AJAX_USER_AGENT_SAFARI = "Safari";

	
	
	/**
	 * ImAjaxへエラーコードを通知するためのレスポンス・ヘッダ名。
	 */
	public static final String RESPONSE_HEADER_NAME_IM_AJAX_ERROR_CODE = "x-org-intra-mart-ajax-error-code"; 

	
	/**
	 * ImAjaxへエラーメッセージを通知するためのレスポンス・ヘッダ名。
	 */
	public static final String RESPONSE_HEADER_NAME_IM_AJAX_ERROR_MESSAGE = "x-org-intra-mart-ajax-error-message"; 


	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param errorCode エラーコード
	 * @throws AccessSecurityException メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(String errorCode) {
		
		HttpServletResponse response = getCurrentResponse();
		setErrorResponseHeaders(response, errorCode);
	}
	
	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param errorCode エラーコード
	 * @param arg 置換文字列
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(String errorCode,
												 String arg) {
		
		HttpServletResponse response = getCurrentResponse();
		setErrorResponseHeaders(response, errorCode, arg);
	}

	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param errorCode エラーコード
	 * @param arg1 置換文字列１
	 * @param arg2 置換文字列２
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(String errorCode,
												 String arg1, 
												 String arg2) {
		
		HttpServletResponse response = getCurrentResponse();
		setErrorResponseHeaders(response, errorCode, arg1, arg2);
	}

	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param errorCode エラーコード
	 * @param arg1 置換文字列１
	 * @param arg2 置換文字列２
	 * @param arg3 置換文字列３
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(String errorCode,
												 String arg1,
												 String arg2,
												 String arg3){
		
		HttpServletResponse response = getCurrentResponse();
		setErrorResponseHeaders(response, errorCode, arg1, arg2, arg3);
	}

	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param errorCode エラーコード
	 * @param args 置換文字列の配列
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(String errorCode, 
												 String[] args) {	
		
		HttpServletResponse response = getCurrentResponse();
		setErrorResponseHeaders(response, errorCode, args);
	}
	
	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param response レスポンス
	 * @param errorCode エラーコード
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(HttpServletResponse response, 
												 String errorCode) {
		
		String errorMessage = getMessage(errorCode, null);
		setErrorFieldHeaders(response, errorCode, errorMessage);
	}

	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param response レスポンス
	 * @param errorCode エラーコード
	 * @param arg 置換文字列
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(HttpServletResponse response, 
												 String errorCode,
												 String arg) {
		
		String errorMessage = getMessage(errorCode, new String[]{ arg });
		setErrorFieldHeaders(response, errorCode, errorMessage);
	}

	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param response レスポンス
	 * @param errorCode エラーコード
	 * @param arg1 置換文字列１
	 * @param arg2 置換文字列２
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(HttpServletResponse response, 
												 String errorCode, 
												 String arg1, 
												 String arg2) {
		
		String errorMessage = getMessage(errorCode, new String[]{ arg1, arg2 });
		setErrorFieldHeaders(response, errorCode, errorMessage);
	}

	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param response レスポンス
	 * @param errorCode エラーコード
	 * @param arg1 置換文字列１
	 * @param arg2 置換文字列２
	 * @param arg3 置換文字列３
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(HttpServletResponse response,
												 String errorCode,
												 String arg1,
												 String arg2,
												 String arg3) {
		
		String errorMessage = getMessage(errorCode, new String[]{ arg1, arg2, arg3 });
		setErrorFieldHeaders(response, errorCode, errorMessage);
	}

	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * レスポンスヘッダのフォーマットは、<a href="#ResponseHeaderFormat">こちら</a>を参照してください。
	 * 
	 * @param response レスポンス
	 * @param errorCode エラーコード
	 * @param args 置換文字列の配列
	 * @ メッセージを取得に失敗した場合、または引数が不正な場合にスローされます。
	 */
	public static void setErrorResponseHeaders(HttpServletResponse response,
												 String errorCode, 
												 String[] args) {	
		
		String errorMessage = getMessage(errorCode, args);
		setErrorFieldHeaders(response, errorCode, errorMessage);
	}
	
	
	/**
	 * @return
	 */
	private static HttpServletResponse getCurrentResponse() {
		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		HttpServletResponse response = httpContext.getResponse();

		if(response == null){
			throw new IllegalStateException("HttpServletResponse in HTTPContext is null.");
		}
		return response;
	}	


	/**
	 * エラーコードに対応するエラーメッセージを取得します。
	 * @param errorCode
	 * @param args
	 */
	private static String getMessage(String errorCode, String[] args){
		
		// TODO ゆくゆくは、MessageManagerを設けたい。その際、Map等でロケールごとにキャッシュする余地あり
		LocaleHandler handler = LocaleHandlerManager.getLocaleHandler();
		Locale locale = handler.getLocale();
		
		ResourceBundle resourceBundle = ResourceBundle.getBundle(_errorMessagePropertiesBaseName, locale);
		
		String errorMessage;
		try{
			errorMessage = resourceBundle.getString(errorCode);
		}
		catch(MissingResourceException mre){
			errorMessage = "#" + errorCode;
		}
		
		return MessageFormat.format(errorMessage, (Object[])args);
	}

	private static String _errorMessagePropertiesBaseName;
	static {
		_errorMessagePropertiesBaseName = System.getProperty("org.intra_mart.jssp.util.ImAjaxUtil.errorMessagePropertiesBaseName");
		if(_errorMessagePropertiesBaseName == null){
			// デフォルト値
			_errorMessagePropertiesBaseName = "conf.jssp-error-message";
		}
	}

	
	/**
	 * 実際にヘッダを設定するメソッドです。
	 * 
	 * @param response
	 * @param errorCode
	 * @param errorMessage
	 */
	private static void setErrorFieldHeaders(HttpServletResponse response, String errorCode, String errorMessage){
		response.setHeader(RESPONSE_HEADER_NAME_IM_AJAX_ERROR_CODE, errorCode);
		
		String encodedErrorMessage = encodeURIComponent(errorMessage);
		response.setHeader(RESPONSE_HEADER_NAME_IM_AJAX_ERROR_MESSAGE, encodedErrorMessage);
	}

	/**
	 * 引数 value に与えられた文字列を encodeURIComponent() します
	 * 
	 * @param value encodeURIComponentを行う文字列
	 * @return encodeURIComponent後の文字列
	 */
	private static String encodeURIComponent(String value) {
		
		String replacedValue = value.replaceAll("\"", "\\\"");

		String varName4EncodedString = "encodedString";
        String jsSourceName = "encodeURIComponentOnECMAScript";
		String jsSource = "var " + varName4EncodedString + " = encodeURIComponent(\"" + replacedValue + "\")";
		
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();

			cx.evaluateString(scope, jsSource, jsSourceName, 1, null);

			Object x = scope.get(varName4EncodedString, scope);
			if (x != Scriptable.NOT_FOUND) {
				replacedValue = Context.toString(x);
			}
			else{
				throw new RuntimeException("Error encodeURIComponent: " + value);
			}
		}
		finally {
			Context.exit();
		}

		return replacedValue;
	}
}
