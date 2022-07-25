package org.intra_mart.jssp.util.locale;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;

/**
 * ロケールハンドラを管理するマネージャです。
 */
public class LocaleHandlerManager{	

	/**
	 * ロケールハンドラ
	 */
	private static LocaleHandler localeHandler = null;
	
	public static synchronized LocaleHandler getLocaleHandler(){
	
		if(localeHandler == null){
			String key = "locale-handler";
			Class[] ctorTypes = null;
			Object[] ctorPrams = null;
			try {
				localeHandler = (LocaleHandler)
							JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
			}
			catch (Exception e) {
				throw new IllegalStateException("Instantiation Error : " + key, e);
			}
		}
		
		return localeHandler;
			
	}
	
}

