package org.intra_mart.jssp.page.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.page.provider.JSSPPageBuilder;
import org.intra_mart.jssp.page.provider.JSSPPageBuilderManager;

/**
 * JSSP-RPC環境の要求を受け付けるサーブレットです。
 */
public class JsspRpcServlet extends HttpServlet {


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		doPost(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

		JSSPPageBuilder pageBuilder = JSSPPageBuilderManager.getBuilder("jssprpc", req, resp);
		
		if(! pageBuilder.verify()){
			// TODO [OSS-JSSP-RPC] 未実装
			throw new ServletException("verify error");
		}
		
		try {
			pageBuilder.invoke();
		} catch (Exception e) {
			// TODO [OSS-JSSP-RPC] 未実装
			throw new ServletException("error", e);
		}
		
	}
}

