package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventConfig;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventListenerException;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.SimpleHttpServletResponseEventListener;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;


/**
 * 指定されたヘッダをレスポンスに設定するイベントリスナ。
 */
public class HttpServletResponseEventListener4header extends SimpleHttpServletResponseEventListener{
	private static final String CONFIG_ID_HEADER_NAME = "header-name";
	private static final String CONFIG_ID_HEADER_VALUE = "header-value";

	private String headerName = null;
	private String headerValue = null;

	/**
	 * 新しいリスナを作成します。
	 */
	public HttpServletResponseEventListener4header(){
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
		this.headerName = config.getInitParameter(CONFIG_ID_HEADER_NAME);
		if(this.headerName == null){
			throw new HttpServletResponseEventListenerException("Initial-parameter is not defined: " + CONFIG_ID_HEADER_NAME);
		}
		if(this.headerName.length() == 0){
			throw new HttpServletResponseEventListenerException("Initial-parameter is invalid: " + CONFIG_ID_HEADER_NAME);
		}

		this.headerValue = config.getInitParameter(CONFIG_ID_HEADER_VALUE);
		if(this.headerValue == null){
			throw new HttpServletResponseEventListenerException("Initial-parameter is not defined: " + CONFIG_ID_HEADER_VALUE);
		}
		if(this.headerValue.length() == 0){
			throw new HttpServletResponseEventListenerException("Initial-parameter is invalid: " + CONFIG_ID_HEADER_VALUE);
		}
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
		this.modifyHeader(response);			// ヘッダの設定
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
		this.modifyHeader(response);			// ヘッダの設定
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
	private void modifyHeader(ExtendedHttpServletResponse response){
		response.setHeader(this.headerName, this.headerValue);
	}
}
