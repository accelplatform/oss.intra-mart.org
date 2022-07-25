package org.intra_mart.jssp.util.config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 独自のJavaScript API, JSSPTag, Initializer,
 * ContextFactoryListenerを自動登録するConfigHandlerです.<br />
 * jarファイル内に含まれるMETA-INF/jssp-api-config.xmlファイルを検索し、登録を行います。<br />
 * jssp-api-config.xmlファイルは下記の形式です。<br /> javascriptで記述されたAPI,
 * Tagはサポートしていません.<br /> 複数のjarファイル中に同名のクラスが存在した場合、クラスローダによるクラス検索方法に依存します。<br />
 * (最初に発見されたクラスが利用されます。)
 * 
 * <pre>
 * &lt;intra-mart&gt;
 *   &lt;jssp&gt;
 *     &lt;java-script-api&gt;
 *       &lt;api-class name=&quot;APIクラス名&quot;&gt;
 *         &lt;!-- 任意のAPI設定 --&gt;
 *       &lt;/api-class&gt;
 *     &lt;/java-script-api&gt;
 *     &lt;jssp-tag&gt;
 *       &lt;tag-class name=&quot;IMARTタグクラス名&quot;&gt;
 *         &lt;!-- 任意のAPI設定 --&gt;
 *       &lt;/tag-class&gt;
 *     &lt;/jssp-tag&gt;
 *     &lt;listener&gt;
 *       &lt;context-factory-listener&gt;
 *         &lt;listener-class&gt;リスナクラス名&lt;/listener-class&gt;
 *       &lt;/context-factory-listener&gt;
 *     &lt;/listener&gt;
 *     &lt;initializer&gt;
 *       &lt;application&gt;
 *         &lt;initializer-class&gt;初期化クラス名&lt;/initializer-class&gt;
 *       &lt;/application&gt;
 *     &lt;/initializer&gt;
 *   &lt;/jssp&gt;
 * &lt;/intra-mart&gt;
 * </pre>
 * 
 */
public class JSSPPluginConfigHandler extends JsspRpcConfigHandlerImpl {

	/** jssp-api-config.xml location. */
	public static final String CONFIG_FILE_LOCATION = "META-INF/jssp-api-config.xml";

	/** XPath. */
	private static final XPath XPATH = XPathFactory.newInstance().newXPath();

	/** JavaScriptAPI4Class. */
	private String[] javascriptAPI4Class;

	/** JavaScriptAPI4ClassConfig. */
	private final Map<String, Document> javascriptAPI4ClassConfig = new HashMap<String, Document>();

	/** JSSPTags4Class. */
	private String[] jsspTags4Class;

	/** JSSPTags4ClassConfig. */
	private final Map<String, Document> jsspTags4ClassConfig = new HashMap<String, Document>();

	/** Initializer4Class. */
	private String[] initializer4Class;

	/** contextFactoryListeners. */
	private String[] contextFactoryListeners;

	/**
	 * このコンストラクタは、
	 * リソース「/org/intra_mart/jssp/util/config/jssp-config.xml」をコンフィグファイルとします。
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 */
	public JSSPPluginConfigHandler() throws ParserConfigurationException, SAXException, IOException {
		this("/org/intra_mart/jssp/util/config/jssp-config.xml");
	}

	/**
	 * このコンストラクタは、引数で指定されたファイルをコンフィグファイルとします。
	 * 
	 * @param configFilePath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 */
	public JSSPPluginConfigHandler(final String configFilePath) throws ParserConfigurationException, SAXException, IOException {
		super(configFilePath);

		// 初期化時のClassLoaderから参照可能な設定ファイルを検索します.
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final Enumeration<URL> resources = classLoader.getResources(CONFIG_FILE_LOCATION);

		// XPathExpressionの準備
		final XPathExpression apiExpression = expression("/intra-mart/jssp/java-script-api/api-class/@name");
		final XPathExpression tagExpression = expression("/intra-mart/jssp/jssp-tag/tag-class/@name");
		final XPathExpression initializerExpression = expression("/intra-mart/jssp/initializer/application/initializer-class");
		final XPathExpression contextFactoryListenerExpression = expression("/intra-mart/jssp/listener/context-factory-listener/listener-class");

		// 基本となるjssp-config.xmlファイルの内容を登録
		final List<String> javascriptAPI4ClassList = new LinkedList<String>(Arrays.asList(super.getJavaScriptAPI4Class()));
		final List<String> jsspTags4ClassList = new LinkedList<String>(Arrays.asList(super.getJSSPTags4Class()));
		final List<String> initializer4ClassList = new LinkedList<String>(Arrays.asList(super.getInitializer4Class()));
		final List<String> contextFactoryListenersList = new LinkedList<String>(Arrays.asList(super.getContextFactoryListeners()));

		final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		// 設定ファイルの読み込み.
		while (resources.hasMoreElements()) {
			final Document document = documentBuilder.parse(resources.nextElement().openStream());

			for (final String clazz : getAttributeValues(getNodeList(document, apiExpression))) {
				if (javascriptAPI4ClassList.contains(clazz)) {
					continue;
				}

				javascriptAPI4ClassList.add(clazz);
				javascriptAPI4ClassConfig.put(clazz, document);
			}

			for (final String clazz : getAttributeValues(getNodeList(document, tagExpression))) {
				if (jsspTags4ClassList.contains(clazz)) {
					continue;
				}

				jsspTags4ClassList.add(clazz);
				jsspTags4ClassConfig.put(clazz, document);
			}

			for (final String clazz : getTextContents(getNodeList(document, initializerExpression))) {
				if (initializer4ClassList.contains(clazz)) {
					continue;
				}

				initializer4ClassList.add(clazz);
			}

			for (final String clazz : getTextContents(getNodeList(document, contextFactoryListenerExpression))) {
				if (contextFactoryListenersList.contains(clazz)) {
					continue;
				}

				contextFactoryListenersList.add(clazz);
			}
		}

		javascriptAPI4Class = javascriptAPI4ClassList.toArray(new String[javascriptAPI4ClassList.size()]);
		jsspTags4Class = jsspTags4ClassList.toArray(new String[jsspTags4ClassList.size()]);
		initializer4Class = initializer4ClassList.toArray(new String[initializer4ClassList.size()]);
		contextFactoryListeners = contextFactoryListenersList.toArray(new String[contextFactoryListenersList.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.intra_mart.jssp.util.config.JSSPConfigHandlerImpl#getJavaScriptAPI4Class
	 * ()
	 */
	@Override
	public String[] getJavaScriptAPI4Class() {
		return javascriptAPI4Class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.intra_mart.jssp.util.config.JSSPConfigHandlerImpl#
	 * getJavaScriptAPI4ClassConfig(java.lang.String)
	 */
	@Override
	public NodeList getJavaScriptAPI4ClassConfig(final String name) {

		if (javascriptAPI4ClassConfig.containsKey(name)) {
			return getNodeList(javascriptAPI4ClassConfig.get(name), expression("/intra-mart/jssp/java-script-api/api-class[@name=\"" + name + "\"]/*"));
		}

		return super.getJavaScriptAPI4ClassConfig(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.intra_mart.jssp.util.config.JSSPConfigHandlerImpl#getJSSPTags4Class()
	 */
	@Override
	public String[] getJSSPTags4Class() {
		return jsspTags4Class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.intra_mart.jssp.util.config.JSSPConfigHandlerImpl#getJSSPTags4ClassConfig
	 * (java.lang.String)
	 */
	@Override
	public NodeList getJSSPTags4ClassConfig(final String name) {

		if (jsspTags4ClassConfig.containsKey(name)) {
			return getNodeList(jsspTags4ClassConfig.get(name), expression("/intra-mart/jssp/jssp-tag/tag-class[@name=\"" + name + "\"]/*"));
		}

		return super.getJSSPTags4ClassConfig(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.intra_mart.jssp.util.config.JSSPConfigHandlerImpl#getInitializer4Class
	 * ()
	 */
	public String[] getInitializer4Class() {
		return initializer4Class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.intra_mart.jssp.util.config.JSSPConfigHandlerImpl#
	 * getContextFactoryListeners()
	 */
	public String[] getContextFactoryListeners() {
		return contextFactoryListeners;
	}

	/**
	 * XPath式を取得します.
	 * 
	 * @param expression
	 *            XPath式
	 * @return XPathExpression
	 */
	private XPathExpression expression(final String expression) {

		try {
			return XPATH.compile(expression);
		} catch (final XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * NodeListを取得します.
	 * 
	 * @param document
	 *            Document.
	 * @param expression
	 *            XpathExpression.
	 * @return NodeList.
	 */
	private NodeList getNodeList(final Document document, final XPathExpression expression) {
		try {
			return (NodeList) expression.evaluate(document, XPathConstants.NODESET);
		} catch (final XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * NodeListに含まれるTextConetntを一括して取得します.
	 * 
	 * @param nodeList
	 *            NodeList.
	 * @return TextContent配列
	 */
	private String[] getTextContents(final NodeList nodeList) {

		final List<String> list = new ArrayList<String>(nodeList.getLength());
		for (int i = 0, length = nodeList.getLength(); i < length; i++) {
			final Node node = nodeList.item(i);

			if (Node.ELEMENT_NODE != node.getNodeType()) {
				continue;
			}

			final String textContent = node.getTextContent();
			if (textContent == null && "".equals(textContent.trim())) {
				continue;
			}

			list.add(textContent);
		}

		return list.toArray(new String[list.size()]);
	}

	/**
	 * 属性値を一括して取得します.
	 * 
	 * @param nodeList
	 *            NodeList.
	 * @return 属性値配列
	 */
	private String[] getAttributeValues(final NodeList nodeList) {

		final List<String> list = new ArrayList<String>(nodeList.getLength());
		for (int i = 0, length = nodeList.getLength(); i < length; i++) {
			final Node node = nodeList.item(i);

			if (Node.ATTRIBUTE_NODE != node.getNodeType()) {
				continue;
			}

			final String attributeValue = ((Attr) node).getValue();
			if (attributeValue == null && "".equals(attributeValue.trim())) {
				continue;
			}

			list.add(attributeValue);
		}

		return list.toArray(new String[list.size()]);
	}
}
