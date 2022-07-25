package org.intra_mart.jssp.view.provider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;
import org.intra_mart.jssp.util.locale.LocaleHandler;
import org.intra_mart.jssp.util.locale.LocaleHandlerManager;

/**
 * ViewScopeビルダを管理するマネージャです。
 */
public class ViewScopeBuilderManager {
	/**
	 * ロケールをキーとしてビルダをマッピングします。
	 */
	private static Map<Locale, ViewScopeBuilder> builderMap = new HashMap<Locale, ViewScopeBuilder>();

	
	/**
	 * デフォルトロケールに対応したビルダを返します。
	 * @return ビルダ
	 */
	public static ViewScopeBuilder getBuilder(){
		LocaleHandler handler = LocaleHandlerManager.getLocaleHandler();
		return getBuilder(handler.getLocale());
	}

	/**
	 * 指定のロケールに対応したビルダを返します。
	 * @param locale ロケール
	 * @return ビルダ
	 */
	public static synchronized ViewScopeBuilder getBuilder(Locale locale) {
		ViewScopeBuilder builder = (ViewScopeBuilder) builderMap.get(locale);
	
		if(builder == null){

			String key = "view-scope-builder";
			Class[] ctorTypes = {Locale.class};
			Object[] ctorPrams = {locale};
			try {
				builder = (ViewScopeBuilder)
							JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
			}
			catch (Exception e) {
				throw new IllegalStateException("Instantiation Error : " + key, e);
			}

			builderMap.put(locale, builder);
		}
	
		return builder;
	}

}