package org.intra_mart.jssp.script;

import java.io.Serializable;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JavaScript 実行可能オブジェクト（実行スコープ）
 */
public class ScriptScope extends ScriptableObject implements Serializable{
	
	/**
	 * スクリプトのソースパス
	 */
	private String scriptSourcePath;

	/**
	 * JavaScript 実行可能オブジェクト（実行スコープ）を生成します。<br>
	 * <br>
	 * Javascript 変数の基本スコープ「{@link org.intra_mart.jssp.script.FoundationScriptScope}」を
	 * プロトタイプに設定します。<br>
	 * 本コンストラクタでは、スクリプトのソースパスに "Unknown Script" を設定します。
	 */
	public ScriptScope(){
		this("Unknown Script");
	}
	

	/**
	 * JavaScript 実行可能オブジェクト（実行スコープ）を生成します。<br>
	 * Javascript 変数の基本スコープ「{@link org.intra_mart.jssp.script.FoundationScriptScope}」を
	 * プロトタイプに設定します。
	 * 
	 * @param scriptSourcePath スクリプトのソースパス(拡張子を除く)
	 */
	public ScriptScope(String scriptSourcePath){
		this.scriptSourcePath = scriptSourcePath;

		// prototype の設定
		this.setPrototype(FoundationScriptScope.instance());
	}
	
	
	/**
	 * JavaScript 実行可能オブジェクト（実行スコープ）を生成します。<br>
	 * 引数で指定されたスコープをプロトタイプに設定します。<br>
	 * 本コンストラクタでは、スクリプトのソースパスに "Unknown Script" を設定します。
	 * 
	 * @param base スコープ
	 */
	public ScriptScope(Scriptable base){
		
		this.scriptSourcePath = "Unknown Script";		// スコープ名称(パス)
		this.setPrototype(base.getPrototype());			// prototype の設定
		this.setParentScope(base.getParentScope());		// 親の設定

		// プロパティのコピー
		Object[] params = base.getIds();
		for(int idx = 0; idx < params.length; idx++){
			if(params[idx] instanceof String){
				
				Object value = base.get((String) params[idx], null);
				
				if(value != ScriptableObject.NOT_FOUND){
					this.defineProperty((String) params[idx], value, ScriptableObject.EMPTY);
				}
			}
		}
	}


	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName() {
		return "Global";
	}


	/**
	 * スクリプトのソースパスを取得します。
	 * @return スクリプトのソースパス
	 */
	public String getScriptSourcePath(){
		return scriptSourcePath;
	}


	/**
	 * 関数を実行します。
	 * 
	 * @param cx 実行環境
	 * @param name 関数名
	 * @param args 実行する関数の引数
	 * @return 関数実行結果
	 * 
	 * @throws JavaScriptException
	 */
	public Object call(Context cx, String name, Object[] args) throws JavaScriptException{
		return call(cx, name, args, this);
	}
	
	
	/**
	 * 関数を実行します。
	 * 
	 * @param cx 実行環境
	 * @param name 関数名
	 * @param args 実行する関数の引数
	 * @param scope
	 * @return 関数実行結果
	 * 
	 * @throws JavaScriptException
	 */
	public Object call(Context cx, String name, Object[] args, ScriptableObject scope) throws JavaScriptException{
		
		// 指定関数の取得
		Object func = this.get(name, this);

		if(func instanceof Function){

			// 登録前のスコープをバックアップ
			ScriptScope previousScriptScope = ScriptScope.entry(this);
			
			try{
				// 指定関数の起動
				synchronized(func){
					return ((Function) func).call(cx, scope, this, args);
				}
			}
			finally{
				// スコープの復元
				ScriptScope.entry(previousScriptScope);
			}
		}
		else{
			return null;		// 目的の関数が見つからなかった
		}
	}
	
	
	/* ========= 以下、staaticメソッド ========= */ 

	// 実行環境保存領域
	// TODO [OSS-JSSP] Manager化する必要があるか？
	private static ThreadLocal<ScriptScope> POOL = new ThreadLocal<ScriptScope>(); 
	

	/**
	 * 引数で指定された実行スコープを、現在実行中のスレッドに紐づけて格納します。<br>
	 * 引数で指定された実行スコープが null の場合は削除処理になります。
	 * 
	 * @param scope 登録する実行スコープ
	 * @return 格納前に現在のスレッドに紐付けられていた実行スコープ
	 */
	public static ScriptScope entry(ScriptScope scope){
		
		// 登録前のスコープをバックアップ
		ScriptScope ss_priv = POOL.get();

		// スコープの登録
		POOL.set(scope);

		// 登録前のスコープを返却
		return ss_priv;
	
	}

	/**
	 * 現在実行中のスレッドに紐づいた実行スコープを取得します。
	 * @return 実行スコープ
	 */
	public static ScriptScope current(){
		// 現在実行中のスコープの返却
		return POOL.get();
	}


	/**
	 * 現在実行中のスレッドに紐づけられている実行スコープを削除します。
	 * 
	 * @return 削除前に現在のスレッドに紐付けられていた実行スコープ
	 */
	public static ScriptScope exit(){
		// 登録前のスコープをバックアップ
		ScriptScope ss_priv = POOL.get();

		// スコープの登録
		POOL.set(null);

		// 登録前のスコープを返却
		return ss_priv;
	}
	

}
