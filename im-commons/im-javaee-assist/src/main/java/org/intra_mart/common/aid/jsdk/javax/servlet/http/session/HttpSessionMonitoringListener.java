package org.intra_mart.common.aid.jsdk.javax.servlet.http.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * {@link HttpSessionManagerFactory#instance()} が返す、
 * {@link HttpSessionManagerFactory} および {@link HttpSessionManager} の
 * 標準実装と連動するセッションリスナです。
 * このリスナを設定し動作させる事で、{@link HttpSessionManager} の標準実装が
 * 正しい値を返します。
 */
public class HttpSessionMonitoringListener implements HttpSessionListener{
	/**
	 * 標準提供となるファクトリの実装クラス名
	 */
	private static final String implClassName = "org.intra_mart.common.aid.jsdk.javax.servlet.http.session.impl.HttpSessionMonitoringListenerImpl";

	/**
	 * 実際に処理をするリスナ実装
	 */
	private HttpSessionListener httpSessionListener = null;

	/**
	 * 新しいセッションリスナを構築します。
	 */
	public HttpSessionMonitoringListener(){
		super();

		try{
			try{
				ClassLoader loader = this.getClass().getClassLoader();
				Class clazz = loader.loadClass(implClassName);
				this.httpSessionListener = (HttpSessionListener) clazz.newInstance();
			}
			catch(ClassNotFoundException cnfe){
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = loader.loadClass(implClassName);
				this.httpSessionListener = (HttpSessionListener) clazz.newInstance();
			}
		}
		catch(Throwable t){
			IllegalStateException ise = new IllegalStateException("No implementation error.");
			ise.initCause(t);
			throw ise;
		}
	}

	/**
	 * セッションが作成されたことを通知します。
	 * @param se 通知するイベント
	 */
	public void sessionCreated(HttpSessionEvent se){
		this.httpSessionListener.sessionCreated(se);
	}

	/**
	 * セッションが無効になったことを通知します。
	 * @param se 通知するイベント
	 */
	public void sessionDestroyed(HttpSessionEvent se){
		this.httpSessionListener.sessionDestroyed(se);
	}
}

