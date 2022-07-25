/*
 * GenericEJBEventListener.java
 *
 * Created on 2002/02/25, 17:45
 */

package org.intra_mart.framework.base.event;

import java.rmi.RemoteException;
import javax.ejb.EJBException;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * EJBを利用する汎用的なイベントリスナです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class GenericEJBEventListener extends EJBEventListener {

    /**
     * ホームインタフェースGenericEJBEventListenerAgentHome
     */
    private GenericEJBEventListenerAgentHome home;

    /**
     * 処理を行う<CODE>StandardEventLinstener</CODE>のクラス名
     */
    private String listenerName;

    /**
     * GenericEJBEventListenerを新規に生成します。
     *
     * @param home ホームインタフェース{@link GenericEJBEventListenerAgentHome}
     * @param listenerName 実際に処理を行う{@link StandardEventListener}のクラス名
     */
    public GenericEJBEventListener(
        GenericEJBEventListenerAgentHome home,
        String listenerName) {
        super();
        this.home = home;
        this.listenerName = listenerName;
    }

    /**
     * 処理を実行します。
     * イベントを元に処理を実行します。
     * このインタフェースを実装するすべてのクラスは、
     * すべてのイベントトリガを実行した後に処理を
     * 実行するように設計されている必要があります。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @throws SystemException システム処理時にアプリケーション例外が発生
     */
    public EventResult execute(Event event)
        throws SystemException, ApplicationException {
        GenericEJBEventListenerAgent agent = null;
        EventResult result = null;

        // リモートインタフェースの取得
        try {
            agent = this.home.create();
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }

        // 処理の実行
        try {
            result = agent.execute(event, this.listenerName);
        } catch (ApplicationException e) {
            throw e;
        } catch (SystemException e) {
            throw e;
        } catch (EJBException e) {
            Exception ex = e.getCausedByException();
            if (ex == null) {
                throw new SystemException(e.getMessage(), e);
            } else if (ex instanceof SystemException) {
                throw (SystemException)ex;
            } else if (ex instanceof ApplicationException) {
                throw (ApplicationException)ex;
            } else {
                throw new SystemException(ex.getMessage(), ex);
            }
        } catch (RemoteException e) {
            Throwable ex = e.detail;
            if (ex == null) {
                throw new SystemException(e.getMessage(), e);
            } else if (ex instanceof SystemException) {
                throw (SystemException)ex;
            } else if (ex instanceof ApplicationException) {
                throw (ApplicationException)ex;
            } else if (ex instanceof EJBException) {
                Exception causedByException =
                    ((EJBException)ex).getCausedByException();
                if (causedByException == null) {
                    throw new SystemException(ex.getMessage(), ex);
                } else if (causedByException instanceof SystemException) {
                    throw (SystemException)causedByException;
                } else if (causedByException instanceof ApplicationException) {
                    throw (ApplicationException)causedByException;
                } else {
                    throw new SystemException(
                        causedByException.getMessage(),
                        causedByException);
                }
            } else {
                throw new SystemException(ex.getMessage(), ex);
            }
        }

        return result;
    }
}
