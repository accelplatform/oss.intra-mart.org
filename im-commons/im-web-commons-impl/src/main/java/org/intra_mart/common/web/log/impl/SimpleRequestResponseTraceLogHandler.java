package org.intra_mart.common.web.log.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.Cookie;

import org.intra_mart.common.aid.jdk.javax.xml.XMLProperties;
import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.common.platform.log.Logger.Level;
import org.intra_mart.common.web.log.HttpServletRequestWrapper4ServletFilterBasedLog;
import org.intra_mart.common.web.log.HttpServletResponseWrapper4ServletFilterBasedLog;
import org.intra_mart.common.web.log.ServletFilterBasedLogFilter;
import org.intra_mart.common.web.log.ServletFilterBasedLogHandler;
import org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType;


/**
 * TODO javadoc
 */
public class SimpleRequestResponseTraceLogHandler implements ServletFilterBasedLogHandler {
	
	private static final Logger.Level DEFAULT_LOG_LEVEL = Logger.Level.TRACE;
	
	private static final Logger logger4Before  = Logger.getLogger(SimpleRequestResponseTraceLogHandler.class.getName() + ".Before");
	private static final Logger logger4Catched = Logger.getLogger(SimpleRequestResponseTraceLogHandler.class.getName() + ".Catched");
	private static final Logger logger4After   = Logger.getLogger(SimpleRequestResponseTraceLogHandler.class.getName() + ".After");
	private static final Logger log = Logger.getLogger(SimpleRequestResponseTraceLogHandler.class.getName() + "ForInnerTrace");

	private static final String _defaultConfigResourceName = "/conf/" + SimpleRequestResponseTraceLogHandler.class.getName() + "_conf.xml";

	private Logger.Level level4Before;
	private Logger.Level level4Catched;
	private Logger.Level level4After;

	private boolean isLoggableDispachType4Request;
	private boolean isLoggableDispachType4Forward;
	private boolean isLoggableDispachType4Include;
	
	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#init()
	 */
	public void init() {
		log.trace("#init()");
		
		try{
			String configResourceName = System.getProperty(SimpleRequestResponseTraceLogHandler.class.getName() + ".configResourceName", _defaultConfigResourceName);
			
			InputStream is = ServletFilterBasedLogFilter.class.getClassLoader().getResourceAsStream(configResourceName);
			XMLProperties xml = new XMLProperties(is);
			
			//=============================
			// log-level
			//=============================
			String logLevelNodeName = "/config/log-level";
			level4Before  = Enum.valueOf(Logger.Level.class, xml.getProperty(logLevelNodeName + "/before",  DEFAULT_LOG_LEVEL.toString()).toUpperCase());
			level4Catched = Enum.valueOf(Logger.Level.class, xml.getProperty(logLevelNodeName + "/catched", DEFAULT_LOG_LEVEL.toString()).toUpperCase());
			level4After   = Enum.valueOf(Logger.Level.class, xml.getProperty(logLevelNodeName + "/after",   DEFAULT_LOG_LEVEL.toString()).toUpperCase());
			
			//=============================
			// dispatchers
			//=============================
			String dispatcherNodeName = "/config/dispatchers/dispatcher";
			String[] dispachers = xml.getProperties(dispatcherNodeName);

			isLoggableDispachType4Request = false;
			isLoggableDispachType4Forward = false;
			isLoggableDispachType4Include = false;

			if(dispachers.length == 0){
				isLoggableDispachType4Request = true;
			}
			else{
				for(int idx = 0, max = dispachers.length; idx < max; idx++){
					if(DispatchType.REQUEST.toString().equals(dispachers[idx].toUpperCase())){
						isLoggableDispachType4Request = true;
					}
					else if(DispatchType.FORWARD.toString().equals(dispachers[idx].toUpperCase())){
						isLoggableDispachType4Forward = true;
					}
					else if(DispatchType.INCLUDE.toString().equals(dispachers[idx].toUpperCase())){
						isLoggableDispachType4Include = true;
					}
				}
			}

			Level l = Level.DEBUG;
			if(log.isEnabled(l)){
				log.log(l, "================================================================");
				log.log(l, "#init() -> configResourceName   : {}", configResourceName);
				log.log(l, "----------------------------------------------------------------");
				log.log(l, "#init() -> level4Before  : {}", level4Before);
				log.log(l, "#init() -> level4Catched : {}", level4Catched);
				log.log(l, "#init() -> level4After   : {}", level4After);
				log.log(l, "#init() -> isLoggableDispachType4Request : {}", isLoggableDispachType4Request);
				log.log(l, "#init() -> isLoggableDispachType4Forward : {}", isLoggableDispachType4Forward);
				log.log(l, "#init() -> isLoggableDispachType4Include : {}", isLoggableDispachType4Include);
				log.log(l, "================================================================");
			}

		}
		catch(Exception e){
			throw new IllegalStateException(e);
		}
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#destroy()
	 */
	public void destroy() {
		log.trace("#destroy()");
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#isLoggableBeforeDoFilter(org.intra_mart.common.web.log.HttpServletRequestWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.HttpServletResponseWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType)
	 */
	public boolean isLoggableBeforeDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {
		
		if(!isLoggableDispatchType(dispatchType)){
			return false;
		}
		
		boolean result = logger4Before.isEnabled(level4Before);
		log.trace("#isLoggableBeforeDoFilter(): {}", result);
		
		return result;
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#isLoggableCatchedDoFilter(org.intra_mart.common.web.log.HttpServletRequestWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.HttpServletResponseWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType)
	 */
	public boolean isLoggableCatchedDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {
		
		if(!isLoggableDispatchType(dispatchType)){
			return false;
		}
		
		boolean result = logger4Catched.isEnabled(level4Catched);
		log.trace("#isLoggableCatchedDoFilter(): {}", result);
		
		return result;
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#isLoggableAfterDoFilter(org.intra_mart.common.web.log.HttpServletRequestWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.HttpServletResponseWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType)
	 */
	public boolean isLoggableAfterDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {

		if(!isLoggableDispatchType(dispatchType)){
			return false;
		}
		
		boolean result = logger4After.isEnabled(level4After);
		log.trace("#isLoggableAfterDoFilter(): {}", result);
		
		return result;
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#publishBeforeDoFilter(org.intra_mart.common.web.log.HttpServletRequestWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.HttpServletResponseWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType)
	 */
	public void publishBeforeDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {

		log.trace("#publishBeforeDoFilter()");

		logger4Before.log(level4Before, "****************************************************");
		logger4Before.log(level4Before, "REQUEST ==> {}", toStringBean(req));
		logger4Before.log(level4Before, "RESPONSE ==> {}", toStringBean(res));
		logger4Before.log(level4Before, "DISPATCH_TYPE ==> {}", dispatchType);
		logger4Before.log(level4Before, "****************************************************");
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#publishCatchedDoFilter(org.intra_mart.common.web.log.HttpServletRequestWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.HttpServletResponseWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType, java.lang.Throwable)
	 */
	public void publishCatchedDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType, Throwable throwable) {

		log.trace("#publishCatchedDoFilter()");

		logger4Catched.log(level4Catched, "####################################################");
		logger4Catched.log(level4Catched, "request ==> {}", toStringBean(req));
		logger4Catched.log(level4Catched, "RESPONSE ==> {}",toStringBean(res));
		logger4Catched.log(level4Catched, "DISPATCH_TYPE ==> {}", dispatchType);
		logger4Catched.log(level4Catched, "####################################################");
		
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#publishAfterDoFilter(org.intra_mart.common.web.log.HttpServletRequestWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.HttpServletResponseWrapper4ServletFilterBasedLog, org.intra_mart.common.web.log.ServletFilterBasedLogFilter.DispatchType)
	 */
	public void publishAfterDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {

		log.trace("#publishAfterDoFilter()");

		logger4After.log(level4After, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		logger4After.log(level4After, "REQUEST ==> {}", toStringBean(req));
		logger4After.log(level4After, "RESPONSE ==> {}", toStringBean(res));
		logger4After.log(level4After, "DISPATCH_TYPE ==> {}", dispatchType);
		logger4After.log(level4After, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	/**
	 * @param dispatchType
	 * @return
	 */
	private boolean isLoggableDispatchType(DispatchType dispatchType) {
		if(DispatchType.REQUEST.equals(dispatchType) && !isLoggableDispachType4Request){
			return false;
		}
		else if(DispatchType.FORWARD.equals(dispatchType) && !isLoggableDispachType4Forward){
			return false;
		}
		else if(DispatchType.INCLUDE.equals(dispatchType) && !isLoggableDispachType4Include){
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * @param bean
	 * @return
	 */
	private StringBuilder toStringBean(Object bean){
		StringBuilder sb = new StringBuilder();
		
		PropertyDescriptor[] propDiscriptorList;
		try {
			BeanInfo info = Introspector.getBeanInfo(bean.getClass());
			propDiscriptorList = info.getPropertyDescriptors();
		}
		catch (IntrospectionException e) {
			log.trace(e.getLocalizedMessage(), e);
			
			sb = new StringBuilder();
			sb.append(bean.toString());
			return sb;
		}
			
		for(int idx = 0, max = propDiscriptorList.length; idx < max; idx++){
			
			String propName = propDiscriptorList[idx].getName();
			
			try {
				Method getter = propDiscriptorList[idx].getReadMethod();
				if(getter == null
				   ||
				   "getSession".equals(getter.getName())
				   ||
				   "getInputStream".equals(getter.getName()) 
				   ||
				   "getReader".equals(getter.getName()) 
				   ||
				   "getOutputStream".equals(getter.getName()) 
				   ||
				   "getWriter".equals(getter.getName())
				   ){
					continue;
				}
					
				Object propValue = getter.invoke(bean, new Object[0]);
				
				if(propValue instanceof Enumeration){
					StringBuilder sb4enumeration = new StringBuilder();
					
					sb4enumeration.append("{");
					Enumeration<?> enumeration = (Enumeration<?>) propValue;
					while(enumeration.hasMoreElements()){
						sb4enumeration.append(enumeration.nextElement());
						if(enumeration.hasMoreElements()){
							sb4enumeration.append(", ");
						}
					}
					sb4enumeration.append("}");

					propValue = sb4enumeration;
				}
				else if(propValue instanceof Cookie[]){
					Cookie[] cookies = (Cookie[]) propValue;
					StringBuilder sb4cookie = new StringBuilder();
					
					sb4cookie.append("{");
					for(int idx4cookie = 0, max4cookie = cookies.length; idx4cookie < max4cookie; idx4cookie++){
						sb4cookie.append(cookies[idx4cookie].getName()).append(": ").append(cookies[idx4cookie].getValue());
						if(idx4cookie + 1 < max4cookie){
							sb.append(", ");
						}
					}
					sb4cookie.append("}");
					
					propValue = sb4cookie;
				}
				
				sb.append(propName).append(": ").append(propValue);
				
			}
			catch (Exception e) {
				log.trace(e.getLocalizedMessage(), e);
				
				sb.append(propName).append(": ").append(e.getLocalizedMessage());
				continue;
			}
			finally{
				if(idx + 1 < max){
					sb.append(", ");
				}
			}
		}
		
		return sb; 
	}
}
