package org.intra_mart.common.aid.jsdk.javax.servlet.http.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;



/**
 * リクエスト、レスポンス、および サーブレットコンテキストへのアクセスを提供します。
 */
public class HTTPContextImpl implements HTTPContext{

	private ServletContext servletContext = null;
	private HttpServletRequest httpServletRequest = null;
	private HttpServletResponse httpServletResponse = null;

	/**
	 * 新しいコンテキストを作成します。
	 */
	public HTTPContextImpl(ServletContext context, HttpServletRequest request, HttpServletResponse response){
		this(request, response);

		this.servletContext = context;
	}

	/**
	 * 新しいコンテキストを作成します。
	 */
	public HTTPContextImpl(HttpServletRequest request, HttpServletResponse response){
		super();

		this.httpServletRequest = request;
		this.httpServletResponse = response;
	}

	/**
	 * サーブレットコンテキストを返します。
	 * @return サーブレットコンテキスト
	 */
	public ServletContext getServletContext(){
		return this.servletContext;
	}

	/**
	 * リクエストを返します。
	 * @return リクエスト
	 */
	public HttpServletRequest getRequest(){
		return this.httpServletRequest;
	}

	/**
	 * レスポンスを返します。
	 * @return レスポンス
	 */
	public HttpServletResponse getResponse(){
		return this.httpServletResponse;
	}

	/**
	 * このリクエストに関連づけられている現在のセッションを返します。
	 * リクエストがセッションを持たない場合はセッションを生成します。
	 * @return このリクエストに関連づけられているHttpSession
	 */
	public HttpSession getSession(){
		HttpSession session = this.httpServletRequest.getSession(false);
		if(session != null){
			return session;
		}
		else{
			return this.httpServletRequest.getSession(true);
		}
	}

	/**
	 * このリクエストに関連づけられている現在のセッションを返します。
	 * リクエストが有効なHttpSessionを持たない場合は nullを返します。
	 * @return このリクエストに関連づけられているHttpSession。リクエストが有効なセッションを持っていなければ null。
	 */
	public HttpSession getCurrentSession(){
		return this.getRequest().getSession(false);
	}

	/**
	 * Servlet ログファイルに指定されたメッセージを出力します。<p>
	 * このメソッドは、現在の ServletContext の #log(String) を呼び出します。
	 * @param msg ログファイルに出力するメッセージを指定する String
	 * @see javax.servlet.ServletContext#log(String msg)
	 */
	public void log(String message){
		this.servletContext.log(message);
	}

	/**
	 * 与えられた Throwable  例外の説明となるメッセージとスタックトレースを
	 * Servlet のログファイルに出力します。 <p>
	 * このメソッドは、現在の ServletContext の #log(String, Throwable) を呼び出します。
	 * @param message エラーや例外を解説する String
	 * @param throwable Throwable エラーや例外
	 * @see javax.servlet.ServletContext#log(String message, Throwable throwable)
	 */
	public void log(String message, Throwable throwable){
		this.servletContext.log(message, throwable);
	}

}

