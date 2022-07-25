package org.intra_mart.jssp.page;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import org.intra_mart.jssp.exception.JavaScriptRedirectException;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.intra_mart.jssp.view.ViewScope;
import org.intra_mart.jssp.view.provider.ViewScopeBuilder;
import org.intra_mart.jssp.view.provider.ViewScopeBuilderManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.ScriptableObject;

/**
 * ScriptScope(ex .jsファイル)とViewScope(ex .htmlファイル)を実行し、ページを作成します。
 */
public class Page implements Serializable {

	private String sourcePath;					// ソースパス
	private String functionName;				// 関数名
	private ScriptScope scriptScope = null;	// JavaScript ソース
	private ViewScope viewScope = null;		// view ソース

	
	/**
	 * 引数にはソースパスを指定します。<br/>
	 * ソースパスは、ソース検索のルートディレクトリからの相対パスで、
	 * ディレクトリ名およびファイル名の区切りを「/」で区切った形式を指定します。（拡張子は省略）
	 * 
	 * @param path ソースパス
	 */
	public Page(String path){
		this.sourcePath = path;
		this.functionName = JSSPConfigHandlerManager.getConfigHandler().getInitialFunctionName();;
	}

	
	/**
	 * 引数にはソースパス、および、実行関数名を指定します。<br/>
	 * ソースパスは、ソース検索のルートディレクトリからの相対パスで、
	 * ディレクトリ名およびファイル名の区切りを「/」で区切った形式を指定します。（拡張子は省略）
	 * 
	 * @param path ソースパス
	 * @param functionName 関数名
	 */
	public Page(String path, String functionName){
		this.sourcePath = path;

		if(functionName == null || functionName.length() == 0){			
			this.functionName = JSSPConfigHandlerManager.getConfigHandler().getInitialFunctionName();;
		}
		else{
			this.functionName = functionName;
			
		}
	}

	
	/**
	 * 実行
	 * 
	 * @param cx 実行環境
	 * @param args 関数の実行時引数
	 * @return 生成されたＨＴＭＬソース
	 * 
	 * @throws JavaScriptException
	 * @throws JavaScriptRedirectException
	 * @throws FileNotFoundException
	 */
	public String execute(Context cx, Object[] args) 
			throws JavaScriptException, JavaScriptRedirectException, FileNotFoundException{
		
		// ページの初期化関数を実行
		try{
			try{
				ScriptScope scope = this.getScriptScope();
				
				if(ScriptableObject.hasProperty(scope, this.functionName)){
					scope.call(cx, this.functionName, args);
				}
				try{
					return this.createView(cx, scope);
				}
				finally{
					String finallyFunctionName = 
							JSSPConfigHandlerManager.getConfigHandler().getFinallyFunctionName();

					if(ScriptableObject.hasProperty(scope, finallyFunctionName)){
						scope.call(cx, finallyFunctionName, args);
					}
				}
			}
			catch(FileNotFoundException fnfe){
				// JavaScript は存在しないらしい
				return this.createView(cx, new ScriptScope());
			}
		}
		catch(InstantiationException ie){
			IllegalStateException ise = new IllegalStateException("JSSP execute error.");
			ise.initCause(ie);
			throw ise;
		}
		catch(IllegalAccessException iae){
			IllegalStateException ise = new IllegalStateException("JSSP execute error.");
			ise.initCause(iae);
			throw ise;
		}
		catch(IOException ioe){
			IllegalStateException ise = new IllegalStateException("JSSP execute error.");
			ise.initCause(ioe);
			throw ise;
		}
		catch(ClassNotFoundException cnfe){
			FileNotFoundException fnfe = new FileNotFoundException("Source not found: " + this.getSourcePath());
			fnfe.initCause(cnfe);
			throw fnfe;
		}
	}

	
	/**
	 * ViewScopeを実行します。
	 * 
	 * @return 作成されたＨＴＭＬソース
	 * 
	 * @throws FileNotFoundException
	 * @throws JavaScriptException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private String createView(Context cx, ScriptScope scope) 
						throws FileNotFoundException,
								JavaScriptException,
								InstantiationException,
								IllegalAccessException,
								IOException,
								ClassNotFoundException {
		
		// view を取得
		ViewScope viewScope = this.getViewScope();

		// view の実行
		ScriptScope before = ScriptScope.entry(scope);
		try{
			return viewScope.execute(cx, scope);
		}
		finally{
			ScriptScope.entry(before);
		}
	}

	
	/**
	 * 指定の関数を実行します。
	 * 
	 * @param cx 実行環境
	 * @param functionName 関数名
	 * @param args 関数の実行時引数
	 * @return 実行結果
	 * 
	 * @throws JavaScriptException
	 */
	public Object executeFunction(Context cx, String functionName, Object[] args) throws JavaScriptException{
		try {
			// 指定関数の実行
			return this.getScriptScope().call(cx, functionName, args);
		}
		catch (FileNotFoundException fnfe) {
			JavaScriptException jse = 
				new JavaScriptException("JavaScript execute error: " + this.getSourcePath(), this.getSourcePath(), 0);
			jse.initCause(fnfe);
			throw jse;
		}
		catch (InstantiationException ie) {
			JavaScriptException jse = 
				new JavaScriptException("JavaScript execute error: " + this.getSourcePath(), this.getSourcePath(), 0);
			jse.initCause(ie);
			throw jse;
		}
		catch (IllegalAccessException ie) {
			JavaScriptException jse = 
				new JavaScriptException("JavaScript execute error: " + this.getSourcePath(), this.getSourcePath(), 0);
			jse.initCause(ie);
			throw jse;
		}
		catch (IOException ie) {
			JavaScriptException jse = 
				new JavaScriptException("JavaScript execute error: " + this.getSourcePath(), this.getSourcePath(), 0);
			jse.initCause(ie);
			throw jse;
		}
		catch (ClassNotFoundException cnfe) {
			JavaScriptException jse = 
				new JavaScriptException("JavaScript execute error: " + this.getSourcePath(), this.getSourcePath(), 0);
			jse.initCause(cnfe);
			throw jse;
		}
	}

	
	/**
	 * ソースパスを取得します。
	 * @return
	 */
	public String getSourcePath(){
		return this.sourcePath;
	}


	/**
	 * JavaScript 実行可能オブジェクトを返却します。
	 * 
	 * @return JavaScript 実行可能オブジェクト
	 * 
	 * @throws FileNotFoundException
	 * @throws JavaScriptException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private ScriptScope getScriptScope() 
						throws FileNotFoundException,
								JavaScriptException,
								InstantiationException,
								IllegalAccessException,
								IOException,
								ClassNotFoundException {
		if(this.scriptScope == null){
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			this.scriptScope = builder.getScriptScope(this.getSourcePath());
		}
		return this.scriptScope;
	}

	
	/**
	 * ViewScopeを返却します。
	 * 
	 * @return ViewScope
	 * 
	 * @throws FileNotFoundException
	 * @throws JavaScriptException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private ViewScope getViewScope() 
						throws FileNotFoundException,
								JavaScriptException,
								InstantiationException,
								IllegalAccessException,
								IOException,
								ClassNotFoundException {
		if(this.viewScope == null){
			// view のソースを取得
			ViewScopeBuilder builder = ViewScopeBuilderManager.getBuilder();
			this.viewScope = builder.getViewScope(this.getSourcePath());
		}
		return this.viewScope;
	}
}