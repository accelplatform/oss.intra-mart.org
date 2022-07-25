package org.intra_mart.jssp.script.api;

import org.intra_mart.jssp.util.ImAjaxUtil;
import org.intra_mart.jssp.util.locale.LocaleHandler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * CSJSライブラリ「ImAjax」で通信を行う際に有用なサーバサイドのユーティリティオブジェクトです。<br/>
 * <br/>
 * 
 * @scope public
 * @name ImAjaxUtil
 * @version 1.0
 */
public class ImAjaxUtilObject extends ScriptableObject {

	/* (non-Javadoc)
	 * @see jp.co.intra_mart.system.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName() {
		return "ImAjaxUtil";
	}

	/**
	 * ImAjaxからの通信だということを判定可能にするためのリクエスト・ヘッダ名。
	 * @scope public
	 */
	public static final String REQUEST_HEADER_NAME_IM_AJAX_REQUEST = ImAjaxUtil.REQUEST_HEADER_NAME_IM_AJAX_REQUEST;
	
	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダ名。
	 * @scope public
	 */
	public static final String REQUEST_HEADER_NAME_IM_AJAX_USER_AGENT = ImAjaxUtil.REQUEST_HEADER_NAME_IM_AJAX_USER_AGENT;

	
	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Internet Explorer 用
	 * @scope public
	 */
	public static final String IM_AJAX_USER_AGENT_IE = ImAjaxUtil.IM_AJAX_USER_AGENT_IE;

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Firefox 用
	 * @scope public
	 */
	public static final String IM_AJAX_USER_AGENT_FIREFOX = ImAjaxUtil.IM_AJAX_USER_AGENT_FIREFOX;

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Netscape 用
	 * @scope public
	 */
	public static final String IM_AJAX_USER_AGENT_NETSCAPE = ImAjaxUtil.IM_AJAX_USER_AGENT_NETSCAPE;

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Opera 用
	 * @scope public
	 */
	public static final String IM_AJAX_USER_AGENT_OPERA = ImAjaxUtil.IM_AJAX_USER_AGENT_OPERA;

	/**
	 * ブラウザの種類を判定可能にするためのリクエスト・ヘッダの設定値 : Safari 用
	 * @scope public
	 */
	public static final String IM_AJAX_USER_AGENT_SAFARI = ImAjaxUtil.IM_AJAX_USER_AGENT_SAFARI;

	
	
	/**
	 * ImAjaxへエラーコードを通知するためのレスポンス・ヘッダ名。
	 * @scope public
	 */
	public static final String RESPONSE_HEADER_NAME_IM_AJAX_ERROR_CODE = ImAjaxUtil.RESPONSE_HEADER_NAME_IM_AJAX_ERROR_CODE; 

	
	/**
	 * ImAjaxへエラーメッセージを通知するためのレスポンス・ヘッダ名。
	 * @scope public
	 */
	public static final String RESPONSE_HEADER_NAME_IM_AJAX_ERROR_MESSAGE = ImAjaxUtil.RESPONSE_HEADER_NAME_IM_AJAX_ERROR_MESSAGE; 

	
	/**
	 * レスポンスヘッダにエラーコードとエラーメッセージを設定します。<br/>
	 * 現在の実行スレッドで有効なロケール（＝{@link LocaleHandler#getLocale()}）でメッセージを設定します。<br/>
	 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
	 * <br/>
	 * 設定される、レスポンスヘッダのフォーマットは以下の通りです。
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
	 * 			<a href="im_ajax_util.html#RESPONSE_HEADER_NAME_IM_AJAX_ERROR_CODE">
	 * 				x-org-intra-mart-ajax-error-code
	 * 			</a>
	 * 		</td>
	 * 		<td>
	 * 			エラーコード
	 * 		</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>
	 * 			<a href="im_ajax_util.html#RESPONSE_HEADER_NAME_IM_AJAX_ERROR_MESSAGE">
	 * 				x-org-intra-mart-ajax-error-message
	 * 			</a>
	 * 		</td>
	 * 		<td>
	 * 			エラーコードに紐づくエラーメッセージ。<br>
	 * 		</td>
	 * 	</tr>
	 * </table>
	 * <br/>
	 * エラーメッセージは、JavaScript(＝EcmaScript) のencodeURIComponent()を利用して、
	 * 自動的にエンコードされます。<br/>
	 * 
	 * @scope public
	 * @param errorCode String エラーコード
     * @param ... ?String メッセージ引数（複数設定可能） 
	 */
    public static void jsStaticFunction_setErrorResponseHeaders(Context cx, Scriptable thisObj, Object[] args, Function funObj){
    	
        // 引数が0個 : なにも行わない
        if(args.length == 0){
        	return;
        }
        
        String errorCode = args[0].toString();
        
        if (args.length == 1) {
        	ImAjaxUtil.setErrorResponseHeaders(errorCode);
        }
        else {
            String[] messageArgs = new String[args.length - 1];
            
            for (int i = 1; i < args.length; i++) {
                messageArgs[i - 1] = args[i].toString();
            }
            
        	ImAjaxUtil.setErrorResponseHeaders(errorCode, messageArgs);
        }
    	
    }

}
