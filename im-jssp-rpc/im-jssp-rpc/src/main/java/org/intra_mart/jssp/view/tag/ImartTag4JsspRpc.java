package org.intra_mart.jssp.view.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.jssp.page.JSSPQuery;
import org.intra_mart.jssp.page.JSSPQueryManager;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.intra_mart.jssp.util.config.JsspRpcConfigHandler;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="jsspRpc"&gt; タグ。<br/>
 * <br/>
 * このタグを利用すると、JavaScriptで記述されたサーバサイドのロジックを、
 * クライアントサイドJavaScript（以下 CSJS）からシームレスに呼び出すことが可能となります。<br/>
 * <br/>
 * 属性 name には、CSJS内でサーバロジックを参照する際のオブジェクト名を指定します。<br/>
 * <br/>
 * 属性 page には、サーバロジックが記述されているページパス（拡張子なし）を指定します。<br/>
 * ここで指定するパスは、JSSPコンテンツのソースディレクトリからの相対パス形式になります。（デフォルトはコンテキストパスに対応する実際のパス）<br/>
 * <br/>
 * サーバ側の処理結果を非同期で受け取りたい場合は、属性 callback を指定します。<br/>
 * 属性 callback には、CSJS関数名を指定します。
 * 属性 callback が指定されている場合、サーバ側の処理結果が 属性 callback で指定されたCSJS関数の引数に渡されます。
 * <br/>
 * <br/>
 * <strong><i>例：</i></strong>
 * <blockquote>
 * サーバサイドに「<b><font color="green">sample/test1</font></b>.js」が存在し、
 * そのJSファイル内に「<b><font color="red">testFunction</font></b>()」という関数が定義されている場合、
 * 以下を行うことによって、CSJSから上記関数を実行することができます。
 * <ol>
 * 	<li>
 * 		HTMLファイルの &lt;HEAD&gt; タグ内に以下のCSJSライブラリを追加します。<br/>
 * 	</li>
 * 	<pre>
		&lt;script language="JavaScript" src="csjs/im_jssp_rpc.js"&gt;&lt;/script&gt;
		&lt;script language="JavaScript" src="csjs/json.js"&gt;&lt;/script&gt;
 * 	</pre>
 * 
 * 	<li>HTMLファイル内に&lt;IMART type="jsspRpc"&gt; タグを以下のように記述します。</li>
 * <pre>
		&lt;IMART type="jsspRpc" name="<b><font color="blue">serverLogic</font></b>" page="<b><font color="green">sample/test1</font></b>" &gt;
 * </pre>
 * 
 * 	<li>CSJS内に以下を記述することで、サーバサイドのロジックを実行します。</li>
 * 	<pre><b><font color="blue">serverLogic</font></b>.<b><font color="red">testFunction</font></b>();</pre>
 * </ol>
 * </blockquote>
 * 
 * <strong><i>注意事項：</i></strong>
 * <blockquote>
 * <ul>
 * 	<li>
 * 		HTMLファイルの &lt;HEAD&gt; タグ内に、必ず以下のCSJSライブラリを追加してください。<br/>
 * 	</li>
 * 		<pre>
			&lt;script language="JavaScript" src="csjs/im_jssp_rpc.js"&gt;&lt;/script&gt;
			&lt;script language="JavaScript" src="csjs/json.js"&gt;&lt;/script&gt;
 * 		</pre>
 * 		（なお、im_jssp_rpc.js 、および、json.js は、URLの関係で呼び出し時のソースパスは変わります。<br/>
 * 	　	&lt;IMART type="link"&gt;や&lt;IMART type="form"&gt;タグで画面遷移を行った場合は、
 * 		csjs/im_jssp_rpc.js、および、csjs/json.js といった相対パス指定でリンクできます）
 * 	<br/>
 * 	<br/>
 * 	<li>
 * 		&lt;IMART type="jsspRpc"&gt; タグでは、
 * 		ブラウザとサーバ間の通信に <a href="http://ja.wikipedia.org/wiki/XMLHttpRequest">XMLHttpRequest</a> を利用しています。
 * 	</li>
 * 	<li>
 * 		ブラウザとサーバ間通信のデータ形式は <a href="http://ja.wikipedia.org/wiki/JSON">JSON</a> です。
 *	</li>
 * 	<li>
 * 		JSONデータとJavaScriptオブジェクトの変換には、
 * 		<a href="http://www.json.org/js.html">json.js</a> を改良したライブラリを使用しています。
 * 	</li>
 * 	<li>
 * 		JSONデータとJavaScriptオブジェクトの変換を行うサーバサイドロジックのデフォルトは、jssp_rpc/common/json.js です。<br/>
 * 		クライアントサイドロジックのデフォルトは、csjs/json.js です。<br/>
 * 	</li>
 * 	<li>
 * 		json.js内でArray、Boolean、Date、Number、Object、Stringに対してtoJSONString()関数を
 * 		プロトタイプオブジェクトとして設定しています。<br/>
 * 		その為、for/in ループ文で取得可能なオブジェクト要素一覧に toJSONString() 関数が列挙されます。<br/>
 * 		取得した要素が自オブジェクトのプロパティかどうかを、hasOwnProperty() 関数等を利用し、
 * 		必要に応じて判定してください。<br/>
 * 	</li>
 * </ul>
 * </blockquote>
 */
public class ImartTag4JsspRpc implements ImartTagType {

	private static String _jsspRpcSuffix = null;
	private static String _functionName4MarshallArguments = null;
	private static String _functionName4Unmarshall = null;

	static {

		JsspRpcConfigHandler jsspRpcConfigHandler;
		try{
			JSSPConfigHandler configHandler = JSSPConfigHandlerManager.getConfigHandler();
			jsspRpcConfigHandler = (JsspRpcConfigHandler)configHandler;
		}
		catch(ClassCastException e){
			// JSSP-RPC環境では、JsspRpcConfigHandlerが設定されている必要があります。
			String errorMessage = "In the JSSP-RPC environment, it is necessary to set JsspRpcConfigHandler.";
			throw new IllegalStateException(errorMessage, e);
		}
		
		_jsspRpcSuffix = jsspRpcConfigHandler.getJsspRpcURISuffix();		//".jssprpc";		
		_functionName4MarshallArguments = jsspRpcConfigHandler.getMarshall4ArgumentsFunctionName(); //"marshall4Arguments";
		_functionName4Unmarshall = jsspRpcConfigHandler.getUnmarshallFunctionName(); 	//"unmarshall";
		
	}
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "jsspRpc";
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#doTag(org.mozilla.javascript.Scriptable, org.mozilla.javascript.Scriptable)
	 */
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		
		// CSJS内のアクセス用JavaScriptオブジェクト名を取得
		String objName = getAttributeValue(oAttr, "name", true);

		// 対象ページパスを取得
		String pagePath = getAttributeValue(oAttr, "page", true);

		// コールバック関数名を取得
		String callback = getAttributeValue(oAttr, "callback", false);
		
		// 対象ページの関数名一覧を取得
		List<String> functionNames = getFunctionNames(pagePath);

		// コンテキストパスを取得
		String contextPath = getContextPath();
		
		// 現在処理中のページパスを取得
		String currentPath = null;
		JSSPQuery current = JSSPQueryManager.currentJSSPQuery();
		if(current != null){
			currentPath = current.getNextEventPagePath();
		}

		// ソース作成
		StringBuffer buf = new StringBuffer();		
		buf.append("<SCRIPT language=\"JavaScript\">\n");
		buf.append("    var " + objName + " = new Object();\n");	
		
		// 対象ページ内の関数を呼び出すためのCSJSソースを作成
		// 【注意】「csjs/im_jssp_rpc.js」「csjs/json.js」を HTMLファイルから参照可能にする必要があります。
		// 　　　　（∵JavaScript関数「sendJsspRpcRequest()」「marshall4Arguments()」を利用するため）
		for(String functionName : functionNames){
						
			JSSPQuery jsspQuery = JSSPQueryManager.createJSSPQuery();
			jsspQuery.setNextEventPagePath(pagePath);
			jsspQuery.setNextEventName(functionName);
			jsspQuery.setFromPagePath(currentPath);
			jsspQuery.setUriSuffix(_jsspRpcSuffix);			
			
			// URLを生成
			String url = contextPath + "/" + jsspQuery.createJSSPQueryString();
			
			buf.append("    " + objName + "[\"" + functionName + "\"] = ");
			buf.append("function(){" );
			buf.append(		"return sendJsspRpcRequest("); 
			buf.append(					"\"" + url + "\", " ); 
			buf.append(					_functionName4MarshallArguments + "(" + objName + "." + functionName + ".arguments), " );
			buf.append(					callback + ", " );
			buf.append(					"\"post\", " );
			buf.append(					_functionName4Unmarshall );			
			buf.append(				");" );
			buf.append(		"};\n");
		}
		
		buf.append("</SCRIPT>");

		return buf.toString();

	}

	/**
	 * コンテキストパスを取得します。
	 * @return コンテキストパス
	 */
	private String getContextPath() {
		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		HttpServletRequest request = httpContext.getRequest();
		String contextPath = request.getContextPath();
		return contextPath;
	}

	
	/**
	 * 対象ページの関数名一覧を取得
	 * @param pagePath ページパス
	 * @return 引数pagePathで指定されたJSソースで定義されている全ての関数名
	 */
	private List<String> getFunctionNames(String pagePath) {

		List<String> functionNames = new ArrayList<String>();

		// 対象ページのScripScopeを取得
		ScriptScope scriptScope4TargetPage;
		try {
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			scriptScope4TargetPage = builder.getScriptScope(pagePath);
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}

		// 関数名一覧を取得
		Object[] ids = scriptScope4TargetPage.getAllIds();
		for(Object id : ids){
			if(id instanceof String){
				Object func = scriptScope4TargetPage.get((String) id, scriptScope4TargetPage);
				
				if(func instanceof Function){
					functionNames.add((String) id);
				}
			}
		}
		
		// 関数が存在しないページの場合例外を投げる
		if(functionNames.size() == 0){
			throw new IllegalStateException(pagePath + " has no function.");
		}
		
		return functionNames;
	}

	
	/**
	 * 属性値を取得します。
	 * @param oAttr タグの属性
	 * @param key 属性名
	 * @param required true：属性は必須、false：属性は必須ではない
	 * @return 属性値
	 */
	private String getAttributeValue(Scriptable oAttr, String key, boolean required){

		if(oAttr.has(key, null)){
			return ScriptRuntime.toString(oAttr.get(key, null));
		}
		
		if(required){			
			throw new IllegalArgumentException("\"" + key + "\" attribute must be specified.");
		}
		else {
			return null;
		}

	}
}
