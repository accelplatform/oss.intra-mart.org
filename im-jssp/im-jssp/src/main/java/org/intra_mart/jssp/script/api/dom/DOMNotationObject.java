package org.intra_mart.jssp.script.api.dom;

import java.io.Serializable;

import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Notation;

/**
 * ノーテーションオブジェクト。<br/>
 * <br/>
 * ノーテーションを示すオブジェクトです。<br/>
 * 
 * @scope public
 * @name DOMNotation
 */
public class DOMNotationObject extends DOMNodeObject implements Serializable{

	private static final ScriptableObject PROTOTYPE = new DOMNotationObject();

	private Notation item;

	/**
	 * プロトタイプ用のコンストラクタです。
	 */
	public DOMNotationObject(){
		super();
		// 基本メソッドの登録
		try{
			String[] names = {"getPublicId","getSystemId","toString"};
			this.defineFunctionProperties(names, DOMNotationObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			System.out.println("Error in DOMNotationObject constructor: " + e.getMessage());
		}
	}

	/**
	 * @param item
	 * @throws NullPointerException
	 */
	public DOMNotationObject(Notation item) throws NullPointerException{
		super(item);
		this.item = item;
		// 基本メソッドの追加登録
		this.setPrototype(PROTOTYPE);
	}

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return 実行環境上での名称
	 */
	public String getClassName() {
		return "DOMNotation";
	}

	/**
	 * パブリックIDを取得します。<br/>
	 * <br/>
	 * 
	 * @scope public
	 * @return String パブリックID
	 */
	public String getPublicId(){
		return this.item.getPublicId();
	}

	/**
	 * システムIDを取得します。<br/>
	 * <br/>
	 * 
	 * @scope public
	 * @return String システムID
	 */
	public String getSystemId(){
		return this.item.getSystemId();
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.script.api.dom.DOMNodeObject#toString()
	 */
	public String toString(){
		return getClassName();
	}
}
