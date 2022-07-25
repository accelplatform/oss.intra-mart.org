/*
 * GenericEJBEventListenerAgentLocalHome.java
 */
package org.intra_mart.framework.base.event;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * ローカルインタフェース{@link GenericEJBEventListenerAgentLocal}を取得するホームインタフェースです。
 *
 * @version 1.0
 * @since 5.0
 */
public interface GenericEJBEventListenerAgentLocalHome extends EJBLocalHome {

	/**
	 * ローカルインタフェース {@link GenericEJBEventListenerAgentLocal}を取得します。
	 * 
	 * @return GenericEJBEventListenerAgentLocal
	 * @throws RemoteException システムエラー
	 * @throws CreateException リモートインタフェース取得時に例外が発生
	 */
	public GenericEJBEventListenerAgentLocal create() throws CreateException;
}