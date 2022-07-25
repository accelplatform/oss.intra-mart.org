package org.intra_mart.jssp.script.api;

import java.io.FileNotFoundException;
import java.io.Serializable;

import org.intra_mart.jssp.page.Page;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * ページコンテンツの実行、および、生成を行います。
 * 
 * @scope public
 * @name Page
 */
public class PageObject extends ScriptableObject implements Serializable {

    /**
     * JSSP実行環境への登録用ゼロパラメータコンストラクタ。
     */
	public PageObject(){
		super();
	}

    /**
     * JSSP実行環境下でのオブジェクト名称を取得します
     *
     * @return String JSSP実行環境下でのオブジェクト名称
     */
	public String getClassName() {
		return "Page";
	}

	/**
	 * コンテンツの実行、および、生成を行います。<br/>
	 * 引数 path に該当する ファンクションコンテナ と プレゼンテーションページ の読込＆解析を行い、<br/>
	 * ページコンテンツの実行、および、生成を行います。
	 * 
	 * @scope public
	 * @param path 
	 * 				String ページパス（拡張子なし）
	 * @param arg 
	 * 			Object init() 関数へ渡す引数（任意）
	 * @param ... 
	 * 			... 複数指定する場合は、カンマの後に続けて指定します。
	 * 
	 * @return String 実行結果HTMLソース
	 */
    public static Object jsStaticFunction_getContents(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

    	if(args.length < 1){
    		IllegalArgumentException iae = new IllegalArgumentException("arguments should be 1 or more.");
    		throw Context.throwAsScriptRuntimeEx(iae);
    	}
    	
    	// パス取得
    	String path = ScriptRuntime.toString(args[0]);
    	
    	// 第2引数(＝配列要素番号は 1)以降をコピー
    	Object[] copiedArgs = copyArgs(args, 1);

    	// ページコンテンツ取得
    	try {
			Page page = new Page(path);
			return page.execute(Context.getCurrentContext(), copiedArgs);
		}
		catch (FileNotFoundException e) {
			throw Context.throwAsScriptRuntimeEx(e);
		}
	}

    
    /**
	 * 指定されたページのファンクションコンテナ内に定義されている関数を実行します。
	 * 
	 * @scope public
	 * @param path 
	 * 				String ページパス（拡張子なし）
	 * @param functionName
	 * 				String 実行関数名
	 * @param arg 
	 * 				Object 関数への引数（任意）
	 * @param ... 
	 * 			... 複数指定する場合は、カンマの後に続けて指定します。
	 * 
	 * @return Object 関数実行結果オブジェクト
	 */
    public static Object jsStaticFunction_executeFunction(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

    	if(args.length < 2){
    		IllegalArgumentException iae = new IllegalArgumentException("arguments should be 2 or more.");
    		throw Context.throwAsScriptRuntimeEx(iae);
    	}
    	
    	// パス取得
    	String path = ScriptRuntime.toString(args[0]);
    	
    	// 関数名取得
    	String functionName = ScriptRuntime.toString(args[1]);
    	
    	// 第3引数(＝配列要素番号は 2)以降をコピー
    	Object[] copiedArgs = copyArgs(args, 2);

    	// 関数を実行！
		Page page = new Page(path);
		return page.executeFunction(Context.getCurrentContext(), functionName, copiedArgs);
	}


	/**
	 * @param args コピー元引数オブジェクト配列
	 * @param copyIndexPosition コピー開始位置の配列要素番号
	 * @return コピー後の引数オブジェクト配列
	 */
	private static Object[] copyArgs(Object[] args, int copyIndexPosition) {
		
		// コピーした引数を格納する変数
		Object[] copiedArgs = new Object[0]; 
    	
    	int copyLength = args.length - copyIndexPosition;
    	
    	if(copyLength > 0){
			copiedArgs = new Object[copyLength];
			System.arraycopy(args, copyIndexPosition, copiedArgs, 0, copyLength);
    	}
    	
		return copiedArgs;
	}


}
