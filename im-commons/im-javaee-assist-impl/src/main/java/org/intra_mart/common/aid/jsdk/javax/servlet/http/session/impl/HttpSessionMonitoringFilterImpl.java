package org.intra_mart.common.aid.jsdk.javax.servlet.http.session.impl;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.HttpSessionManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.HttpSessionManagerFactory;


/**
 * {@link HttpSessionManagerFactory#instance()} が返す、
 * {@link HttpSessionManagerFactory} および {@link HttpSessionManager} の
 * 標準実装と連動するフィルタです。
 * このフィルタを設定し動作させる事で、{@link HttpSessionManager} の標準実装が
 * 正しい値を返します。
 */
public class HttpSessionMonitoringFilterImpl extends AbstractFilter{
	/**
	 * 新しい HttpSessionMonitoringFilterImpl を構築します。
	 */
	public HttpSessionMonitoringFilterImpl(){
		super();
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
	 * フィルタとして動作するロジック。
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param chain チェーンの次のエンティティ
	 * @throws ServletException 実行時エラー
	 * @throws IOException 入出力エラー
	 * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException{
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession httpSession = httpServletRequest.getSession(false);
		if(httpSession != null){
			HttpSessionManagerImpl httpSessionManagerImpl = HttpSessionManagerImpl.getInstance();
			httpSessionManagerImpl.handleRequestAccepted(httpSession);
		}

		chain.doFilter(new HttpServletRequestWrapperImpl(httpServletRequest), response);
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

	/**
	 * セッションを監視するために HttpSession をラップする
	 * サーブレットリクエスト。
	 */
	private static class HttpServletRequestWrapperImpl extends HttpServletRequestWrapper{
		/**
		 * ラップするリクエスト
		 */
		HttpServletRequest httpServletRequest = null;

		/**
		 * 指定のリクエストをラップした新しいリクエストを構築します。
		 * @param request
		 */
		protected HttpServletRequestWrapperImpl(HttpServletRequest request){
			super(request);
			this.httpServletRequest = request;
		}

		/**
		 * セッションをラップして返します。
		 * @return このリクエストに関連づけられているHttpSession
		 */
		public HttpSession getSession(){
			HttpSession session = httpServletRequest.getSession();
			if(session != null){
				return new HttpSessionWrapper(session);
			}
			else{
				return null;
			}
		}

		/**
		 * セッションをラップして返します。
		 * @param create セッションを強制的に作る場合は true。
		 * @return このリクエストに関連づけられているHttpSession。 createの値がfalse  である場合、リクエストが有効なセッションを持っていなければ null
		 */
		public HttpSession getSession(boolean create){
			HttpSession session = httpServletRequest.getSession(create);
			if(session != null){
				return new HttpSessionWrapper(session);
			}
			else{
				return null;
			}
		}

		/**
		 * セッションをラップします。
		 */
		private static class HttpSessionWrapper implements HttpSession{
			/**
			 * ラップする元のセッション
			 */
			private HttpSession session = null;
	
			/**
			 * HttpSessionをラップした新しいセッションを構築します。
			 *
			 */
			protected HttpSessionWrapper(HttpSession session){
				super();
				this.session = session;
			}
	
			/**
			 * 作成された時刻を返します。
			 * @see javax.servlet.http.HttpSession#getCreationTime()
			 */
			public long getCreationTime() {
				return this.session.getCreationTime();
			}
	
			/**
			 * このセッションのセッションＩＤを返します。
			 * @see javax.servlet.http.HttpSession#getId()
			 */
			public String getId() {
				return this.session.getId();
			}
	
			/**
			 * 最後にアクセスした時刻を返します。
			 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
			 */
			public long getLastAccessedTime() {
				return this.session.getLastAccessedTime();
			}
	
			/**
			 * サーブレットコンテキストを返します。
			 * @see javax.servlet.http.HttpSession#getServletContext()
			 */
			public ServletContext getServletContext() {
				return this.session.getServletContext();
			}
	
			/**
			 * セッションを維持する時間を設定します。
			 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
			 */
			public void setMaxInactiveInterval(int interval) {
				this.session.setMaxInactiveInterval(interval);
				HttpSessionManagerImpl httpSessionManagerImpl = HttpSessionManagerImpl.getInstance();
				httpSessionManagerImpl.handleRequestAccepted(this.session);
			}
	
			/**
			 * セッション維持時間を返します。
			 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
			 */
			public int getMaxInactiveInterval() {
				return this.session.getMaxInactiveInterval();
			}
	
			/**
			 * セッションコンテキストを返します。
			 * @see javax.servlet.http.HttpSession#getSessionContext()
			 * @deprecated
			 */
			public HttpSessionContext getSessionContext() {
				return this.session.getSessionContext();
			}
	
			/**
			 * 指定のキーにマッピングされている属性を返します。
			 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
			 */
			public Object getAttribute(String key) {
				return this.session.getAttribute(key);
			}
	
			/**
			 * 指定のキーにマッピングされている値を返します。
			 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
			 * @deprecated
			 */
			public Object getValue(String key) {
				return this.session.getValue(key);
			}
	
			/**
			 * このセッションに結びつけられている全てのオブジェクトの名前を表す StringオブジェクトのEnumeration  オブジェクトを返します。
			 * @see javax.servlet.http.HttpSession#getAttributeNames()
			 */
			public Enumeration getAttributeNames() {
				return this.session.getAttributeNames();
			}
	
			/**
			 * キーの配列を返します。
			 * @see javax.servlet.http.HttpSession#getValueNames()
			 * @deprecated
			 */
			public String[] getValueNames() {
				return this.session.getValueNames();
			}
	
			/**
			 * 属性をセットします。
			 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
			 */
			public void setAttribute(String name, Object value) {
				this.session.setAttribute(name, value);
			}
	
			/**
			 * 値をセットします。
			 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
			 * @deprecated
			 */
			public void putValue(String name, Object value) {
				this.session.putValue(name, value);
			}
	
			/**
			 * 属性を削除します。
			 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
			 */
			public void removeAttribute(String name) {
				this.session.removeAttribute(name);
			}
	
			/**
			 * 値を削除します。
			 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
			 * @deprecated
			 */
			public void removeValue(String name) {
				this.session.removeValue(name);
			}
	
			/**
			 * セッションを無効化します。
			 * @see javax.servlet.http.HttpSession#invalidate()
			 */
			public void invalidate() {
				this.session.invalidate();
			}
	
			/**
			 * このセッションをクライアントが知らない場合 true を返します。
			 * @see javax.servlet.http.HttpSession#isNew()
			 */
			public boolean isNew() {
				return this.session.isNew();
			}
		}
	}
}

