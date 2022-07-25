package org.intra_mart.common.web.log;

import org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType;

/**
 * TODO javadoc
 */
public interface ServletFilterBasedLogHandler {
	
	/**
	 * 
	 */
	public void init();
	
	/**
	 * 
	 */
	public void destroy();
	
	/**
	 * @param req
	 * @param res
	 * @param dispatchType
	 * @return
	 */
	public boolean isLoggableBeforeDoFilter(HttpServletRequestWrapper4ServletFilterBasedLog req,
											  HttpServletResponseWrapper4ServletFilterBasedLog res,
											  DispatchType dispatchType);
	
	/**
	 * @param req
	 * @param res
	 * @param dispatchType
	 */
	public void publishBeforeDoFilter(HttpServletRequestWrapper4ServletFilterBasedLog req,
									   HttpServletResponseWrapper4ServletFilterBasedLog res,
									   DispatchType dispatchType);
	
	
	/**
	 * @param req
	 * @param res
	 * @param dispatchType
	 * @return
	 */
	public boolean isLoggableAfterDoFilter(HttpServletRequestWrapper4ServletFilterBasedLog req,
											 HttpServletResponseWrapper4ServletFilterBasedLog res,
											 DispatchType dispatchType);
	
	/**
	 * @param req
	 * @param res
	 * @param dispatchType
	 */
	public void publishAfterDoFilter(HttpServletRequestWrapper4ServletFilterBasedLog req,
									  HttpServletResponseWrapper4ServletFilterBasedLog res,
									  DispatchType dispatchType);
	
	
	/**
	 * @param req
	 * @param res
	 * @param dispatchType
	 * @return
	 */
	public boolean isLoggableCatchedDoFilter(HttpServletRequestWrapper4ServletFilterBasedLog req,
											   HttpServletResponseWrapper4ServletFilterBasedLog res,
											   DispatchType dispatchType);
	
	/**
	 * @param req
	 * @param res
	 * @param dispatchType
	 * @param throwable
	 */
	public void publishCatchedDoFilter(HttpServletRequestWrapper4ServletFilterBasedLog req,
										HttpServletResponseWrapper4ServletFilterBasedLog res,
										DispatchType dispatchType, Throwable throwable);
	
}
