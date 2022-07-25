package org.intra_mart.jssp.util.locale;

import java.util.Locale;

/**
 * JSSP 実行環境におけるロケールを決定するための実装です。
 * <p>
 * この実装は、常に java.util.Locale.US を返します。
 */
public class UsLocaleHandler implements LocaleHandler{
	/**
	 * コンストラクタ
	 */
	public UsLocaleHandler(){
		super();
	}

	/**
	 * 現在の実行スレッドで有効なロケールを返します。
	 * この実装は、常に java.util.Locale.US を返します。
	 * @return ロケール
	 */
	public Locale getLocale(){
		return Locale.US;
	}
}
