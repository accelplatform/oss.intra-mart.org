package org.intra_mart.jssp.util.charset;

/**
 * JSSP 実行環境における文字セットを決定するための実装です。
 * <p>
 * この実装は、Java-VMのデフォルトエンコーディングを返します。
 *
 */
public class DefaultCharsetHandler implements CharsetHandler{

	/**
	 * コンストラクタ
	 */
	public DefaultCharsetHandler(){
		super();
	}

	/**
	 * Java-VMのデフォルトエンコーディング名を返します。
	 * @return 文字エンコーディング名
	 */
	public String getCharacterEncoding(){
		return System.getProperty("file.encoding");
	}
}
