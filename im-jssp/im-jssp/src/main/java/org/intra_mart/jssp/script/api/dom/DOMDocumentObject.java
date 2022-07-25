package org.intra_mart.jssp.script.api.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.intra_mart.common.aid.jdk.java.lang.StringUtil;
import org.intra_mart.jssp.util.RuntimeObject;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML 文書全体を表す DOM Document オブジェクト。<br/>
 * <br/>
 * このオブジェクトでは、XML の各タグをノードとして管理します。<br/>
 * 各ノードは､属性と子供ノードを持ちます。<br/>
 * 属性と子供ノードは複数持つ事ができ、子供ノードは更に属性と子供ノードを持つ事が出来ます。
 * すべてのノードの頂点となる親ノード（ドキュメントノード）は、XML ソース中の最も外側のタグであり、<a href='#getDocumentElement'>getDocumentElement()</a> によって取得する事が出来ます。<br/>
 * <br/>
 * XMLを解析してDOMツリーを作成するには、{@link XMLParser}オブジェクトのparse()メソッド、または、parseString()メソッドを使います。<br/>
 * （ドキュメントノードが複数存在する場合は、XML 解析エラーとなりますのでご注意下さい。）<br/>
 * <br/>
 * 例えば、以下のようなＸＭＬ
 * 
 * <pre>
 * &lt;A&gt;
 *   &lt;B&gt;Ｂタグの中のテキスト&lt;/B&gt;
 *   &lt;C&gt;Ｃタグの中のテキスト&lt;/C&gt;
 * &lt;/A&gt;
 * </pre>
 * 
 * から、&lt;C&gt; タグに囲まれたテキスト値を取得する場合は以下のようにプログラムします。
 * 
 * <pre>
 * 
 * var xmlString = &quot;&lt;A&gt;&lt;B&gt;Ｂタグの中のテキスト&lt;/B&gt;&lt;C&gt;Ｃタグの中のテキスト&lt;/C&gt;&lt;/A&gt;&quot;;
 * 
 * var parser = new XMLParser();
 * var dom = parser.parseString(xmlString);
 * 
 * if(!parser.isError()){
 *     var elms = dom.getElementsByTagName(&quot;C&quot;);
 *     var elm = elms[0];
 *     var childs = elm.getChildNodes();
 *     var data = childs[0].getValue();
 * 
 *     Debug.print(&quot;C's text: &quot; + data);
 * }
 * else {
 *     Debug.print("Error occured ! : " + parser.getErrorMessage() );
 * }
 * </pre>
 * 
 * @scope public
 * @name DOMDocument
 */
public class DOMDocumentObject extends DOMNodeObject implements Serializable{

	private Document document;
	private boolean error = false;
	private String errorMessage = StringUtil.EMPTY_STRING;

    /**
     * デフォルトコンストラクタ。 <br/>
     * <br/>
     */
	public DOMDocumentObject(){
		super();

		// 基本メソッドの登録
		try{
			String[] names = {
								"getDocumentElement",
								"getElementsByTagName",
								"getElementById",
								"createElement",
								"createTextNode",
								"getDoctype",
								"isError",
								"getErrorMessage"
							};
			this.defineFunctionProperties(names, DOMDocumentObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			System.out.println("Error in DOMDocumentObject constructor: " + e.getMessage());
		}
	}

    /**
     * コンストラクタ。 <br/>
     * <br/>
     * XML ドキュメントの構文解析に成功した場合のコンストラクタ。 <br/>
     * 
	 * @param document DOM Document インスタンス
     */
	public DOMDocumentObject(Document document){
		this();
		
		this.document = document;
		
		if(this.document != null){
			initialize(this.document.getDocumentElement());
		}
	}
    
	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return 実行環境上での名称
	 */
	public String getClassName() {
		return "DOMDocument";
	}

	/**
	 * Documentノードを取得します。<br/>
	 * <br/>
	 * @scope public
	 * @return DOMNode ドキュメントを示すノード
	 */
	public Object getDocumentElement(){
		return this;
	}

	/**
	 * 指定のタグにマッチするエレメントを取得します。<br/>
	 * <br/>
	 * XML ソース中から指定のタグに該当するエンティティを返します。<br/>
	 * 返却値は、{@link DOMNode} オブジェクトを要素に持つ配列です。
	 * 
	 * @scope public
	 * @param tagname String タグ名
	 * @return Array ノードオブジェクト({@link DOMNode})の配列
	 */
	public Object getElementsByTagName(String tagname){
		
		NodeList nodes = this.document.getElementsByTagName(tagname);
		List<DOMNodeObject> list = new ArrayList<DOMNodeObject>();
		int max = nodes.getLength();
		for(int idx = 0; idx < max; idx++){
			list.add(new DOMNodeObject(nodes.item(idx)));
		}
		return RuntimeObject.newArrayInstance(list.toArray());

	}

	/**
	 * 識別子に該当するノードを取得します。<br/>
	 * <br/>
	 * 指定の識別子にマッチしたノードを返します。<br/>
	 * 指定の識別子を持つノードが存在しなかった場合 null を返します。<br/>
	 * 
	 * @scope public
	 * @param elementId String ノード識別子
	 * @return DOMNode ノードオブジェクト
	 */
	public Object getElementById(java.lang.String elementId){
		
		Node item = this.document.getElementById(elementId);
		
		if(item != null){
			return new DOMNodeObject(item);
		}

		return null;
	}

	/**
	 * 新しいノードを作成します。<br/>
	 * <br/>
	 * 
	 * @scope public
	 * @param tagName String タグ名 
	 * @return DOMNode 新しいノードオブジェクト
	 */
	public Object createElement(java.lang.String tagName){
		
		Element element = null;
		try{
			element = this.document.createElement(tagName);
		}
		catch(DOMException dome){
			this.error = true;
			this.errorMessage = dome.getMessage();
			
			return null;
		}

		setSuccessStatus();
		return new DOMNodeObject(element);

	}

	/**
	 * 新しいテキストノードを作成します。<br/>
	 * <br/>
	 * 
	 * @scope public
	 * @param data String テキスト 
	 * @return DOMNode 新しいノードオブジェクト
	 */
	public Object createTextNode(java.lang.String data){
		return new DOMNodeObject(this.document.createTextNode(data));
	}

	/**
	 * ドキュメントタイプを取得します。<br/>
	 * <br/>
	 * 
	 * @scope public
	 * @return DOMDocumentType ドキュメントタイプオブジェクト
	 */
	public Object getDoctype(){
		return new DOMDocumentTypeObject(this.document.getDoctype());
	}	
	
	
	/**
	 * エラー発生のチェックを行います。<br/>
	 * <br/>
	 * 新しいノード作成時にエラーが発生していた場合 true を返します。<br/>
	 * 
	 * @scope public
	 * @return Boolean true : エラーあり / false : エラーなし
	 */
	public boolean isError(){
		return this.error;
	}

	
	/**
	 * エラーメッセージの取得を行います。<br/>
	 * <br/>
	 * 新しいノード作成時にエラーが発生していた場合、そのエラーメッセージを返します。<br/>
	 * 
	 * @scope public
	 * @return String エラーメッセージ
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}
	
	/**
	 * 成功状態を設定します。
	 */
	private void setSuccessStatus(){
		this.error = false;
		this.errorMessage = StringUtil.EMPTY_STRING;
	}

}
