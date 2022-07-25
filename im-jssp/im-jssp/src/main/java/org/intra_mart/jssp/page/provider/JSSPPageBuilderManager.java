package org.intra_mart.jssp.page.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.util.JSSPRuntimeClassProvider;

/**
 * JSSPPageBuilderを管理するマネージャです。
 */
public class JSSPPageBuilderManager {

	public static synchronized JSSPPageBuilder getBuilder(String id, 
															 HttpServletRequest request, 
															 HttpServletResponse response) {

		String key = "jssp-page-builder_" + id;
		Class[] ctorTypes = {HttpServletRequest.class, HttpServletResponse.class};
		Object[] ctorPrams = {request, response};
		try {
			return (JSSPPageBuilder)
						JSSPRuntimeClassProvider.getInstance().getClassInstance(key, ctorTypes, ctorPrams);
		}
		catch (Exception e) {
			throw new IllegalStateException("Instantiation Error : " + key, e);
		}

	}
	
}
