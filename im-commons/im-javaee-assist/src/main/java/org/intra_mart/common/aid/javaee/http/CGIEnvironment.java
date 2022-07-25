package org.intra_mart.common.aid.javaee.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * このクラスは、サーブレットクラス内で CGI 環境変数を扱えるように
 * 各パラメータを CGI 環境変数で定義されているキーワードにマップします。<br/>
 * ユーザー・プログラムでは、このクラスを利用する事でサーブレット環境で
 * CGI 環境変数インターフェースを実装する事が出来ます。<br/>
 *
 * @see javax.servlet.http.HttpServletRequest
 */
public class CGIEnvironment {

	private Map<String, String> cgiEnvironmentVariable = new HashMap<String, String>();
	
	/**
	 * HTTP リクエストヘッダおよび Web サーバ情報から
	 * CGI 環境変数情報を構築します。<br/>
	 *
	 * @param httpServletRequest サーブレット実行時に受け取るリクエストオブジェクト
	 * @exception NullPointerException 引数が null の場合
	 */
	public CGIEnvironment(HttpServletRequest httpServletRequest){
				
		// 環境変数の取得
		Enumeration keys = httpServletRequest.getHeaderNames();
		while(keys.hasMoreElements()){
			String key = (String) keys.nextElement();
			String val = httpServletRequest.getHeader(key);
			this.setenv(("HTTP_").concat(key.toUpperCase().replace('-', '_')), val);
		}
		this.setenv("AUTH_TYPE", httpServletRequest.getAuthType());
		this.setenv("CONTENT_TYPE", httpServletRequest.getContentType());
		this.setenv("REQUEST_METHOD", httpServletRequest.getMethod());
		this.setenv("PATH_INFO", httpServletRequest.getPathInfo());
		this.setenv("PATH_TRANSLATED", httpServletRequest.getPathTranslated());
		this.setenv("QUERY_STRING", httpServletRequest.getQueryString());
		this.setenv("REMOTE_USER", httpServletRequest.getRemoteUser());
		this.setenv("SCRIPT_NAME", httpServletRequest.getServletPath());
		this.setenv("CONTENT_LENGTH", String.valueOf(httpServletRequest.getContentLength()));
		this.setenv("SERVER_PROTOCOL", httpServletRequest.getProtocol());
		this.setenv("SERVER_NAME", httpServletRequest.getServerName());
		this.setenv("SERVER_PORT", String.valueOf(httpServletRequest.getServerPort()));
		this.setenv("REMOTE_ADDR", httpServletRequest.getRemoteAddr());
		this.setenv("REMOTE_HOST", httpServletRequest.getRemoteHost());
	}

	private Object setenv(String key, String value){
		if(key != null){
			if(value != null){
				return cgiEnvironmentVariable.put(key, value);
			}
			else{
				return cgiEnvironmentVariable.remove(key);
			}
		}
		else{
			return null;
		}
	}

	/**
	 * 値の取得。<br/>
	 * <br/>
	 * キーにマップされている値を返します。<br/>
	 * 引数 key には、ＣＧＩ環境変数の変数名を指定して下さい。<br/>
	 * 引数に指定されたＣＧＩ環境変数にマップされている値がない場合 null を返します。
	 *
	 * @param key 値にマップされているキー
	 * @return キーにマップされている値
	 */
	public String getenv(String key){
		return cgiEnvironmentVariable.get(key);
	}
}

/* End of File */