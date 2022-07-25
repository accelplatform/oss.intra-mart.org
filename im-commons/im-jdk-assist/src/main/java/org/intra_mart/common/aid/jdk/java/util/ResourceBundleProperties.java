package org.intra_mart.common.aid.jdk.java.util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * このクラスは、java.util.Properties クラスのインターフェースを実装した、
 * プロパティ管理クラスです。プロパティデータは、java.util.ResourceBundle
 * により取得されます。<BR>
 * ロードできるリソースに関しては、java.util.ResourceBundle の
 * 説明を参照して下さい。<br>
 * このクラスのコンストラクタが、java.util.ResourceBundle クラスの
 * getBundle() メソッドと同様の機能を提供しています。
 * 各コンストラクタは、引数の同じ getBundle() メソッドに対応しています。
 *
 * @see java.util.Properties
 * @see java.util.ResourceBundle
 */
public class ResourceBundleProperties extends Properties{
	private Locale currentLocale;			// このオブジェクトのロケール

	/**
	 * リソースバンドルをプロパティズとして表現したオブジェクトを作成します。<br>
	 *
	 * @param baseName 目的のオブジェクトを含むリソースバンドルのファミリ名
	 * @throws MissingResourceException リソースの照合に失敗した場合
	 */
	public ResourceBundleProperties(String baseName) throws MissingResourceException{
		this(baseName, Locale.getDefault());
	}

	/**
	 * リソースバンドルをプロパティズとして表現したオブジェクトを作成します。<br>
	 *
	 * @param baseName 目的のオブジェクトを含むリソースバンドルのファミリ名
	 * @param locale 目的のロケール
	 * @throws MissingResourceException リソースの照合に失敗した場合
	 */
	public ResourceBundleProperties(String baseName, Locale locale) throws MissingResourceException{
		this(baseName, locale, ResourceBundleProperties.class.getClassLoader());
	}

	/**
	 * リソースバンドルをプロパティズとして表現したオブジェクトを作成します。<br>
	 *
	 * @param baseName 目的のオブジェクトを含むリソースバンドルのファミリ名
	 * @param locale 目的のロケール
	 * @param loader リソースをロードする ClassLoader
	 * @throws MissingResourceException リソースの照合に失敗した場合
	 */
	public ResourceBundleProperties(String baseName, Locale locale, ClassLoader loader) throws MissingResourceException{
		super();

		// リソースの取得・・・なければ例外がスローされるハズ
		try{
			this.defileProperties(ResourceBundle.getBundle(baseName, locale, loader));
		}
		catch(MissingResourceException mre){
			this.defileProperties(ResourceBundle.getBundle(baseName, locale));
		}
	}

	/**
	 * ResourceBundle の内容をこのオブジェクトに反映します。
	 * @param rb ResourceBundle オブジェクト
	 */
	private void defileProperties(ResourceBundle rb){
		// リストの構築
		Enumeration cursor = rb.getKeys();
		while(cursor.hasMoreElements()){
			String key = (String) cursor.nextElement();
			this.setProperty(key, rb.getString(key));
		}

		this.currentLocale = rb.getLocale();		// ロケールの取得
	}

	/**
	 * ResourceBundle からオブジェクトを取得します。
	 * このメソッドは、java.util.ResourceBundle クラスと
	 * インターフェースを合わせるために提供されています。<br>
	 * 通常は、getProperty() メソッドを利用して下さい。
	 * @param key パラメータのキー
	 * @return プロパティ値
	 */
//	public String getString(String key) throws MissingResourceException{
//		String value = this.getProperty(key);
//		if(value != null){
//			return value;
//		}
//		else{
//			throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + key, this.getClass().getName(), key);
//		}
//	}

	/**
	 * ResourceBundle の Locale を返します。<br>
	 * この関数は、このオブジェクトが本当に要求されたロケールに対応しているか、
	 * またはフォールバックであるかを判定するために、このオブジェクトが
	 * 構築されたあとで使用できます。<p>
	 * この関数は、java.util.ResourceBundle クラスの getLocale() と
	 * 同じであり、java.util.ResourceBundle クラスとインターフェースを
	 * 合わせるために提供されています。
	 * java.util.ResourceBundle クラスの getLocale() の説明を参照して下さい。
	 * @return ロケール
	 */
	public Locale getLocale(){
		return currentLocale;
	}
}

