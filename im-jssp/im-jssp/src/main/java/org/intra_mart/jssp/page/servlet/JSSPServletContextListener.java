package org.intra_mart.jssp.page.servlet;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.jssp.page.JSSPInitializer;

/**
 *
 */
public class JSSPServletContextListener implements ServletContextListener{
	
	private static final String CONTEXT_PARAM_NAME_4_JSSP_HOME = "org.intra_mart.jssp.server.home.path";
	private static final String CONTEXT_PARAM_NAME_4_CONFIG_FILE = "org.intra_mart.jssp.config.file.path";
	
	/**
	 * 
	 */
	public JSSPServletContextListener(){
		super();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@SuppressWarnings("deprecation")
	public void contextInitialized(ServletContextEvent sce){
		Logger logger = Logger.getLogger();
		
		String homeDirecory = sce.getServletContext().getRealPath("/");

		// ホームディレクトリの決定		
		String value4JsspHome = sce.getServletContext().getInitParameter(CONTEXT_PARAM_NAME_4_JSSP_HOME);
		if(value4JsspHome != null && (new File(value4JsspHome)).isDirectory()){
			homeDirecory = value4JsspHome;
		}

		// コンフィグファイルの決定
		String configFile = sce.getServletContext().getInitParameter(CONTEXT_PARAM_NAME_4_CONFIG_FILE);		

		
		// 初期化
		JSSPInitializer.init(homeDirecory, configFile);
		logger.debug("homeDirectory: {}", homeDirecory);
		logger.debug("configFile: {}", configFile);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce){
		return;
	}
	
}


