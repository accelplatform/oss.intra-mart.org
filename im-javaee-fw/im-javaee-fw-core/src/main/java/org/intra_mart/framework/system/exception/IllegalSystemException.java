/*
 * IllegalSystemException.java
 *
 * Created on 2001/12/10, 15:06
 */

package org.intra_mart.framework.system.exception;

/**
 * 想定外の例外が発生したときの例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class IllegalSystemException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>IllegalSystemException</code> を構築します。
     */
    public IllegalSystemException() {
    }

    /**
     * 指定された詳細メッセージを持つ <code>IllegalSystemException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public IllegalSystemException(String msg) {
        super(msg);
    }

    /**
     * この例外の発生原因となる例外を持つ <code>IllegalSystemException</code> を構築します。
     *
     * @param throwable この例外の発生原因となる例外
     */
    public IllegalSystemException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>IllegalSystemException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public IllegalSystemException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
