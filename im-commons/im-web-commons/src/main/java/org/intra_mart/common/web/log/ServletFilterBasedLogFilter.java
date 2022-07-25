package org.intra_mart.common.web.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.common.aid.jdk.javax.xml.XMLProperties;
import org.intra_mart.common.platform.log.Logger;
import org.xml.sax.SAXException;

/**
 * TODO javadoc
 * TODO JMXで再設定可能にする
 */
public class ServletFilterBasedLogFilter implements Filter {
	private static Logger logger = Logger.getLogger();
	
	public enum DispatchType{
		REQUEST,
		FORWARD,
		INCLUDE
 	}
	public static final String DISPATCH_TYPE_REQ_ATTR_KEY = ServletFilterBasedLogFilter.class.getName() + "#dispatchType";	
	
	private static final String _configParamName = "configResourceName";
	private static final String _defaultConfigResourceName     = "/conf/" + ServletFilterBasedLogFilter.class.getName() + "_conf.xml";
	private static final String _defaultPropName4XMLProperties = "/config/class-names/class-name";

	private static List<ServletFilterBasedLogHandler> LIST_4_LOG_HANDLERS = new ArrayList<ServletFilterBasedLogHandler>();
	
	/* (非 Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		String paramValue = config.getInitParameter(_configParamName);
		logger.trace("param-name: '{}' ==> {}", _configParamName, paramValue);
		
		try{
			resetConfiguration(paramValue, null);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	
	/* (非 Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		int size = LIST_4_LOG_HANDLERS.size();
		logger.trace("All handlers count: {}", size);
		
		if(size == 0){
			chain.doFilter(req, res);
		}
		else {
			HttpServletRequestWrapper4ServletFilterBasedLog wrappedRequest
									= new HttpServletRequestWrapper4ServletFilterBasedLog((HttpServletRequest)req);
	
			HttpServletResponseWrapper4ServletFilterBasedLog wrappedResponse
									= new HttpServletResponseWrapper4ServletFilterBasedLog((HttpServletResponse)res);
	
			DispatchType dispatchType = DispatchType.REQUEST;
			Object dispatchTypeInReqAttr = req.getAttribute(DISPATCH_TYPE_REQ_ATTR_KEY);
			if(dispatchTypeInReqAttr != null){
				dispatchType = (DispatchType) dispatchTypeInReqAttr;
			}
	
			try{
				// TODO メソッドしか違いがない。。。リフレクションで出来ないか！？ソースの見易さと速度のトレードオフ
				//=================================
				// Before
				//=================================
				for(int idx = 0, max = LIST_4_LOG_HANDLERS.size(); idx < max; idx++){
					ServletFilterBasedLogHandler handler = LIST_4_LOG_HANDLERS.get(idx);
					boolean isLoggable = handler.isLoggableBeforeDoFilter(wrappedRequest, wrappedResponse, dispatchType);
					
					logger.trace("**** Before[{}] **** <{}> : {}", idx, isLoggable, handler.getClass());
					if(isLoggable){
						handler.publishBeforeDoFilter(wrappedRequest, wrappedResponse, dispatchType);
					}
				}
				
				chain.doFilter(wrappedRequest, wrappedResponse);
			}
			catch(Throwable t){
				//=================================
				// Catched
				//=================================
				for(int idx = 0, max = LIST_4_LOG_HANDLERS.size(); idx < max; idx++){
					ServletFilterBasedLogHandler handler = LIST_4_LOG_HANDLERS.get(idx);
					boolean isLoggable = handler.isLoggableCatchedDoFilter(wrappedRequest, wrappedResponse, dispatchType);
					
					logger.trace("#### Catched[{}] #### <{}> : {}", idx, isLoggable, handler.getClass());
					if(isLoggable){
						handler.publishCatchedDoFilter(wrappedRequest, wrappedResponse, dispatchType, t);
					}
				}
				
				if(t instanceof IOException){
					throw (IOException) t;
				}
				else if(t instanceof ServletException){
					throw (ServletException) t;
				}
				else if(t instanceof RuntimeException){
					throw (RuntimeException) t;
				}
				else if(t instanceof Error){
					throw (Error) t;
				}
				else{
					throw new ServletException(t);
				}
			}
			finally{
				//=================================
				// After
				//=================================
				for(int idx = 0, max = LIST_4_LOG_HANDLERS.size(); idx < max; idx++){
					ServletFilterBasedLogHandler handler = LIST_4_LOG_HANDLERS.get(idx);
					boolean isLoggable = handler.isLoggableAfterDoFilter(wrappedRequest, wrappedResponse, dispatchType);
					
					logger.trace("!!!! After[{}] !!!! <{}> : {}", idx, isLoggable, handler.getClass());
					if(isLoggable){
						handler.publishAfterDoFilter(wrappedRequest, wrappedResponse, dispatchType);
					}
				}
				
			}
		}
	}

	/* (非 Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		notifyDestroy();
	}

	/**
	 * @param configResourceName
	 * @param propName4XMLProperties
	 * 
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void resetConfiguration(final String configResourceName, final String propName4XMLProperties)
					throws SAXException, IOException, ParserConfigurationException{
		
		String configResourceName_converted     = (configResourceName == null)      ? _defaultConfigResourceName     : configResourceName;
		String propName4XMLProperties_converted = (propName4XMLProperties == null ) ? _defaultPropName4XMLProperties : propName4XMLProperties;

		logger.trace("------------------------------------------");
		logger.trace("configResourceName          : {}", configResourceName);
		logger.trace("configResourceName_converted: {}", configResourceName_converted);
		logger.trace("propName4XMLProperties          : {}", propName4XMLProperties);
		logger.trace("propName4XMLProperties_converted: {}", propName4XMLProperties_converted);
		logger.trace("------------------------------------------");

		InputStream is = ServletFilterBasedLogFilter.class.getClassLoader().getResourceAsStream(configResourceName_converted);
		XMLProperties xml = new XMLProperties(is);
		String[] classNames = xml.getProperties(propName4XMLProperties_converted);
		
		resetConfiguration(classNames);
		
	}

	/**
	 * @param classNames
	 */
	public static void resetConfiguration(final String[] classNames) {
		notifyDestroy();
		
		synchronized (LIST_4_LOG_HANDLERS) {
			logger.trace("********************* resetConfiguration: START *********************");
			
			// リセット
			LIST_4_LOG_HANDLERS = new ArrayList<ServletFilterBasedLogHandler>();
	
			for(String className : classNames){
				try{
					Class<?> targetClass = Class.forName(className);
					Object targetObj = targetClass.newInstance();
					ServletFilterBasedLogHandler casted = (ServletFilterBasedLogHandler) targetObj;
					
					LIST_4_LOG_HANDLERS.add(casted);
					logger.info("Add {}: {}", ServletFilterBasedLogHandler.class.getSimpleName(), targetClass.getName());
				}
				catch(Exception e){
					logger.warn (e.getMessage() + ": '" + className + "'");		// warn() はスタックトレース無し
					logger.trace(e.getMessage() + ": '" + className + "'", e);	// trace()はスタックトレース有り
				}
			}
			logger.trace("********************* resetConfiguration:  END  *********************");

		}

		notifyInit();
	}
	

	private static void notifyInit(){
		synchronized (LIST_4_LOG_HANDLERS) {
			Iterator<ServletFilterBasedLogHandler> it = LIST_4_LOG_HANDLERS.iterator();
			while(it.hasNext()){
				ServletFilterBasedLogHandler handler = it.next();
				
				logger.trace("Start #init(): {}", handler.getClass().getName());
				handler.init();
			}
		}
	}

	
	private static void notifyDestroy(){
		synchronized (LIST_4_LOG_HANDLERS) {
			Iterator<ServletFilterBasedLogHandler> it = LIST_4_LOG_HANDLERS.iterator();
			while(it.hasNext()){
				ServletFilterBasedLogHandler handler = it.next();
				
				logger.trace("Start #destroy(): {}", handler.getClass().getName());
				handler.destroy();
			}
		}
	}
}

