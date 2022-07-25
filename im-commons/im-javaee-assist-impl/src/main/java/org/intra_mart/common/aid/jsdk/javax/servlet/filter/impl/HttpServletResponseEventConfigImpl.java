package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;


import org.intra_mart.common.aid.jdk.java.util.ExtendedProperties;
import org.intra_mart.common.aid.jdk.org.w3c.dom.ElementProperties;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractHttpServletResponseEventConfig;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * XMLのinit-paramを自動解釈する初期化パラメータオブジェクト
 * 
 */
public class HttpServletResponseEventConfigImpl extends AbstractHttpServletResponseEventConfig{
	private Properties properties = null;

	/**
	 * 指定のプロパティズを初期化パラメータとして扱うための、
	 * 新しい設定オブジェクトを作成します。
	 * @param servletContext サーブレットコンテキスト
	 * @param properties 初期化パラメータを持つプロパティズ
	 */
	public HttpServletResponseEventConfigImpl(ServletContext servletContext, Element element){
		super(servletContext);
		this.properties = this.getInitParameters(element);
	}

	/**
	 * 指定のノードに設定されている <init-param> タグをみつけて、
	 * 初期化パラメータとして抽出します。
	 * @param element 親ノード
	 */
	private ExtendedProperties getInitParameters(Node node){
		ExtendedProperties extendedProperties = new ExtendedProperties();

		NodeList nodeList = node.getChildNodes();
		for(int idx = 0; idx < nodeList.getLength(); idx++){
			Node childNode = nodeList.item(idx);
			if(childNode.getNodeType() != Node.ELEMENT_NODE){ continue; }

			Element element = (Element) childNode;
			if(element.getTagName().equals("init-param")){
				ElementProperties elementProperties = new ElementProperties(element);
				String name = elementProperties.getProperty("/init-param/param-name");
				String value = elementProperties.getProperty("/init-param/param-value");
				if(name == null){
					throw new NullPointerException("/init-param/param-name is not defined");
				}
				if(value == null){
					throw new NullPointerException("/init-param/param-value is not defined");
				}
				extendedProperties.setProperty(name, value);
			}
		}
		return extendedProperties;
	}

	/**
	 * 指定した名前の初期化パラメータの値である String、
	 * あるいはパラメータが存在しなければ null を返します。
	 * @param name 初期化パラメータの名前を指定する String
	 * @return 初期化パラメータの値である Strin
	 */
	public String getInitParameter(String name){
		return this.properties.getProperty(name);
	}

	/**
	 * 初期化パラメータの名前である String オブジェクトの Enumeration を返します。
	 * または、初期化パラメータが無ければ空の Enumeration  を返します。
	 * @return 初期化パラメータの名前である String オブジェクトの Enumeration
	 */
	public Enumeration getInitParameterNames(){
		return this.properties.propertyNames();
	}
}
