/*
 * EventTriggerException.java
 *
 * Created on 2001/12/04, 16:49
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * イベントトリガ生成時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventTriggerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>EventTriggerException</code> を構築します。
     */
    public EventTriggerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>EventTriggerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public EventTriggerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>EventTriggerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public EventTriggerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
