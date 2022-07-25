package org.intra_mart.jssp.view.tag;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;



/**
 * HTML タグソースを生成するクラス。
 */
public class TagObject extends ScriptableObject implements Cloneable, Serializable{

	private String tagId;
	private Hashtable<String, Object> attribute = new Hashtable<String, Object>();

	/**
	 * JavaScript 実行環境下での名称取得メソッド。
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName(){
		return "Tag";
	}

	/**
	 * 
	 */
	public TagObject(){
		tagId = "";
	}
	
	
	/**
	 * @param tagname タグの名称。
	 */
	public TagObject(String tagname){
		tagId = tagname;
	}


	/**
	 * タグの属性値の設定メソッド。
	 * @param name 属性名
	 * @param value 値
	 */
	public void setAttribute(String name, Object value){
		if(value != null){ attribute.put(name, value); }
	}


	/**
	 * タグソース文字列生成メソッド。<br>
	 * <br>
	 * タグを生成します。<br>
	 * &lt;TAG name=value ...&gt; を作成。終了タグは作らない
	 * 
	 * @return タグ文字列
	 */
	public String getBegin(){
		StringBuffer buf = new StringBuffer();

		buf.append("<" + tagId);

		Iterator view = attribute.entrySet().iterator();
		while(view.hasNext()){
			Map.Entry me = (Map.Entry) view.next();
			// キーと値の取得
			Object value = me.getValue();

			if(value instanceof String){
				buf.append(" " + (String) me.getKey() + "=\"" + (String) value + "\"");
			}
			else if(value instanceof Number){
				buf.append(" " + (String) me.getKey() + "=\"" + ScriptRuntime.toString(value) + "\"");
			}
			else if(value instanceof Boolean){
				if(((Boolean) value).booleanValue()){
					buf.append(" " + (String) me.getKey());
				}
			}
		}

		buf.append(">");
		return buf.toString();
	}


	/**
	 * タグ終端ソース文字列生成メソッド。<br>
	 * <br>
	 * 終了タグを生成します。<br>
	 * &lt;/TAG&gt; を作成。
	 * @return タグ終端文字列
	 */
	public String getTerminate(){
		return "</" + tagId + ">";
	}


	/**
	 * JavaScript 実行環境下でのコンストラクターメソッド for JavaScript。
	 * 
	 * @param cx JavaScript 実行環境
	 * @param args 引数群
	 * @param ctorObj
	 * @param inNewExpr new 演算子による呼び出しかの判定フラグ
	 * @return JavaScript オブジェクト
	 */
	public static Object jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr){
		if(inNewExpr){
			if(args.length > 0){
				return new TagObject(ScriptRuntime.toString(args[0]));
			}

			return new TagObject(ScriptRuntime.toString(Undefined.instance));
		}

		// new 演算子での呼び出しではない場合
		return null;
	}


	/**
	 * タグの属性値の設定メソッド for JavaScript。
	 * @param name 属性名
	 * @param value 値
	 */
	public void jsFunction_setAttribute(Object name, Object value){
		setAttribute(ScriptRuntime.toString(name), value);
	}
	public void jsFunction_defineAttribute(Object name, Object value){
		setAttribute(ScriptRuntime.toString(name), value);
	}


	/**
	 * タグソース文字列生成メソッド for JavaScript。<br>
	 * <br>
	 * タグを生成します。<br>
	 * &lt;TAG name=value ...&gt; を作成。終了タグは作らない
	 * 
	 * @return タグ文字列
	 */
	public String jsFunction_getBegin(){
		return getBegin();
	}
	
	/**
	 * タグソース文字列生成メソッド for JavaScript。<br>
	 * <br>
	 * タグを生成します。<br>
	 * &lt;TAG name=value ...&gt; を作成。終了タグは作らない
	 * 
	 * @return タグ文字列
	 */
	public String jsFunction_begin(){
		return getBegin();
	}

	
	/**
	 * タグ終端ソース文字列生成メソッド for JavaScript。<br>
	 * <br>
	 * 終了タグを生成します。<br>
	 * &lt;/TAG&gt; を作成。
	 * @return
	 */
	public String jsFunction_getTerminate(){
		return getTerminate();
	}

	/**
	 * タグ終端ソース文字列生成メソッド for JavaScript。<br>
	 * <br>
	 * 終了タグを生成します。<br>
	 * &lt;/TAG&gt; を作成。
	 * @return
	 */
	public String jsFunction_terminate(){
		return getTerminate();
	}
}

/* End of File */