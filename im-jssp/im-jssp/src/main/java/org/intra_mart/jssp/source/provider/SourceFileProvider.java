package org.intra_mart.jssp.source.provider;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.intra_mart.jssp.source.SourceFile;


/**
 * ソースファイルを扱う共通インターフェースです。
 */
public interface SourceFileProvider{
	/**
	 * ソースを読み込んで返します。
	 * @param path ソースのパス
	 * @return ソースデータ
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public SourceFile getSourceFile(String path) throws FileNotFoundException, IOException;
}

