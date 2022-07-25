/*
 * DataAccessControllerGettingException.java
 *
 * Created on 2001/11/09, 17:47
 */

package org.intra_mart.framework.base.data;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * データコネクタ取得時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataConnectorException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>DataConnectorException</code> を構築します。
     */
    public DataConnectorException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>DataConnectorException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public DataConnectorException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>DataConnectorException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public DataConnectorException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
