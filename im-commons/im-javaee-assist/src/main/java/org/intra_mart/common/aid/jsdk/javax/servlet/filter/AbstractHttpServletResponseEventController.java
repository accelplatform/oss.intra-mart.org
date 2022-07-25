package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponseWrapper;


/**
 * イベントを制御するレスポンスの抽象実装です。<p>
 * {@link javax.servlet.http.HttpServletResponse} が持つ
 * 各メソッド（非推奨を除く）をオーバーライドして、
 * リスナに処理を委任するロジックを追加します。
 * 
 */
public abstract class AbstractHttpServletResponseEventController extends ExtendedHttpServletResponseWrapper implements HttpServletResponseEventController{
	/**
	 * このリスナに渡された設定オブジェクト
	 */
	private HttpServletResponseEventConfig eventConfig = null;

	/**
	 * 新しいインスタンスを生成します。
	 */
	public AbstractHttpServletResponseEventController(ExtendedHttpServletResponse response){
		super(response);
	}

	/**
	 * このリスナを初期化します。
	 * <p>
	 * このメソッドは、{@link #init(HttpServletResponseEventListenerConfig)}
	 * によって呼び出されます。
	 * このメソッドは単にリターンするだけで、何もしません。
	 * <br>
	 * サブクラスが初期化処理を必要とする場合、
	 * このメソッドをオーバーライドしてください。
	 *
	 * @throws HttpServletResponseEventControllerException 初期化に失敗した場合
	 */
	protected void handleInit() throws HttpServletResponseEventControllerException{
		return;
	}

	/**
	 * リクエストを返します。
	 * @return 現在のリクエスト
	 */
	protected abstract HttpServletRequest getHttpServletRequest();

	/**
	 * リスナを返します。
	 * @return リスナ
	 */
	protected abstract HttpServletResponseEventListener getHttpServletResponseEventListener();

	// ----- org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventController interface
	/**
	 * このビルダを初期化します。
	 * @param config 初期化パラメータ
	 *
	 * @throws HttpServletResponseEventControllerException 初期化に失敗した場合
	 */
	public final void init(HttpServletResponseEventConfig config) throws HttpServletResponseEventControllerException{
		this.eventConfig = config;
		this.handleInit();
	}

	// ----- javax.servlet.http.HttpServletResponse interface
	/**
	 * 指定されたCookieをレスポンスに追加します。
	 * @param cookie クライアントに返すCookie
	 */
	public void addCookie(Cookie cookie){
		this.getHttpServletResponseEventListener().addCookie(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), cookie);
	}

	/**
	 * 指定された名前を持つヘッダが既にセットされているかどうかを表す booleanを返します。
	 * @param name ヘッダ名
	 * @return 指定された名前のレスポンスヘッダが既にセットされていれば true、そうでなければfalse
	 */
	public boolean containsHeader(String name){
		return this.getHttpServletResponseEventListener().containsHeader(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), name);
	}

	/**
	 * 指定されたURLがセッションIDを含むようにエンコードします。
	 * @param url エンコードするURL
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public String encodeURL(String url){
		return this.getHttpServletResponseEventListener().encodeURL(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), url);
	}

	/**
	 * {@link #sendRedirect(String)} メソッドの中で使えるように、 指定されたURLをエンコードします。
	 * @param url エンコードするURL
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public String encodeRedirectURL(String url){
		return this.getHttpServletResponseEventListener().encodeRedirectURL(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), url);
	}

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * @param sc エラーステータスコード
	 * @param msg 説明文
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendError(int sc, String msg) throws IOException{
		this.getHttpServletResponseEventListener().sendError(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), sc, msg);
	}

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * @param sc エラーステータスコード
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendError(int sc) throws IOException{
		this.getHttpServletResponseEventListener().sendError(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), sc);
	}

	/**
	 * 指定されたリダイレクト先のURLを用いて、 クライアントに一時的なリダイレクトレスポンスを送信します。
	 * @param location リダイレクト先のURL
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendRedirect(String location) throws IOException{
		this.getHttpServletResponseEventListener().sendRedirect(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), location);
	}

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを設定します。
	 * @param name 設定するヘッダの名称
	 * @param date 設定するヘッダの値
	 */
	public void setDateHeader(String name, long date){
		this.getHttpServletResponseEventListener().setDateHeader(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), name, date);
	}

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを追加します。
	 * @param name 追加するヘッダの名称
	 * @param date 追加するヘッダの値
	 */
	public void addDateHeader(String name, long date){
		this.getHttpServletResponseEventListener().addDateHeader(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), name, date);
	}

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを設定します。
	 * @param name ヘッダの名称
	 * @param value ヘッダの値
	 */
	public void setHeader(String name, String value){
		this.getHttpServletResponseEventListener().setHeader(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), name, value);
	}

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを追加します。
	 * @param name ヘッダの名称
	 * @param value 追加するヘッダの値
	 */
	public void addHeader(String name, String value){
		this.getHttpServletResponseEventListener().addHeader(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), name, value);
	}

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを設定します。
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 */
	public void setIntHeader(String name, int value){
		this.getHttpServletResponseEventListener().setIntHeader(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), name, value);
	}

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを追加します。
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 */
	public void addIntHeader(String name, int value){
		this.getHttpServletResponseEventListener().addIntHeader(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), name, value);
	}

	/**
	 * このレスポンスのステータスコードを設定します。
	 * @param sc ステータスコード
	 */
	public void setStatus(int sc){
		this.getHttpServletResponseEventListener().setStatus(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), sc);
	}

	// ----- javax.servlet.ServletResponse interface
	/**
	 * このレスポンスで送り返す MIME ボディに適用されている文字エンコーディング名を返します。
	 * @return 文字エンコーディング名を意味する String。
	 */
	public String getCharacterEncoding(){
		return this.getHttpServletResponseEventListener().getCharacterEncoding(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}

	/**
	 * レスポンスにバイナリデータを出力する際に使用する
	 * {@link javax.servlet.ServletOutputStream}型のオブジェクトを返します。
	 * @return バイナリデータ出力に使用する {@link javax.servlet.ServletOutputStream}
	 * @throws IllegalStateException このレスポンスですでに {@link #getWriter()}  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public ServletOutputStream getOutputStream() throws IOException{
		return this.getHttpServletResponseEventListener().getOutputStream(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
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
		return this.getHttpServletResponseEventListener().getWriter(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}

	/**
	 * レスポンスのメッセージボディ部分の長さをセットします。
	 * @param len クライアントに送り返すメッセージボディの長さを指定する整数値。
	 */
	public void setContentLength(int len){
		this.getHttpServletResponseEventListener().setContentLength(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), len);
	}

	/**
	 * クライアントに送り返されるレスポンスのコンテントタイプをセットします。
	 * @param type コンテントタイプを指定する String
	 */
	public void setContentType(String type){
		this.getHttpServletResponseEventListener().setContentType(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), type);
	}

	/**
	 * 適当なバッファサイズをレスポンスに含まれるメッセージボディのサイズに設定します。
	 * @param size 適当なバッファサイズを指定する整数値
	 * @throws IllegalStateException すでにコンテンツが出力された後でこのメソッドが実行された場合
	 */
	public void setBufferSize(int size){
		this.getHttpServletResponseEventListener().setBufferSize(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), size);
	}

	/**
	 * このレスポンスに設定されている実バッファサイズを返します。
	 * @return 実際に使われているバッファサイズ
	 */
	public int getBufferSize(){
		return this.getHttpServletResponseEventListener().getBufferSize(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}

	/**
	 * バッファリングされているコンテンツを強制的にクライアントに出力します。
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public void flushBuffer() throws IOException{
		this.getHttpServletResponseEventListener().flushBuffer(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}

	/**
	 * レスポンスのヘッダフィールドの値やステータスコードをそのままにしてバッファに溜められているコンテンツを消去します。
	 */
	public void resetBuffer(){
		this.getHttpServletResponseEventListener().resetBuffer(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}

	/**
	 * レスポンスがすでにコミットされたかどうかを示す boolean を返します。
	 * @return レスポンスがすでにコミットされたかどうかを示す boolean
	 */
	public boolean isCommitted(){
		return this.getHttpServletResponseEventListener().isCommitted(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}

	/**
	 * バッファリングされているデータ、ステータスコードとヘッダフィールドの値を削除します。
	 */
	public void reset(){
		this.getHttpServletResponseEventListener().reset(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}

	/**
	 * レスポンスのロケールをセットしますが、このとき(Content-Type で指定する文字エンコーディングを含む)ヘッダも適当な値にセットします。
	 * @param loc レスポンスのロケールを指定する {@link java.util.Locale} オブジェクト
	 */
	public void setLocale(Locale loc){
		this.getHttpServletResponseEventListener().setLocale(this.getHttpServletRequest(), this.getExtendedHttpServletResponse(), loc);
	}

	/**
	 * レスポンスに設定されているロケールを返します。
	 * @return レスポンスに設定されているロケール
	 */
	public Locale getLocale(HttpServletRequest request, ExtendedHttpServletResponse response){
		return this.getHttpServletResponseEventListener().getLocale(this.getHttpServletRequest(), this.getExtendedHttpServletResponse());
	}
}
