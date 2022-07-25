package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.exception.EmptyQueryException;
import org.intra_mart.common.aid.jsdk.javax.servlet.exception.ExtendedServletException;
import org.intra_mart.common.platform.log.Logger;



/**
 * リクエストをラップするフィルタです。
 * このフィルタを利用する事により、リクエストの情報を、
 * いつでも取得できるようになります。<p>
 * このフィルタを利用する場合は、実装を指定する必要があります。
 * フィルタの初期化パラメータ名 config に、
 * MIME タイプと
 * インタフェース {@link HttpServletRequestMessageBodyWrapperBuilder} の
 * 実装クラス名を指定したプロパティズファイルのパスを設定してください。
 * 
 */
public class RequestMessageBodyFilter extends AbstractFilter {
	private static Logger _logger = Logger.getLogger();
	private Logger.Level logLevel = Logger.Level.INFO;
	
	/**
	 * スレッドをキーとしてリクエストされたクエリを保存します。
	 */
	private static Map requestMap = new WeakHashMap();

	/**
	 * 実行済みかどうかを判定するための情報マップ
	 */
	private Map acceptedMap = new WeakHashMap();
	
	/**
	 * 実行済み時に設定するシンボル
	 */
	private Object acceptedSymbol = new Object();

	/**
	 * 現在のリクエストのメッセージボディを返します。<p>
	 * リクエストまたはメッセージボディが不明の場合は、null を返します。
	 * @return メッセージボディを含む入力ストリーム
	 * @throws IOException 入出力の例外が発生した場合
	 */
	public static InputStream getInputStream() throws IOException{
		HttpServletRequestMessageBodyWrapper request = RequestMessageBodyFilter.getCurrentRequest();
		if(request != null){
			return request.getMessageBody();
		}
		else{
			return null;
		}
	}

	/**
	 * 現在のリクエストのパラメータを返します。<p>
	 * リクエストが不明な場合は null を返します。
	 * @return パラメータ情報オブジェクトの配列
	 * @throws IOException 入出力の例外が発生した場合
	 */
	public static RequestParameter[] getRequestParameters() throws IOException{
		HttpServletRequestMessageBodyWrapper request = RequestMessageBodyFilter.getCurrentRequest();
		if(request != null){
			return request.getRequestParameters();
		}
		else{
			return null;
		}
	}

	/**
	 * 現在処理中のリクエストを返します。<p>
	 * リクエストが不明、
	 * または{@link HttpServletRequestMessageBodyWrapper}に
	 * ラップされていない場合は、null を返します。
	 * @return リクエスト
	 */
	public static HttpServletRequestMessageBodyWrapper getCurrentRequest(){
		synchronized(requestMap){
			return (HttpServletRequestMessageBodyWrapper) requestMap.get(Thread.currentThread());
		}
	}

	/**
	 * ビルダのマップ
	 */
	private Map requestBuilders = new HashMap();

	/**
	 * フィルタを作成します。
	 */
	public RequestMessageBodyFilter(){
		super();
	}

	/**
	 * フィルタの初期化をします。<p>
	 * このメソッドは、フィルタ初期化時に
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter#init(FilterConfig)} に
	 * 呼び出されます。<br>
	 * @throws ServletException 初期化エラー
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter#init(FilterConfig)
	 */
	protected void handleInit()  throws ServletException {
		FilterConfig config = this.getFilterConfig();		// フィルタ設定

		// 設定ファイルの読み込みと初期化
		String resourceName = config.getInitParameter("config");
		if(resourceName != null){
			ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceName);
			Enumeration enumeration = resourceBundle.getKeys();
			while(enumeration.hasMoreElements()){
				String mimeType = (String) enumeration.nextElement();
				String className = resourceBundle.getString(mimeType);
				try{
					Class clazz = this.getBuilderClass(className);
					HttpServletRequestMessageBodyWrapperBuilder builder = (HttpServletRequestMessageBodyWrapperBuilder) clazz.newInstance();
					builder.init(config);
					requestBuilders.put(mimeType, builder);
					
					_logger.log(logLevel, "HttpServletRequestMessageBodyWrapperBuilder entry: " + mimeType + " -> " + className);
				}
				catch(IllegalAccessException iae){
					throw new ExtendedServletException("HttpServletRequestMessageBodyWrapperBuilder creation error: " + mimeType + " -> " + className , iae);
				}
				catch(InstantiationException ie){
					throw new ExtendedServletException("HttpServletRequestMessageBodyWrapperBuilder creation error: " + mimeType + " -> " + className , ie);
				}
				catch(ClassNotFoundException cnfe){
					throw new ExtendedServletException("HttpServletRequestMessageBodyWrapperBuilder not found: " + mimeType + " -> " + className , cnfe);
				}
			}
		}
	}

	/**
	 * クラスを返します。<p>
	 * このメソッドは、以下の手順でクラスを探します。<br>
	 * <ul>
	 * <li>このクラスをロードしたクラスローダから検索
	 * <li>現在のスレッドのコンテキストクラスローダから検索
	 * </ul>
	 * @param name クラス名
	 * @return クラス
	 * @throws ClassNotFoundException クラスがみつからない
	 */
	private Class getBuilderClass(String name) throws ClassNotFoundException{
		try{
			return this.getClass().getClassLoader().loadClass(name);
		}
		catch(Throwable t){
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		}
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

		Object isSetAcceptedSymbol = null;
		
		// フラグチェック
		synchronized (this.acceptedMap) {
			isSetAcceptedSymbol = this.acceptedMap.put(Thread.currentThread(), this.acceptedSymbol);
		}

		if(isSetAcceptedSymbol == null){
			try{
				if(request instanceof HttpServletRequest){
					if(response instanceof HttpServletResponse){
						HttpServletRequest httpRequest = (HttpServletRequest) request;
						HttpServletResponse httpResponse = (HttpServletResponse) response;
						// Content-Type のチェック
						String contentType = request.getContentType();
						if(contentType != null){
							int semiColon = contentType.indexOf(';');
							if(semiColon != -1){
								contentType = contentType.substring(0, semiColon).trim();
							}
							else{
								contentType = contentType.trim();
							}
		
							HttpServletRequestMessageBodyWrapperBuilder builder = (HttpServletRequestMessageBodyWrapperBuilder) this.requestBuilders.get(contentType);
							if(builder != null){
								try{
									// リクエストをラップする
									HttpServletRequestMessageBodyWrapper eRequest = builder.buildExtendedHttpServletRequest(httpRequest, httpResponse);
									Thread thread = Thread.currentThread();	// キー
									synchronized(requestMap){
										requestMap.put(thread, eRequest);	// キャッシュ
									}
									try{
										chain.doFilter(eRequest, response);
									}
									catch(EmptyQueryException eqe){
										throw new ExtendedServletException("Servlet runtime error: " + eqe.getMessage(), eqe);
									}
									finally{
										synchronized(requestMap){
											requestMap.remove(thread);		// 破棄
										}
									}
								}
								catch(EmptyQueryException eqe){
									// クエリがなかった
									if(Boolean.valueOf(this.getFilterConfig().getInitParameter("EmptyQueryException")).booleanValue()){
										throw eqe;
									}
									else{
										// ラップに失敗したのでスッピンで動作
										chain.doFilter(request, response);
									}
								}
							}
							else{
								// ラップの必要なし
								chain.doFilter(request, response);
							}
						}
						else{
							// Content-Type が不明＝ラップの対象外
							chain.doFilter(request, response);
						}
					}
					else{
						// レスポンスがHttpServletResponseと代入互換ではない＝ラップの対象外
						chain.doFilter(request, response);
					}
				}
				else{
					// リクエストがHttpServletRequestと代入互換ではない＝ラップの対象外
					chain.doFilter(request, response);
				}
			}
			finally{
				// フラグ開放
				synchronized (this.acceptedMap) {
					this.acceptedMap.remove(Thread.currentThread());
				}
			}
		}
		else{
			// 既に処理済→スルー
			chain.doFilter(request, response);					// 実行
		}
		
	}

	/**
	 * フィルタの破棄処理。<p>
	 * このメソッドは、フィルタ破棄時に
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter#destroy()} に
	 * 呼び出されます。<br>
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter#destroy()
	 */
	protected void handleDestroy(){
		Iterator iterator = this.requestBuilders.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			HttpServletRequestMessageBodyWrapperBuilder builder = (HttpServletRequestMessageBodyWrapperBuilder) entry.getValue();
			builder.destroy();
		}
	}

	/**
	 * このクラスのログレベルを取得します。
	 * @return ログレベル
	 */
	public Logger.Level getLogLevel() {
		return logLevel;
	}

	/**
	 * このクラスのログレベルを設定します。
	 * @param logLevel ログレベル
	 */
	public void setLogLevel(Logger.Level logLevel) {
		this.logLevel = logLevel;
	}
}

