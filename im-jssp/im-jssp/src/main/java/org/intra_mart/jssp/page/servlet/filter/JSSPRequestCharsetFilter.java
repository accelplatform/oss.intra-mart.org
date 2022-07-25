package org.intra_mart.jssp.page.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.intra_mart.common.aid.jdk.util.charset.CharacterSetManager;
import org.intra_mart.jssp.util.charset.CharsetHandler;
import org.intra_mart.jssp.util.charset.CharsetHandlerManager;

/**
 *
 */
public class JSSPRequestCharsetFilter implements Filter {

	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) 
					throws IOException, ServletException {
		
        if(servletRequest.getCharacterEncoding() == null){
        	
        	// CharsetHandlerから文字エンコーディングを取得＆設定
			CharsetHandler handler = CharsetHandlerManager.getCharsetHandler();
			String encodingName = CharacterSetManager.toJDKName(handler.getCharacterEncoding());
			
        	servletRequest.setCharacterEncoding(encodingName);
        }
		
        filterChain.doFilter(servletRequest, servletResponse);
	}

	
	public void destroy() {
	}

}
