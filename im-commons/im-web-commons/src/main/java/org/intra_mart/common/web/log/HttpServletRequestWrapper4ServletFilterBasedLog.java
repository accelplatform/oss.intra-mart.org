package org.intra_mart.common.web.log;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType;

/**
 *  {@link RequestDispatcher4ServletFilterBasedLog}でラップした
 *  リクエストディスパッチャーを返却するためのHttpServletRequestWrapper。
 */
public class HttpServletRequestWrapper4ServletFilterBasedLog extends HttpServletRequestWrapper {
	// TODO toString()オーバーライド！？
	/**
	 * @param request
	 */
	public HttpServletRequestWrapper4ServletFilterBasedLog(HttpServletRequest request) {
		super(request);
	}

	/**
	 * {@link RequestDispatcher4ServletFilterBasedLog}でラップしたリクエストディスパッチャーを返却
	 *
	 * @param path 遷移先
	 * @return RequestDispatcher {@link RequestDispatcher4ServletFilterBasedLog}でラップしたリクエストディスパッチャーを返却
	 */
	public RequestDispatcher getRequestDispatcher(String path) {
		RequestDispatcher original = super.getRequestDispatcher(path);
		RequestDispatcher wrapped = new RequestDispatcher4ServletFilterBasedLog(original);
		return wrapped;
	}
	
	/**
	 * 「forward」 / 「include」をオーバーライドし、ディスパッチ種別をリクエスト属性に設定します。
	 */
	private static class RequestDispatcher4ServletFilterBasedLog implements RequestDispatcher {

		private RequestDispatcher original = null;

		/**
		 * @param requestDispatcher
		 */
		public RequestDispatcher4ServletFilterBasedLog(RequestDispatcher requestDispatcher) {
			super();
			this.original = requestDispatcher;
		}

		
		/* (非 Javadoc)
		 * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
		 */
		public void forward(ServletRequest req, ServletResponse res) throws ServletException, IOException {
			
			// 「FORWARD」を設定
			req.setAttribute(ServletFilterBasedLogFilter.DISPATCH_TYPE_REQ_ATTR_KEY, DispatchType.FORWARD);
			
			// オリジナルで実行
			this.original.forward(req, res);
		}

		
		/* (非 Javadoc)
		 * @see javax.servlet.RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
		 */
		public void include(ServletRequest req, ServletResponse res) throws ServletException, IOException {
			
			// 「INCLUDE」を設定
			req.setAttribute(ServletFilterBasedLogFilter.DISPATCH_TYPE_REQ_ATTR_KEY, DispatchType.INCLUDE);

			// オリジナルで実行
			this.original.include(req, res);
		}

	}
}
