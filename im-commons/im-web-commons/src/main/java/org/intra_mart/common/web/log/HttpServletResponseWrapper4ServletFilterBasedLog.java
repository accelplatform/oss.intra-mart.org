package org.intra_mart.common.web.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * TODO javadoc
 */
public class HttpServletResponseWrapper4ServletFilterBasedLog extends HttpServletResponseWrapper {
	/**
	 * @param response
	 */
	public HttpServletResponseWrapper4ServletFilterBasedLog(HttpServletResponse response) {
		super(response);
	}
	
	
	@Override
	public void addCookie(Cookie cookie) {
		cookies.add(cookie);
		
		super.addCookie(cookie);
	}

	@Override
	public void addDateHeader(String name, long date) {
		this.addHeaderObject(new HttpServletResponseHeader4ServletFilterBasedLog(name, date));
		
		super.addDateHeader(name, date);
	}

	@Override
	public void addHeader(String name, String value) {
		this.addHeaderObject(new HttpServletResponseHeader4ServletFilterBasedLog(name, value));
		
		super.addHeader(name, value);
	}

	@Override
	public void addIntHeader(String name, int value) {
		this.addHeaderObject(new HttpServletResponseHeader4ServletFilterBasedLog(name, value));
		
		super.addIntHeader(name, value);
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		this.statusCode = sc;
		this.statusMessage = msg;
		
		super.sendError(sc, msg);
	}

	@Override
	public void sendError(int sc) throws IOException {
		this.statusCode = sc;
		this.statusMessage = null;

		super.sendError(sc);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		this.redirectLocation = location;
		
		super.sendRedirect(location);
	}

	@Override
	public void setDateHeader(String name, long date) {
		this.setHeaderObject(new HttpServletResponseHeader4ServletFilterBasedLog(name, date));

		super.setDateHeader(name, date);
	}

	@Override
	public void setHeader(String name, String value) {
		this.setHeaderObject(new HttpServletResponseHeader4ServletFilterBasedLog(name, value));
		
		super.setHeader(name, value);
	}

	@Override
	public void setIntHeader(String name, int value) {
		this.setHeaderObject(new HttpServletResponseHeader4ServletFilterBasedLog(name, value));
		
		super.setIntHeader(name, value);
	}

	@Override
	public void setStatus(int sc, String sm) {
		this.statusCode = sc;
		this.statusMessage = sm;
		
		super.setStatus(sc, sm);
	}

	@Override
	public void setStatus(int sc) {
		this.statusCode = sc;
		this.statusMessage = null;
		
		super.setStatus(sc);
	}
	
	//===========================================
	// 以下、Gettableにするための処理
	//===========================================
	
	private List<Cookie> cookies = new ArrayList<Cookie>();
	private List<HttpServletResponseHeader4ServletFilterBasedLog> headers = new ArrayList<HttpServletResponseHeader4ServletFilterBasedLog>();
			
	private int statusCode = HttpServletResponse.SC_OK; 
	private String statusMessage = null;
	
	private String redirectLocation = null;

	private void addHeaderObject(HttpServletResponseHeader4ServletFilterBasedLog h){
		synchronized (headers) {
			this.headers.add(h);
		}
	}

	private void setHeaderObject(HttpServletResponseHeader4ServletFilterBasedLog h){
		synchronized (headers) {
			String targetHeaderName = h.getName();
			
			for(int idx = 0, max = headers.size(); idx < max; idx++){
				String name = headers.get(idx).getName();
				if(targetHeaderName.equals(name)){
					headers.remove(idx);
				}
			}
		}
		
		this.addHeaderObject(h);
	}
	
	
	/**
	 * {@link #addCookie(Cookie)}で設定された Cookie の一覧を返却します。
	 * @return Cookieの一覧
	 * 			(このResponseラッパー経由でCookieが設定されなかった場合は 要素数 0 の{@link List} を返却します)
	 */
	public List<Cookie> getCookies() {
		return this.cookies;
	}

	/**
	 * {@link #addDateHeader(String, long)}、{@link #addHeader(String, Object)}、{@link #addIntHeader(String, int)}、
	 * {@link #setDateHeader(String, long)}、{@link #setHeader(String, String)}、{@link #setIntHeader(String, int)}
	 * で設定されたHTTPレスポンス・ヘッダーの一覧を返却します。
	 * @return HTTPレスポンス・ヘッダーの一覧
	 * 			(このResponseラッパー経由でHTTPレスポンス・ヘッダーが設定されなかった場合は 要素数 0 の{@link List} を返却します)
	 */
	public List<HttpServletResponseHeader4ServletFilterBasedLog> getAllHeaders() {
		return this.headers;
	}

	/**
	 * {@link #addDateHeader(String, long)}、{@link #addHeader(String, Object)}、{@link #addIntHeader(String, int)}、
	 * {@link #setDateHeader(String, long)}、{@link #setHeader(String, String)}、{@link #setIntHeader(String, int)}
	 * で設定されたHTTPレスポンス・ヘッダーで、引数で指定された名前と同じヘッダーを返却します。<br/>
	 * このメソッドは、{@link #getHeaders(String)}で取得される最初の要素を返却します。
	 * 
	 * @param name ヘッダの名前
	 * @return HTTPレスポンス・ヘッダー
	 * 			(このResponseラッパー経由で該当する名称のHTTPレスポンス・ヘッダーが設定されなかった場合は null を返却します)
	 */
	public HttpServletResponseHeader4ServletFilterBasedLog getHeader(String name) {
		List<HttpServletResponseHeader4ServletFilterBasedLog> list = this.getHeaders(name);
		if(list.size() == 0){
			return null;
		}
		else{
			return list.get(0);
		}
	}

	/**
	 * {@link #addDateHeader(String, long)}、{@link #addHeader(String, Object)}、{@link #addIntHeader(String, int)}、
	 * {@link #setDateHeader(String, long)}、{@link #setHeader(String, String)}、{@link #setIntHeader(String, int)}
	 * で設定されたHTTPレスポンス・ヘッダーで、引数で指定された名前と同じヘッダーをすべて返却します。
	 * 
	 * @param name ヘッダの名前
	 * @return HTTPレスポンス・ヘッダー
	 * 			(このResponseラッパー経由で該当する名称のHTTPレスポンス・ヘッダーが設定されなかった場合は 要素数 0 の{@link List} を返却します)
	 */
	public List<HttpServletResponseHeader4ServletFilterBasedLog> getHeaders(String name) {
		List<HttpServletResponseHeader4ServletFilterBasedLog> list4return = new ArrayList<HttpServletResponseHeader4ServletFilterBasedLog>();
		
		for(int idx = 0, max = this.headers.size(); idx < max; idx++){
			HttpServletResponseHeader4ServletFilterBasedLog header = this.headers.get(idx);
			if(name.equals(header.getName())){
				list4return.add(header);
			}
		}
		
		return list4return;
		
	}
	
	/**
	 * {@link #setStatus(int)}、{@link #setStatus(int, String)}、
	 * {@link #sendError(int)}、{@link #sendError(int, String)}で設定されたHTTPレスポンス・ステータスコードを返却します。
	 * @return HTTPレスポンス・ステータスコード<br/>
	 * 			(このResponseラッパー経由でHTTPレスポンス・ステータスコードが設定されなかった場合は {@link HttpServletResponse.SC_OK} を返却します)
	 */
	public int getStatus(){
		return this.statusCode;
	}
	
	/**
	 * {@link #setStatus(int, String)}、
	 * {@link #sendError(int, String)}で設定された状態メッセージを返却します。
	 * @return 状態メッセージ
	 * 			(このResponseラッパー経由で状態メッセージが設定されなかった場合は null を返却します)
	 */
	public String getStatusMessage(){
		return this.statusMessage;
	}

	/**
	 * {@link #sendRedirect(String)}で設定されたリダイレクト先 URLを返却します。
	 * @return リダイレクト先 URL
	 * 			(このResponseラッパー経由でリダイレクト先 URLが設定されなかった場合は null を返却します)
	 */
	public String getRedirectLocation() {
		return this.redirectLocation;
	}

}
