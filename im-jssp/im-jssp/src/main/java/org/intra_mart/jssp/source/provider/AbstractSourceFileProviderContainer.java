package org.intra_mart.jssp.source.provider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.intra_mart.jssp.source.SourceFile;

/**
 * SourceFileProviderコンテナ（SourceFileProviderを格納するクラス）の抽象実装です。
 * 最初に基礎となるSourceFileProviderを使ってソースを検索します。
 * 基礎となるSourceFileProviderが FileNotFoundException をスローした場合に限り、
 * {@link #findSource(java.lang.String path)}メソッドを実行します。
 */
public abstract class AbstractSourceFileProviderContainer implements SourceFileProvider{
	/**
	 * 基礎となるSourceFileProvider
	 */
	private SourceFileProvider primarySourceFileProvider = null;

	/**
	 * 指定のSourceFileProviderを基礎となるSourceFileProviderとする新しいコンテナを作成します。
	 * @param sourceFileProvider マネージャ
	 */
	protected AbstractSourceFileProviderContainer(SourceFileProvider sourceFileProvider){
		super();

		if(sourceFileProvider != null){
			this.primarySourceFileProvider = sourceFileProvider;
		}
		else{
			throw new NullPointerException("sourceFileProvider is null.");
		}
	}

	/**
	 * 新しいSourceFileProviderコンテナを作成します。
	 */
	protected AbstractSourceFileProviderContainer(){
		this(new EmptySourceFileProvider());
	}


	/**
	 * ソースを読み込んで返します。
	 * @param path ソースのパス
	 * @return ソースデータ
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public final SourceFile getSourceFile(String path) throws FileNotFoundException, IOException{
		try{
			return this.primarySourceFileProvider.getSourceFile(path);
		}
		catch(FileNotFoundException fnfe){
			return this.findSource(path);
		}
	}

	/**
	 * ソースを読み込んで返します。
	 * @param path ソースのパス
	 * @return ソースデータ
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	protected abstract SourceFile findSource(String path) throws FileNotFoundException, IOException;
}

