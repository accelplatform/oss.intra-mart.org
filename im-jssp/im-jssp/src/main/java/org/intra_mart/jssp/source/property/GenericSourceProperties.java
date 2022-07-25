package org.intra_mart.jssp.source.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;


import org.intra_mart.common.aid.jdk.javax.xml.XMLProperties;
import org.xml.sax.SAXException;

/**
 * ソースファイルのデータを持つクラスです。
 */
public class GenericSourceProperties extends AbstractSourceProperties{
	/**
	 * 設定データを作成します。
	 * @param f 設定ファイル（xml）のパス
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public GenericSourceProperties(File f) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		this(new FileInputStream(f));
	}

	/**
	 * 設定データを作成します。
	 * @param in XML ソースを読み込むための入力ストリーム
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public GenericSourceProperties(InputStream in) throws ParserConfigurationException, SAXException, IOException{
		super();
		this.initGenericSourceProperties(in);
	}

	/**
	 * 設定データを作成します。
	 * @param in XML ソースを読み込むための入力ストリーム
	 * @param parent 基礎データ
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public GenericSourceProperties(InputStream in, SourceProperties parent) throws ParserConfigurationException, SAXException, IOException{
		super(parent);
		this.initGenericSourceProperties(in);
	}

	/**
	 * 設定データを作成します。
	 * @param in 設定データの入力
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private void initGenericSourceProperties(InputStream in) 
						throws ParserConfigurationException, SAXException, IOException{
		Properties properties = new XMLProperties(in);
		Enumeration enumeration = properties.propertyNames();
		while(enumeration.hasMoreElements()){
			String key = (String) enumeration.nextElement();
			this.setProperty(key, properties.getProperty(key));
		}
	}

	/**
	 * 文字エンコーディングの設定値がマッピングされているキー名を返します。
	 * @return キー名
	 */
	protected String getKey4characterEncoding(){
		return "/resource-file/charset";
	}

	/**
	 * 最適化レベルの設定値がマッピングされているキー名を返します。
	 * @return キー名
	 */
	protected String getKey4javaScriptOptimizationLevel(){
		return "/resource-file/javascript/optimize/@level";
	}

	/**
	 * JavaScript のコンパイル機能が有効かどうかの設定値がマッピングされている
	 * キー名を返します。
	 * @return キー名
	 */
	protected String getKey4javaScriptCompiler(){
		return "/resource-file/javascript/compiler/@enable";
	}

	/**
	 * View ソースのキャッシュ機能が有効かどうかの設定値がマッピングされている
	 * キー名を返します。
	 * @return キー名
	 */
	protected String getKey4viewCompiler(){
		return "/resource-file/view/compiler/@enable";
	}
}

