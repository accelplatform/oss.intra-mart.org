package org.intra_mart.common.aid.jsdk.javax.servlet.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * リクエスト、レスポンス、および サーブレットコンテキストへのアクセスを提供します。
 */
public interface HTTPContext{
	/**
	 * サーブレットコンテキストを返します。
	 * @return サーブレットコンテキスト
	 */
	public ServletContext getServletContext();

	/**
	 * リクエストを返します。
	 * @return リクエスト
	 */
	public HttpServletRequest getRequest();

	/**
	 * レスポンスを返します。
	 * @return レスポンス
	 */
	public HttpServletResponse getResponse();

	/**
	 * このリクエストに関連づけられている現在のセッションを返します。
	 * リクエストがセッションを持たない場合はセッションを生成します。
	 * @return このリクエストに関連づけられているHttpSession
	 */
	public HttpSession getSession();

	/**
	 * このリクエストに関連づけられている現在のセッションを返します。
	 * リクエストが有効なHttpSessionを持たない場合は nullを返します。
	 * @return このリクエストに関連づけられているHttpSession。リクエストが有効なセッションを持っていなければ null。
	 */
	public HttpSession getCurrentSession();

	/**
	 * Servlet ログファイルに指定されたメッセージを出力します。<p>
	 * このメソッドは、現在の ServletContext の #log(String) を呼び出します。
	 * @param message ログファイルに出力するメッセージを指定する String
	 * @see javax.servlet.ServletContext#log(String msg)
	 */
	public void log(String message);

	/**
	 * 与えられた Throwable  例外の説明となるメッセージとスタックトレースを
	 * Servlet のログファイルに出力します。 <p>
	 * このメソッドは、現在の ServletContext の #log(String, Throwable) を呼び出します。
	 * @param message エラーや例外を解説する String
	 * @param throwable Throwable エラーや例外
	 * @see javax.servlet.ServletContext#log(String message, Throwable throwable)
	 */
	public void log(String message, Throwable throwable);

}

