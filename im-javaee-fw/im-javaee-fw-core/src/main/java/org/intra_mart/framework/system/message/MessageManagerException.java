/*
 * MessageManagerException.java
 *
 * Created on 2002/02/21, 19:13
 */

package org.intra_mart.framework.system.message;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * メッセージマネージャに関連する例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class MessageManagerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>MessageManagerException</code> を構築します。
     */
    public MessageManagerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>MessageManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public MessageManagerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>MessageManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public MessageManagerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
