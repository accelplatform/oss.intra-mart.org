package org.intra_mart.jssp.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;

/**
 * JSSPQueryを管理するマネージャです。
 */
public class JSSPQueryManager {

	public static synchronized JSSPQuery createJSSPQuery(HttpServletRequest request, HttpServletResponse response){
		Class[] ctorTypes = {HttpServletRequest.class, HttpServletResponse.class};
		Object[] ctorPrams = {request, response};
		return buildJSSPQuery(ctorTypes, ctorPrams);
	}
	
	public static synchronized JSSPQuery createJSSPQuery(String jsspQueryString){
		Class[] ctorTypes = {String.class};
		Object[] ctorPrams = {jsspQueryString};
		return buildJSSPQuery(ctorTypes, ctorPrams);		
	}

	public static synchronized JSSPQuery createJSSPQuery(String nextPagePath, String fromPagePath){
		Class[] ctorTypes = {String.class, String.class};
		Object[] ctorPrams = {nextPagePath, fromPagePath};
		return buildJSSPQuery(ctorTypes, ctorPrams);
	}
	
	public static synchronized JSSPQuery createJSSPQuery(){
		Class[] ctorTypes = null;
		Object[] ctorPrams = null;
		return buildJSSPQuery(ctorTypes, ctorPrams);
	}
	
	private static ThreadLocal<JSSPQuery> jsspQueryThreadLocal = new ThreadLocal<JSSPQuery>();

	
	public static JSSPQuery currentJSSPQuery(){
		return jsspQueryThreadLocal.get();
	}

	public static JSSPQuery entry(JSSPQuery jsspQuery){
		JSSPQuery old = currentJSSPQuery();
		
		jsspQueryThreadLocal.set(jsspQuery);
		
		return old;
	}
	
	public static JSSPQuery releaseJSSPQuery(){
		JSSPQuery old = currentJSSPQuery();
		
		jsspQueryThreadLocal.set(null);
		
		return old;		
	}
	
	private static JSSPQuery buildJSSPQuery(Class[] ctorTypes, Object[] ctorPrams){
		
		String key = "jssp-query";
		try {
			return (JSSPQuery)
						JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
		}
		catch (Exception e) {
			throw new IllegalStateException("Instantiation Error : " + key, e);
		}
	}
}
