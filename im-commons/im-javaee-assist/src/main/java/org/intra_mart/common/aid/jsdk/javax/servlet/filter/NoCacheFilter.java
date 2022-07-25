package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * ブラウザのキャッシュを無効にするフィルタ。<p>
 * このフィルタは、レスポンスヘッダに以下を追加します。<br>
 * <pre>
 * Pragma: no-cache
 * Cache-Control: no-cache
 * Expires: Tue, 22 Feb 2000 12:00:00 GMT
 * </pre>
 * <p>
 * レスポンスヘッダ Expires の値については、
 * フィルタの初期化パラメータ expires で任意に設定する事もできます。<p>
 * 設定可能な初期化パラメータは以下のとおりです。<br>
 * <table>
 * <tr>
 * <th>expires</th><td>コンテンツの有効期限日時</td>
 * </tr>
 * </table>
 * 
 */
public class NoCacheFilter extends AbstractFilter{
	/**
	 * 有効期限を設定するための初期化パラメータ名
	 */
	private static final String ID_EXPIRES = "expires";
	/**
	 * キャッシュの有効期限
	 */
	private String expire_date = "Tue, 22 Feb 2000 12:00:00 GMT";

	public NoCacheFilter(){
		super();
	}

	/**
	 * フィルタの初期化をします。<p>
	 * このメソッドは、フィルタ初期化時に {@link #init(FilterConfig)} に
	 * 呼び出されます。<br>
	 * @throws ServletException 初期化エラー
	 * @see #init(FilterConfig)
	 */
	protected void handleInit()  throws ServletException {
		FilterConfig config = this.getFilterConfig();

		// 設定値のチェック
		String expiresValue = config.getInitParameter(ID_EXPIRES);
		if(expiresValue != null){ this.expire_date = expiresValue; }
	}

	/**
	 * フィルタとして動作するロジック。
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param chain チェーンの次のエンティティ
	 * @throws ServletException 実行時エラー
	 * @throws IOException 入出力エラー
	 * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException{
		// キャッシュ無効ヘッダの追加
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setHeader("Cache-Control", "no-cache");
		httpServletResponse.setHeader("Expires", this.expire_date);

		chain.doFilter(request, response);
	}
}

