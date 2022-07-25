package org.intra_mart.jssp.script.provider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;
import org.intra_mart.jssp.util.locale.LocaleHandler;
import org.intra_mart.jssp.util.locale.LocaleHandlerManager;

/**
 * ScriptScopeBuilderを管理するマネージャです。
 */
public class ScriptScopeBuilderManager {

	/**
	 * ロケールをキーとしてビルダをマッピングします。
	 */
	private static Map<Locale, ScriptScopeBuilder> builderMap = new HashMap<Locale, ScriptScopeBuilder>();

	
	/**
	 * デフォルトロケールに対応したビルダを返します。
	 * @return ビルダ
	 */
	public static ScriptScopeBuilder getBuilder(){
		LocaleHandler handler = LocaleHandlerManager.getLocaleHandler();
		return ScriptScopeBuilderManager.getBuilder(handler.getLocale());
	}

	/**
	 * 指定のロケールに対応したビルダを返します。
	 * @param locale ロケール
	 * @return ビルダ
	 */
	public static synchronized ScriptScopeBuilder getBuilder(Locale locale) {
		ScriptScopeBuilder builder = (ScriptScopeBuilder) builderMap.get(locale);
	
		if(builder == null){

			String key = "script-scope-builder";
			Class[] ctorTypes = {Locale.class};
			Object[] ctorPrams = {locale};
			try {
				builder = (ScriptScopeBuilder)
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
