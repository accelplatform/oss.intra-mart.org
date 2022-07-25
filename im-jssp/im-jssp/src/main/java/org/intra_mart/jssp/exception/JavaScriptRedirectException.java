package org.intra_mart.jssp.exception;

/**
 * JavaScript 実行中のリダイレクト要求の例外
 */
public class JavaScriptRedirectException extends JSSPTransitionalException{

	private String location;	// リダイレクト先ＵＲＬ

	/**
	 * @param target_url リダイレクト先ＵＲＬ
	 */
	public JavaScriptRedirectException(String target_url){
		super();
		location = target_url;
	}

	/**
	 * リダイレクト先ＵＲＬを返却します。
	 * @return リダイレクト先ＵＲＬ
	 */
	public String newLocation(){
		return location;
	}
}
