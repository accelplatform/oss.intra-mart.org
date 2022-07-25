/*
 * MessagePropertyException.java
 *
 * Created on 2002/02/21, 20:46
 */

package org.intra_mart.framework.system.message;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * メッセージプロパティハンドラに関連する例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class MessagePropertyException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>MessagePropertyException</code> を構築します。
     */
    public MessagePropertyException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>MessagePropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public MessagePropertyException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>MessagePropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public MessagePropertyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
