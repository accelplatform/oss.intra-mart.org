package org.intra_mart.common.aid.jsdk.javax.servlet.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.ExtendedServletResponse;


/**
 * このインタフェースは、{@link javax.servlet.http.HttpServletResponse}を拡張したものです。
 *
 */
public interface ExtendedHttpServletResponse extends ExtendedServletResponse,
		HttpServletResponse {

	/**
	 * レスポンスにセットされている
	 * ステータスコードを返します。<br>
	 * このメソッドは、
	 * {@link javax.servlet.http.HttpServletResponse#sendRedirect(String location)}、
	 * {@link javax.servlet.http.HttpServletResponse#sendError(int sc)}、
	 * {@link javax.servlet.http.HttpServletResponse#sendError(int sc, String msg)}、
	 * {@link javax.servlet.http.HttpServletResponse#setStatus(int sc)}、
	 * または{@link javax.servlet.http.HttpServletResponse#setStatus(int sc, String sm)}
	 * によってセットされたステータスコードを返します。
	 * 値を未設定の場合、null を返します。
	 * @return ステータスコード
	 */
	public Integer getStatus();

	/**
	 * 指定された名称でレスポンスヘッダに設定されている数値を返します。
	 * このヘッダに対して数値を未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている整数値
	 */
	public Integer getIntHeader(String name);

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての数値を返します。
	 * このヘッダに対して値を未設定の場合、null を返します。
	 * このヘッダに対して数値を未設定の場合、空の配列を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての整数値
	 */
	public int[] getIntHeaders(String name);

	/**
	 * 指定された名称でレスポンスヘッダに設定されている日付の値を返します。
	 * このヘッダに対して日付の値を未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている日付の値
	 */
	public Long getDateHeader(String name);

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての日付の値を返します。
	 * このヘッダに対して値を未設定の場合、null を返します。
	 * このヘッダに対して日付の値を未設定の場合、空の配列を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての日付の値
	 */
	public long[] getDateHeaders(String name);

	/**
	 * 指定された名称でレスポンスヘッダに設定されている文字列の値を返します。
	 * このヘッダに対して文字列の値を未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている文字列の値
	 */
	public String getStringHeader(String name);

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての文字列の値を返します。
	 * このヘッダに対して値を未設定の場合、null を返します。
	 * このヘッダに対して文字列の値を未設定の場合、空の配列を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての文字列の値
	 */
	public String[] getStringHeaders(String name);

	/**
	 * 指定された名称でレスポンスヘッダに設定されている値を返します。
	 * ヘッダをセットするために利用したメソッドにより、
	 * 返り値の型が決定されます。
	 * <table>
	 * <tr>
	 * <th>メソッド</th><th>返り値の型</th>
	 * </tr>
	 * <tr>
	 * <td>
	 * {@link javax.servlet.http.HttpServletResponse#addDateHeader(String name, long date)}
	 * </td>
	 * <td>{@link java.lang.Long}</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * {@link javax.servlet.http.HttpServletResponse#setDateHeader(String name, long date)}
	 * </td>
	 * <td>{@link java.lang.Long}</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * {@link javax.servlet.http.HttpServletResponse#addIntHeader(String name, int value)}
	 * </td>
	 * <td>{@link java.lang.Integer}</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * {@link javax.servlet.http.HttpServletResponse#setIntHeader(String name, int value)}
	 * </td>
	 * <td>{@link java.lang.Integer}</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * {@link javax.servlet.http.HttpServletResponse#addHeader(String name, String value)}
	 * </td>
	 * <td>{@link java.lang.String}</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * {@link javax.servlet.http.HttpServletResponse#setHeader(String name, String value)}
	 * </td>
	 * <td>{@link java.lang.String}</td>
	 * </tr>
	 * </table>
	 * このヘッダが未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている値
	 */
	public Object getHeader(String name);

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての値を返します。
	 * 配列の各要素の型は、{@link #getHeader(String)}の返り値と同様です。
	 * このヘッダが未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての値
	 */
	public Object[] getHeaders(String name);

	/**
	 * このレスポンスにセットされたすべての Cookie を返します。
	 * Cookie を未設定の場合、null を返します。
	 * @return セットされている全ての Cookie
	 */
	public Cookie[] getCookies();
}
