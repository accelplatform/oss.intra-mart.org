package org.intra_mart.jssp.source.property;

/**
 * ソースファイルに関する設定情報の共通インタフェースです。
 */
public interface SourceProperties{
	/**
	 * ソースの文字エンコーディングを返します。
	 * 文字エンコーディングが不明な場合、null を返します。
	 * @return 文字エンコーディング名
	 */
	public String getCharacterEncoding();

	/**
	 * JavaScript 解析（コンパイル）時の最適化レベルを返します。
	 * @return 最適化レベル（-1 <= level <= 9）
	 * @throws UnsupportedOperationException 判定するための設定情報が見つからない場合
	 */
	public int getJavaScriptOptimizationLevel() throws UnsupportedOperationException;

	/**
	 * JavaScript を Java のクラスにコンパイルする機能が有効かどうかを
	 * 判定します。
	 * @return 有効という設定になっている場合 ture。
	 * @throws UnsupportedOperationException 判定するための設定情報が見つからない場合
	 */
	public boolean enableJavaScriptCompile() throws UnsupportedOperationException;

	/**
	 * PresentationPage のソース(html)をキャッシュする機能が有効かどうかを
	 * 判定します。
	 * @return 有効という設定になっている場合 ture。
	 * @throws UnsupportedOperationException 判定するための設定情報が見つからない場合
	 */
	public boolean enableViewCompile() throws UnsupportedOperationException;
}


