/*
 * GenericEJBEventListenerAgentHome.java
 *
 * Created on 2002/02/25, 17:10
 */

package org.intra_mart.framework.base.event;

import javax.ejb.EJBHome;

import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * リモートインタフェースGenericEJBEventListenerAgentを取得するホームインタフェースです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface GenericEJBEventListenerAgentHome extends EJBHome {

    /**
     * リモートインタフェース{@link GenericEJBEventListenerAgent}を取得します。
     *
     * @return GenericEJBEventListenerAgent
     * @throws RemoteException システムエラー
     * @throws CreateException リモートインタフェース取得時に例外が発生
     */
    public GenericEJBEventListenerAgent create() throws RemoteException, CreateException;
}
