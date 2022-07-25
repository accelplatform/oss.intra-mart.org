/*
 * EJBEventListener.java
 *
 * Created on 2002/02/25, 11:49
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * EJBを利用する場合のイベントリスナです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class EJBEventListener implements EventListener {

    /**
     * EJBEventListenerを新規に生成します。
     */
    public EJBEventListener() {
    }

    /**
     * このイベントリスナがトランザクションの中で実行されているかどうかの情報を設定します。
     * このクラスでは実際には何も行いません。
     *
     * @param transaction トランザクションの中で実行されている場合：true、そうでない場合：false
     * @since 4.2
     */
    public void setInTransaction(boolean transaction) {
    }

    /**
     * イベント処理を実行します。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws EventManagerException イベントマネージャの生成に失敗
     * @throws EventException イベント処理実行に失敗
     * @throws SystemException イベント処理時にシステム例外が発生
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @since 4.2
     */
    protected EventResult dispatchEvent(Event event)
        throws
            EventManagerException,
            EventException,
            SystemException,
            ApplicationException {

        return EventManager.getEventManager().dispatch(event, true);
    }
}
