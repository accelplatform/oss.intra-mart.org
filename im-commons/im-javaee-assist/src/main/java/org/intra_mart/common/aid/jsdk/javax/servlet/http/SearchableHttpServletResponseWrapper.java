package org.intra_mart.common.aid.jsdk.javax.servlet.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * {@link javax.servlet.http.HttpServletResponseWrapper}の拡張実装を提供します。
 * このクラスを利用する事により、レスポンスにセットした値を取得可能になり、
 * アプリケーションプログラム内での情報操作の利便性が向上します。
 *
 */
public class SearchableHttpServletResponseWrapper extends HttpServletResponseWrapper implements ExtendedHttpServletResponse{
	/**
	 * セットされたクッキー情報を保持するコレクション
	 */
	private Collection cookieCollection = new ArrayList();
	/**
	 * このレスポンスにセットされたヘッダフィールド値を保存するマップ
	 */
	private Map headerMap = new HashMap();
	/**
	 * このレスポンスにセットされたステータスコード
	 */
	private Integer statusCode = null;
	/**
	 * このレスポンスにセットされた Content-Length 値
	 */
	private Integer contentLength = null;
	/**
	 * このレスポンスにセットされた Content-Type 値
	 */
	private ContentType contentType = null;
	/**
	 * このインスタンスがラップしているレスポンス
	 */
	private HttpServletResponse httpServletResponse = null;

	/**
	 * 指定したレスポンスをラッピングしたレスポンス・アダプタを構築します。
	 * @param response ラップされるレスポンス・オブジェクト
	 * @throws IllegalArgumentException response が null である場合
	 */
	public SearchableHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		this.httpServletResponse = response;
	}

	/**
	 * このオブジェクトにラップされているレスポンスを返します。
	 * @return ラップされているレスポンス
	 */
	public HttpServletResponse getHttpServletResponse(){
		return this.httpServletResponse;
	}

	/**
	 * このメソッドは、スーパークラスにある
	 * {@link javax.servlet.http.HttpServletResponseWrapper#addCookie(Cookie cookie)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 cookie を保持します。
	 * 保持したオブジェクト cookie は、
	 * {@link #getCookies()}メソッドによって
	 * アプリケーションに返されます。
	 *
	 * @param cookie クライアントに返すCookie
	 */
	public void addCookie(Cookie cookie) {
		super.addCookie(cookie);
		this.cookieCollection.add(cookie);
	}

	/**
	 * このレスポンスにセットされたすべての Cookie を返します。
	 * Cookie を１つもセットしていない場合、null を返します。
	 * @return セットされている全ての Cookie
	 */
	public Cookie[] getCookies(){
		return (Cookie[]) this.cookieCollection.toArray(new Cookie[this.cookieCollection.size()]);
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#addDateHeader(java.lang.String name, long date)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 name および date を保持します。
	 * 保持した情報は、以下のメソッドによってアプリケーションに返されます。
	 * <ou>
	 * <li>{@link #getDateHeader(java.lang.String)}
	 * <li>{@link #getDateHeaders(java.lang.String)}
	 * <li>{@link #getHeader(java.lang.String)}
	 * <li>{@link #getHeaders(java.lang.String)}
	 * </ou>
	 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
	 * @see javax.servlet.http.HttpServletResponseWrapper#addDateHeader(java.lang.String, long)
	 */
	public void addDateHeader(String name, long date) {
		super.addDateHeader(name, date);
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection == null){
			collection = new ArrayList();
			this.headerMap.put(name, collection);
		}
		collection.add(new Long(date));
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#setDateHeader(java.lang.String name, long date)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 name および date を保持します。
	 * 保持した情報は、以下のメソッドによってアプリケーションに返されます。
	 * <ou>
	 * <li>{@link #getDateHeader(java.lang.String)}
	 * <li>{@link #getDateHeaders(java.lang.String)}
	 * <li>{@link #getHeader(java.lang.String)}
	 * <li>{@link #getHeaders(java.lang.String)}
	 * </ou>
	 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
	 * @see javax.servlet.http.HttpServletResponseWrapper#setDateHeader(java.lang.String, long)
	 */
	public void setDateHeader(String name, long date) {
		super.setDateHeader(name, date);
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection == null){
			collection = new ArrayList();
			this.headerMap.put(name, collection);
		}
		else{
			collection.clear();
		}
		collection.add(new Long(date));
	}

	/**
	 * 指定された名称でレスポンスヘッダに設定されている日付の値を返します。
	 * このヘッダに対して日付の値を未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている日付の値
	 */
	public Long getDateHeader(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			Iterator iterator = collection.iterator();
			while(iterator.hasNext()){
				Object value = iterator.next();
				if(value instanceof Long){
					return (Long) value;
				}
			}
		}
		return null;
	}

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての日付の値を返します。
	 * このヘッダに対して値を未設定の場合、null を返します。
	 * このヘッダに対して日付の値を未設定の場合、空の配列を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての日付の値
	 */
	public long[] getDateHeaders(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			Collection dateCollection = new ArrayList();
			if(collection.size() > 0){
				Iterator iterator = collection.iterator();
				while(iterator.hasNext()){
					Object value = iterator.next();
					if(value instanceof Long){
						dateCollection.add(value);
					}
				}
			}

			int collectionSize = dateCollection.size();
			if(collectionSize > 0){
				long[] dateArray = new long[collectionSize];
				int idx = 0;
				Iterator iterator = dateCollection.iterator();
				while(iterator.hasNext()){
					Long date = (Long) iterator.next();
					dateArray[idx++] = date.longValue();
				}
				return dateArray;
			}
			else{
				return new long[0];
			}
		}
		return null;
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#addIntHeader(java.lang.String name, int value)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 name および value を保持します。
	 * 保持した情報は、以下のメソッドによってアプリケーションに返されます。
	 * <ou>
	 * <li>{@link #getIntHeader(java.lang.String)}
	 * <li>{@link #getIntHeaders(java.lang.String)}
	 * <li>{@link #getHeader(java.lang.String)}
	 * <li>{@link #getHeaders(java.lang.String)}
	 * </ou>
	 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
	 * @see javax.servlet.http.HttpServletResponseWrapper#addIntHeader(java.lang.String, int)
	 */
	public void addIntHeader(String name, int value) {
		super.addIntHeader(name, value);
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection == null){
			collection = new ArrayList();
			this.headerMap.put(name, collection);
		}
		collection.add(new Integer(value));
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String name, int value)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 name および value を保持します。
	 * 保持した情報は、以下のメソッドによってアプリケーションに返されます。
	 * <ou>
	 * <li>{@link #getIntHeader(java.lang.String)}
	 * <li>{@link #getIntHeaders(java.lang.String)}
	 * <li>{@link #getHeader(java.lang.String)}
	 * <li>{@link #getHeaders(java.lang.String)}
	 * </ou>
	 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
	 * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)
	 */
	public void setIntHeader(String name, int value) {
		super.setIntHeader(name, value);
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection == null){
			collection = new ArrayList();
			this.headerMap.put(name, collection);
		}
		else{
			collection.clear();
		}
		collection.add(new Integer(value));
	}

	/**
	 * 指定された名称でレスポンスヘッダに設定されている数値を返します。
	 * このヘッダに対して数値を未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている整数値
	 */
	public Integer getIntHeader(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			Iterator iterator = collection.iterator();
			while(iterator.hasNext()){
				Object value = iterator.next();
				if(value instanceof Integer){
					return (Integer) value;
				}
			}
		}
		return null;
	}

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての数値を返します。
	 * このヘッダに対して値を未設定の場合、null を返します。
	 * このヘッダに対して数値を未設定の場合、空の配列を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての整数値
	 */
	public int[] getIntHeaders(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			Collection intCollection = new ArrayList();
			if(collection.size() > 0){
				Iterator iterator = collection.iterator();
				while(iterator.hasNext()){
					Object value = iterator.next();
					if(value instanceof Integer){
						intCollection.add(value);
					}
				}
			}

			int collectionSize = intCollection.size();
			if(collectionSize > 0){
				int[] intArray = new int[collectionSize];
				int idx = 0;
				Iterator iterator = intCollection.iterator();
				while(iterator.hasNext()){
					Integer value = (Integer) iterator.next();
					intArray[idx++] = value.intValue();
				}
				return intArray;
			}
			else{
				return new int[0];
			}
		}
		return null;
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String name, java.lang.String value)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 name および value を保持します。
	 * 保持した情報は、以下のメソッドによってアプリケーションに返されます。
	 * <ou>
	 * <li>{@link #getStringHeader(java.lang.String)}
	 * <li>{@link #getStringHeaders(java.lang.String)}
	 * <li>{@link #getHeader(java.lang.String)}
	 * <li>{@link #getHeaders(java.lang.String)}
	 * </ou>
	 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
	 * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String name, String value) {
		super.addHeader(name, value);
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection == null){
			collection = new ArrayList();
			this.headerMap.put(name, collection);
		}
		collection.add(value);
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String name, java.lang.String value)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 name および value を保持します。
	 * 保持した情報は、以下のメソッドによってアプリケーションに返されます。
	 * <ou>
	 * <li>{@link #getStringHeader(java.lang.String)}
	 * <li>{@link #getStringHeaders(java.lang.String)}
	 * <li>{@link #getHeader(java.lang.String)}
	 * <li>{@link #getHeaders(java.lang.String)}
	 * </ou>
	 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
	 * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
	 */
	public void setHeader(String name, String value) {
		super.setHeader(name, value);
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection == null){
			collection = new ArrayList();
			this.headerMap.put(name, collection);
		}
		else{
			collection.clear();
		}
		collection.add(value);
	}
	/**
	 * 指定された名称でレスポンスヘッダに設定されている文字列の値を返します。
	 * このヘッダに対して文字列の値を未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている文字列の値
	 */
	public String getStringHeader(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			Iterator iterator = collection.iterator();
			while(iterator.hasNext()){
				Object value = iterator.next();
				if(value instanceof String){
					return (String) value;
				}
			}
		}
		return null;
	}

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての文字列の値を返します。
	 * このヘッダに対して値を未設定の場合、null を返します。
	 * このヘッダに対して文字列の値を未設定の場合、空の配列を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての文字列の値
	 */
	public String[] getStringHeaders(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			Collection stringCollection = new ArrayList();
			if(collection.size() > 0){
				Iterator iterator = collection.iterator();
				while(iterator.hasNext()){
					Object value = iterator.next();
					if(value instanceof Integer){
						stringCollection.add(value);
					}
				}
			}

			int collectionSize = stringCollection.size();
			if(collectionSize > 0){
				return (String[]) stringCollection.toArray(new String[collectionSize]);
			}
			else{
				return new String[0];
			}
		}
		return null;
	}

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
	public Object getHeader(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			Iterator iterator = collection.iterator();
			if(iterator.hasNext()){
				return iterator.next();
			}
		}
		return null;
	}

	/**
	 * 指定された名称でレスポンスヘッダに設定されている全ての値を返します。
	 * 配列の各要素の型は、{@link #getHeader(String)}の返り値と同様です。
	 * このヘッダが未設定の場合、null を返します。
	 * @param name ヘッダの名称
	 * @return 割り当てられている全ての値
	 */
	public Object[] getHeaders(String name){
		Collection collection = (Collection) this.headerMap.get(name);
		if(collection != null){
			return collection.toArray();
		}
		return null;
	}

	/**
	 * @param sc ステータスコード
	 * @param sm ステータスメッセージ
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 * @deprecated
	 */
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, sm);
		this.statusCode = new Integer(sc);
	}

	/**
	 * このレスポンスのステータスコードを設定します。 <p>
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#setStatus(int)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 sc を保持します。
	 * 保持した情報は、{@link #getStatus()}によってアプリケーションに返されます。
	 * @param sc ステータスコード
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
	 */
	public void setStatus(int sc) {
		super.setStatus(sc);
		this.statusCode = new Integer(sc);
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#sendError(int, java.lang.String)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 sc を保持します。
	 * 保持した情報は、{@link #getStatus()}によってアプリケーションに返されます。
	 * @param sc ステータスコード
	 * @param msg 説明文
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	public void sendError(int sc, String msg) throws IOException {
		super.sendError(sc, msg);
		this.statusCode = new Integer(sc);
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#sendError(int)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 sc を保持します。
	 * 保持した情報は、{@link #getStatus()}によってアプリケーションに返されます。
	 * @param sc ステータスコード
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	public void sendError(int sc) throws IOException {
		super.sendError(sc);
		this.statusCode = new Integer(sc);
	}

	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.http.HttpServletResponseWrapper#sendRedirect(java.lang.String)}
	 * メソッドをそのまま呼び出します。
	 * このメソッドでは、ステータスコード
	 * {@link javax.servlet.http.HttpServletResponse#SC_MOVED_TEMPORARILY}
	 * が指定されたと判断し、このオブジェクト内に
	 * {@link javax.servlet.http.HttpServletResponse#SC_MOVED_TEMPORARILY}
	 * を保持します。
	 * 保持した情報は、{@link #getStatus()}によってアプリケーションに返されます。
	 * @param location リダイレクト先のURL
	 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
	 */
	public void sendRedirect(String location) throws IOException {
		super.sendRedirect(location);
		this.statusCode = new Integer(HttpServletResponse.SC_MOVED_TEMPORARILY);
	}

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
	public Integer getStatus(){
		return this.statusCode;
	}

	/**
	 * レスポンスのメッセージボディ部分の長さをセットします。<p>
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.ServletResponseWrapper#setContentLength(int)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 len を保持します。
	 * 保持した情報は、{@link #getContentLength()}によってアプリケーションに返されます。
	 * @param len クライアントに送り返すメッセージボディの長さを指定する整数
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(int len) {
		super.setContentLength(len);
		this.contentLength = new Integer(len);
	}

	/**
	 * レスポンスにセットされている
	 * メッセージボディ部の長さを返します。<br>
	 * このメソッドは、{@link javax.servlet.ServletResponse#setContentLength(int len)}
	 * によってセットされた長さを返します。
	 * 値を未設定の場合、null を返します。
	 * @return クライアントに送り返すメッセージボディの長さを指定する整数値。 HTTP の Content-Length ヘッダフィールドの値
	 */
	public Integer getContentLength() {
		return this.contentLength;
	}

	/**
	 * クライアントに送り返されるレスポンスのコンテントタイプをセットします。 <p>
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に引数 type を保持します。
	 * 保持した情報は、{@link #getContentType()}によってアプリケーションに返されます。
	 * @param type コンテントタイプを指定する String
	 * @throws IllegalArgumentException type の構文解析に失敗した場合
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public void setContentType(String type) {
		try{
			this.setContentType(new ContentType(type));
		}
		catch(ParseException pe){
			IllegalArgumentException iae = new IllegalArgumentException();
			iae.initCause(pe);
			throw iae;
		}
	}

	/**
	 * レスポンスにセットされている
	 * クライアントに送り返されるレスポンスのコンテントタイプを
	 * 返します。
	 * @return コンテントタイプを指定する ContentType
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public ContentType getContentTypeObject(){
		return this.contentType;
	}

	/**
	 * このメソッドは、
	 * type の
	 * {@link javax.mail.internet.ContentType#toString()} が返す値を
	 * ラップしている
	 * {@link javax.servlet.ServletResponse} にセットします。
	 * @param type コンテントタイプを指定する ContentType
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public void setContentType(ContentType type){
		super.setContentType(type.toString());
		this.contentType = type;
	}


	/**
	 * このメソッドは、スーパークラスの
	 * {@link javax.servlet.ServletResponseWrapper#reset()}
	 * メソッドをそのまま呼び出します。
	 * その後、このオブジェクト内に保持している各データをクリアし、初期状態に戻します。
	 * @throws IllegalStateException レスポンスがすでにコミットされている場合
	 * @see javax.servlet.ServletResponse#reset()
	 */
	public void reset(){
		super.reset();
		this.crearMemories();
	}

	/**
	 * このインスタンスが保持している情報を、すべてクリアします。
	 */
	private void crearMemories(){
		this.cookieCollection.clear();
		this.headerMap.clear();
		this.statusCode = null;
		this.contentLength = null;
		this.contentType = null;
	}
}
