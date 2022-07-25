/*
 * SystemException.java
 *
 * Created on 2001/11/21, 18:18
 */

package org.intra_mart.framework.system.exception;

/**
 * フレームワーク内で発生したシステム例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class SystemException extends FrameworkException {

    /**
     * 詳細メッセージを指定しないで <code>SystemException</code> を構築します。
     */
    public SystemException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>SystemException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public SystemException(String msg) {
        super(msg);
    }

    /**
     * この例外の発生原因となる例外を持つ <code>SystemException</code> を構築します。
     *
     * @param throwable この例外の発生原因となる例外
     */
    public SystemException(Throwable throwable) {
        super(throwable);
    }
    
    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>SystemException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public SystemException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
