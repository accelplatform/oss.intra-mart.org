package org.intra_mart.jssp.view.tag;

import java.io.IOException;
import java.io.Serializable;

import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.view.ViewScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.ScriptableObject;

/**
 * IMARTタグ引数情報オブジェクト。<br>
 * <br>
 * タグ関数の引数に渡されるオブジェクトです。<br>
 * 拡張 &lt;IMART&gt; タグ用の関数を定義した時に、関数の第２引数として実行エンジンから実行時に受け取るオブジェクトで、&lt;IMART&gt; タグおよび対応する &lt;/IMART&gt; タグに挟まれた範囲のスクリプト情報を保持しています。<br>
 * このオブジェクトにより、タグに挟まれた範囲のスクリプトを実行する事が出来ます(タグのネストを有効にすることができます)。<br>
 * 
 * @name InnerText
 * @scope public
 */
public class InnerTextObject extends ScriptableObject implements Serializable{

	private static final ScriptableObject PROTOTYPE = new InnerTextObject();

	private ViewScope inner;	// HTML 解析結果オブジェクト


	/**
	 * プロトタイプ設定用
	 */
	public InnerTextObject(){
		// ネイティブ関数群
		String[] func = { "execute" };

		// 関数の登録
        try {
			this.defineFunctionProperties(func, InnerTextObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			// メソッド設定失敗
		}
	}

	/**
	 * @param inner HTML 解析結果オブジェクト
	 */
	public InnerTextObject(ViewScope inner){
		this.inner = inner;				// 解析結果オブジェクトの登録
		this.setPrototype(PROTOTYPE);	// 基本メソッドの登録
	}


	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName(){
		return "Object";
	}

	/**
	 * オブジェクトの実行を行います。<br>
	 * <br> 
	 * このオブジェクトの示すスクリプトを実行します。<br>
	 * スクリプト中に &lt;IMART&gt; タグの記述があった場合、これを順次実行します。<br>
	 * すべての実行が終了した結果のソースを文字列として返します。<br>
	 * 
	 * @scope public
	 * @return String 実行結果のソース
	 * @throws JavaScriptException
	 */
	public String execute() throws JavaScriptException{
		try{
			return inner.execute(Context.getCurrentContext(), ScriptScope.current());
		}
		catch(IOException ioe){
			JavaScriptException jse = new JavaScriptException("Presentation-Page runtime error.", ScriptScope.current().getScriptSourcePath(), 0);
			jse.initCause(ioe);
			throw jse;
		}
	}
}