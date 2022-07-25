package org.intra_mart.jssp.source.property;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.jssp.util.ThreadContextClassLoader;
import org.xml.sax.SAXException;


/**
 * 基礎となる設定です。
 */
public class FoundationSourceProperties extends GenericSourceProperties{
	/**
	 * 設定ファイルのリソース名
	 */
	private static final String DEFAULT_RESOURCE_FILE_NAME = "/org/intra_mart/jssp/source/property/default-foundation-source-config.xml";
	private static final String RESOURCE_FILE_NAME         = "/org/intra_mart/jssp/source/property/foundation-source-config.xml";

	
	/**
	 * 唯一のインスタンス
	 */
	private static FoundationSourceProperties _instance = null;

	/**
	 * 基礎設定情報オブジェクトを返します。
	 */
	public static synchronized FoundationSourceProperties instance() throws IOException{
		if(_instance == null){
			ClassLoader classLoader = new ThreadContextClassLoader();
			InputStream in = classLoader.getResourceAsStream(RESOURCE_FILE_NAME);

			if(in == null){	
				
				in = classLoader.getResourceAsStream(DEFAULT_RESOURCE_FILE_NAME);
				
				// デフォルトも存在しない。。。
				if(in == null){	

					// このクラスのクラスローダでもう一度検索
					in = FoundationSourceProperties.class.getResourceAsStream(DEFAULT_RESOURCE_FILE_NAME);

					if(in == null){	
						throw new FileNotFoundException("Resource not found: " + DEFAULT_RESOURCE_FILE_NAME);
					}
				}
			}
			try{
				_instance = new FoundationSourceProperties(in);
			}
			catch(SAXException saxe){
				IllegalStateException ise = new IllegalStateException("Configuration file is broken.");
				ise.initCause(saxe);
				throw ise;
			}
			catch(ParserConfigurationException pce){
				IllegalStateException ise = new IllegalStateException("Configuration file is broken.");
				ise.initCause(pce);
				throw ise;
			}
		}

		return _instance;
	}

	/**
	 * コンストラクタ
	 * @param in 設定ソースの入力ストリーム
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private FoundationSourceProperties(InputStream in) throws ParserConfigurationException, SAXException, IOException{
		super(in);
	}

	/**
	 * ディレクトリに対する設定を記述した設定ファイルのパス名を返します。
	 * @return 設定ファイルパスを返します。
	 */
	public String getSourcePropertyName(){
		// TODO [OSS-JSSP] 別ファイルに切り出しか？
		return this.getProperty("/resource-file/config");
	}
}

