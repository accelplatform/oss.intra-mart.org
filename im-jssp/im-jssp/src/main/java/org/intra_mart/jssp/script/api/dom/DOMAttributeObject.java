package org.intra_mart.jssp.script.api.dom;

import java.io.Serializable;

import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Node;

/**
 * ノードの属性オブジェクト。<br/>
 * <br/>
 * ノードの属性情報を保持するオブジェクトです。<br/>
 * 
 * @scope public
 * @name DOMAttribute
 */
public class DOMAttributeObject extends ScriptableObject implements Serializable{

	private static final ScriptableObject PROTOTYPE = new DOMAttributeObject();

	private Node item;
	private DOMNodeObject parentNode = null;

	/**
	 * プロトタイプ用のコンストラクタです。
	 */
	public DOMAttributeObject(){
		// 基本メソッドの登録
		try{
			String[] names = {"getName","getValue","getParentNode","toString"};
			this.defineFunctionProperties(names, DOMAttributeObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			System.out.println("Error in DOMAttributeObject constructor: " + e.getMessage());
		}
	}

	/**
	 * @param item
	 */
	public DOMAttributeObject(Node item){
		this.item = item;
		// 基本メソッドの追加登録
		this.setPrototype(PROTOTYPE);
	}
	

	/**
	 * @param item
	 * @param parentNode
	 */
	public DOMAttributeObject(Node item, DOMNodeObject parentNode){
		this.parentNode = parentNode;
		this.item = item;
		// 基本メソッドの追加登録
		this.setPrototype(PROTOTYPE);
	}

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return 実行環境上での名称
	 */
	public String getClassName() {
		return "DOMAttribute";
	}

	/**
	 * 属性名を取得します。<br/>
	 * <br/>
	 * @scope public
	 * @return String 属性名
	 */
	public String getName(){
		return this.item.getNodeName();
	}

	/**
	 * 属性値を取得します。<br/>
	 * <br/>
	 * @scope public
	 * @return String 属性値
	 */
	public String getValue(){
		return this.item.getNodeValue();
	}

	/**
	 * このオブジェクトの文字列表現を取得メソッド
	 * 
	 * @scope private
	 * @return "name=value" という文字列を返却
	 */
	public String toString(){
		return getName().concat("=").concat(getValue());
	}

	/**
	 * 親ノードを取得します。<br/>
	 * <br/>
	 * 親ノードを持たない場合は null を返します。<br/>
	 * 
	 * @scope public
	 * @return DOMNode 親ノードのオブジェクト
	 */
	public DOMNodeObject getParentNode(){
		return parentNode;
	}
}
