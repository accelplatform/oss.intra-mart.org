package org.intra_mart.jssp.util;

import java.io.Serializable;

import org.intra_mart.jssp.script.FoundationScriptScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RuntimeObject implements Serializable{

	/**
	 * JSSP実行環境の基本スコープを返却します。
	 * すべての実行環境が、このスコープを共有して利用します。
	 * @return JSSP実行環境の基本スコープ
	 */
	public static ScriptableObject scope(){
		return FoundationScriptScope.instance();
	}

	/**
	 * グローバル値の取得メソッド<br>
	 * JSSP実行環境の基本スコープから、指定の変数名称にマップされている値を取得します。
	 * 
	 * @param name グローバル変数名称
	 * @return JavaScript のデータ
	 */
	public static Object get(String name){
		return FoundationScriptScope.instance().get(name, null);
	}


	/**
	 * 要素数「0」の配列を作成します。
	 * @return JavaScriptで利用可能な配列
	 */
	public static NativeArray newArrayInstance(){
		return newArrayInstance(0);
	}

	
	/**
	 * 引数で指定された要素数の配列を作成します。
	 * @param max 要素数
	 * @return JavaScriptで利用可能な配列
	 */
	public static NativeArray newArrayInstance(int max){
		// 指定要素数の空配列を返却
		return (NativeArray) Context.getCurrentContext().newArray(FoundationScriptScope.instance(), max);
	}


	/**
	 * 引数で指定されたオブジェクト配列を格納したJavaScript 上で利用可能な配列を作成します。
	 * @param list 配列にする要素群
	 * @return JavaScriptで利用可能な配列
	 */
	public static NativeArray newArrayInstance(Object[] list){
		if(list != null){
			if(list.length > 0){
								
				// Context#newArray()は、正確に「Object配列」でなければならない（＝「Objectのサブクラス配列」はNG）
				Object[] obj = new Object[list.length];
				System.arraycopy(list, 0, obj, 0, list.length);
				
				// 指定要素群を持つ配列を返却
				return (NativeArray) Context.getCurrentContext().newArray(FoundationScriptScope.instance(), obj);
			}
		}

		// 要素が空の配列を返却
		return newArrayInstance();
	}


	/**
	 * JavaScript 上で利用可能なオブジェクト変数を作成します。<br>
	 * 引数を必要としないコンストラクタを呼び出す場合には、
	 * 引数 args に対して null を指定して下さい。
	 *  
	 * @param constructorName コンストラクタとなる関数名
	 * @param args コンストラクタに渡す引数群
	 * @return JavaScript のオブジェクトデータ
	 */
	public static Scriptable newObject(String constructorName, Object[] args) {
		Object cFunc = get(constructorName);
		if(cFunc != null){
			if(cFunc instanceof Scriptable){
				return Context.getCurrentContext().newObject(((Scriptable) cFunc).getParentScope(), constructorName, args);
			}
		}
		return Context.getCurrentContext().newObject(FoundationScriptScope.instance(), constructorName, args);
	}


	/**
	 * Dateオブジェクトを作成します。
	 * @return JavaScript 上で利用可能なDateオブジェクト
	 */
	public static Scriptable newDate() {
		return Context.getCurrentContext().newObject(FoundationScriptScope.instance(), "Date", null);
	}


	/**
	 * 引数で指定された時間を元に、Dateオブジェクトを作成します。
	 * @param timeMillis 1970/1/1 からの経過時間（ミリ秒）
	 * @return JavaScript 上で利用可能なDateオブジェクト
	 */
	public static Scriptable newDate(long timeMillis) {
		Object[] args = { new Long(timeMillis) };
		return Context.getCurrentContext().newObject(FoundationScriptScope.instance(), "Date", args);
	}
}
