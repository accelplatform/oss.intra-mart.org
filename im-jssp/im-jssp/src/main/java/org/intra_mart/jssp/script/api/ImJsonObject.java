package org.intra_mart.jssp.script.api;

import org.mozilla.javascript.ScriptableObject;

/**
 * JSON 関連のライブラリです。<br/>
 * <br/>
 * JSON (JavaScript Object Notation)は、軽量のデータ交換フォーマットです。<br/>
 * JSON については、<a href="http://www.json.org/json-ja.html">JSON の紹介</a> を参照してください。
 * 
 * @scope public
 * @name ImJson
 */
public class ImJsonObject extends ScriptableObject {

	/* (non-Javadoc)
	 * @see jp.co.intra_mart.system.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName() {
		return "ImJson";
	}

    /** 
     * JSON 文字列からJavaScriptオブジェクトへの変換に失敗した場合に投げられる例外のメッセージです。
	 * @scope public
	 */
	public static final String PARSE_JSON_ERROR_MESSAGE = "parseJSON Error";

	/**
	 * インデント文字列
	 * @scope public
	 */
	public static final String INDENT_STRING = "    ";

	/**
	 * 「null」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_NULL = "/* Null */";

	/**
	 * 「Undefined」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_UNDEFINED = "/* Undefined */";

	/**
	 * 「String」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_STRING = "/* String */";

	/**
	 * 「Date」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_DATE = "/* Date */";

	/**
	 * 「Array」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_ARRAY = "/* Array */";

	/**
	 * 「Object」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_OBJECT = "/* Object */";

	/**
	 * 「Function」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_FUNCTION = "/* Function */";

	/**
	 * 「Number」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_NUMBER = "/* Number */";

	/**
	 * 「Boolean」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_BOOLEAN = "/* Boolean */";

	/**
	 * 「XML」型 を表す定数
	 * @scope public
	 */
	public static final String TYPE_XML = "/* XML */";

	/**
	 * 型が特定できない場合を表す定数
	 * @scope public
	 */
	public static final String TYPE_UNKNOWN = "/* Unknown */";

	/**
	 * 「Java」型を表す定数
	 * @scope public
	 */
	public static final String TYPE_JAVA = "/* Java */";

	/**
     * JSON 文字列の妥当性をチェックします。<br/>
     * <br/>
     * 
     * @scope public
     * @param jsonString String JSON 文字列
     * @return Boolean 正当なJSON 文字列の場合は true、不正なJSON 文字列の場合は false を返却します。
     */
    public static Boolean jsStaticFunction_checkJSONString(String jsonString) {
    	return null;
    }
	
    /**
     * JSON 文字列からJavaScriptオブジェクトに変換します。<br/>
     * <br/>
     * 変換に失敗した場合、SyntaxError が投げられます。<br/>
     * SyntaxError のメッセージは、{@link ImJson#PARSE_JSON_ERROR_MESSAGE} が設定されます。
     *  
     * @scope public
     * @param jsonString String JSON 文字列
     * @return Object JavaScriptオブジェクト
     */
    public static Object jsStaticFunction_parseJSON(String jsonString) {
    	return null;
    }
	
    /**
     * JSON 文字列に変換します。<br/>
     * <br/>
     * 引数「debugFlg」が true の場合、JSON 文字列のインデント化、および、型名の付与を行います。<br/>
     * その際、変換対象オブジェクト（内部のプロパティも含む）が 以下の型の場合、特別な動作をします。<br/>
     * <br/>
     * <table border="1">
     * 	<tr>
     * 		<td bgcolor="lightgrey">Date 型</td>
     * 		<td>型名の右側に、日付の文字列表現がJavaScriptのコメントとして出力されます。</td>
     * 	</tr>
     * 	<tr>
     * 		<td bgcolor="lightgrey">Function 型</td>
     * 		<td><b>"THIS_IS_FUNCTION"</b> として表現します。</td>
     * 	</tr>
     * </table>
     * <br/>
     * なお、引数「debugFlg」が true 時の本メソッドの返却値には型名が付与されているため、<br/>
     * <a href="#checkJSONStringString">checkJSONString() </a>でのチェックには失敗します。<br/>
     * 
     * 
     * @scope public
     * @param value Object 変換対象オブジェクト
     * @param debugFlg ?Boolean JSON 文字列のインデント化、および、型名の付与を行う場合は trueを設定してください。<br/> 
     * 							 省略時のデフォルトは false。
     * @return String JSON 文字列
     */
    public static String jsStaticFunction_toJSONString(Object value, Boolean debugFlg) {
    	return null;
    }

}
