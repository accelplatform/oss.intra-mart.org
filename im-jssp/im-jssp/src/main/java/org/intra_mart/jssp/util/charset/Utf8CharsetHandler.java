package org.intra_mart.jssp.util.charset;

/**
 * JSSP 実行環境における文字セットを決定するための実装です。
 * <p>
 * この実装は、常に「UTF-8」を返します。
 *
 */
public class Utf8CharsetHandler implements CharsetHandler{

	/**
	 * コンストラクタ
	 */
	public Utf8CharsetHandler(){
		super();
	}

	/**
	 * 常に「UTF-8」返します。
	 * @return 文字列「UTF-8」
	 */
	public String getCharacterEncoding(){
		return "UTF-8";
	}

}
