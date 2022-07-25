package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletRequest を拡張したインタフェースです。
 * 
 */
public interface HttpServletRequestMessageBodyWrapperBuilder{
	/**
	 * このオブジェクトを初期化します。
	 */
	public void init(FilterConfig config);

	/**
	 * ExtendedHttpServletRequest を作成して返します。<p>
	 * @param request リクエスト
	 * @param response レスポンス
	 * @throws IOException 入出力エラー
	 * @throws ServletException その他の実行時エラー
	 */
	public HttpServletRequestMessageBodyWrapper buildExtendedHttpServletRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

	/**
	 * このオブジェクトの破棄処理をします。<p>
	 */
	public void destroy();
}
