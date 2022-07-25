package org.intra_mart.common.aid.jsdk.javax.servlet.http.session.impl;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.HttpSessionManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.HttpSessionManagerFactory;


/**
 * {@link HttpSessionManagerFactory#instance()} が返す、
 * {@link HttpSessionManagerFactory} および {@link HttpSessionManager} の
 * 標準実装と連動するセッションリスナの実装です。
 * このリスナを設定し動作させる事で、{@link HttpSessionManager} の標準実装が
 * 正しい値を返します。
 */
public class HttpSessionMonitoringListenerImpl implements HttpSessionListener{
	/**
	 * 新しいセッションリスナを構築します。
	 */
	public HttpSessionMonitoringListenerImpl(){
		super();
	}

	/**
	 * セッションが作成されたことを通知します。
	 * @param se 通知するイベント
	 */
	public void sessionCreated(HttpSessionEvent se){
		HttpSession httpSession = se.getSession();
		HttpSessionManagerImpl httpSessionManagerImpl = HttpSessionManagerImpl.getInstance();
		httpSessionManagerImpl.handleSessionCreated(httpSession);
	}

	/**
	 * セッションが無効になったことを通知します。
	 * @param se 通知するイベント
	 */
	public void sessionDestroyed(HttpSessionEvent se){
		HttpSession httpSession = se.getSession();
		HttpSessionManagerImpl httpSessionManagerImpl = HttpSessionManagerImpl.getInstance();
		httpSessionManagerImpl.handleSessionDestroyed(httpSession);
	}
}

