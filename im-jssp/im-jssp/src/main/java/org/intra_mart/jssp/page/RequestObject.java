package org.intra_mart.jssp.page;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


import org.intra_mart.common.aid.jdk.util.charset.CharacterSetManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestMessageBodyFilter;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter;
import org.intra_mart.jssp.util.RuntimeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * <!-- JavaScriptオブジェクト -->
 * リクエストオブジェクト。<BR/>
 * <BR/>
 * クライアントからのリクエスト情報を保持するオブジェクトです。<BR/>
 * このオブジェクトは、ブラウザからの要求があるたびに生成され、
 * ファンクション・コンテナの init() 関数や action に対するバインド関数の実行時に関数引数として渡されます。<br>
 * <br/>
 * URL引数を取得する場合は、<strong>request.<i>URL引数名</i></strong>で取得できます。<br>
 * (javax.servlet.ServletRequest#getParameterValue(String name)と動作は同じです。)
 * 
 * @name Request
 * @scope public
 */
public class RequestObject extends ScriptableObject implements java.io.Serializable{

	private static final ScriptableObject PROTOTYPE = new RequestObject();
	private static final int ATTR = ScriptableObject.EMPTY;

	private HttpServletRequest httpRequest;

	/**
	 * プロトタイプ・オブジェクト用
	 */
	public RequestObject(){
		// 基本メソッドの登録
		try{
			String[] names = {
			                  "get",				// 互換
			                  "names",				// 互換
			                  "query",				// 互換
			                  "getParameter",
			                  "getParameterNames",
			                  "getParameterValue",
			                  "getParameterValues",
			                  "getHeader",
			                  "getHeaders",
			                  "getHeaderNames",
			                  "getMethod",
			                  "getCookie",
			                  "getCookies",
			                  "getCookieNames",
			                  "getQueryString",
			                  "getMessageBody",
			                  "getMessageBodyAsString",
			                  "getMessageBodyAsStream",
			                  "getContentLength",
			                  "getContentType",
			                  "getAttribute",
			                  "getAttributeNames",
			                  "removeAttribute",
			                  "setAttribute",
			                  "toString"
			                  };
			this.defineFunctionProperties(names, RequestObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			IllegalStateException ise = new IllegalStateException("JavaScript-API <Request> initialize error.");
			ise.initCause(e);
			throw ise;
		}
	}


	/**
	 * @param httpRequest クライアントからのリクエスト
	 */
	public RequestObject(HttpServletRequest httpRequest){
		
		this.httpRequest = httpRequest;

		// request オブジェクトの生成
		try{
			Set<String> nameSet = new HashSet<String>();		// ファイルアップロードの一覧
			RequestParameter[] requestParameters = RequestMessageBodyFilter.getRequestParameters();
			if(requestParameters != null){
				for(int idx = 0; idx < requestParameters.length; idx++){
					String headerValue = requestParameters[idx].getHeader("Content-Disposition");
					if(headerValue != null){
						if(headerValue.indexOf("filename=") != -1){
							nameSet.add(requestParameters[idx].getName());
						}
					}
				}
			}

			Enumeration enumeration = httpRequest.getParameterNames();
			while(enumeration.hasMoreElements()){
				String name = (String) enumeration.nextElement();
				if(! nameSet.contains(name)){
					this.defineProperty(name, this.httpRequest.getParameter(name), ATTR);
				}
			}
		}
		catch(Exception e){
			IllegalStateException ise = new IllegalStateException("JavaScript-API <Request> construction error.");
			ise.initCause(e);
			throw ise;
		}

		// 基本メソッドの追加登録
		this.setPrototype(PROTOTYPE);
	}

	/**
	 * ＵＲＬ引数を連結した文字列を返却します。
	 * 
	 * @return ＵＲＬ引数を連結した文字列
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.httpRequest.getParameterMap().toString();
	}

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName() {
		return "HTTPRequest";
	}

	/**
	 * URL 引数文字列の取得します。<BR>
	 * <BR>
	 * 返却値はクライアントより受信した状態の URL 引数文字列です。<br>
	 * POST or GET どちらのリクエストでも引数を受け取る事が可能です<br>
	 * 
	 * @scope private
	 * @deprecated Request.getMessageBodyAsStream()およびgetQueryString()に置き換えられました。
	 * @return String クライアントから受け取った URL 引数文字列
	 */
	public String query() throws IOException{
		if(this.httpRequest.getMethod().equals("POST")){
			return this.getMessageBodyAsStream();
		}
		else{
			return this.getQueryString();
		}
	}

	/**
	 * 指定のキーに該当する URL 引数データ群を返却します。<BR>
	 * <BR>
	 * キーは、フォームのコントロール名称(name 属性)を指定します。<br>
	 * 返却値は指定キーに該当するデータ(文字列)の個数と同等の要素を持つ配列です。<br>
	 * 指定キーに該当する URL 引数が１つも存在しない場合は空の配列を返却します。<br>
	 * 
	 * @scope private
	 * @deprecated Request.getParameterValues()に置き換えられました。
	 * @param key String URL 引数設定名称
	 * @return Array URL 引数データの配列
	 */
	public Scriptable get(String name){
		String[] values = this.httpRequest.getParameterValues(name);

		// 指定の引数の有無の確認
		if(values != null){
			// 型の変換
			Object[] objects = new Object[values.length];
			for(int idx = values.length - 1; idx >= 0; idx--){
				objects[idx] = values[idx];
			}

			// 引数配列の返却
			return RuntimeObject.newArrayInstance(objects);
		}

		// 空配列の返却
		return RuntimeObject.newArrayInstance();
	}

	/**
	 * 指定のキーに該当する URL 引数データ群を返却します。<BR>
	 * <BR>
	 * キーは、フォームのコントロール名称(name 属性)を指定します。<br>
	 * 返却値は指定キーに該当するデータ(文字列)の個数と同等の要素を持つ配列です。<br>
	 * 指定キーに該当する URL 引数が１つも存在しない場合は空の配列を返却します。<br>
	 * 
	 * @scope public
	 * @param key String URL 引数設定名称
	 * @return Array URL 引数データの配列
	 */
	public Scriptable getParameterValues(String name){
		String[] values = this.httpRequest.getParameterValues(name);

		// 指定の引数の有無の確認
		if(values != null){
			// 引数配列の返却
			return RuntimeObject.newArrayInstance(values);
		}

		// 空配列の返却
		return RuntimeObject.newArrayInstance();
	}

	/**
	 * 指定のキーに該当する URL 引数データを返却します。<BR>
	 * <BR>
	 * キーは、フォームのコントロール名称(name 属性)を指定します。<br>
	 * 返却値は指定キーに該当するデータ(文字列)です。<br>
	 * 該当するデータが配列の場合は、先頭のデータを返却します。<br>
	 * 指定キーに該当する URL 引数が１つも存在しない場合はnullを返却します。<br>
	 * 
	 * @scope public
	 * @param key String URL 引数設定名称
	 * @return String URL 引数データ
	 */
	public String getParameterValue(String name){
		return this.httpRequest.getParameter(name);
	}

	/**
	 * すべてのリクエストパラメータ名を返します。
	 * @deprecated {@link #getParameterNames()}に置き換えられました。
	 * @return
	 */
	public Scriptable names(){
		return this.getParameterNames();
	}

	/**
	 * 指定されたリクエストヘッダの値をStringとして返します。<br>
	 * <br>
	 * 指定した名前を持つヘッダが複数ある場合は、先頭のヘッダを返却します。<br>
	 * 指定した名前を持つヘッダが存在しない場合はnullを返却します。<br>
	 * 
	 * @scope public
	 * @param name String ヘッダ名を表すString
	 * @return String 要求したヘッダの値を持つString
	 */
	public String getHeader(String name){
		return this.httpRequest.getHeader(name);
	}

	/**
	 * 指定されたリクエストヘッダの全ての値をStringの配列として返します。<br>
	 * <br>
	 * このリクエストが指定された名前のヘッダを持たない場合、 空の配列を返します。 <br>
	 * コンテナがヘッダ情報へのアクセスを禁止している場合、 nullを返します。
	 * 
	 * @scope public
	 * @param name String ヘッダ名を表すString
	 * @return Array リクエストヘッダの値を持つ配列
	 * @since 5.0
	 */
	public Scriptable getHeaders(String name){
		Enumeration enumeration = this.httpRequest.getHeaders(name);
		if(enumeration != null){
			Collection<String> collection = new ArrayList<String>();
			while(enumeration.hasMoreElements()){
				collection.add((String) enumeration.nextElement());
			}
			return RuntimeObject.newArrayInstance(collection.toArray());
		}
		else{
			return null;
		}
	}

	/**
	 * このリクエストに含まれる全てのヘッダ名の配列を返します。<br>
	 * <br>
	 * リクエストがヘッダを1つも持たない場合は空の配列を返します。<br>
	 * Servletがこのメソッドを用いることを Servletコンテナが禁止している場合はnullを返却します。<br>
	 * 
	 * @scope public
	 * @return Array このリクエストとともに送信された全てのヘッダの名前の配列
	 */
	public Scriptable getHeaderNames(){
		Enumeration enumeration = this.httpRequest.getHeaderNames();
		if(enumeration != null){
			Collection<String> collection = new HashSet<String>();
			while(enumeration.hasMoreElements()){
				collection.add((String) enumeration.nextElement());
			}
			return RuntimeObject.newArrayInstance(collection.toArray());
		}
		else{
			return null;
		}
	}

	/**
	 * このリクエストを生成したHTTPメソッドの名前を返します。<br>
	 * <br>
	 * 例えばGET、POST、PUTのような、HTTPメソッドの名前です。<br>
	 * 
	 * @scope public
	 * @return String このリクエストを生成したメソッドの名前を表すString
	 */
	public String getMethod(){
		return this.httpRequest.getMethod();
	}

	/**
	 * 指定の名前を持つクッキーの値を返します。<br>
	 * <br>
	 * 指定の名前を持つクッキーの値が複数あった場合、その中のどれかが返されます。<br>
	 * 指定の名前をもつクッキーの値が存在しない場合、nullを返します。<br>
	 * リクエストに Cookie が付加されていない場合も null を返します。<br>
	 * 
	 * @scope public
	 * @param name String クッキーの名前を表すString
	 * @return String 指定された名前をもつ値を表すString
	 */
	public String getCookie(String name){
		Cookie[] cookies = this.httpRequest.getCookies();
		if(cookies != null){
			for(int idx = 0; idx < cookies.length; idx++){
				if(cookies[idx].getName().equals(name)){
					return cookies[idx].getValue();
				}
			}
		}

		return null;		// ヒットなし
	}

	/**
	 * 指定の名前を持つすべてのクッキーの値の配列を返します。<br>
	 * <br>
	 * 指定の名前をもつクッキーの値が存在しない場合、空の配列を返します。<br>
	 * リクエストに Cookie が付加されていない場合は null を返します。<br>
	 * 
	 * @scope public
	 * @param name String クッキーの名前を表すString
	 * @return Array 指定された名前をもつ値の配列
	 */
	public Scriptable getCookies(String name){
		Cookie[] cookies = this.httpRequest.getCookies();
		if(cookies != null){
			Collection<String> collection = new ArrayList<String>();
			for(int idx = 0; idx < cookies.length; idx++){
				if(cookies[idx].getName().equals(name)){
					collection.add(cookies[idx].getValue());
				}
			}
			return RuntimeObject.newArrayInstance(collection.toArray());
		}

		return null;		// ヒットなし
	}

	/**
	 * すべてのクッキーの名前の配列を返します。<br>
	 * <br>
	 * クッキーが一つも存在しない場合、空の配列を返します。<br>
	 * リクエストに Cookie が付加されていない場合は null を返します。<br>
	 * 
	 * @scope public
	 * @return Array 指定された名前をもつ値の配列
	 */
	public Scriptable getCookieNames(){
		Cookie[] cookies = this.httpRequest.getCookies();
		if(cookies != null){
			Collection<String> collection = new HashSet<String>();
			for(int idx = 0; idx < cookies.length; idx++){
				collection.add(cookies[idx].getName());
			}
			return RuntimeObject.newArrayInstance(collection.toArray());
		}

		return null;		// ヒットなし
	}

	/**
	 * リクエストされたURLのパスの後ろに含まれているクエリ文字列を返します。<br>
	 * <br>
	 * URLがクエリ文字列を持っていない場合はnullを返します。<br>
	 * 値はコンテナによってデコードされません。<br>
	 * この値はCGI変数のQUERY_STRINGの値と同じです。<br>
	 * 
	 * @scope public
	 * @return String クエリ文字列を表すString
	 */
	public String getQueryString(){
		return this.httpRequest.getQueryString();
	}

	/**
	 * メッセージボディに含まれているバイナリデータをStringとして返します。<br>
	 * <br>
	 * 返されるデータは、指定の文字エンコーディング名によってUnicode に変換されています。<br>
	 * 
	 * @scope public
	 * @param enc String エンコーディング名
	 * @return String メッセージボディ部を表すString
	 */
	public String getMessageBody(String enc) throws IOException{
		InputStream in = RequestMessageBodyFilter.getInputStream();
		if(in == null){ in = this.httpRequest.getInputStream(); }

		if(in != null){
			Reader reader = new InputStreamReader(in, CharacterSetManager.toJDKName(enc));
			CharArrayWriter out = new CharArrayWriter();
			while(true){
				int c = reader.read();
				if(c != -1){
					out.write(c);
				}
				else{
					break;
				}
			}
			return out.toString();
		}
		else{
			return null;
		}
	}

	/**
	 * メッセージボディに含まれているデータをStringとして返します。<br>
	 * <br>
	 * データは、ServletRequest のエンコーディング名によって Unicode に変換します。<br>
	 * 
	 * @scope public
	 * @return String メッセージボディ部を表すString
	 */
	public String getMessageBodyAsString() throws IOException{
		return this.getMessageBody(this.httpRequest.getCharacterEncoding());
	}

	/**
	 * メッセージボディに含まれているバイナリデータをStringとして返します。<br>
	 * <br>
	 * 文字コード変換されません。バイナリデータ１バイトが１文字となります。<br>
	 * 
	 * @scope public
	 * @return String メッセージボディ部を表すString
	 * @throws IOException
	 */
	public String getMessageBodyAsStream() throws IOException{
		InputStream in = RequestMessageBodyFilter.getInputStream();
		if(in == null){ in = this.httpRequest.getInputStream(); }

		if(in != null){
			CharArrayWriter out = new CharArrayWriter();
			while(true){
				int c = in.read();
				if(c != -1){
					out.write(c);
				}
				else{
					break;
				}
			}
			return out.toString();
		}
		else{
			return null;
		}
	}

	/**
	 * すべてのリクエストパラメータ名を返します。<br>
	 * <br>
	 * 配列の各要素は、パラメータ名を表す String です。<br>
	 * 
	 * @scope public
	 * @return String パラメータ名の配列
	 */
	public Scriptable getParameterNames(){
		// 引数配列の返却
		return RuntimeObject.newArrayInstance(this.httpRequest.getParameterMap().keySet().toArray());
	}

	/**
	 * 指定の名前をもつリクエストパラメータを返します。<br>
	 * <br>
	 * @scope public
	 * @param name String パラメータ名
	 * @return RequestParameter パラメータ情報をもつオブジェクト
	 */
	public RequestParameterObject getParameter(String name) throws IOException{
		RequestParameter[] requestParameters = RequestMessageBodyFilter.getRequestParameters();
		if(requestParameters != null){
			for(int idx = 0; idx < requestParameters.length; idx++){
				if(requestParameters[idx].getName().equals(name)){
					return new RequestParameterObject(requestParameters[idx]);
				}
			}
		}

		String value = this.httpRequest.getParameter(name);
		if(value != null){
			return new RequestParameterObject(name, value, this.httpRequest);
		}
		else{
			return null;
		}
	}

	/**
	 * リクエストコンテンツの長さを取得します。<br>
	 * <br>
	 * リクエストのメッセージボディ、あるいは、入力ストリームから読み込めるバイト長を返しますが、長さがわからない場合は -1 を返します。<br>
	 * HTTP Servlet では CGI 環境変数 CONTENT_LENGTH の値に相当します。<br>
	 * 
	 * @scope public
	 * @return Number リクエストのメッセージボディの長さを表す整数値
	 */
	public int getContentLength(){
		return this.httpRequest.getContentLength();
	}

	/**
	 * リクエストコンテンツタイプを取得します。<br>
	 * <br>
	 * リクエストに含まれるメッセージボディの MIME タイプを返しますが、タイプがわからない場合は null を返します。<br>
	 * HTTP Servlet では CGI 環境変数の CONTENT_TYPE の値に相当します。<br>
	 * 
	 * @scope public
	 * @return String リクエストの MIME タイプの名前を含む String
	 */
	public String getContentType(){
		return this.httpRequest.getContentType();
	}

	/**
	 * 指定された名前の属性値を返します。<br>
	 * <br>
	 * 指定された名前が存在しない場合は、null を返します。<br>
	 * 
	 * @scope public
	 * @param  name String 属性の名前を指定する String
	 * @return Object 属性の値が含まれている Object。 指定された名前の属性が無い場合は null
	 */
	public Object getAttribute(String name, Object o){
		Object value = this.httpRequest.getAttribute(name);
		if(value != null){
			return value;
		}
		else{
			if(o instanceof Undefined){ return null; } else{ return o; }
		}
	}

	/**
	 * このリクエストで利用可能な属性名を含む配列型のオブジェクトを返します。<br>
	 * <br>
	 * @scope public
	 * @return Array リクエストに付随する属性名を含む文字列からなる配列
	 */
	public Scriptable getAttributeNames(){
		Collection<String> collection = new ArrayList<String>();
		Enumeration enumeration = this.httpRequest.getAttributeNames();
		while(enumeration.hasMoreElements()){
			collection.add((String) enumeration.nextElement());
		}

		return RuntimeObject.newArrayInstance(collection.toArray());
	}

	/**
	 * このリクエストから属性を削除します。<br>
	 * <br>
	 * 属性はリクエストがコンテナ内で処理されている間だけ維持されるにすぎないので、 通常、このメソッドは必要ありません。<br>
	 * 
	 * @scope public
	 * @param name String 削除する属性の名前を示す String
	 */
	public void removeAttribute(String name){
		this.httpRequest.removeAttribute(name);
	}

	/**
	 * このリクエストに属性をセットします。<br>
	 * <br>
	 * @scope public
	 * @param name String 属性名を示す String
	 * @param object Object セットするObject
	 */
	public void setAttribute(String name, Object o){
		this.httpRequest.setAttribute(name, o);
	}
}
