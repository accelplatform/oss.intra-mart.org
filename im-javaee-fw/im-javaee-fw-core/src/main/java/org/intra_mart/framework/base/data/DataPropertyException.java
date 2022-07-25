/*
 * DataPropertyException.java
 *
 * Created on 2001/11/09, 18:26
 */

package org.intra_mart.framework.base.data;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * データプロパティ取得時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataPropertyException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>DataPropertyException</code> を構築します。
     */
    public DataPropertyException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>DataPropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public DataPropertyException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>DataPropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public DataPropertyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
