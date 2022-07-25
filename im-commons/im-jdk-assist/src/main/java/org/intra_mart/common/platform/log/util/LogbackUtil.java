package org.intra_mart.common.platform.log.util;

import java.net.URL;
import java.util.Iterator;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * <a href="http://logback.qos.ch/">Logback</a>のユーティリティクラスです。
 */
public class LogbackUtil {
	
	/**
	 * Logback のエラー情報を出力します。
	 */
	public static void printLogbackError() {
		boolean flg = false;
		try {
			Iterator<Status> statusIt = getStatusManager().iterator();		
			while(statusIt.hasNext()){
				Status status = statusIt.next();
				Throwable throwable = status.getThrowable();
				if(throwable != null){
					throwable.printStackTrace();
					flg = true;
				}
			}
		} catch (RuntimeException e) {
			/* Do Nothing */
		}
		finally{
			if(flg == true){
				System.err.println("!!!! Please check your LOGBACK configuration file !!!!");
			}
		}
	}

	/**
	 * Logback のステータス情報を出力します。
	 */
	public static void printLogbackStatus() {
		try {
			StatusPrinter.print(getStatusManager());
		} catch (RuntimeException e) {
			/* Do Nothing */
		}
	}
	
	/**
	 * LogBackの設定を行ないます。
	 * 
	 * @param configFileURL Logback設定ファイルのURL
	 */
	public static void doLogbackConfigure(URL configFileURL){
		LoggerContext lc = getLoggerContext();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.shutdownAndReset();
			configurator.doConfigure(configFileURL);
		}
		catch (JoranException je) {
			StatusPrinter.print(lc);
		}		
	}
	
	
	/**
	 * @return
	 */
	private static LoggerContext getLoggerContext(){
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		return lc;
		
	}

	/**
	 * @return
	 */
	private static StatusManager getStatusManager(){
		LoggerContext lc = getLoggerContext();
		return lc.getStatusManager();
		
	}
}
