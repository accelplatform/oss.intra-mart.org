package org.intra_mart.jssp.source.provider;

import java.util.Locale;


/**
 * {@link EmptySourceFileProvider}を格納するSourceFileProviderjコンテナを作成します。
 */
public class EmptySourceFileProviderContainerBuilder implements
		SourceFileProviderContainerBuilder {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.source.provider.SourceFileProviderContainerBuilder#createSourceFileProvider(java.util.Locale)
	 */
	public SourceFileProvider createSourceFileProvider(Locale locale) {
		return new EmptySourceFileProvider();
	}

}
