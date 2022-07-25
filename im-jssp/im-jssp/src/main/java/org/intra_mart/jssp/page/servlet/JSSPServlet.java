package org.intra_mart.jssp.page.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.page.provider.JSSPPageBuilder;
import org.intra_mart.jssp.page.provider.JSSPPageBuilderManager;

/**
 *
 */
public class JSSPServlet extends HttpServlet{

	/**
	 * 
	 */
	public JSSPServlet(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.execute(req, resp);
	}

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

		JSSPPageBuilder pageBuilder = JSSPPageBuilderManager.getBuilder("default", req, resp);
		
		if(! pageBuilder.verify()){
			// TODO [OSS-JSSP] 未実装
			throw new ServletException("verify error");
		}
		
		try {
			pageBuilder.invoke();
		} catch (Exception e) {
			// TODO [OSS-JSSP] 未実装
			throw new ServletException("error", e);
		}
		
	}

}

