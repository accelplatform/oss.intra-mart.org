package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * HttpServletRequestMessageBodyWrapper の実装です。
 * この実装では、text/xml でリクエストされたクエリを解析します。
 * 
 */
public class HttpServletRequestMessageBodyWrapper4xml extends AbstractBufferedHttpServletRequestMessageBodyWrapper{
	/**
	 * 解析済みのＤＯＭ
	 */
	private Document domDocumentNode = null;

	/**
	 * 拡張されたリクエストを作成します。
	 * @param request サーブレットリクエスト
	 * @param parentRequestParameter 初期化パラメータ「parent.request.parameter」のboolean値
	 * @param parseQueryString 初期化パラメータ「parse.query.string」のboolean値
	 * @throws IOException
	 */
 	public HttpServletRequestMessageBodyWrapper4xml(HttpServletRequest request, 
 													 boolean parentRequestParameter,
													 boolean parseQueryString ) throws ServletException, IOException{
 		super(request, parentRequestParameter, parseQueryString);
	}


// AbstractExtendedHttpServletRequest interface
	/**
	 * パラメータ情報を返します。
	 * このメソッドは、
	 * application/x-www-form-urlencoded で作成されたクエリを解析します。
	 * @param in 入力ストリーム
	 * @return パラメータ
	 * @throws IOException 入出力エラー
	 */
	protected RequestParameter[] getRequestParameters(InputStream in) throws IOException{
		try{
			Document document = this.getDOMDocument(in);
			Collection collection = this.getNodeValues("/", document.getDocumentElement(), new ArrayList());
			return (RequestParameter[]) collection.toArray(new RequestParameter[collection.size()]);
		}
		catch(ParserConfigurationException pce){
			IOException ioe = new IOException("XML parse error: ParserConfigurationException");
			ioe.initCause(pce);
			throw ioe;
		}
		catch(SAXException saxe){
			IOException ioe = new IOException("XML parse error: SAXException");
			ioe.initCause(saxe);
			throw ioe;
		}
	}

	/**
	 * DOM の要素の値を抽出します。
	 * 引数のノードが子ノードを持っている場合、再帰呼び出しにより、
	 * すべての階層のデータを抽出します。
	 * 返り値は、引数のコレクションに抽出したデータを格納した
	 * コレクションを返します。
	 * つまり、引数のコレクションと返り値の実体は同じモノです。
	 * @param element 抽出対象の要素
	 * @param collection 抽出データの格納先
	 * @return 抽出データを格納したコレクション
	 */
	private Collection getNodeValues(String parentPath, Element element, Collection collection){
		String xPathPrefix = parentPath + element.getTagName() + "/";

		// 属性値(Attribute)の取得
		if(element.hasAttributes()){
			NamedNodeMap namedNodeMap = element.getAttributes();
			for(int idx = 0; idx < namedNodeMap.getLength(); idx++){
				Node node = namedNodeMap.item(idx);
				collection.add(new RequestParameter4string(xPathPrefix + "@" + node.getNodeName(), node.getNodeValue(), this));
				collection.add(new RequestParameter4string(node.getNodeName(), node.getNodeValue(), this));
			}
		}

		// 子供属性値の取得
		if(element.hasChildNodes()){
			boolean hasTextNode = false;
			StringBuffer stringBuffer = new StringBuffer();
			NodeList nodeList = element.getChildNodes();
			for(int idx = 0; idx < nodeList.getLength(); idx++){
				Node node = nodeList.item(idx);
				switch(node.getNodeType()){
					case Node.ELEMENT_NODE:
						this.getNodeValues(xPathPrefix, (Element) node, collection);
						break;
					case Node.TEXT_NODE:
						stringBuffer.append(node.getNodeValue());
						hasTextNode = true;
				}
			}

			if(hasTextNode){
				String nodeText = new String(stringBuffer);
				String nodeValue =  nodeText.trim();
				collection.add(new RequestParameter4string(element.getTagName(), nodeValue, this));
				collection.add(new RequestParameter4string(parentPath + element.getTagName(), nodeText, this));
			}
		}
		else{
			collection.add(new RequestParameter4string(element.getTagName(), "", this));
			collection.add(new RequestParameter4string(parentPath + element.getTagName(), "", this));
		}

		return collection;
	}

	/**
	 * リクエストされたＸＭＬを解析して返します。
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private Document getDOMDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException{
		if(this.domDocumentNode == null){
			// XML データの DOM 化
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
//			String enc = this.getRequest().getCharacterEncoding();
//			if(enc != null){
//				InputSource is = new InputSource(in);
//				is.setEncoding(CharacterSetManager.toIANAName(enc));
//				this.domDocumentNode = db.parse(is);
//			}
//			else{
				this.domDocumentNode = db.parse(in);
//			}
		}

		return this.domDocumentNode;
	}
}

