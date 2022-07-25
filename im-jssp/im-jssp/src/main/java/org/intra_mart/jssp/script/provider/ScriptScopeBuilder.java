package org.intra_mart.jssp.script.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.intra_mart.jssp.script.ScriptScope;
import org.mozilla.javascript.JavaScriptException;

/**
 * JavaScript 実行可能オブジェクト「{@link org.intra_mart.script.ScriptScope}」を生成します。
 */
public interface ScriptScopeBuilder {

	/**
	 * このビルダが扱っているロケールを返します。
	 * @return ロケール
	 */
	public Locale getLocale();

	/**
	 * クラスファイルの出力先ディレクトリ
	 * @return ディレクトリパスを表す File オブジェクト
	 */
	public File getOutputDirectory();

	/**
	 * 指定されたパスのJavaScriptソースを実行し、JavaScript実行可能オブジェクト（=実行スコープ）を返します。<br/>
	 * <br/>
	 * このメソッドは、JavaScriptソースの実行結果を、新たに生成した実行スコープに反映させ、そのスコープを返却します。<br/>
	 * （<code>#getScriptScope(path, null, null)</code> と同等の動作です）
	 * 
	 * @param path ソースパス(拡張子を除く)
	 * @return JavaScript実行可能オブジェクト（=実行スコープ）
	 * 
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ScriptScope getScriptScope(String path)
			throws JavaScriptException, 
					FileNotFoundException,
					InstantiationException,
					IllegalAccessException,
					IOException,
					ClassNotFoundException;

	/**
	 * 指定されたパスのJavaScriptソースを実行し、JavaScript実行可能オブジェクト（=実行スコープ）を返します。<br/>
	 * <br/>
	 * このメソッドは、JavaScriptソースの実行結果を、新たに生成した実行スコープに反映させ、そのスコープを返却します。<br/>
	 * （<code>#getScriptScope(path, null, sourceDir)</code> と同等の動作です）<br/>
	 * <br/>
	 * 
	 * @param path ソースパス(拡張子を除く)
	 * @param sourceDir　ソースディレクトリ
	 * @return JavaScript実行可能オブジェクト（=実行スコープ）
	 * 
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ScriptScope getScriptScope(String path, File sourceDir)
			throws JavaScriptException, 
					FileNotFoundException,
					InstantiationException,
					IllegalAccessException,
					IOException,
					ClassNotFoundException;

	/**
	 * 指定されたパスのJavaScriptソースを実行し、JavaScript実行可能オブジェクト（=実行スコープ）を返します。<br/>
	 * <br/>
	 * JavaScriptソースの実行結果が、引数「scope」で指定された実行スコープに反映され、そのスコープが返却されます。<br/>
	 * （<code>#getScriptScope(path, scope, null)</code> と同等の動作です）
	 * 
	 * @param path ソースパス(拡張子を除く)
	 * @param scope スクリプトの実行結果を反映させる実行スコープ
	 * @return JavaScriptソースの実行結果反映後のJavaScript実行可能オブジェクト（=実行スコープ）
	 * 
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ScriptScope getScriptScope(String path, ScriptScope scope)
			throws JavaScriptException, 
					FileNotFoundException,
					InstantiationException,
					IllegalAccessException,
					IOException,
					ClassNotFoundException;

	/**
	 * 指定されたパスのJavaScriptソースを実行し、JavaScript実行可能オブジェクト（=実行スコープ）を返します。<br/>
	 * <br/>
	 * JavaScriptソースの実行結果が、引数「scope」で指定された実行スコープに反映され、そのスコープが返却されます。<br/>
	 * 引数「scope」が null　の場合、新たにJavaScript実行可能オブジェクト（実行スコープ）が生成されます。<br/>
	 * <br/>
	 * このメソッドを利用した場合、まず、引数 「sourceDir」 をソースディレクトリとしてJSソースファイルの検索を行います。<br/>
	 * その後に、コンフィグファイルに設定されているソースディレクトリをもとに検索を行います。<br/>
	 * <br/>
	 * 
	 * @param path ソースパス(拡張子を除く)
	 * @param scope スクリプトの実行結果を反映させる実行スコープ
	 * @param sourceDir　ソースディレクトリ
	 * @return JavaScriptソースの実行結果反映後のJavaScript実行可能オブジェクト（=実行スコープ）
	 * 
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ScriptScope getScriptScope(String path, ScriptScope scope, File sourceDir)
			throws JavaScriptException, 
					FileNotFoundException,
					InstantiationException,
					IllegalAccessException,
					IOException,
					ClassNotFoundException;

}