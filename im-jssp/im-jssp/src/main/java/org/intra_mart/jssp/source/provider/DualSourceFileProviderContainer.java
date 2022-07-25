package org.intra_mart.jssp.source.provider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.intra_mart.jssp.source.SourceFile;


/**
 * 2つのSourceFileProviderからソースを検索するクラスです。<br>
 * 
 * 最初に基礎となるSourceFileProviderを使ってソースを検索します。<br>
 * 基礎となるSourceFileProviderが FileNotFoundException をスローした場合に限り、
 * {@link #findSource(java.lang.String path)}メソッドを実行します。<br>
 * {@link #findSource(java.lang.String path)}は、secondarySourceFileProviderを使ってソースを検索します。
 */
public class DualSourceFileProviderContainer extends AbstractSourceFileProviderContainer{

	/**
	 * このコンテナがソース検索に利用するマネージャ
	 */
	private SourceFileProvider secondarySourceFileProvider = null;

	/**
	 * 2つのSourceFileProviderからソースを検索する新しいSourceFileProviderコンテナを作成します。
	 * @param primarySourceFileProvider 基礎となるSourceFileProvider
	 * @param secondarySourceFileProvider SourceFileProvider
	 */
	public DualSourceFileProviderContainer(SourceFileProvider primarySourceFileProvider, 
											SourceFileProvider secondarySourceFileProvider) {
		super(primarySourceFileProvider);

		this.secondarySourceFileProvider = secondarySourceFileProvider;
	}


	/**
	 * ソースを読み込んで返します。
	 * @param path ソースのパス
	 * @return ソースデータ
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	protected SourceFile findSource(String path) throws FileNotFoundException, IOException{
		return this.secondarySourceFileProvider.getSourceFile(path);
	}
}
