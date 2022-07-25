package org.intra_mart.jssp.source.provider;

import java.util.Locale;

import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;


/**
 * SourceFileProviderコンテナを作成する実装クラスです。
 * コンフィグファイルの設定を元に、ソース検索の基準となるディレクトリを決定します。
 */
public class SourceFileProviderContainerBuilderImpl extends AbstractSourceFileProviderContainerBuilder{

	/**
	 * ビルダを新しく作成します。
	 */
	public SourceFileProviderContainerBuilderImpl(){
		super();
	}

	/**
	 * 標準のソース検索の基準ディレクトリパスを返します。
	 * このメソッドが返す値は、常に同じです。
	 * @return ディレクトリパスの配列
	 */
	protected String[] getSourceDirectories(){
		return JSSPConfigHandlerManager.getConfigHandler().getGeneralSourceDirectories();		
	}

	/**
	 * ロケールに紐づいたソース検索の基準ディレクトリパスを返します。
	 * @param locale ロケール
	 * @return ディレクトリパスの配列
	 */
	protected String[] getSourceDirectories(Locale locale){
		return JSSPConfigHandlerManager.getConfigHandler().getSourceDirectories(locale);
	}

}

