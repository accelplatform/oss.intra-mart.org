package org.intra_mart.jssp.script.api;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


/**
 * 共通関数を管理するオブジェクトです。<br/>
 * <br/>
 * メソッド <a href='#defineStringObject'>define()</a>で設定された JavaScript 関数は、
 * <strong>Procedure</strong> オブジェクトのプロパティ値としてプロセス内のすべての
 * ファンクション・コンテナから利用可能となります。<br/>
 * このＡＰＩの設定値は、Web Application Server の動作しているサーバ・プロセスをスコープとして管理されます。
 * <br/>
 * 他のサーバ・プロセスから<strong>Procedure</strong> オブジェクトに登録された関数を実行
 * (関数を共有)することはできません。<br/> 
 * また、プロセスが終了するまで関数への参照は保証されます(プロセスが終了した場合は、関数も破棄されます)。<br/>
 * <br/>
 * <strong>Procedure</strong> オブジェクトの同一プロパティ(name)に対して関数を再設定した場合、
 * 後に設定した関数が有効となります。<br/>
 * <br/>
 * <strong>Procedure</strong> オブジェクトには、JavaScript関数だけではなく、
 * JavaScriptオブジェクトも登録可能です。
 * 
 * @scope public
 * @name Procedure
 */
public class ProcedureObject extends ScriptableObject implements Cloneable, java.io.Serializable{

	private static final int PROP_ATTR = ScriptableObject.EMPTY;
	
	/**
	 * オブジェクト初期化と JavaScript 実行環境への登録を行います。<br/>
	 * オブジェクトの初期化（メソッド登録）と実行環境(scope)への登録を行います。<br/>
	 * 
	 * @param scope 変数スコープ
	 * @return プロシージャオブジェクト
	 */
	public static Scriptable init(Scriptable scope) {
		
		ProcedureObject oProc = new ProcedureObject();	// インスタンス生成
		oProc.setPrototype(getObjectPrototype(scope));

		// メソッド登録
		String[] names = {"define","toString"};
		oProc.defineFunctionProperties(names, ProcedureObject.class, ScriptableObject.DONTENUM);

		((ScriptableObject) scope).defineProperty("Procedure", oProc, ScriptableObject.DONTENUM);

		return oProc;
	}


    /**
     * JSSP実行環境下でのオブジェクト名称を取得します
     *
     * @return String JSSP実行環境下でのオブジェクト名称
     */
	public String getClassName(){
		return "Procedure";
	}

	
	
	/**
	 * このオブジェクトの文字列表現を返却します。<br/>
	 * 
	 * @return String 文字列表現
	 */
	public String toString(){
		Object[] params = this.getIds();
		List<String> buf = new ArrayList<String>(params.length);

		for(int idx = 0; idx < params.length; idx++){
			buf.add(ScriptRuntime.toString(params[idx]));
		}

		return "{" + buf.toString() + "}";
	}

	
	/**
	 * JavaScript関数 や JavaScriptオブジェクトを設定します。<br/>
	 * 
	 * 引数 funcOrObject にJavaScrip関数を指定した場合、Procedure.name() で その関数が利用可能となります。<br/>
	 * 引数 funcOrObject にJavaScripオブジェクトを指定した場合、Procedure.name でそのオブジェクトが利用可能となります。<br/>
	 * 
	 * @scope public
	 * @param name String 設定プロパティ名称（文字列）
	 * @param funcOrObject Object JavaScript 関数 や JavaScript オブジェクト
	 */
	public void define(Object name, Object funcOrObject){
		this.defineProperty(ScriptRuntime.toString(name), funcOrObject, PROP_ATTR);
	}

}

