
package org.intra_mart.jssp.view.tag;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import org.intra_mart.jssp.exception.IllegalTagException;
import org.intra_mart.jssp.exception.NoSuchTagException;
import org.intra_mart.jssp.exception.TagRuntimeException;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.util.ValueObject;
import org.intra_mart.jssp.view.ViewScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * <!-- JavaScriptオブジェクト -->
 * &lt;IMART&gt; タグ管理オブジェクト。<BR>
 * <BR>
 * プレゼンテーション・ページと連携するためのＡＰＩを持つオブジェクトです。<BR>
 * @name Imart
 * @scope public
 */
public class ImartObject extends ScriptableObject implements java.io.Serializable{
	
	private static Hashtable<String, Object> _constValue = new Hashtable<String, Object>();
	private static ImartTagTypeManger _foundationTag = ImartTagTypeManger.getInstance();

	private static ScriptableObject _tagType = new ValueObject();

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName(){
		return "Imart";
	}

	/**
	 * &lt;IMART&gt; タグの指定 type 関数を実行し、ＨＴＭＬソースを返却します。
	 * 
	 * @param cx 実行環境
	 * @param scope 変数スコープ
	 * @param attr タグ関数へのＰＰからの引数
	 * @param inner タグのネスト情報
	 * @return 実行結果ＨＴＭＬソース
	 * @throws JavaScriptException avaScript 実行時エラー
	 */
	public static String callFunction(Context cx, Scriptable scope, Scriptable attr, Scriptable inner) 
								throws JavaScriptException {
		
		Object typeName = attr.get("type", null);

		if(typeName == Scriptable.NOT_FOUND){
			throw new IllegalTagException("<IMART> tag type is undefined.");
		}

		// 関数の取得
		if(! (typeName instanceof String)){
			throw new NoSuchTagException(String.valueOf(typeName));
		}
		return callFunction((String) typeName, cx, scope, attr, inner);
	}

	/**
	 * &lt;IMART&gt; タグの実行
	 * 
	 * @param name 実行するタグ名
	 * @param cx JavaScript の実行コンテキスト
	 * @param scope 実行スコープ
	 * @param attr タグに対する属性情報
	 * @param inner タグに挟まれている部分の情報
	 * @return ＨＴＭＬソース（文字列）
	 * 
	 * @throws JavaScriptException
	 * @throws NoSuchTagException
	 * @throws IllegalArgumentException
	 */
	public static String callFunction(String name, Context cx, Scriptable scope, Scriptable attr, Scriptable inner) throws JavaScriptException, NoSuchTagException, IllegalArgumentException{
		try{
			return _foundationTag.invoke(name, attr, inner);
		}
		catch(NoSuchTagException nste){
			
			// 関数の実行と返却
			Object value = _tagType.get(name, null);
			
			if(value != Scriptable.NOT_FOUND){
				
				Function func = (Function) value;
				Object[] args = { attr, inner };
				
				Object result = func.call(cx, scope, scope, args);
				
				if(result instanceof String){
					return (String) result; 
				}
				else{
					return ""; 
				}
			}

			throw nste;
		}
		catch(IllegalAccessException iae){
			throw new TagRuntimeException(name, iae);
		}
		catch(InvocationTargetException ite){
			Throwable t = ite.getCause();
			if(t != null){
				if(t instanceof JavaScriptException){
					// JavaScript 実行時例外
					throw (JavaScriptException) t;
				}
				if(t instanceof RuntimeException){
					// 実行例外
					throw (RuntimeException) t;
				}
				if(t instanceof Error){
					// 致命的エラー
					throw (Error) t;
				}
			}

			throw new TagRuntimeException(name, ite);
		}
	}

	
	/**
	 * &lt;IMART&gt; タグ属性値の取得メソッド
	 * 
	 * @param cx 実行環境
	 * @param scope 変数スコープ
	 * @param name 定数値名
	 * @param def 設定値取得失敗時のデフォルト値
	 * @return 
	 * @throws JavaScriptException
	 */
	public static Object getConstant(Context cx, Scriptable scope, Object name, Object def) throws JavaScriptException{
		
		if(_constValue.containsKey(name)) {
			Object value = _constValue.get(name);
			
			if(value instanceof Function){
				return ((Function) value).call(cx, scope, scope, new Object[0]);
			}
			else{
				return value;
			}
		}
		else{
			return def;
		}
	}

	/**
	 * ＨＴＭＬソースのコンパイルを行います。<br>
	 * <br> 
	 * 引数のＨＴＭＬソース文字列を解析してコンパイルします。<br>
	 * 返却値は InnerText のインスタンスです。これは、ＨＴＭＬ生成のための実行可能オブジェクトです。<br>
	 * この {@link InnerText}のインスタンスでは、{@link InnerText#execute}() メソッドを実行する事により、
	 * 解析された &lt;IMART&gt; が順次実行されます。<br>
	 * {@link InnerText#execute}() メソッドの実行結果として、生成されたＨＴＭＬソースが文字列として返されます。<br>
	 * 引数に文字列以外の値を指定した場合の動作は保証外です。<br>
	 * 
	 * @scope public
	 * @param src String 解析対象ＨＴＭＬソース
	 * @return InnerText 実行可能オブジェクト
	 */
	public static Object jsStaticFunction_compile(String src){
		// HTML ソースとして解析結果を返却
		return new InnerTextObject(new ViewScope(new StringReader(src)));
	}

	/**
	 * &lt;IMART&gt; タグ内の属性値に対する定数を定義します。<br>
	 * <br> 
	 * &lt;IMART&gt; タグ内で使用される属性値に対する定数定義をします。<br>
	 * &lt;IMART&gt; タグ内で宣言される各属性に対する属性値のうち、
	 * ダブルクォートで囲まれている定数値に対して適応されます。<br>
	 * &lt;IMART&gt; タグ内で宣言されたダブルクォートで囲まれた定数属性値が定義名称と一致する場合、
	 * 実行時に自動的に実行時値が適応されます。<br>
	 * 
	 * @scope public
	 * @param name String 定義名称
	 * @param value Object 実行時値
	 */
	public static void jsStaticFunction_defineAttribute(Object name, Object value){
		if(name instanceof String){
			_constValue.put((String)name, value);
		}
	}

	/**
	 * &lt;IMART&gt; タグの実行関数を定義します。<br>
	 * <br> 
	 * 引数 name で指定した定義名称が &lt;IMART&gt; タグの type 属性での指定名称になります。<br>
	 * 第２引数には、&lt;IMART&gt; タグの type 属性により定義名称でコールされた際に実行する関数を指定します。<br>
	 * 関数内では、実行時に２つの引数を取ります。<br>
	 * <br>
	 * <strong>function my_tag(oAttr, oInner){ ... }</strong><br>
	 * <br>
	 * 実行関数内の第１引数 oAttr は、&lt;IMART&gt; タグにより指定された引数群になります。<br>
	 * 第２引数 oInner は、&lt;IMART&gt; と &lt;/IMART&gt; に挟まれた内部ソース（実行オブジェクト形式）になります。<br>
	 * 引数が不適切であった場合の動作は保証外です。<br>
	 * 
	 * @scope public
	 * @param name String 定義名称
	 * @param func Function 実行関数
	 */
	public static void jsStaticFunction_defineType(Object name, Object func){
		if(name instanceof String && func instanceof Function){
			_tagType.defineProperty((String) name, func, ScriptableObject.EMPTY);
		}
	}

	/**
	 * 任意の &lt;IMART&gt; タグ定義関数を実行します。<br>
	 * <br> 
	 * 現在定義されている &lt;IMART&gt; タグの任意の定義関数を実行します。<br>
	 * 第１引数 name には、&lt;IMART&gt; タグの type 属性に定義されている定義名称を指定します。<br>
	 * 第２引数 oAttr は、そのタグ関数の実行に必要なタグ引数をオブジェクトの形式で渡します。<br>
	 * タグ引数オブジェクトは、タグ属性名をプロパティ名として、対応する属性値をプロパティ値としたオブジェクトです。<br>
	 * 第３引数 oInner は、&lt;IMART&gt; および &lt;/IMART&gt; に挟まれた部分の実行可能オブジェクトになります。<br>
	 * 
	 * @scope public
	 * @deprecated 代替えのメソッドはありません。
	 * @param name String &lt;IMART&gt; タグ定義名称
	 * @param oAttr Object タグ引数
	 * @param oInner InnerText 実行可能オブジェクト
	 * @return String 実行結果ＨＴＭＬソース
	 * @throws JavaScriptException
	 */
	public static String jsStaticFunction_execute(String name, Scriptable attr, Scriptable inner) throws JavaScriptException{
		try{
			Scriptable scope = ScriptScope.current();
			Context cx = Context.getCurrentContext();
			return callFunction(name, cx, scope, attr, inner);
		}
		catch(NoSuchTagException nste){
			return "";		// 返却値不適切時のデフォルト値
		}
	}


	/**
	 * &lt;IMART&gt; タグの指定 type 関数の取得メソッド for JavaScript
	 * キーワードに該当する type 実行関数を返却
	 * 指定キーワードが未定義に場合は undefined を返却
	 * （Imart.defineType()を利用して定義された &lt;IMART&gt; タグを判定対象とします）
	 * 
	 * @param name タグの type 名称
	 * @return 該当関数
	 */
	public static Object jsStaticFunction_getType(String name){
		// 指定キーワードが登録済みなら該当関数を返却
		if(_tagType.has(name, null)){
			return _tagType.get(name, null); 
		}

		// キーワードが不適切な場合 undefined を返却
		return Undefined.instance;
	}

	/**
	 * &lt;IMART&gt; タグ定義の存在チェックを行います。<br>
	 * <br> 
	 * 指定の定義名称が &lt;IMART&gt; タグの type 属性に定義されているかどうかを判定します。<br>
	 * （Imart.defineType()を利用して定義された &lt;IMART&gt; タグを判定対象とします）<br>
	 * 指定の定義名称が &lt;IMART&gt; タグの type 属性に対して定義済みである場合、
	 * その定義名称で &lt;IMART&gt; を利用可能である事を示します。<br>
	 * 
	 * @scope public
	 * @param name String 定義名称
	 * @return Boolean true : 存在する / false : 存在しない
	 */
	public static boolean jsStaticFunction_isType(String name){
		// 登録状態を返却
		return _tagType.has(name, null);
	}

}
