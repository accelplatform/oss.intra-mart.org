package org.intra_mart.jssp.util.charset;

/**
 * JSSP 実行環境における文字セットを決定するためのインタフェースです。
 * <p>
 * このインタフェースが返す文字セットによって、
 * 実行プログラムソースが切り替わります。
 */
public interface CharsetHandler{
	
	/**
	 * 現在の実行スレッドで有効な文字エンコーディング名を返します。
	 * @return 文字エンコーディング名
	 */
	public String getCharacterEncoding();
	
}


