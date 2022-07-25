package org.intra_mart.jssp.script.api.dom;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.intra_mart.jssp.util.RuntimeObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XMLを解析するパーサーオブジェクト。<br/>
 * <br/>
 * XML を解析するパーサーを提供します。<br/>
 * このオブジェクトでは、XML の各タグをノードとして管理します。<br/>
 * 各ノードは､属性と子供ノードを持ちます。<br/>
 * 属性と子供ノードは複数持つ事ができ、子供ノードは更に属性と子供ノードを持つ事が出来ます。
 * すべてのノードの頂点となる親ノード（ドキュメントノード）は、XML ソース中の最も外側のタグであり、<a href='#getDocumentElement'>getDocumentElement()</a> によって取得する事が出来ます。<br/>
 * ドキュメントノードが複数存在する場合は、XML 解析エラーとなりますのでご注意下さい。<br/>
 * <br/>
 * XMLを解析してDOMツリーを作成するには、コンストラクタを使います。<br/>
 * <br/>
 * 例えば、以下のようなＸＭＬ<br/>
 * <pre>
 * &lt;A&gt;
 *  &lt;B&gt;Ｂタグの中のテキスト&lt;/B&gt;
 *  &lt;C&gt;Ｃタグの中のテキスト&lt;/C&gt;
 * &lt;/A&gt;
 * </pre>
 * から、&lt;C&gt; タグに囲まれたテキスト値を取得する場合は以下のようにプログラムします。<br/> 
 * <pre>
 * var xml = &quot;&lt;A&gt;&lt;B&gt;Ｂタグの中のテキスト&lt;/B&gt;&lt;C&gt;Ｃタグの中のテキスト&lt;/C&gt;&lt;/A&gt;&quot;;
 * var dom = new XMLDocument(xml);
 * var elms = dom.getElementsByTagName(&quot;C&quot;);
 * var elm = elms[0];
 * var childs = elm.getChildNodes();
 * var data = childs[0].getValue();
 * Debug.print(&quot;C's text: &quot; + data);
 * </pre>
 * 
 * @scope public
 * @name XMLDocument
 */
public class XMLDocumentObject extends DOMNodeObject implements Serializable{

	private static final String PROP_NAME_4_JAVA_EXCEPTION = "javaException";
	private Document document;
	private boolean error = false;
	private String errorMessage = null;

	
	/**
	 * @param src
	 */
	public XMLDocumentObject(String src){
		try{
			org.xml.sax.InputSource is = new org.xml.sax.InputSource(new StringReader(src));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.document = db.parse(is);
			initialize(this.document.getDocumentElement());
		}
		catch(SAXException saxe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, saxe);
			this.error = true;
			this.errorMessage = saxe.getMessage();
		}
		catch(IOException ioe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, ioe);
			this.error = true;
			this.errorMessage = ioe.getMessage();
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			this.error = true;
			this.errorMessage = npe.getMessage();
		}
		catch(Exception e){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, e);
			this.error = true;
			this.errorMessage = e.getMessage();
		}
	}

    /**
     * JSSP実行環境への登録用ゼロパラメータコンストラクタ
     */
	public XMLDocumentObject(){
		super();
	}

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return 実行環境上での名称
	 */
	public String getClassName() {
		return "XMLDocument";
	}

	/**
	 * コンストラクタ。<br/>
	 * <br/>
	 * XML ソースを解析して各エンティティを操作するためのオブジェクトを構築します。<br/>
	 * 
	 * @scope public
	 * @param src String XML ソース
	 */
	public static synchronized Object jsConstructor(Context cx, 
													   Object[] args,
													   Function ctorObj,
													   boolean inNewExpr) {
		if(inNewExpr){
			// new 演算子によりコール
			if(args.length != 0){
				return new XMLDocumentObject(ScriptRuntime.toString(args[0]));
			}
			return new XMLDocumentObject("");
		}
		else{
			// 関数指定
		    return null;
		}
	}


	/**
	 * XMLの文字列を取得します。<br/>
	 * <br/>
	 * @scope public
	 * @return String XML文字列
	 */
	public Object jsFunction_getXmlString(){
	    try {
		    CharArrayWriter outputStream = new CharArrayWriter();
	        TransformerFactory transFactory = TransformerFactory.newInstance();
	        Transformer transformer = transFactory.newTransformer();
	        DOMSource source = new DOMSource(this.document);
	        StreamResult result = new StreamResult(outputStream);
	        transformer.setOutputProperty(OutputKeys.ENCODING , "UTF-16"); // サロゲートぺア文字対応（これを行わないと実体参照で出力される（例→&#131083））
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.transform(source, result);
	        return outputStream.toString();
        } catch (TransformerConfigurationException e) {
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, e);
            return null;
        } catch (TransformerException e) {
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, e);
            return null;
        }
	}

	/**
	 * Documentノードを取得します。<br/>
	 * <br/>
	 * @scope public
	 * @return DOMNode ドキュメントを示すノード
	 */
	public Object jsFunction_getDocumentElement(){
		return this;
	}

	/**
	 * 指定のタグにマッチするエレメントを取得します。<br/>
	 * <br/>
	 * XML ソース中から指定のタグに該当するエンティティを返します。<br/>
	 * 返却値は、{@link DOMNode} オブジェクトを要素に持つ配列です。<br/>
	 * 不整合が発生してデータが取得できなかった場合 null が返ります。
	 * 
	 * @scope public
	 * @param tagname String タグ名
	 * @return Array ノードオブジェクト({@link DOMNode})の配列
	 */
	public Object jsFunction_getElementsByTagName(String tagname){
		try{
			NodeList nodes = this.document.getElementsByTagName(tagname);
			List<DOMNodeObject> list = new ArrayList<DOMNodeObject>();
			int max = nodes.getLength();
			for(int idx = 0; idx < max; idx++){
				list.add(new DOMNodeObject(nodes.item(idx)));
			}
			return RuntimeObject.newArrayInstance(list.toArray());
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			return null;
		}
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
	public Object jsFunction_getElementById(String elementId){
		try{
			Node item = this.document.getElementById(elementId);
			if(item != null){
				return new DOMNodeObject(item);
			}

			return null;
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			return null;
		}
	}

	/**
	 * 新しいノードを作成します。<br/>
	 * <br/>
	 * @scope public
	 * @param tagName String タグ名 
	 * @return DOMNode 新しいノードオブジェクト
	 */
	public Object jsFunction_createElement(String tagName){
		try{
			return new DOMNodeObject(this.document.createElement(tagName));
		}
		catch(DOMException dome){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, dome);
			return null;
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			return null;
		}
	}

	/**
	 * 新しいテキストノードを作成します。<br/>
	 * <br/>
	 * @scope public
	 * @param data String テキスト 
	 * @return DOMNode 新しいノードオブジェクト
	 */
	public Object jsFunction_createTextNode(String data){
		try{
			return new DOMNodeObject(this.document.createTextNode(data));
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			return null;
		}
	}

	/**
	 * ドキュメントタイプを取得します。<br/>
	 * <br/>
	 * @scope public
	 * @return DOMDocumentType ドキュメントタイプオブジェクト
	 */
	public Object jsFunction_getDoctype(){
		try{
			return new DOMDocumentTypeObject(this.document.getDoctype());
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			return null;
		}
	}

	/**
	 * エラー発生のチェックを行います。<br/>
	 * <br/>
	 * XML ソースを解析中にエラーが発生していた場合 true を返します。<br/>
	 * @scope public
	 * @return Boolean true : エラーあり / false : エラーなし
	 */
	public boolean jsFunction_isError(){
		return this.error;
	}

	/**
	 * エラーメッセージの取得を行います。<br/>
	 * <br/>
	 * XML ソースを解析中にエラーが発生していた場合、そのエラーメッセージを返します。<br/>
	 * 
	 * @scope public
	 * @return String エラーメッセージ
	 */
	public String jsFunction_getErrorMessage(){
		return this.errorMessage;
	}

}
