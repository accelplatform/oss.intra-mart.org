package org.intra_mart.jssp.script;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import org.intra_mart.jssp.exception.JavaScriptBatonPassException;
import org.intra_mart.jssp.exception.JavaScriptRedirectException;
import org.intra_mart.jssp.exception.JavaScriptTransmissionException;
import org.intra_mart.jssp.script.listener.ContextFactoryListenerManager;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.BufferedInputStreamThread;
import org.intra_mart.jssp.util.JsUtil;
import org.intra_mart.jssp.util.SimpleLog;
import org.intra_mart.jssp.util.ValueObject;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.tools.shell.Environment;
import org.mozilla.javascript.tools.shell.Global;


/**
 * <!-- JavaScriptオブジェクト -->
 * <!--JSSP実行環境の基本スコープ-->
 * グローバル関数。
 * 
 * @name GlobalFunction
 * @scope public
 */
public class FoundationScriptScope extends ImporterTopLevel {
	
	private static FoundationScriptScope _instance = null;
	private static Object instanceMonitor = new Object();
	
    private boolean sealedStdLib = false;

	/**
	 * このクラスの唯一のインスタンスを返します。
	 */
	public static FoundationScriptScope instance(){
		if(_instance == null){
			synchronized(instanceMonitor){
				if(_instance == null){
					ContextFactory.Listener[] listeners;
					try {
						listeners = ContextFactoryListenerManager.getListeners();
					}
					catch (Exception e) {
						throw new IllegalStateException(e.getMessage(), e);
					}
				
					// リスナーの追加
					for(ContextFactory.Listener listener : listeners){
						ContextFactory.getGlobal().addListener(listener);
		
						// TODO [OSS-JSSP] ログ機能実装
						SimpleLog.logp(Level.INFO, "Regist ContextFactory.Listener: " + listener.getClass().getName());				
					}
		
					Context cx = Context.enter();
					try{
						_instance = new FoundationScriptScope(cx);
					}
					finally{
						Context.exit();
					}
				}
			}
		}
		return _instance;
	}
    
    private FoundationScriptScope(Context cx) {
        init(cx);
    }

    private void init(Context cx) {
        // Define some global functions particular to the shell. Note
        // that these functions are not part of ECMA.
        initStandardObjects(cx, sealedStdLib);
    	
        
        String[] names4RhinoGlobal = {
                "defineClass",
                "deserialize",
                "gc",
                // "help",        // ← Globalクラスのインスタンスが必要な関数は登録しない
                // "load",        // ← JSSPに存在するグローバル関数は登録しない
                "loadClass",
                // "print",
                // "quit",        // ← Globalクラスのインスタンスが必要な関数は登録しない
                "readFile",
                "readUrl",
                // "runCommand",  // ← Globalクラスのインスタンスが必要な関数は登録しない
                "seal",
                "serialize",
                "spawn",
                "sync",
                "toint32",
                "version",
        };
        defineFunctionProperties(names4RhinoGlobal, Global.class, ScriptableObject.DONTENUM);

        // Set up "environment" in the global scope to provide access to the
        // System environment variables.
        Environment.defineClass(this);
        Environment environment = new Environment(this);
        defineProperty("environment", environment, ScriptableObject.DONTENUM);

    	// ECMA Script には定義されていないＡＰＩの登録
		String[] names4jsspGlobal = {
				"echo",
				"include",
				"forward",
				"redirect",
				"transmission",
				"execute",
				"isBlank",
				"isNull",
				"isUndefined",
				"isTrue",
				"isFalse",
				"isObject",
				"isArray",
				"isString",
				"isNumber",
				"isBoolean",
				"isFunction",
				"isDate",
				"isEqual",
				"garbageCollector",
				"getMessageDigest", 
				"isDigit",
				"isNumeral",
				"isAlphabet",
				"isInfinity",
				"sleep",
				"isJavaInstance",
				"load"
		};
		this.defineFunctionProperties(names4jsspGlobal, this.getClass(), ScriptableObject.DONTENUM);

		try{
			// JavaScript 実行環境の基礎情報を構築
			cx.initStandardObjects(this, false);
		}
		catch(Exception e){
			// 登録失敗			
			// TODO [OSS-JSSP] ログ機能実装
			SimpleLog.logp(Level.WARNING, "Error in JavaScript Foundation-Scope constructor: " + e.getMessage(), e);
		}
	}

	
	/**
	 * JavaScriptAPIを登録します。
	 * @deprecated
	 * @param apiName
	 * @param function
	 */
	public void defineJavaScriptAPI(String apiName, Function function){
		this.defineProperty(apiName, function, ScriptableObject.EMPTY);
	}
	
	
	/**
	 * JavaScriptAPIを登録します。
	 * @deprecated
	 * @param clazz
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	public void defineJavaScriptAPI(Class clazz) 
					throws IllegalAccessException, 
							InstantiationException, 
							InvocationTargetException {
		
		if(! (clazz.newInstance() instanceof Scriptable) ){
			throw new IllegalStateException(
					"JavaScript-API must implement '" + Scriptable.class.getName() + "': " + clazz.getName());
		}
		
		ScriptableObject.defineClass(this, clazz, false);	
	}
	

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName() {
		return "Foundation";
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
			return "[object " + getClassName() + "]";
	}

		
    /**
	 * メッセージを出力します。
     * 
     * @scope public
     * @param message
     *            String 出力するメッセージ
     * @return void
     */
	public static void echo(String message){
		// TODO [OSS-JSSP] ログ機能実装
		SimpleLog.logp(Level.INFO, message);
	}

	
    /**
     * JSファイルを実行します。 <br/>
     * <br/>
     * 引数で指定されたJSファイル内の init() 関数を実行し、その結果を返却します。
     * 目的のプログラム内の init() 関数には引数として arg_1[, ..., arg_n]が渡されます。
     * <br/>
     * なお、グローバル関数「include()」を実行しても、引数で指定されたJSファイル内で定義されている変数や関数にはアクセスできません。<br/>
     * 現在実行中のスクリプトに別のJSファイルをロードする場合は、グローバル関数「load()」を利用してください。
     * グローバル関数「load()」を利用すると、別のJSファイル内で定義されている変数や関数にアクセスすることができるようになります。
     * 
     * @scope public
     * @param path
     *            String 実行対象ＪＳファイルパス(拡張子なし)
     * @param arg_1 Object JSファイルの init() 関数へ渡す引数群
     * @param ... ... 複数ある場合は、カンマ区切りで続けて設定します。
     *            
     * @return Object 指定したJSファイルの init() 関数の返却値
     */
	public static Object include(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		
		if(args.length > 0){
			// ソース名の決定
			String name = ScriptRuntime.toString(args[0]);
			try{
				// ソースの取得
				ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
				ScriptScope script = builder.getScriptScope(name);

				Object[] list = new Object[Math.max(args.length - 1, 0)];
				if(list.length > 0){
					// 引数データのコピー
					System.arraycopy(args, 1, list, 0, list.length);
				}
				
				String initialFunctionName = 
					JSSPConfigHandlerManager.getConfigHandler().getInitialFunctionName();

				return script.call(Context.getCurrentContext(), initialFunctionName, list);
			}
			catch(Exception e){
	    		throw Context.throwAsScriptRuntimeEx(e);
			}

		}
		else{
			// 引数不足
			return null;
		}
	}

    /**
     * 現在のプログラム実行を中断して別プログラムを実行します。 <BR>
     * <BR>
     * 第２引数 argument は、フォーワード先の init() 関数の引数になります。 <br>
     * 本メソッドは、try...catch 文では使用できません。
     * <!--request オブジェクトを引数としてプログラムを呼び出す場合は、動作の仕組を十分に考慮してプログラムする必要があります。-->
     * 
     * @scope public
     * @param path
     *            String リダイレクト先ソースパス
     * @param argument
     *            Object 引数オブジェクト
     * @return void
     */
	public static void forward(Object path, Object argument) throws JavaScriptBatonPassException{
		throw new JavaScriptBatonPassException(ScriptRuntime.toString(path), argument);
	}


	/**
     * 指定ＵＲＬへリダイレクトを行います。 <BR>
     * 本メソッドは、try...catch 文では使用できません。
     * <BR>
     * 
     * @scope public
     * @param url
     *            String リダイレクト先ＵＲＬ
     * @return void
     */
	public static void redirect(String url) throws JavaScriptRedirectException{
		throw new JavaScriptRedirectException(url);
	}


    /**
     * 現在処理中のプログラム実行を中断して、指定のソースを送信します。 <br>
     * <br>
     * 指定されたソースは、バイナリデータ（指定されたまま）として扱われます。（文字コード変換は行なわれません） <br>
     * 送信ソースには、ＨＴＴＰレスポンスヘッダを含める必要があります。 <br>
     * (このＡＰＩを利用した場合、JSSP実行環境は指定ソースに対する一切の情報操作をおこないません) <br>
     * ＨＴＴＰレスポンスヘッダの作成法法に関しては、一般的なＣＧＩプログラムの作成方法を参考にして下さい。 <br>
     * このＡＰＩにより送信されたソースは、セッション管理対象外となります。 <br>
     * 
     * なお、本メソッドは、try...catch 文では使用できません。
     * <!--request オブジェクトを引数としてプログラムを呼び出す場合は、動作の仕組を十分に考慮してプログラムする必要があります。-->
     * 
     * 
     * @scope public
     * @param stream
     *            String ブラウザへの送信ソース
     * @return void
     */
	public static void transmission(String stream) throws JavaScriptTransmissionException{
		throw new JavaScriptTransmissionException(stream);
	}

    /**
     * システムコールを行います。 <BR>
     * <BR>
     * 指定された文字列 command を、コマンド・プロセッサによって実行されるホスト環境へ渡して、独立した新しいプロセスとして実行します。 <br>
     * この関数は、実行したプロセスが終了するまで待機状態になります。 <br>
     * 返却値はオブジェクト型で以下の形式になります。 <br>
     * <br>
     * ・実行したプロセスが正常終了した場合 <br>
     * <br>
     * 
     * <pre>
     *  	returnObject
     *  		├ output // プロセスからの標準出力ストリーム(String)
     *  		├ error  // プロセスからのエラー出力ストリーム(String)
     *  		└ exit   // プロセスの終了コード
     * </pre>
     * 
     * <br>
     * <br>
     * ・実行したプロセスが正常終了しなかった場合 <br>
     * <br>
     * 
     * <pre>
     *  	returnObject
     *  		├ error // エラー内容(String)
     *  		└ exit  // プロセスの終了コード
     * </pre>
     * 
     * <br>
     * <br>
     * ・プロセス実行時にランタイム内でエラーが発生した場合 <br>
     * <br>
     * 
     * <pre>
     *  	returnObject
     *  		└ exception // エラー内容(String)
     * </pre>
     * 
     * プロセスの終了コードは、0 の場合正常終了となります。 <br>
     * 上記は一例です。環境により上記プロパティの組合わせが異なる場合があります。 <br>
     * 
     * @scope public
     * @param command
     *            String 実行するシステムコマンド
     * @return Object 結果オブジェクト
     */
	public static Object execute(String command){
		int attr = ScriptableObject.EMPTY;
		ValueObject vo = new ValueObject();	// 結果オブジェクト
		BufferedInputStreamThread is = null;
		BufferedInputStreamThread es = null;

		try{
			// 指定コマンドの実行
			Process ps = Runtime.getRuntime().exec(command);

			// 入出力の取得
			is = new BufferedInputStreamThread(ps.getInputStream());
			es = new BufferedInputStreamThread(ps.getErrorStream());

			// プロセス終了待ち
			is.start();		// 標準出力取得スレッド実行開始
			es.start();		// エラー出力取得スレッド実行開始
			while(true){
				try{
					vo.defineProperty("exit", new Integer(ps.waitFor()), attr);
					break;
				}
				catch(InterruptedException ie){
					continue;
				}
			}
			is.join();		// 標準出力取得スレッド実行終了待ち
			es.join();		// エラー出力取得スレッド実行終了待ち

			// 出力の取得
			vo.defineProperty("output", is.getString(), attr);
			vo.defineProperty("error", es.getString(), attr);
		}
		catch(IOException ioe){
			if(is != null && es != null){
				vo.defineProperty("error", ioe.getMessage(), attr);
			}
			else{
				vo.defineProperty("exception", ioe.getMessage(), attr);
			}
		}
		catch(Throwable t){
			vo.defineProperty("exception", t.getMessage(), attr);
		}
		finally{
			try{
				if(is != null){ is.close(); }
			}
			catch(Throwable is_t){
				vo.defineProperty("exception", is_t.getMessage(), attr);
			}
			try{
				if(es != null){ es.close(); }
			}
			catch(Throwable es_t){
				vo.defineProperty("exception", es_t.getMessage(), attr);
			}
		}

		return vo;
	}

    /**
     * 空文字列であるか判別します。 <BR>
     * <BR>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     *  	<table border='0'>
     * 			<tr>
     * 				<td>空文字列</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>null</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>false</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>true</td>
     * 				<td>: false</td>
     * 			</tr>
     * 			<tr>
     * 				<td>undefined</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isBlank(Object value){
		// "" なら真値を返却
		if(value instanceof String){
			return ((String) value).length() == 0;		
		}

		// null なら真値を返却
		if(value == null){
			return true; 
		}

		// 偽値なら真値を返却
		if(value instanceof Boolean){
			return ! ((Boolean) value).booleanValue();	
		}

		// 未定義値なら真値
		if(value instanceof Undefined){
			return true; 
		}
		
		// 該当せず
		return false;
	}

	
    /**
     * 指定の値が null であるかどうか判別します。 <BR>
     * <BR>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>null</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isNull(Object value){
		return value == null;
	}


	/**
     * 指定の値が undefined であるかどうか判別します。 <BR>
     * <BR>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>undefined</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isUndefined(Object value){
		return value instanceof Undefined;
	}

	
    /**
     * 指定の値が真値であるかどうか判別します。 <BR>
     * <BR>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>真偽値型でその値が 真値</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isTrue(Object value){
		if(value instanceof Boolean){
			// 真偽値型ならば真偽判定を返却
			return ((Boolean) value).booleanValue();
		}

		return false;
	}


    /**
     * 指定の値が偽値であるかどうか判別します。 <BR>
     * <BR>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>真偽値型でその値が 偽値</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isFalse(Object value){
		if(value instanceof Boolean){
			// 真偽値型ならば真偽判定を返却
			return ! ((Boolean) value).booleanValue();
		}

		return false;
	}


	/**
     * 指定の値の型がObject型であるかどうか判別します。 <BR>
     * <BR>
     * 指定値が null ではなく、指定値の型が Object 型である場合にのみ真値を返却します。 <br>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>Object 型</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isObject(Object value){
		return value instanceof Scriptable 
				&&
				! (value instanceof Function) 
				&& 
				! (JsUtil.isDate(value)) 
				&& 
				! (value instanceof NativeArray) 
				&&
				! (value instanceof Undefined);
	}


	/**
     * 指定の値の型が配列型であるかどうか判別します。 <BR>
     * <BR>
     * 指定値の型が配列型である場合にのみ真値を返却します。 <br>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>配列型</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isArray(Object value){
		return value instanceof NativeArray;
	}

	
    /**
     * 指定の値の型が文字列型であるかどうか判別します。 <BR>
     * <BR>
     * 指定値の型が文字列型である場合にのみ真値を返却します。 <br>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>文字列型</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isString(Object value){
		return value instanceof String;
	}

	
    /**
     * 指定の値の型が数値型であるかどうか判別します。 <BR>
     * <BR>
     * 指定値の型が数値型である場合にのみ真値を返却します。 <br>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>数値型</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isNumber(Object value){
		return value instanceof Number;
	}


	/**
     * 指定の値の型が真偽値型であるかどうか判別します。 <BR>
     * <BR>
     * 指定値の型が真偽値型である場合にのみ真値を返却します。 <br>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>真偽値型</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isBoolean(Object value){
		return value instanceof Boolean;
	}


    /**
     * 指定の値の型が関数型であるかどうか判別します。 <BR>
     * <BR>
     * 指定値の型が関数型である場合にのみ真値を返却します。 <br>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>関数型</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isFunction(Object value){
		return value instanceof Function;
	}


    /**
     * 指定の値の型がDate型であるかどうか判別します。 <BR>
     * <BR>
     * 指定値の型がDate型である場合にのみ真値を返却します。 <br>
     * 
     * @scope public
     * @param value
     *            Object 判別する変数
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>Date型</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isDate(Object value){
		return JsUtil.isDate(value);
	}


    /**
     * 引数に指定された２つの値が等しいかどうか判別します。 <BR>
     * <BR>
     * 指定の値の内部構造の等価判定をした結果を返却します。 <br>
     * 参照判定ではないので、別インスタンス同志の値の比較が可能です。 <br>
     * 
     * @scope public
     * @param valueA
     *            Object 等価判定対象の値
     * @param valueB
     *            Object 等価判定対象の値
     * @return Boolean 判別結果
     * 		<table border='0'>
     * 			<tr>
     * 				<td>等しい</td>
     * 				<td>: true</td>
     * 			</tr>
     * 			<tr>
     * 				<td>上記以外</td>
     * 				<td>: false</td>
     * 			</tr>
     * 		</table>
     */
	public static boolean isEqual(Object valueA, Object valueB){
		if(valueA != valueB){
			// 一方の値が null であった場合
			if(valueA == null || valueB == null){
				return false; 
			}

			// 一方の値が undefined であった場合
			if( (valueA instanceof Undefined) || (valueB instanceof Undefined) ){
				
				return (valueA instanceof Undefined) && (valueB instanceof Undefined); 
			
			}

			try{
				// 内部構造チェック(データを直列化して比較)
				byte data_a[], data_b[];
				ByteArrayOutputStream buffer;
				ObjectOutputStream oos;

				// value_a の直列化
				buffer = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(buffer);
				oos.writeObject(valueA);
				oos.flush();
				oos.close();
				data_a = buffer.toByteArray();	// 直列化結果データ保存

				// value_b の直列化
				buffer = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(buffer);
				oos.writeObject(valueB);
				oos.flush();
				oos.close();
				data_b = buffer.toByteArray();	// 直列化結果データ保存

				// 比較
				if(data_a.length == data_b.length){
					int idx;
					// データチェック
					for(idx = 0; idx < data_a.length; idx++){
						if(data_a[idx] != data_b[idx]){ return false; }
					}
				}
				else{
					// データ長が異なる
					return false;
				}
			}
			catch(Exception e){
				System.out.println("Error!!: making replica of JavaScript value.");
				return false;
			}
		}

		// 全てのロジックを通過 ＝＝ ２つのデータは等しい
		return true;
	}


	/**
     * ガーベージコレクションの実行を行います。 <BR>
     * <BR>
     * 
     * @scope public
     * @return void
     */
	public static void garbageCollector(){
		// TODO [OSS-JSSP] GarbageController.clean();の内容を一部実行（イベントリスナー呼び出しを行っていない）
		Runtime runtime = Runtime.getRuntime();
		long beforeFreeSize = runtime.freeMemory();
		
		// ごみ処理
		System.runFinalization();		// オブジェクトのファイナライズ処理
		System.gc();					// ガーベージコレクタの明示的起動
		
		// TODO [OSS-JSSP] ログ機能実装
		SimpleLog.logp(Level.INFO, 
					   "The processing end of Garbage-Controller[Free-Memory]: " + beforeFreeSize + " -> " + runtime.freeMemory() + " / " + runtime.totalMemory());
	}

	
	/**
	 * メッセージダイジェストを生成します。<br/>
	 * <br/>
	 * MD5 または SHA などのメッセージダイジェストアルゴリズムの機能を提供します。<br/>
	 * <br/>
	 * 引数 algorithm に指定可能なメッセージダイジェストアルゴリズムは、以下の通りです。<br/>
	 * <table border="1" width="70%">
	 * 	<tr>
	 * 		<th>メッセージダイジェストアルゴリズム名</th>
	 * 		<th>概要</th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>MD2</td>
	 * 		<td>RFC 1319 で定義されている MD2 メッセージダイジェストアルゴリズム</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>MD5</td>
	 * 		<td>RFC 1321 で定義されている MD5 メッセージダイジェストアルゴリズム</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>SHA-1</td>
	 * 		<td>セキュアハッシュ標準 NIST FIPS 180-1 で定義されているセキュアハッシュアルゴリズム</td>
	 * 	</tr>
	 * </table>
	 * 
	 * <br/>
	 * 文字列“1234”から、MD5のダイジェストを生成する例を以下に示します。<br/>
	 * 
	 * <pre>getMessageDigest("MD5", "1234");</pre>
	 * 
	 * 実行結果は、「81dc9bdb52d04dc20036dbd8313ed055」となります。
	 * <br/>
	 * 
     * @scope public
	 * @param algorithm
	 * 				String ダイジェストのアルゴリズム名
	 * @param value
	 * 				String ダイジェストを求めたいデータ
	 * @return
	 * 			String メッセージダイジェスト
	 */
	public static String getMessageDigest(Object algorithm, Object value) {

		if(algorithm == null || value == null){
			return null;
		}

		String targetAlgorithm = ScriptRuntime.toString(algorithm);
		String targetValue = ScriptRuntime.toString(value);
		
		byte[] b;
		try {
			MessageDigest md = MessageDigest.getInstance(targetAlgorithm);
			b = md.digest(targetValue.getBytes("8859_1"));
		}
		catch (NoSuchAlgorithmException e) {
			throw Context.throwAsScriptRuntimeEx(e);
		}
		catch (UnsupportedEncodingException e) {
			throw Context.throwAsScriptRuntimeEx(e);
		}

		char[] c = new char[b.length * 2];
		for(int idx = 0; idx < b.length; idx++){
			c[idx * 2] = Character.forDigit(b[idx] >> 4 & 0x0f, 16);
			c[idx * 2 + 1] = Character.forDigit(b[idx] & 0x0f, 16);
		}
		return new String(c);

	}
	

	/* ========== 以下、非公開API  ========== */
	
	/**
	 * このオブジェクト自身を返却します。
	 * @return
	 */
	public Object valueOf() {
			return this;
	}

	
	/**
	 * チェック対象文字列が数字のみで構成されているかを判定します。
	 * ブランク文字列の場合は真値を返却します。
	 * (∵文字列の構成要素中に数字以外の文字が存在しないから)
	 * 
     * @scope private
	 * @param value チェック対象文字列
	 * @return チェック対象文字列が数字のみで構成されている場合は true、
	 * 			数字以外の文字が含まれている場合は false を返却します。 
	 */
	public static boolean isDigit(String value){
		char[] chrs = value.toCharArray();	// 文字毎に分割

		for(int idx = 0; idx < chrs.length; idx++){
			// 数字でない場合は偽値を返却
			if(! Character.isDigit(chrs[idx])){ return false; }
		}

		// ロジックをすべて通ったので真値を返却
		return true;
	}


	/**
	 * チェック対象文字列が数値として表現可能かを判定します。
	 * 
     * @scope private
	 * @param value チェック対象文字列
	 * @return チェック対象文字列が数値として表現可能な文字列の場合は true、
	 * 			数値として解析可能な文字列ではない場合は falseを返却します。
	 */
	public static boolean isNumeral(String value){
		try{
			Double.parseDouble(value);
		}
		catch(NumberFormatException nfe){
			// Double に解析可能な文字列ではない
			return false;
		}
		return true;
	}


	/**
	 * チェック対象文字列が英字のみで構成されているかを判定します。
	 * ブランク文字列の場合は真値を返却します。
	 * (∵文字列の構成要素中に英字以外の文字が存在しないから)
	 * 
     * @scope private
	 * @param value チェック対象文字列
	 * @return チェック対象文字列が英字のみで構成されている文字列場合は true、
	 * 			英字以外の文字が含まれている場合は false を返却します。
	 */
	public static boolean isAlphabet(String value){
		char[] chrs = value.toCharArray();	// 文字毎に分割

		for(int idx = 0; idx < chrs.length; idx++){
			// 数字でない場合は偽値を返却
			if(! Character.isLetter(chrs[idx])){ return false; }
		}

		// ロジックをすべて通ったので真値を返却
		return true;
	}

	
	/**
	 * チェック対象数値が無限大を示す数値であるかを判定します。
	 * 正の無限大なのか、それとも負の無限大なのかは判定しません。
	 * 
     * @scope private
	 * @param num チェック対象数値
	 * @return チェック対象数値が無限大を示す数値である場合は true、
	 * 			無限大を示さない数値である場合は false を返却します。
	 */
	public static boolean isInfinity(double num){
		return num == Double.POSITIVE_INFINITY || num == Double.NEGATIVE_INFINITY;
	}

	
    /**
     * 実行処理を中断します。 <BR>
     * <BR>
     * 指定時間だけ、プログラムの実行を中断します。 <BR>
     * 時間指定の単位はミリ秒 <BR>
     * 引数には自然数を指定します。 <BR>
     * 仕様外の引数指定時の動作は保証外です。 <BR>
     *
     * @scope private
     * @param millis
     *            Number 中断する時間（ミリ秒）
     */
	public static void sleep(int millis){
		
        if (millis > 0) {
        
            long bedin = System.currentTimeMillis();
            long terminate = bedin + millis;
            
            for (long sleepTime = millis; sleepTime > 0; sleepTime = terminate - System.currentTimeMillis()) {
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException ie) {
                    // 別のスレッドが現在のスレッドに割り込んだことによる例外
                    continue;
                }
            }
        }
        else {
            Thread.yield();
        }
		
	}

	/**
	 * 指定の値がJavaのインスタンスであるかどうか判別します。<br/>
	 * 
	 * @scope private
	 * @param value 判別する変数
	 * @return Javaのインスタンスの場合は true、そうでない場合は falseを返却します。
	 */
	public static boolean isJavaInstance(Object value){
		if( value instanceof NativeJavaObject){
			return true;
		}
		else{
			return false;
		}
	}

    /**
     * JSファイルをロードします。<br/>
     * <br/>
     * 本関数を実行すると、引数で指定されたJSファイル内で定義されている変数や関数にアクセスすることができるようになります。
     * 
     * @scope public
     * @param path String ロードするＪＳファイルパス(拡張子なし)
     * @param ... ...  JSファイルを複数ロードする場合は、カンマ区切りで続けて設定します。
     */
	public static void load(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

        for (int i = 0; i < args.length; i++) {
        	
			String path = (String) args[i];
			ScriptScope scope = null;
			
			if(thisObj != null && thisObj instanceof ScriptScope){
				scope = (ScriptScope)thisObj;
			}
			else if(ScriptScope.current() != null){
				scope = ScriptScope.current();
			}
			else{
				scope = null;
			}
	
			try{
				ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
				builder.getScriptScope(path, scope);
			}
			catch(Exception e){
	    		throw Context.throwAsScriptRuntimeEx(e);
			}
        }
	}
}
