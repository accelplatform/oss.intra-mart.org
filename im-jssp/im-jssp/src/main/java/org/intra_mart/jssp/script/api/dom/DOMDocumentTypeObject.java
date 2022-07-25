package org.intra_mart.jssp.script.api.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.intra_mart.jssp.util.RuntimeObject;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Notation;

/**
 * ドキュメントタイプオブジェクト。<br/>
 * <br/>
 * ドキュメントタイプを示すオブジェクトです。<br/>
 * 
 * @scope public
 * @name DOMDocumentType
 */
public class DOMDocumentTypeObject extends ScriptableObject implements Serializable{

	private static final ScriptableObject PROTOTYPE = new DOMDocumentTypeObject();

	private DocumentType item;

	/**
	 * プロトタイプ用のコンストラクタです。
	 */
	public DOMDocumentTypeObject(){
		// 基本メソッドの登録
		try{
			String[] names = {
								"getName",
								"getPublicId",
								"getSystemId",
								"getInternalSubset",
								"getEntities",
								"getNotations",
								"toString"
							};
			this.defineFunctionProperties(names, DOMDocumentTypeObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			System.out.println("Error in DOMDocumentTypeObject constructor: " + e.getMessage());
		}
	}
	
	/**
	 * @param item
	 * @throws NullPointerException
	 */
	public DOMDocumentTypeObject(DocumentType item) throws NullPointerException{
		if(item != null){
			this.item = item;
			// 基本メソッドの追加登録
			this.setPrototype(PROTOTYPE);
		}
		else{
			throw new NullPointerException();
		}
	}

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return 実行環境上での名称
	 */
	public String getClassName() {
		return "DOMDocumentType";
	}

	/**
	 * ノードの名称を取得します。<br/>
	 * <br/>
	 * 
	 * @scope public
	 * @return String ノードの名称
	 */
	public String getName(){
		return this.item.getName();
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

	/**
	 * インターナルサブセットを取得します。<br/>
	 * <br/>
	 * 
	 * @scope public
	 * @return String インターナルサブセット
	 */
	public String getInternalSubset(){
		return this.item.getInternalSubset();
	}

	/**
	 * エンティティを取得します。<br/>
	 * <br/>
	 * このオブジェクトから取得できるエンティティをすべて取得します。<br/>
	 * 配列の要素は、{@link DOMEntity} オブジェクトです。<br/>
	 * 
	 * @scope public
	 * @return Array このオブジェクトから取得できるエンティティ({@link DOMEntity})を要素に持つ配列
	 */
	public Object getEntities(){
		NamedNodeMap nnm = this.item.getEntities();
		if(nnm != null){
			int max = nnm.getLength();
			List<DOMEntityObject> list = new ArrayList<DOMEntityObject>();
			for(int idx = 0; idx < max; idx++){
				try{
					list.add(new DOMEntityObject((Entity) nnm.item(idx)));
				}
				catch(Exception e){
					// それはないハズ
				}
			}
			return RuntimeObject.newArrayInstance(list.toArray());
		}
		return RuntimeObject.newArrayInstance();
	}

	/**
	 * ノーテーションを取得します。<br/>
	 * <br/>
	 * このオブジェクトから取得できるノーテーションをすべて取得します。<br/>
	 * 配列の要素は、{@link DOMNotation} オブジェクトです。<br/>
	 * @scope public
	 * @return Array このオブジェクトから取得できるノーテーション({@link DOMNotation})を要素に持つ配列
	 */
	public Object getNotations(){
		NamedNodeMap nnm = this.item.getNotations();
		if(nnm != null){
			int max = nnm.getLength();
			List<DOMNotationObject> list = new ArrayList<DOMNotationObject>();
			for(int idx = 0; idx < max; idx++){
				try{
					list.add(new DOMNotationObject((Notation) nnm.item(idx)));
				}
				catch(Exception e){
					// それはないハズ
				}
			}
			return RuntimeObject.newArrayInstance(list.toArray());
		}
		return RuntimeObject.newArrayInstance();
	}

	/**
	 * このオブジェクトの文字列表現を取得メソッド
	 * ノードの名称を返却します。
	 * 
	 * @scope private
	 * @return ノードの名称
	 */
	public String toString(){
		return getName();
	}
}