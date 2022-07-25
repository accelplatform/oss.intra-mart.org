package org.intra_mart.jssp.page.provider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.exception.JSSPTransitionalException;
import org.intra_mart.jssp.exception.JavaScriptBatonPassException;
import org.intra_mart.jssp.exception.JavaScriptDebugBrowseException;
import org.intra_mart.jssp.exception.JavaScriptRedirectException;
import org.intra_mart.jssp.exception.JavaScriptSendResponseException;
import org.intra_mart.jssp.exception.JavaScriptTransmissionException;
import org.intra_mart.jssp.page.Page;
import org.intra_mart.jssp.page.JSSPQuery;
import org.intra_mart.jssp.page.JSSPQueryManager;
import org.intra_mart.jssp.page.RequestObject;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.BASE64;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;

public class JSSPPageBuilderImpl implements JSSPPageBuilder {

	private static ScriptScope requestProcess = null;		// request_process.js

	private static Object lockMonitor = new Object();
	
	private HttpServletRequest request; 
	private HttpServletResponse response;
	private JSSPQuery jsspQuery;

	/**
	 * @deprecated 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	public JSSPPageBuilderImpl(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		if(requestProcess == null){
			synchronized(lockMonitor){
				if(requestProcess == null){
					createRequestProcess();
				}
			}
		}
		
		this.request = request;
		this.response = response;

		// サブミットボタンによるページ遷移指定の補正
		String jsspQueryString = null;
		Enumeration enumeration = request.getParameterNames();
		while(enumeration.hasMoreElements()){			
			String name = (String) enumeration.nextElement();
			if(name.startsWith("imsubmit_")){
				
				// TODO [OSS-JSSP] セッション存在有無確認
//				HttpSession httpSession = request.getSession(false);				
//				if(httpSession == null){
//					throw new IllegalStateException("Session is Empty: query=" + name);
//				}

				// サブミットボタンによる指定を発見
				jsspQueryString = new String(BASE64.decode(name.substring("imsubmit_".length())));
				break;
			}
		}

		this.jsspQuery = JSSPQueryManager.createJSSPQuery(request, response);
		
		// サブミットボタンによるページ遷移情報に置き換え
		if(jsspQueryString != null){
			JSSPQuery jsspQueryBySubmit = JSSPQueryManager.createJSSPQuery(jsspQueryString);			
			this.jsspQuery.replaceJSSPQuery(jsspQueryBySubmit);
		}
		
	}

	/**
	 * @throws ServletException
	 */
	private void createRequestProcess() throws ServletException{

		// request_process.js 読込
		try {
			String requestProcessPath = 
					JSSPConfigHandlerManager.getConfigHandler().getRequestProcessScript();
			
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			requestProcess = builder.getScriptScope(requestProcessPath);
		}
		catch (FileNotFoundException e) {
			/* Do Noting */
		}
		catch (Exception e) {
			throw new ServletException(e);
		}

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
		
		// JS実行環境コンテキストの作成
		Context cx = Context.enter();

		// request オブジェクトの作成
		Object[] args = { new RequestObject(this.request) };
		
		// ベースとなる変数スコープ作成
		ScriptScope baseScriptScope = 
			new ScriptScope(Thread.currentThread().getName().concat(String.valueOf(System.currentTimeMillis()))); 

		// 実行！
		try {
			JSSPQueryManager.entry(this.jsspQuery);

			try{
				// before()...
				before(cx, baseScriptScope, args);

				// ページ遷移前イベント
				action(cx, baseScriptScope, args);
			
				// 実行
				sendPage(cx, baseScriptScope, args);
			}
			catch(WrappedException we){
				analyzeWrappedException(cx, we, baseScriptScope);
			}
			catch(JavaScriptException jse){
				analyzeJavaScriptException(cx, jse, baseScriptScope);
			}
		}
		finally{
			try{
				// after()...
				after(cx, baseScriptScope, args);
			}
			finally{			
				JSSPQueryManager.releaseJSSPQuery();
				Context.exit();
			}
		}
		
	}
	
	/**
	 * @param cx
	 * @param baseScriptScope
	 * @param args
	 */
	private void before(Context cx, ScriptScope baseScriptScope, Object[] args){
		// セッション開始時初期化関数の起動
		if(requestProcess != null){
			synchronized(requestProcess){
				String initialFunctionName = 
							JSSPConfigHandlerManager.getConfigHandler().getInitialFunctionName();
				
				if(ScriptableObject.hasProperty(requestProcess, initialFunctionName)){
					requestProcess.call(cx, initialFunctionName, args, baseScriptScope);
				}				
			}
		}	
	}
	
	/**
	 * @param cx
	 * @param baseScriptScope
	 * @param args
	 */
	private void after(Context cx, ScriptScope baseScriptScope, Object[] args){
		// セッション開始時初期化関数の起動
		if(requestProcess != null){
			synchronized(requestProcess){
				String finallyFunctionName = 
							JSSPConfigHandlerManager.getConfigHandler().getFinallyFunctionName();
				
				if(ScriptableObject.hasProperty(requestProcess, finallyFunctionName)){
					requestProcess.call(cx, finallyFunctionName, args, baseScriptScope);
				}
			}
		}
	}
	
	/**
	 * @param cx
	 * @param baseScriptScope
	 * @param args
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void action(Context cx, ScriptScope baseScriptScope, Object[] args) 
					throws JavaScriptException,
							FileNotFoundException,
							InstantiationException,
							IllegalAccessException,
							IOException,
							ClassNotFoundException {
		// アクション関数の動作
		if(jsspQuery.getActionEventName() != null){
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			ScriptScope scriptScope = builder.getScriptScope(jsspQuery.getActionEventPagePath());
			scriptScope.call(cx, jsspQuery.getActionEventName(), args, baseScriptScope);
		}
	}


	/**
	 * @param cx
	 * @param scriptable
	 * @param args
	 * @throws Exception
	 */
	private void sendPage(Context cx, Scriptable scriptable , Object[] args) throws Exception {		
		
		try{
			JSSPQuery currentJSSPQuery = JSSPQueryManager.currentJSSPQuery();
			String nextPagePath = currentJSSPQuery.getNextEventPagePath();
			String functionName = currentJSSPQuery.getNextEventName();

			Page page = new Page(nextPagePath, functionName);
			String result = page.execute(cx, args);
			
			Writer out = this.response.getWriter();
			out.write(result);
			out.flush();
			out.close();
		}
		catch(WrappedException we){
			analyzeWrappedException(cx, we, scriptable);
		}
		catch(JavaScriptException jse){
			analyzeJavaScriptException(cx, jse, scriptable);
		}
		
	}

	/**
	 * WrappedException 解析ロジック
	 * 
	 * @param cx
	 * @param we
	 * @param scope
	 * @throws Exception
	 */
	private void analyzeWrappedException(Context cx, WrappedException we, Scriptable scope) throws Exception{
		Throwable factor = we.getWrappedException();

		if(factor instanceof JSSPTransitionalException){

			// JavaScript 実行環境中の redirect() 関数の呼び出し
			if(factor instanceof JavaScriptRedirectException){
				sendRedirect(((JavaScriptRedirectException) factor).newLocation());
			}
			
			// JavaScript 実行環境中のPRGへのリダイレクト
			else if(factor instanceof JavaScriptBatonPassException){
				JavaScriptBatonPassException jsbpe = (JavaScriptBatonPassException) factor;
				
				// 要求ページパスの更新
				JSSPQuery old = JSSPQueryManager.currentJSSPQuery();
				JSSPQuery newJSSPQuery = 
							JSSPQueryManager.createJSSPQuery(jsbpe.getPath(), old.getFromPagePath());
				JSSPQueryManager.entry(newJSSPQuery);

				// ページ処理の再帰処理
				Object args[] = { jsbpe.getArgument() };
				sendPage(cx, scope, args);

			}
			else if(factor instanceof JavaScriptSendResponseException){
				JavaScriptSendResponseException jssre = (JavaScriptSendResponseException) factor;
				jssre.send(this.response);
			}
			else if(factor instanceof JavaScriptTransmissionException){
				JavaScriptTransmissionException jste = (JavaScriptTransmissionException) factor;
				jste.send(this.response);
			}
			
			// デバッグ用ＨＴＭＬソースの取得
			else if(factor instanceof JavaScriptDebugBrowseException){
				String src = ((JavaScriptDebugBrowseException) factor).getSource();
				HttpServletResponse response = this.response;
				response.reset();
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = this.response.getWriter();
				out.write(src);
				out.flush();
				out.close();
			}
		}
		else{
			throw we;
		}
	}

	/**
	 * JavaScriptException 解析ロジック
	 * 
	 * @param cx
	 * @param jse
	 * @param scope
	 * @throws Exception
	 */
	private void analyzeJavaScriptException(Context cx, JavaScriptException jse, Scriptable scope) throws Exception {
		Throwable cause;

		if(jse.getValue() instanceof Throwable){
			cause = (Throwable) jse.getValue();
		}
		else if(jse.getCause() != null){
			cause = jse.getCause();
		}
		else{
			throw jse;			
		}
		
		if(cause instanceof WrappedException){
			analyzeWrappedException(cx, (WrappedException) cause, scope);
		}
		else{
			analyzeWrappedException(cx, new WrappedException(cause), scope);				
		}
	}

	/**
	 * 指定ＵＲＬをリダイレクトします。
	 * @param url
	 * @throws IOException
	 */
	private void sendRedirect(String url) throws IOException{
		HttpServletResponse response = this.response;
		response.reset();
		response.sendRedirect(url);
	}

}
