package org.intra_mart.common.web.log.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;

import org.intra_mart.common.aid.jdk.java.lang.management.ThreadDumper;
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
public class ResponseCodeDetectedThreadDumper implements ServletFilterBasedLogHandler {
	private static final Logger logger = Logger.getLogger();
	
	private static final String _defaultConfigResourceName     = "/conf/" + ResponseCodeDetectedThreadDumper.class.getName() + "_conf.xml";

	private String jmxServiceHostname;	// <jmx-service-hostname>
	private int jmxServicePort;			// <jmx-service-port>	
	private Map<String, Object> jmxServiceEnvironment = new HashMap<String, Object>();	// <jmx-service-username>, <jmx-service-password>, <jmx-service-protocol-provider-packages> 
	 
	private int detectedResponseCode;	// <detected-response-code>
	private int intervalSeconds;		// <interval-seconds>
	private int processUntilSeconds;	// <process-until-seconds>

	private String outputDirPath;		// <output-dir-path> 
	private String fileNameFormat;		// <file-name-format>
	
	private ThreadDumpOutputThread outputThread = null;
	
	/* (非 Javadoc)
	 * @see org.intra_mart.common.web.log.ServletFilterBasedLogHandler#init()
	 */
	public void init() {
		try{
			String configResourceName = System.getProperty(ResponseCodeDetectedThreadDumper.class.getName() + ".configResourceName", _defaultConfigResourceName);
			
			InputStream is = ServletFilterBasedLogFilter.class.getClassLoader().getResourceAsStream(configResourceName);
			XMLProperties xml = new XMLProperties(is);
			
			String rootNodeName = "/config";

			jmxServiceHostname = xml.getProperty(rootNodeName + "/jmx-service-hostname");
			if(jmxServiceHostname == null){
				throw new IllegalArgumentException("'" + rootNodeName + "/jmx-service-hostname' must be specified.");
			}
			
			jmxServicePort = Integer.parseInt(xml.getProperty(rootNodeName + "/jmx-service-port"));
			
			String jmxServiceUsername = xml.getProperty(rootNodeName + "/jmx-service-username");
			String jmxServicePassword = xml.getProperty(rootNodeName + "/jmx-service-password");
			if(jmxServiceUsername != null){
				jmxServiceEnvironment.put(JMXConnector.CREDENTIALS, 
										  new String[]{jmxServiceUsername, jmxServicePassword});
			}
			
			String jmxServiceProtocolProviderPackages = xml.getProperty(rootNodeName + "/jmx-service-protocol-provider-packages");
			if(jmxServiceProtocolProviderPackages != null){
				jmxServiceEnvironment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, jmxServiceProtocolProviderPackages);
			}

			detectedResponseCode = Integer.parseInt(xml.getProperty(rootNodeName + "/detected-response-code", "503"));
			intervalSeconds      = Integer.parseInt(xml.getProperty(rootNodeName + "/interval-seconds",       "30"));
			processUntilSeconds  = Integer.parseInt(xml.getProperty(rootNodeName + "/process-until-seconds",  "1800"));
			
			outputDirPath  = xml.getProperty(rootNodeName + "/output-dir-path",  "target/temp/" + ResponseCodeDetectedThreadDumper.class.getName());
			fileNameFormat = xml.getProperty(rootNodeName + "/file-name-format", "yyyy-MM-dd_HHmmss'.tdump'");
			new SimpleDateFormat(fileNameFormat); // For validation : IllegalArgumentException

			Level l = Level.DEBUG;
			if(logger.isEnabled(l)){
				logger.log(l, "================================================================");
				logger.log(l, "#init() -> configResourceName    : {}", configResourceName);
				logger.log(l, "----------------------------------------------------------------");
				logger.log(l, "#init() -> jmxServiceHostname    : {}", jmxServiceHostname);
				logger.log(l, "#init() -> jmxServicePort        : {}", jmxServicePort);
				logger.log(l, "#init() -> jmxServiceEnvironment : {}", jmxServiceEnvironment);
				logger.log(l, "#init() -> detectedResponseCode  : {}", detectedResponseCode);
				logger.log(l, "#init() -> intervalSeconds       : {}", intervalSeconds);
				logger.log(l, "#init() -> processUntilSeconds   : {}", processUntilSeconds);
				logger.log(l, "#init() -> outputDirPath         : {}", outputDirPath);
				logger.log(l, "#init() -> fileNameFormat        : {}", fileNameFormat);
				logger.log(l, "================================================================");
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
		if(this.outputThread != null){
			this.outputThread.discard();
		}
	}

	/* (非 Javadoc)
	 * @see tdump.ServletFilterBasedLogHandler#isLoggableAfterDoFilter(tdump.ServletFilterBasedLogFilter.HttpServletRequestWrapper4ServletFilterBasedLog, tdump.ServletFilterBasedLogFilter.HttpServletResponseWrapper4ServletFilterBasedLog, tdump.ServletFilterBasedLogFilter.DispatchType)
	 */
	public boolean isLoggableAfterDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {
		
		int statusCode = res.getStatus();
		
		Level l = Level.TRACE;
		if(logger.isEnabled(l)){
			logger.log(l, "-------------------------------------------");
			logger.log(l, "#isLoggableAfterDoFilter() -> res.getStatus()     : {}", statusCode);
			logger.log(l, "#isLoggableAfterDoFilter() -> detectedResponseCode: {}", detectedResponseCode);
			logger.log(l, "-------------------------------------------");
		}
		
		if(statusCode == detectedResponseCode){
			return true;
		}
		else{
			return false;
		}
	}

	/* (非 Javadoc)
	 * @see tdump.ServletFilterBasedLogHandler#publishAfterDoFilter(tdump.ServletFilterBasedLogFilter.HttpServletRequestWrapper4ServletFilterBasedLog, tdump.ServletFilterBasedLogFilter.HttpServletResponseWrapper4ServletFilterBasedLog, tdump.ServletFilterBasedLogFilter.DispatchType)
	 */
	public void publishAfterDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {
		
		// TODO スレッド開始している旨をAPIリスト記述（∵Webアプリの規則に反するため）
		// スレッド開始
		if(this.outputThread == null || !this.outputThread.isAlive() || this.outputThread.isDead()){
			this.outputThread = new ThreadDumpOutputThread(jmxServiceHostname, jmxServicePort, jmxServiceEnvironment,
														   outputDirPath, fileNameFormat,
														   intervalSeconds, processUntilSeconds);
			this.outputThread.setDaemon(true);
			this.outputThread.start();
			logger.info("'{}' was started.", this.outputThread.getClass().getSimpleName());
		}
		else{
			logger.debug("'{}' is still alive.", this.outputThread.getClass().getSimpleName());
		}
		
	}


	/**
	 * @return
	 */
	private static Date now(){
		return new Date();
	}
	
	// TODO デバッグログを仕込め！
	/**
	 *
	 */
	private static class ThreadDumpOutputThread extends Thread {

		private String jmxServiceHostname;
		private int jmxServicePort;
		private Map<String, ?> jmxServiceEnvironment;

		private String outputDirPath; 
		private String fileNameFormat;
		private int intervalMillis;

		private Date nextOutputDate = now();
		private Date endDate = now();
		
		public ThreadDumpOutputThread(final String jmxServiceHostname,
									  final int jmxServicePort,
									  final Map<String, ?> jmxServiceEnvironment,
									  final String outputDirPath,
									  final String fileNameFormat,
									  final int intervalSeconds,
									  final int processUntilSeconds){
			
			this.jmxServiceHostname    = jmxServiceHostname;
			this.jmxServicePort        = jmxServicePort;
			this.jmxServiceEnvironment = jmxServiceEnvironment;
			
			this.outputDirPath  = outputDirPath; 
			this.fileNameFormat = fileNameFormat;

			this.intervalMillis = intervalSeconds * 1000;
			this.endDate = new Date(now().getTime() + (processUntilSeconds * 1000));
		}
		
		
		public void discard(){
			this.endDate = now();
		}

		public boolean isDead(){
			if(this.endDate.getTime() < now().getTime()){
				return true;
			}
			else{
				return false;
			}
		}

		public void run() {
			while(true){
				if(this.isDead()){
					break;
				}
				
				try {
					long nowMillis = now().getTime();
					
					if(nextOutputDate.getTime() < nowMillis){
						// 出力
			            doOutputThreadDump();
			            
						// 次回出力時刻設定
						nextOutputDate = new Date(nowMillis + intervalMillis);
						logger.info("Next Output Date: {}", nextOutputDate);
					}
						
					long sleepTimeMillis = nextOutputDate.getTime() - nowMillis;
					
					logger.trace("============================================");
					logger.trace("nowMillis       : {}[ms]", nowMillis);
					logger.trace("sleepTimeMillis : {}[ms]", sleepTimeMillis);
					logger.trace("============================================");
					
					Thread.sleep(sleepTimeMillis);
				}
				catch (InterruptedException e) {
					continue;
				}
				catch (Exception e) {
					// スレッド停止処理
					discard();
					
					logger.trace(e.getLocalizedMessage(), e);
					throw new IllegalStateException(e);
				}
			}
		}

		private void doOutputThreadDump() throws IOException, JMException  {
			PrintStream ps = null;
			try{
				File destDir = new File(outputDirPath);
				if(!destDir.isAbsolute()){
					logger.trace("outputDirPath is not Absolute: {}", outputDirPath);
					destDir = new File(System.getProperty("user.dir"), outputDirPath);
				}
				
				if(!destDir.exists()){
					boolean result = destDir.mkdirs();
					logger.trace("Execute mkdirs(): {} -> {}", result, destDir);
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat(fileNameFormat);
				String fileName = sdf.format(now());
				
				logger.trace("==============================================================");
				logger.trace("destDir  : {}", destDir);
				logger.trace("fileName : {}", fileName);
				logger.trace("==============================================================");

				File destFile = new File(destDir, fileName);
				ps = new PrintStream(destFile);
				
				//============================
				// スレッドダンプ出力！
				//============================
				ThreadDumper threadDumper = new ThreadDumper(ps, 
															 this.jmxServiceHostname, 
															 this.jmxServicePort, 
															 this.jmxServiceEnvironment);
				threadDumper.dump();

				logger.info("Output thread dump: {}", destFile);
			}
			finally{
				if(ps != null){
					ps.flush();
					ps.close();
				}
			}
		}
		
	}
 
	// 以下は、実装しません。
	public boolean isLoggableBeforeDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {
		return false;
	}

	public boolean isLoggableCatchedDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {
		return false;
	}

	public void publishBeforeDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType) {

	}

	public void publishCatchedDoFilter(
			HttpServletRequestWrapper4ServletFilterBasedLog req,
			HttpServletResponseWrapper4ServletFilterBasedLog res,
			DispatchType dispatchType, Throwable throwable) {

	}
	
}
