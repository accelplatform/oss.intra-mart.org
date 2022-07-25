package org.intra_mart.jssp.util.config;

import java.util.Locale;

import org.mozilla.javascript.Context;
import org.w3c.dom.NodeList;

// TODO [OSS-JSSP] コメントを書きましょう！
/**
 * JSSP実行環境に関するコンフィグファイルの設定値を取得するためのインターフェースです。
 */
public interface JSSPConfigHandler {
	
	/**
	 * サーバが使用する文字エンコーディング名を取得します。<br/>
	 * <br/>
	 * サーバは、この設定を標準文字エンコーディングとしてファイルアクセスを行います。<br/>
	 * （ファイルの入出力時にUnicode との文字コード変換する際に利用します）<br/>
	 * このメソッドの返却値は、Java-VM が解釈できる文字エンコーディング名です。<br/>
	 * 文字エンコーディング名に関しては、Java の仕様に依存します。<br/>
	 * <br/>
	 * なお、このメソッドの返却値は、
	 * ファンクションコンテナ、および、プレゼンテーションページのソースファイルを読み込む際に
	 * 利用する文字コードの設定では<b>ありません</b>。<br/>
	 * <!--
	 * ファンクションコンテナ、および、プレゼンテーションページのソースファイルを読み込む際に
	 * 利用する文字コードは source-config.xml 等で設定します。
	 * -->
	 * 
	 * @return 文字エンコーディング名
	 */
	public String getServerCharacterEncoding();
	
	/**
	 * ファンクションコンテナ（JavaScript）をコンパイルした後の、
	 * クラスファイル出力先ディレクトリを取得します。
	 * 
	 * @return ディレクトリパス
	 */
	public String getOutputDirectory4ComiledScript();

	
	/**
	 * プレゼンテーションページ（HTML）をコンパイルした後の、
	 * 中間ファイル出力先ディレクトリを取得します。
	 * 
	 * @return ディレクトリパス
	 */
	public String getOutputDirectory4ComiledView();


	public String[] getGeneralSourceDirectories();
	public String[] getSourceDirectories(Locale locale);
		
	public String[] getGeneralClassPathDirectories();	
	public String[] getGeneralClassArchives();	
	public String[] getGeneralClassArchiveDirectories();
	
	public String[] getClassPathDirectories(Locale locale);	
	public String[] getClassArchives(Locale locale);	
	public String[] getClassArchiveDirectories(Locale locale);
	
	
	/**
	 * JavaScriptAPIの実装クラス名を取得します。<br/>
	 * 本メソッドの返却値で特定されるクラスは、org.mozilla.javascript.Scriptableを実装しています。
	 * 
	 * @return JavaScriptAPIの実装クラス名の配列
	 */
	public String[] getJavaScriptAPI4Class();
	
	
	/**
	 * JavaScriptAPIの実装スクリプト名(および 登録関数名)を取得します。<br/>
	 * <br/>
	 * 本メソッドの返却値は<b>「ページパス(拡張子なし)#登録関数名」</b>形式の配列です。<br/>
	 * 「ページパス」内で定義されている関数のうち「登録関数名」と同名の関数が、<br/>
	 * ファンクションコンテナ内で実行可能となります。
	 * 
	 * @return JavaScriptAPIの実装スクリプト名(および 登録関数名)の配列
	 */
	public String[] getJavaScriptAPI4Script();

	
	/**
	 * &lt;IMART&gt;タグの実装クラス名を取得します。<br/>
	 * 本メソッドの返却値で特定されるクラスは、org.intra_mart.jssp.view.tag.ImartTagTypeを実装しています。
	 * 
	 * @return &lt;IMART&gt;タグの実装クラス名の配列
	 */
	public String[] getJSSPTags4Class();

	
	/**
	 * &lt;IMART&gt;タグの実装スクリプト名(および タグ名)を取得します。<br/>
	 * <br/>
	 * 本メソッドの返却値は<b>「ページパス(拡張子なし)#タグ名」</b>形式の配列です。<br/>
	 * 「ページパス」内で定義されている関数のうち「タグ名」と同名の関数が、<br/>
	 *  &lt;IMART&gt; タグの実行関数となります。<br/>
	 * 
	 * @return &lt;IMART&gt;タグの実装スクリプト名(および タグ名)の配列
	 */
	public String[] getJSSPTags4Script();

	
	/**
	 * 引数で指定したJavaScriptAPIの実装クラス固有の設定情報を取得します。<br/>
	 * @param name 実装クラス名 
	 * @return　設定情報
	 */
	public NodeList getJavaScriptAPI4ClassConfig(String name);

	
	/**
	 * 引数で指定したJavaScriptAPIの実装スクリプト固有の設定情報を取得します。<br/>
	 * @param name　実装スクリプト名　（「ページパス(拡張子なし)#登録関数名」</b>形式）
	 * @return　設定情報
	 */
	public NodeList getJavaScriptAPI4ScriptConfig(String name);

	
	/**
	 * &lt;IMART&gt;タグの実装クラス固有の設定情報を取得します。<br/>
	 * @param name 実装クラス名
	 * @return　設定情報
	 */
	public NodeList getJSSPTags4ClassConfig(String name);

	
	/**
	 * &lt;IMART&gt;タグの実装スクリプト固有の設定情報を取得します。<br/>
	 * @param name　実装スクリプト名　（「ページパス(拡張子なし)#タグ名」</b>形式）
	 * @return　設定情報
	 */
	public NodeList getJSSPTags4ScriptConfig(String name);
	
	
	/**
	 * JSSP実行環境起動時の初期化クラス名を取得します。<br/>
	 * 本メソッドの返却値で特定されるクラスは、org.intra_mart.jssp.util.ApplicationInitializerを実装しています。
	 * 
	 * @return 初期化クラス名
	 */
	public String[] getInitializer4Class();

	
	/**
	 * JSSP実行環境起動時の初期化スクリプト名を取得します。
	 * 
	 * @return 初期化スクリプト名(拡張子なし)
	 */
	public String[] getInitializer4Script();

	
	/**
	 * org.mozilla.javascript.Contextの生成と開放イベントを監視する実装クラス名を取得します。<br/>
	 * 本メソッドの返却値で特定されるクラスは、org.mozilla.javascript.ContextFactory.Listenerを実装しています。
	 * 
	 * @return Contextの生成と開放イベントを監視する実装クラス名の配列
	 */
	public String[] getContextFactoryListeners();
	
	
	/**
	 * リクエストを受け付けた際に実行されるスクリプト名を取得します。
	 * 
	 * @return リクエストを受け付けた際に実行されるスクリプト名(拡張子なし)
	 */
	public String getRequestProcessScript();

	
	/**
	 * ファンクションコンテナ実行開始関数名を取得します。
	 * 
	 * @return 実行開始関数名
	 */
	public String getInitialFunctionName();

	
	/**
	 * ファンクションコンテナ実行終了関数名を取得します。
	 * 
	 * @return 実行終了関数名
	 */
	public String getFinallyFunctionName();
	
	
	/**
	 * JavaScriptAPI「Debug.browse()」の実効可否状態を取得します。
	 * 
	 * @return true  の場合、Debug.browse() は実行可、<br/>
	 *          false の場合、Debug.browse() は実行不可（記述されていても無視される）
	 */
	public boolean isDebugBrowseEnable();
	
	
	/**
	 * JavaScriptAPI「Debug.print()」の実効可否状態を取得します。
	 * 
	 * @return true  の場合、Debug.print() は実行可、<br/>
	 *          false の場合、Debug.print() は実行不可（記述されていても無視される）
	 */
	public boolean isDebugPrintEnable();
	
	
	/**
	 * JavaScriptAPI「Debug.write()」の実効可否状態を取得します。
	 * 
	 * @return true  の場合、Debug.write() は実行可、<br/>
	 *          false の場合、Debug.write() は実行不可（記述されていても無視される）
	 */
	public boolean isDebugWriteEnable();
	
	
	/**
	 * JavaScriptAPI「Debug.write()」実行時の出力先ファイル名を取得します。
	 * 
	 * @return 「Debug.write()」実行時の出力先ファイル名
	 */
	public String getDebugWriteFilePath();
	
	
	/**
	 * JavaScriptAPI「Debug.console()」の実効可否状態を取得します。
	 * 
	 * @return true  の場合、Debug.console() は実行可、<br/>
	 *          false の場合、Debug.console() は実行不可（記述されていても無視される）
	 */
	public boolean isDebugConsoleEnable();

	
	/**
	 * ページ遷移情報に付加するプレフィックスを返します。
	 * 
	 * @return
	 */
	public String getURIPrefix();

	
	/**
	 * ページ遷移情報に付加するサフィックス（拡張子）を返します。
	 * 
	 * @return
	 */
	public String getURISuffix();

	
	/**
	 * 「URL引数のキー名称：遷移元ページパス用」を返します。
	 * 
	 * @return
	 */
	public String getJsspKey4FromPagePath();
	
		
	/**
	 * 「URL引数のキー名称：遷移先ページパス用」を返します。
	 * 
	 * @return
	 */
	public String getJsspKey4NextEventPagePath();

	
	/**
	 * 「URL引数のキー名称：遷移先イベント名(＝実行する関数名)用」を返します。
	 * 
	 * @return
	 */
	public String getJsspKey4NextEventName();
	
	
	/**
	 * 「URL引数のキー名称：遷移前イベントパス(＝実行する関数が記述されているページパス)用」を返します。
	 * 
	 * @return
	 */
	public String getJsspKey4ActionEventPagePath();

	
	/**
	 * 「URL引数のキー名称：遷移前イベント名(＝実行する関数名)用」を返します。
	 * 
	 * @return
	 */
	public String getJsspKey4ActionEventName();


	/**
	 * 「URL引数のキー名称：URL整合性チェック用」を返します。
	 * 
	 * @return
	 */
	public String getJsspKey4Mark();

	
	/**
	 * ページ遷移をチェックするためのセキュリティコードを保存するキー名を返します<br/>
	 * このキー名を利用して、セッションにセキュリティコードを保存します。
	 * 
	 * @return
	 */
	public String getSignatureKey();

	
	/**
	 * JavaScriptのバージョンを返します。
	 * 
	 * @return JavaScriptのバージョン
	 * @see Context#VERSION_DEFAULT
	 * @see Context#VERSION_1_6
	 * @see Context#VERSION_1_5
	 * @see Context#VERSION_1_4
	 * @see Context#VERSION_1_3
	 * @see Context#VERSION_1_2
	 * @see Context#VERSION_1_1
	 * @see Context#VERSION_1_0
	 * @see Context#VERSION_UNKNOWN
	 */
	int getLanguageVersion();
}
