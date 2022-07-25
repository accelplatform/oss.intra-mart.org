package org.intra_mart.common.aid.jdk.org.w3c.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import org.intra_mart.common.aid.jdk.java.util.ExtendedProperties;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * DOM をベースとした不変のプロパティセットを表します。
 * このクラスは、DOM を元データとして、{@link java.util.Properties} として
 * 利用できるインタフェースを提供します。<p>
 * DOM の各情報に対しては、XPath 形式の文字列をプロパティ名として
 * {@link java.util.Properties#getProperty(java.lang.String)} により
 * アクセスできます。
 * <p>
 * プロパティ名の例<br>
 * <ul>
 * <li><code>/root/elm1</code> ・・・タグに挟まれた文字列情報
 * <li><code>/root/elm1/@attr</code> ・・・タグの属性
 * <li><code>/root/elm1/text()</code> ・・・タグに挟まれた文字列情報を連結したもの
 * <li><code>/root/elm1/comment()</code> ・・・コメントを連結したもの
 * </ul>
 * <br>
 * 上記以外の XPath 構文は、解釈できません。
 * また、<code>..</code> や <code>//</code> 等を用いた相対指定もできません。
 * <p>
 * 取得できる値は、テキストノードおよび属性(attribute)ノードの値のみです。
 * コメントノード等の値は取得できません。
 *
 */
public class ElementProperties extends ExtendedProperties{
	/**
	 * プロパティのリスト
	 */
	private Map propertyList = new HashMap();
	/**
	 * このオブジェクトがラップするエレメントノード
	 */
	private Element element = null;

	/**
	 * 指定のエレメントの情報をプロパティズ化します。
	 * @param element プロパティズ化するエレメントノード
	 */
	public ElementProperties(Element element){
		super();
		if(element == null){
			throw new NullPointerException("element is null.");
		}
		this.node2Properties("/", "/", element);
	}

	/**
	 * 指定のエレメントの情報をプロパティズ化します。
	 * @param element プロパティズ化するエレメントノード
	 * @param parent 基礎データ
	 */
	public ElementProperties(Element element, Properties parent){
		super(parent);
		if(element == null){
			throw new NullPointerException("element is null.");
		}
		this.node2Properties("/", "/", element);
	}

	/**
	 * 現在のプロパティズの元となったエレメントノードを返します。
	 * @return エレメントノード
	 */
	public Element getElement(){
		return this.element;
	}

	/**
	 * DOM からプロパティ値に変換して、このインスタンスに保管します。
	 */
	private void node2Properties(String parentName, String absoluteParentName, Element node){
		String absolutePlefix = absoluteParentName.concat("child::").concat(node.getNodeName());
		String simplePlefix = parentName.concat(node.getNodeName());

		// 属性値の解釈
		if(node.hasAttributes()){
			NamedNodeMap attrs = node.getAttributes();
			for(int idx = 0; idx < attrs.getLength(); idx++){
				Node item = attrs.item(idx);
				String nodeValue = item.getNodeValue();
				if(nodeValue != null){
					this.defineProperty(absolutePlefix + "/attribute::" + item.getNodeName(), nodeValue);
					this.defineProperty(simplePlefix + "/@" + item.getNodeName(), nodeValue);
				}
			}
		}

		// 子ノードの解釈
		if(node.hasChildNodes()){
			boolean hasTextNode = false;
			StringBuffer textValue = new StringBuffer();
			boolean hasCommentNode = false;
			StringBuffer commentValue = new StringBuffer();
			NodeList list = node.getChildNodes();
			for(int idx = 0; idx < list.getLength(); idx++){
				Node item = list.item(idx);
				if(item.getNodeType() == Node.TEXT_NODE){
					String nodeValue = item.getNodeValue();
					if(nodeValue != null){
						hasTextNode = true;
						textValue.append(nodeValue);
					}
				}
				else if(item.getNodeType() == Node.ELEMENT_NODE){
					// 再起処理
					this.node2Properties(simplePlefix + "/", absolutePlefix + "/", (Element) item);
				}
				else if(item.getNodeType() == Node.COMMENT_NODE){
					String nodeValue = item.getNodeValue();
					if(nodeValue != null){
						hasCommentNode = true;
						commentValue.append(nodeValue);
					}
				}
			}

			if(hasTextNode){
				this.defineProperty(absolutePlefix, textValue.toString());
				this.defineProperty(simplePlefix, textValue.toString());
				this.defineProperty(simplePlefix + "/text()", textValue.toString());
			}
			if(hasCommentNode){
				this.defineProperty(simplePlefix + "/comment()", commentValue.toString());
			}
		}
	}

	/**
	 * 指定のプロパティ名に指定のデータをマッピングします。
	 * @param name プロパティ名
	 * @param value プロパティ値
	 */
	private void defineProperty(String name, String value){
		this.setProperty(name, value);
		Collection values = (Collection) this.propertyList.get(name);
		if(values == null){
			values = new ArrayList();
			this.propertyList.put(name, values);
		}
		values.add(value);
	}

	/**
	 * 指定のキーにマップされた値のリストを返します。<br>
	 * XML 中で同名のタグが連続している場合など、タグや属性から生成された
	 * キー名称が同一のもののすべてを返します。
	 * @param key キー
	 * @return キーにマップされた値の配列
	 */
	public String[] getProperties(String key){
		Collection values = (Collection) this.propertyList.get(key);
		if(values != null){
			return (String[]) values.toArray(new String[values.size()]);
		}
		else{
			return new String[0];
		}
	}
}

