package org.intra_mart.common.platform.log.layout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.intra_mart.common.aid.jdk.util.UniqueIdGenerator;
import org.intra_mart.common.platform.log.MDC;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableInformation;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.StatusManager;

/**
 * 例外発生時に、例外の内容（Javaの実行トレース）を別ファイルに出力するPatternLayoutの拡張クラスです。<br/>
 * このクラスを利用した場合、以下の情報がファイルに出力されます。<br/>
 * 
 * <ol>
 * 	<li>ログID</li>
 * 	<li>出力日時</li>
 * 	<li>スレッド名</li>
 * 	<li>スレッドグループ</li>
 * 	<li>Javaの実行トレース</li>
 * </ol>
 *
 */
public class OutputStackTracePatternLayout extends PatternLayout {
	
	private static final String KEY_4_GENERATING_TIME = "log.generating.time";
	private static final String KEY_4_LOG_ID = "log.id";
	private static final String KEY_4_LOG_LEVEL = "log.level";
	private static final String KEY_4_LOGGER_NAME = "log.logger.name";
	private static final String KEY_4_THREAD_GROUP = "log.thread.group";
	private static final String KEY_4_THREAD_ID = "log.thread.id";
	private static final String KEY_4_LOG_MASSEGE = "log.message";

	private boolean enableOutputStackTrace = true;
	private String stackTraceDir = null;
	private String stackTraceFilename = null;
	/* (非 Javadoc)
	 * @see ch.qos.logback.classic.PatternLayout#doLayout(ch.qos.logback.classic.spi.LoggingEvent)
	 */
	public String doLayout(LoggingEvent event) {
		
		String result = super.doLayout(event);

		if(enableOutputStackTrace == false){
			return result;
		}
		
		ThrowableInformation tInfo = event.getThrowableInformation();
		if (tInfo != null) {
			Throwable throwable = tInfo.getThrowable();

			PrintWriter pWriter = null;
			try {
				String dir = this.stackTraceDir + File.separator;
				
				// ログIDの取得とファイル名の生成
				String logId = MDC.get(KEY_4_LOG_ID);
				String fileName = createStackTraceLogFileName(logId);
				
				// 出力先ログファイル
				File logFile = new File(dir + fileName);
				
				if(logFile.exists()){
					String logId2nd = UniqueIdGenerator.getUniqueId();

					String fileName2nd = createStackTraceLogFileName(logId2nd);
					logFile = new File(dir + fileName2nd);

					// 旧ログIDを入れ替え(！！！順番厳守！！！)
					result = result.replace(logId, logId2nd);
					logId = logId2nd;
				}
				
				File parent = logFile.getParentFile();
				if(parent.exists() == false){
					parent.mkdirs();
				}
				logFile.createNewFile();

				// Exceptionログの出力
				pWriter = new PrintWriter(logFile);
				
				// 出力日付
				Date date = new Date(event.getTimeStamp());
				pWriter.println(KEY_4_GENERATING_TIME + "=" + date.toString());

				// ログレベル
				pWriter.println(KEY_4_LOG_LEVEL + "=" + event.getLevel());
				
				// ロガー名
				pWriter.println(KEY_4_LOGGER_NAME + "=" + event.getLoggerRemoteView().getName());

				// ログID
				pWriter.println(KEY_4_LOG_ID + "=" + logId);

				// スレッド名
				Thread currentThread = Thread.currentThread();
				pWriter.println(KEY_4_THREAD_ID + "=" + currentThread.getName());
				
				// スレッドグループ
				String thredgroup = currentThread.getThreadGroup().getName();
				pWriter.println(KEY_4_THREAD_GROUP + "=" + thredgroup);
				
				// メッセージ
				pWriter.println(KEY_4_LOG_MASSEGE + "=" + event.getMessage());

				// StackTrace
				pWriter.println();
				throwable.printStackTrace(pWriter);
				pWriter.println();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				StatusManager sm = getContext().getStatusManager();
				sm.add(new ErrorStatus(e.getMessage(), this, e));
				e.printStackTrace();
			}
			finally{
				if(pWriter != null){
					pWriter.close();
				}
			}
		}
		return result;
	}

	private String createStackTraceLogFileName(String logId) {
		String name = this.stackTraceFilename.replaceAll("%logId", logId);
		SimpleDateFormat sdf = new SimpleDateFormat(name);
		String file = sdf.format(new Date());
		return file;
	}

	/**
	 * 例外ファイル（Javaの実行トレースファイル）を出力するディレクトリを返却します。
	 * 
	 * @return 例外ファイルの出力ディレクトリ
	 */
	public String getStackTraceDir() {
		return stackTraceDir;
	}

	/**
	 * 例外ファイル（Javaの実行トレースファイル）を出力するディレクトリを設定します。
	 * 
	 * @param stackTraceDir 例外ファイルの出力ディレクトリ
	 */
	public void setStackTraceDir(String stackTraceDir) {
		this.stackTraceDir = stackTraceDir;
	}

	/**
	 * 例外の内容（Javaの実行トレース）を出力するファイルの名称パターンを返却します。
	 * 
	 * @return 例外の内容を出力するファイルの名称パターン
	 */
	public String getStackTraceFilename() {
		return stackTraceFilename;
	}

	/**
	 * 例外の内容（Javaの実行トレース）を出力するファイルの名称パターンを設定します。
	 * 
	 * @param stackTraceFilename 例外を出力するファイルの名称パターン
	 */
	public void setStackTraceFilename(String stackTraceFilename) {
		this.stackTraceFilename = stackTraceFilename;
	}

	/**
	 * 例外発生時に、例外の内容（Javaの実行トレース）を別ファイルに出力するか否かをチェックします。
	 * 
	 * @return 例外の内容を別ファイルに出力する場合は true, それ以外は false を返却します。
	 */
	public boolean isEnableOutputStackTrace() {
		return enableOutputStackTrace;
	}

	/**
	 * 例外発生時に、例外の内容（Javaの実行トレース）を別ファイルに出力するか否かを設定します。
	 * 
	 * @param enableOutputStackTrace
	 */
	public void setEnableOutputStackTrace(boolean enableOutputStackTrace) {
		this.enableOutputStackTrace = enableOutputStackTrace;
	}


}