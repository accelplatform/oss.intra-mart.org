package org.intra_mart.common.aid.jsdk.javax.servlet.http.session.impl;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.HttpSessionManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.HttpSessionManagerFactory;

/**
 * {@link HttpSessionManager} を作成するファクトリクラスの実装です。
 * 
 */
public class HttpSessionManagerFactoryImpl extends HttpSessionManagerFactory{
	/**
	 * 新しいファクトリを構築します。
	 */
	public HttpSessionManagerFactoryImpl(){
		super();
	}

	/**
	 * マネージャの実装を返します。
	 * @return マネージャ
	 */
	public HttpSessionManager getManager(){
		return HttpSessionManagerImpl.getInstance();
	}
}
