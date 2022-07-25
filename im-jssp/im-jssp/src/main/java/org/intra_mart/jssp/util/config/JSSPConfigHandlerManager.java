package org.intra_mart.jssp.util.config;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;

/**
 * JSSP実行環境のコンフィグ・ハンドラを管理するマネージャです。
 */
public class JSSPConfigHandlerManager {
	
	private static JSSPConfigHandler _instance;
	
	/**
	 * コンフィグ・ハンドラの実装を返却します。
	 * 
	 * @return コンフィグ・ハンドラ
	 */
	public static synchronized JSSPConfigHandler getConfigHandler(){
		
		if(_instance == null){
			return getConfigHandler(null);
		}
		else{
			return _instance;		
		}
	}

	
	/**
	 * コンフィグ・ハンドラの実装を返却します。
	 * 引数で指定されたファイルパスのファイルをコンフィグファイルとします。
	 * 
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 * @param configFilePath
	 * @return
	 */
	public static synchronized JSSPConfigHandler getConfigHandler(String configFilePath){
		
		String key = "jssp-config-handler";
		Class[] ctorTypes  = (configFilePath == null) ? null : new Class[]{String.class};
		Object[] ctorPrams = (configFilePath == null) ? null : new String[]{configFilePath};

		try {
			_instance = (JSSPConfigHandler)
						JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
		}
		catch (Exception e) {
			throw new IllegalStateException("Instantiation Error : " + key, e);
		}

		return _instance;		
	}


}
