/*
 * EventPropertyException.java
 *
 * Created on 2001/11/29, 15:59
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * イベントプロパティ取得時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventPropertyException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>EventPropertyException</code> を構築します。
     */
    public EventPropertyException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>EventPropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public EventPropertyException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>EventPropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public EventPropertyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
