package org.intra_mart.common.platform.log;

import java.util.ArrayList;
import java.util.List;

/**
 * OPEN INTRA-MART モジュールを intra-mart WebPlatform 上で動作させるためのラッパークラスです。
 * 
 * @see jp.co.intra_mart.common.platform.log.Logger
 * @see jp.co.intra_mart.common.platform.log.Logger.Level
 */
public class Logger {

	public enum Level{
		ERROR,
		WARN,
		INFO,
		DEBUG,
		TRACE 
	}
	
	private jp.co.intra_mart.common.platform.log.Logger.Level convertLogLevel(Level level) {
		jp.co.intra_mart.common.platform.log.Logger.Level jciLevel 
			= Enum.valueOf(jp.co.intra_mart.common.platform.log.Logger.Level.class, level.toString());
		return jciLevel;
	}

	private jp.co.intra_mart.common.platform.log.Logger jciLogger;
	
	private Logger(jp.co.intra_mart.common.platform.log.Logger jciLogger){
		this.jciLogger = jciLogger;
	}
	
	public static Logger getLogger(String name) {
		jp.co.intra_mart.common.platform.log.Logger jciLogger
						= jp.co.intra_mart.common.platform.log.Logger.getLogger(name);
		return new Logger(jciLogger);
	}

	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
	public static Logger getLogger(){
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		 
		String callerClassName = null;
		
		for(int dept = 0 ;dept < ste.length; dept ++){ 

			String name = ste[dept].getClassName();

			if(excludeLoggerName4ClassName.contains(name)){ 
				continue;
			}

			callerClassName = ste[dept].getClassName();
			break;
		}

		if(callerClassName == null){
			callerClassName = "anonymous_logger_name";
		}
		
		return getLogger(callerClassName);
	}
	
	private static List<String> excludeLoggerName4ClassName = new ArrayList<String>();

	static{
		// Threadクラス
		excludeLoggerName4ClassName.add("java.lang.Thread");
		
		// 自身
		excludeLoggerName4ClassName.add(org.intra_mart.common.platform.log.Logger.class.getName());
	}
	
	public boolean isEnabled(Level level){
		return jciLogger.isEnabled(convertLogLevel(level));
    }

    public void log(Level level, String msg){
		jciLogger.log(convertLogLevel(level), msg);
    }
    
    public void log(Level level, String format, Object... args){
		jciLogger.log(convertLogLevel(level), format, args);
    }

    public void log(Level level, String msg, Throwable t){
		jciLogger.log(convertLogLevel(level), msg, t);
    }

    public void log(Level level, String msgOrFormat, Throwable t, Object... args){
		jciLogger.log(convertLogLevel(level), msgOrFormat, t, args);
    }

	public boolean isErrorEnabled(){
		return jciLogger.isErrorEnabled();
	}

	public void error(String msg){
		jciLogger.error(msg);
	}
		
	public void error(String format, Object... args){
		jciLogger.error(format, args);
	}
	
	public void error(String msg, Throwable t){
		jciLogger.error(msg, t);
	}

	public boolean isWarnEnabled(){
		return jciLogger.isWarnEnabled();
	}

	public void warn(String msg){
		jciLogger.warn(msg);
	}
	
	public void warn(String format, Object... args){
		jciLogger.warn(format, args);
	}

	public void warn(String msg, Throwable t){
		jciLogger.warn(msg, t);
	}
	
	public boolean isInfoEnabled(){
		return jciLogger.isInfoEnabled();
	}

	public void info(String msg){
		jciLogger.info(msg);
	}
	
	public void info(String format, Object... args){
		jciLogger.info(format, args);
	}
	
	public void info(String msg, Throwable t){
		jciLogger.info(msg, t);
	}
	
	public boolean isDebugEnabled(){
		return jciLogger.isDebugEnabled();
	}

	public void debug(String msg){
		jciLogger.debug(msg);
	}
	
	public void debug(String format, Object... args){
		jciLogger.debug(format, args);
	}
	
	public void debug(String msg, Throwable t){
		jciLogger.debug(msg, t);
	}

	public boolean isTraceEnabled(){
		return jciLogger.isTraceEnabled();
	}

	public void trace(String msg){
		jciLogger.trace(msg);
	}
	
	public void trace(String format, Object... args){
		jciLogger.trace(format, args);
	}
	
	public void trace(String msg, Throwable t){
		jciLogger.trace(msg, t);
	}
}