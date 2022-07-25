package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * このフィルタを利用することにより、
 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager}経由で
 * {@link org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext}を取得できるようになります。
 * 
 */
public class HTTPContextHandlingFilter extends AbstractFilter {

	/**
	 * デフォルトの処理委譲先フィルター・クラス名
	 */
	private static final String defaultImplClassName = "org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl.HTTPContextHandlingFilterImpl";

	/**
	 * 処理委譲先フィルター
	 */
	private Filter filter = null;

	/**
	 * デフォルト・コンストラクタ
	 */
	public HTTPContextHandlingFilter(){
		super();

		if(this.filter == null){
			
			// 処理委譲先フィルターのインスタンス化
			Class clazz = null;
			try {
				clazz = this.getClass().getClassLoader().loadClass(defaultImplClassName);
			}
			catch(ClassNotFoundException e) {
	
				try {
					Thread t = Thread.currentThread();
					clazz = t.getContextClassLoader().loadClass(defaultImplClassName);
				}
				catch(ClassNotFoundException ex) {
					IllegalStateException ise = new IllegalStateException(ex.getMessage());
					ise.initCause(ex);
					throw ise;
				}
	
			}
	
			// 処理委譲先フィルターを格納
			try {
				this.filter = (Filter) clazz.newInstance();
			}
			catch(InstantiationException e) {
				IllegalStateException ise = new IllegalStateException(e.getMessage());
				ise.initCause(e);
				throw ise;
			}
			catch(IllegalAccessException e) {
				IllegalStateException ise = new IllegalStateException(e.getMessage());
				ise.initCause(e);
				throw ise;
			}
	
		}
	}
	
	/* (非 Javadoc)
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter#handleInit()
	 */
	protected void handleInit() throws ServletException {
		FilterConfig filterConfig = this.getFilterConfig();

		// 処理委譲先フィルターの初期化
		this.filter.init(filterConfig);
	}

	/* (非 Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 処理委譲
		this.filter.doFilter(request, response, chain);
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.AbstractFilter#handleDestroy()
	 */
	protected void handleDestroy() {
		
		// 処理委譲先フィルターの破棄
		this.filter.destroy();
	}

}

