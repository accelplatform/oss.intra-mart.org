package org.intra_mart.common.aid.jsdk.utility;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jdk.java.io.URLEncodeOutputStream;


/**
 * URL関連のユーティリティクラス。<BR>
 * <BR>
 * HttpServletRequestが保持している情報を元に、URLを生成するクラスです。<BR>
 * 
 */
public class URLUtil {
	
	/**
	 * 引数で与えられたリクエストのコンテキストを示すURLを生成します。<BR>
	 * このメソッドで生成されたURLの末尾は、常にスラッシュとなります。
	 * 
	 * @param request リクエスト
	 * @return 要求のコンテキストを示すURL
	 * @throws MalformedURLException　引数で与えられたリクエストに使用されるスキーマが未知である場合
	 */
	public static URL getContextURL(HttpServletRequest request) throws MalformedURLException {
		return getContextURL(request, true);
	}
	
	
	/**
	 * 引数で与えられたリクエストのコンテキストを示すURLを生成します。<BR>
	 * このメソッドで生成されたURLの末尾は、引数 appendSlash によって変化します。
	 * 
	 * @param request リクエスト
	 * @param appendSlash このメソッドで生成されたURLの末尾にスラッシュを付加する場合は、true、　それ以外はfalse
	 * @return　要求のコンテキストを示すURL
	 * @throws MalformedURLException 引数で与えられたリクエストに使用されるスキーマが未知である場合
	 */
	public static URL getContextURL(HttpServletRequest request, boolean appendSlash) throws MalformedURLException {
	
		String requestURL = new String(request.getRequestURL());
		String contextPath = request.getContextPath();

		// 「://」以降でコンテキストパスが出現する位置を取得
		int schemeSepPosition = requestURL.indexOf("://");
		int contextPathPosition = requestURL.indexOf(contextPath, schemeSepPosition + 3);

		// リクエストURLの先頭からコンテキストパスの末尾までを取得
		String contextURL = requestURL.substring(0, contextPathPosition + contextPath.length());
		
		// 末尾スラッシュ処理
		if(appendSlash == true && ( contextURL.lastIndexOf("/") != contextURL.length() - 1) ){
			contextURL = contextURL.concat("/");
		}
		
		return new URL(contextURL);
		
	}

	/**
	 * 文字列をURLエンコードします。
	 * @param src URLエンコード元文字列
	 * @return URLエンコード後の文字列
	 */
	public static String encodeUrl(String src) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(new URLEncodeOutputStream(bos));
        ps.print(src);
        String result = bos.toString();
        ps.close();
        ps = null;
        bos = null;
        return result;
    }
}
