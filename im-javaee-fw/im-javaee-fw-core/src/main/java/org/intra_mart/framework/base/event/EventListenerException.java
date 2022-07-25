/*
 * EventListenerException.java
 *
 * Created on 2001/11/30, 16:33
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * イベントリスナ生成時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventListenerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>EventListenerException</code> を構築します。
     */
    public EventListenerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>EventListenerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public EventListenerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>EventListenerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public EventListenerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
