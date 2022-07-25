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


/**
 * レスポンスの各メソッド呼び出しに対するイベントをフックするためのコンポーネント実装です。
 * <p>
 * このクラスは、フックしたレスポンスの適当なメソッドを実行するだけの単純実装です。
 * アプリケーションは、このクラスを継承して、
 * 任意のメソッドのみをオーバーライドする事により
 * 必要なイベントフックのみを実装する事ができます。
 * 
 */
public class SimpleHttpServletResponseEventListener extends AbstractHttpServletResponseEventListener{
	/**
	 * 新しいリスナを作成します。
	 */
	protected SimpleHttpServletResponseEventListener(){
		super();
	}

// ----- javax.servlet.ServletResponse interface
	/**
	 * このレスポンスで送り返す MIME ボディに適用されている文字エンコーディング名を返します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.getCharacterEncoding();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return 文字エンコーディング名を意味する String。
	 */
	public String getCharacterEncoding(HttpServletRequest request, ExtendedHttpServletResponse response){
		return response.getCharacterEncoding();
	}

	/**
	 * レスポンスにバイナリデータを出力する際に使用する
	 * {@link javax.servlet.ServletOutputStream}型のオブジェクトを返します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.getOutputStream();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return バイナリデータ出力に使用する {@link javax.servlet.ServletOutputStream}
	 *
	 * @throws IllegalStateException このレスポンスですでに {@link #getWriter()}  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public ServletOutputStream getOutputStream(HttpServletRequest request, ExtendedHttpServletResponse response) throws IOException{
		return response.getOutputStream();
	}

	/**
	 * 文字データをクライアントに送り返すのに使用する
	 * {@link java.io.PrintWriter}オブジェクトを返します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.getWriter();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return クライアントに文字データを送り返すことができる {@link java.io.PrintWriter} オブジェクト
	 *
	 * @throws UnsupportedEncodingException setContentType  メソッドで指定された文字エンコーディングがサポートされていない場合
	 * @throws IllegalStateException このレスポンスですでに {@link #getOutputStream()}  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public PrintWriter getWriter(HttpServletRequest request, ExtendedHttpServletResponse response) throws IOException{
		return response.getWriter();
	}

	/**
	 * レスポンスのメッセージボディ部分の長さをセットします。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setContentLength(len);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param len クライアントに送り返すメッセージボディの長さを指定する整数値。
	 */
	public void setContentLength(HttpServletRequest request, ExtendedHttpServletResponse response, int len){
		response.setContentLength(len);
	}

	/**
	 * クライアントに送り返されるレスポンスのコンテントタイプをセットします。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setContentType(type);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param type コンテントタイプを指定する String
	 */
	public void setContentType(HttpServletRequest request, ExtendedHttpServletResponse response, String type){
		response.setContentType(type);
	}

	/**
	 * 適当なバッファサイズをレスポンスに含まれるメッセージボディのサイズに設定します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setBufferSize(size);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param size 適当なバッファサイズを指定する整数値
	 *
	 * @throws IllegalStateException すでにコンテンツが出力された後でこのメソッドが実行された場合
	 */
	public void setBufferSize(HttpServletRequest request, ExtendedHttpServletResponse response, int size){
		response.setBufferSize(size);
	}

	/**
	 * このレスポンスに設定されている実バッファサイズを返します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.getBufferSize();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return 実際に使われているバッファサイズ
	 */
	public int getBufferSize(HttpServletRequest request, ExtendedHttpServletResponse response){
		return response.getBufferSize();
	}

	/**
	 * バッファリングされているコンテンツを強制的にクライアントに出力します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.flushBuffer();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public void flushBuffer(HttpServletRequest request, ExtendedHttpServletResponse response) throws IOException{
		response.flushBuffer();
	}

	/**
	 * レスポンスのヘッダフィールドの値やステータスコードをそのままにしてバッファに溜められているコンテンツを消去します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.resetBuffer();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 */
	public void resetBuffer(HttpServletRequest request, ExtendedHttpServletResponse response){
		response.resetBuffer();
	}

	/**
	 * レスポンスがすでにコミットされたかどうかを示す boolean を返します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.isCommitted();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return レスポンスがすでにコミットされたかどうかを示す boolean
	 */
	public boolean isCommitted(HttpServletRequest request, ExtendedHttpServletResponse response){
		return response.isCommitted();
	}

	/**
	 * バッファリングされているデータ、ステータスコードとヘッダフィールドの値を削除します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.reset();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 */
	public void reset(HttpServletRequest request, ExtendedHttpServletResponse response){
		response.reset();
	}

	/**
	 * レスポンスのロケールをセットしますが、このとき(Content-Type で指定する文字エンコーディングを含む)ヘッダも適当な値にセットします。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setLocale(loc);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param loc レスポンスのロケールを指定する {@link java.util.Locale} オブジェクト
	 */
	public void setLocale(HttpServletRequest request, ExtendedHttpServletResponse response, Locale loc){
		response.setLocale(loc);
	}

	/**
	 * レスポンスに設定されているロケールを返します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.getLocale();
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 *
	 * @return レスポンスに設定されているロケール
	 */
	public Locale getLocale(HttpServletRequest request, ExtendedHttpServletResponse response){
		return response.getLocale();
	}


// ----- javax.servlet.http.HttpServletResponse interface
	/**
	 * 指定されたCookieをレスポンスに追加します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.addCookie(cookie);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param cookie クライアントに返すCookie
	 */
	public void addCookie(HttpServletRequest request, ExtendedHttpServletResponse response, Cookie cookie){
		response.addCookie(cookie);
	}

	/**
	 * 指定された名前を持つヘッダが既にセットされているかどうかを表す booleanを返します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.containsHeader(name);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダ名
	 *
	 * @return 指定された名前のレスポンスヘッダが既にセットされていれば true、そうでなければfalse
	 */
	public boolean containsHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name){
		return response.containsHeader(name);
	}

	/**
	 * 指定されたURLがセッションIDを含むようにエンコードします。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.encodeURL(url);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param url エンコードするURL
	 *
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public String encodeURL(HttpServletRequest request, ExtendedHttpServletResponse response, String url){
		return response.encodeURL(url);
	}

	/**
	 * {@link #sendRedirect(String)} メソッドの中で使えるように、 指定されたURLをエンコードします。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * return response.encodeRedirectURL(url);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param url エンコードするURL
	 *
	 * @return エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public String encodeRedirectURL(HttpServletRequest request, ExtendedHttpServletResponse response, String url){
		return response.encodeRedirectURL(url);
	}

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.sendError(sc, msg);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param sc エラーステータスコード
	 * @param msg 説明文
	 *
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendError(HttpServletRequest request, ExtendedHttpServletResponse response, int sc, String msg) throws IOException{
		response.sendError(sc, msg);
	}

	/**
	 * バッファをクリアし、 指定されたステータスを使ってクライアントにエラーレスポンスを送ります。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.sendError(sc);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param sc エラーステータスコード
	 *
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendError(HttpServletRequest request, ExtendedHttpServletResponse response, int sc) throws IOException{
		response.sendError(sc);
	}

	/**
	 * 指定されたリダイレクト先のURLを用いて、 クライアントに一時的なリダイレクトレスポンスを送信します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.sendRedirect(location);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param location リダイレクト先のURL
	 *
	 * @throws IOException 入出力例外が発生した場合
	 * @throws IllegalStateException レスポンスが既にコミットされている場合
	 */
	public void sendRedirect(HttpServletRequest request, ExtendedHttpServletResponse response, String location) throws IOException{
		response.sendRedirect(location);
	}

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを設定します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setDateHeader(name, date);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name 設定するヘッダの名称
	 * @param date 設定するヘッダの値
	 */
	public void setDateHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, long date){
		response.setDateHeader(name, date);
	}

	/**
	 * 指定された名称で指定された日付の値を持つレスポンスヘッダを追加します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.addDateHeader(name, date);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name 追加するヘッダの名称
	 * @param date 追加するヘッダの値
	 */
	public void addDateHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, long date){
		response.addDateHeader(name, date);
	}

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを設定します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setHeader(name, value);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value ヘッダの値
	 */
	public void setHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, String value){
		response.setHeader(name, value);
	}

	/**
	 * 指定された名称で指定された値を持つレスポンスヘッダを追加します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.addHeader(name, value);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value 追加するヘッダの値
	 */
	public void addHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, String value){
		response.addHeader(name, value);
	}

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを設定します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setIntHeader(name, value);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 */
	public void setIntHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, int value){
		response.setIntHeader(name, value);
	}

	/**
	 * 指定された名称で指定された整数値を持つレスポンスヘッダを追加します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.addIntHeader(name, value);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param name ヘッダの名称
	 * @param value 割り当てられる整数値
	 */
	public void addIntHeader(HttpServletRequest request, ExtendedHttpServletResponse response, String name, int value){
		response.addIntHeader(name, value);
	}

	/**
	 * このレスポンスのステータスコードを設定します。
	 * <p>
	 * このメソッドは、単純に以下を実行します。
	 * <br>
	 * <code>
	 * response.setStatus(sc);
	 * </code>
	 *
	 * @param request リクエスト
	 * @param response イベントの発生したレスポンス
	 * @param sc ステータスコード
	 */
	public void setStatus(HttpServletRequest request, ExtendedHttpServletResponse response, int sc){
		response.setStatus(sc);
	}
}
