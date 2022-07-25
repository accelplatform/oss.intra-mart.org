/*
 * StandardEJBEventListenerAgent.java
 *
 * Created on 2002/02/25, 11:40
 */

package org.intra_mart.framework.base.event;

import javax.ejb.EJBObject;

import java.rmi.RemoteException;
import javax.ejb.EJBException;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * EJBを利用するときのイベントリスナのリモートインタフェースです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface StandardEJBEventListenerAgent extends EJBObject {

    /**
     * イベント処理を実行します。
     * 実際の処理は{@link StandardEJBEventListenerAgentBean#execute(org.intra_mart.framework.base.event.Event)}で行われます。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @throws SystemException システム処理時にアプリケーション例外が発生
     * @throws EJBException システムレベルのエラー
     * @throws RemoteException システムエラー
     */
    public EventResult execute(Event event) throws ApplicationException, SystemException, RemoteException, EJBException;
}
