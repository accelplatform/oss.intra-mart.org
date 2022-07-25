package org.intra_mart.jssp.util.locale;

import java.util.Locale;

/**
 * JSSP 実行環境におけるロケールを決定するためのインタフェースです。
 * <p>
 * このインタフェースが返すロケールによって、
 * 実行プログラムソースが切り替わります。
 */
public interface LocaleHandler{
	
	/**
	 * 現在の実行スレッドで有効なロケールを返します。
	 * @return ロケール
	 */
	public Locale getLocale();
}


