package org.intra_mart.jssp.script.api;

import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.util.ClassNameHelper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.Wrapper;

/**
 * ログ出力用オブジェクト。<br/>
 * Loggerオブジェクトで使用される5つのログレベルは(順番に)以下のようになります。
 * 
 * <ol>
 * 	<li>trace (最も軽微)</li>
 * 	<li>debug</li>
 * 	<li>info</li>
 * 	<li>warn</li>
 * 	<li>error (最も重大)</li>
 * </ol>
 * 
 * <br/>
 * ●サンプルコード(JavaScript)
 * <pre>
 *    1: var logger = Logger.getLogger();
 *    2: logger.error("メッセージです。");
 *    3: logger.warn("パラメータは「{}」です。", "その１");
 *    4: logger.info("パラメータは「{}」 と 「{}」です。", "その１", "その２");
 *    5: 
 *    6: var argArray = new Array();
 *    7: argArray[0] = "第1パラメータ";  // String型
 *    8: argArray[1] = 5.678;            // Number型
 *    9: argArray[2] = new Date();       // Date型
 *   10: argArray[3] = false;            // Boolean型
 *   11: logger.debug("パラメータは「{}」 と 「{}」 と 「{}」 と 「{}」です。", argArray);
 * </pre>
 * 
 * @scope public
 * @name Logger
 */
public class LoggerObject extends ScriptableObject {

	private static final ScriptableObject PROTOTYPE = new LoggerObject();

	/* (非 Javadoc)
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName(){
		return "Logger";
	}
	
	/**
     * JSSP実行環境への登録用デフォルトコンストラクタ
     */
	public LoggerObject(){
		super();
		
		String[] funcs = {
				"isErrorEnabled", "error",
				"isWarnEnabled",  "warn",
				"isInfoEnabled",  "info",
				"isDebugEnabled", "debug",
				"isTraceEnabled", "trace",				
		};
		
		this.defineFunctionProperties(funcs, LoggerObject.class, ScriptableObject.DONTENUM);
	}


	private org.intra_mart.common.platform.log.Logger thisLogger;

	/**
	 * @param logger
	 */
	LoggerObject(org.intra_mart.common.platform.log.Logger logger){
		this.thisLogger = logger;

		// 基本メソッドの追加登録
        this.setPrototype(PROTOTYPE);
	}

	
    /**
     * @scope private
     * @see #getLogger()
     * @see #getLogger(String)
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     */
    public static Scriptable jsStaticFunction_getLogger(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
    	
    	if(args != null && args.length > 0){
    		Logger imLogger = Logger.getLogger(args[0].toString());
    		return new LoggerObject(imLogger);
    	}
    	else{
    		ScriptScope currentScriptSocpe = ScriptScope.current();
    		if(currentScriptSocpe == null){
    			throw new NullPointerException("Current ScriptScope is null.");
    		}

			String path = currentScriptSocpe.getScriptSourcePath();
			String packagePath = ClassNameHelper.toClassName(path, "");
			
			Logger imLogger = Logger.getLogger(packagePath);
			return new LoggerObject(imLogger);
    	}
    }
    
	/**
	 * {@link Logger}オブジェクトを返します。<br/>
	 *  
     * @scope public
	 * @param name String ロガー名
	 * @return Logger Loggerオブジェクト
	 */
	public static LoggerObject getLogger(String name) {
		// APIリスト用ダミー
		return null;
	}
	
	/**
	 * {@link Logger}オブジェクトを返します。<br/>
	 * ロガー名は、このメソッドを呼び出したJSファイルのソースパスを元に作成されます。<br/>
	 * 具体的には、JSファイルのソースパスのファイル区切りを「.」に置き換えた文字列をロガー名とします。<br/>
	 * <br/>
	 * なお、JSファイルのソースパスとは、ソース検索ディレクトリからの相対パス（拡張子なし）を意味します。
	 * 
     * @scope public
	 * @return Logger Loggerオブジェクト
	 */
	public static LoggerObject getLogger() {
		// APIリスト用ダミー
		return null;
	}

	
	// ############### 以下、インスタンスメソッド ###############
	// =============== error =============== 
	/**
	 * errorレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
     * @scope public
	 * @return Boolean errorレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isErrorEnabled(){
		return thisLogger.isErrorEnabled();
	}
	
	/**
     * @scope private
     * @see #doLog(org.intra_mart.common.platform.log.Logger.Level, Object, Object, Object) 
	 * 
	 * @param arg
	 */
	public void error(Object arg0, Object arg1, Object arg2){
		doLog(Logger.Level.ERROR, arg0, arg1, arg2);
	}

	/**
	 * errorレベルのログを出力します。<br/>
	 * 
	 * @scope public
	 * @param msg　String メッセージ文字列
	 */
	public void jsFunction_error(String msg){
		// APIリスト用ダミー
		// （メソッド名に「jsFunction_」を付与している理由）
		//  → オーバーロードされているメソッドは、JSオブジェクトのプロトタイプな関数としてを登録できない為。
	}
	
	/**
	 * errorレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 *
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param arg Object メッセージのパラメータ
	 */
	public void jsFunction_error(String format, Object arg){
		// APIリスト用ダミー
	}
	
	/**
	 * errorレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param argArray Array メッセージのパラメータの配列
	 */
	public void jsFunction_error(String format, Object argArray, String dummy){
		// APIリスト用ダミー
	}
	
	/**
	 * errorレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format　 String フォーマット文字列
	 * @param arg1　 Object メッセージの第1パラメータ
	 * @param arg2 Object メッセージの第2パラメータ
	 */
	public void jsFunction_error(String format, Object arg1, Object arg2){
		// APIリスト用ダミー
	}

	/**
	 * errorレベルの例外をログ出力します。<br/>
	 * 
	 * @scope private
	 * @param msg　String 例外に伴うメッセージ
	 * @param t Object ログに出力する例外
	 */
	public void jsFunction_error(String msg, Object t, Boolean dummy){
		// APIリスト用ダミー
	}
	
	
	// =============== warn =============== 
	/**
	 * warnレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
     * @scope public
	 * @return Boolean warnレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isWarnEnabled(){
		return thisLogger.isWarnEnabled();
	}

	/**
     * @scope private
     * @see #doLog(org.intra_mart.common.platform.log.Logger.Level, Object, Object, Object) 
	 * 
	 * @param arg
	 */
	public void warn(Object arg0, Object arg1, Object arg2){
		doLog(Logger.Level.WARN, arg0, arg1, arg2);
	}

	/**
	 * warnレベルのログを出力します。<br/>
	 * 
	 * @scope public
	 * @param msg　String メッセージ文字列
	 */
	public void jsFunction_warn(String msg){
		// APIリスト用ダミー
		// （メソッド名に「jsFunction_」を付与している理由）
		//  → オーバーロードされているメソッドは、JSオブジェクトのプロトタイプな関数としてを登録できない為。
	}
	
	/**
	 * warnレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 *
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param arg Object メッセージのパラメータ
	 */
	public void jsFunction_warn(String format, Object arg){
		// APIリスト用ダミー
	}
	
	/**
	 * warnレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param argArray Array メッセージのパラメータの配列
	 */
	public void jsFunction_warn(String format, Object argArray, String dummy){
		// APIリスト用ダミー
	}
	
	/**
	 * warnレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format　 String フォーマット文字列
	 * @param arg1　 Object メッセージの第1パラメータ
	 * @param arg2 Object メッセージの第2パラメータ
	 */
	public void jsFunction_warn(String format, Object arg1, Object arg2){
		// APIリスト用ダミー
	}

	/**
	 * warnレベルの例外をログ出力します。<br/>
	 * 
	 * @scope private
	 * @param msg　String 例外に伴うメッセージ
	 * @param t Object ログに出力する例外
	 */
	public void jsFunction_warn(String msg, Object t, Boolean dummy){
		// APIリスト用ダミー
	}
	

	// =============== info =============== 
	/**
	 * infoレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
     * @scope public
	 * @return Boolean infoレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isInfoEnabled(){
		return thisLogger.isInfoEnabled();
	}

	/**
     * @scope private
     * @see #doLog(org.intra_mart.common.platform.log.Logger.Level, Object, Object, Object) 
	 * 
	 * @param arg
	 */
	public void info(Object arg0, Object arg1, Object arg2){
		doLog(Logger.Level.INFO, arg0, arg1, arg2);
	}

	/**
	 * infoレベルのログを出力します。<br/>
	 * 
	 * @scope public
	 * @param msg　String メッセージ文字列
	 */
	public void jsFunction_info(String msg){
		// APIリスト用ダミー
		// （メソッド名に「jsFunction_」を付与している理由）
		//  → オーバーロードされているメソッドは、JSオブジェクトのプロトタイプな関数としてを登録できない為。
	}
	
	/**
	 * infoレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 *
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param arg Object メッセージのパラメータ
	 */
	public void jsFunction_info(String format, Object arg){
		// APIリスト用ダミー
	}
	
	/**
	 * infoレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param argArray Array メッセージのパラメータの配列
	 */
	public void jsFunction_info(String format, Object argArray, String dummy){
		// APIリスト用ダミー
	}
	
	/**
	 * infoレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format　 String フォーマット文字列
	 * @param arg1　 Object メッセージの第1パラメータ
	 * @param arg2 Object メッセージの第2パラメータ
	 */
	public void jsFunction_info(String format, Object arg1, Object arg2){
		// APIリスト用ダミー
	}

	/**
	 * infoレベルの例外をログ出力します。<br/>
	 * 
	 * @scope private
	 * @param msg　String 例外に伴うメッセージ
	 * @param t Object ログに出力する例外
	 */
	public void jsFunction_info(String msg, Object t, Boolean dummy){
		// APIリスト用ダミー
	}
	
	
	// =============== debug =============== 
	/**
	 * debugレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
     * @scope public
	 * @return Boolean debugレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isDebugEnabled(){
		return thisLogger.isDebugEnabled();
	}

	/**
     * @scope private
     * @see #doLog(org.intra_mart.common.platform.log.Logger.Level, Object, Object, Object) 
	 * 
	 * @param arg
	 */
	public void debug(Object arg0, Object arg1, Object arg2){
		doLog(Logger.Level.DEBUG, arg0, arg1, arg2);
	}

	/**
	 * debugレベルのログを出力します。<br/>
	 * 
	 * @scope public
	 * @param msg　String メッセージ文字列
	 */
	public void jsFunction_debug(String msg){
		// APIリスト用ダミー
		// （メソッド名に「jsFunction_」を付与している理由）
		//  → オーバーロードされているメソッドは、JSオブジェクトのプロトタイプな関数としてを登録できない為。
	}
	
	/**
	 * debugレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 *
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param arg Object メッセージのパラメータ
	 */
	public void jsFunction_debug(String format, Object arg){
		// APIリスト用ダミー
	}
	
	/**
	 * debugレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param argArray Array メッセージのパラメータの配列
	 */
	public void jsFunction_debug(String format, Object argArray, String dummy){
		// APIリスト用ダミー
	}
	
	/**
	 * debugレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format　 String フォーマット文字列
	 * @param arg1　 Object メッセージの第1パラメータ
	 * @param arg2 Object メッセージの第2パラメータ
	 */
	public void jsFunction_debug(String format, Object arg1, Object arg2){
		// APIリスト用ダミー
	}

	/**
	 * debugレベルの例外をログ出力します。<br/>
	 * 
	 * @scope private
	 * @param msg　String 例外に伴うメッセージ
	 * @param t Object ログに出力する例外
	 */
	public void jsFunction_debug(String msg, Object t, Boolean dummy){
		// APIリスト用ダミー
	}
	
	// =============== trace =============== 
	/**
	 * traceレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
     * @scope public
	 * @return Boolean traceレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isTraceEnabled(){
		return thisLogger.isTraceEnabled();
	}

	/**
     * @scope private
     * @see #doLog(org.intra_mart.common.platform.log.Logger.Level, Object, Object, Object) 
	 * 
	 * @param arg
	 */
	public void trace(Object arg0, Object arg1, Object arg2){
		doLog(Logger.Level.TRACE, arg0, arg1, arg2);
	}

	/**
	 * traceレベルのログを出力します。<br/>
	 * 
	 * @scope public
	 * @param msg　String メッセージ文字列
	 */
	public void jsFunction_trace(String msg){
		// APIリスト用ダミー
		// （メソッド名に「jsFunction_」を付与している理由）
		//  → オーバーロードされているメソッドは、JSオブジェクトのプロトタイプな関数としてを登録できない為。
	}
	
	/**
	 * traceレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 *
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param arg Object メッセージのパラメータ
	 */
	public void jsFunction_trace(String format, Object arg){
		// APIリスト用ダミー
	}
	
	/**
	 * traceレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format String フォーマット文字列
	 * @param argArray Array メッセージのパラメータの配列
	 */
	public void jsFunction_trace(String format, Object argArray, String dummy){
		// APIリスト用ダミー
	}
	
	/**
	 * traceレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @scope public
	 * @param format　 String フォーマット文字列
	 * @param arg1　 Object メッセージの第1パラメータ
	 * @param arg2 Object メッセージの第2パラメータ
	 */
	public void jsFunction_trace(String format, Object arg1, Object arg2){
		// APIリスト用ダミー
	}

	/**
	 * traceレベルの例外をログ出力します。<br/>
	 * 
	 * @scope private
	 * @param msg　String 例外に伴うメッセージ
	 * @param t Object ログに出力する例外
	 */
	public void jsFunction_trace(String msg, Object t, Boolean dummy){
		// APIリスト用ダミー
	}
	
	// ############### 以下、プライベート ###############	
	/**
	 * @param level
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	private void doLog(Logger.Level level, Object arg0, Object arg1, Object arg2) {

		if(thisLogger.isEnabled(level) == false){
			return;
		}
		
		String msgOrFormat = Context.toString(arg0); 
		
		// パラメータ指定なし
		if(isNullOrUndefined(arg1)
		   &&
		   isNullOrUndefined(arg2)){
			
			thisLogger.log(level, msgOrFormat);
			return;
		}
		// 2つのパラメータ指定
		else if((isNullOrUndefined(arg1) == false)
			    &&
			    (isNullOrUndefined(arg2) == false)){
			
			thisLogger.log(level, msgOrFormat, Context.toString(arg1), Context.toString(arg2));
			return;
		}
		// パラメータ1つ(配列 or Object or 例外)
		else if((isNullOrUndefined(arg1) == false)
			    &&
			    isNullOrUndefined(arg2)){

			if(arg1 instanceof NativeArray){
				NativeArray array = (NativeArray) arg1;
				long length = array.getLength();
	
				Object[] argArray = new Object[(int)length];
				for(int idx = 0; idx < length; idx++){
					argArray[idx] = Context.toString(array.get(idx, null));
				}
				
				thisLogger.log(level, msgOrFormat, argArray);
				return;
			}
			else if(arg1 instanceof NativeJavaObject){
				
				NativeJavaObject nativeJavaObject = (NativeJavaObject) arg1;
				Object unwraped = nativeJavaObject.unwrap();
				
				if(unwraped instanceof Throwable){
					Throwable t = (Throwable) unwraped;
					thisLogger.log(level, msgOrFormat, t);
					return;
				}
				else{
					thisLogger.log(level, msgOrFormat, unwraped);
					return;
				}
			}

			// Javaの例外がプロパティに含まれているかをチェック
			Throwable throwable = null; 
			if(arg1 instanceof Scriptable){
				Scriptable scriptable = (Scriptable) arg1;
				Object prop = scriptable.get("javaException", scriptable);
				
				if(prop instanceof Wrapper){
					Wrapper wrapper = (Wrapper) prop;
					Object unwrapped = wrapper.unwrap();
					
					if(unwrapped instanceof Throwable){
						throwable = (Throwable) unwrapped;
					}
				}
			}

			if(throwable != null){
				thisLogger.log(level, msgOrFormat, throwable);
				return;
			}
			else {
				thisLogger.log(level, msgOrFormat, Context.toString(arg1));
				return;
			}
		}
		else {
			return;
		}
	}
	
	/**
	 * 引数が null または undefinedの場合 true、 それ以外は、false を返却します。
	 * @param arg
	 * @return
	 */
	private boolean isNullOrUndefined(Object arg){
		if(arg == null || arg instanceof Undefined){
			return true;
		}
		else{
			return false;
		}
		
	}
}