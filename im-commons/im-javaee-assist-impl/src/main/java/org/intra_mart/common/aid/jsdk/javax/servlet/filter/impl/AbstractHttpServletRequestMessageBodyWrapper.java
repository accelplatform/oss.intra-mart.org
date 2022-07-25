package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.intra_mart.common.aid.jsdk.javax.servlet.FilterServletInputStream;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletRequestMessageBodyWrapper;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter;


/**
 * HttpServletRequestWrapper を拡張した機能を提供します。
 *
 * <P>HttpServletRequestWrapper には定義されていないが、
 * 共通仕様として定義されていると便利なインタフェースを提供します。
 *
 */
public abstract class AbstractHttpServletRequestMessageBodyWrapper extends HttpServletRequestWrapper 
																	  implements HttpServletRequestMessageBodyWrapper{
	/**
	 * ラップしたリクエストオブジェクト
	 */
	private HttpServletRequest httpServletRequest;

	/**
	 * メッセージボディを読むための入力ストリーム
	 */
	private ServletInputStream inputStream4messageBody;

	/**
	 * メッセージボディを読むための入力ストリーム
	 */
	private BufferedReader bufferedReader4messageBody;

	/**
	 * RequestParameterのキャッシュ。<BR>
	 * RequestParameterが既に生成済みの場合は、この変数に格納されています。
	 */
	private Reference requestParameterReference  = null;

    /**
	 * 初期化パラメータ「parent.request.parameter」のboolean値<BR><BR>
	 * この値は、{@link #getRequestParameters()}内部の実行制御を行います。<BR>
	 * tureの場合、{@link #getRequest()} が返すリクエストの{@link #getParameterMap()}を返却値に追加します。<BR>
	 * falseの場合、上記処理を行いません。<BR>
	 */
	private boolean parentRequestParameter = false;

	/**
	 * 初期化パラメータ「parse.query.string」のboolean値<BR><BR>
	 * この値は、{@link #getRequestParameters()}内部の実行制御を行います。<BR>
	 * tureの場合、{@link #getRequestParameters()}実行時にクエリー文字列を解析し、
	 * キーと値のペアをリクエストパラメータとして付加します。<BR>
	 * falseの場合、上記処理を行いません。<BR>
	 */
	private boolean parseQueryString = false;

	
	/**
	 * HTTP からのリクエストオブジェクトを作成します。
	 * @param request 基礎となるリクエストオブジェクト
	 * @param parentRequestParameter 初期化パラメータ「parent.request.parameter」のboolean値
	 * @param parseQueryString 初期化パラメータ「parse.query.string」のboolean値
	 */
	public AbstractHttpServletRequestMessageBodyWrapper(HttpServletRequest request, 
														 boolean parentRequestParameter, 
														 boolean parseQueryString){
		super(request);
		this.httpServletRequest = request;
		this.parentRequestParameter = parentRequestParameter;
		this.parseQueryString = parseQueryString;
	}


// HttpServletRequestWrapper interface
	/**
	 * ServletInputStreamを使ってリクエストのメッセージボディに
	 * 含まれているバイナリデータを読み込むためのストリームを取得します。<p>
	 * このメソッドか getReader() のどちらか一つだけが実行されて
	 * メッセージボディを取得します。両方ではありません。
	 * @return リクエストのメッセージボディを含む ServletInputStream オブジェクト
	 * @throws IOException 入出力の例外が発生した場合
	 * @throws IllegalStateException このリクエストですでに getReader() メソッドが実行されている場合
	 */
	public ServletInputStream getInputStream() throws IOException {

		if (this.bufferedReader4messageBody == null) {
			if (this.inputStream4messageBody == null) {
				this.inputStream4messageBody = new FilterServletInputStream(this.getMessageBody());
			}
			return this.inputStream4messageBody;
		} else {
			throw new IllegalStateException("#getReader() is already called.");
		}
	}

	/**
	 * リクエストのメッセージボディを BufferedReader を使い、
	 * 文字データとして取り出します。<p>
	 * 文字データはメッセージボディと同じ文字エンコーディングに変換されます。
	 * <br>
	 * メッセージボディを読み込むにはこのメソッドか
	 * getInputStream() メソッドのどちらか一方を使います。
	 * 両方は使えません。
	 * @throws IOException 入出力の例外が発生した場合
	 * @throws IllegalStateException このリクエストですでに getInputStream() メソッドが実行されている場合
	 */
	public BufferedReader getReader() throws IOException {
		if (this.inputStream4messageBody == null) {
			if (this.bufferedReader4messageBody == null) {
				
				this.bufferedReader4messageBody = 
					new BufferedReader(new InputStreamReader(new FilterServletInputStream(this.getMessageBody()), 
															  this.getCharacterEncoding()));
			}
			return this.bufferedReader4messageBody;
		} else {
			throw new IllegalStateException("#getInputStream() is already called.");
		}
	}

	/**
	 *
	 * @param name a String specifying the name of the parameter
	 * @return a String representing the single value of the parameter
	 */
	public String getParameter(String name){
		try{
			RequestParameter[] requestParameters = this.getRequestParameters();
			for(int idx = 0; idx < requestParameters.length; idx++){
				if(requestParameters[idx].getName().equals(name)){
					return requestParameters[idx].getValue();
				}
			}
		}
		catch(IOException ioe){
			IllegalStateException ise = new IllegalStateException("Illegal Request-Parameter; searching " + name);
			ise.initCause(ioe);
			throw ise;
		}
		return null;
	}

	/**
	 *
	 * @return an Enumeration of String  objects, each String containing the name of a request parameter; 
	 * or an empty Enumeration if the request has no parameters
	 */
	public Enumeration getParameterNames(){
		try{
			Vector collection = new Vector();
			RequestParameter[] requestParameters = this.getRequestParameters();
			for(int idx = 0; idx < requestParameters.length; idx++){
				collection.add(requestParameters[idx].getName());
			}
			return collection.elements();
		}
		catch(IOException ioe){
			IllegalStateException ise = new IllegalStateException("Illegal Request-Parameter; searching Parameter-Names.");
			ise.initCause(ioe);
			throw ise;
		}
	}

	/**
	 *
	 * @param name a String containing the name of the parameter whose value is requested
	 * @return an array of String objects containing the parameter's values
	 */
	public String[] getParameterValues(String name){
		try{
			Collection collection = new ArrayList();
			RequestParameter[] requestParameters = this.getRequestParameters();
			for(int idx = 0; idx < requestParameters.length; idx++){
				if(requestParameters[idx].getName().equals(name)){
					collection.add(requestParameters[idx].getValue());
				}
			}
			if(collection.isEmpty()){
				return null;
			}
			else{
				return (String[]) collection.toArray(new String[collection.size()]);
			}
		}
		catch(IOException ioe){
			IllegalStateException ise = new IllegalStateException("Illegal Request-Parameter; searching " + name);
			ise.initCause(ioe);
			throw ise;
		}
	}

	/**
	 * @return an immutable java.util.Map containing parameter names as keys and parameter values as map values. 
	 * The keys in the parameter map are of type String. The values in the parameter map are of type String array.
	 */
	public Map getParameterMap(){
		// 返却値用のマップオブジェクトを作成
		return new HashMap(this.findParameterMap());
	}

	/**
	 * このリクエストのメッセージボディで使われている文字エンコーディング名を上書きします。
	 * @param env 文字エンコーディング名を含む String
	 */
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException{
		this.requestParameterReference = null;
		this.inputStream4messageBody = null;
		this.bufferedReader4messageBody = null;
		this.getRequest().setCharacterEncoding(env);
	}


//	 HttpServletRequestMessageBodyWrapper interface
	/**
	 * リクエストのメッセージボディを返します。<p>
	 * このメソッドは、{@link javax.servlet.ServletRequest#getInputStream() }
	 * または {@link javax.servlet.ServletRequest#getReader() } を
	 * 実行した後でも、メッセージボディを含む入力ストリームを返します。<br>
	 * また同様に、
	 * {@link javax.servlet.ServletRequest#getParameter(String name) }
	 * を実行した後でも、メッセージボディを含む入力ストリームを返します。
	 * @return メッセージボディを含む入力ストリーム
	 * @throws IOException 入出力の例外が発生した場合
	 */
	public abstract InputStream getMessageBody() throws IOException;

	/**
	 * パラメータ情報を返します。
	 *
	 * 初期化パラメータ「parent.request.parameter」がtrueに設定されている場合は、
	 * {@link #getRequestParameters(InputStream)}のリターン値に、
	 * {@link #getRequest()} が返すリクエストの{@link #getParameterMap()}の
	 * 結果を追加したものを返します。
	 *
	 * @return パラメータ
	 * @throws IOException 入出力エラー
	 */
	public RequestParameter[] getRequestParameters() throws IOException{

		// RequestParameterを既に生成済みの場合は、キャッシュを返却
		if(this.requestParameterReference != null){
			Object requestParameters = this.requestParameterReference.get();
			if(requestParameters != null){
				return (RequestParameter[]) requestParameters; 
			}
		}
		
		Collection collection = new ArrayList();

		// 初期化パラメータ「parent.request.parameter」がtrueの場合
		// 親オブジェクトの情報を追加する
		if(this.parentRequestParameter == true){

			try{
				Map map = this.getRequest().getParameterMap();
				Iterator iterator = map.entrySet().iterator();
				while(iterator.hasNext()){
					Map.Entry entry = (Map.Entry) iterator.next();
					String[] values = (String[]) entry.getValue();
					if(values != null){
						String key = (String) entry.getKey();
						for(int idx = 0; idx < values.length; idx++){
							collection.add(new RequestParameter4string(key, values[idx], this));
						}
					}
				}
			}
			catch(Exception e){
				// エラー発生→無視
			}

		} 
		
		// 初期化パラメータ「parse.query.string」がtrueの場合
		// クエリー文字列の内容を追加する
		if(this.parseQueryString == true){

			String qString = ( (HttpServletRequest)this.getRequest() ).getQueryString();
			
			if( qString != null ){
				StringTokenizer qStringTokenizer = new StringTokenizer(qString, "&");
				
				while( qStringTokenizer.hasMoreElements() ){
					String requestParameter = qStringTokenizer.nextToken();
					
					// 「key=value」「key=」および「=」が含まれない形式の場合のみ処理続行
					// (「=value」形式の場合はRequestParameterとして追加しない)
					if( requestParameter.indexOf("=") != 0 ) {
					
						String keyValueSet[] = requestParameter.split("=");
						
						// 「key=」の場合
						if( keyValueSet.length == 1 ){
							collection.add(new RequestParameter4string(keyValueSet[0], "", this));
						}
						// 「key=value」の場合
						else if( keyValueSet.length == 2 ){
							collection.add(new RequestParameter4string(keyValueSet[0], keyValueSet[1], this));
						}
					}

				}
			}			
				
		}

		// このオブジェクトの情報を取得
		InputStream in = this.getMessageBody();
		try{
			RequestParameter[] parameters = this.getRequestParameters(in);
			for(int idx = 0; idx < parameters.length; idx++){
				collection.add(parameters[idx]);
			}
		}
		finally{
			in.close();
		}

		RequestParameter[] requestParameters = 
			(RequestParameter[]) collection.toArray(new RequestParameter[collection.size()]);
		
		this.requestParameterReference = new SoftReference(requestParameters);
		return requestParameters;

	}

//	 Original interface
	/**
	 * このオブジェクトがラップしているリクエストオブジェクトを返します。
	 * @return リクエスト
	 */
	public HttpServletRequest getHttpServletRequest(){
		return this.httpServletRequest;
	}

	/**
	 * パラメータ情報を返します。<p>
	 * このメソッドは、{@link #getRequestParameters()} から呼び出されます。
	 * @param in 入力ストリーム
	 * @return パラメータ
	 * @throws IOException 入出力エラー
	 */
	protected abstract RequestParameter[] getRequestParameters(InputStream in) throws IOException;

	/**
	 * パラメータのマップを返します。
	 * 既に作成済みのものがあれば、それを返します。
	 * 初めて呼び出されたときは、#createParameterMap()を呼び出します。
	 * @return #getParameterMap() の仕様にあわせたマップ
	 * @throws RuntimeException パラメータの入力に失敗した場合
	 */
	private Map findParameterMap(){
		try{
			return this.createParameterMap();
		}
		catch(IOException ioe){
			throw new RuntimeException("Parameter input error.", ioe);
		}
	}

	/**
	 * パラメータのマップを作成して返します。
	 * @return #getParameterMap() の仕様にあわせたマップ
	 */
	private Map createParameterMap() throws IOException{
		
		Map map = new HashMap();

		// このオブジェクトの解釈するパラメータの取得
		RequestParameter[] parameters = this.getRequestParameters();

		// 文字コード変換とキーによるパラメータの集合化
		for(int idx = 0; idx < parameters.length; idx++){
			String name = parameters[idx].getName();
			
			// 格納済みリクエストパラメータのリストを取得
			List values = (List) map.get(name);

			if(values == null){
				List list = new ArrayList();
				list.add(parameters[idx].getValue());
				map.put(name, list);
			} else {
				values.add(parameters[idx].getValue());
			}
		}


		// 最終形態を保存するための器
		Map formattedMap = new HashMap();

		// マップのフォーマットを getParameterMap() メソッドの仕様にあわせる
		Iterator entries = map.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry entry = (Map.Entry) entries.next();
			Collection values = (Collection) entry.getValue();
			formattedMap.put(entry.getKey(), values.toArray(new String[values.size()]));
		}

		return formattedMap;

	}
}
