package org.intra_mart.jssp.page;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.intra_mart.jssp.script.FoundationScriptScope;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.ApplicationInitializer;
import org.intra_mart.jssp.util.SimpleLog;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.intra_mart.jssp.view.tag.ImartObject;
import org.intra_mart.jssp.view.tag.ImartTagType;
import org.intra_mart.jssp.view.tag.ImartTagTypeManger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JSSP実行環境の初期化を行います。
 */
public class JSSPInitializer {

	/**
	 * JSSP実行環境の初期化を行います。
	 * 
	 * @param homeDirecory　ホームディレクトリ
	 * @param configFile　コンフィグファイルのリソース名。例：「/conf/jssp-config.xml」)
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 */
	public static void init(String homeDirecory, String configFile){
		
		// ホームディレクトリの設定
		HomeDirectory.definePath(homeDirecory);
		
		// コンフィグファイルの初期化
		initializeConfigHandler(configFile);
		
		// 実行環境の初期化(コンフィグファイルの初期化後に行う必要有り)
		initializeJSSPRuntimeEnvironment();
	}
	
	
	@SuppressWarnings("deprecation")
	private static void initializeConfigHandler(String configFile) {
		if(configFile != null){
			// 指定されたコンフィグファイルを利用する。
			JSSPConfigHandlerManager.getConfigHandler(configFile);
		}
		else{
			// デフォルトのコンフィグファイルを利用する
			JSSPConfigHandlerManager.getConfigHandler();
		}
	}

	
	/**
	 * 
	 */
	private static void initializeJSSPRuntimeEnvironment() {
		
		try {
			// 基本ＡＰＩの設定
			defineJavaScriptAPI4Class();

			// スクリプトで作られているＡＰＩの設定
			defineJavaScriptAPI4Script();			
			
			// 基本IMARTタグの設定
			defineImartTag4Class();	
			
			// スクリプトで作られているIMARTタグの設定
			defineImartTag4Script();
		}
		catch (Exception e) {
			throw new IllegalStateException("JSSP runtime environment initialize error", e);
		}
		
		
		try{
			// 初期化クラスの実行
			invokeInitializer4Class();
			
			// 初期化スクリプトの実行
			invokeInitializer4Script();			
		}
		catch(Exception e){
			throw new IllegalStateException("Invoke initializer error", e);
		}
	}


	/**
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("deprecation")
	private static void defineJavaScriptAPI4Class() throws ClassNotFoundException {

		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		String[] classNames = config.getJavaScriptAPI4Class();
		
		for(String className : classNames){
			
			Class clazz = findClass(className);

			try{
				// 登録
				FoundationScriptScope.instance().defineJavaScriptAPI(clazz);

				// TODO [OSS-JSSP] ログ機能実装
				Scriptable scriptable = (Scriptable)clazz.newInstance();
				SimpleLog.logp(Level.INFO, "Regist API: " + scriptable.getClassName());
			}
			catch(Exception e){
				throw new IllegalStateException("JavaScript-API initialize error: " + clazz.getName(), e);
			}
		}
	}


	/**
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("deprecation")
	private static void defineJavaScriptAPI4Script() 
					throws JavaScriptException,
							FileNotFoundException, 
							InstantiationException, 
							IllegalAccessException, 
							IOException, 
							ClassNotFoundException {
		
		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		String[] scriptNames = config.getJavaScriptAPI4Script();
		
		for(String scriptName : scriptNames){
			
			StringTokenizer tokenizer = new StringTokenizer(scriptName, "#");

			if(tokenizer.countTokens() != 2){
				throw new IllegalArgumentException("Script name format error: " + scriptName);				
			}

			String path = tokenizer.nextToken();
			String apiName = tokenizer.nextToken();
			
			// 登録
			Function function = getFunction(path, apiName);
			FoundationScriptScope.instance().defineJavaScriptAPI(apiName, function);
			
			// TODO [OSS-JSSP] ログ機能実装
			SimpleLog.logp(Level.INFO, "Regist API: " + apiName);
		}
	}
	
	
	/**
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("deprecation")
	private static void defineImartTag4Class() throws ClassNotFoundException {
		
		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		String[] classNames = config.getJSSPTags4Class();
		
		for(String className : classNames){
			
			Class clazz = findClass(className);
			try{
				// 登録
				ImartTagTypeManger.getInstance().defineImartTag(clazz);

				// TODO [OSS-JSSP] ログ機能実装
				ImartTagType imartTagType = (ImartTagType) clazz.newInstance();
				SimpleLog.logp(Level.INFO, "Regist Tag: <IMART type=\"" + imartTagType.getTagName() + "\">");
			}
			catch(Exception e){				
				throw new IllegalStateException("ImartTag initialize error: " + clazz.getName(), e);
			}	
		}
	}

	
	/**
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void defineImartTag4Script() 
					throws JavaScriptException, 
							FileNotFoundException, 
							InstantiationException, 
							IllegalAccessException, 
							IOException, 
							ClassNotFoundException {
		
		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		String[] scriptNames = config.getJSSPTags4Script();
		
		for(String scriptName : scriptNames){
			
			StringTokenizer tokenizer = new StringTokenizer(scriptName, "#");
			
			if(tokenizer.countTokens() != 2){
				throw new IllegalArgumentException("Script name format error: " + scriptName);				
			}

			String path = tokenizer.nextToken();
			String tagName = tokenizer.nextToken();

			// 登録
			Function function = getFunction(path, tagName);
			ImartObject.jsStaticFunction_defineType(tagName, function);

			// TODO [OSS-JSSP] ログ機能実装
			SimpleLog.logp(Level.INFO, "Regist Tag: <IMART type=\"" + tagName + "\">");
			
		}
	}
	
	
	/**
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void invokeInitializer4Class() 
					throws ClassNotFoundException, 
							InstantiationException, 
							IllegalAccessException {
		
		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		String[] classNames = config.getInitializer4Class();
		
		for(String className : classNames){
			Class clazz = findClass(className);
			ApplicationInitializer appInitializer = (ApplicationInitializer) clazz.newInstance();
			
			// 初期化処理実行
			SimpleLog.logp(Level.INFO, "Application initialize: call " + className);
			appInitializer.initialize();
		}
		
	}

	/**
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void invokeInitializer4Script() 
						throws JavaScriptException, 
								FileNotFoundException, 
								InstantiationException, 
								IllegalAccessException, 
								IOException, 
								ClassNotFoundException {

		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		String[] scriptNames = config.getInitializer4Script();
		
		Context cx = Context.enter(); 		
		
		try{
			String initialFunctionName = 
				JSSPConfigHandlerManager.getConfigHandler().getInitialFunctionName();

			for(String scriptName : scriptNames) {
				
				ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
				ScriptScope scriptScope = builder.getScriptScope(scriptName);
					
				// 初期化処理実行
				SimpleLog.logp(Level.INFO, "Application initialize: call #" + initialFunctionName + "() in " + scriptName + ".js");
				scriptScope.call(cx, initialFunctionName, new Object[0]);				
			}
		}
		finally{
			Context.exit();
		}
	}

	/**
	 * @param path
	 * @param functionName
	 * @return
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static Function getFunction(String path, String functionName) 
						throws FileNotFoundException, 
								InstantiationException, 
								IllegalAccessException, 
								IOException, 
								ClassNotFoundException {
		
		ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
		ScriptScope scriptScope = builder.getScriptScope(path);
		
		Object function = scriptScope.get(functionName, null);

		if(function == ScriptableObject.NOT_FOUND
		   ||
		   !(function instanceof Function)) {
			
			throw new IllegalArgumentException("Function is not found: " + path + "#" + functionName);						
		}
		
		return (Function) function;
	}
	
	/**
	 * 指定のクラス名を持つクラスを返します。
	 * @param name クラス名
	 * @return クラス
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 */
	private static Class findClass(String name) throws ClassNotFoundException{
		try{
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		}
		catch(ClassNotFoundException cnfe){
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		}
	}
}
