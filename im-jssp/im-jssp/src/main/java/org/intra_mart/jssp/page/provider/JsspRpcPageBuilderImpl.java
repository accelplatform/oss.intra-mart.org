package org.intra_mart.jssp.page.provider;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.rmi.UnmarshalException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.exception.JavaScriptDebugBrowseException;
import org.intra_mart.jssp.page.JSSPQuery;
import org.intra_mart.jssp.page.JSSPQueryManager;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.ImAjaxUtil;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.intra_mart.jssp.util.config.JsspRpcConfigHandler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.WrappedException;

/**
 * 要求されたファンクションコンテナ内に定義された関数を実行し、
 * 実行結果(=JSオブジェクト)をJSON形式の文字列に変換して返却します。
 */
public class JsspRpcPageBuilderImpl implements JSSPPageBuilder {

	private static ScriptScope _marshallerScriptScope = null;
	private static String _marshallerPagePath = null;
	private static String _functionName4Marshall = null;
	private static String _functionName4Unmarshall = null;
	private static boolean _isThrowUnmarshallException = true;
	private static boolean _isCacheMarshallerScriptScope = true;
	
	private HttpServletRequest request; 
	private HttpServletResponse response;
	private JSSPQuery jsspQuery;

    /**
	 * Static変数の初期化を行います。
	 */
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

		// マーシャル用スクリプトの設定
		_marshallerPagePath           = jsspRpcConfigHandler.getMarshallerPagePath();
		_functionName4Marshall        = jsspRpcConfigHandler.getMarshallFunctionName();
		_functionName4Unmarshall      = jsspRpcConfigHandler.getUnmarshallFunctionName();
		_isThrowUnmarshallException   = jsspRpcConfigHandler.isThrowUnmarshallException();
		_isCacheMarshallerScriptScope = jsspRpcConfigHandler.isCacheMarshallerScriptScope();
		
		// マーシャル用スクリプトをキャッシュ
		refreshMarshallerScriptScope();	
	}
	
	/**
	 * @deprecated 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	public JsspRpcPageBuilderImpl(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		this.request = request;
		this.response = response;
		this.jsspQuery = JSSPQueryManager.createJSSPQuery(request, response);
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.page.provider.JSSPPageBuilder#verify()
	 */
	public boolean verify() {
		return this.jsspQuery.verify();
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.page.provider.JSSPPageBuilder#invoke()
	 */
	public void invoke() throws Exception {
		
		// 引数（＝JSON形式のPOSTデータ）取得
		CharArrayWriter data = getArgsData();
		String jsonArgs = data.toString().replaceAll("\"", "\\\"");
		
		// 実行するScriptScopeを取得（関数存在チェック含む）
		ScriptScope targetScriptScope = getTargetScriptScope();			

		// アンマーシャル
		Object[] args4Java = unmarshall(jsonArgs);

		String jsonString;
		Context cx = Context.enter();
		try{

			// 実行
			Object returnObj = targetScriptScope.call(cx, this.jsspQuery.getNextEventName(), args4Java);
			
			// マーシャル
			jsonString = marshall(cx, returnObj);

		}
		catch (WrappedException we){

			Throwable factor = we.getCause();

			if(factor instanceof JavaScriptDebugBrowseException){
				// デバッグ用ＨＴＭＬソースの取得
				String src = ((JavaScriptDebugBrowseException) factor).getSource();
				this.response.reset();
				this.response.setContentType("text/html; charset=utf-8");
				
				ImAjaxUtil.setErrorResponseHeaders(this.response, "IM-JSSP-00001");
				
				PrintWriter out = this.response.getWriter();
				out.write(src);
				out.flush();
				out.close();
				return;
			}
			else{
				// 以下の例外を利用した遷移系処理は行わない（つまり、JSSP-RPCでは、以下の関数を利用できません）
				// 　・forward()
				// 　・redirect()
				// 　・secureRedirect()
				// 　・transmission()
				// 　・HTTPResponse#sendMessageBody()
				// 　・            #sendMessageBodyString()
				// （∵JSSP-RPCは、CSJSからSSJSの関数を呼び出す機構なので、遷移処理は必要なし）
				throw we;
			}
		}
		catch (Exception e) {
			ImAjaxUtil.setErrorResponseHeaders(this.response, "IM-JSSP-99999");
			throw e;
		}
		finally{
			Context.exit();
		}
		
		// 実行結果（＝JSON形式）を返却
		this.response.setContentType("application/json; charset=utf-8");
		Writer out = this.response.getWriter();
		out.write(jsonString);
		out.flush();
		out.close();
		
	}


	/**
	 * 引数（＝JSON形式のPOSTデータ）取得
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private CharArrayWriter getArgsData() throws UnsupportedEncodingException, IOException {

		final int BUF_SIZE = 4096;
		
		String charset = this.request.getCharacterEncoding();
		if(charset == null){
			charset = "UTF-8";
		}

		// 引数（＝JSON形式のPOSTデータ）取得
		BufferedReader in = new BufferedReader(new InputStreamReader(this.request.getInputStream(), charset));
        CharArrayWriter data = new CharArrayWriter();
        char buf[] = new char[BUF_SIZE];
        int ret;
        while((ret = in.read(buf, 0, BUF_SIZE)) != -1){
            data.write(buf, 0, ret);
        }
		return data;
	}

	/**
	 * 実行するScriptScopeを取得（関数存在チェック含む）
	 * @return
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 */
	private ScriptScope getTargetScriptScope() throws FileNotFoundException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException, NoSuchMethodException {

		ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
		ScriptScope scriptScope = builder.getScriptScope(this.jsspQuery.getNextEventPagePath());
		
		// 関数存在チェック
		Object nativeFunction = scriptScope.get(this.jsspQuery.getNextEventName(), null);
		if(nativeFunction == null
		   ||
		   !(nativeFunction instanceof Function)) {
			
			String errMsg = this.jsspQuery.getNextEventPagePath() + "#" + this.jsspQuery.getNextEventName() + "()";
			throw new NoSuchMethodException(errMsg);
			
		}
		return scriptScope;
	}

	/**
	 * @return
	 */
	private ScriptScope getMarshaller() {
		
		if(_isCacheMarshallerScriptScope == false){
			// キャッシュしない場合
			refreshMarshallerScriptScope();
		}

		return _marshallerScriptScope;		
	}

	/**
	 * 
	 */
	private static synchronized void refreshMarshallerScriptScope() {
		try {
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			_marshallerScriptScope = builder.getScriptScope(_marshallerPagePath);
		}
		catch (Exception e) {
			throw new IllegalStateException("[JSSP-RPC] Marshaller page load error: " + _marshallerPagePath, e);
		}
	}

	
	/**
	 * アンマーシャル
	 * @param jsonArgs
	 * @return
	 * @throws UnmarshalException
	 */
	private Object[] unmarshall(String jsonArgs) throws UnmarshalJSONStringException {
		
		Object[] args4Java;

		Context cx = Context.enter();
		
		try {
			// 引数（＝JSON形式のPOSTデータ）をアンマーシャル
			Object[] receiveArgs = { jsonArgs };
			ScriptScope marshaller = getMarshaller();
			
			if(!ScriptableObject.hasProperty(marshaller, _functionName4Unmarshall)){
				throw new IllegalStateException("[JSSP-RPC] Unmarshall function is not defined: " + 
												_marshallerPagePath + "#" + _functionName4Unmarshall);
			}
			
			Object argsInNativeArray = marshaller.call(cx, _functionName4Unmarshall, receiveArgs);
			
			// 「JavaScript配列 (=NativeArray)」で渡された引数を、Object[] 配列に入れ替え
			// （∵クライアントは、引数を「JavaScript配列」に格納して送信するため (可変長引数対応) ）
			if(argsInNativeArray instanceof NativeArray){
				
				args4Java = (Object[]) Context.jsToJava(argsInNativeArray, Object[].class);

				// 文字列 "undefined" を org.mozilla.javascript.Undefined に変換
				for(int idx = 0; idx < args4Java.length; idx++){
					if("undefined".equals(args4Java[idx])){
						args4Java[idx] = Undefined.instance;
					}
				}
			}
			else{
				args4Java = new Object[1];
				args4Java[0] = argsInNativeArray;
			}
		}
		catch (JavaScriptException jse) {
			if(_isThrowUnmarshallException){
				throw new UnmarshalJSONStringException(jse.getMessage(), jse);
			}
			else {
				// 空の引数を渡す
				// ※引数に null を指定してJS関数を実行するとNullPointerExceptionが発生します
				// 　（発生箇所(Rhino 1.6R5) → Interpreter.interpret(Interpreter.java:2248)）
				args4Java = new Object[0];
			}
		}
		finally{
			Context.exit();
		}
		
		return args4Java;
	}


	/**
	 * マーシャル
	 * @param cx
	 * @param returnObj
	 * @return
	 */
	private String marshall(Context cx, Object returnObj) {
		
		String jsonString;

		if(returnObj == null){
			jsonString = "null";
		}
		else {
			Object[] objects = { returnObj };
			ScriptScope marshaller = getMarshaller();
			
			if(!ScriptableObject.hasProperty(marshaller, _functionName4Marshall)){
				throw new IllegalStateException("[JSSP-RPC] Marshall function is not defined: " + 
												_marshallerPagePath + "#" + _functionName4Marshall);
			}
			
			jsonString = (String)marshaller.call(cx, _functionName4Marshall, objects);				
		}
		
		return jsonString;
	}

}
