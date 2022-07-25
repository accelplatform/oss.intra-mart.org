package org.intra_mart.common.aid.jsdk.javax.servlet.http;

import javax.mail.internet.ContentType;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * レスポンスのサプクラス化による拡張を可能とする
 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse}
 * インタフェースの簡潔な実装方法を提供します。
 * <br>
 * このクラスは、WrapperパターンもしくはDecoratorパターンに基づいて
 * 実装されています。
 * 各々のメソッドは、ラップされたレスポンスオブジェクトのメソッドを
 * デフォルトで呼び出します。
 *
 */
public class ExtendedHttpServletResponseWrapper extends HttpServletResponseWrapper implements ExtendedHttpServletResponse {
	/**
	 * このインスタンスがラップしているレスポンス
	 */
	private ExtendedHttpServletResponse extendedHttpServletResponse = null;

	/**
	 * 指定したレスポンスをラッピングしたレスポンス・アダプタを構築します。
	 * @param response ラップ対象となるレスポンス
	 * @throws IllegalArgumentException response が null である場合
	 */
	protected ExtendedHttpServletResponseWrapper(ExtendedHttpServletResponse response){
		super(response);
		this.extendedHttpServletResponse = response;
	}


// ----- ExtendedHttpServletResponse interface
	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getStatus(String)}
	 * メソッドをそのまま呼び出します。
	 * @return ステータスコード
	 */
	public Integer getStatus(){
		return this.getExtendedHttpServletResponse().getStatus();
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getIntHeader(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている整数値
	 */
	public Integer getIntHeader(String name){
		return this.getExtendedHttpServletResponse().getIntHeader(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getIntHeaders(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての整数値
	 */
	public int[] getIntHeaders(String name){
		return this.getExtendedHttpServletResponse().getIntHeaders(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getDateHeader(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている日付の値
	 */
	public Long getDateHeader(String name){
		return this.getExtendedHttpServletResponse().getDateHeader(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getDateHeaders(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての日付の値
	 */
	public long[] getDateHeaders(String name){
		return this.getExtendedHttpServletResponse().getDateHeaders(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getStringHeader(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている文字列の値
	 */
	public String getStringHeader(String name){
		return this.getExtendedHttpServletResponse().getStringHeader(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getStringHeaders(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての文字列の値
	 */
	public String[] getStringHeaders(String name){
		return this.getExtendedHttpServletResponse().getStringHeaders(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getHeader(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている値
	 */
	public Object getHeader(String name){
		return this.getExtendedHttpServletResponse().getHeader(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getHeaders(String)}
	 * メソッドをそのまま呼び出します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての値
	 */
	public Object[] getHeaders(String name){
		return this.getExtendedHttpServletResponse().getHeaders(name);
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse#getCookies()}
	 * メソッドをそのまま呼び出します。
	 * @return セットされている全ての Cookie
	 */
	public Cookie[] getCookies(){
		return this.getExtendedHttpServletResponse().getCookies();
	}


// ----- ExtendedServletResponse interface
	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedServletResponse#getContentTypeObject()}
	 * メソッドをそのまま呼び出します。
	 * @return コンテントタイプを指定する ContentType
	 */
	public ContentType getContentTypeObject(){
		return this.getExtendedHttpServletResponse().getContentTypeObject();
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedServletResponse#setContentType(ContentType)}
	 * メソッドをそのまま呼び出します。
	 * @param type コンテントタイプを指定する ContentType
	 */
	public void setContentType(ContentType type){
		this.getResponse().setContentType(type.toString());
	}

	/**
	 * このメソッドにおけるデフォルトのふるまいとして、
	 * ラップされたレスポンス・オブジェクトにある
	 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedServletResponse#getContentLength()}
	 * メソッドをそのまま呼び出します。
	 * @return クライアントに送り返すメッセージボディの長さを指定する整数値。 HTTP の Content-Length ヘッダフィールドの値
	 */
	public Integer getContentLength(){
		return this.getExtendedHttpServletResponse().getContentLength();
	}


// ----- Original implemntation
	/**
	 * このオブジェクトにラップされているレスポンスを返します。
	 * @return ラップされているレスポンス
	 */
	public ExtendedHttpServletResponse getExtendedHttpServletResponse(){
		return this.extendedHttpServletResponse;
	}
}
