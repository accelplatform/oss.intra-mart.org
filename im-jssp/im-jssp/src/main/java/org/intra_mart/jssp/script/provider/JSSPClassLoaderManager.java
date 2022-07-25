package org.intra_mart.jssp.script.provider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.intra_mart.jssp.util.ClassLoaderWrapper;
import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;

/**
 * JSSP実行環境のClassLoaderを管理するマネージャです。
 */
public class JSSPClassLoaderManager {
		
	/**
	 * ロケールに関連づいたクラスローダのマップ
	 */
	private static Map<Locale, ClassLoader> classLoaderMap = new HashMap<Locale, ClassLoader>();
	/**
	 * ロケールに依存しない基本となるクラスローダ
	 */
	private static ClassLoader generalClassLoader = null;

	/**
	 * 指定のロケールに対応したクラスローダを返します。
	 * @param locale ロケール
	 * @return クラスローダ
	 */
	public static synchronized ClassLoader getClassLoader(Locale locale){
		ClassLoader classLoader = (ClassLoader) classLoaderMap.get(locale);

		if(classLoader == null){
			
			// 指定のロケールに対応したクラスローダを作成
			String key = "jssp-class-loader_i18n";
			Class[] ctorTypes = {Locale.class};
			Object[] ctorPrams = {locale};
			try {
				classLoader = (ClassLoader)
							JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
			}
			catch (Exception e) {
				throw new IllegalStateException("Instantiation Error : " + key, e);
			}
			

			// ロケールの階層構造に対応したクラスローダに変換
			if(locale.getVariant().length() > 0){
				if(locale.getCountry().length() > 0){
					Locale parentLocale = new Locale(locale.getLanguage(), locale.getCountry());
					ClassLoader parentLoader = getClassLoader(parentLocale);
					classLoader = new ClassLoaderWrapper(parentLoader, classLoader);
				}
				else{
					if(locale.getLanguage().length() > 0){
						Locale parentLocale = new Locale(locale.getLanguage());
						ClassLoader parentLoader = getClassLoader(parentLocale);
						classLoader = new ClassLoaderWrapper(parentLoader, classLoader);
					}
				}
			}
			else{
				if(locale.getCountry().length() > 0){
					if(locale.getLanguage().length() > 0){
						Locale parentLocale = new Locale(locale.getLanguage());
						ClassLoader parentLoader = getClassLoader(parentLocale);
						classLoader = new ClassLoaderWrapper(parentLoader, classLoader);
					}
				}
			}

			// キャッシュ
			classLoaderMap.put(locale, classLoader);
		}

		return classLoader;
	}

	/**
	 * ロケールに依存しないクラスローダを返します。
	 * @return クラスローダ
	 */
	public static synchronized ClassLoader getClassLoader(){
		
		if(generalClassLoader == null){
			
			String key = "jssp-class-loader_general";
			Class[] ctorTypes = null;
			Object[] ctorPrams = null;
			try {
				generalClassLoader = (ClassLoader)
							JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
			}
			catch (Exception e) {
				throw new IllegalStateException("Instantiation Error : " + key, e);
			}
		
		}
		return generalClassLoader;
	}
	
	
}
