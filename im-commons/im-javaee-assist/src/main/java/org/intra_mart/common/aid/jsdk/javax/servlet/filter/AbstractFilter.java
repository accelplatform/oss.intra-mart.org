package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * フィルタを実装するための抽象クラスです。
 * 
 */
public abstract class AbstractFilter implements Filter{
	/**
	 * フィルター設定情報オブジェクト
	 */
	private FilterConfig filterConfig = null;

	/**
	 * フィルタオブジェクトを作成します。
	 */
	protected AbstractFilter(){
		super();
	}

	/**
	 * フィルタを初期化します。<p>
	 * このメソッドは、この抽象クラスの初期化を実行後に、
	 * {@link #handleInit()} を実行します。
	 * サブクラスが独自の初期化処理を必要とする場合は、
	 * {@link #handleInit()} をオーバーライドして初期化処理を実装してください。
	 * @param config フィルタ設定オブジェクト
	 * @throws ServletException 初期化エラー
	 * @see javax.servlet.Filter#init(FilterConfig)
	 */
	public final void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		this.handleInit();
	}

	/**
	 * フィルタの初期化をします。<p>
	 * このメソッドは、フィルタ初期化時に {@link #init(FilterConfig)} に
	 * 呼び出されます。<br>
	 * このメソッドは何もしません。ただリターンするだけです。<p>
	 * サブクラスが初期化処理を必要とする場合は、
	 * このメソッドをオーバーライドして初期化処理を実装してください。
	 * @throws ServletException 初期化エラー
	 * @see #init(FilterConfig)
	 */
	protected void handleInit()  throws ServletException {
		return;
	}

	/**
	 * このフィルタの設定オブジェクトを返します。
	 * <p>このメソッドは、コンストラクタでは利用できません。
	 * @return フィルタ設定オブジェクト。コンストラクタで呼び出した場合は null を返します。
	 */
	public FilterConfig getFilterConfig(){
		return this.filterConfig;
	}

	/**
	 * この Filter を実行している ServletContext への参照を返します。
	 * <p>このメソッドは、コンストラクタでは利用できません。
	 * @return このフィルタが Servlet コンテナとの対話に使っている ServletContext オブジェクト
	 * @throws NullPointerException コンストラクタで呼び出した場合
	 */
	public ServletContext getServletContext(){
		return this.getFilterConfig().getServletContext();
	}

	/**
	 * Servlet ログファイルに指定されたメッセージを出力します。<br>
	 * 通常は event ログです。
	 * Servlet ログファイルの名前やタイプは Servlet コンテナによって違います。
	 * <p>
	 * このメソッドの呼び出しは、下記と同等です。<br>
	 * <code>this.getFilterConfig().getServletContext().log(msg);</code>
	 * <p>このメソッドは、コンストラクタでは利用できません。
	 * @param msg ログファイルに出力するメッセージ
	 * @throws NullPointerException コンストラクタで呼び出した場合
	 * @see javax.servlet.ServletContext#log(String)
	 */
	protected void log(String msg){
		this.getServletContext().log(msg);
	}

	/**
	 * 与えられた Throwable  例外の説明となるメッセージとスタックトレースを
	 * Servlet のログファイルに出力します。<br>
	 * Servlet ログファイルは通常は event ログですが、
	 * 名前やタイプは Servlet コンテナによって違います。<p>
	 * このメソッドの呼び出しは、下記と同等です。<br>
	 * <code>this.getFilterConfig().getServletContext().log(msg, throwable);</code>
	 * <p>このメソッドは、コンストラクタでは利用できません。
	 * @param msg ログファイルに出力するメッセージ
	 * @param throwable エラーや例外
	 * @throws NullPointerException コンストラクタで呼び出した場合
	 * @see javax.servlet.ServletContext#log(String, Throwable)
	 */
	protected void log(String msg, Throwable throwable){
		this.getServletContext().log(msg, throwable);
	}

	/**
	 * フィルタとして動作するロジック。
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param chain チェーンの次のエンティティ
	 * @throws ServletException 実行時エラー
	 * @throws IOException 入出力エラー
	 * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public abstract void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException;

	/**
	 * フィルタの終了処理。<p>
	 * このメソッドは、{@link #handleDestroy()} を呼び出します。
	 * {@link #handleDestroy()} から正常に制御が返ってきた場合も、
	 * また {@link #handleDestroy()} が例外をスローした場合も、
	 * このメソッドが終了する直前に、この抽象クラスの破棄処理が行われます。<p>
	 * サブクラスが独自の初期化処理を必要とする場合は、
	 * {@link #handleDestroy()} をオーバーライドして
	 * 初期化処理を実装してください。
	 * @see javax.servlet.Filter#destroy()
	 */
	public final void destroy() {
		try{
			this.handleDestroy();
		}
		finally{
			this.filterConfig = null;
		}
	}

	/**
	 * フィルタの破棄処理。<p>
	 * このメソッドは、フィルタ破棄時に {@link #destroy()} に
	 * 呼び出されます。<br>
	 * このメソッドは何もしません。ただリターンするだけです。<p>
	 * サブクラスが破棄処理を必要とする場合は、
	 * このメソッドをオーバーライドして破棄処理を実装してください。
	 * @see #destroy()
	 */
	protected void handleDestroy() {
		return;
	}
}
