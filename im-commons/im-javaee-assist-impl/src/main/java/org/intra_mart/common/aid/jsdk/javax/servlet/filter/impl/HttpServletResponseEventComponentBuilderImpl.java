package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.intra_mart.common.aid.jdk.org.w3c.dom.ElementProperties;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventComponentBuilder;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventConfig;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventException;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventFilterException;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventListener;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventListenerException;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventValidator;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * コンポーネントを構築するビルダのためのファクトリです。
 * 
 */
public class HttpServletResponseEventComponentBuilderImpl implements HttpServletResponseEventComponentBuilder{
	private Document document = null;
	private ServletContext servletContext = null;

	/**
	 * 新しいファクトリを構築します。
	 */
	public HttpServletResponseEventComponentBuilderImpl(){
		super();
	}

	/**
	 * ビルダを返します。
	 * @param name 設定のリソース名
	 * @param servletContext サーブレットコンテキスト
	 * @throws HttpServletResponseEventFilterException エラーが発生した場合
	 */
	public void init(String initParameterValue, ServletContext servletContext) throws HttpServletResponseEventException {
		this.servletContext = servletContext;
		if(initParameterValue != null){
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream in = classLoader.getResourceAsStream(initParameterValue);
			if(in != null){
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
					this.document = documentBuilder.parse(in);;
				}
				catch (ParserConfigurationException pce){
					throw new HttpServletResponseEventException("Configuration parse error: " + initParameterValue, pce);
				}
				catch (SAXException saxe){
					throw new HttpServletResponseEventException("Configuration format error: " + initParameterValue, saxe);
				}
				catch (IOException ioe){
					throw new HttpServletResponseEventException("Configuration load error: " + initParameterValue, ioe);
				}

			}
			else{
				throw new HttpServletResponseEventException("Resource is not found: " + initParameterValue);
			}
		}
		else{
			throw new HttpServletResponseEventException("Initial-parameter is null.");
		}
	}

	/**
	 * リスナの配列を返します。
	 * @return リスナの配列
	 * @throws HttpServletResponseEventListenerException エラーが発生した場合
	 */
	public HttpServletResponseEventListener[] getListeners() throws HttpServletResponseEventListenerException{
		Collection collection = new ArrayList();
		Element[] elements = this.getChildElements("listener");

		for(int idx = 0; idx < elements.length; idx++){
			// コントローラのインスタンス化
			ElementProperties elementProperties = new ElementProperties(elements[idx]);
			String className = elementProperties.getProperty("/listener/listener-class");

			try{
				HttpServletResponseEventListener listener = (HttpServletResponseEventListener) this.getInstance(className);

				// パラメータの解決とコントローラの初期化
				HttpServletResponseEventConfig eventConfig = new HttpServletResponseEventConfigImpl(this.servletContext, elements[idx]);
				listener.init(eventConfig);
				collection.add(listener);
			}
			catch(HttpServletResponseEventListenerException e){
				throw e;
			}
			catch(HttpServletResponseEventException e){
				throw new HttpServletResponseEventListenerException("Listener initialize error: " + className, e);
			}
		}

		return (HttpServletResponseEventListener[]) collection.toArray(new HttpServletResponseEventListener[collection.size()]);
	}

	/**
	 * バリデータの配列を返します。
	 * @return バリデータの配列
	 * @throws HttpServletResponseEventValidatorException エラーが発生した場合
	 */
	public HttpServletResponseEventValidator[] getValidators() throws HttpServletResponseEventValidatorException{
		Collection collection = new ArrayList();
		Element[] elements = this.getChildElements("validator");

		for(int idx = 0; idx < elements.length; idx++){
			// コントローラのインスタンス化
			ElementProperties elementProperties = new ElementProperties(elements[idx]);
			String className = elementProperties.getProperty("/validator/validator-class");

			try{
				HttpServletResponseEventValidator validator = (HttpServletResponseEventValidator) this.getInstance(className);

				// パラメータの解決とコントローラの初期化
				HttpServletResponseEventConfig eventConfig = new HttpServletResponseEventConfigImpl(this.servletContext, elements[idx]);
				validator.init(eventConfig);
				collection.add(validator);
			}
			catch(HttpServletResponseEventValidatorException e){
				throw e;
			}
			catch(HttpServletResponseEventException e){
				throw new HttpServletResponseEventValidatorException("Validator initialize error: " + className, e);
			}
		}

		return (HttpServletResponseEventValidator[]) collection.toArray(new HttpServletResponseEventValidator[collection.size()]);
	}

	private Element[] getChildElements(String name){
		Collection collection = new ArrayList();
		Element documentElement = this.document.getDocumentElement();
		NodeList nodeList = documentElement.getChildNodes();
		for(int idx = 0; idx < nodeList.getLength(); idx++){
			Node node = nodeList.item(idx);
			if(node.getNodeType() != Node.ELEMENT_NODE){ continue; }
			Element element = (Element) node;
			if(element.getTagName().equals(name)){
				collection.add(element);
			}
		}
		return (Element[]) collection.toArray(new Element[collection.size()]);
	}

	/**
	 * 指定の名称をもつクラスを返します。
	 * @param name クラス名
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 */
	private Class getClass(String name) throws ClassNotFoundException{
		ClassLoader classLoader = this.getClass().getClassLoader();
		return classLoader.loadClass(name);
	}

	private Object getInstance(String name) throws HttpServletResponseEventException{
		try{
			Class clazz = this.getClass(name);
			return clazz.newInstance();
		}
		catch(ClassNotFoundException cnfe){
			throw new HttpServletResponseEventException("Class not found: " + name, cnfe);
		}
		catch(IllegalAccessException iae){
			throw new HttpServletResponseEventException("Class access error: " + name, iae);
		}
		catch(InstantiationException ie){
			throw new HttpServletResponseEventException("Constructor's modifier error: " + name, ie);
		}
	}
}
