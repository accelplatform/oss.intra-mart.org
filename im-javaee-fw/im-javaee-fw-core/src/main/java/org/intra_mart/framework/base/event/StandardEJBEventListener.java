/*
 * StandardEJBEventListener.java
 *
 * Created on 2002/03/24, 17:27
 */

package org.intra_mart.framework.base.event;

import java.rmi.RemoteException;
import javax.ejb.EJBException;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * StandardEJBEventListenerAgentに接続するイベントリスナです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class StandardEJBEventListener extends EJBEventListener {

    /**
     * ホームインタフェースStandardEJBEventListenerAgentHome
     */
    private StandardEJBEventListenerAgentHome home;

    /**
     * StandardEJBEventListenerを新規に生成します。
     *
     * @param home ホームインタフェース{@link StandardEJBEventListenerAgentHome}
     */
    public StandardEJBEventListener(StandardEJBEventListenerAgentHome home) {
        super();
        this.home = home;
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
    public EventResult execute(Event event) throws SystemException, ApplicationException {
        StandardEJBEventListenerAgent agent = null;
        EventResult result = null;

        // リモートインタフェースの取得
        try {
            agent = this.home.create();
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }

        // 処理の実行
        try {
            result = agent.execute(event);
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
                Exception causedByException = ((EJBException)ex).getCausedByException();
                if (causedByException == null) {
                    throw new SystemException(ex.getMessage(), ex);
                } else if (causedByException instanceof SystemException) {
                    throw (SystemException)causedByException;
                } else if (causedByException instanceof ApplicationException) {
                    throw (ApplicationException)causedByException;
                } else {
                    throw new SystemException(causedByException.getMessage(), causedByException);
                }
            } else {
                throw new SystemException(ex.getMessage(), ex);
            }
        }

        return result;
    }
}
