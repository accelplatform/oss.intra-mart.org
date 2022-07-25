/*
 * GenericEJBEventListnerLocal.java
 */
package org.intra_mart.framework.base.event;

import javax.ejb.EJBException;

import org.intra_mart.framework.base.event.EJBEventListener;
import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * ローカルEJBを利用する汎用的なイベントリスナです。
 *
 * @version 1.0
 * @since 5.0
 */
public class GenericEJBEventListenerLocal extends EJBEventListener {

    /**
     * ホームインタフェースGenericEJBEventListenerAgentHome
     */
    private GenericEJBEventListenerAgentLocalHome home;

    /**
     * 処理を行うStandardEventLinstenerのクラス名
     */
    private String listenerName;

    /**
     * GenericEJBEventListnerLocalを新規に生成します。
     *
     * @param home ホームインタフェース
     * @param listenerName 実際に処理を行うStandardEventListenerのクラス名
     */
    public GenericEJBEventListenerLocal(
    		GenericEJBEventListenerAgentLocalHome home,
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
    	GenericEJBEventListenerAgentLocal agent = null;
        EventResult result = null;

        // ローカルインタフェースの取得
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
        }

        return result;
    }
}
