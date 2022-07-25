/*
 * ServiceManagerException.java
 *
 * Created on 2001/12/17, 14:35
 */

package org.intra_mart.framework.base.event.container;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * イベントコンテナ設定時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventContainerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>EventContainerException</code> を構築します。
     */
    public EventContainerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>EventContainerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public EventContainerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>EventContainerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public EventContainerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
