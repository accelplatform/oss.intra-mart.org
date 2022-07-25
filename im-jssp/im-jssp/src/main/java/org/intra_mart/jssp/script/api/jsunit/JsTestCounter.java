package org.intra_mart.jssp.script.api.jsunit;

/**
 * JsUnitテストカウンターインタフェース。
 */
public interface JsTestCounter {

	/**
	 * エラーの総数を取得します。
	 * 
	 * @return エラーの総数
	 */
	public int getErrorCount();

	/**
	 * 評価に失敗した総数を取得します。
	 * 
	 * @return 評価に失敗した総数
	 */
	public int getFailureCount();

	/**
	 * 評価の総数を取得します。<br>
	 * エラーの数も含まれます。
	 * 
	 * @return 評価の総数
	 */
	public int getTotal();

	/**
	 * 総実行時間を取得します。<br>
	 * 
	 * @return 総実行時間
	 */
	public long getTime();
}
