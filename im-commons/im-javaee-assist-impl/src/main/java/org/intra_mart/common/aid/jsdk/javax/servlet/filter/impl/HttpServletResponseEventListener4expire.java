package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventConfig;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventListenerException;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.SimpleHttpServletResponseEventListener;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;


/**
 * レスポンスの為の出力を返すメソッドをオーバーライドして、
 * コンテンツに対するキャッシュ制御をするリスナです。
 * <p>
 * このリスナは、レスポンスのための出力を現在のレスポンスから取得する直前に、
 * 以下のヘッダを設定します。
 * <br>
 * <pre>
 * Expires: Tue, 22 Feb 2000 12:00:00 GMT
 * </pre>
 * <p>
 * ただし、以下の条件を全て満たす場合、ヘッダの設定は行いません。
 * <ul>
 * <li>Content-Dispositionヘッダが設定されている
 * <li>Content-Dispositionヘッダの値にファイル名を指定するための「filename=」という文字列が含まれている
 * </ul>
 */
public class HttpServletResponseEventListener4expire extends SimpleHttpServletResponseEventListener{
	private static final String CONFIG_ID_SESSION = "session";
	private boolean validSession = true;

	/**
	 * 新しいリスナを作成します。
	 */
	public HttpServletResponseEventListener4expire(){
		super();
	}

	/**
	 * このリスナを初期化します。
	 * <p>
	 * このメソッドは、{@link #init(ExtendedProperties, ServletContext}
	 * によって呼び出されます。
	 * このメソッドは単にリターンするだけで、何もしません。
	 * <br>
	 * サブクラスが初期化処理を必要とする場合、
	 * このメソッドをオーバーライドしてください。
	 *
	 * @throws ServletException 初期化に失敗した場合
	 */
	protected void handleInit() throws HttpServletResponseEventListenerException{
		HttpServletResponseEventConfig config = this.getHttpServletResponseEventConfig();
		String value = config.getInitParameter(CONFIG_ID_SESSION);
		if(value == null){
			throw new HttpServletResponseEventListenerException("Initial-parameter is not defined: " + CONFIG_ID_SESSION);
		}
		this.validSession = Boolean.valueOf(value).booleanValue();
	}

// ----- javax.servlet.ServletResponse interface
	/**
	 * レスポンスにバイナリデータを出力する際に使用する
	 * {@link javax.servlet.ServletOutputStream}型のオブジェクトを返します。
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
		this.modifyHeader(request, response);	// ヘッダの設定
		return response.getOutputStream();		// 出力の取得と返却
	}

	/**
	 * 文字データをクライアントに送り返すのに使用する
	 * {@link java.io.PrintWriter}オブジェクトを返します。
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
		this.modifyHeader(request, response);	// ヘッダの設定
		return response.getWriter();			// 出力の取得と返却
	}

// ----- Original interface
	/**
	 * キャッシュ無効のヘッダを追加します。
	 * ただし、{@link #invalidNoCache(ExtendedHttpServletResponse)}
	 * が true を返した場合、キャッシュ制御の必要がないと判断して、
	 * レスポンスに対して何も行いません。
	 * @param response イベントの発生したレスポンス
	 */
	private void modifyHeader(HttpServletRequest request, ExtendedHttpServletResponse response){
		if(response.getHeader("Expires") == null){
			String format = "E, dd MMM yyyy hh:mm:ss zzz";
			SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			if(this.validSession){
				HttpSession httpSession = request.getSession(false);
				if(httpSession != null){
					int interval = httpSession.getMaxInactiveInterval();
					if(interval >= 0){
						long intervalMillis = ((long) interval) * 1000;
						response.setHeader("Expires",  formatter.format(new Date(System.currentTimeMillis() + intervalMillis)));
					}
				}
				else{
					response.setHeader("Expires",  formatter.format(new Date()));
				}
			}
			else{
				response.setHeader("Expires",  formatter.format(new Date()));
			}
		}
	}
}
