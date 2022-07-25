/*
 * StandardEJBEventListenerAgentHome.java
 *
 * Created on 2002/02/25, 12:01
 */

package org.intra_mart.framework.base.event;

import javax.ejb.EJBHome;

import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * {@link StandardEJBEventListenerAgent}のホームインタフェースです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface StandardEJBEventListenerAgentHome extends EJBHome {

    /**
     * StandardEJBEventListenerAgentと接続します。
     *
     * @return StandardEJBEventListenerAgent
     * @throws RemoteException システムレベルエラー
     * @throws CreateException EJBEventListenerAgent生成時に例外が発生
     */
    public StandardEJBEventListenerAgent create() throws RemoteException, CreateException;
}
