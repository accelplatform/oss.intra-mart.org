/*
 * DataAccessException.java
 *
 * Created on 2001/11/22, 16:26
 */

package org.intra_mart.framework.base.data;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * データアクセス時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataAccessException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>DataAccessException</code> を構築します。
     */
    public DataAccessException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>DataAccessException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public DataAccessException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>DataAccessException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public DataAccessException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
