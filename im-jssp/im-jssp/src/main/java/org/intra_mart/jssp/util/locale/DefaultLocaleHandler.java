package org.intra_mart.jssp.util.locale;

import java.util.Locale;

/**
 * JSSP 実行環境におけるロケールを決定するための実装です。
 * <p>
 * この実装は、Java-VMのデフォルトロケールを返します。
 */
public class DefaultLocaleHandler implements LocaleHandler{

	/**
	 * コンストラクタ
	 */
	public DefaultLocaleHandler(){
		super();
	}

	/**
	 * 現在の実行スレッドで有効なロケールを返します。<br>
	 * この実装は、Java-VMのデフォルトロケールを返します。
	 * @return ロケール
	 */
	public Locale getLocale(){
		return Locale.getDefault();
	}
}
