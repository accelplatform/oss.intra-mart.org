/*
 * DataManagerException.java
 *
 * Created on 2001/10/29, 16:34
 */

package org.intra_mart.framework.base.data;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * データマネージャの設定時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataManagerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>DataManagerException</code> を構築します。
     */
    public DataManagerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>DataManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public DataManagerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>DataManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public DataManagerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
