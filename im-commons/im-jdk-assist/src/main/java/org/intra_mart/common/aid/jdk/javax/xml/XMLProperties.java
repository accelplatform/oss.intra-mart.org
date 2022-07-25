package org.intra_mart.common.aid.jdk.javax.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.intra_mart.common.aid.jdk.org.w3c.dom.ElementProperties;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/**
 * XML をベースとした不変のプロパティセットを表します。
 * このクラスは、XML を元データとして、{@link java.util.Properties} として
 * 利用できるインタフェースを提供します。<p>
 * 読み込んだ XML ソースは、{@link javax.xml.parsers.DocumentBuilderFactory} が
 * 返すパーサを利用して解析します。
 * XML で記述された各情報に対しては、XPath 形式の文字列をプロパティ名として
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
public class XMLProperties extends ElementProperties{
	/**
	 * XML ソースを解析してＤＯＭを返します。
	 * @param in XML ソースの入力ストリーム
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Document parseXML(InputSource in) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		return documentBuilder.parse(in);
	}

	/**
	 * このプロパティセットの元データとなった DOM
	 */
	private Document domDocument = null;
	/**
	 * プロパティのリスト
	 */
	private Map propertyList = new HashMap();

	/**
	 * 指定データを持つ新しいプロパティセットを作成します。
	 * @param f 設定ファイル（xml）のパス
	 * @throws FileNotFoundException ファイルがな場合。またはファイルにアクセスできない場合。
	 * @throws IOException 入出力エラー
	 * @throws SAXException XML の解析エラー
	 * @throws ParserConfigurationException XML の解析エラー
	 */
	public XMLProperties(File f) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		this(new FileInputStream(f));
	}

	/**
	 * 指定データを持つ新しいプロパティセットを作成します。
	 * @param in XML ソースを入力するストリーム
	 * @throws IOException 入出力エラー
	 * @throws SAXException XML の解析エラー
	 * @throws ParserConfigurationException XML の解析エラー
	 */
	public XMLProperties(InputStream in) throws ParserConfigurationException, SAXException, IOException{
		this(new InputSource(in));
	}

	/**
	 * 指定データを持つ新しいプロパティセットを作成します。
	 * @param in XML ソースを入力するストリーム
	 * @throws IOException 入出力エラー
	 * @throws SAXException XML の解析エラー
	 * @throws ParserConfigurationException XML の解析エラー
	 */
	public XMLProperties(InputSource in) throws ParserConfigurationException, SAXException, IOException{
		this(parseXML(in));
	}

	/**
	 * 指定データを持つ新しいプロパティセットを作成します。
	 * @param document XMLの解析結果
	 */
	private XMLProperties(Document document) throws ParserConfigurationException, SAXException, IOException{
		super(document.getDocumentElement());
		this.domDocument = document;
	}

	/**
	 * 指定データを持つ新しいプロパティセットを作成します。
	 * @param in XML ソースを入力するストリーム
	 * @param parent 基礎データ
	 * @throws IOException 入出力エラー
	 * @throws SAXException XML の解析エラー
	 * @throws ParserConfigurationException XML の解析エラー
	 */
	public XMLProperties(InputStream in, Properties parent) throws ParserConfigurationException, SAXException, IOException{
		this(new InputSource(in), parent);
	}

	/**
	 * 指定データを持つ新しいプロパティセットを作成します。
	 * @param in XML ソースを入力するストリーム
	 * @param parent 基礎データ
	 * @throws IOException 入出力エラー
	 * @throws SAXException XML の解析エラー
	 * @throws ParserConfigurationException XML の解析エラー
	 */
	public XMLProperties(InputSource in, Properties parent) throws ParserConfigurationException, SAXException, IOException{
		this(parseXML(in), parent);
	}

	/**
	 * 指定データを持つ新しいプロパティセットを作成します。
	 * @param document XMLの解析結果
	 * @param parent 基礎データ
	 */
	private XMLProperties(Document document, Properties parent){
		super(document.getDocumentElement(), parent);
		this.domDocument = document;
	}

	/**
	 * 設定ファイルのドキュメントノードを返します。
	 * @return ドキュメントノード
	 */
	public Document getDocument(){
		return this.domDocument;
	}
}

