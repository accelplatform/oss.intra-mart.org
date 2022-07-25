package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.SearchableHttpServletResponseWrapper;


/**
 * サーブレットレスポンスの各メソッド呼び出しに対するリスナを設定するフィルタ。
 * <p>
 * このフィルタは、以下の設定値を持ちます。<br>
 * <table>
 * <tr>
 * <th>component.builder.class</th>
 * <td>レスポンスリスナやバリデータのビルダ実装のクラス名（FQN）</td>
 * </tr>
 * <tr>
 * <th>component.builder.parameter</th>
 * <td>ビルダに対する初期化パラメータ</td>
 * </tr>
 * <tr>
 * <th>controller.builder.class</th>
 * <td>コントローラビルダ実装のクラス名（FQN）</td>
 * </tr>
 * <tr>
 * <th>controller.builder.parameter</th>
 * <td>コントローラビルダに対する初期化パラメータ</td>
 * </tr>
 * </table>
 * <p>
 * 設定値 property.builder.class には、
 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventComponentBuilder}
 * の実装クラス名を指定してください。
 * リスナクラスには、デフォルトコンストラクタが必要です。
 * <br>
 * property.builder.parameter に指定したパラメータ値は、
 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventComponentBuilder#init(String, ServletContext)}
 * への引数に渡されます。
 * <p>
 * 設定値 controller.builder.class には、
 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventControllerBuilder}
 * の実装クラス名を指定してください。
 * リスナクラスには、デフォルトコンストラクタが必要です。
 * <br>
 * controller.builder.parameter に指定したパラメータ値は、
 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventControllerBuilder#init(String, ServletContext)}
 * への引数に渡されます。
 *
 */
public class HttpServletResponseEventFilter extends AbstractFilter {
	/**
	 * 設定ファイルのパスを指定するための初期化パラメータ名
	 */
	private static final String PARAM_ID_COMPONENT_BUILDER = "component.builder.class";
	private static final String PARAM_ID_COMPONENT_CONFIG = "component.builder.parameter";
	private static final String PARAM_ID_CONTROLLER_BUILDER = "controller.builder.class";
	private static final String PARAM_ID_CONTROLLER_CONFIG = "controller.builder.parameter";
	/**
	 * コントローラビルダ
	 */
	private HttpServletResponseEventControllerBuilder httpServletResponseEventControllerBuilder = null;
	/**
	 * リスナ本体
	 */
	private HttpServletResponseEventListener[] httpServletResponseEventListeners = null;
	/**
	 * バリデータ
	 */
	private HttpServletResponseEventValidator[] httpServletResponseEventValidators = null;

	/**
	 * 新しいフィルタを作成します。
	 */
	public HttpServletResponseEventFilter() {
		super();
	}

	/**
	 * フィルタの初期化をします。<p>
	 * @throws ServletException 初期化エラー
	 */
	protected void handleInit()  throws ServletException {
		this.initController();
		this.initProperty();
	}

	private void initController() throws HttpServletResponseEventFilterException{
		FilterConfig config = this.getFilterConfig();
		String className = config.getInitParameter(PARAM_ID_CONTROLLER_BUILDER);
		if(className != null){
			ClassLoader classLoader = this.getClass().getClassLoader();
			try{
				Class clazz = classLoader.loadClass(className);
				this.httpServletResponseEventControllerBuilder = (HttpServletResponseEventControllerBuilder) clazz.newInstance();

				String parameterValue = config.getInitParameter(PARAM_ID_CONTROLLER_CONFIG);
				ServletContext servletContext = this.getServletContext();
				this.httpServletResponseEventControllerBuilder.init(parameterValue, servletContext);
			}
			catch(ClassNotFoundException cnfe){
				throw new HttpServletResponseEventFilterException("Class load error: " + className, cnfe);
			}
			catch(IllegalAccessException iae){
				throw new HttpServletResponseEventFilterException("Instance create error: " + className, iae);
			}
			catch(InstantiationException ie){
				throw new HttpServletResponseEventFilterException("Instance create error: " + className, ie);
			}
			catch(HttpServletResponseEventException e){
				throw new HttpServletResponseEventFilterException("httpServletResponseEventControllerBuilder initialize error: " + className, e);
			}
		}
		else{
			throw new HttpServletResponseEventFilterException("Filter initial parameter \"" + PARAM_ID_CONTROLLER_BUILDER + "\" is not defined.");
		}
	}

	private void initProperty() throws HttpServletResponseEventFilterException{
		FilterConfig config = this.getFilterConfig();
		String className = config.getInitParameter(PARAM_ID_COMPONENT_BUILDER);
		if(className != null){
			ClassLoader classLoader = this.getClass().getClassLoader();
			try{
				Class clazz = classLoader.loadClass(className);
				HttpServletResponseEventComponentBuilder builder = (HttpServletResponseEventComponentBuilder) clazz.newInstance();

				String parameterValue = config.getInitParameter(PARAM_ID_COMPONENT_CONFIG);
				ServletContext servletContext = this.getServletContext();
				builder.init(parameterValue, servletContext);

				this.httpServletResponseEventListeners = builder.getListeners();
				if(this.httpServletResponseEventListeners.length == 0){
					throw new HttpServletResponseEventFilterException("HttpServletResponseEventListener is not defined in " + parameterValue);
				}

				this.httpServletResponseEventValidators = builder.getValidators();
			}
			catch(ClassNotFoundException cnfe){
				throw new HttpServletResponseEventFilterException("Class load error: " + className, cnfe);
			}
			catch(IllegalAccessException iae){
				throw new HttpServletResponseEventFilterException("Instance create error: " + className, iae);
			}
			catch(InstantiationException ie){
				throw new HttpServletResponseEventFilterException("Instance create error: " + className, ie);
			}
			catch(HttpServletResponseEventException e){
				throw new HttpServletResponseEventFilterException("HttpServletResponseEventPropertyBuilder initialize error: " + className, e);
			}
		}
		else{
			throw new HttpServletResponseEventFilterException("Filter initial parameter \"" + PARAM_ID_COMPONENT_BUILDER + "\" is not defined.");
		}
	}

	/**
	 * フィルタとして動作するロジック。
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param chain チェーンの次のエンティティ
	 * @throws ServletException 実行時エラー
	 * @throws IOException 入出力エラー
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

		if(response instanceof ExtendedHttpServletResponse){
			this.doFilter(request, (ExtendedHttpServletResponse) response, chain);
		}
		else{
			this.doFilter(request, new SearchableHttpServletResponseWrapper((HttpServletResponse) response), chain);
		}

	}

	/**
	 * フィルタとして動作するロジック。
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param chain チェーンの次のエンティティ
	 * @throws ServletException 実行時エラー
	 * @throws IOException 入出力エラー
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	private void doFilter(ServletRequest request, ExtendedHttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		HttpServletResponseEventDispatcher eventDispatcher = new HttpServletResponseEventDispatcher((HttpServletRequest) request, response, this.httpServletResponseEventControllerBuilder);

		// リスナの設定
		for(int listenerIndex = 0; listenerIndex < this.httpServletResponseEventListeners.length; listenerIndex++){
			eventDispatcher.addHttpServletResponseEventListener(this.httpServletResponseEventListeners[listenerIndex]);
		}
		// バリデータの設定
		for(int validatorIndex = 0; validatorIndex < this.httpServletResponseEventValidators.length; validatorIndex++){
			eventDispatcher.addHttpServletResponseEventValidator(this.httpServletResponseEventValidators[validatorIndex]);
		}

		// 次のチェーンを実行
		chain.doFilter(request, eventDispatcher);
	}
}



/**
 * レスポンスのラッパークラス。<p>
 * {@link javax.servlet.http.HttpServletResponse} が持つ
 * 各メソッド（非推奨を除く）をオーバーライドして、
 * リスナに処理を委任するロジックを追加します。
 */
class HttpServletResponseEventDispatcher extends HttpServletResponseWrapper{
	/**
	 * コントローラビルダ
	 */
	private HttpServletResponseEventControllerBuilder controllerBuilder = null;
	/**
	 * デフォルトのレスポンス
	 */
	private ExtendedHttpServletResponse extendedHttpServletResponse = null;
	/**
	 * 各メソッド呼び出しに対するイベントリスナを制御するコントローラ
	 */
	private ExtendedHttpServletResponse httpServletResponseEventController = null;
	/**
	 * バリデータ
	 */
	private HttpServletResponseEventValidator[] validators = new HttpServletResponseEventValidator[0];
	/**
	 * リクエスト
	 */
	private HttpServletRequest httpServletRequest = null;

	/**
	 * 新しいインスタンスを生成します。
	 * @param request リクエスト
	 * @param reqponse ラップするレスポンス
	 * @param controllerBuilder コントローラのビルダ
	 */
	public HttpServletResponseEventDispatcher(HttpServletRequest request, ExtendedHttpServletResponse response, HttpServletResponseEventControllerBuilder controllerBuilder){
		super(response);
		this.httpServletRequest = request;
		this.extendedHttpServletResponse = response;
		this.httpServletResponseEventController = this.extendedHttpServletResponse;
		this.controllerBuilder = controllerBuilder;
	}

	/**
	 * リクエストを返します。
	 * @return 現在のリクエスト
	 */
	private HttpServletRequest getHttpServletRequest(){
		return this.httpServletRequest;
	}

	/**
	 * バリデータを追加します。
	 * @param validator バリデータ
	 */
	public void addHttpServletResponseEventValidator(HttpServletResponseEventValidator validator){
		Collection collection = new ArrayList();
		collection.add(validator);

		for(int idx = 0; idx < this.validators.length; idx++){
			collection.add(this.validators[idx]);
		}

		this.validators = (HttpServletResponseEventValidator[]) collection.toArray(new HttpServletResponseEventValidator[collection.size()]);
	}

	/**
	 * リスナを追加します。
	 * @param listener リスナ
	 * @throws HttpServletResponseEventControllerException
	 */
	public void addHttpServletResponseEventListener(HttpServletResponseEventListener listener) throws HttpServletResponseEventControllerException{
		this.httpServletResponseEventController = this.controllerBuilder.createController(this.getHttpServletRequest(), this.httpServletResponseEventController, listener);
	}

	/**
	 * レスポンスを返します。
	 * @return レスポンス
	 */
	private ExtendedHttpServletResponse getExtendedHttpServletResponse(){
		try{
			for(int idx = 0; idx < this.validators.length; idx++){
				if(! this.validators[idx].isValid(this.getHttpServletRequest(), this.httpServletResponseEventController)){
					// イベントリスナの動作はなし
					return this.extendedHttpServletResponse;
				}
			}

			// イベントリスナ動作用のコントローラを返却
			return this.httpServletResponseEventController;
		}
		catch(HttpServletResponseEventValidatorException exception){
			throw new RuntimeException("HttpServletResponseEventValidator runtime error.", exception);
		}
	}

	// ----- javax.servlet.http.HttpServletResponse interface
	/**
	 * 指定されたCookieをレスポンスに追加します。
	 * @param cookie クライアントに返すCookie
	 */
	public void addCookie(Cookie cookie){
		this.getExtendedHttpServletResponse().addCookie(cookie);
	}

	/**
	 * 指定された名前を持つヘッダが既にセットされているかどうかを表す booleanを返します。
	 * @param name ヘッダ名
	 * @return 指定された名前のレスポンスヘッダが既にセットされていれば true、そうでなければfalse
	 */
	public boolean containsHeader(String name){
		return this.getExtendedHttpServletResponse().containsHeader(name);
	}

	/**
	 * 指定されたURLがセッションIDを含むようにエンコードします。
	 * @param url エンコードするURL
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public String encodeURL(String url){
		return this.getExtendedHttpServletResponse().encodeURL(url);
	}

	/**
	 * {@link #sendRedirect(String)} メソッドの中で使えるように、 指定されたURLをエンコードします。
	 * @param url エンコードするURL
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public String encodeRedirectURL(String url){
		return this.getExtendedHttpServletResponse().encodeRedirectURL(url);
	}

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * @param sc エラーステータスコード
	 * @param msg 説明文
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendError(int sc, String msg) throws IOException{
		this.getExtendedHttpServletResponse().sendError(sc, msg);
	}

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * @param sc エラーステータスコード
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendError(int sc) throws IOException{
		this.getExtendedHttpServletResponse().sendError(sc);
	}

	/**
	 * 指定されたリダイレクト先のURLを用いて、 クライアントに一時的なリダイレクトレスポンスを送信します。
	 * @param location リダイレクト先のURL
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendRedirect(String location) throws IOException{
		this.getExtendedHttpServletResponse().sendRedirect(location);
	}

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを設定します。
	 * @param name 設定するヘッダの名称
	 * @param date 設定するヘッダの値
	 */
	public void setDateHeader(String name, long date){
		this.getExtendedHttpServletResponse().setDateHeader(name, date);
	}

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを追加します。
	 * @param name 追加するヘッダの名称
	 * @param date 追加するヘッダの値
	 */
	public void addDateHeader(String name, long date){
		this.getExtendedHttpServletResponse().addDateHeader(name, date);
	}

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを設定します。
	 * @param name ヘッダの名称
	 * @param value ヘッダの値
	 */
	public void setHeader(String name, String value){
		this.getExtendedHttpServletResponse().setHeader(name, value);
	}

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを追加します。
	 * @param name ヘッダの名称
	 * @param value 追加するヘッダの値
	 */
	public void addHeader(String name, String value){
		this.getExtendedHttpServletResponse().addHeader(name, value);
	}

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを設定します。
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 */
	public void setIntHeader(String name, int value){
		this.getExtendedHttpServletResponse().setIntHeader(name, value);
	}

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを追加します。
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 */
	public void addIntHeader(String name, int value){
		this.getExtendedHttpServletResponse().addIntHeader(name, value);
	}

	/**
	 * このレスポンスのステータスコードを設定します。
	 * @param sc ステータスコード
	 */
	public void setStatus(int sc){
		this.getExtendedHttpServletResponse().setStatus(sc);
	}

	// ----- javax.servlet.ServletResponse interface
	/**
	 * このレスポンスで送り返す MIME ボディに適用されている文字エンコーディング名を返します。
	 * @return 文字エンコーディング名を意味する String。
	 */
	public String getCharacterEncoding(){
		return this.getExtendedHttpServletResponse().getCharacterEncoding();
	}

	/**
	 * レスポンスにバイナリデータを出力する際に使用する
	 * {@link javax.servlet.ServletOutputStream}型のオブジェクトを返します。
	 * @return バイナリデータ出力に使用する {@link javax.servlet.ServletOutputStream}
	 * @throws IllegalStateException このレスポンスですでに {@link #getWriter()}  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public ServletOutputStream getOutputStream() throws IOException{
		return this.getExtendedHttpServletResponse().getOutputStream();
	}

	/**
	 * 文字データをクライアントに送り返すのに使用する
	 * {@link java.io.PrintWriter}オブジェクトを返します。
	 * @return クライアントに文字データを送り返すことができる {@link java.io.PrintWriter} オブジェクト
	 * @throws UnsupportedEncodingException setContentType  メソッドで指定された文字エンコーディングがサポートされていない場合
	 * @throws IllegalStateException このレスポンスですでに {@link #getOutputStream()}  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public PrintWriter getWriter() throws IOException{
		return this.getExtendedHttpServletResponse().getWriter();
	}

	/**
	 * レスポンスのメッセージボディ部分の長さをセットします。
	 * @param len クライアントに送り返すメッセージボディの長さを指定する整数値。
	 */
	public void setContentLength(int len){
		this.getExtendedHttpServletResponse().setContentLength(len);
	}

	/**
	 * クライアントに送り返されるレスポンスのコンテントタイプをセットします。
	 * @param type コンテントタイプを指定する String
	 */
	public void setContentType(String type){
		this.getExtendedHttpServletResponse().setContentType(type);
	}

	/**
	 * 適当なバッファサイズをレスポンスに含まれるメッセージボディのサイズに設定します。
	 * @param size 適当なバッファサイズを指定する整数値
	 * @throws IllegalStateException すでにコンテンツが出力された後でこのメソッドが実行された場合
	 */
	public void setBufferSize(int size){
		this.getExtendedHttpServletResponse().setBufferSize(size);
	}

	/**
	 * このレスポンスに設定されている実バッファサイズを返します。
	 * @return 実際に使われているバッファサイズ
	 */
	public int getBufferSize(){
		return this.getExtendedHttpServletResponse().getBufferSize();
	}

	/**
	 * バッファリングされているコンテンツを強制的にクライアントに出力します。
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public void flushBuffer() throws IOException{
		this.getExtendedHttpServletResponse().flushBuffer();
	}

	/**
	 * レスポンスのヘッダフィールドの値やステータスコードをそのままにしてバッファに溜められているコンテンツを消去します。
	 */
	public void resetBuffer(){
		this.getExtendedHttpServletResponse().resetBuffer();
	}

	/**
	 * レスポンスがすでにコミットされたかどうかを示す boolean を返します。
	 * @return レスポンスがすでにコミットされたかどうかを示す boolean
	 */
	public boolean isCommitted(){
		return this.getExtendedHttpServletResponse().isCommitted();
	}

	/**
	 * バッファリングされているデータ、ステータスコードとヘッダフィールドの値を削除します。
	 */
	public void reset(){
		this.getExtendedHttpServletResponse().reset();
	}

	/**
	 * レスポンスのロケールをセットしますが、このとき(Content-Type で指定する文字エンコーディングを含む)ヘッダも適当な値にセットします。
	 * @param loc レスポンスのロケールを指定する {@link java.util.Locale} オブジェクト
	 */
	public void setLocale(Locale loc){
		this.getExtendedHttpServletResponse().setLocale(loc);
	}

	/**
	 * レスポンスに設定されているロケールを返します。
	 * @return レスポンスに設定されているロケール
	 */
	public Locale getLocale(HttpServletRequest request, ExtendedHttpServletResponse response){
		return this.getExtendedHttpServletResponse().getLocale();
	}
}

