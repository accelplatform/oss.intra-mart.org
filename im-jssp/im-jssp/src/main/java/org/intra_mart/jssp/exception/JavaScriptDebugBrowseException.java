package org.intra_mart.jssp.exception;

/**
 * JavaScript 実行中のデバッグ表示の例外
 */
public class JavaScriptDebugBrowseException extends JSSPTransitionalException{

	private String pageSource;		// 表示ＨＴＭＬソース

	/**
	 * @param src 表示ＨＴＭＬソース
	 */
	public JavaScriptDebugBrowseException(String src){
		super();
		pageSource = src;
	}

	/**
	 * クライアントへの表示ＨＴＭＬソースの返却メソッド
	 * @return クライアントへの表示ＨＴＭＬソース
	 */
	public String getSource(){
		return pageSource;
	}
}
