package org.intra_mart.common.aid.jdk.util.charset;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * このクラスは、文字エンコードのマッピング定義を管理します。
 * <P>
 * 定義ファイルは、クラスローダを利用して検索および読み込まれます。
 * したがって、定義ファイルはクラスパスに設定されているディレクトリまたは
 * アーカイブファイル内に配置する必要があります。<br>
 * 各文字エンコーディングと文字の変換テーブルの関連については、
 * 定義ファイル <code>org/intra_mart/resources/charset/encoding/mapping.xml</code> に記載されています。
 * <br>
 *
 */
public class CharacterMappingBuilder{
	/**
	 * マッピングを定義した定義ファイルのリストが記載されている定義ファイル名
	 */
	public static final String CONFIG_FILE_PATH = "org/intra_mart/resources/charset/encoding/mapping.xml";
	/**
	 * このクラスのインスタンスを保存
	 */
	private static Reference _instance = null;

	/**
	 * 単体試験用
	 */
/*
	public static void main(String[] args){
		try{
			CharacterMappingBuilder builder = CharacterMappingBuilder.instance();

			System.out.println(builder.mappingProperties);
			System.out.println(builder.resourceMapping);
			char[] m = builder.getMapping("EUC_JP");
			for(int i = 0; i < m.length; i++){
				if(i != m[i]){
					System.out.println("source=" + Integer.toHexString(i) + " / revise=" + Integer.toHexString(m[i]));
				}
			}

			String str = "　ＡＢＣＤＥＦＧａｂｃｄｅｆｇ";
			File f = new File("E:/work/test.txt");
			FileOutputStream out = new FileOutputStream(f);
			OutputStreamWriter writer = new OutputStreamWriter(out, "SJIS");
			for(int i = 0; i < str.length(); i++){
				int c = str.charAt(i);
				writer.write(Integer.toHexString(c));
				writer.write("\r\n");
			}
			writer.flush();

			writer.close();
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
*/

	/**
	 * ビルダを返します。
	 * @return ビルダ
	 * @throws ParserConfigurationException XML 解析エラー
	 * @throws SAXException XML 解析エラー
	 * @throws ResourceFormatException 設定情報に不整合がある場合
	 * @throws IOException 設定ファイルの入力エラー
	 * @throws ResourceNotFoundException 設定ファイルがない場合
	 */
	public static synchronized CharacterMappingBuilder instance() throws ParserConfigurationException, SAXException, ResourceFormatException, IOException, ResourceNotFoundException{
		// キャッシュチェック
		if(_instance != null){
			CharacterMappingBuilder o = (CharacterMappingBuilder) _instance.get();
			if(o != null){ return o; }
		}

		// 新規作成
		CharacterMappingBuilder o = new CharacterMappingBuilder();
		_instance = new SoftReference(o);
		return o;
	}

	/**
	 * 設定が存在しなくてサポートできない文字エンコーディング名のセット
	 */
	private Set unsupportedEncodingSet = new HashSet();
	/**
	 * マッピングを定義した設定ファイルへのマップ
	 */
	private Properties mappingProperties = new Properties();
	/**
	 * マッピングを定義した設定ファイルへのマップ
	 */
	private Properties resourceMapping = new Properties();
	/**
	 * エンコーディングのマップ
	 * 文字エンコーディング名をキーとして、文字の変換マップとなる
	 * char 型の配列がマッピングされています。
	 */
	private Map mappingMap = new HashMap();

	/**
	 * 文字コード変換のためのエンコーダを作成します。<p>
	 * @param enc エンコーディング名
	 * @throws NullPointerException 引数が null の場合
	 */
	private CharacterMappingBuilder() throws ResourceNotFoundException, IOException, SAXException, ResourceFormatException, ParserConfigurationException{
		super();

		// 設定ファイルの読み込み
		InputStream in = getResourceAsStream(CONFIG_FILE_PATH);
		if(in == null){
			throw new ResourceNotFoundException("Configuration file is not found: name=" + CONFIG_FILE_PATH);
		}

		// 設定内容の解析
		Document document = getDOMDocument(in);
		Element documentElement = document.getDocumentElement();
		if(! documentElement.getTagName().equals("charset")){
			// 書式エラー
			throw new ResourceFormatException("Document-Element <charset> is not defined: document=" + documentElement.getTagName() + "/resource=" + CONFIG_FILE_PATH);
		}

		// 各定義情報の解析
		NodeList nodeList = documentElement.getChildNodes();
		for(int idx = 0; idx < nodeList.getLength(); idx++){
			Node node = nodeList.item(idx);
			if(node.getNodeType() != Node.ELEMENT_NODE){ continue; }

			Element element = (Element) node;
			if(! element.getTagName().equals("encoding")){ continue; }

			// 定義内容の取得
			String name = getDOMParameter(element, "name");
			if(name == null){
				throw new ResourceFormatException("Encoding-name is not defined: attribute=name/element=encoding/resource=" + CONFIG_FILE_PATH);
			}

			String resourceName = getDOMParameter(element, "mapping");
			if(resourceName == null){
				throw new ResourceFormatException("Character-mapping is not defined: element=mapping/element=encoding/resource=" + CONFIG_FILE_PATH);
			}

			this.mappingProperties.setProperty(name, resourceName);
			this.resourceMapping.setProperty(name, resourceName);
		}
	}

	/**
	 * 文字の修正用マッピングを返します。
	 * マッピングは、対象の文字コードの数値表現をインデックスにした要素に
	 * 置換文字を持つ char 型配列です。
	 * @param enc 文字エンコーディング名
	 * @return エンコード用の文字マッピング
	 * @throws UnsupportedEncodingException エンコーディング別のマッピング定義ファイルが見つからなかった場合
	 */
	public char[] getMapping(String enc) throws ParserConfigurationException, IOException, SAXException, ResourceFormatException, ResourceNotFoundException{
		// 設定ファイルのリソース名を取得
		String resourceName = this.getResourceName(enc);
		if(resourceName == null){
			throw new UnsupportedEncodingException("Revise mapping is not found: name=" + enc);
		}

		// マッピングの取得
		char[] mapping = (char[]) this.mappingMap.get(resourceName);

		if(mapping == null){
			// マッピング定義ファイルの検索
			InputStream in = getResourceAsStream(resourceName);
			if(in == null){
				throw new ResourceNotFoundException("Configuration is not found: charset=" + enc + "/resource=" + resourceName);
			}
			else{
				// 設定内容の解析
				Document document = getDOMDocument(in);
				Element documentElement = document.getDocumentElement();
				if(documentElement.getTagName().equals("encoding")){
					// デフォルトマッピングの取得
					mapping = newCharacterMapping();

					if(documentElement.hasChildNodes()){
						// マッピングの修正
						NodeList nodeList = documentElement.getChildNodes();
						for(int idx = 0; idx < nodeList.getLength(); idx++){
							Node node = nodeList.item(idx);
							if(node.getNodeType() != Node.ELEMENT_NODE){ continue; }

							Element element = (Element) node;
							if(! element.getTagName().equals("mapping")){ continue; }

							String source = getDOMParameter(element, "source");
							if(source == null){
								throw new ResourceFormatException("Encoding-mapping <source> is not defined: element=source/element=mapping/element=encoding/resource=" + resourceName);
							}
							String revise = getDOMParameter(element, "revise");
							if(revise == null){
								throw new ResourceFormatException("Encoding-mapping <revise> is not defined: element=revise/element=mapping/element=encoding/resource=" + resourceName);
							}

							int sourceRadix = 10;
							Element sourceElement = getChildElement(element, "source");
							if(sourceElement != null){
								String radix = getDOMParameter(sourceElement, "radix");
								try{
									sourceRadix = Integer.parseInt(radix);
								}
								catch(NumberFormatException nfe){
									throw new ResourceFormatException("Data format error: attribute=radix/element=source<" + source + ">/element=mapping/element=encoding/resource=" + resourceName, nfe);
								}
							}
							int reviseRadix = 10;
							Element reviseElement = getChildElement(element, "revise");
							if(reviseElement != null){
								String radix = getDOMParameter(reviseElement, "radix");
								try{
									reviseRadix = Integer.parseInt(radix);
								}
								catch(NumberFormatException nfe){
									throw new ResourceFormatException("Data format error: attribute=radix/element=revise<" + revise + ">/element=mapping/element=encoding/resource=" + resourceName, nfe);
								}
							}

							try{
								int sourceChar = Integer.parseInt(source, sourceRadix);
								if(sourceChar < Character.MIN_VALUE || sourceChar > Character.MAX_VALUE){
									// 不正な設定値
									throw new ResourceFormatException("Illegal character: character='\\u" + Integer.toHexString(sourceChar) + "'/source=" + source + "/radix=" + sourceRadix + "/resource=" + resourceName);
								}

								try{
									int reviseChar = Integer.parseInt(revise, reviseRadix);
									if(reviseChar < Character.MIN_VALUE || reviseChar > Character.MAX_VALUE){
										// 不正な設定値
										throw new ResourceFormatException("Illegal character: character='\\u" + Integer.toHexString(reviseChar) + "'/revise=" + revise + "/radix=" + reviseRadix + "/resource=" + resourceName);
									}

									mapping[sourceChar] = (char) reviseChar;
								}
								catch(NumberFormatException nfe){
									throw new ResourceFormatException("Data format error: revise=" + revise + "/radix=" + reviseRadix + "/resource=" + resourceName, nfe);
								}
								catch(ArrayIndexOutOfBoundsException aioobe){
									throw new ResourceFormatException("Unsupported character: character='\\u" + Integer.toHexString(sourceChar) + "'/source=" + source + "/radix=" + sourceRadix + "/resource=" + resourceName, aioobe);
								}
							}
							catch(NumberFormatException nfe){
									throw new ResourceFormatException("Data format error: source=" + source + "/radix=" + sourceRadix + "/resource=" + resourceName, nfe);
							}
						}
					}

					this.mappingMap.put(enc, mapping);
					return mapping;
				}
				else{
					// 定義エラー
					throw new ResourceFormatException("Document-Element <encoding> is not defined: document=" + documentElement.getTagName() + "/resource=" + resourceName);
				}
			}
		}

		// チェックと返却
		if(mapping != null){
			return mapping;
		}
		else{
			throw new UnsupportedEncodingException("Revise mapping is not found: name=" + enc);
		}
	}


// ----- Private interface
	/**
	 * 指定されたノード名を持つ子ノードを返します。<p>
	 * このメソッドは、子ノードの持つ子ノードまで再帰的に検索しません。
	 * つまり返却されるエレメントは、
	 * 指定されたノードを親とするノードとなります。
	 * @param nodeName ノード名
	 * @return ノード。該当ノードがない場合は null
	 */
	private Element getChildElement(Element root, String nodeName){
		NodeList nodeList = root.getChildNodes();
		for(int index = 0; index < nodeList.getLength(); index++){
			Node node = nodeList.item(index);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) node;
				if(element.getTagName().equals(nodeName)){
					// 該当ノードを発見
					return element;
				}
			}
		}

		return null;
	}

	/**
	 * 指定のパラメータ名にマッピングされているパラメータ値を返します。<p>
	 * パラメータは、以下の順に検索されます。<br>
	 * <ul>
	 * <li>対象の Element の属性
	 * <li>対象の Element の子ノード(Element)
	 * </ul>
	 * また、パラメータ値は前後の空白および改行等が除去されます。
	 * 従って、例えば設定値が空白のみの場合、空文字列が返されます。
	 * @param element 検索対象のノード
	 * @param name パラメータ名
	 * @return 指定のパラメータ名にマッピングされるパラメータ値。パラメータが存在しない場合は null。
	 */
	private String getDOMParameter(Element root, String name){
		String parameterValue = null;

		Attr nodeAttribute = root.getAttributeNode(name);
		if(nodeAttribute != null){
			// 属性値の取得
			parameterValue = nodeAttribute.getValue();
		}
		else{
			// 子ノードの検索
			NodeList nodeList = root.getChildNodes();
			for(int index = 0; index < nodeList.getLength(); index++){
				Node node = nodeList.item(index);
				if(node.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) node;
					if(element.getTagName().equals(name)){
						// 該当ノードを発見
						NodeList childNodes = element.getChildNodes();
						for(int idx = 0; idx < childNodes.getLength(); idx++){
							Node item = childNodes.item(idx);
							if(item.getNodeType() == Node.TEXT_NODE){
								parameterValue = item.getNodeValue();
								if(parameterValue != null){
									if(parameterValue.length() > 0){ break; }
								}
							}
						}
					}
				}

				if(parameterValue != null){ break; }	// 発見されたらしい
			}
		}

		// パラメータ値の不要な部分(前後の空白)を除去して返却
		if(parameterValue != null){
			String returnValue = parameterValue.trim();
			if(returnValue.length() > 0){
				return returnValue;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}

	/**
	 * 指定のリソースを返します。
	 * @param name リソース名
	 * @return リソースの入力ストリーム。見つからない場合は null。
	 */
	private InputStream getResourceAsStream(String name){
		// このクラスを読み込んだクラスローダを利用して検索
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(name);
		if(in != null){
			return in;
		}
		else{
			// 現在のスレッドのコンテキストクラスローダを利用して再検索
			Thread thread = Thread.currentThread();
			classLoader = thread.getContextClassLoader();
			return classLoader.getResourceAsStream(name);
		}
	}

	/**
	 * 指定の入力ストリームからＸＭＬソースを読み込んで DOM を作成します。
	 * @param in ＸＭＬソースの入力ストリーム
	 * @return 解析結果
	 */
	private Document getDOMDocument(InputStream in) throws ParserConfigurationException, IOException, SAXException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.parse(in);
	}

	/**
	 * デフォルトの文字マッピング配列を返します。
	 * デフォルトの配列とは、配列の要素番号とその要素のコードが同じモノです。
	 * @return 文字配列
	 */
	private char[] newCharacterMapping(){
		// Unicode の文字(= char)は 16 bit なので、16 bit の要素数の配列を作成
		char[] chars = new char[1 << 16];

		// 初期値の設定
		for(int idx = 0; idx < chars.length; idx++){ chars[idx] = (char) idx; }

		return chars;		// 返却
	}

	/**
	 * 指定の文字エンコーディングに対応した設定ファイルパスを返します。
	 * @param enc 文字エンコーディング名
	 * @return 設定ファイルパス
	 */
	private synchronized String getResourceName(String enc){
		// 設定ファイルのリソース名を取得
		String resourceName = this.resourceMapping.getProperty(enc);
		if(resourceName != null){
			return resourceName;
		}
		else{
			if(! unsupportedEncodingSet.contains(enc)){
				// 大文字・小文字の判定を無視して検索
				String ignoreName = enc.toUpperCase();
				Enumeration cursor = this.mappingProperties.propertyNames();
				while(cursor.hasMoreElements()){
					String name = (String) cursor.nextElement();
					if(ignoreName.equals(name.toUpperCase())){
						resourceName = this.resourceMapping.getProperty(name);
						this.resourceMapping.setProperty(enc, resourceName);
						return resourceName;
					}
				}

				// 類似定義も発見できず
				unsupportedEncodingSet.add(enc);
				return null;
			}
			else{
				return null;
			}
		}
	}
}

