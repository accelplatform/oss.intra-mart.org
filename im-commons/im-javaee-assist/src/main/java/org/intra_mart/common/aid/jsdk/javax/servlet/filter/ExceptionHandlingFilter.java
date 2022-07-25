package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.common.aid.jsdk.javax.servlet.exception.ExtendedServletException;
import org.intra_mart.common.platform.log.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * サーブレットの実行例外をキャッチして、
 * 指定のエラーページへフォワードするフィルタです。<p>
 * このフィルタを利用すると、例外の種類によって表示するエラーページを
 * 切り替える事ができます。また、フォワード先ＵＲＬを JSP や Servlet にすると、
 * エラーログを出力するなどの例外処理をすることもできます。<p>
 * フィルタの設定値は、以下のとおりです。<br>
 * <table>
 * <tr>
 * <th>id.attribute.exception</th>
 * <td>
 * フォワード先へ、発生した例外を通知するためのキー名です。
 * この指定値をキー名として、発生した例外を ServletRequest に
 * #setAttribute() してから、エラーページへフォワードします。
 * </td>
 * </tr>
 * <tr>
 * <th>id.attribute.url</th>
 * <td>
 * フォワード先へ、リクエストＵＲＬを通知するためのキー名です。
 * この指定値をキー名として、
 * 現在の ServletRequest が #getRequestURL() の結果として返す値を
 * ServletRequest に #setAttribute() してから、エラーページへフォワードします。
 * </td>
 * </tr>
 * <tr>
 * <th>id.attribute.uri</th>
 * <td>
 * フォワード先へ、リクエストＵＲＩを通知するためのキー名です。
 * この指定値をキー名として、
 * 現在の ServletRequest が #getRequestURI() の結果として返す値を
 * ServletRequest に #setAttribute() してから、エラーページへフォワードします。
 * </td>
 * </tr>
 * <tr>
 * <th>id.attribute.servlet</th>
 * <td>
 * フォワード先へ、サーブレットパスを通知するためのキー名です。
 * この指定値をキー名として、
 * 現在の ServletRequest が #getServletPath() の結果として返す値を
 * ServletRequest に #setAttribute() してから、エラーページへフォワードします。
 * </td>
 * </tr>
 * <tr>
 * <th>path.mapping</th>
 * <td>
 * マッピングに関して定義した定義ファイルです。
 * 定義ファイルはＸＭＬ形式で、以下のフォーマットになります。<p>
 * <pre>
 *   &lt;error-page&gt;
 *     &lt;mapping&gt;
 *       &lt;exception-type&gt;例外のクラス名&lt;/exception-type&gt;
 *       &lt;location&gt;例外発生時にフォワードするパス&lt;/location&gt;
 *     &lt;/mapping&gt;
 *   &lt;/error-page&gt;
 * </pre>
 * &lt;mapping&gt; は任意の個数だけ定義できます。
 * <p>
 * フィルタは、指定されたパスをクラスパスから検索します。
 * 定義ファイルは、必ずクラスパスに設定されているディレクトリに
 * 保存してください。
 * </td>
 * </tr>
 * <tr>
 * <th>exception.cause.enable</th>
 * <td>
 * 例外がスローされた直接原因となった例外
 *（{@link java.lang.Throwable#getCause()} が返す値）
 * もチェック対象とするかどうかの設定です。<p>
 * 文字列 'true' を設定した場合、ネストされた例外もチェックします。
 * ただし、パフォーマンスが悪化する可能性があります。<br>
 * 文字列 'false' を設定した場合、スローされた例外のみをチェック対象とします。
 * パフォーマンス面では有利ですが、パターンによっては
 * 適切なエラーページを表示できない恐れがあります。
 * </td>
 * </tr>
 * <table>
 *
 */
public class ExceptionHandlingFilter extends AbstractFilter{
	
	private static Logger _logger = Logger.getLogger();
	
	private static final String ID_ARGUMENT_FUMBLE_EXCEPTION = "id.fumble.exception";
	private static final String ID_ARGUMENT_FUMBLE_URL = "id.fumble.url";
	private static final String ID_ARGUMENT_FUMBLE_URI = "id.fumble.uri";
	private static final String ID_ARGUMENT_FUMBLE_SERVLET = "id.fumble.servlet";
	private static final String ID_ARGUMENT_MAPPING = "path.mapping";
	private static final String ID_ARGUMENT_ENABLE_ROOTCAUSE = "exception.cause.enable";

	/**
	 * 発生した例外をフォワード先へ通知するための属性キー名
	 * このキー名で、リクエスト(ServletRequest)の属性にセットします。
	 */
	private String attributeId4exception = "fumble.exception";
	/**
	 * リクエストされたＵＲＬをフォワード先へ通知するための属性キー名
	 * このキー名で、リクエスト(ServletRequest)の属性にセットします。
	 */
	private String attributeId4url = "fumble.url";
	/**
	 * リクエストされたＵＲＩをフォワード先へ通知するための属性キー名
	 * このキー名で、リクエスト(ServletRequest)の属性にセットします。
	 */
	private String attributeId4uri = "fumble.uri";
	/**
	 * リクエストされたサーブレットをフォワード先へ通知するための属性キー名
	 * このキー名で、リクエスト(ServletRequest)の属性にセットします。
	 */
	private String attributeId4servlet = "fumble.servlet";
	/**
	 * ネストされた例外を対象とするかどうかのフラグ
	 */
	private boolean enableRootCause = false;
	/**
	 * 例外クラスとフォワード先ＵＲＬのマッピング
	 */
	private Map forwardMap = new HashMap();
	/**
	 * エラーページへフォワードしない例外クラスのリスト
	 */
	private Set throwSet = new HashSet();

	/**
	 * 新しくフィルタオブジェクトを作成します。
	 */
	public ExceptionHandlingFilter() {
		super();
	}

	/**
	 * フィルタの初期化をします。<p>
	 * @throws ServletException 初期化エラー
	 */
	protected void handleInit()  throws ServletException {
		FilterConfig config = this.getFilterConfig();

		// ネストされた例外をチェックするかどうかの判定フラグ
		String isRouutCause = config.getInitParameter(ID_ARGUMENT_ENABLE_ROOTCAUSE);
		this.enableRootCause = Boolean.valueOf(isRouutCause).booleanValue();

		// フォワード先へ通知する各種データの属性キー名
		String arg4exception = config.getInitParameter(ID_ARGUMENT_FUMBLE_EXCEPTION);
		if(arg4exception != null){ this.attributeId4exception = arg4exception; }

		String arg4url = config.getInitParameter(ID_ARGUMENT_FUMBLE_URL);
		if(arg4url != null){ this.attributeId4url = arg4url; }

		String arg4uri = config.getInitParameter(ID_ARGUMENT_FUMBLE_URI);
		if(arg4uri != null){ this.attributeId4uri = arg4uri; }

		String arg4servlet = config.getInitParameter(ID_ARGUMENT_FUMBLE_SERVLET);
		if(arg4servlet != null){ this.attributeId4servlet = arg4servlet; }

		try{
			ClassLoader classLoader = ExceptionHandlingFilter.class.getClassLoader();

			// 例外とフォワードパスのマッピング定義を読み込む
			String resourcePath = config.getInitParameter(ID_ARGUMENT_MAPPING);
			InputStream in = this.getResourceAsStream(resourcePath);
			if(in == null){
				throw new FileNotFoundException("Configuration file is not found: name=" + resourcePath);
			}
			Document document = this.getDOMDocument(in);
			Element documentElement = document.getDocumentElement();
			if(! documentElement.getTagName().equals("error-page")){
				// 書式エラー
				throw new MissingResourceException("Document-Element <error-page> is not defined.", resourcePath, documentElement.getTagName());
			}

			// 各定義情報の解析
			NodeList nodeList = documentElement.getChildNodes();
			for(int idx = 0; idx < nodeList.getLength(); idx++){
				Node node = nodeList.item(idx);
				if(node.getNodeType() != Node.ELEMENT_NODE){ continue; }

				Element element = (Element) node;
				if(! element.getTagName().equals("mapping")){ continue; }

				// 定義内容の取得
				String className = this.getDOMParameter(element, "exception-type");
				if(className == null){
					throw new MissingResourceException("Exception class name is not defined.", resourcePath, "exception-type");
				}

				String location = this.getDOMParameter(element, "location");
				if(location == null){
					throw new MissingResourceException("Error page for " + className + " is not defined.", resourcePath, "location");
				}

				// マッピングを定義
				try{
					Class clazz = classLoader.loadClass(className);
					forwardMap.put(clazz, location);
				}
				catch(Exception e){
					_logger.error("Configuration error[Class not found]: " + className, e);
				}
			}
		}
		catch(Exception e){
			throw new ExtendedServletException("Filter initialize error: " + e.getMessage(), e);
		}
	}

	/**
	 * フィルタとして動作するロジック。
	 * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException{

		try{
			chain.doFilter(request, response);			// 実行
		}
		catch(Throwable t){
			this.adjustCause(t);
			this.log("Servlet runtime error: " + t.toString(), t);

			String path = this.getForwardPath(t);

			if(path != null){
				HttpServletRequest httpServletRequest = (HttpServletRequest) request;

				// フォワード先への通知データの設定
				request.setAttribute(this.attributeId4exception, t);
				request.setAttribute(this.attributeId4url, httpServletRequest.getRequestURL());
				request.setAttribute(this.attributeId4uri, httpServletRequest.getRequestURI());
				request.setAttribute(this.attributeId4servlet, httpServletRequest.getServletPath());

				// 互換用の設定
				request.setAttribute("org.intra_mart.data.request.fumble.request", new FumbleHttpServletRequestRequest(httpServletRequest));
				request.setAttribute("org.intra_mart.data.request.fumble.exception", t);

				// フォワード処理
				RequestDispatcher dispatcher = request.getRequestDispatcher(path);
				dispatcher.forward(request, response);
			}
			else{
				// フォワード先が定義されていないので例外をスロー
				if(t instanceof ServletException){
					throw (ServletException) t;
				}
				if(t instanceof RuntimeException){
					throw (RuntimeException) t;
				}
				if(t instanceof Error){
					throw (Error) t;
				}
				throw new ExtendedServletException("Servlet runtime error: " + t.toString(), t);
			}
		}
	}

	/**
	 * ServletException の原因例外を Throwable の原因例外として設定する。
	 * このメソッドでチェックする事によって、printStackTrace() 時に、
	 * 原因例外のスタックトレースも表示されるようになる。
	 * @param t 例外
	 */
	private void adjustCause(Throwable t){
		Throwable cause = t.getCause();
		if(cause != null){
			this.adjustCause(cause);
		}
		else{
			if(t instanceof ServletException){
				ServletException servletException = (ServletException) t;
				Throwable rootCause = servletException.getRootCause();
				if(rootCause != null){
					t.initCause(rootCause);
					this.adjustCause(rootCause);
				}
			}
		}
	}

	/**
	 * 指定の例外にマップされているフォワード先パス名を返します。
	 * @param t 例外
	 * @return マッピングされているパス
	 */
	private synchronized String getForwardPath(Throwable t){
		String path = (String) this.forwardMap.get(t.getClass());
		if(path != null){
			return path;
		}
		else{
			if(this.throwSet.contains(t.getClass())){
				return null;
			}
			else{
				// ネストされた例外のチェック
				if(this.enableRootCause){
					Throwable cause = t.getCause();
					if(cause != null){ return this.getForwardPath(cause); }
				}

				Iterator cursor = this.forwardMap.keySet().iterator();
				while(cursor.hasNext()){
					Class clazz = (Class) cursor.next();
					if(clazz.isInstance(t)){
						path = (String) this.forwardMap.get(clazz);
						if(path != null){
							this.forwardMap.put(t.getClass(), path);
							return path;		// 遷移先パスの決定
						}
					}
				}
				// マッピング中に該当なし
				this.throwSet.add(t.getClass());

				return null;		// 適合せず
			}
		}
	}

	/**
	 * 指定のリソースを返します。
	 * @param name リソース名
	 * @return リソースの入力ストリーム。見つからない場合は null。
	 */
	private InputStream getResourceAsStream(String name){
		// このクラスを読み込んだクラスローダを利用して検索
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(name);
		if(in != null){
			return in;
		}
		else{
			// 現在のスレッドのコンテキストクラスローダを利用して再検索
			Thread thread = Thread.currentThread();
			classLoader = thread.getContextClassLoader();
			return classLoader.getResourceAsStream(name);
		}
	}

	/**
	 * 指定の入力ストリームからＸＭＬソースを読み込んで DOM を作成します。
	 * @param in ＸＭＬソースの入力ストリーム
	 * @return 解析結果
	 */
	private Document getDOMDocument(InputStream in) throws ParserConfigurationException, IOException, SAXException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.parse(in);
	}

	/**
	 * 指定のパラメータ名にマッピングされているパラメータ値を返します。<p>
	 * パラメータは、以下の順に検索されます。<br>
	 * <ul>
	 * <li>対象の Element の属性
	 * <li>対象の Element の子ノード(Element)
	 * </ul>
	 * また、パラメータ値は前後の空白および改行等が除去されます。
	 * 従って、例えば設定値が空白のみの場合、空文字列が返されます。
	 * @param element 検索対象のノード
	 * @param name パラメータ名
	 * @return 指定のパラメータ名にマッピングされるパラメータ値。パラメータが存在しない場合は null。
	 */
	private String getDOMParameter(Element root, String name){
		String parameterValue = null;

		Attr nodeAttribute = root.getAttributeNode(name);
		if(nodeAttribute != null){
			// 属性値の取得
			parameterValue = nodeAttribute.getValue();
		}
		else{
			// 子ノードの検索
			NodeList nodeList = root.getChildNodes();
			for(int index = 0; index < nodeList.getLength(); index++){
				Node node = nodeList.item(index);
				if(node.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) node;
					if(element.getTagName().equals(name)){
						// 該当ノードを発見
						NodeList childNodes = element.getChildNodes();
						for(int idx = 0; idx < childNodes.getLength(); idx++){
							Node item = childNodes.item(idx);
							if(item.getNodeType() == Node.TEXT_NODE){
								parameterValue = item.getNodeValue();
								if(parameterValue != null){
									if(parameterValue.length() > 0){ break; }
								}
							}
						}
					}
				}

				if(parameterValue != null){ break; }	// 発見されたらしい
			}
		}

		// パラメータ値の不要な部分(前後の空白)を除去して返却
		if(parameterValue != null){
			String returnValue = parameterValue.trim();
			if(returnValue.length() > 0){
				return returnValue;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}


	private static class FumbleHttpServletRequestRequest extends HttpServletRequestWrapper{
		private String requestURI;
		private StringBuffer requestURL;
		private String servletPath;

		protected FumbleHttpServletRequestRequest(HttpServletRequest request){
			super(request);
			this.requestURI = request.getRequestURI();
			this.requestURL = request.getRequestURL();
			this.servletPath = request.getServletPath();
		}

		public String getRequestURI(){
			return this.requestURI;
		}

		public StringBuffer getRequestURL(){
			return this.requestURL;
		}

		public String getServletPath(){
			return this.servletPath;
		}
	}
}

