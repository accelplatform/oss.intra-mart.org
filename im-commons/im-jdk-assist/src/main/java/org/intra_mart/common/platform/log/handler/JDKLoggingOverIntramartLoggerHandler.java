package org.intra_mart.common.platform.log.handler;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.intra_mart.common.platform.log.Logger;

/**
 * JDK標準の{@link java.util.logging.Logger} を利用したログ出力を、
 * im-jdk-assistの{@link Logger} 経由で出力するためのハンドラです。<br/>
 * <br/>
 * システムプロパティ 「java.util.logging.config.file」 を設定することで、このハンドラを設定することが可能です。<br/>
 * （その他のハンドラ設定方法は {@link java.util.logging.LogManager} を参照してください）
 * 
 * <pre>-Djava.util.logging.config.file=<i>設定ファイル「logging.properties」のパス</i></pre>
 * 
 * 以下に logging.properties の記述を例示します。
 * <table border="1">
 * <tr>
 * 	<th>例：logging.properties</th>
 * </tr>
 * 	<tr>
 * 		<td>
 * 			<pre>
 * handlers= org.intra_mart.common.platform.log.handler.JDKLoggingOverIntramartLoggerHandler
 * 
 * # 実際のログレベルは「logback.xml」で設定します。
 * .level= ALL
 * 
 * org.intra_mart.common.platform.log.handler.JDKLoggingOverIntramartLoggerHandler.level = ALL
 * org.intra_mart.common.platform.log.handler.JDKLoggingOverIntramartLoggerHandler.formatter = java.util.logging.SimpleFormatter
 * 
 * # ロガーを指定してログレベルを設定
 * java.level = CONFIG
 * javax.level = CONFIG</pre>
 * 		</td>
 * 	</tr>
 * </table border="1">
 * 
 * @see org.intra_mart.common.platform.log.Logger
 * @see java.util.logging.Logger
 * @see java.util.logging.Handler
 * @see java.util.logging.LogManager
 */
public class JDKLoggingOverIntramartLoggerHandler extends Handler {

	private static ThreadLocal<LogRecord> logRecordThreadLocal = new ThreadLocal<LogRecord>();
	private static boolean enablePrintStackTrace = false;
	
	static {
		String key = JDKLoggingOverIntramartLoggerHandler.class.getCanonicalName() + ".enablePrintStackTrace";
		String value = System.getProperty(key);
		if(value != null){
			enablePrintStackTrace = Boolean.parseBoolean(value);
		}
	}
	
	/**
	 * JDK標準の{@link java.util.logging.Logger} を利用したログ出力を、
	 * im_jdk_assistの{@link Logger}経由で出力します。<br/>
	 * <br/>
	 * ログレベルのマッピングは以下の通りです。
	 * <table border="1">
	 * 	<tr>
	 * 		<th>JDK標準のLogger</th>
	 * 		<th>&nbsp;</th>
	 * 		<th>im-jdk-assistの{@link Logger}</th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>SEVERE</td>
	 * 		<td>→</td>
	 * 		<td>error</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WARNING</td>
	 * 		<td>→</td>
	 * 		<td>warn</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>INFO</td>
	 * 		<td>→</td>
	 * 		<td>info</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>CONFIG</td>
	 * 		<td>→</td>
	 * 		<td>debug</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>FINE</td>
	 * 		<td rowspan="4">→</td>
	 * 		<td rowspan="4">trace</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>FINER</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>FINEST</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ALL</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>OFF</td>
	 * 		<td>→</td>
	 * 		<td>(ログを出力しません)</td>
	 * 	</tr>
	 * </table>
	 * 
	 * @see org.intra_mart.common.platform.log.Logger
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	public void publish(LogRecord record) {
		try{
			Level level = record.getLevel();
			
			// OFF
			if(Level.OFF.equals(level)){
				return;
			}		
	
			logRecordThreadLocal.set(record);
	
			String message = record.getMessage();
			String loggerName = record.getLoggerName();
			Object[] parameters = record.getParameters();
			Throwable t = record.getThrown();
			
			ResourceBundle rb = record.getResourceBundle();
			if(rb != null){
				String key = message;
				String value = rb.getString(key);
				
				message = value;
			}

			// パラメータ
			if(parameters != null){
				message = MessageFormat.format(message, parameters);
			}
			
			Logger logger = null;
			try {
				logger = Logger.getLogger(loggerName);
			}
			catch (RuntimeException re) {
				// org.slf4j.LoggerFactory の static初期化子実行時にJDKのログ出力が行われた場合 
				// (例：クラスパス直下のlogback.xmlでJMXの設定を行い、かつ、このHandlerが設定されている場合)
				java.util.logging.Logger jdkLogger = java.util.logging.Logger.getLogger(loggerName);
	
				if(jdkLogger.isLoggable(level)){
					// 標準出力に出力
					System.out.println(message);
	
					if(enablePrintStackTrace == true){
						re.printStackTrace();
					}
				}
				return;
			}
			
			
			// SEVERE → error()
			if (Level.SEVERE.equals(level) 
			    &&
			    logger.isErrorEnabled()) {
				
				if(t == null){
					logger.error(message);
				}
				else {
					logger.error(message, t);
				}
	
			}
			// WARNING → warn()
			else if (Level.WARNING.equals(level) 
					 &&
					 logger.isWarnEnabled()) {
	
				if(t == null){
					logger.warn(message);
				}
				else {
					logger.warn(message, t);
				}
	
			}
			// INFO → info()
			else if (Level.INFO.equals(level) 
					 &&
					 logger.isInfoEnabled()) {
	
				if(t == null){
					logger.info(message);
				}
				else {
					logger.info(message, t);
				}
	
			}
			// CONFIG → debug()
			else if (Level.CONFIG.equals(level) 
					 &&
					 logger.isDebugEnabled()) {
	
				if(t == null){
					logger.debug(message);
				}
				else {
					logger.debug(message, t);
				}
	
			}
			// FINE → trace()
			else if (Level.FINE.equals(level) 
					 &&
					 logger.isTraceEnabled()) {
	
				if(t == null){
					logger.trace(message);
				}
				else {
					logger.trace(message, t);
				}
				
			}
			// FINER → trace()
			else if (Level.FINER.equals(level) 
					 &&
					 logger.isTraceEnabled()) {
	
				if(t == null){
					logger.trace(message);
				}
				else {
					logger.trace(message, t);
				}
	
			}
			// FINEST or ALL → trace()
			else if (Level.FINEST.intValue() >= level.intValue()
					 &&
					 logger.isTraceEnabled()) {
	
				if(t == null){
					logger.trace(message);
				}
				else {
					logger.trace(message, t);
				}
			}
		}
		finally{
			logRecordThreadLocal.remove();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#close()
	 */
	public void close() throws SecurityException {
		// Do Nothing
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#flush()
	 */
	public void flush() {
		// Do Nothing
	}

	/**
	 * 現在のスレッドに紐づいたログレコードを返却します。<br/>
	 * @return ログレコード
	 */
	public static LogRecord getCurrentThreadLogRecord() {
		return logRecordThreadLocal.get();
	}
}
