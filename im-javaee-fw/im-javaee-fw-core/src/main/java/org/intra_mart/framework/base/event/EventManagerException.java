/*
 * EventManagerException.java
 *
 * Created on 2001/11/30, 16:16
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * イベントマネージャ設定時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventManagerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>EventManagerException</code> を構築します。
     */
    public EventManagerException() {
        super();
    }


    /**
     * 指定された詳細メッセージを持つ <code>EventManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public EventManagerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>EventManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public EventManagerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
