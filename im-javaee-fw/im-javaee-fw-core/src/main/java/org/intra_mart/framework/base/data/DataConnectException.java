/*
 * DataConnectException.java
 *
 * Created on 2001/11/21, 18:37
 */

package org.intra_mart.framework.base.data;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * データ接続に関連する例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataConnectException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>DataConnectException</code> を構築します。
     */
    public DataConnectException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>DataConnectException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public DataConnectException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>DataConnectException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public DataConnectException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
