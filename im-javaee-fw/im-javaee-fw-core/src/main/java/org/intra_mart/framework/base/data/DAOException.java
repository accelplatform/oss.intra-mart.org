/*
 * DAOException.java
 *
 * Created on 2001/10/29, 16:55
 */

package org.intra_mart.framework.base.data;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * リソースコネクタの情報取得時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DAOException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>DAOException</code> を構築します。
     */
    public DAOException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>DAOException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public DAOException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>DAOException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public DAOException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
