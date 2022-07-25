package org.intra_mart.common.aid.jsdk.javax.servlet;

import javax.mail.internet.ContentType;
import javax.servlet.ServletResponse;

/**
 * このインタフェースは、{@link javax.servlet.ServletResponse}を拡張したものです。
 *
 */
public interface ExtendedServletResponse extends ServletResponse {
	/**
	 * レスポンスにセットされている
	 * クライアントに送り返されるレスポンスのコンテントタイプを
	 * 返します。<br>
	 * このメソッドは、
	 * {@link javax.servlet.ServletResponse#setContentType(String type)}
	 * または
	 * {@link #setContentType(ContentType)}
	 * によってセットされたコンテントタイプを返します。
	 * コンテントタイプには、
	 * 例えば、 text/html; charset=ISO-8859-4  のように
	 * 文字エンコーディングのタイプが含まれることがあります。
	 * @return コンテントタイプを指定する ContentType
	 */
	public ContentType getContentTypeObject();

	/**
	 * レスポンスに Content-Type をセットします。
	 * <p>
	 * このメソッドは、
	 * {@link javax.mail.internet.ContentType} での
	 * 引数指定を可能とするインタフェースを提供します。
	 * @param type コンテントタイプを指定する ContentType
	 */
	public void setContentType(ContentType type);

	/**
	 * レスポンスにセットされている
	 * メッセージボディ部の長さを返します。<br>
	 * このメソッドは、{@link javax.servlet.ServletResponse#setContentLength(int len)}
	 * によってセットされた長さを返します。
	 * 値を未設定の場合、null を返します。
	 * @return クライアントに送り返すメッセージボディの長さを指定する整数値。 HTTP の Content-Length ヘッダフィールドの値
	 */
	public Integer getContentLength();
}
