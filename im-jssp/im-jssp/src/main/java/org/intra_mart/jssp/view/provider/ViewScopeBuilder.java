package org.intra_mart.jssp.view.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.intra_mart.jssp.view.ViewScope;

/**
 * ViewScopeビルダー
 */
public interface ViewScopeBuilder {

	/**
	 * このビルダが扱っているロケールを返します。
	 * @return ロケール
	 */
	public abstract Locale getLocale();

	/**
	 * クラスファイルの出力先ディレクトリ
	 * @return ディレクトリパスを表す File オブジェクト
	 */
	public abstract File getOutputDirectory();

	/**
	 * 実行スコープを作成して返します。
	 * @param path ソースパス(拡張子を除く)
	 * @return 実行可能スコープ
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public abstract ViewScope getViewScope(String path)
				throws FileNotFoundException, 
						InstantiationException, 
						IllegalAccessException, 
						IOException, 
						ClassNotFoundException;

}
