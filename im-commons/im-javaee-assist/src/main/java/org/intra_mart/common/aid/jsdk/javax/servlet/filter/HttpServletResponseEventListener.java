package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jdk.java.util.ExtendedProperties;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;


/**
 * レスポンスの各メソッド呼び出しに対するイベントをフックするためのインタフェースです。
 * 
 */
public interface HttpServletResponseEventListener{
	/**
	 * このビルダを初期化します。
	 * @param config 初期化パラメータ
	 *
	 * @throws HttpServletResponseEventListenerException 初期化に失敗した場合
	 */
	public void init(HttpServletResponseEventConfig config) throws HttpServletResponseEventListenerException;

// ----- javax.servlet.ServletResponse interface
	/**
	 * このレスポンスで送り返す MIME ボディに適用されている文字エンコーディング名を返します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#getCharacterEncoding()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#getCharacterEncoding()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return 文字エンコーディング名を意味する String。
	 *
	 * @see javax.servlet.ServletResponse#getCharacterEncoding()
	 */
	public String getCharacterEncoding(HttpServletRequest request, ExtendedHttpServletResponse response);

	/**
	 * レスポンスにバイナリデータを出力する際に使用する
	 * {@link javax.servlet.ServletOutputStream}型のオブジェクトを返します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#getOutputStream()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#getOutputStream()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return バイナリデータ出力に使用する {@link javax.servlet.ServletOutputStream}
	 *
	 * @throws IllegalStateException このレスポンスですでに {@link #getWriter()}  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 *
	 * @see #getWriter(HTTPContext, ExtendedHttpServletResponse)
	 * @see javax.servlet.ServletResponse#getWriter()
	 * @see javax.servlet.ServletResponse#getOutputStream()
	 */
	public ServletOutputStream getOutputStream(HttpServletRequest request, ExtendedHttpServletResponse response) throws IOException;

	/**
	 * 文字データをクライアントに送り返すのに使用する
	 * {@link java.io.PrintWriter}オブジェクトを返します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#getWriter()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#getWriter()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return クライアントに文字データを送り返すことができる {@link java.io.PrintWriter} オブジェクト
	 *
	 * @throws UnsupportedEncodingException setContentType  メソッドで指定された文字エンコーディングがサポートされていない場合
	 * @throws IllegalStateException このレスポンスですでに {@link #getOutputStream()}  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 *
	 * @see #getOutputStream(HTTPContext, ExtendedHttpServletResponse)
	 * @see javax.servlet.ServletResponse#getOutputStream()
	 * @see javax.servlet.ServletResponse#getWriter()
	 */
	public PrintWriter getWriter(HttpServletRequest request, ExtendedHttpServletResponse response) throws IOException;

	/**
	 * レスポンスのメッセージボディ部分の長さをセットします。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#setContentLength(int)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#setContentLength(int)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param len クライアントに送り返すメッセージボディの長さを指定する整数値。
	 *
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(HttpServletRequest request, ExtendedHttpServletResponse response, int len);

	/**
	 * クライアントに送り返されるレスポンスのコンテントタイプをセットします。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#setContentLength(String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#setContentLength(String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param type コンテントタイプを指定する String
	 *
	 * @see javax.servlet.ServletResponse#setContentType(String)
	 * @see #getOutputStream(HTTPContext, ExtendedHttpServletResponse)
	 * @see #getWriter(HTTPContext, ExtendedHttpServletResponse)
	 */
	public void setContentType(HttpServletRequest request, ExtendedHttpServletResponse response, String type);

	/**
	 * 適当なバッファサイズをレスポンスに含まれるメッセージボディのサイズに設定します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#setBufferSize(int)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#setBufferSize(int)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param size 適当なバッファサイズを指定する整数値
	 *
	 * @throws IllegalStateException すでにコンテンツが出力された後でこのメソッドが実行された場合
	 *
	 * @see javax.servlet.ServletResponse#setBufferSize(int)
	 * @see #getBufferSize(HTTPContext, ExtendedHttpServletResponse)
	 * @see #flushBuffer(HTTPContext, ExtendedHttpServletResponse)
	 * @see #isCommitted(HTTPContext, ExtendedHttpServletResponse)
	 * @see #reset(HTTPContext, ExtendedHttpServletResponse)
	 */
	public void setBufferSize(HttpServletRequest request, ExtendedHttpServletResponse response, int size);

	/**
	 * このレスポンスに設定されている実バッファサイズを返します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#getBufferSize()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#getBufferSize()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return 実際に使われているバッファサイズ
	 *
	 * @see javax.servlet.ServletResponse#getBufferSize()
	 * @see #setBufferSize(HTTPContext, ExtendedHttpServletResponse, int)
	 * @see #flushBuffer(HTTPContext, ExtendedHttpServletResponse)
	 * @see #isCommitted(HTTPContext, ExtendedHttpServletResponse)
	 * @see #reset(HTTPContext, ExtendedHttpServletResponse)
	 */
	public int getBufferSize(HttpServletRequest request, ExtendedHttpServletResponse response);

	/**
	 * バッファリングされているコンテンツを強制的にクライアントに出力します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#flushBuffer()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#flushBuffer()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @throws IOException 入出力時に例外が発生した場合
	 *
	 * @see javax.servlet.ServletResponse#flushBuffer()
	 * @see #setBufferSize(HTTPContext, ExtendedHttpServletResponse, int)
	 * @see #getBufferSize(HTTPContext, ExtendedHttpServletResponse)
	 * @see #isCommitted(HTTPContext, ExtendedHttpServletResponse)
	 * @see #reset(HTTPContext, ExtendedHttpServletResponse)
	 */
	public void flushBuffer(HttpServletRequest request, ExtendedHttpServletResponse response) throws IOException;

	/**
	 * レスポンスのヘッダフィールドの値やステータスコードをそのままにしてバッファに溜められているコンテンツを消去します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#resetBuffer()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#resetBuffer()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @see javax.servlet.ServletResponse#resetBuffer()
	 * @see #setBufferSize(HTTPContext, ExtendedHttpServletResponse, int)
	 * @see #getBufferSize(HTTPContext, ExtendedHttpServletResponse)
	 * @see #isCommitted(HTTPContext, ExtendedHttpServletResponse)
	 * @see #reset(HTTPContext, ExtendedHttpServletResponse)
	 */
	public void resetBuffer(HttpServletRequest request, ExtendedHttpServletResponse response);

	/**
	 * レスポンスがすでにコミットされたかどうかを示す boolean を返します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#isCommitted()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#isCommitted()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return レスポンスがすでにコミットされたかどうかを示す boolean
	 *
	 * @see javax.servlet.ServletResponse#isCommitted()
	 * @see #setBufferSize(HTTPContext, ExtendedHttpServletResponse, int)
	 * @see #getBufferSize(HTTPContext, ExtendedHttpServletResponse)
	 * @see #flushBuffer(HTTPContext, ExtendedHttpServletResponse)
	 * @see #reset(HTTPContext, ExtendedHttpServletResponse)
	 */
	public boolean isCommitted(HttpServletRequest request, ExtendedHttpServletResponse response);

	/**
	 * バッファリングされているデータ、ステータスコードとヘッダフィールドの値を削除します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#reset()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#reset()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @see javax.servlet.ServletResponse#reset()
	 * @see #setBufferSize(HTTPContext, ExtendedHttpServletResponse, int)
	 * @see #getBufferSize(HTTPContext, ExtendedHttpServletResponse)
	 * @see #flushBuffer(HTTPContext, ExtendedHttpServletResponse)
	 * @see #isCommitted(HTTPContext, ExtendedHttpServletResponse)
	 */
	public void reset(HttpServletRequest request, ExtendedHttpServletResponse response);

	/**
	 * レスポンスのロケールをセットしますが、このとき(Content-Type で指定する文字エンコーディングを含む)ヘッダも適当な値にセットします。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#setLocale(Locale)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#setLocale(Locale)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param loc レスポンスのロケールを指定する {@link java.util.Locale} オブジェクト
	 *
	 * @see javax.servlet.ServletResponse#setLocale(Locale)
	 * @see #getLocale(HTTPContext, ExtendedHttpServletResponse)
	 */
	public void setLocale(HttpServletRequest request, ExtendedHttpServletResponse response, Locale loc);

	/**
	 * レスポンスに設定されているロケールを返します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#getLocale()}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.ServletResponse#getLocale()}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return レスポンスに設定されているロケール
	 *
	 * @see javax.servlet.ServletResponse#getLocale()
	 * @see #setLocale(HTTPContext, ExtendedHttpServletResponse, java.util.Locale)
	 */
	public Locale getLocale(HttpServletRequest request, ExtendedHttpServletResponse response);


// ----- javax.servlet.http.HttpServletResponse interface
	/**
	 * 指定されたCookieをレスポンスに追加します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#addCookie(Cookie)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#addCookie(Cookie)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param cookie クライアントに返すCookie
	 *
	 * @see javax.servlet.http.HttpServletResponse#addCookie(Cookie)
	 */
	public void addCookie(HttpServletRequest request, ExtendedHttpServletResponse response, Cookie cookie);

	/**
	 * 指定された名前を持つヘッダが既にセットされているかどうかを表す booleanを返します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#containsHeader(String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#containsHeader(String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダ名
	 *
	 * @return 指定された名前のレスポンスヘッダが既にセットされていれば true、そうでなければfalse
	 *
	 * @see javax.servlet.http.HttpServletResponse#containsHeader(String)
	 */
	public boolean containsHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name);

	/**
	 * 指定されたURLがセッションIDを含むようにエンコードします。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#encodeURL(String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#encodeURL(String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param url エンコードするURL
	 *
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 *
	 * @see javax.servlet.http.HttpServletResponse#encodeURL(String)
	 */
	public String encodeURL(HttpServletRequest request, ExtendedHttpServletResponse response, String url);

	/**
	 * {@link #sendRedirect(String)} メソッドの中で使えるように、 指定されたURLをエンコードします。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#encodeRedirectURL(String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#encodeRedirectURL(String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param url エンコードするURL
	 *
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 *
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(String)
	 * @see #sendRedirect(HTTPContext, ExtendedHttpServletResponse, String)
	 * @see #encodeURL(HTTPContext, ExtendedHttpServletResponse, String)
	 */
	public String encodeRedirectURL(HttpServletRequest request, ExtendedHttpServletResponse response, String url);

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#sendError(int, String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#sendError(int, String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param sc エラーステータスコード
	 * @param msg 説明文
	 *
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 *
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, String)
	 */
	public void sendError(HttpServletRequest request, ExtendedHttpServletResponse response, int sc, String msg) throws IOException;

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#sendError(int)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#sendError(int)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param sc エラーステータスコード
	 *
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 *
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	public void sendError(HttpServletRequest request, ExtendedHttpServletResponse response, int sc) throws IOException;

	/**
	 * 指定されたリダイレクト先のURLを用いて、 クライアントに一時的なリダイレクトレスポンスを送信します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#sendRedirect(String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#sendRedirect(String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param location リダイレクト先のURL
	 *
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 *
	 * @see javax.servlet.http.HttpServletResponse#sendRedirect(String)
	 */
	public void sendRedirect(HttpServletRequest request, ExtendedHttpServletResponse response, String location) throws IOException;

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを設定します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#setDateHeader(String, long)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#setDateHeader(String, long)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name 設定するヘッダの名称
	 * @param date 設定するヘッダの値
	 *
	 * @see javax.servlet.http.HttpServletResponse#setDateHeader(String, long)
	 * @see #containsHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String)
	 * @see #addDateHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String, long)
	 */
	public void setDateHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, long date);

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを追加します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#addDateHeader(String, long)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#addDateHeader(String, long)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name 追加するヘッダの名称
	 * @param date 追加するヘッダの値
	 *
	 * @see javax.servlet.http.HttpServletResponse#addDateHeader(String, long)
	 * @see #setDateHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String, long)
	 */
	public void addDateHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, long date);

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを設定します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#setHeader(String, String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#setHeader(String, String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value ヘッダの値
	 *
	 * @see javax.servlet.http.HttpServletResponse#setHeader(String, String)
	 * @see #containsHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String)
	 * @see #addHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String, java.lang.String)
	 */
	public void setHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, String value);

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを追加します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#addHeader(String, String)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#addHeader(String, String)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value 追加するヘッダの値
	 *
	 * @see javax.servlet.http.HttpServletResponse#addHeader(String, String)
	 * @see #setHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String, java.lang.String)
	 */
	public void addHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, String value);

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを設定します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#setIntHeader(String, int)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#setIntHeader(String, int)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 *
	 * @see javax.servlet.http.HttpServletResponse#setIntHeader(String, int)
	 * @see #containsHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String)
	 * @see #addIntHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String, int)
	 */
	public void setIntHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, int value);

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを追加します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#addIntHeader(String, int)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#addIntHeader(String, int)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 *
	 * @see javax.servlet.http.HttpServletResponse#addIntHeader(String, int)
	 * @see #setIntHeader(HTTPContext, ExtendedHttpServletResponse, java.lang.String, int)
	 */
	public void addIntHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, int value);

	/**
	 * このレスポンスのステータスコードを設定します。
	 * <p>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#setStatus(int)}
	 * の呼び出しをフックします。
	 * このメソッドの返り値が、フックしたレスポンスの
	 * {@link javax.servlet.http.HttpServletResponse#setStatus(int)}
	 * 呼び出しに対する返り値になります。
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param sc ステータスコード
	 *
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
	 * @see #sendError(HTTPContext, ExtendedHttpServletResponse, int, java.lang.String)
	 */
	public void setStatus(HttpServletRequest request, ExtendedHttpServletResponse response, int sc);
}
