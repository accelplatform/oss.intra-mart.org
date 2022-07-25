package org.intra_mart.jssp.view.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * サーバサイドの処理結果を非同期で受け取りたい場合は、属性 callback を指定します。<br/>
 * 属性 callback には、CSJS関数名を指定します。
 * 属性 callback が指定されている場合、サーバサイドの処理結果が 属性 callback で指定されたCSJS関数の引数に渡されます。<br/>
 * <br/>
 * 属性 onErrorCallback には、非同期通信時にエラーが発生した場合に呼び出される関数名を指定します。<br/>
 * 属性 onErrorCallback で指定された関数の引数には、
 * <a href="#JsspRpcComErrorObject">JSSP-RPC 通信エラーオブジェクト</a>が渡されます。<br/>
 * 
 * @version 1.0
 */
public class ImartTag4JsspRpc implements ImartTagType {

	private static final String KEY_JSSPRPC_TAG_ALREADY_USE_FLG = "org.intra_mart.jssp.view.tag.ImartTag4JsspRpc.jsspRpcTagAlreadyUseFlg";

	private static String _jsspRpcSuffix          = null;
	private static String _csjsPath4imJson        = null;
	private static String _csjsPath4imAjaxRequest = null;
	private static String _csjsPath4imJsspRpc     = null;

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
		
		_jsspRpcSuffix          = jsspRpcConfigHandler.getJsspRpcURISuffix(); //".jssprpc";				
		_csjsPath4imJson        = jsspRpcConfigHandler.getCsjsPath4ImJson();
		_csjsPath4imAjaxRequest = jsspRpcConfigHandler.getCsjsPath4ImAjaxRequest();
		_csjsPath4imJsspRpc     = jsspRpcConfigHandler.getCsjsPath4ImJsspRpc();
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

		// エラー発生時のコールバック関数名を取得
		String onErrorCallback = getAttributeValue(oAttr, "onErrorCallback", false);

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
		
		// JsspRpcタグの一回目の出現
		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		HttpServletRequest request = httpContext.getRequest();
		Object tag1stUseFlg = request.getAttribute(KEY_JSSPRPC_TAG_ALREADY_USE_FLG);
		if(tag1stUseFlg == null){
			request.setAttribute(KEY_JSSPRPC_TAG_ALREADY_USE_FLG, Boolean.valueOf(true));
			
			// 絶対パス指定の場合 (=パスが "/" で始まる場合は、現在のコンテキストのルートに対する相対パスであると解釈)
			if("/".equals(_csjsPath4imJson.substring(0, 1))){
				buf.append("<script language=\"javascript\" src=\"" + contextPath + _csjsPath4imJson        + "\"></script>\n");
				buf.append("<script language=\"javascript\" src=\"" + contextPath + _csjsPath4imAjaxRequest + "\"></script>\n");
				buf.append("<script language=\"javascript\" src=\"" + contextPath + _csjsPath4imJsspRpc     + "\"></script>\n");
			}
			// 相対パス指定の場合
			else{
				buf.append("<script language=\"javascript\" src=\"" + _csjsPath4imJson        + "\"></script>\n");
				buf.append("<script language=\"javascript\" src=\"" + _csjsPath4imAjaxRequest + "\"></script>\n");
				buf.append("<script language=\"javascript\" src=\"" + _csjsPath4imJsspRpc     + "\"></script>\n");
			}
		}

		buf.append("<script language=\"javascript\">\n");
		buf.append("    var " + objName + " = new Object();\n");	
		
		// 対象ページ内の関数を呼び出すためのCSJSソースを作成
		for(String functionName : functionNames){
						
			JSSPQuery jsspQuery = JSSPQueryManager.createJSSPQuery();
			jsspQuery.setNextEventPagePath(pagePath);
			jsspQuery.setNextEventName(functionName);
			jsspQuery.setFromPagePath(currentPath);
			jsspQuery.setUriSuffix(_jsspRpcSuffix);			
			
			// URLを生成
			String url = contextPath + "/" + jsspQuery.createJSSPQueryString();
			
			// セッションIDを含めてエンコード
			HttpServletResponse response = httpContext.getResponse();
			url = response.encodeURL(url);

			String arguments = objName + "." + functionName + ".arguments";
			
			buf.append("    " + objName + "[\"" + functionName + "\"] = ");
			buf.append("function(){" );
			buf.append(		"var argsArray = new Array();"); 
			buf.append(		"for(var idx = 0, max = " + arguments + ".length; idx < max; idx++){"); 
			buf.append(			"argsArray[idx] = " + arguments + "[idx]"); 
			buf.append(		"}"); 
			buf.append(		"return ImJsspRpc.sendJsspRpcRequest("); 
			buf.append(					"\"" + url + "\", " );
			buf.append(					"argsArray, " );
			buf.append(					callback + ", " );
			buf.append(					onErrorCallback + ", " );
			buf.append(					"\"post\" " );
			buf.append(				");" );
			buf.append(		"};\n");
		}
		
		buf.append("</script>");

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
