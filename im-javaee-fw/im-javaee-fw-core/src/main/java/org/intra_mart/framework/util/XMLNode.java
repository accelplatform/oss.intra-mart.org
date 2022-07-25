/*
 * 作成日: 2004/01/12
 */
package org.intra_mart.framework.util;

/**
 * XmlNodeを管理するクラスです。
 * @author INTRAMART
 * @version 1.0
 */

import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLNode {

	private Node node;
	private String value = null;
	private Text text = null;
	private String uri = null;
	private Node parent_node = null;

	/**
	 * コンストラクタです。 
	 * @param d Document 
	 */
	public XMLNode(Document d) {
		  node = d;
	}

	/**
	 * コンストラクタです。 
	 * @param n Element 
	 */
	public XMLNode(Element n) {
		  node = n;

	}

	/**
	 * コンストラクタです。 
	 * @param n Attr
	 */
	public XMLNode(Attr n) {
		  node = n;
	}

	/**
	 * コンストラクタです。 
	 * @param Node
	 */
	public XMLNode(Node n) {
		  node = n;
	}

	/**
	 * コンストラクタです。 
	 * @param Node
	 * @param Node
	 */
	public XMLNode(Node p,Node n) {
		  parent_node = p;
		  node = n;
	}

	/**
	 * ノード名を返却します。
	 * @return String ノード名 
	 */
	public String getNodeName() {
		return node.getNodeName();
	}

	/**
	 * ノード名を設定します。
	 * @param String name
	 */
	public void setNodeName(String name){

		if(node instanceof Element) {

			Element ele = (Element)node;

			NodeList list = ele.getChildNodes();

			Node new_node = ele.getOwnerDocument().createElement(name);

			if (list.getLength() > 0 ) {
				Node items[] = new Node[list.getLength()];
				// なぜか一回、保管しなおさないとうまくいかないみたい(泣)
				for (int i = 0 ; i < list.getLength() ; ++i) {
				    items[i] = list.item(i);
				}
				for (int i = 0 ; i < items.length ; ++i) {
				    new_node.appendChild(items[i]);
				}
			}
			ele.getParentNode().replaceChild(new_node,ele);
		}
	}

	/**
	 * タグの配列を返却します。 
	 * @param String tags
	 * @return Vector タグ
	 */
	private Vector parseTags(String tags) {
		Vector v = new Vector();
		if(tags != null) {
			StringTokenizer st = new StringTokenizer(tags,"/");

			while(st.hasMoreTokens()){
				v.add(st.nextToken());
			}
		}
		return v;
	}


	/**
	 * XMLNodeを返却します。
	 * @param String tags
	 * @return XMLNode
	 */
	public XMLNode lookup(String tags) {
		return lookup(parseTags(tags));
	}

	/**
	 * XMLNodeを返却します。
	 * @param Vector tags
	 * @return XMLNode
	 */
	public XMLNode lookup(Vector tags) {

		XMLNode res = null;

		if (node == null) {
		    return res;
		}
		if (tags.size() == 0) {
		    return this;
		}

		String tag = (String)tags.elementAt(0);
		tags.removeElementAt(0);

		if (node instanceof Document) {
			Document doc = (Document)node;

			if(doc.hasChildNodes()) {
				NodeList nlist = doc.getChildNodes();
				for (int i = 0 ;  i < nlist.getLength() ; ++i) {
					if(tag.equals(nlist.item(i).getNodeName())) {
						res = new XMLNode(nlist.item(i));
						if (tags.size() > 0) {
						    res = res.lookup(tags);
						}
						break;
					}
				}
			}
		} else if (node instanceof Element) {
			Element ele = (Element)node;

			NamedNodeMap alist = ele.getAttributes();
			if(alist != null) {
				if(tags.size() == 0 && alist.getLength() > 0) {
					Attr at = ele.getAttributeNode(tag);
					if (at != null) {
					    return new XMLNode(at);
					}
				}
			}

			if(ele.hasChildNodes()) {
				NodeList nlist = ele.getChildNodes();
				for (int i = 0 ;  i < nlist.getLength() ; ++i) {
					if(tag.equals(nlist.item(i).getNodeName())) {
						res = new XMLNode(nlist.item(i));
						if (tags.size() > 0) {
						    res = res.lookup(tags);
						}
						break;
					}
				}
			}

		}
		return res;

	}

	/**
	 * ノードの配列を返却します。
	 * @param String tags
	 * @return XMLNode[] ノード配列
	 */
	public XMLNode[] select(String tags) {

		String target;
		XMLNode cn;

		Vector res = new Vector();

		Vector vtag = parseTags(tags);

		int size = vtag.size();
		
		if(size > 0 ) {
			target =(String)vtag.lastElement();
			vtag.removeElementAt(size-1);
			if((cn = lookup(vtag)) == null) {
			    return new XMLNode[0];
			}

		} else {
		    return new XMLNode[0];
		}

		if (cn.getNode() instanceof Document) {
			Document doc = (Document)cn.getNode();

			if(doc.hasChildNodes()) {
				NodeList nlist = doc.getChildNodes();
				for (int i = 0 ;  i < nlist.getLength() ; ++i) {
					if(target.equals(nlist.item(i).getNodeName())) {
						res.add(new XMLNode(nlist.item(i)));
					}
				}
			}
		} else if (cn.getNode() instanceof Element) {
			Element ele = (Element)cn.getNode();

			NamedNodeMap alist = ele.getAttributes();
			if(alist != null) {
				if(alist.getLength() > 0) {
					Attr at = (Attr)ele.getAttributeNode(target);
					if (at != null) {
						res.add(new XMLNode(at));
					}
				}
			}

			if(res.size() == 0 && ele.hasChildNodes()) {
				NodeList nlist = ele.getChildNodes();
				for (int i = 0 ;  i < nlist.getLength() ; ++i) {
					if(target.equals(nlist.item(i).getNodeName())) {
						res.add(new XMLNode(nlist.item(i)));
					}
				}
			}

		}

		return (XMLNode[]) res.toArray(new XMLNode[res.size()]);
	}

	/**
	 * Attributeを追加します。
	 * @param String attr
	 * @param String value
	 * @return XMLNode 
	 */
	public XMLNode addAttr(String attr, String value) {

	  XMLNode res =  null;

	  if ( node instanceof Element) {
		  Element ele = (Element)node;
		  Attr at = (Attr)ele.getAttributeNode(attr);

		  if (at == null) {
			  at = ele.getOwnerDocument().createAttribute(attr);
			  at.setValue(value);
			  at = ele.setAttributeNode(at);
			  res = new XMLNode(at);
		  } else {
			  at.setValue(value);
			  res = new XMLNode(at);
		  }
	  }
	  return res;

	}

	/**
	 * Attributeを削除します。
	 * @param String attr
	 * @return XMLNode 
	 */
	public XMLNode delAttr(String attr) {

	  XMLNode res =  null;

	  if ( node instanceof Element) {
		  Element ele = (Element)node;
		  Attr at = ele.getAttributeNode(attr);
		  if (at != null) {
			  ele.removeAttributeNode(at);
			  res = new XMLNode(at);
		  }
	  }

	  return res;

	}

	/**
	 * Nodeを追加します。 
	 * @param String name
	 * @return XMLNode 
	 */
	public XMLNode addNode(String name) {

	  XMLNode res = null;

	  if ( (node instanceof Document) || (node instanceof Element)) {
		  Node n = node.appendChild(node.getOwnerDocument().createElement(name));
		  res = new XMLNode(n);
	  }

	  return res;

	}

	/**
	 * Nodeを追加します。 
	 * @param name
	 * @param target
	 * @return XMLNode 
	 */
	public XMLNode addNode(String name,String target) {

	  XMLNode res = null;
	  Node cn = null;
	  XMLNode xn = null;

	  if ( (node instanceof Document) || (node instanceof Element)) {
		  if ( node.hasChildNodes()) {
			  Node n = node.appendChild(node.getOwnerDocument().createElement(name));
			  cn = node.getFirstChild();
			  if (target != null && (xn = this.lookup(target)) != null) {
				  cn = xn.getNode();
			  }
			  res = new XMLNode(node.insertBefore(n,cn));
		  }
		  else res = addNode(name);
	  }
	  return res;
	}

	/**
	 * Nodeを削除します。 
	 * @param tag
	 * @return XMLNode 
	 */
	public XMLNode delNode(String tag) {

	  XMLNode res = null;

	  res = lookup(tag);

	  if (res == null) {
	      return res;
	  }

	  if ( (node instanceof Document) || (node instanceof Element)) {
		  if( node.removeChild(res.getNode()) == null) res = null;
	  }

	  return res;
	}

	/**
	 * Nodeを削除します。 
	 * @param child
	 * @return XMLNode 
	 */
	public XMLNode delNode(XMLNode child) {


	  if (child != null) {
		  if ( (node instanceof Document) || (node instanceof Element)) {
			  if( node.removeChild(child.getNode()) == null) child = null;
		  }
	  }
	  return child;
	}

	/**
	 * Nodeを削除します。 
	 * @return XMLNode 
	 */
	public XMLNode delNode() {

		Node parent = node.getParentNode();
		if ( (parent instanceof Document) || (parent instanceof Element)) {
			if( parent.removeChild(node) == null) {
			    return null;
			}
		}
		return this;
	}

	/**
	 * Nodeを取得します。
	 * @return Node 
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * 値を取得します。
	 * @return String
	 */
	public String getValue() {
		return lookupValue();
	}

	/**
	 * 値を取得します。
	 * @param def
	 * @return String 
	 */
	public String getValue(String def) {
		String cont = lookupValue();
		return (cont != null) ? cont : def;
	}

	/**
	 * 値を設定します。 
	 * @param str
	 */
	public void setValue(String str) {
		lookupValue();
		if (( node instanceof Document) || ( node instanceof Element)) {
			if (text == null) {
				text = node.getOwnerDocument().createTextNode(str);
				if (node.hasChildNodes()) {
					node.insertBefore(text,node.getFirstChild());
				} else {
					node.appendChild(text);
				}
				value = str;
			} else {
				text.setNodeValue(str);
			}
		} else if ( node instanceof Attr) {
			Attr atr = (Attr)node;
			atr.setValue(str);
			value = str;
		}

	}

	/**
	 * 値を設定します。
	 * @param tags
	 * @param str
	 */
	public void setValue(String tags , String str) {
		XMLNode cn = lookup(tags);
		if (cn != null) cn.setValue(str);
	}

	/**
	 * 値を削除します。 
	 *
	 */
	public void removeValue() {
		lookupValue();
		if (( node instanceof Document) || ( node instanceof Element)) {
			if (text != null) {
				node.removeChild(text);
				text = null;
				value = null;
			}
		}

	}

	/**
	 * 値を参照します。
	 * @return String 
	 */
	private String lookupValue() {

		if ( value != null) {
		    return value;
		}

		if(( node instanceof Document) || ( node instanceof Element)) {
			if(node.hasChildNodes()) {
				NodeList nlist = node.getChildNodes();
				for (int i = 0 ; i < nlist.getLength() ; ++i) {
					Node cn = nlist.item(i);
					if ( cn instanceof Text ) {
						String nv = cn.getNodeValue().trim();
						if(nv.length() > 0) {
							value = nv;
							text = (Text)cn;
							break;
						}
					}
				}
			}
		} else if ( node instanceof Attr) {
			Attr atr = (Attr)node;
			value = atr.getValue();
			text = null;
		}

		return value;

	}

	/**
	 * NodeListを返却します。
	 * @return XMLNode[] 
	 */
	public XMLNode[] getNodeList() {
		Vector list = new Vector();
		getChildNodeList(list);
		return (XMLNode[]) list.toArray(new XMLNode[list.size()]);
	}

	/**
	 * 子NodeListを返却します。
	 * @param list
	 */
	private void getChildNodeList(Vector list) {

		NamedNodeMap alist = node.getAttributes();
		if(alist != null) {
			if(alist.getLength() > 0) {
				for ( int i = 0 ; i < alist.getLength() ; ++i) {
					XMLNode cn = new XMLNode(node,alist.item(i));
					list.add(cn);
				}
			}
		}

		if(node.hasChildNodes()) {
			NodeList nlist = node.getChildNodes();
			if(nlist.getLength() == 1 && nlist.item(0) instanceof Text) {
				list.add(this);
			}
			for ( int i = 0 ; i < nlist.getLength() ; ++i) {
				XMLNode cn = new XMLNode(nlist.item(i));
				if ( cn.getNode() instanceof Element) {
					cn.getChildNodeList(list);
				}
			}
		}
	}

	/**
	 * 子NodeListを返却します。
	 * @return XMLNode[] 
	 */
	public XMLNode[] getChildNode() {

		Vector result = new Vector();  
            
		NamedNodeMap alist = node.getAttributes();
		if(alist != null) {
			if(alist.getLength() > 0) {
				for ( int i = 0 ; i < alist.getLength() ; ++i) {
					XMLNode cn = new XMLNode(node,alist.item(i));
					result.add(cn);
				}
			}
		}

		if(node.hasChildNodes()) {
			NodeList nlist = node.getChildNodes();
			if(nlist.getLength() == 1 && nlist.item(0) instanceof Text) {
				result.add(this);
			}
			for ( int i = 0 ; i < nlist.getLength() ; ++i) {
				XMLNode cn = new XMLNode(nlist.item(i));
				if ( cn.getNode() instanceof Element) {
					result.add(cn);
				}
			}
		}
		return (XMLNode[]) result.toArray(new XMLNode[result.size()]);
	}

	/**
	 * URIを返却します。
	 * @return String 
	 */
	public String getURI() {

		Node parent;

		if (uri != null) {
			return uri;
		}

	  	if (node == null) {
			uri = "";
		} else {
			uri = node.getNodeName();
			if( node instanceof Attr) {
				Attr at = (Attr)node;
				try {
					parent = at.getOwnerElement();
				} catch (NoSuchMethodError e) {
					// DOM がレベル１です。親のノードが欲しいな。
					parent = parent_node;
				}
			} else {
				parent = node.getParentNode();
			} while(parent != null) {
				uri = parent.getNodeName() + "/" + uri;
				parent = parent.getParentNode();
				if(parent instanceof Document) break;
			}
		}

		return uri;

	}

	/**
	 * URIを取得します。 
	 * @param root
	 * @return String URI  
	 */
	public String getURI(String root) {

		Node parent;
		String node_uri = "";

		if (node != null) {
			node_uri = node.getNodeName();
			if( node instanceof Attr) {
				Attr at = (Attr)node;
				try {
					parent = at.getOwnerElement();
				} catch (NoSuchMethodError e) {
					//DOM がレベル１です。親のノードが欲しいな。
					parent = parent_node;
				}
			} else {
				parent = node.getParentNode();
			}
			while(parent != null) {
				node_uri = parent.getNodeName() + "/" + node_uri;
				if(parent.getNodeName().equals(root)) {
				    break;
				}
				parent = parent.getParentNode();
				if(parent instanceof Document) {
				    break;
				}
			}
		}

		return node_uri;

	}

	/**
	 * プロパティを取得します。
	 * @param aName キー
	 * @return String キーに対する値
	 */
	public String getString(String aName){
		return getString(aName,null);
	}

	/**
	 * プロパティを取得します。
	 * @param sName キー
	 * @param def デフォルト値
	 * @return String キーに対する値
	 */
	public String getString(String sName,String def){
		XMLNode cn = lookup(sName);
		if (cn != null) return cn.getValue(def);
		else return def;
	}

	/**
	 * 指定項目に設定されている値を数値に変換して返します。
	 * 
	 * 返却値は浮動小数点数として返します。
	 * 値を正しく数値に変換できなかった場合には、初期値(第二引数)を
	 * 返します。
	 * 初期値とは、その項目が未設定時に返却する値のことです。
	 * 指定初期値の型はチェックしません。
	 * 
	 * @param sName 設定項目名
	 * @param nInit 初期値
	 * @return float 設定値（数値）または指定初期値
	 */
	public float getFloat(String sName,float nInit){
		try{
			return Float.parseFloat(getString(sName));
		}catch(Exception e){
			return nInit;
		}
	}
	/**
	 * 指定項目に設定されている値を数値に変換して返します。
	 * 
	 * 返却値は浮動小数点数として返します。
	 * 値を正しく数値に変換できなかった場合には、“0”(ZERO)を
	 * 返します。
	 * 
	 * @param sName 設定項目名
	 * @return float 設定値（数値）
	 */
	public float getFloat(String sName){
		try{
			return Float.parseFloat(getString(sName));
		}catch(Exception e){
			return 0;
		}
	}

	/**
	 * 指定項目に設定されている値を数値に変換して返します。
	 * 
	 * 返却値は浮動小数点数として返します。
	 * 値を正しく数値に変換できなかった場合には、初期値(第二引数)を
	 * 返します。
	 * 初期値とは、その項目が未設定時に返却する値のことです。
	 * 指定初期値の型はチェックしません。
	 * 
	 * @param sName 設定項目名
	 * @param nInit 初期値
	 * @return double 設定値（数値）または指定初期値
	 */
	public double getDouble(String sName, double nInit){
		try{
			return Double.parseDouble(getString(sName));
		}catch(Exception e){
			return nInit;
		}
	}

	/**
	 * 指定項目に設定されている値を数値に変換して返します。
	 * 
	 * 返却値は浮動小数点数として返します。
	 * 値を正しく数値に変換できなかった場合には、“0”(ZERO)を
	 * 返します。
	 * 
	 * @param sName 設定項目名
	 * @return double 設定値（数値）
	 */
	public double getDouble(String sName){
		try{
			return Double.parseDouble(getString(sName));
		}catch(Exception e){
			return 0;
		}
	}

	/**
	 * 指定項目に設定されている値を整数に変換して返します。
	 * 
	 * 返却値は整数として返します。
	 * 値を正しく整数に変換できなかった場合には、初期値(第二引数)を
	 * 返します。
	 * 初期値とは、その項目が未設定時に返却する値のことです。
	 * 指定初期値の型はチェックしません。
	 * 
	 * @param sName 設定項目名
	 * @param nInit 初期値
	 * @return int 設定値（数値）または指定初期値
	 */
	public int getInteger(String sName,int nInit){
		try{
			return Integer.parseInt(getString(sName));
		}catch(Exception e){
			return nInit;
		}
	}

	/**
	 * 指定項目に設定されている値を整数に変換して返します。
	 * 
	 * 返却値は整数として返します。
	 * 値を正しく整数に変換できなかった場合には、“0”(ZERO)を
	 * 返します。
	 * 
	 * @param sName 設定項目名
	 * @return int 設定値（数値）
	 */
	public int getInteger(String sName){
		try{
			return Integer.parseInt(getString(sName));
		}catch(Exception e){
			return 0;
		}
	}

	/**
	 * 指定項目に設定されている値を整数に変換して返します。
	 * 
	 * 返却値は整数として返します。
	 * 値を正しく整数に変換できなかった場合には、初期値(第二引数)を
	 * 返します。
	 * 初期値とは、その項目が未設定時に返却する値のことです。
	 * 指定初期値の型はチェックしません。
	 * 
	 * @param sName 設定項目名
	 * @param nInit 初期値
	 * @return long 設定値（数値）または指定初期値
	 */
	public long getLong(String sName,long nInit){
		try{
			return Long.parseLong(getString(sName));
		}catch(Exception e){
			return nInit;
		}
	}
	
	/**
	 * 指定項目に設定されている値を整数に変換して返します。
	 * 
	 * 返却値は整数として返します。
	 * 値を正しく整数に変換できなかった場合には、“0”(ZERO)を
	 * 返します。
	 * 
	 * @param sName 設定項目名
	 * @return long 設定値（数値）
	 */
	public long getLong(String sName){
		try{
			return Long.parseLong(getString(sName));
		}catch(Exception e){
			return 0;
		}
	}

	/**
	 * 設定ファイル内に指定項目が定義されているかどうかを
	 * チェックします。
	 * 
	 * @param sName imart.ini 内の設定項目名
	 * @return boolean 設定ファイル内の情報定義有無
	 */
	public boolean isValid(String sName){
		return lookup(sName) != null;
	}


	/**
	 * 設定データが真値判定であるかをチェックします。
	 * 設定データが真値である条件は以下のとおり。
	 * 値が、“ON”または“TRUE”の場合。
	 * 大文字・小文字の区別あり。
	 * 指定項目が未定義の場合には偽値を返します。
	 * 
	 * @param sName 設定項目名
	 * @return boolean 設定データ真偽値
	 */
	public boolean isTrue(String sName){
		String val = getString(sName);
		return (val != null && (val.equals("ON") || val.toUpperCase().equals("TRUE")));
	}


	/**
	 * 設定データが偽値判定であるかをチェックします。
	 * 設定データが偽値である条件は以下のとおり。
	 * 値が、“OFF”または“FALSE”の場合。
	 * 大文字・小文字の区別あり。
	 * 指定項目が未定義の場合には偽値を返します。
	 * 
	 * @param sName 設定項目名
	 * @return boolean 設定データ真偽値
	 */
	public boolean isFalse(String sName){
		String val = getString(sName);
		return (val != null && (val.equals("OFF") || val.toUpperCase().equals("FALSE")));
	}

}
