package org.intra_mart.jssp.script.api;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.javaee.http.CGIEnvironment;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HTTPContextHandlingFilter;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.common.aid.jsdk.utility.URLUtil;
import org.intra_mart.jssp.page.JSSPQuery;
import org.intra_mart.jssp.page.JSSPQueryManager;
import org.intra_mart.jssp.page.RequestObject;
import org.mozilla.javascript.ScriptableObject;

/**
 * Webクラス。
 * 
 * @scope public
 * @name Web
 */
public class WebObject extends ScriptableObject implements Serializable{
	
    
    /**
     * JSSP実行環境への登録用デフォルトコンストラクタ
     */
	public WebObject(){
		super();
	}

    /**
     * JSSP実行環境下でのオブジェクト名称を取得します
     *
     * @return String JSSP実行環境下でのオブジェクト名称
     */
	public String getClassName(){
		return "Web";
	}

	
	/**
	 * 現在処理中ページパスを取得します。
	 * @scope public
	 * @return String 処理中のページパス
	 */
	public static String jsStaticFunction_current(){
		JSSPQuery currentJSSPQuery = getCurrentJSSPQuery();
		return currentJSSPQuery.getNextEventPagePath();	
	}

	
	/**
	 * リクエストを要求したページパスを取得します。<br/>
	 * @scope public
	 * @return String リクエストを要求したページパス
	 */
	public static String jsStaticFunction_referer(){
		JSSPQuery currentJSSPQuery = getCurrentJSSPQuery();
		return currentJSSPQuery.getFromPagePath();
	}
	
	
	/**
	 * CGI 環境変数データを取得します。<br/>
	 * ＣＧＩ環境変数引数を取得<br/>
	 * 指定キーに該当するＣＧＩ環境変数が存在しない場合は null<br/>
	 * 
	 * @scope public
	 * @param key String ＣＧＩ環境変数の変数名
	 * @return String 環境変数データ
	 */
	public static String jsStaticFunction_getenv(String key){
		HttpServletRequest request = getCurrentRequest();		
		CGIEnvironment cgiEnvironment = new CGIEnvironment(request);
		return cgiEnvironment.getenv(key);
	
	}

	/**
	 * Ｗｅｂサーバ名を取得します。<br/>
	 * 
	 * @scope public
	 * @return String Ｗｅｂサーバ名
	 */
	public static String jsStaticFunction_host(){
		return jsStaticFunction_getServerName();
	}

	
	/**
	 * ＷｅｂサーバのＨＴＴＰポートを取得します。<br/>
	 * @scope public
	 * @return int ＷｅｂサーバのＨＴＴＰ待ち受けポート
	 */
	public static int jsStaticFunction_port(){
		return jsStaticFunction_getServerPort();
	}

	
	/**
	 * Ｗｅｂサーバのプロトコルを取得します。<br/>
	 * @scope public
	 * @return String Ｗｅｂサーバのプロトコル
	 */
	public static String jsStaticFunction_protocol(){
		return jsStaticFunction_getScheme();
	}

	
	/**
	 * Ｗｅｂサーバスクリプト名を取得します。<br/>
	 * @scope public
	 * @return String Ｗｅｂスクリプトファイル名
	 */
	public static String jsStaticFunction_script(){
		HttpServletRequest request = getCurrentRequest();
		return request.getServletPath();
	}

	/**
	 * WebサーバのベースURLを取得します。<br/>
	 * "http://server:port/path" を返却します。
	 * 
	 * @scope public
	 * @return String ベースURL
	 */
	public static String jsStaticFunction_base(){
		
		HttpServletRequest request = getCurrentRequest();

		try {
			URL url = URLUtil.getContextURL(request, false);
			return url.toExternalForm();
		}
		catch (MalformedURLException e) {
			return new String(request.getRequestURL());
		}

	}
	
	/**
	 * リクエストURLを取得します。<br/>
	 * @scope public
	 * @return String リクエストURL
	 */
	public static String jsStaticFunction_location(){
		HttpServletRequest request = getCurrentRequest();
		return new String(request.getRequestURL());
	}

	/**
	 * リクエストオブジェクトを返します。
	 * @scope public
	 * @return Request リクエストオブジェクト
	 */
	public static RequestObject jsStaticFunction_getRequest(){
		HttpServletRequest request = getCurrentRequest();
		return new RequestObject(request);
	}

	/**
	 * レスポンスオブジェクトを返します。
	 * @scope public
	 * @return HTTPResponse レスポンス
	 */
	public static HTTPResponseObject jsStaticFunction_getHTTPResponse(){
		HttpServletResponse response = getCurrentResponse();
		return new HTTPResponseObject(response);
	}


	/**
	 * HTTP レスポンスヘッダを設定します。<br/>
	 * 
	 * @scope public
	 * @param name String HTTP レスポンスヘッダの名称（文字列）
	 * @param value String 値（文字列）
	 */
	public static void jsStaticFunction_setHTTPResponseHeader(String name, String value){
		HTTPResponseObject response = jsStaticFunction_getHTTPResponse();
		response.setHeader(name, value);
	}

	/**
	 * 指定されたURLがセッションIDを含むようにエンコードします。
	 * @scope public
	 * @param url String エンコードするURL
	 * @return String エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public static String jsStaticFunction_encodeURL(String  url){
		HttpServletResponse response = getCurrentResponse();
		return response.encodeURL(url);
	}

	/**
	 * sendRedirectメソッドの中で使えるように、
	 * 指定されたURLをエンコードします。<br/>
	 * エンコードが不要である場合は指定されたURLをそのまま返します。
	 * このメソッドの実装はセッションIDを
	 * URLにエンコードするかどうかを決定するためのロジックを含みます。<br/>
	 * このメソッドがencodeURLメソッドとは別になっているのは、
	 * この決定をするための規則が通常のリンクをエンコードするかどうかを
	 * 決定する規則と異なることがあるからです。<br/>
	 * HttpServletResponse.sendRedirect メソッドに送られる全てのURLは
	 * このメソッドを通すべきです。<br/>
	 * そうでなければ、Cookieをサポートしないブラウザでは
	 *  URLリライティングの処理を行なうことができません。<br/>
	 * 
	 * @scope public
	 * @param url String エンコードするURL
	 * @return String エンコードが必要である場合、エンコードされたURL。 そうでない場合は与えられたそのままのURL
	 */
	public static String jsStaticFunction_encodeRedirectURL(String  url){
		HttpServletResponse response = getCurrentResponse();
		return response.encodeRedirectURL(url);
	}

	/**
	 * リクエストされたURIのうち、
	 * リクエストのコンテキストを指す部分を返します。<br/>
	 * コンテキストパスは通常リクエストURIの最初に来ます。
	 * コンテキストパスは "/" から始まりますが、"/" では終わりません。<br/>
	 * デフォルト（ルート）のコンテキストに属するServletの場合、
	 * このメソッドは "" を返します。<br/>
	 * コンテナはこの文字列をデコードしません。
	 * 
	 * @scope public
	 * @return String リクエストされたURIのうち、 リクエストのコンテキストを指す部分のString
	 */
	public static String jsStaticFunction_getContextPath(){
		HttpServletRequest request = getCurrentRequest();
		return request.getContextPath();
	}

	/**
	 * リクエストを送ってきたクライアントの
	 * IP(Internet Protocol) アドレスを返します。<br/>
	 * HTTP Servlet では CGI で使用される環境変数の REMOTE_ADDR と同等です。<br/>
	 * 
	 * @scope public
	 * @return String リクエストを送ってきたクライアントの IP アドレスを示す文字列
	 */
	public static String jsStaticFunction_getRemoteAddr(){
		HttpServletRequest request = getCurrentRequest();
		return request.getRemoteAddr();
	}

	/**
	 * リクエストを送ってきたクライアントの
	 * FQDN(Fully Qualified Domain Name: 完全修飾ドメイン名)を返します。<br/>
	 * コンテナがホスト名を解決できないか、
	 * (パフォーマンスを上げるため)解決しないように設定されている場合は
	 * IP アドレスをピリオドで区切った形式にして返します。<br/>
	 * HTTP Servlet では CGI で使用される環境変数の REMOTE_HOST と同等です。<br/>
	 * 
	 * @scope public
	 * @return String クライアントの FQDN(Fullu Qualified Domain Name) を示す String
	 */
	public static String jsStaticFunction_getRemoteHost(){
		HttpServletRequest request = getCurrentRequest();
		return request.getRemoteHost();
	}

	/**
	 * リクエストのプロトコル名とバージョンを HTTP/1.1 のように
	 * プロトコル名/メジャーバージョン番号.マイナーバージョン番号
	 * の形式で返します。<br/>
	 * HTTP Servlet ではこのメソッドで取得できる値は CGI で使用する環境変数の
	 * SERVER_PROTOCOL と同等です。<br/>
	 * 
	 * @scope public
	 * @return String プロトコル名とバージョンを示す
	 */
	public static String jsStaticFunction_getProtocol(){
		HttpServletRequest request = getCurrentRequest();
		return request.getProtocol();
	}

	/**
	 * 例えば http や https, ftp  のようなリクエストのスキームを返します。<br/>
	 * RFC1738 で詳説されているように、スキームが違うと URL の形式も
	 * 違ってきます。<br/>
	 * 
	 * @scope public
	 * @return String このリクエストに使われたスキームを示す文字列
	 */
	public static String jsStaticFunction_getScheme(){
		HttpServletRequest request = getCurrentRequest();
		return request.getScheme();
	}

	/**
	 * リクエストを受け取ったサーバのホスト名を返します。<br/>
	 * HTTP Servlet では CGI で使用する環境変数の SERVER_NAME と同等です。<br/>
	 * 
	 * @scope public
	 * @return String リクエストが送り込まれたサーバの名前を示す
	 */
	public static String jsStaticFunction_getServerName(){
		HttpServletRequest request = getCurrentRequest();
		return request.getServerName();
	}

	/**
	 * このリクエストを受け取るのに使われたポート番号を返します。<br/>
	 * HTTP Servlet では CGI で使用される環境変数の SERVER_PORT と同等です。<br/>
	 * 
	 * @scope public
	 * @return int ポート番号を示す整数値
	 */
	public static int jsStaticFunction_getServerPort(){
		HttpServletRequest request = getCurrentRequest();
		return request.getServerPort();
	}

	/**
	 * このリクエストが HTTPS のようなセキュアなチャネルを使って
	 * 送られたものかどうかを示す boolean  を返します。<br/>
	 * 
	 * @scope public
	 * @return Boolean リクエストがセキュアなチャネルを使って送られたものかどうかを示す boolean
	 */
	public static boolean jsStaticFunction_isSecure(){
		HttpServletRequest request = getCurrentRequest();
		return request.isSecure();
	}

	
	/**
	 * @return
	 */
	private static HttpServletRequest getCurrentRequest() {
		try{
			HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
			HttpServletRequest request = httpContext.getRequest();
			return request;
		}
		catch(NullPointerException npe){
			throw new NullPointerException("\"" + HTTPContextHandlingFilter.class.getName() + "\" is not set.");
		}
	}

	/**
	 * @return
	 */
	private static HttpServletResponse getCurrentResponse() {
		try{
			HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
			HttpServletResponse response = httpContext.getResponse();
			return response;
		}
		catch(NullPointerException npe){
			throw new NullPointerException("\"" + HTTPContextHandlingFilter.class.getName() + "\" is not set.");
		}
	}

	/**
	 * @return
	 */
	private static JSSPQuery getCurrentJSSPQuery() {
		JSSPQuery currentJSSPQuery = JSSPQueryManager.currentJSSPQuery();
		return currentJSSPQuery;
	}
}

