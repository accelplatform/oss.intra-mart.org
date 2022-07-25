package org.intra_mart.jssp.script.api;

import java.util.HashMap;
import java.util.Map;

import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 定数値管理オブジェクト。<br/>
 * <br/>
 * 定数値を管理するＡＰＩです。<br/>
 * メソッド <a href='#defineStringString'>define()</a>で設定された値は、
 * <strong>Constant</strong> オブジェクトのプロパティ値としてプロセス内のすべての
 * ファンクション・コンテナから変更不能な定数値として参照することができます。<br/>
 * このＡＰＩの設定値は、Web Application Server の動作しているサーバ・プロセスをスコープとして管理されます。
 * <br/>
 * 他のサーバ・プロセスから参照(値の共有)することはできません。<br/> 
 * また、プロセスが終了するまで値の参照は保証されます(プロセスが終了した場合は、値も破棄されます)。<br/>
 * <br/>
 * このオブジェクトの同一プロパティ(key)に対して定数値の二重定義はできません。<br/>
 * 
 * @scope public
 * @name Constant
 */
public class ConstantObject extends ScriptableObject implements Cloneable, java.io.Serializable{

	private final int PROP_ATTR = ScriptableObject.READONLY | ScriptableObject.PERMANENT;

	/**
	 * オブジェクト初期化と JSSP実行環境への登録メソッド
	 * @param scope
	 * @return
	 */
	public static Scriptable init(Scriptable scope) {
		ConstantObject oConst = new ConstantObject();	// インスタンス生成
		oConst.setPrototype(getObjectPrototype(scope));

		// メソッド登録
		String[] names = {"define","load","toString"};
		oConst.defineFunctionProperties(names, ConstantObject.class, ScriptableObject.DONTENUM);

		((ScriptableObject) scope).defineProperty("Constant", oConst, ScriptableObject.DONTENUM);

		return oConst;
	}


    /**
     * JSSP実行環境下でのオブジェクト名称を取得します
     *
     * @return String JSSP実行環境下でのオブジェクト名称
     */
	public String getClassName(){
		return "Constant";
	}

	/**
	 * 定数値を設定します。<br/>
	 * <br/>
	 * システムで共通の定数値を設定します。<br/>
	 * 引数 key で指定した文字列が <strong>Constant</strong> オブジェクトのプロパティとなり、
	 * 設定後は <strong>Constant</strong> オブジェクトを介して key で指定されたプロパティにより
	 * 定数値 value を参照できるようになります。<br/>
	 * <br/>
	 * <strong>「Constant.key」</strong>で、設定した定数値を参照することが出来ます。<br/>
	 * <br/>
	 * <br/>
	 * <strong><i>例:</i></strong><br/>
	 * <blockquote>
	 * Constant.define(&quot;product_name&quot;, &quot;intra-mart&quot;);<br/>
	 * Debug.browse(Constant.product_name);<br/>
	 * <br/>
	 * 画面には、変数の内容として <strong>intra-mart</strong> が表示されます。<br/>
	 * </blockquote>
	 * 
	 * @scope public static
	 * @param key String 定数値参照名称
	 * @param value String 定数値
	 * @return void
	 */
	public Scriptable define(Object key, Object value) {
		this.defineProperty(ScriptRuntime.toString(key), value, PROP_ATTR);
		return this;
	}

	/**
	 * 定数値設定ファイルを読込ます。<br/>
	 * <br/>
	 * 指定ファイル内に定義されたシステムで共通の定数値を設定します。<br/>
	 * 引数には、定数値設定ファイルのページパス（拡張子なし）を指定して下さい。
	 * <br/>
	 * 設定ファイル内には、通常のファンクション・コンテナを作成する要領で、グローバル変数を定義して下さい。<br/>
	 * グローバル変数の宣言は初期化（初期値代入）を伴うように記述して下さい。
	 * グローバル変数に対して初期値が設定されていない場合、正しく定数値を設定することができません。<br/>
	 * <br/>
	 * 定数値設定ファイルが読み込まれた後は、ファイル内のグローバル変数名が <strong>Constant</strong> オブジェクトの
	 * プロパティ名称となり、グローバル変数値が定数値として設定されます。<br/>
	 * <br/>
	 * <strong>「Constant.変数名」</strong>で、変数値を参照することが出来ます。<br/>
	 * <br/>
	 * <br/>
	 * <strong><i>例:</i></strong>
	 * <blockquote>
	 * <table border='1'>
	 * <tr><td>定数値設定ファイル（test.js）</td></tr> 
	 * <tr><td>
	 * var SUN = 0;<br/>
	 * var MON = 1;<br/>
	 * var TUE = 2;<br/>
	 * var WED = 3;<br/>
	 * var THU = 4;<br/>
	 * var FRI = 5;<br/>
	 * var SAT = 6;<br/>
	 * </td></tr>
	 * </table>
	 * <strong>ファンクション・コンテナ</strong><br/>
	 * <br/>
	 * <pre>Constant.load(&quot;test&quot;);<br/>Debug.browse(Constant.WED);</pre>
	 * <br/>
	 * 画面には、変数の内容として <strong>3</strong> が表示されます。<br/> 
	 * </blockquote>
	 * 
	 * @scope public static
	 * @param pagePath String 定数値設定ファイルのページパス（拡張子なし）
	 * @return void
	 */
	public Scriptable load(String pagePath) {
		try {
			// JavaScript ソースのロード
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			ScriptScope scriptScope = builder.getScriptScope(pagePath);

			// プロパティの登録
			Object[] params = scriptScope.getIds();
			for(int idx = 0; idx < params.length; idx++){
				String name = ScriptRuntime.toString(params[idx]);
				this.defineProperty(name, scriptScope.get(name, null), PROP_ATTR);
			}

			return this;	// Constant Object 自身を返却
		}
		catch(Exception e){
			throw Context.throwAsScriptRuntimeEx(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		Object[] params = this.getIds();
		Map<String, String> buf = new HashMap<String, String>();

		for(int idx = 0; idx < params.length; idx++){
			buf.put(ScriptRuntime.toString(params[idx]),
					ScriptRuntime.toString(this.get((String) params[idx], null)));
		}

		return buf.toString();
	}
}
