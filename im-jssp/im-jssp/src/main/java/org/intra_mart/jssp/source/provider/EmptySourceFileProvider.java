package org.intra_mart.jssp.source.provider;

import java.io.FileNotFoundException;

import org.intra_mart.jssp.source.SourceFile;


/**
 * ソースファイルにアクセスするクラスです。
 * このクラスは、ソースファイルを検索しません。
 * したがって、検索結果は必ずエラー（{@link java.io.FileNotFoundException}）に
 * なります。
 */
public class EmptySourceFileProvider implements SourceFileProvider{
	/**
	 * ソースを読み込むオブジェクトを新しく作成します。
	 */
	public EmptySourceFileProvider(){
		super();
	}

	/**
	 * ソースを読み込んで返します。
	 * このメソッドは、必ず FileNotFoundException をスローします。
	 * @param path ソースのパス
	 * @return なし。（このメソッドは、必ず FileNotFoundException をスローします）
	 * @throws FileNotFoundException
	 */
	public SourceFile getSourceFile(String path) throws FileNotFoundException{
		throw new FileNotFoundException(path);
	}
}

