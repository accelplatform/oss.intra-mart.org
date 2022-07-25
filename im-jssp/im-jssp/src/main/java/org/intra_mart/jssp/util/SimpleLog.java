package org.intra_mart.jssp.util;

import java.util.logging.Level;
import java.util.logging.Logger;

// TODO [OSS-JSSP] ログ機能実装
public class SimpleLog {

	/**
	 * @param level
	 * @param message
	 */
	public static void logp(Level level, String message) {

		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		
		String className = ste[1].getClassName();
		String methodName = ste[1].getMethodName();

		Logger logger = Logger.getLogger(className);
		logger.logp(level, className, methodName, message);
	}
	
	/**
	 * @param level
	 * @param message
	 * @param thrown
	 */
	public static void logp(Level level, String message, Throwable thrown) {

		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		
		String className = ste[1].getClassName();
		String methodName = ste[1].getMethodName();

		Logger logger = Logger.getLogger(className);
		logger.logp(level, className, methodName, message, thrown);
	}

}
