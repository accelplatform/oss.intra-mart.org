package org.intra_mart.jssp.source.provider;

import java.util.Locale;

/**
 * SourceFileProviderコンテナを作成する抽象クラスです。
 */
public abstract class AbstractSourceFileProviderContainerBuilder implements SourceFileProviderContainerBuilder{

	protected AbstractSourceFileProviderContainerBuilder(){
		super();
	}

	/**
	 * 標準のソース検索の基準ディレクトリパスを返します。
	 * このメソッドが返す値は、常に同じです。
	 * @return ディレクトリパスの配列
	 */
	protected abstract String[] getSourceDirectories();

	/**
	 * ロケールに紐づいたソース検索の基準ディレクトリパスを返します。
	 * @param locale ロケール
	 * @return ディレクトリパスの配列
	 */
	protected abstract String[] getSourceDirectories(Locale locale);


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.source.provider.SourceFileProviderContainerBuilder#createSourceFileProvider(java.util.Locale)
	 */
	public SourceFileProvider createSourceFileProvider(Locale locale){

		String[] localeDirs = this.getSourceDirectories(locale);
		if(localeDirs != null){
			if(localeDirs.length > 0){
				SourceFileProvider provider = this.createProviderContainer(localeDirs);

				String[] stdDirs = this.getSourceDirectories();
				if(stdDirs != null){
					if(stdDirs.length > 0){
						return new DualSourceFileProviderContainer(provider, this.createProviderContainer(stdDirs));
					}
				}

				return provider;
			}
		}

		String[] stdDirs = this.getSourceDirectories();
		if(stdDirs != null){
			if(stdDirs.length > 0){
				return this.createProviderContainer(stdDirs);
			}
		}

		return new EmptySourceFileProvider();
	}

	/**
	 * 指定のディレクトリを検索パスとするSourceFileProviderコンテナを作成して返します。
	 */
	private SourceFileProvider createProviderContainer(String[] dirs){
		SourceFileProvider manager = new EmptySourceFileProvider();

		for(int idx = 0; idx < dirs.length; idx++){
			manager = new DualSourceFileProviderContainer(manager, LocalSourceFileProvider.getInstance(dirs[idx]));
		}

		return manager;
	}
}

