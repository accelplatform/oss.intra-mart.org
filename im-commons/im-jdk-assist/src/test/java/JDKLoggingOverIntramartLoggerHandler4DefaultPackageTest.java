
import java.net.URL;
import java.util.Iterator;

import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.common.platform.log.util.LogbackUtil;
import junit.framework.TestCase;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.status.Status;



public class JDKLoggingOverIntramartLoggerHandler4DefaultPackageTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		URL jdkLoggingProp = this.getClass().getResource("/org/intra_mart/common/platform/log/handler/logging_all.properties");
		System.setProperty("java.util.logging.config.file", jdkLoggingProp.getFile());
		System.out.println(jdkLoggingProp.getFile());

		URL logbackXml = this.getClass().getResource("/org/intra_mart/common/platform/log/handler/JDKLoggingOverIntramartLoggerHandlerTest_logback.xml");
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		lc.shutdownAndReset();
		configurator.setContext(lc);
		configurator.doConfigure(logbackXml.getFile());
		System.out.println(logbackXml.getFile());
		
		printLogbackError();
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		printLogbackError();
	}

	public void testPublish_呼び出しクラスがデフォルトパッケージの場合() throws Exception {
		java.util.logging.Logger jclLogger = java.util.logging.Logger.getLogger(this.getClass().getName());

		jclLogger.severe ("doLog via JDK Logging -> severe");
		jclLogger.warning("doLog via JDK Logging -> warning");
		jclLogger.info   ("doLog via JDK Logging -> info");
		jclLogger.config ("doLog via JDK Logging -> config");
		jclLogger.fine   ("doLog via JDK Logging -> fine");
		jclLogger.finer  ("doLog via JDK Logging -> finer");
		jclLogger.finest ("doLog via JDK Logging -> finest");
		
		Logger imLogger = Logger.getLogger();
		imLogger.error("doLog via intra-mart Logger -> error");
		imLogger.warn ("doLog via intra-mart Logger -> warn");
		imLogger.info ("doLog via intra-mart Logger -> info");
		imLogger.debug("doLog via intra-mart Logger -> debug");
		imLogger.trace("doLog via intra-mart Logger -> trace");

		assertTrue("例外が出力されなければOK", true);

	}


	private void printLogbackError() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		Iterator<Status> statusIt = lc.getStatusManager().iterator();
		
		while(statusIt.hasNext()){
			Status status = statusIt.next();
			Throwable t = status.getThrowable();
			if(t != null){
				t.printStackTrace();
				LogbackUtil.printLogbackStatus();
				fail("Logback関連のエラーあり");
			}
		}
		
		//ch.qos.logback.core.util.StatusPrinter.print(lc); // <- DEBUG
	}

}
