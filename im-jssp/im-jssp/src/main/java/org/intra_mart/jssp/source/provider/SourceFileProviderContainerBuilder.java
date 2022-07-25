package org.intra_mart.jssp.source.provider;

import java.util.Locale;

public interface SourceFileProviderContainerBuilder {

	/**
	 * SourceFileProviderを返します。
	 * @param locale ロケール
	 * @return SourceFileProvider
	 */
	public SourceFileProvider createSourceFileProvider(Locale locale);

}