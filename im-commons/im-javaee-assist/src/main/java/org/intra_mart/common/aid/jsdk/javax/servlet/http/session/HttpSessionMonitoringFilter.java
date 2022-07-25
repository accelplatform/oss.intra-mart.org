package org.intra_mart.common.aid.jsdk.javax.servlet.http.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter;


/**
 * {@link HttpSessionManagerFactory#instance()} が返す、
 * {@link HttpSessionManagerFactory} および {@link HttpSessionManager} の
 * 標準実装と連動するフィルタです。
 * このフィルタを設定し動作させる事で、{@link HttpSessionManager} の標準実装が
 * 正しい値を返します。
 */
public class HttpSessionMonitoringFilter extends AbstractFilter{
	/**
	 * 標準提供となるフィルタの実装クラス名
	 */
	private static final String implClassName = "org.intra_mart.common.aid.jsdk.javax.servlet.http.session.impl.HttpSessionMonitoringFilterImpl";

	/**
	 * 実際に処理をするリスナ実装
	 */
	private Filter filter = null;

	/**
	 * 新しい HttpSessionMonitoringFilter を構築します。
	 */
	public HttpSessionMonitoringFilter(){
		super();

		try{
			try{
				ClassLoader loader = this.getClass().getClassLoader();
				Class clazz = loader.loadClass(implClassName);
				this.filter = (Filter) clazz.newInstance();
			}
			catch(ClassNotFoundException cnfe){
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = loader.loadClass(implClassName);
				this.filter = (Filter) clazz.newInstance();
			}
		}
		catch(Throwable t){
			IllegalStateException ise = new IllegalStateException("No implementation error.");
			ise.initCause(t);
			throw ise;
		}
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
		FilterConfig filterConfig = this.getFilterConfig();
		this.filter.init(filterConfig);
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException{
		this.filter.doFilter(request, response, chain);
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
		this.filter.destroy();
	}
}

