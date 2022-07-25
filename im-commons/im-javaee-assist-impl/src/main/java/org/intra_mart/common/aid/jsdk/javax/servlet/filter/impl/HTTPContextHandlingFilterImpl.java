package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.impl.HTTPContextManagerImpl;


/**
 * デフォルトで使用されるHTTPContextHandlingFilter
 */
public class HTTPContextHandlingFilterImpl extends AbstractFilter {

	/**
	 * デフォルト・コンストラクタ
	 */
	public HTTPContextHandlingFilterImpl() {
		super();
	}

	/**
	 * 現在のスレッドに指定の HTTP コンテキストを関連付け、
	 * 次のフィルタチェーンを実行します。
	 * 
	 * @param servletRequest リクエスト
	 * @param servletResponse レスポンス
	 * @param filterChain フィルタ
	 * @throws IOException フィルタ処理実行時にIOExceptionが発生
	 * @throws ServletException フィルタ処理実行時にServletExceptionが発生
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		
		FilterConfig filterConfig = this.getFilterConfig();

		// HTTP コンテキスト・マネージャの取得
		HTTPContextManagerImpl httpContextManager = (HTTPContextManagerImpl) HTTPContextManager.getInstance();

		// HTTP コンテキストの関連付け
		HTTPContext oldHTTPContext = httpContextManager.entry(filterConfig.getServletContext(), 
									 						  (HttpServletRequest)request, 
									 						  (HttpServletResponse)response);

		try{
			chain.doFilter(request, response);
		}
		finally{
			if(oldHTTPContext != null){
				// 本フィルター前回実行時の HTTPコンテキストを再登録
				httpContextManager.entry(oldHTTPContext.getServletContext(), 
										 oldHTTPContext.getRequest(), 
										 oldHTTPContext.getResponse());
				
			}
			else {
				// 本フィルターが最初に実行された場合(つまり、oldHTTPContext==nullの場合)、HTTP コンテキストの関連付けを開放
				httpContextManager.release();
			}
		}
	}

}

