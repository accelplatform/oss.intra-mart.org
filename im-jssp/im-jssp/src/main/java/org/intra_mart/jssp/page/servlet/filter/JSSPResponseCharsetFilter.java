package org.intra_mart.jssp.page.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.intra_mart.common.aid.jdk.util.charset.CharacterSetManager;
import org.intra_mart.jssp.util.charset.CharsetHandler;
import org.intra_mart.jssp.util.charset.CharsetHandlerManager;

/**
 *
 */
public class JSSPResponseCharsetFilter implements Filter {

	private String defaultMimeType = "text/html"; 
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		
		String mimeType = filterConfig.getInitParameter("default-mime-type");
		if(mimeType != null){
			this.defaultMimeType = mimeType;
		}
		
	}

	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
					throws IOException, ServletException {
		
        HttpServletResponseWrapper responseWrapper = 
        	new ResponseWrapper4JSSPResponseCharsetFilter((HttpServletResponse) servletResponse,
        												   this.defaultMimeType);

        filterChain.doFilter(servletRequest, responseWrapper);

	}

	public void destroy() {
	}

}

/**
 *
 */
class ResponseWrapper4JSSPResponseCharsetFilter extends HttpServletResponseWrapper {

	private String defaultMimeType = "text/html";
    private String contentType = null;

    /**
     * @param response
     * @param mimeType
     * @param charset
     */
    public ResponseWrapper4JSSPResponseCharsetFilter(HttpServletResponse response, String mimeType) {
        super(response);
        this.defaultMimeType = mimeType;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)
     */
    public void setContentType(String type) {
        this.getResponse().setContentType(type);
        this.contentType = type;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    public PrintWriter getWriter() throws IOException {

        if (this.contentType == null) {
        	// Content-Typeが不明 → エンコーディングが不明 → 設定
			CharsetHandler handler = CharsetHandlerManager.getCharsetHandler();
			String encodingName = CharacterSetManager.toIANAName(handler.getCharacterEncoding());
			
	        this.getResponse().setContentType(this.defaultMimeType + "; charset=" + encodingName);
        }

        return this.getResponse().getWriter();
    }
}
