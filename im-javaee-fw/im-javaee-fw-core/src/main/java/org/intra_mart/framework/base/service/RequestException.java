/*
 * RequestException.java
 *
 * Created on 2001/12/25, 16:08
 */

package org.intra_mart.framework.base.service;

import org.intra_mart.framework.system.exception.ApplicationException;

/**
 * リクエストの内容に関連する間違いがある場合の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class RequestException extends ApplicationException {

    /**
     * 詳細メッセージを指定しないで <code>RequestException</code> を構築します。
     */
    public RequestException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>RequestException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public RequestException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>RequestException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public RequestException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
