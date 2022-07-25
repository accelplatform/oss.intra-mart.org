package org.intra_mart.jssp.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * データオブジェクト for Java Script
 */
public class ValueObject extends ScriptableObject implements Serializable{

	private static final ScriptableObject PROTOTYPE = new ValueObject();

	public ValueObject(){
		super();
		// 基本メソッドの登録
		try{
			String[] names = {"toString","valueOf","hasOwnProperty"};
			this.defineFunctionProperties(names, ValueObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			System.out.println("Error in JavaScript anonymous-Object constructor: " + e.getMessage());
		}
	}
	
	
	/**
	 * @param args 引数群
	 */
	public ValueObject(Map args){
		super();

		try{
			if(args != null){
				// 引数値の設定
				Iterator view = args.entrySet().iterator();
				while (view.hasNext()) {
					Map.Entry e = (Map.Entry) view.next();
					this.defineProperty((String) e.getKey(), e.getValue(), ScriptableObject.EMPTY);
				}
			}
		}
		catch(Exception e){
			// TODO [OSS-JSSP] ログ機能実装
			SimpleLog.logp(Level.WARNING, "Error in ValueObject constructor: " + e.getMessage(), e);
		}

		// 基本メソッドの追加登録
		this.setPrototype(PROTOTYPE);
	}

	private String className = "anonymous-Object";

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * JavaScript 実行環境上での名称を設定します。
	 * @param classNameJavaScript 実行環境上での名称
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * このオブジェクトの文字列表現を返します。
	 * @return 文字列表現
	 */
	public String toString(){
		return this.getClassName();
	}

	/**
	 * このオブジェクトの数値表現を返します。
	 * @return 数値表現
	 */
	public double valueOf(){
		return Double.NaN;
	}
	
	/**
	 * 指定した名前のプロパティがオブジェクトにあるかどうかを表す真偽値値を返します
	 * @param scriptable
	 * @return 指定した名前のプロパティがオブジェクトにある場合は true、それ以外は false。
	 */
	public boolean hasOwnProperty(Scriptable scriptable){
        String property = ScriptRuntime.toString(scriptable);
        if (this.has(property, this)){
            return true;
        }
        else{
        	return false;
        }
	}        
}
