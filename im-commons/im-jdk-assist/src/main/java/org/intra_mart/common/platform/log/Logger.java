package org.intra_mart.common.platform.log;

import java.util.ArrayList;
import java.util.List;

import org.intra_mart.common.aid.jdk.util.UniqueIdGenerator;
import org.slf4j.LoggerFactory;

/**
 * ログ出力を提供するクラスです。<br/>
 * Logger で使用される5つのログレベルは(順番に)以下のようになります。
 * 
 * <ol>
 * 	<li>trace (最も軽微)</li>
 * 	<li>debug</li>
 * 	<li>info</li>
 * 	<li>warn</li>
 * 	<li>error (最も重大)</li>
 * </ol>
 * 
 * <h3>サンプルコード</h3>
 * <pre><code>
 * Logger logger = Logger.getLogger();
 * 
 * logger.error("メッセージです");
 * logger.warn("パラメータは「{}」です。", "その１");
 * logger.info("パラメータは「{}」 と 「{}」です。", "その１", "その２");
 * 
 * Object[] argArray = new Object[4];
 * argArray[0] = "第1パラメータ";
 * argArray[1] = 5.678;
 * argArray[2] = new java.util.Date();
 * argArray[3] = true;
 * logger.debug("パラメータは「{}」 と 「{}」 と 「{}」 と 「{}」です。", argArray);
 * 
 * logger.log(Logger.Level.TRACE, "引数で指定されたレベルのログを出力します");
 * 
 * Throwable t = new Exception("例外");
 * logger.error("例外をログ出力します", t);
 * logger.log(Logger.Level.TRACE, "引数で指定されたレベルの例外をログ出力します。", t);
 * </code>
 * </pre>
 * 
 */
public class Logger {
	
	/**
	 * ログレベルを定義した列挙型です。
	 */
	public enum Level{
		/**	errorレベル */
		ERROR,
		
		/**	warnレベル */
		WARN,
		
		/**	infoレベル */
		INFO,
		
		/**	debugレベル */
		DEBUG,
		
		/**	traceレベル */
		TRACE 
	}

	/**
	 * {@link Logger}を返します。<br/>
	 *  
	 * @param name ロガー名
	 * @return {@link Logger}
	 */
	public static Logger getLogger(String name) {
		org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(name);
		return new Logger(slf4jLogger);
	}

	/**
	 * {@link Logger}を返します。<br/>
	 * 引数で指定されたクラスに対応する名称のロガーを返却します。
	 *  
	 * @param clazz　ロガー名を決定するためのクラス
	 * @return {@link Logger}
	 */
	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
	/**
	 * {@link Logger}を返します。<br/>
	 * このメソッドを呼び出したクラスに対応する名称のロガーを返却します。
	 *  
	 * @return {@link Logger}
	 */
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
		excludeLoggerName4ClassName.add(Logger.class.getName());
	}
	
    private org.slf4j.Logger thisLogger;

    /**
     * プライベート・コンストラクタ
     * @param slf4jLogger
     */
    private Logger(org.slf4j.Logger slf4jLogger) {
        this.thisLogger = slf4jLogger;
    }
    
    /**
     * サブクラス用のコンストラクタです。<br/>
     * {@link Logger}のインスタンスを取得するには、以下のメソッドを利用します。<br/>
     * <br/>
     * {@link #getLogger()}<br/>
     * {@link #getLogger(Class)}<br/>
     * {@link #getLogger(String)}<br/>
     */
    protected Logger(org.intra_mart.common.platform.log.Logger imLogger){
    	this(imLogger.thisLogger);
    }

    /**
	 * 引数で指定されたレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
	 * @return 引数で指定されたレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isEnabled(Level level){
    	switch (level) {
			case ERROR:
				return thisLogger.isErrorEnabled();
			case WARN:
				return thisLogger.isWarnEnabled();
			case INFO:
				return thisLogger.isInfoEnabled();
			case DEBUG:
				return thisLogger.isDebugEnabled();
			case TRACE:
				return thisLogger.isTraceEnabled();
			default:
				return true;
		}
    }
	
	private static final Object[] NULL_ARRAY = null;
	
    /**
	 * 引数で指定されたレベルのログを出力します。<br/>
	 * 
     * @param level レベル
     * @param msg　メッセージ文字列
     */
    public void log(Level level, String msg){
    	this.log(level, msg, null, NULL_ARRAY);
    }
    
    /**
	 * 引数で指定されたレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
     * @param level レベル
     * @param format フォーマット文字列
     * @param args メッセージのパラメータ
     */
    public void log(Level level, String format, Object... args){
    	this.log(level, format, null, args);
    }

	/**
	 * 引数で指定されたレベルの例外をログ出力します。<br/>
	 * 
     * @param level レベル
	 * @param msg　例外に伴うメッセージ
	 * @param t ログに出力する例外
     */
    public void log(Level level, String msg, Throwable t){
    	this.log(level, msg, t, NULL_ARRAY);
    }

    /**
	 * 引数で指定されたレベルのログを出力します。<br/>
	 * 
     * @param level レベル
     * @param msgOrFormat メッセージ文字列 または フォーマット文字列
     * @param t ログに出力する例外
     * @param args メッセージのパラメータ
     */
    public void log(Level level, String msgOrFormat, Throwable t, Object... args){

    	// ログの基礎情報となるパラメータをMDCに保存
    	setBasicInformationMDC();
    	
    	switch (level) {
			case ERROR:
				if(thisLogger.isErrorEnabled()){
					if(t != null){
						thisLogger.error(msgOrFormat, t);
					}
					else{
						thisLogger.error(msgOrFormat, args);
					}
				}
				break;
			case WARN:
				if(thisLogger.isWarnEnabled()){
					if(t != null){
						thisLogger.warn(msgOrFormat, t);
					}
					else{
						thisLogger.warn(msgOrFormat, args);
					}
				}
				break;
			case INFO:
				if(thisLogger.isInfoEnabled()){
					if(t != null){
						thisLogger.info(msgOrFormat, t);
					}
					else{
						thisLogger.info(msgOrFormat, args);
					}
				}
				break;
			case DEBUG:
				if(thisLogger.isDebugEnabled()){
					if(t != null){
						thisLogger.debug(msgOrFormat, t);
					}
					else{
						thisLogger.debug(msgOrFormat, args);
					}
				}
				break;
			case TRACE:
				if(thisLogger.isTraceEnabled()){
					if(t != null){
						thisLogger.trace(msgOrFormat, t);
					}
					else{
						thisLogger.trace(msgOrFormat, args);
					}
				}
				break;
			default:
				break;
		}
    }

    /**
	 * errorレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
	 * @return errorレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isErrorEnabled(){
		return this.isEnabled(Level.ERROR);
	}

	/**
	 * errorレベルのログを出力します。<br/>
	 * 
	 * @param msg　メッセージ文字列
	 */
	public void error(String msg){
		this.log(Level.ERROR, msg);
	}
		
	/**
	 * errorレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @param format フォーマット文字列
	 * @param args メッセージのパラメータ
	 */
	public void error(String format, Object... args){
		this.log(Level.ERROR, format, args);
	}
	
	/**
	 * errorレベルの例外をログ出力します。<br/>
	 * 
	 * @param msg　例外に伴うメッセージ
	 * @param t ログに出力する例外
	 */
	public void error(String msg, Throwable t){
		this.log(Level.ERROR, msg, t);
	}
	

	/**
	 * warnレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
	 * @return warnレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isWarnEnabled(){
		return this.isEnabled(Level.WARN);
	}

	/**
	 * warnレベルのログを出力します。<br/>
	 * 
	 * @param msg　メッセージ文字列
	 */
	public void warn(String msg){
		this.log(Level.WARN, msg);
	}
	
	/**
	 * warnレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @param format フォーマット文字列
	 * @param args メッセージのパラメータ
	 */
	public void warn(String format, Object... args){
		this.log(Level.WARN, format, args);
	}
	
	/**
	 * warnレベルの例外をログ出力します。<br/>
	 * 
	 * @param msg　例外に伴うメッセージ
	 * @param t ログに出力する例外
	 */
	public void warn(String msg, Throwable t){
		this.log(Level.WARN, msg, t);
	}
	
	
	/**
	 * infoレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
	 * @return infoレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isInfoEnabled(){
		return this.isEnabled(Level.INFO);
	}

	/**
	 * infoレベルのログを出力します。<br/>
	 * 
	 * @param msg　メッセージ文字列
	 */
	public void info(String msg){
		this.log(Level.INFO, msg);
	}
	
	/**
	 * infoレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @param format フォーマット文字列
	 * @param args メッセージのパラメータ
	 */
	public void info(String format, Object... args){
		this.log(Level.INFO, format, args);
	}
	
	/**
	 * infoレベルの例外をログ出力します。<br/>
	 * 
	 * @param msg　例外に伴うメッセージ
	 * @param t ログに出力する例外
	 */
	public void info(String msg, Throwable t){
		this.log(Level.INFO, msg, t);
	}
	

	/**
	 * debugレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
	 * @return debugレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isDebugEnabled(){
		return this.isEnabled(Level.DEBUG);
	}

	/**
	 * debugレベルのログを出力します。<br/>
	 * 
	 * @param msg　メッセージ文字列
	 */
	public void debug(String msg){
		this.log(Level.DEBUG, msg);
	}
	
	/**
	 * debugレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @param format フォーマット文字列
	 * @param args メッセージのパラメータ
	 */
	public void debug(String format, Object... args){
		this.log(Level.DEBUG, format, args);
	}
	
	/**
	 * debugレベルの例外をログ出力します。<br/>
	 * 
	 * @param msg　例外に伴うメッセージ
	 * @param t ログに出力する例外
	 */
	public void debug(String msg, Throwable t){
		this.log(Level.DEBUG, msg, t);
	}
	

	/**
	 * traceレベルのログ処理が現在有効かどうかチェックします。<br/>
	 * 
	 * @return traceレベルのログ処理が有効の場合は true, それ以外は false を返却します。
	 */
	public boolean isTraceEnabled(){
		return this.isEnabled(Level.TRACE);
	}

	/**
	 * traceレベルのログを出力します。<br/>
	 * 
	 * @param msg　メッセージ文字列
	 */
	public void trace(String msg){
		this.log(Level.TRACE, msg);
	}
	
	/**
	 * traceレベルのログを出力します。<br/>
	 * 指定されたフォーマットとパラメータを元にログを出力します。
	 * 
	 * @param trace フォーマット文字列
	 * @param args メッセージのパラメータ
	 */
	public void trace(String format, Object... args){
		this.log(Level.TRACE, format, args);
	}
	
	/**
	 * traceレベルの例外をログ出力します。<br/>
	 * 
	 * @param msg　例外に伴うメッセージ
	 * @param t ログに出力する例外
	 */
	public void trace(String msg, Throwable t){
		this.log(Level.TRACE, msg, t);
	}
	
	private static final String KEY_4_LOG_ID = "log.id";
	private static final String KEY_4_THREAD_GROUP = "log.thread.group";

	/**
	 * ログの基礎情報となるパラメータをMDCに保存します。<br/>
	 * {@link Logger}を使用してログ出力を行う場合は、<br/>
	 * 以下の情報がデフォルトでMDCに保存されます。<br/><br/>
	 * 
	 * [key]log.id - ログID<br/>
	 * [key]log.thread.group - スレッドグループ
	 * 
	 */
	private void setBasicInformationMDC(){

		// ログID
		String uniqueId = UniqueIdGenerator.getUniqueId();
		MDC.put(KEY_4_LOG_ID, uniqueId);

		// スレッドグループ
		MDC.put(KEY_4_THREAD_GROUP, Thread.currentThread().getThreadGroup().getName()); 
	
	}

}