package org.intra_mart.jssp.source.provider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;


/**
 * SourceFileProviderを管理するマネージャです。
 */
public class SourceFileProviderManager {
	/**
	 * SourceFileProvider格納用Map<br>
	 * ロケールをキーにして、SourceFileProviderをマッピングしています。
	 */
	private static Map<Locale, SourceFileProvider> sourceManagerMap = new HashMap<Locale, SourceFileProvider>();

	/**
	 * ロケールに紐づいたSourceFileProviderを返却します。
	 * @param locale ロケール
	 * @return SourceFileProvider
	 */
	public static synchronized SourceFileProvider getSourceFileProvider(Locale locale){
		SourceFileProvider sourceFileProvider = (SourceFileProvider) sourceManagerMap.get(locale);

		if(sourceFileProvider == null){

			// primary
			SourceFileProviderContainerBuilder primaryBuilder = get1stSourceFileProviderContainerBuilder();
			SourceFileProvider primary = primaryBuilder.createSourceFileProvider(locale);

			// secondary
			SourceFileProviderContainerBuilder secondaryBuilder = get2ndSourceFileProviderContainerBuilder();
			SourceFileProvider secondary = secondaryBuilder.createSourceFileProvider(locale);

			// 合体！
			sourceFileProvider = new DualSourceFileProviderContainer(primary, secondary);
			
			// キャッシュ
			sourceManagerMap.put(locale, sourceFileProvider);
		}

		return sourceFileProvider;
	}
	
	
	/**
	 * @return
	 */
	private static SourceFileProviderContainerBuilder get1stSourceFileProviderContainerBuilder(){

		String key = "source-file-provider-container-builder-1st";
		Class[] ctorTypes = null;
		Object[] ctorPrams = null;
		try {
			return (SourceFileProviderContainerBuilder)
						JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
		}
		catch (Exception e) {
			throw new IllegalStateException("Instantiation Error : " + key, e);
		}
	}

	/**
	 * @return
	 */
	private static SourceFileProviderContainerBuilder get2ndSourceFileProviderContainerBuilder(){

		String key = "source-file-provider-container-builder-2nd";
		Class[] ctorTypes = null;
		Object[] ctorPrams = null;
		try {
			return (SourceFileProviderContainerBuilder)
						JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
		}
		catch (Exception e) {
			throw new IllegalStateException("Instantiation Error : " + key, e);
		}
	}
}

