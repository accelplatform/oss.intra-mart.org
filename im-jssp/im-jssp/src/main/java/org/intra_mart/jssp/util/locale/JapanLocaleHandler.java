package org.intra_mart.jssp.util.locale;

import java.util.Locale;

/**
 * JSSP 実行環境におけるロケールを決定するための実装です。
 * <p>
 * この実装は、常に java.util.Locale.JAPAN を返します。
 */
public class JapanLocaleHandler implements LocaleHandler{
	/**
	 * コンストラクタ
	 */
	public JapanLocaleHandler(){
		super();
	}

	/**
	 * 現在の実行スレッドで有効なロケールを返します。
	 * この実装は、常に java.util.Locale.JAPAN を返します。
	 * @return ロケール
	 */
	public Locale getLocale(){
		return Locale.JAPAN;
	}
}
