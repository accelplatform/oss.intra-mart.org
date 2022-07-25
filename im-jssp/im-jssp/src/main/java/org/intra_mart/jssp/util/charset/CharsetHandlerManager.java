package org.intra_mart.jssp.util.charset;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;

/**
 * 文字セットハンドラを管理するマネージャです。
 */
public class CharsetHandlerManager {

	/**
	 * 文字セットハンドラ
	 */
	private static CharsetHandler charsetHandler = null;
	
	/**
	 * 文字セットハンドラの実装クラスを返却します。
	 * @return 文字セットハンドラ
	 */
	public static synchronized CharsetHandler getCharsetHandler(){
	
		if(charsetHandler == null){
			String key = "charset-handler";
			Class[] ctorTypes = null;
			Object[] ctorPrams = null;
			try {
				charsetHandler = (CharsetHandler)
							JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
			}
			catch (Exception e) {
				throw new IllegalStateException("Instantiation Error : " + key, e);
			}
		}
		
		return charsetHandler;
			
	}

}
